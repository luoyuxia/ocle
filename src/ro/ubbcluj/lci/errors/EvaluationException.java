package ro.ubbcluj.lci.errors;

import ro.ubbcluj.lci.gui.tools.Warning;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.utils.Utils;

public class EvaluationException extends AbstractEvaluationError implements Warning {
   public EvaluationException(String stream, Object context, OclNode rule, String msg) {
      super(stream, context, rule);
      this.description = "Evaluation exception for context \"" + Utils.name(context) + "\":" + OclUtil.className(context.getClass()) + "[" + msg + "]";
   }

   public void accept(ErrorVisitor v) {
      v.visitEvaluationError(this);
   }
}
