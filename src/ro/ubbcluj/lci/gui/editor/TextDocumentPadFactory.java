package ro.ubbcluj.lci.gui.editor;

import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.event.CaretListener;
import javax.swing.event.EventListenerList;
import ro.ubbcluj.lci.gui.editor.jedit.InputHandler;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.editor.jedit.SyntaxStyle;
import ro.ubbcluj.lci.gui.editor.jedit.TextAreaPainter;
import ro.ubbcluj.lci.gui.editor.options.EditorProperties;

public class TextDocumentPadFactory {
   private EventListenerList listeners = new EventListenerList();
   private ArrayList actions = new ArrayList();
   private ArrayList shortcuts = new ArrayList();
   private ArrayList pads;
   private JEditTextArea sample;
   private int tabSize;
   private SyntaxStyle[] styles;
   private Font font;
   private JPopupMenu popup;
   private Comparator comp = new Comparator() {
      public boolean equals(Object o) {
         return o == this;
      }

      public int compare(Object o1, Object o2) {
         return ((String)o1).compareToIgnoreCase((String)o2);
      }
   };
   private static TextDocumentPadFactory instance = null;

   private TextDocumentPadFactory() {
      this.sample = new JEditTextArea(EditorProperties.appProps);
      this.font = this.sample.getPainter().getFont();
      this.styles = this.sample.getPainter().getStyles();
      this.tabSize = 4;
      this.pads = new ArrayList();
   }

   public static TextDocumentPadFactory getFactory() {
      if (instance == null) {
         instance = new TextDocumentPadFactory();
      }

      return instance;
   }

   public void setFont(Font f) {
      this.font = f;
   }

   public void setStyles(SyntaxStyle[] st) {
      this.styles = st;
   }

   public void setSample(JEditTextArea s) {
      this.sample = s;
   }

   void removePad(TextDocumentPad padToRemove) {
      this.pads.remove(padToRemove);
   }

   public void addKeyListener(KeyListener kl) {
      this.listeners.add(KeyListener.class, kl);

      for(int i = 0; i < this.pads.size(); ++i) {
         ((TextDocumentPad)this.pads.get(i)).getView().addKeyListener(kl);
      }

   }

   public void removeKeyListener(KeyListener kl) {
      this.listeners.remove(KeyListener.class, kl);

      for(int i = 0; i < this.pads.size(); ++i) {
         ((TextDocumentPad)this.pads.get(i)).getView().removeKeyListener(kl);
      }

   }

   public void addCaretListener(CaretListener cl) {
      this.listeners.add(CaretListener.class, cl);

      for(int i = 0; i < this.pads.size(); ++i) {
         ((TextDocumentPad)this.pads.get(i)).getView().addCaretListener(cl);
      }

   }

   public void removeCaretListener(CaretListener cl) {
      this.listeners.remove(CaretListener.class, cl);

      for(int i = 0; i < this.pads.size(); ++i) {
         ((TextDocumentPad)this.pads.get(i)).getView().removeCaretListener(cl);
      }

   }

   public void addMouseListener(MouseListener ml) {
      this.listeners.add(MouseListener.class, ml);

      for(int i = 0; i < this.pads.size(); ++i) {
         ((TextDocumentPad)this.pads.get(i)).getView().addMouseListener(ml);
      }

   }

   public void removeMouseListener(MouseListener ml) {
      this.listeners.remove(MouseListener.class, ml);

      for(int i = 0; i < this.pads.size(); ++i) {
         ((TextDocumentPad)this.pads.get(i)).getView().removeMouseListener(ml);
      }

   }

   public void setPopup(JPopupMenu mnu) {
      this.popup = mnu;

      for(int i = 0; i < this.pads.size(); ++i) {
         ((TextDocumentPad)this.pads.get(i)).getView().setRightClickPopup(this.popup);
      }

   }

   public void addAction(String shortcut, Action ac) {
      int ind = Collections.binarySearch(this.shortcuts, shortcut, this.comp);
      if (ind >= 0) {
         this.actions.set(ind, ac);
      } else {
         this.shortcuts.add(-(ind + 1), shortcut);
         this.actions.add(-(ind + 1), ac);
      }

      for(int i = 0; i < this.pads.size(); ++i) {
         JEditTextArea ta = ((TextDocumentPad)this.pads.get(i)).getView();
         ta.getInputHandler().addKeyBinding(shortcut, ac);
      }

   }

   public void removeAction(String shortcut) {
      int index = Collections.binarySearch(this.shortcuts, shortcut, this.comp);
      if (index >= 0) {
         for(int i = 0; i < this.pads.size(); ++i) {
            JEditTextArea ta = ((TextDocumentPad)this.pads.get(i)).getView();
            ta.getInputHandler().removeKeyBinding(shortcut);
            this.actions.remove(index);
            this.shortcuts.remove(index);
         }

      }
   }

   public TextDocumentPad newPad() {
      TextDocumentPad pad = new TextDocumentPad(this);
      JEditTextArea ta = pad.getView();
      InputHandler iph = ta.getInputHandler();
      EventListener[] ls = this.listeners.getListeners(KeyListener.class);

      int i;
      for(i = 0; i < ls.length; ++i) {
         ta.addKeyListener((KeyListener)ls[i]);
      }

      ls = this.listeners.getListeners(MouseListener.class);

      for(i = 0; i < ls.length; ++i) {
         ta.getPainter().addMouseListener((MouseListener)ls[i]);
      }

      ls = this.listeners.getListeners(CaretListener.class);

      for(i = 0; i < ls.length; ++i) {
         ta.addCaretListener((CaretListener)ls[i]);
      }

      for(i = 0; i < this.shortcuts.size(); ++i) {
         iph.addKeyBinding((String)this.shortcuts.get(i), (Action)this.actions.get(i));
      }

      pad.getView().setRightClickPopup(this.popup);
      this.pads.add(pad);
      this.setAttributesFor(pad);
      return pad;
   }

   public void reloadProperties() {
      for(int i = 0; i < this.pads.size(); ++i) {
         this.setAttributesFor((TextDocumentPad)this.pads.get(i));
      }

   }

   private void setAttributesFor(TextDocumentPad pad) {
      JEditTextArea ta = pad.getView();
      TextAreaPainter p = ta.getPainter();
      TextAreaPainter pp = this.sample.getPainter();
      ta.getDocument().putProperty("tabSize", new Integer(this.tabSize));
      p.setStyles(this.styles);
      p.setBracketHighlightColor(pp.getBracketHighlightColor());
      p.setCaretColor(pp.getCaretColor());
      p.setEOLMarkerColor(pp.getEOLMarkerColor());
      p.setLineHighlightColor(pp.getLineHighlightColor());
      p.setSelectionColor(pp.getSelectionColor());
      p.setBracketHighlightEnabled(pp.isBracketHighlightEnabled());
      p.setEOLMarkersPainted(pp.getEOLMarkersPainted());
      p.setLineHighlightEnabled(pp.isLineHighlightEnabled());
      ta.setCaretBlinkEnabled(this.sample.isCaretBlinkEnabled());
      p.setFont(this.font);
   }
}
