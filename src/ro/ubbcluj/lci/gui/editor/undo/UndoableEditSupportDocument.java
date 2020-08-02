package ro.ubbcluj.lci.gui.editor.undo;

public interface UndoableEditSupportDocument {
   void undo();

   void redo();

   boolean canUndo();

   boolean canRedo();
}
