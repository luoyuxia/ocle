package ro.ubbcluj.lci.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.swing.event.EventListenerList;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import ro.ubbcluj.lci.errors.BasicErrorMessage;
import ro.ubbcluj.lci.errors.ErrorListener;
import ro.ubbcluj.lci.errors.ErrorMessage;
import ro.ubbcluj.lci.errors.ErrorSource;
import ro.ubbcluj.lci.ocl.OclType;
import ro.ubbcluj.lci.ocl.codegen.norm.NormalForm;
import ro.ubbcluj.lci.ocl.codegen.utils.OclCodeGenUtilities;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.utils.InterruptibleTask;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.NamingContext;
import ro.ubbcluj.lci.utils.progress.ProgressEventSource;
import ro.ubbcluj.lci.utils.progress.ProgressListener;
import ro.ubbcluj.lci.utils.text.TextFormatter;
import ro.ubbcluj.lci.utils.uml.InheritanceAndAbstractionUtility;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public final class CodeGeneratorManager implements TemplatesSymbolicNames, ErrorSource, JavaReservedWords, CodeGenerationConstants, ProgressEventSource {
   private static VelocityContext sharedContext = null;
   private static boolean velocityInitialized = false;
   private ArrayList preparedElements;
   private ArrayList errors;
   private ArrayList decorators;
   private ArrayList queueDirectories;
   private EventListenerList listeners;
   private HashMap files;
   private TextFormatter formatter;
   private Model sourceModel;
   private CodeGenerationOptions currentOptions;
   private Comparator orderStrategy;
   private static CodeGeneratorManager activeInstance;
   private Hashtable accessors;
   private NamingContext topLevelPackageNames;

   private CodeGeneratorManager() {
      this.listeners = new EventListenerList();
      this.formatter = null;
      this.sourceModel = null;
      this.orderStrategy = new ClassContentsOrderStrategy();
      this.accessors = new Hashtable();
      if (!velocityInitialized) {
         initializeVelocity();
      }

   }

   public CodeGeneratorManager(Model mdl) {
      this();
      this.sourceModel = mdl;
      activeInstance = this;
   }

   public static CodeGeneratorManager getActiveInstance() {
      return activeInstance;
   }

   public InterruptibleTask getPreparationTask() {
      return new CodeGeneratorManager.PreparationTask();
   }

   public InterruptibleTask getGenerationTask() {
      return new CodeGeneratorManager.GenerationTask();
   }

   public void setFormatter(TextFormatter tf) {
      this.formatter = tf;
   }

   public static Context newContext() {
      return new VelocityContext(sharedContext);
   }

   public static Context getSharedContext() {
      return sharedContext;
   }

   public void errorOccured(ErrorMessage msg) {
      Object[] ls = this.listeners.getListeners(ErrorListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((ErrorListener)ls[i]).errorOccured(msg);
      }

      this.errors.add(msg);
   }

   public List getErrors() {
      return Collections.unmodifiableList(this.errors);
   }

   public void addProgressListener(ProgressListener l) {
      this.listeners.add(ProgressListener.class, l);
   }

   public void removeProgressListener(ProgressListener l) {
      this.listeners.remove(ProgressListener.class, l);
   }

   public void addErrorListener(ErrorListener l) {
      this.listeners.add(ErrorListener.class, l);
   }

   public void removeErrorListener(ErrorListener l) {
      this.listeners.remove(ErrorListener.class, l);
   }

   public void progressValueChanged(int newValue, Object details) {
      Object[] ls = this.listeners.getListeners(ProgressListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((ProgressListener)ls[i]).progressValueChanged(newValue, details);
      }

   }

   public void clearDecorators() {
      this.decorators = null;
   }

   public void addCodeDecorator(CodeDecorator cd) {
      if (this.decorators == null) {
         this.decorators = new ArrayList();
      }

      this.decorators.add(cd);
   }

   private void registerAccessorMethodName(String methodKind, Classifier targetCls, String fieldName, String methodName) {
      String key = methodKind + UMLUtilities.getFullyQualifiedName((ModelElement)targetCls) + fieldName;
      this.accessors.put(key, methodName);
   }

   public String getAccessorMethodName(String methodKind, Classifier targetCls, String fieldName) {
      String key = methodKind + UMLUtilities.getFullyQualifiedName((ModelElement)targetCls) + fieldName;
      String res = (String)this.accessors.get(key);
      return res;
   }

   private static void initializeVelocity() {
      InputStream is = null;
      is = (CodeGeneratorManager.class).getResourceAsStream("VelocityEngine.properties");
      if (is != null) {
         try {
            Properties tempProps = new Properties();
            tempProps.load(is);
            Velocity.init(tempProps);
            Velocity.getTemplate("MacroDefs.vm", "ISO-8859-1");
         //   System.out.println("Macros succesfully loaded");
            sharedContext = new VelocityContext();
         } catch (Exception var3) {
            System.err.println("Exception while initializing the Velocity template engine!");
            var3.printStackTrace();
         }

         try {
            is.close();
         } catch (IOException var2) {
            System.err.println("Exception while closing Velocity init property file!");
            var2.printStackTrace();
         }

         velocityInitialized = true;
      } else {
         System.err.println("File VelocityEngine.properties cannot be read");
      }

   }

   public static Template getTemplate(String templateName) {
      Template template = null;

      try {
         template = Velocity.getTemplate(templateName);
      } catch (ResourceNotFoundException var3) {
         System.err.println("Template not found in the available resources(" + templateName + ')');
         var3.printStackTrace();
      } catch (ParseErrorException var4) {
         System.err.println("Template cannot be parsed due to syntax!" + templateName + ')');
         var4.printStackTrace();
      } catch (Exception var5) {
         System.err.println("Exception in the template initialization!(" + templateName + ')');
         var5.printStackTrace();
      }

      return template;
   }

   private void decorate(PGClass cls) {
      if (this.decorators != null) {
         for(int i = 0; i < this.decorators.size(); ++i) {
            ((CodeDecorator)this.decorators.get(i)).decorate(cls);
         }
      }

   }

   private static String getFileName(List directories, String fileName) {
      StringBuffer bf = new StringBuffer();

      for(int i = 0; i < directories.size(); ++i) {
         bf.append(directories.get(i)).append(File.separator);
      }

      bf.append(fileName);
      return bf.toString();
   }

   public void setOptions(CodeGenerationOptions opts) {
      this.currentOptions = opts;
   }

   private class GenerationTask extends InterruptibleTask {
      private GenerationTask() {
      }

      public void realRun() {
         CodeGeneratorManager.this.errors = new ArrayList();
         this.generateCode();
      }

      private void generateCode() {
         int n = CodeGeneratorManager.this.preparedElements.size();
         if (n == 0) {
            CodeGeneratorManager.this.progressValueChanged(100, (Object)null);
         } else {
            int i = 0;

            while(i < n && !this.isCancelled) {
               PGClass pcls = (PGClass)CodeGeneratorManager.this.preparedElements.get(i);
               CodeGeneratorManager.this.decorate(pcls);
               pcls.orderChildren(CodeGeneratorManager.this.orderStrategy);
               StringBuffer bf = pcls.generateCode();
               if (CodeGeneratorManager.this.formatter != null) {
                  CodeGeneratorManager.this.formatter.format(bf);
               }

               String outputFile = (String)CodeGeneratorManager.this.files.get(pcls);

               try {
                  PrintWriter pw = new PrintWriter(new FileOutputStream(outputFile));
                  pw.print(bf);
                  pw.close();
               } catch (Exception var12) {
                  BasicErrorMessage msg = new BasicErrorMessage("Error writing file '" + outputFile + "': " + var12.getMessage());
                  CodeGeneratorManager.this.errorOccured(msg);
               } finally {
                  ++i;
                  CodeGeneratorManager.this.progressValueChanged(i / n, (Object)null);
               }
            }
         }

      }
   }

   private class PreparationTask extends InterruptibleTask {
      private PreparationTask() {
      }

      public void realRun() {
         CodeGeneratorManager.this.queueDirectories = new ArrayList();
         CodeGeneratorManager.this.queueDirectories.add(CodeGeneratorManager.this.currentOptions.destinationDirectory.getAbsolutePath());
         CodeGeneratorManager.this.topLevelPackageNames = new NamingContext();
         CodeGeneratorManager.this.files = new HashMap();
         CodeGeneratorManager.this.preparedElements = new ArrayList();
         CodeGeneratorManager.this.errors = new ArrayList();
         this.preparePackage(CodeGeneratorManager.this.sourceModel);
         this.resolveAssociations();
         OclCodeGenUtilities.setTupleTypePackage(CodeGeneratorManager.this.topLevelPackageNames.newName("tupleTypes"));
         this.registerTupleTypes(OclCodeGenUtilities.getCurrentNormalForm());
      }

      private void registerTupleTypes(NormalForm nf) {
         if (nf != null) {
            Set tts = nf.getTupleTypes();
            if (!tts.isEmpty()) {
               ArrayList managers = new ArrayList();
               String ttPackageName = OclCodeGenUtilities.getTupleTypesPackage();
               File ttPackageDir = new File(CodeGeneratorManager.this.currentOptions.destinationDirectory, ttPackageName);
               boolean ok = true;
               BasicErrorMessage msg;
               if (!ttPackageDir.exists()) {
                  if (!ttPackageDir.mkdir()) {
                     ok = false;
                     msg = new BasicErrorMessage("Could not create directory " + ttPackageDir.getAbsolutePath());
                     CodeGeneratorManager.this.errorOccured(msg);
                  }
               } else if (!ttPackageDir.isDirectory()) {
                  ok = false;
                  msg = new BasicErrorMessage("File " + ttPackageDir.getAbsolutePath() + " exists and is not a directory");
                  CodeGeneratorManager.this.errorOccured(msg);
               }

               if (ok) {
                  Iterator itt = tts.iterator();

                  while(itt.hasNext()) {
                     OclType tt = (OclType)itt.next();
                     String ttName = nf.tupleTypeName(tt);
                     ImportStatementManager mgr = new ImportStatementManager(ttPackageName, ttName);
                     managers.add(mgr);
                     String fullTypeName = ttPackageName + '.' + ttName;
                     Iterator it = managers.iterator();

                     while(it.hasNext()) {
                        ((ImportStatementManager)it.next()).registerClass(fullTypeName);
                     }

                     this.fromTupleType(tt, ttName, mgr, ttPackageName);
                  }
               }
            }

         }
      }

      private void fromTupleType(OclType tt, String ttName, ImportStatementManager mgr, String ttPackage) {
         PGClass ttc = new PGClass((PGElement)null);
         ttc.setName(ttName);
         ttc.addModifier("public");
         ttc.addModifier("class");
         ttc.setImportManager(mgr);
         ttc.setPackage(ttPackage);
         NamingContext localContext = new NamingContext();

         String objectTypeName;
         for(int i = 0; i < tt.getPartCount(); ++i) {
            objectTypeName = tt.getPartName(i);
            OclType pt = tt.getPartType(i);
            localContext.add(objectTypeName);
            PGField field = new PGField(ttc);
            field.setName(objectTypeName);
            field.setTypeName(OclCodeGenUtilities.getTypeName(pt, mgr, false));
            field.addModifier("public");
         }

         PGMethod eqM = new PGMethod(ttc);
         eqM.setName("equals");
         eqM.addModifier("public");
         eqM.setReturnTypeName("boolean");
         objectTypeName = OclCodeGenUtilities.getTypeName("java.lang.Object", mgr);
         String argumentName = localContext.registerNewName("arg");
         eqM.addParameter(new PGParameter(argumentName, objectTypeName));
         PGBlock methodBodyBlock = new PGBlock(eqM, false, false);
         String tempVar = localContext.registerNewName("local");
         StringBuffer bodyCode = new StringBuffer();
         Template tIf = CodeGeneratorManager.getTemplate("OCL_if");
         Context cIf = CodeGeneratorManager.newContext();
         cIf.put("TEST", "!(" + argumentName + " instanceof " + ttName + ")");
         cIf.put("BODY_IF", "return false;\n");
         bodyCode.append(CodeGenUtilities.getText(tIf, cIf));
         String temp = CodeGenUtilities.getTextForCast(ttName, argumentName);
         bodyCode.append(CodeGenUtilities.getTextForAssignment((String)null, ttName, tempVar, temp));
         String resultVar = localContext.newName("result");
         bodyCode.append(CodeGenUtilities.getTextForAssignment((String)null, "boolean", resultVar, "true"));

         for(int ix = 0; ix < tt.getPartCount(); ++ix) {
            OclType ptx = tt.getPartType(ix);
            String pn = tt.getPartName(ix);
            String test;
            if (ptx.isPrimitiveType()) {
               test = pn + " == " + tempVar + '.' + pn;
            } else {
               test = pn + " != null ? " + pn + ".equals(" + tempVar + '.' + pn + ") : " + tempVar + '.' + pn + " == null";
            }

            StringBuffer tmpBuf = new StringBuffer(resultVar);
            tmpBuf.append("&= (");
            tmpBuf.append(test).append(");\n");
            bodyCode.append(tmpBuf);
         }

         bodyCode.append(CodeGenUtilities.getTextForReturnStatement(resultVar));
         methodBodyBlock.setBody(bodyCode);
         File file = new File(new File(CodeGeneratorManager.this.currentOptions.destinationDirectory, ttPackage), ttName + ".java");
         CodeGeneratorManager.this.files.put(ttc, file.getAbsolutePath());
         CodeGeneratorManager.this.preparedElements.add(ttc);
      }

      private void prepareClassifier(Classifier classifier) {
         boolean isValid = classifier instanceof Class || classifier instanceof Interface || classifier instanceof AssociationClass || classifier instanceof Enumeration || classifier instanceof DataType && !ModelFactory.getDataTypeSystem().isPredefinedType(classifier);
         if (isValid) {
            PGClass pgclass = this.fromClassifier(classifier, (PGElement)null);
            if (CodeGeneratorManager.this.preparedElements == null) {
               CodeGeneratorManager.this.preparedElements = new ArrayList();
            }

            CodeGeneratorManager.this.preparedElements.add(pgclass);
            String fileName = CodeGeneratorManager.getFileName(CodeGeneratorManager.this.queueDirectories, classifier.getName() + ".java");
            CodeGeneratorManager.this.files.put(pgclass, fileName);
         }
      }

      private PGClass fromClassifier(Classifier cls, PGElement container) {
         PGClass ret = new PGClass(container);
         ret.setUserObject(cls);
         ImportStatementManager mgr = new ImportStatementManager(cls);
         ret.setImportManager(mgr);
         ret.setName(cls.getName());
         Namespace pack = cls.directGetNamespace();
         if (pack != null) {
            Namespace parent = pack.directGetNamespace();
            if (parent != null) {
               String packName = UMLUtilities.getFullyQualifiedName((ModelElement)pack);
               ret.setPackage(packName.replaceAll("::", "."));
            }
         }

         int visibility = cls.getNamespace().getVisibility();
         switch(visibility) {
         case 0:
            ret.addModifier("private");
         case 1:
         default:
            break;
         case 2:
            ret.addModifier("protected");
            break;
         case 3:
            ret.addModifier("public");
         }

         ret.setIsInterface(cls instanceof Interface);
         if (cls.isAbstract() && !ret.isInterface()) {
            ret.addModifier("abstract");
         }

         if (!(cls instanceof Class) && !(cls instanceof AssociationClass) && !(cls instanceof DataType)) {
            if (cls instanceof Interface) {
               ret.addModifier("interface");
            }
         } else {
            ret.addModifier("class");
         }

         if (cls instanceof Enumeration) {
            this.fromEnumeration((Enumeration)cls, ret);
            return ret;
         } else if (UMLUtilities.isEnumeration(cls)) {
            Collection attrs = new ArrayList();
            Collection features = cls.getCollectionFeatureList();
            Iterator itF = features.iterator();

            while(itF.hasNext()) {
               Object f = itF.next();
               if (f instanceof Attribute) {
                  attrs.add(f);
               }
            }

            this.fromEnumeration((Collection)attrs, ret);
            return ret;
         } else {
            java.util.Enumeration en = cls.getGeneralizationList();

            String implIntName;
            while(en.hasMoreElements()) {
               Generalization gen = (Generalization)en.nextElement();
               GeneralizableElement genParent = gen.getParent();
               implIntName = CodeGenUtilities.getTypeName((Classifier)genParent, mgr);
               ret.addExtClass(implIntName);
            }

            if (cls instanceof Class) {
               List implInts = InheritanceAndAbstractionUtility.getImplementedInterfaces((Class)cls);
               if (implInts != null) {
                  Iterator iter = implInts.iterator();

                  while(iter.hasNext()) {
                     Classifier implInterface = (Classifier)iter.next();
                     implIntName = CodeGenUtilities.getTypeName(implInterface, mgr);
                     ret.addImplInterface(implIntName);
                  }
               }
            }

            this.fromOperations(cls, ret);
            this.fromAttributes(cls, ret);
            this.fromOppositeEnds(cls, ret);
            this.fromInnerClasses(cls, ret);
            return ret;
         }
      }

      private void fromOppositeEnds(Classifier cls, PGClass target) {
         Iterator opEnds = cls.oppositeAssociationEnds().iterator();

         while(true) {
            String accessorName;
            String suffix;
            AssociationEnd opEnd;
            do {
               if (!opEnds.hasNext()) {
                  if (cls instanceof AssociationClass) {
                     Iterator connections = ((Association)cls).getCollectionConnectionList().iterator();

                     while(connections.hasNext()) {
                        AssociationEnd conn = (AssociationEnd)connections.next();
                        if (conn.isNavigable()) {
                           suffix = CodeGenUtilities.getPreferredAccessorSuffix(conn);
                           accessorName = target.getAdmissibleMethodName("set" + suffix);
                           CodeGeneratorManager.this.registerAccessorMethodName(CodeGenerationConstants.SETTER_PREFIX, cls, conn.getName(), accessorName);
                           accessorName = target.getAdmissibleMethodName(CodeGenerationConstants.GETTER_PREFIX + suffix);
                           CodeGeneratorManager.this.registerAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, cls, conn.getName(), accessorName);
                           PGField end = new PGField(target);
                           end.setTypeName(CodeGenUtilities.getTypeName(conn.getParticipant(), target.getImportManager()));
                           end.setName(conn.getName());
                           end.setUserObject(conn);
                           switch(conn.getVisibility()) {
                           case 0:
                              end.addModifier("private");
                           case 1:
                           default:
                              break;
                           case 2:
                              end.addModifier("protected");
                              break;
                           case 3:
                              end.addModifier("public");
                           }
                        }
                     }
                  }

                  return;
               }

               opEnd = (AssociationEnd)opEnds.next();
            } while(!opEnd.isNavigable());

            boolean isMultiple = UMLUtilities.isMultiple(opEnd);
            boolean isQualified = UMLUtilities.isQualified(opEnd);
            String opEndName = opEnd.getName();
            suffix = CodeGenUtilities.getPreferredAccessorSuffix(opEnd);
            accessorName = target.getAdmissibleMethodName(CodeGenerationConstants.GETTER_PREFIX + suffix);
            CodeGeneratorManager.this.registerAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, cls, opEndName, accessorName);
            boolean bOverAssociationClass = opEnd.getAssociation() instanceof AssociationClass;
            if (bOverAssociationClass) {
               String assocClassSuffix = opEnd.getAssociation().getName() + suffix;
               accessorName = target.getAdmissibleMethodName(CodeGenerationConstants.GETTER_PREFIX + assocClassSuffix);
               CodeGeneratorManager.this.registerAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, cls, opEnd.getAssociation().getName() + opEndName, accessorName);
            }

            accessorName = target.getAdmissibleMethodName(CodeGenUtilities.getPreferredSetterPrefix(opEnd) + suffix);
            CodeGeneratorManager.this.registerAccessorMethodName(CodeGenerationConstants.SETTER_PREFIX, cls, opEndName, accessorName);
            if (isMultiple || isQualified) {
               accessorName = target.getAdmissibleMethodName(CodeGenerationConstants.REMOVE_PREFIX + suffix);
               CodeGeneratorManager.this.registerAccessorMethodName(CodeGenerationConstants.REMOVE_PREFIX, cls, opEndName, accessorName);
            }

            PGField endx = new PGField(target);
            endx.setName(opEndName);
            endx.setUserObject(opEnd);
            if (bOverAssociationClass && !isMultiple) {
               endx.setTypeName(CodeGenUtilities.getTypeName((Classifier)opEnd.getAssociation(), target.getImportManager()));
            } else {
               String typeName;
               if (isMultiple) {
                  if (isQualified) {
                     typeName = CodeGenUtilities.getTypeName(CodeGenerationConstants.JAVA_MAP_TYPE, target.getImportManager());
                  } else {
                     typeName = CodeGenUtilities.getTypeName(CodeGenerationConstants.JAVA_SET_TYPE, target.getImportManager());
                  }
               } else if (isQualified) {
                  typeName = CodeGenUtilities.getTypeName(CodeGenerationConstants.JAVA_MAP_TYPE, target.getImportManager());
               } else {
                  typeName = CodeGenUtilities.getTypeName(opEnd.getParticipant(), target.getImportManager());
               }

               endx.setTypeName(typeName);
            }

            switch(opEnd.getVisibility()) {
            case 0:
               endx.addModifier("private");
            case 1:
            default:
               break;
            case 2:
               endx.addModifier("protected");
               break;
            case 3:
               endx.addModifier("public");
            }
         }
      }

      private void resolveAssociations() {
         Iterator it = CodeGeneratorManager.this.preparedElements.iterator();

         while(it.hasNext()) {
            PGClass pgc = (PGClass)it.next();
            this.resolveAssociations(pgc);
         }

      }

      private void resolveAssociations(PGClass target) {
         Object userObject = target.getUserObject();
         if (userObject instanceof Classifier) {
            Classifier thisParticipant = (Classifier)userObject;
            Iterator it = thisParticipant.oppositeAssociationEnds().iterator();

            Classifier opParticipant;
            AssociationEnd opEnd;
            String thisEndName;
            String opEndName;
            boolean bThisEndNavigabile;
            boolean bThisEndMultiple;
            while(it.hasNext()) {
               opEnd = (AssociationEnd)it.next();
               if (opEnd.isNavigable()) {
                  AssociationEnd thisEnd = UMLUtilities.getOppositeAssociationEnd(opEnd);
                  thisEndName = thisEnd.getName();
                  bThisEndNavigabile = thisEnd.isNavigable();
                  bThisEndMultiple = UMLUtilities.isMultiple(thisEnd);
                  boolean bThisEndQualified = UMLUtilities.isQualified(thisEnd);
                  opEndName = opEnd.getName();
                  boolean bOpEndMultiple = UMLUtilities.isMultiple(opEnd);
                  opParticipant = opEnd.getParticipant();
                  if (UMLUtilities.isQualified(opEnd)) {
                     this.prepareAccessorsForQualifiedEnd(thisParticipant, opEnd, bThisEndNavigabile, bThisEndMultiple, UMLUtilities.isQualified(thisEnd), thisEndName, target);
                  } else {
                     Classifier possibleAssociationClass = opEnd.getAssociation() instanceof AssociationClass ? (Classifier)opEnd.getAssociation() : null;
                     if (possibleAssociationClass != null) {
                        bThisEndMultiple = false;
                     }

                     this.prepareStandardAccsessors(thisParticipant, opParticipant, bThisEndNavigabile, bThisEndMultiple, bThisEndQualified, bOpEndMultiple, thisEndName, opEndName, possibleAssociationClass, target);
                  }
               }
            }

            if (thisParticipant instanceof AssociationClass) {
               Association a = (Association)thisParticipant;
               Iterator itConnections = a.getCollectionConnectionList().iterator();

               while(itConnections.hasNext()) {
                  opEnd = (AssociationEnd)itConnections.next();
                  if (opEnd.isNavigable()) {
                     opParticipant = opEnd.getParticipant();
                     opEndName = opEnd.getName();
                     AssociationEnd tmp = UMLUtilities.getOppositeAssociationEnd(opEnd);
                     thisEndName = tmp.getName();
                     bThisEndMultiple = UMLUtilities.isMultiple(tmp);
                     bThisEndNavigabile = tmp.isNavigable();
                     this.prepareStandardAccsessors(thisParticipant, opParticipant, bThisEndNavigabile, bThisEndMultiple, false, false, thisEndName, opEndName, (Classifier)null, target);
                  }
               }
            }

         }
      }

      private PGMethod getAccessor(String accessorName, String returnType, PGClass target) {
         PGMethod result = new PGMethod(target);
         result.setName(accessorName);
         result.setReturnTypeName(returnType);
         result.addModifier("public");
         result.addModifier("final");
         return result;
      }

      private void prepareAccessorsForQualifiedEnd(Classifier thisParticipant, AssociationEnd opEnd, boolean bThisEndNavigabile, boolean bThisEndMultiple, boolean bThisEndQualified, String thisEndName, PGClass target) {
         ImportStatementManager importManager = target.getImportManager();
         String opEndName = opEnd.getName();
         Classifier opEndParticipant = opEnd.getParticipant();
         String accessorName = CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, thisParticipant, opEndName);
         String retType = CodeGenUtilities.getTypeName(CodeGenerationConstants.JAVA_SET_TYPE, importManager);
         PGMethod accessor = this.getAccessor(accessorName, retType, target);
         NamingContext localVariables = new NamingContext();
         localVariables.add(opEndName);
         String tempVar = localVariables.registerNewName("temp");
         Context c = CodeGeneratorManager.newContext();
         c.put("END", opEndName);
         c.put("TEMP", tempVar);
         c.put("SET_TYPE", CodeGenUtilities.getTypeName(CodeGenerationConstants.CONCRETE_SET_TYPE_ASSOCIATIONS, importManager));
         Template t = CodeGeneratorManager.getTemplate("AEGetQm");
         PGBlock bodyCodeBlock = new PGBlock(accessor, false, false);
         bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
         String oppositeType = retType = CodeGenUtilities.getTypeName(opEndParticipant, importManager);
         accessor = this.getAccessor(accessorName, retType, target);
         ArrayList qualifierFormalParams = new ArrayList();
         Iterator itQualifiers = opEnd.getCollectionQualifierList().iterator();

         while(itQualifiers.hasNext()) {
            Attribute q = (Attribute)itQualifiers.next();
            String qName = localVariables.registerNewName(q.getName());
            String qType = CodeGenUtilities.getTypeName(q.getType(), importManager);
            PGParameter fp;
            qualifierFormalParams.add(fp = new PGParameter(qName, qType));
            accessor.addParameter(fp);
         }

         String keyVariabile = localVariables.registerNewName("key");
         StringBuffer keyConfigurationCode = CodeGenUtilities.getTextForKey(opEnd.getCollectionQualifierList(), keyVariabile, importManager);
         c.put("KEY", keyVariabile);
         c.put("KEY_BUILDER", keyConfigurationCode);
         c.put("OPPOSITE_TYPE", oppositeType);
         t = CodeGeneratorManager.getTemplate("AEGetQ1");
         bodyCodeBlock = new PGBlock(accessor, false, false);
         bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
         boolean bThisEndNavigabile_setters = bThisEndQualified ? false : bThisEndNavigabile;
         String templateName = "AESetQ";
         if (bThisEndNavigabile_setters) {
            templateName = templateName + (bThisEndMultiple ? 'm' : '1');
         }

         t = CodeGeneratorManager.getTemplate(templateName);
         accessorName = CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.SETTER_PREFIX, thisParticipant, opEndName);
         accessor = this.getAccessor(accessorName, (String)null, target);
         itQualifiers = qualifierFormalParams.iterator();

         while(itQualifiers.hasNext()) {
            accessor.addParameter((PGParameter)itQualifiers.next());
         }

         String argumentName = localVariables.registerNewName("arg");
         c.put("ARG", argumentName);
         accessor.addParameter(new PGParameter(argumentName, oppositeType));
         c.put("MAP_TYPE", CodeGenUtilities.getTypeName(CodeGenerationConstants.CONCRETE_MAP_TYPE_ASSOCIATIONS, importManager));
         c.put("OPPOSITE_SETTER", CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.SETTER_PREFIX, opEnd.getParticipant(), thisEndName));
         c.put("OPPOSITE_REMOVE", CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.REMOVE_PREFIX, opEnd.getParticipant(), thisEndName));
         bodyCodeBlock = new PGBlock(accessor, false, false);
         bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
         templateName = "AERemoveQQ";
         accessorName = CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.REMOVE_PREFIX, thisParticipant, opEndName);
         accessor = this.getAccessor(accessorName, (String)null, target);
         if (bThisEndNavigabile) {
            templateName = templateName + (bThisEndMultiple ? 'm' : '1');
         }

         t = CodeGeneratorManager.getTemplate(templateName);
         itQualifiers = qualifierFormalParams.iterator();

         while(itQualifiers.hasNext()) {
            accessor.addParameter((PGParameter)itQualifiers.next());
         }

         bodyCodeBlock = new PGBlock(accessor, false, false);
         bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
         accessor = this.getAccessor(accessorName, (String)null, target);
         accessor.addParameter(new PGParameter(argumentName, oppositeType));
         templateName = "AERemoveQO";
         if (bThisEndNavigabile) {
            templateName = templateName + (bThisEndMultiple ? 'm' : '1');
         }

         t = CodeGeneratorManager.getTemplate(templateName);
         bodyCodeBlock = new PGBlock(accessor, false, false);
         bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
      }

      private void prepareStandardAccsessors(Classifier thisEndParticipant, Classifier opEndParticipant, boolean bThisEndNavigable, boolean bThisEndMultiple, boolean bThisEndQualified, boolean bOpEndMultiple, String thisEndName, String opEndName, Classifier assocClass, PGClass target) {
         ImportStatementManager importManager = target.getImportManager();
         String templateName = "AE";
         String simpleGetter = CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, thisEndParticipant, opEndName);
         String directGetter = null;
         String realTemplateName = templateName + "Get";
         String retType;
         if (bOpEndMultiple) {
            retType = CodeGenUtilities.getTypeName(CodeGenerationConstants.JAVA_SET_TYPE, importManager);
            realTemplateName = realTemplateName + 'm';
         } else {
            realTemplateName = realTemplateName + '1';
            retType = CodeGenUtilities.getTypeName(opEndParticipant, importManager);
         }

         PGMethod getter;
         Template t;
         Context c;
         PGBlock bodyCodeBlock;
         NamingContext localVariables;
         if (assocClass != null) {
            directGetter = CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, thisEndParticipant, assocClass.getName() + opEndName);
            if (!bOpEndMultiple) {
               retType = CodeGenUtilities.getTypeName(assocClass, importManager);
            }

            getter = this.getAccessor(directGetter, retType, target);
            bodyCodeBlock = new PGBlock(getter, false, false);
            t = CodeGeneratorManager.getTemplate(realTemplateName);
            c = CodeGeneratorManager.newContext();
            c.put("END", opEndName);
            bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
            realTemplateName = templateName + "DirectGet" + (bOpEndMultiple ? "m" : "1");
            t = CodeGeneratorManager.getTemplate(realTemplateName);
            c.put("END", opEndName);
            c.put("FORWARD_GET", CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, assocClass, opEndName));
            if (!bOpEndMultiple) {
               retType = CodeGenUtilities.getTypeName(opEndParticipant, importManager);
            } else {
               localVariables = new NamingContext();
               localVariables.add(opEndName);
               c.put("TEMP", localVariables.newName("temp"));
               c.put("ITERATOR_VAR", localVariables.newName("it"));
               c.put("ITERATOR_TYPE", CodeGenUtilities.getTypeName(CodeGenerationConstants.JAVA_ITERATOR_TYPE, importManager));
               c.put("SET_TYPE", CodeGenUtilities.getTypeName(CodeGenerationConstants.CONCRETE_SET_TYPE_ASSOCIATIONS, importManager));
               c.put("OPPOSITE_TYPE", CodeGenUtilities.getTypeName(assocClass, importManager));
            }

            getter = this.getAccessor(simpleGetter, retType, target);
            bodyCodeBlock = new PGBlock(getter, false, false);
            bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
         } else {
            getter = this.getAccessor(simpleGetter, retType, target);
            bodyCodeBlock = new PGBlock(getter, false, false);
            t = CodeGeneratorManager.getTemplate(realTemplateName);
            c = CodeGeneratorManager.newContext();
            c.put("END", opEndName);
            bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
         }

         PGMethod setter = this.getAccessor(CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.SETTER_PREFIX, thisEndParticipant, opEndName), (String)null, target);
         realTemplateName = templateName + "Set";
         realTemplateName = realTemplateName + (bOpEndMultiple ? "m" : "1");
         if (bThisEndNavigable && !bThisEndQualified) {
            realTemplateName = realTemplateName + (bThisEndMultiple ? "m" : "1");
         }

         localVariables = new NamingContext();
         localVariables.add(opEndName);
         String formalParameterName = localVariables.registerNewName("arg");
         Classifier oppositeParticipant = assocClass != null ? assocClass : opEndParticipant;
         String formalParameterType = CodeGenUtilities.getTypeName(oppositeParticipant, target.getImportManager());
         setter.addParameter(new PGParameter(formalParameterName, formalParameterType));
         bodyCodeBlock = new PGBlock(setter, false, false);
         t = CodeGeneratorManager.getTemplate(realTemplateName);
         c.put("ARG", formalParameterName);
         c.put("OPPOSITE_SETTER", CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.SETTER_PREFIX, oppositeParticipant, thisEndName));
         if (bOpEndMultiple) {
            c.put("SET_TYPE", CodeGenUtilities.getTypeName(CodeGenerationConstants.CONCRETE_SET_TYPE_ASSOCIATIONS, importManager));
         }

         if (bThisEndNavigable) {
            c.put("OPPOSITE_REMOVE", CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.REMOVE_PREFIX, oppositeParticipant, thisEndName));
            c.put("OPPOSITE_TYPE", CodeGenUtilities.getTypeName(oppositeParticipant, importManager));
            c.put("TEMP", localVariables.newName("temp"));
         }

         bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
         if (bOpEndMultiple) {
            setter = this.getAccessor(CodeGeneratorManager.this.getAccessorMethodName(CodeGenerationConstants.REMOVE_PREFIX, thisEndParticipant, opEndName), (String)null, target);
            setter.addParameter(new PGParameter(formalParameterName, formalParameterType));
            realTemplateName = templateName + "Remove";
            if (bThisEndNavigable) {
               if (bThisEndQualified) {
                  realTemplateName = realTemplateName + 'm';
               } else {
                  realTemplateName = realTemplateName + (bThisEndMultiple ? 'm' : '1');
               }
            }

            t = CodeGeneratorManager.getTemplate(realTemplateName);
            bodyCodeBlock = new PGBlock(setter, false, false);
            bodyCodeBlock.setBody(CodeGenUtilities.getText(t, c));
         }

      }

      private void fromAttributes(Classifier cls, PGClass target) {
         Collection features = cls.getCollectionFeatureList();
         Iterator it = features.iterator();

         while(it.hasNext()) {
            Object f = it.next();
            if (f instanceof Attribute) {
               this.fromAttribute((Attribute)f, target);
            }
         }

      }

      private void fromAttribute(Attribute attr, PGClass target) {
         String name = attr.getName();
         PGField field = new PGField(target);
         field.setUserObject(attr);
         field.setName(name);
         int vis = attr.getVisibility();
         switch(vis) {
         case 0:
            field.addModifier("private");
         case 1:
         default:
            break;
         case 2:
            field.addModifier("protected");
            break;
         case 3:
            field.addModifier("public");
         }

         String fieldTypeName = CodeGenUtilities.getTypeName(attr.getType(), target.getImportManager());
         field.setTypeName(fieldTypeName);
      }

      private void fromEnumeration(Enumeration enum, PGClass result) {
         this.fromEnumeration((Collection)enum.getCollectionLiteralList(), result);
      }

      private void fromEnumeration(Collection literals, PGClass target) {
         String name = target.getName();
         NamingContext localFeatures = new NamingContext();
         Iterator itItems = literals.iterator();

         while(itItems.hasNext()) {
            ModelElement item = (ModelElement)itItems.next();
            PGField field = new PGField(target);
            field.addModifier("public");
            field.addModifier("static");
            field.addModifier("final");
            field.setName(localFeatures.add(item.getName()));
            field.setTypeName(name);
         }

         PGConstructor pc = new PGConstructor(target);
         pc.setName(target.getName());
         pc.addModifier("private");
         PGBlock bodyBlock = new PGBlock(pc, false, false);
         bodyBlock.setBody(new StringBuffer());
         PGBlock initializerBlock = new PGBlock(target, true, false);
         initializerBlock.setStartText("static");
         StringBuffer bfBody = new StringBuffer();
         itItems = literals.iterator();

         while(itItems.hasNext()) {
            StringBuffer bfConstructorCall = CodeGenUtilities.getTextForMethodCall((String)null, name, (List)null);
            bfBody.append(CodeGenUtilities.getTextForAssignment((String)null, (String)null, ((ModelElement)itItems.next()).getName(), "new " + bfConstructorCall));
         }

         initializerBlock.setBody(bfBody);
      }

      private void fromInnerClasses(Classifier cls, PGElement target) {
         Collection owned = cls.directGetCollectionOwnedElementList();
         Iterator it = owned.iterator();

         while(it.hasNext()) {
            Object next = it.next();
            if (next instanceof Classifier) {
               this.fromClassifier((Classifier)next, target);
            }
         }

      }

      private void fromOperations(Classifier cls, PGClass target) {
         Collection features = cls.getCollectionFeatureList();
         Iterator it = features.iterator();
         boolean bHasConstructors = false;

         while(it.hasNext()) {
            Object next = it.next();
            if (next instanceof Operation) {
               Operation op = (Operation)next;
               if (op.getName().equals(cls.getName())) {
                  bHasConstructors = true;
                  this.fromConstructor(op, target);
               } else {
                  this.fromOperation(op, target);
               }
            }
         }

         if (!bHasConstructors && !target.isInterface()) {
            this.prepareDefaultConstructor(cls, target);
         }

      }

      private void prepareDefaultConstructor(Classifier cls, PGClass target) {
         PGConstructor pgc = new PGConstructor(target);
         pgc.setName(cls.getName());
         pgc.addModifier("public");
      }

      private void fromOperation(Operation op, PGClass container) {
         PGMethod meth = new PGMethod(container);
         String opName = op.getName();
         meth.setUserObject(op);
         meth.setName(opName);
         container.registerOperation(opName);
         switch(op.getVisibility()) {
         case 0:
            meth.addModifier("private");
         case 1:
         default:
            break;
         case 2:
            meth.addModifier("protected");
            break;
         case 3:
            meth.addModifier("public");
         }

         Collection params = op.getCollectionParameterList();
         Iterator it = params.iterator();
         String returnType = null;

         while(it.hasNext()) {
            Parameter p = (Parameter)it.next();
            String paramTypeName = CodeGenUtilities.getTypeName(p.getType(), container.getImportManager());
            if (p.getKind() == 3) {
               returnType = paramTypeName;
               meth.setReturnTypeName(paramTypeName);
            } else {
               PGParameter pgp = new PGParameter(p.getName(), paramTypeName);
               meth.addParameter(pgp);
            }
         }

         if (returnType == null) {
            meth.setReturnTypeName(PGMethod.DEFAULT_RETURN_TYPE);
         }

         if (op.isAbstract() && !container.isInterface()) {
            meth.addModifier("abstract");
         }

         if (op.getOwnerScope() == 0) {
            meth.addModifier("static");
         }

      }

      public void fromConstructor(Operation cons, PGClass container) {
         PGConstructor pgc = new PGConstructor(container);
         pgc.setName(container.getName());
         Collection params = cons.getCollectionParameterList();
         Iterator it = params.iterator();

         while(it.hasNext()) {
            Parameter p = (Parameter)it.next();
            if (p.getKind() != 3) {
               String typeName = CodeGenUtilities.getTypeName(p.getType(), container.getImportManager());
               PGParameter pgp = new PGParameter(p.getName(), typeName);
               pgc.addParameter(pgp);
            }
         }

         switch(cons.getVisibility()) {
         case 0:
            pgc.addModifier("private");
         case 1:
         default:
            break;
         case 2:
            pgc.addModifier("protected");
            break;
         case 3:
            pgc.addModifier("public");
         }

      }

      private boolean preparePackage(Package pack) {
         if (this.isCancelled) {
            return false;
         } else {
            Namespace directNamespace = pack.directGetNamespace();
            boolean bIsRootPackage = directNamespace == null;
            File dir;
            if (!bIsRootPackage) {
               dir = new File(CodeGeneratorManager.getFileName(CodeGeneratorManager.this.queueDirectories, pack.getName()));
               CodeGeneratorManager.this.queueDirectories.add(pack.getName());
               if (directNamespace.directGetNamespace() == null) {
                  CodeGeneratorManager.this.topLevelPackageNames.add(pack.getName());
               }
            } else {
               dir = new File(CodeGeneratorManager.this.queueDirectories.get(0).toString());
            }

            BasicErrorMessage msg;
            if (!dir.exists()) {
               if (!dir.mkdir()) {
                  msg = new BasicErrorMessage("Could not create directory " + dir.getAbsolutePath());
                  CodeGeneratorManager.this.errorOccured(msg);
                  return false;
               }
            } else if (!dir.isDirectory()) {
               msg = new BasicErrorMessage(dir.getAbsolutePath() + " is not a directory");
               CodeGeneratorManager.this.errorOccured(msg);
               return false;
            }

            java.util.Enumeration en = pack.directGetOwnedElementList();

            while(en.hasMoreElements()) {
               ModelElement me = (ModelElement)en.nextElement();
               if (me instanceof Classifier) {
                  this.prepareClassifier((Classifier)me);
               } else if (me instanceof Package) {
                  boolean b = this.preparePackage((Package)me);
                  if (!b) {
                     return false;
                  }
               }
            }

            if (!bIsRootPackage) {
               CodeGeneratorManager.this.queueDirectories.remove(CodeGeneratorManager.this.queueDirectories.size() - 1);
            }

            return true;
         }
      }
   }
}
