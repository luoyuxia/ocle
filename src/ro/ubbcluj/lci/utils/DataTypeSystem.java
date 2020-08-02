package ro.ubbcluj.lci.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataType;

public final class DataTypeSystem {
   public static final String DT_REAL_NAME = "Real";
   public static final String DT_INTEGER_NAME = "Integer";
   public static final String DT_BOOLEAN_NAME = "Boolean";
   public static final String DT_UNLIMITEDINTEGER_NAME = "UnlimitedInteger";
   public static final String DT_STRING_NAME = "String";
   public static final String DT_UNDEFINED_NAME = "undefined";
   private HashMap stdTypes = new HashMap();
   private static Map dataTypeNameMap = new HashMap();

   void bind(String name, Classifier type) {
      Set ts = (Set)this.stdTypes.get(name);
      if (ts == null) {
         ts = new HashSet();
         this.stdTypes.put(name, ts);
      }

      ((Set)ts).add(type);
   }

   public DataType lookup(String name) {
      DataType dt = null;
      Set ts = (Set)this.stdTypes.get(name);
      if (ts != null && !ts.isEmpty()) {
         dt = (DataType)ts.iterator().next();
      }

      return dt;
   }

   public boolean isPredefinedType(Classifier dt) {
      boolean is = false;

      Set ds;
      for(Iterator values = this.stdTypes.values().iterator(); !is && values.hasNext(); is = ds.contains(dt)) {
         ds = (Set)values.next();
      }

      if (!is) {
         is = this.getTypeId(dt) != null;
      }

      return is;
   }

   public boolean isPredefined(String typeId, Classifier dataType) {
      boolean is = false;
      Set ts = (Set)this.stdTypes.get(typeId);
      if (ts != null) {
         is = ts.contains(dataType);
      }

      return is;
   }

   public String getTypeId(Classifier dt) {
      String r = null;
      String tn = dt.getName();
      Iterator keys = dataTypeNameMap.keySet().iterator();

      while(r == null && keys.hasNext()) {
         r = (String)keys.next();
         Set ns = (Set)dataTypeNameMap.get(r);
         if (!ns.contains(tn)) {
            r = null;
         }
      }

      return r;
   }

   DataTypeSystem() {
   }

   static {
      Set ts = new HashSet();
      ts.add("Boolean");
      ts.add("boolean");
      ts.add("bool");
      dataTypeNameMap.put("Boolean", ts);
      ts = new HashSet();
      ts.add("Integer");
      ts.add("integer");
      ts.add("int");
      ts.add("short");
      ts.add("long");
      dataTypeNameMap.put("Integer", ts);
      ts = new HashSet();
      ts.add("Real");
      ts.add("real");
      ts.add("float");
      ts.add("double");
      dataTypeNameMap.put("Real", ts);
      ts = new HashSet();
      ts.add("String");
      dataTypeNameMap.put("String", ts);
      ts = new HashSet();
      ts.add("undefined");
      ts.add("void");
      ts.add("Void");
      dataTypeNameMap.put("undefined", ts);
      ts = new HashSet();
      ts.add("UnlimitedInteger");
      dataTypeNameMap.put("UnlimitedInteger", ts);
   }
}
