package ro.ubbcluj.lci.gui.editor.event;

import java.util.EventObject;
import javax.swing.undo.UndoableEdit;

public class UndoEvent extends EventObject {
   protected UndoableEdit edit;

   public UndoEvent(Object src, UndoableEdit ed) {
      super(src);
      this.edit = ed;
   }

   public UndoableEdit getEdit() {
      return this.edit;
   }
}
