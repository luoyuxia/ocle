package ro.ubbcluj.lci.gui.editor.jedit;

import javax.swing.text.Segment;

public class XMLTokenMarker extends TokenMarker {
   public XMLTokenMarker() {
   }

   public byte markTokensImpl(byte token, Segment line, int lineIndex) {
      char[] array = line.array;
      int offset = line.offset;
      int lastOffset = offset;
      int length = line.count + offset;
      boolean sk1 = token == 6;

      for(int i = offset; i < length; ++i) {
         int ip1 = i + 1;
         char c = array[i];
         switch(token) {
         case 0:
            switch(c) {
            case '&':
               this.addToken(i - lastOffset, token);
               lastOffset = i;
               token = 5;
               continue;
            case '<':
               this.addToken(i - lastOffset, token);
               lastOffset = i;
               if (SyntaxUtilities.regionMatches(false, line, ip1, "!--")) {
                  i += 3;
                  token = 1;
               } else if (array[ip1] == '!') {
                  ++i;
                  token = 2;
               } else if (array[ip1] == '?') {
                  ++i;
                  token = 8;
               } else {
                  token = 6;
               }
            default:
               continue;
            }
         case 1:
            if (SyntaxUtilities.regionMatches(false, line, i, "-->")) {
               this.addToken(i + 3 - lastOffset, token);
               lastOffset = i + 3;
               token = 0;
            }
            break;
         case 2:
            if (SyntaxUtilities.regionMatches(false, line, i, ">")) {
               this.addToken(ip1 - lastOffset, token);
               lastOffset = ip1;
               token = 0;
            }
            break;
         case 3:
         case 4:
            if (token == 3 && c == '"' || token == 4 && c == '\'') {
               this.addToken(ip1 - lastOffset, token);
               lastOffset = ip1;
               token = 6;
            }
            break;
         case 5:
            if (c == ';') {
               this.addToken(ip1 - lastOffset, token);
               lastOffset = ip1;
               token = 0;
            }
            break;
         case 6:
            switch(c) {
            case '\t':
            case ' ':
               this.addToken(i - lastOffset, token);
               lastOffset = i;
               token = 7;
               sk1 = false;
               continue;
            case '>':
               this.addToken(ip1 - lastOffset, token);
               lastOffset = ip1;
               token = 0;
               sk1 = false;
               continue;
            default:
               if (sk1) {
                  token = 7;
                  sk1 = false;
               }
               continue;
            }
         case 7:
            switch(c) {
            case '/':
               this.addToken(i - lastOffset, token);
               lastOffset = i;
               token = 6;
               continue;
            case '=':
               this.addToken(i - lastOffset, token);
               lastOffset = i;
               token = 9;
               continue;
            case '>':
               this.addToken(ip1 - lastOffset, token);
               lastOffset = ip1;
               token = 0;
            default:
               continue;
            }
         case 8:
            if (SyntaxUtilities.regionMatches(false, line, i, "?>")) {
               this.addToken(i + 2 - lastOffset, token);
               lastOffset = i + 2;
               token = 0;
            }
            break;
         case 9:
            switch(c) {
            case '"':
            case '\'':
               this.addToken(i - lastOffset, token);
               lastOffset = i;
               if (c == '"') {
                  token = 3;
               } else {
                  token = 4;
               }
            default:
               continue;
            }
         default:
            throw new InternalError("Invalid state: " + token);
         }
      }

      switch(token) {
      case 5:
         this.addToken(length - lastOffset, (byte)10);
         token = 0;
         break;
      default:
         this.addToken(length - lastOffset, token);
      }

      return token;
   }
}
