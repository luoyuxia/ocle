package ro.ubbcluj.lci.redtd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public class UMLToDTDTransformer extends UMLTransformer {
   private ReDTDModelCache modelCache;
   private FileOutputStream dtdOutputStream;
   private final int SEQUENCE = 0;
   private final int CHOICE = 1;
   private final int MIXED = 2;

   public UMLToDTDTransformer() {
   }

   public void transformModel(Model umlModel, File outputFile) {
      this.initOutputStream(outputFile);
      Iterator classesIt = this.modelCache.getClasses().iterator();

      while(classesIt.hasNext()) {
         Classifier elementClass = (Classifier)classesIt.next();
         this.transformClassToDTDElement(elementClass);
      }

      try {
         this.dtdOutputStream.close();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   private void transformClassToDTDElement(Classifier elementClass) {
      if (!ReDTDUtilities.isDTDGroup(elementClass) && !elementClass.getName().equals("#PCDATA")) {
         this.emitDTD("<!ELEMENT ");
         this.emitDTD(elementClass.getName());
         this.emitDTD(" ");
         Collection contentEnds = ReDTDUtilities.getNavigableOppositeEnds(elementClass);
         if (contentEnds.isEmpty()) {
            if (!elementClass.getName().equals("#PCDATA")) {
               this.emitDTD("EMPTY");
            }
         } else if (contentEnds.size() == 1) {
            AssociationEnd contentEnd = (AssociationEnd)contentEnds.iterator().next();
            if (ReDTDUtilities.isDTDGroup(contentEnd.getParticipant())) {
               this.transformClassToDTDContentModel(contentEnd);
            } else {
               this.emitDTD("(");
               this.transformClassToDTDContentModel(contentEnd);
               this.emitDTD(")");
            }
         } else {
            this.emitDTD("(");
            Object[] oppEnds = ReDTDUtilities.getValidOrderedNavigableOppositeEnds(elementClass);
            int groupType = this.getGroupTypeByTaggedValue((AssociationEnd)oppEnds[0]);
            String groupSeparator = this.getGroupSeparator(groupType);
            if (groupType == 2) {
               this.transformClassToDTDContentElement((Classifier)this.modelCache.getClass("#PCDATA"), (Multiplicity)null);
               this.emitDTD(groupSeparator);
            }

            for(int i = 0; i < oppEnds.length; ++i) {
               AssociationEnd assocEnd = (AssociationEnd)oppEnds[i];
               Multiplicity multiplicity = groupType == 0 ? assocEnd.getMultiplicity() : null;
               this.transformClassToDTDContentElement(assocEnd.getParticipant(), multiplicity);
               if (i < oppEnds.length - 1) {
                  this.emitDTD(groupSeparator);
               }
            }

            this.emitDTD(")*");
         }

         this.emitDTD(">\n");
         Iterator attributesIt = elementClass.allAttributes().iterator();
         if (attributesIt.hasNext()) {
            this.emitDTD("<!ATTLIST " + elementClass.getName() + "\n");

            while(attributesIt.hasNext()) {
               Attribute attr = (Attribute)attributesIt.next();
               this.transformAttribute(attr);
            }

            this.emitDTD(">\n");
         }
      }

   }

   private void transformClassToDTDContentModel(AssociationEnd contentEnd) {
      Classifier contentClass = contentEnd.getParticipant();
      Collection stereotypes = contentClass.getCollectionStereotypeList();
      if (stereotypes.contains(this.modelCache.getStereotype("DTDAny"))) {
         this.emitDTD("ANY");
      } else {
         this.transformClassToDTDContentElement(contentEnd.getParticipant(), contentEnd.getMultiplicity());
      }

   }

   private void transformClassToDTDContentElement(Classifier content, Multiplicity multiplicity) {
      int groupType = this.getGroupTypeByClass(content);
      String groupSeparator = this.getGroupSeparator(groupType);
      if (groupType != 0 && groupType != 1 && groupType != 2) {
         String className = content.getName();
         this.emitDTD(className);
         if (!className.equals("#PCDATA")) {
            this.applyMultiplicity(multiplicity);
         }
      } else {
         Object[] oppEnds = ReDTDUtilities.getValidOrderedNavigableOppositeEnds(content);
         this.emitDTD("(");
         if (groupType == 2) {
            this.transformClassToDTDContentElement((Classifier)this.modelCache.getClass("#PCDATA"), (Multiplicity)null);
            this.emitDTD(groupSeparator);
         }

         for(int i = 0; i < oppEnds.length; ++i) {
            AssociationEnd assocEnd = (AssociationEnd)oppEnds[i];
            Multiplicity leafMultiplicity = groupType != 0 && groupType != 1 ? null : assocEnd.getMultiplicity();
            this.transformClassToDTDContentElement(assocEnd.getParticipant(), leafMultiplicity);
            if (i < oppEnds.length - 1) {
               this.emitDTD(groupSeparator);
            }
         }

         this.emitDTD(")");
         if (groupType == 2) {
            this.emitDTD("*");
         } else {
            this.applyMultiplicity(multiplicity);
         }
      }

   }

   private void transformAttribute(Attribute umlAttribute) {
      this.emitDTD(" " + umlAttribute.getName() + " " + this.getDTDAttributeType(umlAttribute));
      if (!umlAttribute.getCollectionStereotypeList().contains(this.modelCache.getStereotype("FIXED")) && umlAttribute.getChangeability() != 0) {
         if (!umlAttribute.getCollectionStereotypeList().contains(this.modelCache.getStereotype("REQUIRED")) && ((MultiplicityRange)umlAttribute.getMultiplicity().getRangeList().nextElement()).getLower() != 1) {
            this.emitDTD(" #IMPLIED ");
         } else {
            this.emitDTD(" #REQUIRED ");
         }
      } else {
         this.emitDTD(" #FIXED \"" + umlAttribute.getInitialValue().getBody() + '"');
      }

      this.emitDTD("\n");
   }

   private String getDTDAttributeType(Attribute umlAttribute) {
      StringBuffer result = new StringBuffer();
      Classifier attrType = umlAttribute.getType();
      if (attrType instanceof Enumeration) {
         Iterator literalIt = ((Enumeration)attrType).getCollectionLiteralList().iterator();
         result.append("(");

         while(literalIt.hasNext()) {
            result.append(((EnumerationLiteral)literalIt.next()).getName());
            if (literalIt.hasNext()) {
               result.append("|");
            }
         }

         result.append(")");
      } else {
         Collection stereotypes = umlAttribute.getCollectionStereotypeList();
         if (stereotypes.contains(this.modelCache.getStereotype("ID"))) {
            result.append(" ID ");
         } else if (stereotypes.contains(this.modelCache.getStereotype("IDREF"))) {
            result.append(" IDREF ");
         } else if (stereotypes.contains(this.modelCache.getStereotype("IDREFS"))) {
            result.append(" IDREFS ");
         } else if (stereotypes.contains(this.modelCache.getStereotype("NMTOKEN"))) {
            result.append(" NMTOKEN ");
         } else if (stereotypes.contains(this.modelCache.getStereotype("NMTOKENS"))) {
            result.append(" NMTOKENS ");
         } else if (stereotypes.contains(this.modelCache.getStereotype("ENTITY"))) {
            result.append(" ENTITY ");
         } else if (stereotypes.contains(this.modelCache.getStereotype("ENTITIES"))) {
            result.append(" ENTITIES ");
         } else {
            result.append(" CDATA ");
         }
      }

      return result.toString();
   }

   public void applyMultiplicity(Multiplicity multiplicity) {
      if (multiplicity != null) {
         MultiplicityRange range = (MultiplicityRange)multiplicity.getRangeList().nextElement();
         int lower = range.getLower();
         int upper = range.getUpper().intValue();
         if (lower == 0) {
            if (upper == 1) {
               this.emitDTD("?");
            } else {
               this.emitDTD("*");
            }
         } else if (upper != 1) {
            this.emitDTD("+");
         }

      }
   }

   private int getGroupTypeByClass(Classifier groupClass) {
      if (ReDTDUtilities.isDTDSequenceGroup(groupClass)) {
         return 0;
      } else if (ReDTDUtilities.isDTDChoiceGroup(groupClass)) {
         return 1;
      } else {
         return ReDTDUtilities.isDTDMixedGroupKind(groupClass) ? 2 : -1;
      }
   }

   private int getGroupTypeByTaggedValue(AssociationEnd assocEnd) {
      Collection taggedValues = assocEnd.getCollectionTaggedValueList();
      if (!taggedValues.isEmpty()) {
         if (((TaggedValue)taggedValues.iterator().next()).getName().equals("sequence")) {
            return 0;
         }

         if (((TaggedValue)taggedValues.iterator().next()).getName().equals("choice")) {
            return 1;
         }

         if (((TaggedValue)taggedValues.iterator().next()).getName().equals("mixed")) {
            return 2;
         }
      }

      return -1;
   }

   private String getGroupSeparator(int groupType) {
      switch(groupType) {
      case 0:
         return ",";
      case 1:
      case 2:
         return "|";
      default:
         return "";
      }
   }

   private void initOutputStream(File outputFile) {
      try {
         this.dtdOutputStream = new FileOutputStream(outputFile);
         this.modelCache = ReDTDModelCache.getInstance();
      } catch (FileNotFoundException var3) {
         var3.printStackTrace();
      }

   }

   private void emitDTD(String chars) {
      try {
         this.dtdOutputStream.write(chars.getBytes());
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }
}
