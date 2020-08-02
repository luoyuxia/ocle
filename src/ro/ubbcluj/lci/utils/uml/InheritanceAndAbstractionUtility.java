package ro.ubbcluj.lci.utils.uml;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import ro.ubbcluj.lci.uml.foundation.core.Abstraction;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.utils.Utils;

public class InheritanceAndAbstractionUtility {
   public static List getDirectParents(GeneralizableElement c) {
      List result = null;

      GeneralizableElement p;
      for(Enumeration generalizations = c.getGeneralizationList(); generalizations.hasMoreElements(); result.add(p)) {
         Generalization g = (Generalization)generalizations.nextElement();
         p = g.getParent();
         if (p == null) {
            throw new RuntimeException("Parent not set for a generalization");
         }

         if (result == null) {
            result = new ArrayList();
         }
      }

      return result;
   }

   public static List getImplementedInterfaces(Class c) {
      List result = null;
      Enumeration cdl = c.getClientDependencyList();

      while(true) {
         Abstraction a;
         do {
            Object clientDep;
            do {
               if (!cdl.hasMoreElements()) {
                  return result;
               }

               clientDep = cdl.nextElement();
            } while(!(clientDep instanceof Abstraction));

            a = (Abstraction)clientDep;
         } while(!UMLUtilities.isRealization(a));

         Object supplier;
         for(Enumeration sl = a.getSupplierList(); sl.hasMoreElements(); result.add(supplier)) {
            supplier = sl.nextElement();
            if (supplier == null) {
               throw new RuntimeException("Supplier not set for realization");
            }

            if (result == null) {
               result = new ArrayList();
            }
         }
      }
   }

   public static List getAllParents(GeneralizableElement e) {
      List result = null;
      List directParents = getDirectParents(e);
      if (directParents != null) {
         Iterator it = directParents.iterator();

         while(it.hasNext()) {
            GeneralizableElement dp = (GeneralizableElement)it.next();
            List all = getAllParents(dp);
            if (all != null) {
               if (result == null) {
                  result = new ArrayList();
               }

               result.addAll(all);
            }
         }

         if (result == null) {
            result = new ArrayList();
         }

         result.addAll(directParents);
      }

      return result;
   }

   public static List getDirectParents(List clss) {
      List result = null;
      if (clss != null) {
         Iterator it = clss.iterator();

         while(it.hasNext()) {
            GeneralizableElement e = (GeneralizableElement)it.next();
            List dp = getDirectParents(e);
            if (dp != null) {
               if (result == null) {
                  result = new ArrayList();
               }

               Utils.distinctAddAll(result, dp);
            }
         }
      }

      return result;
   }

   public static List getImplementedInterfaces(List clss) {
      List result = null;
      if (clss != null) {
         Iterator it = clss.iterator();

         while(it.hasNext()) {
            Class e = (Class)it.next();
            List dp = getImplementedInterfaces(e);
            if (dp != null) {
               if (result == null) {
                  result = new ArrayList();
               }

               Utils.distinctAddAll(result, dp);
            }
         }
      }

      return result;
   }

   public static List getAllParents(List clss) {
      List result = null;
      if (clss != null) {
         Iterator it = clss.iterator();

         while(it.hasNext()) {
            GeneralizableElement e = (GeneralizableElement)it.next();
            List tmp = getAllParents(e);
            if (tmp != null) {
               if (result == null) {
                  result = new ArrayList();
               }

               Utils.distinctAddAll(result, tmp);
            }
         }
      }

      return result;
   }

   private InheritanceAndAbstractionUtility() {
   }
}
