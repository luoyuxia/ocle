package ro.ubbcluj.lci.utils.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.text.BadLocationException;
import ro.ubbcluj.lci.gui.editor.SyntaxDocument;

public class TextFinder {
   public static final int SD_FORWARD = 1;
   public static final int SD_BACKWARD = 2;
   private static final int ONE_MB = 1048576;
   private static SearchStrategy strategy = null;
   private static boolean stopped = false;

   public TextFinder() {
   }

   public static final void setSearchStrategy(SearchStrategy ss) {
      strategy = ss;
   }

   public static final SearchStrategy getSearchStrategy() {
      return strategy;
   }

   public static synchronized void setStopped(boolean s) {
      stopped = s;
   }

   public static synchronized boolean isStopped() {
      return stopped;
   }

   public static final int findInBuffer(SyntaxDocument buffer, int start, int direction, String text) {
      int chunkLength;
      String chunk;
      int max;
      int so;
      int relPos;
      if (direction == 1) {
         max = buffer.getLength();
         int currentChunkStart = start;

         do {
            chunkLength = Math.min(1048576, max - currentChunkStart);
            so = buffer.getLineOfOffset(chunkLength + currentChunkStart);
            int eo = buffer.getLineEndOffset(so);
            chunkLength = Math.min(max, eo) - currentChunkStart;

            try {
               chunk = buffer.getText(currentChunkStart, chunkLength);
            } catch (BadLocationException var13) {
               chunk = "";
            }

            relPos = strategy.getSubstringPos(chunk, text, 0, 1);
            if (relPos >= 0) {
               break;
            }

            chunk = null;
            currentChunkStart += chunkLength;
         } while(currentChunkStart < max);

         return relPos >= 0 ? relPos + currentChunkStart : -1;
      } else {
         int currentChunkEnd = start;

         do {
            chunkLength = Math.min(1048576, currentChunkEnd);
            max = buffer.getLineOfOffset(currentChunkEnd - chunkLength);
            so = buffer.getLineStartOffset(max);
            chunkLength = currentChunkEnd - so;

            try {
               chunk = buffer.getText(currentChunkEnd - chunkLength, chunkLength);
            } catch (BadLocationException var14) {
               chunk = "";
            }

            relPos = strategy.getSubstringPos(chunk, text, chunkLength, 2);
            if (relPos >= 0) {
               break;
            }

            chunk = null;
            currentChunkEnd -= chunkLength;
         } while(currentChunkEnd > 0);

         return relPos >= 0 ? relPos + currentChunkEnd - chunkLength : -1;
      }
   }

   public static final void findInBuffer(SyntaxDocument buffer, int start, int direction, String text, List results) {
      int i = start;
      int len = buffer.getLength();
      int tlen = text.length();
      stopped = false;
      int pos;
      SearchResult sr;
      if (direction == 1) {
         while(i < len && !isStopped()) {
            pos = findInBuffer(buffer, i, 1, text);
            if (pos < 0) {
               break;
            }

            sr = new SearchResult(getLine(buffer, pos, len), pos, pos + tlen);
            results.add(sr);
            i = pos + 1;
         }
      } else {
         while(i >= 0 && !isStopped()) {
            pos = findInBuffer(buffer, i, 2, text);
            if (pos < 0) {
               break;
            }

            sr = new SearchResult(getLine(buffer, pos, len), pos, pos + tlen);
            results.add(sr);
            i = pos + tlen - 1;
         }
      }

   }

   public static final void findInFile(File file, String textToFind, List results) {
      String text = null;
      stopped = false;
      results.clear();

      try {
         FileInputStream fis = new FileInputStream(file);
         byte[] buffer = new byte[fis.available()];
         fis.read(buffer);
         text = (new String(buffer)).replaceAll("\r", "");
         buffer = null;
         System.gc();
         fis.close();
      } catch (IOException var8) {
         var8.printStackTrace();
      }

      int i = 0;

      int pos;
      for(int len = textToFind.length(); i < text.length(); i = pos + 1) {
         pos = strategy.getSubstringPos(text, textToFind, i, 1);
         if (pos < 0) {
            break;
         }

         results.add(new SearchResult(getLine(text, pos), pos, pos + len));
      }

   }

   private static final String getLine(String text, int offset) {
      int end = offset;

      int start;
      char c;
      for(start = offset; start >= 0; --start) {
         c = text.charAt(start);
         if (c == '\n') {
            break;
         }
      }

      while(end < text.length()) {
         c = text.charAt(end);
         if (c == '\n') {
            break;
         }

         ++end;
      }

      return text.substring(start + 1, end);
   }

   private static final String getLine(SyntaxDocument doc, int offset, int docLen) {
      try {
         int start;
         char c;
         for(start = offset; start >= 0; --start) {
            c = doc.getText(start, 1).charAt(0);
            if (c == '\n') {
               break;
            }
         }

         int length;
         for(length = offset - start; start + length < docLen; ++length) {
            c = doc.getText(start + length, 1).charAt(0);
            if (c == '\n') {
               break;
            }
         }

         return doc.getText(start + 1, length - 1);
      } catch (BadLocationException var6) {
         var6.printStackTrace();
         return "";
      }
   }
}
