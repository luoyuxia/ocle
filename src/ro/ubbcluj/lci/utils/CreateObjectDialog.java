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
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
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
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkObject;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ClassifierImpl;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public final class CreateObjectDialog extends JDialog {
   public static int EXIT_OK = 1;
   public static int EXIT_CANCEL = 0;
   private static Classifier dummyClassifier = new ClassifierImpl();
   private Object object = null;
   private Classifier classifier = null;
   private JComboBox clsCombo;
   private JTable atrTable;
   private DefaultTableModel attrData;
   private AttributeLinksTableCellEditor editor;
   private JTextField nameField;
   private int exitCode;
   private static boolean allAttributesSet;

   protected CreateObjectDialog(Collaboration colab) {
      super(GApplication.frame, "Create object", true);
      this.exitCode = EXIT_CANCEL;

      try {
         this.object = (Object)ModelFactory.createNewElement(colab, "Object");
         this.init();
         this.pack();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   protected CreateObjectDialog(Object obj) {
      super(GApplication.frame, "Edit object", true);
      this.exitCode = EXIT_CANCEL;

      try {
         this.object = obj;
         this.init();
         this.pack();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void init() {
      JLabel nameLabel = new JLabel("Name:");
      JLabel clbLabel = new JLabel("Collaboration:");
      JLabel clbLabel2 = new JLabel(this.object.directGetNamespace().getName());
      JLabel clsLabel = new JLabel("Classifier:");
      JLabel alLabel = new JLabel("AttributeLinks:");
      this.nameField = new JTextField(this.object.getName());
      this.clsCombo = new JComboBox();
      Iterator it = this.object.getCollectionClassifierList().iterator();
      if (it.hasNext()) {
         this.classifier = (Classifier)it.next();
         this.clsCombo.setEnabled(false);
      }

      if (this.classifier == null) {
         this.clsCombo.addItem(dummyClassifier);
         this.classifier = dummyClassifier;
      }

      Classifier[] cls = this.getAllVisibleTypes(ModelFactory.currentModel);

      for(int i = 0; i < cls.length; ++i) {
         if (!cls[i].isAbstract() && cls[i] instanceof Class) {
            this.clsCombo.addItem(cls[i]);
         }
      }

      this.clsCombo.setSelectedItem(this.classifier);
      this.clsCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            CreateObjectDialog.this.buildAttrData();
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
      JButton okButton = new JButton("OK");
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            CreateObjectDialog.this.exitCode = CreateObjectDialog.EXIT_OK;

            for(int i = 0; i < CreateObjectDialog.this.atrTable.getRowCount(); ++i) {
               CreateObjectDialog.this.atrTable.getCellEditor(i, 2).stopCellEditing();
            }

            CreateObjectDialog.allAttributesSet = CreateObjectDialog.this.updateObject();
            if (!CreateObjectDialog.allAttributesSet) {
               JOptionPane.showMessageDialog(CreateObjectDialog.this, "Object could not be created.\n", "Error creating object", 0);
            }

            CreateObjectDialog.this.dispose();
            CreateObjectDialog.this.setVisible(false);
         }
      });
      JButton cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            CreateObjectDialog.this.exitCode = CreateObjectDialog.EXIT_CANCEL;
            CreateObjectDialog.this.dispose();
            CreateObjectDialog.this.setVisible(false);
         }
      });
      GridBagLayout layout = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      Insets is = new Insets(5, 5, 5, 5);
      this.getContentPane().setLayout(layout);
      c.weightx = 0.5D;
      c.weighty = 0.5D;
      c.gridx = 0;
      c.gridy = 0;
      c.insets = is;
      c.anchor = 17;
      this.getContentPane().add(nameLabel, c);
      c.gridx = -1;
      c.gridwidth = 0;
      c.fill = 2;
      this.getContentPane().add(this.nameField, c);
      c.gridx = 0;
      c.gridy = -1;
      c.gridwidth = 1;
      this.getContentPane().add(clbLabel, c);
      c.gridx = -1;
      c.gridy = 1;
      c.gridwidth = 0;
      this.getContentPane().add(clbLabel2, c);
      c.gridx = 0;
      c.gridy = -1;
      c.gridwidth = 1;
      this.getContentPane().add(clsLabel, c);
      c.gridx = -1;
      c.gridwidth = 0;
      c.gridy = 2;
      c.weightx = 1.0D;
      this.getContentPane().add(this.clsCombo, c);
      c.gridy = -1;
      c.gridx = 0;
      c.gridwidth = 1;
      this.getContentPane().add(alLabel, c);
      c.gridwidth = 0;
      c.fill = 1;
      atrScroll.setMinimumSize(new Dimension(400, 150));
      atrScroll.setPreferredSize(atrScroll.getMinimumSize());
      this.getContentPane().add(atrScroll, c);
      c.gridwidth = 1;
      c.gridx = 0;
      c.fill = 0;
      c.anchor = 10;
      okButton.setPreferredSize(cancelButton.getPreferredSize());
      okButton.setMinimumSize(okButton.getPreferredSize());
      this.getContentPane().add(okButton, c);
      c.gridx = 3;
      c.gridy = 5;
      cancelButton.setMinimumSize(cancelButton.getPreferredSize());
      this.getContentPane().add(cancelButton, c);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            CreateObjectDialog.this.object = null;
         }
      });
   }

   private void buildAttrData() {
      while(this.attrData.getRowCount() > 0) {
         this.attrData.removeRow(0);
      }

      this.classifier = (Classifier)this.clsCombo.getSelectedItem();
      Set attrs = this.classifier.allAttributes();
      this.attrData.setRowCount(attrs.size());
      Iterator it = attrs.iterator();

      for(int i = 0; it.hasNext(); ++i) {
         Attribute at = (Attribute)it.next();
         this.attrData.setValueAt(at, i, 0);
         this.attrData.setValueAt(at.getType().getName(), i, 1);
         Iterator alIt = this.object.getCollectionSlotList().iterator();
         Instance oldValue = null;
         boolean hasSlot = false;

         AttributeLink v;
         while(alIt.hasNext()) {
            v = (AttributeLink)alIt.next();
            if (v.getAttribute() == at) {
               oldValue = v.getValue();
               hasSlot = true;
               break;
            }
         }

         if (!hasSlot) {
            this.object.addSlot((AttributeLink)ModelFactory.createNewElement(at, "AttributeLink"));
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
            literals.add("<undefined>");
            literals.addAll(((Enumeration)at.getType()).getCollectionLiteralList());
            this.editor.setEditorAt(new Integer(i), new DefaultCellEditor(new JComboBox(literals.toArray())));
         } else if (!(at.getType() instanceof DataType)) {
            Instance[] objects = ModelUtilities.getAllObjectsOfKind(at.getType());
            if (objects.length > 0) {
               expr = null;
               java.lang.Object[] objs;
               if (at.getType() == this.classifier) {
                  objs = new java.lang.Object[objects.length + 2];
                  System.arraycopy(objects, 0, objs, 2, objects.length);
                  objs[0] = "undefined";
                  objs[1] = this.object;
               } else {
                  objs = new java.lang.Object[objects.length + 1];
                  System.arraycopy(objects, 0, objs, 1, objects.length);
                  objs[0] = "undefined";
               }

               this.editor.setEditorAt(new Integer(i), new DefaultCellEditor(new JComboBox(objs)));
               int pos = this.findPos(objs, oldValue);
               this.attrData.setValueAt(pos >= 0 ? objs[pos] : objs[0], i, 2);
            } else {
               this.editor.setEditorAt(new Integer(i), new DefaultCellEditor(new JTextField("undefined")));
               this.attrData.setValueAt("undefined", i, 2);
            }
         } else {
            v1 = null;
            if (oldValue == null || oldValue.getName() != null && oldValue.getName().equals("")) {
               expr = at.getInitialValue();
               if (expr != null && expr.getBody() != null && !expr.getBody().equals("")) {
                  v1 = expr.getBody();
               } else {
                  v1 = ModelFactory.getDefaultDataValueAsString((DataType)at.getType());
               }
            } else {
               v1 = oldValue.getName();
            }

            this.attrData.setValueAt(v1, i, 2);
            if (at.getType().getName().equals("Boolean")) {
               this.editor.setEditorAt(new Integer(i), new DefaultCellEditor(new JComboBox(new java.lang.Object[]{"true", "false", "<undefined>"})));
            } else {
               this.editor.setEditorAt(new Integer(i), new DefaultCellEditor(new JTextField(v1)));
            }
         }
      }

   }

   private boolean updateObject() {
      if (this.object.getClassifierList().hasMoreElements()) {
         Classifier old = (Classifier)this.object.getClassifierList().nextElement();
         this.object.removeClassifier(old);
      }

      if (this.classifier != dummyClassifier) {
         this.object.addClassifier(this.classifier);
      }

      this.object.setName(this.nameField.getText());
      Collection attrSlots = new LinkedList();
      Iterator it = this.object.getCollectionSlotList().iterator();

      while(it.hasNext()) {
         AttributeLink slot = (AttributeLink)it.next();
         attrSlots.add(slot.getAttribute());
      }

      Iterator featuresIt = ((Classifier)this.object.getClassifierList().nextElement()).getCollectionFeatureList().iterator();
      LinkedList attrs = new LinkedList();

      while(featuresIt.hasNext()) {
         java.lang.Object feature = featuresIt.next();
         if (feature instanceof Attribute) {
            attrs.add(feature);
         }
      }

      Iterator attrsIt = attrs.iterator();

      while(attrsIt.hasNext()) {
         java.lang.Object att = attrsIt.next();
         if (!attrSlots.contains(att)) {
            AttributeLink newSlot = (AttributeLink)ModelFactory.createNewElement(att, "AttributeLink");
            this.object.addSlot(newSlot);
         }
      }

      for(int i = 0; i < this.attrData.getRowCount(); ++i) {
         Attribute attr = (Attribute)this.attrData.getValueAt(i, 0);
         if (attr == null) {
            return false;
         }

         AttributeLink slot = null;
         if (!this.object.getCollectionSlotList().isEmpty()) {
            Iterator slotsIt = this.object.getCollectionSlotList().iterator();

            while(slotsIt.hasNext()) {
               AttributeLink atLink = (AttributeLink)slotsIt.next();
               if (atLink.getAttribute() == attr) {
                  slot = atLink;
                  break;
               }
            }
         }

         if (slot != null) {
            if (attr.getType() instanceof DataType) {
               if (this.attrData.getValueAt(i, 2) == null) {
                  return false;
               }

               DataValue dVal = ModelFactory.createNewDataValue(this.attrData.getValueAt(i, 2).toString(), attr.getType());
               DataValue oldValue = (DataValue)slot.getValue();
               if (oldValue != null) {
                  java.util.Enumeration classifiers = oldValue.getClassifierList();
                  Classifier cls = null;
                  if (classifiers.hasMoreElements()) {
                     cls = (Classifier)classifiers.nextElement();
                  }

                  if (!oldValue.getAttributeLinkList().hasMoreElements() && !oldValue.getLinkEndList().hasMoreElements()) {
                     ModelFactory.removeDataValue(oldValue.getName(), cls);
                     oldValue.remove();
                  }
               }

               slot.setValue(dVal);
            } else if (attr.getType() instanceof Class) {
               if (this.attrData.getValueAt(i, 2) == null) {
                  return false;
               }

               java.lang.Object obj = this.attrData.getValueAt(i, 2);
               if (obj instanceof Object) {
                  slot.setValue((Object)obj);
               }
            }
         }
      }

      return true;
   }

   public Object getObject() {
      return this.object;
   }

   public static java.lang.Object createInstance(Collaboration collab) {
      CreateObjectDialog dialog = new CreateObjectDialog(collab);
      dialog.setLocation(300, 200);
      dialog.setVisible(true);
      if (dialog.getExitCode() != EXIT_CANCEL && allAttributesSet) {
         ModelFactory.fireModelEvent(dialog.getObject(), collab, 21);
         return dialog.getObject();
      } else {
         ModelFactory.removeElement(dialog.getObject(), collab);
         return null;
      }
   }

   public static void editObject(Object obj) {
      CreateObjectDialog dialog = new CreateObjectDialog(obj);
      dialog.setLocation(300, 200);
      dialog.setVisible(true);
      if (dialog.getExitCode() == EXIT_CANCEL) {
         if (obj instanceof LinkObject) {
            ModelFactory.fireModelEvent(dialog.getObject(), (java.lang.Object)null, 21);
         }

      } else {
         ModelFactory.fireModelEvent(dialog.getObject(), (java.lang.Object)null, 21);
      }
   }

   public int getExitCode() {
      return this.exitCode;
   }

   private Collection getAllVisibleTypes(Namespace ns) {
      Collection col = ns.directGetCollectionOwnedElementList();
      Collection result = new ArrayList();
      Iterator iter = col.iterator();

      while(iter.hasNext()) {
         ModelElement me = (ModelElement)iter.next();
         if (me instanceof Namespace) {
            result.addAll(this.getAllVisibleTypes((Namespace)me));
         }

         if (me instanceof Classifier) {
            result.add(me);
         }
      }

      return result;
   }

   private Classifier[] getAllVisibleTypes(Model model) {
      Classifier[] types = (Classifier[])this.getAllVisibleTypes((Namespace)model).toArray(new Classifier[0]);
      Arrays.sort(types, new Comparator() {
         public int compare(java.lang.Object o1, java.lang.Object o2) {
            Classifier c1 = (Classifier)o1;
            Classifier c2 = (Classifier)o2;
            String name1 = c1.getName() != null ? c1.getName() : "";
            String name2 = c2.getName() != null ? c2.getName() : "";
            return name1.compareTo(name2);
         }

         public boolean equal(java.lang.Object o) {
            return this.compare(this, o) == 0;
         }
      });
      return types;
   }

   private int findPos(java.lang.Object[] objects, java.lang.Object obj) {
      for(int i = 0; i < objects.length; ++i) {
         if (objects[i] == obj) {
            return i;
         }
      }

      return -1;
   }

   static {
      dummyClassifier.setName("<not assigned>");
      allAttributesSet = false;
   }
}
