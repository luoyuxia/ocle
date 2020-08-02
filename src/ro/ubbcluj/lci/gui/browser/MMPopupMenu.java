package ro.ubbcluj.lci.gui.browser;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import ro.ubbcluj.lci.errors.CompilationErrorMessage;
import ro.ubbcluj.lci.gui.browser.BTree.BrowserFilterDialog;
import ro.ubbcluj.lci.gui.editor.Editor;
import ro.ubbcluj.lci.gui.editor.TextDocumentPad;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.gui.tools.FeatureLocator;
import ro.ubbcluj.lci.ocl.ExceptionCompiler;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.utils.SampleLocator;

public class MMPopupMenu extends GBPopupMenu {
   public MMPopupMenu(GBrowser browser) {
      super(browser);
   }

   protected JPopupMenu createPopup(Object element) {
      JPopupMenu pum = new JPopupMenu();
      pum.add(new GMenuItem(new MMPopupMenu.BrowserFilterAction()));
      pum.add(new GMenuItem(new GBPopupMenu.ExpandTreeNodeAction()));
      pum.add(new GMenuItem(new GBPopupMenu.ColapseTreeNodeAction()));
      pum.addSeparator();
      pum.add(new GMenuItem(new GBPopupMenu.ShowInDiagramAction()));
      if (element instanceof Operation) {
         pum.add(new MMPopupMenu.SpecificationAction(element));
         pum.add(new MMPopupMenu.GoToSampleAction((Operation)element));
      }

      pum.add(new GMenuItem(new GBPopupMenu.FindAction()));
      return pum;
   }

   private class GoToSampleAction extends AbstractAction {
      private Operation element;

      public GoToSampleAction(Operation elem) {
         this.putValue("Name", "Go to sample");
         this.element = elem;
      }

      public void actionPerformed(ActionEvent evt) {
         Thread locator = new Thread("Sample locator") {
            public void run() {
               SampleLocator sl = SampleLocator.getLocator();
               GMainFrame mf = GMainFrame.getMainFrame();

               try {
                  mf.setCursor(new Cursor(3));
                  GRepository rep = GRepository.getInstance();
                  GUMLModel sampleModel = sl.getSampleModel();
                  if (sampleModel != rep.getUsermodel()) {
                     rep.changeUsermodel(sampleModel);
                  }

                  OclNode location = sl.getLocation(GoToSampleAction.this.element);
                  if (location == null) {
                     JOptionPane.showMessageDialog(mf, "Sample not found for operation " + GoToSampleAction.this.element.getName() + "().", "Information", 1);
                  } else {
                     String fileName = location.getFilename();
                     Editor ed = GApplication.getApplication().getEditor();
                     if (ed.activateFileView(fileName) == null) {
                        JOptionPane.showMessageDialog(mf, "Could not open file " + fileName, "Error", 0);
                     } else {
                        JEditTextArea ta = ((TextDocumentPad)ed.getActivePad()).getView();
                        ta.select(location.getStart(), location.getStop() + 1);
                        ta.repaint();
                     }
                  }
               } catch (Exception var12) {
                  if (var12 instanceof ExceptionCompiler) {
                     ExceptionCompiler ec = (ExceptionCompiler)var12;
                     CompilationErrorMessage cem = new CompilationErrorMessage(ec.getFilename(), ec.getStart(), ec.getStop(), ec.getColumn(), ec.getRow(), ec.getMessage());
                     mf.updateMessages((Object)cem);
                  } else {
                     mf.updateMessages((Object)var12.getMessage());
                  }
               } finally {
                  mf.setCursor(new Cursor(0));
               }

            }
         };
         locator.start();
      }
   }

   private class SpecificationAction extends AbstractAction {
      private Object element;

      public SpecificationAction(Object element) {
         this.putValue("Name", "Specification");
         this.element = element;
      }

      public void actionPerformed(ActionEvent evt) {
         Thread locator = new Thread("locator") {
            public void run() {
               OclNode def = FeatureLocator.getLocator().getDefinition((Operation)SpecificationAction.this.element);
               if (def != null) {
                  Editor ed = GApplication.getApplication().getEditor();
                  if (ed.activateFileView(def.getFilename()) == null) {
                     JOptionPane.showMessageDialog(GApplication.frame, "Could not open file: " + def.getFilename());
                  } else {
                     JEditTextArea area = ((TextDocumentPad)ed.getView().getActivePad()).getView();
                     area.select(def.getStart(), def.getStop() + 1);
                     area.repaint();
                  }
               } else {
                  JOptionPane.showMessageDialog(GApplication.frame, "Operation not found:" + ((Operation)SpecificationAction.this.element).getName(), "Message", 1);
               }

            }
         };
         locator.start();
      }
   }

   private class BrowserFilterAction extends GBPopupMenu.BrowserFilterAction {
      private BrowserFilterAction() {
         super();
      }

      public void actionPerformed(ActionEvent evt) {
         try {
            BrowserFilterDialog dialog = new BrowserFilterDialog(MMPopupMenu.this.gbrowser.getFilter());
            dialog.setBounds(300, 200, 580, 500);
            dialog.setVisible(true);
            if (dialog.wereChangesMade()) {
               MMPopupMenu.this.gbrowser.setFilter(dialog.getFilter());
            }
         } catch (Exception var3) {
         }

      }
   }
}
