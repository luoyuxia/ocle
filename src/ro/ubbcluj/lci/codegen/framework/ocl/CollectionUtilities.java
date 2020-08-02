package ro.ubbcluj.lci.codegen.framework.ocl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.Integer;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.dt.Real;

public final class CollectionUtilities {
   public CollectionUtilities() {
   }

   public static int size(Collection coll) {
      return coll != null ? coll.size() : 2147483647;
   }

   public static int size(Object self) {
      return Ocl.isDefined(self) ? 1 : 0;
   }

   public static boolean includes(Collection coll, Object o) {
      return coll != null ? coll.contains(o) : false;
   }

   public static boolean excludes(Collection coll, Object o) {
      return coll != null ? !coll.contains(o) : false;
   }

   public static int count(Collection coll, Object x) {
      boolean isUndefined = Ocl.isUndefined(coll);
      int c = 2147483647;
      if (!isUndefined) {
         c = 0;
         Iterator it = coll.iterator();

         while(true) {
            Object current;
            do {
               if (!it.hasNext()) {
                  return c;
               }

               current = it.next();
            } while((current == null || !current.equals(x)) && current != x);

            ++c;
         }
      } else {
         return c;
      }
   }

   public static boolean includesAll(Collection self, Collection arg) {
      if (arg != null && self != null) {
         try {
            Collection tmp2 = (Collection)self.getClass().newInstance();
            tmp2.addAll(self);
            Iterator it = arg.iterator();

            boolean ia;
            Object next;
            for(ia = true; it.hasNext() && ia; ia = tmp2.remove(next)) {
               next = it.next();
            }

            return ia;
         } catch (Exception var6) {
            System.err.println(var6.getMessage());
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean excludesAll(Collection self, Collection arg) {
      if (arg != null && self != null) {
         Iterator it = arg.iterator();
         boolean ea = true;

         while(it.hasNext() && ea) {
            if (self.contains(it.next())) {
               ea = false;
            }
         }

         return ea;
      } else {
         return false;
      }
   }

   public static boolean isEmpty(Collection self) {
      return self != null ? self.isEmpty() : false;
   }

   public static boolean isEmpty(Object self) {
      return Ocl.isUndefined(self);
   }

   public static boolean notEmpty(Collection self) {
      return self != null ? !self.isEmpty() : false;
   }

   public static boolean notEmpty(Object self) {
      return Ocl.isDefined(self);
   }

   public static float sum(Collection self) throws IllegalArgumentException {
      if (self == null) {
         return (float) (1.0F / 0.0);
      } else {
         float s = 0.0F;

         Object next;
         for(Iterator it = self.iterator(); it.hasNext(); s += ((Real)next).asReal()) {
            next = it.next();
            if (!(next instanceof Real)) {
               throw new IllegalArgumentException("'sum()' expects only numeric objects. Invalid collection element(" + next + ")");
            }
         }

         return s;
      }
   }

   public static List including(List self, Object o) {
      if (self == null) {
         return null;
      } else {
         List ret = newBag(self);
         ret.add(o);
         return ret;
      }
   }

   public static Set including(Set self, Object o) throws IllegalArgumentException {
      if (self == null) {
         return null;
      } else if (o == null) {
         throw new IllegalArgumentException("Undefined values not allowed in sets");
      } else {
         Set ret = newSet(self);
         ret.add(o);
         return ret;
      }
   }

   public static OrderedSet including(OrderedSet self, Object o) throws IllegalArgumentException {
      if (self == null) {
         return null;
      } else if (o == null) {
         throw new IllegalArgumentException("Undefined values not allowed in ordered sets");
      } else {
         OrderedSet ret = newOrderedSet(self);
         ret.add(o);
         return ret;
      }
   }

   public static List excluding(List self, Object o) {
      if (self == null) {
         return null;
      } else if (!self.contains(o)) {
         return self;
      } else {
         List ret = newBag(self);
         ret.remove(o);
         return ret;
      }
   }

   public static Set excluding(Set self, Object o) {
      if (self == null) {
         return null;
      } else if (!self.contains(o)) {
         return self;
      } else {
         Set ret = newSet(self);
         ret.remove(o);
         return ret;
      }
   }

   public static OrderedSet excluding(OrderedSet self, Object o) {
      if (self == null) {
         return null;
      } else if (!self.contains(o)) {
         return self;
      } else {
         OrderedSet ret = newOrderedSet(self);
         ret.remove(o);
         return ret;
      }
   }

   public static Set flatten(Set self) {
      if (self == null) {
         return null;
      } else {
         Set result = newSet();
         flatten(result, self);
         return result;
      }
   }

   public static OrderedSet flatten(OrderedSet self) {
      if (self == null) {
         return null;
      } else {
         OrderedSet result = newOrderedSet();
         flatten(result, self);
         return result;
      }
   }

   public static List flatten(List self) {
      if (self == null) {
         return null;
      } else {
         List result = newBag();
         flatten(result, self);
         return result;
      }
   }

   public static Set asSet(Collection self) {
      if (self == null) {
         return null;
      } else if (self instanceof Set) {
         return (Set)self;
      } else {
         Set ret = newSet(self);
         return ret;
      }
   }

   public static List asBag(Collection self) {
      if (self == null) {
         return null;
      } else {
         return self instanceof List ? (List)self : newBag(self);
      }
   }

   public static List asSequence(Collection self) {
      return asBag(self);
   }

   public static OrderedSet asOrderedSet(Collection self) {
      if (self == null) {
         return null;
      } else {
         return self instanceof OrderedSet ? (OrderedSet)self : newOrderedSet(self);
      }
   }

   public static Set union(Set self, Set arg) {
      if (self != null && arg != null) {
         Set ret = newSet(self);
         ret.addAll(arg);
         return ret;
      } else {
         return null;
      }
   }

   public static List union(List self, Set arg) {
      if (self != null && arg != null) {
         List result = newBag(self);
         result.addAll(arg);
         return result;
      } else {
         return null;
      }
   }

   public static List union(Set self, List arg) {
      return union(arg, self);
   }

   public static List union(List self, List arg) {
      if (self != null && arg != null) {
         List ret = newBag(self);
         ret.addAll(arg);
         return ret;
      } else {
         return null;
      }
   }

   public static Set union(Object self, Set arg) {
      Set result = self != null && arg != null ? newSet() : null;
      if (result != null) {
         result.add(self);
         result.addAll(arg);
      }

      return result;
   }

   public static List union(Object self, List arg) {
      List result = self != null && arg != null ? newBag() : null;
      if (result != null) {
         result.add(self);
         result.addAll(arg);
      }

      return result;
   }

   public static List intersection(List self, List arg) {
      if (self != null && arg != null) {
         if (!(self instanceof OrderedSet) && !(arg instanceof OrderedSet)) {
            List ret = newBag(self);
            ret.retainAll(arg);
            return ret;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Set intersection(Set self, Set arg) {
      if (self != null && arg != null) {
         if (!(self instanceof OrderedSet) && !(arg instanceof OrderedSet)) {
            Set ret = newSet(self);
            ret.retainAll(arg);
            return ret;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static List intersection(List self, Set arg) {
      if (self != null && arg != null) {
         if (!(self instanceof OrderedSet) && !(arg instanceof OrderedSet)) {
            List ret = newBag(self);
            ret.retainAll(arg);
            return ret;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static List intersection(Set self, List arg) {
      return intersection(arg, self);
   }

   public static Set minus(Set self, Set arg) {
      if (self != null && arg != null) {
         Set ret = newSet();
         Iterator it = self.iterator();

         while(it.hasNext()) {
            Object candidate = it.next();
            if (!arg.contains(candidate)) {
               ret.add(candidate);
            }
         }

         return ret;
      } else {
         return null;
      }
   }

   public static OrderedSet minus(OrderedSet self, Set arg) {
      if (self != null && arg != null) {
         OrderedSet ret = newOrderedSet();
         Iterator it = self.iterator();

         while(it.hasNext()) {
            Object c = it.next();
            if (!arg.contains(c)) {
               ret.add(c);
            }
         }

         return ret;
      } else {
         return null;
      }
   }

   public static Set symmetricDifference(Set self, Set arg) {
      if (self != null && arg != null && !(self instanceof OrderedSet) && !(arg instanceof OrderedSet)) {
         Set result = newSet();
         result.addAll(self);
         Iterator it = arg.iterator();

         while(it.hasNext()) {
            Object next = it.next();
            if (!result.add(next)) {
               result.remove(next);
            }
         }

         return result;
      } else {
         return null;
      }
   }

   public static List append(List self, Object arg) {
      if (self == null) {
         return null;
      } else {
         List ret = newSequence();
         append(ret, self, arg);
         return ret;
      }
   }

   public static OrderedSet append(OrderedSet self, Object arg) {
      if (self != null && arg != null) {
         OrderedSet ret = newOrderedSet();
         append(ret, self, arg);
         return ret;
      } else {
         return null;
      }
   }

   public static List prepend(List self, Object arg) {
      if (self == null) {
         return null;
      } else {
         List ret = newSequence();
         ret.add(arg);
         ret.addAll(self);
         return ret;
      }
   }

   public static OrderedSet prepend(OrderedSet self, Object arg) {
      if (self != null && arg != null) {
         OrderedSet ret = newOrderedSet();
         ret.add(arg);
         ret.addAll(self);
         return ret;
      } else {
         return null;
      }
   }

   public static List insertAt(List self, int idx, Object arg) {
      if (self != null && idx >= 0 && idx <= self.size()) {
         List ret = newBag();
         ret.addAll(self);
         ret.add(idx, arg);
         return ret;
      } else {
         return null;
      }
   }

   public static OrderedSet insertAt(OrderedSet self, int idx, Object arg) {
      if (self != null && idx >= 0 && idx <= self.size() && arg != null) {
         OrderedSet ret = newOrderedSet();
         ret.addAll(self);
         ret.add(idx, arg);
         return ret;
      } else {
         return null;
      }
   }

   public static List subSequence(List self, int start, int end) {
      if (self != null && end <= self.size() && start >= 1) {
         List ret = newSequence();

         for(int i = start - 1; i < end; ++i) {
            ret.add(self.get(i));
         }

         return ret;
      } else {
         return null;
      }
   }

   public static OrderedSet subOrderedSet(OrderedSet self, int start, int end) {
      if (self != null && start >= 1 && end <= self.size()) {
         OrderedSet ret = newOrderedSet();

         for(int i = start - 1; i < end; ++i) {
            ret.add(self.get(i));
         }

         return ret;
      } else {
         return null;
      }
   }

   public static Object at(List self, int idx) {
      if (self == null) {
         return null;
      } else {
         Object r;
         try {
            r = self.get(idx - 1);
         } catch (IndexOutOfBoundsException var4) {
            System.err.println(var4.getMessage());
            r = null;
         }

         return r;
      }
   }

   public static int indexOf(List self, Object arg) {
      if (self == null) {
         return 2147483647;
      } else {
         int i = self.indexOf(arg);
         return i < 0 ? 2147483647 : i + 1;
      }
   }

   public static Object first(List self) {
      return self != null && self.size() > 0 ? self.get(0) : null;
   }

   public static Object last(List self) {
      return self != null && self.size() > 0 ? self.get(self.size() - 1) : null;
   }

   public static void add(Collection c, Object x) {
      if (c != null) {
         c.add(x);
      }

   }

   public static Set newSet() {
      return new LinkedHashSet();
   }

   public static Set newSet(Collection c) {
      return c != null ? new LinkedHashSet(c) : new LinkedHashSet();
   }

   public static OrderedSet newOrderedSet() {
      return new OrderedSet();
   }

   public static OrderedSet newOrderedSet(Collection c) {
      return c != null ? new OrderedSet(c) : new OrderedSet();
   }

   public static List newBag() {
      return new ArrayList();
   }

   public static List newBag(Collection c) {
      return c != null ? new ArrayList(c) : new ArrayList();
   }

   public static List newSequence() {
      return new ArrayList();
   }

   public static List newSequence(Collection c) {
      return c != null ? new ArrayList(c) : new ArrayList();
   }

   public static void addRange(Collection arg, int start, int stop) {
      if (arg != null) {
         for(int i = start; i <= stop; ++i) {
            arg.add(new Integer(i));
         }
      }

   }

   private static void flatten(Collection result, Collection arg) {
      try {
         Iterator it = arg.iterator();

         while(it.hasNext()) {
            Object next = it.next();
            if (!(next instanceof Collection)) {
               result.add(next);
            } else {
               Collection temp = (Collection)result.getClass().newInstance();
               flatten(temp, (Collection)next);
               result.addAll(temp);
            }
         }
      } catch (Exception var5) {
         System.err.println(var5.getMessage());
      }

   }

   private static void append(Collection result, Collection target, Object x) {
      result.clear();
      result.addAll(target);
      result.add(x);
   }
}
