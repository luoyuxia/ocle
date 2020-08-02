package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.CellView;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.GraphCell;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.VertexView;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Actor;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.UseCase;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.utils.CreateObjectDialog;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.ModelUtilities;

public class DiagramListener extends MouseAdapter implements MouseMotionListener, KeyListener {
   protected Diagram diagram;
   protected GraphCell source;
   protected GraphCell destination;
   protected int oldX;
   protected int oldY;
   protected boolean isCtrlDown = false;
   private int increment = 1;

   protected Diagram getDiagram() {
      return this.diagram;
   }

   public DiagramListener(Diagram d) {
      this.diagram = d;
   }

   public void mouseClicked(MouseEvent e) {
      Element el = null;
      Object o = this.diagram.getGraph().getFirstCellForLocation(e.getX(), e.getY());
      if (o != null) {
         if (!e.isControlDown()) {
            Object userObject = ((DefaultGraphCell)o).getUserObject();
            if (userObject instanceof String && this.diagram instanceof ClassDiagram) {
               userObject = ((ClassDiagram)this.diagram).getAssociationEndFromLabel((DefaultGraphCell)o);
            }

            if (userObject instanceof Element) {
               el = (Element)userObject;
               if (el != null) {
                  ModelFactory.fireModelEvent(el, (Object)null, 0);
               }
            }

         }
      }
   }

   public void mousePressed(MouseEvent e) {
      Object o = this.diagram.getGraph().getFirstCellForLocation(e.getX(), e.getY());
      if (e.isControlDown() && o instanceof SpecialEdge && SwingUtilities.isRightMouseButton(e)) {
         this.edgeStraightner();
         e.consume();
      }

      if (SwingUtilities.isRightMouseButton(e)) {
         this.showPopup(e);
      } else {
         int action = this.diagram.getActionKind();
         if (o == null && action >= 100 && action < 200) {
            this.diagram.setActionKind(0);
         } else {
            Element el = null;
            if (o instanceof GraphCell && action > 100 && action < 200) {
               this.diagram.getGraph().setCursor(Cursor.getPredefinedCursor(1));
               this.oldX = e.getX();
               this.oldY = e.getY();
               this.source = (GraphCell)o;
               this.diagram.lockItem(this.source, true);
            }

            Point p = new Point(e.getX(), e.getY());
            if (action > 0 && action < 100) {
               if (!(((GAbstractDiagram)this.diagram).getContext() instanceof Namespace)) {
                  return;
               }

               Namespace context = (Namespace)((GAbstractDiagram)this.diagram).getContext();
               if (action == 1) {
                  Classifier c = (Classifier)ModelFactory.createNewElement(context, "Class");
                  el = c;
               }

               if (action == 2) {
                  Interface c = (Interface)ModelFactory.createNewElement(context, "Interface");
                  el = c;
               }

               if (action == 3) {
                  Package pk = (Package)ModelFactory.createNewElement(context, "Package");
                  el = pk;
               }

               if (action == 4) {
                  Actor a = (Actor)ModelFactory.createNewElement(context, "Actor");
                  el = a;
               }

               if (action == 5) {
                  UseCase uc = (UseCase)ModelFactory.createNewElement(context, "UseCase");
                  el = uc;
               }

               if (action == 6) {
                  ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object obj = (ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object)CreateObjectDialog.createInstance((Collaboration)context);
                  el = obj;
               }

               if (!this.isCtrlDown) {
                  this.diagram.setActionKind(0);
               }

               if (el != null) {
                  this.diagram.addElement(el, p, true);
               }
            }

         }
      }
   }

   public void mouseReleased(MouseEvent e) {
      ArrayList sel = this.diagram.getSelected();

      int action;
      for(action = 0; action < sel.size(); ++action) {
         GraphCell cell = (GraphCell)sel.get(action);
         if (cell instanceof ClassCell) {
            Object userObj = ((ClassCell)cell).getUserObject();
            if (userObj instanceof Classifier) {
               Set assocs = ModelUtilities.findAssociationsForParticipant((Classifier)userObj);
               Iterator it = assocs.iterator();

               while(it.hasNext()) {
                  SpecialEdge edge = ((ClassDiagram)this.diagram).getViewForRelation((Association)it.next());
                  if (edge != null) {
                     edge.regroupLabels(this.diagram.getGraph());
                  }
               }
            }
         } else if (cell instanceof SpecialEdge && !(this.diagram instanceof ObjectDiagram)) {
            ((SpecialEdge)cell).regroupLabels(this.diagram.getGraph());
         }

         CellView cview = this.diagram.getGraph().getView().getMapping(cell, false);
         if (cview instanceof SpecialEdgeView) {
            SpecialEdge edge = (SpecialEdge)cell;
            if (edge.getRouted()) {
               edge.route((SpecialEdgeView)cview, ((SpecialEdgeView)cview).getPoints());
            }
         } else if (cview instanceof VertexView) {
            Map attrs = cview.getAttributes();
            Rectangle bounds = GraphConstants.getBounds(cview.getAttributes());
            if (bounds.x < 0 && bounds.y > 0) {
               bounds.x = 0;
            }

            if (bounds.x > 0 && bounds.y < 0) {
               bounds.y = 0;
            }

            if (bounds.x < 0 && bounds.y < 0) {
               bounds.x = 0;
               bounds.y = 0;
            }

            GraphConstants.setBounds(attrs, bounds);
            cview.setAttributes(attrs);
         }
      }

      if (SwingUtilities.isRightMouseButton(e)) {
         this.showPopup(e);
      } else {
         action = this.diagram.getActionKind();
         if (action > 100 && action < 200) {
            Object o = this.diagram.getGraph().getFirstCellForLocation(e.getX(), e.getY());
            if (o instanceof GraphCell) {
               this.destination = (GraphCell)o;
               this.diagram.addRelation(this.source, this.destination);
            }

            this.diagram.setActionKind(0);
            this.diagram.lockItem(this.source, false);
            this.source = null;
            DiagramUtilities.deselectCells((DiagramGraph)this.diagram.getGraph());
         }

      }
   }

   public void mouseDragged(MouseEvent e) {
      if (!SwingUtilities.isRightMouseButton(e)) {
         if (this.diagram.getActionKind() > 100 && this.diagram.getActionKind() < 200 && this.source != null) {
            int curX = e.getX();
            int curY = e.getY();
            this.diagram.getGraph().repaint();
            Graphics g = this.diagram.getGraph().getGraphics();
            g.drawLine(this.oldX, this.oldY, curX, curY);
         }

      }
   }

   public void mouseMoved(MouseEvent e) {
      Cursor cc = DiagramListener.CursorGenerator.generateCursor(this.diagram.getActionKind());
      if (cc != null && !cc.getName().equals(this.diagram.getGraph().getCursor().getName())) {
         this.diagram.getGraph().setCursor(cc);
      }

   }

   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == 17) {
         this.isCtrlDown = true;
      }

      ArrayList lst;
      if (e.getKeyCode() == 127) {
         lst = this.diagram.getSelected();

         while(!lst.isEmpty()) {
            this.diagram.removeItem(lst.get(0));
         }
      }

      int i;
      if (e.getKeyCode() == 71 && e.isControlDown()) {
         this.isCtrlDown = true;
         if (!(this.diagram instanceof ClassDiagram)) {
            return;
         }

         i = 0;

         while(true) {
            if (i >= this.diagram.getSelected().size()) {
               ((ClassDiagram)this.diagram).groupCells(this.diagram.getSelected().toArray());
               break;
            }

            if (this.diagram.getSelected().get(i) instanceof ClassCell || this.diagram.getSelected().get(i) instanceof SpecialEdge) {
               return;
            }

            ++i;
         }
      }

      if (e.getKeyCode() == 85 && e.isControlDown()) {
         this.isCtrlDown = true;
         if (!(this.diagram instanceof ClassDiagram)) {
            return;
         }

         i = 0;

         while(true) {
            if (i >= this.diagram.getSelected().size()) {
               ((ClassDiagram)this.diagram).ungroupCells(this.diagram.getSelected().toArray());
               break;
            }

            if (this.diagram.getSelected().get(i) instanceof ClassCell || this.diagram.getSelected().get(i) instanceof SpecialEdge) {
               return;
            }

            ++i;
         }
      }

      lst = this.diagram.getSelected();

      for(i = 0; i < lst.size(); ++i) {
         DefaultGraphCell cell = (DefaultGraphCell)lst.get(i);
         CellView view = this.diagram.getGraph().getView().getMapping(cell, false);
         Map attrs = view.getAttributes();
         Rectangle bounds = GraphConstants.getBounds(attrs);
         if (bounds != null) {
            switch(e.getKeyCode()) {
            case 37:
               bounds.x -= this.increment;
               break;
            case 38:
               bounds.y -= this.increment;
               break;
            case 39:
               bounds.x += this.increment;
               break;
            case 40:
               bounds.y += this.increment;
            }

            GraphConstants.setBounds(attrs, bounds);
            view.setAttributes(attrs);
         }
      }

      this.diagram.getGraph().repaint();
   }

   public void keyReleased(KeyEvent e) {
      ArrayList selected;
      GraphCell cell;
      if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == 76) {
         this.edgeRouter();
         selected = this.diagram.getSelected();
         if (!selected.isEmpty()) {
            cell = (GraphCell)selected.get(0);
            if (cell instanceof SpecialEdge) {
               ((SpecialEdge)cell).regroupLabels(this.diagram.getGraph());
            }

            this.isCtrlDown = false;
         }
      }

      if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == 75) {
         this.edgeAngler();
         selected = this.diagram.getSelected();
         if (!selected.isEmpty()) {
            cell = (GraphCell)selected.get(0);
            if (cell instanceof SpecialEdge) {
               ((SpecialEdge)cell).regroupLabels(this.diagram.getGraph());
            }
         }

         this.isCtrlDown = false;
      }

      if (e.getKeyCode() == 17) {
         this.isCtrlDown = false;
      }

      if (this.diagram.getActionKind() > 0 && this.diagram.getActionKind() < 100) {
         this.diagram.setActionKind(0);
      }

   }

   public void keyTyped(KeyEvent e) {
   }

   private void edgeStraightner() {
      ArrayList sel = this.diagram.getSelected();
      if (sel.size() == 1) {
         if (sel.get(0) instanceof SpecialEdge) {
            SpecialEdge edge = (SpecialEdge)sel.get(0);
            SpecialEdgeView sev = (SpecialEdgeView)this.diagram.getGraph().getView().getMapping(edge, false);

            int i;
            while((i = sev.getControlPointIndex()) != -1) {
               sev.removePoint(i);
            }
         }

      }
   }

   private void edgeRouter() {
      ArrayList sel = this.diagram.getSelected();
      if (sel.size() == 1) {
         if (sel.get(0) instanceof SpecialEdge) {
            SpecialEdge edge = (SpecialEdge)sel.get(0);
            edge.setRouted(!edge.getRouted());
            SpecialEdgeView sev = (SpecialEdgeView)this.diagram.getGraph().getView().getMapping(edge, false);
            sev.refresh(true);
            if (!edge.getRouted()) {
               sev.removeAllControlPoints();
            }

            this.diagram.getGraph().repaint();
         }

      }
   }

   private void edgeAngler() {
      ArrayList sel = this.diagram.getSelected();

      for(int i = 0; i < sel.size(); ++i) {
         if (sel.get(i) instanceof SpecialEdge) {
            SpecialEdge edge = (SpecialEdge)sel.get(i);
            SpecialEdgeView sev = (SpecialEdgeView)this.diagram.getGraph().getView().getMapping(edge, false);
            sev.straightAngles();
            sev.straightAngles();
         }
      }

      this.diagram.getGraph().repaint();
      this.diagram.getGraph().removeSelectionCell(sel.get(0));
   }

   private boolean checkUniformity() {
      ArrayList selected = this.diagram.getSelected();
      if (selected.size() == 0) {
         return false;
      } else {
         Class cls = selected.get(0).getClass();

         for(int i = 1; i < selected.size(); ++i) {
            if (!selected.get(i).getClass().equals(cls)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean supportGraphicalAttributes(ArrayList cells) {
      boolean result = true;

      for(int i = 0; i < cells.size(); ++i) {
         Object cell = cells.get(i);
         result &= cell instanceof ClassCell || cell instanceof PackageCell || cell instanceof ActorCell || cell instanceof UseCaseCell || cell instanceof ObjectCell;
      }

      return result;
   }

   protected void showPopup(MouseEvent e) {
      boolean multipleSel = false;
      Object o = this.diagram.getGraph().getFirstCellForLocation(e.getX(), e.getY());
      if (o != null) {
         this.source = (GraphCell)o;
      } else {
         if (this.diagram.getSelected().size() <= 0 || !this.checkUniformity()) {
            ((GAbstractDiagram)this.diagram).showPopup(e);
            return;
         }

         if (!this.supportGraphicalAttributes(this.diagram.getSelected())) {
            return;
         }

         this.source = (GraphCell)this.diagram.getSelected().get(0);
         o = this.source;
         multipleSel = true;
      }

      Point p = new Point(e.getX(), e.getY());
      SwingUtilities.convertPointToScreen(p, this.diagram.getGraph());
      if (o instanceof ClassCell) {
         ((ClassCell)o).setLocation(p);
         ((ClassCell)o).getProperty().getPopup().show(this.diagram.getGraph(), e.getX(), e.getY());
      }

      if (o instanceof PackageCell) {
         ((PackageCell)o).setLocation(p);
         ((PackageCell)o).getProperty().getPopup().show(this.diagram.getGraph(), e.getX(), e.getY());
      }

      if (o instanceof UseCaseCell) {
         ((UseCaseCell)o).setLocation(p);
         ((UseCaseCell)o).getProperty().getPopup().show(this.diagram.getGraph(), e.getX(), e.getY());
      }

      if (o instanceof ActorCell) {
         ((ActorCell)o).getProperty().getPopup().show(this.diagram.getGraph(), e.getX(), e.getY());
      }

      if (o instanceof ObjectCell) {
         ((ObjectCell)o).getProperty().getPopup().show(this.diagram.getGraph(), e.getX(), e.getY());
      }

   }

   static class CursorGenerator {
      CursorGenerator() {
      }

      public static Cursor generateCursor(int type) {
         ImageIcon ic = null;
         switch(type) {
         case 1:
            ic = new ImageIcon((DiagramListener.CursorGenerator.class).getResource("/images/Class.gif"));
            break;
         case 2:
            ic = new ImageIcon((DiagramListener.CursorGenerator.class).getResource("/images/Interface.gif"));
            break;
         case 3:
            ic = new ImageIcon((DiagramListener.CursorGenerator.class).getResource("/images/Package.gif"));
            break;
         case 4:
            ic = new ImageIcon((DiagramListener.CursorGenerator.class).getResource("/images/Actor.gif"));
            break;
         case 5:
            ic = new ImageIcon((DiagramListener.CursorGenerator.class).getResource("/images/UseCase.gif"));
         }

         if (ic != null) {
            Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(ic.getImage(), new Point(1, 1), "x");
            return c;
         } else {
            return Cursor.getDefaultCursor();
         }
      }
   }
}
