package ro.ubbcluj.lci.gui.browser;

import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;
import ro.ubbcluj.lci.gui.browser.BTree.BTreeNode;

public class GTree extends JTree implements DragGestureListener, DragSourceListener {
   public static Object dnd_selected = null;

   public GTree() {
      DragSource dragSource = DragSource.getDefaultDragSource();
      dragSource.createDefaultDragGestureRecognizer(this, 3, this);
      ToolTipManager.sharedInstance().registerComponent(this);
   }

   public void dragGestureRecognized(DragGestureEvent e) {
      e.startDrag(DragSource.DefaultCopyDrop, new StringSelection(this.getPath()), this);
      TreePath[] paths = this.getSelectionPaths();
      if (paths != null) {
         ArrayList objList = new ArrayList();

         for(int i = 0; i < paths.length; ++i) {
            objList.add(((BTreeNode)paths[i].getLastPathComponent()).getUserObject());
         }

         dnd_selected = objList;
      }
   }

   public void dragDropEnd(DragSourceDropEvent e) {
   }

   public void dragEnter(DragSourceDragEvent e) {
   }

   public void dragExit(DragSourceEvent e) {
   }

   public void dragOver(DragSourceDragEvent e) {
   }

   public void dropActionChanged(DragSourceDragEvent e) {
   }

   public String getPath() {
      TreePath path = this.getSelectionPath();
      return path != null ? path.toString() : null;
   }

   public BTreeNode getNodeFor(Object o) {
      Enumeration nodes = ((BTreeNode)this.getModel().getRoot()).breadthFirstEnumeration();

      BTreeNode node;
      do {
         if (!nodes.hasMoreElements()) {
            return null;
         }

         node = (BTreeNode)nodes.nextElement();
      } while(node.getUserObject() != o);

      return node;
   }
}
