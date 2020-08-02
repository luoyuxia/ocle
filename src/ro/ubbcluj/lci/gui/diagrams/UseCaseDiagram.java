package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import com.jgraph.event.GraphModelEvent;
import com.jgraph.event.GraphModelListener;
import com.jgraph.event.GraphSelectionEvent;
import com.jgraph.event.GraphSelectionListener;
import com.jgraph.graph.CellView;
import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Edge;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphUndoManager;
import com.jgraph.graph.ParentMap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.gxapi.GXActorCell;
import ro.ubbcluj.lci.gxapi.GXClassGProperty;
import ro.ubbcluj.lci.gxapi.GXConnectionSet;
import ro.ubbcluj.lci.gxapi.GXDefaultEdge;
import ro.ubbcluj.lci.gxapi.GXDefaultGraphCell;
import ro.ubbcluj.lci.gxapi.GXDefaultMutableTreeNode;
import ro.ubbcluj.lci.gxapi.GXDefaultPort;
import ro.ubbcluj.lci.gxapi.GXHashtable;
import ro.ubbcluj.lci.gxapi.GXSpecialEdge;
import ro.ubbcluj.lci.gxapi.GXSpecialPort;
import ro.ubbcluj.lci.gxapi.GXUseCaseCell;
import ro.ubbcluj.lci.gxapi.GXUseCaseDiagram;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Actor;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Extend;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Include;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.UseCase;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Relationship;
import ro.ubbcluj.lci.utils.ErrorMessage;
import ro.ubbcluj.lci.utils.ModelFactory;

public class UseCaseDiagram extends GAbstractDiagram {
   protected DefaultGraphModel model = new DefaultGraphModel();
   protected DiagramGraph graph;
   protected ArrayList actors;
   protected ArrayList actorViews;
   protected ArrayList useCases;
   protected ArrayList useCaseViews;
   protected ArrayList relationViews;
   protected transient ArrayList selected;
   int ACTION_KIND;
   private GraphUndoManager undoManager;
   private DiagramListener listener;
   private static DiagramValidator validator = new UseCaseDiagramValidator();

   public UseCaseDiagram(GUMLModel gmodel, ModelElement context) {
      this.graph = new DiagramGraph(this.model);
      this.actors = new ArrayList();
      this.actorViews = new ArrayList();
      this.useCases = new ArrayList();
      this.useCaseViews = new ArrayList();
      this.relationViews = new ArrayList();
      this.selected = new ArrayList();
      this.ACTION_KIND = 0;
      this.undoManager = new GraphUndoManager();
      this.setUserObject(gmodel);
      this.context = context;
      this.initGraph();
      this.addDiagramListener(this.listener);
   }

   private void initGraph() {
      this.graph.setGridEnabled(false);
      this.graph.setGridColor(Color.black);
      this.graph.setBackground(Color.white);
      this.graph.setDisconnectable(false);
      this.graph.setEditable(false);
      this.graph.setCloneable(false);
      this.graph.setSelectNewCells(true);
      this.graph.setHandleColor(Color.blue);
      this.graph.setHighlightColor(Color.red);
      this.graph.addGraphSelectionListener(new UseCaseDiagram.DiagramSelectionListener());
      this.graph.getModel().addUndoableEditListener(this.undoManager);
      this.graph.getModel().addGraphModelListener(new UseCaseDiagram.DiagramModelListener());
      this.listener = new DiagramListener(this);
      this.diagram = new JPanel(new BorderLayout());
      this.diagram.add(new JScrollPane(this.graph), "Center");
   }

   ArrayList getActors() {
      return this.actors;
   }

   ArrayList getActorViews() {
      return this.actorViews;
   }

   ArrayList getUseCases() {
      return this.useCases;
   }

   ArrayList getUseCaseViews() {
      return this.useCaseViews;
   }

   ArrayList getRelationViews() {
      return this.relationViews;
   }

   DefaultGraphModel getGraphModel() {
      return this.model;
   }

   public JGraph getGraph() {
      return this.graph;
   }

   public GXUseCaseDiagram copy() {
      GXUseCaseDiagram gxucd = new GXUseCaseDiagram();
      gxucd.setName(this.getName());
      gxucd.setIsLocked(this.isLocked);
      gxucd.setGridVisible(this.graph.isGridVisible());
      gxucd.setBackground(this.graph.getBackground().toString());
      gxucd.setOwner(this.getContext());
      gxucd.setActors(this.actors);
      gxucd.setUseCases(this.useCases);
      Object[] cells = this.graph.getView().order(this.graph.getRoots());
      Object[] flat = DefaultGraphModel.getDescendants(this.graph.getModel(), cells).toArray();
      ConnectionSet cs = ConnectionSet.create(this.graph.getModel(), flat, false);
      Map viewAttributes = GraphConstants.createPropertyMap(flat, this.graph.getView());
      Hashtable clones = new Hashtable();

      int i;
      for(i = 0; i < flat.length; ++i) {
         Object clone = null;
         if (flat[i] instanceof ActorCell) {
            clone = new GXActorCell();
         } else if (flat[i] instanceof UseCaseCell) {
            clone = new GXUseCaseCell();
         } else if (flat[i] instanceof SpecialEdge) {
            clone = new GXSpecialEdge();
         } else if (flat[i] instanceof SpecialPort) {
            clone = new GXSpecialPort();
         } else if (flat[i] instanceof DefaultPort) {
            clone = new GXDefaultPort();
         } else if (flat[i] instanceof DefaultEdge) {
            clone = new GXDefaultEdge();
         } else if (flat[i] instanceof DefaultGraphCell) {
            clone = new GXDefaultGraphCell();
         }

         clones.put(flat[i], clone);
      }

      for(i = 0; i < flat.length; ++i) {
         GXDefaultMutableTreeNode clone = (GXDefaultMutableTreeNode)clones.get(flat[i]);
         clone.copy((DefaultMutableTreeNode)flat[i], clones);
      }

      for(i = 0; i < flat.length; ++i) {
         gxucd.addFlat((GXDefaultGraphCell)clones.get(flat[i]));
      }

      for(i = 0; i < cells.length; ++i) {
         gxucd.addCells((GXDefaultGraphCell)clones.get(cells[i]));
      }

      Iterator it = this.actorViews.iterator();

      while(it.hasNext()) {
         gxucd.addActorViews(clones.get(it.next()));
      }

      it = this.useCaseViews.iterator();

      while(it.hasNext()) {
         gxucd.addUseCaseViews(clones.get(it.next()));
      }

      GXConnectionSet csclone = new GXConnectionSet();
      clones.put(cs, csclone);
      csclone.copy(cs, clones);
      gxucd.setConnectionSet(csclone);
      GXHashtable gxh = new GXHashtable();
      gxh.copy((Hashtable)viewAttributes, clones);
      gxucd.setViewAttributes(gxh);
      return gxucd;
   }

   public void extractData(GXUseCaseDiagram gxucd) {
      this.initGraph();
      this.addDiagramListener(this.listener);
      this.setName(gxucd.getName());
      this.isLocked = gxucd.getIsLocked();
      this.actors = gxucd.getCollectionActors();
      this.useCases = gxucd.getCollectionUseCases();
      Hashtable clones = new Hashtable();

      Enumeration en;
      Object flatObj;
      Object clone;
      for(en = gxucd.getFlatList(); en.hasMoreElements(); clones.put(flatObj, clone)) {
         flatObj = en.nextElement();
         clone = null;
         if (flatObj instanceof GXActorCell) {
            clone = new ActorCell();
         } else if (flatObj instanceof GXUseCaseCell) {
            clone = new UseCaseCell();
         } else if (flatObj instanceof GXSpecialEdge) {
            clone = new SpecialEdge();
         } else if (flatObj instanceof GXSpecialPort) {
            clone = new SpecialPort();
         } else if (flatObj instanceof GXDefaultPort) {
            clone = new DefaultPort();
         } else if (flatObj instanceof GXDefaultEdge) {
            clone = new DefaultEdge();
         } else if (flatObj instanceof GXDefaultGraphCell) {
            clone = new DefaultGraphCell();
         }
      }

      en = gxucd.getFlatList();

      while(en.hasMoreElements()) {
         GXDefaultMutableTreeNode flatObj1 = (GXDefaultMutableTreeNode)en.nextElement();
         DefaultMutableTreeNode clone1 = (DefaultMutableTreeNode)clones.get(flatObj1);
         flatObj1.extractData(clone1, clones);
      }

      Object[] cells = new Object[gxucd.getCollectionCells().size()];
      int i = 0;

      Object packageView;
      for(en = gxucd.getCellsList(); en.hasMoreElements(); cells[i++] = clones.get(packageView)) {
         packageView = en.nextElement();
      }

      en = gxucd.getActorViewsList();

      while(en.hasMoreElements()) {
         packageView = en.nextElement();
         this.actorViews.add(clones.get(packageView));
      }

      en = gxucd.getUseCaseViewsList();

      while(en.hasMoreElements()) {
         packageView = en.nextElement();
         this.useCaseViews.add(clones.get(packageView));
      }

      ConnectionSet cs = new ConnectionSet();
      clones.put(gxucd.getConnectionSet(), cs);
      gxucd.getConnectionSet().extractData(cs, clones);
      Hashtable attrib = new Hashtable();
      gxucd.getViewAttributes().extractData(attrib, clones);
      this.model = new DefaultGraphModel();
      this.graph = new DiagramGraph(this.model);
      this.undoManager = new GraphUndoManager();
      this.selected = new ArrayList();
      this.graph.getModel().insert(cells, cs, (ParentMap)null, attrib);
      this.initGraph();
      this.graph.setGridVisible(gxucd.getGridVisible());
      this.graph.setGridEnabled(gxucd.getGridVisible());
      this.graph.setBackground(GXClassGProperty.stringToColor(gxucd.getBackground()));
      this.graph.repaint();
      this.addDiagramListener(this.listener);

      for(i = 0; i < this.actors.size(); ++i) {
         ActorCell ac = (ActorCell)this.actorViews.get(i);
         ac.getProperty().getPopup().addMouseListener(this.listener);
      }

      for(i = 0; i < this.useCases.size(); ++i) {
         UseCaseCell ucc = (UseCaseCell)this.useCaseViews.get(i);
         ucc.getProperty().getPopup().addMouseListener(this.listener);
      }

   }

   public Color getBackgroundColor() {
      return this.graph.getBackground();
   }

   public void setBackgroundColor(Color newColor) {
      this.graph.setBackground(newColor);
   }

   public void setGridVisible(boolean state) {
      this.graph.setGridVisible(state);
   }

   public boolean getGridVisible() {
      return this.graph.isGridVisible();
   }

   public ArrayList getSelected() {
      return this.selected;
   }

   public void setSelected(Object o) {
      if (o instanceof Classifier) {
         DefaultGraphCell view = null;
         int i = this.actors.indexOf(o);
         if (i >= 0) {
            view = (DefaultGraphCell)this.actorViews.get(i);
         } else {
            i = this.useCases.indexOf(o);
            if (i >= 0) {
               view = (DefaultGraphCell)this.useCaseViews.get(i);
            }
         }

         if (view != null) {
            this.graph.setSelectionCell(view);
            this.graph.scrollCellToVisible(view);
         }
      } else if (o instanceof DefaultGraphCell) {
         this.graph.setSelectionCell(o);
         this.graph.scrollCellToVisible(o);
      }

   }

   public void setActionKind(int kind) {
      this.ACTION_KIND = kind;
   }

   public int getActionKind() {
      return this.ACTION_KIND;
   }

   public void addElement(Object o, Point p, boolean isNew) {
      this.zoomDefault();
      if (!this.contains(o)) {
         if (o instanceof Actor) {
            this.addActor(o, p, isNew);
         } else if (o instanceof UseCase) {
            this.addUseCase(o, p, isNew);
         }

      }
   }

   public void addRelation(Object from, Object to) {
      DefaultGraphCell fromCell = (DefaultGraphCell)from;
      DefaultGraphCell toCell = (DefaultGraphCell)to;
      String[] types = new String[]{"Relation", "Association", "Generalization", "", "", "", "", "", "Include", "Extend"};
      ModelElement cl = (ModelElement)fromCell.getUserObject();
      ModelElement sp = (ModelElement)toCell.getUserObject();
      Object relation = ModelFactory.createNewRelation(cl, sp, (ModelElement)null, types[this.ACTION_KIND - 100]);
      if (relation == null) {
         GMainFrame.getMainFrame().updateMessages((Object)("Illegal " + types[this.ACTION_KIND - 100] + " relation."));
      } else if (relation instanceof ErrorMessage) {
         GMainFrame.getMainFrame().updateMessages((Object)((ErrorMessage)relation).getErrorMessage());
      } else {
         ConnectionSet cs = new ConnectionSet();
         DefaultPort from_port = (DefaultPort)this.graph.getModel().getChild(fromCell, 0);
         DefaultPort to_port = null;
         if (fromCell == toCell) {
            to_port = (DefaultPort)this.model.getChild(toCell, 1);
         } else {
            to_port = (DefaultPort)this.model.getChild(toCell, 0);
         }

         int supEnd = 2;
         if (!(relation instanceof Include) && !(relation instanceof Extend)) {
            if (relation instanceof Generalization) {
               supEnd = 1;
            }
         } else {
            supEnd = 3;
         }

         Edge edge = this.edgeGenerator(0, supEnd, relation);
         cs.connect(edge, from_port, true);
         cs.connect(edge, to_port, false);
         Object[] insert = new Object[]{edge};
         this.model.insert(insert, cs, (ParentMap)null, (Map)null);
         this.relationViews.add(edge);
         if (fromCell == toCell && fromCell.getChildCount() == 1) {
            SpecialPort port = new SpecialPort();
            Map attrs = GraphConstants.createMap();
            int u = 1000;
            GraphConstants.setOffset(attrs, new Point(u / 2, 0));
            port.setAttributes(attrs);
            fromCell.add(port);
            if (fromCell instanceof ClassCell) {
               ((ClassCell)fromCell).setAutoRelation(true);
            }
         }

         if (fromCell == toCell) {
            EdgeView eview = (EdgeView)this.graph.getView().getMapping(edge, false);
            CellView cview = this.graph.getView().getMapping(fromCell, false);
            Rectangle bounds = GraphConstants.getBounds(cview.getAttributes());
            Point firstPort = eview.getPoint(0);
            Point secndPort = eview.getPoint(1);
            int by = bounds.y;
            int px = firstPort.x;
            int py = firstPort.y;
            int width = bounds.width / 2;
            int height = bounds.height / 2;
            Point p1 = new Point(px + width, py);
            Point p2 = new Point(px + width, by - height);
            Point p3 = new Point(secndPort.x, by - height);
            eview.addPoint(1, p1);
            eview.addPoint(2, p2);
            eview.addPoint(3, p3);
         }

         this.graph.repaint();
      }
   }

   public void updateItem(Object o) {
      if (o instanceof AssociationEnd) {
         DiagramUtilities.updateGraphicalRelation(((AssociationEnd)o).getAssociation(), this.relationViews, this.graph);
         this.graph.repaint();
      } else {
         this.signalElementChange(o);
      }

   }

   public void removeItem(Object o) {
      this.removeGraphicalItem(o);
      int i;
      if (o instanceof Actor) {
         i = this.actors.indexOf(o);
         if (i != -1) {
            this.actors.remove(i);
            this.actorViews.remove(i);
         }
      } else if (o instanceof UseCase) {
         i = this.useCases.indexOf(o);
         if (i != -1) {
            this.useCases.remove(i);
            this.useCaseViews.remove(i);
         }
      } else if (o instanceof ActorCell) {
         i = this.actorViews.indexOf(o);
         if (i != -1) {
            this.actors.remove(i);
            this.actorViews.remove(i);
         }
      } else if (o instanceof UseCaseCell) {
         i = this.useCaseViews.indexOf(o);
         if (i != -1) {
            this.useCases.remove(i);
            this.useCaseViews.remove(i);
         }
      } else {
         if (o instanceof Relationship) {
            for(i = 0; i < this.relationViews.size(); ++i) {
               if (((DefaultGraphCell)this.relationViews.get(i)).getUserObject() == o) {
                  this.removeGraphicalItem(this.relationViews.get(i));
                  this.relationViews.remove(i);
                  return;
               }
            }
         }

         if (o instanceof Edge) {
            for(i = this.relationViews.indexOf(o); i != -1; i = this.relationViews.indexOf(o)) {
               this.relationViews.remove(i);
            }

            this.removeGraphicalItem(o);
         }

      }
   }

   public boolean contains(Object o) {
      return this.actors.contains(o) || this.useCases.contains(o);
   }

   public void addDiagramListener(DiagramListener dl) {
      this.graph.addMouseListener(dl);
      this.graph.addMouseMotionListener(dl);
      this.graph.addKeyListener(dl);
   }

   public void lockItem(Object o, boolean lock) {
      this.setDragable(o, !lock);
   }

   public void lockDiagram(boolean state) {
      this.isLocked = !state;

      int i;
      DefaultGraphCell cell;
      Map attrs;
      for(i = 0; i < this.useCaseViews.size(); ++i) {
         cell = (DefaultGraphCell)this.useCaseViews.get(i);
         attrs = cell.getAttributes();
         GraphConstants.setMoveable(attrs, state);
         cell.setAttributes(attrs);
      }

      for(i = 0; i < this.actorViews.size(); ++i) {
         cell = (DefaultGraphCell)this.actorViews.get(i);
         attrs = cell.getAttributes();
         GraphConstants.setMoveable(attrs, state);
         cell.setAttributes(attrs);
      }

   }

   public void undo() {
      try {
         if (this.undoManager.canUndo()) {
            this.undoManager.undo(this.graph.getView());
         }
      } catch (Exception var2) {
      }

   }

   public void redo() {
      try {
         if (this.undoManager.canRedo()) {
            this.undoManager.redo(this.graph.getView());
         }
      } catch (Exception var2) {
      }

   }

   public void zoomDefault() {
      this.graph.setScale(1.0D);
   }

   public void zoomIn() {
      this.graph.setScale(1.5D * this.graph.getScale());
   }

   public void zoomOut() {
      this.graph.setScale(this.graph.getScale() / 1.5D);
   }

   public JPanel getDropPanel() {
      return (JPanel)this.diagram;
   }

   public void addObject(Object o, int x, int y) {
      this.addElement(o, new Point(x, y), false);
   }

   public boolean accept(Object o) {
      return true;
   }

   public String getDiagramKind() {
      return "UseCase";
   }

   private void addActor(Object o, Point loc, boolean isNew) {
      Actor actor = (Actor)o;
      this.actors.add(actor);
      ActorCell actorCell = new ActorCell(actor);
      GProperty property = GProperty.createProperty(actorCell);
      property.getPopup().addMouseListener(this.listener);
      actorCell.setProperty(property);
      DefaultPort port = new DefaultPort();
      actorCell.add(port);
      this.actorViews.add(actorCell);
      Map map = GraphConstants.createMap();
      Map attributeMap = new Hashtable();
      Point pt = this.graph.snap(new Point(loc));
      GraphConstants.setBounds(map, new Rectangle(pt, DiagramUtilities.getDefaultSize(actorCell)));
      GraphConstants.setOpaque(map, true);
      attributeMap.put(actorCell, map);
      Object[] insert = new Object[]{actorCell};
      this.model.insert(insert, (ConnectionSet)null, (ParentMap)null, attributeMap);
      if (!isNew) {
         validator.validate(o, this);
      }

   }

   private void addUseCase(Object o, Point loc, boolean isNew) {
      UseCase useCase = (UseCase)o;
      this.useCases.add(useCase);
      UseCaseCell useCaseCell = new UseCaseCell(useCase);
      GProperty property = GProperty.createProperty(useCaseCell);
      property.getPopup().addMouseListener(this.listener);
      useCaseCell.setProperty(property);
      DefaultPort port = new DefaultPort();
      useCaseCell.add(port);
      this.useCaseViews.add(useCaseCell);
      Map map = GraphConstants.createMap();
      Map attributeMap = new Hashtable();
      Point pt = this.graph.snap(new Point(loc));
      GraphConstants.setBounds(map, new Rectangle(pt, DiagramUtilities.getDefaultSize(useCaseCell)));
      GraphConstants.setOpaque(map, true);
      attributeMap.put(useCaseCell, map);
      Object[] insert = new Object[]{useCaseCell};
      this.model.insert(insert, (ConnectionSet)null, (ParentMap)null, attributeMap);
      if (!isNew) {
         validator.validate(o, this);
      }

   }

   protected void setDragable(Object o, boolean state) {
      if (o instanceof DefaultGraphCell) {
         DefaultGraphCell cell = (DefaultGraphCell)o;
         Map map = GraphConstants.createMap();
         GraphConstants.setMoveable(map, state);
         cell.setAttributes(map);
         this.graph.setSelectionCell(cell);
      }

   }

   private Map getAttributesforEdge(int stereotype_left, int stereotype_right) {
      Map map = GraphConstants.createMap();
      GraphConstants.setLineWidth(map, 1.0F);
      GraphConstants.setEditable(map, false);
      GraphConstants.setEndSize(map, 18);
      GraphConstants.setBeginSize(map, 18);
      if (stereotype_left == 5 || stereotype_right == 5 || stereotype_left == 3 || stereotype_right == 3 || stereotype_left == 6 || stereotype_right == 6) {
         GraphConstants.setDashPattern(map, new float[]{3.0F, 0.0F, 3.0F});
      }

      switch(stereotype_left) {
      case 1:
      case 5:
         GraphConstants.setLineBegin(map, 2);
         break;
      case 2:
      case 3:
         GraphConstants.setLineBegin(map, 4);
         break;
      case 4:
         GraphConstants.setLineBegin(map, 9);
      }

      switch(stereotype_right) {
      case 1:
      case 5:
         GraphConstants.setLineEnd(map, 2);
         break;
      case 2:
      case 3:
         GraphConstants.setLineEnd(map, 4);
         break;
      case 4:
         GraphConstants.setLineEnd(map, 9);
      }

      return map;
   }

   Edge edgeGenerator(int stereotype_left, int stereotype_right, Object name) {
      Edge e = new SpecialEdge(name);
      e.setAttributes(this.getAttributesforEdge(stereotype_left, stereotype_right));
      return e;
   }

   protected void signalElementChange(Object o) {
      if (o instanceof Classifier) {
         int pos = this.actors.indexOf(o);
         if (pos == -1) {
            pos = this.useCases.indexOf(o);
            if (pos == -1) {
               return;
            }

            UseCaseCell cell = (UseCaseCell)this.useCaseViews.get(pos);
            UseCaseView cell_view = (UseCaseView)this.graph.getView().getMapping(cell, false);
            cell_view.signalViewChange();
            this.graph.setSelectionCell(cell);
            this.graph.repaint();
            return;
         }

         ActorCell cell = (ActorCell)this.actorViews.get(pos);
         ActorView cell_view = (ActorView)this.graph.getView().getMapping(cell, false);
         cell_view.signalViewChange();
         this.graph.setSelectionCell(cell);
         this.graph.repaint();
      }

   }

   protected void removeGraphicalItem(Object o) {
      DefaultGraphCell cell = null;
      if (o instanceof DefaultGraphCell) {
         cell = (DefaultGraphCell)o;
      }

      int index;
      if (o instanceof Actor) {
         index = this.actors.indexOf(o);
         if (index == -1) {
            return;
         }

         cell = (DefaultGraphCell)this.actorViews.get(index);
      } else if (o instanceof UseCase) {
         index = this.useCases.indexOf(o);
         if (index == -1) {
            return;
         }

         cell = (DefaultGraphCell)this.useCaseViews.get(index);
      }

      if (cell != null) {
         ArrayList edges = new ArrayList();

         for(int i = 0; i < cell.getChildCount(); ++i) {
            DefaultPort p = (DefaultPort)cell.getChildAt(i);
            Iterator it = p.edges();

            while(it.hasNext()) {
               edges.add(it.next());
            }
         }

         while(edges.size() > 0) {
            DefaultEdge e = (DefaultEdge)edges.get(0);

            for(int idx = this.relationViews.indexOf(e); idx != -1; idx = this.relationViews.indexOf(e)) {
               this.relationViews.remove(idx);
            }

            edges.remove(0);
         }

         Object[] self = new Object[]{cell};
         this.graph.getModel().remove(this.graph.getDescendants(self));
      }
   }

   public void clear() {
      this.model = null;
      this.graph = null;
      this.actors = null;
      this.actorViews = null;
      this.useCases = null;
      this.useCaseViews = null;
      this.relationViews = null;
      this.selected = null;
      this.setUserObject((Object)null);
      this.setContext((ModelElement)null);
   }

   class DiagramModelListener implements GraphModelListener {
      DiagramModelListener() {
      }

      public void graphChanged(GraphModelEvent e) {
         if (UseCaseDiagram.this.ACTION_KIND == 301 || UseCaseDiagram.this.ACTION_KIND == 302) {
            Object[] inserted = e.getChange().getInserted();
            Object[] removed = e.getChange().getRemoved();
            int i;
            Object usr_obj;
            if (inserted != null) {
               for(i = 0; i < inserted.length; ++i) {
                  if (inserted[i] instanceof ActorCell) {
                     usr_obj = ((ActorCell)inserted[i]).getUserObject();
                     if (!UseCaseDiagram.this.actors.contains(usr_obj)) {
                        UseCaseDiagram.this.actors.add(usr_obj);
                        UseCaseDiagram.this.actorViews.add(inserted[i]);
                     }
                  }

                  if (inserted[i] instanceof UseCaseCell) {
                     usr_obj = ((UseCaseCell)inserted[i]).getUserObject();
                     if (!UseCaseDiagram.this.useCases.contains(usr_obj)) {
                        UseCaseDiagram.this.useCases.add(usr_obj);
                        UseCaseDiagram.this.useCaseViews.add(inserted[i]);
                     }
                  }
               }
            }

            if (removed != null) {
               for(i = 0; i < removed.length; ++i) {
                  if (removed[i] instanceof ActorCell) {
                     usr_obj = ((ActorCell)removed[i]).getUserObject();
                     if (UseCaseDiagram.this.actors.contains(usr_obj)) {
                        UseCaseDiagram.this.actors.remove(usr_obj);
                        UseCaseDiagram.this.actorViews.remove(removed[i]);
                     }
                  }

                  if (removed[i] instanceof UseCaseCell) {
                     usr_obj = ((UseCaseCell)removed[i]).getUserObject();
                     if (UseCaseDiagram.this.useCases.contains(usr_obj)) {
                        UseCaseDiagram.this.useCases.remove(usr_obj);
                        UseCaseDiagram.this.useCaseViews.remove(removed[i]);
                     }
                  }
               }
            }
         }

      }
   }

   class DiagramSelectionListener implements GraphSelectionListener {
      DiagramSelectionListener() {
      }

      public void valueChanged(GraphSelectionEvent e) {
         UseCaseDiagram.this.selected.removeAll(UseCaseDiagram.this.selected);

         for(int i = 0; i < UseCaseDiagram.this.graph.getSelectionCells().length; ++i) {
            UseCaseDiagram.this.selected.add(UseCaseDiagram.this.graph.getSelectionCells()[i]);
         }

         UseCaseDiagram.this.graph.getView().toFront(UseCaseDiagram.this.graph.getView().getMapping(UseCaseDiagram.this.selected.toArray()));
         if (e.getCell() instanceof UseCaseCell) {
            UseCaseCell c = (UseCaseCell)e.getCell();
            UseCaseView view = (UseCaseView)UseCaseDiagram.this.graph.getView().getMapping(c, false);
            if (view == null) {
               return;
            }

            Rectangle old_rect = GraphConstants.getBounds(view.getAttributes());
            UseCaseDiagram.this.graph.paintImmediately(old_rect);
            Dimension d = view.getPreferredSize();
            if (old_rect.width < d.width || old_rect.height < d.height) {
               Map map = GraphConstants.createMap();
               GraphConstants.setBounds(map, new Rectangle(old_rect.x, old_rect.y, d.width, d.height));
               view.setAttributes(map);
            }
         }

      }
   }
}
