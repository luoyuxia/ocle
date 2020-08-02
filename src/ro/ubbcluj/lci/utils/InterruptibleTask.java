package ro.ubbcluj.lci.utils;

public abstract class InterruptibleTask extends Thread {
   protected boolean isCancelled = false;
   private boolean finished;
   private Object sync_object = new Object();

   public InterruptibleTask() {
   }

   public void start() {
      this.isCancelled = false;
      this.finished = false;
      super.start();
   }

   public boolean isCancelled() {
      synchronized(this.sync_object) {
         return this.isCancelled;
      }
   }

   public boolean isFinished() {
      synchronized(this.sync_object) {
         return this.finished;
      }
   }

   public void cancel() {
      synchronized(this.sync_object) {
         this.isCancelled = true;
      }
   }

   public void run() {
      this.realRun();
      synchronized(this) {
         this.notifyAll();
      }

      this.finished = true;
   }

   public abstract void realRun();

   public void interrupt() {
   }
}
