package ro.ubbcluj.lci.gui.browser.BTree;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.TemplateArgument;
import ro.ubbcluj.lci.uml.foundation.core.TemplateParameter;

public class CustomFilter implements Filter {
   private String[] defAcceptList = new String[]{"Subsystem", "Class", "Interface", "AssociationClass", "Attribute", "Operation", "Method", "Association", "Permission", "Usage", "Flow", "ActionState", "ActivityGraph", "CallState", "ClassifierInState", "ObjectFlowState", "Partition", "SubactivityState", "AssociationEndRole", "AssociationRole", "ClassifierRole", "Collaboration", "CollaborationInstanceSet", "Interaction", "InteractionInstanceSet", "Message", "ActionInstance", "ActionSequence", "Argument", "AttributeLink", "Call", "CallAction", "ComponentInstance", "CreateAction", "DataValue", "DestroyAction", "Exception", "Link", "LinkEnd", "LinkObject", "NodeInstance", "Object", "Reception", "ReturnAction", "SendAction", "Signal", "Stimulus", "SubsystemInstance", "TerminateAction", "UninterpretedAction", "CallEvent", "ChangeEvent", "Pseudostate", "SimpleState", "State", "StubState", "SynchState", "SubmachineState", "CompositeState", "FinalState", "Guard", "SignalEvent", "StateMachine", "TimeEvent", "Transition", "Actor", "UseCase", "UseCaseInstance", "ExtensionPoint", "Enumeration", "TemplateParameter", "Artifact", "AssociationEnd", "Binding", "Component", "EnumerationLiteral", "Node", "Parameter", "TemplateArgument"};
   private String[] defDenyList = new String[]{"TagDefinition", "TaggedValue", "DataType", "ProgrammingLanguageDataType", "Stereotype", "Comment", "Constraint", "Dependency", "Abstraction", "Generalization", "Primitive", "Include", "Extend"};
   private String[] alwaysAccept = new String[]{"Model", "Package", "Namespace"};
   private Vector acceptList;
   private Vector denyList;
   private Vector alwaysAcceptList;

   public CustomFilter() {
      this.acceptList = new Vector(this.defAcceptList.length);
      this.denyList = new Vector(this.defDenyList.length);
      this.alwaysAcceptList = new Vector(this.alwaysAccept.length);
      Arrays.sort(this.defAcceptList);

      int i;
      for(i = 0; i < this.defAcceptList.length; ++i) {
         this.acceptList.add(this.defAcceptList[i]);
      }

      Arrays.sort(this.defDenyList);

      for(i = 0; i < this.defDenyList.length; ++i) {
         this.denyList.add(this.defDenyList[i]);
      }

      Arrays.sort(this.alwaysAccept);

      for(i = 0; i < this.alwaysAccept.length; ++i) {
         this.alwaysAcceptList.add(this.alwaysAccept[i]);
      }

   }

   public boolean accept(String metaclass) {
      return this.acceptList.contains(metaclass) || this.alwaysAcceptList.contains(metaclass);
   }

   public boolean accept(Object object) {
      if (object instanceof ModelElement) {
         return this.accept(((ModelElement)object).getMetaclassName());
      } else if (object instanceof TemplateParameter) {
         return this.accept("TemplateParameter");
      } else if (object instanceof TemplateArgument) {
         return this.accept("TemplateArgument");
      } else {
         return object == null ? false : this.accept(object.getClass().getName());
      }
   }

   public void filter(List vector) {
      Iterator it = vector.iterator();

      while(it.hasNext()) {
         Object me = it.next();
         if (!this.accept(me)) {
            it.remove();
         }
      }

   }

   public Vector getAccepted() {
      return this.acceptList;
   }

   public Vector getAlwaysAccepted() {
      return this.alwaysAcceptList;
   }

   public Vector getDenied() {
      return this.denyList;
   }
}
