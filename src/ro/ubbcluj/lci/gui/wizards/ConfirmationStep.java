package ro.ubbcluj.lci.gui.wizards;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ro.ubbcluj.lci.codegen.CodeGenerationOptions;

class ConfirmationStep extends AbstractWizardStep {
   private JPanel confirmPanel1;
   private JPanel confirmPanel2;
   private JPanel detailPanel;

   ConfirmationStep(CodeGenerationWizard owner) {
      super(owner);
      this.thePanel.setLayout(new BoxLayout(this.thePanel, 1));
      this.thePanel.add(WizardUtilities.getDefaultPanel("Confirm your choices", "/images/wizards/npw_kop.gif"));
      this.thePanel.add(this.detailPanel = new JPanel());
      this.detailPanel.setLayout(new BoxLayout(this.detailPanel, 1));
      this.confirmPanel1 = getHorizontalPanel(new JLabel("Code generation wizard has gathered all required information"));
      this.confirmPanel2 = getHorizontalPanel(new JLabel("Click \"Finish\" to start code generation, \"Back\" if you want to change the options"));
   }

   public boolean prepareToShow() {
      Dimension dm = new Dimension(0, 10);
      this.detailPanel.removeAll();
      JComponent[] lbls = this.buildConfirmationLabels();
      this.detailPanel.add(Box.createRigidArea(new Dimension(0, 20)));

      for(int i = 0; i < lbls.length; ++i) {
         this.detailPanel.add(lbls[i]);
         this.detailPanel.add(Box.createRigidArea(dm));
      }

      this.detailPanel.validate();
      return super.prepareToShow();
   }

   private JComponent[] buildConfirmationLabels() {
      JComponent[] temp = getText((CodeGenerationOptions)this.owner.result);
      JPanel[] result = new JPanel[2 + temp.length];
      result[0] = this.confirmPanel1;
      result[1] = this.confirmPanel2;
      System.arraycopy(temp, 0, result, 2, temp.length);
      return result;
   }

   private static JComponent[] getText(CodeGenerationOptions cgo) {
      StringBuffer bf1 = new StringBuffer();
      bf1.append("Destination directory:").append(cgo.destinationDirectory.getAbsolutePath());
      StringBuffer bf2 = new StringBuffer();
      bf2.append("Opening brace will be placed on ").append(cgo.braceOnNewLine ? "a new line" : "the same line");
      StringBuffer bf3 = new StringBuffer();
      bf3.append("Tabs are ").append(cgo.convertTabsToSpaces ? "" : "not ").append("converted to spaces\n");
      StringBuffer bf4 = null;
      if (cgo.convertTabsToSpaces) {
         bf4 = new StringBuffer("Indent size is ");
         bf4.append(cgo.indentSize);
      }

      JPanel[] result = new JPanel[bf4 == null ? 3 : 4];
      result[0] = getHorizontalPanel(new JLabel(bf1.toString()));
      result[1] = getHorizontalPanel(new JLabel(bf2.toString()));
      result[2] = getHorizontalPanel(new JLabel(bf3.toString()));
      if (bf4 != null) {
         result[3] = getHorizontalPanel(new JLabel(bf4.toString()));
      }

      return result;
   }

   private static JPanel getHorizontalPanel(JComponent c) {
      JPanel result = new JPanel();
      result.setLayout(new BoxLayout(result, 0));
      result.add(Box.createHorizontalGlue());
      result.add(c);
      result.add(Box.createHorizontalGlue());
      return result;
   }
}
