package ro.ubbcluj.lci.redtd;

public class DTDToUMLTransformationSession {
   private int groupNo = 1;
   private String context;

   public DTDToUMLTransformationSession(String context) {
      this.context = context;
   }

   public String getContext() {
      return this.context;
   }

   public int getNextGroupNumber() {
      return this.groupNo++;
   }
}
