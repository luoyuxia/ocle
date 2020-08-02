package ro.ubbcluj.lci.codegen;

public class PGParameter {
   private String name;
   private String type;

   public PGParameter(String pName, String typeSpec) {
      this.name = pName;
      this.type = typeSpec;
   }

   public String getTypeName() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return this.type + " " + this.name;
   }
}
