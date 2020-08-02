package ro.ubbcluj.lci.gui.tools;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class ActionButton extends JButton implements PropertyChangeListener {
   public ActionButton(Action a) {
      a.addPropertyChangeListener(this);
      this.setText("");
      this.setEnabled(a.isEnabled());
      this.setIcon((Icon)a.getValue("SmallIcon"));
      this.setToolTipText((String)a.getValue("Name"));
      this.addActionListener(a);
   }

   public void propertyChange(PropertyChangeEvent evt) {
      if ("Name".equals(evt.getPropertyName())) {
         this.setToolTipText((String)evt.getNewValue());
      } else if ("enabled".equalsIgnoreCase(evt.getPropertyName())) {
         Boolean b = (Boolean)evt.getNewValue();
         this.setEnabled(b.booleanValue());
      }

   }
}
