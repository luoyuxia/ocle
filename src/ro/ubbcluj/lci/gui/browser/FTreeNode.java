package ro.ubbcluj.lci.gui.browser;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;

public class FTreeNode extends DefaultMutableTreeNode {
   public static final int DIRECTORY = 0;
   public static final int CONSTRAINT = 1;
   public static final int MODEL = 2;
   public static final int DIAGRAM = 3;
   public static final int ZIPPED_MODEL = 4;
   public static final int ZIP_ENTRY = 5;
   protected int nodeKind;

   public FTreeNode(String directory) {
      this.setUserObject(directory);
      this.setAllowsChildren(true);
      this.setKind(0);
   }

   public FTreeNode(File file, int kind) {
      this.setUserObject(file);
      this.setKind(kind);
      if (kind != 2 && kind != 4) {
         this.setAllowsChildren(false);
      } else {
         this.setAllowsChildren(true);
      }

   }

   public void setKind(int kind) {
      this.nodeKind = kind;
   }

   public int getKind() {
      return this.nodeKind;
   }

   public boolean isDirectory() {
      return this.getKind() == 0;
   }

   public boolean isFile() {
      return this.getUserObject() instanceof File;
   }

   public boolean isConstraintFile() {
      return this.getKind() == 1;
   }

   public boolean isModelFile() {
      return this.getKind() == 2;
   }

   public boolean isDiagramFile() {
      return this.getKind() == 3;
   }

   public boolean isZippedModelFile() {
      return this.getKind() == 4;
   }

   public boolean isZipEntry() {
      return this.getKind() == 5;
   }

   public String toString() {
      Object obj = this.getUserObject();
      return obj instanceof String ? (String)obj : ((File)obj).getName();
   }
}
