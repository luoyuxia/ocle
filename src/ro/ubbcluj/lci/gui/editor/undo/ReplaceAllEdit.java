package ro.ubbcluj.lci.gui.editor.undo;

import javax.swing.undo.CompoundEdit;

public class ReplaceAllEdit extends CompoundEdit {
   public ReplaceAllEdit() {
   }

   public String getPresentationName() {
      return "replace all";
   }
}
