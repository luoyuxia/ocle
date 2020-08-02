package ro.ubbcluj.lci.ocl;

import java.util.Iterator;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.eval.OclConstant;
import ro.ubbcluj.lci.ocl.eval.OclExpression;

public class OclLetItem {
   private static int sID = 0;
   private int ID;
   public static final int SELF = 0;
   public static final int ITERATOR = 1;
   public static final int LETDEF = 2;
   public static final int PARAMETER = 4;
   public static final int IMPLICIT = 5;
   public String name;
   Vector params = new Vector();
   public OclType rettype;
   public int vartype;
   public OclConstant[] paramvars;
   public OclExpression bodyexp;
   public OclConstant selfvar;
   public OclNode stereotype;
   public int uniqueSignatureIndex;

   OclLetItem() {
      ++sID;
      this.ID = sID;
      this.vartype = 2;
   }

   public OclLetItem(String _name, OclType _rettype, int _vartype, OclExpression _bodyexp) {
      this.name = _name;
      this.rettype = _rettype;
      this.vartype = _vartype;
      this.bodyexp = _bodyexp;
      ++sID;
      this.ID = sID;
   }

   private boolean sameSignature(OclLetItem other) {
      return this.sameSignature(other.name, other.params, other.rettype);
   }

   private boolean sameSignature(String name, Vector params, OclType rettype) {
      return this.sameSignature(name, params) && !rettype.sameTypeAs(rettype);
   }

   boolean conformingSignature(String name, Vector params) {
      if (!this.name.equals(name)) {
         return false;
      } else if (params == null && this.params.size() == 0) {
         return true;
      } else if (this.params.size() != params.size()) {
         return false;
      } else {
         Iterator it1 = this.params.iterator();
         Iterator it2 = params.iterator();

         while(it1.hasNext() && it2.hasNext()) {
            OclType t1 = (OclType)it1.next();
            OclType t2 = (OclType)it2.next();
            if (!t2.conforms(t1)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean sameSignature(String name, Vector params) {
      if (!this.name.equals(name)) {
         return false;
      } else if (params == null && this.params.size() == 0) {
         return true;
      } else if (this.params.size() != params.size()) {
         return false;
      } else {
         Iterator it1 = this.params.iterator();
         Iterator it2 = params.iterator();

         while(it1.hasNext() && it2.hasNext()) {
            OclType t1 = (OclType)it1.next();
            OclType t2 = (OclType)it2.next();
            if (!t2.sameTypeAs(t1)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean equals(Object obj) {
      return !(obj instanceof OclLetItem) ? false : this.sameSignature((OclLetItem)obj);
   }

   public String toString() {
      return "OclLetItem(" + this.ID + "," + this.name + "," + (this.params == null ? 0 : this.params.size()) + ")";
   }
}
