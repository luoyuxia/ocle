package ro.ubbcluj.lci.gui.mainframe;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ListRenderer extends JLabel implements ListCellRenderer {
   public ListRenderer() {
      this.setOpaque(true);
   }

   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      JLabel label = (JLabel)value;
      String name = label.getIcon().toString();
      if (isSelected) {
         if (name.indexOf("Inform") != -1) {
            this.setBackground(new Color(29, 206, 220));
            this.setForeground(Color.black);
         }

         if (name.indexOf("Error") != -1) {
            this.setBackground(new Color(251, 136, 130));
            this.setForeground(Color.black);
         }

         if (name.indexOf("Warn") != -1) {
            this.setBackground(new Color(230, 193, 85));
            this.setForeground(Color.black);
         }

         if (name.indexOf("Question") != -1) {
            this.setBackground(new Color(94, 236, 168));
            this.setForeground(Color.black);
         }
      } else {
         this.setBackground(list.getBackground());
         this.setForeground(list.getForeground());
      }

      Icon icon = label.getIcon();
      if (name.indexOf("Inform") != -1) {
         this.setText(label.getText());
      }

      if (name.indexOf("Error") != -1) {
         this.setText(" (Error)  " + label.getText());
      }

      if (name.indexOf("Warn") != -1) {
         this.setText(" (Warning)  " + label.getText());
      }

      if (name.indexOf("Question") != -1) {
         this.setText(label.getText());
      }

      this.setIcon(icon);
      return this;
   }
}
