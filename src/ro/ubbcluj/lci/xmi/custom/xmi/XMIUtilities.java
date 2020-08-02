package ro.ubbcluj.lci.xmi.custom.xmi;

import java.util.StringTokenizer;

public class XMIUtilities {
   public XMIUtilities() {
   }

   public static String renamePackages(String xmlName) {
      StringBuffer sb = new StringBuffer();
      String classNoDot = xmlName.substring(xmlName.lastIndexOf(".") + 1);
      String packages = xmlName.substring(xmlName.lastIndexOf("uml") + 4, xmlName.lastIndexOf("."));

      String pack;
      for(StringTokenizer st = new StringTokenizer(packages, "."); st.hasMoreTokens(); sb.append(pack + ".")) {
         pack = st.nextToken();
         if (pack.equals("behavioralElements")) {
            pack = "Behavioral_Elements";
         } else if (pack.equals("foundation")) {
            pack = "Foundation";
         } else if (pack.equals("modelManagement")) {
            pack = "Model_Management";
         } else if (pack.equals("activityGraphs")) {
            pack = "Activity_Graphs";
         } else if (pack.equals("collaborations")) {
            pack = "Collaborations";
         } else if (pack.equals("commonBehavior")) {
            pack = "Common_Behavior";
         } else if (pack.equals("stateMachines")) {
            pack = "State_Machines";
         } else if (pack.equals("useCases")) {
            pack = "Use_Cases";
         } else if (pack.equals("core")) {
            pack = "Core";
         } else if (pack.equals("dataTypes")) {
            pack = "Data_Types";
         } else if (pack.equals("extensionMechanisms")) {
            pack = "Extension_Mechanisms";
         }
      }

      sb.append(classNoDot);
      return sb.toString();
   }
}
