package ro.ubbcluj.lci.gui.tools.selectionmenu;

import java.util.EventObject;

public class MenuSelectionEvent extends EventObject {
   private Object selectedValue = null;

   public MenuSelectionEvent(Object src, Object selectedValue) {
      super(src);
      this.selectedValue = selectedValue;
   }

   public Object getSelectedValue() {
      return this.selectedValue;
   }
}
