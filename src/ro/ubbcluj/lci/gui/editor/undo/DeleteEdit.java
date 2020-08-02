package ro.ubbcluj.lci.gui.editor.undo;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.editor.utils.Utilities;

public class DeleteEdit extends MyUndoableEdit {
   private String deletedText;
   private int caretPos;
   private int direction;
   private int start;
   private String presentationName;
   public static final int DEL_DIR_FORWARD = 0;
   public static final int DEL_DIR_BACKWARD = 1;

   public DeleteEdit(JEditTextArea a, boolean wasSelected, int dir, int caretP, String text, String pName) {
      super(a);
      this.bWasSelected = wasSelected;
      this.direction = dir;
      if (dir == 0) {
         this.start = caretP;
      } else {
         this.start = caretP - text.length();
      }

      this.caretPos = caretP;
      this.deletedText = text;
      this.presentationName = pName;
   }

   public DeleteEdit(JEditTextArea a, boolean wasSelected, int dir, int caretP, String text) {
      this(a, wasSelected, dir, caretP, text, "deletion");
   }

   public String getPresentationName() {
      return this.presentationName;
   }

   public boolean addEdit(UndoableEdit edit) {
      if (!(edit instanceof DeleteEdit)) {
         return false;
      } else {
         DeleteEdit dEdit = (DeleteEdit)edit;
         if (!this.presentationName.equalsIgnoreCase(dEdit.presentationName)) {
            return false;
         } else {
            boolean s1 = Utilities.startsWithSeparator(this.deletedText);
            boolean s2 = Utilities.startsWithSeparator(dEdit.deletedText);
            boolean e1 = Utilities.endsWithSeparator(this.deletedText);
            boolean e2 = Utilities.endsWithSeparator(dEdit.deletedText);
            if (this.direction == 0) {
               if (dEdit.direction == 0) {
                  if (this.caretPos != dEdit.caretPos || (!e1 || !s2) && (e1 || s2)) {
                     return false;
                  } else {
                     this.deletedText = this.deletedText + dEdit.deletedText;
                     this.bWasSelected = false;
                     this.length += dEdit.length;
                     dEdit.die();
                     return true;
                  }
               } else if (this.caretPos != dEdit.caretPos || (!e2 || !s1) && (e2 || s1)) {
                  return false;
               } else {
                  this.deletedText = dEdit.deletedText + this.deletedText;
                  this.bWasSelected = false;
                  this.start = dEdit.start;
                  this.length += dEdit.length;
                  dEdit.die();
                  return true;
               }
            } else if (dEdit.direction == 0) {
               if (this.start != dEdit.start || (!e1 || !s2) && (e1 || s2)) {
                  return false;
               } else {
                  this.deletedText = this.deletedText + dEdit.deletedText;
                  this.bWasSelected = false;
                  this.length += dEdit.length;
                  this.direction = 0;
                  dEdit.die();
                  return true;
               }
            } else if (this.start != dEdit.caretPos || (!e2 || !s1) && (e2 || s1)) {
               return false;
            } else {
               this.deletedText = dEdit.deletedText + this.deletedText;
               this.start = dEdit.start;
               this.length += dEdit.length;
               dEdit.die();
               return true;
            }
         }
      }
   }

   public void undo() throws CannotUndoException {
      super.undo();
      int h = this.deletedText.length();

      try {
         this.document.insertString(this.start, this.deletedText, (AttributeSet)null);
         if (this.bWasSelected) {
            this.component.select(this.start, this.start + h);
         } else {
            this.component.setCaretPosition(this.caretPos);
         }

         this.component.requestFocus();
      } catch (BadLocationException var3) {
         var3.printStackTrace();
      }

   }

   public void execute() {
      int h = this.deletedText.length();

      try {
         this.document.remove(this.start, h);
         this.component.requestFocus();
      } catch (BadLocationException var3) {
         var3.printStackTrace();
      }

   }
}
