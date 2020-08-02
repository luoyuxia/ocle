package ro.ubbcluj.lci.errors;

import java.io.File;
import ro.ubbcluj.lci.gui.tools.Error;

public class CompilationErrorMessage extends TextLocalizableErrorMessage implements Error {
   public CompilationErrorMessage(String stream, int start, int stop, int column, int row, String msg) {
      super(stream);
      this.start = start;
      this.stop = stop;
      int i = stream.lastIndexOf(File.separatorChar);
      String tmp = i < 0 ? stream : stream.substring(i + 1);
      this.description = "(" + tmp + ')' + msg + " at line " + row + ", column " + column;
   }

   public void accept(ErrorVisitor v) {
      v.visitCompilationError(this);
   }
}
