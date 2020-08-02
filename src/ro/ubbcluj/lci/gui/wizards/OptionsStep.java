package ro.ubbcluj.lci.gui.wizards;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ro.ubbcluj.lci.codegen.CodeGenerationOptions;
import ro.ubbcluj.lci.gui.tools.AFileFilter;

class OptionsStep extends AbstractWizardStep {
   private JTextField txtDestinationDirectory;
   private JTextField txtIndentSize;
   private JButton btnBrowse;
   private JCheckBox cbIncludeConstraints;
   private JCheckBox cbConvertToSpaces;
   private JCheckBox cbBraceOnNewLine;
   private JLabel lblIndentSize;

   OptionsStep(CodeGenerationWizard owner, CodeGenerationOptions opts) {
      super(owner);
      this.result = opts;
      this.txtDestinationDirectory = new JTextField();
      this.txtIndentSize = new JTextField(4);
      this.btnBrowse = new JButton("...");
      this.btnBrowse.setToolTipText("Browse");
      this.btnBrowse.addActionListener(new OptionsStep.BrowseActionListener());
      this.cbIncludeConstraints = new JCheckBox("Validate business constraints(OCL)");
      this.cbConvertToSpaces = new JCheckBox("Convert tabs to spaces");
      ItemListener ref = new OptionsStep.CustomItemListener();
      this.cbConvertToSpaces.addItemListener(ref);
      this.cbBraceOnNewLine = new JCheckBox("Put opening brace on new line");
      this.lblIndentSize = new JLabel("Number of spaces per tab:");
      this.layoutComponents();
   }

   public boolean prepareToShow() {
      CodeGenerationOptions opts = (CodeGenerationOptions)this.result;

      try {
         this.txtDestinationDirectory.setText(opts.destinationDirectory.getCanonicalFile().getAbsolutePath());
      } catch (IOException var3) {
         this.txtDestinationDirectory = new JTextField();
      }

      this.txtIndentSize.setText(String.valueOf(opts.indentSize));
      if (opts.constraintsAvailable) {
         this.cbIncludeConstraints.setEnabled(true);
         this.cbIncludeConstraints.setSelected(opts.processConstraints);
      } else {
         opts.processConstraints = false;
         this.cbIncludeConstraints.setEnabled(false);
      }

      this.cbConvertToSpaces.setSelected(opts.convertTabsToSpaces);
      this.cbBraceOnNewLine.setSelected(opts.braceOnNewLine);
      return true;
   }

   public boolean prepareToDissapear() {
      String msg = this.updateResult();
      if (msg == null) {
         return true;
      } else {
         JOptionPane.showMessageDialog(this.owner, msg, "Error", 0);
         return false;
      }
   }

   private void layoutComponents() {
      JLabel lblDestination = new JLabel("Destination directory:", 2);
      JPanel topPanel = WizardUtilities.getDefaultPanel("Please select your choices", "/images/wizards/npw_kop.gif");
      JPanel optionPanel = new JPanel();
      JPanel destinationDirectoryPanel = new JPanel();
      JPanel otherOptionsPanel = new JPanel();
      GridBagConstraints gbc = new GridBagConstraints();
      GridBagLayout gbl = new GridBagLayout();
      destinationDirectoryPanel.setLayout(gbl);
      gbc.gridx = gbc.gridy = 0;
      gbc.fill = 0;
      gbc.anchor = 17;
      gbc.gridheight = gbc.gridwidth = 1;
      gbc.weightx = 0.05D;
      gbc.weighty = 1.0D;
      gbc.insets = new Insets(10, 4, 10, 2);
      gbc.ipady = gbc.ipady = 2;
      gbl.setConstraints(lblDestination, gbc);
      destinationDirectoryPanel.add(lblDestination);
      gbc.gridx = 1;
      gbc.fill = 2;
      gbc.anchor = 10;
      gbc.weightx = 0.95D;
      gbc.insets.left = 0;
      gbc.ipadx = gbc.ipady = 0;
      gbl.setConstraints(this.txtDestinationDirectory, gbc);
      destinationDirectoryPanel.add(this.txtDestinationDirectory);
      gbc.gridx = 2;
      gbc.fill = 0;
      gbc.anchor = 13;
      gbc.weightx = 0.0D;
      gbl.setConstraints(this.btnBrowse, gbc);
      destinationDirectoryPanel.add(this.btnBrowse);
      optionPanel.setLayout(new BorderLayout());
      optionPanel.add(destinationDirectoryPanel, "North");
      otherOptionsPanel.setBorder(BorderFactory.createTitledBorder("Code properties"));
      GridBagLayout gbl1 = new GridBagLayout();
      otherOptionsPanel.setLayout(gbl1);
      JPanel pTemp = getHorizontalPanel((Component)this.cbIncludeConstraints);
      this.cbIncludeConstraints.setDisplayedMnemonicIndex(19);
      this.cbIncludeConstraints.setMnemonic('o');
      GridBagConstraints gbc1 = new GridBagConstraints();
      gbc1.gridheight = gbc1.gridwidth = 1;
      gbc1.gridx = gbc1.gridy = 0;
      gbc1.weightx = 0.5D;
      gbc1.weighty = 0.25D;
      gbc1.anchor = 18;
      gbc1.fill = 2;
      gbl1.setConstraints(pTemp, gbc1);
      otherOptionsPanel.add(pTemp);
      pTemp = getHorizontalPanel((Component)this.cbBraceOnNewLine);
      this.cbBraceOnNewLine.setMnemonic('B');
      this.cbBraceOnNewLine.setDisplayedMnemonicIndex(0);
      gbc1.anchor = 17;
      gbc1.gridy = 1;
      gbl1.setConstraints(pTemp, gbc1);
      otherOptionsPanel.add(pTemp);
      pTemp = getHorizontalPanel((Component)this.cbConvertToSpaces);
      this.cbConvertToSpaces.setDisplayedMnemonicIndex(0);
      this.cbConvertToSpaces.setMnemonic('c');
      gbc1.gridy = 2;
      gbc1.anchor = 16;
      gbl1.setConstraints(pTemp, gbc1);
      otherOptionsPanel.add(pTemp);
      this.lblIndentSize.setDisplayedMnemonic('n');
      this.lblIndentSize.setLabelFor(this.txtIndentSize);
      pTemp = getHorizontalPanel(new Component[]{this.lblIndentSize, this.txtIndentSize});
      gbc1.gridx = 1;
      gbc1.anchor = 14;
      gbl1.setConstraints(pTemp, gbc1);
      otherOptionsPanel.add(pTemp);
      pTemp = getHorizontalPanel(Box.createRigidArea(new Dimension(0, 15)));
      gbc1.gridx = 0;
      gbc1.gridy = 3;
      gbc1.anchor = 15;
      gbc1.weightx = 1.0D;
      gbl1.setConstraints(pTemp, gbc1);
      otherOptionsPanel.add(pTemp);
      optionPanel.add(otherOptionsPanel, "Center");
      this.thePanel.add(topPanel, "North");
      this.thePanel.add(optionPanel, "Center");
   }

   private String updateResult() {
      CodeGenerationOptions cgo = (CodeGenerationOptions)this.result;
      this.owner.result = cgo;

      try {
         cgo.destinationDirectory = (new File(this.txtDestinationDirectory.getText())).getCanonicalFile();
      } catch (IOException var3) {
         return var3.getMessage();
      }

      if (cgo.destinationDirectory.exists() && !cgo.destinationDirectory.isDirectory()) {
         return "Chosen file exists and is not a directory.";
      } else {
         cgo.braceOnNewLine = this.cbBraceOnNewLine.isSelected();
         cgo.convertTabsToSpaces = this.cbConvertToSpaces.isSelected();

         try {
            cgo.indentSize = Integer.parseInt(this.txtIndentSize.getText());
            if (cgo.indentSize <= 0 || cgo.indentSize > 8) {
               throw new NumberFormatException("Indent size must be between 1 and 8.");
            }
         } catch (NumberFormatException var4) {
            return "Invalid indent size specified: " + var4.getMessage();
         }

         cgo.processConstraints = this.cbIncludeConstraints.isSelected();
         return null;
      }
   }

   private static JPanel getHorizontalPanel(Component c) {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, 0));
      p.add(Box.createRigidArea(new Dimension(12, 0)));
      p.add(c);
      p.add(Box.createHorizontalGlue());
      return p;
   }

   private static JPanel getHorizontalPanel(Component[] c) {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, 0));
      p.add(Box.createRigidArea(new Dimension(12, 0)));

      for(int i = 0; i < c.length; ++i) {
         p.add(c[i]);
      }

      p.add(Box.createHorizontalGlue());
      return p;
   }

   private class CustomItemListener implements ItemListener {
      private CustomItemListener() {
      }

      public void itemStateChanged(ItemEvent e) {
         OptionsStep.this.lblIndentSize.setVisible(OptionsStep.this.cbConvertToSpaces.isSelected());
         OptionsStep.this.txtIndentSize.setVisible(OptionsStep.this.cbConvertToSpaces.isSelected());
      }
   }

   private class BrowseActionListener implements ActionListener {
      private BrowseActionListener() {
      }

      public void actionPerformed(ActionEvent evt) {
         File dest = AFileFilter.chooseDirectory(OptionsStep.this.owner.getParent());

         try {
            if (dest != null) {
               OptionsStep.this.txtDestinationDirectory.setText(dest.getCanonicalFile().getAbsolutePath());
            }
         } catch (IOException var4) {
         }

      }
   }
}
