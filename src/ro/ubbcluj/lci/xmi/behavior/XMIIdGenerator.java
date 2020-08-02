package ro.ubbcluj.lci.xmi.behavior;

public class XMIIdGenerator {
   private static final int START = 100;
   private static final int END = 999;
   private static final String LETTERS = "SGQXRABCDEFHIJKLMNOPTUVYZ";
   private int id;
   private int startLimit;
   private int endLimit;
   private int letter;
   private int inc;

   public XMIIdGenerator() {
      this.resetIdCounter();
   }

   public void resetIdCounter() {
      this.startLimit = 100;
      this.endLimit = 999;
      this.letter = 0;
      this.inc = 1;
      this.id = 0;
   }

   public String getNewID() {
      if (this.id < this.endLimit) {
         ++this.id;
      } else {
         this.id = this.startLimit;
         ++this.letter;
         if (this.letter > "SGQXRABCDEFHIJKLMNOPTUVYZ".length() - this.inc - 1) {
            this.letter = 0;
            ++this.inc;
            if (this.inc >= "SGQXRABCDEFHIJKLMNOPTUVYZ".length()) {
               this.startLimit = this.endLimit + 1;
               this.endLimit = this.startLimit * 10 - 1;
               this.id = this.startLimit;
               this.letter = 0;
               this.inc = 1;
            }
         }
      }

      return new String("SGQXRABCDEFHIJKLMNOPTUVYZ".substring(this.letter, this.letter + this.inc) + "." + (new Integer(this.id)).toString());
   }
}
