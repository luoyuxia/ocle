package ro.ubbcluj.lci.ocl.codegen.norm;

public class SyntaxCodeInfo {
   private String variableName = null;
   private String text = null;

   public SyntaxCodeInfo(String vName) {
      this.variableName = vName;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String name) {
      this.variableName = name;
   }

   public void setText(String t) {
      this.text = t;
   }

   public String getText() {
      if (this.variableName != null) {
         return this.variableName;
      } else if (this.text != null) {
         return this.text;
      } else {
         throw new InternalError("Code information queried without being set");
      }
   }
}
