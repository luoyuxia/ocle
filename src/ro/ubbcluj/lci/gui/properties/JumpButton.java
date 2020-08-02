package ro.ubbcluj.lci.gui.properties;

import java.awt.Insets;
import javax.swing.JButton;

public class JumpButton extends JButton {
   protected Object theDestination;

   public JumpButton() {
      this("");
   }

   public JumpButton(Object element) {
      this.theDestination = element;
      super.setText(element.toString());
      Insets i = this.getInsets();
      i.left = 0;
      i.right = 0;
      this.setMargin(i);
      this.setHorizontalAlignment(2);
   }

   public Object getDestination() {
      return this.theDestination;
   }

   public void setDestination(Object dest) {
      this.theDestination = dest;
   }
}
