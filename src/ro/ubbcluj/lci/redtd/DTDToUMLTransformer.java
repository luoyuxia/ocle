package ro.ubbcluj.lci.redtd;

import java.util.Iterator;
import ro.ubbcluj.lci.redtd.dtdmetamodel.ChildrenContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.ChoiceContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAnyElement;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAttribute;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAttributeEnumType;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAttributeOtherType;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAttributeType;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDElement;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDRestrictedElement;
import ro.ubbcluj.lci.redtd.dtdmetamodel.ElementContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.LeafContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.MixedContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.SequenceContent;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;

public class DTDToUMLTransformer implements DTDElementContentVisitor {
   private ModelBuilder modelBuilder = new ReDTDUMLModelBuilder();
   private DTDToUMLTransformationSession session;

   public DTDToUMLTransformer() {
   }

   public void transformElement(DTDElement dtdElement) {
      String dtdElementName = dtdElement.getName();
      Classifier dtdElementClass = this.modelBuilder.buildClass(dtdElementName);
      this.modelBuilder.detachStereotype(dtdElementClass, "DTDUndefinedElement");
      ElementContent content = null;
      Classifier classForContent;
      if (dtdElement instanceof DTDAnyElement) {
         this.modelBuilder.attachStereotype(dtdElementClass, "DTDAnyElement");
         classForContent = this.modelBuilder.buildClass("ANY");
         this.modelBuilder.buildAssociation(dtdElementClass, classForContent, this.modelBuilder.buildMultiplicity(8), (TaggedValue)null);
      } else {
         content = ((DTDRestrictedElement)dtdElement).getContent();
         if (content == null) {
            this.modelBuilder.attachStereotype(dtdElementClass, "DTDEmptyElement");
            return;
         }

         this.modelBuilder.attachStereotype(dtdElementClass, "DTDElement");
         this.session = new DTDToUMLTransformationSession(dtdElementName);
         classForContent = (Classifier)content.accept(this);
         if (content instanceof MixedContent) {
            this.modelBuilder.buildAssociation(dtdElementClass, classForContent, this.modelBuilder.buildMultiplicity(8), (TaggedValue)null);
         } else {
            this.modelBuilder.buildAssociation(dtdElementClass, classForContent, this.modelBuilder.buildMultiplicity(((ChildrenContent)content).getMultiplicity()), (TaggedValue)null);
         }
      }

   }

   public void transformAttribute(DTDAttribute dtdAttribute) {
      DTDAttributeType dtdAttributeType = dtdAttribute.getType();
      DataType umlAttributeType = null;
      if (dtdAttributeType instanceof DTDAttributeEnumType) {
         umlAttributeType = this.modelBuilder.buildEnumeration(((DTDAttributeEnumType)dtdAttributeType).getLiterals());
      } else {
         umlAttributeType = this.modelBuilder.buildDataType("String");
      }

      Attribute attr = this.modelBuilder.buildAttribute(this.modelBuilder.buildClass(dtdAttribute.getElementName()), dtdAttribute.getAttributeName(), (DataType)umlAttributeType, dtdAttribute.getValue(), dtdAttribute.isRequired(), dtdAttribute.isFixed());
      if (attr != null && dtdAttributeType instanceof DTDAttributeOtherType) {
         this.modelBuilder.attachStereotype(attr, ((DTDAttributeOtherType)dtdAttributeType).getAttributeType());
      }

   }

   public void resolveAnyContent() {
      this.modelBuilder.buildAnyContent();
   }

   public Object visitMixedContent(MixedContent mixedContent) {
      Classifier groupClass = this.modelBuilder.buildClass(this.session.getContext() + "_grp");
      this.modelBuilder.attachStereotype(groupClass, "DTDMixed");
      Iterator leafsIt = mixedContent.getLeafs().iterator();

      for(int mixedNo = 1; leafsIt.hasNext(); ++mixedNo) {
         LeafContent leaf = (LeafContent)leafsIt.next();
         Classifier leafClass = (Classifier)leaf.accept(this);
         this.modelBuilder.buildAssociation(groupClass, leafClass, this.modelBuilder.buildMultiplicity(6), this.modelBuilder.buildTaggedValue("mixed", String.valueOf(mixedNo)));
      }

      return groupClass;
   }

   public Object visitLeafContent(LeafContent leafContent) {
      Classifier leafClass;
      if (leafContent.getName().equals("#PCDATA")) {
         leafClass = this.modelBuilder.buildClass("#PCDATA");
         this.modelBuilder.buildAttribute(leafClass, "value", this.modelBuilder.buildDataType("String"), (String)null, true, false);
         return leafClass;
      } else {
         leafClass = this.modelBuilder.buildClass(leafContent.getName());
         if (leafClass.getStereotypeList() == null || !leafClass.getStereotypeList().hasMoreElements()) {
            this.modelBuilder.attachStereotype(leafClass, "DTDUndefinedElement");
         }

         return leafClass;
      }
   }

   public Object visitSequenceContent(SequenceContent sequenceContent) {
      Classifier groupClass = this.modelBuilder.buildClass(this.session.getContext() + "_grp" + this.session.getNextGroupNumber());
      this.modelBuilder.attachStereotype(groupClass, "DTDSequence");
      Iterator leafsIt = sequenceContent.getChildren().iterator();

      for(int seqNo = 1; leafsIt.hasNext(); ++seqNo) {
         ChildrenContent child = (ChildrenContent)leafsIt.next();
         Classifier childClass = (Classifier)child.accept(this);
         this.modelBuilder.buildAssociation(groupClass, childClass, this.modelBuilder.buildMultiplicity(child.getMultiplicity()), this.modelBuilder.buildTaggedValue("sequence", String.valueOf(seqNo)));
      }

      return groupClass;
   }

   public Object visitChoiceContent(ChoiceContent choiceContent) {
      Classifier groupClass = this.modelBuilder.buildClass(this.session.getContext() + "_grp" + this.session.getNextGroupNumber());
      this.modelBuilder.attachStereotype(groupClass, "DTDChoice");
      Iterator leafsIt = choiceContent.getChildren().iterator();

      for(int choiceNo = 1; leafsIt.hasNext(); ++choiceNo) {
         ChildrenContent child = (ChildrenContent)leafsIt.next();
         Classifier childClass = (Classifier)child.accept(this);
         this.modelBuilder.buildAssociation(groupClass, childClass, this.modelBuilder.buildMultiplicity(child.getMultiplicity()), this.modelBuilder.buildTaggedValue("choice", String.valueOf(choiceNo)));
      }

      return groupClass;
   }
}
