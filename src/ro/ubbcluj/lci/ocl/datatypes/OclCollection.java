package ro.ubbcluj.lci.ocl.datatypes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.ExceptionAny;
import ro.ubbcluj.lci.ocl.OclLetItem;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.SearchResult;
import ro.ubbcluj.lci.ocl.eval.ExceptionEvaluate;
import ro.ubbcluj.lci.ocl.eval.OclAccumulator;
import ro.ubbcluj.lci.ocl.eval.OclConstant;
import ro.ubbcluj.lci.ocl.eval.OclExpression;
import ro.ubbcluj.lci.ocl.eval.OclLetCall;
import ro.ubbcluj.lci.ocl.eval.OclPropertyCall;

public abstract class OclCollection extends OclAny {
   public OclCollection() {
   }

   public OclCollection newInstance() throws ExceptionAny {
      try {
         return (OclCollection)this.getClass().newInstance();
      } catch (Exception var2) {
         throw new ExceptionAny(var2.getMessage());
      }
   }

   public boolean equals(Object obj) {
      return obj instanceof OclCollection ? this.equal((OclCollection)obj).isTrue() : false;
   }

   public OclCollection add(Object obj) throws ExceptionAny {
      OclCollection col = this.newInstance();
      col.getCollection().addAll(this.getCollection());
      if (obj instanceof OclCollection) {
         col.getCollection().addAll(((OclCollection)obj).getCollection());
      } else {
         col.getCollection().add(obj);
      }

      return col;
   }

   public OclCollection addNested(Object obj) throws ExceptionAny {
      OclCollection col = this.newInstance();
      col.getCollection().addAll(this.getCollection());
      col.getCollection().add(obj);
      return col;
   }

   public static Method getCollectMethod() {
      Class[] paramTypes = new Class[]{OclConstant.class, OclExpression.class};

      try {
         return (OclCollection.class).getMethod("collect", paramTypes);
      } catch (Exception var2) {
         System.err.println("[fatal] can't find collect method in collection");
         return null;
      }
   }

   public static Method getClosureMethod() {
      Class[] paramTypes = new Class[]{OclConstant.class, OclExpression.class};

      try {
         return (OclCollection.class).getMethod("closure", paramTypes);
      } catch (Exception var2) {
         System.err.println("[fatal] can't find closure method in collection");
         return null;
      }
   }

   public static Method getAddMethod() {
      Class[] paramTypes = new Class[]{Object.class};

      try {
         return (OclCollection.class).getMethod("add", paramTypes);
      } catch (Exception var2) {
         System.err.println("[fatal] can't find add method in collection");
         return null;
      }
   }

   public static Method getAddNestedMethod() {
      Class[] paramTypes = new Class[]{Object.class};

      try {
         return (OclCollection.class).getMethod("addNested", paramTypes);
      } catch (Exception var2) {
         System.err.println("[fatal] can't find add method in collection");
         return null;
      }
   }

   public String printValue() {
      StringBuffer s = new StringBuffer();
      if (this instanceof OclSet) {
         s.append("Set{");
      }

      if (this instanceof OclBag) {
         s.append("Bag{");
      }

      if (this instanceof OclSequence) {
         s.append("Sequence{");
      }

      if (this instanceof OclOrderedSet) {
         s.append("OrderedSet{");
      }

      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         Object obj = it.next();
         s.append(OclUtil.printValue(obj));
         if (it.hasNext()) {
            s.append(',');
         }
      }

      s.append('}');
      return s.toString();
   }

   public abstract Collection getCollection();

   public OclInteger size() {
      return new OclInteger((long)this.getCollection().size());
   }

   public Object includes(Object obj) {
      return new OclBoolean(this.getCollection().contains(obj));
   }

   public Object excludes(Object obj) {
      return new OclBoolean(!this.getCollection().contains(obj));
   }

   public Object includesAll(OclCollection col) {
      return new OclBoolean(this.getCollection().containsAll(col.getCollection()));
   }

   public Object excludesAll(OclCollection col) {
      Iterator it = col.getCollection().iterator();

      do {
         if (!it.hasNext()) {
            return new OclBoolean(true);
         }
      } while(!this.getCollection().contains(it.next()));

      return new OclBoolean(false);
   }

   public OclInteger count(Object obj) {
      int k = 0;
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         if (obj.equals(it.next())) {
            ++k;
         }
      }

      return new OclInteger((long)k);
   }

   public OclBoolean isEmpty() {
      return new OclBoolean(this.getCollection().isEmpty());
   }

   public OclBoolean notEmpty() {
      return new OclBoolean(!this.getCollection().isEmpty());
   }

   public OclReal sum() {
      int si = 0;
      double sd = 0.0D;
      boolean isInt = true;

      OclReal no;
      for(Iterator it = this.getCollection().iterator(); it.hasNext(); sd += no.val()) {
         no = (OclReal)it.next();
         if (no instanceof OclInteger) {
            si = (int)((long)si + ((OclInteger)no).longValue());
         } else {
            isInt = false;
         }
      }

      if (isInt) {
         return new OclInteger((long)si);
      } else {
         return new OclReal(sd);
      }
   }

   public Object exists(OclConstant itervar, OclExpression exp) throws ExceptionEvaluate {
      boolean existsUndefined = false;
      Object obj = null;
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());

         try {
            obj = exp.evaluate();
            if (obj instanceof Undefined) {
               existsUndefined = true;
            } else if (((OclBoolean)obj).isTrue()) {
               return new OclBoolean(true);
            }
         } catch (ExceptionEvaluate var7) {
         }
      }
      if (existsUndefined) {
         obj = new Undefined();
      } else {
         obj =  new OclBoolean(false);
      }
      return obj;
   }

   public Object forAll(OclConstant itervar, OclExpression exp) throws ExceptionEvaluate {
      Iterator it = this.getCollection().iterator();

      Object obj;
      do {
         if (!it.hasNext()) {
            return new OclBoolean(true);
         }

         itervar.setValue(it.next());
         obj = exp.evaluate();
         if (obj instanceof Undefined) {
            return obj;
         }
      } while(!((OclBoolean)obj).isFalse());

      return new OclBoolean(false);
   }

   public Object forAll(OclConstant itervar, OclConstant itervar2, OclExpression exp) throws ExceptionEvaluate {
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());
         Iterator it2 = this.getCollection().iterator();

         while(it2.hasNext()) {
            itervar2.setValue(it2.next());
            Object obj = exp.evaluate();
            if (obj instanceof Undefined) {
               return obj;
            }

            if (((OclBoolean)obj).isFalse()) {
               return new OclBoolean(false);
            }
         }
      }

      return new OclBoolean(true);
   }

   public OclBoolean isUnique(OclConstant itervar, OclExpression exp) throws ExceptionEvaluate {
      Vector sofar = new Vector();
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());
         Object obj = exp.evaluate();
         if (sofar.contains(obj)) {
            return new OclBoolean(false);
         }

         sofar.add(obj);
      }

      return new OclBoolean(true);
   }

   public Object sortedBy(OclConstant itervar, OclExpression exp) throws ExceptionEvaluate {
      Collection col = this.getCollection();
      Object[] array = new Object[col.size()];
      int i = 0;

      Object item;
      for(Iterator it = col.iterator(); it.hasNext(); array[i++] = new Object[]{item, exp.evaluate()}) {
         item = it.next();
         if (item instanceof Undefined) {
            return item;
         }

         itervar.setValue(item);
         if (exp.evaluate() instanceof Undefined) {
            return new Undefined();
         }
      }

      try {
         Arrays.sort(array, new OclCollection.Comp(itervar, exp));
      } catch (ExceptionAny var8) {
         throw new ExceptionEvaluate("expression does not implement the < (less than) operation (" + var8.getMessage() + ")", exp.nod);
      }

      if (!(this instanceof OclSet) && !(this instanceof OclOrderedSet)) {
         OclSequence rez = new OclSequence();

         for(i = 0; i < array.length; ++i) {
            rez.col.add(((Object[])array[i])[0]);
         }

         return rez;
      } else {
         OclOrderedSet rez = new OclOrderedSet();

         for(i = 0; i < array.length; ++i) {
            rez.col.directAdd(((Object[])array[i])[0]);
         }

         return rez;
      }
   }

   public Object iterate(OclConstant itervar, OclAccumulator accum, OclExpression exp) throws ExceptionEvaluate {
      Object oldAccumValue = accum.getValue();
      accum.evaluate();
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());
         accum.setValue(exp.evaluate());
      }

      Object result = accum.getValue();
      accum.setValue(oldAccumValue);
      return result;
   }

   public Object iterate(OclConstant itervar, OclConstant itervar2, OclAccumulator accum, OclExpression exp) throws ExceptionEvaluate {
      Object oldAccumValue = accum.getValue();
      accum.evaluate();
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());
         Iterator it2 = this.getCollection().iterator();

         while(it2.hasNext()) {
            itervar2.setValue(it2.next());
            accum.setValue(exp.evaluate());
         }
      }

      Object result = accum.getValue();
      accum.setValue(oldAccumValue);
      return result;
   }

   public Object any(OclConstant itervar, OclExpression exp) throws ExceptionEvaluate {
      Object obj = null;
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());

         try {
            obj = exp.evaluate();
            if (!(obj instanceof Undefined) && ((OclBoolean)obj).isTrue()) {
               return itervar.getValue();
            }
         } catch (ExceptionEvaluate var6) {
         }
      }

      return new Undefined();
   }

   public Object one(OclConstant itervar, OclExpression exp) throws ExceptionEvaluate {
      boolean foundone = false;
      boolean existsUndefined = false;
      Object obj = null;
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());

         try {
            obj = exp.evaluate();
            if (obj instanceof Undefined) {
               existsUndefined = true;
            } else if (((OclBoolean)obj).isTrue()) {
               if (foundone) {
                  return new OclBoolean(false);
               }

               foundone = true;
            }
         } catch (ExceptionEvaluate var8) {
         }
      }
      if (!foundone && existsUndefined) {
         obj = new Undefined();
      } else {
         obj = new OclBoolean(foundone);
      }

      return  obj;
   }

   public Object select(OclConstant itervar, OclExpression exp) throws ExceptionAny, ExceptionEvaluate {
      OclCollection col = this.newInstance();
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());
         Object obj = exp.evaluate();
         if (obj instanceof Undefined) {
            return obj;
         }

         if (((OclBoolean)obj).isTrue()) {
            col.getCollection().add(itervar.getValue());
         }
      }

      return col;
   }

   public Object reject(OclConstant itervar, OclExpression exp) throws ExceptionAny, ExceptionEvaluate {
      OclCollection col = this.newInstance();
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());
         Object obj = exp.evaluate();
         if (obj instanceof Undefined) {
            return obj;
         }

         if (((OclBoolean)obj).isFalse()) {
            col.getCollection().add(itervar.getValue());
         }
      }

      return col;
   }

   public Object collect(OclConstant itervar, OclExpression exp) throws ExceptionAny, ExceptionEvaluate {
      return ((OclCollection)this.collectNested(itervar, exp)).flatten();
   }

   public Object collectNested(OclConstant itervar, OclExpression exp) throws ExceptionAny, ExceptionEvaluate {
      Object col;
      if (this instanceof OclSequence) {
         col = new OclSequence();
      } else {
         col = new OclBag();
      }

      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         itervar.setValue(it.next());
         Object obj = exp.evaluate();
         ((OclCollection)col).getCollection().add(obj);
      }

      return col;
   }

   public OclCollection flatten() throws ExceptionAny {
      OclCollection col = this.newInstance();
      Iterator it = this.getCollection().iterator();

      while(it.hasNext()) {
         Object obj = it.next();
         if (obj instanceof OclCollection) {
            col.getCollection().addAll(((OclCollection)obj).flatten().getCollection());
         } else {
            col.getCollection().add(obj);
         }
      }

      return col;
   }

   public OclSet asSet() {
      return new OclSet(this.getCollection());
   }

   public OclBag asBag() {
      OclBag bag = new OclBag();
      bag.col = new Vector(this.getCollection());
      return bag;
   }

   public OclSequence asSequence() {
      OclSequence seq = new OclSequence();
      seq.col = new Vector(this.getCollection());
      return seq;
   }

   public OclOrderedSet asOrderedSet() {
      return new OclOrderedSet(this.getCollection());
   }

   public OclCollection including(Object obj) throws ExceptionAny {
      OclCollection col = this.newInstance();
      col.getCollection().addAll(this.getCollection());
      col.getCollection().add(obj);
      return col;
   }

   public OclCollection excluding(Object obj) throws ExceptionAny {
      OclCollection col = this.newInstance();
      col.getCollection().addAll(this.getCollection());
      col.getCollection().remove(obj);
      return col;
   }

   public abstract OclBoolean equal(OclCollection var1);

   public OclBoolean notequal(OclCollection col) {
      return this.equal(col).not();
   }

   public OclCollection union(OclCollection col) {
      Object rez;
      if (!(this instanceof OclSequence) && !(col instanceof OclSequence)) {
         if (!(this instanceof OclBag) && !(col instanceof OclBag)) {
            if (!(this instanceof OclOrderedSet) && !(col instanceof OclOrderedSet)) {
               rez = new OclSet();
            } else {
               rez = new OclOrderedSet();
            }
         } else {
            rez = new OclBag();
         }
      } else {
         rez = new OclSequence();
      }

      ((OclCollection)rez).getCollection().addAll(this.getCollection());
      ((OclCollection)rez).getCollection().addAll(col.getCollection());
      return (OclCollection)rez;
   }

   public OclCollection intersection(OclCollection col) {
      Object rez;
      if (!(this instanceof OclSet) && !(col instanceof OclSet)) {
         rez = new OclBag();
      } else {
         rez = new OclSet();
      }

      Iterator it = this.getCollection().iterator();
      Vector v = new Vector(col.getCollection());

      while(it.hasNext()) {
         Object obj = it.next();
         if (v.contains(obj)) {
            ((OclCollection)rez).getCollection().add(obj);
            v.remove(obj);
         }
      }

      return (OclCollection)rez;
   }

   public OclSet closure(OclConstant itervar, OclExpression exp) throws ExceptionAny, ExceptionEvaluate {
      OclSet result = new OclSet();
      OclSet queue = new OclSet(this.getCollection());

      for(int i = 0; i < queue.getCollection().size(); ++i) {
         itervar.setValue(((ArrayListSet)queue.getCollection()).get(i));
         Object obj = exp.evaluate();
         if (obj instanceof OclCollection) {
            Collection temp = ((OclCollection)obj).getCollection();
            queue.getCollection().addAll(temp);
            result.getCollection().addAll(temp);
         } else {
            queue.getCollection().add(obj);
            result.getCollection().add(obj);
         }
      }

      return result;
   }

   public Object dumpi() {
      this.dump(this);
      return this;
   }

   public Object dumpi(OclString msg, Object[] params) {
      this.dump(this, msg, params);
      return this;
   }

   public OclBoolean fail() throws ExceptionAny {
      return this.fail(new OclString("Error: "));
   }

   public OclBoolean fail(OclString msg) throws ExceptionAny {
      String s = "" + msg.getValue() + this.printValue();
      throw new ExceptionAny(s);
   }

   private class Comp implements Comparator {
      OclConstant itervar;
      OclExpression exp;
      OclExpression lt;
      OclConstant left;
      OclConstant right;

      Comp(OclConstant p_itervar, OclExpression p_exp) throws ExceptionAny {
         this.itervar = p_itervar;
         this.exp = p_exp;
         Vector params = new Vector();
         params.add(this.exp.nod.type);
         SearchResult sr = this.exp.nod.type.findFeature("", ".lt", params, 2);
         if (sr.type.type != OclUtil.umlapi.BOOLEAN) {
            throw new ExceptionAny("the < (less than) operation returns non-boolean type");
         } else {
            this.left = new OclConstant(this.exp.nod, (Object)null);
            this.right = new OclConstant(this.exp.nod, (Object)null);
            if (sr.foundType == 2) {
               Method method = sr.foundOwner.getJavaMethod();
               this.lt = new OclPropertyCall(this.exp.nod, this.left, method, new OclExpression[]{this.right});
            } else {
               if (sr.foundType != 8) {
                  throw new ExceptionAny("invalid feature type");
               }

               OclLetItem letitem = sr.foundLetItem;
               this.lt = new OclLetCall(sr.foundOwner, this.left, this.exp.nod, new OclExpression[]{this.right}, letitem.uniqueSignatureIndex, false, (OclNode)null);
            }

         }
      }

      public int compare(Object o1, Object o2) {
         try {
            this.left.setValue(((Object[])o1)[1]);
            this.right.setValue(((Object[])o2)[1]);
            Object rez = this.lt.evaluate();
            if (rez instanceof OclBoolean) {
               return ((OclBoolean)rez).isTrue() ? -1 : 1;
            } else {
               return -1;
            }
         } catch (ExceptionEvaluate var4) {
            System.err.println("[internal] exception in sortedBy comparator");
            return 0;
         }
      }
   }
}
