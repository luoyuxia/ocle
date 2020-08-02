package ro.ubbcluj.lci.redtd.dtdmetamodel;

public class DTDAttribute {
   private String elementName;
   private String attributeName;
   private DTDAttributeType type;
   private String value;
   private boolean isRequired;
   private boolean isFixed;

   public DTDAttribute(String elementName, String attributeName, DTDAttributeType type, String value, boolean isRequired, boolean isFixed) {
      this.elementName = elementName;
      this.attributeName = attributeName;
      this.type = type;
      this.value = value;
      this.isRequired = isRequired;
      this.isFixed = isFixed;
   }

   public String getElementName() {
      return this.elementName;
   }

   public void setElementName(String elementName) {
      this.elementName = elementName;
   }

   public String getAttributeName() {
      return this.attributeName;
   }

   public void setAttributeName(String attributeName) {
      this.attributeName = attributeName;
   }

   public DTDAttributeType getType() {
      return this.type;
   }

   public void setType(DTDAttributeType type) {
      this.type = type;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public boolean isRequired() {
      return this.isRequired;
   }

   public void setRequired(boolean required) {
      this.isRequired = required;
   }

   public boolean isFixed() {
      return this.isFixed;
   }

   public void setFixed(boolean fixed) {
      this.isFixed = fixed;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof DTDAttribute)) {
         return false;
      } else {
         DTDAttribute dtdAttribute = (DTDAttribute)o;
         if (!this.attributeName.equals(dtdAttribute.attributeName)) {
            return false;
         } else {
            return this.elementName.equals(dtdAttribute.elementName);
         }
      }
   }

   public int hashCode() {
      int result = this.elementName.hashCode();
      result = 29 * result + this.attributeName.hashCode();
      return result;
   }
}
