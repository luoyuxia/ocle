package ro.ubbcluj.lci.gui.editor.undo;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotUndoException;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;

public class CutEdit extends MyUndoableEdit {
   private String cutText;
   private int start;
   private int caretPos;

   public CutEdit(JEditTextArea comp, int start, int caretPos) {
      super(comp);
      this.cutText = this.component.getSelectedText();
      this.start = start;
      this.caretPos = caretPos;
   }

   public String getPresentationName() {
      return "Cut";
   }

   public void undo() throws CannotUndoException {
      super.undo();

      try {
         this.document.insertString(this.start, this.cutText, (AttributeSet)null);
         if (this.start < this.caretPos) {
            this.component.select(this.start, this.caretPos);
         } else {
            this.component.select(this.start + this.cutText.length(), this.start);
         }

         this.component.requestFocus();
      } catch (BadLocationException var2) {
         var2.printStackTrace();
      }

   }

   public void execute() {
      try {
         this.document.remove(this.start, this.cutText.length());
         this.component.setCaretPosition(this.start);
         this.component.requestFocus();
      } catch (BadLocationException var2) {
         var2.printStackTrace();
      }

   }
}
