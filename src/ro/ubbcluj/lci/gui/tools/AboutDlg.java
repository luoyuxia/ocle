package ro.ubbcluj.lci.gui.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import ro.ubbcluj.lci.gui.mainframe.GApplication;

public class AboutDlg extends JDialog implements ActionListener {
   private static final String HISTORY_FILE = "resources/credits.gif";
   private static final String NAME = "LCI OCL Evaluator";
   private static final String VER = "1.0 Beta";
   private static final String COPYRIGHT = "(C)2001-2002 LCI UBB, Cluj-Napoca, ROMANIA";
   private static final String ADDITIONAL = "Visit http://lci.cs.ubbcluj.ro for additional info.";

   private AboutDlg() {
      super(GApplication.frame);
      this.setTitle("About OCL Evaluator");
      this.setModal(true);
      this.setDefaultCloseOperation(0);
      this.setEnabled(true);
      this.setResizable(false);
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      int width = screen.width / 2;
      int height = screen.height / 2;
      this.setSize(475, 440);
      this.setDefaultCloseOperation(2);
      this.initDialog();
   }

   private void initDialog() {
      Point location = this.getParent().getLocation();
      Dimension psize = this.getParent().getSize();
      Dimension winsize = this.getSize();
      Point win_loc = new Point(Math.abs(psize.width / 2 - winsize.width / 2) + location.x, Math.abs(psize.height / 2 - winsize.height / 2) + location.y);
      JTabbedPane tabs = new JTabbedPane(1);
      tabs.addTab("About", this.getAboutPanel());
      JPanel aux = new JPanel(new BorderLayout());
      aux.add(tabs, "Center");
      this.getContentPane().setLayout(new BorderLayout());
      this.getContentPane().add(aux, "Center");
      this.setLocation(win_loc);
   }

   private String parseFile() {
      String content = "";

      try {
         InputStream in = (AboutDlg.class).getResourceAsStream("resources/credits.gif");
         if (in != null) {
            byte[] buf = new byte[in.available()];
            int red = in.read(buf);

            for(int i = 0; i < red / 2; ++i) {
               byte a = 0;
               byte b = 0;

               for(int j = 0; j < 8; ++j) {
                  a = (byte)(a | (j < 4 ? (buf[i] & 1 << j) << 7 - (j << 1) : (buf[i] & 1 << j) >> (j << 1) - 7));
                  b = (byte)(b | (j < 4 ? (buf[red - i - 1] & 1 << j) << 7 - (j << 1) : (buf[red - 1 - i] & 1 << j) >> (j << 1) - 7));
               }

               buf[i] = b;
               buf[red - 1 - i] = a;
            }

            content = new String(buf, 0, red);
            in.close();
         }
      } catch (Exception var9) {
         JOptionPane.showMessageDialog(this, "Cannot open file credits.gif", "Error", 0);
      }

      return content;
   }

   private JPanel getVersionsPanel() {
      JPanel main_panel = new JPanel(new BorderLayout());
      JTextArea text = new JTextArea();
      text.setFont(new Font("Courier New", 0, 11));
      text.setEditable(false);
      text.setText(this.parseFile());
      main_panel.add(new JScrollPane(text), "Center");
      return main_panel;
   }

   private JPanel getAboutPanel() {
      JPanel main_panel = new JPanel(new BorderLayout());
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, 1));
      JLabel image = new JLabel(new ImageIcon(this.getClass().getResource("/images/Splash.gif")));
      main_panel.add(image, "North");
      panel.add(Box.createRigidArea(new Dimension(0, 10)));
      JLabel addinfo = new JLabel("This work was partially supported by the NEPTUNE IST 1999 - 20017 Project");
      addinfo.setAlignmentX(0.5F);
      addinfo.setFont(new Font("SansSerif", 0, 12));
      panel.add(addinfo, "South");
      panel.add(Box.createRigidArea(new Dimension(0, 15)));
      JButton ok = new JButton("OK");
      ok.setPreferredSize(new Dimension(40, 30));
      ok.setAlignmentX(0.5F);
      ok.addActionListener(this);
      panel.add(ok);
      main_panel.add(panel, "Center");
      return main_panel;
   }

   public static void showDialog() {
      (new AboutDlg()).setVisible(true);
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().equals("OK")) {
         this.dispose();
      }

   }
}
