package ro.ubbcluj.lci.xmi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import ro.ubbcluj.lci.utils.progress.ProgressListener;

public class ProgressFileInputStream extends FileInputStream {
   private long size;
   private long pos;
   private String filename;
   private ProgressListener progressListener;

   public ProgressFileInputStream(String filename, ProgressListener pl) throws IOException {
      super(filename);
      this.progressListener = pl;
      if (this.progressListener != null) {
         this.progressListener.start((new File(filename)).getName());
      }

      this.size = (long)this.available();
   }

   public int read() throws IOException {
      int result = super.read();
      if (result >= 0) {
         this.advance(1);
      }

      return result;
   }

   public int read(byte[] buf) throws IOException {
      int result = super.read(buf);
      if (result >= 0) {
         this.advance(result);
      }

      return result;
   }

   public int read(byte[] buf, int ofs, int len) throws IOException {
      int result = super.read(buf, ofs, len);
      if (result >= 0) {
         this.advance(result);
      }

      return result;
   }

   public long skip(long n) throws IOException {
      long result = super.skip(n);
      if (result >= 0L) {
         this.advance((int)result);
      }

      return result;
   }

   public boolean markSupported() {
      return false;
   }

   private void advance(int n) {
      this.pos += (long)n;
      long percent = this.pos * 100L / this.size;
      if (this.progressListener != null) {
         this.progressListener.progressValueChanged((int)percent, (Object)null);
      }

      if (this.pos == this.size && this.progressListener != null) {
         this.progressListener.stop();
      }

   }
}
