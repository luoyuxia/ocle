package ro.ubbcluj.lci.gui.diagrams;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import ro.ubbcluj.lci.gui.browser.GTree;
import ro.ubbcluj.lci.gui.editor.AbstractPad;
import ro.ubbcluj.lci.gui.editor.mdi.PadContainer;
import ro.ubbcluj.lci.gui.mainframe.GApplication;

public class DiagramPad extends AbstractPad implements DropTargetListener {
   private GAbstractDiagram diagram;
   private ImageIcon image;
   private JPopupMenu pmenu = new JPopupMenu();
   private DiagramPad.PopupListener plistener = new DiagramPad.PopupListener();
   private DropTarget dt;

   public DiagramPad(GAbstractDiagram _diagram, PadContainer _view) {
      this.diagram = _diagram;
      this.dt = new DropTarget(this.diagram.getGraph(), 3, this);
      this.initPopup();
   }

   public void activate() {
      this.diagram.getComponent().requestFocus();
   }

   public void initialize() {
   }

   public JComponent getComponent() {
      return this.diagram.getComponent();
   }

   public GAbstractDiagram getDiagram() {
      return this.diagram;
   }

   public int getType() {
      return 1;
   }

   public String getTitle() {
      return this.diagram.getName();
   }

   public Icon getIcon() {
      if (this.diagram instanceof ClassDiagram) {
         return new ImageIcon("");
      } else if (this.diagram instanceof UseCaseDiagram) {
         return new ImageIcon("");
      } else {
         return this.diagram instanceof ObjectDiagram ? new ImageIcon("") : null;
      }
   }

   public int close() {
      return 2;
   }

   public JPopupMenu getPopupMenu() {
      return this.pmenu;
   }

   private void initPopup() {
      this.pmenu.add(new DiagramPad.CloseAction());
      this.pmenu.addPopupMenuListener(this.plistener);
   }

   public void drop(DropTargetDropEvent e) {
      int x = (int)e.getLocation().getX();
      int y = (int)e.getLocation().getY();
      ArrayList objList = (ArrayList)GTree.dnd_selected;

      for(int i = 0; i < objList.size(); ++i) {
         this.diagram.addObject(objList.get(i), x + i * 100, y);
      }

   }

   public void dragEnter(DropTargetDragEvent e) {
   }

   public void dragExit(DropTargetEvent e) {
   }

   public void dragOver(DropTargetDragEvent e) {
   }

   public void dropActionChanged(DropTargetDragEvent e) {
   }

   private class PopupListener implements PopupMenuListener {
      private PopupListener() {
      }

      public void popupMenuCanceled(PopupMenuEvent e) {
      }

      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
      }

      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
         ((JMenuItem)DiagramPad.this.pmenu.getComponent(0)).getAction().putValue("Name", "Close " + DiagramPad.this.diagram.getName());
      }
   }

   private class CloseAction extends AbstractAction {
      CloseAction() {
         super("", new ImageIcon(""));
      }

      public void actionPerformed(ActionEvent e) {
         GApplication.getApplication().getEditor().getView().removePadView(DiagramPad.this);
      }
   }
}
