package ro.ubbcluj.lci.ocl;

public class OclToken {
   public static final int RESERVED = 0;
   public static final int STRING = 1;
   public static final int INTEGER = 2;
   public static final int OTHER = 3;
   public static final int IDENTIFIER = 4;
   public static final int REAL = 5;
   public static final int BLANK = 6;
   public static final int ERROR = 7;
   private int typ;
   private String val;
   private int ofs;
   private int ofs2;
   private int row;
   private int col;
   private String filename;
   private int constant;

   public static String getTypeAsString(int typ) {
      switch(typ) {
      case 0:
         return "RESERVED";
      case 1:
         return "STRING";
      case 2:
         return "INTEGER";
      case 3:
         return "OTHER";
      case 4:
         return "IDENTIFIER";
      case 5:
         return "REAL";
      default:
         return "UNKNOWN";
      }
   }

   public OclToken(OclToken arg) {
      this.typ = arg.typ;
      this.val = arg.val;
      this.ofs = arg.ofs;
      this.ofs2 = arg.ofs2;
      this.row = arg.row;
      this.col = arg.col;
      this.filename = arg.filename;
      this.constant = arg.constant;
   }

   public OclToken(int _typ, String _val, int _ofs, int _ofs2, int _row, int _col, String filename) {
      this.ofs = _ofs;
      this.row = _row;
      this.col = _col;
      this.ofs2 = _ofs2;
      this.typ = _typ;
      this.val = _val;
      this.filename = filename;
      this.constant = -1;
      if (this.typ == 0 || this.typ == 3) {
         Integer cst = (Integer)OclParser.tokenmap.get(this.val);
         if (cst != null) {
            this.constant = cst.intValue();
         }
      }

   }

   public int getType() {
      return this.typ;
   }

   public int getConstant() {
      return this.constant;
   }

   public String getValue() {
      return this.val;
   }

   public int getStart() {
      return this.ofs;
   }

   public int getStop() {
      return this.ofs2;
   }

   public int getRow() {
      return this.row;
   }

   public int getColumn() {
      return this.col;
   }

   public String getFilename() {
      return this.filename;
   }

   public String toString() {
      return this.val;
   }
}
