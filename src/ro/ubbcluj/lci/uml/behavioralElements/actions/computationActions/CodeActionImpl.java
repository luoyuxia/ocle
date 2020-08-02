package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;

public class CodeActionImpl extends PrimitiveActionImpl implements CodeAction {
   protected String theLanguage;
   protected String theEncoding;

   public CodeActionImpl() {
   }

   public String getLanguage() {
      return this.theLanguage;
   }

   public void setLanguage(String language) {
      this.theLanguage = language;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "language", 0));
      }

   }

   public String getEncoding() {
      return this.theEncoding;
   }

   public void setEncoding(String encoding) {
      this.theEncoding = encoding;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "encoding", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
