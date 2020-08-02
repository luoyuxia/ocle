package ro.ubbcluj.lci.gui.browser.BTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public final class ModelElementOrder implements Order {
   private String[] modelElements = new String[]{"UseCase", "Class", "Object", "Sequence", "Model", "Package", "Subsystem", "Class", "Interface", "AssociationClass", "Actor", "UseCase", "Attribute", "Operation", "Method", "Association", "Generalization", "Dependency", "Permission", "Usage", "Flow", "Abstraction", "ActionState", "ActivityGraph", "CallState", "ClassifierInState", "ObjectFlowState", "Partition", "SubactivityState", "AssociationEndRole", "AssociationRole", "ClassifierRole", "Collaboration", "CollaborationInstanceSet", "Interaction", "InteractionInstanceSet", "Message", "Action", "ActionInstance", "ActionSequence", "Argument", "AttributeLink", "Call", "CallAction", "ComponentInstance", "CreateAction", "DataValue", "DestroyAction", "Exception", "Instance", "NodeInstance", "Object", "UseCaseInstance", "Link", "LinkEnd", "LinkObject", "Reception", "ReturnAction", "SendAction", "Signal", "Stimulus", "SubsystemInstance", "TerminateAction", "UninterpretedAction", "CallEvent", "ChangeEvent", "Event", "Pseudostate", "SimpleState", "State", "StubState", "SynchState", "SubmachineState", "CompositeState", "FinalState", "Guard", "SignalEvent", "StateMachine", "StateVertex", "TimeEvent", "Transition", "Include", "Extend", "ExtensionPoint", "Artifact", "AssociationEnd", "Binding", "Comment", "Component", "Constraint", "EnumerationLiteral", "Node", "Parameter", "PresentationElement", "DataType", "Primitive", "ProgrammingLanguageDataType", "TemplateArgument", "TemplateParameter", "Stereotype", "TagDefinition", "TaggedValue"};
   private Vector priorityList = new Vector();

   public ModelElementOrder() {
      for(int i = 0; i < this.modelElements.length; ++i) {
         this.priorityList.add(this.modelElements[i]);
      }

   }

   public List order(List v) {
      Object[] obj = v.toArray();
      Arrays.sort(obj, this);
      int length = obj.length;
      List ret = new ArrayList(length);

      for(int i = 0; i < length; ++i) {
         ret.add(obj[i]);
      }

      return ret;
   }

   public void add(List vector, Object obj) {
      Vector tmp = new Vector();
      tmp.add(obj);
      this.add(vector, (List)tmp);
   }

   public void add(List dest, List toAdd) {
      for(int i = 0; i < toAdd.size(); ++i) {
         Object eToAdd = toAdd.get(i);
         int idx = this.getIndexFor(dest, eToAdd);
         dest.add(idx, eToAdd);

         while(i + 1 < toAdd.size() && eToAdd.getClass() == toAdd.get(i + 1).getClass()) {
            ++i;
            ++idx;
            dest.add(idx, toAdd.get(i));
         }
      }

   }

   private int getIndexFor(List dest, Object obj) {
      int idx;
      for(idx = 0; idx < dest.size(); ++idx) {
         Object tmp = dest.get(idx);
         if (this.compare(tmp, obj) > 0) {
            return idx;
         }
      }

      return idx;
   }

   public int compare(Object obj1, Object obj2) {
      if (obj1 instanceof GAbstractDiagram && obj2 instanceof GAbstractDiagram) {
         GAbstractDiagram d1 = (GAbstractDiagram)obj1;
         GAbstractDiagram d2 = (GAbstractDiagram)obj2;
         return d1.getName().compareTo(d2.getName());
      } else if (obj1 instanceof GAbstractDiagram) {
         return -1;
      } else if (obj2 instanceof GAbstractDiagram) {
         return 1;
      } else if (obj1 instanceof ModelElement && obj2 instanceof ModelElement) {
         ModelElement me1 = (ModelElement)obj1;
         ModelElement me2 = (ModelElement)obj2;
         int i1 = this.priorityList.indexOf(me1.getMetaclassName());
         int i2 = this.priorityList.indexOf(me2.getMetaclassName());
         if (i1 != i2) {
            return i1 - i2;
         } else if (me1.getName() == null && me2.getName() == null) {
            return 0;
         } else if (me1.getName() == null) {
            return "".compareTo(me2.getName());
         } else {
            return me2.getName() == null ? me1.getName().compareTo("") : me1.getName().compareTo(me2.getName());
         }
      } else {
         return 0;
      }
   }
}
