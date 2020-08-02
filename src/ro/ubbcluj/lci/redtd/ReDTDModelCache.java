package ro.ubbcluj.lci.redtd;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.utils.ModelEvent;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.ModelListener;
import ro.ubbcluj.lci.utils.ModelUtilities;

public class ReDTDModelCache implements ModelListener {
   private static ReDTDModelCache instance;
   private static Map dtdStereotypes = null;
   private static Map dtdTagDefinitions = null;
   private static Map modelClasses = new HashMap();
   private static Collection modelEnumerations = new LinkedList();
   private static Collaboration xmlCollaboration;

   public static ReDTDModelCache getInstance() {
      if (instance == null) {
         instance = new ReDTDModelCache();
      }

      if (dtdStereotypes == null || dtdStereotypes.isEmpty()) {
         dtdStereotypes = ModelFactory.getDTDStereotypes();
      }

      if (dtdTagDefinitions == null || dtdTagDefinitions.isEmpty()) {
         dtdTagDefinitions = ModelFactory.getDTDTagDefinitions();
      }

      if (modelClasses.isEmpty() && ModelFactory.currentModel != null) {
         modelClasses.putAll(ModelUtilities.getClasses(ModelFactory.currentModel));
      }

      if (modelEnumerations.isEmpty() && ModelFactory.currentModel != null) {
         modelEnumerations.addAll(ModelUtilities.getEnumerations(ModelFactory.currentModel));
      }

      return instance;
   }

   private ReDTDModelCache() {
   }

   public void putClass(String name, Object value) {
      modelClasses.put(name, value);
   }

   public Object getClass(String name) {
      return modelClasses.get(name);
   }

   public Collection getClasses() {
      Collection dtdClasses = new LinkedList();
      Iterator classesIt = modelClasses.values().iterator();

      while(true) {
         while(classesIt.hasNext()) {
            Classifier cls = (Classifier)classesIt.next();
            Iterator attachedStereotypes = cls.getCollectionStereotypeList().iterator();

            while(attachedStereotypes.hasNext()) {
               Object attachedStereotype = attachedStereotypes.next();
               if (dtdStereotypes.containsValue(attachedStereotype)) {
                  dtdClasses.add(cls);
                  break;
               }
            }
         }

         return dtdClasses;
      }
   }

   public void addEnumeration(Object value) {
      modelEnumerations.add(value);
   }

   public Iterator getEnumerations() {
      return modelEnumerations.iterator();
   }

   public Object getStereotype(String name) {
      return dtdStereotypes.get(name);
   }

   public Collection getStereotypes() {
      return dtdStereotypes.values();
   }

   public Object getTagDefinition(String name) {
      return dtdTagDefinitions.get(name);
   }

   public Collaboration getXmlCollaboration() {
      return xmlCollaboration;
   }

   public void setXmlCollaboration(Collaboration xmlCollaboration) {
      ReDTDModelCache.xmlCollaboration = xmlCollaboration;
   }

   public void clearCache() {
      if (dtdStereotypes != null) {
         dtdStereotypes.clear();
      }

      if (dtdTagDefinitions != null) {
         dtdTagDefinitions.clear();
      }

      modelClasses.clear();
      modelEnumerations.clear();
      xmlCollaboration = null;
   }

   public void modelChanged(ModelEvent event) {
      if (event.getSubject() instanceof ModelElement) {
         int operation = event.getOperation();
         ModelElement element = (ModelElement)event.getSubject();
         switch(operation) {
         case 10:
            if (element instanceof Class) {
               modelClasses.put(element.getName(), element);
            } else if (element instanceof Enumeration) {
               modelEnumerations.add(element);
            }
            break;
         case 20:
         case 21:
            if (element instanceof Class) {
               Iterator classesIt = modelClasses.entrySet().iterator();

               while(classesIt.hasNext()) {
                  Entry currentEntry = (Entry)classesIt.next();
                  if (currentEntry.getValue() == element) {
                     String currentKey = (String)currentEntry.getKey();
                     modelClasses.remove(currentKey);
                     break;
                  }
               }

               modelClasses.put(element.getName(), element);
            }
            break;
         case 30:
            if (element instanceof Class) {
               modelClasses.remove(element.getName());
            } else if (element instanceof Enumeration) {
               modelEnumerations.remove(element);
            }
         }

      }
   }
}
