package ro.ubbcluj.lci.ocl.codegen.norm;

import java.util.ArrayList;
import ro.ubbcluj.lci.ocl.OclType;

public class DefinitionConstraintNode extends AbstractConstraintNode {
   private ArrayList parameters = new ArrayList();
   private OclType returnType;

   public DefinitionConstraintNode() {
   }

   public final int getKind() {
      return 3;
   }

   public final void addParameter(Variable p) {
      this.parameters.add(p);
   }

   public final Variable[] getParameters() {
      int n = this.parameters.size();
      Variable[] ret = new Variable[n];

      for(int i = 0; i < n; ++i) {
         ret[i] = (Variable)this.parameters.get(i);
      }

      return ret;
   }

   public void setReturnType(OclType rt) {
      this.returnType = rt;
   }

   public OclType getReturnType() {
      return this.returnType;
   }

   public String toString() {
      StringBuffer bf = (new StringBuffer("[definition]")).append(this.constraintName);
      Variable[] v = this.getParameters();
      if (v.length > 0) {
         bf.append('(');
         int i = 0;

         while(i < v.length) {
            bf.append(v[i].getName()).append(':').append(v[i].getType().getFullName());
            ++i;
            if (i < v.length) {
               bf.append(',');
            }
         }

         bf.append(')');
      }

      bf.append(':');
      bf.append(this.returnType.getFullName());
      return bf.toString();
   }

   public boolean hasSameSignature(DefinitionConstraintNode other) {
      boolean has = this.getName().equals(other.getName());
      if (has) {
         has = this.returnType.equals(other.getReturnType());
         if (has) {
            Variable[] fp = this.getParameters();
            Variable[] fp2 = other.getParameters();
            has = fp.length == fp2.length;
            if (has) {
               for(int i = 0; i < fp.length && has; ++i) {
                  has &= fp[i].getType().equals(fp2[i].getType());
               }
            }
         }
      }

      return has;
   }
}
