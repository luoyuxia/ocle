package ro.ubbcluj.lci.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import ro.ubbcluj.lci.gui.Actions.AToolsActions;

public class OclEvaluator extends MouseAdapter {
   public OclEvaluator() {
   }

   public void mouseClicked(MouseEvent evt) {
      if (evt.getClickCount() >= 2) {
         ((AToolsActions.EvaluateSelAction)AToolsActions.evaluateSelAction).evaluateSelection(evt);
      }

   }
}
