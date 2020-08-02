package ro.ubbcluj.lci.ocl;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;

public class OclDynamicBinding {
   private Vector result = new Vector();
   private HashSet marked = new HashSet();
   private OclUmlApi umlapi;
   private OclNode stereotype;
   private Vector signatureTable = new Vector();
   private Vector signatureIndexes = new Vector();

   OclDynamicBinding(OclUmlApi umlapi) {
      this.umlapi = umlapi;
   }

   private boolean searchTable(Vector table, OclClassInfo ci, int index) {
      if (table == null) {
         return false;
      } else {
         boolean ind = false;
         Iterator it = table.iterator();

         while(true) {
            OclLetItem li;
            do {
               do {
                  if (!it.hasNext()) {
                     return ind;
                  }

                  li = (OclLetItem)it.next();
               } while(li.uniqueSignatureIndex != index);
            } while(li.stereotype != null && li.stereotype != this.stereotype);

            Object[] pair = new Object[]{ci, li};
            boolean exists = false;
            Iterator it2 = this.result.iterator();

            while(it2.hasNext()) {
               Object[] existingPair = (Object[])it2.next();
               if (pair[0] == existingPair[0] && pair[1] == existingPair[1]) {
                  exists = true;
                  break;
               }
            }

            if (!exists) {
               this.result.add(pair);
               ind = true;
            }
         }
      }
   }

   private void search(OclClassInfo ci, int index, boolean onlyMarked) throws ExceptionAny {
      List parentList = new LinkedList();
      parentList.add(ci);

      while(!parentList.isEmpty()) {
         OclClassInfo currentCI = (OclClassInfo)parentList.get(0);
         parentList.remove(0);
         if (!onlyMarked || this.marked.contains(currentCI)) {
            boolean found = this.searchTable(currentCI.letTable, currentCI, index) || this.searchTable(currentCI.defTable, currentCI, index);
            if (found) {
               break;
            }

            Enumeration gene = currentCI.classifier.getGeneralizationList();

            while(gene.hasMoreElements()) {
               Classifier cparent = (Classifier)((Generalization)gene.nextElement()).getParent();
               OclClassInfo parent = this.umlapi.getClassInfoByClassifier(cparent);
               parentList.add(parent);
            }
         }
      }

   }

   private void mark(OclClassInfo ci) throws ExceptionAny {
      this.marked.add(ci);
      Enumeration gene = ci.classifier.getSpecializationList();

      while(gene.hasMoreElements()) {
         Classifier child = (Classifier)((Generalization)gene.nextElement()).getChild();
         OclClassInfo cci = this.umlapi.getClassInfoByClassifier(child);
         if (!this.marked.contains(cci)) {
            this.mark(cci);
         }
      }

   }

   private String getAmbiguityMessage() {
      String s = new String();

      OclClassInfo ci;
      OclLetItem li;
      for(Iterator it = this.result.iterator(); it.hasNext(); s = s + ci.name + "::" + li.name) {
         Object[] pair = (Object[])it.next();
         ci = (OclClassInfo)pair[0];
         li = (OclLetItem)pair[1];
         if (s.length() > 0) {
            s = s + " or ";
         }
      }

      return s;
   }

   public Object[] getStaticLetItem(OclClassInfo staticType, String name, Vector paramtypes, OclClassInfo resolutionClass) throws ExceptionAny {
      this.stereotype = OclUtil.getOclChecker().lastStereotype;
      if (resolutionClass != null && !staticType.conforms(resolutionClass)) {
         throw new ExceptionAny("either the resolution class " + resolutionClass.name + " isn't a parent class of the class " + staticType.name + " or enumeration value " + resolutionClass.name + "#" + name + " not found");
      } else {
         Vector indexes = this.getSignatureIndexes(name, paramtypes);
         this.result.clear();
         Iterator it = indexes.iterator();

         while(it.hasNext()) {
            int index = ((Integer)it.next()).intValue();
            if (resolutionClass == null) {
               this.search(staticType, index, false);
            } else {
               this.searchTable(resolutionClass.letTable, resolutionClass, index);
               this.searchTable(resolutionClass.defTable, resolutionClass, index);
            }
         }

         if (this.result.size() == 0) {
            return null;
         } else if (this.result.size() == 1) {
            return (Object[])this.result.firstElement();
         } else {
            throw new ExceptionAny("ambiguity, function call could refer to " + this.getAmbiguityMessage());
         }
      }
   }

   public OclLetItem getDynamicLetItem(OclClassInfo staticType, Object dynamicValue, int letindex, boolean forceStatic, OclNode stereotype) throws ExceptionAny {
      this.stereotype = stereotype;
      OclClassInfo dynamicType = null;
      if (dynamicValue instanceof Instance) {
         Enumeration en = ((Instance)dynamicValue).getClassifierList();
         if (!en.hasMoreElements()) {
            throw new ExceptionAny("malformed model, object does not instantiate any classifier");
         }

         Classifier csf = (Classifier)en.nextElement();
         if (dynamicValue instanceof ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object) {
            dynamicType = this.umlapi.getClassInfoByClassifier(csf);
         }

         forceStatic = true;
      } else {
         dynamicType = this.umlapi.getClassInfoByJavaClass(dynamicValue.getClass());
      }

      if (!forceStatic) {
         this.marked.clear();
         this.mark(staticType);
         this.result.clear();
         this.search(dynamicType, letindex, true);
      } else {
         this.result.clear();
         this.search(dynamicType, letindex, false);
      }

      if (this.result.size() == 0) {
         throw new ExceptionAny("[internal] dynamic binding error, can't find method " + (String)((Object[])this.signatureTable.elementAt(letindex))[0] + " in the object " + OclUtil.printValue(dynamicValue) + " having the static type " + staticType.name);
      } else if (this.result.size() == 1) {
         return (OclLetItem)((Object[])this.result.firstElement())[1];
      } else {
         throw new ExceptionAny("dynamic binding ambiguity, function call could refer to " + this.getAmbiguityMessage());
      }
   }

   int getSignatureIndex(String name, Vector paramtypes, OclType rettype) {
      return this.getSignatureIndex(name, paramtypes, rettype, true);
   }

   private Vector getSignatureIndexes(String name, Vector paramtypes) {
      this.signatureIndexes.clear();
      this.getSignatureIndex(name, paramtypes, (OclType)null, false);
      return this.signatureIndexes;
   }

   private int getSignatureIndex(String name, Vector paramtypes, OclType rettype, boolean fullCompare) {
      int i = 0;

      for(Iterator it = this.signatureTable.iterator(); it.hasNext(); ++i) {
         Object[] entry = (Object[])it.next();
         String ename = (String)entry[0];
         Vector eparamtypes = (Vector)entry[1];
         OclType erettype = (OclType)entry[2];
         if (name.equals(ename) && paramtypes.size() == eparamtypes.size()) {
            boolean ind = true;

            for(int j = 0; j < paramtypes.size(); ++j) {
               OclType etype = (OclType)eparamtypes.elementAt(j);
               OclType type = (OclType)paramtypes.elementAt(j);
               if (fullCompare && !type.equals(etype)) {
                  ind = false;
                  break;
               }

               if (!fullCompare && !type.conforms(etype)) {
                  ind = false;
                  break;
               }
            }

            if (ind) {
               if (fullCompare) {
                  if (rettype.equals(erettype)) {
                     return i;
                  }

                  ind = false;
               } else {
                  this.signatureIndexes.add(new Integer(i));
               }
            }
         }
      }

      this.signatureTable.add(new Object[]{name, paramtypes, rettype});
      return this.signatureTable.size() - 1;
   }
}
