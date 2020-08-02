package ro.ubbcluj.lci.gui.Actions;

import java.util.ArrayList;
import java.util.HashMap;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public class CompilationInfo {
   public static final int WFR = 0;
   public static final int BCR = 1;
   private HashMap contextBindings = new HashMap();
   private boolean compiled = false;
   private OclNode root;
   private int mode = -1;
   private Model targetModel;
   private ArrayList affectedFiles;

   public CompilationInfo(boolean c) {
      this.compiled = c;
   }

   public HashMap getContextBindings() {
      return this.contextBindings;
   }

   public void setCompiled(boolean c) {
      this.compiled = c;
   }

   public boolean isCompiled() {
      return this.compiled;
   }

   public OclNode getRoot() {
      return this.root;
   }

   public void setRoot(OclNode r) {
      this.root = r;
   }

   public int getMode() {
      return this.mode;
   }

   public void setMode(int m) {
      this.mode = m;
   }

   private void clearContextsInAst() {
      if (this.root != null) {
         OclUtil.clean(this.root);
      }
   }

   public void clearUserModelData() {
      this.targetModel = null;
      this.contextBindings.clear();
      this.clearContextsInAst();
   }

   public void setTargetModel(Model m) {
      this.targetModel = m;
   }

   public Model getTargetModel() {
      return this.targetModel;
   }

   public boolean isAffected(String fileName) {
      return this.affectedFiles != null && this.affectedFiles.contains(fileName);
   }

   public void markAffected(String[] a) {
      if (this.affectedFiles == null) {
         this.affectedFiles = new ArrayList();
      } else {
         this.affectedFiles.clear();
      }

      for(int i = 0; i < a.length; ++i) {
         this.affectedFiles.add(a[i]);
      }

   }
}
