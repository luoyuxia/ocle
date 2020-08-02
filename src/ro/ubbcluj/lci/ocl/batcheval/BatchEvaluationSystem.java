package ro.ubbcluj.lci.ocl.batcheval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.errors.ErrorListener;
import ro.ubbcluj.lci.errors.ErrorMessage;
import ro.ubbcluj.lci.errors.ErrorSource;
import ro.ubbcluj.lci.errors.EvaluationErrorMessage;
import ro.ubbcluj.lci.errors.EvaluationException;
import ro.ubbcluj.lci.gui.tools.tree.AbstractModelCache;
import ro.ubbcluj.lci.gui.tools.tree.SelectionTreeNode;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.datatypes.OclBoolean;
import ro.ubbcluj.lci.ocl.datatypes.Undefined;
import ro.ubbcluj.lci.ocl.eval.ExceptionEvaluate;
import ro.ubbcluj.lci.ocl.eval.OclConstant;
import ro.ubbcluj.lci.ocl.nodes.OclROOT;
import ro.ubbcluj.lci.ocl.nodes.classifierContext;
import ro.ubbcluj.lci.ocl.nodes.constraint;
import ro.ubbcluj.lci.ocl.nodes.oclExpression;
import ro.ubbcluj.lci.ocl.nodes.oclExpressions;
import ro.ubbcluj.lci.ocl.nodes.oclFile;
import ro.ubbcluj.lci.ocl.nodes.oclPackage;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ActivityGraph;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Interaction;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CompositeState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Transition;
import ro.ubbcluj.lci.uml.foundation.core.Abstraction;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Component;
import ro.ubbcluj.lci.uml.foundation.core.Constraint;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.uml.foundation.core.StructuralFeature;
import ro.ubbcluj.lci.uml.foundation.core.TemplateParameter;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.utils.InterruptibleTask;
import ro.ubbcluj.lci.utils.Utils;
import ro.ubbcluj.lci.utils.progress.ProgressEventSource;
import ro.ubbcluj.lci.utils.progress.ProgressListener;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class BatchEvaluationSystem implements ProgressEventSource, ErrorSource {
   private HashMap userModelPackages;
   private Model userModel;
   private AbstractModelCache userModelCache;
   private AbstractModelCache metamodelCache;
   private EventListenerList listeners;
   private long total;
   private long performed;
   private List wfrErrorMessages = new ArrayList();
   private List bcrErrorMessages = new ArrayList();
   private List errorMessages;
   private ArrayList metamodelGeneralizations = new ArrayList();
   private ArrayList userModelGeneralizations = new ArrayList();
   private OclNode currentRule;
   private Object currentContext;
   private InterruptibleTask evalTask = null;
   private boolean clearedData = true;
   private CustomizationDialog dialog;

   public BatchEvaluationSystem(Model umlapi, JFrame parentFrame) throws Exception {
      this.dialog = new CustomizationDialog(parentFrame);
      this.listeners = new EventListenerList();
      this.metamodelCache = new AbstractModelCache();
      this.metamodelCache.nodeFactory = new SelectionTreeNodeFactory();
      this.userModelCache = new AbstractModelCache();
      this.userModelCache.nodeFactory = this.metamodelCache.nodeFactory;
      this.userModelPackages = new HashMap();
      this.setUmlApi(umlapi);
      this.updateGeneralizations(umlapi, this.metamodelCache, this.metamodelGeneralizations);
   }

   public void clearData() {
      if (!this.clearedData) {
         this.clearedData = true;
         this.userModelCache.clear();
         this.clearRules();
         this.dialog.defaultUserModel();
         this.userModelGeneralizations.clear();
         Iterator it = this.metamodelGeneralizations.iterator();

         while(it.hasNext()) {
            LocalGeneralization lg = (LocalGeneralization)it.next();
            lg.child.clearInstances();
            lg.parent.clearInstances();
         }

         this.currentContext = null;
         this.wfrErrorMessages.clear();
         this.bcrErrorMessages.clear();
         this.currentRule = null;
         this.userModelPackages.clear();
         this.userModel = null;
      }
   }

   public void setUserModel(Model uml) {
      this.clearData();
      DefaultMutableTreeNode umn = this.userModelCache.loadTree(uml);
      this.dialog.appendUserModel(umn);
      this.clearedData = false;
      this.userModel = uml;
      this.updateGeneralizations(this.userModel, this.userModelCache, this.userModelGeneralizations);
      this.loadUserModelTree();
   }

   public Model getUserModel() {
      return this.userModel;
   }

   public CustomizationDialog getCustomizationDialog() {
      return this.dialog;
   }

   public void addEvaluationListener(EvaluationListener el) {
      this.listeners.add(EvaluationListener.class, el);
   }

   public void removeEvaluationListener(EvaluationListener el) {
      this.listeners.remove(EvaluationListener.class, el);
   }

   public void addWellFormednessRules(OclNode rules) {
      this.addRules(rules, this.metamodelCache);
   }

   public void setBusinessConstraintRules(OclNode rules) {
      this.addRules(rules, this.userModelCache);
   }

   public InterruptibleTask getTask() {
      return this.evalTask = new BatchEvaluationSystem.EvaluationTask();
   }

   public List getWfrErrorMessages() {
      return Collections.unmodifiableList(this.wfrErrorMessages);
   }

   public List getBcrErrorMessages() {
      return Collections.unmodifiableList(this.bcrErrorMessages);
   }

   public void addProgressListener(ProgressListener pl) {
      this.listeners.add(ProgressListener.class, pl);
   }

   public void removeProgressListener(ProgressListener pl) {
      this.listeners.remove(ProgressListener.class, pl);
   }

   public void progressValueChanged(int progress, Object details) {
      Object[] ls = this.listeners.getListeners(ProgressListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((ProgressListener)ls[i]).progressValueChanged(progress, details);
      }

   }

   public void addErrorListener(ErrorListener l) {
      this.listeners.add(ErrorListener.class, l);
   }

   public void removeErrorListener(ErrorListener l) {
      this.listeners.remove(ErrorListener.class, l);
   }

   public void errorOccured(ErrorMessage msg) {
      Object[] ls = this.listeners.getListeners(ErrorListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((ErrorListener)ls[i]).errorOccured(msg);
      }

   }

   private void setUmlApi(Model umlapi) {
      DefaultMutableTreeNode umlapiNode = this.metamodelCache.loadTree(umlapi);
      this.dialog.loadReferencedModelsTree(umlapiNode);
   }

   private void clearInstances() {
      Iterator classes = this.metamodelCache.classes.values().iterator();

      while(classes.hasNext()) {
         ClassNode node = (ClassNode)classes.next();
         node.clearInstances();
      }

   }

   private void addRules(OclNode root, AbstractModelCache cache) {
      if (root != null) {
         List q = new ArrayList();
         q.add(root);

         while(!q.isEmpty()) {
            OclNode node = (OclNode)q.remove(0);
            int m = node.getChildCount();
            OclNode nd;
            if (node instanceof oclExpression) {
               nd = node.getParent().getChild(0).getChild(1);
               if (nd instanceof classifierContext) {
                  Classifier context = OclUtil.getContext(node);
                  ClassNode mn = (ClassNode)cache.addNode(context);
                  RuleNode x = new RuleNode((oclExpression)node);
                  mn.addRule(x);
                  mn.add(x);
               }
            }

            for(int i = 0; i < m; ++i) {
               nd = node.getChild(i);
               if (nd instanceof oclFile || nd instanceof OclROOT || nd instanceof oclPackage || nd instanceof constraint || nd instanceof oclExpressions || nd instanceof oclExpression) {
                  q.add(nd);
               }
            }
         }

         this.dialog.reloadReferencedModelsTree();
      }
   }

   private void loadUserModelTree() {
      DefaultMutableTreeNode userModelTreeRoot = this.loadUserModelTree2(this.userModel);
      this.dialog.loadUserModelTree(userModelTreeRoot);
   }

   private SelectionTreeNode loadUserModelTree2(Package source) {
      SelectionTreeNode result = new PackageNode(source);
      this.userModelPackages.put(UMLUtilities.getFullyQualifiedName((ModelElement)source), result);
      Enumeration lst = source.directGetOwnedElementList();

      while(lst.hasMoreElements()) {
         ModelElement elem = (ModelElement)lst.nextElement();
         if (elem instanceof Package) {
            Package p = (Package)elem;
            result.add(this.loadUserModelTree2(p));
         }
      }

      return result;
   }

   private void clearRules() {
      Iterator nodes = this.metamodelCache.classes.values().iterator();

      while(nodes.hasNext()) {
         ClassNode nd = (ClassNode)nodes.next();
         if (nd.getParent() != null) {
            nd.removeFromParent();
         }

         nd.removeAllChildren();
         nd.clearRules();
      }

   }

   private void evaluationCancelled() {
      EvaluationEvent evt = new EvaluationEvent(this, this.total, 0L, 0L);
      EventListener[] ls = this.listeners.getListeners(EvaluationListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((EvaluationListener)ls[i]).evaluationCancelled(evt);
      }

   }

   private void evaluationCompleted() {
      EvaluationEvent evt = new EvaluationEvent(this, this.total, this.performed, (long)(this.bcrErrorMessages.size() + this.wfrErrorMessages.size()));
      EventListener[] ls = this.listeners.getListeners(EvaluationListener.class);

      for(int i = 0; i < ls.length; ++i) {
         ((EvaluationListener)ls[i]).evaluationCompleted(evt);
      }

   }

   private void evaluateForClass(ClassNode node) {
      List instances = node.getInstances();
      List rules = node.getRules();
      if (instances.size() > 0) {
         Iterator it1 = rules.iterator();

         while(it1.hasNext() && !this.evalTask.isCancelled()) {
            RuleNode rn = (RuleNode)it1.next();
            if (rn.isSelected()) {
               this.currentRule = rn.getRule();
               Iterator it2 = instances.iterator();

               while(it2.hasNext() && !this.evalTask.isCancelled()) {
                  this.currentContext = it2.next();
                  this.evaluateSelection();
               }
            }
         }

      }
   }

   private void evaluateSelection() {
      ErrorMessage msg = null;
      ((OclConstant)this.currentRule.parent.getChild(0).getChild(1).evnode).value = this.currentContext;

      try {
         Object result = this.currentRule.evnode.evaluate();
         if (result instanceof OclBoolean && ((OclBoolean)result).isFalse()) {
            msg = new EvaluationErrorMessage(this.currentRule.getFilename(), this.currentContext, this.currentRule);
         } else if (result instanceof Undefined) {
            msg = new EvaluationException(this.currentRule.getFilename(), this.currentContext, this.currentRule, "Result is undefined");
         }
      } catch (ExceptionEvaluate var4) {
         msg = new EvaluationException(this.currentRule.getFilename(), this.currentContext, this.currentRule, var4.getMessage());
      }

      ++this.performed;
      this.progressValueChanged((int)(100L * this.performed / this.total), (Object)null);
      if (msg != null) {
         this.errorMessages.add(msg);
         this.errorOccured((ErrorMessage)msg);
      }

   }

   private void calculateTotal() {
      this.total = 0L;
      Iterator it = this.metamodelCache.classes.values().iterator();
      int level = 1;
      boolean finish = false;

      do {
         if (!it.hasNext()) {
            if (level == 1) {
               it = this.userModelCache.classes.values().iterator();
               level = 2;
               if (!it.hasNext()) {
                  finish = true;
               }
            } else {
               finish = true;
            }
         }

         if (!finish) {
            ClassNode node = (ClassNode)it.next();
            int s = 0;
            int i = node.getInstances().size();
            if (i > 0) {
               List lst = node.getRules();
               int ss = lst.size();

               for(int j = 0; j < ss; ++j) {
                  if (((RuleNode)lst.get(j)).isSelected()) {
                     ++s;
                  }
               }

               if (s > 0) {
                  this.total += (long)(s * i);
               }
            }
         }
      } while(!finish);

   }

   private void buildInstanceTable() {
      Iterator p = this.userModelPackages.values().iterator();
      this.clearInstances();

      while(p.hasNext() && !this.evalTask.isCancelled()) {
         PackageNode node = (PackageNode)p.next();
         if (node.isSelected()) {
            this.updateInstanceLists((Object)node.getPackage(), (ClassNode)null);
         }
      }

   }

   private void updateInstanceLists(Object start, ClassNode nd) {
      if (start != null) {
         Class cls = start.getClass();
         String key = Utils.toUml(cls);
         if (nd == null) {
            nd = (ClassNode)this.metamodelCache.classes.get(key);
         }

         if (nd == null) {
            throw new RuntimeException("Metaclass not registered:" + key);
         } else {
            nd.addInstance(start);
            ClassNode trn;
            if (start instanceof ModelElement) {
               trn = (ClassNode)this.metamodelCache.classes.get("Foundation::Extension Mechanisms::TaggedValue");
               ModelElement m = (ModelElement)start;
               this.updateInstanceLists((Object)m.getNamespace(), (ClassNode)null);
               this.updateInstanceLists((Collection)m.getCollectionTaggedValueList(), trn);
               this.updateInstanceLists((Collection)m.getCollectionParameterTemplateList(), (ClassNode)null);
            }

            if (start instanceof Abstraction) {
               this.updateInstanceLists((Object)((Abstraction)start).getMapping(), (ClassNode)null);
            }

            if (start instanceof ActivityGraph) {
               trn = (ClassNode)this.metamodelCache.classes.get("Behavioral Elements::Activity Graphs::Partition");
               this.updateInstanceLists((Collection)((ActivityGraph)start).getCollectionPartitionList(), trn);
            }

            if (start instanceof Association) {
               trn = (ClassNode)this.metamodelCache.classes.get("Foundation::Core::AssociationEnd");
               this.updateInstanceLists((Collection)((Association)start).getCollectionConnectionList(), trn);
            }

            if (start instanceof AssociationEnd) {
               this.updateInstanceLists((Object)((AssociationEnd)start).getMultiplicity(), (ClassNode)null);
            }

            if (start instanceof AssociationEndRole) {
               this.updateInstanceLists((Object)((AssociationEndRole)start).getCollaborationMultiplicity(), (ClassNode)null);
            }

            if (start instanceof AssociationRole) {
               this.updateInstanceLists((Object)((AssociationRole)start).getMultiplicity(), (ClassNode)null);
            }

            if (start instanceof Attribute) {
               this.updateInstanceLists((Object)((Attribute)start).getInitialValue(), (ClassNode)null);
            }

            Classifier c;
            if (start instanceof Classifier) {
               c = (Classifier)start;
               this.updateInstanceLists((Collection)c.getCollectionFeatureList(), (ClassNode)null);
            }

            if (start instanceof BehavioralFeature) {
               trn = (ClassNode)this.metamodelCache.classes.get("Foundation::Core::Parameter");
               this.updateInstanceLists((Collection)((BehavioralFeature)start).getCollectionParameterList(), trn);
            }

            if (start instanceof ClassifierRole) {
               this.updateInstanceLists((Object)((ClassifierRole)start).getMultiplicity(), (ClassNode)null);
            }

            if (start instanceof Collaboration) {
               trn = (ClassNode)this.metamodelCache.classes.get("Behavioral Elements::Collaborations::Interaction");
               this.updateInstanceLists((Collection)((Collaboration)start).getCollectionInteractionList(), trn);
            }

            if (start instanceof Component) {
               this.updateInstanceLists((Collection)((Component)start).getCollectionResidentList(), (ClassNode)null);
            }

            if (start instanceof CompositeState) {
               this.updateInstanceLists((Collection)((CompositeState)start).getCollectionSubvertexList(), (ClassNode)null);
            }

            if (start instanceof Constraint) {
               this.updateInstanceLists((Object)((Constraint)start).getBody(), (ClassNode)null);
            }

            ClassNode cn;
            if (start instanceof ro.ubbcluj.lci.uml.foundation.core.Class) {
               c = (Classifier)start;
               cn = (ClassNode)this.userModelCache.classes.get(UMLUtilities.getFullyQualifiedName((ModelElement)c));
               Iterator it = c.getCollectionInstanceList().iterator();

               while(it.hasNext()) {
                  ModelElement inst = (ModelElement)it.next();

                  Object nms;
                  for(nms = inst.directGetNamespace(); nms instanceof Collaboration; nms = ((Namespace)nms).directGetNamespace()) {
                  }

                  if (nms == null) {
                     nms = this.userModel;
                  }

                  PackageNode parentNode = (PackageNode)this.userModelPackages.get(UMLUtilities.getFullyQualifiedName((ModelElement)nms));
                  if (parentNode != null && parentNode.isSelected()) {
                     cn.addInstance(inst);
                  }
               }
            }

            if (start instanceof Instance) {
               trn = (ClassNode)this.metamodelCache.classes.get("Behavioral Elements::Common Behavior::AttributeLink");
               this.updateInstanceLists((Collection)((Instance)start).getCollectionSlotList(), trn);
            }

            if (start instanceof Interaction) {
               trn = (ClassNode)this.metamodelCache.classes.get("Behavioral Elements::Collaborations::Message");
               this.updateInstanceLists((Collection)((Interaction)start).getCollectionMessageList(), trn);
            }

            if (start instanceof Link) {
               trn = (ClassNode)this.metamodelCache.classes.get("Behavioral Elements::Common Behavior::LinkEnd");
               this.updateInstanceLists((Collection)((Link)start).getCollectionConnectionList(), trn);
            }

            if (start instanceof LinkEnd) {
               trn = (ClassNode)this.metamodelCache.classes.get("Behavioral Elements::Common Behavior::AttributeLink");
               this.updateInstanceLists((Collection)((LinkEnd)start).getCollectionQualifierValueList(), trn);
            }

            if (start instanceof Multiplicity) {
               trn = (ClassNode)this.metamodelCache.classes.get("Foundation::Data Types::MultiplicityRange");
               this.updateInstanceLists((Collection)((Multiplicity)start).getCollectionRangeList(), trn);
            }

            if (start instanceof Namespace) {
               this.updateInstanceLists((Collection)((Namespace)start).directGetCollectionOwnedElementList(), (ClassNode)null);
            }

            if (start instanceof Parameter) {
               this.updateInstanceLists((Object)((Parameter)start).getDefaultValue(), (ClassNode)null);
            }

            if (start instanceof State) {
               trn = (ClassNode)this.metamodelCache.classes.get("Behavioral Elements::State Machines::Transition");
               State s = (State)start;
               this.updateInstanceLists((Object)s.getEntry(), (ClassNode)null);
               this.updateInstanceLists((Object)s.getExit(), (ClassNode)null);
               this.updateInstanceLists((Collection)s.getCollectionInternalTransitionList(), trn);
               this.updateInstanceLists((Object)s.getDoActivity(), (ClassNode)null);
            }

            if (start instanceof StateMachine) {
               StateMachine sm = (StateMachine)start;
               cn = (ClassNode)this.metamodelCache.classes.get("Behavioral Elements::State Machines::Transition");
               this.updateInstanceLists((Object)sm.getTop(), (ClassNode)null);
               this.updateInstanceLists((Collection)sm.getCollectionTransitionsList(), cn);
            }

            if (start instanceof Stereotype) {
               Stereotype st = (Stereotype)start;
               cn = (ClassNode)this.metamodelCache.classes.get("Foundation::Core::Constraint");
               this.updateInstanceLists((Collection)st.getCollectionDefinedTagList(), (ClassNode)null);
               this.updateInstanceLists((Collection)st.getCollectionStereotypeConstraintList(), cn);
            }

            if (start instanceof StructuralFeature) {
               this.updateInstanceLists((Object)((StructuralFeature)start).getMultiplicity(), (ClassNode)null);
            }

            if (start instanceof TaggedValue) {
               this.updateInstanceLists((Object)((TaggedValue)start).getType(), (ClassNode)null);
            }

            if (start instanceof TemplateParameter) {
               this.updateInstanceLists((Object)((TemplateParameter)start).getParameterTemplate(), (ClassNode)null);
            }

            if (start instanceof Transition) {
               Transition t = (Transition)start;
               this.updateInstanceLists((Object)t.getGuard(), (ClassNode)null);
               this.updateInstanceLists((Object)t.getEffect(), (ClassNode)null);
            }

         }
      }
   }

   private void updateInstanceLists(Collection elements, ClassNode nd) {
      if (elements != null) {
         Iterator it = elements.iterator();

         while(it.hasNext()) {
            Object next = it.next();
            if (!(next instanceof Package)) {
               this.updateInstanceLists(next, nd);
            }
         }

      }
   }

   private void inheritRules() {
      this.inheritRules(this.metamodelGeneralizations);
      this.inheritRules(this.userModelGeneralizations);
   }

   private void inheritRules(ArrayList gens) {
      int i = 0;

      for(int s = gens.size(); i < s; ++i) {
         LocalGeneralization lg = (LocalGeneralization)gens.get(i);
         Iterator it = lg.parent.getRules().iterator();

         while(it.hasNext()) {
            lg.child.addRule((RuleNode)it.next());
         }
      }

   }

   private void updateGeneralizations(ClassNode childNode, AbstractModelCache c, ArrayList gens) {
      ro.ubbcluj.lci.uml.foundation.core.Class child = (ro.ubbcluj.lci.uml.foundation.core.Class)childNode.getMetaclass();
      Iterator parents = child.parent().iterator();
      int currentIndex = gens.size();

      while(true) {
         ClassNode parentNode;
         do {
            if (!parents.hasNext()) {
               return;
            }

            ModelElement parent = (ModelElement)parents.next();
            parentNode = (ClassNode)c.classes.get(UMLUtilities.getFullyQualifiedName(parent));
         } while(parentNode == null);

         LocalGeneralization lg = new LocalGeneralization();
         lg.parent = parentNode;
         lg.child = childNode;

         for(int i = currentIndex - 1; i >= 0; --i) {
            if (((LocalGeneralization)gens.get(i)).parent == childNode) {
               currentIndex = i;
            }
         }

         gens.add(currentIndex, lg);
      }
   }

   private void updateGeneralizations(Model mdl, AbstractModelCache c, ArrayList gens) {
      ArrayList q = new ArrayList();
      q.add(mdl);

      while(!q.isEmpty()) {
         Object x = q.remove(0);
         if (x instanceof Namespace) {
            if (x instanceof ro.ubbcluj.lci.uml.foundation.core.Class) {
               ClassNode cn = (ClassNode)c.classes.get(UMLUtilities.getFullyQualifiedName((ModelElement)((ro.ubbcluj.lci.uml.foundation.core.Class)x)));
               if (cn != null) {
                  this.updateGeneralizations(cn, c, gens);
               }
            }

            q.addAll(((Namespace)x).directGetCollectionOwnedElementList());
         }
      }

   }

   private class EvaluationTask extends InterruptibleTask {
      private EvaluationTask() {
      }

      public void realRun() {
         Iterator it = BatchEvaluationSystem.this.metamodelCache.classes.values().iterator();
         boolean level = true;
         BatchEvaluationSystem.this.bcrErrorMessages.clear();
         BatchEvaluationSystem.this.wfrErrorMessages.clear();
         BatchEvaluationSystem.this.performed = 0L;
         BatchEvaluationSystem.this.buildInstanceTable();
         BatchEvaluationSystem.this.inheritRules();
         BatchEvaluationSystem.this.calculateTotal();
         if (BatchEvaluationSystem.this.total == 0L) {
            BatchEvaluationSystem.this.evaluationCompleted();
         } else {
            if (!this.isCancelled) {
               BatchEvaluationSystem.this.errorMessages = BatchEvaluationSystem.this.wfrErrorMessages;

               while(!this.isCancelled) {
                  if (!it.hasNext()) {
                     if (!level) {
                        break;
                     }

                     it = BatchEvaluationSystem.this.userModelCache.classes.values().iterator();
                     if (!it.hasNext()) {
                        break;
                     }

                     level = true;
                     BatchEvaluationSystem.this.errorMessages = BatchEvaluationSystem.this.bcrErrorMessages;
                  }

                  ClassNode node = (ClassNode)it.next();
                  BatchEvaluationSystem.this.evaluateForClass(node);
               }

               BatchEvaluationSystem.this.evaluationCompleted();
            } else {
               BatchEvaluationSystem.this.evaluationCancelled();
            }

         }
      }
   }
}
