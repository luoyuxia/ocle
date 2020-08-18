package ro.ubbcluj.lci.gui.mainframe;


import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.swing.*;

import ro.ubbcluj.lci.gui.Actions.AToolsActions;
import ro.ubbcluj.lci.gui.FileSelectionData;
import ro.ubbcluj.lci.gui.SplashScreen;
import ro.ubbcluj.lci.gui.Actions.AProjectActions;
import ro.ubbcluj.lci.gui.Actions.AUMLModelActions;
import ro.ubbcluj.lci.gui.browser.MMBrowser;
import ro.ubbcluj.lci.gui.editor.Editor;
import ro.ubbcluj.lci.gui.editor.options.EditorProperties;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.ocl.OclLoader;
import ro.ubbcluj.lci.ocl.batcheval.BatchEvaluationSystem;
import ro.ubbcluj.lci.utils.uml.UMLNavigationUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GApplication {
   public static final String APP_NAME = "OCL 模型验证";
   private static final String APP_VERSION = "2.0";
   private static final String APP_DESCRIPTION = "";
   public static final String APP_CONF_DIR;
   public static final String APP_TEMP_DIR;
   public static JFrame frame;
   private static GApplication instance;
   private Editor editor;

   private GApplication() {
      try {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.configureEditor();
   }

   private void configureEditor() {
      Properties pp = new Properties();
      EditorProperties.initProperties();
      EditorProperties.setDirectory(APP_CONF_DIR);
      this.editor = new Editor();
      InputStream is = Editor.class.getResourceAsStream("editor.properties");
      if (is != null) {
         try {
            pp.load(is);
            this.editor.configure(pp);
            is.close();
         } catch (IOException var4) {
            JOptionPane.showMessageDialog((Component)null, "Could not configure editor: " + var4.getMessage(), "Fatal error", 0);
            System.exit(-1);
         }
      } else {
         JOptionPane.showMessageDialog((Component)null, "Could not configure editor: properties file not found", "Fatal error", 0);
         System.exit(-1);
      }

   }

   public final Editor getEditor() {
      return this.editor;
   }

   public static GApplication getApplication() {
      return instance;
   }

   public void exit(int exit_code) {
      GAbstractProject project = GRepository.getInstance().getProject();
      if (project != null) {
         AProjectActions.closeProjectAction.actionPerformed((ActionEvent)null);
      } else {
         GUMLModel model = GRepository.getInstance().getUsermodel();
         if (model != null) {
            AUMLModelActions.closeUmlAction.actionPerformed((ActionEvent)null);
         }
      }

      AFileFilter ff = new AFileFilter("ocl", "OCL text files (*.ocl)");
      AFileFilter ff2 = new AFileFilter("bcr", "OCL text files (*.bcr)");
      this.editor.addFileFilter(ff);
      this.editor.addFileFilter(ff2);
      if (this.editor.getView().closeAll() == 2) {
         System.exit(exit_code);
      } else {
         this.editor.removeFileFilter(ff);
         this.editor.removeFileFilter(ff2);
      }

   }

   public String toString() {
      return APP_NAME + " " + APP_VERSION + " - " + APP_DESCRIPTION;
   }

   public static void main(String[] args) {
      String modelPath = args[0];
      String oclPath = args[1];
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension app_size = new Dimension(screen.width / 2,
              screen.height / 2);
      Point app_start_point = new Point(0, 0);
      frame.setSize(app_size);
      frame.setLocation(app_start_point);
      frame.setDefaultCloseOperation(0);
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            GApplication.instance.exit(0);
         }
      });
      frame.setLocationRelativeTo(null);
   //   new SplashScreen("/images/Splash.gif", frame);

      try {
         GUMLModel metamodel = new GUMLModel("metamodel/uml15.xml.zip", true) {
            protected void initModelViews() {
               MMBrowser browser = new MMBrowser(this);
               if (this.views.size() > 0 && this.views.get(0) != null) {
                  this.views.set(0, browser);
               } else {
                  this.views.add(browser);
               }

            }
         };
         UMLNavigationUtilities.setOwnerModel(metamodel.getModel());
         GRepository.getInstance().setMetamodel(metamodel);
         OclLoader.setMetamodel(metamodel.getModel());
      } catch (Exception var6) {
         var6.printStackTrace();
         JOptionPane.showMessageDialog(frame, "Could not load UML 1.5 Metamodel!", "Fatal error", 0);
         System.exit(-1);
      }

      try {
         GRepository.getInstance().setEvaluationSystem(new BatchEvaluationSystem(GRepository.getInstance().getMetamodel().getModel(), frame));
      } catch (Exception var5) {
         JOptionPane.showMessageDialog(frame, "Unable to load and configure evaluation system. Model checking will not be enabled!", "Error", 1);
         var5.printStackTrace();
         GRepository.getInstance().setEvaluationSystem((BatchEvaluationSystem)null);
      }

      GMainFrame mainframe = GMainFrame.getMainFrame();
      frame.getContentPane().add(mainframe);
      frame.setVisible(false);
      mainframe.updateLog("UML 1.5 Metamodel succesfully loaded.\n");
      if (GRepository.getInstance().getEvaluationSystem() != null) {
         mainframe.updateLog("Evaluation system successfully loaded and configured.\n");
      }

   //   SplashScreen.signal();
      mainframe.focusFileTree();
      mainframe.updateCompilationActions();
      mainframe.updateEvaluationActions();
      mainframe.updateProjectActions();
      mainframe.updateFilesActions();
      mainframe.updateModelActions();
      mainframe.setVisible(false);


      String tmpProjectName = "tmpOclProject";
      String tmpProjectFile = "tmpProjectFile";
      DefaultListModel modelXmlFiles = new DefaultListModel();
      modelXmlFiles.addElement(new FileSelectionData(modelPath,
              true));
      DefaultListModel oclXmlFiles = new DefaultListModel();
      oclXmlFiles.addElement(new FileSelectionData(oclPath,
              true));

      List tmpPaths = ProjectManager.getInstance().newEmptyProject(tmpProjectName, tmpProjectFile,
              modelXmlFiles.toArray(),
              oclXmlFiles.toArray());
      AToolsActions.compileAction.actionPerformed(null);
      deleteFiles(tmpPaths);
      for (int i = 0; i < mainframe.messagePane.list.size(); i++) {
         System.out.println(mainframe.messagePane.list.get(i).toString());
      }
      GApplication.instance.exit(0);
   }
   private static void deleteFiles(List files) {
      for (int i = 0; i < files.size(); i++) {
         File file = new File(files.get(i).toString());
         if (file.exists()) {
            file.delete();
         }
      }
   }

   static {
      APP_CONF_DIR = APP_NAME + "_" + APP_VERSION;
      APP_TEMP_DIR = APP_CONF_DIR + File.separator + "Temporary";
      frame = new JFrame(APP_NAME + " " + APP_VERSION + " - " + APP_DESCRIPTION);
      instance = new GApplication();
   }
}
