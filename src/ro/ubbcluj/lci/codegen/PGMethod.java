package ro.ubbcluj.lci.codegen;

import java.util.Iterator;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;

public class PGMethod extends PGBehavioralFeature implements Cloneable {
   public static String DEFAULT_RETURN_TYPE = new String("void");

   public PGMethod(PGClass container) {
      super(container);
      this.returnTypeName = DEFAULT_RETURN_TYPE;
   }

   public Object clone() {
      PGMethod clone = new PGMethod((PGClass)null);
      clone.setName(this.name);
      Iterator it;
      if (this.modifiers != null) {
         it = this.modifiers.iterator();

         while(it.hasNext()) {
            clone.addModifier((String)it.next());
         }
      }

      clone.setReturnTypeName(this.returnTypeName);
      if (this.parameters != null) {
         it = this.parameters.iterator();

         while(it.hasNext()) {
            clone.addParameter((PGParameter)it.next());
         }
      }

      return clone;
   }

   protected StringBuffer getDeclarationCode() {
      Template template;
      if (this.isAbstract()) {
         template = CodeGeneratorManager.getTemplate("UML_Abstract_Method_Declaration.vm");
      } else {
         template = CodeGeneratorManager.getTemplate("UML_MethodDeclaration.vm");
      }

      Context context = CodeGeneratorManager.newContext();
      context.put("methodname", this.name);
      context.put("returntype", this.returnTypeName != null ? this.returnTypeName : DEFAULT_RETURN_TYPE);
      context.put("modifiers", this.modifiers);
      context.put("parameters", this.parameters);
      StringBuffer sb = CodeGenUtilities.getText(template, context);
      return sb;
   }

   public StringBuffer getFinalizerCode() {
      return this.isAbstract() ? null : super.getFinalizerCode();
   }

   public StringBuffer generateCode() {
      StringBuffer result = new StringBuffer();
      StringBuffer temp = this.getDeclarationCode();
      result.append(temp);
      if (!this.isAbstract()) {
         temp = this.getChildrenCode();
         result.append('\n');
         if (temp == null) {
            temp = this.defaultReturnStatement();
            if (temp != null) {
               result.append(temp).append('\n');
            }
         } else {
            result.append(temp);
         }
      }

      temp = this.getFinalizerCode();
      if (temp != null) {
         result.append(temp);
      }

      return result;
   }

   public boolean matchesSignature(String returnType, PGParameter[] fp) {
      boolean result = true;
      if (!returnType.equals(this.returnTypeName)) {
         result = false;
      }

      if (result) {
         for(int i = 0; result && i < fp.length; ++i) {
            if (i >= this.parameters.size()) {
               result = false;
            } else {
               PGParameter p = (PGParameter)this.parameters.get(i);
               if (!p.getTypeName().equals(fp[i].getTypeName())) {
                  result = false;
               }
            }
         }
      }

      return result;
   }

   private StringBuffer defaultReturnStatement() {
      String tr = this.getReturnTypeName();
      if (tr != null && !DEFAULT_RETURN_TYPE.equals(tr)) {
         Template tReturn = CodeGeneratorManager.getTemplate("OCL_return_statement");
         String value = "null";
         if (!"int".equals(tr) && !"float".equals(tr)) {
            if ("boolean".equals(tr)) {
               value = "false";
            }
         } else {
            value = "0";
         }

         Context cRet = CodeGeneratorManager.newContext();
         cRet.put("VAL", value);
         return CodeGenUtilities.getText(tReturn, cRet);
      } else {
         return null;
      }
   }

   private boolean isAbstract() {
      boolean contextIsInterface = ((PGClass)this.getParent()).isInterface();
      return contextIsInterface || this.modifiers != null && this.modifiers.contains("abstract");
   }
}
