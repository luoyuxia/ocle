package ro.ubbcluj.lci.gui.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileFilter;
import ro.ubbcluj.lci.gui.editor.event.DocumentStateListener;
import ro.ubbcluj.lci.gui.editor.jedit.TokenMarker;
import ro.ubbcluj.lci.gui.editor.mdi.PadContainer;
import ro.ubbcluj.lci.gui.editor.utils.AFileFilter;

public class Editor {
   private final List activeFileFilters = new ArrayList();
   public static final int MODEL_SAVED = -104;
   public static final int MODEL_NOT_SAVED = -105;
   public static final int MODEL_NEEDS_NO_SAVE = -106;
   public static final int OPERATION_CANCELLED = -107;
   public static final int OPERATION_OK = -108;
   public static final int OPERATION_NOT_OK = -109;
   private static final String MARKER_SUFFIX = "SuffixMarker";
   private static final String SUPPORTS_MVC = "supportsMVC";
   private static final String IMAGE_KEY_SUFFIX = "SuffixImage";
   private static final String MARKER_CLASS_PATH = "ro.ubbcluj.lci.gui.editor.jedit";
   private PadContainer view;
   private TextDocumentModelFactory modelFactory = new TextDocumentModelFactory(this);
   ArrayList models = new ArrayList();
   private EventListenerList listeners = new EventListenerList();
   private HashMap tokenMarkers = new HashMap();
   private HashMap images = new HashMap();
   private boolean supportsMVC;

   public Editor() {
   }

   public final ImageIcon getImage(String fileNameSuffix) {
      ImageIcon image = (ImageIcon)this.images.get(fileNameSuffix + "SuffixImage");
      return image != null ? image : (ImageIcon)this.images.get("defaultSuffixImage");
   }

   public final TokenMarker getMarker(String fileNameSuffix) {
      Class tm = (Class)this.tokenMarkers.get(fileNameSuffix + "SuffixMarker");

      try {
         if (tm != null) {
            return (TokenMarker)tm.newInstance();
         } else {
            if (!"default".equals(fileNameSuffix)) {
               tm = (Class)this.tokenMarkers.get("defaultSuffixMarker");
               if (tm != null) {
                  return (TokenMarker)tm.newInstance();
               }
            }

            return null;
         }
      } catch (Exception var4) {
         return null;
      }
   }

   public final void addFileFilter(FileFilter fileFilter) {
      if (!this.activeFileFilters.contains(fileFilter)) {
         this.activeFileFilters.add(fileFilter);
      }

   }

   public final void removeFileFilter(FileFilter fileFilter) {
      this.activeFileFilters.remove(fileFilter);
   }

   public final boolean supportsMVC() {
      return this.supportsMVC;
   }

   public final PadContainer getView() {
      return this.view;
   }

   public final void setView(PadContainer v) {
      this.view = v;
   }

   public final TextDocumentModel getModel(File f) {
      Iterator it = this.models.iterator();
      String path = f.getAbsolutePath();

      TextDocumentModel model;
      do {
         if (!it.hasNext()) {
            return null;
         }

         model = (TextDocumentModel)it.next();
      } while(!model.isAssigned() || !model.getFileName().equals(path));

      return model;
   }

   public final AbstractPad getActivePad() {
      return this.view != null ? this.view.getActivePad() : null;
   }

   public final File getOpenFileName() {
      FileFilter[] filters = new FileFilter[this.activeFileFilters.size()];
      Iterator it = this.activeFileFilters.iterator();

      for(int i = 0; it.hasNext(); ++i) {
         filters[i] = (FileFilter)it.next();
      }

      return AFileFilter.chooseFile(0, this.view.getComponent(), filters);
   }

   public final File getSaveFileName() {
      FileFilter[] filters = new FileFilter[this.activeFileFilters.size()];
      Iterator it = this.activeFileFilters.iterator();

      for(int i = 0; it.hasNext(); ++i) {
         filters[i] = (FileFilter)it.next();
      }

      return AFileFilter.chooseFile(1, this.view.getComponent(), filters);
   }

   public void newFile() {
      TextDocumentModel padModel = this.modelFactory.newPadModel();
      TextDocumentPad pad = TextDocumentPadFactory.getFactory().newPad();
      this.models.add(padModel);
      this.processModel(padModel);
      padModel.addView(pad);
      this.view.activatePad(pad);
   }

   public File openFile(File f) {
      AbstractPad activePad = this.view.getActivePad();
      TextDocumentModel activeModel = null;
      boolean useActivePad;
      if (activePad == null) {
         useActivePad = false;
      } else if (activePad.getType() == 0) {
         activeModel = ((TextDocumentPad)activePad).getModel();
         useActivePad = !activeModel.isAssigned() && !activeModel.isDirty();
      } else {
         useActivePad = false;
      }

      TextDocumentModel possibleModel = this.getModel(f);
      if (possibleModel != null) {
         if (this.supportsMVC()) {
            TextDocumentPad newPad = useActivePad ? (TextDocumentPad)activePad : TextDocumentPadFactory.getFactory().newPad();
            possibleModel.addView(newPad);
            if (useActivePad) {
               this.models.remove(activeModel);
            }

            if (!useActivePad) {
               this.view.activatePad(newPad);
            }
         } else {
            this.view.activatePad(possibleModel.getViews()[0]);
         }
      } else {
         TextDocumentModel mdl = useActivePad ? activeModel : this.modelFactory.newUncountedPadModel();
         if (mdl.loadFromFile(f) == null) {
            return null;
         }

         if (!useActivePad) {
            this.models.add(mdl);
            this.processModel(mdl);
         }

         TextDocumentPad newPad = useActivePad ? (TextDocumentPad)activePad : TextDocumentPadFactory.getFactory().newPad();
         if (!f.canWrite()) {
            newPad.getView().setEditable(false);
         }

         if (!useActivePad) {
            mdl.addView(newPad);
         }

         if (!useActivePad) {
            this.view.activatePad(newPad);
         } else {
            this.view.contentsModified(new AbstractPad[]{newPad}, false);
         }
      }

      return f;
   }

   public List getFiles() {
      return (List)this.models.clone();
   }

   public String activateFileView(String f) {
      Iterator it = this.models.iterator();

      TextDocumentModel mdl;
      do {
         if (!it.hasNext()) {
            File op = this.openFile(new File(f));
            if (op == null) {
               return null;
            }

            return op.getAbsolutePath();
         }

         mdl = (TextDocumentModel)it.next();
      } while(!mdl.getFileName().equals(f));

      this.view.activatePad(mdl.getViews()[0]);
      return f;
   }

   public int saveAll() {
      Iterator it = this.models.iterator();
      byte ret2 = -108;

      while(it.hasNext()) {
         TextDocumentModel padModel = (TextDocumentModel)it.next();
         if (padModel.isDirty()) {
            int ret = padModel.save();
            if (ret == -107) {
               return -107;
            }

            if (ret == -105) {
               ret2 = -109;
            }
         }
      }

      return ret2;
   }

   public int saveActiveFile() {
      TextDocumentPad pad = (TextDocumentPad)this.view.getActivePad();
      return pad.getModel().save();
   }

   public int saveActiveFileAs(File f) {
      TextDocumentPad pad = (TextDocumentPad)this.view.getActivePad();
      return pad.getModel().saveToFile(f);
   }

   public void setModelFactory(TextDocumentModelFactory pmf) {
      this.modelFactory = pmf;
   }

   public void undo() {
      TextDocumentPad active = (TextDocumentPad)this.getActivePad();
      active.getModel().undo();
   }

   public void redo() {
      TextDocumentPad active = (TextDocumentPad)this.getActivePad();
      active.getModel().redo();
   }

   public void addDocumentStateListener(DocumentStateListener dsl) {
      this.listeners.add(DocumentStateListener.class, dsl);
   }

   public void configure(Properties p) {
      this.loadImages(p);
      String s = p.getProperty("supportsMVC", "false");
      this.supportsMVC = s.equalsIgnoreCase("true");
      Enumeration en = p.keys();

      while(en.hasMoreElements()) {
         String key = (String)en.nextElement();
         if (key.endsWith("SuffixMarker")) {
            String value = p.getProperty(key);
            if (!value.equals("null")) {
               String v = "ro.ubbcluj.lci.gui.editor.jedit." + value;

               try {
                  Class tm = Class.forName(v);
                  this.tokenMarkers.put(key, tm);
               } catch (Exception var9) {
                  System.err.println("TokenMarker class not found:" + v);
               }
            }
         }
      }

   }

   private void loadImages(Properties p) {
      Enumeration keys = p.keys();

      while(keys.hasMoreElements()) {
         String key = (String)keys.nextElement();
         if (key.endsWith("SuffixImage")) {
            try {
               ImageIcon icon = new ImageIcon(p.getProperty(key));
               this.images.put(key, icon);
            } catch (Exception var5) {
            }
         }
      }

   }

   private void processModel(TextDocumentModel mdl) {
      EventListener[] ls = this.listeners.getListeners(DocumentStateListener.class);

      for(int i = 0; i < ls.length; ++i) {
         mdl.addDocumentStateListener((DocumentStateListener)ls[i]);
      }

      if (!mdl.isAssigned()) {
         TokenMarker tm = this.getMarker("default");
         mdl.model.setTokenMarker(tm);
      }

   }
}
