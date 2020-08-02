package ro.ubbcluj.lci.errors;

import ro.ubbcluj.lci.ocl.OclNode;

public class AbstractEvaluationError extends TextLocalizableErrorMessage {
   private Object context;
   private OclNode rule;

   public AbstractEvaluationError(Object src, Object context, OclNode rule) {
      super(src);
      this.context = context;
      this.rule = rule;
      this.start = rule.getStart();
      this.stop = rule.getStop() + 1;
   }

   public Object getContext() {
      return this.context;
   }

   public OclNode getRule() {
      return this.rule;
   }

   public void accept(ErrorVisitor v) {
      v.visitEvaluationError(this);
   }
}
