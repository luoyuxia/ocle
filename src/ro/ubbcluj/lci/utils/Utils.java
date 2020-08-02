package ro.ubbcluj.lci.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.nodes.name;
import ro.ubbcluj.lci.ocl.nodes.oclExpression;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class Utils {
   public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

   public Utils() {
   }

   public static String formatRule(OclNode rule, int length) {
      String name = rule.getValueAsString();
      String result = "";
      if (rule instanceof oclExpression) {
         String temp = getRuleName((oclExpression)rule);
         if (temp.length() > 0) {
            result = "[" + temp + "]";
         }
      }

      result = result + name;
      if (result.length() > length) {
         result = result.substring(0, length - 3);
         result = result + "...";
      }

      return result;
   }

   public static String getRuleName(oclExpression rule) {
      int ind = rule.getParent().indexOfChild(rule);
      int i = ind - 2;
      OclNode temp = rule.getParent().getChild(i);
      return temp instanceof name ? temp.getValueAsString() : "";
   }

   public static String repeatString(String start, int count) {
      StringBuffer str = new StringBuffer(start);

      for(int i = 1; i < count; ++i) {
         str.append(start);
      }

      return str.toString();
   }

   public static String toUml(Class cls) {
      StringBuffer bf = new StringBuffer();
      ArrayList q = new ArrayList();
      String name = cls.getName();
      int i = "ro.ubbcluj.lci.uml.".length();

      while(i >= 0) {
         int j = name.indexOf(46, i);
         if (j < 0) {
            i = -1;
         } else {
            q.add(name.substring(i, j));
            i = j + 1;
         }
      }

      i = 0;

      for(int s = q.size(); i < s; ++i) {
         bf.append(toUml((String)q.get(i))).append("::");
      }

      bf.append(OclUtil.className(cls));
      return bf.toString();
   }

   public static String toUml(String packageName) {
      StringBuffer ret = new StringBuffer();
      int s = packageName.length();
      if (s == 0) {
         return "";
      } else {
         ret.append(Character.toUpperCase(packageName.charAt(0)));

         for(int i = 1; i < s; ++i) {
            if (Character.isUpperCase(packageName.charAt(i))) {
               ret.append(" ").append(packageName.charAt(i));
            } else {
               ret.append(packageName.charAt(i));
            }
         }

         return ret.toString();
      }
   }

   public static String oclToUml(String name) {
      return name.replace('_', ' ');
   }

   public static String name(Object obj) {
      if (!(obj instanceof ModelElement)) {
         return getNameForObject(obj);
      } else {
         String temp = ((ModelElement)obj).getName();
         if (temp == null || "".equals(temp)) {
            temp = "<Unnamed " + OclUtil.className(obj.getClass()) + ">";
         }

         return temp;
      }
   }

   public static String fullName(Object obj) {
      if (!(obj instanceof ModelElement)) {
         return getNameForObject(obj);
      } else {
         String r = UMLUtilities.getFullyQualifiedName((ModelElement)obj);
         if (r.endsWith("::") || "".equals(r)) {
            r = r.concat("<Unnamed " + OclUtil.className(obj.getClass()) + ">");
         }

         return r;
      }
   }

   private static String formatRange(MultiplicityRange mr) {
      BigInteger u = mr.getUpper();
      BigInteger m = BigInteger.ONE.negate();
      String temp;
      if (m.equals(u)) {
         temp = "*";
      } else {
         temp = u.toString();
      }

      return mr.getLower() + ".." + temp;
   }

   private static String getNameForObject(Object obj) {
      String ret;
      if (obj instanceof Expression) {
         ret = ((Expression)obj).getBody();
      } else if (obj instanceof MultiplicityRange) {
         ret = formatRange((MultiplicityRange)obj);
      } else if (obj instanceof Multiplicity) {
         Collection ranges = ((Multiplicity)obj).getCollectionRangeList();
         Iterator it = ranges.iterator();
         String temp = "[";
         if (it.hasNext()) {
            temp = temp + formatRange((MultiplicityRange)it.next());
         }

         while(it.hasNext()) {
            temp = temp + "," + formatRange((MultiplicityRange)it.next());
         }

         temp = temp + "]";
         ret = temp;
      } else {
         ret = "?";
      }

      return ret;
   }

   public static boolean distinctAddAll(List target, Collection data) {
      Iterator it = data.iterator();
      boolean changed = false;

      while(it.hasNext()) {
         Object item = it.next();
         if (!target.contains(item)) {
            changed = target.add(item);
         }
      }

      return changed;
   }

   public static boolean matchesByWildcards(String pattern, String str) {
      return matchesByWildcards(0, pattern.toCharArray(), 0, str.toCharArray());
   }

   private static boolean matchesByWildcards(int i1, char[] pattern, int i2, char[] str) {
      if (i1 == pattern.length) {
         return i2 == str.length;
      } else if (pattern[i1] == '?') {
         return i2 == str.length ? false : matchesByWildcards(i1 + 1, pattern, i2 + 1, str);
      } else if (pattern[i1] != '*') {
         if (i2 == str.length) {
            return false;
         } else {
            return pattern[i1] == str[i2] && matchesByWildcards(i1 + 1, pattern, i2 + 1, str);
         }
      } else {
         while(i1 < pattern.length && pattern[i1] == '*') {
            ++i1;
         }

         boolean bMatchAny = false;
         if (i1 == pattern.length) {
            return true;
         } else {
            for(int i = i2; i < str.length && !bMatchAny; ++i) {
               if (matchesByWildcards(i1, pattern, i, str)) {
                  bMatchAny = true;
               }
            }

            return bMatchAny;
         }
      }
   }
}
