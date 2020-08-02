package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class EnumerationLiteralImpl extends ModelElementImpl implements EnumerationLiteral {
   protected Enumeration theEnumeration;

   public EnumerationLiteralImpl() {
   }

   public Enumeration getEnumeration() {
      return this.theEnumeration;
   }

   public void setEnumeration(Enumeration arg) {
      if (this.theEnumeration != arg) {
         Enumeration temp = this.theEnumeration;
         this.theEnumeration = null;
         if (temp != null) {
            temp.removeLiteral(this);
         }

         if (arg != null) {
            this.theEnumeration = arg;
            arg.addLiteral(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "enumeration", 0));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpEnumeration = this.getEnumeration();
      if (tmpEnumeration != null) {
         tmpEnumeration.removeLiteral(this);
      }

      super.internalRemove();
   }
}
