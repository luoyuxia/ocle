package ro.ubbcluj.lci.gxapi;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class GXDefaultMutableTreeNode extends DefaultMutableTreeNode {
   protected String userObjectString;

   public GXDefaultMutableTreeNode() {
   }

   public GXDefaultMutableTreeNode(Object userObject) {
      super(userObject);
   }

   public GXDefaultMutableTreeNode(Object userObject, boolean allowsChildren) {
      super(userObject, allowsChildren);
   }

   public void addChildren(GXDefaultMutableTreeNode mtn) {
      if (this.children == null) {
         this.children = new Vector();
      }

      this.children.add(mtn);
   }

   public Enumeration getChildrenList() {
      return this.children.elements();
   }

   public void setUserObjectString(String userObjectString) {
      this.userObjectString = userObjectString;
   }

   public String getUserObjectString() {
      return this.userObjectString;
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      this.allowsChildren = dmtn.getAllowsChildren();
      this.userObject = dmtn.getUserObject();
      this.userObjectString = null;
      if (this.userObject instanceof String) {
         this.userObjectString = (String)this.userObject;
         this.userObject = null;
      }

      Object dmtnParent = dmtn.getParent();
      if (dmtnParent == null) {
         this.parent = null;
      } else {
         this.parent = (MutableTreeNode)clones.get(dmtnParent);
      }

      if (this.children == null) {
         this.children = new Vector();
      } else {
         this.children.clear();
      }

      for(int i = 0; i < dmtn.getChildCount(); ++i) {
         TreeNode mtn = dmtn.getChildAt(i);
         this.children.add((MutableTreeNode)clones.get(mtn));
      }

   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      dmtn.setAllowsChildren(this.allowsChildren);
      if (this.userObject != null) {
         dmtn.setUserObject(this.userObject);
      } else if (this.userObjectString != null) {
         dmtn.setUserObject(this.userObjectString);
      }

      if (this.children != null) {
         for(int i = 0; i < this.children.size(); ++i) {
            TreeNode mtn = this.getChildAt(i);
            dmtn.add((MutableTreeNode)clones.get(mtn));
         }
      }

   }
}
