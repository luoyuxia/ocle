package ro.ubbcluj.lci.ocl;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.ModelImpl;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.xmi.XMIDecoder;
import ro.ubbcluj.lci.xmi.XMISerializer;

public final class OclLoader {
   private static Model metamodel = null;
   private static Package dataTypesPackage = null;
   private static Vector dataTypesPackagePath = null;
   private static Classifier oclAnyRef = null;
   private static Classifier oclEnumRef = null;

   public OclLoader() {
   }

   public static void setMetamodel(Model mm) {
      metamodel = mm;
   }

   public static Package getOclTypesModel() {
      if (dataTypesPackage == null) {
         introspect(metamodel);
         computeReferences();
      }

      return dataTypesPackage;
   }

   public static Vector getDatatypesPackagePath() {
      if (dataTypesPackagePath == null) {
         introspect(metamodel);
      }

      return dataTypesPackagePath;
   }

   public static Classifier getOclAnyRef() {
      getOclTypesModel();
      return oclAnyRef;
   }

   public static Classifier getEnumRef() {
      getOclTypesModel();
      return oclEnumRef;
   }

   private static void computeReferences() {
      oclAnyRef = null;
      oclEnumRef = null;
      Iterator it = dataTypesPackage.directGetCollectionOwnedElementList().iterator();

      while(it.hasNext()) {
         Object nextElement = it.next();
         if (nextElement instanceof Classifier) {
            Classifier cls = (Classifier)nextElement;
            String clsName = cls.getName();
            if ("OclAny".equals(clsName)) {
               oclAnyRef = cls;
            } else if ("Enumeration".equals(clsName)) {
               oclEnumRef = cls;
            }
         }
      }

      if (oclAnyRef == null || oclEnumRef == null) {
         throw new RuntimeException("Enumeration/OclAny not found in the \"Data Types\" package");
      }
   }

   private static void introspect(Model m) {
      dataTypesPackagePath = new Vector();
      dataTypesPackage = null;
      String[] path = new String[]{"Foundation", "Data Types"};
      Package cp = m;
      int i = 0;

      boolean found;
      do {
         if (i >= path.length) {
            dataTypesPackage = (Package)cp;
            return;
         }

         Iterator itOwned = ((Package)cp).directGetCollectionOwnedElementList().iterator();
         found = false;

         while(itOwned.hasNext() && !found) {
            Object candidate = itOwned.next();
            if (candidate instanceof Package) {
               Package np = (Package)candidate;
               if (path[i].equals(np.getName())) {
                  found = true;
                  ++i;
                  cp = np;
                  dataTypesPackagePath.add(np);
               }
            }
         }
      } while(found);

      throw new RuntimeException("Package \"" + path[i] + "\" not found in the metamodel.");
   }

   public static Model XML2Model(String filename) {
      Model model = null;

      try {
         XMIDecoder decoder = XMISerializer.getInstance().getDecoder();
         decoder.newSession((File)null);
         decoder.setMetadataRootClass((new ModelImpl()).getClass());
         Object m = decoder.decode(filename);
         decoder.closeSession();
         if (m instanceof Model) {
            model = (Model)m;
         }

         return model;
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }
}
