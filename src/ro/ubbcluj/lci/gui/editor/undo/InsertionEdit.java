package ro.ubbcluj.lci.gui.editor.undo;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.editor.utils.Utilities;

public class InsertionEdit extends MyUndoableEdit {
   private int caretPos;
   private String text;
   private String presentationName;

   public InsertionEdit(JEditTextArea comp, int pos, String typed, String pName) {
      super(comp);
      if (pos < 0) {
         throw new IllegalArgumentException("Bad location");
      } else {
         this.text = typed;
         this.caretPos = pos;
         this.presentationName = pName;
      }
   }

   public InsertionEdit(JEditTextArea comp, int pos, String typed) {
      this(comp, pos, typed, "insertion");
   }

   public String getPresentationName() {
      return this.presentationName;
   }

   public boolean addEdit(UndoableEdit edit) {
      if (!(edit instanceof InsertionEdit)) {
         return false;
      } else {
         InsertionEdit tEdit = (InsertionEdit)edit;
         if (!this.presentationName.equalsIgnoreCase(tEdit.presentationName)) {
            return false;
         } else if (this.caretPos + this.text.length() != tEdit.caretPos) {
            return false;
         } else {
            boolean e1 = Utilities.endsWithSeparator(this.text);
            boolean s2 = Utilities.startsWithSeparator(tEdit.text);
            if ((!e1 || !s2) && (e1 || s2)) {
               return false;
            } else {
               this.text = this.text + tEdit.text;
               this.length += tEdit.length;
               tEdit.die();
               return true;
            }
         }
      }
   }

   public void undo() throws CannotUndoException {
      super.undo();

      try {
         this.document.remove(this.caretPos, this.text.length());
         this.component.setCaretPosition(this.caretPos);
         this.component.requestFocus();
      } catch (BadLocationException var2) {
         var2.printStackTrace();
      }

   }

   public void execute() {
      try {
         this.document.insertString(this.caretPos, this.text, (AttributeSet)null);
         this.component.requestFocus();
      } catch (BadLocationException var2) {
         var2.printStackTrace();
      }

   }
}
