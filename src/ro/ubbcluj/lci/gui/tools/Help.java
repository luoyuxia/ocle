package ro.ubbcluj.lci.gui.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

public class Help extends JFrame {
   private static JTextPane content = new JTextPane();
   private JPanel mainPanel = new JPanel(new BorderLayout());
   private static JTextPane status = new JTextPane();
   private static JButton home;
   private static JButton back;
   private static JButton front;
   private static JButton find;
   private static final String HOME = "Home";
   private static final String BACK = "Back";
   private static final String FRONT = "Front";
   private static final String FIND = "Find";
   private static ArrayList links = new ArrayList();
   private static int index = 0;
   private static URL HOME_LINK = null;
   private static Help instance = new Help();

   private Help() {
      super("OCL Evaluator help");
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      int width = (int)screen.getWidth() * 3 / 4;
      int height = (int)screen.getHeight() * 3 / 4;
      this.setSize(width, height);
      this.setLocation((int)screen.getWidth() / 7, (int)screen.getHeight() / 7);
      this.setDefaultCloseOperation(2);
      content.setEditable(false);
      content.addHyperlinkListener(new Help.HelpLinkListener());
      status.setEditable(false);
      status.setBorder(BorderFactory.createEtchedBorder());
      status.setBackground(Color.lightGray);
      this.mainPanel.add(new JScrollPane(content), "Center");
      this.mainPanel.add(status, "South");
      this.mainPanel.add(this.getToolBar(), "North");
      this.setContentPane(this.mainPanel);
   }

   private JToolBar getToolBar() {
      JToolBar bar = new JToolBar(0);
      bar.setRollover(true);
      bar.setBorderPainted(false);
      ActionListener listener = new Help.ButtonActionListener();
      home = new JButton("");
      home.setActionCommand("Home");
      home.addActionListener(listener);
      back = new JButton("");
      back.setActionCommand("Back");
      back.addActionListener(listener);
      back.setEnabled(false);
      front = new JButton("");
      front.setActionCommand("Front");
      front.addActionListener(listener);
      front.setEnabled(false);
      find = new JButton("");
      find.setActionCommand("Find");
      find.addActionListener(listener);
      bar.add(home);
      bar.add(back);
      bar.add(front);
      bar.add(find);
      return bar;
   }

   private static void disableButtons() {
      front.setEnabled(false);
      back.setEnabled(false);
   }

   public static void showHelp(String startURL) {
      try {
         if (HOME_LINK == null) {
            HOME_LINK = (Help.class).getResource(startURL);
         }

         links.removeAll(links);
         links.add(HOME_LINK);
         index = 0;
         disableButtons();
         content.setPage(HOME_LINK);
         status.setText(HOME_LINK.toString());
         instance.show();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   class ButtonActionListener implements ActionListener {
      ButtonActionListener() {
      }

      public void actionPerformed(ActionEvent e) {
         if (e.getActionCommand().equals("Home")) {
            try {
               Help.disableButtons();
               Help.links.removeAll(Help.links);
               Help.links.add(Help.HOME_LINK);
               Help.index = 0;
               Help.content.setPage(Help.HOME_LINK);
               Help.status.setText(Help.HOME_LINK.toString());
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }

         if (e.getActionCommand().equals("Back")) {
            try {
               if (!Help.front.isEnabled()) {
                  Help.front.setEnabled(true);
               }

               if (Help.index > 0) {
                  Help.index--;
               }

               if (Help.index == 0) {
                  Help.back.setEnabled(false);
               }

               Help.content.setPage((URL)Help.links.get(Help.index));
               Help.status.setText(((URL)Help.links.get(Help.index)).toString());
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }

         if (e.getActionCommand().equals("Front")) {
            try {
               if (!Help.back.isEnabled()) {
                  Help.back.setEnabled(true);
               }

               Help.index++;
               if (Help.index == Help.links.size() - 1) {
                  Help.front.setEnabled(false);
               }

               Help.content.setPage((URL)Help.links.get(Help.index));
               Help.status.setText(((URL)Help.links.get(Help.index)).toString());
            } catch (Exception var3) {
            }
         }

         if (e.getActionCommand().equals("Find")) {
            Help var10000 = Help.this;
            FindDialog.showFindFor(Help.content, Help.this.getLocation(), Help.this.getSize());
         }

      }
   }

   class HelpLinkListener implements HyperlinkListener {
      HelpLinkListener() {
      }

      public void hyperlinkUpdate(HyperlinkEvent e) {
         if (e.getEventType() == EventType.ACTIVATED) {
            try {
               if (!Help.back.isEnabled()) {
                  Help.back.setEnabled(true);
               }

               URL url = e.getURL();
               Help.content.setPage(url);
               Help.status.setText(url.toString());
               if (!Help.links.contains(url)) {
                  Help.links.add(url);
                  Help.index++;
               } else {
                  Help.index = Help.links.indexOf(url);
                  if (Help.index == 0) {
                     Help.back.setEnabled(false);
                  }
               }
            } catch (Exception var3) {
               var3.printStackTrace();
            }
         }

         if (e.getEventType() == EventType.ENTERED) {
            Help.status.setText(e.getURL().toString());
         }

      }
   }
}
