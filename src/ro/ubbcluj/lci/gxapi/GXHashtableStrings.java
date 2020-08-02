package ro.ubbcluj.lci.gxapi;

import com.jgraph.graph.GraphConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class GXHashtableStrings {
   protected ArrayList keys = new ArrayList();
   protected ArrayList values = new ArrayList();
   protected Object value;

   public GXHashtableStrings() {
   }

   public void addKeys(String key) {
      this.keys.add(key);
   }

   public boolean containsKey(String key) {
      return this.keys.contains(key);
   }

   public Enumeration getKeysList() {
      return Collections.enumeration(this.keys);
   }

   public void addValues(String value) {
      this.values.add(value);
   }

   public Enumeration getValuesList() {
      return Collections.enumeration(this.values);
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public Object getValue() {
      return this.value;
   }

   public void copy(Hashtable h) {
      Enumeration en = h.keys();

      while(true) {
         while(en.hasMoreElements()) {
            String key = (String)en.nextElement();
            if (key.equals("value") && !(h.get(key) instanceof String)) {
               this.value = h.get(key);
            } else {
               this.keys.add(key);
               Object o = h.get(key);
               String string = o.toString();
               string = string.replace(' ', '_');
               if (o instanceof float[]) {
                  StringBuffer sb = new StringBuffer();
                  sb.append("[");

                  for(int i = 0; i < ((float[])o).length; ++i) {
                     sb.append((new Float(((float[])o)[i])).toString() + ",");
                  }

                  sb.setLength(sb.length() - 1);
                  sb.append("]");
                  string = sb.toString();
               }

               this.values.add(string);
            }
         }

         return;
      }
   }

   public void extractData(Hashtable result) {
      if (this.keys.size() > 0) {
         String key = (String)this.keys.get(0);
         String val = (String)this.values.get(0);
         StringTokenizer stval = new StringTokenizer(val);
         StringTokenizer stkey = new StringTokenizer(key);

         while(stkey.hasMoreTokens()) {
            String ktemp = stkey.nextToken();
            Object o = this.objFromVal(ktemp, stval.hasMoreTokens() ? stval.nextToken() : "");
            result.put(ktemp, o);
         }
      }

      if (this.value != null) {
         result.put("value", this.value);
      }

   }

   private Object objFromVal(String key, String val) {
      if (!key.equals("fontName") && !key.equals("value")) {
         if (!key.equals("a_pub") && !key.equals("a_pro") && !key.equals("a_pri") && !key.equals("a_pac") && !key.equals("m_pub") && !key.equals("m_pro") && !key.equals("m_pri") && !key.equals("m_pac") && !key.equals("stereotype") && !key.equals("msignature") && !key.equals("replaceAttributes") && !key.equals("opaque") && !key.equals(GraphConstants.EDITABLE) && !key.equals(GraphConstants.MOVEABLE) && !key.equals(GraphConstants.SIZEABLE) && !key.equals(GraphConstants.AUTOSIZE) && !key.equals(GraphConstants.BENDABLE) && !key.equals(GraphConstants.CONNECTABLE) && !key.equals(GraphConstants.DISCONNECTABLE) && !key.equals(GraphConstants.ABSOLUTE) && !key.equals("beginFill") && !key.equals("endFill")) {
            if (!key.equals("fontSize") && !key.equals("fontStyle") && !key.equals("verticalAlignment") && !key.equals("horizontalAlignment") && !key.equals("verticalTextPosition") && !key.equals("horizontalTextPosition") && !key.equals("lineStyle") && !key.equals("lineBegin") && !key.equals("lineEnd") && !key.equals("beginSize") && !key.equals("endsize")) {
               String temp;
               int index;
               int width;
               int lindex;
               if (!key.equals("linecolor") && !key.equals("bordercolor") && !key.equals("foregroundColor") && !key.equals("backgroundColor")) {
                  if (key.equals("linewidth")) {
                     return new Float(Float.parseFloat(val));
                  } else if (key.equals("dashPattern")) {
                     val = val.substring(1, val.length() - 1);
                     StringTokenizer st = new StringTokenizer(val, ",");
                     float[] value = new float[st.countTokens()];

                     for(width = 0; st.hasMoreTokens(); value[width++] = Float.parseFloat(st.nextToken())) {
                     }

                     return value;
                  } else {
                     int x;
                     if (key.equals(GraphConstants.BOUNDS)) {
                        lindex = val.indexOf("x=");
                        temp = val.substring(lindex + 2, val.indexOf(",", lindex));
                        width = Integer.parseInt(temp);
                        lindex = val.indexOf("y=");
                        temp = val.substring(lindex + 2, val.indexOf(",", lindex));
                        index = Integer.parseInt(temp);
                        lindex = val.indexOf("width=");
                        temp = val.substring(lindex + 6, val.indexOf(",", lindex));
                        width = Integer.parseInt(temp);
                        lindex = val.indexOf("height=");
                        temp = val.substring(lindex + 7, val.indexOf("]", lindex));
                        x = Integer.parseInt(temp);
                        return new Rectangle(width, index, width, x);
                     } else if (!key.equals(GraphConstants.LABELPOSITION) && !key.equals(GraphConstants.OFFSET)) {
                        if (!key.equals(GraphConstants.POINTS)) {
                           if (key.equals(GraphConstants.SIZE)) {
                              lindex = val.indexOf("width=");
                              temp = val.substring(lindex + 6, val.indexOf(",", lindex));
                              width = Integer.parseInt(temp);
                              lindex = val.indexOf("height=");
                              temp = val.substring(lindex + 7, val.indexOf("]", lindex));
                              index = Integer.parseInt(temp);
                              return new Dimension(width, index);
                           } else {
                              return null;
                           }
                        } else {
                           lindex = 0;

                           ArrayList retList;
                           for(retList = new ArrayList(); val.indexOf("java.awt.Point", lindex) >= 0; ++lindex) {
                              lindex = val.indexOf("java.awt.Point", lindex);
                              String tempP = val.substring(lindex, val.indexOf("]", lindex) + 1);
                              index = tempP.indexOf("x=");
                              temp = tempP.substring(index + 2, tempP.indexOf(",", index));
                              x = Integer.parseInt(temp);
                              index = tempP.indexOf("y=");
                              temp = tempP.substring(index + 2, tempP.indexOf("]", index));
                              int y = Integer.parseInt(temp);
                              retList.add(new Point(x, y));
                           }

                           return retList;
                        }
                     } else {
                        lindex = val.indexOf("x=");
                        temp = val.substring(lindex + 2, val.indexOf(",", lindex));
                        width = Integer.parseInt(temp);
                        lindex = val.indexOf("y=");
                        temp = val.substring(lindex + 2, val.indexOf("]", lindex));
                        index = Integer.parseInt(temp);
                        return new Point(width, index);
                     }
                  }
               } else {
                  lindex = val.indexOf("r=");
                  temp = val.substring(lindex + 2, val.indexOf(",", lindex));
                  width = Integer.parseInt(temp);
                  lindex = val.indexOf("g=");
                  temp = val.substring(lindex + 2, val.indexOf(",", lindex));
                  index = Integer.parseInt(temp);
                  lindex = val.indexOf("b=");
                  temp = val.substring(lindex + 2, val.indexOf("]", lindex));
                  width = Integer.parseInt(temp);
                  return new Color(width, index, width);
               }
            } else {
               return new Integer(Integer.parseInt(val));
            }
         } else {
            return new Boolean(val);
         }
      } else {
         return val;
      }
   }
}
