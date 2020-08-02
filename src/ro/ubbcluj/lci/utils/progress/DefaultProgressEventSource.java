package ro.ubbcluj.lci.utils.progress;

import javax.swing.event.EventListenerList;

public class DefaultProgressEventSource implements ProgressEventSource {
   private EventListenerList listeners = new EventListenerList();

   public DefaultProgressEventSource() {
   }

   public void addProgressListener(ProgressListener l) {
      this.listeners.add(ProgressListener.class, l);
   }

   public void removeProgressListener(ProgressListener l) {
      this.listeners.remove(ProgressListener.class, l);
   }

   public void progressValueChanged(int newValue, Object details) {
      synchronized(this.listeners) {
         Object[] ls = this.listeners.getListeners(ProgressListener.class);

         for(int i = 0; i < ls.length; ++i) {
            ((ProgressListener)ls[i]).progressValueChanged(newValue, details);
         }

      }
   }
}
