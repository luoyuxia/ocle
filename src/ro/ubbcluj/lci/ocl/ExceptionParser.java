package ro.ubbcluj.lci.ocl;

public class ExceptionParser extends ExceptionCompiler {
   private String msg;
   private OclToken tok;

   ExceptionParser(String _msg, OclToken _tok) {
      this.msg = _msg;
      this.tok = _tok;
   }

   public String getMessage() {
      return this.tok == null ? "Unexpected end of file" : this.msg;
   }

   public OclToken getToken() {
      return this.tok;
   }

   public int getStart() {
      return this.tok == null ? 0 : this.tok.getStart();
   }

   public int getStop() {
      return this.tok == null ? 0 : this.tok.getStop() + 1;
   }

   public String getFilename() {
      return this.tok == null ? "" : this.tok.getFilename();
   }

   public int getColumn() {
      return this.tok != null ? this.tok.getColumn() : 0;
   }

   public int getRow() {
      return this.tok != null ? this.tok.getRow() : 0;
   }
}
