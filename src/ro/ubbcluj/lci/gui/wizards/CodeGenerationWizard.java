package ro.ubbcluj.lci.gui.wizards;

import java.awt.Frame;
import ro.ubbcluj.lci.codegen.CodeGenerationOptions;

public class CodeGenerationWizard extends Wizard {
   public CodeGenerationWizard(Frame parentFrame, CodeGenerationOptions opts) {
      super(parentFrame, "Code generation");
      this.registerSteps(opts);
   }

   protected void refreshBtnStates() {
      this.btn[0].setEnabled(this.currentStep > 0);
      this.btn[1].setEnabled(this.currentStep == 0);
      this.btn[2].setEnabled(this.currentStep == 1);
      this.btn[3].setEnabled(true);
   }

   private void registerSteps(CodeGenerationOptions opts) {
      this.registerWizardStep(new OptionsStep(this, opts));
      this.registerWizardStep(new ConfirmationStep(this));
   }
}
