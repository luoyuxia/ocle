package ro.ubbcluj.lci.redtd;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEndImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ObjectImpl;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.ClassImpl;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;
import ro.ubbcluj.lci.uml.foundation.dataTypes.ExpressionImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.ModelUtilities;

public class ReDTDUMLModelBuilder implements ModelBuilder {
   private ReDTDModelCache modelCache = ReDTDModelCache.getInstance();

   public ReDTDUMLModelBuilder() {
      ModelFactory.addModelListener(this.modelCache);
   }

   public Classifier buildClass(String className) {
      Classifier cls = (Class)this.modelCache.getClass(className);
      if (cls != null) {
         return cls;
      } else {
         cls = new ClassImpl();
         cls.setAbstract(false);
         ((Class)cls).setActive(true);
         cls.setLeaf(false);
         cls.setRoot(false);
         cls.setName(className);
         ModelFactory.createElementOwnership(ModelFactory.currentModel, cls);
         cls.setOwnerModel(ModelFactory.currentModel);
         this.modelCache.putClass(className, cls);
         return cls;
      }
   }

   public Attribute buildAttribute(Classifier ownerClass, String attributeName, DataType attributeType, String initialValue, boolean isRequired, boolean isFixed) {
      Attribute attr = ModelUtilities.findAttribute(attributeName, ownerClass);
      if (attr != null) {
         return attr;
      } else {
         try {
            attr = (Attribute)ModelFactory.getCreator().createNewAttribute(new Object[]{ownerClass});
            attr.setName(attributeName);
            attr.setType(attributeType);
            attr.setVisibility(3);
            if (!isRequired) {
               attr.setMultiplicity(this.buildMultiplicity(7));
               attr.addStereotype((Stereotype)this.modelCache.getStereotype("IMPLIED"));
            } else {
               attr.addStereotype((Stereotype)this.modelCache.getStereotype("REQUIRED"));
            }

            if (initialValue != null) {
               Expression expression = new ExpressionImpl();
               expression.setBody(initialValue);
               attr.setInitialValue(expression);
            }

            if (isFixed) {
               attr.setChangeability(0);
               attr.addStereotype((Stereotype)this.modelCache.getStereotype("FIXED"));
            }

            attr.setOwnerModel(ModelFactory.currentModel);
            return attr;
         } catch (Exception var9) {
            var9.printStackTrace();
            return null;
         }
      }
   }

   public Association buildAssociation(Classifier fromClass, Classifier toClass, Multiplicity multiplicity, TaggedValue taggedValue) {
      try {
         Association assoc = (Association)ModelFactory.getCreator().createNewAssociation(new Object[]{fromClass, toClass, null});
         Iterator connectionIt = assoc.getCollectionConnectionList().iterator();

         while(connectionIt.hasNext()) {
            AssociationEnd assocEnd = (AssociationEnd)connectionIt.next();
            if (assocEnd.getParticipant() == toClass) {
               assocEnd.setMultiplicity(multiplicity);
               if (taggedValue != null) {
                  assocEnd.addTaggedValue(taggedValue);
               }
               break;
            }

            assocEnd.setChangeability(0);
         }

         assoc.setOwnerModel(ModelFactory.currentModel);
         return assoc;
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }

   public DataType buildDataType(String typeDef) {
      return ModelFactory.getDataTypeSystem().lookup("String");
   }

   public Enumeration buildEnumeration(Collection literals) {
      Iterator enumIterator = this.modelCache.getEnumerations();

      Enumeration enum;
      LinkedList literalStringList;
      do {
         if (!enumIterator.hasNext()) {
            try {
               enum = (Enumeration)ModelFactory.getCreator().createNewEnumeration(new Object[]{ModelFactory.currentModel});
               Iterator literalsIt = literals.iterator();

               while(literalsIt.hasNext()) {
                  String literal = (String)literalsIt.next();
                  EnumerationLiteral enumLiteral = (EnumerationLiteral)ModelFactory.getCreator().createNewEnumerationLiteral(new Object[]{enum});
                  enumLiteral.setName(literal);
               }

               enum.setOwnerModel(ModelFactory.currentModel);
               this.modelCache.addEnumeration(enum);
               return enum;
            } catch (Exception var7) {
               var7.printStackTrace();
               return null;
            }
         }

         enum = (Enumeration)enumIterator.next();
         literalStringList = new LinkedList();
         Iterator literalIterator = enum.getCollectionLiteralList().iterator();

         while(literalIterator.hasNext()) {
            literalStringList.add(((EnumerationLiteral)literalIterator.next()).getName());
         }
      } while(!literals.containsAll(literalStringList) || !literalStringList.containsAll(literals));

      return enum;
   }

   public Multiplicity buildMultiplicity(int dtdMultiplicity) {
      if (dtdMultiplicity == 6) {
         return ModelFactory.parseMultiplicity("1");
      } else if (dtdMultiplicity == 7) {
         return ModelFactory.parseMultiplicity("0..1");
      } else if (dtdMultiplicity == 8) {
         return ModelFactory.parseMultiplicity("0..*");
      } else {
         return dtdMultiplicity == 9 ? ModelFactory.parseMultiplicity("1..*") : null;
      }
   }

   public void attachStereotype(ModelElement stereotypedElement, String stereotypeName) {
      Stereotype stereotype = (Stereotype)this.modelCache.getStereotype(stereotypeName);
      if (stereotype.getBaseClass().equals("Classifier") && stereotypedElement instanceof Classifier) {
         stereotypedElement.addStereotype(stereotype);
      } else if (stereotype.getBaseClass().equals("Object") && stereotypedElement instanceof ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object) {
         stereotypedElement.addStereotype(stereotype);
      } else if (stereotype.getBaseClass().equals("Attribute") && stereotypedElement instanceof Attribute) {
         stereotypedElement.addStereotype(stereotype);
      } else if (stereotype.getBaseClass().equals("AttributeLink") && stereotypedElement instanceof AttributeLink) {
         stereotypedElement.addStereotype(stereotype);
      }

      if (stereotype.getBaseClass().equals("Association") && stereotypedElement instanceof Association) {
         stereotypedElement.addStereotype(stereotype);
      } else if (stereotype.getBaseClass().equals("Link") && stereotypedElement instanceof Link) {
         stereotypedElement.addStereotype(stereotype);
      }

   }

   public void detachStereotype(ModelElement stereotypedElement, String stereotypeName) {
      Stereotype stereotype = (Stereotype)this.modelCache.getStereotype(stereotypeName);
      if (stereotype != null) {
         stereotypedElement.removeStereotype(stereotype);
      }

   }

   public TaggedValue buildTaggedValue(String tagName, String tagValue) {
      try {
         TaggedValue taggedValue = (TaggedValue)ModelFactory.getCreator().createNewTaggedValue(new Object[]{this.modelCache.getTagDefinition(tagName)});
         taggedValue.setDataValue(tagValue);
         taggedValue.setOwnerModel(ModelFactory.currentModel);
         return taggedValue;
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public void buildAnyContent() {
      Class anyClass = (Class)this.modelCache.getClass("ANY");
      if (anyClass != null) {
         this.attachStereotype(anyClass, "DTDAny");
         int anyNo = 1;
         Class pcdataClass = (Class)this.buildClass("#PCDATA");
         this.buildAssociation(anyClass, pcdataClass, this.buildMultiplicity(6), this.buildTaggedValue("any", String.valueOf(anyNo)));
         Iterator classesIt = this.modelCache.getClasses().iterator();

         while(true) {
            Class cls;
            Set stereotypes;
            do {
               do {
                  if (!classesIt.hasNext()) {
                     return;
                  }

                  ++anyNo;
                  cls = (Class)classesIt.next();
                  stereotypes = cls.getCollectionStereotypeList();
               } while(stereotypes == null);
            } while(!stereotypes.contains(this.modelCache.getStereotype("DTDElement")) && !stereotypes.contains(this.modelCache.getStereotype("DTDAnyElement")) && !stereotypes.contains(this.modelCache.getStereotype("DTDEmptyElement")));

            this.buildAssociation(anyClass, cls, this.buildMultiplicity(6), this.buildTaggedValue("any", String.valueOf(anyNo)));
         }
      }
   }

   public Instance buildObject(Classifier cls, Collaboration collaboration) {
      Instance obj = new ObjectImpl();
      obj.addClassifier(cls);
      int instanceNo = cls.getCollectionInstanceList().size();
      obj.setName(cls.getName() + "_" + instanceNo);
      ModelFactory.createElementOwnership(collaboration, obj);
      Stereotype stereotype = (Stereotype)this.modelCache.getStereotype("DTDUndefinedElement");
      if (cls.getCollectionStereotypeList().contains(stereotype)) {
         this.attachStereotype(obj, "XMLUnexpectedElement");
      }

      obj.setOwnerModel(ModelFactory.currentModel);
      return obj;
   }

   public AttributeLink buildAttributeLink(Instance obj, String attrName, String attrValue) {
      try {
         Classifier classifier = (Classifier)obj.getClassifierList().nextElement();
         Attribute attr = ModelUtilities.findAttribute(attrName, classifier);
         if (attr == null) {
            if (!classifier.getName().equals("#PCDATA") || !attrName.equals("value")) {
               return null;
            }

            attr = this.buildAttribute(classifier, attrName, this.buildDataType("String"), "", true, false);
         }

         AttributeLink attrLink = ModelUtilities.findAttributeLink(attrName, obj);
         if (attrLink == null) {
            attrLink = (AttributeLink)ModelFactory.getCreator().createNewAttributeLink(new Object[]{attr});
            attrLink.setInstance(obj);
         }

         DataValue newDataValue = ModelFactory.createNewDataValue(attrValue, attr.getType());
         attrLink.setValue(newDataValue);
         Stereotype stereotype = (Stereotype)this.modelCache.getStereotype("DTDUndefinedAttribute");
         if (attr.getCollectionStereotypeList().contains(stereotype)) {
            this.attachStereotype(attrLink, "XMLUnexpectedAttribute");
         }

         attrLink.setOwnerModel(ModelFactory.currentModel);
         return attrLink;
      } catch (Exception var9) {
         return null;
      }
   }

   public Link buildLink(Instance fromObject, Instance toObject, Association association) {
      Classifier fromClass = (Classifier)fromObject.getClassifierList().nextElement();
      Link lnk = new LinkImpl();
      lnk.setAssociation(association);
      Iterator it = association.getCollectionConnectionList().iterator();

      while(it.hasNext()) {
         LinkEnd le = new LinkEndImpl();
         AssociationEnd ae = (AssociationEnd)it.next();
         le.setAssociationEnd(ae);
         le.setName(ae.getName());
         le.setLink(lnk);
         if (ae.getParticipant() == fromClass) {
            le.setInstance(fromObject);
         } else {
            le.setInstance(toObject);
         }
      }

      ModelFactory.createElementOwnership(fromObject.directGetNamespace(), lnk);
      lnk.setOwnerModel(ModelFactory.currentModel);
      lnk.setName("A_" + fromObject.getName() + "_" + toObject.getName());
      Stereotype stereotype = (Stereotype)this.modelCache.getStereotype("DTDUndefinedAssociation");
      if (association.getCollectionStereotypeList().contains(stereotype)) {
         this.attachStereotype(lnk, "XMLUnexpectedLink");
      }

      stereotype = (Stereotype)this.modelCache.getStereotype("DTDInvalidAssociation");
      if (association.getCollectionStereotypeList().contains(stereotype)) {
         this.attachStereotype(lnk, "XMLInvalidLink");
      }

      return lnk;
   }

   public Collaboration buildCollaboration(String collaborationName) {
      Collaboration collaboration = this.modelCache.getXmlCollaboration();
      if (collaboration != null) {
         return collaboration;
      } else {
         try {
            collaboration = (Collaboration)ModelFactory.getCreator().createNewCollaboration(new Object[]{ModelFactory.currentModel});
            collaboration.setName(collaborationName);
            collaboration.setOwnerModel(ModelFactory.currentModel);
            this.attachStereotype(collaboration, "XMLObjectsCollaboration");
            this.modelCache.setXmlCollaboration(collaboration);
            return collaboration;
         } catch (Exception var4) {
            var4.printStackTrace();
            return null;
         }
      }
   }
}
