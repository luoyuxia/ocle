package ro.ubbcluj.lci.ocl;

import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public class OclUserElement {
   private Object elem;
   private String path;
   private String name;

   public OclUserElement(Object p_elem, String p_path) {
      this.elem = p_elem;
      this.path = p_path;
      if (this.elem instanceof ModelElement) {
         this.name = ((ModelElement)this.elem).getName();
      } else {
         this.name = "<noname>";
      }

   }

   public String toString() {
      return this.path + this.name;
   }

   public Object getElement() {
      return this.elem;
   }
}
