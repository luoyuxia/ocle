package ro.ubbcluj.lci.codegen.framework.ocl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import ro.ubbcluj.lci.codegen.framework.dt.Integer;
import ro.ubbcluj.lci.codegen.framework.dt.Real;

public final class Ocl {
   public Ocl() {
   }

   public static boolean inState(Object self, Object stateSpec) {
      return false;
   }

   public static boolean isTypeOf(Object self, OclType type) {
      boolean result = false;
      if (self != null && type != null) {
         OclType typeOfSelf = type(self);
         result = typeOfSelf.equals(type);
         if (!result && type.getRealType().isInterface()) {
            Class concrete = typeOfSelf.getRealType();
            Class intrf = type.getRealType();
            Class[] impl = concrete.getInterfaces();

            for(int i = 0; i < impl.length && !result; ++i) {
               if (intrf == impl[i]) {
                  result = true;
               }
            }
         }
      }

      return result;
   }

   public static boolean isKindOf(Object self, OclType type) {
      return self == null || (type != null && type(self).conforms(type));
   }

   public static OclType type(Object self) {
      if (self == null) {
         return new OclType(Object.class);
      } else {
         Class c = self.getClass();
         OclType ret = new OclType(c);
         if ((Set.class).isAssignableFrom(c)) {
            Collection tc = (Collection)self;
            Iterator it = tc.iterator();
            OclType[] types = new OclType[tc.size()];

            for(int var6 = 0; it.hasNext(); types[var6++] = type(it.next())) {
            }

            ret.setInnerType(OclType.commonType(types));
         }

         return ret;
      }
   }

   public static boolean isUndefined(Object self) {
      if (self instanceof Integer) {
         return Math.abs(((Integer)self).asInteger()) == 2147483647;
      } else if (self instanceof Real) {
         return Math.abs(((Real)self).asReal()) == 1.0F / 0.0;
      } else {
         return self == null;
      }
   }

   public static boolean isDefined(Object self) {
      if (self instanceof Integer) {
         return Math.abs(((Integer)self).asInteger()) < 2147483647;
      } else if (self instanceof Real) {
         return Math.abs(((Real)self).asReal()) < 1.0F / 0.0;
      } else {
         return self != null;
      }
   }

   public static boolean isNew(Object self) {
      return false;
   }

   public static final OclType getType(Class c) {
      return new OclType(c);
   }

   public static final OclType getType(Class[] types) {
      return new OclType(types);
   }
}
