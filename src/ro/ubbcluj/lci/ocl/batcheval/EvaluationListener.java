package ro.ubbcluj.lci.ocl.batcheval;

import java.util.EventListener;

public interface EvaluationListener extends EventListener {
   void evaluationCancelled(EvaluationEvent var1);

   void evaluationCompleted(EvaluationEvent var1);
}
