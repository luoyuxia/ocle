package ro.ubbcluj.lci.gui.Actions;

import javax.swing.JOptionPane;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.tools.ProgressBarDialog;
import ro.ubbcluj.lci.ocl.batcheval.BatchEvaluationSystem;
import ro.ubbcluj.lci.ocl.batcheval.CustomizationDialog;
import ro.ubbcluj.lci.utils.InterruptibleTask;

public class EvaluationSystemInvoker {
   private static EvaluationSystemInvoker instance = null;
   public static final int PARTIAL = 0;
   public static final int FULL = 1;

   private EvaluationSystemInvoker() {
   }

   public static EvaluationSystemInvoker getInvoker() {
      if (instance == null) {
         instance = new EvaluationSystemInvoker();
      }

      return instance;
   }

   public void invokeEvaluationSystem(final int mode) {
      Thread runner = new Thread("Batch evaluation") {
         public void run() {
            GRepository r = GRepository.getInstance();
            BatchEvaluationSystem local_copy = r.getEvaluationSystem();
            if (local_copy == null) {
               JOptionPane.showMessageDialog(GApplication.frame, "Evaluation system not initialized. Cannot check model.", "Error", 0);
            } else {
               CompilationInfo[] targetCI = r.getLastCompilationInfos();
               CompilationInfo ci = targetCI[0];
               local_copy.setUserModel(ci.getTargetModel());
               if (ci.getMode() == 0) {
                  local_copy.addWellFormednessRules(ci.getRoot());
               } else {
                  local_copy.setBusinessConstraintRules(ci.getRoot());
               }

               if (targetCI.length > 1) {
                  local_copy.setBusinessConstraintRules(targetCI[1].getRoot());
               }

               CustomizationDialog dlg = local_copy.getCustomizationDialog();
               dlg.ensureSelection();
               GMainFrame.getMainFrame().clearErrorMessages();
               if (mode == 0) {
                  dlg.invoke();
                  if (dlg.getExitCode() == 2) {
                     return;
                  }
               }

               InterruptibleTask task = local_copy.getTask();
               ProgressBarDialog pbd = new ProgressBarDialog(GApplication.frame, "Evaluating OCL expressions", task);
               local_copy.addProgressListener(pbd);
               pbd.setDescription("Evaluating...");
               pbd.startProgressDialog();
               task.start();

               try {
                  if (!task.isFinished()) {
                     synchronized(task) {
                        task.wait();
                     }
                  }
               } catch (InterruptedException var11) {
                  var11.printStackTrace();
               }

               local_copy.removeProgressListener(pbd);
               if (pbd.isVisible()) {
                  pbd.setVisible(false);
                  pbd.dispose();
               }

            }
         }
      };
      runner.setPriority(1);
      runner.start();
   }
}
