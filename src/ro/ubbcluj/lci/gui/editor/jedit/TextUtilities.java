package ro.ubbcluj.lci.gui.editor.jedit;

import javax.swing.text.BadLocationException;
import ro.ubbcluj.lci.gui.editor.SyntaxDocument;

public class TextUtilities {
   public TextUtilities() {
   }

   public static int findMatchingBracket(SyntaxDocument doc, int offset) throws BadLocationException {
      if (doc.getLength() == 0) {
         return -1;
      } else {
         char c = doc.getText(offset, 1).charAt(0);
         byte cprime;
         boolean var4;
         switch(c) {
         case '(':
            cprime = 41;
            var4 = false;
            break;
         case ')':
            cprime = 40;
            var4 = true;
            break;
         case '[':
            cprime = 93;
            var4 = false;
            break;
         case ']':
            cprime = 91;
            var4 = true;
            break;
         case '{':
            cprime = 125;
            var4 = false;
            break;
         case '}':
            cprime = 123;
            var4 = true;
            break;
         default:
            return -1;
         }

         int count;
         if (var4) {
            count = 1;
            String text = doc.getText(0, offset);

            for(int i = offset - 1; i >= 0; --i) {
               char x = text.charAt(i);
               if (x == c) {
                  ++count;
               } else if (x == cprime) {
                  --count;
                  if (count == 0) {
                     return i;
                  }
               }
            }
         } else {
            count = 1;
            ++offset;
            int len = doc.getLength() - offset;
            String text = doc.getText(offset, len);

            for(int i = 0; i < len; ++i) {
               char x = text.charAt(i);
               if (x == c) {
                  ++count;
               } else if (x == cprime) {
                  --count;
                  if (count == 0) {
                     return i + offset;
                  }
               }
            }
         }

         return -1;
      }
   }

   public static int findWordStart(String line, int pos, String noWordSep) {
      char ch = line.charAt(pos - 1);
      if (noWordSep == null) {
         noWordSep = "";
      }

      boolean selectNoLetter = !Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1;
      int wordStart = 0;

      for(int i = pos - 1; i >= 0; --i) {
         ch = line.charAt(i);
         if (selectNoLetter ^ (!Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1)) {
            wordStart = i + 1;
            break;
         }
      }

      return wordStart;
   }

   public static int findWordEnd(String line, int pos, String noWordSep) {
      char ch = line.charAt(pos);
      if (noWordSep == null) {
         noWordSep = "";
      }

      boolean selectNoLetter = !Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1;
      int wordEnd = line.length();

      for(int i = pos; i < line.length(); ++i) {
         ch = line.charAt(i);
         if (selectNoLetter ^ (!Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1)) {
            wordEnd = i;
            break;
         }
      }

      return wordEnd;
   }
}
