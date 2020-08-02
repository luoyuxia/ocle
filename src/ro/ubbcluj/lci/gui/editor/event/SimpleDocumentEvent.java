package ro.ubbcluj.lci.gui.editor.event;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentEvent.ElementChange;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class SimpleDocumentEvent implements DocumentEvent {
   private int length;
   private int offset;
   private int linesInserted;
   private int linesRemoved;
   private EventType type;

   public SimpleDocumentEvent(EventType t, String text, int offset) {
      this.offset = offset;
      this.type = t;
      this.length = text.replaceAll("\r\n", "\n").length();
      int i = 0;

      int bn;
      for(bn = 0; i < this.length; ++i) {
         if (text.charAt(i) == '\n') {
            ++bn;
         }
      }

      if (this.type.equals(EventType.INSERT)) {
         this.linesInserted = bn;
         this.linesRemoved = 0;
      } else {
         this.linesInserted = 0;
         this.linesRemoved = bn;
      }

   }

   public SimpleDocumentEvent(EventType t, byte[] text, int offset) {
      this.type = t;
      this.offset = offset;
      int i = 0;
      int bn = 0;

      int len;
      for(len = 0; i < this.length; ++i) {
         if (text[i] == 10) {
            ++bn;
         }

         if (text[i] != 13) {
            ++len;
         }
      }

      if (this.type.equals(EventType.INSERT)) {
         this.linesInserted = bn;
         this.linesRemoved = 0;
      } else {
         this.linesInserted = 0;
         this.linesRemoved = bn;
      }

      this.length = len;
   }

   public SimpleDocumentEvent(EventType t, int length, int offset, int linesInserted, int linesRemoved) {
      this.offset = offset;
      this.type = t;
      this.length = length;
      this.linesInserted = linesInserted;
      this.linesRemoved = linesRemoved;
   }

   public int getLinesRemoved() {
      return this.linesRemoved;
   }

   public int getLinesInserted() {
      return this.linesInserted;
   }

   public int getOffset() {
      return this.offset;
   }

   public int getLength() {
      return this.length;
   }

   public Document getDocument() {
      return null;
   }

   public EventType getType() {
      return this.type;
   }

   public ElementChange getChange(Element elem) {
      return null;
   }
}
