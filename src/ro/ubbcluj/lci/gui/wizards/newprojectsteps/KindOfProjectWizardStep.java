package ro.ubbcluj.lci.gui.wizards.newprojectsteps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import ro.ubbcluj.lci.gui.GUtils;
import ro.ubbcluj.lci.gui.wizards.AbstractWizardStep;
import ro.ubbcluj.lci.gui.wizards.Wizard;

public class KindOfProjectWizardStep extends AbstractWizardStep implements ActionListener {
   public static final String EMPTY_PRJ = "Empty project";
   public static final String EXIST_PRJ = "Existing project";
   private boolean nextEnabled = false;
   private ButtonGroup bg;

   public KindOfProjectWizardStep(Wizard owner) {
      super(owner);
      this.initComponents();
   }

   public boolean prepareToShow() {
      if (this.bg.getSelection() != null) {
         this.setNextButton(true);
      } else {
         this.setNextButton(false);
      }

      return true;
   }

   public boolean prepareToDissapear() {
      return true;
   }

   private void initComponents() {
      JPanel topPanel = new JPanel();
      topPanel.setBackground(Color.WHITE);
      topPanel.setLayout(new BoxLayout(topPanel, 0));
      topPanel.setBorder(BorderFactory.createEtchedBorder());
      JLabel text = new JLabel("Select kind of project");
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
      centerPanel.setLayout(new BorderLayout());
      JPanel optionsPanel = new JPanel();
      optionsPanel.setBackground(Color.WHITE);
      optionsPanel.setLayout(new FlowLayout(0, 10, 10));
      JRadioButton emptyJrb = new JRadioButton();
      emptyJrb.setContentAreaFilled(false);
      emptyJrb.setIcon(GUtils.loadIcon("/images/wizards/npw_kop_ep.gif"));
      emptyJrb.setSelectedIcon(GUtils.loadIcon("/images/wizards/npw_kop_eps.gif"));
      emptyJrb.setSelected(false);
      emptyJrb.setActionCommand("Empty");
      emptyJrb.addActionListener(this);
      JRadioButton existingJrb = new JRadioButton();
      existingJrb.setContentAreaFilled(false);
      existingJrb.setIcon(GUtils.loadIcon("/images/wizards/npw_kop_ef.gif"));
      existingJrb.setSelectedIcon(GUtils.loadIcon("/images/wizards/npw_kop_efs.gif"));
      existingJrb.setSelected(false);
      existingJrb.setActionCommand("Existing");
      existingJrb.addActionListener(this);
      this.bg = new ButtonGroup();
      this.bg.add(emptyJrb);
      this.bg.add(existingJrb);
      optionsPanel.add(emptyJrb);
      optionsPanel.add(existingJrb);
      centerPanel.add(optionsPanel, "Center");
      this.thePanel.add(centerPanel, "Center");
      this.thePanel.add(topPanel, "North");
   }

   public void actionPerformed(ActionEvent ae) {
      JRadioButton source = (JRadioButton)ae.getSource();
      if (source.getActionCommand().equals("Empty")) {
         if (!this.nextEnabled) {
            this.setNextButton(true);
         }

         this.result = "Empty project";
      } else if (source.getActionCommand().equals("Existing")) {
         if (!this.nextEnabled) {
            this.setNextButton(true);
         }

         this.result = "Existing project";
      }

   }
}
