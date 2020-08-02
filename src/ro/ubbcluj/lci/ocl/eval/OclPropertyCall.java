package ro.ubbcluj.lci.ocl.eval;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import ro.ubbcluj.lci.ocl.ExceptionAny;
import ro.ubbcluj.lci.ocl.OclClassInfo;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.datatypes.OclAny;
import ro.ubbcluj.lci.ocl.datatypes.OclBag;
import ro.ubbcluj.lci.ocl.datatypes.OclBoolean;
import ro.ubbcluj.lci.ocl.datatypes.OclCollection;
import ro.ubbcluj.lci.ocl.datatypes.OclEnumLiteral;
import ro.ubbcluj.lci.ocl.datatypes.OclInteger;
import ro.ubbcluj.lci.ocl.datatypes.OclOrderedSet;
import ro.ubbcluj.lci.ocl.datatypes.OclSequence;
import ro.ubbcluj.lci.ocl.datatypes.OclSet;
import ro.ubbcluj.lci.ocl.datatypes.OclString;
import ro.ubbcluj.lci.ocl.datatypes.Undefined;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public class OclPropertyCall extends OclExpression {
   public static int invokes = 0;
   public static int statics = 0;
   private OclExpression owner = null;
   private Method method = null;
   private OclExpression[] params = null;
   private boolean isDirect;
   private int methodIndex = 0;
   static HashMap cache = new HashMap();
   public static long cacheHits = 0L;
   public static long cacheMisses = 0L;
   public static boolean caching = false;

   public OclPropertyCall(OclNode p_nod, OclExpression p_owner, Method p_method, OclExpression[] p_params) {
      this.nod = p_nod;
      this.owner = p_owner;
      this.method = p_method;
      this.params = p_params;
      this.isDirect = false;
   }

   public String trimPath(String s) {
      return s.indexOf(".") < 0 ? s : s.substring(s.lastIndexOf(".") + 1);
   }

   public Object evaluate() throws ExceptionEvaluate {
      super.evaluate();
      if (OclUtil.isDebug) {
         OclUtil.debugListener.beforePropertyCall(this);
      }

      if (this.owner == null) {
         throw new ExceptionEvaluate("property call for a null object", this.nod);
      } else {
         Object ownerobj = this.owner.evaluate();
         if (caching && this.params.length <= 0) {
            HashMap subcache = (HashMap)cache.get(this.method);
            Object cachedResult;
            if (subcache != null) {
               cachedResult = subcache.get(ownerobj);
               if (cachedResult != null) {
                  ++cacheHits;
                  return cachedResult;
               } else {
                  ++cacheMisses;
                  Object result = this.trueEvaluate(ownerobj);
                  subcache.put(ownerobj, result);
                  return result;
               }
            } else {
               ++cacheMisses;
               subcache = new HashMap();
               cachedResult = this.trueEvaluate(ownerobj);
               subcache.put(ownerobj, cachedResult);
               cache.put(this.method, subcache);
               return cachedResult;
            }
         } else {
            ++cacheMisses;
            Object result = this.trueEvaluate(ownerobj);
            return result;
         }
      }
   }

   private Object trueEvaluate(Object ownerobj) throws ExceptionEvaluate {
      if (this.method.getDeclaringClass().equals(OclCollection.class) && !(ownerobj instanceof OclCollection) && this.nod != null && this.nod.parent != null && this.nod.parent.getChild(this.nod.parent.getChildCount() - 3) != null && this.nod.parent.getChild(this.nod.parent.getChildCount() - 3).type != null && this.nod.parent.getChild(this.nod.parent.getChildCount() - 3).type.is01) {
         OclSet fakeOwnerObj = new OclSet();
         if (!(ownerobj instanceof Undefined)) {
            fakeOwnerObj.getCollection().add(ownerobj);
         }

         ownerobj = fakeOwnerObj;
      }

      if (ownerobj instanceof OclBoolean && this.method.getName().equals("iif")) {
         return ((OclBoolean)ownerobj).isTrue() ? this.params[0].evaluate() : this.params[1].evaluate();
      } else {
         Object result;
         int right;
         if (this.method.getDeclaringClass() == (OclBoolean.class) && (this.method.getName().equals("implies") || this.method.getName().equals("or") || this.method.getName().equals("and"))) {
            int left;
            if (ownerobj instanceof Undefined) {
               left = 85;
            } else {
               left = ((OclBoolean)ownerobj).isTrue() ? 84 : 70;
            }

            String s = this.method.getName();
            if (s.equals("and") && left == 70) {
               return new OclBoolean(false);
            }

            if (s.equals("or") && left == 84) {
               return new OclBoolean(true);
            }

            if (s.equals("implies") && left == 70) {
               return new OclBoolean(true);
            }

            if (s.equals("implies") && left == 85) {
               return new Undefined();
            }

            result = this.params[0].evaluate();
            if (result instanceof Undefined) {
               right = 85;
            } else {
               right = ((OclBoolean)result).isTrue() ? 84 : 70;
            }

            if (s.equals("implies")) {
               if (left != 70 && (left != 84 || right != 84)) {
                  if (left == 84 && right == 70) {
                     return new OclBoolean(false);
                  }

                  return new Undefined();
               }

               return new OclBoolean(true);
            }

            if (s.equals("and")) {
               if (left != 70 && right != 70) {
                  if (left == 84 && right == 84) {
                     return new OclBoolean(true);
                  }

                  return new Undefined();
               }

               return new OclBoolean(false);
            }

            if (s.equals("or")) {
               if (left != 84 && right != 84) {
                  if (left == 70 && right == 70) {
                     return new OclBoolean(false);
                  }

                  return new Undefined();
               }

               return new OclBoolean(true);
            }
         }

         Object[] values = new Object[this.params.length];
         boolean foundUndefined = false;

         for(int i = 0; i < this.params.length; ++i) {
            values[i] = this.params[i].evaluate();
            if (values[i] instanceof OclEnumLiteral) {
               OclEnumLiteral lit = (OclEnumLiteral)values[i];
               if (lit.getLiteral() != null) {
                  values[i] = lit.getLiteral();
               }
            }

            if (values[i] instanceof Undefined) {
               foundUndefined = true;
            }
         }

         if (foundUndefined && !(ownerobj instanceof OclCollection)) {
            return new Undefined();
         } else if (ownerobj instanceof Undefined && this.method.getDeclaringClass() != (OclAny.class)) {
            return new Undefined();
         } else if (ownerobj == null) {
            System.err.println("[internal] evaluation of the propertycall owner resulted in null");
            return null;
         } else {
            try {
               Object[] values2;
               if ((this.method.getName().equals("dump") || this.method.getName().equals("dumpi")) && this.method.getParameterTypes().length > 0 && this.method.getParameterTypes()[this.method.getParameterTypes().length - 1] == (Object.class)) {
                  values2 = new Object[]{values[0], null};
                  Object[] paramlist = new Object[values.length - 1];

                  for(int i = 0; i < values.length - 1; ++i) {
                     paramlist[i] = values[i + 1];
                  }

                  values2[1] = paramlist;
                  values = values2;
               }

               if (this.method.getDeclaringClass() == (OclAny.class)) {
                  values2 = new Object[values.length + 1];
                  if (ownerobj instanceof OclEnumLiteral) {
                     EnumerationLiteral literal = ((OclEnumLiteral)ownerobj).getLiteral();
                     if (literal != null) {
                        values2[0] = literal;
                     } else {
                        values2[0] = ownerobj;
                     }
                  } else {
                     values2[0] = ownerobj;
                  }

                  for(right = 0; right < values.length; ++right) {
                     values2[right + 1] = values[right];
                  }

                  values = values2;
                  ownerobj = new OclAny();
               }

               if (this.method.getDeclaringClass() == (OclDirect.class)) {
                  OclDirect od = new OclDirect((ModelElement)ownerobj);
                  result = this.method.invoke(od, values);
               } else {
                  result = this.method.invoke(ownerobj, values);
                  ++invokes;
               }

               if (result == null && this.method.getReturnType() == (String.class)) {
                  result = "";
               }

               result = this.convertJavaToOcl(result);
               if (result == null && this.nod.type.is01) {
                  return new Undefined();
               } else if (result == null) {
                  throw new ExceptionEvaluate("the result of method " + OclUtil.printValue(ownerobj) + "." + this.method.getName() + " is null", this.nod);
               } else {
                  if (OclUtil.isDebug) {
                     OclUtil.debugListener.afterPropertyCall(this);
                  }

                  return result;
               }
            } catch (ExceptionEvaluate var7) {
               throw new ExceptionEvaluate(var7.getMessage(), var7.getEvalNode());
            } catch (InvocationTargetException var8) {
               Throwable ue = var8.getTargetException();
               if (ue == null) {
                  throw new ExceptionEvaluate("unknown invoke exception", this.nod);
               } else if (ue instanceof ExceptionAny) {
                  throw new ExceptionEvaluate(ue.getMessage(), this.nod);
               } else if (ue instanceof ExceptionEvaluate) {
                  throw (ExceptionEvaluate)ue;
               } else {
                  throw new ExceptionEvaluate(ue.getClass().getName() + " - " + ue.getMessage(), this.nod);
               }
            } catch (IllegalArgumentException var9) {
               throw new ExceptionEvaluate("illegal arguments in invoke: " + var9.getMessage(), this.nod);
            } catch (IllegalAccessException var10) {
               throw new ExceptionEvaluate("not visible in invoke: " + var10.getMessage(), this.nod);
            }
         }
      }
   }

   private Object convertJavaToOcl(Object result) throws ExceptionEvaluate {
      if (result instanceof Integer && this.nod.type != null && this.nod.type.type.umlapi.isMetamodel && (this.nod.type.type.name.endsWith("Kind") || this.nod.type.element != null && this.nod.type.element.name.endsWith("Kind"))) {
         OclClassInfo ec = this.nod.type.type.name.endsWith("Kind") ? this.nod.type.type : this.nod.type.element;
         return new OclEnumLiteral(ec, ((Integer)result).intValue());
      } else if (result instanceof Boolean) {
         return new OclBoolean(((Boolean)result).booleanValue());
      } else if (result instanceof Integer) {
         return new OclInteger(((Integer)result).longValue());
      } else if (result instanceof BigInteger) {
         int n = ((BigInteger)result).intValue();
         return new OclInteger(n == -1 ? 2147483647L : (long)n);
      } else if (result instanceof String) {
         return new OclString((String)result);
      } else if (result instanceof Character) {
         return new OclString(((Character)result).toString());
      } else if (result instanceof Collection) {
         Object oclresult;
         if (this.nod.type.type == this.umlapi.ORDEREDSET) {
            oclresult = new OclOrderedSet();
         } else if (this.nod.type.type == this.umlapi.BAG) {
            oclresult = new OclBag();
         } else if (this.nod.type.type == this.umlapi.SEQUENCE) {
            oclresult = new OclSequence();
         } else {
            oclresult = new OclSet();
         }

         Iterator it = ((Collection)result).iterator();

         while(it.hasNext()) {
            Object obj = it.next();
            if (obj != null) {
               ((OclCollection)oclresult).getCollection().add(this.convertJavaToOcl(obj));
            }
         }

         return oclresult;
      } else if (result instanceof Enumeration) {
         if (this.nod.type.type == this.nod.type.type.umlapi.SET) {
            return new OclSet((Enumeration)result);
         } else if (this.nod.type.type == this.nod.type.type.umlapi.BAG) {
            return new OclBag((Enumeration)result);
         } else if (this.nod.type.type == this.nod.type.type.umlapi.SEQUENCE) {
            return new OclSequence((Enumeration)result);
         } else {
            throw new ExceptionEvaluate("[internal] collection type expected " + this.methodIndex + " " + this.method, this.nod);
         }
      } else {
         return result;
      }
   }

   public String toString() {
      return "OclFunctionCall(" + this.method.getName() + ")";
   }
}
