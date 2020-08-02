package ro.ubbcluj.lci.gui.mainframe;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.EventListenerList;
import ro.ubbcluj.lci.gui.editor.AbstractPad;
import ro.ubbcluj.lci.gui.editor.event.ViewActivationEvent;
import ro.ubbcluj.lci.gui.editor.event.ViewActivationListener;

public abstract class PadContainer implements PropertyChangeListener {
   public static final String UNSAVED_MARKER = new String("*");
   protected EventListenerList listeners = new EventListenerList();
   protected AbstractPad activePad = null;
   protected AbstractPad oldActivePad = null;
   protected JPopupMenu viewActionsMenu;

   public PadContainer() {
   }

   public abstract void activatePad(AbstractPad var1);

   public abstract JComponent getComponent();

   public AbstractPad getActivePad() {
      return this.activePad;
   }

   public abstract Collection getPads();

   public abstract int removePadView(AbstractPad var1);

   public int closeAll() {
      Collection p = this.getPads();
      Iterator it = p.iterator();
      AbstractPad[] pads = new AbstractPad[p.size()];

      for(int i = 0; it.hasNext(); ++i) {
         pads[i] = (AbstractPad)it.next();
      }

      return this.closeAll(pads);
   }

   public abstract int closeAll(AbstractPad[] var1);

   public int getPadCount() {
      return this.getPads().size();
   }

   public void setViewPopup(JPopupMenu menu) {
      this.viewActionsMenu = menu;
   }

   public void addViewActivationListener(ViewActivationListener l) {
      this.listeners.add(ViewActivationListener.class, l);
   }

   public void removeViewActivationListener(ViewActivationListener l) {
      this.listeners.remove(ViewActivationListener.class, l);
   }

   public abstract void contentsModified(AbstractPad[] var1, boolean var2);

   protected void fireViewActivated() {
      ViewActivationEvent evt = new ViewActivationEvent(this, this.activePad, this.oldActivePad);
      EventListener[] ls = this.listeners.getListeners(ViewActivationListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((ViewActivationListener)ls[i]).viewActivated(evt);
      }

   }

   void fireViewClosed(AbstractPad view) {
      EventListener[] ls = this.listeners.getListeners(ViewActivationListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((ViewActivationListener)ls[i]).viewClosed(view);
      }

   }
}
