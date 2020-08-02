package ro.ubbcluj.lci.gui.editor.jedit;

import javax.swing.text.Segment;

public class HTMLTokenMarker extends TokenMarker {
   public static final byte JAVASCRIPT = 100;
   private KeywordMap keywords;
   private boolean js;
   private int lastOffset;
   private int lastKeyword;

   public HTMLTokenMarker() {
      this(true);
   }

   public HTMLTokenMarker(boolean js) {
      this.js = js;
      this.keywords = JavaScriptTokenMarker.getKeywords();
   }

   public byte markTokensImpl(byte token, Segment line, int lineIndex) {
      char[] array = line.array;
      int offset = line.offset;
      this.lastOffset = offset;
      this.lastKeyword = offset;
      int length = line.count + offset;
      boolean backslash = false;

      label103:
      for(int i = offset; i < length; ++i) {
         int i1 = i + 1;
         char c = array[i];
         if (c == '\\') {
            backslash = !backslash;
         } else {
            switch(token) {
            case 0:
               backslash = false;
               switch(c) {
               case '&':
                  this.addToken(i - this.lastOffset, token);
                  this.lastOffset = this.lastKeyword = i;
                  token = 7;
                  continue;
               case '<':
                  this.addToken(i - this.lastOffset, token);
                  this.lastOffset = this.lastKeyword = i;
                  if (SyntaxUtilities.regionMatches(false, line, i1, "!--")) {
                     i += 3;
                     token = 1;
                  } else if (this.js && SyntaxUtilities.regionMatches(true, line, i1, "script>")) {
                     this.addToken(8, (byte)6);
                     i += 8;
                     this.lastOffset = this.lastKeyword = i;
                     token = 100;
                  } else {
                     token = 6;
                  }
               default:
                  continue;
               }
            case 1:
               backslash = false;
               if (SyntaxUtilities.regionMatches(false, line, i, "-->")) {
                  this.addToken(i + 3 - this.lastOffset, token);
                  this.lastOffset = this.lastKeyword = i + 3;
                  token = 0;
               }
               break;
            case 2:
               backslash = false;
               if (c == '*' && length - i > 1 && array[i1] == '/') {
                  i += 2;
                  this.addToken(i - this.lastOffset, (byte)2);
                  this.lastOffset = this.lastKeyword = i;
                  token = 100;
               }
               break;
            case 3:
               if (backslash) {
                  backslash = false;
               } else if (c == '"') {
                  this.addToken(i1 - this.lastOffset, (byte)3);
                  this.lastOffset = this.lastKeyword = i1;
                  token = 100;
               }
               break;
            case 4:
               if (backslash) {
                  backslash = false;
               } else if (c == '\'') {
                  this.addToken(i1 - this.lastOffset, (byte)3);
                  this.lastOffset = this.lastKeyword = i1;
                  token = 100;
               }
               break;
            case 6:
               backslash = false;
               if (c == '>') {
                  this.addToken(i1 - this.lastOffset, token);
                  this.lastOffset = this.lastKeyword = i1;
                  token = 0;
               }
               break;
            case 7:
               backslash = false;
               if (c == ';') {
                  this.addToken(i1 - this.lastOffset, token);
                  this.lastOffset = this.lastKeyword = i1;
                  token = 0;
               }
               break;
            case 100:
               switch(c) {
               case '"':
                  if (backslash) {
                     backslash = false;
                  } else {
                     this.doKeyword(line, i, c);
                     this.addToken(i - this.lastOffset, (byte)0);
                     this.lastOffset = this.lastKeyword = i;
                     token = 3;
                  }
                  continue;
               case '\'':
                  if (backslash) {
                     backslash = false;
                  } else {
                     this.doKeyword(line, i, c);
                     this.addToken(i - this.lastOffset, (byte)0);
                     this.lastOffset = this.lastKeyword = i;
                     token = 4;
                  }
                  continue;
               case '/':
                  backslash = false;
                  this.doKeyword(line, i, c);
                  if (length - i > 1) {
                     this.addToken(i - this.lastOffset, (byte)0);
                     this.lastOffset = this.lastKeyword = i;
                     if (array[i1] == '/') {
                        this.addToken(length - i, (byte)2);
                        this.lastOffset = this.lastKeyword = length;
                        break label103;
                     }

                     if (array[i1] == '*') {
                        token = 2;
                     }
                  }
                  continue;
               case '<':
                  backslash = false;
                  this.doKeyword(line, i, c);
                  if (SyntaxUtilities.regionMatches(true, line, i1, "/script>")) {
                     this.addToken(i - this.lastOffset, (byte)0);
                     this.addToken(9, (byte)6);
                     i += 9;
                     this.lastOffset = this.lastKeyword = i;
                     token = 0;
                  }
                  continue;
               default:
                  backslash = false;
                  if (!Character.isLetterOrDigit(c) && c != '_') {
                     this.doKeyword(line, i, c);
                  }
                  continue;
               }
            default:
               throw new InternalError("Invalid state: " + token);
            }
         }
      }

      switch(token) {
      case 3:
      case 4:
         this.addToken(length - this.lastOffset, (byte)10);
         token = 100;
         break;
      case 7:
         this.addToken(length - this.lastOffset, (byte)10);
         token = 0;
         break;
      case 100:
         this.doKeyword(line, length, '\u0000');
         this.addToken(length - this.lastOffset, (byte)0);
         break;
      default:
         this.addToken(length - this.lastOffset, token);
      }

      return token;
   }

   private boolean doKeyword(Segment line, int i, char c) {
      int i1 = i + 1;
      int len = i - this.lastKeyword;
      byte id = this.keywords.lookup(line, this.lastKeyword, len);
      if (id != 0) {
         if (this.lastKeyword != this.lastOffset) {
            this.addToken(this.lastKeyword - this.lastOffset, (byte)0);
         }

         this.addToken(len, id);
         this.lastOffset = i;
      }

      this.lastKeyword = i1;
      return false;
   }
}
