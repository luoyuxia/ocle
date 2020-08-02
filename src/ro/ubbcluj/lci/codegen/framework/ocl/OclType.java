package ro.ubbcluj.lci.codegen.framework.ocl;

import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public final class OclType {
   private static final List oclAnyOps = new ArrayList();
   private static final List oclAnyAttrs;
   private static final List oclAnyAssEnds;
   private static final HashMap instances = new HashMap();
   private Class realType;
   private OclType innerType = null;

   public OclType(Class rt) {
      this.realType = TypeUtilities.normalize(rt);
   }

   public OclType(Class[] types) {
      int l = types.length - 1;
      OclType current = this;

      int i;
      for(i = 0; i < l && (Collection.class).isAssignableFrom(types[i]); ++i) {
         current.realType = TypeUtilities.normalize(types[i]);
         OclType temp = new OclType();
         current.innerType = temp;
         current = temp;
      }

      if (i >= types.length) {
         throw new IllegalArgumentException("Inner type not specified for collection type");
      } else {
         current.realType = types[i];
      }
   }

   private OclType() {
   }

   public Class getRealType() {
      return this.realType;
   }

   public OclType getInnerType() {
      return this.innerType;
   }

   public void setInnerType(OclType it) {
      this.innerType = it;
   }

   public boolean isCollectionType() {
      return (Collection.class).isAssignableFrom(this.realType);
   }

   public boolean equals(Object o) {
      if (!(o instanceof OclType)) {
         return false;
      } else {
         OclType it = (OclType)o;

         for(OclType is = this; is != null; it = it.innerType) {
            if (it == null) {
               return false;
            }

            if (it.realType != is.realType) {
               return false;
            }

            is = is.innerType;
         }

         return true;
      }
   }

   public boolean conforms(OclType t) {
      OclType child = this;

      for(OclType pp = t; child != null; child = child.innerType) {
         if (pp == null) {
            return false;
         }

         if (!pp.realType.isAssignableFrom(child.realType)) {
            return false;
         }

         pp = pp.innerType;
      }

      return true;
   }

   public static OclType commonType(OclType[] types) {
      int n = types.length;
      if (n < 1) {
         return new OclType(Object.class);
      } else {
         OclType temp = types[0];

         for(int i = 1; i < n; ++i) {
            temp = commonType(temp, types[i]);
         }

         return temp;
      }
   }

   static final OclType commonType(OclType t1, OclType t2) {
      boolean bIsColl1 = t1.isCollectionType();
      boolean bIsColl2 = t2.isCollectionType();
      if (bIsColl1 ^ bIsColl2) {
         return new OclType(Object.class);
      } else {
         Class start = commonType(t1.getRealType(), t2.getRealType());
         if (!bIsColl1 && !bIsColl2) {
            return new OclType(start);
         } else {
            OclType ciType = commonType(t1.innerType, t2.innerType);
            OclType ret = new OclType(start);
            ret.setInnerType(ciType);
            return ret;
         }
      }
   }

   public String name() {
      StringBuffer bf = new StringBuffer();
      Class temp = TypeUtilities.normalize(this.realType);
      String baseName = BasicUtilities.getLastComponent(temp.getName());
      if ((OrderedSet.class) == temp) {
         baseName = "OrderedSet";
      } else if ((List.class).isAssignableFrom(temp)) {
         baseName = "List";
      } else if ((Set.class).isAssignableFrom(temp)) {
         baseName = "Set";
      }

      bf.append(baseName);
      if (this.isCollectionType()) {
         String inner = this.innerType.name();
         bf.append('(').append(inner).append(')');
      }

      return bf.toString();
   }

   static final Class commonType(Class c1, Class c2) {
      HashSet c1Par = new LinkedHashSet();
      HashSet c2Par = new LinkedHashSet();
      HashSet commonParents = null;
      HashSet c1GlobalPar = new LinkedHashSet();
      HashSet c2GlobalPar = new LinkedHashSet();
      c1Par.add(c1);
      c2Par.add(c2);
      c1GlobalPar.add(c1);
      c2GlobalPar.add(c2);
      boolean ready = false;

      while(!ready) {
         commonParents = (HashSet)c1GlobalPar.clone();
         commonParents.retainAll(c2GlobalPar);
         ready = !commonParents.isEmpty();
         HashSet temp = (HashSet)c1Par.clone();
         c1Par.clear();
         Iterator it = temp.iterator();

         int i;
         Class c;
         Class[] p;
         while(it.hasNext()) {
            c = (Class)it.next();
            p = TypeUtilities.parents(c);

            for(i = 0; i < p.length; ++i) {
               c1Par.add(p[i]);
            }
         }

         c1GlobalPar.addAll(c1Par);
         temp = (HashSet)c2Par.clone();
         c2Par.clear();
         it = temp.iterator();

         while(it.hasNext()) {
            c = (Class)it.next();
            p = TypeUtilities.parents(c);

            for(i = 0; i < p.length; ++i) {
               c2Par.add(p[i]);
            }
         }

         c2GlobalPar.addAll(c2Par);
      }

      return (Class)commonParents.iterator().next();
   }

   public Set operations() {
      Set result = CollectionUtilities.newSet();
      ArrayList classes = new ArrayList();
      classes.add(this.realType);

      while(true) {
         int i;
         Class current;
         do {
            if (classes.isEmpty()) {
               result.addAll(oclAnyOps);
               return result;
            }

            current = (Class)classes.remove(0);
            Method[] m = current.getDeclaredMethods();

            for(i = 0; i < m.length; ++i) {
               result.add(m[i].getName());
            }

            if (current.getSuperclass() != null) {
               classes.add(current.getSuperclass());
            }
         } while(!Modifier.isAbstract(current.getModifiers()));

         Class[] iis = current.getInterfaces();

         for(i = 0; i < iis.length; ++i) {
            classes.add(iis[i]);
         }
      }
   }

   public Set attributes() {
      Set result = CollectionUtilities.newSet();
      ArrayList classes = new ArrayList();
      classes.add(this.realType);

      while(!classes.isEmpty()) {
         Class current = (Class)classes.remove(0);
         Field[] f = current.getDeclaredFields();

         int i;
         for(i = 0; i < f.length; ++i) {
            result.add(f[i].getName());
         }

         if (current.getSuperclass() != null) {
            classes.add(current.getSuperclass());
         }

         Class[] ints = current.getInterfaces();

         for(i = 0; i < ints.length; ++i) {
            classes.add(ints[i]);
         }
      }

      result.addAll(oclAnyAttrs);
      return result;
   }

   public Set associationEnds() {
      Set r = CollectionUtilities.newSet();
      r.addAll(oclAnyAssEnds);
      return r;
   }

   public Set supertypes() {
      Set result = CollectionUtilities.newOrderedSet();
      Class[] parents = TypeUtilities.parents(this.realType);
      boolean isCollection = this.isCollectionType();
      if (isCollection) {
         Set innerSupertypes = this.innerType.supertypes();
         Iterator its = innerSupertypes.iterator();

         while(its.hasNext()) {
            OclType t = new OclType(this.realType);
            t.innerType = (OclType)its.next();
            result.add(t);
         }
      }

      for(int i = 0; i < parents.length; ++i) {
         OclType pt = new OclType(parents[i]);
         pt.innerType = isCollection && (Collection.class).isAssignableFrom(parents[i]) ? this.innerType : null;
         result.add(pt);
      }

      return result;
   }

   public Set allSupertypes() {
      Set result = CollectionUtilities.newOrderedSet();
      ArrayList temp = new ArrayList();
      temp.add(this);

      while(!temp.isEmpty()) {
         OclType currentType = (OclType)temp.remove(0);
         Set directParents = currentType.supertypes();
         temp.addAll(directParents);
         result.addAll(directParents);
      }

      return result;
   }

   public String toString() {
      return this.name();
   }

   public Set allInstances() {
      return allInstances(this.realType);
   }

   private static Set allInstances(Class type) {
      Set result = CollectionUtilities.newSet();
      List instProxies = null;
      synchronized(type) {
         instProxies = (List)instances.get(type);
         if (instProxies != null) {
            List tmp = new ArrayList();
            System.gc();
            Iterator it = instProxies.iterator();

            while(it.hasNext()) {
               WeakReference ref = (WeakReference)it.next();
               if (ref.get() == null) {
                  tmp.add(ref);
               } else {
                  result.add(ref.get());
               }
            }

            instProxies.removeAll(tmp);
            if (instProxies.isEmpty()) {
               instances.remove(type);
            }

            tmp = null;
         }

         return result;
      }
   }

   public static void registerInstance(Object inst, Class cls) {
      synchronized(cls) {
         List instProxies = (List)instances.get(cls);
         WeakReference newRef = new WeakReference(inst);
         if (instProxies == null) {
            instProxies = new ArrayList();
            instances.put(cls, instProxies);
         }

         ((List)instProxies).add(newRef);
      }
   }

   static {
      oclAnyOps.add("oclIsKindOf");
      oclAnyOps.add("oclIsTypeOf");
      oclAnyOps.add("oclAsType");
      oclAnyOps.add("isUndefined");
      oclAnyOps.add("isDefined");
      oclAnyOps.add("oclInState");
      oclAnyOps.add("oclIsNew");
      oclAnyAttrs = Collections.EMPTY_LIST;
      oclAnyAssEnds = new ArrayList();
      oclAnyAssEnds.add("oclType");
   }
}
