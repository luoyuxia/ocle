package ro.ubbcluj.lci.codegen.framework.ocl;

;
import ro.ubbcluj.lci.codegen.framework.dt.Real;

final class TypeUtilities {
   TypeUtilities() {
   }

   static Class[] parents(Class c) {
      c = normalize(c);
      Class[] ints = c.getInterfaces();
      Class p = c.getSuperclass();
      if (p == null && c != (Object.class)) {
         p = Object.class;
      }

      Class[] result = new Class[p != null ? 1 + ints.length : ints.length];
      if (p != null) {
         result[result.length - 1] = p;
      }

      System.arraycopy(ints, 0, result, 0, ints.length);
      return result;
   }

   static Class normalize(Class c) {
      Class v = c;
      if (c != Integer.TYPE && c != (Integer.class)) {
         if (c != Float.TYPE && c != Double.TYPE && c != (Float.class) && c != (Double.class)) {
            if (c == Boolean.TYPE || c == (Boolean.class)) {
               v =  ro.ubbcluj.lci.codegen.framework.dt.Boolean.class;
            }
         } else {
            v = Real.class;
         }
      } else {
         v = ro.ubbcluj.lci.codegen.framework.dt.Integer.class;
      }

      return v;
   }
}
