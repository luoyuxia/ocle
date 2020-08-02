package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class PrimitiveFunctionImpl extends ModelElementImpl implements PrimitiveFunction {
   protected String theLanguage;
   protected String theEncoding;
   protected Set theOutputSpecList;
   protected Set theInputSpecList;

   public PrimitiveFunctionImpl() {
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

   public Set getCollectionOutputSpecList() {
      return this.theOutputSpecList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theOutputSpecList);
   }

   public Enumeration getOutputSpecList() {
      return Collections.enumeration(this.getCollectionOutputSpecList());
   }

   public void addOutputSpec(ArgumentSpecification arg) {
      if (arg != null) {
         if (this.theOutputSpecList == null) {
            this.theOutputSpecList = new LinkedHashSet();
         }

         this.theOutputSpecList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "outputSpec", 1));
         }
      }

   }

   public void removeOutputSpec(ArgumentSpecification arg) {
      if (this.theOutputSpecList != null && arg != null) {
         this.theOutputSpecList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "outputSpec", 2));
         }
      }

   }

   public Set getCollectionInputSpecList() {
      return this.theInputSpecList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theInputSpecList);
   }

   public Enumeration getInputSpecList() {
      return Collections.enumeration(this.getCollectionInputSpecList());
   }

   public void addInputSpec(ArgumentSpecification arg) {
      if (arg != null) {
         if (this.theInputSpecList == null) {
            this.theInputSpecList = new LinkedHashSet();
         }

         this.theInputSpecList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "inputSpec", 1));
         }
      }

   }

   public void removeInputSpec(ArgumentSpecification arg) {
      if (this.theInputSpecList != null && arg != null) {
         this.theInputSpecList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "inputSpec", 2));
         }
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
