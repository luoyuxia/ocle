package ro.ubbcluj.lci.utils;

import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValueImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.core.*;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;
import ro.ubbcluj.lci.uml.foundation.dataTypes.ExpressionImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRangeImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.StereotypeImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinition;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinitionImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.ModelImpl;

public class ModelFactory {
   public static Model currentModel;
   private static DataTypeSystem dataTypeSystem;
   private static Map dataValues = new HashMap();
   protected static ArrayList listeners = new ArrayList();
   protected static InstanceCreator creator = new InstanceCreator();

   protected ModelFactory() {
   }

   public static Model createNewModel() {
      Model newModel = new ModelImpl();
      newModel.setAbstract(false);
      newModel.setLeaf(true);
      newModel.setName("NewModel");
      createElementOwnership((Namespace)null, newModel);
      newModel.setRoot(true);
      currentModel = newModel;
      newModel.setOwnerModel(currentModel);
      attachPredefinedDataTypes();
      attachStereotypes();
      return newModel;
   }

   public static void clearStaticReferences() {
      dataValues.clear();
      currentModel = null;
   }

   public static void attachPredefinedDataTypes() {
      dataTypeSystem = new DataTypeSystem();
      ArrayList predefs = new ArrayList();
      ArrayList existent = allInstances(currentModel, DataTypeImpl.class);
      predefs.add("Boolean");
      predefs.add("Integer");
      predefs.add("UnlimitedInteger");
      predefs.add("Real");
      predefs.add("String");
      predefs.add("undefined");
      Iterator it1 = existent.iterator();

      while(it1.hasNext()) {
         Object tmp1 = it1.next();
         if (tmp1 instanceof DataType) {
            Classifier dt = (Classifier)tmp1;
            String id = dataTypeSystem.getTypeId(dt);
            if (id != null) {
               predefs.remove(id);
               dataTypeSystem.bind(id, dt);
            }
         }
      }

      Iterator it2 = predefs.iterator();

      while(it2.hasNext()) {
         DataType dt = (DataType)createNewElement(currentModel, "DataType");
         dt.setName((String)it2.next());
         dataTypeSystem.bind(dt.getName(), dt);
      }

   }

   public static void attachStereotypes() {
      createNewStereotype("Classifier", "enumeration");
      createNewStereotype("Permission", "access");
      createNewStereotype("Package", "appliedProfile");
      createNewStereotype("AssociationEnd", "association");
      createNewStereotype("Class", "auxiliary");
      createNewStereotype("Flow", "become");
      createNewStereotype("Usage", "call");
      createNewStereotype("Flow", "copy");
      createNewStereotype("BehavioralFeature", "create");
      createNewStereotype("CallEvent", "create");
      createNewStereotype("Usage", "create");
      createNewStereotype("Abstraction", "derive");
      createNewStereotype("BehavioralFeature", "destroy");
      createNewStereotype("CallEvent", "destroy");
      createNewStereotype("Abstraction", "document");
      createNewStereotype("Abstraction", "executable");
      createNewStereotype("Package", "facade");
      createNewStereotype("Abstraction", "file");
      createNewStereotype("Class", "focus");
      createNewStereotype("Package", "framework");
      createNewStereotype("Permission", "friend");
      createNewStereotype("AssociationEnd", "global");
      createNewStereotype("Class", "implementation");
      createNewStereotype("Generalization", "implementation");
      createNewStereotype("Permission", "import");
      createNewStereotype("Usage", "instantiate");
      createNewStereotype("Constraint", "invariant");
      createNewStereotype("Abstraction", "library");
      createNewStereotype("AssociationEnd", "local");
      createNewStereotype("Class", "metaclass");
      createNewStereotype("Package", "metamodel");
      createNewStereotype("Package", "modelLibrary");
      createNewStereotype("AssociationEnd", "parameter");
      createNewStereotype("Constraint", "postcondition");
      createNewStereotype("Class", "powertype");
      createNewStereotype("Constraint", "precondition");
      createNewStereotype("Classifier", "process");
      createNewStereotype("Package", "profile");
      createNewStereotype("Abstraction", "realize");
      createNewStereotype("Abstraction", "refine");
      createNewStereotype("Comment", "requirement");
      createNewStereotype("Comment", "responsibility");
      createNewStereotype("AssociationEnd", "self");
      createNewStereotype("Usage", "send");
      createNewStereotype("ObjectFlowState", "signalflow");
      createNewStereotype("Abstraction", "source");
      createNewStereotype("Constraint", "stateInvariant");
      createNewStereotype("Package", "stub");
      createNewStereotype("Package", "systemModel");
      createNewStereotype("Abstraction", "table");
      createNewStereotype("Classifier", "thread");
      createNewStereotype("Package", "topLevel");
      createNewStereotype("Abstraction", "trace");
      createNewStereotype("Class", "type");
      createNewStereotype("Classifier", "utility");
      createNewStereotype("Classifier", "DTDChoice");
      createNewStereotype("Classifier", "DTDSequence");
      createNewStereotype("Classifier", "DTDMixed");
      createNewStereotype("Classifier", "DTDAny");
      createNewStereotype("Classifier", "DTDAnyElement");
      createNewStereotype("Classifier", "DTDEmptyElement");
      createNewStereotype("Classifier", "DTDElement");
      createNewStereotype("Classifier", "DTDUndefinedElement");
      createNewStereotype("Attribute", "DTDUndefinedAttribute");
      createNewStereotype("Association", "DTDUndefinedAssociation");
      createNewStereotype("Association", "DTDInvalidAssociation");
      createNewStereotype("Attribute", "CDATA");
      createNewStereotype("Attribute", "ID");
      createNewStereotype("Attribute", "IDREF");
      createNewStereotype("Attribute", "IDREFS");
      createNewStereotype("Attribute", "NMTOKEN");
      createNewStereotype("Attribute", "NMTOKENS");
      createNewStereotype("Attribute", "ENTITY");
      createNewStereotype("Attribute", "ENTITIES");
      createNewStereotype("Attribute", "NOTATION");
      createNewStereotype("Attribute", "REQUIRED");
      createNewStereotype("Attribute", "IMPLIED");
      createNewStereotype("Attribute", "FIXED");
      createNewStereotype("AttributeLink", "XMLMissingAttribute");
      createNewStereotype("AttributeLink", "XMLUnexpectedAttribute");
      createNewStereotype("Object", "XMLMissingElement");
      createNewStereotype("Object", "XMLUnexpectedElement");
      createNewStereotype("Link", "XMLMissingLink");
      createNewStereotype("Link", "XMLUnexpectedLink");
      createNewStereotype("Link", "XMLInvalidLink");
      createNewStereotype("Collaboration", "XMLObjectsCollaboration");
      createNewTagDefinition("sequence", "Integer");
      createNewTagDefinition("choice", "Integer");
      createNewTagDefinition("mixed", "Integer");
      createNewTagDefinition("any", "Integer");
   }

   public static void createNewTagDefinition(String tagName, String tagType) {
      if (getTagDefinition(currentModel, tagName) == null) {
         TagDefinition tagDefinition = new TagDefinitionImpl();
         tagDefinition.setName(tagName);
         tagDefinition.setTagType(tagType);
         Multiplicity m = new MultiplicityImpl();
         MultiplicityRange mr = new MultiplicityRangeImpl();
         mr.setLower(1);
         mr.setUpper(new BigInteger("1"));
         m.addRange(mr);
         tagDefinition.setMultiplicity(m);
         createElementOwnership(currentModel, tagDefinition);
         tagDefinition.setOwnerModel(currentModel);
      }

   }

   public static Object createNewElement(Object context, String type) {
      Object result = null;
      Class clz = creator.getClass();

      try {
         Method method =  clz.getMethod("createNew" + type, new Class[] {
                 ((Object) (new Object[0])).getClass()
         });
       //  Method method = clz.getMethod("createNew" + type, new Class[]{Object.class});
       //  result = method.invoke(creator, new Object[]{context});
         result = method.invoke(creator, new Object[] {
                 new Object[] {
                         context
                 }
         });
      } catch (NoSuchMethodException var5) {
         System.err.println("Not found method for creating: " + type);
      } catch (SecurityException var6) {
         System.err.println("Not allowed invoking method for creating: " + type);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      if (result != null) {
         ((Element)result).setOwnerModel(currentModel);
         fireModelEvent(result, context, 10);
         fireModelEvent(result, context, 0);
         if (result instanceof Attribute) {
            fireModelEvent(result, context, 40);
         }

         return result;
      } else {
         return new ErrorMessage("Illegal element.");
      }
   }

   public static Object createNewRelation(ModelElement client, ModelElement supplier, ModelElement baseType, String type) {
      Object result = null;
      Object context = null;
      Stereotype toApply = null;
      if (type.equals("Realization")) {
         type = "Abstraction";
         toApply = getStereotype(currentModel, "realize", "Abstraction");
      }

      Class clz = creator.getClass();

      try {
         Method method = clz.getMethod("createNew" + type, new Class[] {
                 ((Object) (new Object[0])).getClass()
         });
         result = method.invoke(creator, new Object[] {
                 new Object[] {
                         client, supplier, baseType
                 }
         });
      } catch (NoSuchMethodException var12) {
         System.err.println("Not found method for creating: " + type);
      } catch (SecurityException var13) {
         System.err.println("Not allowed invoking method for creating: " + type);
      } catch (Exception var14) {
      }

      if (toApply != null) {
         ((ModelElement)result).addStereotype(toApply);
      }

      if (result == null) {
         return new ErrorMessage("Illegal Relationship: the creation of a new " + type + " is not allowed in this context.");
      } else {
         ((Element)result).setOwnerModel(currentModel);
         context = ((ModelElement)result).directGetNamespace();
         fireModelEvent(result, context, 10);
         fireModelEvent(result, context, 0);
         if (result instanceof Generalization && context instanceof Classifier) {
            Collection attributes = new ArrayList();
            Iterator it = ((Classifier)context).getCollectionFeatureList().iterator();

            while(it.hasNext()) {
               Object tmpFeature = it.next();
               if (tmpFeature instanceof Attribute) {
                  attributes.add(tmpFeature);
               }
            }

            Set allAttributes = ((Classifier)context).allAttributes();
             it = allAttributes.iterator();

            while(it.hasNext()) {
               Object tmp = it.next();
               if (!attributes.contains(tmp)) {
                  fireModelEvent(tmp, context, 40);
               }
            }
         }

         return result;
      }
   }

   public static void removeElement(ModelElement element, ModelElement context) {
      Collection attributeLinkList = new ArrayList();
      if (element instanceof Attribute) {
         if (((Attribute)element).getCollectionAttributeLinkList() != null) {
            attributeLinkList.addAll(((Attribute)element).getCollectionAttributeLinkList());
         }

         element.remove();
         Iterator it = attributeLinkList.iterator();

         while(it.hasNext()) {
            fireModelEvent(it.next(), (Object)null, 30);
         }
      } else if (element instanceof Generalization) {
         if (context instanceof Classifier) {
            Collection oldAttributes = ((Classifier)context).allAttributes();
            element.remove();
            Collection newAttributes = ((Classifier)context).allAttributes();
            Iterator it = oldAttributes.iterator();

            while(it.hasNext()) {
               Object tmp = it.next();
               if (!newAttributes.contains(tmp)) {
                  attributeLinkList.addAll(((Attribute)tmp).getCollectionAttributeLinkList());
               }
            }

            it = attributeLinkList.iterator();

            while(it.hasNext()) {
               fireModelEvent(it.next(), (Object)null, 30);
            }
         }
      } else {
         element.remove();
      }

      fireModelEvent(context, (Object)null, 0);
   }

   public static void setAttribute(Object target, String attrName, Object newValue) {
      if (attrName.equals("Visibility") && !(target instanceof Feature) && !(target instanceof AssociationEnd)) {
         ElementOwnership namespace = ((ModelElement)target).getNamespace();
         if (namespace != null) {
            namespace.setVisibility(((Integer)newValue).intValue());
         }

      } else {
         Object oldValueAsType = null;
         Method getMethod = null;

         try {
            getMethod = target.getClass().getMethod("get" + attrName, new Class[0]);
         } catch (NoSuchMethodException var15) {
            try {
               getMethod = target.getClass().getMethod("is" + attrName, new Class[0]);
               Method setMethod = target.getClass().getMethod("set" + attrName, new Class[]{Boolean.TYPE});
               setMethod.invoke(target, new Object[]{ newValue });
            } catch (NoSuchMethodException var11) {
            } catch (IllegalAccessException var12) {
            } catch (InvocationTargetException var13) {
            } catch (IllegalArgumentException var14) {
            }
         }

         if (getMethod != null) {
            try {
               oldValueAsType = getMethod.invoke(target, new Object[0]);
               Method setMethod;
               if (oldValueAsType == null) {
                  setMethod = target.getClass().getMethod("set" + attrName, new Class[] { getMethod.getReturnType() } );
                  setMethod.invoke(target, new Object[] { newValue });
               } else if (oldValueAsType instanceof Integer) {
                  setMethod = target.getClass().getMethod("set" + attrName, new Class[] { Integer.TYPE });
                  setMethod.invoke(target, new Object[] { newValue });
               } else if (oldValueAsType instanceof String) {
                  setMethod = target.getClass().getMethod("set" + attrName, new Class[] { String.class });
                  setMethod.invoke(target, new Object[] { newValue });
                  if (!checkRules(target)) {
                     setMethod.invoke(target, new Object[] { oldValueAsType });
                  }
               } else if (oldValueAsType instanceof Expression) {
                  setMethod = target.getClass().getMethod("set" + attrName, new Class[] { Expression.class });
                  Expression newValueAsType = new ExpressionImpl();
                  newValueAsType.setBody(newValue.toString());
                  setMethod.invoke(target, new Object[] { newValueAsType });
               } else if (oldValueAsType instanceof Multiplicity) {
                  setMethod = target.getClass().getMethod("set" + attrName, new Class[]{ Multiplicity.class });
                  Multiplicity newValueAsType = parseMultiplicity(newValue.toString());
                  setMethod.invoke(target, new Object[] { newValueAsType });
               } else if (oldValueAsType instanceof Classifier) {
                  setMethod = target.getClass().getMethod("set" + attrName, new Class[]{ Classifier.class });
                  setMethod.invoke(target, new Object[] { newValue });
               } else if (oldValueAsType instanceof Instance) {
                  setMethod = target.getClass().getMethod("set" + attrName, new Class[]{ Instance.class });
                  setMethod.invoke(target, new Object[] { newValue });
               } else if (oldValueAsType instanceof TagDefinition) {
                  setMethod = target.getClass().getMethod("set" + attrName, new Class[]{  TagDefinition.class } );
                  setMethod.invoke(target, new Object[] { newValue });
               }
            } catch (NoSuchMethodException var7) {
            } catch (IllegalAccessException var8) {
            } catch (InvocationTargetException var9) {
            } catch (IllegalArgumentException var10) {
            }
         }

         if (attrName.equals("Type")) {
            if (target instanceof Attribute) {
               if (((Attribute)target).getAttributeLinkList() != null) {
                  Iterator it = ((Attribute)target).getCollectionAttributeLinkList().iterator();

                  while(it.hasNext()) {
                     AttributeLink tmpAttrLink = (AttributeLink)it.next();
                     if (tmpAttrLink != null && tmpAttrLink.getValue() != null) {
                        tmpAttrLink.getValue().removeClassifier((Classifier)oldValueAsType);
                        tmpAttrLink.getValue().addClassifier(((Attribute)target).getType());
                        fireModelEvent(tmpAttrLink.getValue(), (Object)null, 20);
                     }
                  }
               }
            } else if (target instanceof TaggedValue) {
               ((TaggedValue)target).setName(((TaggedValue)target).getType().getName());
            }
         }

         fireModelEvent(target, (Object)null, 20);
         fireModelEvent(target, (Object)null, 0);
      }
   }

   public static Object addValue(Element target, String name, Element value) {
      Method[] methods = target.getClass().getMethods();
      Method addMethod = null;

      for(int i = 0; i < methods.length; ++i) {
         if (methods[i].getName().equals("add" + name)) {
            addMethod = methods[i];
         }
      }

      if (addMethod != null) {
         try {
            addMethod.invoke(target, new Object[] {value});
         } catch (Exception var6) {
            return new ErrorMessage("Attribute " + name + " can not be set to " + value + ".");
         }
      }

      fireModelEvent(target, (Object)null, 20);
      fireModelEvent(target, (Object)null, 0);
      return target;
   }

   public static Multiplicity parseMultiplicity(String str) {
      Multiplicity mul = new MultiplicityImpl();
      MultiplicityRange mr = new MultiplicityRangeImpl();
      mul.addRange(mr);
      int idx = str.indexOf("..");
      if (idx < 0) {
         mr.setLower(Integer.parseInt(str));
         mr.setUpper(new BigInteger(str));
      } else {
         String low = str.substring(0, idx);
         String high = str.substring(idx + 2);
         mr.setLower(Integer.parseInt(low));
         if (high.equals("*") || high.equals("n")) {
            high = "-1";
         }

         mr.setUpper(new BigInteger(high));
      }

      return mul;
   }

   public static boolean checkRules(Object target) {
      boolean checked = true;
      if (target instanceof Attribute) {
         Attribute at = (Attribute)target;
         if (at.getOwner() != null) {
            Iterator featuresIt = at.getOwner().getCollectionFeatureList().iterator();
            LinkedList attributes = new LinkedList();

            while(featuresIt.hasNext()) {
               Object feature = featuresIt.next();
               if (feature instanceof Attribute) {
                  attributes.add(feature);
               }
            }

            return checkNames(at, attributes);
         }

         if (at.getAssociationEnd() != null) {
            return checkNames(at, at.getAssociationEnd().getCollectionQualifierList());
         }
      } else if (target instanceof Parameter) {
         Parameter param = (Parameter)target;
         if (param.getBehavioralFeature() != null) {
            return checkNames(param, param.getBehavioralFeature().getCollectionParameterList());
         }
      } else {
         ModelElement me = (ModelElement)target;
         Namespace ns = me.directGetNamespace();
         if (ns != null) {
            return checkNames(me, ns.directGetCollectionOwnedElementList());
         }
      }

      return checked;
   }

   private static boolean checkNames(ModelElement me, Collection col) {
      Iterator it = col.iterator();

      Object tmp;
      do {
         if (!it.hasNext()) {
            return true;
         }

         tmp = it.next();
      } while(!tmp.getClass().equals(me.getClass()) || !me.getName().equals(((ModelElement)tmp).getName()) || tmp == me);

      return false;
   }

   public static boolean hasStereotype(ModelElement modelElement, String stereotypeName, String stereotypeBaseClass) {
      boolean hasStereotype = false;
      Enumeration enum = modelElement.getStereotypeList();

      while(enum.hasMoreElements()) {
         Stereotype s = (Stereotype)enum.nextElement();
         if (s.getName().equals(stereotypeName) && s.getCollectionBaseClassList().contains(stereotypeBaseClass)) {
            hasStereotype = true;
         }
      }

      return hasStereotype;
   }

   public static boolean existStereotypeInModel(Model m, String sName, String sBaseClass) {
      return getStereotype(m, sName, sBaseClass) != null;
   }

   public static Stereotype getStereotype(Namespace m, String stereotypeName, String baseClass) {
      Collection elems = m.directGetCollectionOwnedElementList();
      Iterator it = elems.iterator();

      while(it.hasNext()) {
         Object o = it.next();
         if (o instanceof Stereotype) {
            Stereotype s = (Stereotype)o;
            if (s.getName().equals(stereotypeName)) {
               if (baseClass == null) {
                  return s;
               }

               if (s.getCollectionBaseClassList().contains(baseClass)) {
                  return s;
               }
            }
         }
      }

      return null;
   }

   public static TagDefinition getTagDefinition(Namespace m, String tagDefinitionName) {
      Collection elems = m.directGetCollectionOwnedElementList();
      Iterator it = elems.iterator();

      while(it.hasNext()) {
         Object o = it.next();
         if (o instanceof TagDefinition) {
            TagDefinition td = (TagDefinition)o;
            if (td.getName().equals(tagDefinitionName)) {
               return td;
            }
         }
      }

      return null;
   }

   public static Map getDTDStereotypes() {
      if (currentModel == null) {
         return null;
      } else {
         String[] dtdStereotypesNames = new String[]{"DTDAnyElement", "DTDEmptyElement", "DTDElement", "DTDChoice", "DTDSequence", "DTDMixed", "DTDAny", "DTDUndefinedElement", "DTDUndefinedAttribute", "DTDUndefinedAssociation", "DTDInvalidAssociation", "XMLMissingElement", "XMLMissingAttribute", "XMLMissingLink", "XMLUnexpectedElement", "XMLUnexpectedAttribute", "XMLUnexpectedLink", "XMLInvalidLink", "CDATA", "ID", "IDREF", "IDREFS", "NMTOKEN", "NMTOKENS", "ENTITY", "ENTITIES", "NOTATION", "REQUIRED", "IMPLIED", "FIXED", "XMLObjectsCollaboration"};
         LinkedList dtdStereotypesNamesList = new LinkedList();

         for(int i = 0; i < dtdStereotypesNames.length; ++i) {
            dtdStereotypesNamesList.add(dtdStereotypesNames[i]);
         }

         Collection ownedElems = currentModel.directGetCollectionOwnedElementList();
         Iterator ownedElemsIt = ownedElems.iterator();
         HashMap stereotypeMap = new HashMap();

         while(ownedElemsIt.hasNext()) {
            Object ownedElem = ownedElemsIt.next();
            if (ownedElem instanceof Stereotype) {
               Stereotype stereotype = (Stereotype)ownedElem;
               if (dtdStereotypesNamesList.contains(stereotype.getName())) {
                  stereotypeMap.put(stereotype.getName(), stereotype);
               }
            }
         }

         return stereotypeMap;
      }
   }

   public static Map getDTDTagDefinitions() {
      if (currentModel == null) {
         return null;
      } else {
         String[] dtdTagDefinitionsNames = new String[]{"sequence", "choice", "mixed", "any"};
         LinkedList dtdTagDefinitionsNamesList = new LinkedList();

         for(int i = 0; i < dtdTagDefinitionsNames.length; ++i) {
            dtdTagDefinitionsNamesList.add(dtdTagDefinitionsNames[i]);
         }

         Collection ownedElems = currentModel.directGetCollectionOwnedElementList();
         Iterator ownedElemsIt = ownedElems.iterator();
         HashMap tagDefinitionsMap = new HashMap();

         while(ownedElemsIt.hasNext()) {
            Object ownedElem = ownedElemsIt.next();
            if (ownedElem instanceof TagDefinition) {
               TagDefinition tagDefinition = (TagDefinition)ownedElem;
               if (dtdTagDefinitionsNamesList.contains(tagDefinition.getName())) {
                  tagDefinitionsMap.put(tagDefinition.getName(), tagDefinition);
               }
            }
         }

         return tagDefinitionsMap;
      }
   }

   public static String getDefaultDataValueAsString(DataType dt) {
      if (!dt.getName().equals("int") && !dt.getName().equals("Integer")) {
         if (!dt.getName().equals("float") && !dt.getName().equals("Float") && !dt.getName().equals("double") && !dt.getName().equals("Double") && !dt.getName().equals("real") && !dt.getName().equals("Real")) {
            if (!dt.getName().equals("boolean") && !dt.getName().equals("Boolean")) {
               return !dt.getName().equals("string") && !dt.getName().equals("String") && !dt.getName().equals("char") && !dt.getName().equals("Char") ? "<undefined>" : "<undefined>";
            } else {
               return "false";
            }
         } else {
            return "0.0";
         }
      } else {
         return "0";
      }
   }

   public static ElementOwnership createElementOwnership(Namespace namespace, ModelElement ownedElement) {
      ElementOwnership eo = new ElementOwnershipImpl();
      eo.setNamespace(namespace);
      eo.setOwnedElement(ownedElement);
      eo.setSpecification(false);
      eo.setVisibility(3);
      eo.setOwnerModel(currentModel);
      return eo;
   }

   public static String getDefaultName(Namespace namespace, ModelElement modelElement, String prefix) {
      if (namespace == null) {
         modelElement.setName(prefix);
      }

      Collection all = namespace.directGetCollectionOwnedElementList();
      return getDefaultName((Collection)all, modelElement, prefix);
   }

   public static String getDefaultName(Collection elements, ModelElement modelElement, String prefix) {
      Iterator iter = elements.iterator();
      ArrayList names = new ArrayList();

      String tmpName;
      while(iter.hasNext()) {
         Object tmp = iter.next();
         if (modelElement.getClass().equals(tmp.getClass())) {
            tmpName = ((ModelElement)tmp).getName();
            if (tmpName == null) {
               tmpName = "";
            }

            if (tmpName.startsWith(prefix)) {
               names.add(tmpName);
            }
         }
      }

      int sufix = 0;

      for(tmpName = prefix; names.contains(tmpName); tmpName = prefix + Integer.toString(sufix)) {
         ++sufix;
      }

      return tmpName;
   }

   public static DataType getUndefinedType() {
      return dataTypeSystem.lookup("undefined");
   }

   public static DataTypeSystem getDataTypeSystem() {
      return dataTypeSystem;
   }

   static void moveContent(Element destination, Element source, String operation, String parameter) {
      try {
         Enumeration enum = (Enumeration)source.getClass().getMethod("get" + operation + "List", new Class[0]).invoke(source, new Object[0]);
         ArrayList temp = new ArrayList();

         while(enum.hasMoreElements()) {
            temp.add(enum.nextElement());
         }

         Iterator it = temp.iterator();

         while(it.hasNext()) {
            Object elem = it.next();
            String name = elem.getClass().getName();
            name = name.substring(0, name.lastIndexOf(".") + 1) + parameter;
            Class clazz = Class.forName(name);
            destination.getClass().getMethod("add" + operation, new Class[]{clazz}).invoke(destination, new Object[] { elem });
         }
      } catch (Exception var10) {
         System.err.println(operation + "(" + parameter + ")");
      }

   }

   private static Stereotype createNewStereotype(String base, String name) {
      if (existStereotypeInModel(currentModel, name, base)) {
         return null;
      } else {
         if (existStereotypeInModel(currentModel, name, (String)null)) {
            Collection elems = currentModel.directGetCollectionOwnedElementList();
            Iterator it = elems.iterator();

            while(it.hasNext()) {
               Object o = it.next();
               if (o instanceof Stereotype) {
                  Stereotype s = (Stereotype)o;
                  if (s.getName().equals(name)) {
                     String baseClass = s.getBaseClass();
                     s.setBaseClass(baseClass != null ? baseClass + " " + base : base);
                     return s;
                  }
               }
            }
         }

         Stereotype newSt = new StereotypeImpl();
         newSt.setAbstract(false);
         newSt.setBaseClass(base);
         newSt.setLeaf(false);
         newSt.setName(name);
         createElementOwnership(currentModel, newSt);
         newSt.setRoot(false);
         newSt.setOwnerModel(currentModel);
         return newSt;
      }
   }

   public static ArrayList allInstances(Namespace namespace, Class clazz) {
      ArrayList result = new ArrayList();
      Collection owned = namespace.directGetCollectionOwnedElementList();
      Iterator it = owned.iterator();

      while(it.hasNext()) {
         Object tmp = it.next();
         if (clazz.isAssignableFrom(tmp.getClass())) {
            result.add(tmp);
         }

         if (tmp instanceof Namespace) {
            result.addAll(allInstances((Namespace)tmp, clazz));
         }
      }

      return result;
   }

   public static void addModelListener(ModelListener listener) {
      if (!listeners.contains(listener)) {
         listeners.add(listener);
      }
   }

   public static void removeModelListener(ModelListener listener) {
      listeners.remove(listener);
   }

   public static void removeAllListeners() {
      listeners.clear();
   }

   public static void fireModelEvent(Object subject, Object context, int operation) {
      ModelEvent event = new ModelEvent(new ModelFactory(), subject, context, operation);
      Iterator it = listeners.iterator();

      while(it.hasNext()) {
         ModelListener listener = (ModelListener)it.next();
         listener.modelChanged(event);
      }

   }

   public static InstanceCreator getCreator() {
      return creator;
   }

   public static DataValue createNewDataValue(String value, Classifier valueType) {
      ModelFactory.DataValueKey valueKey = new ModelFactory.DataValueKey(value, valueType);
      DataValue dataValue = (DataValue)dataValues.get(valueKey);
      if (dataValue == null) {
         dataValue = new DataValueImpl();
         ((DataValue)dataValue).addClassifier(valueType);
         createElementOwnership(currentModel, (ModelElement)dataValue);
         ((DataValue)dataValue).setOwnerModel(currentModel);
         ((DataValue)dataValue).setName(value);
         dataValues.put(valueKey, dataValue);
         fireModelEvent(dataValue, currentModel, 10);
      }

      return (DataValue)dataValue;
   }

   public static void removeDataValue(String value, Classifier valueType) {
      ModelFactory.DataValueKey valueKey = new ModelFactory.DataValueKey(value, valueType);
      dataValues.remove(valueKey);
   }

   private static class DataValueKey {
      private String value;
      private Classifier valueType;

      public DataValueKey(String value, Classifier valueType) {
         this.value = value;
         this.valueType = valueType;
      }

      public String getValue() {
         return this.value;
      }

      public Classifier getValueType() {
         return this.valueType;
      }

      public boolean equals(Object obj) {
         return obj instanceof ModelFactory.DataValueKey && this.value != null && this.value.equals(((ModelFactory.DataValueKey)obj).getValue()) && this.valueType != null && this.valueType == ((ModelFactory.DataValueKey)obj).getValueType();
      }

      public int hashCode() {
         return this.valueType != null ? this.value.hashCode() + this.valueType.hashCode() : this.value.hashCode();
      }
   }
}
