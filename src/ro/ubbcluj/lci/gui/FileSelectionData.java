package ro.ubbcluj.lci.gui;

public class FileSelectionData {
   private String fileName;
   private boolean selected;

   public FileSelectionData(String fileName, boolean selected) {
      this.fileName = fileName;
      this.selected = selected;
   }

   public String getFileName() {
      return this.fileName;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   public String toString() {
      return this.fileName;
   }
}
