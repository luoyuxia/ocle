package ro.ubbcluj.lci.ocl.datatypes;

import java.lang.reflect.Method;
import ro.ubbcluj.lci.ocl.ExceptionAny;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.eval.ExceptionEvaluate;

public class OclTuple extends OclAny {
   private int partCount;
   private String[] partNames;
   private Object[] partValues;
   private OclNode nod;

   public OclTuple(int partCount, String[] partNames, Object[] partValues, OclNode nod) {
      this.partCount = partCount;
      this.partNames = partNames;
      this.partValues = partValues;
      this.nod = nod;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof OclTuple)) {
         return false;
      } else {
         OclTuple other = (OclTuple)obj;
         if (this.partCount != other.partCount) {
            return false;
         } else {
            for(int i = 0; i < this.partCount; ++i) {
               boolean found = false;

               for(int j = 0; j < other.partCount; ++j) {
                  if (this.partNames[i].equals(other.partNames[j])) {
                     found = true;
                     if (!this.partValues[i].equals(other.partValues[j])) {
                        return false;
                     }
                     break;
                  }
               }

               if (!found) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public Object getPart(int index) throws ExceptionEvaluate {
      if (index >= 0 && index < this.partCount) {
         return this.partValues[index];
      } else {
         throw new ExceptionEvaluate("tuple part index out of range", this.nod);
      }
   }

   public String[] getPartNames() {
      return this.partNames;
   }

   public Object[] getPartValues() {
      return this.partValues;
   }

   public int getPartCount() {
      return this.partCount;
   }

   public Object getPart(String name) throws ExceptionEvaluate {
      int index = -1;

      for(int i = 0; i < this.partCount; ++i) {
         if (this.partNames[i].equals(name)) {
            index = i;
         }
      }

      if (index < 0) {
         throw new ExceptionEvaluate("tuple part name " + name + " not found", this.nod);
      } else {
         return this.partValues[index];
      }
   }

   public static Method getGetPartMethod() {
      try {
         return (OclTuple.class).getMethod("getPart", new Class[]{ String.class });
      } catch (NoSuchMethodException var1) {
         System.err.println("[internal] getPart method not found using reflection in OclTuple");
         return null;
      }
   }

   public String printValue() {
      StringBuffer s = new StringBuffer("Tuple{");

      for(int i = 0; i < this.partCount; ++i) {
         s.append(OclUtil.printValue(this.partValues[i]));
         if (i != this.partCount - 1) {
            s.append(',');
         }
      }

      s.append('}');
      return s.toString();
   }

   public OclBoolean dump() {
      return this.dump(new OclString("DEBUG: "));
   }

   public OclBoolean dump(OclString msg) {
      return new OclBoolean(true);
   }

   public Object dumpi() {
      this.dump();
      return this;
   }

   public Object dumpi(OclString msg) {
      this.dump(msg);
      return this;
   }

   public OclBoolean fail() throws ExceptionAny {
      return this.fail(new OclString("Error: "));
   }

   public OclBoolean fail(OclString msg) throws ExceptionAny {
      String s = "" + msg.getValue() + this.printValue();
      throw new ExceptionAny(s);
   }
}
