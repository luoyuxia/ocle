package ro.ubbcluj.lci.gui.Actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import ro.ubbcluj.lci.gui.editor.AbstractPad;
import ro.ubbcluj.lci.gui.editor.TextDocumentPad;
import ro.ubbcluj.lci.gui.mainframe.GAbstractProject;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.tools.search.FindReplaceDialog;
import ro.ubbcluj.lci.utils.Utils;

public class AEditActions {
   public static final Action clearLogAction = new AEditActions.ClearLogAction();
   public static final Action undoAction = new AEditActions.UndoAction();
   public static final Action redoAction = new AEditActions.RedoAction();
   public static final Action copyAction = new AEditActions.CopyAction();
   public static final Action cutAction = new AEditActions.CutAction();
   public static final Action pasteAction = new AEditActions.PasteAction();
   public static final Action findAction = new AEditActions.FindAction();
   public static final Action selectAllAction = new AEditActions.SelectAllAction();
   private static Action[] actions;

   public AEditActions() {
   }

   public static Action[] getActions() {
      return actions;
   }

   static {
      actions = new Action[]{clearLogAction, undoAction, redoAction, copyAction, cutAction, pasteAction, findAction, selectAllAction};
   }

   public static class ClearLogAction extends AbstractAction {
      private ClearLogAction() {
         this.putValue("ActionCommandKey", "menubar.edit.clearlog");
      }

      public void actionPerformed(ActionEvent ev) {
         GMainFrame.getMainFrame().clearLog();
      }
   }

   private static class FindAction extends AbstractAction {
      static FindReplaceDialog dlg = null;

      public FindAction() {
         this.putValue("ActionCommandKey", "menubar.edit.find");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(70, 2));
      }

      public void actionPerformed(ActionEvent ev) {
         if (dlg == null) {
            dlg = new FindReplaceDialog(GApplication.frame, "Find / Replace", false);
            dlg.setSize(Utils.screenSize.width / 2, Utils.screenSize.height / 2);
            dlg.setLocationRelativeTo(GApplication.frame);
            dlg.setDefaultFilter("*.ocl");
         }

         GRepository rep = GRepository.getInstance();
         GAbstractProject activeP = rep.getProject();
         AbstractPad p = GMainFrame.getMainFrame().getViewContainer().getActivePad();
         File[] projectFiles;
         if (activeP != null) {
            projectFiles = (File[])activeP.getFileBrowser().getAllConstraintFiles().toArray(new File[0]);
         } else {
            projectFiles = new File[0];
         }

         dlg.setTargets(p instanceof TextDocumentPad ? (TextDocumentPad)p : null, projectFiles);
         dlg.setVisible(true);
      }
   }

   private static class PasteAction extends AbstractAction {
      public PasteAction() {
         this.putValue("ActionCommandKey", "menubar.edit.paste");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(86, 2));
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent ev) {
         if (this.isEnabled()) {
            TextDocumentPad pad = (TextDocumentPad)GMainFrame.getMainFrame().getViewContainer().getActivePad();
            pad.paste();
         }
      }
   }

   private static class CutAction extends AbstractAction {
      public CutAction() {
         this.putValue("ActionCommandKey", "menubar.edit.cut");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(88, 2));
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent ev) {
         if (this.isEnabled()) {
            TextDocumentPad pad = (TextDocumentPad)GApplication.getApplication().getEditor().getActivePad();
            pad.cut();
         }
      }
   }

   private static class CopyAction extends AbstractAction {
      public CopyAction() {
         this.putValue("ActionCommandKey", "menubar.edit.copy");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(67, 2));
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent ev) {
         if (this.isEnabled()) {
            TextDocumentPad pad = (TextDocumentPad)GApplication.getApplication().getEditor().getActivePad();
            pad.copy();
            pad.getView().requestFocus();
         }
      }
   }

   private static class RedoAction extends AbstractAction {
      public RedoAction() {
         this.putValue("ActionCommandKey", "menubar.edit.redo");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(89, 2));
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent ev) {
         if (this.isEnabled()) {
            try {
               GApplication.getApplication().getEditor().redo();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      }
   }

   private static class UndoAction extends AbstractAction {
      public UndoAction() {
         this.putValue("ActionCommandKey", "menubar.edit.undo");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(90, 2));
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent ev) {
         if (this.isEnabled()) {
            try {
               GApplication.getApplication().getEditor().undo();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      }
   }

   private static class SelectAllAction extends AbstractAction {
      public SelectAllAction() {
         this.putValue("ActionCommandKey", "menubar.edit.selectall");
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent ev) {
         ((TextDocumentPad)GApplication.getApplication().getEditor().getActivePad()).getView().selectAll();
      }
   }
}
