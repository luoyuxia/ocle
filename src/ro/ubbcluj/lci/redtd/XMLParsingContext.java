package ro.ubbcluj.lci.redtd;

import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;

public class XMLParsingContext {
   private Instance contextElement;
   private AssociationEnd subcontextEnd;

   public XMLParsingContext(Instance contextElement, AssociationEnd subcontextEnd) {
      this.contextElement = contextElement;
      this.subcontextEnd = subcontextEnd;
   }

   public Instance getContextElement() {
      return this.contextElement;
   }

   public AssociationEnd getSubcontextEnd() {
      return this.subcontextEnd;
   }

   public void setSubcontextEnd(AssociationEnd subcontextEnd) {
      this.subcontextEnd = subcontextEnd;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("Context=");
      if (this.contextElement != null) {
         sb.append(this.contextElement.getName());
      } else {
         sb.append("null");
      }

      sb.append("   Subcontext=");
      if (this.subcontextEnd != null) {
         sb.append(this.subcontextEnd.getName());
      } else {
         sb.append("null");
      }

      return sb.toString();
   }
}
