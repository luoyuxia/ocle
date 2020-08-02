package ro.ubbcluj.lci.gui.editor.event;

import java.util.EventObject;
import ro.ubbcluj.lci.gui.editor.TextDocumentModel;

public class DocumentStateEvent extends EventObject {
   public DocumentStateEvent(TextDocumentModel model) {
      super(model);
   }
}
