package ro.ubbcluj.lci.errors;

import ro.ubbcluj.lci.gui.tools.Error;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.utils.Utils;

public class EvaluationErrorMessage extends AbstractEvaluationError implements Error {
   public EvaluationErrorMessage(String stream, Object context, OclNode rule) {
      super(stream, context, rule);
      this.description = "Rule failed for context \"" + Utils.name(context) + "\":" + OclUtil.className(context.getClass());
   }
}
