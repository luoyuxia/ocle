package ro.ubbcluj.lci.ocl.codegen.norm;

import ro.ubbcluj.lci.ocl.OclType;

public class Variable {
   private String name;
   private OclType type;

   public Variable() {
      this.name = "?";
   }

   public Variable(String n, OclType t) {
      this.name = n;
      this.type = t;
   }

   public String getName() {
      return this.name;
   }

   public OclType getType() {
      return this.type;
   }
}
