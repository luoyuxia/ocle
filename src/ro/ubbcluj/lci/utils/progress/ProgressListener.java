package ro.ubbcluj.lci.utils.progress;

import java.util.EventListener;

public interface ProgressListener extends EventListener {
   void start(String var1);

   void progressValueChanged(int var1, Object var2);

   void stop();
}
