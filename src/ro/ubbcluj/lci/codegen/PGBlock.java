package ro.ubbcluj.lci.codegen;

import org.apache.velocity.Template;

public final class PGBlock extends PGElement {
   private StringBuffer body;
   private boolean delimited = false;
   private String blockStart;

   public PGBlock(PGElement parent, boolean delimitedBlock, boolean acceptChildren) {
      super(parent);
      this.delimited = delimitedBlock;
      this.setAllowsChildren(acceptChildren);
   }

   public void setBody(StringBuffer b) {
      if (this.getAllowsChildren()) {
         throw new RuntimeException("This block cannot have a body");
      } else {
         this.body = b;
      }
   }

   public String getBody() {
      return this.body.toString();
   }

   public void setStartText(String start) {
      if (!this.delimited) {
         throw new RuntimeException("This block does not allow a start text");
      } else {
         this.blockStart = start;
      }
   }

   protected StringBuffer getDeclarationCode() {
      StringBuffer result = new StringBuffer();
      if (this.blockStart != null) {
         result.append(this.blockStart);
      }

      if (this.delimited) {
         Template t = CodeGeneratorManager.getTemplate("block_start");
         result.append(CodeGenUtilities.getText(t, CodeGeneratorManager.newContext()));
      }

      return result;
   }

   protected StringBuffer getFinalizerCode() {
      StringBuffer result = null;
      if (this.delimited) {
         result = super.getFinalizerCode();
      }

      return result;
   }

   protected StringBuffer getChildrenCode() {
      StringBuffer result = this.body;
      if (this.getAllowsChildren()) {
         result = super.getChildrenCode();
      }

      return result;
   }
}
