package ro.ubbcluj.lci.gui.tools;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class ActionMenuItem extends JMenuItem implements PropertyChangeListener {
   public ActionMenuItem(Action a) {
      a.addPropertyChangeListener(this);
      this.setText(a.getValue("Name").toString());
      this.setEnabled(a.isEnabled());
      this.setAccelerator((KeyStroke)a.getValue("AcceleratorKey"));
      this.addActionListener(a);
      this.setIcon((Icon)a.getValue("SmallIcon"));
   }

   public void propertyChange(PropertyChangeEvent evt) {
      if ("Name".equals(evt.getPropertyName())) {
         this.setText((String)evt.getNewValue());
      } else if ("enabled".equals(evt.getPropertyName())) {
         Boolean b = (Boolean)evt.getNewValue();
         this.setEnabled(b.booleanValue());
      }

   }
}
