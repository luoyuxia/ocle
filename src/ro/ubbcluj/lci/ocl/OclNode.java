package ro.ubbcluj.lci.ocl;

import java.util.Iterator;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.eval.OclExpression;

public abstract class OclNode implements Cloneable {
   public OclNode parent;
   public OclNodeArray children = new OclNodeArray();
   public OclToken[] tokens;
   public int token_start;
   public int token_stop;
   public OclType type;
   public String stringValue;
   public Vector vectorValue;
   public Object objectValue;
   public Vector vectorEval;
   public Vector vectorLet;
   public boolean isCollectionOperation = false;
   public boolean hasIterator = false;
   public OclExpression evnode;
   public SearchResult searchResult = null;

   public OclNode() {
   }

   public void end() {
      this.children.end();
   }

   public String getValueAsString() {
      StringBuffer s = new StringBuffer();
      String last = " ";
      String now = " ";

      for(int i = this.token_start; i <= this.token_stop; ++i) {
         now = this.tokens[i].getValue();
         if (Character.isLetterOrDigit(now.charAt(now.length() - 1)) && Character.isLetterOrDigit(last.charAt(last.length() - 1))) {
            s.append(" ");
         }

         s.append(this.tokens[i].getValue());
         last = now;
      }

      return s.toString();
   }

   public abstract void acceptVisitor(OclVisitor var1) throws ExceptionChecker;

   public void visitChildren(OclVisitor visitor) throws ExceptionChecker {
      Iterator it = this.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         child.acceptVisitor(visitor);
      }

   }

   public int token_count() {
      return this.token_stop - this.token_start + 1;
   }

   public OclToken firstToken() {
      return this.tokens[this.token_start];
   }

   public OclToken lastToken() {
      return this.tokens[this.token_stop];
   }

   public OclNode firstChild() {
      return this.children.get(0);
   }

   public OclNode lastChild() {
      return this.children.get(this.children.size() - 1);
   }

   public OclNode getChild(int i) {
      return i >= 0 && i < this.children.size() ? this.children.get(i) : null;
   }

   public int getChildCount() {
      return this.children.size();
   }

   public int getStart() {
      return this.tokens[this.token_start].getStart();
   }

   public int getStop() {
      return this.tokens[this.token_stop].getStop();
   }

   public String getFilename() {
      return this.tokens[this.token_start].getFilename();
   }

   public OclNode getParent() {
      return this.parent;
   }

   public int indexOfChild(OclNode child) {
      for(int i = 0; i < this.children.size(); ++i) {
         if (child == this.children.get(i)) {
            return i;
         }
      }

      return -1;
   }

   public String toString() {
      return this.getValueAsString();
   }

   public void addChild(OclNode ch) {
      this.children.add(ch);
      ch.parent = this;
   }

   public final Object clone() throws CloneNotSupportedException {
      OclNode ret = null;

      try {
         ret = (OclNode)this.getClass().newInstance();
         OclToken[] tokens2 = new OclToken[this.token_count()];

         for(int i = 0; i < tokens2.length; ++i) {
            tokens2[i] = new OclToken(this.tokens[i + this.token_start]);
         }

         ret.copyFrom(this, -this.token_start, tokens2);
         return ret;
      } catch (Exception var4) {
         throw new CloneNotSupportedException(var4.getMessage());
      }
   }

   private void copyFrom(OclNode source, int tokenArrayDisplacement, OclToken[] t) throws Exception {
      this.token_start = source.token_start + tokenArrayDisplacement;
      this.token_stop = source.token_stop + tokenArrayDisplacement;
      this.tokens = t;
      this.searchResult = new SearchResult(source.searchResult);
      this.type = source.type;
      this.stringValue = source.stringValue;
      this.vectorValue = source.vectorValue != null ? (Vector)source.vectorValue.clone() : null;
      this.objectValue = source.objectValue;
      this.vectorEval = source.vectorEval != null ? (Vector)source.vectorEval.clone() : null;
      this.vectorLet = source.vectorLet != null ? (Vector)source.vectorLet.clone() : null;
      this.isCollectionOperation = source.isCollectionOperation;
      this.hasIterator = source.hasIterator;
      this.evnode = source.evnode;

      for(int i = 0; i < source.children.size(); ++i) {
         OclNode child = source.children.get(i);
         OclNode copyOfChild = (OclNode)child.getClass().newInstance();
         copyOfChild.copyFrom(child, tokenArrayDisplacement, t);
         this.addChild(copyOfChild);
      }

   }
}
