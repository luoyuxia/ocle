package ro.ubbcluj.lci.ocl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.eval.OclDirect;
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
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Event;
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
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.uml.foundation.core.StructuralFeature;
import ro.ubbcluj.lci.uml.foundation.core.TemplateParameter;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.PackageImpl;

public class OclUserModel {
   public static final ModelElement ALLINSTANCES = new ModelElementImpl();
   private ModelElement model;
   private Vector result = null;
   private int countAll;

   public OclUserModel(String name) {
      this.model = OclLoader.XML2Model(name);
      OclUtil.usermodel = this;
   }

   public OclUserModel(ModelElement p_model) {
      this.model = p_model;
      OclUtil.usermodel = this;
   }

   private static void sort(Vector result) {
      Collections.sort(result, new Comparator() {
         public int compare(Object a, Object b) {
            String sa = ((OclUserElement)a).toString();
            String sb = ((OclUserElement)b).toString();
            return sa.compareTo(sb);
         }
      });
   }

   public Vector getAllInstances(Class cls) {
      ModelElement oldmodel = this.model;

      while(true) {
         ElementOwnership eo = this.model.getNamespace();
         if (eo == null) {
            break;
         }

         Namespace ns = eo.getNamespace();
         if (ns == null) {
            break;
         }

         this.model = ns;
      }

      Vector rezz = this.getInstances(cls);
      this.model = oldmodel;
      return rezz;
   }

   public Vector getInstances(Class cls, boolean exact, boolean sorted) {
      if (this.result == null) {
         this.result = new Vector();
         this.countAll = 0;
         this.rec(this.model);
         if (sorted) {
            sort(this.result);
         }
      }

      Vector rez = new Vector();
      Iterator it = this.result.iterator();

      while(it.hasNext()) {
         OclUserElement ue = (OclUserElement)it.next();
         if (cls.isInstance(ue.getElement())) {
            rez.add(ue);
         }
      }

      return rez;
   }

   public Vector getInstances(Class cls) {
      return this.getInstances(cls, false, false);
   }

   public Vector getInstances(Class cls, boolean sorted) {
      return this.getInstances(cls, false, sorted);
   }

   private void rec(Object elem) {
      if (elem != null) {
         ++this.countAll;
         this.result.add(new OclUserElement(elem, this.getPath(elem)));
         if (elem instanceof Association) {
            this.recEnum(((Association)elem).getConnectionList(), elem);
         }

         if (elem instanceof Classifier) {
            this.recEnum(((Classifier)elem).getFeatureList(), elem);
         }

         if (elem instanceof Namespace) {
            this.recEnum((new OclDirect((Namespace)elem)).directGetOwnedElementList(), elem);
         }

         if (elem instanceof ModelElement) {
            this.rec(((ModelElement)elem).getNamespace());
         }

         if (elem instanceof BehavioralFeature) {
            this.recEnum(((BehavioralFeature)elem).getParameterList(), elem);
         }

         if (elem instanceof AssociationEnd) {
            this.recEnum(((AssociationEnd)elem).getQualifierList(), elem);
         }

         if (elem instanceof ModelElement) {
            this.recEnum(((ModelElement)elem).getParameterTemplateList(), elem);
         }

         if (elem instanceof TemplateParameter) {
            this.rec(((TemplateParameter)elem).getParameterTemplate());
         }

         if (elem instanceof Multiplicity) {
            this.recEnum(((Multiplicity)elem).getRangeList(), elem);
         }

         if (elem instanceof Stereotype) {
            this.recEnum(((Stereotype)elem).getDefinedTagList(), elem);
         }

         if (elem instanceof Stereotype) {
            this.recEnum(((Stereotype)elem).getStereotypeConstraintList(), elem);
         }

         if (elem instanceof ModelElement) {
            this.recEnum(((ModelElement)elem).getTaggedValueList(), elem);
         }

         if (elem instanceof TaggedValue) {
            this.rec(((TaggedValue)elem).getType());
         }

         if (elem instanceof Instance) {
            this.recEnum(((Instance)elem).getSlotList(), elem);
         }

         if (elem instanceof Link) {
            this.recEnum(((Link)elem).getConnectionList(), elem);
         }

         if (elem instanceof LinkEnd) {
            this.recEnum(((LinkEnd)elem).getQualifierValueList(), elem);
         }

         if (elem instanceof State) {
            this.rec(((State)elem).getEntry());
         }

         if (elem instanceof State) {
            this.rec(((State)elem).getExit());
         }

         if (elem instanceof Event) {
            this.recEnum(((Event)elem).getParameterList(), elem);
         }

         if (elem instanceof Transition) {
            this.rec(((Transition)elem).getGuard());
         }

         if (elem instanceof StateMachine) {
            this.rec(((StateMachine)elem).getTop());
         }

         if (elem instanceof CompositeState) {
            this.recEnum(((CompositeState)elem).getSubvertexList(), elem);
         }

         if (elem instanceof Transition) {
            this.rec(((Transition)elem).getEffect());
         }

         if (elem instanceof State) {
            this.recEnum(((State)elem).getInternalTransitionList(), elem);
         }

         if (elem instanceof StateMachine) {
            this.recEnum(((StateMachine)elem).getTransitionsList(), elem);
         }

         if (elem instanceof State) {
            this.rec(((State)elem).getDoActivity());
         }

         if (elem instanceof Interaction) {
            this.recEnum(((Interaction)elem).getMessageList(), elem);
         }

         if (elem instanceof Collaboration) {
            this.recEnum(((Collaboration)elem).getInteractionList(), elem);
         }

         if (elem instanceof ActivityGraph) {
            this.recEnum(((ActivityGraph)elem).getPartitionList(), elem);
         }

         if (elem instanceof StructuralFeature) {
            this.rec(((StructuralFeature)elem).getMultiplicity());
         }

         if (elem instanceof AssociationEnd) {
            this.rec(((AssociationEnd)elem).getMultiplicity());
         }

         if (elem instanceof Attribute) {
            this.rec(((Attribute)elem).getInitialValue());
         }

         if (elem instanceof Parameter) {
            this.rec(((Parameter)elem).getDefaultValue());
         }

         if (elem instanceof Constraint) {
            this.rec(((Constraint)elem).getBody());
         }

         if (elem instanceof Abstraction) {
            this.rec(((Abstraction)elem).getMapping());
         }

         if (elem instanceof ClassifierRole) {
            this.rec(((ClassifierRole)elem).getMultiplicity());
         }

         if (elem instanceof AssociationRole) {
            this.rec(((AssociationRole)elem).getMultiplicity());
         }

         if (elem instanceof Operation) {
            this.rec(((Operation)elem).getSpecification());
         }

         if (elem instanceof AssociationEndRole) {
            this.rec(((AssociationEndRole)elem).getCollaborationMultiplicity());
         }

         if (elem instanceof Component) {
            this.recEnum(((Component)elem).getResidentList(), elem);
         }

      }
   }

   private void recEnum(Enumeration en, Object elem) {
      while(en.hasMoreElements()) {
         Object obj = en.nextElement();
         if (obj != elem) {
            this.rec(obj);
         }
      }

   }

   public Vector getInstancesReflection(Class cls, boolean exact, boolean sorted) {
      return this.getInstancesReflection(cls, sorted);
   }

   public Vector getInstancesReflection(Class cls, boolean sorted) {
      this.reflectionNavigation(this.model, cls, sorted);
      if (sorted) {
         sort(this.result);
      }

      return this.result;
   }

   public Vector getInstancesReflection(Class cls) {
      return this.getInstancesReflection(cls, false);
   }

   private String getPath(Object elem) {
      String path = "";

      while(elem instanceof ModelElement) {
         if (elem instanceof Feature) {
            elem = ((Feature)elem).getOwner();
         } else {
            elem = ((ModelElement)elem).getNamespace();
            if (elem instanceof ElementOwnership) {
               elem = ((ElementOwnership)elem).getNamespace();
            }
         }

         if (elem instanceof ModelElement && !(elem instanceof Model)) {
            String sep = elem instanceof PackageImpl ? "::" : ".";
            path = ((ModelElement)elem).getName() + sep + path;
         }
      }

      return path;
   }

   private boolean isUmlElement(Class pcls) {
      return pcls.getName().startsWith("ro.ubbcluj.lci.uml.");
   }

   private boolean isUmlElement(Object obj) {
      return obj == null ? false : this.isUmlElement(obj.getClass());
   }

   private Vector reflectionNavigation(Object start, Class cls, boolean sorted) {
      Object[] queue = new Object[1000000];
      int head = 0;
      int tail = 0;
      queue[0] = start;
      HashSet mark = new HashSet();
      mark.add(start);

      while(head <= tail) {
         Object elem = queue[head++];
         Method[] methods = elem.getClass().getMethods();

         for(int i = 0; i < methods.length; ++i) {
            if (methods[i].getName().startsWith("get") && methods[i].getParameterTypes().length == 0 && (methods[i].getReturnType() == (Enumeration.class) || this.isUmlElement(methods[i].getReturnType()))) {
               try {
                  Object obj = methods[i].invoke(elem, null);
                  if (obj instanceof Enumeration) {
                     while(((Enumeration)obj).hasMoreElements()) {
                        Object obj2 = ((Enumeration)obj).nextElement();
                        if (this.isUmlElement(obj2) && mark.add(obj2)) {
                           ++tail;
                           queue[tail] = obj2;
                        }
                     }
                  }

                  if (this.isUmlElement(obj) && mark.add(obj)) {
                     ++tail;
                     queue[tail] = obj;
                  }
               } catch (InvocationTargetException var13) {
               } catch (IllegalAccessException var14) {
               }
            }
         }
      }

      Vector result = new Vector();

      for(int i = 0; i <= tail; ++i) {
         if (cls.isInstance(queue[i])) {
            result.add(new OclUserElement(queue[i], sorted ? this.getPath(queue[i]) : ""));
         }
      }

      return result;
   }

   public static Vector getObjectInstances(Classifier cls, boolean sorted) {
      HashSet temp = new HashSet();
      getObjectInstances(cls, temp);
      Vector result = new Vector();
      Iterator it = temp.iterator();

      while(it.hasNext()) {
         result.add(new OclUserElement(it.next(), ""));
      }

      if (sorted) {
         sort(result);
      }

      return result;
   }

   private static void getObjectInstances(Classifier cls, HashSet result) {
      Enumeration en = cls.getInstanceList();

      while(en.hasMoreElements()) {
         result.add(en.nextElement());
      }

      en = cls.getSpecializationList();

      while(en.hasMoreElements()) {
         GeneralizableElement ge = ((Generalization)en.nextElement()).getChild();
         if (ge instanceof Classifier) {
            getObjectInstances((Classifier)ge, result);
         }
      }

   }

   public Model getUMLModel() {
      return this.model instanceof Model ? (Model)this.model : null;
   }

   static {
      ALLINSTANCES.setName("All instances");
   }
}
