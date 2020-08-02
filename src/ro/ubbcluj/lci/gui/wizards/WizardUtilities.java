package ro.ubbcluj.lci.gui.wizards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ro.ubbcluj.lci.gui.GUtils;

public class WizardUtilities {
   public WizardUtilities() {
   }

   public static JPanel getDefaultPanel(String description, String imageURL) {
      JPanel panel = new JPanel();
      JLabel lblDescription = new JLabel(description);
      lblDescription.setFont(new Font("SansSerif", 1, 12));
      panel.setLayout(new BorderLayout());
      panel.setBackground(Color.WHITE);
      panel.add(lblDescription, "West");
      JLabel lblImage;
      panel.add(lblImage = new JLabel(GUtils.loadIcon(imageURL)), "East");
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, lblImage.getPreferredSize().height + 2));
      return panel;
   }
}
