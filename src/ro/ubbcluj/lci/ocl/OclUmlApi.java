package ro.ubbcluj.lci.ocl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.datatypes.OclEnumLiteral;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEndImpl;
import ro.ubbcluj.lci.uml.foundation.core.AssociationImpl;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnershipImpl;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRangeImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.ModelImpl;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class OclUmlApi {
   static boolean debug = false;
   private Model model;
   private Vector path = new Vector();
   Vector classes = new Vector();
   private Vector packages = new Vector();
   private HashMap map_classifier = new HashMap();
   private HashMap map_javaclass = new HashMap();
   private ArrayList stateMachines = new ArrayList();
   private OclClassInfo dum = new OclClassInfo((OclUmlApi)null, (Classifier)null, (Vector)null, 3);
   public OclClassInfo TUPLE;
   public OclClassInfo OCLANY;
   public OclClassInfo INTEGER;
   public OclClassInfo REAL;
   public OclClassInfo STRING;
   public OclClassInfo BOOLEAN;
   public OclClassInfo ENUMERATION;
   public OclClassInfo NAME;
   public OclClassInfo UNLIMITED;
   public OclClassInfo BAG;
   public OclClassInfo SET;
   public OclClassInfo ORDEREDSET;
   public OclClassInfo SEQUENCE;
   public OclClassInfo COLLECTION;
   public OclClassInfo UNDEFINED;
   public OclClassInfo OCLTYPE;
   public OclClassInfo OCLITERATOR;
   public OclClassInfo OCLEVALUATION;
   public OclClassInfo OCLTEMPLATE;
   public OclClassInfo OCLSTATE;
   public OclClassInfo ANYEXPR;
   private Vector asclasses;
   private Vector aschanges;
   OclVarTable varTable;
   public boolean isMetamodel;
   public OclDynamicBinding dynamicBinding;
   private static final String[] ROSE_INT = new String[]{"Integer", "int", "long", "short", "Long", "Short", "integer", "BigInteger", "UnlimitedInteger"};
   private static final String[] ROSE_REAL = new String[]{"Real", "real", "double", "float", "Double", "Float"};
   private static final String[] ROSE_BOOLEAN = new String[]{"Boolean", "bool", "boolean", "Bool"};
   private static final String[] ROSE_STRING = new String[]{"Name", "String", "string", "char", "Character"};

   public OclUmlApi(Model model, boolean isMetamodel) {
      this.NAME = this.dum;
      this.UNLIMITED = this.dum;
      this.asclasses = new Vector();
      this.varTable = new OclVarTable();
      this.isMetamodel = true;
      OclUtil.umlapi = this;
      this.isMetamodel = isMetamodel;
      this.init(model);
   }

   private void init(Model model) {
      this.model = model;
      this.loadDatatypes();
      this.processModel();
   }

   public Model getModel() {
      return this.model;
   }

   private void processModel() {
      this.visitModelElement(this.model, 0, true, (ModelElement)null);
      this.aschanges = new Vector();
      Iterator it = this.asclasses.iterator();

      while(it.hasNext()) {
         AssociationClass ascls = (AssociationClass)it.next();
         ModelElement owner = (ModelElement)it.next();
         this.processAssociationClass(ascls, owner);
      }

      this.visitModelElement(this.model, 0, false, (ModelElement)null);
      it = this.aschanges.iterator();

      while(it.hasNext()) {
         ((OclUmlApi.Change)it.next()).undo();
      }

      this.aschanges.clear();
      this.OCLTEMPLATE = new OclClassInfo(this, (Classifier)null, (Vector)null, 3);
      it = this.classes.iterator();

      while(it.hasNext()) {
         try {
            OclClassInfo ci = (OclClassInfo)it.next();
            Class cls = ci.getJavaClass();
            cls = ci.getJavaClassImpl();
            this.map_javaclass.put(cls, ci);
         } catch (ExceptionAny var4) {
            if (debug) {
               System.out.println(var4.getMessage());
            }
         }
      }

      if (debug) {
         System.out.println();
      }

      this.visitStateMachines(this.model, "");
      if (debug) {
         System.out.println();
      }

   }

   public static String firstLower(String s) {
      return s.length() == 0 ? s : s.substring(0, 1).toLowerCase() + s.substring(1);
   }

   public static String firstUpper(String s) {
      return s.length() == 0 ? s : s.substring(0, 1).toUpperCase() + s.substring(1);
   }

   private Multiplicity clone(Multiplicity mul) {
      Multiplicity newmul = new MultiplicityImpl();
      Enumeration en = mul.getRangeList();

      while(en.hasMoreElements()) {
         MultiplicityRangeImpl range = (MultiplicityRangeImpl)en.nextElement();
         MultiplicityRangeImpl newrange = new MultiplicityRangeImpl();
         newrange.setLower(range.getLower());
         newrange.setUpper(range.getUpper());
         newmul.addRange(newrange);
      }

      return newmul;
   }

   private void processAssociationClass(AssociationClass ascls, ModelElement parent) {
      Enumeration ends = ascls.getConnectionList();
      AssociationEnd endAB = null;
      AssociationEnd endBA = null;
      if (ends.hasMoreElements()) {
         endAB = (AssociationEnd)ends.nextElement();
      }

      if (ends.hasMoreElements()) {
         endBA = (AssociationEnd)ends.nextElement();
      }

      if (endAB != null && endBA != null) {
         MultiplicityRangeImpl mrange = new MultiplicityRangeImpl();
         mrange.setLower(1);
         mrange.setUpper(BigInteger.ONE);
         MultiplicityImpl multi = new MultiplicityImpl();
         multi.addRange(mrange);
         OclUmlApi.MyAssociationEndImpl endCA = new OclUmlApi.MyAssociationEndImpl();
         OclUmlApi.MyAssociationEndImpl endAC = new OclUmlApi.MyAssociationEndImpl();
         OclUmlApi.MyAssociationEndImpl endBC = new OclUmlApi.MyAssociationEndImpl();
         OclUmlApi.MyAssociationEndImpl endCB = new OclUmlApi.MyAssociationEndImpl();
         endAC.setParticipant(ascls);
         endBC.setParticipant(ascls);
         endCA.setParticipant(endBA.getParticipant());
         endCB.setParticipant(endAB.getParticipant());
         endAC.setMultiplicity(this.clone(endAB.getMultiplicity()));
         endBC.setMultiplicity(this.clone(endBA.getMultiplicity()));
         endCA.setMultiplicity(multi);
         endCB.setMultiplicity(this.clone(multi));
         endAC.setVisibility(endAB.getVisibility());
         endBC.setVisibility(endBA.getVisibility());
         endCA.setVisibility(endBA.getVisibility());
         endCB.setVisibility(endAB.getVisibility());
         endAC.setNavigable(endAB.isNavigable());
         endBC.setNavigable(endBA.isNavigable());
         endCA.setNavigable(endBA.isNavigable());
         endCB.setNavigable(endAB.isNavigable());
         endAC.setOrdering(endAB.getOrdering());
         endBC.setOrdering(endBA.getOrdering());
         endCA.setOrdering(1);
         endCB.setOrdering(1);

         for(int i = 0; i < 2; ++i) {
            AssociationEnd end = i == 0 ? endAB : endBA;
            if (end.getParticipant() == null) {
               System.err.println("\nWARNING: UMLAPI error in association class <<<" + ascls.getName() + ">>>, invalid XMI file");
               System.err.println("         The end named <<<" + end.getName() + ">>> returns null to getParticipant()");
               System.err.println("         Navigations involving the association class will not function properly\n");
            }
         }

         String ab = endAB.getName();
         String ba = endBA.getName();
         if (ab == null) {
            ab = "";
         }

         if (ba == null) {
            ba = "";
         }

         if (ab.equals("")) {
            ab = firstLower(endAB.getParticipant().getName());
         }

         if (ba.equals("")) {
            ba = firstLower(endBA.getParticipant().getName());
         }

         ab = firstUpper(endAB.getName());
         ba = firstUpper(endBA.getName());
         endAC.setName(firstLower(ascls.getName()));
         endAC.methodName = "get" + ab;
         endBC.setName(firstLower(ascls.getName()));
         endBC.methodName = "get" + ba;
         endCA.setName(firstLower(ba));
         endCA.methodName = "get" + ba;
         endCB.setName(firstLower(ab));
         endCB.methodName = "get" + ab;
         AssociationImpl asAC = new AssociationImpl();
         asAC.addConnection(endAC);
         asAC.addConnection(endCA);
         AssociationImpl asBC = new AssociationImpl();
         asBC.addConnection(endBC);
         asBC.addConnection(endCB);
         ElementOwnershipImpl own1 = new ElementOwnershipImpl();
         own1.setOwnedElement(asAC);
         ((Namespace)parent).addOwnedElement(own1);
         ElementOwnershipImpl own2 = new ElementOwnershipImpl();
         own2.setOwnedElement(asBC);
         ((Namespace)parent).addOwnedElement(own2);
         OclUmlApi.Change change = new OclUmlApi.Change();
         change.namespace = (Namespace)parent;
         change.ownership2 = own2;
         change.ownership1 = own1;
         change.endAC = endAC;
         change.endBC = endBC;
         change.endCA = endCA;
         change.endCB = endCB;
         this.aschanges.add(change);
      } else {
         throw new InternalError("Association end not found for " + ascls.getName());
      }
   }

   private void loadDatatypes() {
      Package dtPack = OclLoader.getOclTypesModel();
      Iterator it = dtPack.directGetCollectionOwnedElementList().iterator();

      while(it.hasNext()) {
         Object candidate = it.next();
         if (candidate instanceof Classifier) {
            Classifier type = (Classifier)candidate;
            if (UMLUtilities.hasStereotype(type, "OCL_TYPE")) {
               String typeName = type.getName();
               OclClassInfo ci = new OclClassInfo(this, type, OclLoader.getDatatypesPackagePath(), 2);
               this.classes.add(ci);
               this.map_classifier.put(type, ci);
               if ("Sequence".equals(typeName)) {
                  this.SEQUENCE = ci;
               } else if ("Bag".equals(typeName)) {
                  this.BAG = ci;
               } else if ("Set".equals(typeName)) {
                  this.SET = ci;
               } else if ("OrderedSet".equals(typeName)) {
                  this.ORDEREDSET = ci;
               } else if ("Collection".equals(typeName)) {
                  this.COLLECTION = ci;
               } else if ("OclAny".equals(typeName)) {
                  this.OCLANY = ci;
               } else if ("OclState".equals(typeName)) {
                  this.OCLSTATE = ci;
               } else if ("OclType".equals(typeName)) {
                  this.OCLTYPE = ci;
               } else if ("Integer".equals(typeName)) {
                  this.INTEGER = ci;
               } else if ("Real".equals(typeName)) {
                  this.REAL = ci;
               } else if ("String".equals(typeName)) {
                  this.STRING = ci;
               } else if ("Boolean".equals(typeName)) {
                  this.BOOLEAN = ci;
               } else if ("Undefined".equals(typeName)) {
                  this.UNDEFINED = ci;
               } else if ("OclIterator".equals(typeName)) {
                  this.OCLITERATOR = ci;
               } else if ("Enumeration".equals(typeName)) {
                  this.ENUMERATION = ci;
               } else if ("Name".equals(typeName)) {
                  this.NAME = ci;
               } else if ("UnlimitedInteger".equals(typeName)) {
                  this.UNLIMITED = ci;
               } else if ("AnyExpr".equals(typeName)) {
                  this.ANYEXPR = ci;
               } else if ("Accumulator".equals(typeName)) {
                  this.OCLEVALUATION = ci;
               } else if ("Tuple".equals(typeName)) {
                  this.TUPLE = ci;
               }
            }
         }
      }

   }

   private void processElement(ModelElement elem, int level, boolean solveAssociationClasses, ModelElement parent) {
      if (solveAssociationClasses) {
         if (elem instanceof AssociationClass) {
            this.asclasses.add(elem);
            this.asclasses.add(parent);
         }

      } else {
         if (elem instanceof Package) {
            StringBuffer fullPackagePath = new StringBuffer();
            Iterator it = this.path.iterator();

            while(it.hasNext()) {
               ModelElement currentPathElement = (ModelElement)it.next();
               fullPackagePath.append("::").append(currentPathElement.getName());
            }

            fullPackagePath.append("::" + elem.getName());

            for(int i = 0; i < fullPackagePath.length(); ++i) {
               if (fullPackagePath.charAt(i) == ' ') {
                  fullPackagePath.setCharAt(i, '_');
               }
            }

            this.packages.add(fullPackagePath.toString());
            if (debug) {
               if (elem instanceof ModelImpl) {
                  System.out.println("\nModel: " + elem.getName() + "\n");
               } else {
                  System.out.println("\nPackage: " + (fullPackagePath.length() < 2 ? fullPackagePath.toString() : fullPackagePath.substring(2)) + "\n");
               }
            }
         }

         if (elem instanceof ro.ubbcluj.lci.uml.foundation.core.Class || elem instanceof ro.ubbcluj.lci.uml.foundation.core.Enumeration || !this.isMetamodel && (elem instanceof Interface || elem instanceof DataType)) {
            Classifier type = (Classifier)elem;
            boolean isOclTypeStereotyped = UMLUtilities.hasStereotype(type, "OCL_TYPE");
            boolean isOclType = isOclTypeStereotyped && parent == OclLoader.getOclTypesModel();
            if (!isOclType && (ModelFactory.getDataTypeSystem().getTypeId(type) == null || "void".equals(type.getName()) || "Void".equals(type.getName()))) {
               OclClassInfo ci = new OclClassInfo(this, type, this.path, this.isMetamodel ? 1 : 3);
               this.classes.add(ci);
               this.map_classifier.put(elem, ci);
            }
         }

      }
   }

   private void visitModelElement(ModelElement node, int level, boolean solveAssociationClasses, ModelElement parent) {
      this.processElement(node, level, solveAssociationClasses, parent);
      if (node instanceof Namespace) {
         boolean updatePath;
         if (node instanceof Model) {
            updatePath = ((Model)node).directGetNamespace() != null;
         } else {
            Class c = node.getClass();
            updatePath = (Package.class).isAssignableFrom(c) || (ro.ubbcluj.lci.uml.foundation.core.Class.class).isAssignableFrom(c);
         }

         if (updatePath) {
            this.path.add(node);
         }

         Enumeration ownedElements = ((Namespace)node).getOwnedElementList();

         while(ownedElements.hasMoreElements()) {
            Object item = ownedElements.nextElement();
            if (item instanceof ElementOwnership) {
               ModelElement modelElement = ((ElementOwnership)item).getOwnedElement();
               if (modelElement != null && node != modelElement) {
                  this.visitModelElement(modelElement, level + 1, solveAssociationClasses, node);
               }
            }
         }

         if (updatePath) {
            this.path.remove(node);
         }
      }

   }

   private void visitStateMachines(Namespace node, String path) {
      Enumeration en = node.getOwnedElementList();

      while(en.hasMoreElements()) {
         ModelElement elem = ((ElementOwnership)en.nextElement()).getOwnedElement();
         if (elem instanceof StateMachine) {
            StateMachine sm = (StateMachine)elem;

            try {
               ModelElement smContext = sm.getContext();
               if (smContext != null && smContext instanceof Classifier) {
                  OclClassInfo ci = this.getClassInfoByClassifier((Classifier)smContext);
                  ArrayList states = new ArrayList();
                  OclClassInfo.addStates(sm.getTop(), "::" + sm.getName(), states);
                  if (ci.states == null) {
                     ci.states = new ArrayList();
                  }

                  ci.states.addAll(states);
                  this.stateMachines.add(new Object[]{sm, path, states});
               }
            } catch (ExceptionAny var9) {
               System.err.println("warning: error processing state machine " + sm.getName());
            }
         }

         if (elem instanceof Model) {
            this.visitStateMachines((Namespace)elem, path);
         } else if (elem instanceof Namespace) {
            this.visitStateMachines((Namespace)elem, path + elem.getName() + "::");
         }
      }

   }

   private boolean hasPackageAccess(String context, OclClassInfo ci) {
      if (!this.isMetamodel && ci.classType != 2) {
         String copy = new String(context);
         ArrayList path1 = new ArrayList();
         int i = copy.indexOf("::");
         if (i <= 0) {
            if (!"".equals(copy)) {
               path1.add(copy);
            }
         } else {
            while(i > 0) {
               String sub = copy.substring(0, i);
               path1.add(sub);
               copy = copy.substring(i + 2);
               i = copy.indexOf("::");
            }

            path1.add(copy);
         }

         String[] path = (String[])path1.toArray(new String[0]);
         Package contextPackage = UMLUtilities.getPackage(this.model, path);
         if (contextPackage == null) {
            throw new RuntimeException("Package not found: " + context);
         } else {
            Package p = (Package)ci.classifier.directGetNamespace();
            return UMLUtilities.hasPermission(contextPackage, p);
         }
      } else {
         return true;
      }
   }

   public OclClassInfo getClassInfoByName(String context, String s) throws ExceptionAny {
      s = this.replaceRoseDatatype(s);
      boolean isQualified = s.indexOf("::") >= 0;
      Iterator it = this.classes.iterator();
      OclClassInfo result = null;
      boolean foundOne = false;
      boolean visibilityProblem = false;

      while(it.hasNext()) {
         OclClassInfo ci = (OclClassInfo)it.next();
         if (isQualified && s.equals(ci.fullname)) {
            if (this.hasPackageAccess(context, ci)) {
               result = ci;
               break;
            }

            visibilityProblem = true;
         }

         if (!isQualified && (context + "::" + s).equals(ci.fullname)) {
            result = ci;
            break;
         }

         if (!isQualified && ci.fullname.endsWith("::" + s)) {
            if (!this.hasPackageAccess(context, ci)) {
               visibilityProblem = true;
            } else {
               if (foundOne) {
                  throw new ExceptionAny("classifier name " + s + " is ambiguous in context " + context);
               }

               foundOne = true;
               result = ci;
            }
         }
      }

      if (!foundOne && visibilityProblem) {
         throw new ExceptionAny("class " + s + " is not visible in the current context");
      } else {
         if (result == this.NAME) {
            result = this.STRING;
         }

         if (result == this.UNLIMITED) {
            result = this.INTEGER;
         }

         return this.replaceRoseDatatype(result);
      }
   }

   String checkPackageName(String name) throws ExceptionAny {
      boolean foundOne = false;
      Iterator it = this.packages.iterator();

      while(it.hasNext()) {
         if (("::" + name).equals(it.next())) {
            if (foundOne) {
               throw new ExceptionAny("there are more packages matching the name " + name);
            }

            foundOne = true;
         }
      }

      if (!foundOne) {
         throw new ExceptionAny("package " + name + " not found");
      } else {
         return name;
      }
   }

   OclEnumLiteral checkEnumerationValue(String classname, String value) throws ExceptionAny {
      OclEnumLiteral result = null;
      Iterator it = this.classes.iterator();

      while(true) {
         OclClassInfo ci;
         boolean isEnumeration;
         do {
            do {
               if (!it.hasNext()) {
                  if (result == null) {
                     throw new ExceptionAny("enumeration value " + classname + "#" + value + " cannot be found");
                  }

                  return result;
               }

               ci = (OclClassInfo)it.next();
               Enumeration en = ci.classifier.getStereotypeList();
               isEnumeration = ci.getClassifier() instanceof ro.ubbcluj.lci.uml.foundation.core.Enumeration;

               while(en.hasMoreElements()) {
                  Stereotype st = (Stereotype)en.nextElement();
                  if (st.getName().equalsIgnoreCase("enumeration")) {
                     isEnumeration = true;
                     break;
                  }
               }
            } while(!isEnumeration);
         } while(!classname.equals("") && !ci.fullname.equals(classname) && !ci.fullname.endsWith("::" + classname));

         ArrayList literals = new ArrayList();
         Iterator litit;
         if (ci.classifier instanceof ro.ubbcluj.lci.uml.foundation.core.Enumeration) {
            Enumeration lits = ((ro.ubbcluj.lci.uml.foundation.core.Enumeration)ci.classifier).getLiteralList();

            while(lits.hasMoreElements()) {
               EnumerationLiteral el = (EnumerationLiteral)lits.nextElement();
               literals.add(el);
            }
         } else {
            litit = ci.attrs.iterator();

            while(litit.hasNext()) {
               literals.add(((Attribute)litit.next()).getName());
            }
         }

         litit = literals.iterator();

         while(litit.hasNext()) {
            Object literal = litit.next();
            if (literal instanceof String) {
               if (literal.equals(value)) {
                  if (result != null) {
                     throw new ExceptionAny("enumeration value " + classname + "#" + value + " is ambiguous");
                  }

                  result = new OclEnumLiteral(ci, (String)literal);
               }
            } else if (literal instanceof EnumerationLiteral && ((EnumerationLiteral)literal).getName().equals(value)) {
               if (result != null) {
                  throw new ExceptionAny("enumeration value " + classname + "#" + value + " is ambiguous");
               }

               result = new OclEnumLiteral(ci, (EnumerationLiteral)literal);
            }
         }
      }
   }

   public OclType getOclTypeByName(String context, String name) throws ExceptionAny {
      String s = name;
      StringBuffer ss = new StringBuffer();

      int i;
      for(i = 0; i < s.length(); ++i) {
         char ch = s.charAt(i);
         ss.append(ch == ' ' ? '_' : ch);
      }

      s = ss.toString();
      if (s.equals("[...]")) {
         return OclType.PARAMLIST;
      } else {
         i = s.indexOf("Logical_View::");
         if (i >= 0) {
            ss.delete(i, i + 14);
            s = ss.toString();
         }

         if (s.equals("Foundation::Data_Types::Name")) {
            s = "Name";
         }

         if (s.equals("Set<Foundation::Data_Types::Name>")) {
            s = "Set<Name>";
         }

         OclClassInfo col = null;
         OclClassInfo elem = null;
         i = s.indexOf("<");
         if (i < 0) {
            i = s.indexOf("(");
         }

         if (i >= 0) {
            if (s.substring(0, i).equals("Set")) {
               col = this.SET;
            }

            if (s.substring(0, i).equals("Bag")) {
               col = this.BAG;
            }

            if (s.substring(0, i).equals("Sequence")) {
               col = this.SEQUENCE;
            }

            if (s.substring(0, i).equals("Collection")) {
               col = this.COLLECTION;
            }

            if (s.substring(0, i).equals("OrderedSet")) {
               col = this.ORDEREDSET;
            }

            if (col != null) {
               s = s.substring(i + 1);
               s = s.substring(0, s.length() - 1);
            }
         }

         if (s.equals("T")) {
            elem = this.OCLTEMPLATE;
         } else if (s.equals("TT")) {
            elem = this.OCLEVALUATION;
         } else {
            elem = this.getClassInfoByName(context, s);
         }

         if (col == null && elem != null) {
            if (elem.isCollection) {
               System.err.println("[WARNING] the model contains the collection type " + name + " which is invalid because it should be something like " + name + "(some_type)");
            }

            return new OclType(elem);
         } else if (col != null && elem != null) {
            return new OclType(col, elem);
         } else if (this.isMetamodel) {
            throw new ExceptionAny("[internal] type " + s + " not found in the metamodel");
         } else {
            throw new ExceptionAny("the type " + s + " is not defined in the user model");
         }
      }
   }

   public OclClassInfo getClassInfoByClassifier(Classifier c) throws ExceptionAny {
      if (c == null) {
         throw new ExceptionAny("[internal] getClassInfoByClassifier called with null");
      } else {
         c = this.replaceRoseDatatype(c);
         OclClassInfo ci = (OclClassInfo)this.map_classifier.get(c);
         if (ci == this.NAME) {
            ci = this.STRING;
         }

         if (ci == this.UNLIMITED) {
            ci = this.INTEGER;
         }

         if (ci == null) {
            ci = this.getClassInfoByJavaClass(c.getClass());
         }

         if (ci != null) {
            return ci;
         } else {
            throw new ExceptionAny("user model contains an undefined type: " + c.getName());
         }
      }
   }

   public OclClassInfo getClassInfoByJavaClass(Class c) throws ExceptionAny {
      if (c == null) {
         throw new ExceptionAny("[internal] getClassInfoByJavaClass called with null");
      } else {
         OclClassInfo ci = (OclClassInfo)this.map_javaclass.get(c);
         if (ci == this.NAME) {
            ci = this.STRING;
         }

         if (ci == this.UNLIMITED) {
            ci = this.INTEGER;
         }

         if (ci != null) {
            return ci;
         } else {
            throw new ExceptionAny("[internal] could not find the classifier for " + c.getName());
         }
      }
   }

   private boolean is(String s, String[] set) {
      for(int i = 0; i < set.length; ++i) {
         if (s.equals(set[i])) {
            return true;
         }
      }

      return false;
   }

   Classifier replaceRoseDatatype(Classifier c) {
      if (c == null) {
         System.err.println("replaceRoseDatatypes with c==null");
         return null;
      } else {
         String name = c.getName();
         if (name.equals("<undefined>")) {
            return this.UNDEFINED.classifier;
         } else if (this.is(name, ROSE_INT)) {
            return this.INTEGER.classifier;
         } else if (this.is(name, ROSE_REAL)) {
            return this.REAL.classifier;
         } else if (this.is(name, ROSE_BOOLEAN)) {
            return this.BOOLEAN.classifier;
         } else {
            return this.is(name, ROSE_STRING) ? this.STRING.classifier : c;
         }
      }
   }

   private String replaceRoseDatatype(String name) {
      if (name == null) {
         System.err.println("replaceRoseDatatypes with s==null");
         return null;
      } else if (name.equals("<undefined>")) {
         return "Undefined";
      } else if (this.is(name, ROSE_INT)) {
         return "Integer";
      } else if (this.is(name, ROSE_REAL)) {
         return "Real";
      } else if (this.is(name, ROSE_BOOLEAN)) {
         return "Boolean";
      } else {
         return this.is(name, ROSE_STRING) ? "String" : name;
      }
   }

   private OclClassInfo replaceRoseDatatype(OclClassInfo ci) {
      if (ci == null) {
         return null;
      } else {
         Classifier c = this.replaceRoseDatatype(ci.classifier);
         return c == ci.classifier ? ci : (OclClassInfo)this.map_classifier.get(c);
      }
   }

   public Object[] findStateMachine(String fullname) throws ExceptionAny {
      Object[] result = null;
      Iterator it = this.stateMachines.iterator();

      while(it.hasNext()) {
         Object[] tuple = (Object[])it.next();
         String sfullname = (String)tuple[1] + "::" + ((StateMachine)tuple[0]).getName();
         if (("::" + sfullname).endsWith("::" + fullname)) {
            if (result != null) {
               throw new ExceptionAny("ambiguity, state machine name could refer to more than one state machine");
            }

            result = tuple;
         }
      }

      if (result == null) {
         throw new ExceptionAny("state machine not found");
      } else {
         return result;
      }
   }

   public String findState(Object[] tuple, String name) throws ExceptionAny {
      ArrayList states = (ArrayList)tuple[2];
      return OclClassInfo.checkState(name, states);
   }

   public void checkSequenceDiagram(String fullname) throws ExceptionAny {
      Object[] result = null;
   }

   public class MyAssociationEndImpl extends AssociationEndImpl {
      public String methodName;
      private Classifier myParticipant;

      public MyAssociationEndImpl() {
      }

      public Classifier getParticipant() {
         return this.myParticipant == null ? super.getParticipant() : this.myParticipant;
      }

      public void setMyParticipant(Classifier participant) {
         this.myParticipant = participant;
      }
   }

   private class Change {
      Namespace namespace;
      ElementOwnership ownership1;
      ElementOwnership ownership2;
      OclUmlApi.MyAssociationEndImpl endAC;
      OclUmlApi.MyAssociationEndImpl endCA;
      OclUmlApi.MyAssociationEndImpl endBC;
      OclUmlApi.MyAssociationEndImpl endCB;

      private Change() {
      }

      void undo() {
         this.namespace.removeOwnedElement(this.ownership1);
         this.namespace.removeOwnedElement(this.ownership2);
         Classifier pAC = this.endAC.getParticipant();
         Classifier pCA = this.endCA.getParticipant();
         Classifier pBC = this.endBC.getParticipant();
         Classifier pCB = this.endCB.getParticipant();
         this.endAC.setParticipant((Classifier)null);
         this.endCA.setParticipant((Classifier)null);
         this.endBC.setParticipant((Classifier)null);
         this.endCB.setParticipant((Classifier)null);
         this.endAC.setMyParticipant(pAC);
         this.endCA.setMyParticipant(pCA);
         this.endBC.setMyParticipant(pBC);
         this.endCB.setMyParticipant(pCB);
      }
   }
}
