package ro.ubbcluj.lci.uml.foundation.dataTypes;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ElementImpl;

public class MappingImpl extends ElementImpl implements Mapping {
   protected String theBody;

   public MappingImpl() {
   }

   public String getBody() {
      return this.theBody;
   }

   public void setBody(String body) {
      this.theBody = body;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "body", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
