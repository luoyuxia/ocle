package ro.ubbcluj.lci.gui.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.html.HTMLDocument;

public class FindDialog extends JDialog {
   private static Icon cb;
   private static Icon cbs;
   private static FindDialog instance;
   private static JTextPane editor;
   private JTextField search = new JTextField();
   private JCheckBox chb = new JCheckBox("Ignore case");
   private static String toFind;
   private static String doc;
   private static String noCasedoc;
   private static int index;

   private FindDialog() {
      this.setTitle("Find text in current topic");
      this.setModal(true);
      this.setSize(300, 110);
      this.setDefaultCloseOperation(2);
      this.setResizable(false);
      this.initDialog();
   }

   private void initDialog() {
      FindDialog.ButtonListener listener = new FindDialog.ButtonListener();
      JPanel main_panel = new JPanel(new FlowLayout(1));
      main_panel.add(Box.createRigidArea(new Dimension(0, 30)));
      JPanel left = new JPanel();
      left.setLayout(new BoxLayout(left, 0));
      JLabel text = new JLabel("Text to find:");
      text.setFont(new Font("SanSerif", 0, 15));
      left.add(text);
      this.search.setEditable(true);
      this.search.setPreferredSize(new Dimension(200, 15));
      this.search.setBorder(new LineBorder(Color.black));
      this.search.setFont(new Font("SanSerif", 0, 15));
      left.add(Box.createRigidArea(new Dimension(5, 0)));
      left.add(this.search);
      main_panel.add(left);
      main_panel.add(Box.createRigidArea(new Dimension(0, 30)));
      JPanel rigth = new JPanel();
      rigth.setLayout(new BoxLayout(rigth, 0));
      JButton find = new JButton("Find Next");
      find.setActionCommand("FIND");
      find.addActionListener(listener);
      rigth.add(find);
      JButton cancel = new JButton("Cancel");
      cancel.setActionCommand("CANCEL");
      cancel.addActionListener(listener);
      rigth.add(cancel);
      JPanel middle = new JPanel();
      middle.setLayout(new BoxLayout(middle, 0));
      this.chb.setIcon(cb);
      this.chb.setSelectedIcon(cbs);
      this.chb.setSelected(false);
      this.chb.setMargin(new Insets(0, 0, 0, 0));
      middle.add(this.chb);
      main_panel.add(middle);
      main_panel.add(rigth);
      this.setContentPane(main_panel);
   }

   public static void showFindFor(JTextPane parent, Point plocation, Dimension psize) {
      editor = parent;
      index = 0;
      if (parent != null) {
         int incx = (psize.width - instance.getSize().width) / 2;
         int incy = (psize.height - instance.getSize().height) / 2;
         instance.setLocation(plocation.x + incx, plocation.y + incy);
         int startDoc = editor.getDocument().getStartPosition().getOffset();
         int endDoc = editor.getDocument().getEndPosition().getOffset();

         try {
            doc = ((HTMLDocument)editor.getDocument()).getText(startDoc, endDoc);
            noCasedoc = doc.toLowerCase();
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         editor.setSelectionColor(Color.red);
         editor.getCaret().setSelectionVisible(true);
      }

      instance.show();
   }

   private void findText() {
      int idx;
      if (!this.chb.isSelected()) {
         idx = doc.indexOf(toFind, index);
      } else {
         String toFindAux = toFind.toLowerCase();
         idx = noCasedoc.indexOf(toFindAux, index);
      }

      if (idx == -1) {
         JOptionPane.showMessageDialog(this, "Text: " + toFind + " not found !");
      } else {
         int end = idx + toFind.length();
         editor.select(idx, end);
         index = end + 1;
      }
   }

   static {
      cb = new ImageIcon("");
      cbs = new ImageIcon("");
      instance = new FindDialog();
      toFind = null;
      doc = null;
      noCasedoc = null;
      index = 0;
   }

   class ButtonListener implements ActionListener {
      ButtonListener() {
      }

      public void actionPerformed(ActionEvent e) {
         if (e.getActionCommand().equals("CANCEL")) {
            FindDialog.instance.dispose();
         }

         if (e.getActionCommand().equals("FIND")) {
            String text = FindDialog.this.search.getText().trim();
            if (text.equals("")) {
               return;
            }

            if (!text.equals(FindDialog.toFind)) {
               FindDialog.index = 0;
               FindDialog.toFind = text;
            }

            FindDialog.this.findText();
         }

      }
   }
}
