package ro.ubbcluj.lci.gui.Actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import ro.ubbcluj.lci.codegen.CodeGenerationOptions;
import ro.ubbcluj.lci.codegen.CodeGeneratorManager;
import ro.ubbcluj.lci.errors.BasicErrorMessage;
import ro.ubbcluj.lci.errors.CompilationErrorMessage;
import ro.ubbcluj.lci.gui.mainframe.GAbstractProject;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.wizards.CodeGenerationWizard;
import ro.ubbcluj.lci.ocl.ExceptionCompiler;
import ro.ubbcluj.lci.ocl.OclCompiler;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.batcheval.BatchEvaluationSystem;
import ro.ubbcluj.lci.ocl.codegen.gen.OclCodeGenerator;
import ro.ubbcluj.lci.ocl.codegen.norm.DefaultCodePreparator;
import ro.ubbcluj.lci.ocl.codegen.norm.NormalForm;
import ro.ubbcluj.lci.ocl.codegen.norm.NormalFormBuilder;
import ro.ubbcluj.lci.ocl.codegen.utils.OclCodeGenUtilities;
import ro.ubbcluj.lci.uml.ext.profiles.Profile;
import ro.ubbcluj.lci.uml.ext.profiles.ProfileManager;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.utils.InterruptibleTask;
import ro.ubbcluj.lci.utils.text.BlockStructureLanguageFormatter;

public class CodeGenerationController {
   private CodeGenerationWizard wizard = null;
   private boolean stop;
   private CodeGenerationOptions options = new CodeGenerationOptions();
   private CodeGenerationProgressDialog cgpd;
   private NormalForm nf;
   private CodeGeneratorManager cgManager;
   private boolean bSuccessfullyCompleted;

   public CodeGenerationController() {
      this.options.indentSize = 4;
      this.options.processConstraints = true;
      this.options.braceOnNewLine = false;
      this.options.convertTabsToSpaces = true;
      this.options.destinationDirectory = new File("./output");
   }

   public void startCodeGeneration() {
      GMainFrame.getMainFrame().clearErrorMessages();
      Thread t = new Thread("Code generator") {
         public void run() {
            CodeGenerationController.this.stop = false;
            CodeGenerationController.this.bSuccessfullyCompleted = true;
            CodeGenerationController.this.options.constraintsAvailable = GRepository.getInstance().getProject() != null;
            CodeGenerationController.this.invokeWizard();
            if (CodeGenerationController.this.wizard.getResult() == null) {
               CodeGenerationController.this.stop = true;
            }

            if (!CodeGenerationController.this.stop) {
               CodeGenerationController.this.cgpd = new CodeGenerationProgressDialog(GApplication.frame);
               CodeGenerationController.this.checkModel();
               if (CodeGenerationController.this.cgpd.isCancelled()) {
                  CodeGenerationController.this.stop = true;
               } else if (CodeGenerationController.this.cgpd.getCurrentErrorCount() > 0) {
                  int choice = JOptionPane.showConfirmDialog(CodeGenerationController.this.cgpd, "Current model contains errors.The resulting Java code might not compile. Are you sure you want to continue with code generation?", "Erroneus model", 0, 2);
                  if (choice != 0) {
                     CodeGenerationController.this.stop = true;
                  }
               }
            }

            if (!CodeGenerationController.this.stop) {
               CodeGenerationController.this.processConstraints();
            }

            if (!CodeGenerationController.this.stop) {
               CodeGenerationController.this.prepareModelElements();
            }

            if (!CodeGenerationController.this.stop) {
               CodeGenerationController.this.generateCode();
            }

            CodeGenerationController.this.cleanUp();
            System.gc();
            if (CodeGenerationController.this.stop) {
               JOptionPane.showMessageDialog(GApplication.frame, "Code generation cancelled.", "Code generation", 1);
            } else {
               JOptionPane.showMessageDialog(GApplication.frame, "Code generation completed " + (CodeGenerationController.this.bSuccessfullyCompleted ? "successfully." : "with errors."), "Code generation", 1);
            }

         }
      };
      t.start();
   }

   private void invokeWizard() {
      if (this.wizard == null) {
         this.wizard = new CodeGenerationWizard(GApplication.frame, this.options);
      }

      this.wizard.runWizard();
   }

   private void checkModel() {
      GRepository ri = GRepository.getInstance();
      InformationWindow iw = new InformationWindow(GApplication.frame, "Loading and configuring profiles. Please wait ...");
      iw.setVisible(true);
      BatchEvaluationSystem evsys = ri.getEvaluationSystem();
      Model userModel = ri.getUsermodel().getModel();
      evsys.setUserModel(userModel);
      ArrayList profileErrors = null;
      GRepository.getInstance().loadProfiles();
      ProfileManager localProfiler = ri.getProfileManager();
      Profile umlProfile = localProfiler.getProfileByName("UML");
      umlProfile.compile(ri.getMetamodel().getModel());
      OclNode rules = umlProfile.getRules();
      if (rules == null) {
         this.stop = true;
         profileErrors = new ArrayList();
         profileErrors.addAll(umlProfile.getErrors());
      } else {
         evsys.addWellFormednessRules(rules);
         umlProfile.getInfo().setTargetModel(userModel);
      }

      Profile javaProfile = localProfiler.getProfileByName("Java");
      javaProfile.compile(GRepository.getInstance().getMetamodel().getModel());
      rules = javaProfile.getRules();
      if (rules == null) {
         this.stop = true;
         if (profileErrors == null) {
            profileErrors = new ArrayList();
         }

         profileErrors.addAll(javaProfile.getErrors());
      } else {
         evsys.addWellFormednessRules(rules);
         javaProfile.getInfo().setTargetModel(userModel);
      }

      iw.setVisible(false);
      iw.dispose();
      iw = null;
      if (this.stop) {
         JOptionPane.showMessageDialog(GApplication.frame, "Current profiles could not be compiled", "Error", 0);
         GMainFrame.getMainFrame().updateMessages((List)profileErrors);
      } else {
         Thread checkerTask = evsys.getTask();
         evsys.addProgressListener(this.cgpd);
         evsys.addErrorListener(this.cgpd);
         Thread tVisible = new Thread("Make progress dialog visible") {
            public void run() {
               CodeGenerationController.this.cgpd.setVisible(true);
            }
         };
         SwingUtilities.invokeLater(tVisible);
         this.cgpd.startTask(checkerTask, 0, true);
         synchronized(checkerTask) {
            try {
               checkerTask.wait();
            } catch (InterruptedException var15) {
               var15.printStackTrace();
            }
         }

         evsys.removeErrorListener(this.cgpd);
         evsys.removeProgressListener(this.cgpd);
         this.cgpd.stopBlinking();
      }
   }

   private void processConstraints() {
      if (this.options.processConstraints) {
         this.cgpd.resetErrorCount();
         GAbstractProject prj = GRepository.getInstance().getProject();
         CompilationInfo ciBcr = prj.getBcrInfo();
         List files = prj.getSelectedConstraints();
         List realBcrs = new ArrayList();

         for(int i = 0; i < files.size(); ++i) {
            String currentFile = (String)files.get(i);
            if (OclUtil.getOclFileType(currentFile) == 2) {
               realBcrs.add(currentFile);
            }
         }

         String[] fileNames = (String[])realBcrs.toArray(new String[0]);
         if (fileNames.length > 0) {
            this.cgpd.startBlinking(1);
            char[][] data = (char[][])null;

            try {
               data = CompilerInvoker.getCompileData(fileNames);
            } catch (IOException var18) {
               this.stop = true;
               BasicErrorMessage ioerr = new BasicErrorMessage("One or more files could not be compiled");
               GMainFrame.getMainFrame().updateMessages((Object)ioerr);
               return;
            }

            if (this.cgpd.isCancelled()) {
               this.stop = true;
            } else {
               OclCompiler compiler = new OclCompiler(GRepository.getInstance().getUsermodel().getModel(), false);

               try {
                  compiler.compile(data, fileNames);
                  ciBcr.setRoot(compiler.getRoot());
                  ciBcr.setCompiled(true);
               } catch (ExceptionCompiler var17) {
                  CompilationErrorMessage cem = new CompilationErrorMessage(var17.getFilename(), var17.getStart(), var17.getStop(), var17.getColumn(), var17.getRow(), var17.getMessage());
                  this.cgpd.errorOccured(cem);
                  GMainFrame.getMainFrame().updateMessages((Object)cem);
                  this.stop = true;
                  return;
               }

               if (this.cgpd.isCancelled()) {
                  this.stop = true;
               } else {
                  NormalFormBuilder builder = new NormalFormBuilder(ciBcr.getRoot());
                  builder.addErrorListener(this.cgpd);
                  this.cgpd.startTask(builder, 1, false);

                  try {
                     if (!builder.isFinished()) {
                        synchronized(builder) {
                           builder.wait();
                        }
                     }
                  } catch (InterruptedException var16) {
                     System.err.println("Interrupted while normalizing");
                  }

                  builder.removeErrorListener(this.cgpd);
                  if (this.cgpd.isCancelled()) {
                     this.stop = true;
                  } else {
                     this.nf = builder.getResult();
                     OclCodeGenUtilities.setCurrentNormalForm(this.nf);
                     List errs = builder.getWarningsAsList();
                     GMainFrame.getMainFrame().updateMessages(errs);
                     DefaultCodePreparator dcp = new DefaultCodePreparator(this.nf);
                     this.cgpd.startTask(dcp, 1, false);

                     try {
                        if (!dcp.isFinished()) {
                           synchronized(dcp) {
                              dcp.wait();
                           }
                        }
                     } catch (InterruptedException var14) {
                     }

                     dcp = null;
                     if (this.cgpd.isCancelled()) {
                        this.stop = true;
                     }

                  }
               }
            }
         }
      }
   }

   private void prepareModelElements() {
      this.cgManager = new CodeGeneratorManager(GRepository.getInstance().getUsermodel().getModel());
      this.cgManager.setOptions(this.options);
      if (this.options.processConstraints) {
         this.cgManager.clearDecorators();
         this.cgManager.addCodeDecorator(new OclCodeGenerator(this.nf));
      }

      this.cgManager.addErrorListener(this.cgpd);
      InterruptibleTask task = this.cgManager.getPreparationTask();
      this.cgpd.startTask(task, 2, false);

      try {
         if (!task.isFinished()) {
            synchronized(task) {
               task.wait();
            }
         }
      } catch (InterruptedException var5) {
      }

      this.cgManager.removeErrorListener(this.cgpd);
      List errs = this.cgManager.getErrors();
      if (errs.size() > 0) {
         this.bSuccessfullyCompleted = false;
         GMainFrame.getMainFrame().updateMessages(errs);
      }

      if (this.cgpd.isCancelled()) {
         this.stop = true;
      }

   }

   private void generateCode() {
      this.cgpd.resetErrorCount();
      BlockStructureLanguageFormatter tf = new BlockStructureLanguageFormatter();
      tf.addBlockDelimiter("{");
      tf.addBlockEndDelimiter("}");
      tf.addSeparators(";*/+=-(){}[]^%.,?:!~|\\<> \t\n");
      tf.setConvertTabsToSpaces(this.options.convertTabsToSpaces);
      tf.setIndentTabSize(this.options.indentSize);
      CodeGeneratorManager.getSharedContext().put("BRACE_ON_NEW_LINE", this.options.braceOnNewLine ? Boolean.TRUE : Boolean.FALSE);
      this.cgManager.setFormatter(tf);
      this.cgManager.addProgressListener(this.cgpd);
      this.cgManager.addErrorListener(this.cgpd);
      InterruptibleTask task = this.cgManager.getGenerationTask();
      this.cgpd.startTask(task, 3, true);

      try {
         if (!task.isFinished()) {
            synchronized(task) {
               task.wait();
            }
         }
      } catch (InterruptedException var6) {
      }

      this.cgManager.removeProgressListener(this.cgpd);
      this.cgManager.removeErrorListener(this.cgpd);
      List errs = this.cgManager.getErrors();
      if (errs.size() > 0) {
         this.bSuccessfullyCompleted = false;
         GMainFrame.getMainFrame().updateMessages(errs);
      }

      if (this.cgpd.isCancelled()) {
         this.stop = true;
      }

   }

   private void cleanUp() {
      this.nf = null;
      OclCodeGenUtilities.setCurrentNormalForm((NormalForm)null);
      this.cgManager = null;
      if (this.cgpd != null) {
         this.cgpd.setVisible(false);
         this.cgpd.dispose();
         this.cgpd = null;
      }

   }
}
