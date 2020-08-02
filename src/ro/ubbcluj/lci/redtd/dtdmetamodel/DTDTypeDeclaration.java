package ro.ubbcluj.lci.redtd.dtdmetamodel;

public abstract class DTDTypeDeclaration {
   protected String name;

   protected DTDTypeDeclaration(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
