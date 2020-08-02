package ro.ubbcluj.lci.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;

public class ModelUtilities {
   public ModelUtilities() {
   }

   public static Map findAssociationsBetween(Classifier supplier, Classifier client) {
      Map assocs = new HashMap();
      Iterator it = client.allOppositeAssociationEnds().iterator();

      while(true) {
         while(true) {
            AssociationEnd oppEnd;
            Association tmpAssoc;
            AssociationEnd thisEnd;
            Classifier oppParticipant;
            do {
               if (!it.hasNext()) {
                  return assocs;
               }

               oppEnd = (AssociationEnd)it.next();
               tmpAssoc = oppEnd.getAssociation();
               thisEnd = null;
               Iterator assocIt = tmpAssoc.getCollectionConnectionList().iterator();

               while(assocIt.hasNext()) {
                  thisEnd = (AssociationEnd)assocIt.next();
                  if (oppEnd != thisEnd) {
                     break;
                  }
               }

               oppParticipant = oppEnd.getParticipant();
            } while(supplier != oppParticipant && !supplier.allParents().contains(oppParticipant));

            if (client != oppParticipant && !client.allParents().contains(oppParticipant)) {
               assocs.put(tmpAssoc.getName(), tmpAssoc);
            } else if (thisEnd.getName().equals(oppEnd.getName())) {
               assocs.put("InvalidAutoassociation", tmpAssoc);
            } else {
               assocs.put(tmpAssoc.getName() + " {" + thisEnd.getName() + "-" + oppEnd.getName() + "}", tmpAssoc);
            }
         }
      }
   }

   public static Set findAssociationsForParticipant(Classifier participant) {
      Set assocs = new HashSet();
      Iterator it = participant.allOppositeAssociationEnds().iterator();

      while(it.hasNext()) {
         AssociationEnd oppEnd = (AssociationEnd)it.next();
         assocs.add(oppEnd.getAssociation());
      }

      return assocs;
   }

   public static AssociationEnd findOppositeEnd(AssociationEnd thisEnd) {
      Iterator it = thisEnd.getAssociation().getCollectionConnectionList().iterator();

      AssociationEnd assocEnd;
      do {
         if (!it.hasNext()) {
            return null;
         }

         assocEnd = (AssociationEnd)it.next();
      } while(assocEnd == thisEnd);

      return assocEnd;
   }

   public static Attribute findAttribute(String attributeName, Classifier cls) {
      Iterator attrIt = cls.allAttributes().iterator();

      Attribute attr;
      do {
         if (!attrIt.hasNext()) {
            return null;
         }

         attr = (Attribute)attrIt.next();
      } while(!attr.getName().equals(attributeName));

      return attr;
   }

   public static AttributeLink findAttributeLink(String attributeLinkName, Instance obj) {
      Iterator attrLinkIt = obj.getCollectionSlotList().iterator();

      AttributeLink attrLink;
      do {
         if (!attrLinkIt.hasNext()) {
            return null;
         }

         attrLink = (AttributeLink)attrLinkIt.next();
      } while(!attrLink.getName().equals(attributeLinkName));

      return attrLink;
   }

   public static Map getClasses(Namespace container) {
      Map classes = new LinkedHashMap();
      Iterator ownedElements = container.directGetCollectionOwnedElementList().iterator();

      while(ownedElements.hasNext()) {
         ModelElement ownedElement = (ModelElement)ownedElements.next();
         if (ownedElement instanceof Class) {
            classes.put(ownedElement.getName(), ownedElement);
         }
      }

      return classes;
   }

   public static Collection getEnumerations(Namespace container) {
      Collection enumerations = new LinkedList();
      Iterator ownedElements = container.directGetCollectionOwnedElementList().iterator();

      while(ownedElements.hasNext()) {
         ModelElement ownedElement = (ModelElement)ownedElements.next();
         if (ownedElement instanceof Enumeration) {
            enumerations.add(ownedElement);
         }
      }

      return enumerations;
   }

   public static Object[] getAllObjectsOfKind(Classifier cls) {
      if (!(cls instanceof Class)) {
         return new Object[0];
      } else {
         Collection objects = new ArrayList();
         Classifier[] allCls = getAllChildren((Class)cls);

         for(int i = 0; i < allCls.length; ++i) {
            objects.addAll(allCls[i].getCollectionInstanceList());
         }

         Object[] objArray = new Object[objects.size()];
         Iterator it = objects.iterator();
         int i = 0;

         while(it.hasNext()) {
            java.lang.Object obj = it.next();
            if (obj instanceof Object) {
               objArray[i] = (Object)obj;
               ++i;
            }
         }

         return objArray;
      }
   }

   private static Classifier[] getAllChildren(Class cls) {
      Collection children = new ArrayList();
      children.add(cls);
      Collection specs = cls.getCollectionSpecializationList();
      Iterator it = specs.iterator();

      while(true) {
         GeneralizableElement child;
         Classifier[] tmp;
         int i;
         do {
            if (!it.hasNext()) {
               Iterator childrenIt = children.iterator();
               tmp = new Classifier[children.size()];

               for(i = 0; childrenIt.hasNext(); ++i) {
                  tmp[i] = (Classifier)childrenIt.next();
               }

               return tmp;
            }

            child = ((Generalization)it.next()).getChild();
         } while(!(child instanceof Class));

         tmp = getAllChildren((Class)child);

         for(i = 0; i < tmp.length; ++i) {
            children.add(tmp[i]);
         }
      }
   }
}
