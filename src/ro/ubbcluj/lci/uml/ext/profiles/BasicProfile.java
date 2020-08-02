package ro.ubbcluj.lci.uml.ext.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ro.ubbcluj.lci.errors.BasicErrorMessage;
import ro.ubbcluj.lci.errors.CompilationErrorMessage;
import ro.ubbcluj.lci.gui.Actions.CompilationInfo;
import ro.ubbcluj.lci.gui.Actions.CompilerInvoker;
import ro.ubbcluj.lci.ocl.ExceptionCompiler;
import ro.ubbcluj.lci.ocl.OclCompiler;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.utils.FileUtilities;

class BasicProfile implements Profile {
   private String name;
   private ArrayList files = new ArrayList();
   private ArrayList errors;
   private CompilationInfo info = new CompilationInfo(false);

   BasicProfile() {
      this.info.setMode(0);
   }

   public boolean containsFile(String fileName) {
      String real = null;

      try {
         real = FileUtilities.asFullPath(fileName);
      } catch (IOException var4) {
      }

      return real == null ? false : this.files.contains(real);
   }

   public void cleanUp() {
      this.info.clearUserModelData();
   }

   public List getErrors() {
      return Collections.unmodifiableList(this.errors);
   }

   public OclNode getRules() {
      return this.info.getRoot();
   }

   public CompilationInfo getInfo() {
      return this.info;
   }

   public void compile(Model umlMetamodel) {
      this.errors = new ArrayList();
      OclNode rules = this.info.getRoot();
      if (rules == null) {
         OclCompiler compiler = new OclCompiler(umlMetamodel, true);
         String[] cfiles = (String[])this.files.toArray(new String[0]);

         try {
            char[][] data = CompilerInvoker.getCompileData(cfiles);
            compiler.compile(data, cfiles);
         } catch (IOException var7) {
            this.errors.add(new BasicErrorMessage("Error reading profile files: " + var7.getMessage()));
         } catch (ExceptionCompiler var8) {
            CompilationErrorMessage msg = new CompilationErrorMessage(var8.getFilename(), var8.getStart(), var8.getStop(), var8.getColumn(), var8.getRow(), var8.getMessage());
            this.errors.add(msg);
         }

         if (this.errors.isEmpty()) {
            this.info.setRoot(compiler.getRoot());
            this.info.setCompiled(true);
         } else {
            this.info.setRoot((OclNode)null);
            this.info.setCompiled(false);
         }

      }
   }

   public void registerFile(String localName) {
      String realFile = null;

      try {
         realFile = FileUtilities.asFullPath(localName);
      } catch (IOException var4) {
      }

      if (realFile != null) {
         this.files.add(realFile);
      }

   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
