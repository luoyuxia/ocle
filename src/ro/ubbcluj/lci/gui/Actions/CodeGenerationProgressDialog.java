package ro.ubbcluj.lci.gui.Actions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import ro.ubbcluj.lci.errors.ErrorListener;
import ro.ubbcluj.lci.errors.ErrorMessage;
import ro.ubbcluj.lci.gui.GUtils;
import ro.ubbcluj.lci.utils.BlinkerThread;
import ro.ubbcluj.lci.utils.InterruptibleTask;
import ro.ubbcluj.lci.utils.Utils;
import ro.ubbcluj.lci.utils.progress.ProgressListener;

class CodeGenerationProgressDialog extends JDialog implements ErrorListener, ProgressListener {
   private JLabel[] labels = new JLabel[4];
   private String[] descriptionBases = new String[4];
   private JButton btnCancel;
   private int errorCount = 0;
   private int activeLabelIndex;
   private boolean isCancelled;
   private InterruptibleTask currentTask = null;
   private BlinkerThread blinker;
   private JProgressBar pb;

   public CodeGenerationProgressDialog(Frame owner) {
      super(owner, "Code generation progress", true);
      this.setDefaultCloseOperation(0);
      this.setSize(3 * Utils.screenSize.width / 8, 3 * Utils.screenSize.height / 8);
      this.setLocation(5 * Utils.screenSize.width / 16, 5 * Utils.screenSize.height / 16);
      this.descriptionBases[0] = "Checking model.. ";
      this.descriptionBases[1] = "Processing constraints.. ";
      this.descriptionBases[2] = "Preparing model elements.. ";
      this.descriptionBases[3] = "Generating source files.. ";

      for(int i = 0; i < 4; ++i) {
         this.labels[i] = new JLabel(this.descriptionBases[i]);
      }

      this.labels[0].setIcon(GUtils.loadIcon("/resources/model_check.gif"));
      this.labels[1].setIcon(GUtils.loadIcon("/resources/ocl1.gif"));
      this.btnCancel = new JButton("Cancel");
      this.btnCancel.addActionListener(new CodeGenerationProgressDialog.CancellationListener());
      this.pb = new JProgressBar(0, 0, 100);
      this.pb.setForeground(Color.BLUE.darker());
      this.pb.setStringPainted(false);
      this.layoutComponents();
   }

   public void startTask(final Thread task, final int taskIndex, final boolean progressWatched) {
      Thread taskStarter = new Thread("Task starter") {
         public void run() {
            synchronized(CodeGenerationProgressDialog.this.pb) {
               CodeGenerationProgressDialog.this.pb.setValue(CodeGenerationProgressDialog.this.pb.getMinimum());
               CodeGenerationProgressDialog.this.pb.setIndeterminate(!progressWatched);
            }

            CodeGenerationProgressDialog.this.activeLabelIndex = taskIndex;
            CodeGenerationProgressDialog.this.startBlinking(CodeGenerationProgressDialog.this.activeLabelIndex);
            if (task instanceof InterruptibleTask) {
               CodeGenerationProgressDialog.this.currentTask = (InterruptibleTask)task;
               CodeGenerationProgressDialog.this.btnCancel.setEnabled(true);
            } else {
               CodeGenerationProgressDialog.this.btnCancel.setEnabled(false);
            }

            CodeGenerationProgressDialog.this.isCancelled = false;
            task.start();
         }
      };
      taskStarter.start();
   }

   boolean isCancelled() {
      return this.isCancelled;
   }

   public void stopBlinking() {
      if (this.blinker != null) {
         this.blinker.cancel();
         if (this.blinker.isAlive()) {
            try {
               synchronized(this.blinker) {
                  this.blinker.wait();
               }
            } catch (InterruptedException var4) {
            }
         }

         this.blinker = null;
      }

   }

   public void startBlinking(int labelIndex) {
      this.stopBlinking();
      this.blinker = new BlinkerThread(this.labels[labelIndex], 250);
      this.blinker.start();
   }

   public void errorOccured(ErrorMessage msg) {
      Thread updater = new Thread("Label updater") {
         public void run() {
            CodeGenerationProgressDialog.this.errorCount++;
            StringBuffer bf = new StringBuffer(CodeGenerationProgressDialog.this.descriptionBases[CodeGenerationProgressDialog.this.activeLabelIndex]);
            bf.append('(').append(CodeGenerationProgressDialog.this.errorCount).append(" problems)");
            synchronized(CodeGenerationProgressDialog.this.labels[CodeGenerationProgressDialog.this.activeLabelIndex]) {
               CodeGenerationProgressDialog.this.labels[CodeGenerationProgressDialog.this.activeLabelIndex].setText(bf.toString());
            }
         }
      };
      updater.start();
   }

   public void stop() {
   }

   public void progressValueChanged(final int precent, Object details) {
      Thread updater = new Thread("Progress bar updater") {
         public void run() {
            synchronized(CodeGenerationProgressDialog.this.pb) {
               CodeGenerationProgressDialog.this.pb.setValue(precent);
            }
         }
      };
      updater.start();
   }

   public void start(String filename) {
   }

   public void resetErrorCount() {
      this.errorCount = 0;
   }

   public int getCurrentErrorCount() {
      return this.errorCount;
   }

   private void layoutComponents() {
      JPanel mainPanel = new JPanel();
      JPanel progressPanel = new JPanel();
      JPanel buttonPanel = new JPanel();
      progressPanel.setLayout(new GridLayout(5, 1));
      JPanel temp = new JPanel();
      temp.setLayout(new BoxLayout(temp, 0));
      temp.add(Box.createRigidArea(new Dimension(10, 0)));
      temp.add(this.pb);
      temp.add(Box.createRigidArea(new Dimension(10, 0)));
      Dimension ms = this.pb.getMaximumSize();
      this.pb.setMaximumSize(new Dimension(ms.width, ms.height / 2));
      this.pb.setBorderPainted(false);
      progressPanel.add(temp);

      for(int i = 0; i < 4; ++i) {
         temp = new JPanel();
         temp.setLayout(new BoxLayout(temp, 0));
         temp.add(Box.createHorizontalGlue());
         temp.add(this.labels[i]);
         temp.add(Box.createHorizontalGlue());
         progressPanel.add(temp);
      }

      buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
      this.btnCancel.setMaximumSize(this.btnCancel.getPreferredSize());
      buttonPanel.add(Box.createHorizontalGlue());
      buttonPanel.add(this.btnCancel);
      mainPanel.setLayout(new BorderLayout());
      mainPanel.add(progressPanel, "Center");
      mainPanel.add(buttonPanel, "South");
      this.getContentPane().add(mainPanel);
   }

   private class CancellationListener implements ActionListener {
      private CancellationListener() {
      }

      public void actionPerformed(ActionEvent e) {
         Thread stopper = new Thread("Cancellation thread") {
            public void run() {
               CodeGenerationProgressDialog.this.stopBlinking();
               CodeGenerationProgressDialog.this.currentTask.cancel();
               CodeGenerationProgressDialog.this.isCancelled = true;
               CodeGenerationProgressDialog.this.btnCancel.setEnabled(false);
            }
         };
         stopper.start();
      }
   }
}
