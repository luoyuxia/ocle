package ro.ubbcluj.lci.gui.editor.event;

import java.util.EventListener;
import ro.ubbcluj.lci.gui.editor.TextDocumentModel;

public interface DocumentStateListener extends EventListener {
   void documentSaved(DocumentStateEvent var1);

   void documentModified(DocumentStateEvent var1);

   void documentStateChanged(TextDocumentModel var1);
}
