package ro.ubbcluj.lci.ocl.codegen.norm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.utils.InterruptibleTask;
import ro.ubbcluj.lci.utils.NamingContext;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class DefaultCodePreparator extends InterruptibleTask {
   private HashMap featureContexts;
   private NormalForm normalForm;

   public DefaultCodePreparator() {
      this.featureContexts = new HashMap();
      this.isCancelled = false;
   }

   public DefaultCodePreparator(NormalForm nf) {
      this();
      this.normalForm = nf;
   }

   public void realRun() {
      this.prepare(this.normalForm);
   }

   public void prepare(NormalForm nf) {
      ConstrainedClass[] cClasses = nf.getConstrainedClasses();

      int i;
      for(i = 0; i < cClasses.length; ++i) {
         if (this.isCancelled) {
            return;
         }

         this.updateCodeInfo(cClasses[i], nf);
      }

      ConstrainedOperation[] cOperations = nf.getConstrainedOperations();

      for(i = 0; i < cOperations.length; ++i) {
         if (this.isCancelled) {
            return;
         }

         this.updateCodeInfo(cOperations[i]);
      }

   }

   private void updateCodeInfo(ConstrainedClass cc, NormalForm nf) {
      Classifier cls = cc.getConstrainedClass();
      String fName = UMLUtilities.getFullyQualifiedName((ModelElement)cls);
      NamingContext features = new NamingContext();
      NamingContext inner_cls = new NamingContext();
      NamingContext cNames = new NamingContext();
      ConstrainedClass parent = null;
      this.featureContexts.put(fName, features);
      Iterator fs = cls.getCollectionFeatureList().iterator();

      while(fs.hasNext()) {
         Object f = fs.next();
         if (f instanceof BehavioralFeature) {
            features.add(((BehavioralFeature)f).getName());
         }
      }

      if (!this.isCancelled) {
         AbstractConstraintNode[] constr = cc.getConstraints();

         int i;
         for(i = 0; i < constr.length; ++i) {
            if (constr[i] instanceof DefinitionConstraintNode) {
               features.add(constr[i].getName());
            } else if (constr[i].getName() != null) {
               cNames.add(constr[i].getName());
            }
         }

         if (!this.isCancelled) {
            Iterator parents = cls.parent().iterator();

            while(parents.hasNext()) {
               Classifier p = (Classifier)parents.next();
               parent = nf.getFor(p);
               if (parent != null) {
                  break;
               }
            }

            if (!this.isCancelled) {
               Iterator contents = cls.directGetCollectionOwnedElementList().iterator();

               while(contents.hasNext()) {
                  Object cl = contents.next();
                  if (cl instanceof Classifier) {
                     inner_cls.add(((Classifier)cl).getName());
                  }
               }

               if (!this.isCancelled) {
                  for(i = 0; i < constr.length; ++i) {
                     if (constr[i].getName() == null) {
                        constr[i].setName(cNames.registerNewName(constr[i].getStereotypeText()));
                     }
                  }

                  if (!this.isCancelled) {
                     IntegrationCodeInfo ci = new IntegrationCodeInfo();
                     ci.setBaseClass(parent);
                     ci.setImplementationMethodName((String)null);
                     ci.setInnerClassName(inner_cls.newName("ConstraintChecker"));
                     cc.setCodeInfo(ci);
                  }
               }
            }
         }
      }
   }

   private void updateCodeInfo(ConstrainedOperation co) {
      Operation op = co.getConstrainedOperation();
      String base = "internal_" + op.getName();
      IntegrationCodeInfo ci = new IntegrationCodeInfo();
      String ownerName = UMLUtilities.getFullyQualifiedName((ModelElement)op.getOwner());
      NamingContext ownerFeatures = (NamingContext)this.featureContexts.get(ownerName);
      NamingContext local = new NamingContext();
      if (ownerFeatures == null) {
         ownerFeatures = new NamingContext();
         this.featureContexts.put(ownerName, ownerFeatures);
      }

      ci.setImplementationMethodName(ownerFeatures.registerNewName(base));
      ci.setBaseClass((ConstrainedClass)null);
      if (!this.isCancelled) {
         Iterator parameters = op.getCollectionParameterList().iterator();

         while(parameters.hasNext()) {
            local.add(((Parameter)parameters.next()).getName());
         }

         ci.setInnerClassName(local.newName("ConstraintChecker"));
         if (!this.isCancelled) {
            ArrayList unnamed = new ArrayList();
            local.clear();
            AbstractConstraintNode[] constraints = co.getConstraints();

            int i;
            for(i = 0; i < constraints.length; ++i) {
               if (constraints[i].getName() == null) {
                  unnamed.add(constraints[i]);
               } else {
                  local.add(constraints[i].getName());
               }
            }

            for(i = 0; i < unnamed.size(); ++i) {
               AbstractConstraintNode cn = (AbstractConstraintNode)unnamed.get(i);
               cn.setName(local.registerNewName(cn.getStereotypeText()));
            }

            co.setCodeInfo(ci);
         }
      }
   }
}
