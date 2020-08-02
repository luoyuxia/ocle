package ro.ubbcluj.lci.utils.search;

public class SearchResult {
   private int start;
   private int end;
   private String line;

   public SearchResult(String line, int start, int end) {
      this.line = line;
      this.start = start;
      this.end = end;
   }

   public String toString() {
      return this.line;
   }

   public int getStart() {
      return this.start;
   }

   public int getEnd() {
      return this.end;
   }
}
