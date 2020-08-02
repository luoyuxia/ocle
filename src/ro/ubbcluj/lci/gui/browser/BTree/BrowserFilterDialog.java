package ro.ubbcluj.lci.gui.browser.BTree;

import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import ro.ubbcluj.lci.gui.mainframe.GApplication;

public class BrowserFilterDialog extends JDialog implements ActionListener {
   protected Filter filter;
   protected boolean changes = false;
   JList accList;
   DefaultListModel accModel;
   JScrollPane accScroll;
   JList denyList;
   DefaultListModel denyModel;
   JScrollPane denyScroll;
   JButton hideButton = new JButton();
   JButton showButton = new JButton();
   JButton showAllButton = new JButton();
   JButton okButton = new JButton();
   JButton cancelButton = new JButton();
   JButton defaultsButton = new JButton();
   JLabel jLabel1 = new JLabel();
   JLabel jLabel2 = new JLabel();

   public BrowserFilterDialog(Filter f) throws Exception {
      super(GApplication.frame, "Customize browser filter", true);

      try {
         if (f == null) {
            throw new RuntimeException("Illegal argument in constructor");
         }

         this.filter = f;
         BrowserFilterDialog.CBMouseListener ml = new BrowserFilterDialog.CBMouseListener();
         this.accModel = new SortedListModel();

         int i;
         for(i = 0; i < this.filter.getAccepted().size(); ++i) {
            this.accModel.addElement(this.filter.getAccepted().get(i));
         }

         this.accList = new JList(this.accModel);
         this.accList.setSelectionMode(2);
         this.accList.addMouseListener(ml);
         this.denyModel = new SortedListModel();

         for(i = 0; i < this.filter.getDenied().size(); ++i) {
            this.denyModel.addElement(this.filter.getDenied().get(i));
         }

         this.denyList = new JList(this.denyModel);
         this.denyList.setSelectionMode(0);
         this.denyList.addMouseListener(ml);
         this.accScroll = new JScrollPane(this.accList);
         this.denyScroll = new JScrollPane(this.denyList);
         this.hideButton.setBounds(new Rectangle(285, 180, 35, 27));
         this.hideButton.setToolTipText("Hide");
         this.hideButton.setActionCommand("Hide");
         this.hideButton.setMargin(new Insets(2, 2, 2, 2));
         this.hideButton.setText(">");
         this.hideButton.addActionListener(this);
         this.showButton.setBounds(new Rectangle(250, 180, 35, 27));
         this.showButton.setToolTipText("Show");
         this.showButton.setActionCommand("Show");
         this.showButton.setMargin(new Insets(2, 2, 2, 2));
         this.showButton.setText("<");
         this.showButton.addActionListener(this);
         this.showAllButton.setBounds(new Rectangle(250, 207, 70, 27));
         this.showAllButton.setToolTipText("Show all");
         this.showAllButton.setActionCommand("ShowAll");
         this.showAllButton.setText("<<<");
         this.showAllButton.addActionListener(this);
         this.okButton.setBounds(new Rectangle(300, 425, 80, 27));
         this.okButton.setText("OK");
         this.okButton.addActionListener(this);
         this.cancelButton.setBounds(new Rectangle(420, 425, 80, 27));
         this.cancelButton.setText("Cancel");
         this.cancelButton.addActionListener(this);
         this.defaultsButton.setBounds(new Rectangle(175, 425, 80, 27));
         this.defaultsButton.setMargin(new Insets(2, 2, 2, 2));
         this.defaultsButton.setText("Defaults");
         this.defaultsButton.addActionListener(this);
         this.jLabel1.setText("Show:");
         this.jLabel1.setBounds(new Rectangle(23, 23, 41, 17));
         this.jLabel2.setText("Hide:");
         this.jLabel2.setBounds(new Rectangle(353, 23, 41, 17));
         this.denyScroll.setBounds(new Rectangle(350, 40, 200, 360));
         this.accScroll.setBounds(new Rectangle(20, 40, 200, 360));
         this.getContentPane().setLayout((LayoutManager)null);
         this.getContentPane().add(this.accScroll, (Object)null);
         this.getContentPane().add(this.jLabel1, (Object)null);
         this.getContentPane().add(this.defaultsButton, (Object)null);
         this.getContentPane().add(this.denyScroll, (Object)null);
         this.getContentPane().add(this.showButton, (Object)null);
         this.getContentPane().add(this.showAllButton, (Object)null);
         this.getContentPane().add(this.hideButton, (Object)null);
         this.getContentPane().add(this.okButton, (Object)null);
         this.getContentPane().add(this.jLabel2, (Object)null);
         this.getContentPane().add(this.cancelButton, (Object)null);
         this.pack();
         this.setResizable(false);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void actionPerformed(ActionEvent evt) {
      if (evt.getActionCommand().equals("Hide")) {
         this.hideTypes();
      }

      if (evt.getActionCommand().equals("Show")) {
         this.showTypes();
      }

      if (evt.getActionCommand().equals("ShowAll")) {
         this.showAllTypes();
      }

      if (evt.getActionCommand().equals("OK")) {
         if (this.changes) {
            this.filter.getAccepted().clear();

            int i;
            for(i = 0; i < this.accModel.size(); ++i) {
               this.filter.getAccepted().add(this.accModel.get(i));
            }

            this.filter.getDenied().clear();

            for(i = 0; i < this.denyModel.size(); ++i) {
               this.filter.getDenied().add(this.denyModel.get(i));
            }
         }

         this.setVisible(false);
      }

      if (evt.getActionCommand().equals("Cancel")) {
         this.setVisible(false);
         this.changes = false;
      }

      if (evt.getActionCommand().equals("Defaults")) {
         CustomFilter defFilter = new CustomFilter();
         this.accModel.clear();

         int i;
         for(i = 0; i < defFilter.getAccepted().size(); ++i) {
            this.accModel.addElement(defFilter.getAccepted().get(i));
         }

         this.denyModel.clear();

         for(i = 0; i < defFilter.getDenied().size(); ++i) {
            this.denyModel.addElement(defFilter.getDenied().get(i));
         }

         this.changes = true;
      }

   }

   private void hideTypes() {
      if (this.accList.getSelectedIndices().length > 0) {
         Object[] sel = this.accList.getSelectedValues();

         for(int i = 0; i < sel.length; ++i) {
            this.denyModel.addElement(sel[i]);
            this.accModel.removeElement(sel[i]);
         }

         this.changes = true;
         this.showButton.setEnabled(true);
         this.showAllButton.setEnabled(true);
         if (this.accModel.getSize() == 0) {
            this.hideButton.setEnabled(false);
         }
      }

   }

   private void showTypes() {
      if (this.denyList.getSelectedIndices().length > 0) {
         Object[] sel = this.denyList.getSelectedValues();

         for(int i = 0; i < sel.length; ++i) {
            this.accModel.addElement(sel[i]);
            this.denyModel.removeElement(sel[i]);
         }

         this.changes = true;
         this.hideButton.setEnabled(true);
         if (this.denyModel.getSize() == 0) {
            this.showButton.setEnabled(false);
            this.showAllButton.setEnabled(false);
         }
      }

   }

   private void showAllTypes() {
      for(int i = 0; i < this.denyModel.size(); ++i) {
         this.accModel.addElement(this.denyModel.get(i));
      }

      this.denyModel.clear();
      this.changes = true;
      this.showButton.setEnabled(false);
      this.showAllButton.setEnabled(false);
      this.hideButton.setEnabled(true);
   }

   public Filter getFilter() {
      return this.filter;
   }

   public boolean wereChangesMade() {
      return this.changes;
   }

   class CBMouseListener extends MouseAdapter {
      CBMouseListener() {
      }

      public void mousePressed(MouseEvent evt) {
         if (evt.getClickCount() == 2) {
            if (evt.getSource() == BrowserFilterDialog.this.accList) {
               BrowserFilterDialog.this.hideTypes();
            } else if (evt.getSource() == BrowserFilterDialog.this.denyList) {
               BrowserFilterDialog.this.showTypes();
            }
         }

      }
   }
}
