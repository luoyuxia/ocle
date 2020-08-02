package ro.ubbcluj.lci.gui.editor.undo;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotUndoException;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;

public class PasteEdit extends MyUndoableEdit {
   private String text;
   private int start;

   public PasteEdit(JEditTextArea comp, int start, String text) {
      super(comp);
      this.start = start;
      this.text = text;
   }

   public String getPresentationName() {
      return "paste";
   }

   public void undo() throws CannotUndoException {
      super.undo();

      try {
         this.document.remove(this.start, this.text.length());
         this.component.requestFocus();
      } catch (BadLocationException var2) {
         var2.printStackTrace();
      }

   }

   public void execute() {
      try {
         this.document.insertString(this.start, this.text, (AttributeSet)null);
         this.component.requestFocus();
      } catch (BadLocationException var2) {
         var2.printStackTrace();
      }

   }
}
