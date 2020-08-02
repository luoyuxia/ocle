package ro.ubbcluj.lci.gui.mainframe;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OutputPane extends JPanel implements ListSelectionListener {
   private Vector DATA;
   private GridLayout Layout = new GridLayout(1, 1);
   private JList list = new JList();
   private JScrollPane scrl;
   private JPanel but_pane = new JPanel(new FlowLayout());
   private ArrayList objects = new ArrayList();
   private Object selected = null;

   public void valueChanged(ListSelectionEvent e) {
      JList the_list = (JList)e.getSource();
      if (the_list.getSelectedIndex() > -1) {
         int elindex = the_list.getSelectedIndex();
         this.selected = this.objects.get(elindex);
         String mtype = this.getMessageType();
         if (mtype.indexOf("Information") != -1) {
            the_list.setToolTipText("Information:");
         } else if (mtype.indexOf("Question") != -1) {
            the_list.setToolTipText("Question:");
         } else if (!mtype.equals("Undefined")) {
            the_list.setToolTipText("Click to go to " + mtype + " location");
         }

         if (!this.but_pane.isVisible()) {
            this.but_pane.setVisible(true);
         }
      } else {
         this.but_pane.setVisible(false);
      }

   }

   public OutputPane() {
      try {
         this.jbInit();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void jbInit() throws Exception {
      this.DATA = new Vector();
      this.setLayout(this.Layout);
      this.list.setSelectionMode(0);
      ListRenderer lr = new ListRenderer();
      this.list.setCellRenderer(lr);
      this.list.addListSelectionListener(this);
      this.scrl = new JScrollPane(this.list);
      this.add(this.scrl);
   }

   private String getMessageType() {
      String rez = "Undefined";
      if (this.list.getSelectedIndex() != -1) {
         JLabel l = (JLabel)this.list.getSelectedValue();
         String icon_name = l.getIcon().toString();
         if (icon_name.indexOf("Inform") != -1) {
            rez = "Information";
         }

         if (icon_name.indexOf("Error") != -1) {
            rez = "Error";
         }

         if (icon_name.indexOf("Question") != -1) {
            rez = "Question";
         }

         if (icon_name.indexOf("Warn") != -1) {
            rez = "Warning";
         }
      }

      return rez;
   }

   private void autoScroll(int i) {
      this.list.setSelectedIndex(this.DATA.size() - 1);
      this.list.ensureIndexIsVisible(this.DATA.size() - 1);
   }

   public void addInfo(Object msg) {
      this.objects.add(msg);
      ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/Inform.gif"));
      JLabel l = new JLabel(msg != null ? msg.toString() : "null", icon, 2);
      this.DATA.add(l);
      this.list.setListData(this.DATA.toArray());
      this.autoScroll(this.DATA.size() - 1);
   }

   public void addError(Object msg) {
      this.objects.add(msg);
      ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/Error.gif"));
      JLabel l = new JLabel(msg.toString(), icon, 11);
      this.DATA.add(l);
      this.list.setListData(this.DATA.toArray());
      this.autoScroll(this.DATA.size() - 1);
   }

   public void addWarning(Object msg) {
      this.objects.add(msg);
      ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/Warn.gif"));
      JLabel l = new JLabel(msg.toString(), icon, 2);
      this.DATA.add(l);
      this.list.setListData(this.DATA.toArray());
      this.autoScroll(this.DATA.size() - 1);
   }

   public void addQuestion(Object msg) {
      this.objects.add(msg);
      ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/Question.gif"));
      JLabel l = new JLabel(msg.toString(), icon, 2);
      this.DATA.add(l);
      this.list.setListData(this.DATA.toArray());
      this.autoScroll(this.DATA.size() - 1);
   }

   public void clearPane() {
      this.objects.removeAll(this.objects);
      this.DATA.removeAllElements();
      this.list.setListData(this.DATA);
      this.list.setToolTipText((String)null);
   }

   public void addMouseListener(MouseListener the_listener) {
      this.list.addMouseListener(the_listener);
   }

   public void removeMouseListener(MouseListener l) {
      this.list.removeMouseListener(l);
   }

   public Object getSelectedObject() {
      return this.selected;
   }
}
