package ro.ubbcluj.lci.gui.editor.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import ro.ubbcluj.lci.gui.editor.SyntaxDocument;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;

public abstract class MyUndoableEdit extends AbstractUndoableEdit {
   protected SyntaxDocument document;
   protected int length = 1;
   protected JEditTextArea component;
   protected boolean bWasSelected;

   public MyUndoableEdit(JEditTextArea area) {
      this.component = area;
      this.document = area.getDocument();
   }

   public abstract void execute();

   public void redo() throws CannotRedoException {
      super.redo();
      this.execute();
   }

   public int length() {
      return this.length;
   }
}
