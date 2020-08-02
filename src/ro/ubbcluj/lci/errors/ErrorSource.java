package ro.ubbcluj.lci.errors;

public interface ErrorSource {
   void addErrorListener(ErrorListener var1);

   void removeErrorListener(ErrorListener var1);

   void errorOccured(ErrorMessage var1);
}
