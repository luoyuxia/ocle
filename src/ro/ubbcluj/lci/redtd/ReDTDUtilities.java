package ro.ubbcluj.lci.redtd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;

public class ReDTDUtilities {
   public static ReDTDModelCache modelCache = ReDTDModelCache.getInstance();

   public ReDTDUtilities() {
   }

   public static boolean isPCDATA(Classifier pcdataClass) {
      return pcdataClass.getName().equals("#PCDATA");
   }

   public static boolean isDTDElement(Classifier elemClass) {
      return elemClass.getCollectionStereotypeList().contains(modelCache.getStereotype("DTDElement"));
   }

   public static boolean isDTDAnyElement(Classifier elemClass) {
      return elemClass.getCollectionStereotypeList().contains(modelCache.getStereotype("DTDAnyElement"));
   }

   public static boolean isDTDEmptyElement(Classifier elemClass) {
      return elemClass.getCollectionStereotypeList().contains(modelCache.getStereotype("DTDEmptyElement"));
   }

   public static boolean isDTDChoiceGroup(Classifier groupClass) {
      return groupClass.getCollectionStereotypeList().contains(modelCache.getStereotype("DTDChoice"));
   }

   public static boolean isDTDSequenceGroup(Classifier groupClass) {
      return groupClass.getCollectionStereotypeList().contains(modelCache.getStereotype("DTDSequence"));
   }

   public static boolean isDTDMixedGroupKind(Classifier groupClass) {
      Collection stereotypes = groupClass.getCollectionStereotypeList();
      return stereotypes.contains(modelCache.getStereotype("DTDMixed")) || stereotypes.contains(modelCache.getStereotype("DTDAny"));
   }

   public static boolean isDTDGroup(Classifier groupClass) {
      Collection stereotypes = groupClass.getCollectionStereotypeList();
      return stereotypes.contains(modelCache.getStereotype("DTDChoice")) || stereotypes.contains(modelCache.getStereotype("DTDSequence")) || stereotypes.contains(modelCache.getStereotype("DTDMixed")) || stereotypes.contains(modelCache.getStereotype("DTDAny"));
   }

   public static Object[] getOrderedNavigableOppositeEnds(Classifier fromClass) {
      Collection navigableOppositeEnds = new ArrayList();
      Iterator it = fromClass.oppositeAssociationEnds().iterator();

      while(it.hasNext()) {
         AssociationEnd oppEnd = (AssociationEnd)it.next();
         if (oppEnd.isNavigable()) {
            Collection oppEndTagValuesList = oppEnd.getCollectionTaggedValueList();
            if (oppEndTagValuesList != null && oppEndTagValuesList.size() > 0) {
               navigableOppositeEnds.add(oppEnd);
            }
         }
      }

      Object[] sortedOppEnds = navigableOppositeEnds.toArray();
      Arrays.sort(sortedOppEnds, new Comparator() {
         public int compare(Object obj1, Object obj2) {
            String tagValue1 = ((TaggedValue)((AssociationEnd)obj1).getTaggedValueList().nextElement()).getDataValue();
            String tagValue2 = ((TaggedValue)((AssociationEnd)obj2).getTaggedValueList().nextElement()).getDataValue();

            try {
               if (Integer.parseInt(tagValue1) > Integer.parseInt(tagValue2)) {
                  return 1;
               } else {
                  return Integer.parseInt(tagValue1) < Integer.parseInt(tagValue2) ? -1 : 0;
               }
            } catch (NumberFormatException var6) {
               var6.printStackTrace();
               return -2;
            }
         }
      });
      return sortedOppEnds;
   }

   public static Object[] getValidOrderedNavigableOppositeEnds(Classifier fromClass) {
      Collection navigableOppositeEnds = new ArrayList();
      Iterator it = fromClass.oppositeAssociationEnds().iterator();

      while(it.hasNext()) {
         AssociationEnd oppEnd = (AssociationEnd)it.next();
         if (oppEnd.isNavigable() && !oppEnd.getAssociation().getCollectionStereotypeList().contains(modelCache.getStereotype("DTDInvalidAssociation")) && !oppEnd.getParticipant().getName().equals("#PCDATA")) {
            Collection oppEndTagValuesList = oppEnd.getCollectionTaggedValueList();
            if (oppEndTagValuesList.size() > 0) {
               navigableOppositeEnds.add(oppEnd);
            }
         }
      }

      Object[] sortedOppEnds = navigableOppositeEnds.toArray();
      Arrays.sort(sortedOppEnds, new Comparator() {
         public int compare(Object obj1, Object obj2) {
            String tagValue1 = ((TaggedValue)((AssociationEnd)obj1).getTaggedValueList().nextElement()).getDataValue();
            String tagValue2 = ((TaggedValue)((AssociationEnd)obj2).getTaggedValueList().nextElement()).getDataValue();

            try {
               if (Integer.parseInt(tagValue1) > Integer.parseInt(tagValue2)) {
                  return 1;
               } else {
                  return Integer.parseInt(tagValue1) < Integer.parseInt(tagValue2) ? -1 : 0;
               }
            } catch (NumberFormatException var6) {
               var6.printStackTrace();
               return -2;
            }
         }
      });
      return sortedOppEnds;
   }

   public static Instance findRootInstance() {
      Iterator modelClasses = modelCache.getClasses().iterator();

      label46:
      while(modelClasses.hasNext()) {
         Classifier cls = (Classifier)modelClasses.next();
         Iterator oppEndsIt = cls.oppositeAssociationEnds().iterator();
         boolean isRoot = true;

         while(true) {
            while(true) {
               Classifier participant;
               do {
                  AssociationEnd assocEnd;
                  do {
                     if (!oppEndsIt.hasNext()) {
                        if (isRoot) {
                           Collection instances = cls.getCollectionInstanceList();
                           if (!instances.isEmpty()) {
                              return (Instance)instances.iterator().next();
                           }
                        }
                        continue label46;
                     }

                     assocEnd = (AssociationEnd)oppEndsIt.next();
                  } while(assocEnd.isNavigable());

                  participant = assocEnd.getParticipant();
               } while(participant.getName().equals("ANY"));

               Iterator appliedStereotypesIt = participant.getCollectionStereotypeList().iterator();
               Collection dtdStereotypes = modelCache.getStereotypes();

               while(appliedStereotypesIt.hasNext()) {
                  if (dtdStereotypes.contains(appliedStereotypesIt.next())) {
                     isRoot = false;
                     break;
                  }
               }
            }
         }
      }

      return null;
   }

   public static Collection getNavigableOppositeEnds(Classifier fromClass) {
      Collection navigableOppositeEnds = new ArrayList();
      Iterator it = fromClass.oppositeAssociationEnds().iterator();

      while(it.hasNext()) {
         AssociationEnd oppEnd = (AssociationEnd)it.next();
         if (oppEnd.isNavigable()) {
            navigableOppositeEnds.add(oppEnd);
         }
      }

      return navigableOppositeEnds;
   }

   public static String getOppositeEndsTagName(Classifier fromClass) {
      if (isDTDSequenceGroup(fromClass)) {
         return "sequence";
      } else if (isDTDChoiceGroup(fromClass)) {
         return "choice";
      } else if (isDTDMixedGroupKind(fromClass)) {
         return fromClass.getCollectionStereotypeList().contains(modelCache.getStereotype("DTDMixed")) ? "mixed" : "any";
      } else {
         return null;
      }
   }

   public static Collection getNavigableOppositeLinkEnds(Instance obj) {
      Collection navigableOppositeLinkEnds = new ArrayList();
      Iterator it = obj.allOppositeLinkEnds().iterator();

      while(it.hasNext()) {
         LinkEnd oppEnd = (LinkEnd)it.next();
         if (oppEnd.getAssociationEnd().isNavigable()) {
            navigableOppositeLinkEnds.add(oppEnd);
         }
      }

      return navigableOppositeLinkEnds;
   }
}
