package ro.ubbcluj.lci.gui.editor.event;

import java.util.EventListener;

public interface UndoListener extends EventListener {
   void editRegistered(UndoEvent var1);

   void editRedone(UndoEvent var1);

   void editUndone(UndoEvent var1);
}
