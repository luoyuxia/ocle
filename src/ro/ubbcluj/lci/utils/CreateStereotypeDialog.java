package ro.ubbcluj.lci.utils;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.uml.foundation.core.ClassImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public class CreateStereotypeDialog extends JDialog implements ActionListener, ListSelectionListener {
   JTextField nameField = new JTextField();
   JList baseClassList = new JList(new DefaultListModel());
   JButton deleteButton = new JButton("   Delete   ");
   JComboBox addCombo = new JComboBox();
   JButton okButton = new JButton("OK");
   JButton cancelButton = new JButton("Cancel");
   Stereotype stereotype;
   boolean createNew;

   public CreateStereotypeDialog(Frame owner, Stereotype s) {
      super(owner, "Create Stereotype", true);
      if (s == null) {
         this.stereotype = (Stereotype)ModelFactory.createNewElement(ModelFactory.currentModel, "Stereotype");
         this.createNew = true;
      } else {
         this.stereotype = s;
         this.setTitle("Edit stereotype");
         this.createNew = false;
      }

      this.nameField.setText(this.stereotype.getName());
      this.addCombo.addItem("Add");
      Collection c = this.getMetaclasses(GRepository.getInstance().getMetamodel().getModel());
      String[] b = (String[])c.toArray(new String[0]);
      Arrays.sort(b);

      for(int i = 0; i < b.length; ++i) {
         this.addCombo.addItem(b[i]);
      }

      this.addCombo.addActionListener(this);
      Collection bc = this.stereotype.getCollectionBaseClassList();
      Iterator it = bc.iterator();

      while(it.hasNext()) {
         ((DefaultListModel)this.baseClassList.getModel()).addElement(it.next());
      }

      this.baseClassList.setSelectionMode(0);
      this.baseClassList.addListSelectionListener(this);
      this.deleteButton.addActionListener(this);
      this.okButton.addActionListener(this);
      if (this.baseClassList.getModel().getSize() == 0) {
         this.deleteButton.setEnabled(false);
         this.okButton.setEnabled(false);
      }

      this.cancelButton.addActionListener(this);
      this.getContentPane().setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      Insets is = new Insets(5, 20, 5, 20);
      gbc.anchor = 18;
      gbc.gridx = 0;
      gbc.gridy = 0;
      is.top = 20;
      gbc.insets = is;
      this.getContentPane().add(new JLabel("Name:"), gbc);
      gbc.gridwidth = 0;
      gbc.gridx = -1;
      gbc.fill = 2;
      gbc.weightx = 1.0D;
      this.getContentPane().add(this.nameField, gbc);
      gbc.gridx = 0;
      gbc.gridy = -1;
      gbc.gridwidth = 0;
      is.top = 5;
      this.getContentPane().add(new JLabel("BaseClass:"), gbc);
      gbc.gridwidth = 2;
      gbc.gridheight = 2;
      gbc.fill = 1;
      gbc.weighty = 1.0D;
      is.right = 5;
      this.getContentPane().add(new JScrollPane(this.baseClassList), gbc);
      gbc.anchor = 17;
      gbc.gridx = 2;
      gbc.gridy = 2;
      gbc.weightx = 0.0D;
      gbc.weighty = 0.0D;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.fill = 0;
      is.left = 5;
      is.right = 20;
      this.addCombo.setMinimumSize(this.deleteButton.getPreferredSize());
      this.addCombo.setPreferredSize(this.deleteButton.getPreferredSize());
      this.getContentPane().add(this.addCombo, gbc);
      gbc.gridy = 3;
      this.getContentPane().add(this.deleteButton, gbc);
      this.okButton.setMinimumSize(this.cancelButton.getPreferredSize());
      this.okButton.setPreferredSize(this.cancelButton.getPreferredSize());
      JPanel okCancelPanel = new JPanel(new FlowLayout());
      okCancelPanel.add(this.okButton);
      okCancelPanel.add(this.cancelButton);
      is.bottom = 10;
      gbc.insets = is;
      gbc.gridx = 1;
      gbc.gridy = 4;
      gbc.anchor = 11;
      this.getContentPane().add(okCancelPanel, gbc);
      this.pack();
   }

   public void actionPerformed(ActionEvent evt) {
      Object src = evt.getSource();
      DefaultListModel dfm;
      if (src == this.deleteButton) {
         dfm = (DefaultListModel)this.baseClassList.getModel();
         if (this.baseClassList.getSelectedIndex() != -1) {
            dfm.remove(this.baseClassList.getSelectedIndex());
            if (dfm.getSize() == 0) {
               this.okButton.setEnabled(false);
            }
         }
      } else {
         String baseClass;
         if (src == this.addCombo && this.addCombo.getSelectedIndex() > 0) {
            dfm = (DefaultListModel)this.baseClassList.getModel();
            baseClass = (String)this.addCombo.getSelectedItem();
            if (!dfm.contains(baseClass)) {
               dfm.add(dfm.size(), baseClass);
            }

            this.addCombo.setSelectedIndex(0);
            this.okButton.setEnabled(true);
         } else if (src == this.okButton) {
            dfm = (DefaultListModel)this.baseClassList.getModel();
            baseClass = "";

            for(int i = 0; i < dfm.size(); ++i) {
               String elem = (String)dfm.getElementAt(i);
               baseClass = baseClass + " " + elem;
            }

            this.setVisible(false);
            ModelFactory.fireModelEvent(this.stereotype, ModelFactory.currentModel, 20);
            ModelFactory.fireModelEvent(this.stereotype, ModelFactory.currentModel, 0);
         } else if (src == this.cancelButton) {
            this.setVisible(false);
            if (this.createNew) {
               ModelFactory.removeElement(this.stereotype, ModelFactory.currentModel);
            }
         }
      }

   }

   public void valueChanged(ListSelectionEvent evt) {
      if (this.baseClassList.getSelectedIndex() == -1) {
         this.deleteButton.setEnabled(false);
      } else {
         this.deleteButton.setEnabled(true);
      }

   }

   private Collection getMetaclasses(Package pak) {
      ArrayList result = new ArrayList();
      if (!pak.getName().equals("Datatypes") && !pak.getName().equals("Data Types")) {
         Collection tmp = pak.directGetCollectionOwnedElementList();
         Iterator it = tmp.iterator();

         while(it.hasNext()) {
            Object o = it.next();
            if (o.getClass() == (ClassImpl.class)) {
               result.add(((ClassImpl)o).getName());
            }

            if (o instanceof Package) {
               result.addAll(this.getMetaclasses((Package)o));
            }
         }

         return result;
      } else {
         return result;
      }
   }
}
