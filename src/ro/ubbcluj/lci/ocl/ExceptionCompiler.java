package ro.ubbcluj.lci.ocl;

public abstract class ExceptionCompiler extends Exception {
   public ExceptionCompiler() {
   }

   public abstract int getStart();

   public abstract int getStop();

   public abstract int getColumn();

   public abstract int getRow();

   public abstract String getFilename();
}
