package ro.ubbcluj.lci.gui.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EventListener;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import ro.ubbcluj.lci.gui.editor.event.DocumentStateEvent;
import ro.ubbcluj.lci.gui.editor.event.DocumentStateListener;
import ro.ubbcluj.lci.gui.editor.event.UndoEvent;
import ro.ubbcluj.lci.gui.editor.event.UndoListener;
import ro.ubbcluj.lci.gui.editor.utils.Utilities;

public class TextDocumentModel implements UndoListener {
   private static final String UNASSIGNED_MARKER = new String("Untitled");
   protected SyntaxDocument model;
   private int indexOfLastSave = 0;
   private int ordinal;
   private boolean dirty = false;
   private boolean assigned;
   private ArrayList views = new ArrayList();
   private File file;
   private boolean readOnly = false;
   private EventListenerList listeners = new EventListenerList();
   private Editor owner;

   TextDocumentModel(Editor owner, int ord) {
      this.owner = owner;
      this.ordinal = ord;
      this.model = new SyntaxDocument();
      this.model.addUndoListener(this);
   }

   public boolean isAssigned() {
      return this.assigned;
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public void addDocumentStateListener(DocumentStateListener l) {
      if (l != null) {
         this.listeners.add(DocumentStateListener.class, l);
      }

   }

   public void removeDocumentStateListener(DocumentStateListener l) {
      this.listeners.remove(DocumentStateListener.class, l);
   }

   public SyntaxDocument getModel() {
      return this.model;
   }

   public File getFile() {
      return this.file;
   }

   private void assignTo(File f) {
      this.assigned = f != null;
      this.file = f;
      this.readOnly = f != null && !f.canWrite();
      String name = this.getShortFileName();
      if (this.owner == null) {
         System.err.println("Owner editor not set");
      }

      String suffix;
      Icon ic = this.owner.getImage(suffix = Utilities.getFileNameSuffix(name));

      for(int i = 0; i < this.views.size(); ++i) {
         AbstractPad p = (AbstractPad)this.views.get(i);
         p.setNotifies(false);
         p.setTitle(name);
         p.setIcon(ic);
         if (f != null) {
            p.setDescription(this.getFileName());
         }

         p.setNotifies(true);
      }

      this.model.setTokenMarker(this.owner.getMarker(suffix));
   }

   public int save() {
      File f;
      if (!this.assigned) {
         f = this.owner.getSaveFileName();
         if (f == null) {
            return -107;
         }
      } else {
         if (!this.dirty) {
            return -106;
         }

         f = this.file;
      }

      return this.saveToFile(f);
   }

   public int saveToFile(File f) {
      int ret = -104;
      if (!this.dirty && f.equals(this.file)) {
         ret = -106;
      }

      boolean needOverwriteConfirmation = false;
      if (f.exists()) {
         if (!this.assigned) {
            needOverwriteConfirmation = true;
         }

         if (this.assigned && !f.getAbsolutePath().equals(this.file.getAbsolutePath())) {
            needOverwriteConfirmation = true;
         }
      }

      if (needOverwriteConfirmation) {
         int res = this.askOverwriteConfirmation(f);
         if (res == 2) {
            ret = -107;
         }

         if (res == 1) {
            ret = -105;
         }
      }

      if (ret != -106) {
         String result = this.writeToFile(f);
         if (!result.equals(f.getAbsolutePath())) {
            return -105;
         }

         TextDocumentModel possibleModel = this.owner.getModel(f);
         if (possibleModel != null && possibleModel != this) {
            this.copyFrom(possibleModel);
            this.owner.models.remove(possibleModel);
         }

         this.setDirty(false);
         this.indexOfLastSave = this.model.undoIndex;
      }

      this.assignTo(f);
      return ret;
   }

   public File loadFromFile(File f) {
      System.gc();

      try {
         FileInputStream fis = new FileInputStream(f);
         byte[] buf = new byte[fis.available()];
         fis.read(buf, 0, buf.length);
         fis.close();
         this.model.setText(buf);
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }

      this.assignTo(f);
      return f;
   }

   private String writeToFile(File f) {
      String fName = f.getAbsolutePath();

      try {
         PrintWriter pw = new PrintWriter(new FileWriter(f));
         int lc = this.model.getLineCount();

         for(int i = 0; i + 1 < lc; ++i) {
            pw.println(this.model.getLine(i));
         }

         if (lc > 0) {
            pw.print(this.model.getLine(lc - 1));
         }

         pw.close();
         return fName;
      } catch (Exception var6) {
         StringBuffer errMsg = (new StringBuffer("Error writing to file '")).append(fName).append("': ").append(var6.getMessage());
         return errMsg.toString();
      }
   }

   private void setDirty(boolean bDirty) {
      if (this.dirty != bDirty) {
         DocumentStateEvent evt = new DocumentStateEvent(this);
         this.dirty = bDirty;
         if (this.dirty) {
            this.fireDocumentModified(evt);
         } else {
            this.fireDocumentSaved(evt);
         }
      }

   }

   public boolean isDirty() {
      return this.dirty;
   }

   public String getFileName() {
      return this.assigned ? this.file.getAbsolutePath() : UNASSIGNED_MARKER + this.ordinal;
   }

   public String getShortFileName() {
      return this.assigned ? this.file.getName() : UNASSIGNED_MARKER + this.ordinal;
   }

   private void fireDocumentSaved(DocumentStateEvent evt) {
      EventListener[] ls = this.listeners.getListeners(DocumentStateListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((DocumentStateListener)ls[i]).documentSaved(evt);
      }

      AbstractPad[] pads = this.getViews();
      pads[0].getDescriptor().getOwner().contentsModified(pads, false);
   }

   private void fireDocumentModified(DocumentStateEvent evt) {
      EventListener[] ls = this.listeners.getListeners(DocumentStateListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((DocumentStateListener)ls[i]).documentModified(evt);
      }

      AbstractPad[] pads = this.getViews();
      pads[0].getDescriptor().getOwner().contentsModified(pads, true);
   }

   private void fireDocumentStateChanged() {
      EventListener[] ls = this.listeners.getListeners(DocumentStateListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((DocumentStateListener)ls[i]).documentStateChanged(this);
      }

   }

   public int askSaveConfirmation() {
      return JOptionPane.showConfirmDialog(this.owner.getView().getComponent(), "Save file '" + this.getFileName() + "'?", "Confirmation", 1);
   }

   private int askOverwriteConfirmation(File f) {
      return JOptionPane.showConfirmDialog(this.owner.getView().getComponent(), "Overwrite file '" + f.getAbsolutePath() + "' ?", "Confirmation", 1);
   }

   public AbstractPad[] getViews() {
      int l = this.views.size();
      AbstractPad[] result = new AbstractPad[l];

      for(int i = 0; i < l; ++i) {
         result[i] = (AbstractPad)this.views.get(i);
      }

      return result;
   }

   public void addView(TextDocumentPad v) {
      this.views.add(v);
      v.setModel(this);
      v.setNotifies(false);
      String name = this.getShortFileName();
      Icon ic = this.owner.getImage(Utilities.getFileNameSuffix(name));
      v.setIcon(ic);
      v.setTitle(name);
      if (this.assigned) {
         v.setDescription(this.file.getAbsolutePath());
      }

      v.setNotifies(true);
   }

   public void removeView(TextDocumentPad view) {
      this.views.remove(view);
      if (this.views.size() <= 0) {
         this.owner.models.remove(this);
      }

   }

   private void copyFrom(TextDocumentModel source) {
      AbstractPad[] pads = source.getViews();

      for(int i = 0; i < pads.length; ++i) {
         this.addView((TextDocumentPad)pads[i]);
      }

   }

   public void undo() {
      try {
         this.model.undo();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void redo() {
      try {
         this.model.redo();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void editRegistered(UndoEvent evt) {
      if (this.indexOfLastSave >= this.model.undoIndex) {
         this.indexOfLastSave = -1;
      }

      this.setDirty(true);
      this.fireDocumentStateChanged();
   }

   public void editUndone(UndoEvent evt) {
      this.setDirty(this.indexOfLastSave != this.model.undoIndex);
      this.fireDocumentStateChanged();
   }

   public void editRedone(UndoEvent evt) {
      this.setDirty(this.indexOfLastSave != this.model.undoIndex);
      this.fireDocumentStateChanged();
   }
}
