package ro.ubbcluj.lci.gui.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import ro.ubbcluj.lci.gui.editor.event.OverwriteModeListener;
import ro.ubbcluj.lci.gui.editor.jedit.DefaultInputHandler;
import ro.ubbcluj.lci.gui.editor.jedit.InputHandler;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.editor.undo.CutEdit;
import ro.ubbcluj.lci.gui.editor.undo.DeleteEdit;
import ro.ubbcluj.lci.gui.editor.undo.PasteEdit;

public class TextDocumentPad extends AbstractPad implements CaretListener, OverwriteModeListener {
   private TextDocumentModel model;
   private JPanel component;
   private JPanel statusPanel;
   private JLabel lblCaretCoords;
   private JLabel lblOverwrite;
   private JLabel lblReadOnly;
   private JEditTextArea view;
   private TextDocumentPadFactory factory;

   TextDocumentPad(TextDocumentPadFactory fac) {
      this.factory = fac;
      Component glue = Box.createHorizontalGlue();
      this.component = new JPanel();
      this.component.setLayout(new BoxLayout(this.component, 1));
      this.view = new JEditTextArea();
      this.view.setInputHandler(new DefaultInputHandler(new AutoIndentFormatter()));
      this.lblCaretCoords = new JLabel("1:1", 0);
      this.lblOverwrite = new JLabel("", 0);
      this.lblReadOnly = new JLabel("", 0);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int tmpWidth = screenSize.width / 20;
      this.statusPanel = new JPanel();
      this.statusPanel.setBorder(BorderFactory.createEtchedBorder());
      this.statusPanel.setMaximumSize(new Dimension(screenSize.width, 22));
      this.lblCaretCoords.setMaximumSize(new Dimension(tmpWidth, 25));
      this.lblOverwrite.setMaximumSize(new Dimension(tmpWidth, 25));
      this.lblReadOnly.setMaximumSize(new Dimension(tmpWidth, 25));
      this.statusPanel.setLayout(new BoxLayout(this.statusPanel, 0));
      this.statusPanel.add(glue);
      this.statusPanel.add(this.lblOverwrite);
      this.statusPanel.add(Box.createRigidArea(new Dimension(20, 0)));
      this.statusPanel.add(this.lblCaretCoords);
      this.statusPanel.add(Box.createRigidArea(new Dimension(20, 0)));
      this.statusPanel.add(this.lblReadOnly);
      this.component.add(this.view);
      this.component.add(this.statusPanel);
      this.view.addCaretListener(this);
      this.view.addOverwriteModeListener(this);
      this.updateOverwriteMode(this.view.isOverwriteEnabled());
   }

   public void caretUpdate(CaretEvent evt) {
      this.updateCaretCoords();
   }

   public void overwriteModeChanged(boolean ovm) {
      this.updateOverwriteMode(ovm);
   }

   private void setReadOnly(boolean ro) {
      this.view.setEditable(!ro);
      this.lblReadOnly.setText(ro ? "Read-only" : "Write enabled");
   }

   public int close() {
      if (!this.model.isDirty()) {
         this.factory.removePad(this);
         this.model.removeView(this);
         return 2;
      } else {
         int save = this.model.askSaveConfirmation();
         if (save == 0) {
            int ret = this.model.save();
            if (ret == -107) {
               return 4;
            } else if (ret == -104) {
               this.factory.removePad(this);
               this.model.removeView(this);
               return 2;
            } else {
               this.updateDescriptorsForClose();
               return 3;
            }
         } else if (save == 1) {
            this.factory.removePad(this);
            this.model.removeView(this);
            return 2;
         } else {
            return 4;
         }
      }
   }

   public TextDocumentModel getModel() {
      return this.model;
   }

   public void duplicateView(TextDocumentModel mdl) {
      this.setModel(mdl);
      mdl.addView(this);
   }

   public JComponent getComponent() {
      return this.component;
   }

   public int getType() {
      return 0;
   }

   public void setModel(TextDocumentModel mdl) {
      this.model = mdl;
      this.view.setDocument(this.model.getModel());
      this.setReadOnly(mdl.isReadOnly());
   }

   public JEditTextArea getView() {
      return this.view;
   }

   public void cut() {
      if (this.view.isEditable()) {
         this.copy();
         SyntaxDocument doc = this.view.getDocument();
         CutEdit ce = new CutEdit(this.view, this.view.getSelectionStart(), this.view.getCaretPosition());
         doc.addUndoableEdit(ce);
         ce.execute();
      }
   }

   public void paste() {
      String cc = getClipboardContents();
      if (cc == null) {
         this.view.getToolkit().beep();
      } else {
         StringBuffer bf = new StringBuffer();
         InputHandler iph = this.view.getInputHandler();
         SyntaxDocument doc = this.view.getDocument();
         int rc = iph.getRepeatCount();

         for(int i = 0; i < rc; ++i) {
            bf.append(cc);
         }

         int cp = this.view.getCaretPosition();
         int ss = this.view.getSelectionStart();
         int se = this.view.getSelectionEnd();
         if (ss != se) {
            DeleteEdit de = new DeleteEdit(this.view, true, cp == ss ? 0 : 1, cp, this.view.getSelectedText());
            doc.addUndoableEdit(de);
            de.execute();
         }

         PasteEdit pe = new PasteEdit(this.view, this.view.getCaretPosition(), bf.toString());
         doc.addUndoableEdit(pe);
         pe.execute();
      }
   }

   public void copy() {
      InputHandler iph = this.view.getInputHandler();
      int rc = iph.getRepeatCount();
      Clipboard systemClipboard = this.view.getToolkit().getSystemClipboard();
      String text = this.view.getSelectedText();
      if (text != null && text != "") {
         StringBuffer bf = new StringBuffer();

         for(int i = 0; i < rc; ++i) {
            bf.append(text);
         }

         systemClipboard.setContents(new StringSelection(bf.toString()), (ClipboardOwner)null);
      }
   }

   private static String getClipboardContents() {
      Toolkit tk = Toolkit.getDefaultToolkit();
      Clipboard clip = tk.getSystemClipboard();
      Transferable tf = clip.getContents(new Object());

      String cc;
      try {
         cc = (String)tf.getTransferData(DataFlavor.stringFlavor);
      } catch (Exception var5) {
         cc = null;
      }

      return cc;
   }

   public void activate() {
      this.view.requestFocus();
   }

   public void initialize() {
      this.view.setCaretPosition(0);
   }

   private void updateCaretCoords() {
      int cl = this.view.getCaretLine();
      int cc = this.view.getCaretPosition() - this.view.getLineStartOffset(cl);
      JLabel var10000 = this.lblCaretCoords;
      StringBuffer var10001 = new StringBuffer();
      ++cl;
      var10001 = var10001.append(cl).append(":");
      ++cc;
      var10000.setText(var10001.append(cc).toString());
   }

   private void updateOverwriteMode(boolean ovm) {
      this.lblOverwrite.setText(ovm ? "Overwrite" : "Insert");
   }

   private void updateDescriptorsForClose() {
      AbstractPad[] views = this.model.getViews();

      for(int i = 0; i < views.length; ++i) {
         views[i].getDescriptor().needsClosing(false);
      }

      views = null;
   }
}
