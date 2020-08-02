package ro.ubbcluj.lci.ocl.gui;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface OclEditor {
   OclEditorConfig getConfig();

   void loadFromStream(InputStream var1) throws IOException;

   void saveToStream(OutputStream var1) throws IOException;

   void setText(String var1);

   void insertText(int var1, String var2);

   char[] getText();

   int getTextLength();

   int getCaretPosition();

   void setCaretPosition(int var1);

   int getSelectionStart();

   int getSelectionEnd();

   void select(int var1, int var2);

   String getSelectedText();

   void setError(int var1, int var2);

   void copy();

   void paste();

   void cut();

   void delete();

   void undo();

   void redo();

   int[] find(String var1, boolean var2, boolean var3);

   Point screenCoord(int var1);

   void requestFocus();
}
