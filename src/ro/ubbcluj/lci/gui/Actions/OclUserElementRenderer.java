package ro.ubbcluj.lci.gui.Actions;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import ro.ubbcluj.lci.gui.tools.AbstractRenderer;
import ro.ubbcluj.lci.gui.tools.RendererInfo;
import ro.ubbcluj.lci.ocl.OclUserElement;
import ro.ubbcluj.lci.utils.Utils;

class OclUserElementRenderer extends DefaultListCellRenderer {
   OclUserElementRenderer() {
   }

   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      OclUserElement el = (OclUserElement)value;
      Object obj = el.getElement();
      String str = Utils.fullName(obj);
      RendererInfo ri = AbstractRenderer.getRendererInfo(obj);
      this.setText(str);
      this.setFont(this.getFont().deriveFont(ri.fontStyle));
      if (ri.icon != null) {
         this.setIcon(ri.icon);
      }

      return this;
   }
}
