package ro.ubbcluj.lci.ocl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.nodes.OclParserBase;
import ro.ubbcluj.lci.ocl.nodes.OclROOT;
import ro.ubbcluj.lci.ocl.nodes.OclTEXT;

public class OclParser extends OclParserBase {
   static HashMap tokenmap = new HashMap();
   private static int CONTEXT;
   private static int PACKAGE;
   private static int MODEL;
   private static int IN;
   private static int BODY;
   private OclToken[] data;
   private int maxk;
   private OclNode root;
   private int lastExpectedToken;

   public OclParser(Collection tokens) {
      this.data = new OclToken[tokens.size()];
      Iterator it = tokens.iterator();

      for(int i = 0; i < tokens.size(); ++i) {
         this.data[i] = (OclToken)it.next();
      }

   }

   public OclParser(OclTokenizer tokenizer) throws ExceptionTokenizer {
      this(new OclTokenizer[]{tokenizer});
   }

   public OclParser(OclTokenizer[] tokenizer) throws ExceptionTokenizer {
      Vector vtok = new Vector();

      OclToken tok;
      int i;
      for(i = 0; i < tokenizer.length; ++i) {
         while((tok = tokenizer[i].getToken(false)) != null) {
            vtok.add(tok);
         }
      }

      this.data = new OclToken[vtok.size()];

      for(i = 0; i < vtok.size(); ++i) {
         this.data[i] = (OclToken)vtok.elementAt(i);
      }

   }

   public OclNode getRoot() {
      return this.root;
   }

   public void parse() throws ExceptionParser {
      this.k = 0;
      this.maxk = 0;
      this.root = new OclROOT();
      this.root.token_start = 1;
      this.root.token_stop = 0;
      this.root.tokens = this.data;
      if (this.data.length != 0 && this.oclFile(this.root)) {
         while(this.k < this.data.length && this.data[this.k].getType() == 6) {
            ++this.k;
         }

         if (this.k < this.data.length) {
            throw new ExceptionParser("extra text after end of file", this.getErrorToken());
         }
      } else if (this.data.length != 0) {
         throw new ExceptionParser("syntax error: expected \"" + tokens[this.lastExpectedToken] + "\"", this.getErrorToken());
      }
   }

   private OclToken getErrorToken() {
      if (this.data.length == 0) {
         return null;
      } else {
         return this.maxk < this.data.length ? this.data[this.maxk] : this.data[this.data.length - 1];
      }
   }

   protected boolean match(OclNode parent, int s) {
      while(this.k < this.data.length) {
         if (this.data[this.k].getType() != 6) {
            if (this.k >= this.maxk) {
               this.maxk = this.k;
               this.lastExpectedToken = s;
            }

            int entryk = this.k;
            if (this.data[this.k].getType() == 6) {
               ++this.k;
            }

            switch(this.data[this.k].getType()) {
            case 0:
               int cst = this.data[this.k].getConstant();
               if (s == NAME && (cst == CONTEXT || cst == PACKAGE || cst == MODEL || cst == IN || cst == BODY)) {
                  break;
               }
            case 3:
               if (s != this.data[this.k].getConstant()) {
                  return false;
               }
               break;
            case 1:
               if (s != STRING) {
                  return false;
               }
               break;
            case 2:
            case 5:
               if (s != NUMBER) {
                  return false;
               }
               break;
            case 4:
               if (s != NAME) {
                  return false;
               }
               break;
            default:
               return false;
            }

            ++this.k;
            if (this.k > this.maxk) {
               this.maxk = this.k;
            }

            OclNode nod = new OclTEXT();
            this.complete(nod, parent, entryk);
            return true;
         }

         ++this.k;
      }

      return false;
   }

   protected boolean complete(OclNode node, OclNode parent, int kk) {
      node.tokens = this.data;
      node.token_start = kk;
      node.token_stop = this.k - 1;
      node.parent = parent;
      parent.children.add(node);
      node.end();
      return true;
   }

   static {
      for(int i = 0; i < tokens.length; ++i) {
         tokenmap.put(tokens[i], new Integer(i));
      }

      STRING = ((Integer)tokenmap.get("STRING")).intValue();
      NAME = ((Integer)tokenmap.get("NAME")).intValue();
      NUMBER = ((Integer)tokenmap.get("NUMBER")).intValue();
      CONTEXT = ((Integer)tokenmap.get("context")).intValue();
      PACKAGE = ((Integer)tokenmap.get("package")).intValue();
      MODEL = ((Integer)tokenmap.get("model")).intValue();
      IN = ((Integer)tokenmap.get("in")).intValue();
      BODY = ((Integer)tokenmap.get("body")).intValue();
   }
}
