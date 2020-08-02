package ro.ubbcluj.lci.utils;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.ControlFlow;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.ControlFlowImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.DataFlow;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.DataFlowImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.InputPin;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.InputPinImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPinImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.CollectionAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.CollectionActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.FilterAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.FilterActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.IterateAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.IterateActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.MapAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.MapActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.ReduceAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions.ReduceActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.Clause;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.ClauseImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.ConditionalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.ConditionalActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.GroupAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.GroupActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.LoopAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.LoopActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.Variable;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.VariableImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.ApplyFunctionAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.ApplyFunctionActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.ArgumentSpecification;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.ArgumentSpecificationImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.CodeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.CodeActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.LiteralValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.LiteralValueActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.MarshalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.MarshalActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.NullAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.NullActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.PrimitiveFunction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.PrimitiveFunctionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.TestIdentityAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.TestIdentityActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.UnmarshalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions.UnmarshalActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.HandlerAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.HandlerActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.JumpAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.JumpActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.JumpHandler;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.JumpHandlerImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.AsynchronousInvocationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.AsynchronousInvocationActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.BroadcastSignalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.BroadcastSignalActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.CallOperationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.CallOperationActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.ExplicitInvocationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.ExplicitInvocationActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.InvocationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.InvocationActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.SendSignalAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.SendSignalActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.SynchronousInvocationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions.SynchronousInvocationActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ClearAssociationAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ClearAssociationActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.CreateLinkAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.CreateLinkActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.CreateLinkObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.CreateLinkObjectActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.DestroyLinkAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.DestroyLinkActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.LinkEndCreationData;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.LinkEndCreationDataImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.LinkEndData;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.LinkEndDataImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.QualifierValue;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.QualifierValueImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ReadLinkAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ReadLinkActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ReadLinkObjectEndAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ReadLinkObjectEndActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ReadLinkObjectQualifierAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.ReadLinkObjectQualifierActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.WriteLinkAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions.WriteLinkActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.AddAttributeValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.AddAttributeValueActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.AttributeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.AttributeActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.ClearAttributeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.ClearAttributeActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.ReadAttributeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.ReadAttributeActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.RemoveAttributeValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.RemoveAttributeValueActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.WriteAttributeAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions.WriteAttributeActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.CreateObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.CreateObjectActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.DestroyObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.DestroyObjectActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.ReadIsClassifiedObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.ReadIsClassifiedObjectActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.ReclassifyObjectAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions.ReclassifyObjectActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.CallProcedureAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.CallProcedureActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.ReadExtentAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.ReadExtentActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.ReadSelfAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.ReadSelfActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.StartObjectStateMachineAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions.StartObjectStateMachineActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.AddVariableValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.AddVariableValueActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.ClearVariableAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.ClearVariableActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.ReadVariableAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.ReadVariableActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.RemoveVariableValueAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions.RemoveVariableValueActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ActionState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ActionStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ActivityGraph;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ActivityGraphImpl;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.CallState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.CallStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ClassifierInState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ClassifierInStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ObjectFlowState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ObjectFlowStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.Partition;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.PartitionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.SubactivityState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.SubactivityStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRoleImpl;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationRoleImpl;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRoleImpl;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationImpl;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSetImpl;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Interaction;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.InteractionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.InteractionInstanceSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.InteractionInstanceSetImpl;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Message;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.MessageImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLinkImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ComponentInstance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ComponentInstanceImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValueImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Exception;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ExceptionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.InstanceImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEndImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkObject;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkObjectImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.NodeInstance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.NodeInstanceImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ObjectImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ProcedureImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Reception;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ReceptionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Signal;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.SignalImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Stimulus;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.StimulusImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.SubsystemInstance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.SubsystemInstanceImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CallEvent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CallEventImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.ChangeEvent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.ChangeEventImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CompositeState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CompositeStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.FinalState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.FinalStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Guard;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.GuardImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Pseudostate;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.PseudostateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SignalEvent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SignalEventImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SimpleState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SimpleStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachineImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StubState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StubStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SubmachineState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SubmachineStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SynchState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SynchStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.TimeEvent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.TimeEventImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Transition;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.TransitionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Actor;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.ActorImpl;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Extend;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.ExtendImpl;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.ExtensionPoint;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.ExtensionPointImpl;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Include;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.IncludeImpl;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.UseCase;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.UseCaseImpl;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.UseCaseInstance;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.UseCaseInstanceImpl;
import ro.ubbcluj.lci.uml.foundation.core.Abstraction;
import ro.ubbcluj.lci.uml.foundation.core.AbstractionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Artifact;
import ro.ubbcluj.lci.uml.foundation.core.ArtifactImpl;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClassImpl;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEndImpl;
import ro.ubbcluj.lci.uml.foundation.core.AssociationImpl;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.AttributeImpl;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Binding;
import ro.ubbcluj.lci.uml.foundation.core.BindingImpl;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.ClassImpl;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Comment;
import ro.ubbcluj.lci.uml.foundation.core.CommentImpl;
import ro.ubbcluj.lci.uml.foundation.core.Component;
import ro.ubbcluj.lci.uml.foundation.core.ComponentImpl;
import ro.ubbcluj.lci.uml.foundation.core.Constraint;
import ro.ubbcluj.lci.uml.foundation.core.ConstraintImpl;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.DataTypeImpl;
import ro.ubbcluj.lci.uml.foundation.core.Dependency;
import ro.ubbcluj.lci.uml.foundation.core.DependencyImpl;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnershipImpl;
import ro.ubbcluj.lci.uml.foundation.core.ElementResidence;
import ro.ubbcluj.lci.uml.foundation.core.ElementResidenceImpl;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationImpl;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteralImpl;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.Flow;
import ro.ubbcluj.lci.uml.foundation.core.FlowImpl;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizationImpl;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.InterfaceImpl;
import ro.ubbcluj.lci.uml.foundation.core.Method;
import ro.ubbcluj.lci.uml.foundation.core.MethodImpl;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Node;
import ro.ubbcluj.lci.uml.foundation.core.NodeImpl;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.OperationImpl;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.uml.foundation.core.ParameterImpl;
import ro.ubbcluj.lci.uml.foundation.core.Permission;
import ro.ubbcluj.lci.uml.foundation.core.PermissionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Primitive;
import ro.ubbcluj.lci.uml.foundation.core.PrimitiveImpl;
import ro.ubbcluj.lci.uml.foundation.core.ProgrammingLanguageDataType;
import ro.ubbcluj.lci.uml.foundation.core.ProgrammingLanguageDataTypeImpl;
import ro.ubbcluj.lci.uml.foundation.core.TemplateArgument;
import ro.ubbcluj.lci.uml.foundation.core.TemplateArgumentImpl;
import ro.ubbcluj.lci.uml.foundation.core.TemplateParameter;
import ro.ubbcluj.lci.uml.foundation.core.TemplateParameterImpl;
import ro.ubbcluj.lci.uml.foundation.core.Usage;
import ro.ubbcluj.lci.uml.foundation.core.UsageImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpression;
import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpressionImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;
import ro.ubbcluj.lci.uml.foundation.dataTypes.ExpressionImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Geometry;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRangeImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.StereotypeImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinition;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinitionImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValueImpl;
import ro.ubbcluj.lci.uml.modelManagement.ElementImport;
import ro.ubbcluj.lci.uml.modelManagement.ElementImportImpl;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.ModelImpl;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.uml.modelManagement.PackageImpl;
import ro.ubbcluj.lci.uml.modelManagement.Subsystem;
import ro.ubbcluj.lci.uml.modelManagement.SubsystemImpl;
import ro.ubbcluj.lci.utils.exceptions.CreationException;

public class InstanceCreator {
   public InstanceCreator() {
   }

   public Object createNewAbstraction(Object[] params) throws CreationException {
      ModelElement client = (ModelElement)params[0];
      ModelElement supplier = (ModelElement)params[1];
      Abstraction result = new AbstractionImpl();
      if (client instanceof Namespace) {
         ModelFactory.createElementOwnership((Namespace)client, result);
      } else if (client.directGetNamespace() != null) {
         ModelFactory.createElementOwnership(client.directGetNamespace(), result);
      }

      result.setName("A_" + client.getName() + "_" + supplier.getName() + "_Abstraction");
      result.addClient(client);
      result.addSupplier(supplier);
      return result;
   }

   public Object createNewActionState(Object[] params) throws CreationException {
      ActionState result = new ActionStateImpl();
      return result;
   }

   public Object createNewActivityGraph(Object[] params) throws CreationException {
      ActivityGraph result = new ActivityGraphImpl();
      return result;
   }

   public Object createNewActor(Object[] params) throws CreationException {
      if (!(params[0] instanceof Model)) {
         throw new CreationException(params[0], "Actor");
      } else {
         Actor result = new ActorImpl();
         Model context = (Model)params[0];
         result.setAbstract(false);
         result.setLeaf(false);
         result.setRoot(false);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewActor"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewAddAttributeValueAction(Object[] params) throws CreationException {
      AddAttributeValueAction result = new AddAttributeValueActionImpl();
      return result;
   }

   public Object createNewAddVariableValueAction(Object[] params) throws CreationException {
      AddVariableValueAction result = new AddVariableValueActionImpl();
      return result;
   }

   public Object createNewApplyFunctionAction(Object[] params) throws CreationException {
      ApplyFunctionAction result = new ApplyFunctionActionImpl();
      return result;
   }

   public Object createNewArgumentSpecification(Object[] params) throws CreationException {
      ArgumentSpecification result = new ArgumentSpecificationImpl();
      return result;
   }

   public Object createNewArtifact(Object[] params) throws CreationException {
      Artifact result = new ArtifactImpl();
      return result;
   }

   public Object createNewAssociation(Object[] params) throws CreationException {
      if (params[0] instanceof Classifier && params[1] instanceof Classifier) {
         Classifier supplier = (Classifier)params[0];
         Classifier client = (Classifier)params[1];
         int associationNumber = ModelUtilities.findAssociationsBetween(supplier, client).values().size();
         Association result = new AssociationImpl();
         AssociationEnd supplierEnd = (AssociationEnd)this.createNewAssociationEnd(new Object[]{result});
         AssociationEnd clientEnd = (AssociationEnd)this.createNewAssociationEnd(new Object[]{result});
         supplierEnd.setParticipant(supplier);
         clientEnd.setParticipant(client);
         supplierEnd.setNavigable(false);
         clientEnd.setNavigable(true);
         char first;
         if (supplier == client) {
            first = supplier.getName().charAt(0);
            supplierEnd.setName(Character.toLowerCase(first) + supplier.getName().substring(1) + "_1");
            first = client.getName().charAt(0);
            clientEnd.setName(Character.toLowerCase(first) + client.getName().substring(1) + "_2");
         } else {
            first = supplier.getName().charAt(0);
            supplierEnd.setName(Character.toLowerCase(first) + supplier.getName().substring(1) + (associationNumber > 0 ? "_" + String.valueOf(associationNumber) : ""));
            first = client.getName().charAt(0);
            clientEnd.setName(Character.toLowerCase(first) + client.getName().substring(1) + (associationNumber > 0 ? "_" + String.valueOf(associationNumber) : ""));
         }

         result.setAbstract(false);
         result.setLeaf(false);
         result.setRoot(false);
         result.setName("A_" + supplier.getName() + "_" + client.getName() + (associationNumber > 0 ? "_" + String.valueOf(associationNumber) : ""));
         ModelFactory.createElementOwnership(client.getNamespace().getNamespace(), result);
         return result;
      } else {
         throw new CreationException(params[0], "Association");
      }
   }

   public Object createNewAssociationClass(Object[] params) throws CreationException {
      if (params[0] instanceof Class && params[1] instanceof Association) {
         AssociationClass result = new AssociationClassImpl();
         Class clazz = (Class)params[0];
         Association assoc = (Association)params[1];
         ModelFactory.moveContent(result, assoc, "AssociationRole", "AssociationRole");
         ModelFactory.moveContent(result, assoc, "Behavior", "StateMachine");
         ModelFactory.moveContent(result, assoc, "ClientDependency", "Dependency");
         ModelFactory.moveContent(result, assoc, "Comment", "Comment");
         ModelFactory.moveContent(result, assoc, "Connection", "AssociationEnd");
         ModelFactory.moveContent(result, assoc, "Constraint", "Constraint");
         ModelFactory.moveContent(result, assoc, "Container", "ElementResidence");
         ModelFactory.moveContent(result, assoc, "Generalization", "Generalization");
         ModelFactory.moveContent(result, assoc, "Link", "Link");
         ModelFactory.moveContent(result, assoc, "Package", "ElementImport");
         ModelFactory.moveContent(result, assoc, "Partition", "Partition");
         ModelFactory.moveContent(result, assoc, "Presentation", "PresentationElement");
         ModelFactory.moveContent(result, assoc, "ReferenceTag", "TaggedValue");
         ModelFactory.moveContent(result, assoc, "SourceFlow", "Flow");
         ModelFactory.moveContent(result, assoc, "Specialization", "Generalization");
         ModelFactory.moveContent(result, assoc, "Stereotype", "Stereotype");
         ModelFactory.moveContent(result, assoc, "SupplierDependency", "Dependency");
         ModelFactory.moveContent(result, assoc, "TaggedValue", "ReferenceTag");
         ModelFactory.moveContent(result, assoc, "TargetFlow", "Flow");
         ModelFactory.moveContent(result, assoc, "ParameterTemplate", "TemplateParameter");
         assoc.getNamespace().setNamespace((Namespace)null);
         assoc.setNamespace((ElementOwnership)null);
         result.setAbstract(clazz.isAbstract());
         result.setActive(clazz.isActive());
         result.setLeaf(clazz.isLeaf());
         result.setName(clazz.getName());
         ModelFactory.createElementOwnership(clazz.getNamespace().getNamespace(), result);
         result.setRoot(clazz.isRoot());
         ModelFactory.moveContent(result, clazz, "Association", "AssociationEnd");
         ModelFactory.moveContent(result, clazz, "Behavior", "StateMachine");
         ModelFactory.moveContent(result, clazz, "ClassifierInState", "ClassifierInState");
         ModelFactory.moveContent(result, clazz, "ClientDependency", "Dependency");
         ModelFactory.moveContent(result, clazz, "Comment", "Comment");
         ModelFactory.moveContent(result, clazz, "Constraint", "Constraint");
         ModelFactory.moveContent(result, clazz, "Container", "ElementResidence");
         ModelFactory.moveContent(result, clazz, "Feature", "Feature");
         ModelFactory.moveContent(result, clazz, "Generalization", "Generalization");
         ModelFactory.moveContent(result, clazz, "Instance", "Instance");
         ModelFactory.moveContent(result, clazz, "ObjectFlowState", "ObjectFlowState");
         ModelFactory.moveContent(result, clazz, "OwnedElement", "ElementOwnership");
         ModelFactory.moveContent(result, clazz, "Package", "ElementResidence");
         ModelFactory.moveContent(result, clazz, "Partition", "Partition");
         ModelFactory.moveContent(result, clazz, "PowertypeRange", "Generalization");
         ModelFactory.moveContent(result, clazz, "Presentation", "PresentationElement");
         ModelFactory.moveContent(result, clazz, "ReferenceTag", "TaggedValue");
         ModelFactory.moveContent(result, clazz, "SourceFlow", "Flow");
         ModelFactory.moveContent(result, clazz, "Specialization", "Generalization");
         ModelFactory.moveContent(result, clazz, "SpecifiedEnd", "AssociationEnd");
         ModelFactory.moveContent(result, clazz, "Stereotype", "Stereotype");
         ModelFactory.moveContent(result, clazz, "SupplierDependency", "Dependency");
         ModelFactory.moveContent(result, clazz, "TaggedValue", "TaggedValue");
         ModelFactory.moveContent(result, clazz, "TargetFlow", "Flow");
         ModelFactory.moveContent(result, clazz, "ParameterTemplate", "TemplateParameter");
         ModelFactory.moveContent(result, clazz, "TypedFeature", "StructuralFeature");
         ModelFactory.moveContent(result, clazz, "TypedParameter", "Parameter");
         clazz.getNamespace().setNamespace((Namespace)null);
         clazz.setNamespace((ElementOwnership)null);
         return result;
      } else {
         throw new CreationException(params[0], "AssociationClass");
      }
   }

   public Object createNewAssociationEnd(Object[] params) throws CreationException {
      if (!(params[0] instanceof Association)) {
         throw new CreationException(params[0], "AssociationEnd");
      } else {
         AssociationEnd result = new AssociationEndImpl();
         Association context = (Association)params[0];
         result.setAggregation(1);
         result.setAssociation(context);
         result.setChangeability(2);
         result.setOrdering(1);
         result.setTargetScope(1);
         result.setVisibility(3);
         Multiplicity m = new MultiplicityImpl();
         MultiplicityRange mr = new MultiplicityRangeImpl();
         mr.setLower(1);
         mr.setUpper(new BigInteger("1"));
         m.addRange(mr);
         result.setMultiplicity(m);
         return result;
      }
   }

   public Object createNewAssociationEndRole(Object[] params) throws CreationException {
      AssociationEndRole result = new AssociationEndRoleImpl();
      return result;
   }

   public Object createNewAssociationRole(Object[] params) throws CreationException {
      AssociationRole result = new AssociationRoleImpl();
      return result;
   }

   public Object createNewAsynchronousInvocationAction(Object[] params) throws CreationException {
      AsynchronousInvocationAction result = new AsynchronousInvocationActionImpl();
      return result;
   }

   public Object createNewAttribute(Object[] params) throws CreationException {
      if (!(params[0] instanceof Class) && !(params[0] instanceof Interface) && !(params[0] instanceof AssociationEnd)) {
         throw new CreationException(params[0], "Attribute");
      } else {
         Attribute result = new AttributeImpl();
         if (params[0] instanceof AssociationEnd) {
            AssociationEnd context = (AssociationEnd)params[0];
            result.setName(ModelFactory.getDefaultName((Collection)context.getCollectionQualifierList(), result, "newQualifier"));
            result.setAssociationEnd(context);
         } else {
            Classifier context = (Classifier)params[0];
            Collection attrs = context.allAttributes();
            result.setName(ModelFactory.getDefaultName((Collection)attrs, result, "newAttribute"));
            result.setOwner(context);
         }

         result.setChangeability(2);
         result.setVisibility(2);
         result.setOwnerScope(1);
         result.setTargetScope(1);
         result.setOrdering(1);
         Multiplicity mult = new MultiplicityImpl();
         MultiplicityRange range = new MultiplicityRangeImpl();
         range.setLower(1);
         range.setUpper(BigInteger.ONE);
         mult.addRange(range);
         result.setMultiplicity(mult);
         result.setType(ModelFactory.getUndefinedType());
         Expression initialValue = new ExpressionImpl();
         initialValue.setBody("");
         result.setInitialValue(initialValue);
         return result;
      }
   }

   public Object createNewAttributeAction(Object[] params) throws CreationException {
      AttributeAction result = new AttributeActionImpl();
      return result;
   }

   public Object createNewAttributeLink(Object[] params) throws CreationException {
      if (!(params[0] instanceof Attribute)) {
         throw new CreationException(params[0], "AttributeLink");
      } else {
         Attribute tmpAttr = (Attribute)params[0];
         AttributeLink result = new AttributeLinkImpl();
         result.setAttribute(tmpAttr);
         result.setName(tmpAttr.getName());
         if (tmpAttr.getInitialValue() != null) {
            String initialValue = tmpAttr.getInitialValue().getBody();
            if (initialValue != null) {
               DataValue currDataValue = ModelFactory.createNewDataValue(initialValue, tmpAttr.getType());
               result.setValue(currDataValue);
            }
         }

         return result;
      }
   }

   public Object createNewBinding(Object[] params) throws CreationException {
      Binding result = new BindingImpl();
      return result;
   }

   public Object createNewBroadcastSignalAction(Object[] params) throws CreationException {
      BroadcastSignalAction result = new BroadcastSignalActionImpl();
      return result;
   }

   public Object createNewCallEvent(Object[] params) throws CreationException {
      CallEvent result = new CallEventImpl();
      return result;
   }

   public Object createNewCallOperationAction(Object[] params) throws CreationException {
      CallOperationAction result = new CallOperationActionImpl();
      return result;
   }

   public Object createNewCallProcedureAction(Object[] params) throws CreationException {
      CallProcedureAction result = new CallProcedureActionImpl();
      return result;
   }

   public Object createNewCallState(Object[] params) throws CreationException {
      CallState result = new CallStateImpl();
      return result;
   }

   public Object createNewChangeEvent(Object[] params) throws CreationException {
      ChangeEvent result = new ChangeEventImpl();
      return result;
   }

   public Object createNewClass(Object[] params) throws CreationException {
      if (!(params[0] instanceof Package) && !(params[0] instanceof Class)) {
         throw new CreationException(params[0], "Class");
      } else {
         Class result = new ClassImpl();
         Namespace context = (Namespace)params[0];
         result.setAbstract(false);
         result.setActive(true);
         result.setLeaf(false);
         result.setRoot(false);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewClass"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewClassifierInState(Object[] params) throws CreationException {
      ClassifierInState result = new ClassifierInStateImpl();
      return result;
   }

   public Object createNewClassifierRole(Object[] params) throws CreationException {
      ClassifierRole result = new ClassifierRoleImpl();
      return result;
   }

   public Object createNewClause(Object[] params) throws CreationException {
      Clause result = new ClauseImpl();
      return result;
   }

   public Object createNewClearAssociationAction(Object[] params) throws CreationException {
      ClearAssociationAction result = new ClearAssociationActionImpl();
      return result;
   }

   public Object createNewClearAttributeAction(Object[] params) throws CreationException {
      ClearAttributeAction result = new ClearAttributeActionImpl();
      return result;
   }

   public Object createNewClearVariableAction(Object[] params) throws CreationException {
      ClearVariableAction result = new ClearVariableActionImpl();
      return result;
   }

   public Object createNewCodeAction(Object[] params) throws CreationException {
      CodeAction result = new CodeActionImpl();
      return result;
   }

   public Object createNewCollaboration(Object[] params) throws CreationException {
      Collaboration result = new CollaborationImpl();
      Namespace ns = (Namespace)params[0];
      ModelFactory.createElementOwnership(ns, result);
      result.setName(ModelFactory.getDefaultName((Namespace)ns, result, "Collaboration"));
      return result;
   }

   public Object createNewCollaborationInstanceSet(Object[] params) throws CreationException {
      CollaborationInstanceSet result = new CollaborationInstanceSetImpl();
      return result;
   }

   public Object createNewCollectionAction(Object[] params) throws CreationException {
      CollectionAction result = new CollectionActionImpl();
      return result;
   }

   public Object createNewComment(Object[] params) throws CreationException {
      Comment result = new CommentImpl();
      return result;
   }

   public Object createNewComponent(Object[] params) throws CreationException {
      Component result = new ComponentImpl();
      return result;
   }

   public Object createNewComponentInstance(Object[] params) throws CreationException {
      ComponentInstance result = new ComponentInstanceImpl();
      return result;
   }

   public Object createNewCompositeState(Object[] params) throws CreationException {
      CompositeState result = new CompositeStateImpl();
      return result;
   }

   public Object createNewConditionalAction(Object[] params) throws CreationException {
      ConditionalAction result = new ConditionalActionImpl();
      return result;
   }

   public Object createNewConstraint(Object[] params) throws CreationException {
      if (!(params[0] instanceof Stereotype) && !(params[0] instanceof Namespace)) {
         throw new CreationException(params[0], "Constraint");
      } else {
         Constraint result = new ConstraintImpl();
         if (params[0] instanceof Stereotype) {
            Stereotype var3 = (Stereotype)params[0];
         } else {
            Namespace context = (Namespace)params[0];
            result.setName(ModelFactory.getDefaultName((Namespace)context, result, "newConstraint"));
            ModelFactory.createElementOwnership(context, result);
         }

         BooleanExpression expr = new BooleanExpressionImpl();
         expr.setBody("");
         result.setBody(expr);
         return result;
      }
   }

   public Object createNewControlFlow(Object[] params) throws CreationException {
      ControlFlow result = new ControlFlowImpl();
      return result;
   }

   public Object createNewCreateLinkAction(Object[] params) throws CreationException {
      CreateLinkAction result = new CreateLinkActionImpl();
      return result;
   }

   public Object createNewCreateLinkObjectAction(Object[] params) throws CreationException {
      CreateLinkObjectAction result = new CreateLinkObjectActionImpl();
      return result;
   }

   public Object createNewCreateObjectAction(Object[] params) throws CreationException {
      CreateObjectAction result = new CreateObjectActionImpl();
      return result;
   }

   public Object createNewDataFlow(Object[] params) throws CreationException {
      DataFlow result = new DataFlowImpl();
      return result;
   }

   public Object createNewDataType(Object[] params) throws CreationException {
      if (!(params[0] instanceof Model)) {
         throw new CreationException(params[0], "DataType");
      } else {
         DataType result = new DataTypeImpl();
         Model context = (Model)params[0];
         result.setAbstract(false);
         result.setLeaf(false);
         result.setRoot(false);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewDataType"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewDataValue(Object[] params) throws CreationException {
      DataValue result = new DataValueImpl();
      ModelFactory.createElementOwnership(ModelFactory.currentModel, result);
      return result;
   }

   public Object createNewDependency(Object[] params) throws CreationException {
      ModelElement client = (ModelElement)params[0];
      ModelElement supplier = (ModelElement)params[1];
      Dependency result = new DependencyImpl();
      result.setName("A_" + client.getName() + "_" + supplier.getName() + "_Dependency");
      result.addClient(client);
      result.addSupplier(supplier);
      if (client.getNamespace() != null && client.getNamespace().getNamespace() != null) {
         ModelFactory.createElementOwnership(client.getNamespace().getNamespace(), result);
      }

      return result;
   }

   public Object createNewDestroyLinkAction(Object[] params) throws CreationException {
      DestroyLinkAction result = new DestroyLinkActionImpl();
      return result;
   }

   public Object createNewDestroyObjectAction(Object[] params) throws CreationException {
      DestroyObjectAction result = new DestroyObjectActionImpl();
      return result;
   }

   public Object createNewElementImport(Object[] params) throws CreationException {
      ElementImport result = new ElementImportImpl();
      return result;
   }

   public Object createNewElementOwnership(Object[] params) throws CreationException {
      ElementOwnership result = new ElementOwnershipImpl();
      return result;
   }

   public Object createNewElementResidence(Object[] params) throws CreationException {
      ElementResidence result = new ElementResidenceImpl();
      return result;
   }

   public Object createNewEnumeration(Object[] params) throws CreationException {
      if (!(params[0] instanceof Model) && !(params[0] instanceof Package) && !(params[0] instanceof Classifier)) {
         throw new CreationException(params[0], "Enumeration");
      } else {
         Enumeration result = new EnumerationImpl();
         Namespace context = (Namespace)params[0];
         result.setAbstract(false);
         result.setLeaf(true);
         result.setRoot(true);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewEnumeration"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewEnumerationLiteral(Object[] params) throws CreationException {
      if (!(params[0] instanceof Enumeration)) {
         throw new CreationException(params[0], "EnumerationLiteral");
      } else {
         EnumerationLiteral result = new EnumerationLiteralImpl();
         Enumeration context = (Enumeration)params[0];
         result.setEnumeration(context);
         result.setName(ModelFactory.getDefaultName((Collection)context.getCollectionLiteralList(), result, "literal"));
         return result;
      }
   }

   public Object createNewException(Object[] params) throws CreationException {
      if (!(params[0] instanceof Namespace)) {
         throw new CreationException(params[0], "Exception");
      } else {
         Exception result = new ExceptionImpl();
         Namespace ns = (Namespace)params[0];
         result.setName(ModelFactory.getDefaultName((Collection)ns.directGetCollectionOwnedElementList(), result, "NewException"));
         ModelFactory.createElementOwnership(ns, result);
         result.setAbstract(false);
         result.setLeaf(false);
         result.setRoot(false);
         return result;
      }
   }

   public Object createNewExplicitInvocationAction(Object[] params) throws CreationException {
      ExplicitInvocationAction result = new ExplicitInvocationActionImpl();
      return result;
   }

   public Object createNewExpression(Object[] params) throws CreationException {
      if (!(params[0] instanceof Namespace)) {
         throw new CreationException(params[0], "Expression");
      } else {
         Expression result = new ExpressionImpl();
         return result;
      }
   }

   public Object createNewExtend(Object[] params) throws CreationException {
      if (params[0] instanceof UseCase && params[1] instanceof UseCase) {
         Extend result = new ExtendImpl();
         UseCase client = (UseCase)params[0];
         UseCase supplier = (UseCase)params[1];
         result.setBase(supplier);
         result.setExtension(client);
         ModelFactory.createElementOwnership(client, result);
         result.setName(client.getName() + "_extends_" + supplier.getName());
         return result;
      } else {
         throw new CreationException(params[0], "Extend");
      }
   }

   public Object createNewExtensionPoint(Object[] params) throws CreationException {
      ExtensionPoint result = new ExtensionPointImpl();
      return result;
   }

   public Object createNewFilterAction(Object[] params) throws CreationException {
      FilterAction result = new FilterActionImpl();
      return result;
   }

   public Object createNewFinalState(Object[] params) throws CreationException {
      FinalState result = new FinalStateImpl();
      return result;
   }

   public Object createNewFlow(Object[] params) throws CreationException {
      Flow result = new FlowImpl();
      return result;
   }

   public Object createNewGeneralization(Object[] params) throws CreationException {
      Generalization result = null;
      GeneralizableElement child = null;
      GeneralizableElement parent = null;
      boolean interfaceRealization = false;
      if (((Element)params[0]).getMetaclassName().endsWith("Class") && ((Element)params[1]).getMetaclassName().equals("Interface")) {
         interfaceRealization = true;
      }

      boolean isValidGen = true;
      if (!(params[0] instanceof GeneralizableElement) || !(params[1] instanceof GeneralizableElement)) {
         isValidGen = false;
      }

      if (params[0] instanceof Actor && params[1] instanceof UseCase || params[0] instanceof UseCase && params[1] instanceof Actor) {
         isValidGen = false;
      }

      if (isValidGen) {
         child = (GeneralizableElement)params[0];
         parent = (GeneralizableElement)params[1];
         if (parent.allParents().contains(child) || parent == child) {
            isValidGen = false;
         }

         if (child.parent().contains(parent)) {
            isValidGen = false;
         }

         if (parent.isLeaf()) {
            isValidGen = false;
         }

         if (child.isRoot()) {
            isValidGen = false;
         }
      }

      if (!isValidGen) {
         throw new CreationException(params[0], "Generalization");
      } else {
         result = new GeneralizationImpl();
         result.setChild(child);
         result.setParent(parent);
         result.setName(child.getName() + "_is_a_" + parent.getName());
         if (child instanceof Namespace) {
            ModelFactory.createElementOwnership((Namespace)child, result);
         } else {
            Namespace childNms = child.directGetNamespace();
            if (childNms != null) {
               ModelFactory.createElementOwnership(childNms, result);
            }
         }

         return result;
      }
   }

   public Object createNewGroupAction(Object[] params) throws CreationException {
      GroupAction result = new GroupActionImpl();
      return result;
   }

   public Object createNewGuard(Object[] params) throws CreationException {
      Guard result = new GuardImpl();
      return result;
   }

   public Object createNewHandlerAction(Object[] params) throws CreationException {
      HandlerAction result = new HandlerActionImpl();
      return result;
   }

   public Object createNewInclude(Object[] params) throws CreationException {
      if (params[0] instanceof UseCase && params[1] instanceof UseCase) {
         Include result = new IncludeImpl();
         UseCase client = (UseCase)params[0];
         UseCase supplier = (UseCase)params[1];
         result.setBase(supplier);
         result.setAddition(client);
         ModelFactory.createElementOwnership(client, result);
         result.setName(client.getName() + "_includes_" + supplier.getName());
         return result;
      } else {
         throw new CreationException(params[0], "Include");
      }
   }

   public Object createNewInputPin(Object[] params) throws CreationException {
      InputPin result = new InputPinImpl();
      return result;
   }

   public Object createNewInteraction(Object[] params) throws CreationException {
      Interaction result = new InteractionImpl();
      return result;
   }

   public Object createNewInteractionInstanceSet(Object[] params) throws CreationException {
      InteractionInstanceSet result = new InteractionInstanceSetImpl();
      return result;
   }

   public Object createNewInterface(Object[] params) throws CreationException {
      if (!(params[0] instanceof Package)) {
         throw new CreationException(params[0], "Interface");
      } else {
         Interface result = new InterfaceImpl();
         Package context = (Package)params[0];
         result.setAbstract(true);
         result.setLeaf(false);
         result.setRoot(false);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewInterface"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewInvocationAction(Object[] params) throws CreationException {
      InvocationAction result = new InvocationActionImpl();
      return result;
   }

   public Object createNewIterateAction(Object[] params) throws CreationException {
      IterateAction result = new IterateActionImpl();
      return result;
   }

   public Object createNewJumpAction(Object[] params) throws CreationException {
      JumpAction result = new JumpActionImpl();
      return result;
   }

   public Object createNewJumpHandler(Object[] params) throws CreationException {
      JumpHandler result = new JumpHandlerImpl();
      return result;
   }

   public Object createNewLink(Object[] params) throws CreationException {
      if (params[0] instanceof Instance && params[1] instanceof Instance && params[2] instanceof Association) {
         Instance sp = (Instance)params[0];
         Instance cl = (Instance)params[1];
         Instance[] instances = new Instance[]{sp, cl};
         Association assoc = (Association)params[2];
         Iterator it = assoc.getCollectionLinkList().iterator();

         Instance[] participants;
         do {
            if (!it.hasNext()) {
               Link lnk = new LinkImpl();
               lnk.setAssociation(assoc);
               int instanceIndex = 0;
                it = assoc.getCollectionConnectionList().iterator();

               while(it.hasNext()) {
                  LinkEnd le = new LinkEndImpl();
                  AssociationEnd ae = (AssociationEnd)it.next();
                  le.setAssociationEnd(ae);
                  le.setName(ae.getName());
                  le.setLink(lnk);
                  le.setInstance(instances[instanceIndex]);
                  ++instanceIndex;

                  AttributeLinkImpl currAttrLink;
                  for(Iterator qualIt = ae.getCollectionQualifierList().iterator(); qualIt.hasNext(); le.addQualifierValue(currAttrLink)) {
                     Attribute currAttr = (Attribute)qualIt.next();
                     currAttrLink = new AttributeLinkImpl();
                     currAttrLink.setAttribute(currAttr);
                     currAttrLink.setName(currAttr.getName());
                     if (currAttr.getInitialValue() != null) {
                        if (!(currAttr.getType() instanceof DataType) && !(currAttr.getType() instanceof Enumeration)) {
                           Instance currInstance = new InstanceImpl();
                           currInstance.setName(currAttr.getInitialValue().getBody());
                           currInstance.addClassifier(currAttr.getType());
                           currAttrLink.setValue(currInstance);
                        } else {
                           DataValue currDataValue = ModelFactory.createNewDataValue(currAttr.getInitialValue().getBody(), currAttr.getType());
                           currAttrLink.setValue(currDataValue);
                        }
                     }
                  }
               }

               ModelFactory.createElementOwnership(cl.directGetNamespace(), lnk);
               lnk.setName(ModelFactory.getDefaultName((Namespace)lnk.directGetNamespace(), lnk, "A_" + sp.getName() + "_" + cl.getName()));
               return lnk;
            }

            Link link = (Link)it.next();
            participants = new Instance[2];
            int i = 0;

            for(Iterator connectionIt = link.getCollectionConnectionList().iterator(); connectionIt.hasNext(); ++i) {
               LinkEnd tmpLinkEnd = (LinkEnd)connectionIt.next();
               participants[i] = tmpLinkEnd.getInstance();
            }
         } while((participants[0] != sp || participants[1] != cl) && (participants[0] != cl || participants[1] != sp));

         throw new CreationException(params[0], "Link");
      } else {
         throw new CreationException(params[0], "Link");
      }
   }

   public Object createNewLinkEnd(Object[] params) throws CreationException {
      if (!(params[0] instanceof Link)) {
         throw new CreationException(params[0], "LinkEnd");
      } else {
         LinkEnd result = new LinkEndImpl();
         Link lnk = (Link)params[0];
         result.setLink(lnk);
         return result;
      }
   }

   public Object createNewLinkEndCreationData(Object[] params) throws CreationException {
      LinkEndCreationData result = new LinkEndCreationDataImpl();
      return result;
   }

   public Object createNewLinkEndData(Object[] params) throws CreationException {
      LinkEndData result = new LinkEndDataImpl();
      return result;
   }

   public Object createNewLinkObject(Object[] params) throws CreationException {
      if (params[0] instanceof Instance && params[1] instanceof Instance && params[2] instanceof AssociationClass) {
         Instance cl = (Instance)params[0];
         Instance sp = (Instance)params[1];
         AssociationClass assoc = (AssociationClass)params[2];
         Iterator it = assoc.getCollectionLinkList().iterator();

         Instance[] participants;
         do {
            if (!it.hasNext()) {
               Classifier ccl = (Classifier)cl.getClassifierList().nextElement();
               Classifier csp = (Classifier)sp.getClassifierList().nextElement();
               LinkObject lnkObj = new LinkObjectImpl();
               lnkObj.setAssociation(assoc);
               lnkObj.addClassifier(assoc);
                it = assoc.getCollectionConnectionList().iterator();

               while(true) {
                  while(it.hasNext()) {
                     LinkEnd le = new LinkEndImpl();
                     AssociationEnd ae = (AssociationEnd)it.next();
                     le.setAssociationEnd(ae);
                     le.setName(ae.getName());
                     le.setLink(lnkObj);
                     if (ae.getParticipant() == (Classifier)sp.getClassifierList().nextElement()) {
                        le.setInstance(sp);
                     } else {
                        le.setInstance(cl);
                     }

                     AttributeLinkImpl currAttrLink;
                     for(Iterator qualIt = ae.getCollectionQualifierList().iterator(); qualIt.hasNext(); le.addQualifierValue(currAttrLink)) {
                        Attribute currAttr = (Attribute)qualIt.next();
                        currAttrLink = new AttributeLinkImpl();
                        currAttrLink.setAttribute(currAttr);
                        currAttrLink.setName(currAttr.getName());
                        if (currAttr.getInitialValue() != null) {
                           if (!(currAttr.getType() instanceof DataType) && !(currAttr.getType() instanceof Enumeration)) {
                              Instance currInstance = new InstanceImpl();
                              currInstance.setName(currAttr.getInitialValue().getBody());
                              currInstance.addClassifier(currAttr.getType());
                              currAttrLink.setValue(currInstance);
                           } else {
                              DataValue currDataValue = ModelFactory.createNewDataValue(currAttr.getInitialValue().getBody(), currAttr.getType());
                              currAttrLink.setValue(currDataValue);
                           }
                        }
                     }

                     if (ccl.getCollectionAssociationList().containsAll(csp.getCollectionAssociationList())) {
                        le.setInstance(cl);
                        LinkEnd le2 = new LinkEndImpl();
                        AssociationEnd ae2 = (AssociationEnd)it.next();
                        le2.setAssociationEnd(ae2);
                        le2.setName(ae2.getName());
                        le2.setLink(lnkObj);

                        for(Iterator qualIt = ae2.getCollectionQualifierList().iterator(); qualIt.hasNext(); le2.addQualifierValue(currAttrLink)) {
                           Attribute currAttr = (Attribute)qualIt.next();
                           currAttrLink = new AttributeLinkImpl();
                           currAttrLink.setAttribute(currAttr);
                           currAttrLink.setName(currAttr.getName());
                           if (currAttr.getInitialValue() != null) {
                              if (!(currAttr.getType() instanceof DataType) && !(currAttr.getType() instanceof Enumeration)) {
                                 Instance currInstance = new InstanceImpl();
                                 currInstance.setName(currAttr.getInitialValue().getBody());
                                 currInstance.addClassifier(currAttr.getType());
                                 currAttrLink.setValue(currInstance);
                              } else {
                                 DataValue currDataValue = ModelFactory.createNewDataValue(currAttr.getInitialValue().getBody(), currAttr.getType());
                                 currAttrLink.setValue(currDataValue);
                              }
                           }
                        }

                        le2.setInstance(sp);
                     } else if (ccl.getCollectionAssociationList().contains(ae)) {
                        le.setInstance(cl);
                     } else if (csp.getCollectionAssociationList().contains(ae)) {
                        le.setInstance(sp);
                     }
                  }

                  it = assoc.getCollectionFeatureList().iterator();

                  while(true) {
                     Feature tmpFeature;
                     do {
                        if (!it.hasNext()) {
                           ModelFactory.createElementOwnership(cl.directGetNamespace(), lnkObj);
                           lnkObj.setName(ModelFactory.getDefaultName((Namespace)lnkObj.directGetNamespace(), lnkObj, "A_" + sp.getName() + "_" + cl.getName()));
                           return lnkObj;
                        }

                        tmpFeature = (Feature)it.next();
                     } while(!(tmpFeature instanceof Attribute));

                     Attribute currAttr = (Attribute)tmpFeature;
                     AttributeLink currAttrLink = new AttributeLinkImpl();
                     currAttrLink.setAttribute(currAttr);
                     currAttrLink.setName(currAttr.getName());
                     if (currAttr.getInitialValue() != null) {
                        if (!(currAttr.getType() instanceof DataType) && !(currAttr.getType() instanceof Enumeration)) {
                           Instance currInstance = new InstanceImpl();
                           currInstance.setName(currAttr.getInitialValue().getBody());
                           currInstance.addClassifier(currAttr.getType());
                           currAttrLink.setValue(currInstance);
                        } else {
                           DataValue currDataValue = ModelFactory.createNewDataValue(currAttr.getInitialValue().getBody(), currAttr.getType());
                           currAttrLink.setValue(currDataValue);
                        }
                     }

                     lnkObj.addSlot(currAttrLink);
                  }
               }
            }

            Link link = (Link)it.next();
            participants = new Instance[2];
            int i = 0;

            for(Iterator connectionIt = link.getCollectionConnectionList().iterator(); connectionIt.hasNext(); ++i) {
               LinkEnd tmpLinkEnd = (LinkEnd)connectionIt.next();
               participants[i] = tmpLinkEnd.getInstance();
            }
         } while((participants[0] != sp || participants[1] != cl) && (participants[0] != cl || participants[1] != sp));

         throw new CreationException(params[0], "Link");
      } else {
         throw new CreationException(params[0], "LinkObject");
      }
   }

   public Object createNewLiteralValueAction(Object[] params) throws CreationException {
      LiteralValueAction result = new LiteralValueActionImpl();
      return result;
   }

   public Object createNewLoopAction(Object[] params) throws CreationException {
      LoopAction result = new LoopActionImpl();
      return result;
   }

   public Object createNewMapAction(Object[] params) throws CreationException {
      MapAction result = new MapActionImpl();
      return result;
   }

   public Object createNewMarshalAction(Object[] params) throws CreationException {
      MarshalAction result = new MarshalActionImpl();
      return result;
   }

   public Object createNewMessage(Object[] params) throws CreationException {
      Message result = new MessageImpl();
      return result;
   }

   public Object createNewMethod(Object[] params) throws CreationException {
      Method result = new MethodImpl();
      return result;
   }

   public Object createNewModel(Object[] params) throws CreationException {
      if (!(params[0] instanceof Model)) {
         throw new CreationException(params[0], "Model");
      } else {
         Model result = new ModelImpl();
         Model context = (Model)params[0];
         result.setAbstract(false);
         result.setLeaf(false);
         result.setRoot(false);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewModel"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewNode(Object[] params) throws CreationException {
      Node result = new NodeImpl();
      return result;
   }

   public Object createNewNodeInstance(Object[] params) throws CreationException {
      NodeInstance result = new NodeInstanceImpl();
      return result;
   }

   public Object createNewNullAction(Object[] params) throws CreationException {
      NullAction result = new NullActionImpl();
      return result;
   }

   public Object createNewObject(Object[] params) throws CreationException {
      if (!(params[0] instanceof Collaboration)) {
         throw new CreationException(params[0], "Object");
      } else {
         ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object result = new ObjectImpl();
         Collaboration context = (Collaboration)params[0];
         ModelFactory.createElementOwnership(context, result);
         result.setName(ModelFactory.getDefaultName((Collection)context.directGetCollectionOwnedElementList(), result, "newObject"));
         return result;
      }
   }

   public Object createNewObjectFlowState(Object[] params) throws CreationException {
      ObjectFlowState result = new ObjectFlowStateImpl();
      return result;
   }

   public Object createNewOperation(Object[] params) throws CreationException {
      if (!(params[0] instanceof Class) && !(params[0] instanceof Interface)) {
         throw new CreationException(params[0], "Operation");
      } else {
         Operation result = new OperationImpl();
         Classifier context = (Classifier)params[0];
         result.setAbstract(false);
         result.setLeaf(false);
         result.setRoot(false);
         result.setQuery(false);
         result.setOwner(context);
         result.setOwnerScope(1);
         result.setVisibility(3);
         Collection ops = context.allOperations();
         ops.retainAll(context.getCollectionFeatureList());
         result.setName(ModelFactory.getDefaultName((Collection)ops, result, "newOperation"));
         return result;
      }
   }

   public Object createNewOutputPin(Object[] params) throws CreationException {
      OutputPin result = new OutputPinImpl();
      return result;
   }

   public Object createNewPackage(Object[] params) throws CreationException {
      if (!(params[0] instanceof Package)) {
         throw new CreationException(params[0], "Package");
      } else {
         Package result = new PackageImpl();
         Package context = (Package)params[0];
         result.setAbstract(false);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewPackage"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewParameter(Object[] params) throws CreationException {
      if (!(params[0] instanceof BehavioralFeature)) {
         throw new CreationException(params[0], "Parameter");
      } else {
         Parameter result = new ParameterImpl();
         BehavioralFeature context = (BehavioralFeature)params[0];
         result.setBehavioralFeature(context);
         result.setKind(2);
         result.setType(ModelFactory.getUndefinedType());
         result.setName(ModelFactory.getDefaultName((Collection)context.getCollectionParameterList(), result, "newParameter"));
         return result;
      }
   }

   public Object createNewPartition(Object[] params) throws CreationException {
      Partition result = new PartitionImpl();
      return result;
   }

   public Object createNewPermission(Object[] params) throws CreationException {
      if (!(params[1] instanceof Namespace)) {
         throw new CreationException(params[0], "Permission");
      } else {
         Permission result = new PermissionImpl();
         ModelElement client = (ModelElement)params[0];
         ModelElement supplier = (ModelElement)params[1];
         result.setName("a_" + client.getName() + "_" + supplier.getName() + "_permission");
         result.addClient(client);
         result.addSupplier(supplier);
         if (client.getNamespace() != null && client.getNamespace().getNamespace() != null) {
            ModelFactory.createElementOwnership(client.getNamespace().getNamespace(), result);
         }

         return result;
      }
   }

   public Object createNewPrimitive(Object[] params) throws CreationException {
      if (!(params[0] instanceof Model)) {
         throw new CreationException(params[0], "Primitive");
      } else {
         Primitive result = new PrimitiveImpl();
         Model context = (Model)params[0];
         result.setAbstract(false);
         result.setLeaf(true);
         result.setRoot(true);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "newPrimitive"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewPrimitiveFunction(Object[] params) throws CreationException {
      PrimitiveFunction result = new PrimitiveFunctionImpl();
      return result;
   }

   public Object createNewProcedure(Object[] params) throws CreationException {
      Procedure result = new ProcedureImpl();
      return result;
   }

   public Object createNewProgrammingLanguageDataType(Object[] params) throws CreationException {
      if (!(params[0] instanceof Model)) {
         throw new CreationException(params[0], "ProgrammingLanguageDataType");
      } else {
         ProgrammingLanguageDataType result = new ProgrammingLanguageDataTypeImpl();
         Model context = (Model)params[0];
         result.setAbstract(false);
         result.setLeaf(false);
         result.setRoot(false);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewProgrammingLanguageDataType"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewPseudostate(Object[] params) throws CreationException {
      Pseudostate result = new PseudostateImpl();
      return result;
   }

   public Object createNewQualifierValue(Object[] params) throws CreationException {
      QualifierValue result = new QualifierValueImpl();
      return result;
   }

   public Object createNewReadAttributeAction(Object[] params) throws CreationException {
      ReadAttributeAction result = new ReadAttributeActionImpl();
      return result;
   }

   public Object createNewReadExtentAction(Object[] params) throws CreationException {
      ReadExtentAction result = new ReadExtentActionImpl();
      return result;
   }

   public Object createNewReadIsClassifiedObjectAction(Object[] params) throws CreationException {
      ReadIsClassifiedObjectAction result = new ReadIsClassifiedObjectActionImpl();
      return result;
   }

   public Object createNewReadLinkAction(Object[] params) throws CreationException {
      ReadLinkAction result = new ReadLinkActionImpl();
      return result;
   }

   public Object createNewReadLinkObjectEndAction(Object[] params) throws CreationException {
      ReadLinkObjectEndAction result = new ReadLinkObjectEndActionImpl();
      return result;
   }

   public Object createNewReadLinkObjectQualifierAction(Object[] params) throws CreationException {
      ReadLinkObjectQualifierAction result = new ReadLinkObjectQualifierActionImpl();
      return result;
   }

   public Object createNewReadSelfAction(Object[] params) throws CreationException {
      ReadSelfAction result = new ReadSelfActionImpl();
      return result;
   }

   public Object createNewReadVariableAction(Object[] params) throws CreationException {
      ReadVariableAction result = new ReadVariableActionImpl();
      return result;
   }

   public Object createNewReception(Object[] params) throws CreationException {
      Reception result = new ReceptionImpl();
      return result;
   }

   public Object createNewReclassifyObjectAction(Object[] params) throws CreationException {
      ReclassifyObjectAction result = new ReclassifyObjectActionImpl();
      return result;
   }

   public Object createNewReduceAction(Object[] params) throws CreationException {
      ReduceAction result = new ReduceActionImpl();
      return result;
   }

   public Object createNewRemoveAttributeValueAction(Object[] params) throws CreationException {
      RemoveAttributeValueAction result = new RemoveAttributeValueActionImpl();
      return result;
   }

   public Object createNewRemoveVariableValueAction(Object[] params) throws CreationException {
      RemoveVariableValueAction result = new RemoveVariableValueActionImpl();
      return result;
   }

   public Object createNewSendSignalAction(Object[] params) throws CreationException {
      SendSignalAction result = new SendSignalActionImpl();
      return result;
   }

   public Object createNewSignal(Object[] params) throws CreationException {
      Signal result = new SignalImpl();
      return result;
   }

   public Object createNewSignalEvent(Object[] params) throws CreationException {
      SignalEvent result = new SignalEventImpl();
      return result;
   }

   public Object createNewSimpleState(Object[] params) throws CreationException {
      SimpleState result = new SimpleStateImpl();
      return result;
   }

   public Object createNewStartObjectStateMachineAction(Object[] params) throws CreationException {
      StartObjectStateMachineAction result = new StartObjectStateMachineActionImpl();
      return result;
   }

   public Object createNewState(Object[] params) throws CreationException {
      State result = new StateImpl();
      return result;
   }

   public Object createNewStateMachine(Object[] params) throws CreationException {
      StateMachine result = new StateMachineImpl();
      return result;
   }

   public Object createNewStereotype(Object[] params) throws CreationException {
      if (!(params[0] instanceof Model)) {
         throw new CreationException(params[0], "Stereotype");
      } else {
         Stereotype result = new StereotypeImpl();
         Model context = (Model)params[0];
         result.setAbstract(false);
         result.setLeaf(false);
         result.setRoot(false);
         result.setIcon((Geometry)null);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "newStereotype"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewStimulus(Object[] params) throws CreationException {
      Stimulus result = new StimulusImpl();
      return result;
   }

   public Object createNewStubState(Object[] params) throws CreationException {
      StubState result = new StubStateImpl();
      return result;
   }

   public Object createNewSubactivityState(Object[] params) throws CreationException {
      SubactivityState result = new SubactivityStateImpl();
      return result;
   }

   public Object createNewSubmachineState(Object[] params) throws CreationException {
      SubmachineState result = new SubmachineStateImpl();
      return result;
   }

   public Object createNewSubsystem(Object[] params) throws CreationException {
      if (!(params[0] instanceof Package)) {
         throw new CreationException(params[0], "Subsystem");
      } else {
         Subsystem result = new SubsystemImpl();
         Package context = (Package)params[0];
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewSubsystem"));
         ModelFactory.createElementOwnership(context, result);
         result.setAbstract(false);
         result.setRoot(false);
         result.setLeaf(false);
         result.setInstantiable(false);
         return result;
      }
   }

   public Object createNewSubsystemInstance(Object[] params) throws CreationException {
      SubsystemInstance result = new SubsystemInstanceImpl();
      return result;
   }

   public Object createNewSynchState(Object[] params) throws CreationException {
      SynchState result = new SynchStateImpl();
      return result;
   }

   public Object createNewSynchronousInvocationAction(Object[] params) throws CreationException {
      SynchronousInvocationAction result = new SynchronousInvocationActionImpl();
      return result;
   }

   public Object createNewTagDefinition(Object[] params) throws CreationException {
      if (!(params[0] instanceof Stereotype)) {
         throw new CreationException(params[0], "TagDefinition");
      } else {
         TagDefinition result = new TagDefinitionImpl();
         Stereotype context = (Stereotype)params[0];
         result.setOwner(context);
         result.setTagType("");
         result.setName(ModelFactory.getDefaultName((Collection)context.getCollectionDefinedTagList(), result, "newTagDefinition"));
         Multiplicity m = new MultiplicityImpl();
         MultiplicityRange mr = new MultiplicityRangeImpl();
         mr.setLower(1);
         mr.setUpper(new BigInteger("1"));
         m.addRange(mr);
         result.setMultiplicity(m);
         return result;
      }
   }

   public Object createNewTaggedValue(Object[] params) throws CreationException {
      if (!(params[0] instanceof TagDefinition)) {
         throw new CreationException(params[0], "TaggedValue");
      } else {
         TaggedValue result = new TaggedValueImpl();
         TagDefinition tagDef = (TagDefinition)params[0];
         result.setType(tagDef);
         result.setName(tagDef.getName());
         result.setDataValue("");
         return result;
      }
   }

   public Object createNewTemplateArgument(Object[] params) throws CreationException {
      TemplateArgument result = new TemplateArgumentImpl();
      return result;
   }

   public Object createNewTemplateParameter(Object[] params) throws CreationException {
      TemplateParameter result = new TemplateParameterImpl();
      return result;
   }

   public Object createNewTestIdentityAction(Object[] params) throws CreationException {
      TestIdentityAction result = new TestIdentityActionImpl();
      return result;
   }

   public Object createNewTimeEvent(Object[] params) throws CreationException {
      TimeEvent result = new TimeEventImpl();
      return result;
   }

   public Object createNewTransition(Object[] params) throws CreationException {
      Transition result = new TransitionImpl();
      return result;
   }

   public Object createNewUnmarshalAction(Object[] params) throws CreationException {
      UnmarshalAction result = new UnmarshalActionImpl();
      return result;
   }

   public Object createNewUsage(Object[] params) throws CreationException {
      Usage result = new UsageImpl();
      return result;
   }

   public Object createNewUseCase(Object[] params) throws CreationException {
      if (!(params[0] instanceof Model)) {
         throw new CreationException(params[0], "UseCase");
      } else {
         UseCase result = new UseCaseImpl();
         Model context = (Model)params[0];
         result.setAbstract(false);
         result.setLeaf(false);
         result.setRoot(false);
         result.setName(ModelFactory.getDefaultName((Namespace)context, result, "NewUseCase"));
         ModelFactory.createElementOwnership(context, result);
         return result;
      }
   }

   public Object createNewUseCaseInstance(Object[] params) throws CreationException {
      UseCaseInstance result = new UseCaseInstanceImpl();
      return result;
   }

   public Object createNewVariable(Object[] params) throws CreationException {
      Variable result = new VariableImpl();
      return result;
   }

   public Object createNewWriteAttributeAction(Object[] params) throws CreationException {
      WriteAttributeAction result = new WriteAttributeActionImpl();
      return result;
   }

   public Object createNewWriteLink(Object[] params) throws CreationException {
      WriteLinkAction result = new WriteLinkActionImpl();
      return result;
   }
}
