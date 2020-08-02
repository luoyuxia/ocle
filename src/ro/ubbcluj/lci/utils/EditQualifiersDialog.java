package ro.ubbcluj.lci.utils;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;

public class EditQualifiersDialog extends JDialog {
   public static int EXIT_OK = 1;
   public static int EXIT_CANCEL = 0;
   private LinkEnd linkEnd = null;
   private JTable atrTable;
   private DefaultTableModel attrData;
   private AttributeLinksTableCellEditor editor;
   private JLabel jlDescription;
   private int exitCode;
   private static boolean allAttributesSet = false;

   protected EditQualifiersDialog(LinkEnd linkEnd) {
      super(GApplication.frame, "Edit Qualifiers", true);
      this.exitCode = EXIT_CANCEL;
      this.linkEnd = linkEnd;
      this.init();
      this.pack();
   }

   private void init() {
      this.jlDescription = new JLabel("Qualifiers for " + this.linkEnd.getName() + " link end:");
      JButton okButton = new JButton("OK");
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            EditQualifiersDialog.this.exitCode = EditQualifiersDialog.EXIT_OK;

            for(int i = 0; i < EditQualifiersDialog.this.atrTable.getRowCount(); ++i) {
               EditQualifiersDialog.this.atrTable.getCellEditor(i, 2).stopCellEditing();
            }

            EditQualifiersDialog.allAttributesSet = EditQualifiersDialog.this.updateLinkEnd();
            if (!EditQualifiersDialog.allAttributesSet) {
               JOptionPane.showMessageDialog(EditQualifiersDialog.this, "Qualifiers could not be set.\n", "Error editing qualifiers.", 0);
            }

            EditQualifiersDialog.this.setVisible(false);
         }
      });
      JButton cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            EditQualifiersDialog.this.exitCode = EditQualifiersDialog.EXIT_CANCEL;
            EditQualifiersDialog.this.setVisible(false);
         }
      });
      this.attrData = new AttributeLinksTableModel(new String[]{"Attribute", "Type", "Value"}, 0);
      this.atrTable = new JTable(this.attrData);
      this.editor = new AttributeLinksTableCellEditor(this.atrTable);
      this.atrTable.getColumn("Value").setCellEditor(this.editor);
      this.buildAttrData();
      this.atrTable.setSelectionMode(0);
      this.atrTable.getTableHeader().setReorderingAllowed(false);
      JScrollPane atrScroll = new JScrollPane(this.atrTable);
      GridBagLayout layout = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      Insets is = new Insets(5, 5, 5, 5);
      this.getContentPane().setLayout(layout);
      c.weightx = 0.5D;
      c.weighty = 0.5D;
      c.gridx = 0;
      c.gridy = 0;
      c.gridwidth = 3;
      c.insets = is;
      c.anchor = 17;
      this.getContentPane().add(this.jlDescription, c);
      c.gridwidth = 0;
      c.gridy = 1;
      c.fill = 1;
      atrScroll.setMinimumSize(new Dimension(400, 150));
      atrScroll.setPreferredSize(atrScroll.getMinimumSize());
      this.getContentPane().add(atrScroll, c);
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 5;
      c.fill = 0;
      c.anchor = 10;
      okButton.setPreferredSize(cancelButton.getPreferredSize());
      okButton.setMinimumSize(okButton.getPreferredSize());
      this.getContentPane().add(okButton, c);
      c.gridx = 2;
      c.gridy = 5;
      cancelButton.setMinimumSize(cancelButton.getPreferredSize());
      this.getContentPane().add(cancelButton, c);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            EditQualifiersDialog.this.linkEnd = null;
         }
      });
   }

   private void buildAttrData() {
      while(this.attrData.getRowCount() > 0) {
         this.attrData.removeRow(0);
      }

      Collection attrs = this.linkEnd.getAssociationEnd().getCollectionQualifierList();
      this.attrData.setRowCount(attrs.size());
      Iterator it = attrs.iterator();

      for(int i = 0; it.hasNext(); ++i) {
         Attribute at = (Attribute)it.next();
         this.attrData.setValueAt(at, i, 0);
         this.attrData.setValueAt(at.getType().getName(), i, 1);
         Iterator alIt = this.linkEnd.getCollectionQualifierValueList().iterator();
         Instance oldValue = null;

         AttributeLink v;
         while(alIt.hasNext()) {
            v = (AttributeLink)alIt.next();
            if (v.getAttribute() == at) {
               oldValue = v.getValue();
               break;
            }
         }

         Expression expr;
         String v1;
         if (at.getType() instanceof Enumeration) {
            v1 = null;
            if (oldValue != null && !oldValue.getName().equals("")) {
               v1 = oldValue.getName();
            } else {
               expr = at.getInitialValue();
               if (expr != null && expr.getBody() != null && !expr.getBody().equals("")) {
                  v1 = expr.getBody();
               } else {
                  v1 = ((EnumerationLiteral)((Enumeration)at.getType()).getCollectionLiteralList().iterator().next()).getName();
               }
            }

            this.attrData.setValueAt(v1, i, 2);
            ArrayList literals = new ArrayList();
            literals.addAll(((Enumeration)at.getType()).getCollectionLiteralList());
            literals.add("<undefined>");
            this.editor.setEditorAt(new Integer(i), new DefaultCellEditor(new JComboBox(literals.toArray())));
         } else if (at.getType() instanceof DataType) {
            v1 = null;
            if (oldValue != null && !oldValue.getName().equals("")) {
               v1 = oldValue.getName();
            } else {
               expr = at.getInitialValue();
               if (expr != null && expr.getBody() != null && !expr.getBody().equals("")) {
                  v1 = expr.getBody();
               } else {
                  v1 = ModelFactory.getDefaultDataValueAsString((DataType)at.getType());
               }
            }

            this.attrData.setValueAt(v1, i, 2);
            if (at.getType().getName().equals("Boolean")) {
               this.editor.setEditorAt(new Integer(i), new DefaultCellEditor(new JComboBox(new Object[]{"true", "false", "<undefined>"})));
            } else {
               this.editor.setEditorAt(new Integer(i), new DefaultCellEditor(new JTextField(v1)));
            }
         }
      }

   }

   private boolean updateLinkEnd() {
      for(int i = 0; i < this.attrData.getRowCount(); ++i) {
         Attribute attr = (Attribute)this.attrData.getValueAt(i, 0);
         if (attr == null) {
            return false;
         }

         AttributeLink slot = null;
         if (!this.linkEnd.getCollectionQualifierValueList().isEmpty()) {
            Iterator it = this.linkEnd.getCollectionQualifierValueList().iterator();

            while(it.hasNext()) {
               AttributeLink atLink = (AttributeLink)it.next();
               if (atLink.getAttribute() == attr) {
                  slot = atLink;
                  break;
               }
            }
         } else {
            slot = (AttributeLink)ModelFactory.createNewElement(attr, "AttributeLink");
            slot.setLinkEnd(this.linkEnd);
         }

         if (slot != null) {
            if (attr.getType() instanceof DataType) {
               if (this.attrData.getValueAt(i, 2) == null) {
                  return false;
               }

               DataValue dVal = ModelFactory.createNewDataValue(this.attrData.getValueAt(i, 2).toString(), attr.getType());
               slot.setValue(dVal);
            } else if (attr.getType() instanceof Classifier) {
               if (this.attrData.getValueAt(i, 2) == null) {
                  return false;
               }

               Object obj = this.attrData.getValueAt(i, 2);
               if (obj instanceof Instance) {
                  slot.setValue((Instance)obj);
               }
            }
         }
      }

      return true;
   }

   public LinkEnd getLinkEnd() {
      return this.linkEnd;
   }

   public static void editLinkEnd(LinkEnd le) {
      EditQualifiersDialog dialog = new EditQualifiersDialog(le);
      dialog.setLocation(300, 200);
      dialog.setVisible(true);
      if (dialog.getExitCode() != CreateObjectDialog.EXIT_CANCEL) {
         ModelFactory.fireModelEvent(dialog.getLinkEnd(), (Object)null, 21);
      }
   }

   public int getExitCode() {
      return this.exitCode;
   }
}
