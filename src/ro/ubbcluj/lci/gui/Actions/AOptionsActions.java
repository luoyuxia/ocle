package ro.ubbcluj.lci.gui.Actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import ro.ubbcluj.lci.gui.editor.options.EditorOptionsDialog;
import ro.ubbcluj.lci.gui.mainframe.GApplication;

public class AOptionsActions {
   public static Action oclEditorOptionsAction = new AOptionsActions.OCLEditorOptionsAction();
   static Action[] actions;

   public AOptionsActions() {
   }

   public static Action[] getActions() {
      return actions;
   }

   static {
      actions = new Action[]{oclEditorOptionsAction};
   }

   public static class OCLEditorOptionsAction extends AbstractAction {
      static JDialog editorOptionsDialog;

      private OCLEditorOptionsAction() {
         this.newDialog();
         this.putValue("ActionCommandKey", "menubar.options.ocleditoroptions");
      }

      public void actionPerformed(ActionEvent ev) {
         this.newDialog();
         editorOptionsDialog.setVisible(true);
      }

      private void newDialog() {
         editorOptionsDialog = new EditorOptionsDialog(GApplication.frame, "OCL editor options", true);
         editorOptionsDialog.setLocationRelativeTo(GApplication.frame);
         editorOptionsDialog.setDefaultCloseOperation(2);
      }
   }
}
