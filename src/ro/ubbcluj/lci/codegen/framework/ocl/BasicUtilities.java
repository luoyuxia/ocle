package ro.ubbcluj.lci.codegen.framework.ocl;

import java.util.List;
import java.util.StringTokenizer;

public final class BasicUtilities {
   public BasicUtilities() {
   }

   static String getLastComponent(String s) {
      String last = null;
      if (s != null) {
         for(StringTokenizer st = new StringTokenizer(s, ".", false); st.hasMoreTokens(); last = st.nextToken()) {
         }
      }

      return last;
   }

   public static boolean equal(Object o1, Object o2) {
      boolean r;
      if (o1 == null) {
         r = o2 == null;
      } else {
         r = o1.equals(o2);
      }

      return r;
   }

   public static boolean notEqual(Object o1, Object o2) {
      boolean r;
      if (o1 == null) {
         r = o2 != null;
      } else {
         r = !o1.equals(o2);
      }

      return r;
   }

   public static boolean isTrue(boolean self) {
      return self;
   }

   public static boolean isFalse(boolean self) {
      return !self;
   }

   public static boolean and(boolean self, boolean arg) {
      return self && arg;
   }

   public static boolean or(boolean self, boolean arg) {
      return self || arg;
   }

   public static boolean xor(boolean self, boolean arg) {
      return self ^ arg;
   }

   public static boolean not(boolean self) {
      return !self;
   }

   public static boolean implies(boolean self, boolean arg) {
      return !self || arg;
   }

   public static float min(float self, float arg) {
      return Math.min(self, arg);
   }

   public static float max(float self, float arg) {
      return Math.max(self, arg);
   }

   public static float divide(float self, float arg) {
      float r;
      if ((double)arg == 0.0D) {
         r = (float) (self < 0.0F ? -1.0F / 0.0 : 1.0F / 0.0);
      } else {
         r = self / arg;
      }

      return r;
   }

   public static float add(float self, float arg) {
      return self + arg;
   }

   public static int add(int self, int arg) {
      return self + arg;
   }

   public static float subs(float self, float arg) {
      return self - arg;
   }

   public static int subs(int self, int arg) {
      return self - arg;
   }

   public static float multiply(float self, float arg) {
      return self * arg;
   }

   public static int multiply(int self, int arg) {
      return self * arg;
   }

   public static float negate(float self) {
      return -self;
   }

   public static int negate(int self) {
      return -self;
   }

   public static int round(float self) {
      return Math.round(self);
   }

   public static float floor(float self) {
      return (float)Math.floor((double)self);
   }

   public static float abs(float self) {
      return Math.abs(self);
   }

   public static int abs(int self) {
      return Math.abs(self);
   }

   public static int max(int self, int arg) {
      return Math.max(self, arg);
   }

   public static int min(int self, int arg) {
      return Math.min(self, arg);
   }

   public static int div(int self, int arg) {
      return self / arg;
   }

   public static int mod(int self, int arg) {
      return self % arg;
   }

   public static boolean lt(float self, float arg) {
      return self < arg;
   }

   public static boolean le(float self, float arg) {
      return self <= arg;
   }

   public static boolean gt(float self, float arg) {
      return self > arg;
   }

   public static boolean ge(float self, float arg) {
      return self >= arg;
   }

   public static int size(String self) {
      return self == null ? 2147483647 : self.length();
   }

   public static String toLower(String self) {
      String result = null;
      if (self != null) {
         result = (new String(self)).toLowerCase();
      }

      return result;
   }

   public static String toUpper(String self) {
      String result = null;
      if (self != null) {
         result = (new String(self)).toUpperCase();
      }

      return result;
   }

   public static String substring(String self, int start, int stop) {
      return self == null ? null : self.substring(start - 1, stop);
   }

   public static int toInteger(String self) {
      int ret = 2147483647;

      try {
         ret = Integer.parseInt(self);
      } catch (NumberFormatException var3) {
      }

      return ret;
   }

   public static float toReal(String self) {
      float ret = (float) (1.0F / 0.0);

      try {
         ret = Float.parseFloat(self);
      } catch (NumberFormatException var3) {
      }

      return ret;
   }

   public static String concat(String self, String arg) {
      return self != null && arg != null ? self.concat(arg) : null;
   }

   public static boolean contains(String self, String arg) {
      return self != null && arg != null ? self.indexOf(arg) >= 0 : false;
   }

   public static int pos(String self, String arg) {
      return self != null && arg != null ? self.indexOf(arg) + 1 : 2147483647;
   }

   public static List split(String self, String separators) {
      if (self != null && separators != null) {
         StringTokenizer st = new StringTokenizer(self, separators, false);
         List ret = CollectionUtilities.newSequence();

         while(st.hasMoreTokens()) {
            ret.add(st.nextToken());
         }

         return ret;
      } else {
         return null;
      }
   }

   public static boolean lt(String self, String arg) {
      return self == null ? false : self.compareTo(arg) < 0;
   }

   public static boolean gt(String self, String arg) {
      return self == null ? false : self.compareTo(arg) > 0;
   }

   public static boolean ge(String self, String arg) {
      return self == null ? false : self.compareTo(arg) >= 0;
   }

   public static boolean le(String self, String arg) {
      return self == null ? false : self.compareTo(arg) <= 0;
   }
}
