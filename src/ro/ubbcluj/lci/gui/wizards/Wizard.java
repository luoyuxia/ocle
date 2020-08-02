package ro.ubbcluj.lci.gui.wizards;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class Wizard extends JDialog implements Observer, ActionListener {
   private static final int BACK_STEP = -1;
   private static final int NEXT_STEP = 1;
   protected int currentStep;
   protected Vector steps = new Vector();
   protected Object result = null;
   protected boolean cancelled;
   protected JButton btnContinue;
   protected JButton[] btn = new JButton[4];

   public Wizard(Frame owner, String title) {
      super(owner, title, true);
      this.initComponents();
      this.pack();
      this.setSize(500, 325);
      this.setResizable(false);
      Point location = this.getParent().getLocation();
      Dimension psize = this.getParent().getSize();
      Dimension winsize = this.getSize();
      Point win_loc = new Point(Math.abs(psize.width / 2 - winsize.width / 2) + location.x, Math.abs(psize.height / 2 - winsize.height / 2) + location.y);
      this.setLocation(win_loc);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
            Wizard.this.cancelled = true;
            Wizard.this.result = null;
            Wizard.this.endWizard();
         }
      });
   }

   public void runWizard() {
      this.runWizard(0);
   }

   public void runWizard(int step) {
      this.currentStep = step;
      if (this.currentStep > this.steps.size()) {
         System.err.println("No steps registered for this wizard");
      } else {
         this.getContentPane().remove(0);
         AbstractWizardStep aws = (AbstractWizardStep)this.steps.elementAt(this.currentStep);
         this.getContentPane().add(aws.getPanel(), 0);
         this.refreshBtnStates();
         aws.prepareToShow();
         this.setVisible(true);
      }
   }

   protected void endWizard() {
      this.setVisible(false);
   }

   public Object getResult() {
      return this.result;
   }

   public void registerWizardStep(AbstractWizardStep ws) {
      this.steps.add(ws);
   }

   public void initComponents() {
      Container contentPane = this.getContentPane();
      JPanel btnPanel = new JPanel() {
         public Dimension getPreferredSize() {
            return new Dimension(this.preferredSize().width + 20, this.preferredSize().height + 20);
         }
      };
      btnPanel.setBorder(BorderFactory.createEtchedBorder());
      btnPanel.setLayout(new BoxLayout(btnPanel, 0));
      btnPanel.add(Box.createHorizontalGlue());
      this.btn[0] = new JButton("<   Back");
      this.btn[0].addActionListener(this);
      btnPanel.add(this.btn[0]);
      btnPanel.add(Box.createHorizontalStrut(5));
      this.btn[1] = new JButton("Next   >");
      this.btn[1].addActionListener(this);
      btnPanel.add(this.btn[1]);
      btnPanel.add(Box.createHorizontalStrut(20));
      this.btn[2] = new JButton("Finish");
      this.btn[2].addActionListener(this);
      btnPanel.add(this.btn[2]);
      btnPanel.add(Box.createHorizontalStrut(20));
      this.btn[3] = new JButton("Cancel");
      this.btn[3].addActionListener(this);
      btnPanel.add(this.btn[3]);
      btnPanel.add(Box.createHorizontalStrut(10));
      JPanel ws = new JPanel();
      ws.setBorder(BorderFactory.createEtchedBorder());
      contentPane.add(ws, "Center");
      contentPane.add(btnPanel, "South");
   }

   public void update(Observable o, Object arg) {
      Wizard.NextButtonState bs = (Wizard.NextButtonState)arg;
      bs.setButtonState();
   }

   public void actionPerformed(ActionEvent ae) {
      JButton source = (JButton)ae.getSource();
      if (source.getText().equals("<   Back")) {
         this.changeWizardStep(-1);
      } else if (source.getText().equals("Next   >")) {
         this.changeWizardStep(1);
      } else if (source.getText().equals("Finish")) {
         if (((AbstractWizardStep)this.steps.elementAt(this.currentStep)).prepareToDissapear()) {
            this.cancelled = false;
            this.endWizard();
         }
      } else if (source.getText().equals("Cancel")) {
         this.cancelled = true;
         this.result = null;
         this.endWizard();
      }

   }

   private void changeWizardStep(int step) {
      AbstractWizardStep aws = (AbstractWizardStep)this.steps.elementAt(this.currentStep);
      if (step == -1 || step == 1 && aws.prepareToDissapear()) {
         this.processStepResult(this.currentStep);
         this.currentStep += step;
         this.getContentPane().remove(0);
         aws = (AbstractWizardStep)this.steps.elementAt(this.currentStep);
         this.getContentPane().add(aws.getPanel(), 0);
         this.refreshBtnStates();
         if (!aws.prepareToShow()) {
            System.err.println("Wrong wizard step configuration!");
         }

         this.paintAll(this.getGraphics());
      }

   }

   protected void refreshBtnStates() {
      this.btn[0].setEnabled(this.currentStep != 0);
      this.btn[1].setEnabled(false);
      this.btn[2].setEnabled(false);
      this.btnContinue = this.currentStep != this.steps.size() - 1 ? this.btn[1] : this.btn[2];
   }

   protected void processStepResult(int step) {
   }

   public class NextButtonState {
      boolean state;

      public NextButtonState(boolean state) {
         this.state = state;
      }

      private void setButtonState() {
         Wizard.this.btnContinue.setEnabled(this.state);
      }
   }
}
