package ro.ubbcluj.lci.uml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public class RepositoryChangeAgent {
   private static Map agentsMap = new HashMap();
   private ArrayList listeners = new ArrayList();
   private boolean fireEvents = true;

   protected RepositoryChangeAgent() {
   }

   public static RepositoryChangeAgent getAgent(Element element) {
      Model model = element.getOwnerModel();
      RepositoryChangeAgent rca = null;
      if (model != null) {
         Object o = agentsMap.get(model);
         if (o == null) {
            rca = new RepositoryChangeAgent();
            agentsMap.put(model, rca);
         } else {
            rca = (RepositoryChangeAgent)o;
         }
      }

      return rca;
   }

   public static void removeAgent(Element element) {
      Model model = element.getOwnerModel();
      agentsMap.remove(model);
   }

   public boolean firesEvents() {
      return this.fireEvents;
   }

   public void fireEvents(boolean b) {
      this.fireEvents = b;
   }

   public void clearListeners() {
      if (this.listeners == null) {
         this.listeners = new ArrayList();
      } else {
         this.listeners.clear();
      }

   }

   public void addChangeListener(RepositoryChangeListener rcl) {
      this.listeners.add(rcl);
   }

   public void fireChangeEvent(ChangeEventObject ceo) {
      if (this.fireEvents) {
         Iterator it = this.listeners.iterator();

         while(it.hasNext()) {
            ((RepositoryChangeListener)it.next()).propertyChanged(ceo);
         }
      }

   }
}
