package ro.ubbcluj.lci.ocl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import ro.ubbcluj.lci.ocl.datatypes.OclAny;
import ro.ubbcluj.lci.ocl.datatypes.OclBag;
import ro.ubbcluj.lci.ocl.datatypes.OclCollection;
import ro.ubbcluj.lci.ocl.datatypes.OclEnumLiteral;
import ro.ubbcluj.lci.ocl.datatypes.OclOrderedSet;
import ro.ubbcluj.lci.ocl.datatypes.OclSequence;
import ro.ubbcluj.lci.ocl.datatypes.OclSet;
import ro.ubbcluj.lci.ocl.datatypes.OclTuple;
import ro.ubbcluj.lci.ocl.eval.OclConstant;
import ro.ubbcluj.lci.ocl.eval.OclExpression;
import ro.ubbcluj.lci.ocl.eval.OclPropertyCall;
import ro.ubbcluj.lci.ocl.nodes.classifierContext;
import ro.ubbcluj.lci.ocl.nodes.name;
import ro.ubbcluj.lci.ocl.nodes.oclExpression;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.ElementResidence;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.uml.foundation.core.TemplateParameter;
import ro.ubbcluj.lci.uml.modelManagement.ElementImport;

public class OclUtil {
   public static final String PACKAGE = "ro.ubbcluj.lci.";
   public static final String OCL14FILE = "metamodel/ocl20Types.xml";
   public static final String UMLAPI = "ro.ubbcluj.lci.uml.";
   public static Hashtable loopOperations = new Hashtable();
   public static OclUmlApi umlapi;
   public static OclUserModel usermodel;
   public static boolean isDebug;
   public static OclUtil.DebugListener debugListener;
   private static OclCompiler.DumpListener dumpListener;
   public static OclChecker oclChecker;
   public static final int OCL_WFR = 3;
   public static final int OCL_BCR = 2;
   public static final int OCL_ERROR = 1;

   public OclUtil() {
   }

   public static void clean(OclNode rules) {
      Stack st = new Stack();
      st.push(rules);

      while(!st.isEmpty()) {
         OclNode current = (OclNode)st.pop();
         if (current instanceof classifierContext) {
            OclExpression ev = current.evnode;
            if (ev != null) {
               ((OclConstant)ev).setValue((Object)null);
            }
         }

         for(int i = 0; i < current.getChildCount(); ++i) {
            st.push(current.getChild(i));
         }
      }

   }

   public static void cleanup() {
      umlapi = null;
      usermodel = null;
      oclChecker = null;
   }

   public static String className(Class c) {
      String s = new String(trimPath(c.getName()));
      if (s.endsWith("Impl")) {
         s = s.substring(0, s.length() - 4);
      }

      return s;
   }

   public static String cutImpl(String s) {
      int k;
      do {
         k = s.indexOf("Impl");
         if (k >= 0) {
            s = s.substring(0, k) + s.substring(k + 4);
         }
      } while(k > 0);

      return s;
   }

   public static String className(Object o) {
      return cutImpl(trimPath(o.getClass().getName()));
   }

   public static String trimPath(String s) {
      return s.indexOf(".") < 0 ? s : s.substring(s.lastIndexOf(".") + 1);
   }

   public static ArrayList getCollection(Object o) {
      ArrayList result = new ArrayList();
      if (o instanceof OclCollection) {
         if (o instanceof OclSet) {
            result.add("Set{");
         }

         if (o instanceof OclBag) {
            result.add("Bag{");
         }

         if (o instanceof OclSequence) {
            result.add("Sequence{");
         }

         if (o instanceof OclOrderedSet) {
            result.add("OrderedSet{");
         }

         Iterator it = ((OclCollection)o).getCollection().iterator();

         while(it.hasNext()) {
            Object obj = it.next();
            result.addAll(getCollection(obj));
            if (it.hasNext()) {
               result.add(",");
            }
         }

         result.add("}");
      } else if (o instanceof OclTuple) {
         result.add("Tuple{");
         OclTuple tuple = (OclTuple)o;
         int pc = tuple.getPartCount();

         for(int i = 0; i < pc; ++i) {
            result.addAll(getCollection(tuple.getPartValues()[i]));
            if (i < pc - 1) {
               result.add(",");
            }
         }

         result.add("}");
      } else if (!(o instanceof Element) && !(o instanceof ElementOwnership) && !(o instanceof ElementResidence) && !(o instanceof ElementImport) && !(o instanceof TemplateParameter)) {
         if (o instanceof OclEnumLiteral) {
            result.add(((OclEnumLiteral)o).getLiteral());
         } else {
            result.add(printValue(o));
         }
      } else {
         result.add(o);
      }

      return result;
   }

   public static String printValue(Object o) {
      if (o == null) {
         return "null";
      } else if (o instanceof String) {
         return (String)o;
      } else if (o instanceof OclCollection) {
         return ((OclCollection)o).printValue();
      } else if (o instanceof OclTuple) {
         return ((OclTuple)o).printValue();
      } else if (o instanceof OclAny) {
         return ((OclAny)o).printValue();
      } else {
         String name;
         if (o instanceof Feature) {
            name = className(o) + "(";
            Classifier c = ((Feature)o).getOwner();
            if (c == null) {
               name = name + "?";
            } else {
               name = name + c.getName();
            }

            return name + "::" + ((ModelElement)o).getName() + ")";
         } else if (o instanceof ModelElement) {
            name = ((ModelElement)o).getName();
            if (name == null) {
               name = "?";
            }

            return className(o) + "(" + name + ")";
         } else {
            return o instanceof OclType ? ((OclType)o).getFullName() : className(o) + "(" + o.toString() + ")";
         }
      }
   }

   public static String operationAsString(Operation op) {
      StringBuffer str = new StringBuffer(op.getName() + "(");
      Enumeration en = op.getParameterList();
      boolean first = true;
      Parameter ret = null;

      while(en.hasMoreElements()) {
         Parameter par = (Parameter)en.nextElement();
         boolean isReturn;
         if (par.getKind() == 3) {
            ret = par;
            isReturn = true;
         } else {
            if (!first) {
               str.append(",");
            }

            str.append(par.getType().getName());
            isReturn = false;
         }

         if (!isReturn) {
            first = false;
         }
      }

      str.append("):").append(ret != null ? ret.getType().getName() : "null");
      return str.toString();
   }

   public static String getRuleName(oclExpression rule) {
      int ind = rule.getParent().indexOfChild(rule);
      int i = ind - 2;
      OclNode temp = rule.getParent().getChild(i);
      return temp instanceof name ? temp.getValueAsString() : "";
   }

   public static void setDumpListener(OclCompiler.DumpListener dumpListener) {
      OclUtil.dumpListener = dumpListener;
   }

   public static void dump(List list) {
      if (dumpListener != null) {
         dumpListener.write(list);
      }

   }

   public static OclChecker getOclChecker() {
      return oclChecker;
   }

   public static Classifier getContext(OclNode node) {
      OclNode context = node.getParent().getChild(0).getChild(1);
      return context != null && context.type != null && context.type.type != null ? context.type.type.classifier : null;
   }

   public static int getOclFileType(String filename) {
      int result = 1;
      if (filename != null) {
         String uppercased = (new String(filename)).toUpperCase();
         if (uppercased.endsWith(".OCL")) {
            result = 3;
         } else if (uppercased.endsWith(".BCR")) {
            result = 2;
         }
      }

      return result;
   }

   static {
      String[] opit = new String[]{"exists", "forAll", "isUnique", "sortedBy", "iterate", "any", "one", "select", "reject", "collect", "closure", "collectNested"};

      for(int i = 0; i < opit.length; ++i) {
         loopOperations.put(opit[i], opit[i]);
      }

      umlapi = null;
      usermodel = null;
      isDebug = false;
      oclChecker = null;
   }

   public interface DebugListener {
      void beforePropertyCall(OclPropertyCall var1);

      void afterPropertyCall(OclPropertyCall var1);
   }
}
