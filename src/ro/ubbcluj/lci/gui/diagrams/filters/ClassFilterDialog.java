package ro.ubbcluj.lci.gui.diagrams.filters;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import ro.ubbcluj.lci.gui.browser.GTree;
import ro.ubbcluj.lci.gui.diagrams.ClassCell;
import ro.ubbcluj.lci.gui.diagrams.ClassDiagram;
import ro.ubbcluj.lci.gui.diagrams.Diagram;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public class ClassFilterDialog extends JDialog {
   private static ClassFilterDialog instance = null;
   private static ClassFilter filter = null;
   private static Diagram diagram;
   private static ClassCell cell;
   static JCheckBox a_pub = null;
   static JCheckBox a_pro;
   static JCheckBox a_pac;
   static JCheckBox a_pri;
   static JCheckBox m_pub;
   static JCheckBox m_pro;
   static JCheckBox m_pac;
   static JCheckBox m_pri;
   static JCheckBox stereotype;
   static JCheckBox msignature;
   static JButton ok = null;
   static JButton cancel;
   private static final Font textFont = new Font("Tahoma", 0, 14);

   private ClassFilterDialog() {
      this.setSize(270, 350);
      this.setResizable(false);
      this.setDefaultCloseOperation(2);
      this.setModal(true);
      this.initDialog();
   }

   private void initDialog() {
      JPanel main = new JPanel();
      main.setLayout(new BoxLayout(main, 1));
      main.setBorder(BorderFactory.createBevelBorder(0));
      JPanel p1 = new JPanel();
      p1.setLayout(new BoxLayout(p1, 0));
      JLabel show = new JLabel("Show:");
      show.setFont(textFont);
      show.setHorizontalAlignment(2);
      show.setPreferredSize(new Dimension(15, 15));
      show.setForeground(Color.blue);
      p1.add(show);
      this.initCheckBoxes();
      JPanel p2 = new JPanel(new GridLayout(4, 2));
      TitledBorder p2Border = BorderFactory.createTitledBorder(" Attributes & Methods ");
      p2Border.setTitleFont(textFont);
      p2Border.setTitleColor(Color.blue);
      p2.setBorder(p2Border);
      p2.add(a_pub);
      p2.add(m_pub);
      p2.add(a_pac);
      p2.add(m_pac);
      p2.add(a_pro);
      p2.add(m_pro);
      p2.add(a_pri);
      p2.add(m_pri);
      JPanel p4 = new JPanel();
      p4.setLayout(new BoxLayout(p4, 0));
      TitledBorder p4Border = BorderFactory.createTitledBorder(" Stereotypes ");
      p4Border.setTitleColor(Color.blue);
      p4Border.setTitleFont(textFont);
      p4.add(stereotype);
      p4.add(Box.createRigidArea(new Dimension(this.getSize().width - 10, 0)));
      p4.setBorder(p4Border);
      this.initButtons();
      JPanel p5 = new JPanel();
      p5.setLayout(new BoxLayout(p5, 0));
      TitledBorder p5Border = BorderFactory.createTitledBorder(" Signatures ");
      p5Border.setTitleColor(Color.blue);
      p5Border.setTitleFont(textFont);
      p5.add(msignature);
      p5.add(Box.createRigidArea(new Dimension(this.getSize().width - 10, 0)));
      p5.setBorder(p5Border);
      JPanel p3 = new JPanel();
      p3.setLayout(new BoxLayout(p3, 0));
      p3.add(Box.createRigidArea(new Dimension(7, 0)));
      p3.add(ok);
      p3.add(Box.createRigidArea(new Dimension(10, 0)));
      p3.add(cancel);
      main.add(Box.createRigidArea(new Dimension(0, 5)));
      main.add(p1);
      main.add(Box.createRigidArea(new Dimension(0, 5)));
      main.add(p4);
      main.add(Box.createRigidArea(new Dimension(0, 5)));
      main.add(p5);
      main.add(Box.createRigidArea(new Dimension(0, 5)));
      main.add(p2);
      main.add(p3);
      main.add(Box.createRigidArea(new Dimension(0, 5)));
      this.setContentPane(main);
   }

   private void initCheckBoxes() {
      if (filter == null) {
         System.err.println("Filter Dialog: Can't init check-boxes");
      } else {
         if (a_pub == null) {
            a_pub = new JCheckBox("Public attributes", filter.getPropertyValue("a_pub"));
            a_pac = new JCheckBox("Package attributes", filter.getPropertyValue("a_pac"));
            a_pro = new JCheckBox("Protected attributes", filter.getPropertyValue("a_pro"));
            a_pri = new JCheckBox("Private attributes", filter.getPropertyValue("a_pri"));
            m_pub = new JCheckBox("Public metods", filter.getPropertyValue("m_pub"));
            m_pac = new JCheckBox("Package methods", filter.getPropertyValue("m_pac"));
            m_pro = new JCheckBox("Protected methods", filter.getPropertyValue("m_pro"));
            m_pri = new JCheckBox("Private methods", filter.getPropertyValue("m_pri"));
            stereotype = new JCheckBox("Stereotypes", filter.getPropertyValue("stereotype"));
            msignature = new JCheckBox("Full method signature", filter.getPropertyValue("msignature"));
         } else {
            a_pub.setSelected(filter.getPropertyValue("a_pub"));
            a_pac.setSelected(filter.getPropertyValue("a_pac"));
            a_pro.setSelected(filter.getPropertyValue("a_pro"));
            a_pri.setSelected(filter.getPropertyValue("a_pri"));
            m_pub.setSelected(filter.getPropertyValue("m_pub"));
            m_pac.setSelected(filter.getPropertyValue("m_pac"));
            m_pro.setSelected(filter.getPropertyValue("m_pro"));
            m_pri.setSelected(filter.getPropertyValue("m_pri"));
            stereotype.setSelected(filter.getPropertyValue("stereotype"));
            msignature.setSelected(filter.getPropertyValue("msignature"));
         }

      }
   }

   private void initButtons() {
      ClassFilterDialog.FilterDialogListener listener = new ClassFilterDialog.FilterDialogListener();
      ok = new JButton("Ok");
      ok.setPreferredSize(new Dimension(20, 20));
      ok.addActionListener(listener);
      cancel = new JButton("Cancel");
      cancel.setPreferredSize(new Dimension(20, 20));
      cancel.addActionListener(listener);
   }

   public static void show(Diagram diag, ClassCell the_cell) {
      diagram = diag;
      cell = the_cell;
      String className = null;
      Point location = null;
      ClassFilter filter = null;
      if (the_cell == null) {
         GTree tree = (GTree)GRepository.getInstance().getUsermodel().getBrowser().getComponent();
         location = tree.getLocation();
         if (diag instanceof ClassDiagram) {
            filter = ((ClassDiagram)diag).getClassFilter();
            className = ((ClassDiagram)diag).getName();
         }
      } else {
         location = cell.getLocation();
         filter = (ClassFilter)cell.getFilter();
         className = ((Classifier)cell.getUserObject()).getName();
      }

      ClassFilterDialog.filter = filter;
      if (instance == null) {
         instance = new ClassFilterDialog();
      } else {
         instance.initDialog();
      }

      instance.setTitle("Filter " + className);
      instance.setLocation(location);
      instance.show();
   }

   static boolean isFiltered() {
      if (!a_pub.isSelected()) {
         return true;
      } else if (!a_pro.isSelected()) {
         return true;
      } else if (!a_pac.isSelected()) {
         return true;
      } else if (!a_pri.isSelected()) {
         return true;
      } else if (!m_pub.isSelected()) {
         return true;
      } else if (!m_pro.isSelected()) {
         return true;
      } else if (!m_pac.isSelected()) {
         return true;
      } else if (!m_pri.isSelected()) {
         return true;
      } else if (!stereotype.isSelected()) {
         return true;
      } else {
         return !msignature.isSelected();
      }
   }

   class FilterDialogListener implements ActionListener {
      FilterDialogListener() {
      }

      public void actionPerformed(ActionEvent e) {
         if (e.getActionCommand().equals("Ok")) {
            ClassFilterDialog.filter.markProperty("a_pub", new Boolean(ClassFilterDialog.a_pub.isSelected()));
            ClassFilterDialog.filter.markProperty("a_pac", new Boolean(ClassFilterDialog.a_pac.isSelected()));
            ClassFilterDialog.filter.markProperty("a_pro", new Boolean(ClassFilterDialog.a_pro.isSelected()));
            ClassFilterDialog.filter.markProperty("a_pri", new Boolean(ClassFilterDialog.a_pri.isSelected()));
            ClassFilterDialog.filter.markProperty("m_pub", new Boolean(ClassFilterDialog.m_pub.isSelected()));
            ClassFilterDialog.filter.markProperty("m_pac", new Boolean(ClassFilterDialog.m_pac.isSelected()));
            ClassFilterDialog.filter.markProperty("m_pro", new Boolean(ClassFilterDialog.m_pro.isSelected()));
            ClassFilterDialog.filter.markProperty("m_pri", new Boolean(ClassFilterDialog.m_pri.isSelected()));
            ClassFilterDialog.filter.markProperty("stereotype", new Boolean(ClassFilterDialog.stereotype.isSelected()));
            ClassFilterDialog.filter.markProperty("msignature", new Boolean(ClassFilterDialog.msignature.isSelected()));
            boolean filtered = ClassFilterDialog.isFiltered();
            if (ClassFilterDialog.cell == null) {
               if (ClassFilterDialog.diagram instanceof ClassDiagram) {
                  ClassDiagram cd = (ClassDiagram)ClassFilterDialog.diagram;
                  ArrayList lst = cd.getClassCells();

                  for(int i = 0; i < lst.size(); ++i) {
                     ((ClassCell)lst.get(i)).setFilter(ClassFilterDialog.filter);
                     ((ClassCell)lst.get(i)).setFiltered(filtered);
                  }
               }
            } else {
               ClassFilterDialog.cell.setFiltered(filtered);
               ClassFilterDialog.cell = null;
            }

            ClassFilterDialog.instance.dispose();
            ClassFilterDialog.diagram.getGraph().repaint();
         }

         if (e.getActionCommand().equals("Cancel")) {
            ClassFilterDialog.instance.dispose();
         }

      }
   }
}
