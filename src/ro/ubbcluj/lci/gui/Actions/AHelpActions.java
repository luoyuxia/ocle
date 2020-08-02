package ro.ubbcluj.lci.gui.Actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import ro.ubbcluj.lci.gui.tools.AboutDlg;

public class AHelpActions {
   public AHelpActions() {
   }

   public static Action[] getActions() {
      Action[] actions = new Action[]{new AHelpActions.AboutAction(), new AHelpActions.HelpAction()};
      return actions;
   }

   public static class HelpAction extends AbstractAction {
      private HelpAction() {
         this.putValue("ActionCommandKey", "menubar.help.help_topics");
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent e) {
      }
   }

   public static class AboutAction extends AbstractAction {
      private AboutAction() {
         this.putValue("ActionCommandKey", "menubar.help.about");
      }

      public void actionPerformed(ActionEvent e) {
         AboutDlg.showDialog();
      }
   }
}
