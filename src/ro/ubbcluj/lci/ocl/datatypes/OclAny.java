package ro.ubbcluj.lci.ocl.datatypes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import ro.ubbcluj.lci.ocl.ExceptionAny;
import ro.ubbcluj.lci.ocl.OclType;
import ro.ubbcluj.lci.ocl.OclUtil;

public class OclAny {
   public OclAny() {
   }

   public Object getValue() {
      return this;
   }

   public String printValue() {
      return "OclAny()";
   }

   public boolean equals(Object obj) {
      if (obj != null && obj instanceof OclAny) {
         if (this == this.getValue() && obj == ((OclAny)obj).getValue()) {
            System.err.println("[internal] OclAny.equals on two OclAny descendants without equals nor getValue");
            return false;
         } else {
            return this.getValue().equals(((OclAny)obj).getValue());
         }
      } else {
         return false;
      }
   }

   public static Method getMethod(String name, int noParams) throws ExceptionAny {
      try {
         if (noParams == 1) {
            Class[] cArg = new Class[1];
            cArg[0] = Object.class;
            return (OclAny.class).getMethod(name, cArg);
         } else {
            return noParams == 2 ? (OclAny.class).getMethod(name, new Class[]{Object.class, Object.class}) : (OclAny.class).getMethod(name,new Class[]{Object.class, Object.class, Object.class});
         }
      } catch (NoSuchMethodException var3) {
         throw new ExceptionAny("[fatal] method " + name + " in OclAny not found");
      }
   }

   public OclBoolean equal(Object _this, Object n) {
      return new OclBoolean(_this.equals(n));
   }

   public OclBoolean notequal(Object _this, Object n) {
      return new OclBoolean(!_this.equals(n));
   }

   public OclBoolean oclIsTypeOf(Object _this, Object t) throws ExceptionAny {
      OclType t1 = OclType.getType(_this);
      OclType t2 = OclType.getType(t).celement;
      return new OclBoolean(t1.sameTypeAs(t2));
   }

   public OclBoolean oclIsKindOf(Object _this, Object t) throws ExceptionAny {
      OclType t1 = OclType.getType(_this);
      OclType t2 = OclType.getType(t).celement;
      return new OclBoolean(t1.conforms(t2));
   }

   public Object oclAsType(Object _this, Object t) throws ExceptionAny {
      return this.oclIsKindOf(_this, t).isTrue() ? _this : new Undefined("invalid cast");
   }

   public Object oclIsNew(Object _this) {
      return new Undefined();
   }

   public Object oclInState(Object _this, Object state) {
      return new OclBoolean(false);
   }

   public Object oclType(Object _this) throws ExceptionAny {
      return new OclType(OclUtil.umlapi.OCLTYPE, OclType.getType(_this), false);
   }

   public OclBoolean dump(Object _this) {
      return this.dump(_this, new OclString("DEBUG: %0"), new Object[0]);
   }

   public OclBoolean dump(Object _this, Object msg, Object[] params) {
      String s = (String)((OclString)msg).getValue() + '%';
      List list = new ArrayList();
      boolean state = false;
      int var = 0;
      String buf = "";

      for(int i = 0; i < s.length(); ++i) {
         char ch = s.charAt(i);
         if (!state) {
            if (ch == '%') {
               list.add(buf);
               state = true;
               var = 0;
            } else {
               buf = buf + ch;
            }
         } else if (ch >= '0' && ch <= '9') {
            var = var * 10 + (byte)ch - 48;
         } else {
            if (var == 0) {
               list.add(_this);
            } else if (var <= params.length) {
               list.add(params[var - 1]);
            }

            state = false;
            buf = "" + ch;
         }
      }

      OclUtil.dump(list);
      return new OclBoolean(true);
   }

   public Object dumpi(Object _this) {
      this.dump(_this);
      return _this;
   }

   public Object dumpi(Object _this, Object msg, Object[] params) {
      this.dump(_this, msg, params);
      return _this;
   }

   public OclBoolean fail(Object _this) throws ExceptionAny {
      return this.fail(_this, new OclString("Error: "));
   }

   public OclBoolean fail(Object _this, Object msg) throws ExceptionAny {
      String s = "" + ((OclString)msg).getValue() + OclUtil.printValue(_this);
      throw new ExceptionAny(s);
   }

   public OclBoolean isDefined(Object _this) {
      return new OclBoolean(!(_this instanceof Undefined));
   }

   public OclBoolean isUndefined(Object _this) {
      return new OclBoolean(_this instanceof Undefined);
   }
}
