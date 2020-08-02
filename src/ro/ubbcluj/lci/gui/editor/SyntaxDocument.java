package ro.ubbcluj.lci.gui.editor;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Hashtable;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Segment;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import ro.ubbcluj.lci.gui.editor.event.SimpleDocumentEvent;
import ro.ubbcluj.lci.gui.editor.event.UndoEvent;
import ro.ubbcluj.lci.gui.editor.event.UndoListener;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.editor.jedit.TokenMarker;
import ro.ubbcluj.lci.gui.editor.undo.DeleteEdit;
import ro.ubbcluj.lci.gui.editor.undo.InsertionEdit;
import ro.ubbcluj.lci.gui.editor.undo.ReplaceAllEdit;
import ro.ubbcluj.lci.gui.editor.undo.ReplaceEdit;
import ro.ubbcluj.lci.gui.editor.undo.UndoableEditSupportDocument;
import ro.ubbcluj.lci.gui.editor.utils.IntArray;

public class SyntaxDocument implements UndoableEditSupportDocument {
   private TokenMarker tokenMarker;
   private ArrayList edits = new ArrayList(100);
   private CompoundEdit compound = null;
   private EventListenerList listenerList = new EventListenerList();
   private Hashtable properties = new Hashtable();
   private IntArray lineStarts = new IntArray();
   private IntArray lineCounts = new IntArray();
   private ArrayList lines = new ArrayList();
   private byte[] buffer = new byte[0];
   private int currentLength = 0;
   int undoIndex = 0;
   private static final int MAX_VISIBLE_LINES = 500;

   public SyntaxDocument() {
      this.lines.add("");
      this.lineStarts.add(0, -1);
      this.lineCounts.add(0, -1);
   }

   public TokenMarker getTokenMarker() {
      return this.tokenMarker;
   }

   public void setTokenMarker(TokenMarker tm) {
      this.tokenMarker = tm;
      if (tm != null) {
         this.tokenMarker.insertLines(0, this.getLineCount());
         this.tokenizeLines();
      }
   }

   public void tokenizeLines() {
      this.tokenizeLines(0, this.lines.size());
   }

   public void tokenizeLines(int start, int len) {
      if (this.tokenMarker != null && this.tokenMarker.supportsMultilineTokens()) {
         Segment lineSegment = new Segment();
         int max = Math.min(500, len);
         max += start;
         int stop = Math.min(max, this.lines.size());

         try {
            int i = start;
            int lineStart = this.offset(start, 0);
            int ll = this.lineLength(start);

            while(i < stop) {
               this.getText(lineStart, ll, lineSegment);
               this.tokenMarker.markTokens(lineSegment, i);
               ++i;
               lineStart += ll + 1;
               if (i < stop) {
                  ll = this.lineLength(i);
               }
            }
         } catch (BadLocationException var10) {
            var10.printStackTrace();
         }

      }
   }

   public void beginCompoundEdit() {
   }

   public void endCompoundEdit() {
      if (this.compound != null) {
         this.compound.end();
         this.registerUndo(this.compound);
         this.compound = null;
      }

   }

   public void addUndoableEdit(UndoableEdit ed) {
      if (this.compound == null) {
         this.registerUndo(ed);
      } else {
         this.compound.addEdit(ed);
      }

   }

   protected void fireInsertUpdate(DocumentEvent evt) {
      if (this.tokenMarker != null) {
         SimpleDocumentEvent sde = (SimpleDocumentEvent)evt;
         int off = sde.getOffset();
         int c = this.coord(off)[0];
         this.tokenMarker.insertLines(c, sde.getLinesInserted());
      }

      this.standardFireInsertUpdate(evt);
   }

   protected void fireRemoveUpdate(DocumentEvent evt) {
      if (this.tokenMarker != null) {
         SimpleDocumentEvent sde = (SimpleDocumentEvent)evt;
         int off = sde.getOffset();
         int c = this.coord(off)[0];
         this.tokenMarker.deleteLines(c, sde.getLinesRemoved());
      }

      this.standardFireRemoveUpdate(evt);
   }

   public void undo() throws CannotUndoException {
      if (!this.canUndo()) {
         throw new CannotUndoException();
      } else {
         UndoableEdit ed = (UndoableEdit)this.edits.get(this.undoIndex - 1);
         ed.undo();
         --this.undoIndex;
         this.fireEditUndone(ed);
      }
   }

   public void redo() throws CannotRedoException {
      if (!this.canRedo()) {
         throw new CannotRedoException();
      } else {
         UndoableEdit ed = (UndoableEdit)this.edits.get(this.undoIndex);
         ed.redo();
         ++this.undoIndex;
         this.fireEditRedone(ed);
      }
   }

   public boolean canUndo() {
      return this.undoIndex > 0 && this.compound == null && this.edits.size() > 0 ? ((UndoableEdit)this.edits.get(this.undoIndex - 1)).canUndo() : false;
   }

   public String getUndoPresentationName() {
      String undoText = UIManager.getString("AbstractUndoableEdit.undoText");
      return this.canUndo() ? undoText + " " + ((UndoableEdit)this.edits.get(this.undoIndex - 1)).getPresentationName() : undoText;
   }

   public boolean canRedo() {
      return this.compound == null && this.undoIndex < this.edits.size() ? ((UndoableEdit)this.edits.get(this.undoIndex)).canRedo() : false;
   }

   public String getRedoPresentationName() {
      String redoText = UIManager.getString("AbstractUndoableEdit.redoText");
      return this.canRedo() ? redoText + " " + ((UndoableEdit)this.edits.get(this.undoIndex)).getPresentationName() : redoText;
   }

   public void setText(byte[] text) {
      this.buffer = null;
      System.gc();
      this.buffer = text;
      this.clearUndo();
      this.lines.clear();
      this.lineStarts.clear();
      this.lineCounts.clear();
      this.currentLength = 0;
      char last = ' ';
      int j = 0;
      int count = 0;

      int i;
      int len;
      for(i = 0; i < this.buffer.length; ++i) {
         char ch = (char)this.buffer[i];
         if (ch == '\n') {
            len = last == '\r' ? i - j - 1 : i - j;
            this.lines.add((Object)null);
            this.lineStarts.add(j);
            this.lineCounts.add(len);
            this.currentLength += len + 1;
            j = i + 1;
            ++count;
         }

         last = ch;
      }

      if (last != '\n') {
         len = i - j;
         this.lines.add((Object)null);
         this.lineStarts.add(j);
         this.lineCounts.add(len);
         this.currentLength += len;
      } else {
         this.lines.add("");
         this.lineStarts.add(-1);
         this.lineCounts.add(-1);
      }

   }

   private void insertString(int o, int line, int col, String text) {
      String temp = this.getLine(line);
      int l = text.length();
      int j = 0;
      int count = 0;
      text = temp.substring(0, col) + text + temp.substring(col) + '\n';
      this.removeLine(line);
      char last = ' ';

      for(int i = 0; i < text.length(); ++i) {
         char ch = text.charAt(i);
         if (ch == '\n') {
            String ss = text.substring(j, last == '\r' ? i - 1 : i);
            this.insertLine(line++, ss);
            j = i + 1;
            ++count;
         }

         last = ch;
      }

      this.currentLength += l;
      this.fireInsertUpdate(new SimpleDocumentEvent(EventType.INSERT, l, o, count, 0));
   }

   private void insertLine(int lineIndex, String line) {
      this.lines.add(lineIndex, line);
      this.lineStarts.add(lineIndex, -1);
      this.lineCounts.add(lineIndex, -1);
   }

   public int[] coord(int offset) {
      int s = this.lines.size();

      for(int i = 0; i < s; ++i) {
         int len = this.lineLength(i) + 1;
         if (offset < len) {
            return new int[]{i, offset};
         }

         offset -= len;
      }

      return null;
   }

   private int lineLength(int i) {
      Object obj = this.lines.get(i);
      return obj != null ? ((String)obj).length() : this.lineCounts.get(i);
   }

   public String getLine(int i) {
      if (i >= 0 && i < this.lines.size()) {
         Object obj = this.lines.get(i);
         return obj != null ? (String)obj : new String(this.buffer, this.lineStarts.get(i), this.lineCounts.get(i));
      } else {
         return null;
      }
   }

   private void removeLine(int lineIndex) {
      this.lines.remove(lineIndex);
      this.lineStarts.remove(lineIndex);
      this.lineCounts.remove(lineIndex);
   }

   public int offset(int lin, int col) {
      int result = 0;

      for(int i = 0; i < lin; ++i) {
         result += this.lineLength(i) + 1;
      }

      return result + col;
   }

   public void remove(int off, int length) throws BadLocationException {
      if (off >= 0 && off + length <= this.currentLength && length >= 0) {
         int[] c1 = this.coord(off);
         int[] c2 = this.coord(off + length);
         if (c1 != null && c2 != null) {
            int lin = c1[0];
            int col = c1[1];
            int lin2 = c2[0];
            int col2 = c2[1];
            if (lin != 0 || col >= 0) {
               if (lin != this.lines.size() - 1 || col < this.lineLength(this.lines.size() - 1)) {
                  String line = this.getLine(lin2);
                  String str = line.substring(col2);
                  this.lines.set(lin, this.getLine(lin).substring(0, col) + str);

                  for(int i = lin2; i > lin; --i) {
                     this.removeLine(i);
                  }

                  this.currentLength -= length;
                  this.fireRemoveUpdate(new SimpleDocumentEvent(EventType.REMOVE, length, off, 0, lin2 - lin));
               }
            }
         }
      } else {
         throw new BadLocationException("Bad arguments (" + off + ", " + length + ") to remove().", off);
      }
   }

   public void clearUndo() {
      int count = this.edits.size();

      for(int i = 0; i < count; ++i) {
         UndoableEdit ed = (UndoableEdit)this.edits.get(0);
         ed.die();
         this.edits.remove(0);
      }

      this.undoIndex = 0;
   }

   protected void registerUndo(UndoableEdit ed) {
      int i = this.edits.size() - this.undoIndex;

      for(int j = 0; j < i; ++j) {
         UndoableEdit p = (UndoableEdit)this.edits.get(this.undoIndex);
         p.die();
         this.edits.remove(p);
      }

      if (this.undoIndex == 0 || !((UndoableEdit)this.edits.get(this.undoIndex - 1)).addEdit(ed)) {
         this.edits.add(this.undoIndex, ed);
         ++this.undoIndex;
      }

      this.fireEditRegistered(ed);
   }

   public void insert(int pos, String text, JEditTextArea view, Object details) {
      if (!this.equals(view.getDocument())) {
         throw new IllegalArgumentException("'insert(UndoableEditSupportSyntaxDocument)': illegal view object.");
      } else {
         InsertionEdit ie = new InsertionEdit(view, pos, text, details.toString());
         this.addUndoableEdit(ie);
         ie.execute();
      }
   }

   public void remove(int pos, int len, JEditTextArea view, Object details) {
      if (!this.equals(view.getDocument())) {
         throw new IllegalArgumentException("'remove(SyntaxDocument.java)': illegal view object.");
      } else if (len != 0) {
         int start = pos;
         int caretPos = view.getCaretPosition();
         int ss = view.getSelectionStart();
         int se = view.getSelectionEnd();
         boolean selected = ss != se;
         if (len < 0) {
            start = pos + len;
         }

         int dir;
         String text;
         if (ss != se) {
            text = view.getSelectedText();
            dir = caretPos == ss ? 0 : 1;
         } else {
            try {
               text = this.getText(start, Math.abs(len));
            } catch (BadLocationException var13) {
               throw new IllegalArgumentException("'remove':position and length are invalid.");
            }

            dir = len > 0 ? 0 : 1;
         }

         DeleteEdit de = new DeleteEdit(view, selected, dir, caretPos, text, details.toString());
         this.addUndoableEdit(de);
         de.execute();
      }
   }

   public void replace(int pos, int len, String newText, JEditTextArea view) {
      if (!this.equals(view.getDocument())) {
         throw new IllegalArgumentException("'replace': invalid view object.");
      } else {
         int start;
         if (len < 0) {
            start = pos + len;
         } else {
            start = pos;
         }

         String text;
         try {
            text = this.getText(start, Math.abs(len));
         } catch (BadLocationException var8) {
            throw new IllegalArgumentException("'replace':position and length are invalid.");
         }

         ReplaceEdit re = new ReplaceEdit(view, start, pos, text, newText);
         this.addUndoableEdit(re);
         re.execute();
      }
   }

   public void replaceAll() {
      this.compound = new ReplaceAllEdit();
   }

   public void addUndoListener(UndoListener l) {
      this.listenerList.add(UndoListener.class, l);
   }

   public void removeUndoListener(UndoListener l) {
      this.listenerList.remove(UndoListener.class, l);
   }

   public void addDocumentListener(DocumentListener l) {
      this.listenerList.add(DocumentListener.class, l);
   }

   public void removeDocumentListener(DocumentListener l) {
      this.listenerList.remove(DocumentListener.class, l);
   }

   public void putProperty(Object key, Object prop) {
      this.properties.put(key, prop);
   }

   public Object getProperty(Object key) {
      return this.properties.get(key);
   }

   public String getText(int start, int length) throws BadLocationException {
      if (length + start <= this.getLength() && start >= 0) {
         StringBuffer bf = new StringBuffer();
         int line = this.getLineOfOffset(start);
         int x = this.offset(line, 0);
         int len = Math.min(length, this.lineLength(line) - (start - x));
         length -= len;
         start -= x;
         bf.append(this.getLine(line).substring(start, start + len));

         while(length > 0) {
            ++line;
            if (line < this.lines.size()) {
               bf.append('\n');
               --length;
               String str = this.getLine(line);
               len = Math.min(length, str.length());
               bf.append(str.substring(0, len));
               length -= len;
            }
         }

         return bf.toString();
      } else {
         throw new BadLocationException("Bad arguments (" + start + ", " + length + ") to getText().", start);
      }
   }

   public void getText(int start, int length, Segment seg) throws BadLocationException {
      if (length + start <= this.getLength() && start >= 0) {
         seg.array = new char[length];
         seg.offset = 0;
         seg.count = length;
         StringBuffer bf = new StringBuffer();
         int line = this.getLineOfOffset(start);
         int x = this.offset(line, 0);
         int len = Math.min(length, this.lineLength(line) - (start - x));
         length -= len;
         start -= x;
         bf.append(this.getLine(line).substring(start, start + len));

         while(length > 0) {
            ++line;
            if (line < this.lines.size()) {
               bf.append("\n");
               --length;
               String str = this.getLine(line);
               len = Math.min(length, str.length());
               bf.append(str.substring(0, len));
               length -= len;
            }
         }

         for(int i = 0; i < bf.length(); ++i) {
            seg.array[i] = bf.charAt(i);
         }

      } else {
         throw new BadLocationException("Bad arguments (" + start + ", " + length + ") to getText().", start);
      }
   }

   public int getLength() {
      return this.currentLength;
   }

   public void insertString(int start, String text, AttributeSet attr) throws BadLocationException {
      if (start >= 0 && start <= this.currentLength) {
         int[] coord = this.coord(start);
         if (coord != null) {
            this.insertString(start, coord[0], coord[1], text);
         }

      } else {
         throw new BadLocationException("Bad arguments (" + start + ") to insertString().", start);
      }
   }

   public int getLineCount() {
      return this.lines.size();
   }

   public int getLineOfOffset(int offset) {
      int[] c = this.coord(offset);
      return c[0];
   }

   public int getLineStartOffset(int line) {
      return line >= 0 && line < this.lines.size() ? this.offset(line, 0) : -1;
   }

   public int getLineEndOffset(int line) {
      if (line >= 0 && line < this.lines.size()) {
         String str = (String)this.lines.get(line);
         int len;
         if (str == null) {
            len = this.lineCounts.get(line);
         } else {
            len = str.length();
         }

         return len + this.offset(line, 0);
      } else {
         return -1;
      }
   }

   public int getLineLength(int line) {
      if (line >= 0 && line < this.lines.size()) {
         String ll = (String)this.lines.get(line);
         return ll == null ? this.lineCounts.get(line) : ll.length() + 1;
      } else {
         return -1;
      }
   }

   protected void updateLineStarts(int offset) {
      int line = 0;

      for(boolean var3 = false; line < this.lineStarts.size() && this.lineStarts.get(line) <= offset; ++line) {
      }

      for(int i = offset; i < this.buffer.length; ++i) {
         if (this.buffer[i] == 10) {
            this.lineStarts.add(line++, i + 1);
         }
      }

      this.lineStarts.setSize(line);
   }

   protected void fireEditRegistered(UndoableEdit ed) {
      UndoEvent evt = new UndoEvent(this, ed);
      EventListener[] ls = this.listenerList.getListeners(UndoListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((UndoListener)ls[i]).editRegistered(evt);
      }

   }

   protected void fireEditUndone(UndoableEdit ed) {
      UndoEvent evt = new UndoEvent(this, ed);
      EventListener[] ls = this.listenerList.getListeners(UndoListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((UndoListener)ls[i]).editUndone(evt);
      }

   }

   protected void fireEditRedone(UndoableEdit ed) {
      UndoEvent evt = new UndoEvent(this, ed);
      EventListener[] ls = this.listenerList.getListeners(UndoListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((UndoListener)ls[i]).editRedone(evt);
      }

   }

   protected void standardFireInsertUpdate(DocumentEvent evt) {
      EventListener[] ls = this.listenerList.getListeners(DocumentListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((DocumentListener)ls[i]).insertUpdate(evt);
      }

   }

   protected void standardFireRemoveUpdate(DocumentEvent evt) {
      EventListener[] ls = this.listenerList.getListeners(DocumentListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((DocumentListener)ls[i]).removeUpdate(evt);
      }

   }
}
