package ro.ubbcluj.lci.ocl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import ro.ubbcluj.lci.ocl.datatypes.*;
import ro.ubbcluj.lci.ocl.eval.*;
import ro.ubbcluj.lci.ocl.gui.IntArray;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CompositeState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateVertex;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.ParameterImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class OclClassInfo {
   static final int FULL_PERFECT = 1;
   public static final int NORET_CONFORM = 2;
   private static final int FT_ATTRIBUTE = 1;
   private static final int FT_METHOD = 2;
   private static final int FT_NAVIGATION = 3;
   static final int CT_METAMODEL = 1;
   static final int CT_OCL = 2;
   static final int CT_USER = 3;
   private static final String oclpackage = "ro.ubbcluj.lci.ocl.datatypes.";
   private static final String umlpackage = "ro.ubbcluj.lci.uml.";
   public Classifier classifier;
   String fullname;
   private String fullpath;
   public Vector attrs;
   private Vector ops;
   private IntArray opslevels;
   private Set assocs;
   private Set processedClasses;
   ArrayList states;
   boolean isCollection;
   int classType;
   Vector letTable;
   Vector defTable;
   public OclUmlApi umlapi;
   private Class javaclass;
   private Class javaclassimpl;
   public String name;
   HashMap invNames;
   static SFindInfo FindInfo = new SFindInfo();
   static boolean foundHasResolution = false;
   private static boolean isSpanishAPI;

   public static void cleanup() {
      FindInfo = new SFindInfo();
   }

   OclClassInfo(OclUmlApi container, Classifier classfier, Vector currentPath, int typeOfClass) {
      this.umlapi = container;
      this.classifier = classfier;
      if (container != null && classfier != null) {
         this.attrs = new Vector();
         this.allAttributes();
         this.ops = new Vector();
         this.opslevels = new IntArray();
         this.processedClasses = new HashSet();
         this.allOperations(this.classifier, 0);
         this.assocs = new HashSet();
         this.allAssociations();
         this.classType = typeOfClass;
         StringBuffer packagePath = new StringBuffer();
         StringBuffer innerClassPath = new StringBuffer();
         Iterator iter = currentPath.iterator();

         while(iter.hasNext()) {
            ModelElement me = (ModelElement)iter.next();
            if (me instanceof Package) {
               packagePath.append(me.getName()).append("::");
            }

            if (me instanceof ro.ubbcluj.lci.uml.foundation.core.Class) {
               innerClassPath.append(me.getName()).append('.');
            }
         }

         if (packagePath.toString().endsWith("::")) {
            packagePath.setLength(packagePath.length() - 2);
         }

         for(int i = 0; i < packagePath.length(); ++i) {
            if (packagePath.charAt(i) == ' ') {
               packagePath.setCharAt(i, '_');
            }
         }

         this.fullpath = packagePath.toString();
         packagePath.append("::");
         packagePath.append(innerClassPath.toString() + this.classifier.getName());
         this.fullname = packagePath.toString();
         this.name = innerClassPath.toString() + this.classifier.getName();
         this.isCollection = this.classType == 2 && (this.classifier.getName().equals("Collection") || this.classifier.getName().equals("Bag") || this.classifier.getName().equals("Set") || this.classifier.getName().equals("OrderedSet") || this.classifier.getName().equals("Sequence"));
         if (OclUmlApi.debug) {
            System.out.println("     " + this.name + " A:" + this.attrs.size() + "/O:" + this.ops.size() + "/L:" + this.assocs.size() + "/ASO:" + this.classifier.associations().size());
         }

      }
   }

   OclExpression buildOclObjectDiagram(OclNode node, OclExpression exp, String qualifier) {
      if (FindInfo.foundType == 1) {
         return new OclObjectDiagram(node, exp, FindInfo.theAttribute, FindInfo.attrType.type);
      } else if (FindInfo.foundType == 3) {
         if (FindInfo.isCollection) {
            OclCollection col = null;
            if (FindInfo.collectionType == this.umlapi.SET) {
               col = new OclSet();
            }

            if (FindInfo.collectionType == this.umlapi.BAG) {
               col = new OclBag();
            }

            if (FindInfo.collectionType == this.umlapi.SEQUENCE) {
               col = new OclSequence();
            }

            return new OclObjectDiagram(node, exp, FindInfo.theAssociationEnd, (OclCollection)col);
         } else {
            return qualifier == null ? new OclObjectDiagram(node, exp, FindInfo.theAssociationEnd) : new OclObjectDiagram(node, exp, FindInfo.theAssociationEnd, qualifier);
         }
      } else {
         return new OclConstant("methods cannot be evaluated at user model level", node);
      }
   }

   public Class getJavaClass() throws ExceptionAny {
      if (this.javaclass == null) {
         this.javaclass = this.getIt();
      }

      return this.javaclass;
   }

   public Class getJavaClassImpl() throws ExceptionAny {
      if (this.javaclass == null) {
         this.javaclass = this.getIt();
      }

      if (this.javaclassimpl == null) {
         return Undefined.class;
      } else {
         return this.javaclassimpl;
      }
   }

   private Class getIt() throws ExceptionAny {
      if (!this.umlapi.isMetamodel && this.classType == 3) {
         return null;
      } else {
         if (this.name != null) {
            if (this.name.equals("BooleanExpr")) {
               return OclExpression.class;
            }

            if (this.name.equals("AnyExpr")) {
               return OclExpression.class;
            }

            if (this.name.equals("NumericExpr")) {
               return OclExpression.class;
            }

            if (this.name.equals("StringExpr")) {
               return OclExpression.class;
            }

            if (this.name.endsWith("Kind")) {
               return OclExpression.class;
            }
         }

         if (this == this.umlapi.OCLITERATOR) {
            return OclConstant.class;
         } else if (this == this.umlapi.OCLTYPE) {
            return OclType.class;
         } else if (this == this.umlapi.OCLTEMPLATE) {
            return Object.class;
         } else if (this == this.umlapi.OCLEVALUATION) {
            return OclAccumulator.class;
         } else if (this == this.umlapi.OCLSTATE) {
            return OclString.class;
         } else if (this == this.umlapi.ENUMERATION) {
            return OclInteger.class;
         } else {
            String jname;
            if (this != this.umlapi.OCLANY && this != this.umlapi.UNDEFINED) {
               if (this != this.umlapi.INTEGER && this != this.umlapi.REAL && this != this.umlapi.STRING && this != this.umlapi.BOOLEAN && this != this.umlapi.COLLECTION && this != this.umlapi.SET && this != this.umlapi.BAG && this != this.umlapi.SEQUENCE && this != this.umlapi.ORDEREDSET) {
                  StringBuffer s = new StringBuffer("ro.ubbcluj.lci.uml.");
                  if (this.fullpath.length() > 0) {
                     s.append(this.fullpath.substring(0, 1).toLowerCase());

                     for(int i = 1; i < this.fullpath.length(); ++i) {
                        char ch = this.fullpath.charAt(i);
                        if (ch == ':') {
                           s.append('.');
                           ++i;
                           ++i;
                           s.append(this.fullpath.substring(i, i + 1).toLowerCase());
                        } else if (ch != ' ' && ch != '_') {
                           s.append(ch);
                        }
                     }

                     s.append('.');
                  }

                  s.append(this.name);
                  jname = s.toString();
               } else {
                  jname = "ro.ubbcluj.lci.ocl.datatypes.Ocl" + this.name;
               }
            } else {
               jname = "ro.ubbcluj.lci.ocl.datatypes." + this.name;
            }

            try {
               this.javaclassimpl = Class.forName(jname + "Impl");
            } catch (ClassNotFoundException var6) {
            }

            try {
               Class result = Class.forName(jname);
               if (!result.isInterface()) {
                  this.javaclassimpl = result;
               }

               return result;
            } catch (ClassNotFoundException var5) {
               throw new ExceptionAny("[fatal] Java class for " + this.fullname + " not found (" + jname + ")");
            }
         }
      }
   }

   public Method getJavaMethod() throws ExceptionAny {
      if (FindInfo.foundType <= 3 && FindInfo.name != null) {
         String name = FindInfo.name;
         if (name.equals("closure")) {
            return OclCollection.getClosureMethod();
         } else if (FindInfo.owner != this.umlapi.OCLANY.classifier && FindInfo.owner != this.umlapi.ENUMERATION.classifier) {
            FindInfo.owner = null;
            switch(FindInfo.foundType) {
            case 1:
               if (!FindInfo.isBoolean) {
                  name = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                  if (FindInfo.isCollection) {
                     name = name + "List";
                  }
               }
            case 2:
            default:
               break;
            case 3:
               if (FindInfo.theAssociationEnd.getClass() == (OclUmlApi.MyAssociationEndImpl.class)) {
                  name = ((OclUmlApi.MyAssociationEndImpl)FindInfo.theAssociationEnd).methodName;
                  if (FindInfo.isCollection) {
                     name = name + "List";
                  }
               } else {
                  boolean isDirect = FindInfo.foundType == 3 && FindInfo.theAssociationEnd.getAssociation() instanceof AssociationClass;
                  String prefix = isDirect ? "directGet" : "get";
                  name = prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
                  if (FindInfo.isCollection) {
                     name = name + "List";
                  }
               }
            }

            if (name.equals("directGetImportingPackageList")) {
               name = "directGetPackageList";
            }

            if (name.equals("getImportingPackageList")) {
               name = "getPackageList";
            }

            if (isSpanishAPI && name.equals("getPackageList")) {
               name = "getElementImportList";
            }

            Class[] params;
            if (FindInfo.params == null) {
               params = new Class[0];
            } else {
               Vector vparams = new Vector();
               if (FindInfo.params != null) {
                  while(FindInfo.params.hasMoreElements()) {
                     vparams.add(FindInfo.params.nextElement());
                  }

                  if (vparams.size() == 0) {
                     throw new ExceptionAny("[internal] method with no return type in find Java method");
                  }

                  Object first = vparams.get(0);
                  if (first instanceof Classifier) {
                     vparams.setSize(vparams.size() - 1);
                  } else {
                     int p = 0;

                     label178:
                     while(true) {
                        while(true) {
                           if (p >= vparams.size()) {
                              break label178;
                           }

                           ParameterImpl pp = (ParameterImpl)vparams.get(p);
                           if (pp != null && pp.getKind() != 3) {
                              ++p;
                           } else {
                              vparams.remove(p);
                           }
                        }
                     }
                  }
               }

               params = new Class[vparams.size()];
               int i = 0;

               for(Iterator it = vparams.iterator(); it.hasNext(); ++i) {
                  Object obj = it.next();
                  Classifier param;
                  if (obj instanceof Classifier) {
                     param = (Classifier)obj;
                  } else {
                     param = ((ParameterImpl)obj).getType();
                  }

                  OclType ocltype = this.umlapi.getOclTypeByName(this.fullpath, param.getName());
                  if (ocltype == OclType.PARAMLIST) {
                     params[i] = Object.class;
                  } else {
                     params[i] = ocltype.type.getJavaClass();
                  }
               }
            }

            boolean isDirect = name.startsWith("directGet");
            Class javaclass;
            if (!isDirect) {
               javaclass = this.getJavaClass();
            } else {
               javaclass = OclDirect.class;
            }

            Method[] meth = javaclass.getMethods();
            Method found = null;
            Class owner = null;
            boolean perfect = false;

            for(int i = 0; i < meth.length; ++i) {
               Class[] fparams = meth[i].getParameterTypes();
               if (name.equals(meth[i].getName()) && params.length == fparams.length) {
                  boolean match = true;
                  boolean thisperfect = true;

                  for(int j = 0; j < fparams.length; ++j) {
                     if (params[j] != fparams[j]) {
                        thisperfect = false;
                     }

                     if (!fparams[j].isAssignableFrom(params[j])) {
                        match = false;
                     }
                  }

                  if (match) {
                     if (found != null && (!thisperfect || perfect)) {
                        if (thisperfect == perfect && owner.isAssignableFrom(meth[i].getDeclaringClass())) {
                           found = meth[i];
                           owner = found.getDeclaringClass();
                           perfect = thisperfect;
                        }
                     } else {
                        found = meth[i];
                        owner = found.getDeclaringClass();
                        perfect = thisperfect;
                     }
                  }
               }
            }

            if (found != null) {
               return found;
            } else {
               throw new ExceptionAny("[fatal] java method " + name + " not found in " + this.fullname + " (" + javaclass.getName() + ")");
            }
         } else {
            Enumeration en = FindInfo.params;
            int k = 0;

            while(en.hasMoreElements()) {
               ++k;
               en.nextElement();
            }

            FindInfo.owner = null;
            if ((FindInfo.name.equals("dump") || FindInfo.name.equals("dumpi")) && k == 2) {
               k = 3;
            }

            return OclAny.getMethod(FindInfo.name, k);
         }
      } else {
         throw new ExceptionAny("[fatal] last find feature info incomplete");
      }
   }

   private void allAttributes() {
      this.processedClasses = new HashSet();
      ArrayList inProgress = new ArrayList();
      inProgress.add(this.classifier);

      while(true) {
         Classifier currentClassifier;
         do {
            Object current;
            do {
               if (inProgress.isEmpty()) {
                  return;
               }

               current = inProgress.remove(inProgress.size() - 1);
            } while(!(current instanceof Classifier));

            currentClassifier = (Classifier)current;
         } while(!this.processedClasses.add(currentClassifier));

         Enumeration features = currentClassifier.getFeatureList();

         while(features.hasMoreElements()) {
            Object candidate = features.nextElement();
            if (candidate instanceof Attribute) {
               this.attrs.add(candidate);
            }
         }

         inProgress.addAll(extendedGetParents(currentClassifier));
      }
   }

   private void allOperations(Classifier c, int level) {
      Enumeration feat = c.getFeatureList();

      while(feat.hasMoreElements()) {
         Object obj = feat.nextElement();
         if (obj instanceof Operation) {
            this.ops.add(obj);
            this.opslevels.add(level);
         }
      }

      Iterator itParents = extendedGetParents(c).iterator();

      while(itParents.hasNext()) {
         Object candidate = itParents.next();
         if (candidate instanceof Classifier && this.processedClasses.add(candidate)) {
            this.allOperations((Classifier)candidate, level + 1);
         }
      }

   }

   private void allAssociations() {
      this.processedClasses = new HashSet();
      ArrayList inProgress = new ArrayList();
      inProgress.add(this.classifier);

      while(!inProgress.isEmpty()) {
         Object current = inProgress.remove(inProgress.size() - 1);
         if (current instanceof Classifier) {
            Classifier currentClass = (Classifier)current;
            if (this.processedClasses.add(currentClass)) {
               this.assocs.addAll(currentClass.oppositeAssociationEnds());
               inProgress.addAll(extendedGetParents(currentClass));
            }
         }
      }

   }

   private static List extendedGetParents(Classifier source) {
      Set directParents = source.parent();
      ArrayList result;
      if (directParents.isEmpty()) {
         result = new ArrayList();
         if (!UMLUtilities.isEnumeration(source) && !(source instanceof ro.ubbcluj.lci.uml.foundation.core.Enumeration)) {
            if (source != OclLoader.getOclAnyRef()) {
               result.add(OclLoader.getOclAnyRef());
            }
         } else {
            result.add(OclLoader.getEnumRef());
         }
      } else {
         result = new ArrayList(directParents);
      }

      return result;
   }

   public boolean conforms(OclClassInfo a) {
      return this.conforms(this.classifier, a.classifier);
   }

   private boolean conforms(Classifier a, Classifier b) {
      a = this.umlapi.replaceRoseDatatype(a);
      if (b == null) {
         System.out.println("");
      }

      b = this.umlapi.replaceRoseDatatype(b);
      if (a == b) {
         return true;
      } else if (b != this.umlapi.OCLANY.classifier && b != this.umlapi.ANYEXPR.classifier) {
         Iterator iter = a.parent().iterator();

         Object obj;
         do {
            if (!iter.hasNext()) {
               return false;
            }

            obj = iter.next();
         } while(!(obj instanceof Classifier) || obj == a || !this.conforms((Classifier)obj, b));

         return true;
      } else {
         return true;
      }
   }

   OclClassInfo commonParent(OclClassInfo b) {
      HashSet thisParents = new HashSet();
      HashSet commonParents = new HashSet();
      this.getAllParents(this.classifier, thisParents);
      this.getCommonParents(b.classifier, thisParents, commonParents);
      if (commonParents.size() != 1) {
         return this.umlapi.OCLANY;
      } else {
         try {
            return this.umlapi.getClassInfoByClassifier((Classifier)commonParents.iterator().next());
         } catch (ExceptionAny var5) {
            return null;
         }
      }
   }

   private void getAllParents(Classifier a, HashSet hs) {
      hs.add(a);
      Collection parents = a.parent();
      if (parents.size() == 0) {
         hs.add(this.umlapi.OCLANY.classifier);
      }

      Iterator it = parents.iterator();

      while(it.hasNext()) {
         Classifier cls = (Classifier)it.next();
         this.getAllParents(cls, hs);
      }

   }

   private void getCommonParents(Classifier a, HashSet bParents, HashSet commonParents) {
      if (bParents.contains(a)) {
         commonParents.add(a);
      } else {
         Collection parents = a.parent();
         Iterator it = parents.iterator();

         while(it.hasNext()) {
            Classifier cls = (Classifier)it.next();
            this.getCommonParents(cls, bParents, commonParents);
         }

      }
   }

   private void getAllDescendants(Classifier a, HashSet hs) {
      hs.add(a);
      Enumeration en = a.getSpecializationList();

      while(en.hasMoreElements()) {
         Generalization gene = (Generalization)en.nextElement();
         this.getAllDescendants((Classifier)gene.getChild(), hs);
      }

   }

   private boolean haveCommonDescendant(Classifier a, Classifier b) {
      if (a != this.umlapi.OCLANY && b != this.umlapi.OCLANY) {
         HashSet ha = new HashSet();
         HashSet hb = new HashSet();
         this.getAllDescendants(a, ha);
         this.getAllDescendants(b, hb);
         Iterator it = ha.iterator();

         Object elem;
         do {
            if (!it.hasNext()) {
               return false;
            }

            elem = it.next();
         } while(!hb.contains(elem));

         return true;
      } else {
         return true;
      }
   }

   public Operation findOperation(String name) {
      Iterator it = this.ops.iterator();

      Operation op;
      do {
         if (!it.hasNext()) {
            return null;
         }

         op = (Operation)it.next();
      } while(!op.getName().equals(name));

      return op;
   }

   private AssociationEnd findAssociationEnd(String name) {
      Iterator it = this.assocs.iterator();

      AssociationEnd as;
      do {
         if (!it.hasNext()) {
            return null;
         }

         as = (AssociationEnd)it.next();
      } while(!as.getName().equals(name));

      return as;
   }

   private OclType checkOclAnyFeature(String name, Vector params) throws ExceptionAny {
      if (!name.equals("oclAsType")) {
         if (!name.equals("oclType") && !name.equals("evaluationType")) {
            return null;
         } else if (params != null && params.size() != 0) {
            throw new ExceptionAny(name + " takes no parameters");
         } else {
            FindInfo.name = "oclType";
            FindInfo.theAssociationEnd = this.umlapi.OCLTYPE.findAssociationEnd(name);
            Vector v = new Vector();
            v.add((Object)null);
            FindInfo.params = v.elements();
            FindInfo.foundType = 3;
            FindInfo.isCollection = false;
            FindInfo.isBoolean = false;
            FindInfo.owner = this.umlapi.OCLANY.classifier;
            return new OclType(this.umlapi.OCLTYPE, this);
         }
      } else {
         OclType param = params != null && params.size() == 1 ? (OclType)params.firstElement() : null;
         if (param != null && param.type == this.umlapi.OCLTYPE) {
            if (!this.haveCommonDescendant(this.classifier, param.element.classifier)) {
               throw new ExceptionAny("cannot cast from " + this.fullname + " to " + param.element.fullname);
            } else {
               FindInfo.name = name;
               FindInfo.theOperation = this.umlapi.OCLTYPE.findOperation(name);
               Vector v = new Vector();
               v.add(param.type.classifier);
               v.add(param.type.classifier);
               FindInfo.params = v.elements();
               FindInfo.foundType = 2;
               FindInfo.isCollection = false;
               FindInfo.isBoolean = false;
               FindInfo.owner = this.umlapi.OCLANY.classifier;
               OclType result = new OclType(param.celement, 2);
               return result;
            }
         } else {
            throw new ExceptionAny("oclAsType should have a single OclType parameter");
         }
      }
   }

   private OclType checkOclTypeFeature(String name, Vector params, OclClassInfo oclTypeElement) throws ExceptionAny {
      if (name.equals("allInstances")) {
         if (params != null && params.size() != 0) {
            throw new ExceptionAny("OclType.allInstances feature doesn't take any parameters");
         } else if (oclTypeElement == null) {
            throw new ExceptionAny("allInstances can be called only for OclType expressions with the type known at compile time");
         } else if (oclTypeElement.classType == 2) {
            throw new ExceptionAny("allInstances can't be called for predefined OCL datatypes");
         } else {
            FindInfo.name = name;
            FindInfo.theOperation = this.umlapi.OCLTYPE.findOperation(name);
            Vector v = new Vector();
            v.add((Object)null);
            FindInfo.params = v.elements();
            FindInfo.foundType = 2;
            FindInfo.isCollection = true;
            FindInfo.isBoolean = false;
            FindInfo.owner = this.umlapi.OCLTYPE.classifier;
            return new OclType(this.umlapi.SET, oclTypeElement);
         }
      } else {
         return null;
      }
   }

   private boolean isVisible(Classifier parentOfFeature, int visibility) {
      if (this.umlapi.isMetamodel) {
         return true;
      } else {
         if (parentOfFeature == null) {
            System.out.println("[warning] isVisible, parentOfFeature==null");
         }

         OclClassInfo selfcontext = this.umlapi.varTable.getSelf().rettype.type;

         try {
            OclClassInfo owner = this.umlapi.getClassInfoByClassifier(parentOfFeature);
            switch(visibility) {
            case 0:
               return this == owner && this == selfcontext;
            case 1:
               return this.fullpath.equals(selfcontext.fullpath) && owner.fullpath.equals(this.fullpath);
            case 2:
               return selfcontext.conforms(owner) && this.conforms(owner);
            case 3:
               return true;
            default:
               return true;
            }
         } catch (ExceptionAny var5) {
            return false;
         }
      }
   }

   private OclType paramsMatch(Enumeration formalEnum, Vector actualParams, boolean[] isPerfectMatch) throws ExceptionAny {
      Vector formalParams = new Vector();

      while(formalEnum.hasMoreElements()) {
         formalParams.add(formalEnum.nextElement());
      }

      isPerfectMatch[0] = true;
      Iterator itformal = formalParams.iterator();
      Iterator itactual = actualParams.iterator();
      ParameterImpl theReturn = null;

      ParameterImpl param;
      label73:
      while(itformal.hasNext() && itactual.hasNext()) {
         param = (ParameterImpl)itformal.next();
         OclType actualparamType = (OclType)itactual.next();
         OclClassInfo actualparam = actualparamType.type;
         if (param.getType().getName().equals("[...]")) {
            isPerfectMatch[0] = false;

            while(true) {
               if (!itactual.hasNext()) {
                  break label73;
               }

               itactual.next();
            }
         }

         if (param.getKind() == 3) {
            theReturn = param;
            if (itformal.hasNext()) {
               param = (ParameterImpl)itformal.next();
            }
         }

         if (actualparamType != OclType.ANYTYPE) {
            if (param.getType() != actualparam.classifier) {
               isPerfectMatch[0] = false;
            }

            if (!this.conforms(actualparam.classifier, param.getType())) {
               return null;
            }
         }
      }

      if (itactual.hasNext()) {
         return null;
      } else if (theReturn == null && !itformal.hasNext()) {
         return null;
      } else {
         if (itformal.hasNext()) {
            param = (ParameterImpl)itformal.next();
            if (param.getType().getName().equals("[...]")) {
               param = (ParameterImpl)itformal.next();
            }

            if (theReturn == null) {
               theReturn = param;
            }
         }

         if (!itformal.hasNext() && theReturn.getKind() == 3) {
            OclType rez = this.umlapi.getOclTypeByName(this.fullpath, theReturn.getType().getName());
            if (rez.type == this.umlapi.OCLTEMPLATE) {
               rez.type = this;
            }

            return rez;
         } else {
            return null;
         }
      }
   }

   private boolean isMultiple(AssociationEnd as) {
      Enumeration mul = as.getMultiplicity().getRangeList();
      if (!mul.hasMoreElements()) {
         return false;
      } else {
         MultiplicityRange range = (MultiplicityRange)mul.nextElement();
         int lo = range.getLower();
         int hi = range.getUpper().shortValue();
         return lo != 0 && lo != 1 || hi != 0 && hi != 1;
      }
   }

   private boolean is01(AssociationEnd as) {
      Enumeration mul = as.getMultiplicity().getRangeList();
      if (!mul.hasMoreElements()) {
         return false;
      } else {
         MultiplicityRange range = (MultiplicityRange)mul.nextElement();
         int lo = range.getLower();
         int hi = range.getUpper().shortValue();
         return (lo == 0 || lo == 1) && hi == 1;
      }
   }

   private AssociationEnd getAssociationEnd(AssociationEnd opend) {
      Enumeration links = opend.getAssociation().getConnectionList();
      AssociationEnd thisend = null;

      while(links.hasMoreElements()) {
         AssociationEnd link = (AssociationEnd)links.nextElement();
         if (link != opend) {
            thisend = link;
         }
      }

      return thisend;
   }

   private OclClassInfo getNavigationResult(AssociationEnd thisend, AssociationEnd opend) {
      if (!this.isMultiple(opend)) {
         FindInfo.is01 = this.is01(opend);
         return null;
      } else {
         return opend.getOrdering() == 0 ? this.umlapi.SEQUENCE : this.umlapi.SET;
      }
   }

   private boolean qualifierMatch(String qualifier, AssociationEnd opend) throws ExceptionAny {
      if (qualifier == null) {
         return true;
      } else if (!(opend.getParticipant() instanceof AssociationClass)) {
         return false;
      } else {
         Enumeration links = opend.getAssociation().getConnectionList();
         AssociationEnd thisend = null;

         while(links.hasMoreElements()) {
            AssociationEnd link = (AssociationEnd)links.nextElement();
            if (link != opend) {
               thisend = link;
            }
         }

         String asname = thisend.getName();
         if (asname.equals("")) {
            asname = thisend.getParticipant().getName();
            asname = asname.substring(0, 1).toLowerCase() + asname.substring(1);
         }

         if (asname.indexOf("///") >= 0) {
            asname = asname.substring(0, asname.indexOf("///"));
         }

         if (asname.equals("templateParameter")) {
            asname = "parameterTemplate";
         }

         return asname.equals(qualifier);
      }
   }

   private boolean qualifiersMatch(Vector actual, AssociationEnd asend) {
      Enumeration formal = asend.getQualifierList();
      Iterator actualIt = actual.iterator();

      while(formal.hasMoreElements() && actualIt.hasNext()) {
         Classifier frm = ((Attribute)formal.nextElement()).getType();
         Classifier act = ((OclType)actualIt.next()).type.classifier;
         if (!this.conforms(act, frm)) {
            return false;
         }
      }

      return !formal.hasMoreElements() && !actualIt.hasNext();
   }

   SearchResult findFeature(String name, Vector actualParams, int matchKind, String qualifier, Vector qualifiers, OclClassInfo oclTypeElement, boolean isCasted) throws ExceptionAny {
      return this.findFeature(name, actualParams, matchKind, qualifier, qualifiers, oclTypeElement, isCasted, false);
   }

   SearchResult findFeature(String name, Vector actualParams, int matchKind, String qualifier, Vector qualifiers, OclClassInfo oclTypeElement, boolean isCasted, boolean isStatic) throws ExceptionAny {
      FindInfo.clear();
      int foundType = 2;
      OclClassInfo foundOwner = this;
      OclLetItem foundLetItem = null;
      OclType result = null;
      if (matchKind == 2) {
         result = this.checkOclAnyFeature(name, actualParams);
         if (result == null && this == this.umlapi.OCLTYPE) {
            result = this.checkOclTypeFeature(name, actualParams, oclTypeElement);
         }

         if (result != null) {
            SearchResult sr = new SearchResult(result, foundType, this.umlapi.getClassInfoByClassifier(FindInfo.owner), (OclLetItem)null);
            sr.attr = FindInfo.theAttribute;
            sr.asend = FindInfo.theAssociationEnd;
            sr.operation = FindInfo.theOperation;
            return sr;
         }
      }

      boolean foundOne = false;
      boolean visibilityProblem = false;
      boolean isQueryProblem = false;
      boolean isAbstractProblem = false;
      boolean isNavigableProblem = false;
      boolean paramProblem = false;
      boolean notPerfectProblem = false;
      boolean ambiguityProblem = false;
      boolean isQualifierProblem = false;
      boolean isScopeProblem = false;
      Operation ambigOp1 = null;
      Operation ambigOp2 = null;
      Operation noMatchOp = null;
      OclClassInfo qualifierClass = null;
      String qualifierPath = null;
      if (name.indexOf("::") >= 0) {
         qualifierPath = name.substring(0, name.lastIndexOf("::"));
         qualifierClass = this.umlapi.getClassInfoByName(this.fullpath, qualifierPath);
         name = name.substring(name.lastIndexOf("::") + 2);
      }

      if (actualParams == null) {
         Iterator it;
         if (qualifier == null) {
            it = this.attrs.iterator();

            label433:
            while(true) {
               while(true) {
                  Attribute at;
                  do {
                     do {
                        if (!it.hasNext()) {
                           break label433;
                        }

                        at = (Attribute)it.next();
                     } while(qualifierPath != null && (qualifierClass == null || at.getOwner() != qualifierClass.classifier));
                  } while(!name.equals(at.getName()));

                  if (this.isVisible(at.getOwner(), at.getVisibility())) {
                     if (!isStatic || at.getOwnerScope() == 0) {
                        foundOne = true;
                        Classifier attype = at.getType();
                        if (attype == null) {
                           System.out.println("[fatal] OclClassInfo.findFeature, attype==null");
                        }

                        int lo = 1;
                        int hi = 1;
                        if (at.getMultiplicity() != null && at.getMultiplicity().getRangeList() != null && at.getMultiplicity().getRangeList().hasMoreElements()) {
                           MultiplicityRange range = (MultiplicityRange)at.getMultiplicity().getRangeList().nextElement();
                           lo = range.getLower();
                           hi = range.getUpper().intValue();
                        }

                        boolean is01 = lo == 0 && hi == 1 && !this.umlapi.isMetamodel;
                        boolean isCollection = !is01 && (lo != 1 || hi != 1) && !this.umlapi.isMetamodel;
                        OclClassInfo atci = this.umlapi.getClassInfoByClassifier(attype);
                        result = new OclType(atci, is01 ? 1 : 0);
                        if (isCollection) {
                           result = new OclType(this.umlapi.SEQUENCE, result, false);
                        }

                        FindInfo.is01 = is01;
                        FindInfo.name = name;
                        FindInfo.theOperation = null;
                        FindInfo.params = null;
                        FindInfo.foundType = 1;
                        FindInfo.isCollection = isCollection;
                        FindInfo.isBoolean = !FindInfo.isCollection && result.type == this.umlapi.BOOLEAN;
                        FindInfo.theAttribute = at;
                        FindInfo.owner = at.getOwner();
                        FindInfo.attrType = result;
                        FindInfo.collectionType = FindInfo.isCollection ? this.umlapi.SEQUENCE : null;
                        FindInfo.elementType = atci;
                        break label433;
                     }

                     isScopeProblem = true;
                  } else {
                     visibilityProblem = true;
                  }
               }
            }
         }

         if (!isStatic) {
            it = this.assocs.iterator();
            AssociationEnd foundAs = null;

            label381:
            while(true) {
               while(true) {
                  while(true) {
                     AssociationEnd as;
                     AssociationEnd asend;
                     OclClassInfo participant;
                     boolean isQualifiedAs;
                     do {
                        String asname;
                        do {
                           do {
                              if (!it.hasNext()) {
                                 break label381;
                              }

                              as = (AssociationEnd)it.next();
                              asend = this.getAssociationEnd(as);
                              Classifier part = as.getParticipant();
                              if (part == null) {
                                 System.out.println("[fatal] OclClassInfo.findfeature, participant==null");
                              }

                              participant = this.umlapi.getClassInfoByClassifier(part);
                              asname = as.getName();
                              if (asname == null) {
                                 asname = "";
                              }

                              if (asname.equals("")) {
                                 asname = part.getName();
                                 asname = asname.substring(0, 1).toLowerCase() + asname.substring(1);
                              }

                              if (asname.indexOf("///") >= 0) {
                                 asname = asname.substring(0, asname.indexOf("///"));
                              }
                           } while(!name.equals(asname));
                        } while(qualifierPath != null && !participant.fullpath.endsWith(qualifierPath) && (qualifierClass == null || asend.getParticipant() != qualifierClass.classifier));

                        isQualifierProblem = true;
                        isQualifiedAs = qualifiers != null && this.qualifiersMatch(qualifiers, as);
                     } while(qualifier != null && !this.qualifierMatch(qualifier, as) && !isQualifiedAs);

                     if (as.isNavigable()) {
                        if (this.isVisible(asend.getParticipant(), as.getVisibility())) {
                           if (foundOne && foundAs != as && (!isCasted || asend.getParticipant() != this.classifier)) {
                              throw new ExceptionAny("feature call " + name + " is ambiguous\n" + " found association end " + name + " to " + as.getParticipant().getName() + " (" + as.getAssociation().getName() + ")" + " and \n" + (foundAs == null ? " attribute " + FindInfo.theAttribute.getOwner().getName() + "::" + FindInfo.theAttribute.getName() : " to " + foundAs.getParticipant().getName() + " (" + foundAs.getAssociation().getName() + ")" + "\n"));
                           }

                           foundOne = true;
                           OclClassInfo coltype = this.getNavigationResult(asend, as);
                           if (isQualifiedAs) {
                              coltype = null;
                           }

                           foundAs = as;
                           FindInfo.name = name;
                           FindInfo.theOperation = null;
                           FindInfo.params = null;
                           FindInfo.foundType = 3;
                           FindInfo.theAssociationEnd = as;
                           FindInfo.collectionType = coltype;
                           FindInfo.elementType = participant;
                           FindInfo.isCollection = coltype != null;
                           FindInfo.isBoolean = coltype == null && participant == this.umlapi.BOOLEAN;
                           FindInfo.owner = asend.getParticipant();
                           if (coltype == null) {
                              result = new OclType(participant, FindInfo.is01 ? 1 : 0);
                           } else {
                              result = new OclType(coltype, participant);
                           }

                           if (isCasted && asend.getParticipant() == this.classifier) {
                              break label381;
                           }
                        } else {
                           visibilityProblem = true;
                        }
                     } else {
                        isNavigableProblem = true;
                     }
                  }
               }
            }
         }
      }

      if (result == null && qualifier == null && (qualifierClass != null || qualifierPath == null)) {
         if (actualParams == null) {
            actualParams = new Vector();
         }

         boolean isMatch = false;
         int matchDist = foundOne ? -1000 : 0;
         Operation matchop = null;
         Iterator it = this.ops.iterator();
         IntArray.Iterator itlevels = this.opslevels.iterator();

         label299:
         while(true) {
            while(true) {
               Operation op;
               int inhdist;
               do {
                  do {
                     if (!it.hasNext() || !itlevels.hasNext()) {
                        if (isCasted && qualifierClass == null) {
                           qualifierClass = this;
                        }

                        Object[] rez = this.umlapi.dynamicBinding.getStaticLetItem(this, name, actualParams, qualifierClass);
                        if (rez != null) {
                           if (result != null) {
                              throw new ExceptionAny("ambiguity, feature " + name + " found in " + matchop.getOwner().getName() + " and in " + ((OclClassInfo)rez[0]).name + " as def function");
                           }

                           foundType = 8;
                           foundOwner = (OclClassInfo)rez[0];
                           foundLetItem = (OclLetItem)rez[1];
                           foundHasResolution = qualifierClass != null;
                           result = foundLetItem.rettype;
                        }
                        break label299;
                     }

                     op = (Operation)it.next();
                     inhdist = itlevels.next();
                  } while(qualifierClass != null && op.getOwner() != qualifierClass.classifier);
               } while(!op.getName().equals(name));

               if (!this.isVisible(op.getOwner(), op.getVisibility())) {
                  visibilityProblem = true;
               } else if (op.isAbstract()) {
                  isAbstractProblem = true;
               } else if (isStatic && op.getOwnerScope() != 0) {
                  isScopeProblem = true;
               } else {
                  paramProblem = true;
                  noMatchOp = op;
                  boolean[] isPerfectMatch = new boolean[1];
                  if (matchKind == 2) {
                     OclType rettype = this.paramsMatch(op.getParameterList(), actualParams, isPerfectMatch);
                     if (rettype != null) {
                        int dist = isPerfectMatch[0] ? inhdist - 1000 : inhdist;
                        if (isMatch && dist == matchDist && op != matchop) {
                           ambigOp1 = matchop;
                           ambigOp2 = op;
                           ambiguityProblem = true;
                           result = null;
                        } else if (!isMatch || dist < matchDist) {
                           isMatch = true;
                           result = rettype;
                           matchDist = dist;
                           matchop = op;
                           FindInfo.name = name;
                           FindInfo.theOperation = op;
                           FindInfo.params = op.getParameterList();
                           FindInfo.foundType = 2;
                           FindInfo.isCollection = false;
                           FindInfo.isBoolean = rettype.type == this.umlapi.BOOLEAN;
                           FindInfo.owner = op.getOwner();
                        }
                     }
                  } else {
                     notPerfectProblem = true;
                     if (this.paramsMatchWithReturn(op.getParameterList(), actualParams, isPerfectMatch) && isPerfectMatch[0]) {
                        SearchResult sr = new SearchResult(new OclType(this), foundType, this, foundLetItem);
                        sr.operation = op;
                        return sr;
                     }
                  }
               }
            }
         }
      }

      if (result == null) {
         if (isScopeProblem) {
            throw new ExceptionAny("the feature is not static and is not accesible from a static context");
         } else if (ambiguityProblem) {
            throw new ExceptionAny("feature call is ambiguous\nfound in " + (ambigOp1 == null ? "?" : ambigOp1.getOwner().getName()) + " and " + (ambigOp2 == null ? "?" : ambigOp2.getOwner().getName()) + "\n");
         } else if (paramProblem) {
            throw new ExceptionAny((notPerfectProblem ? "extended " : "") + "operation signature doesn't match" + (notPerfectProblem ? " perfectly" : "") + "\n" + "   formal: " + noMatchOp.getOwner().getName() + "." + name + this.dumpSignature("", noMatchOp.getParameterList(), (OclClassInfo)null, (OclType)null) + "\n" + "   actual: " + this.classifier.getName() + "." + name + this.dumpSignature(actualParams, matchKind == 1 ? "mayhave" : "noreturn") + "\n");
         } else if (visibilityProblem) {
            throw new ExceptionAny("feature not visible");
         } else if (isQueryProblem) {
            throw new ExceptionAny("operation is not a query");
         } else if (isAbstractProblem) {
            throw new ExceptionAny("operation is abstract");
         } else if (isNavigableProblem) {
            throw new ExceptionAny("association is not navigable");
         } else if (isQualifierProblem) {
            throw new ExceptionAny("the qualifier(s) do(es) not match");
         } else {
            throw new ExceptionAny("feature " + this.name + "." + name + " not found");
         }
      } else {
         SearchResult sr = new SearchResult(result, foundType, foundOwner, foundLetItem);
         sr.attr = FindInfo.theAttribute;
         sr.asend = FindInfo.theAssociationEnd;
         sr.operation = FindInfo.theOperation;
         return sr;
      }
   }

   private String dumpSignature(String packagecontext, Enumeration params, OclClassInfo element, OclType celement) throws ExceptionAny {
      Vector v = new Vector();
      OclType theReturn = null;

      while(params.hasMoreElements()) {
         ParameterImpl param = (ParameterImpl)params.nextElement();
         Classifier cls = param.getType();

         try {
            OclType ocltype = this.umlapi.getOclTypeByName(packagecontext, cls.getName());
            if (ocltype.type == this.umlapi.OCLTEMPLATE) {
               ocltype = celement;
            }

            if (ocltype.element == this.umlapi.OCLTEMPLATE) {
               ocltype.setCElement(celement);
            }

            if (param.getKind() == 3) {
               theReturn = ocltype;
            } else {
               v.add(ocltype);
            }
         } catch (ExceptionAny var10) {
            throw new ExceptionAny("the type " + cls.getName() + " in not defined (usually this happens in incomplete models)");
         }
      }

      if (theReturn != null) {
         v.add(theReturn);
         return this.dumpSignature(v, "return");
      } else {
         return this.dumpSignature(v, "noreturn");
      }
   }

   private String dumpSignature(Vector params, String hasReturnInd) {
      boolean hasReturn = false;
      if (hasReturnInd.equals("noreturn")) {
         hasReturn = false;
      } else if (hasReturnInd.equals("return")) {
         hasReturn = true;
      } else if (params.size() > 0 && ((OclType)params.lastElement()).isReturn) {
         hasReturn = true;
      }

      boolean first = true;
      StringBuffer s = new StringBuffer("(");

      for(Iterator it = params.iterator(); it.hasNext(); first = false) {
         OclType ocltype = (OclType)it.next();
         if (!it.hasNext() && hasReturn) {
            s.append("):");
         } else if (!first) {
            s.append(",");
         }

         s.append(ocltype.getFullName());
      }

      if (first || !hasReturn) {
         s.append(")");
      }

      return s.toString();
   }

   SearchResult findCollectionFeature(String packagecontext, String name, Vector actualParams, OclClassInfo element, OclType celement) throws ExceptionAny {
      FindInfo.clear();
      boolean paramNoMatch = false;
      boolean paramNrDif = false;
      Operation noMatchOp = null;
      if (actualParams == null) {
         actualParams = new Vector();
      }

      if (name.equals("sum") && element != this.umlapi.REAL && element != this.umlapi.INTEGER) {
         throw new ExceptionAny("sum() collection operation can be called for numeric collections only");
      } else if (name.equals("oclType") && actualParams.size() == 0) {
         FindInfo.name = name;
         FindInfo.owner = this.umlapi.OCLANY.classifier;
         actualParams.add((Object)null);
         FindInfo.params = actualParams.elements();
         OclType type = new OclType(this, celement, false);
         type = new OclType(this.umlapi.OCLTYPE, type, false);
         SearchResult sr = new SearchResult(type, 2, this.umlapi.OCLANY, (OclLetItem)null);
         sr.asend = this.umlapi.OCLANY.findAssociationEnd(name);
         return sr;
      } else if (name.equals("oclAsType") && actualParams.size() == 1) {
         FindInfo.name = name;
         FindInfo.owner = this.umlapi.OCLANY.classifier;
         actualParams.insertElementAt((Object)null, 0);
         FindInfo.params = actualParams.elements();
         Object param = actualParams.elementAt(1);
         if (!(param instanceof OclType)) {
            throw new ExceptionAny("oclAsType must have one parameter with the type OclType");
         } else {
            OclType type = ((OclType)param).celement;
            SearchResult sr = new SearchResult(type, 2, this.umlapi.OCLANY, (OclLetItem)null);
            sr.operation = this.umlapi.OCLANY.findOperation(name);
            return sr;
         }
      } else {
         Iterator it = this.ops.iterator();

         while(true) {
            Operation op;
            do {
               if (!it.hasNext()) {
                  Object[] rez = this.umlapi.dynamicBinding.getStaticLetItem(this, name, actualParams, (OclClassInfo)null);
                  if (rez != null) {
                     OclLetItem let = (OclLetItem)rez[1];
                     return new SearchResult(let.rettype, 8, (OclClassInfo)rez[0], (OclLetItem)rez[1]);
                  }

                  if (paramNoMatch) {
                     throw new ExceptionAny("collection operation signature doesn't match\n   formal: " + noMatchOp.getOwner().getName() + "." + name + this.dumpSignature(packagecontext, noMatchOp.getParameterList(), element, celement) + "\n" + "   actual: " + this.classifier.getName() + "." + name + this.dumpSignature(actualParams, "noreturn") + "\n");
                  }

                  if (paramNrDif) {
                     throw new ExceptionAny("invalid number of parameters in collection operation");
                  }

                  throw new ExceptionAny("collection operation not found");
               }

               op = (Operation)it.next();
            } while(!op.getName().equals(name));

            Iterator itactual = actualParams.iterator();
            Enumeration enformal = op.getParameterList();
            boolean isMatch = true;
            boolean oneSkip = false;
            OclType evalType = null;

            OclType ret;
            while(itactual.hasNext() && enformal.hasMoreElements()) {
               OclType actual = (OclType)itactual.next();
               ret = this.umlapi.getOclTypeByName(packagecontext, ((ParameterImpl)enformal.nextElement()).getType().getName());
               if (ret == OclType.PARAMLIST) {
                  while(itactual.hasNext()) {
                     itactual.next();
                  }
                  break;
               }

               if (ret == null) {
                  throw new ExceptionAny("[internal] collection operation parameter from model not solved");
               }

               if (actual.type != this.umlapi.OCLITERATOR && ret.type == this.umlapi.OCLITERATOR && !oneSkip) {
                  if (!enformal.hasMoreElements()) {
                     isMatch = false;
                     paramNrDif = true;
                     break;
                  }

                  oneSkip = true;
                  ret = this.umlapi.getOclTypeByName(packagecontext, ((ParameterImpl)enformal.nextElement()).getType().getName());
               }

               if (ret == null) {
                  throw new ExceptionAny("[internal] collection operation parameter from model not solved");
               }

               if (ret.type == this.umlapi.OCLEVALUATION && actual.type != this.umlapi.OCLITERATOR) {
                  if (evalType == null) {
                     evalType = actual;
                  } else if (!actual.conforms(evalType)) {
                     isMatch = false;
                     paramNoMatch = true;
                     noMatchOp = op;
                     break;
                  }
               } else if (actual.type != this.umlapi.OCLITERATOR || ret.type != this.umlapi.OCLTEMPLATE) {
                  if (ret.type == this.umlapi.OCLTEMPLATE) {
                     ret = celement;
                  }

                  if (ret.element == this.umlapi.OCLTEMPLATE) {
                     ret.setCElement(celement);
                  }

                  if (!actual.conforms(ret)) {
                     paramNoMatch = true;
                     noMatchOp = op;
                     isMatch = false;
                     break;
                  }
               }
            }

            if (itactual.hasNext() || !enformal.hasMoreElements()) {
               paramNrDif = true;
               isMatch = false;
            }

            if (isMatch) {
               String lastone = ((ParameterImpl)enformal.nextElement()).getType().getName();
               if (lastone.equals("[...]")) {
                  lastone = ((ParameterImpl)enformal.nextElement()).getType().getName();
               }

               if (!enformal.hasMoreElements()) {
                  ret = this.umlapi.getOclTypeByName(packagecontext, lastone);
                  if (ret == null) {
                     throw new ExceptionAny("[internal] collection operation return type not solved");
                  }

                  if (ret.type == this.umlapi.OCLTEMPLATE) {
                     ret = celement;
                  }

                  if (ret.element == this.umlapi.OCLTEMPLATE) {
                     ret.setCElement(celement);
                  }

                  FindInfo.name = name;
                  FindInfo.theOperation = op;
                  FindInfo.params = op.getParameterList();
                  FindInfo.foundType = 2;
                  FindInfo.isCollection = false;
                  FindInfo.isBoolean = ret.type == this.umlapi.BOOLEAN;
                  FindInfo.owner = op.getOwner();
                  SearchResult sr;
                  if (ret.type == this.umlapi.OCLEVALUATION) {
                     sr = new SearchResult(evalType, 2, this, (OclLetItem)null);
                     sr.operation = op;
                     return sr;
                  }

                  if (ret.element == this.umlapi.OCLEVALUATION) {
                     ret = new OclType(ret.type, evalType, false);
                  }

                  sr = new SearchResult(ret, 2, this, (OclLetItem)null);
                  sr.operation = op;
                  return sr;
               }

               paramNrDif = true;
               isMatch = false;
            }
         }
      }
   }

   private boolean paramsMatchWithReturn(Enumeration formalEnum, Vector actualParams, boolean[] isPerfectMatch) {
      isPerfectMatch[0] = true;
      Iterator itactual = actualParams.iterator();
      ParameterImpl theReturn = null;
      ArrayList formalParams = new ArrayList();

      ParameterImpl formalParam;
      while(formalEnum.hasMoreElements()) {
         formalParam = (ParameterImpl)formalEnum.nextElement();
         if (formalParam.getKind() == 3) {
            theReturn = formalParam;
         } else {
            formalParams.add(formalParam);
         }
      }

      if (theReturn != null) {
         formalParams.add(theReturn);
      }

      formalEnum = Collections.enumeration(formalParams);

      while(formalEnum.hasMoreElements() && itactual.hasNext()) {
         formalParam = (ParameterImpl)formalEnum.nextElement();
         Classifier formalClasif = formalParam.getType();
         formalClasif = this.umlapi.replaceRoseDatatype(formalClasif);
         OclType actualType = (OclType)itactual.next();
         Classifier actualClasif = actualType.type.classifier;
         this.umlapi.replaceRoseDatatype(actualClasif);
         if (actualType.isReturn != (formalParam.getKind() == 3)) {
            return false;
         }

         try {
            OclType formalType = this.umlapi.getOclTypeByName("", formalClasif.getName());
            if (!actualType.sameTypeAs(formalType)) {
               isPerfectMatch[0] = false;
            }

            if (!actualType.conforms(formalType)) {
               return false;
            }
         } catch (ExceptionAny var12) {
            var12.printStackTrace();
         }
      }

      return !itactual.hasNext() && !formalEnum.hasMoreElements();
   }

   SearchResult findVariableFeature(String context, String name, Vector params, String qualifier, Vector qualifiers) throws ExceptionAny {
      SearchResult lastsr = null;
      int foundType = 0;
      OclClassInfo foundOwner = null;
      OclLetItem foundLetItem = null;
      OclLetItem foundImplicitVar = null;
      OclLetItem oclvar = this.umlapi.varTable.findLetItem(name, params);
      if (oclvar != null) {
         return new SearchResult(oclvar.rettype, 0, (OclClassInfo)null, oclvar);
      } else {
         OclType result = null;
         String possibleError = "self or iterator feature not found";
         Vector vartable = this.umlapi.varTable.getVarTable();

         for(int i = vartable.size() - 1; i >= 0; --i) {
            oclvar = (OclLetItem)vartable.elementAt(i);
            if (oclvar.vartype == 0 || oclvar.vartype == 5 || oclvar.vartype == 1) {
               OclType rettype = null;

               try {
                  String nname = (oclvar.rettype.type.isCollection ? "->" : ".") + name;
                  SearchResult sr = oclvar.rettype.findFeature(context, nname, params, 2, qualifier, qualifiers);
                  rettype = sr.type;
                  if (rettype != null) {
                     foundType = sr.foundType;
                     foundLetItem = sr.foundLetItem;
                     foundOwner = sr.foundOwner;
                     lastsr = sr;
                  }
               } catch (ExceptionAny var19) {
                  possibleError = var19.getMessage();
               }

               if (rettype != null) {
                  if (result != null) {
                     throw new ExceptionAny("ambiguity, feature in implicit iterators and self more than once, must be qualified");
                  }

                  result = rettype;
                  foundType |= 128;
                  foundImplicitVar = oclvar;
                  break;
               }
            }
         }

         if (result == null) {
            throw new ExceptionAny(possibleError);
         } else {
            SearchResult sr = new SearchResult(result, foundType, foundOwner, foundLetItem);
            sr.attr = lastsr.attr;
            sr.operation = lastsr.operation;
            sr.asend = lastsr.asend;
            sr.foundImplicitOwner = foundImplicitVar;
            return sr;
         }
      }
   }

   public String addLetItem(OclLetItem v, boolean isDef) {
      v.uniqueSignatureIndex = this.umlapi.dynamicBinding.getSignatureIndex(v.name, v.params, v.rettype);

      try {
         SearchResult sr = this.findFeature(this.name + "::" + v.name, v.params, 2, (String)null, (Vector)null, (OclClassInfo)null, false);
         if (sr != null) {
            if (sr.foundType == 8) {
               return "classifier already contains a def function with the name " + v.name;
            }

            return "classifier already contains a feature with the name " + v.name;
         }
      } catch (ExceptionAny var5) {
      }

      OclLetItem li;
      Iterator it;
      if (this.letTable != null) {
         it = this.letTable.iterator();

         while(it.hasNext()) {
            li = (OclLetItem)it.next();
            if (li.uniqueSignatureIndex == v.uniqueSignatureIndex && li.stereotype == v.stereotype) {
               return "function already defined in this classifier";
            }
         }
      }

      if (this.defTable != null) {
         it = this.defTable.iterator();

         while(it.hasNext()) {
            li = (OclLetItem)it.next();
            if (li.uniqueSignatureIndex == v.uniqueSignatureIndex) {
               return "function already defined in this classifier";
            }
         }
      }

      if (isDef) {
         if (this.defTable == null) {
            this.defTable = new Vector();
         }

         this.defTable.add(v);
      } else {
         if (this.letTable == null) {
            this.letTable = new Vector();
         }

         this.letTable.add(v);
      }

      return null;
   }

   static void addStates(StateVertex cst, String path, ArrayList states) {
      if (cst instanceof CompositeState) {
         Enumeration en = ((CompositeState)cst).getSubvertexList();

         while(en.hasMoreElements()) {
            StateVertex st = (StateVertex)en.nextElement();
            states.add(path + "::" + st.getName());
            addStates(st, path + "::" + st.getName(), states);
         }
      }

   }

   static String checkState(String statename, ArrayList states) throws ExceptionAny {
      String result = null;
      if (states != null) {
         Iterator it = states.iterator();

         while(it.hasNext()) {
            String estatename = (String)it.next();
            if (estatename.endsWith("::" + statename)) {
               if (result != null) {
                  throw new ExceptionAny("ambiguity, the name " + statename + " could refer to both " + result + " and " + estatename);
               }

               result = estatename;
            }
         }
      }

      return result;
   }

   public Classifier getClassifier() {
      return this.classifier;
   }

   public String toString() {
      return this.name;
   }

   public boolean isOclType() {
      return this == OclUtil.umlapi.BOOLEAN || this == OclUtil.umlapi.BAG || this == OclUtil.umlapi.COLLECTION || this == OclUtil.umlapi.ENUMERATION || this == OclUtil.umlapi.INTEGER || this == OclUtil.umlapi.REAL || this == OclUtil.umlapi.OCLANY || this == OclUtil.umlapi.STRING || this == OclUtil.umlapi.OCLTYPE || this == OclUtil.umlapi.SET || this == OclUtil.umlapi.ORDEREDSET || this == OclUtil.umlapi.SEQUENCE;
   }

   public boolean isPrimitiveType() {
      return this == OclUtil.umlapi.BOOLEAN || this == OclUtil.umlapi.INTEGER || this == OclUtil.umlapi.REAL;
   }

   public boolean isCollectionType() {
      return this.isCollection;
   }

   public int hashCode() {
      return this.fullname != null ? this.fullname.hashCode() : this.name.hashCode();
   }

   static {
      try {
         (ModelElement.class).getMethod("getPackageList", new Class[0]);
         isSpanishAPI = false;
      } catch (NoSuchMethodException var1) {
         isSpanishAPI = true;
      }

   }
}
