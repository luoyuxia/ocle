package ro.ubbcluj.lci.gui.mainframe;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import ro.ubbcluj.lci.uml.foundation.core.ClassImpl;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public class PseudoGenerator {
   private static String filePath = "src/ro/ubbcluj/lci/utils/InstanceCreator.java";

   public PseudoGenerator() {
   }

   public static void generate() {
      System.out.println("Are you sure you want start this generator ?");
      System.exit(-2);
      Collection metaclasses = getMetaclasses(GRepository.getInstance().getMetamodel().getModel());
      Classifier[] metaArray = (Classifier[])metaclasses.toArray(new Classifier[0]);
      Arrays.sort(metaArray, new Comparator() {
         public int compare(Object o1, Object o2) {
            Classifier c1 = (Classifier)o1;
            Classifier c2 = (Classifier)o2;
            return c1.getName().compareTo(c2.getName());
         }
      });

      try {
         FileOutputStream fos = new FileOutputStream(filePath);
         PrintStream ps = new PrintStream(fos);
         ps.println("/**");
         ps.println(" * InstanceCreator.java");
         ps.println(" *");
         ps.println(" * @author Sorin MOLDOVAN");
         ps.println(" */");
         ps.println();
         ps.println("package ro.ubbcluj.lci.utils;");
         ps.println();
         ps.println("import ro.ubbcluj.lci.uml.repository.foundation.core.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.foundation.dataTypes.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.foundation.extensionMechanisms.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.modelManagement.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.actionFoundation.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.collectionActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.compositeActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.computationActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.jumpActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.mesagingActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.readWriteActions.associationActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.readWriteActions.attributeActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.readWriteActions.objectActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.readWriteActions.otherActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.actions.readWriteActions.variableActions.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.activityGraphs.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.collaborations.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.commonBehavior.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.stateMachines.*;");
         ps.println("import ro.ubbcluj.lci.uml.repository.behavioralElements.useCases.*;");
         ps.println();
         ps.println("import ro.ubbcluj.lci.utils.exceptions.CreationException;");
         ps.println();
         ps.println("/**");
         ps.println(" * Class responsible for creating instances.");
         ps.println(" */");
         ps.println("public class InstanceCreator {");
         ps.println("\tpublic InstanceCreator() {}");
         ps.println();

         for(int i = 0; i < metaArray.length; ++i) {
            Classifier clazz = metaArray[i];
            if (!clazz.isAbstract()) {
               ps.println("\t/**");
               ps.println("\t * Creates a new " + clazz.getName());
               ps.println("\t * @param params");
               ps.println("\t * @pre");
               ps.println("\t */");
               ps.println("\tpublic java.lang.Object createNew" + clazz.getName() + "(java.lang.Object[] params)");
               ps.println("\t\t\tthrows CreationException {");
               ps.println();
               ps.println("\t\t//@pre");
               ps.println("\t\tif (false)");
               ps.println("\t\t\tthrow new CreationException(params[0], \"" + clazz.getName() + "\");");
               ps.println();
               ps.println("\t\t" + clazz.getName() + " result = new " + clazz.getName() + "Impl();");
               ps.println();
               ps.println("\t\t//set");
               ps.println();
               ps.println("\t\treturn result;");
               ps.println("\t}");
               ps.println();
            }
         }

         ps.println("}");
         ps.flush();
         ps.close();
         fos.flush();
         fos.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      System.out.println("Generation complete.");
      System.exit(27);
   }

   private static Collection getMetaclasses(Package pak) {
      ArrayList result = new ArrayList();
      if (!pak.getName().equalsIgnoreCase("Datatypes") && !pak.getName().equalsIgnoreCase("Data Types") && !pak.getName().equalsIgnoreCase("java")) {
         Collection tmp = pak.directGetCollectionOwnedElementList();
         Iterator it = tmp.iterator();

         while(it.hasNext()) {
            Object o = it.next();
            if (o.getClass() == (ClassImpl.class)) {
               result.add(o);
            }

            if (o instanceof Package) {
               result.addAll(getMetaclasses((Package)o));
            }
         }

         return result;
      } else {
         return result;
      }
   }
}
