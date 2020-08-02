package ro.ubbcluj.lci.gui.editor.jedit;

import javax.swing.text.Segment;

public class OCLTokenMarker extends TokenMarker {
   public static char[] seps = new char[]{' ', '\t', '\n', '\r'};
   public static char[] opers = new char[]{'+', '-', '*', '/', '<', '>', '=', ',', '.', '(', ')', '{', '}', '[', ']', ':', '|', ';'};
   public static char[] otherChars = new char[]{'~', '$', '%', '^', '&', '_'};
   private static KeywordMap cKeywords;
   private KeywordMap keywords;
   private int lastOffset;
   private int lastKeyword;

   public OCLTokenMarker() {
      this(getKeywords());
   }

   public OCLTokenMarker(KeywordMap keywords) {
      this.keywords = keywords;
   }

   public static KeywordMap getKeywords() {
      if (cKeywords == null) {
         cKeywords = new KeywordMap(false);
         cKeywords.add("context", (byte)6);
         cKeywords.add("pre", (byte)6);
         cKeywords.add("post", (byte)6);
         cKeywords.add("inv", (byte)6);
         cKeywords.add("package", (byte)6);
         cKeywords.add("endpackage", (byte)6);
         cKeywords.add("let", (byte)6);
         cKeywords.add("in", (byte)6);
         cKeywords.add("if", (byte)6);
         cKeywords.add("then", (byte)6);
         cKeywords.add("else", (byte)6);
         cKeywords.add("endif", (byte)6);
         cKeywords.add("and", (byte)6);
         cKeywords.add("or", (byte)6);
         cKeywords.add("not", (byte)6);
         cKeywords.add("xor", (byte)6);
         cKeywords.add("def", (byte)6);
         cKeywords.add("implies", (byte)6);
         cKeywords.add("self", (byte)6);
         cKeywords.add("result", (byte)6);
         cKeywords.add("@pre", (byte)6);
         cKeywords.add("model", (byte)6);
         cKeywords.add("endmodel", (byte)6);
      }

      return cKeywords;
   }

   public byte markTokensImpl(byte token, Segment line, int lineIndex) {
      char[] array = line.array;
      int offset = line.offset;
      this.lastOffset = offset;
      this.lastKeyword = offset;
      int lineEnd = line.count + offset;

      label168:
      for(int i = offset; i < lineEnd; ++i) {
         int i1 = i + 1;
         char c = array[i];
         switch(token) {
         case 0:
            switch(c) {
            case '#':
               this.doKeyword(line, i, c);
               this.addToken(i - this.lastOffset, token);
               token = 4;
               this.lastOffset = this.lastKeyword = i;
               break;
            case '\'':
               this.doKeyword(line, i, c);
               this.addToken(i - this.lastOffset, token);
               token = 3;
               this.lastOffset = this.lastKeyword = i;
               break;
            case '-':
               this.doKeyword(line, i, c);
               if (lineEnd - i > 1 && array[i1] == '-') {
                  this.addToken(i - this.lastOffset, token);
                  this.addToken(lineEnd - i, (byte)1);
                  this.lastOffset = this.lastKeyword = lineEnd;
                  break label168;
               }
               break;
            case '/':
               this.doKeyword(line, i, c);
               if (lineEnd - i > 1) {
                  if (array[i1] == '/') {
                     this.addToken(i - this.lastOffset, token);
                     this.addToken(lineEnd - i, (byte)1);
                     this.lastOffset = this.lastKeyword = lineEnd;
                     break label168;
                  }

                  if (array[i1] == '*') {
                     this.addToken(i - this.lastOffset, token);
                     this.lastOffset = this.lastKeyword = i;
                     token = 2;
                  }
               }
               break;
            case '@':
               if (lineEnd - i > 4 && array[i1] == 'p' && array[i + 2] == 'r' && array[i + 3] == 'e' && (isOperator(array[i + 4]) || isSeparator(array[i + 4])) || lineEnd - i == 4 && array[i1] == 'p' && array[i + 2] == 'r' && array[i + 3] == 'e') {
                  this.doKeyword(line, i, c);
               } else {
                  this.doKeyword(line, i, c);
                  this.addToken(i - this.lastOffset, token);
                  token = 10;
                  this.lastOffset = this.lastKeyword = i;
               }
               break;
            default:
               if (!Character.isLetterOrDigit(c) && !isOtherChar(c)) {
                  this.doKeyword(line, i, c);
               } else if (Character.isDigit(c) && (i == offset || i > offset && (isSeparator(array[i - 1]) || isOperator(array[i - 1])))) {
                  int old_i = i;
                  i = this.number(line, i);
                  if (old_i == i && i != lineEnd - 1 && (i >= lineEnd - 1 || !isSeparator(array[i1]) && !isOperator(array[i1]) && array[i1] != '\'')) {
                     this.doKeyword(line, i, c);
                     this.addToken(i - this.lastOffset, token);
                     token = 10;
                     this.lastOffset = this.lastKeyword = i;
                  } else {
                     this.doKeyword(line, old_i, c);
                     this.addToken(old_i - this.lastOffset, token);
                     this.addToken(i - old_i + 1, (byte)3);
                     token = 0;
                     this.lastOffset = this.lastKeyword = i + 1;
                  }
               }
            }
         case 1:
            break;
         case 2:
            if (c == '*' && lineEnd - i > 1 && array[i1] == '/') {
               ++i;
               this.addToken(i + 1 - this.lastOffset, token);
               token = 0;
               this.lastOffset = this.lastKeyword = i + 1;
            }
            break;
         case 3:
            if (c == '\'') {
               this.addToken(i1 - this.lastOffset, token);
               token = 0;
               this.lastOffset = this.lastKeyword = i1;
            }
            break;
         case 4:
            if (isOperator(c) || isSeparator(c)) {
               boolean cond = true;
               if (i > 0) {
                  cond = array[i - 1] == '#' || isSeparator(array[i - 1]);
                  cond &= isSeparator(c);
               }

               if (!cond) {
                  this.addToken(i - this.lastOffset, token);
                  token = 0;
                  this.lastOffset = i;
                  this.lastKeyword = i1;
               }
            }

            if (c == '-' && lineEnd - i > 1 && array[i1] == '-') {
               token = 1;
            }
            break;
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         default:
            throw new InternalError("Invalid state: " + token);
         case 10:
            if (isSeparator(c) || isOperator(c)) {
               this.addToken(i - this.lastOffset, token);
               token = 0;
               this.lastOffset = this.lastKeyword = i;
            }
         }
      }

      if (token == 0) {
         this.doKeyword(line, lineEnd, '\u0000');
      }

      switch(token) {
      case 3:
         this.addToken(lineEnd - this.lastOffset, (byte)10);
         token = 0;
         break;
      default:
         this.addToken(lineEnd - this.lastOffset, token);
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

   private int number(Segment line, int i) {
      int j = i + 1;
      int length = line.count + line.offset;

      char[] array;
      for(array = line.array; j < length && !isSeparator(array[j]) && (!isOperator(array[j]) || array[j] == '-' || array[j] == '+' || array[j] == '.'); ++j) {
      }

      while(j > i + 1 && !isReal(new String(array, i, j - i))) {
         --j;
      }

      if (j < length) {
         return !isOperator(array[j]) && !isSeparator(array[j]) ? i : j - 1;
      } else {
         return j - 1;
      }
   }

   public static boolean isReal(String s) {
      Character ch = new Character(s.charAt(0));

      int count_plus;
      for(count_plus = 0; count_plus < s.length(); ++count_plus) {
         if (s.charAt(count_plus) != '+' && s.charAt(count_plus) != '-' && s.charAt(count_plus) != 'e' && s.charAt(count_plus) != 'E' && !Character.isDigit(s.charAt(count_plus)) && s.charAt(count_plus) != '.') {
            return false;
         }
      }

      if (s.charAt(s.length() - 1) == '.') {
         return false;
      } else {
         count_plus = 0;
         int count_e = 0;
         int count_point = 0;

         for(int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) != 'e' && s.charAt(i) != 'E') {
               if (s.charAt(i) != '+' && s.charAt(i) != '-') {
                  if (s.charAt(i) == '.') {
                     ++count_point;
                  }
               } else {
                  ++count_plus;
               }
            } else {
               ++count_e;
            }
         }

         if (count_plus <= 2 && count_e <= 1) {
            if (count_plus == 2 && s.charAt(0) != '+' && s.charAt(0) != '-') {
               return false;
            } else if (count_e == 0 && count_point > 1) {
               return false;
            } else if (count_point > 2) {
               return false;
            } else if (!s.startsWith("e") && !s.endsWith("e") && !s.startsWith("E") && !s.endsWith("E")) {
               if (!s.endsWith("+") && !s.endsWith("-")) {
                  if (s.indexOf(101) != -1 && s.indexOf(101) < s.lastIndexOf(46)) {
                     return false;
                  } else if (s.indexOf(69) != -1 && s.indexOf(69) < s.lastIndexOf(46)) {
                     return false;
                  } else if (s.indexOf(43) != -1 && s.indexOf(101) == -1 && s.indexOf(69) == -1) {
                     return false;
                  } else if (s.indexOf(45) != -1 && s.indexOf(101) == -1 && s.indexOf(69) == -1) {
                     return false;
                  } else if (s.indexOf(43) != -1 && s.indexOf(101) != -1 && s.indexOf(101) != s.indexOf(43) - 1) {
                     return false;
                  } else if (s.indexOf(43) != -1 && s.indexOf(69) != -1 && s.indexOf(69) != s.indexOf(43) - 1) {
                     return false;
                  } else if (s.indexOf(45) != -1 && s.indexOf(101) != -1 && s.indexOf(101) != s.indexOf(45) - 1) {
                     return false;
                  } else if (s.indexOf(45) != -1 && s.indexOf(69) != -1 && s.indexOf(69) != s.indexOf(45) - 1) {
                     return false;
                  } else if (s.indexOf(46) != -1 && s.indexOf(101) != -1 && s.indexOf(101) == s.indexOf(46) + 1) {
                     return false;
                  } else if (s.indexOf(46) != -1 && s.indexOf(69) != -1 && s.indexOf(69) == s.indexOf(46) + 1) {
                     return false;
                  } else {
                     return true;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public static boolean isOperator(char ss) {
      int k;
      for(k = 0; k < opers.length && ss != opers[k]; ++k) {
      }

      return k < opers.length;
   }

   public static boolean isSeparator(char ss) {
      int j;
      for(j = 0; j < seps.length && ss != seps[j]; ++j) {
      }

      return j < seps.length;
   }

   public static boolean isOtherChar(char ss) {
      int j;
      for(j = 0; j < otherChars.length && ss != otherChars[j]; ++j) {
      }

      return j < otherChars.length;
   }

   public static String getDelimiters() {
      String op = new String(opers);
      String sep = new String(seps);
      return op.concat(sep);
   }

   public static boolean isDelimiter(char c) {
      String aux = getDelimiters();
      return aux.indexOf(c) != -1;
   }
}
