package ro.ubbcluj.lci.gui.mainframe;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import ro.ubbcluj.lci.gui.browser.GBrowser;
import ro.ubbcluj.lci.gui.browser.GTree;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;

public class GDiagramFrame extends JInternalFrame implements DropTargetListener {
   static int openFrameCount = 1;
   static final int xOffset = 30;
   static final int yOffset = 30;
   protected GAbstractDiagram diagram;
   private DropTarget dt;
   private JDesktopPane dparent = null;

   public GDiagramFrame(GAbstractDiagram dia) {
      super(dia.getDiagramKind() + ": " + dia.getName(), true, true, true);
      this.setSize(300, 300);
      this.setLocation(30 * openFrameCount, 30 * openFrameCount);
      this.diagram = dia;
      ++openFrameCount;
      this.getContentPane().add(this.diagram.getComponent(), "Center");
      this.dt = new DropTarget(this.diagram.getGraph(), 3, this);
      this.addInternalFrameListener(new GDiagramFrame.DiagramFrameListener());
   }

   public boolean equals(Object o) {
      return o instanceof GAbstractDiagram ? this.diagram.equals(o) : super.equals(o);
   }

   public void drop(DropTargetDropEvent e) {
      int x = (int)e.getLocation().getX();
      int y = (int)e.getLocation().getY();
      ArrayList objList = (ArrayList)GTree.dnd_selected;

      for(int i = 0; i < objList.size(); ++i) {
         this.diagram.addObject(objList.get(i), x + i * 100, y);
      }

   }

   protected void setNewParent(JDesktopPane newParent) {
      this.dparent = newParent;
   }

   public JDesktopPane getNewParent() {
      return this.dparent;
   }

   public GAbstractDiagram getDiagram() {
      return this.diagram;
   }

   public void dragEnter(DropTargetDragEvent e) {
   }

   public void dragExit(DropTargetEvent e) {
   }

   public void dragOver(DropTargetDragEvent e) {
   }

   public void dropActionChanged(DropTargetDragEvent e) {
   }

   public void doDefaultCloseAction() {
      this.setVisible(false);
      ((GDiagramDesktop)this.dparent).hideDiagram(this.diagram);
   }

   class DiagramFrameListener extends InternalFrameAdapter {
      DiagramFrameListener() {
      }

      public void internalFrameActivated(InternalFrameEvent e) {
         ((GDiagramDesktop)GDiagramFrame.this.dparent).showDiagram(GDiagramFrame.this.diagram);
         GBrowser browser = GRepository.getInstance().getUsermodel().getBrowser();
         browser.selectInBrowser(GDiagramFrame.this.diagram);
      }
   }
}
