package ro.ubbcluj.lci.gui.editor;

public class TextDocumentModelFactory {
   private Editor editor;
   private int currentFileCounter = 0;

   public TextDocumentModelFactory(Editor ed) {
      this.editor = ed;
   }

   public TextDocumentModel newPadModel() {
      ++this.currentFileCounter;
      return new TextDocumentModel(this.editor, this.currentFileCounter);
   }

   public TextDocumentModel newUncountedPadModel() {
      return new TextDocumentModel(this.editor, -1);
   }
}
