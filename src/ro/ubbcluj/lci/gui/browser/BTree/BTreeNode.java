package ro.ubbcluj.lci.gui.browser.BTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.modelManagement.PackageImpl;

public class BTreeNode extends DefaultMutableTreeNode {
   private boolean explored = false;

   public BTreeNode(Object obj, Filter filter) {
      this.setUserObject(obj);
      this.explore(filter);
   }

   public BTreeNode(GAbstractDiagram diagram) {
      this.setUserObject(diagram);
   }

   public boolean getAllowsChildren() {
      return !(this.userObject instanceof GAbstractDiagram);
   }

   public boolean isExplored() {
      return this.explored;
   }

   public boolean allowsDiagramChildren() {
      Object obj = this.getUserObject();
      return obj instanceof PackageImpl;
   }

   public void explore(Filter filter) {
      if (this.getAllowsChildren()) {
         if (!this.isExplored()) {
            Object ch = new ArrayList();

            try {
               Navigation n = new Navigation(filter);
               ch = n.getChildren(this.userObject);
            } catch (Exception var5) {
               var5.printStackTrace();
            }

            for(int i = 0; i < ((List)ch).size(); ++i) {
               BTreeNode child = new BTreeNode(((List)ch).get(i), filter);
               this.add(child);
            }

            this.explored = true;
         }

      }
   }

   public void refresh(Filter filter) {
      this.explored = false;
      this.removeAllChildren();
      this.explore(filter);
   }

   private void sortIntegersVector(Vector v) {
      for(int i = 0; i < v.size() - 1; ++i) {
         for(int j = i + 1; j < v.size(); ++j) {
            Integer ei = (Integer)v.get(i);
            Integer ej = (Integer)v.get(j);
            if (ei.compareTo(ej) > 0) {
               v.setElementAt(ej, i);
               v.setElementAt(ei, j);
            }
         }
      }

   }

   public int addDiagram(GAbstractDiagram diagram) {
      if (!this.allowsDiagramChildren()) {
         return -1;
      } else {
         String diagram_name = new String(diagram.getDiagramKind());
         int count = this.getChildCount();
         Vector context_postfixs = new Vector();

         BTreeNode child;
         for(int i = 0; i < count; ++i) {
            child = (BTreeNode)this.getChildAt(i);
            Object u_obj = child.getUserObject();
            if (u_obj instanceof GAbstractDiagram) {
               String temp_name = ((GAbstractDiagram)u_obj).getName();
               if (temp_name.indexOf(diagram_name) == 0) {
                  int aux = diagram_name.length();
                  temp_name = temp_name.substring(aux);
                  Integer integ = new Integer(temp_name);
                  context_postfixs.addElement(integ);
               }
            }
         }

         this.sortIntegersVector(context_postfixs);
         Integer rez = new Integer(1);

         for(int i = 0; i < context_postfixs.size(); ++i) {
            Integer aux = (Integer)context_postfixs.get(i);
            if (rez.compareTo(aux) == 0) {
               rez = new Integer(rez.intValue() + 1);
            }
         }

         diagram_name = diagram_name + rez;
         diagram.setName(diagram_name);
         child = new BTreeNode(diagram);
         this.insert(child, rez.intValue() - 1);
         return rez.intValue() - 1;
      }
   }

   public void removeDiagram(GAbstractDiagram diagram) {
      int count = this.getChildCount();

      for(int i = 0; i < count; ++i) {
         BTreeNode obj = (BTreeNode)this.getChildAt(i);
         Object u_obj = obj.getUserObject();
         if (u_obj instanceof GAbstractDiagram && ((GAbstractDiagram)u_obj).getName().equals(diagram.getName())) {
            this.remove(obj);
            return;
         }
      }

   }

   public static String getNameForClass(Class cls) {
      String name = cls.getName();
      return name.substring(name.lastIndexOf(".") + 1, name.length());
   }

   protected int getInsertPositon() {
      int children_count = this.getChildCount();
      int pos = children_count;

      for(int i = 0; i < children_count; ++i) {
         if (((BTreeNode)this.getChildAt(i)).getUserObject() instanceof GAbstractDiagram) {
            pos = i;
         }
      }

      return pos;
   }

   protected boolean isInterfaceEmpty(Interface interf) {
      return interf.allFeatures().size() == 0;
   }

   public ArrayList getFullPath() {
      ArrayList result = new ArrayList();
      BTreeNode parent = (BTreeNode)this.getParent();
      if (!parent.isRoot()) {
         result.addAll(parent.getFullPath());
      }

      result.add(parent);
      return result;
   }

   public String toString() {
      Object obj = this.getUserObject();
      if (obj instanceof ModelElement) {
         ModelElement me = (ModelElement)this.getUserObject();
         return me.toString();
      } else if (obj instanceof GAbstractDiagram) {
         return ((GAbstractDiagram)obj).getName();
      } else {
         return obj != null ? obj.toString() : new String();
      }
   }
}
