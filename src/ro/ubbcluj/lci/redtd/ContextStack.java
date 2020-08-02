package ro.ubbcluj.lci.redtd;

import java.util.Collection;
import java.util.LinkedList;

public class ContextStack {
   private LinkedList contextStack = new LinkedList();

   public ContextStack() {
   }

   public void push(XMLParsingContext context) {
      this.contextStack.addLast(context);
   }

   public XMLParsingContext pop() {
      if (this.contextStack.isEmpty()) {
         return null;
      } else {
         XMLParsingContext oldContext = (XMLParsingContext)this.contextStack.removeLast();
         return oldContext;
      }
   }

   public XMLParsingContext peek() {
      return !this.contextStack.isEmpty() ? (XMLParsingContext)this.contextStack.getLast() : null;
   }

   public Collection getContextStack() {
      return this.contextStack;
   }

   public boolean isEmpty() {
      return this.contextStack.isEmpty();
   }
}
