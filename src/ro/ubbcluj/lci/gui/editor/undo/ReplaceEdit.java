package ro.ubbcluj.lci.gui.editor.undo;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotUndoException;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;

public class ReplaceEdit extends MyUndoableEdit {
   private String textToFind;
   private String newText;
   private int start;
   private int caretPos;

   public ReplaceEdit(JEditTextArea comp, int startPos, int caretPos, String textToFind, String newText) {
      super(comp);
      this.start = startPos;
      this.caretPos = caretPos;
      this.textToFind = textToFind;
      this.newText = newText;
   }

   public void execute() {
      try {
         this.document.remove(this.start, this.textToFind.length());
         this.document.insertString(this.start, this.newText, (AttributeSet)null);
         if (this.caretPos > this.start) {
            this.component.setCaretPosition(this.start + this.newText.length());
         } else {
            this.component.setCaretPosition(this.start);
         }

         this.component.requestFocus();
      } catch (BadLocationException var2) {
         var2.printStackTrace();
      }

      this.component.requestFocus();
   }

   public void undo() throws CannotUndoException {
      super.undo();

      try {
         this.document.remove(this.start, this.newText.length());
         this.document.insertString(this.start, this.textToFind, (AttributeSet)null);
         this.component.setCaretPosition(this.caretPos);
         this.component.requestFocus();
      } catch (BadLocationException var2) {
         var2.printStackTrace();
      }

   }

   public String getPresentationName() {
      return "replace";
   }
}
