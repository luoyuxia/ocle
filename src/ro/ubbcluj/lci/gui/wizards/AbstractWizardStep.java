package ro.ubbcluj.lci.gui.wizards;

import java.awt.BorderLayout;
import java.util.Observable;
import javax.swing.JPanel;

public abstract class AbstractWizardStep extends Observable {
   protected JPanel thePanel = new JPanel(new BorderLayout());
   protected Wizard owner;
   protected Object result;
   protected boolean nextEnabled;

   public AbstractWizardStep(Wizard owner) {
      this.setOwner(owner);
      this.addObserver(owner);
   }

   public JPanel getPanel() {
      return this.thePanel;
   }

   public void setOwner(Wizard owner) {
      this.owner = owner;
   }

   public Wizard getOwner() {
      return this.owner;
   }

   public Object getResult() {
      return this.result;
   }

   public boolean prepareToShow() {
      return true;
   }

   public boolean prepareToDissapear() {
      return true;
   }

   protected void setNextButton(boolean state) {
      this.nextEnabled = state;
      Wizard.NextButtonState bs = this.owner.new NextButtonState(this.nextEnabled);
      this.setChanged();
      this.notifyObservers(bs);
   }
}
