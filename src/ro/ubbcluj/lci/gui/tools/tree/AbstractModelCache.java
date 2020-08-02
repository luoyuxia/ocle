package ro.ubbcluj.lci.gui.tools.tree;

import java.util.HashMap;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class AbstractModelCache {
   public HashMap classes = new HashMap();
   public HashMap packages = new HashMap();
   public NodeFactory nodeFactory;

   public AbstractModelCache() {
   }

   public DefaultMutableTreeNode loadTree(Namespace start) {
      DefaultMutableTreeNode ret;
      if (start instanceof Class) {
         ret = this.nodeFactory.createNode((Classifier)((Class)start));
         this.classes.put(UMLUtilities.getFullyQualifiedName((ModelElement)start), ret);
      } else {
         if (!(start instanceof Package)) {
            return null;
         }

         ret = this.nodeFactory.createNode((Package)start);
         this.packages.put(UMLUtilities.getFullyQualifiedName((ModelElement)start), ret);
      }

      Iterator owned = start.directGetCollectionOwnedElementList().iterator();

      while(owned.hasNext()) {
         Object x = owned.next();
         if (x instanceof Namespace) {
            DefaultMutableTreeNode node = this.loadTree((Namespace)x);
            if (x instanceof Package) {
               ret.add(node);
            }
         }
      }

      return ret;
   }

   public DefaultMutableTreeNode addNode(Classifier c) {
      String name;
      DefaultMutableTreeNode st = (DefaultMutableTreeNode)this.classes.get(name = UMLUtilities.getFullyQualifiedName((ModelElement)c));
      DefaultMutableTreeNode ret = st;
      if (st == null) {
         throw new RuntimeException("Class not registered:" + name);
      } else {
         DefaultMutableTreeNode nd;
         Namespace nms;
         for(nms = c.directGetNamespace(); nms instanceof Classifier; st = nd) {
            nd = (DefaultMutableTreeNode)this.classes.get(name = UMLUtilities.getFullyQualifiedName((ModelElement)nms));
            if (nd == null) {
               throw new RuntimeException("Class not registered:" + name);
            }

            if (nd.getIndex(st) < 0) {
               nd.add(st);
            }

            nms = nms.directGetNamespace();
         }

         nd = (DefaultMutableTreeNode)this.packages.get(name = UMLUtilities.getFullyQualifiedName((ModelElement)nms));
         if (nd == null) {
            throw new RuntimeException("Package not registered:" + name);
         } else {
            if (nd.getIndex(st) < 0) {
               nd.add(st);
            }

            return ret;
         }
      }
   }

   public void clear() {
      this.classes.clear();
      this.packages.clear();
   }
}
