package ro.ubbcluj.lci.gui.tools;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ro.ubbcluj.lci.gui.mainframe.GApplication;

public class JDialogChooser extends JDialog {
   public static final int EXIT_OK = 1;
   public static final int EXIT_CANCEL = 0;
   protected int exitCode = 0;
   protected JLabel infoLabel = new JLabel();
   protected JLabel participantsLabel = new JLabel();
   protected DefaultListModel dlm = new DefaultListModel();
   protected JList list;

   public JDialogChooser(String title) {
      super(GApplication.frame, title, true);
      this.list = new JList(this.dlm);
      JButton okB = new JButton("OK");
      JButton cancelB = new JButton("Cancel");
      ActionListener l = new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            if (evt.getActionCommand().equals("OK")) {
               JDialogChooser.this.exitCode = 1;
            }

            JDialogChooser.this.setVisible(false);
         }
      };
      okB.addActionListener(l);
      cancelB.addActionListener(l);
      okB.setPreferredSize(cancelB.getPreferredSize());
      okB.setMinimumSize(cancelB.getMinimumSize());
      JPanel bPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      bPanel.add(okB, gbc);
      gbc.gridx = 1;
      bPanel.add(Box.createHorizontalStrut(10));
      gbc.gridx = 2;
      bPanel.add(cancelB, gbc);
      this.getContentPane().setLayout(new GridBagLayout());
      gbc = new GridBagConstraints();
      Insets is = new Insets(5, 5, 5, 5);
      gbc.anchor = 17;
      gbc.insets = is;
      gbc.weightx = 1.0D;
      this.getContentPane().add(this.infoLabel, gbc);
      gbc.gridy = 1;
      this.getContentPane().add(this.participantsLabel, gbc);
      gbc.gridy = 2;
      gbc.fill = 1;
      gbc.weighty = 1.0D;
      this.getContentPane().add(new JScrollPane(this.list), gbc);
      gbc.gridy = 3;
      gbc.weightx = 0.0D;
      gbc.weighty = 0.0D;
      gbc.fill = 0;
      gbc.anchor = 13;
      gbc.insets.right = 15;
      this.getContentPane().add(bPanel, gbc);
   }

   public void setInfo(String info) {
      this.infoLabel.setText(info);
   }

   public void setParticipants(String source, String target) {
      this.participantsLabel.setText("Source object is " + source + " and " + "target object is " + target);
   }

   public void setListContent(Object[] items) {
      for(int i = 0; i < items.length; ++i) {
         this.dlm.addElement(items[i]);
      }

   }

   public void setSelectionMode(int mode) {
      this.list.setSelectionMode(mode);
   }

   public int getExitCode() {
      return this.exitCode;
   }

   public Object[] getSelectedValues() {
      return this.list.getSelectedValues();
   }

   public void show() {
      this.pack();
      Dimension d = this.getPreferredSize();
      Point p = new Point();
      p.x = GApplication.frame.getSize().width / 2 - d.width / 2;
      p.y = GApplication.frame.getSize().height / 2 - d.height / 2;
      this.setLocation(p);
      super.show();
   }
}
