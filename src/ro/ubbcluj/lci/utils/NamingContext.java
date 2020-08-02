package ro.ubbcluj.lci.utils;

import java.util.ArrayList;
import java.util.List;

public class NamingContext {
   private NamingContext parent;
   private List names;

   public NamingContext() {
      this.parent = null;
      this.names = new ArrayList();
   }

   public NamingContext(NamingContext p) {
      this();
      this.parent = p;
   }

   public String newName(String base) {
      String start = base;

      for(int i = 0; this.contains(start); ++i) {
         start = base + i;
      }

      return start;
   }

   public final NamingContext getParent() {
      return this.parent;
   }

   public String registerNewName(String base) {
      if (base == null || "".equals(base)) {
         base = "name";
      }

      String name = this.newName(base);
      this.names.add(name);
      return name;
   }

   public boolean contains(String name) {
      boolean c = this.names.contains(name);
      if (!c && this.parent != null) {
         c = this.parent.contains(name);
      }

      return c;
   }

   public void clear() {
      this.names.clear();
   }

   public String add(String n) {
      this.names.add(n);
      return n;
   }
}
