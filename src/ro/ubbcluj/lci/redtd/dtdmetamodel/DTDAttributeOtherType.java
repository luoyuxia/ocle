package ro.ubbcluj.lci.redtd.dtdmetamodel;

public class DTDAttributeOtherType implements DTDAttributeType {
   private String attributeType;

   public DTDAttributeOtherType(String attributeType) {
      this.attributeType = attributeType;
   }

   public String getAttributeType() {
      return this.attributeType;
   }

   public void setAttributeType(String attributeType) {
      this.attributeType = attributeType;
   }
}
