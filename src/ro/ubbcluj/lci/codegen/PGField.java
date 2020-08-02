package ro.ubbcluj.lci.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;

public final class PGField extends PGElement implements CodeGenerationConstants {
   private String fieldTypeName;

   public PGField(PGClass container) {
      super(container);
      this.setAllowsChildren(false);
   }

   public void setTypeName(String typeName) {
      this.fieldTypeName = typeName;
   }

   public String getTypeName() {
      return this.fieldTypeName;
   }

   protected StringBuffer getDeclarationCode() {
      Template template = CodeGeneratorManager.getTemplate("UML_FieldDeclaration.vm");
      Context context = CodeGeneratorManager.newContext();
      context.put("fieldname", this.name);
      context.put("modifiers", this.modifiers);
      context.put("fieldtype", this.fieldTypeName);
      return CodeGenUtilities.getText(template, context);
   }

   protected StringBuffer getFinalizerCode() {
      return null;
   }
}
