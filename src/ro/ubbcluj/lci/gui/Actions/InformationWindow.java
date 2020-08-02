package ro.ubbcluj.lci.gui.Actions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JWindow;
import ro.ubbcluj.lci.utils.Utils;

class InformationWindow extends JWindow {
   public InformationWindow(Frame owner, String msg) {
      super(owner);
      JLabel lbl;
      this.getContentPane().add(lbl = new JLabel(msg));
      lbl.setHorizontalAlignment(0);
      lbl.setBorder(BorderFactory.createEtchedBorder(0, Color.lightGray, Color.darkGray));
      Dimension ms = lbl.getPreferredSize();
      this.setSize(ms.width + 40, 4 * ms.height);
      this.getSize(ms);
      this.setLocation((Utils.screenSize.width - ms.width) / 2, (Utils.screenSize.height - ms.height) / 2);
   }
}
