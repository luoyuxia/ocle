package ro.ubbcluj.lci.ocl;

public class ExceptionTokenizer extends ExceptionCompiler {
   public static final int STRING = 0;
   public static final int NUMBER = 1;
   public static final int BADCHAR = 2;
   public static final int BADCOMMENT = 3;
   private static String[] msgs = new String[]{"unfinished string constant", "error in numeric constant", "unexpected character", "unfinished comment"};
   private int msgtype;
   private String msg;
   private int ofs;
   private int row;
   private int col;
   private int ofs2;
   private String filename;

   ExceptionTokenizer(String _msg, int _ofs, int _ofs2, int _row, int _col, String filename) {
      this.msg = _msg;
      this.ofs = _ofs;
      this.row = _row;
      this.col = _col;
      this.ofs2 = _ofs2;
      this.filename = filename;
   }

   ExceptionTokenizer(int _msg, int _ofs, int _ofs2, int _row, int _col, String filename) {
      this.msgtype = _msg;
      this.msg = msgs[_msg];
      this.ofs = _ofs;
      this.row = _row;
      this.col = _col;
      this.ofs2 = _ofs2;
      this.filename = filename;
   }

   public String getFilename() {
      return this.filename;
   }

   public String getMessage() {
      return this.msg;
   }

   public int getStart() {
      return this.ofs;
   }

   public int getRow() {
      return this.row;
   }

   public int getColumn() {
      return this.col;
   }

   public int getStop() {
      return this.ofs2;
   }
}
