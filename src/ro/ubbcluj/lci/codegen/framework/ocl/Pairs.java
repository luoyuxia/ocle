package ro.ubbcluj.lci.codegen.framework.ocl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;

public final class Pairs {
   private Comparator c = new Pairs.DefaultComparator();
   private ArrayList pairs = new ArrayList();

   public Pairs() {
   }

   public void addPair(Object first, Object second) {
      Pairs.Pair p = new Pairs.Pair();
      p.firstComponent = first;
      p.secondComponent = second;
      synchronized(this.pairs) {
         this.pairs.add(p);
      }
   }

   public void sort() {
      synchronized(this.pairs) {
         Collections.sort(this.pairs, this.c);
      }
   }

   public List asSequence() {
      List res = CollectionUtilities.newSequence();
      synchronized(this.pairs) {
         int i = 0;

         for(int n = this.pairs.size(); i < n; ++i) {
            res.add(((Pairs.Pair)this.pairs.get(i)).firstComponent);
         }

         return res;
      }
   }

   public OrderedSet asOrderedSet() {
      OrderedSet res = CollectionUtilities.newOrderedSet();
      synchronized(this.pairs) {
         int i = 0;

         for(int n = this.pairs.size(); i < n; ++i) {
            res.add(((Pairs.Pair)this.pairs.get(i)).firstComponent);
         }

         return res;
      }
   }

   private static class DefaultComparator implements Comparator {
      private DefaultComparator() {
      }

      public int compare(Object o1, Object o2) {
         Pairs.Pair p1 = (Pairs.Pair)o1;
         Pairs.Pair p2 = (Pairs.Pair)o2;
         return p1.secondComponent instanceof Comparable && p2.secondComponent instanceof Comparable ? ((Comparable)p1.secondComponent).compareTo(p2.secondComponent) : 0;
      }
   }

   private static class Pair {
      Object firstComponent;
      Object secondComponent;

      private Pair() {
      }
   }
}
