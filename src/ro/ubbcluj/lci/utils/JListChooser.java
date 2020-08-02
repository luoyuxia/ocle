package ro.ubbcluj.lci.utils;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JListChooser extends JPanel implements ActionListener, ListSelectionListener {
   protected JListChooser.SortedListModel allListModel = new JListChooser.SortedListModel();
   protected JListChooser.SortedListModel selListModel = new JListChooser.SortedListModel();
   protected JList allList;
   protected JList selList;
   protected JButton selAllButton;
   protected JButton selButton;
   protected JButton unselButton;
   protected JButton unselAllButton;
   protected String selString;
   protected String unselString;
   protected String selAllString;
   protected String unselAllString;

   public JListChooser() {
      this.allList = new JList(this.allListModel);
      this.selList = new JList(this.selListModel);
      this.selAllButton = new JButton("<<<");
      this.selButton = new JButton("  <  ");
      this.unselButton = new JButton("  >  ");
      this.unselAllButton = new JButton(">>>");
      this.selString = "Select";
      this.unselString = "Unselect";
      this.selAllString = "Select all";
      this.unselAllString = "Unselect all";
      this.createPane();
      this.setBehaviour();
      this.enableOrDisableButtons();
   }

   private void createPane() {
      JPanel bPanel = new JPanel();
      GridBagLayout layout = new GridBagLayout();
      GridBagConstraints gc = new GridBagConstraints();
      bPanel.setLayout(layout);
      gc.anchor = 10;
      gc.fill = 0;
      Dimension commonSize = this.selAllButton.getPreferredSize();
      this.selButton.setMaximumSize(commonSize);
      this.selButton.setPreferredSize(commonSize);
      this.unselButton.setMaximumSize(commonSize);
      this.unselButton.setPreferredSize(commonSize);
      gc.gridy = 0;
      bPanel.add(this.selButton, gc);
      gc.gridy = 1;
      bPanel.add(Box.createVerticalStrut(10), gc);
      gc.gridy = 2;
      bPanel.add(this.selAllButton, gc);
      gc.gridy = 3;
      bPanel.add(Box.createVerticalStrut(10), gc);
      gc.gridy = 4;
      bPanel.add(this.unselButton, gc);
      gc.gridy = 5;
      bPanel.add(Box.createVerticalStrut(10), gc);
      gc.gridy = 6;
      bPanel.add(this.unselAllButton, gc);
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      Insets is = new Insets(5, 5, 5, 5);
      this.setLayout(gbl);
      gbc.anchor = 17;
      gbc.fill = 0;
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.insets = is;
      JLabel l1 = new JLabel("Selected:");
      l1.setFont(l1.getFont().deriveFont(1));
      this.add(l1, gbc);
      gbc.gridx = 2;
      JLabel l2 = new JLabel("Add:     ");
      l2.setFont(l2.getFont().deriveFont(1));
      l2.setMinimumSize(l1.getMinimumSize());
      l2.setPreferredSize(l1.getPreferredSize());
      this.add(l2, gbc);
      Dimension commonListSize = new Dimension(150, 100);
      gbc.anchor = 10;
      gbc.fill = 1;
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.weighty = 1.0D;
      gbc.weightx = 0.5D;
      is.top = 0;
      this.selList.setPreferredSize(commonListSize);
      this.selList.setMinimumSize(commonListSize);
      this.add(new JScrollPane(this.selList), gbc);
      gbc.gridx = 1;
      gbc.fill = 0;
      gbc.weightx = 0.0D;
      gbc.weighty = 0.0D;
      this.add(bPanel, gbc);
      gbc.fill = 1;
      gbc.weighty = 1.0D;
      gbc.weightx = 0.5D;
      gbc.gridx = 2;
      this.allList.setMinimumSize(commonListSize);
      this.allList.setPreferredSize(commonListSize);
      this.add(new JScrollPane(this.allList), gbc);
   }

   private void setBehaviour() {
      this.selButton.addActionListener(this);
      this.unselButton.addActionListener(this);
      this.selAllButton.addActionListener(this);
      this.unselAllButton.addActionListener(this);
      this.selButton.setToolTipText(this.selString);
      this.selAllButton.setToolTipText(this.selAllString);
      this.unselButton.setToolTipText(this.unselString);
      this.unselAllButton.setToolTipText(this.unselAllString);
      this.selList.setSelectionMode(2);
      this.allList.setSelectionMode(2);
      this.selList.addListSelectionListener(this);
      this.allList.addListSelectionListener(this);
   }

   public void setDomain(Object[] values) {
      this.allListModel.clear();

      for(int i = 0; i < values.length; ++i) {
         this.allListModel.addElement(values[i]);
      }

      this.enableOrDisableButtons();
   }

   public Object[] getDomain() {
      return this.allListModel.toArray();
   }

   public void setSelected(Object[] values) {
      this.selListModel.clear();

      for(int i = 0; i < values.length; ++i) {
         this.selListModel.addElement(values[i]);
      }

      this.enableOrDisableButtons();
   }

   public Object[] getSelected() {
      return this.selListModel.toArray();
   }

   public void actionPerformed(ActionEvent evt) {
      if (evt.getSource() == this.selButton) {
         this.exchange(this.allList, this.selList, this.allList.getSelectedValues());
      } else if (evt.getSource() == this.unselButton) {
         this.exchange(this.selList, this.allList, this.selList.getSelectedValues());
      } else if (evt.getSource() == this.selAllButton) {
         this.exchange(this.allList, this.selList, this.allListModel.toArray());
      } else if (evt.getSource() == this.unselAllButton) {
         this.exchange(this.selList, this.allList, this.selListModel.toArray());
      }

      this.enableOrDisableButtons();
   }

   public void valueChanged(ListSelectionEvent e) {
      this.enableOrDisableButtons();
   }

   protected void enableOrDisableButtons() {
      if (this.selList.getSelectedIndex() == -1) {
         this.unselButton.setEnabled(false);
      } else {
         this.unselButton.setEnabled(true);
      }

      if (this.allList.getSelectedIndex() == -1) {
         this.selButton.setEnabled(false);
      } else {
         this.selButton.setEnabled(true);
      }

      if (this.selListModel.getSize() == 0) {
         this.unselAllButton.setEnabled(false);
      } else {
         this.unselAllButton.setEnabled(true);
      }

      if (this.allListModel.getSize() == 0) {
         this.selAllButton.setEnabled(false);
      } else {
         this.selAllButton.setEnabled(true);
      }

   }

   protected void exchange(JList source, JList destination, Object[] values) {
      DefaultListModel sourceModel = (DefaultListModel)source.getModel();
      DefaultListModel destinationModel = (DefaultListModel)destination.getModel();

      for(int i = 0; i < values.length; ++i) {
         sourceModel.removeElement(values[i]);
         destinationModel.addElement(values[i]);
      }

   }

   class SortedListModel extends DefaultListModel {
      SortedListModel() {
      }

      public void addElement(Object obj) {
         String str = obj.toString();

         int index;
         for(index = 0; index < this.getSize() && str.compareTo(this.get(index).toString()) > 0; ++index) {
         }

         this.add(index, obj);
      }
   }
}
