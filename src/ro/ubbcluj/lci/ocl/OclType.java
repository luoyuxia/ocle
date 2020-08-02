package ro.ubbcluj.lci.ocl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import ro.ubbcluj.lci.ocl.datatypes.*;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;

public class OclType extends OclAny {
   public OclClassInfo type;
   public OclClassInfo element = null;
   public OclType celement = null;
   public boolean is01 = false;
   public boolean isEmpty01;
   private boolean isCasted;
   public boolean isReturn = false;
   public static final int FT_VARREF = 0;
   public static final int FT_COLLECT = 64;
   public static final int FT_FEATURE = 2;
   public static final int FT_LETDEF = 8;
   public static final int FT_IMPLICIT = 128;
   public static final int FT_TYPEREF = 4;
   public static final int FT_TUPLEPART = 16;
   public static final int FT_STATIC = 256;
   public static final int FT_ENUM_LITERAL = 512;
   static final int OT_IS01 = 1;
   static final int OT_ISCASTED = 2;
   private boolean isTuple = false;
   private int partCount;
   private String[] partNames;
   private OclType[] partTypes;
   private boolean isMessage = false;
   private Operation messageOperation = null;
   static final OclType PARAMLIST = new OclType((OclClassInfo)null);
   static final OclType ANYTYPE = new OclType((OclClassInfo)null);

   public OclType(OclClassInfo p_type) {
      this.type = p_type;
   }

   public OclType(OclClassInfo type, int flags) {
      this.type = type;
      this.isCasted = (flags & 2) == 2;
      this.is01 = (flags & 1) == 1;
   }

   public OclType(OclType type, int flags) {
      this.type = type.type;
      this.element = type.element;
      this.celement = type.celement;
      this.isTuple = type.isTuple;
      this.partCount = type.partCount;
      this.partNames = type.partNames;
      this.partTypes = type.partTypes;
      this.isCasted = (flags & 2) == 2;
      this.is01 = (flags & 1) == 1;
   }

   public OclType(OclClassInfo p_type, OclClassInfo p_element) {
      this.type = p_type;
      this.element = p_element;
      this.celement = new OclType(p_element);
   }

   public OclType(OclClassInfo type, OclType element, boolean flatten) {
      this.type = type;
      if (flatten) {
         boolean onlySequence;
         for(onlySequence = type == type.umlapi.SEQUENCE; element.type.isCollection; element = element.celement) {
            if (element.type != type.umlapi.SEQUENCE) {
               onlySequence = false;
            }
         }

         this.type = onlySequence ? type.umlapi.SEQUENCE : type.umlapi.BAG;
      }

      this.element = element.type;
      this.celement = element;
   }

   public void setElement(OclClassInfo element) {
      this.element = element;
      this.celement = new OclType(element);
   }

   public void setCElement(OclType celement) {
      this.celement = celement;
      this.element = celement.type;
   }

   public OclType(int count, String[] names, OclType[] types) {
      this.type = OclUtil.umlapi.TUPLE;
      this.isTuple = true;
      this.partCount = count;
      this.partNames = names;
      this.partTypes = types;
   }

   public int getPartCount() {
      return this.partCount;
   }

   public OclType getPartType(int index) throws IndexOutOfBoundsException {
      return this.partTypes[index];
   }

   public String getPartName(int index) throws IndexOutOfBoundsException {
      return this.partNames[index];
   }

   private void flatten() {
      if (this.type.isCollection) {
         OclType p;
         for(p = this; p.type.isCollection; p = p.celement) {
         }

         this.element = p.type;
         this.celement = p;
      }

   }

   public static OclType commonType(OclType a, OclType b) {
      if (!a.isTuple && !b.isTuple) {
         if (!a.type.isCollection && !b.type.isCollection) {
            return new OclType(a.type.commonParent(b.type));
         } else {
            return a.type.isCollection ^ b.type.isCollection ? new OclType(OclUtil.umlapi.OCLANY) : new OclType(a.type.commonParent(b.type), commonType(a.celement, b.celement), false);
         }
      } else if (a.isTuple ^ b.isTuple) {
         return new OclType(OclUtil.umlapi.OCLANY);
      } else {
         int n = a.partCount < b.partCount ? a.partCount : b.partCount;
         String[] names = new String[n];
         OclType[] types = new OclType[n];
         int k = 0;

         for(int i = 0; i < a.partCount; ++i) {
            for(int j = 0; j < b.partCount; ++j) {
               if (a.partNames[i].equals(b.partNames[j])) {
                  names[k] = a.partNames[i];
                  types[k] = commonType(a.partTypes[i], b.partTypes[j]);
                  if (types[k] == null) {
                     return new OclType(OclUtil.umlapi.OCLANY);
                  }

                  ++k;
                  break;
               }
            }
         }

         if (k == 0) {
            return new OclType(OclUtil.umlapi.OCLANY);
         } else {
            return new OclType(k, names, types);
         }
      }
   }

   public String getFullName() {
      if (this == PARAMLIST) {
         return "[...]";
      } else if (this.isTuple) {
         if (this.isMessage) {
            return "OclMessage(" + OclUtil.operationAsString(this.messageOperation) + ")";
         } else {
            StringBuffer s = new StringBuffer("Tuple(");

            for(int i = 0; i < this.partCount; ++i) {
               s.append(this.partNames[i] + ":" + this.partTypes[i].getFullName());
               if (i != this.partCount - 1) {
                  s.append(",");
               }
            }

            s.append(")");
            return s.toString();
         }
      } else if (this.type == OclUtil.umlapi.OCLITERATOR) {
         return "[OclIterator]";
      } else if (this.element == null) {
         return this.type == null ? "null" : this.type.name;
      } else {
         return this.type == null ? "null" : this.type.name + "(" + (this.celement == null ? "null" : this.celement.getFullName()) + ")";
      }
   }

   public String toString() {
      return this.getFullName();
   }

   public String printValue() {
      return this.getFullName();
   }

   public boolean conforms(OclType a) {
      if (this != ANYTYPE && a != ANYTYPE) {
         if (a.type != OclUtil.umlapi.OCLANY && a.type != OclUtil.umlapi.ANYEXPR) {
            boolean found;
            if (!this.isTuple && !a.isTuple) {
               boolean isSimpleA = !this.type.isCollection;
               found = !a.type.isCollection;
               if (isSimpleA ^ found) {
                  return false;
               } else if (isSimpleA && found) {
                  return this.type.conforms(a.type);
               } else if (!this.type.conforms(a.type)) {
                  return false;
               } else {
                  return this.element != this.element.umlapi.UNDEFINED && a.element != this.element.umlapi.UNDEFINED ? this.celement.conforms(a.celement) : true;
               }
            } else if (a.isTuple && this.isTuple) {
               if (this.partCount < a.partCount) {
                  return false;
               } else {
                  for(int i = 0; i < a.partCount; ++i) {
                     found = false;

                     for(int j = 0; j < this.partCount; ++j) {
                        if (this.partNames[j].equals(a.partNames[i])) {
                           found = true;
                           if (!this.partTypes[j].conforms(a.partTypes[i])) {
                              return false;
                           }
                           break;
                        }
                     }

                     if (!found) {
                        return false;
                     }
                  }

                  return true;
               }
            } else {
               return false;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean sameTypeAs(OclType a) {
      if (!this.isTuple && !a.isTuple) {
         if (this.type.isCollection ^ a.type.isCollection) {
            return false;
         } else if (this.type == OclUtil.umlapi.OCLTYPE ^ a.type == OclUtil.umlapi.OCLTYPE) {
            return false;
         } else if (!this.type.isCollection) {
            return this.type == a.type;
         } else {
            return this.type == a.type && this.celement.sameTypeAs(a.celement);
         }
      } else if (this.isTuple ^ a.isTuple) {
         return false;
      } else if (this.partCount != a.partCount) {
         return false;
      } else {
         for(int i = 0; i < this.partCount; ++i) {
            boolean found = false;

            for(int j = 0; j < this.partCount; ++j) {
               if (this.partNames[j].equals(a.partNames[i])) {
                  found = true;
                  if (!this.partTypes[j].sameTypeAs(a.partTypes[i])) {
                     return false;
                  }
                  break;
               }
            }

            if (!found) {
               return false;
            }
         }

         return true;
      }
   }

   public SearchResult findFeature(String packagecontext, String name, Vector actualParams, int matchKind) throws ExceptionAny {
      return this.findFeature(packagecontext, name, actualParams, matchKind, (String)null, (Vector)null);
   }

   public SearchResult findFeature(String packagecontext, String name, Vector actualParams, int matchKind, String qualifier, Vector qualifiers) throws ExceptionAny {
      if (!name.startsWith("^") && !name.startsWith("^^")) {
         SearchResult searchResult1;
         if (this.isTuple && qualifier == null && qualifiers == null) {
            for(int i = 0; i < this.partCount; ++i) {
               if (name.equals("." + this.partNames[i]) && (actualParams == null || this.isMessage && actualParams.size() == 0 && (i <= 2 || i == this.partCount - 1))) {
                  searchResult1 = new SearchResult(this.partTypes[i], 16, this.type, (OclLetItem)null);
                  return searchResult1;
               }
            }
         }

         if (!name.startsWith("->") && !name.startsWith(".") && !name.startsWith("~")) {
            if (actualParams == null && matchKind == 2) {
               OclClassInfo rez = this.type.umlapi.getClassInfoByName(packagecontext, name);
               if (rez != null) {
                  return new SearchResult(new OclType(this.type.umlapi.OCLTYPE, rez), 4);
               }
            }

            return this.type.findVariableFeature(packagecontext, name, actualParams, qualifier, qualifiers);
         } else {
            SearchResult searchResult;
            OclType result;
            if (this.type.isCollection) {
               if (!name.startsWith("->")) {
                  if (name.startsWith("~")) {
                     result = this.type.findCollectionFeature(packagecontext, name.substring(1), actualParams, this.element, this.celement).type;
                     return new SearchResult(result, 2, this.type, (OclLetItem)null);
                  } else {
                     String nname = (this.celement.type.isCollection ? "->" : ".") + name.substring(1);
                     searchResult = this.celement.findFeature(packagecontext, nname, actualParams, matchKind, qualifier, qualifiers);
                     result = new OclType(this.type, searchResult.type, true);
                     SearchResult rez = new SearchResult(result, searchResult.foundType | 64, searchResult.foundOwner, searchResult.foundLetItem);
                     rez.asend = searchResult.asend;
                     rez.operation = searchResult.operation;
                     rez.attr = searchResult.attr;
                     return rez;
                  }
               } else {
                  searchResult = this.type.findCollectionFeature(packagecontext, name.substring(2), actualParams, this.element, this.celement);
                  if (name.equals("->collect") || name.equals("->flatten")) {
                     searchResult.type.flatten();
                  }

                  return searchResult;
               }
            } else if (name.startsWith("->")) {
               if (this.is01) {
                  result = this.type.umlapi.SET.findCollectionFeature(packagecontext, name.substring(2), actualParams, this.type, this).type;
                  result.isEmpty01 = true;
                  return new SearchResult(result, 2, this.type.umlapi.SET, (OclLetItem)null);
               } else {
                  throw new ExceptionAny("collection feature call for simple type");
               }
            } else if (this.type == OclUtil.umlapi.OCLTYPE) {
               try {
                  searchResult = this.type.findFeature(name.substring(1), actualParams, matchKind, qualifier, qualifiers, this.element, this.isCasted, false);
                  return searchResult;
               } catch (ExceptionAny var12) {
                  try {
                     searchResult = this.element.findFeature(name.substring(1), actualParams, matchKind, qualifier, qualifiers, this.element, this.isCasted, true);
                     searchResult.foundType |= 256;
                     return searchResult;
                  } catch (ExceptionAny var11) {
                     throw new ExceptionAny(var12.getMessage() + " or " + var11.getMessage());
                  }
               }
            } else {
               searchResult = this.type.findFeature(name.substring(1), actualParams, matchKind, qualifier, qualifiers, this.element, this.isCasted);
               return searchResult;
            }
         }
      } else {
         return this.findMessage(packagecontext, name, actualParams, matchKind, qualifier, qualifiers);
      }
   }

   private SearchResult findMessage(String packagecontext, String name, Vector actualParams, int matchKind, String qualifier, Vector qualifiers) throws ExceptionAny {
      int preflen = name.startsWith("^^") ? 2 : 1;
      SearchResult result = this.type.findFeature(name.substring(preflen), actualParams, matchKind, qualifier, qualifiers, this.element, this.isCasted);
      if (result.operation == null) {
         throw new ExceptionAny("The right side property has to be an operation of " + this.type.toString());
      } else {
         OclType bool = new OclType(OclUtil.umlapi.BOOLEAN);
         if (preflen == 1) {
            result.type = bool;
         } else {
            ArrayList names = new ArrayList();
            ArrayList types = new ArrayList();
            names.add("hasReturned");
            types.add(bool);
            names.add("isSignalSent");
            types.add(bool);
            names.add("isOperationCall");
            types.add(bool);
            OclType rettype = new OclType(OclUtil.umlapi.UNDEFINED);
            Enumeration en = result.operation.getParameterList();

            while(en.hasMoreElements()) {
               Parameter param = (Parameter)en.nextElement();
               OclType type = new OclType(OclUtil.umlapi.getClassInfoByClassifier(param.getType()));
               if (param.getKind() == 3) {
                  rettype = type;
               } else {
                  names.add(param.getName());
                  types.add(type);
               }
            }

            names.add("result");
            types.add(rettype);
            result.type = new OclType(names.size(), (String[])names.toArray(new String[0]), (OclType[])types.toArray(new OclType[0]));
            result.type.isMessage = true;
            result.type.messageOperation = result.operation;
            result.type = new OclType(OclUtil.umlapi.SEQUENCE, result.type, false);
         }

         return result;
      }
   }

   public static OclType getType(Object obj) throws ExceptionAny {
      Class cls = obj.getClass();
      if (cls == (OclType.class)) {
         return (OclType)obj;
      } else {
         OclClassInfo type;
         if (!(obj instanceof OclAny)) {
            try {
               type = OclUtil.umlapi.getClassInfoByJavaClass(cls);
               return new OclType(type);
            } catch (ExceptionAny var9) {
               try {
                  Enumeration en = ((ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object)obj).getClassifierList();
                  if (!en.hasMoreElements()) {
                     throw new ExceptionAny("malformed model, object does not instantiate any classifier");
                  } else {
                     Classifier csf = (Classifier)en.nextElement();
                     OclClassInfo ci = OclUtil.umlapi.getClassInfoByClassifier(csf);
                     return new OclType(ci);
                  }
               } catch (ExceptionAny var8) {
                  throw new ExceptionAny("[internal] classifier from the model for " + cls.getName() + " not found");
               }
            }
         } else if (cls == (OclBoolean.class)) {
            return new OclType(OclUtil.umlapi.BOOLEAN);
         } else if (cls == (OclString.class)) {
            return new OclType(OclUtil.umlapi.STRING);
         } else if (cls == (OclReal.class)) {
            return new OclType(OclUtil.umlapi.REAL);
         } else if (cls == (OclInteger.class)) {
            return new OclType(OclUtil.umlapi.INTEGER);
         } else if (cls == (Undefined.class)) {
            return new OclType(OclUtil.umlapi.UNDEFINED);
         } else if (cls == (OclAny.class)) {
            return new OclType(OclUtil.umlapi.OCLANY);
         } else if (obj instanceof OclCollection) {
            type = null;
            if (cls == (OclSet.class)) {
               type = OclUtil.umlapi.SET;
            } else if (cls == (OclBag.class)) {
               type = OclUtil.umlapi.BAG;
            } else if (cls == (OclSequence.class)) {
               type = OclUtil.umlapi.SEQUENCE;
            } else {
               System.err.println("[internal] type " + cls.getName() + " with supertype OclCollection ?!");
            }

            Collection col = ((OclCollection)obj).getCollection();
            if (col.size() == 0) {
               return new OclType(type, OclUtil.umlapi.UNDEFINED);
            } else {
               Iterator it = col.iterator();

               OclType elem;
               OclType elem2;
               for(elem = getType(it.next()); it.hasNext(); elem = commonType(elem, elem2)) {
                  elem2 = getType(it.next());
               }

               return new OclType(type, elem, false);
            }
         } else if (!(obj instanceof OclTuple)) {
            throw new ExceptionAny("[fatal] OclAny.oclType with unknown OclAny descendant " + obj.getClass().getName());
         } else {
            OclTuple tup = (OclTuple)obj;
            int partCount = tup.getPartCount();
            String[] partNames = tup.getPartNames();
            Object[] partValues = tup.getPartValues();
            OclType[] partTypes = new OclType[partCount];

            for(int i = 0; i < partCount; ++i) {
               partTypes[i] = getType(partValues[i]);
            }

            return new OclType(partCount, partNames, partTypes);
         }
      }
   }

   public static Method getMethod(String name) throws ExceptionAny {
      Method meth = null;
      Class[] params = new Class[0];

      try {
         meth = (OclType.class).getMethod(name, params);
      } catch (NoSuchMethodException var4) {
      }

      if (meth == null) {
         throw new ExceptionAny("[fatal] method " + name + " in OclType not found");
      } else {
         return meth;
      }
   }

   public String name() {
      return this.type.name;
   }

   public OclSet allInstances() throws ExceptionAny {
      Vector instances;
      if (this.type.umlapi.isMetamodel) {
         OclUserModel um = OclUtil.usermodel;
         if (um == null) {
            throw new ExceptionAny("allInstances called with no user model loaded");
         }

         instances = um.getAllInstances(this.element.getJavaClass());
      } else {
         instances = OclUserModel.getObjectInstances((this.element == null ? this.type : this.element).classifier, false);
      }

      OclSet rez = new OclSet();
      Iterator it = instances.iterator();

      while(it.hasNext()) {
         OclUserElement inst = (OclUserElement)it.next();
         rez.directAdd(inst.getElement());
      }

      return rez;
   }

   private OclSet filterIntegrationClasses(Set s) {
      Iterator it = s.iterator();
      OclSet rez = new OclSet();

      while(it.hasNext()) {
         try {
            OclClassInfo ci = OclUtil.umlapi.getClassInfoByClassifier((Classifier)it.next());
            boolean bRemove = false;
            if (ci.classType == 2 && ("NumericExpr".equals(ci.name) || "AnyExpr".equals(ci.name))) {
               bRemove = true;
            }

            if (!bRemove) {
               rez.getCollection().add(new OclType(OclUtil.umlapi.OCLTYPE, ci));
            }
         } catch (ExceptionAny var6) {
         }
      }

      return rez;
   }

   public OclSet allSupertypes() {
      Set set = this.element.classifier.allParents();
      set.add(OclUtil.umlapi.OCLANY.classifier);
      return this.filterIntegrationClasses(set);
   }

   public OclSet supertypes() {
      Set set = this.element.classifier.parent();
      if (set.size() == 0) {
         set.add(OclUtil.umlapi.OCLANY.classifier);
      }

      return this.filterIntegrationClasses(set);
   }

   public OclSet attributes() {
      Iterator it = this.element.classifier.allAttributes().iterator();
      OclSet rez = new OclSet();

      while(it.hasNext()) {
         String str = ((Attribute)it.next()).getName();
         rez.getCollection().add(new OclString(str));
      }

      return rez;
   }

   public OclSet operations() {
      Iterator it = this.element.classifier.allOperations().iterator();
      OclSet rez = new OclSet();

      while(it.hasNext()) {
         String str = ((Operation)it.next()).getName();
         rez.getCollection().add(new OclString(str));
      }

      return rez;
   }

   public OclSet signatures() {
      Iterator it = this.element.classifier.allOperations().iterator();
      OclSet rez = new OclSet();

      while(it.hasNext()) {
         Operation op = (Operation)it.next();
         rez.getCollection().add(new OclString(this.element.name + "." + OclUtil.operationAsString(op)));
      }

      return rez;
   }

   public OclSet associationEnds() {
      Iterator it = this.element.classifier.allOppositeAssociationEnds().iterator();

      OclSet rez;
      String str;
      for(rez = new OclSet(); it.hasNext(); rez.getCollection().add(new OclString(str))) {
         AssociationEnd ae = (AssociationEnd)it.next();
         str = ae.getName();
         if (str == null || str.equals("")) {
            str = ae.getParticipant().getName();
         }
      }

      return rez;
   }

   public boolean equals(Object obj) {
      return !(obj instanceof OclType) ? false : this.sameTypeAs((OclType)obj);
   }

   public OclBoolean equal(Object n) {
      return new OclBoolean(this.sameTypeAs((OclType)n));
   }

   public OclBoolean notequal(Object n) {
      return new OclBoolean(!this.sameTypeAs((OclType)n));
   }

   public boolean isTuple() {
      return this.isTuple;
   }

   public boolean isPrimitiveType() {
      return this.type.isPrimitiveType();
   }

   public int hashCode() {
      return this.type.hashCode();
   }
}
