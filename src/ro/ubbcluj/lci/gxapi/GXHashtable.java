package ro.ubbcluj.lci.gxapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

public class GXHashtable {
   protected ArrayList keys = new ArrayList();
   protected ArrayList values = new ArrayList();

   public GXHashtable() {
   }

   public void addKeys(Object key) {
      this.keys.add(key);
   }

   public Enumeration getKeysList() {
      return Collections.enumeration(this.keys);
   }

   public void addValues(GXHashtableStrings value) {
      this.values.add(value);
   }

   public Enumeration getValuesList() {
      return Collections.enumeration(this.values);
   }

   public void copy(Hashtable h, Hashtable clones) {
      Enumeration en = h.keys();

      while(en.hasMoreElements()) {
         Object key = en.nextElement();
         this.keys.add(clones.get(key));
         GXHashtableStrings gxhs = new GXHashtableStrings();
         gxhs.copy((Hashtable)h.get(key));
         this.values.add(gxhs);
      }

   }

   public void extractData(Hashtable h, Hashtable clones) {
      for(int i = 0; i < this.keys.size(); ++i) {
         Object key = clones.get(this.keys.get(i));
         Hashtable ht = new Hashtable();
         ((GXHashtableStrings)this.values.get(i)).extractData(ht);
         h.put(key, ht);
      }

   }
}
