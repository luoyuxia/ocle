package ro.ubbcluj.lci.gui.Actions;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import ro.ubbcluj.lci.errors.CompilationErrorMessage;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.ocl.ExceptionCompiler;
import ro.ubbcluj.lci.ocl.OclCompiler;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public class CompilerInvoker {
   public CompilerInvoker() {
   }

   public static void invokeCompilerOnFile(int mode, String filePath, ArrayList errors) {
      String[] files = new String[]{filePath};
      char[] data = null;
      char[][] cdata = new char[1][];

      try {
         data = getCompileData(filePath);
         cdata[0] = data;
      } catch (IOException var12) {
         JOptionPane.showMessageDialog((Component)null, "Could not read file " + filePath + "!", "Error", 0);
         return;
      }

      GRepository r = GRepository.getInstance();
      GUMLModel userModel = r.getUsermodel();
      OclCompiler compiler = GRepository.getInstance().getCompiler();
      switch(mode) {
      case 0:
         compiler.setModel(r.getMetamodel().getModel(), true);
         if (userModel != null) {
            compiler.setUserModel(userModel.getModel());
         }
         break;
      default:
         if (userModel != null) {
            compiler.setModel(userModel.getModel(), false);
         } else {
            compiler.setModel((Model)null, false);
         }
      }

      try {
         compiler.compile(cdata, files);
      } catch (ExceptionCompiler var13) {
         String realFileName = var13.getFilename();
         if (realFileName == null || "".equals(realFileName)) {
            realFileName = filePath;
         }

         CompilationErrorMessage errMsg = new CompilationErrorMessage(realFileName, var13.getStart(), var13.getStop(), var13.getColumn(), var13.getRow(), var13.getMessage());
         errors.add(errMsg);
      }

   }

   public static void invokeCompilerOnFiles(int mode, String[] files, ArrayList errors) {
      GRepository r = GRepository.getInstance();
      OclCompiler compiler = GRepository.getInstance().getCompiler();
      switch(mode) {
      case 0:
         compiler.setModel(r.getMetamodel().getModel(), true);
         if (r.getUsermodel() != null) {
            compiler.setUserModel(r.getUsermodel().getModel());
         }
         break;
      default:
         if (r.getUsermodel() != null) {
            compiler.setModel(r.getUsermodel().getModel(), false);
         } else {
            compiler.setModel((Model)null, false);
         }
      }

      try {
         char[][] data = getCompileData(files);
         compiler.compile(data, files);
      } catch (IOException var8) {
         JOptionPane.showMessageDialog(GMainFrame.getMainFrame(), "One or more files could not be read:" + var8.getMessage(), "Error", 0);
         return;
      } catch (ExceptionCompiler var9) {
         CompilationErrorMessage errMsg = new CompilationErrorMessage(var9.getFilename(), var9.getStart(), var9.getStop(), var9.getColumn(), var9.getRow(), var9.getMessage());
         errors.add(errMsg);
      }

   }

   public static char[][] getCompileData(String[] files) throws IOException {
      char[][] result = new char[files.length][];

      for(int i = 0; i < files.length; ++i) {
         result[i] = getCompileData(files[i]);
      }

      return result;
   }

   private static char[] getCompileData(String file) throws IOException {
      StringBuffer bf = new StringBuffer();
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = br.readLine();

      while(line != null) {
         bf.append(line);
         line = br.readLine();
         if (line != null) {
            bf.append("\n");
         }
      }

      br.close();
      line = bf.toString();
      char[] result = new char[line.length()];
      line.getChars(0, line.length(), result, 0);
      return result;
   }
}
