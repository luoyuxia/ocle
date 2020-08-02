package ro.ubbcluj.lci.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;

public class PGConstructor extends PGBehavioralFeature {
   public PGConstructor(PGClass container) {
      super(container);
   }

   protected StringBuffer getDeclarationCode() {
      Template template = CodeGeneratorManager.getTemplate("UML_ConstructorDeclaration.vm");
      Context context = CodeGeneratorManager.newContext();
      context.put("methodname", this.name);
      context.put("modifiers", this.modifiers);
      context.put("parameters", this.parameters);
      StringBuffer sb = CodeGenUtilities.getText(template, context);
      return sb;
   }
}
