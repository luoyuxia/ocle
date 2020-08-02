package ro.ubbcluj.lci.gui.properties;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.ControlFlow;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.DataFlow;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.InputPin;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Pin;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.CollectionAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.FilterAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.IterateAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.MapAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.ReduceAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.Clause;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.ConditionalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.GroupAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.LoopAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.Variable;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.ApplyFunctionAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.ArgumentSpecification;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.CodeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.LiteralValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.MarshalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.NullAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.PrimitiveFunction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.TestIdentityAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.UnmarshalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.HandlerAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.JumpAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.JumpHandler;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.AsynchronousInvocationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.BroadcastSignalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.CallOperationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.ExplicitInvocationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.InvocationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.SendSignalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.SynchronousInvocationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ClearAssociationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.CreateLinkAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.CreateLinkObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.DestroyLinkAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.LinkAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.LinkEndCreationData;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.LinkEndData;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.QualifierValue;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ReadLinkAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ReadLinkObjectEndAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ReadLinkObjectQualifierAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.WriteLinkAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.AddAttributeValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.AttributeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.ClearAttributeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.ReadAttributeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.RemoveAttributeValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.WriteAttributeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.CreateObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.DestroyObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.ReadIsClassifiedObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.ReclassifyObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.CallProcedureAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.ReadExtentAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.ReadSelfAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.StartObjectStateMachineAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.AddVariableValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.ClearVariableAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.ReadVariableAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.RemoveVariableValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.VariableAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.WriteVaribleAction;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ActionState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ActivityGraph;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.CallState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ClassifierInState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ObjectFlowState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.Partition;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.SubactivityState;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Interaction;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.InteractionInstanceSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Message;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ComponentInstance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkObject;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.NodeInstance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Reception;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Signal;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.SignalImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Stimulus;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.SubsystemInstance;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CallEvent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.ChangeEvent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CompositeState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Event;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.FinalState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Guard;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Pseudostate;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SignalEvent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SimpleState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateVertex;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StubState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SubmachineState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SynchState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.TimeEvent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Transition;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Actor;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Extend;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.ExtensionPoint;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Include;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.UseCase;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.UseCaseInstance;
import ro.ubbcluj.lci.uml.foundation.core.Abstraction;
import ro.ubbcluj.lci.uml.foundation.core.Artifact;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Binding;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Comment;
import ro.ubbcluj.lci.uml.foundation.core.Component;
import ro.ubbcluj.lci.uml.foundation.core.Constraint;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.Dependency;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.Flow;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Node;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.uml.foundation.core.Permission;
import ro.ubbcluj.lci.uml.foundation.core.PresentationElement;
import ro.ubbcluj.lci.uml.foundation.core.Primitive;
import ro.ubbcluj.lci.uml.foundation.core.ProgrammingLanguageDataType;
import ro.ubbcluj.lci.uml.foundation.core.Relationship;
import ro.ubbcluj.lci.uml.foundation.core.StructuralFeature;
import ro.ubbcluj.lci.uml.foundation.core.TemplateArgument;
import ro.ubbcluj.lci.uml.foundation.core.Usage;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinition;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.uml.modelManagement.Subsystem;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.ModelUtilities;

public class PropertiesInspector implements IProperties {
   Object element;

   public PropertiesInspector(Object element) {
      this.element = element;
   }

   public Object getElement() {
      return this.element;
   }

   public Collection getProperties() {
      if (this.element instanceof GAbstractDiagram) {
         GAbstractDiagram diag = (GAbstractDiagram)this.element;
         Collection result = new ArrayList();
         if (diag.getName() != null) {
            result.add(new SimpleProperty("Name", diag.getName(), new GCellEditor(new JTextField(diag.getName()))));
         } else {
            result.add(new SimpleProperty("Name", "N/A"));
         }

         return result;
      } else if (!(this.element instanceof Element)) {
         return Collections.EMPTY_LIST;
      } else {
         Object result = null;
         Element umlElement = (Element)this.element;
         String name = umlElement.getMetaclassName();
         name = "getPropertiesFor" + name;

         try {
            Method m = this.getClass().getMethod(name, new java.lang.Class[]{  umlElement.getClass().getInterfaces()[0] });
            result = m.invoke(this, new Object[] { umlElement } );
         } catch (Exception var5) {
            var5.printStackTrace();
         }

         return (Collection)(result == null ? Collections.EMPTY_LIST : (Collection)result);
      }
   }

   private Collection getAllVisibleTypes(Namespace namespace) {
      Iterator ownedElementsIterator = namespace.directGetCollectionOwnedElementList().iterator();
      HashSet allVisibleTypes = new HashSet();

      while(ownedElementsIterator.hasNext()) {
         ModelElement element = (ModelElement)ownedElementsIterator.next();
         if (element.getNamespace().getVisibility() == 3) {
            if (element instanceof Namespace) {
               allVisibleTypes.addAll(this.getAllVisibleTypes((Namespace)element));
            }

            if (element instanceof Classifier) {
               allVisibleTypes.add(element);
            }
         }
      }

      return allVisibleTypes;
   }

   private Classifier[] getAllVisibleTypes(Model model) {
      Iterator ownedElementsIterator = model.directGetCollectionOwnedElementList().iterator();
      LinkedHashSet allVisibleTypes = new LinkedHashSet();

      while(ownedElementsIterator.hasNext()) {
         ModelElement element = (ModelElement)ownedElementsIterator.next();
         if (element instanceof Namespace) {
            allVisibleTypes.addAll(this.getAllVisibleTypes((Namespace)element));
         }

         if (element instanceof Classifier) {
            allVisibleTypes.add(element);
         }
      }

      Classifier[] types = (Classifier[])allVisibleTypes.toArray(new Classifier[0]);
      Arrays.sort(types, new Comparator() {
         public int compare(Object o1, Object o2) {
            Classifier c1 = (Classifier)o1;
            Classifier c2 = (Classifier)o2;
            String name1 = c1.getName() != null ? c1.getName() : "";
            String name2 = c2.getName() != null ? c2.getName() : "";
            return name1.compareTo(name2);
         }

         public boolean equal(Object o) {
            return this.compare(this, o) == 0;
         }
      });
      return types;
   }

   public Collection getPropertiesForAbstraction(Abstraction element) {
      Collection result = this.getPropertiesForDependency(element);
      return result;
   }

   public Collection getPropertiesForAction(Action element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForActionState(ActionState element) {
      Collection result = this.getPropertiesForSimpleState(element);
      return result;
   }

   public Collection getPropertiesForActivityGraph(ActivityGraph element) {
      Collection result = this.getPropertiesForStateMachine(element);
      return result;
   }

   public Collection getPropertiesForActor(Actor element) {
      Collection result = this.getPropertiesForClassifier(element);
      return result;
   }

   public Collection getPropertiesForAddAttributeValueAction(AddAttributeValueAction element) {
      Collection result = this.getPropertiesForWriteAttributeAction(element);
      return result;
   }

   public Collection getPropertiesForAddVariableValueAction(AddVariableValueAction element) {
      Collection result = this.getPropertiesForWriteVaribleAction(element);
      return result;
   }

   public Collection getPropertiesForApplyFunctionAction(ApplyFunctionAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForArgumentSpecification(ArgumentSpecification element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForArtifact(Artifact element) {
      Collection result = this.getPropertiesForClassifier(element);
      return result;
   }

   public Collection getPropertiesForAssociation(Association element) {
      Collection result = this.getPropertiesForGeneralizableElement(element);
      ComposedProperty cp = new ComposedProperty("Connections");
      Collection col = element.getCollectionConnectionList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("Connection", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForAssociationClass(AssociationClass element) {
      Collection result = this.getPropertiesForClass(element);
      ComposedProperty cp = new ComposedProperty("Connections");
      Collection col = element.getCollectionConnectionList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("Connection", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForAssociationEnd(AssociationEnd element) {
      Collection result = this.getPropertiesForModelElement(element);
      if (element.getAssociation() != null) {
         result.add(new SimpleProperty("Association", element.getAssociation(), new GCellEditor(new JumpButton(element.getAssociation()))));
      } else {
         result.add(new SimpleProperty("Association", "N/A"));
      }

      if (element.getParticipant() != null) {
         result.add(new SimpleProperty("Participant", element.getParticipant(), new GCellEditor(new JumpButton(element.getParticipant()))));
      } else {
         result.add(new SimpleProperty("Participant", "N/A"));
      }

      JComboBox combo = new JComboBox();
      ComboItem[] items = new ComboItem[5];
      combo.addItem(items[3] = new ComboItem(new Integer(3), "public"));
      combo.addItem(items[2] = new ComboItem(new Integer(2), "protected"));
      combo.addItem(items[0] = new ComboItem(new Integer(0), "private"));
      combo.addItem(items[1] = new ComboItem(new Integer(1), "package"));
      combo.setSelectedItem(items[element.getVisibility()]);
      result.add(new SimpleProperty("Visibility", items[element.getVisibility()], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      int idx = element.isNavigable() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Navigable", items[idx], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[4];
      combo.addItem(items[0] = new ComboItem("1", "1"));
      combo.addItem(items[1] = new ComboItem("0..1", "0..1"));
      combo.addItem(items[2] = new ComboItem("1..*", "1..*"));
      combo.addItem(items[3] = new ComboItem("0..*", "0..*"));
      String mul = "";
      if (element.getMultiplicity() != null && element.getMultiplicity().getRangeList().hasMoreElements()) {
         MultiplicityRange mr = (MultiplicityRange)element.getMultiplicity().getRangeList().nextElement();
         String low = Integer.toString(mr.getLower());
         String high = mr.getUpper().equals(new BigInteger("-1")) ? "*" : mr.getUpper().toString();
         combo.setEditable(true);
         mul = low.equals(high) ? low : low + ".." + high;
      }

      combo.setSelectedItem(mul);
      result.add(new SimpleProperty("Multiplicity", mul, new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[4];
      combo.addItem(items[1] = new ComboItem(new Integer(1), "none"));
      combo.addItem(items[2] = new ComboItem(new Integer(2), "aggregate"));
      combo.addItem(items[0] = new ComboItem(new Integer(0), "composite"));
      combo.setSelectedItem(items[element.getAggregation()]);
      result.add(new SimpleProperty("Aggregation", items[element.getAggregation()], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[4];
      combo.addItem(items[1] = new ComboItem(new Integer(1), "addOnly"));
      combo.addItem(items[2] = new ComboItem(new Integer(2), "changeable"));
      combo.addItem(items[0] = new ComboItem(new Integer(0), "frozen"));
      combo.setSelectedItem(items[element.getChangeability()]);
      result.add(new SimpleProperty("Changeability", items[element.getChangeability()], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[0] = new ComboItem(new Integer(0), "classifier"));
      combo.addItem(items[1] = new ComboItem(new Integer(1), "instance"));
      combo.setSelectedItem(items[element.getTargetScope()]);
      result.add(new SimpleProperty("TargetScope", items[element.getTargetScope()], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[0] = new ComboItem(new Integer(0), "ordered"));
      combo.addItem(items[1] = new ComboItem(new Integer(1), "unordered"));
      combo.setSelectedItem(items[element.getOrdering()]);
      result.add(new SimpleProperty("Ordering", items[element.getOrdering()], new GCellEditor(combo)));
      ComposedProperty cp = new ComposedProperty("Qualifiers");
      Collection col = element.getCollectionQualifierList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("Qualifier", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForAssociationEndRole(AssociationEndRole element) {
      Collection result = this.getPropertiesForAssociationEnd(element);
      return result;
   }

   public Collection getPropertiesForAssociationRole(AssociationRole element) {
      Collection result = this.getPropertiesForAssociation(element);
      return result;
   }

   public Collection getPropertiesForAsynchronousInvocationAction(AsynchronousInvocationAction element) {
      Collection result = this.getPropertiesForInvocationAction(element);
      return result;
   }

   public Collection getPropertiesForAttribute(Attribute element) {
      Collection result = this.getPropertiesForStructuralFeature(element);
      if (element.getInitialValue() != null) {
         String expr = null;
         if (element.getInitialValue().getBody() != null) {
            expr = element.getInitialValue().getBody();
         } else {
            expr = "undefined";
         }

         result.add(new SimpleProperty("InitialValue", expr, new GCellEditor(new JTextField(expr))));
      }

      return result;
   }

   public Collection getPropertiesForAttributeAction(AttributeAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForAttributeLink(AttributeLink element) {
      Collection result = this.getPropertiesForModelElement(element);
      if (element.getInstance() != null) {
         result.add(new SimpleProperty("Instance", element.getInstance(), new GCellEditor(new JumpButton(element.getInstance()))));
      } else {
         result.add(new SimpleProperty("Instance", "N/A"));
      }

      if (element.getLinkEnd() != null) {
         result.add(new SimpleProperty("LinkEnd", element.getLinkEnd(), new GCellEditor(new JumpButton(element.getLinkEnd()))));
      }

      if (element.getAttribute() != null) {
         result.add(new SimpleProperty("Attribute", element.getAttribute(), new GCellEditor(new JumpButton(element.getAttribute()))));
      } else {
         result.add(new SimpleProperty("Attribute", "N/A"));
      }

      Instance value = element.getValue();
      Classifier type = element.getAttribute().getType();
      Object[] items;
      if (type instanceof Class) {
         Object[] allObjs = ModelUtilities.getAllObjectsOfKind(type);
         items = new Object[allObjs.length + 1];
         System.arraycopy(allObjs, 0, items, 1, allObjs.length);
         items[0] = "<undefined>";
         JComboBox combo = new JComboBox(items);
         if (element.getValue() != null) {
            combo.setSelectedItem(element.getValue());
         } else {
            combo.setSelectedItem("<undefined>");
         }

         result.add(new SimpleProperty("Value", value, new GCellEditor(combo)));
      } else if (type instanceof Enumeration) {
         Collection literals = ((Enumeration)type).getCollectionLiteralList();
         items = new Object[literals.size() + 1];
         items[0] = "<undefined>";
         Iterator literalsIt = literals.iterator();

         for(int i = 1; literalsIt.hasNext(); ++i) {
            items[i] = ((EnumerationLiteral)literalsIt.next()).getName();
         }

         JComboBox combo = new JComboBox(items);
         if (element.getValue() != null) {
            combo.setSelectedItem(element.getValue().getName());
         } else {
            combo.setSelectedItem("<undefined>");
         }

         result.add(new SimpleProperty("Value", value, new GCellEditor(combo)));
      } else if (type instanceof DataType) {
         if (type.getName().equals("Boolean")) {
            JComboBox combo = new JComboBox(new Object[]{"true", "false", "<undefined>"});
            if (element.getValue() != null) {
               combo.setSelectedItem(element.getValue().getName());
            } else {
               combo.setSelectedItem("<undefined>");
            }

            result.add(new SimpleProperty("Value", value, new GCellEditor(combo)));
         } else if (value != null) {
            result.add(new SimpleProperty("Value", value, new GCellEditor(new JTextField(value.getName()))));
         } else {
            result.add(new SimpleProperty("Value", "N/A"));
         }
      }

      return result;
   }

   public Collection getPropertiesForBehavioralFeature(BehavioralFeature element) {
      Collection result = this.getPropertiesForFeature(element);
      JComboBox combo = new JComboBox();
      ComboItem[] items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      int idx = element.isQuery() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Query", items[idx], new GCellEditor(combo)));
      ComposedProperty cp = new ComposedProperty("Parameters");
      Collection col = element.getCollectionParameterList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("Parameter", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      Collection signals = ModelFactory.allInstances(ModelFactory.currentModel, (new SignalImpl()).getClass());
      combo = new JComboBox();
      combo.addItem(new ComboItem2((Object)null, "Add..."));
       it = signals.iterator();

      Object current;
      while(it.hasNext()) {
         current = it.next();
         if (!element.getCollectionRaisedSignalList().contains(current)) {
            combo.addItem(new ComboItem2(current, current.toString()));
         }
      }

      combo.setSelectedIndex(0);
      cp = new ComposedProperty("RaisedSignals");
      cp.addProperty(new SimpleProperty("RaisedSignal", "Add...", new GCellEditor(combo)));
       col = element.getCollectionRaisedSignalList();
      it = col.iterator();

      while(it.hasNext()) {
         current = it.next();
         cp.addProperty(new SimpleProperty("RaisedSignal", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForBinding(Binding element) {
      Collection result = this.getPropertiesForDependency(element);
      return result;
   }

   public Collection getPropertiesForBroadcastSignalAction(BroadcastSignalAction element) {
      Collection result = this.getPropertiesForExplicitInvocationAction(element);
      return result;
   }

   public Collection getPropertiesForCallEvent(CallEvent element) {
      Collection result = this.getPropertiesForEvent(element);
      return result;
   }

   public Collection getPropertiesForCallOperationAction(CallOperationAction element) {
      Collection result = this.getPropertiesForExplicitInvocationAction(element);
      return result;
   }

   public Collection getPropertiesForCallProcedureAction(CallProcedureAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForCallState(CallState element) {
      Collection result = this.getPropertiesForActionState(element);
      return result;
   }

   public Collection getPropertiesForChangeEvent(ChangeEvent element) {
      Collection result = this.getPropertiesForEvent(element);
      return result;
   }

   public Collection getPropertiesForClass(Class element) {
      Collection result = this.getPropertiesForClassifier(element);
      JComboBox combo = new JComboBox();
      ComboItem[] items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      int idx = element.isActive() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Active", items[idx], new GCellEditor(combo)));
      return result;
   }

   public Collection getPropertiesForClassifier(Classifier element) {
      Collection result = this.getPropertiesForNamespace(element);
      JComboBox combo = new JComboBox();
      ComboItem[] items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      int idx = element.isRoot() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Root", items[idx], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      idx = element.isLeaf() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Leaf", items[idx], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      idx = element.isAbstract() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      Property sp = new SimpleProperty("Abstract", items[idx], new GCellEditor(combo));
      if (element instanceof Interface) {
         sp = new SimpleProperty("Abstract", items[idx]);
      }

      result.add(sp);
      ComposedProperty cp = new ComposedProperty("Features");
      Collection col = element.getCollectionFeatureList();
      Object[] elems = col.toArray(new Object[0]);
      Arrays.sort(elems, new Comparator() {
         public int compare(Object o1, Object o2) {
            String s1 = ((Element)o1).getMetaclassName() + o1;
            String s2 = ((Element)o2).getMetaclassName() + o2;
            return s1.compareTo(s2);
         }
      });

      for(int i = 0; i < elems.length; ++i) {
         cp.addProperty(new SimpleProperty(((Element)elems[i]).getMetaclassName(), elems[i], new GCellEditor(new JumpButton(elems[i]))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForClassifierInState(ClassifierInState element) {
      Collection result = this.getPropertiesForClassifier(element);
      return result;
   }

   public Collection getPropertiesForClassifierRole(ClassifierRole element) {
      Collection result = this.getPropertiesForClassifier(element);
      return result;
   }

   public Collection getPropertiesForClause(Clause element) {
      Collection result = this.getPropertiesForElement(element);
      return result;
   }

   public Collection getPropertiesForClearAssociationAction(ClearAssociationAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForClearAttributeAction(ClearAttributeAction element) {
      Collection result = this.getPropertiesForAttributeAction(element);
      return result;
   }

   public Collection getPropertiesForClearVariableAction(ClearVariableAction element) {
      Collection result = this.getPropertiesForVariableAction(element);
      return result;
   }

   public Collection getPropertiesForCodeAction(CodeAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForCollaboration(Collaboration element) {
      Collection result = this.getPropertiesForGeneralizableElement(element);
      return result;
   }

   public Collection getPropertiesForCollaborationInstanceSet(CollaborationInstanceSet element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForCollectionAction(CollectionAction element) {
      Collection result = this.getPropertiesForAction(element);
      return result;
   }

   public Collection getPropertiesForComment(Comment element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForComponent(Component element) {
      Collection result = this.getPropertiesForClassifier(element);
      return result;
   }

   public Collection getPropertiesForComponentInstance(ComponentInstance element) {
      Collection result = this.getPropertiesForInstance(element);
      return result;
   }

   public Collection getPropertiesForCompositeState(CompositeState element) {
      Collection result = this.getPropertiesForState(element);
      return result;
   }

   public Collection getPropertiesForConditionalAction(ConditionalAction element) {
      Collection result = this.getPropertiesForAction(element);
      return result;
   }

   public Collection getPropertiesForConstraint(Constraint element) {
      Collection result = this.getPropertiesForModelElement(element);
      if (element.getBody() != null && element.getBody().getBody() != null) {
         String expr = element.getBody().getBody();
         result.add(new SimpleProperty("Body", expr, new GCellEditor(new JTextField(expr))));
      }

      return result;
   }

   public Collection getPropertiesForControlFlow(ControlFlow element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForCreateLinkAction(CreateLinkAction element) {
      Collection result = this.getPropertiesForWriteLinkAction(element);
      return result;
   }

   public Collection getPropertiesForCreateLinkObjectAction(CreateLinkObjectAction element) {
      Collection result = this.getPropertiesForCreateLinkAction(element);
      return result;
   }

   public Collection getPropertiesForCreateObjectAction(CreateObjectAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForDataFlow(DataFlow element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForDataType(DataType element) {
      Collection result = this.getPropertiesForClassifier(element);
      return result;
   }

   public Collection getPropertiesForDataValue(DataValue element) {
      Collection result = this.getPropertiesForInstance(element);
      return result;
   }

   public Collection getPropertiesForDependency(Dependency element) {
      Collection result = this.getPropertiesForRelationship(element);
      ComposedProperty cp = new ComposedProperty("Clients");
      Collection col = element.getCollectionClientList();
      Iterator it = col.iterator();

      Object current;
      while(it.hasNext()) {
         current = it.next();
         cp.addProperty(new SimpleProperty("Client", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      cp = new ComposedProperty("Suppliers");
      col = element.getCollectionSupplierList();
      it = col.iterator();

      while(it.hasNext()) {
         current = it.next();
         cp.addProperty(new SimpleProperty("Supplier", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForDestroyLinkAction(DestroyLinkAction element) {
      Collection result = this.getPropertiesForWriteLinkAction(element);
      return result;
   }

   public Collection getPropertiesForDestroyObjectAction(DestroyObjectAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForElement(Element element) {
      Collection result = new ArrayList();
      return result;
   }

   public Collection getPropertiesForEnumeration(Enumeration element) {
      Collection result = this.getPropertiesForDataType(element);
      ComposedProperty cp = new ComposedProperty("Literals");
      Collection col = element.getCollectionLiteralList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("Literal", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForEnumerationLiteral(EnumerationLiteral element) {
      Collection result = this.getPropertiesForModelElement(element);
      if (element.getEnumeration() != null) {
         result.add(new SimpleProperty("Enumeration", element.getEnumeration(), new GCellEditor(new JumpButton(element.getEnumeration()))));
      } else {
         result.add(new SimpleProperty("Enumeration", "N/A"));
      }

      return result;
   }

   public Collection getPropertiesForEvent(Event element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForException(ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Exception element) {
      Collection result = this.getPropertiesForSignal(element);
      return result;
   }

   public Collection getPropertiesForExplicitInvocationAction(ExplicitInvocationAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForExtend(Extend element) {
      Collection result = this.getPropertiesForRelationship(element);
      if (element.getBase() != null) {
         result.add(new SimpleProperty("Base", element.getBase(), new GCellEditor(new JumpButton(element.getBase()))));
      } else {
         result.add(new SimpleProperty("Base", "N/A"));
      }

      if (element.getExtension() != null) {
         result.add(new SimpleProperty("Extension", element.getExtension(), new GCellEditor(new JumpButton(element.getExtension()))));
      } else {
         result.add(new SimpleProperty("Extension", "N/A"));
      }

      ComposedProperty cp = new ComposedProperty("ExtensionPoints");
      Collection col = element.getCollectionExtensionPointList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("ExtensionPoint", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForExtensionPoint(ExtensionPoint element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForFeature(Feature element) {
      Collection result = this.getPropertiesForModelElement(element);
      if (element.getOwner() != null) {
         result.add(new SimpleProperty("Owner", element.getOwner(), new GCellEditor(new JumpButton(element.getOwner()))));
      }

      JComboBox combo = new JComboBox();
      ComboItem[] items = new ComboItem[5];
      combo.addItem(items[3] = new ComboItem(new Integer(3), "public"));
      combo.addItem(items[2] = new ComboItem(new Integer(2), "protected"));
      combo.addItem(items[0] = new ComboItem(new Integer(0), "private"));
      combo.addItem(items[1] = new ComboItem(new Integer(1), "package"));
      combo.setSelectedItem(items[element.getVisibility()]);
      result.add(new SimpleProperty("Visibility", items[element.getVisibility()], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[0] = new ComboItem(new Integer(0), "classifier"));
      combo.addItem(items[1] = new ComboItem(new Integer(1), "instance"));
      combo.setSelectedItem(items[element.getOwnerScope()]);
      result.add(new SimpleProperty("OwnerScope", items[element.getOwnerScope()], new GCellEditor(combo)));
      return result;
   }

   public Collection getPropertiesForFilterAction(FilterAction element) {
      Collection result = this.getPropertiesForCollectionAction(element);
      return result;
   }

   public Collection getPropertiesForFinalState(FinalState element) {
      Collection result = this.getPropertiesForState(element);
      return result;
   }

   public Collection getPropertiesForFlow(Flow element) {
      Collection result = this.getPropertiesForRelationship(element);
      return result;
   }

   public Collection getPropertiesForGeneralizableElement(GeneralizableElement element) {
      Collection result = this.getPropertiesForModelElement(element);
      JComboBox combo = new JComboBox();
      ComboItem[] items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      int idx = element.isRoot() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Root", items[idx], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      idx = element.isLeaf() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Leaf", items[idx], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      idx = element.isAbstract() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Abstract", items[idx], new GCellEditor(combo)));
      return result;
   }

   public Collection getPropertiesForGeneralization(Generalization element) {
      Collection result = this.getPropertiesForRelationship(element);
      if (element.getParent() != null) {
         result.add(new SimpleProperty("Parent", element.getParent(), new GCellEditor(new JumpButton(element.getParent()))));
      } else {
         result.add(new SimpleProperty("Parent", "N/A"));
      }

      if (element.getChild() != null) {
         result.add(new SimpleProperty("Child", element.getChild(), new GCellEditor(new JumpButton(element.getChild()))));
      } else {
         result.add(new SimpleProperty("Child", "N/A"));
      }

      result.add(new SimpleProperty("Discriminator", element.getDiscriminator(), new GCellEditor(new JTextField(element.getDiscriminator()))));
      return result;
   }

   public Collection getPropertiesForGroupAction(GroupAction element) {
      Collection result = this.getPropertiesForAction(element);
      return result;
   }

   public Collection getPropertiesForGuard(Guard element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForHandlerAction(HandlerAction element) {
      Collection result = this.getPropertiesForAction(element);
      return result;
   }

   public Collection getPropertiesForInclude(Include element) {
      Collection result = this.getPropertiesForRelationship(element);
      if (element.getBase() != null) {
         result.add(new SimpleProperty("Base", element.getBase(), new GCellEditor(new JumpButton(element.getBase()))));
      } else {
         result.add(new SimpleProperty("Base", "N/A"));
      }

      if (element.getAddition() != null) {
         result.add(new SimpleProperty("Addition", element.getAddition(), new GCellEditor(new JumpButton(element.getAddition()))));
      } else {
         result.add(new SimpleProperty("Addition", "N/A"));
      }

      return result;
   }

   public Collection getPropertiesForInputPin(InputPin element) {
      Collection result = this.getPropertiesForPin(element);
      return result;
   }

   public Collection getPropertiesForInstance(Instance element) {
      Collection result = this.getPropertiesForModelElement(element);
      if (element.getOwner() != null) {
         result.add(new SimpleProperty("Owner", element.getOwner(), new GCellEditor(new JumpButton(element.getOwner()))));
      }

      ComposedProperty cp = new ComposedProperty("Classifiers");
      Collection col = element.getCollectionClassifierList();
      Iterator it = col.iterator();

      Object current;
      while(it.hasNext()) {
         current = it.next();
         cp.addProperty(new SimpleProperty("Classifier", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      cp = new ComposedProperty("OwnedInstances");
      col = element.getCollectionOwnedInstanceList();
      it = col.iterator();

      while(it.hasNext()) {
         current = it.next();
         cp.addProperty(new SimpleProperty("OwnedInstance", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      cp = new ComposedProperty("Slots");
      col = element.getCollectionSlotList();
      it = col.iterator();

      while(it.hasNext()) {
         current = it.next();
         cp.addProperty(new SimpleProperty("Slot", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForInteraction(Interaction element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForInteractionInstanceSet(InteractionInstanceSet element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForInterface(Interface element) {
      Collection result = this.getPropertiesForClassifier(element);
      return result;
   }

   public Collection getPropertiesForInvocationAction(InvocationAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForIterateAction(IterateAction element) {
      Collection result = this.getPropertiesForCollectionAction(element);
      return result;
   }

   public Collection getPropertiesForJumpAction(JumpAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForJumpHandler(JumpHandler element) {
      Collection result = this.getPropertiesForElement(element);
      return result;
   }

   public Collection getPropertiesForLink(Link element) {
      Collection result = this.getPropertiesForModelElement(element);
      if (element.getAssociation() != null) {
         result.add(new SimpleProperty("Association", element.getAssociation(), new GCellEditor(new JumpButton(element.getAssociation()))));
      } else {
         result.add(new SimpleProperty("Association", "N/A"));
      }

      ComposedProperty cp = new ComposedProperty("Connections");
      Collection col = element.getCollectionConnectionList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("Connection", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForLinkAction(LinkAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForLinkEnd(LinkEnd element) {
      Collection result = this.getPropertiesForModelElement(element);
      if (element.getAssociationEnd() != null) {
         result.add(new SimpleProperty("AssociationEnd", element.getAssociationEnd(), new GCellEditor(new JumpButton(element.getAssociationEnd()))));
      } else {
         result.add(new SimpleProperty("AssociationEnd", "N/A"));
      }

      if (element.getLink() != null) {
         result.add(new SimpleProperty("Link", element.getLink(), new GCellEditor(new JumpButton(element.getLink()))));
      } else {
         result.add(new SimpleProperty("Link", "N/A"));
      }

      if (element.getInstance() != null) {
         result.add(new SimpleProperty("Instance", element.getInstance(), new GCellEditor(new JumpButton(element.getInstance()))));
      } else {
         result.add(new SimpleProperty("Instance", "N/A"));
      }

      ComposedProperty cp = new ComposedProperty("QualifierValues");
      Collection col = element.getCollectionQualifierValueList();

      AttributeLink current;
      DataValue value;
      for(Iterator it = col.iterator(); it.hasNext(); cp.addProperty(new SimpleProperty(current.getName(), value, new GCellEditor(new JumpButton(value))))) {
         current = (AttributeLink)it.next();
         value = (DataValue)current.getValue();
         if (value.getName() == null) {
            value.setName("");
         }
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForLinkEndCreationData(LinkEndCreationData element) {
      Collection result = this.getPropertiesForLinkEndData(element);
      return result;
   }

   public Collection getPropertiesForLinkEndData(LinkEndData element) {
      Collection result = this.getPropertiesForElement(element);
      return result;
   }

   public Collection getPropertiesForLinkObject(LinkObject element) {
      Collection result = this.getPropertiesForLink(element);
      return result;
   }

   public Collection getPropertiesForLiteralValueAction(LiteralValueAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForLoopAction(LoopAction element) {
      Collection result = this.getPropertiesForAction(element);
      return result;
   }

   public Collection getPropertiesForMapAction(MapAction element) {
      Collection result = this.getPropertiesForCollectionAction(element);
      return result;
   }

   public Collection getPropertiesForMarshalAction(MarshalAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForMessage(Message element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForMethod(ro.ubbcluj.lci.uml.foundation.core.Method element) {
      Collection result = this.getPropertiesForBehavioralFeature(element);
      if (element.getSpecification() != null) {
         result.add(new SimpleProperty("Specification", element.getSpecification(), new GCellEditor(new JumpButton(element.getSpecification()))));
      } else {
         result.add(new SimpleProperty("Specification", "N/A"));
      }

      return result;
   }

   public Collection getPropertiesForModel(Model element) {
      Collection result = this.getPropertiesForPackage(element);
      return result;
   }

   public Collection getPropertiesForModelElement(ModelElement element) {
      Collection result = this.getPropertiesForElement(element);
      result.add(new SimpleProperty("Name", element.getName(), new GCellEditor(new JTextField(element.getName()))));
      if (element.directGetNamespace() != null) {
         result.add(new SimpleProperty("Namespace", element.directGetNamespace(), new GCellEditor(new JumpButton(element.directGetNamespace()))));
         JComboBox combo = new JComboBox();
         ComboItem[] items = new ComboItem[5];
         combo.addItem(items[3] = new ComboItem(new Integer(3), "public"));
         combo.addItem(items[2] = new ComboItem(new Integer(2), "protected"));
         combo.addItem(items[0] = new ComboItem(new Integer(0), "private"));
         combo.addItem(items[1] = new ComboItem(new Integer(1), "package"));
         int visibility = element.getNamespace().getVisibility();
         combo.setSelectedItem(items[visibility]);
         result.add(new SimpleProperty("Visibility", items[visibility], new GCellEditor(combo)));
      }

      ComposedProperty cp = new ComposedProperty("Stereotypes");
      Collection col = element.getCollectionStereotypeList();
      Object[] elems = col.toArray(new Object[0]);
      Arrays.sort(elems, new Comparator() {
         public int compare(Object o1, Object o2) {
            String s1 = ((Element)o1).getMetaclassName() + o1;
            String s2 = ((Element)o2).getMetaclassName() + o2;
            return s1.compareTo(s2);
         }
      });

      for(int i = 0; i < elems.length; ++i) {
         cp.addProperty(new SimpleProperty("Stereotype", elems[i], new GCellEditor(new JumpButton(elems[i]))));
      }

      result.add(cp);
      ComposedProperty taggedValues = new ComposedProperty("Tagged Values");
      Collection taggedValuesList = element.getCollectionTaggedValueList();
      Object[] taggedValuesElems = taggedValuesList.toArray(new Object[0]);
      Arrays.sort(taggedValuesElems, new Comparator() {
         public int compare(Object o1, Object o2) {
            String s1 = ((Element)o1).getMetaclassName() + o1;
            String s2 = ((Element)o2).getMetaclassName() + o2;
            return s1.compareTo(s2);
         }
      });

      for(int i = 0; i < taggedValuesElems.length; ++i) {
         taggedValues.addProperty(new SimpleProperty("TaggedValue", taggedValuesElems[i], new GCellEditor(new JumpButton(taggedValuesElems[i]))));
      }

      result.add(taggedValues);
      cp = new ComposedProperty("Constraints");
      col = element.getCollectionConstraintList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("Constraint", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForNamespace(Namespace element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForNode(Node element) {
      Collection result = this.getPropertiesForClassifier(element);
      return result;
   }

   public Collection getPropertiesForNodeInstance(NodeInstance element) {
      Collection result = this.getPropertiesForInstance(element);
      return result;
   }

   public Collection getPropertiesForNullAction(NullAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForObject(ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object element) {
      Collection result = this.getPropertiesForInstance(element);
      return result;
   }

   public Collection getPropertiesForObjectFlowState(ObjectFlowState element) {
      Collection result = this.getPropertiesForSimpleState(element);
      return result;
   }

   public Collection getPropertiesForOperation(Operation element) {
      Collection result = this.getPropertiesForBehavioralFeature(element);
      JComboBox combo = new JComboBox();
      ComboItem[] items = new ComboItem[4];
      combo.addItem(items[0] = new ComboItem(new Integer(0), "concurrent"));
      combo.addItem(items[2] = new ComboItem(new Integer(2), "guarded"));
      combo.addItem(items[1] = new ComboItem(new Integer(1), "sequential"));
      combo.setSelectedItem(items[element.getConcurrency()]);
      result.add(new SimpleProperty("Concurrency", items[element.getConcurrency()], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      int idx = element.isRoot() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Root", items[idx], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      idx = element.isLeaf() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Leaf", items[idx], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      idx = element.isAbstract() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Abstract", items[idx], new GCellEditor(combo)));
      result.add(new SimpleProperty("Specification", element.getSpecification(), new GCellEditor(new JTextField(element.getSpecification()))));
      return result;
   }

   public Collection getPropertiesForOutputPin(OutputPin element) {
      Collection result = this.getPropertiesForPin(element);
      return result;
   }

   public Collection getPropertiesForPackage(Package element) {
      Collection result = this.getPropertiesForNamespace(element);
      return result;
   }

   public Collection getPropertiesForParameter(Parameter element) {
      Collection result = this.getPropertiesForModelElement(element);
      if (element.getBehavioralFeature() != null) {
         result.add(new SimpleProperty("BehavioralFeature", element.getBehavioralFeature(), new GCellEditor(new JumpButton(element.getBehavioralFeature()))));
      }

      if (element.getType() == null) {
         element.setType(ModelFactory.getUndefinedType());
      }

      Model model = element.getOwnerModel() != null ? element.getOwnerModel() : GRepository.getInstance().getUsermodel().getModel();
      Classifier[] types = this.getAllVisibleTypes(model);
      JComboBox combo = new JComboBox();
      int selIdx = -1;

      for(int i = 0; i < types.length; ++i) {
         combo.addItem(new ComboItem(types[i], types[i].getName()));
         if (element.getType() == types[i]) {
            selIdx = i;
         }
      }

      if (selIdx >= 0) {
         combo.setSelectedIndex(selIdx);
      }

      JPanel panel = new JPanel(new BorderLayout());
      panel.add(combo, "Center");
      JButton but = new JumpButton(element.getType()) {
         public String getText() {
            return "...";
         }
      };
      Insets is = but.getMargin();
      is.left = 0;
      is.right = 0;
      but.setMargin(is);
      panel.add(but, "East");
      result.add(new SimpleProperty("Type", element.getType(), new GCellEditor(panel)));
      combo = new JComboBox();
      ComboItem[] items = new ComboItem[5];
      combo.addItem(items[2] = new ComboItem(new Integer(2), "in"));
      combo.addItem(items[1] = new ComboItem(new Integer(1), "out"));
      combo.addItem(items[0] = new ComboItem(new Integer(0), "inout"));
      combo.addItem(items[3] = new ComboItem(new Integer(3), "return"));
      combo.setSelectedItem(items[element.getKind()]);
      result.add(new SimpleProperty("Kind", items[element.getKind()], new GCellEditor(combo)));
      if (element.getDefaultValue() != null && element.getDefaultValue().getBody() != null) {
         String expr = element.getDefaultValue().getBody();
         result.add(new SimpleProperty("DefaultValue", expr, new GCellEditor(new JTextField(expr))));
      }

      return result;
   }

   public Collection getPropertiesForPartition(Partition element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForPermission(Permission element) {
      Collection result = this.getPropertiesForDependency(element);
      return result;
   }

   public Collection getPropertiesForPin(Pin element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForPresentationElement(PresentationElement element) {
      Collection result = this.getPropertiesForElement(element);
      return result;
   }

   public Collection getPropertiesForPrimitive(Primitive element) {
      Collection result = this.getPropertiesForDataType(element);
      return result;
   }

   public Collection getPropertiesForPrimitiveAction(PrimitiveAction element) {
      Collection result = this.getPropertiesForAction(element);
      return result;
   }

   public Collection getPropertiesForPrimitiveFunction(PrimitiveFunction element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForProcedure(Procedure element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForProgrammingLanguageDataType(ProgrammingLanguageDataType element) {
      Collection result = this.getPropertiesForDataType(element);
      return result;
   }

   public Collection getPropertiesForPseudostate(Pseudostate element) {
      Collection result = this.getPropertiesForStateVertex(element);
      return result;
   }

   public Collection getPropertiesForQualifierValue(QualifierValue element) {
      Collection result = this.getPropertiesForElement(element);
      return result;
   }

   public Collection getPropertiesForReadAttribute(ReadAttributeAction element) {
      Collection result = this.getPropertiesForAttributeAction(element);
      return result;
   }

   public Collection getPropertiesForReadExtentAction(ReadExtentAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForReadIsClassifiedObjectAction(ReadIsClassifiedObjectAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForReadLinkAction(ReadLinkAction element) {
      Collection result = this.getPropertiesForLinkAction(element);
      return result;
   }

   public Collection getPropertiesForReadLinkObjectEndAction(ReadLinkObjectEndAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForReadLinkObjectQualifierAction(ReadLinkObjectQualifierAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForReadSelfAction(ReadSelfAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForReadVariableAction(ReadVariableAction element) {
      Collection result = this.getPropertiesForVariableAction(element);
      return result;
   }

   public Collection getPropertiesForReception(Reception element) {
      Collection result = this.getPropertiesForBehavioralFeature(element);
      return result;
   }

   public Collection getPropertiesForReclassifyObjectAction(ReclassifyObjectAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForReduceAction(ReduceAction element) {
      Collection result = this.getPropertiesForCollectionAction(element);
      return result;
   }

   public Collection getPropertiesForRelationship(Relationship element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForRemoveAttributeValueAction(RemoveAttributeValueAction element) {
      Collection result = this.getPropertiesForWriteAttributeAction(element);
      return result;
   }

   public Collection getPropertiesForRemoveVariableValueAction(RemoveVariableValueAction element) {
      Collection result = this.getPropertiesForWriteVaribleAction(element);
      return result;
   }

   public Collection getPropertiesForSendSignalAction(SendSignalAction element) {
      Collection result = this.getPropertiesForExplicitInvocationAction(element);
      return result;
   }

   public Collection getPropertiesForSignal(Signal element) {
      Collection result = this.getPropertiesForClassifier(element);
      ComposedProperty cp = new ComposedProperty("Contexts");
      Collection col = element.getCollectionContextList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("Context", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForSignalEvent(SignalEvent element) {
      Collection result = this.getPropertiesForEvent(element);
      return result;
   }

   public Collection getPropertiesForSimpleState(SimpleState element) {
      Collection result = this.getPropertiesForState(element);
      return result;
   }

   public Collection getPropertiesForStartObjectStateMachineAction(StartObjectStateMachineAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForState(State element) {
      Collection result = this.getPropertiesForStateVertex(element);
      return result;
   }

   public Collection getPropertiesForStateMachine(StateMachine element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForStateVertex(StateVertex element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForStereotype(Stereotype element) {
      Collection result = this.getPropertiesForGeneralizableElement(element);
      return result;
   }

   public Collection getPropertiesForStimulus(Stimulus element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForStructuralFeature(StructuralFeature element) {
      Collection result = this.getPropertiesForFeature(element);
      if (element.getType() == null) {
         element.setType(ModelFactory.getUndefinedType());
      }

      Model model = element.getOwnerModel() != null ? element.getOwnerModel() : GRepository.getInstance().getUsermodel().getModel();
      Classifier[] types = this.getAllVisibleTypes(model);
      JComboBox combo = new JComboBox();
      int selIdx = -1;

      for(int i = 0; i < types.length; ++i) {
         combo.addItem(new ComboItem(types[i], types[i].getName()));
         if (element.getType() == types[i]) {
            selIdx = i;
         }
      }

      if (selIdx >= 0) {
         combo.setSelectedIndex(selIdx);
      }

      JPanel panel = new JPanel(new BorderLayout());
      panel.add(combo, "Center");
      JButton but = new JumpButton(element.getType()) {
         public String getText() {
            return "...";
         }
      };
      Insets is = but.getMargin();
      is.left = 0;
      is.right = 0;
      but.setMargin(is);
      panel.add(but, "East");
      result.add(new SimpleProperty("Type", element.getType(), new GCellEditor(panel)));
      combo = new JComboBox();
      ComboItem[] items = new ComboItem[4];
      combo.addItem(items[0] = new ComboItem("1", "1"));
      combo.addItem(items[1] = new ComboItem("0..1", "0..1"));
      combo.addItem(items[2] = new ComboItem("1..*", "1..*"));
      combo.addItem(items[3] = new ComboItem("0..*", "0..*"));
      String mul = "";
      if (element.getMultiplicity() != null && element.getMultiplicity().getRangeList().hasMoreElements()) {
         MultiplicityRange mr = (MultiplicityRange)element.getMultiplicity().getRangeList().nextElement();
         String low = Integer.toString(mr.getLower());
         String high = mr.getUpper().equals(new BigInteger("-1")) ? "*" : mr.getUpper().toString();
         combo.setEditable(true);
         mul = low.equals(high) ? low : low + ".." + high;
      }

      combo.setSelectedItem(mul);
      result.add(new SimpleProperty("Multiplicity", mul, new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[4];
      combo.addItem(items[1] = new ComboItem(new Integer(1), "addOnly"));
      combo.addItem(items[2] = new ComboItem(new Integer(2), "changeable"));
      combo.addItem(items[0] = new ComboItem(new Integer(0), "frozen"));
      combo.setSelectedItem(items[element.getChangeability()]);
      result.add(new SimpleProperty("Changeability", items[element.getChangeability()], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[0] = new ComboItem(new Integer(0), "classifier"));
      combo.addItem(items[1] = new ComboItem(new Integer(1), "instance"));
      combo.setSelectedItem(items[element.getTargetScope()]);
      result.add(new SimpleProperty("TargetScope", items[element.getTargetScope()], new GCellEditor(combo)));
      combo = new JComboBox();
      items = new ComboItem[3];
      combo.addItem(items[0] = new ComboItem(new Integer(0), "ordered"));
      combo.addItem(items[1] = new ComboItem(new Integer(1), "unordered"));
      combo.setSelectedItem(items[element.getOrdering()]);
      result.add(new SimpleProperty("Ordering", items[element.getOrdering()], new GCellEditor(combo)));
      return result;
   }

   public Collection getPropertiesForStubState(StubState element) {
      Collection result = this.getPropertiesForStateVertex(element);
      return result;
   }

   public Collection getPropertiesForSubactivityState(SubactivityState element) {
      Collection result = this.getPropertiesForSubmachineState(element);
      return result;
   }

   public Collection getPropertiesForSubmachineState(SubmachineState element) {
      Collection result = this.getPropertiesForCompositeState(element);
      return result;
   }

   public Collection getPropertiesForSubsystem(Subsystem element) {
      Collection result = this.getPropertiesForPackage(element);
      JComboBox combo = new JComboBox();
      ComboItem[] items = new ComboItem[3];
      combo.addItem(items[1] = new ComboItem(new Boolean(false), "false"));
      combo.addItem(items[2] = new ComboItem(new Boolean(true), "true"));
      int idx = element.isInstantiated() ? 2 : 1;
      combo.setSelectedIndex(idx - 1);
      result.add(new SimpleProperty("Instantiated", items[idx], new GCellEditor(combo)));
      return result;
   }

   public Collection getPropertiesForSubsystemInstance(SubsystemInstance element) {
      Collection result = this.getPropertiesForInstance(element);
      return result;
   }

   public Collection getPropertiesForSynchState(SynchState element) {
      Collection result = this.getPropertiesForStateVertex(element);
      return result;
   }

   public Collection getPropertiesForSynchronousInvocationAction(SynchronousInvocationAction element) {
      Collection result = this.getPropertiesForInvocationAction(element);
      return result;
   }

   public Collection getPropertiesForTagDefinition(TagDefinition element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForTaggedValue(TaggedValue element) {
      Collection result = this.getPropertiesForModelElement(element);
      String expr = null;
      if (element.getDataValue() != null) {
         expr = element.getDataValue();
      } else {
         expr = "";
      }

      result.add(new SimpleProperty("DataValue", expr, new GCellEditor(new JTextField(expr))));
      if (element.getType() != null) {
         String[] typeNames = new String[]{"choice", "sequence", "mixed", "any"};
         TagDefinition[] types = new TagDefinition[4];

         for(int i = 0; i < 4; ++i) {
            types[i] = ModelFactory.getTagDefinition(ModelFactory.currentModel, typeNames[i]);
         }

         JComboBox combo = new JComboBox();
         int selIdx = -1;

         for(int i = 0; i < types.length; ++i) {
            combo.addItem(new ComboItem(types[i], types[i].getName()));
            if (element.getType() == types[i]) {
               selIdx = i;
            }
         }

         if (selIdx >= 0) {
            combo.setSelectedIndex(selIdx);
         }

         JPanel panel = new JPanel(new BorderLayout());
         panel.add(combo, "Center");
         JButton but = new JumpButton(element.getType()) {
            public String getText() {
               return "...";
            }
         };
         Insets is = but.getMargin();
         is.left = 0;
         is.right = 0;
         but.setMargin(is);
         panel.add(but, "East");
         result.add(new SimpleProperty("Type", element.getType(), new GCellEditor(panel)));
      } else {
         result.add(new SimpleProperty("Type", "N/A"));
      }

      return result;
   }

   public Collection getPropertiesForTemplateArgument(TemplateArgument element) {
      Collection result = this.getPropertiesForElement(element);
      return result;
   }

   public Collection getPropertiesForTestIdentityAction(TestIdentityAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForTimeEvent(TimeEvent element) {
      Collection result = this.getPropertiesForEvent(element);
      return result;
   }

   public Collection getPropertiesForTransition(Transition element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForUnmarshalAction(UnmarshalAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForUsage(Usage element) {
      Collection result = this.getPropertiesForDependency(element);
      return result;
   }

   public Collection getPropertiesForUseCase(UseCase element) {
      Collection result = this.getPropertiesForClassifier(element);
      ComposedProperty cp = new ComposedProperty("ExtensionPoints");
      Collection col = element.getCollectionExtensionPointList();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object current = it.next();
         cp.addProperty(new SimpleProperty("ExtensionPoint", current, new GCellEditor(new JumpButton(current))));
      }

      result.add(cp);
      return result;
   }

   public Collection getPropertiesForUseCaseInstance(UseCaseInstance element) {
      Collection result = this.getPropertiesForInstance(element);
      return result;
   }

   public Collection getPropertiesForVariable(Variable element) {
      Collection result = this.getPropertiesForModelElement(element);
      return result;
   }

   public Collection getPropertiesForVariableAction(VariableAction element) {
      Collection result = this.getPropertiesForPrimitiveAction(element);
      return result;
   }

   public Collection getPropertiesForWriteAttributeAction(WriteAttributeAction element) {
      Collection result = this.getPropertiesForAttributeAction(element);
      return result;
   }

   public Collection getPropertiesForWriteLinkAction(WriteLinkAction element) {
      Collection result = this.getPropertiesForLinkAction(element);
      return result;
   }

   public Collection getPropertiesForWriteVaribleAction(WriteVaribleAction element) {
      Collection result = this.getPropertiesForVariableAction(element);
      return result;
   }
}
