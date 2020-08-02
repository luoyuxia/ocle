package ro.ubbcluj.lci.gui.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import ro.ubbcluj.lci.utils.BlinkerThread;
import ro.ubbcluj.lci.utils.InterruptibleTask;
import ro.ubbcluj.lci.utils.Utils;
import ro.ubbcluj.lci.utils.progress.ProgressListener;

public class ProgressBarDialog extends JDialog implements ProgressListener {
   private static JProgressBar jpb = new JProgressBar(0, 0, 100);
   private boolean isCanceled;
   private Object sync_object;
   private JLabel lblDescription;
   private BlinkerThread blinker;
   private InterruptibleTask task;

   public ProgressBarDialog(Frame parent, String title, InterruptibleTask tTask) {
      this(parent, title);
      this.task = tTask;
   }

   public ProgressBarDialog(Frame parent, String title) {
      super(parent, title);
      this.isCanceled = false;
      this.sync_object = new Object();
      this.task = null;
      this.setDefaultCloseOperation(0);
      this.setSize(400, 150);
      this.setResizable(false);
      Dimension winsize = this.getSize();
      Point win_loc = new Point((Utils.screenSize.width - winsize.width) / 2, (Utils.screenSize.height - winsize.height) / 2);
      this.setLocation(win_loc);
      jpb.setForeground(Color.blue);
      jpb.setBorderPainted(true);
      jpb.setStringPainted(true);
      jpb.setAlignmentX(0.5F);
      jpb.setBorder(BorderFactory.createLineBorder(Color.black, 1));
      jpb.setMaximumSize(new Dimension(this.getWidth() - 50, jpb.getPreferredSize().height));
      JPanel mainPanel = new JPanel();
      JPanel labelPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, 1));
      mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      this.lblDescription = new JLabel("", 0);
      labelPanel.setLayout(new BoxLayout(labelPanel, 0));
      labelPanel.add(Box.createHorizontalGlue());
      labelPanel.add(this.lblDescription);
      labelPanel.add(Box.createHorizontalGlue());
      mainPanel.add(labelPanel);
      mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      mainPanel.add(jpb);
      JButton bCancel = new JButton("Cancel");
      bCancel.setAlignmentX(0.5F);
      ProgressBarDialog.ProgressBarListener listener = new ProgressBarDialog.ProgressBarListener();
      bCancel.addActionListener(listener);
      mainPanel.add(Box.createRigidArea(new Dimension(0, this.getHeight() / 5)));
      mainPanel.add(bCancel);
      this.getContentPane().add(mainPanel);
   }

   public void setDescription(String strDesc) {
      this.lblDescription.setText(strDesc);
   }

   public boolean isCanceled() {
      synchronized(this.sync_object) {
         return this.isCanceled;
      }
   }

   public void startProgressDialog() {
      this.isCanceled = false;
      jpb.setValue(0);
      Runnable start = new Runnable() {
         public void run() {
            ProgressBarDialog.this.setVisible(true);
            ProgressBarDialog.this.blinker = new BlinkerThread(ProgressBarDialog.this.lblDescription, 500);
            ProgressBarDialog.this.blinker.start();
         }
      };

      try {
         SwingUtilities.invokeLater(start);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void setBarColor(Color newColor) {
      jpb.setForeground(newColor);
   }

   public void progressValueChanged(int newValue, Object details) {
      jpb.setValue(newValue);
      if (newValue >= jpb.getMaximum()) {
         Runnable finish = new Runnable() {
            public void run() {
               ProgressBarDialog.this.blinker.cancel();
               ProgressBarDialog.this.setVisible(false);
               ProgressBarDialog.this.dispose();
            }
         };

         try {
            SwingUtilities.invokeAndWait(finish);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   public void start(String filename) {
   }

   public void stop() {
   }

   private class ProgressBarListener implements ActionListener {
      private ProgressBarListener() {
      }

      public void actionPerformed(ActionEvent e) {
         if (e.getActionCommand().equals("Cancel")) {
            ProgressBarDialog.this.dispose();
            synchronized(ProgressBarDialog.this.sync_object) {
               ProgressBarDialog.this.isCanceled = true;
            }

            ProgressBarDialog.this.blinker.cancel();
            if (ProgressBarDialog.this.task != null) {
               Runnable interrupt = new Runnable() {
                  public void run() {
                     ProgressBarDialog.this.task.cancel();
                     ProgressBarDialog.this.task = null;
                  }
               };

               try {
                  SwingUtilities.invokeLater(interrupt);
               } catch (Exception var4) {
                  var4.printStackTrace();
               }
            }
         }

      }
   }
}
