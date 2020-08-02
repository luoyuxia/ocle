package ro.ubbcluj.lci.utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinition;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;

public class CreateTaggedValueDialog extends JDialog {
   public static int EXIT_OK = 1;
   public static int EXIT_CANCEL = 0;
   private TaggedValue taggedValue;
   private TagDefinition tagDefinition = null;
   private JComboBox tagDefinitionsCombo;
   private JLabel jlTagDefinition;
   private JLabel jlTagDefType;
   private JLabel jlTagDefTypeName;
   private JLabel jlTaggedValue;
   private JTextField jtTagValue;
   private int exitCode;

   protected CreateTaggedValueDialog() {
      super(GApplication.frame, "Create tagged value", true);
      this.exitCode = EXIT_CANCEL;

      try {
         this.init();
         this.pack();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void init() {
      this.jlTagDefinition = new JLabel("Tag definitions :");
      this.jlTaggedValue = new JLabel("TaggedValue value: ");
      this.jlTagDefType = new JLabel("Tag definition type : ");
      this.jlTagDefTypeName = new JLabel();
      this.jtTagValue = new JTextField();
      this.tagDefinitionsCombo = new JComboBox();
      Iterator it = ModelFactory.getDTDTagDefinitions().values().iterator();

      while(it.hasNext()) {
         this.tagDefinitionsCombo.addItem(it.next());
      }

      this.tagDefinition = (TagDefinition)this.tagDefinitionsCombo.getModel().getElementAt(0);
      if (this.tagDefinitionsCombo.getSelectedItem() != null) {
         this.jlTagDefTypeName.setText(((TagDefinition)this.tagDefinitionsCombo.getSelectedItem()).getTagType());
      }

      this.tagDefinitionsCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            CreateTaggedValueDialog.this.tagDefinition = (TagDefinition)CreateTaggedValueDialog.this.tagDefinitionsCombo.getSelectedItem();
            if (CreateTaggedValueDialog.this.tagDefinition != null) {
               CreateTaggedValueDialog.this.jlTagDefTypeName.setText(CreateTaggedValueDialog.this.tagDefinition.getTagType());
            }

         }
      });
      JButton okButton = new JButton("OK");
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            CreateTaggedValueDialog.this.exitCode = CreateTaggedValueDialog.EXIT_OK;
            CreateTaggedValueDialog.this.createTaggedValue();
            CreateTaggedValueDialog.this.dispose();
            CreateTaggedValueDialog.this.setVisible(false);
         }
      });
      JButton cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            CreateTaggedValueDialog.this.exitCode = CreateTaggedValueDialog.EXIT_CANCEL;
            CreateTaggedValueDialog.this.dispose();
            CreateTaggedValueDialog.this.setVisible(false);
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
      this.getContentPane().add(this.jlTagDefinition, c);
      c.gridx = -1;
      c.gridwidth = 0;
      c.fill = 2;
      this.getContentPane().add(this.tagDefinitionsCombo, c);
      c.gridx = 0;
      c.gridy = -1;
      c.gridwidth = 1;
      this.getContentPane().add(this.jlTagDefType, c);
      c.gridx = -1;
      c.gridy = 1;
      c.gridwidth = 0;
      this.getContentPane().add(this.jlTagDefTypeName, c);
      c.gridx = 0;
      c.gridy = -1;
      c.gridwidth = 1;
      this.getContentPane().add(this.jlTaggedValue, c);
      c.gridx = -1;
      c.gridwidth = 0;
      c.gridy = 2;
      this.getContentPane().add(this.jtTagValue, c);
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 3;
      c.fill = 0;
      c.anchor = 10;
      okButton.setPreferredSize(cancelButton.getPreferredSize());
      okButton.setMinimumSize(okButton.getPreferredSize());
      this.getContentPane().add(okButton, c);
      c.gridx = 3;
      c.gridy = 3;
      cancelButton.setMinimumSize(cancelButton.getPreferredSize());
      this.getContentPane().add(cancelButton, c);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            CreateTaggedValueDialog.this.taggedValue = null;
         }
      });
   }

   public TaggedValue getTaggedValue() {
      return this.taggedValue;
   }

   public void createTaggedValue() {
      this.taggedValue = (TaggedValue)ModelFactory.createNewElement(this.tagDefinition, "TaggedValue");
      this.taggedValue.setDataValue(this.jtTagValue.getText());
   }

   public static Object createTaggedValue(ModelElement owner) {
      CreateTaggedValueDialog dialog = new CreateTaggedValueDialog();
      dialog.setLocation(300, 200);
      dialog.setVisible(true);
      if (dialog.getExitCode() == CreateObjectDialog.EXIT_CANCEL) {
         if (dialog.getTaggedValue() != null) {
            ModelFactory.removeElement(dialog.getTaggedValue(), owner);
         }

         return null;
      } else {
         owner.addTaggedValue(dialog.getTaggedValue());
         ModelFactory.fireModelEvent(dialog.getTaggedValue(), owner, 21);
         return dialog.getTaggedValue();
      }
   }

   public int getExitCode() {
      return this.exitCode;
   }
}
