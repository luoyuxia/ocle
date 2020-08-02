package ro.ubbcluj.lci.errors;

public abstract class ErrorVisitor {
   public ErrorVisitor() {
   }

   public abstract void visitCompilationError(CompilationErrorMessage var1);

   public abstract void visitEvaluationError(AbstractEvaluationError var1);

   public abstract void visitTextLocalizableErrorMessage(TextLocalizableErrorMessage var1);
}
