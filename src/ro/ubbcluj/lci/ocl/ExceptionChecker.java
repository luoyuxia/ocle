package ro.ubbcluj.lci.ocl;

public class ExceptionChecker extends ExceptionCompiler {
   private String msg;
   private OclNode node;

   ExceptionChecker(String _msg, OclNode _node) {
      this.msg = _msg;
      this.node = _node;
   }

   public String getMessage() {
      return this.msg;
   }

   public OclNode getNode() {
      return this.node;
   }

   public int getStart() {
      return this.node.firstToken().getStart();
   }

   public int getStop() {
      return this.node.lastToken().getStop() + 1;
   }

   public String getFilename() {
      return this.node.firstToken().getFilename();
   }

   public int getColumn() {
      return this.node.firstToken().getColumn();
   }

   public int getRow() {
      return this.node.firstToken().getRow();
   }
}
