package ro.ubbcluj.lci.ocl.eval;

import ro.ubbcluj.lci.ocl.ExceptionAny;
import ro.ubbcluj.lci.ocl.OclClassInfo;
import ro.ubbcluj.lci.ocl.OclLetItem;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.datatypes.Undefined;

public class OclLetCall extends OclExpression {
   private static final int MAX_REC_LEVEL = 1000;
   private OclExpression selfobj = null;
   private OclExpression[] actual = null;
   private int letindex = -1;
   private static int recursionLevel = 0;
   private OclClassInfo owner;
   private boolean forceStatic;
   private OclNode stereotype;
   private OclLetItem innerLet = null;
   private Object simpleCacheLastSelf = null;
   private Object simpleCacheLastResult = null;

   public OclLetCall(OclClassInfo owner, OclExpression selfobj, OclNode nod, OclExpression[] actual, int letindex, boolean forceStatic, OclNode stereotype) {
      this.owner = owner;
      this.selfobj = selfobj;
      this.nod = nod;
      this.actual = actual;
      this.letindex = letindex;
      this.forceStatic = forceStatic;
      this.stereotype = stereotype;
   }

   public OclLetCall(OclClassInfo owner, OclExpression selfobj, OclNode nod, OclExpression[] actual, OclLetItem innerLet) {
      this.owner = owner;
      this.selfobj = selfobj;
      this.nod = nod;
      this.actual = actual;
      this.innerLet = innerLet;
   }

   public Object evaluate() throws ExceptionEvaluate {
      super.evaluate();
      if (recursionLevel > 1000) {
         recursionLevel = 0;
         throw new ExceptionEvaluate("recursion depth greater than 1000 in function", this.nod);
      } else {
         Object selfobjValue = this.selfobj.evaluate();
         if (selfobjValue instanceof Undefined && this.innerLet == null) {
            return selfobjValue;
         } else {
            ++recursionLevel;
            OclLetItem letvar = this.innerLet;
            if (this.innerLet == null) {
               try {
                  letvar = OclUtil.umlapi.dynamicBinding.getDynamicLetItem(this.owner, selfobjValue, this.letindex, this.forceStatic, this.stereotype);
               } catch (ExceptionAny var9) {
                  --recursionLevel;
                  throw new ExceptionEvaluate(var9.getMessage(), this.nod);
               }
            }

            OclConstant selfvar = letvar.selfvar;
            Object oldSelfValue = selfvar.getValue();
            selfvar.setValue(selfobjValue);
            Object[] saved = new Object[letvar.paramvars.length];
            boolean foundUndefined = selfvar.getValue() instanceof Undefined && this.innerLet == null;

            for(int i = 0; i < letvar.paramvars.length; ++i) {
               saved[i] = letvar.paramvars[i].getValue();
               letvar.paramvars[i].setValue(this.actual[i].evaluate());
               if (letvar.paramvars[i].getValue() instanceof Undefined) {
                  foundUndefined = true;
               }
            }

            Object result = foundUndefined ? new Undefined() : letvar.bodyexp.evaluate();
            selfvar.setValue(oldSelfValue);

            for(int i = 0; i < letvar.paramvars.length; ++i) {
               letvar.paramvars[i].setValue(saved[i]);
            }

            --recursionLevel;
            this.simpleCacheLastSelf = selfobjValue;
            this.simpleCacheLastResult = result;
            return result;
         }
      }
   }

   public String toString() {
      return "OclFunctionCall(" + this.letindex + ")";
   }
}
