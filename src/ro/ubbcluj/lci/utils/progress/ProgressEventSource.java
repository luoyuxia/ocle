package ro.ubbcluj.lci.utils.progress;

public interface ProgressEventSource {
   void progressValueChanged(int var1, Object var2);

   void addProgressListener(ProgressListener var1);

   void removeProgressListener(ProgressListener var1);
}
