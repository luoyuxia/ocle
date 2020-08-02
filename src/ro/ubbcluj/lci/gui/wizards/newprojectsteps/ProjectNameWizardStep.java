package ro.ubbcluj.lci.gui.wizards.newprojectsteps;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import ro.ubbcluj.lci.gui.GUtils;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.gui.wizards.AbstractWizardStep;
import ro.ubbcluj.lci.gui.wizards.Wizard;

public class ProjectNameWizardStep extends AbstractWizardStep implements ActionListener {
   private static final int NAME_ID = 0;
   private static final int LOC_ID = 1;
   private boolean[] fieldStates = new boolean[2];
   private ProjectNameWizardStep.ContentSensitiveTextField nameField;
   private ProjectNameWizardStep.ContentSensitiveTextField locField;

   public ProjectNameWizardStep(Wizard owner) {
      super(owner);
      this.initComponents();
      this.nextEnabled = false;
   }

   public boolean prepareToShow() {
      if (this.nameField.getText().length() > 0 && this.locField.getText().length() > 0) {
         this.setNextButton(true);
      } else {
         this.setNextButton(false);
      }

      return true;
   }

   public boolean prepareToDissapear() {
      this.result = new Vector();
      ((Vector)this.result).add(this.nameField.getText());
      ((Vector)this.result).add(this.locField.getText());
      return true;
   }

   private void initComponents() {
      JPanel topPanel = new JPanel();
      topPanel.setBackground(Color.WHITE);
      topPanel.setLayout(new BoxLayout(topPanel, 0));
      topPanel.setBorder(BorderFactory.createEtchedBorder());
      JLabel text = new JLabel("Name the project and select the location of the project file");
      text.setAlignmentX(0.0F);
      text.setAlignmentY(0.5F);
      text.setFont(new Font("SansSerif", 1, 12));
      JLabel image = new JLabel(GUtils.loadIcon("/images/wizards/npw_kop.gif"));
      topPanel.add(Box.createHorizontalStrut(10));
      topPanel.add(text);
      topPanel.add(Box.createHorizontalGlue());
      topPanel.add(image);
      topPanel.add(Box.createHorizontalStrut(10));
      JPanel centerPanel = new JPanel();
      centerPanel.setBorder(BorderFactory.createEtchedBorder());
      centerPanel.setLayout(new BoxLayout(centerPanel, 1));
      JLabel nameText = new JLabel("Project name");
      JLabel locText = new JLabel("Project file and location");
      this.nameField = new ProjectNameWizardStep.ContentSensitiveTextField(0);
      this.locField = new ProjectNameWizardStep.ContentSensitiveTextField(1);
      this.locField.setText("");
      JButton browseButton = new JButton("Browse...");
      browseButton.addActionListener(this);
      browseButton.setActionCommand("Browse");
      browseButton.setMargin(new Insets(1, 5, 1, 5));
      browseButton.setFont(new Font("Default", 0, 9));
      JPanel nameFieldPanel = new JPanel();
      nameFieldPanel.setLayout(new BoxLayout(nameFieldPanel, 0));
      nameFieldPanel.add(Box.createHorizontalStrut(10));
      nameFieldPanel.add(this.nameField);
      nameFieldPanel.add(Box.createHorizontalStrut(10));
      JPanel nameTextPanel = new JPanel();
      nameTextPanel.setLayout(new FlowLayout(0, 10, 10));
      nameTextPanel.add(nameText);
      JPanel locFieldPanel = new JPanel();
      locFieldPanel.setLayout(new BoxLayout(locFieldPanel, 0));
      locFieldPanel.add(Box.createHorizontalStrut(10));
      locFieldPanel.add(this.locField);
      locFieldPanel.add(browseButton);
      locFieldPanel.add(Box.createHorizontalStrut(10));
      JPanel locTextPanel = new JPanel();
      locTextPanel.setLayout(new FlowLayout(0, 10, 10));
      locTextPanel.add(locText);
      centerPanel.add(Box.createVerticalStrut(10));
      centerPanel.add(nameTextPanel);
      centerPanel.add(nameFieldPanel);
      centerPanel.add(Box.createVerticalStrut(5));
      centerPanel.add(locTextPanel);
      centerPanel.add(locFieldPanel);
      centerPanel.add(Box.createVerticalStrut(100));
      this.thePanel.add(centerPanel, "Center");
      this.thePanel.add(topPanel, "North");
   }

   public void actionPerformed(ActionEvent ae) {
      JButton source = (JButton)ae.getSource();
      if (source.getActionCommand().equals("Browse")) {
         AFileFilter[] ff = new AFileFilter[]{new AFileFilter("oepr", "OCLE projects (*.oepr)")};
         File file = AFileFilter.chooseFile(1, this.owner, ff);
         if (file != null) {
            try {
               this.locField.setText(file.getCanonicalPath());
               String fileName = file.getName();
               int dotPosition = fileName.indexOf(46);
               this.nameField.setText(fileName.substring(0, dotPosition));
            } catch (IOException var7) {
               System.err.println("File does not exist!");
            }
         }
      }

   }

   private final void signalTextFieldChange(int id, boolean state) {
      this.fieldStates[id] = state;
      if (this.nextEnabled != (this.fieldStates[0] && this.fieldStates[1])) {
         this.setNextButton(this.fieldStates[0] && this.fieldStates[1]);
      }

   }

   private class ContentSensitiveTextField extends JTextField {
      private final int id;

      public ContentSensitiveTextField(int id) {
         this.id = id;
      }

      protected Document createDefaultModel() {
         return new ProjectNameWizardStep.ContentSensitiveTextField.ContentSensitiveDocument();
      }

      private class ContentSensitiveDocument extends PlainDocument {
         private ContentSensitiveDocument() {
         }

         public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offs, str, a);
            if (this.getLength() > 0) {
               ProjectNameWizardStep.this.signalTextFieldChange(ContentSensitiveTextField.this.id, true);
            } else {
               ProjectNameWizardStep.this.signalTextFieldChange(ContentSensitiveTextField.this.id, false);
            }

         }

         public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);
            if (this.getLength() > 0) {
               ProjectNameWizardStep.this.signalTextFieldChange(ContentSensitiveTextField.this.id, true);
            } else {
               ProjectNameWizardStep.this.signalTextFieldChange(ContentSensitiveTextField.this.id, false);
            }

         }
      }
   }
}
