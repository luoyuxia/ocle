package ro.ubbcluj.lci.gxapi.filters;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import ro.ubbcluj.lci.gui.diagrams.filters.ClassFilter;
import ro.ubbcluj.lci.gxapi.GXHashtableStrings;

public class GXClassFilter {
   protected GXHashtableStrings map;

   public GXClassFilter() {
      this.updateFilter();
   }

   public void setMap(GXHashtableStrings map) {
      this.map = map;
   }

   public GXHashtableStrings getMap() {
      return this.map;
   }

   public void copy(ClassFilter cf) {
      HashMap cfm = cf.getMap();
      this.map = new GXHashtableStrings();
      Iterator it = cfm.keySet().iterator();

      while(it.hasNext()) {
         String key = (String)it.next();
         this.map.addKeys(key);
         this.map.addValues(cfm.get(key).toString());
      }

   }

   public void extractData(ClassFilter cf) {
      Hashtable ht = new Hashtable();
      this.map.extractData(ht);
      HashMap hm = new HashMap();
      Enumeration en = ht.keys();

      while(en.hasMoreElements()) {
         Object key = en.nextElement();
         hm.put(key, ht.get(key));
      }

      cf.setMap(hm);
   }

   private void updateFilter() {
      if (this.map == null) {
         this.map = new GXHashtableStrings();
      }

      if (!this.map.containsKey("a_pub")) {
         this.map.addKeys("a_pub");
         this.map.addValues("true");
      }

      if (!this.map.containsKey("a_pro")) {
         this.map.addKeys("a_pro");
         this.map.addValues("true");
      }

      if (!this.map.containsKey("a_pri")) {
         this.map.addKeys("a_pri");
         this.map.addValues("true");
      }

      if (!this.map.containsKey("a_pac")) {
         this.map.addKeys("a_pac");
         this.map.addValues("true");
      }

      if (!this.map.containsKey("m_pub")) {
         this.map.addKeys("m_pub");
         this.map.addValues("true");
      }

      if (!this.map.containsKey("m_pro")) {
         this.map.addKeys("m_pro");
         this.map.addValues("true");
      }

      if (!this.map.containsKey("m_pri")) {
         this.map.addKeys("m_pri");
         this.map.addValues("true");
      }

      if (!this.map.containsKey("m_pac")) {
         this.map.addKeys("a_pub");
         this.map.addValues("true");
      }

      if (!this.map.containsKey("stereotype")) {
         this.map.addKeys("stereotype");
         this.map.addValues("true");
      }

      if (!this.map.containsKey("msignature")) {
         this.map.addKeys("msignature");
         this.map.addValues("true");
      }

   }
}
