package ro.ubbcluj.lci.utils;

import java.util.ArrayList;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;

class ObjectArrayList extends ArrayList {
   AssociationEnd end;

   public ObjectArrayList(AssociationEnd end) {
      this.end = end;
   }

   public String toString() {
      String result;
      if (this.size() == 0) {
         result = "<empty>";
      } else {
         result = this.get(0).toString();

         for(int j = 1; j < this.size(); ++j) {
            result = result + " ," + this.get(j);
         }
      }

      return result;
   }
}
