package ro.ubbcluj.lci.redtd.dtdmetamodel;

public class DTDRestrictedElement extends DTDElement {
   private ElementContent content = null;

   public DTDRestrictedElement(String name) {
      super(name);
   }

   public ElementContent getContent() {
      return this.content;
   }

   public void setContent(ElementContent content) {
      this.content = content;
   }
}
