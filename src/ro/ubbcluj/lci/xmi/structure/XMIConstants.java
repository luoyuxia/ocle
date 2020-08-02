package ro.ubbcluj.lci.xmi.structure;

public class XMIConstants {
   public static final int ATTRIBUTE = 0;
   public static final int VALUE_CDATA = 1;
   public static final int CDATA = 2;
   public static final int ELEMENT = 3;
   public static final int HREF = 4;
   public static final int OBJECT_HREF = 5;
   public static final int DO_NOT_EXPORT = 6;
   public static final int IDREF = 7;
   public static final String XMI_ID_ATTRIBUTE_NAME = "xmi.id";
   public static final String XMI_IDREF_ATTRIBUTE_NAME = "xmi.idref";
   public static final String XMI_UUID_ATTRIBUTE_NAME = "xmi.uuid";
   public static final String XMI_UUIDREF_ATTRIBUTE_NAME = "xmi.uuidref";
   public static final String XMI_VALUE_ATTRIBUTE_NAME = "xmi.value";
   public static final String XMI_HREF_ATTRIBUTE_NAME = "href";

   public XMIConstants() {
   }

   public static int getIntValue(String value) {
      if (value != null) {
         if (value.equals("ATTRIBUTE")) {
            return 0;
         }

         if (value.equals("VALUE_CDATA")) {
            return 1;
         }

         if (value.equals("CDATA")) {
            return 2;
         }

         if (value.equals("ELEMENT")) {
            return 3;
         }

         if (value.equals("HREF")) {
            return 4;
         }

         if (value.equals("OBJECT_HREF")) {
            return 5;
         }

         if (value.equals("DO_NOT_EXPORT")) {
            return 6;
         }

         if (value.equals("IDREF")) {
            return 7;
         }
      }

      return -1;
   }
}
