package ro.ubbcluj.lci.gui.mainframe;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class GQuickView extends GView {
   JComponent quick_view = new JPanel();

   public GQuickView(GAbstractProject project) {
      this.setUserObject(project);
   }

   public JComponent getComponent() {
      return this.quick_view;
   }
}
