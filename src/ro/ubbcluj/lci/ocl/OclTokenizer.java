package ro.ubbcluj.lci.ocl;

import java.util.HashSet;

public class OclTokenizer {
   private static final HashSet reserved = new HashSet();
   private static final char[][] operators2;
   private char[] data;
   private int k;
   private char ch;
   private char nextch;
   private int ofs = 0;
   private int row = 1;
   private int col = 0;
   private String filename;

   public OclTokenizer(char[] _data, String filename) throws ExceptionTokenizer {
      this.filename = filename;
      this.data = _data;
      if (this.data.length >= 2) {
         this.ch = this.data[0];
         this.nextch = this.data[1];
         this.k = 2;
      } else if (this.data.length == 1) {
         this.ch = this.data[0];
         this.nextch = 0;
         this.k = this.data.length;
      } else {
         this.ch = 0;
         this.nextch = 0;
         this.k = 0;
      }

   }

   public OclToken getToken(boolean coloring) throws ExceptionTokenizer {
      OclToken blank = this.skipBlanksAndComments(coloring);
      if (blank != null && coloring) {
         return blank;
      } else if (this.ch <= 0) {
         return null;
      } else if (Character.isDigit(this.ch)) {
         return this.readNumber(coloring);
      } else if (this.ch != '\'' && this.ch != '"' && this.ch != '`') {
         return !Character.isLetter(this.ch) && this.ch != '_' ? this.readOther() : this.readIdentifier();
      } else {
         return this.readString(coloring);
      }
   }

   private void readChar(ExceptionTokenizer exc) throws ExceptionTokenizer {
      this.ch = this.nextch;
      if (this.k < this.data.length) {
         this.nextch = this.data[this.k++];
      } else {
         this.nextch = 0;
      }

      if (this.ch <= 0 && exc != null) {
         throw exc;
      } else {
         ++this.ofs;
         ++this.col;
         if (this.ch == '\n') {
            ++this.row;
            this.col = 0;
         }

      }
   }

   private OclToken readIdentifier() throws ExceptionTokenizer {
      int _ofs = this.ofs;
      int _row = this.row;
      int _col = this.col;
      StringBuffer s = new StringBuffer("");

      do {
         do {
            s.append(this.ch);
            this.readChar((ExceptionTokenizer)null);
         } while(Character.isLetterOrDigit(this.ch));
      } while(this.ch == '_');

      boolean ind = reserved.contains(s.toString());
      return new OclToken(ind ? 0 : 4, s.toString(), _ofs, this.ofs - 1, _row, _col, this.filename);
   }

   private OclToken readString(boolean coloring) throws ExceptionTokenizer {
      int _ofs = this.ofs;
      int _row = this.row;
      int _col = this.col;
      StringBuffer s = new StringBuffer();
      char marker = this.ch;
      boolean escapeErrors = false;

      do {
         if (this.ch == '\\') {
            if (coloring) {
               s.append("\\");
            }

            this.readChar(coloring ? null : new ExceptionTokenizer(0, _ofs, this.data.length - 1, _row, _col, this.filename));
            if (this.ch == 0) {
               continue;
            }

            if (coloring) {
               s.append(this.ch);
            }

            char tch;
            label125:
            switch(this.ch) {
            case '"':
               tch = '"';
               break;
            case '\'':
               tch = '\'';
               break;
            case '\\':
               tch = '\\';
               break;
            case '`':
               tch = '`';
               break;
            case 'b':
               tch = '\b';
               break;
            case 'f':
               tch = '\f';
               break;
            case 'n':
               tch = '\n';
               break;
            case 'r':
               tch = '\r';
               break;
            case 't':
               tch = '\t';
               break;
            case 'u':
               int bofs = this.ofs - 1;
               tch = 0;
               int i = 0;

               while(true) {
                  if (i >= 4) {
                     break label125;
                  }

                  this.readChar(coloring ? null : new ExceptionTokenizer(0, _ofs, this.data.length - 1, _row, _col, this.filename));
                  if (coloring) {
                     s.append(this.ch);
                  }

                  if (this.ch >= '0' && this.ch <= '9') {
                     tch = (char)(tch * 16 + this.ch - 48);
                  } else if (this.ch >= 'a' && this.ch <= 'f') {
                     tch = (char)(tch * 16 + this.ch - 97 + 10);
                  } else {
                     if (this.ch < 'A' || this.ch > 'F') {
                        if (!coloring) {
                           throw new ExceptionTokenizer("unicode escape sequence expects four hexadecimal chars", bofs, this.ofs, _row, _col, this.filename);
                        }

                        escapeErrors = true;
                        break label125;
                     }

                     tch = (char)(tch * 16 + this.ch - 65 + 10);
                  }

                  ++i;
               }
            default:
               if (this.ch >= '0' && this.ch <= '7') {
                  tch = (char)(this.ch - 48);
                  this.readChar(coloring ? null : new ExceptionTokenizer(0, _ofs, this.data.length - 1, _row, _col, this.filename));
                  if (this.ch < '0' || this.ch > '7') {
                     continue;
                  }

                  tch = (char)(tch * 8 + this.ch - 48);
                  if (coloring) {
                     s.append(this.ch);
                  }

                  this.readChar(coloring ? null : new ExceptionTokenizer(0, _ofs, this.data.length - 1, _row, _col, this.filename));
                  if (this.ch < '0' || this.ch > '7') {
                     continue;
                  }

                  tch = (char)(tch * 8 + this.ch - 48);
                  if (coloring) {
                     s.append(this.ch);
                  }
               } else {
                  if (!coloring) {
                     throw new ExceptionTokenizer("invalid escape sequence", this.ofs - 1, this.ofs, _row, _col, this.filename);
                  }

                  escapeErrors = true;
                  tch = this.ch;
               }
            }

            if (!coloring) {
               s.append(tch);
            }
         } else {
            s.append(this.ch);
         }

         this.readChar(coloring ? null : new ExceptionTokenizer(0, _ofs, this.data.length - 1, _row, _col, this.filename));
      } while(this.ch != marker && this.ch != 0 && this.ch != '\n' && this.ch != '\r');

      if (this.ch == marker && !escapeErrors) {
         s.append(this.ch);
         this.readChar((ExceptionTokenizer)null);
         return new OclToken(1, s.toString(), _ofs, this.ofs - 1, _row, _col, this.filename);
      } else if (!coloring) {
         throw new ExceptionTokenizer(0, _ofs, this.ofs - 1, _row, _col, this.filename);
      } else {
         return new OclToken(7, s.toString(), _ofs, this.ofs - 1, _row, _col, this.filename);
      }
   }

   private String readInteger() throws ExceptionTokenizer {
      StringBuffer s = new StringBuffer();

      do {
         s.append(this.ch);
         this.readChar((ExceptionTokenizer)null);
      } while(Character.isDigit(this.ch));

      return s.toString();
   }

   private OclToken readNumber(boolean coloring) throws ExceptionTokenizer {
      boolean isFloat = false;
      int _ofs = this.ofs;
      int _row = this.row;
      int _col = this.col;
      StringBuffer s = new StringBuffer(this.readInteger());
      if (this.ch == '.' && Character.isDigit(this.nextch)) {
         s.append(this.readInteger());
         isFloat = true;
      }

      try {
         if (this.ch == 'e' || this.ch == 'E') {
            s.append(this.ch);
            this.readChar(new ExceptionTokenizer(1, _ofs, this.ofs, _row, _col, this.filename));
            if (this.ch == '+' || this.ch == '-') {
               s.append(this.ch);
               this.readChar(new ExceptionTokenizer(1, _ofs, this.ofs, _row, _col, this.filename));
            }

            if (!Character.isDigit(this.ch)) {
               throw new ExceptionTokenizer(1, _ofs, this.ofs, _row, _col, this.filename);
            }

            s.append(this.readInteger());
            isFloat = true;
         }

         if (Character.isLetter(this.ch) || this.ch == '_') {
            throw new ExceptionTokenizer(1, _ofs, this.ofs, _row, _col, this.filename);
         }
      } catch (ExceptionTokenizer var8) {
         if (!coloring) {
            throw var8;
         }

         while(this.ch != 0 && Character.isJavaIdentifierPart(this.ch)) {
            s.append(this.ch);
            this.readChar((ExceptionTokenizer)null);
         }

         return new OclToken(7, s.toString(), _ofs, this.ofs - 1, _row, _col, this.filename);
      }

      return new OclToken(isFloat ? 5 : 2, s.toString(), _ofs, this.ofs - 1, _row, _col, this.filename);
   }

   private OclToken readOther() throws ExceptionTokenizer {
      int _ofs = this.ofs;
      int _row = this.row;
      int _col = this.col;
      OclToken tok = null;

      for(int i = 0; i < operators2.length; ++i) {
         if (this.ch == operators2[i][0] && this.nextch == operators2[i][1]) {
            tok = new OclToken(3, "" + this.ch + this.nextch, _ofs, this.ofs + 1, _row, _col, this.filename);
            this.readChar((ExceptionTokenizer)null);
            this.readChar((ExceptionTokenizer)null);
         }
      }

      if (tok == null) {
         tok = new OclToken(3, "" + this.ch, _ofs, this.ofs, _row, _col, this.filename);
         this.readChar((ExceptionTokenizer)null);
      }

      return tok;
   }

   private OclToken skipBlanksAndComments(boolean coloring) throws ExceptionTokenizer {
      int _ofs = this.ofs;
      int _row = this.row;
      int _col = this.col;
      boolean haveComment = false;
      StringBuffer comment = new StringBuffer();

      boolean ind;
      do {
         for(ind = false; this.ch > 0 && Character.isWhitespace(this.ch); ind = true) {
            comment.append(this.ch);
            this.readChar((ExceptionTokenizer)null);
         }

         if (this.ch == '/' && this.nextch == '*') {
            int __ofs = this.ofs;
            ind = true;
            haveComment = true;

            while((this.ch != '*' || this.nextch != '/') && this.ch > 0) {
               comment.append(this.ch);
               if (coloring) {
                  this.readChar((ExceptionTokenizer)null);
               } else {
                  this.readChar(new ExceptionTokenizer(3, __ofs, this.ofs, this.row, this.col, this.filename));
               }
            }

            if (this.ch > 0) {
               comment.append(this.ch);
               comment.append(this.nextch);
            }

            if (this.ch > 0) {
               this.readChar((ExceptionTokenizer)null);
            }

            if (this.ch > 0) {
               this.readChar((ExceptionTokenizer)null);
            }
         }

         if (this.ch == '-' && this.nextch == '-' || this.ch == '/' && this.nextch == '/') {
            ind = true;
            haveComment = true;

            while(this.ch != '\n' && this.ch != '\r' && this.ch > 0) {
               comment.append(this.ch);
               this.readChar((ExceptionTokenizer)null);
            }
         }
      } while(ind);

      if (haveComment) {
         return new OclToken(6, comment.toString(), _ofs, this.ofs - 1, _row, _col, this.filename);
      } else {
         return null;
      }
   }

   static {
      String[] sreserved = new String[]{"if", "then", "else", "endif", "not", "let", "or", "and", "xor", "implies", "endpackage", "package", "context", "def", "inv", "pre", "post", "in", "Set", "Bag", "Sequence", "Collection", "model", "endmodel", "Tuple", "TupleType", "div", "mod", "OrderedSet", "option", "body"};

      for(int i = 0; i < sreserved.length; ++i) {
         reserved.add(sreserved[i]);
      }

      operators2 = new char[][]{{'<', '='}, {'>', '='}, {'<', '>'}, {':', ':'}, {'-', '>'}, {'.', '.'}, {'^', '^'}};
   }
}
