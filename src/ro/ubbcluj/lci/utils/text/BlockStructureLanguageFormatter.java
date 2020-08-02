package ro.ubbcluj.lci.utils.text;

import java.util.HashSet;
import ro.ubbcluj.lci.utils.CharArray;

public class BlockStructureLanguageFormatter implements TextFormatter {
   private int currentIndent = 0;
   private int currentIndex;
   private int indentTabSize = 4;
   private boolean separatorZone;
   private boolean convertTabsToSpaces = true;
   private boolean decreaseIndent;
   private int currentTokenStart;
   private int currentTokenEnd;
   private int currentLineStart;
   private int indentDelta;
   private CharArray separators = new CharArray();
   private HashSet blockDelimiters = new HashSet();
   private HashSet blockEndDelimiters = new HashSet();
   private StringBuffer source;
   private StringBuffer tab;

   public BlockStructureLanguageFormatter() {
   }

   public void format(StringBuffer source) {
      this.reset();
      this.source = source;

      while(true) {
         while(this.currentIndex < source.length()) {
            char c = source.charAt(this.currentIndex);
            if (this.separators.contains(c)) {
               int start = this.currentIndex;
               if (!this.separatorZone) {
                  this.separatorZone = true;
                  this.processCurrentToken(this.currentTokenStart, this.currentTokenEnd);
               }

               this.processCurrentToken(start, start + 1);
               if (this.currentIndex == start) {
                  ++this.currentIndex;
               }
            } else {
               if (this.separatorZone || this.currentIndex == 0) {
                  this.separatorZone = false;
                  this.currentTokenStart = this.currentIndex;
               }

               this.currentTokenEnd = ++this.currentIndex;
            }
         }

         return;
      }
   }

   public void addBlockDelimiter(String blockDelimiter) {
      this.blockDelimiters.add(blockDelimiter);
   }

   public void addBlockEndDelimiter(String beDelimiter) {
      this.blockEndDelimiters.add(beDelimiter);
   }

   public void addSeparator(char c) {
      if (!this.separators.contains(c)) {
         this.separators.add(c);
      }

   }

   public void addSeparators(String seps) {
      for(int i = 0; i < seps.length(); ++i) {
         this.addSeparator(seps.charAt(i));
      }

   }

   public void setConvertTabsToSpaces(boolean bConvert) {
      this.convertTabsToSpaces = bConvert;
   }

   public void setIndentTabSize(int nTabSize) {
      this.indentTabSize = nTabSize;
      this.computeTabBuffer();
   }

   private void processCurrentToken(int ttStart, int ttEnd) {
      String token = this.source.substring(ttStart, ttEnd);
      if ("\n".equals(token)) {
         if (this.decreaseIndent) {
            if (this.currentIndent > 0) {
               this.indent(this.currentIndent - 1);
            }
         } else {
            this.indent(this.currentIndent);
         }

         this.currentIndent += this.indentDelta;
         if (this.currentIndent < 0) {
            this.currentIndent = 0;
         }

         int i;
         for(i = this.currentIndex; i < this.source.length() && this.source.charAt(i) == '\n'; ++i) {
         }

         this.currentLineStart = this.currentIndex = i;
         this.indentDelta = 0;
         this.decreaseIndent = false;
      } else if (this.blockDelimiters.contains(token)) {
         ++this.indentDelta;
      } else if (this.blockEndDelimiters.contains(token)) {
         --this.indentDelta;
         this.decreaseIndent = true;
      }

   }

   private void reset() {
      this.currentIndent = 0;
      this.currentIndex = 0;
      this.separatorZone = false;
      this.currentTokenStart = this.currentTokenEnd = -1;
      this.currentLineStart = 0;
      this.indentDelta = 0;
      this.decreaseIndent = false;
      this.computeTabBuffer();
   }

   private void computeTabBuffer() {
      this.tab = new StringBuffer();

      for(int i = 0; i < this.indentTabSize; ++i) {
         this.tab.append(' ');
      }

   }

   private void indent(int currentIndent) {
      if (currentIndent > 0) {
         StringBuffer bfIndent = new StringBuffer();
         int i;
         if (this.convertTabsToSpaces) {
            for(i = 0; i < currentIndent; ++i) {
               bfIndent.append(this.tab);
            }
         } else {
            for(i = 0; i < currentIndent; ++i) {
               bfIndent.append('\t');
            }
         }

         this.currentIndex += bfIndent.length();
         this.source.insert(this.currentLineStart, bfIndent.toString());
      }

   }
}
