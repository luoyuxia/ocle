package ro.ubbcluj.lci.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.properties.ComboItem;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Constraint;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinition;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public class EditStereotypesDialog extends JDialog implements ListSelectionListener, ActionListener, ChangeListener {
   public static final int RETURN_OK = 1;
   public static final int RETURN_CANCEL = 0;
   protected int returnCode = 0;
   private ModelElement modelElement;
   private static final Collection metaclasses = getClasses(GRepository.getInstance().getMetamodel().getModel());
   protected JButton okButton = new JButton("OK");
   protected JButton applyButton = new JButton("Apply");
   protected JButton cancelButton = new JButton("Cancel");
   protected JList stereotypeList;
   protected DefaultListModel stereotypeListModel = new DefaultListModel();
   protected JListChooser cPanel;
   protected DefaultListModel scListModel = new DefaultListModel();
   DefaultTableModel tvTableModel = new DefaultTableModel(new String[]{"TagDefinition", "Value"}, 0);
   JTable tvTable;
   JComboBox addCombo;

   public EditStereotypesDialog(ModelElement me) {
      super(GApplication.frame, "Extension mechanisms", true);
      this.tvTable = new JTable(this.tvTableModel);
      this.addCombo = new JComboBox();
      this.modelElement = me;
      JTabbedPane tabbedPane = new JTabbedPane(1);
      tabbedPane.add("Stereotypes", this.getStereotypesPanel());
      tabbedPane.add("Constraints", this.getConstraintsPanel());
      tabbedPane.add("TaggedValues", this.getTaggedValuesPanel());
      tabbedPane.addChangeListener(this);
      GridBagLayout gbl = new GridBagLayout();
      this.getContentPane().setLayout(gbl);
      GridBagConstraints gc = new GridBagConstraints();
      Insets is = new Insets(5, 20, 5, 20);
      gc.anchor = 11;
      gc.gridy = 0;
      gc.gridwidth = 2;
      gc.insets = is;
      gc.weightx = 1.0D;
      gc.fill = 2;
      this.getContentPane().add(new JLabel("<html><b>  Element:    </b>" + this.modelElement + "</html>"), gc);
      gc.weighty = 1.0D;
      gc.gridy = 1;
      gc.fill = 1;
      this.getContentPane().add(tabbedPane, gc);
      JPanel bPanel = new JPanel(new GridBagLayout());
      GridBagConstraints bgc = new GridBagConstraints();
      this.okButton.setPreferredSize(this.cancelButton.getPreferredSize());
      this.okButton.setMinimumSize(this.okButton.getPreferredSize());
      this.applyButton.setPreferredSize(this.cancelButton.getPreferredSize());
      this.applyButton.setMinimumSize(this.okButton.getPreferredSize());
      this.cancelButton.setMinimumSize(this.cancelButton.getPreferredSize());
      bPanel.add(this.okButton, bgc);
      bgc.gridx = 1;
      bPanel.add(Box.createHorizontalStrut(15), bgc);
      bgc.gridx = 2;
      bPanel.add(this.cancelButton, bgc);
      bgc.gridx = 3;
      bPanel.add(Box.createHorizontalStrut(15), bgc);
      bgc.gridx = 4;
      bPanel.add(this.applyButton, bgc);
      gc.anchor = 13;
      gc.fill = 0;
      gc.gridx = 0;
      gc.gridy = 2;
      gc.gridwidth = 0;
      gc.weightx = 0.0D;
      gc.weighty = 0.0D;
      gc.insets.right = 40;
      this.getContentPane().add(bPanel, gc);
      this.okButton.addActionListener(this);
      this.applyButton.addActionListener(this);
      this.cancelButton.addActionListener(this);
      this.pack();
   }

   private JPanel getStereotypesPanel() {
      JPanel sPanel = new JPanel();
      Collection ancestors = this.getAncestors();

      try {
         Iterator it = GRepository.getInstance().getUsermodel().getModel().directGetCollectionOwnedElementList().iterator();
         Set meSt = this.modelElement.getCollectionStereotypeList();

         label51:
         while(true) {
            Object elem;
            do {
               if (!it.hasNext()) {
                  this.stereotypeList = new JList(this.stereotypeListModel);
                  this.stereotypeList.getSelectionModel().setSelectionMode(1);
                  this.stereotypeList.setCellRenderer(new EditStereotypesDialog.CheckBoxListCellRenderer());
                  this.stereotypeList.addListSelectionListener(this);
                  GridBagLayout gbl = new GridBagLayout();
                  GridBagConstraints gbc = new GridBagConstraints();
                  sPanel.setLayout(gbl);
                  gbc.anchor = 10;
                  gbc.fill = 1;
                  gbc.weightx = 1.0D;
                  gbc.weighty = 1.0D;
                  gbc.insets = new Insets(5, 5, 5, 5);
                  sPanel.add(new JScrollPane(this.stereotypeList), gbc);
                  return sPanel;
               }

               elem = it.next();
            } while(!(elem instanceof Stereotype));

            Stereotype st = (Stereotype)elem;
            Iterator it2 = ancestors.iterator();

            while(true) {
               do {
                  if (!it2.hasNext()) {
                     continue label51;
                  }
               } while(!st.getCollectionBaseClassList().contains(it2.next()));

               boolean added = false;

               for(int i = 0; i < this.stereotypeListModel.size(); ++i) {
                  EditStereotypesDialog.ListItem item = (EditStereotypesDialog.ListItem)this.stereotypeListModel.get(i);
                  if (((Stereotype)item.stereotype).getName().compareTo(st.getName()) > 0) {
                     this.stereotypeListModel.add(i, new EditStereotypesDialog.ListItem(st, meSt.contains(st)));
                     added = true;
                     break;
                  }
               }

               if (!added) {
                  this.stereotypeListModel.addElement(new EditStereotypesDialog.ListItem(elem, meSt.contains(st)));
               }
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
         return sPanel;
      }
   }

   private void fillStereotypeConstraintList() {
      Collection sc = new ArrayList();

      for(int i = 0; i < this.stereotypeListModel.getSize(); ++i) {
         EditStereotypesDialog.ListItem item = (EditStereotypesDialog.ListItem)this.stereotypeListModel.get(i);
         if (item.selected) {
            Iterator it = ((Stereotype)item.stereotype).getCollectionStereotypeConstraintList().iterator();

            while(it.hasNext()) {
               Constraint tmpC = (Constraint)it.next();
               sc.add(tmpC);
            }
         }
      }

      this.scListModel.clear();
      Object[] scA = sc.toArray();
      Arrays.sort(scA, new Comparator() {
         public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
         }
      });

      for(int i = 0; i < scA.length; ++i) {
         this.scListModel.addElement(scA[i]);
      }

   }

   private JPanel getConstraintsPanel() {
      this.cPanel = new JListChooser();
      this.fillStereotypeConstraintList();
      JList scList = new JList(this.scListModel);
      scList.setMinimumSize(new Dimension(100, 40));
      scList.setPreferredSize(new Dimension(100, 40));
      Model model = GRepository.getInstance().getUsermodel().getModel();
      Iterator it = model.directGetCollectionOwnedElementList().iterator();
      ArrayList constraints = new ArrayList();

      while(it.hasNext()) {
         Object tmp = it.next();
         if (tmp instanceof Constraint) {
            constraints.add(tmp);
         }
      }

      it = this.modelElement.getCollectionConstraintList().iterator();

      while(it.hasNext()) {
         constraints.remove(it.next());
      }

      this.cPanel.setSelected(this.modelElement.getCollectionConstraintList().toArray(new Object[0]));
      this.cPanel.setDomain(constraints.toArray(new Object[0]));
      JPanel result = new JPanel(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();
      gc.fill = 1;
      gc.weighty = 0.8D;
      gc.weightx = 1.0D;
      result.add(this.cPanel, gc);
      gc.gridx = 0;
      gc.insets = new Insets(0, 5, 0, 5);
      gc.weighty = 0.0D;
      result.add(new JLabel("Stereotype constraints:"), gc);
      gc.weighty = 0.2D;
      gc.gridy = 2;
      result.add(new JScrollPane(scList), gc);
      return result;
   }

   private JPanel getTaggedValuesPanel() {
      JPanel tPanel = new JPanel();
      JButton delButton = new JButton(" Delete ");
      this.fillAddCombo();
      Iterator it = this.modelElement.getCollectionTaggedValueList().iterator();

      while(it.hasNext()) {
         TaggedValue tv = (TaggedValue)it.next();
         this.tvTableModel.addRow(new Object[]{tv.getType(), tv.getDataValue()});
      }

      this.addCombo.setActionCommand("AddTaggedValue");
      this.addCombo.addActionListener(this);
      delButton.setActionCommand("DeleteTaggedValue");
      delButton.addActionListener(this);
      this.tvTable.setSelectionMode(0);
      this.tvTable.setRowHeight(this.tvTable.getRowHeight() + 5);
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      tPanel.setLayout(gbl);
      Dimension common = delButton.getPreferredSize();
      common.width += 40;
      gbc.anchor = 10;
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 0.5D;
      gbc.insets = new Insets(5, 5, 5, 5);
      this.addCombo.setMinimumSize(common);
      this.addCombo.setPreferredSize(common);
      tPanel.add(this.addCombo, gbc);
      gbc.gridx = 1;
      delButton.setMinimumSize(common);
      delButton.setPreferredSize(common);
      tPanel.add(delButton, gbc);
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 2;
      gbc.fill = 1;
      gbc.weightx = 1.0D;
      gbc.weighty = 1.0D;
      tPanel.add(new JScrollPane(this.tvTable), gbc);
      return tPanel;
   }

   private void fillAddCombo() {
      Collection tds = new ArrayList();

      TagDefinition td;
      for(int i = 0; i < this.stereotypeListModel.getSize(); ++i) {
         EditStereotypesDialog.ListItem item = (EditStereotypesDialog.ListItem)this.stereotypeListModel.get(i);
         if (item.selected) {
            Iterator it = ((Stereotype)item.stereotype).getCollectionDefinedTagList().iterator();

            while(it.hasNext()) {
               td = (TagDefinition)it.next();
               tds.add(td);
            }
         }
      }

      Collection owned = GRepository.getInstance().getUsermodel().getModel().directGetCollectionOwnedElementList();
      Iterator it = owned.iterator();

      while(it.hasNext()) {
         Object tmp = it.next();
         if (tmp instanceof TagDefinition) {
            td = (TagDefinition)tmp;
            tds.add(td);
         }
      }

      Object[] tdsArray = tds.toArray(new Object[0]);
      Arrays.sort(tdsArray, new Comparator() {
         public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
         }
      });
      this.addCombo.removeAllItems();
      this.addCombo.addItem(new ComboItem((Object)null, "Add TagValue"));

      for(int i = 0; i < tdsArray.length; ++i) {
         td = (TagDefinition)tdsArray[i];
         this.addCombo.addItem(new ComboItem(td, td.getName()));
      }

   }

   private Collection getAncestors() {
      String metaName = this.modelElement.getMetaclassName();
      Class metaClass = null;
      Iterator it = metaclasses.iterator();

      while(it.hasNext()) {
         Class tmp = (Class)it.next();
         if (tmp.getName().equals(metaName)) {
            metaClass = tmp;
         }
      }

      Collection result = new ArrayList();
      result.add(metaName);
      it = metaClass.allParents().iterator();

      while(it.hasNext()) {
         result.add(((ModelElement)it.next()).getName());
      }

      return result;
   }

   private static Collection getClasses(Package pack) {
      Collection result = new ArrayList();
      Iterator it = pack.directGetCollectionOwnedElementList().iterator();

      while(it.hasNext()) {
         Object tmp = it.next();
         if (tmp instanceof Package) {
            result.addAll(getClasses((Package)tmp));
         } else if (tmp instanceof Class) {
            result.add(tmp);
         }
      }

      return result;
   }

   public int getReturnCode() {
      return this.returnCode;
   }

   public void update() {
      Collection meSt = this.modelElement.getCollectionStereotypeList();

      for(int i = 0; i < this.stereotypeListModel.getSize(); ++i) {
         EditStereotypesDialog.ListItem item = (EditStereotypesDialog.ListItem)this.stereotypeListModel.get(i);
         if (item.selected && !meSt.contains(item.stereotype)) {
            this.modelElement.addStereotype((Stereotype)item.stereotype);
         }

         if (!item.selected && meSt.contains(item.stereotype)) {
            this.modelElement.removeStereotype((Stereotype)item.stereotype);
         }
      }

      Object[] constr = this.cPanel.getSelected();
      Iterator it = this.modelElement.getCollectionConstraintList().iterator();

      while(it.hasNext()) {
         Constraint tmpC = (Constraint)it.next();
         boolean exist = false;

         for(int i = 0; i < constr.length; ++i) {
            if (constr[i] == tmpC) {
               exist = true;
               break;
            }
         }

         if (!exist) {
            it.remove();
         }
      }

      int i;
      for(i = 0; i < constr.length; ++i) {
         if (!this.modelElement.getCollectionConstraintList().contains(constr[i])) {
            this.modelElement.addConstraint((Constraint)constr[i]);
         }
      }

      it = this.modelElement.getCollectionTaggedValueList().iterator();

      while(it.hasNext()) {
         TaggedValue tmp = (TaggedValue)it.next();
         ModelFactory.removeElement(tmp, tmp.getModelElement());
      }

      for(i = 0; i < this.tvTableModel.getRowCount(); ++i) {
         if (!"".equals(this.tvTableModel.getValueAt(i, 1))) {
            TaggedValue tv = (TaggedValue)ModelFactory.createNewElement(this.modelElement, "TaggedValue");
            ModelFactory.setAttribute(tv, "Type", this.tvTableModel.getValueAt(i, 0));
            ModelFactory.setAttribute(tv, "DataValue", this.tvTableModel.getValueAt(i, 1).toString());
         }
      }

   }

   public void valueChanged(ListSelectionEvent evt) {
      Object sel = this.stereotypeList.getSelectedValue();
      if (sel != null) {
         ((EditStereotypesDialog.ListItem)sel).selected = !((EditStereotypesDialog.ListItem)sel).selected;
         this.stereotypeList.getSelectionModel().clearSelection();
      }

   }

   public void actionPerformed(ActionEvent evt) {
      if (evt.getActionCommand().equals("AddTaggedValue")) {
         JComboBox combo = (JComboBox)evt.getSource();
         if (combo.getSelectedIndex() == 0) {
            return;
         }

         ComboItem item = (ComboItem)combo.getSelectedItem();
         if (item != null && item.getValue() != null) {
            TagDefinition td = (TagDefinition)item.getValue();
            this.tvTableModel.addRow(new Object[]{td, ""});
            combo.setSelectedIndex(0);
         }
      }

      if (evt.getActionCommand().equals("DeleteTaggedValue") && this.tvTable.getSelectedRow() != -1) {
         this.tvTableModel.removeRow(this.tvTable.getSelectedRow());
      }

      if (evt.getSource() == this.okButton) {
         this.setVisible(false);
         this.returnCode = 1;
      }

      if (evt.getSource() == this.cancelButton) {
         this.returnCode = 0;
         this.setVisible(false);
      }

      if (evt.getSource() == this.applyButton) {
         this.fillAddCombo();
         this.fillStereotypeConstraintList();
         int i = 0;

         while(i < this.tvTableModel.getRowCount()) {
            Object td = this.tvTableModel.getValueAt(i, 0);
            boolean existTD = false;

            for(int j = 0; j < this.addCombo.getModel().getSize(); ++j) {
               ComboItem item = (ComboItem)this.addCombo.getModel().getElementAt(j);
               if (td == item.getValue()) {
                  existTD = true;
               }
            }

            if (!existTD) {
               this.tvTableModel.removeRow(i);
            } else {
               ++i;
            }
         }

         this.update();
      }

   }

   public void stateChanged(ChangeEvent evt) {
   }

   class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer {
      CheckBoxListCellRenderer() {
      }

      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         this.setBackground(Color.white);
         this.setText(((EditStereotypesDialog.ListItem)value).stereotype.toString());
         this.setSelected(((EditStereotypesDialog.ListItem)EditStereotypesDialog.this.stereotypeListModel.get(index)).selected);
         return this;
      }
   }

   class ListItem {
      Object stereotype;
      boolean selected;

      ListItem(Object st, boolean sel) {
         this.stereotype = st;
         this.selected = sel;
      }
   }
}
