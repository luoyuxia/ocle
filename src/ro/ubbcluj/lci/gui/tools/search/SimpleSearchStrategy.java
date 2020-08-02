package ro.ubbcluj.lci.gui.tools.search;

import ro.ubbcluj.lci.gui.editor.jedit.OCLTokenMarker;
import ro.ubbcluj.lci.utils.search.SearchStrategy;

class SimpleSearchStrategy extends SearchStrategy {
   private boolean matchCase;
   private boolean matchWholeWord;

   SimpleSearchStrategy() {
   }

   public int getSubstringPos(String source, String textToFind, int currentPos, int searchDirection) {
      String newSource = source;
      String newTextToFind = textToFind;
      if (source.equals("")) {
         return -1;
      } else if (currentPos > source.length()) {
         return -1;
      } else {
         if (!this.matchCase) {
            newSource = source.toLowerCase();
            newTextToFind = textToFind.toLowerCase();
         }

         int ret;
         if (this.matchWholeWord) {
            int pos;
            switch(searchDirection) {
            case 1:
               for(pos = newSource.indexOf(newTextToFind, currentPos); pos != -1 && (pos > 0 && !OCLTokenMarker.isDelimiter(newSource.charAt(pos - 1)) || pos + newTextToFind.length() < newSource.length() && !OCLTokenMarker.isDelimiter(newSource.charAt(pos + newTextToFind.length()))); pos = newSource.indexOf(newTextToFind, pos + 1)) {
               }

               ret = pos;
               break;
            default:
               for(pos = newSource.substring(0, currentPos).lastIndexOf(newTextToFind); pos != -1 && (pos > 0 && !OCLTokenMarker.isDelimiter(newSource.charAt(pos - 1)) || pos + newTextToFind.length() < newSource.length() && !OCLTokenMarker.isDelimiter(newSource.charAt(pos + newTextToFind.length()))); pos = newSource.substring(0, pos).lastIndexOf(newTextToFind)) {
               }

               ret = pos;
            }
         } else {
            switch(searchDirection) {
            case 1:
               ret = newSource.indexOf(newTextToFind, currentPos);
               break;
            default:
               ret = newSource.substring(0, currentPos).lastIndexOf(newTextToFind);
            }
         }

         return ret;
      }
   }

   public void matchCase(boolean matchC) {
      this.matchCase = matchC;
   }

   public boolean matchCase() {
      return this.matchCase;
   }

   public boolean matchWholeWords() {
      return this.matchWholeWord;
   }

   public void matchWholeWords(boolean mww) {
      this.matchWholeWord = mww;
   }
}
