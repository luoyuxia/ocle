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
import com.jgraph.graph.Port;
import com.jgraph.graph.PortView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.diagrams.filters.ClassFilter;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.gxapi.GXAssociationClassEdge;
import ro.ubbcluj.lci.gxapi.GXClassCell;
import ro.ubbcluj.lci.gxapi.GXClassDiagram;
import ro.ubbcluj.lci.gxapi.GXClassGProperty;
import ro.ubbcluj.lci.gxapi.GXConnectionSet;
import ro.ubbcluj.lci.gxapi.GXDefaultEdge;
import ro.ubbcluj.lci.gxapi.GXDefaultGraphCell;
import ro.ubbcluj.lci.gxapi.GXDefaultMutableTreeNode;
import ro.ubbcluj.lci.gxapi.GXDefaultPort;
import ro.ubbcluj.lci.gxapi.GXHashtable;
import ro.ubbcluj.lci.gxapi.GXPackageCell;
import ro.ubbcluj.lci.gxapi.GXSpecialEdge;
import ro.ubbcluj.lci.gxapi.GXSpecialPort;
import ro.ubbcluj.lci.gxapi.filters.GXClassFilter;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Relationship;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.utils.ErrorMessage;
import ro.ubbcluj.lci.utils.ModelFactory;

public class ClassDiagram extends GAbstractDiagram implements Serializable {
   private transient DefaultGraphModel model = new DefaultGraphModel();
   private transient DiagramGraph graph;
   private ArrayList classes;
   private ArrayList classViews;
   private ArrayList packages;
   private ArrayList packageViews;
   private ArrayList relationViews;
   private transient ArrayList selected;
   private int ACTION_KIND;
   private transient GraphUndoManager undoManager;
   private transient ClassDiagram.DiagramModelListener diagramModelListener;
   private transient DiagramListener listener;
   private ClassFilter globalFilter;
   private static DiagramValidator validator = new ClassDiagramValidator();

   public ClassDiagram(GUMLModel gmodel, ModelElement context) {
      this.graph = new DiagramGraph(this.model);
      this.classes = new ArrayList();
      this.classViews = new ArrayList();
      this.packages = new ArrayList();
      this.packageViews = new ArrayList();
      this.relationViews = new ArrayList();
      this.selected = new ArrayList();
      this.undoManager = new GraphUndoManager();
      this.diagramModelListener = new ClassDiagram.DiagramModelListener();
      this.globalFilter = new ClassFilter();
      this.setUserObject(gmodel);
      this.setContext(context);
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
      this.graph.setScale(0.9D);
      this.graph.addGraphSelectionListener(new ClassDiagram.DiagramSelectionListener());
      this.graph.getModel().addUndoableEditListener(this.undoManager);
      this.graph.getModel().addGraphModelListener(this.diagramModelListener);
      this.listener = new DiagramListener(this);
      this.diagram = new JPanel(new BorderLayout());
      this.diagram.add(new JScrollPane(this.graph), "Center");
   }

   ArrayList getClasses() {
      return this.classes;
   }

   ArrayList getClassViews() {
      return this.classViews;
   }

   ArrayList getPackages() {
      return this.packages;
   }

   ArrayList getPackageViews() {
      return this.packageViews;
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

   public ArrayList getClassCells() {
      return this.classViews;
   }

   public SpecialEdge getViewForRelation(Relationship r) {
      for(int i = 0; i < this.relationViews.size(); ++i) {
         if (((SpecialEdge)this.relationViews.get(i)).getUserObject() == r) {
            return (SpecialEdge)this.relationViews.get(i);
         }
      }

      return null;
   }

   public ArrayList getSelected() {
      return this.selected;
   }

   public ClassFilter getClassFilter() {
      return this.globalFilter;
   }

   public void setSelected(Object o) {
      if (o instanceof ModelElement) {
         DefaultGraphCell view = null;
         int i;
         if (this.classes.contains(o)) {
            i = this.classes.indexOf(o);
            view = (DefaultGraphCell)this.classViews.get(i);
         }

         if (this.packages.contains(o)) {
            i = this.packages.indexOf(o);
            view = (DefaultGraphCell)this.packageViews.get(i);
         }

         if (view == null) {
            for(int j = 0; j < this.relationViews.size(); ++j) {
               Object obj = ((SpecialEdge)this.relationViews.get(j)).getUserObject();
               if (obj == o) {
                  view = (SpecialEdge)this.relationViews.get(j);
                  break;
               }
            }
         }

         if (view != null) {
            this.graph.setSelectionCell(view);
            this.graph.scrollCellToVisible(view);
         }
      }

      if (o instanceof AssociationEnd) {
         this.highlightRole((AssociationEnd)o);
      }

      if (o instanceof DefaultGraphCell) {
         this.graph.setSelectionCell(o);
         this.graph.scrollCellToVisible(o);
      }

      if (o == null) {
         this.graph.clearSelection();
      }

   }

   public void setActionKind(int kind) {
      this.ACTION_KIND = kind;
   }

   public int getActionKind() {
      return this.ACTION_KIND;
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
      return "Class";
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      Object[] cells = (Object[])in.readObject();
      ConnectionSet cs = (ConnectionSet)in.readObject();
      Map attrib = (Map)in.readObject();
      this.setName((String)in.readObject());
      in.defaultReadObject();
      this.model = new DefaultGraphModel();
      this.graph = new DiagramGraph(this.model);
      this.undoManager = new GraphUndoManager();
      this.selected = new ArrayList();
      this.graph.getModel().insert(cells, cs, (ParentMap)null, attrib);
      this.initGraph();
      this.addDiagramListener(this.listener);

      int i;
      for(i = 0; i < this.classes.size(); ++i) {
         ClassCell cc = (ClassCell)this.classViews.get(i);
         cc.getProperty().getPopup().addMouseListener(this.listener);
      }

      for(i = 0; i < this.packages.size(); ++i) {
         PackageCell pc = (PackageCell)this.packageViews.get(i);
         pc.getProperty().getPopup().addMouseListener(this.listener);
      }

   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      Object[] cells = this.graph.getView().order(this.graph.getRoots());
      Object[] flat = DefaultGraphModel.getDescendants(this.graph.getModel(), cells).toArray();
      ConnectionSet cs = ConnectionSet.create(this.graph.getModel(), flat, false);
      Map viewAttributes = GraphConstants.createPropertyMap(flat, this.graph.getView());
      out.writeObject(cells);
      out.writeObject(cs);
      out.writeObject(viewAttributes);
      out.writeObject(this.getName());
      out.defaultWriteObject();
   }

   public GXClassDiagram copy() {
      GXClassDiagram gxcd = new GXClassDiagram();
      gxcd.setName(this.getName());
      gxcd.setIsLocked(this.isLocked);
      gxcd.setOwner(this.getContext());
      gxcd.setGridVisible(this.graph.isGridVisible());
      gxcd.setBackground(this.graph.getBackground().toString());
      gxcd.setClasses(this.classes);
      gxcd.setPackages(this.packages);
      Object[] cells = this.graph.getView().order(this.graph.getRoots());
      Object[] flat = DefaultGraphModel.getDescendants(this.graph.getModel(), cells).toArray();
      ConnectionSet cs = ConnectionSet.create(this.graph.getModel(), flat, false);
      Map viewAttributes = GraphConstants.createPropertyMap(flat, this.graph.getView());
      Hashtable clones = new Hashtable();

      int i;
      Object o;
      for(i = 0; i < flat.length; ++i) {
         o = null;
         if (flat[i] instanceof ClassCell) {
            o = new GXClassCell();
         } else if (flat[i] instanceof PackageCell) {
            o = new GXPackageCell();
         } else if (flat[i] instanceof SpecialEdge) {
            o = new GXSpecialEdge();
         } else if (flat[i] instanceof SpecialPort) {
            o = new GXSpecialPort();
         } else if (flat[i] instanceof AssociationClassEdge) {
            o = new GXAssociationClassEdge();
         } else if (flat[i] instanceof DefaultPort) {
            o = new GXDefaultPort();
         } else if (flat[i] instanceof DefaultEdge) {
            o = new GXDefaultEdge();
         } else if (flat[i] instanceof DefaultGraphCell) {
            o = new GXDefaultGraphCell();
         }

         clones.put(flat[i], o);
      }

      for(i = 0; i < flat.length - 1; ++i) {
         if (flat[i] instanceof SpecialEdge) {
            o = flat[i];

            for(int j = i + 1; j < flat.length; ++j) {
               flat[j - 1] = flat[j];
            }

            flat[flat.length - 1] = o;
         }
      }

      for(i = 0; i < flat.length; ++i) {
         GXDefaultMutableTreeNode clone = (GXDefaultMutableTreeNode)clones.get(flat[i]);
         clone.copy((DefaultMutableTreeNode)flat[i], clones);
      }

      for(i = 0; i < flat.length; ++i) {
         gxcd.addFlat((GXDefaultGraphCell)clones.get(flat[i]));
      }

      for(i = 0; i < cells.length; ++i) {
         gxcd.addCells((GXDefaultGraphCell)clones.get(cells[i]));
      }

      Iterator it = this.classViews.iterator();

      while(it.hasNext()) {
         gxcd.addClassViews(clones.get(it.next()));
      }

      it = this.packageViews.iterator();

      while(it.hasNext()) {
         gxcd.addPackageViews(clones.get(it.next()));
      }

      it = this.relationViews.iterator();

      while(it.hasNext()) {
         gxcd.addRelationViews(clones.get(it.next()));
      }

      GXConnectionSet csclone = new GXConnectionSet();
      clones.put(cs, csclone);
      csclone.copy(cs, clones);
      gxcd.setConnectionSet(csclone);
      GXHashtable gxh = new GXHashtable();
      gxh.copy((Hashtable)viewAttributes, clones);
      gxcd.setViewAttributes(gxh);
      GXClassFilter gxcf = new GXClassFilter();
      gxcf.copy(this.globalFilter);
      gxcd.setGlobalFilter(gxcf);
      return gxcd;
   }

   public void extractData(GXClassDiagram gxcd) {
      this.initGraph();
      this.addDiagramListener(this.listener);
      this.setName(gxcd.getName());
      this.isLocked = gxcd.getIsLocked();
      this.classes = gxcd.getCollectionClasses();
      this.packages = gxcd.getCollectionPackages();
      Hashtable clones = new Hashtable();

      Enumeration en;
      Object flatObj;
      Object clone;
      for(en = gxcd.getFlatList(); en.hasMoreElements(); clones.put(flatObj, clone)) {
         flatObj = en.nextElement();
         clone = null;
         if (flatObj instanceof GXClassCell) {
            clone = new ClassCell();
         } else if (flatObj instanceof GXPackageCell) {
            clone = new PackageCell();
         } else if (flatObj instanceof GXSpecialEdge) {
            clone = new SpecialEdge();
         } else if (flatObj instanceof GXSpecialPort) {
            clone = new SpecialPort();
         } else if (flatObj instanceof GXAssociationClassEdge) {
            clone = new AssociationClassEdge();
         } else if (flatObj instanceof GXDefaultPort) {
            clone = new DefaultPort();
         } else if (flatObj instanceof GXDefaultEdge) {
            clone = new DefaultEdge();
         } else if (flatObj instanceof GXDefaultGraphCell) {
            clone = new DefaultGraphCell();
         }
      }

      en = gxcd.getFlatList();

      while(en.hasMoreElements()) {
         GXDefaultMutableTreeNode flatObj1 = (GXDefaultMutableTreeNode)en.nextElement();
         DefaultMutableTreeNode clone1 = (DefaultMutableTreeNode)clones.get(flatObj1);
         flatObj1.extractData(clone1, clones);
      }

      Object[] cells = new Object[gxcd.getCollectionCells().size()];
      int i = 0;

      Object relationView;
      for(en = gxcd.getCellsList(); en.hasMoreElements(); cells[i++] = clones.get(relationView)) {
         relationView = en.nextElement();
      }

      en = gxcd.getClassViewsList();

      while(en.hasMoreElements()) {
         relationView = en.nextElement();
         this.classViews.add(clones.get(relationView));
      }

      en = gxcd.getPackageViewsList();

      while(en.hasMoreElements()) {
         relationView = en.nextElement();
         this.packageViews.add(clones.get(relationView));
      }

      en = gxcd.getRelationViewsList();

      while(en.hasMoreElements()) {
         relationView = en.nextElement();
         this.relationViews.add(clones.get(relationView));
      }

      ConnectionSet cs = new ConnectionSet();
      clones.put(gxcd.getConnectionSet(), cs);
      gxcd.getConnectionSet().extractData(cs, clones);
      Hashtable attrib = new Hashtable();
      gxcd.getViewAttributes().extractData(attrib, clones);
      this.globalFilter = new ClassFilter();
      gxcd.getGlobalFilter().extractData(this.globalFilter);
      this.model = new DefaultGraphModel();
      this.graph = new DiagramGraph(this.model);
      this.undoManager = new GraphUndoManager();
      this.selected = new ArrayList();
      this.graph.getModel().insert(cells, cs, (ParentMap)null, attrib);
      this.initGraph();
      this.graph.setGridVisible(gxcd.getGridVisible());
      this.graph.setGridEnabled(gxcd.getGridVisible());
      this.graph.setBackground(GXClassGProperty.stringToColor(gxcd.getBackground()));
      this.graph.repaint();
      this.addDiagramListener(this.listener);

      for(i = 0; i < this.classes.size(); ++i) {
         ClassCell cc = (ClassCell)this.classViews.get(i);
         cc.getProperty().getPopup().addMouseListener(this.listener);
      }

      for(i = 0; i < this.packages.size(); ++i) {
         PackageCell pc = (PackageCell)this.packageViews.get(i);
         pc.getProperty().getPopup().addMouseListener(this.listener);
      }

   }

   public void addElement(Object o, Point location, boolean isNew) {
      this.zoomDefault();
      if (this.contains(o)) {
         GMainFrame.getMainFrame().updateMessages((Object)"Element already added to this diagram.");
      } else {
         if (o instanceof Package) {
            this.addPackage(o, location, isNew);
         } else if (o instanceof Classifier) {
            this.addClass(o, location, isNew);
         } else {
            GMainFrame.getMainFrame().updateMessages((Object)"Cannot add this type of element to this diagram.");
         }

      }
   }

   public void addRelation(Object from, Object to) {
      this.addRelation(from, to, this.getStereotype());
   }

   public void zoomDefault() {
      this.graph.setScale(0.9D);
   }

   public void zoomIn() {
      this.graph.setScale(1.5D * this.graph.getScale());
   }

   public void zoomOut() {
      this.graph.setScale(this.graph.getScale() / 1.5D);
   }

   public void undo() {
      try {
         if (this.undoManager.canUndo()) {
            this.undoManager.undo(this.graph.getView());
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void redo() {
      try {
         if (this.undoManager.canRedo()) {
            this.undoManager.redo(this.graph.getView());
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void highlightRole(AssociationEnd end) {
      Association asoc = end.getAssociation();
      Classifier cls = end.getParticipant();
      SpecialEdge edge = null;

      for(int i = 0; i < this.relationViews.size(); ++i) {
         if (((DefaultGraphCell)this.relationViews.get(i)).getUserObject() == asoc) {
            edge = (SpecialEdge)this.relationViews.get(i);
         }

         ((SpecialEdge)this.relationViews.get(i)).higlightLabel(this.graph, true, true, true);
      }

      if (edge != null) {
         if (edge.getSource() != null && edge.getTarget() != null) {
            Object beginCls = ((DefaultGraphCell)((SpecialPort)edge.getSource()).getParent()).getUserObject();
            Object endCls = ((DefaultGraphCell)((SpecialPort)edge.getTarget()).getParent()).getUserObject();
            edge.higlightLabel(this.graph, true, true, true);
            if (beginCls == endCls && edge.beginName != null && edge.endName != null) {
               String beginName = edge.beginName.substring(1);
               String endName = edge.endName.substring(1);
               if (beginName.equals(end.getName().trim())) {
                  edge.higlightLabel(this.graph, true, false, false);
               } else if (endName.equals(end.getName().trim())) {
                  edge.higlightLabel(this.graph, false, true, false);
               }

            } else {
               if (beginCls == cls) {
                  edge.higlightLabel(this.graph, true, false, false);
               } else {
                  edge.higlightLabel(this.graph, false, true, false);
               }

            }
         }
      }
   }

   protected void addRelation(Object from, Object to, int stereotype) {
      DefaultGraphCell from_cell = null;
      DefaultGraphCell to_cell = null;
      if (from instanceof DefaultGraphCell && to instanceof DefaultGraphCell) {
         from_cell = (DefaultGraphCell)from;
         to_cell = (DefaultGraphCell)to;
         if (!(from_cell.getUserObject() instanceof String) && !(to_cell.getUserObject() instanceof String)) {
            this.addGraphicalRelation(from_cell, to_cell, stereotype);
         }
      } else {
         int from_index;
         int to_index;
         if (this.isPackage(from) && this.isPackage(to)) {
            from_index = this.packages.indexOf(from);
            to_index = this.packages.indexOf(to);
            if (from_index == -1 || to_index == -1) {
               GMainFrame.getMainFrame().updateMessages((Object)"AddRelation:Can't find package!");
               return;
            }

            from_cell = (DefaultGraphCell)this.packageViews.get(from_index);
            to_cell = (DefaultGraphCell)this.packageViews.get(to_index);
         } else if (from instanceof Classifier && to instanceof Classifier) {
            from_index = this.classes.indexOf(from);
            to_index = this.classes.indexOf(to);
            if (from_index == -1 || to_index == -1) {
               GMainFrame.getMainFrame().updateMessages((Object)"AddRelation:Can't find package!");
               return;
            }

            from_cell = (DefaultGraphCell)this.classViews.get(from_index);
            to_cell = (DefaultGraphCell)this.classViews.get(to_index);
         }

         this.addGraphicalRelation(from_cell, to_cell, stereotype);
      }
   }

   public void updateItem(Object o) {
      if (o instanceof AssociationEnd) {
         SpecialEdge edge = (SpecialEdge)DiagramUtilities.updateGraphicalRelation(((AssociationEnd)o).getAssociation(), this.relationViews, this.graph);
         if (edge != null) {
            this.addLabelsForAsociation(edge);
         }

         this.graph.repaint();
      } else if (this.classes.contains(o) || this.packages.contains(o)) {
         this.signalElementChange(o);
      }

   }

   public void removeItem(Object o) {
      this.removeGraphicalItem(o);
      int i;
      if (o instanceof Package) {
         i = this.packages.indexOf(o);
         if (i != -1) {
            this.packages.remove(i);
            this.packageViews.remove(i);
         }
      } else if (o instanceof Classifier) {
         i = this.classes.indexOf(o);
         if (i != -1) {
            this.classes.remove(i);
            this.classViews.remove(i);
         }
      } else if (o instanceof PackageCell) {
         i = this.packageViews.indexOf(o);
         if (i != -1) {
            this.packages.remove(i);
            this.packageViews.remove(i);
         }
      } else if (o instanceof ClassCell) {
         i = this.classViews.indexOf(o);
         if (i != -1) {
            this.classes.remove(i);
            this.classViews.remove(i);
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
      if (o instanceof Edge) {
         return this.relationViews.contains(o);
      } else if (o instanceof Relationship) {
         for(int i = 0; i < this.relationViews.size(); ++i) {
            if (((DefaultGraphCell)this.relationViews.get(i)).getUserObject() == o) {
               return true;
            }
         }

         return false;
      } else {
         return this.classes.contains(o) || this.packages.contains(o);
      }
   }

   public void lockItem(Object o, boolean state) {
      this.setDragable(o, !state);
   }

   public void lockDiagram(boolean state) {
      this.isLocked = !state;

      int i;
      DefaultGraphCell cell;
      Map attrs;
      for(i = 0; i < this.classViews.size(); ++i) {
         cell = (DefaultGraphCell)this.classViews.get(i);
         attrs = cell.getAttributes();
         GraphConstants.setMoveable(attrs, state);
         cell.setAttributes(attrs);
      }

      for(i = 0; i < this.packageViews.size(); ++i) {
         cell = (DefaultGraphCell)this.packageViews.get(i);
         attrs = cell.getAttributes();
         GraphConstants.setMoveable(attrs, state);
         cell.setAttributes(attrs);
      }

      for(i = 0; i < this.relationViews.size(); ++i) {
         SpecialEdge edge = (SpecialEdge)this.relationViews.get(i);
         attrs = edge.getAttributes();
         GraphConstants.setMoveable(attrs, state);
         GraphConstants.setBendable(attrs, state);
         edge.setAttributes(attrs);
         ArrayList labels = edge.getLabels();
         if (labels != null) {
            for(int j = 0; j < labels.size(); ++j) {
                cell = (DefaultGraphCell)labels.get(j);
               Map attr = cell.getAttributes();
               GraphConstants.setMoveable(attr, state);
               cell.setAttributes(attr);
            }
         }
      }

   }

   public void addDiagramListener(DiagramListener dl) {
      this.graph.addMouseListener(dl);
      this.graph.addMouseMotionListener(dl);
      this.graph.addKeyListener(dl);
   }

   private Edge getAutoRelation(ClassCell cell) {
      if (cell.hasAutoRelation() && cell.getChildCount() >= 2) {
         ArrayList ports = new ArrayList();

         for(int i = 1; i < cell.getChildCount(); ++i) {
            ports.add(cell.getChildAt(i));
         }

         SpecialPort port = (SpecialPort)cell.getChildAt(0);
         Iterator it = port.edges();

         Edge e;
         do {
            if (!it.hasNext()) {
               return null;
            }

            e = (Edge)it.next();
         } while(!ports.contains(e.getSource()) && !ports.contains(e.getTarget()));

         return e;
      } else {
         return null;
      }
   }

   private void deselectLabelsFromAssociation() {
      for(int i = 0; i < this.relationViews.size(); ++i) {
         SpecialEdge e = (SpecialEdge)this.relationViews.get(i);
         e.higlightLabel(this.graph, true, true, true);
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
         GraphConstants.setLineBegin(map, 2);
         break;
      case 2:
         GraphConstants.setLineBegin(map, 4);
         break;
      case 3:
         GraphConstants.setLineBegin(map, 4);
         break;
      case 4:
         GraphConstants.setLineBegin(map, 9);
         break;
      case 5:
         GraphConstants.setLineBegin(map, 2);
      }

      switch(stereotype_right) {
      case 1:
         GraphConstants.setLineEnd(map, 2);
         break;
      case 2:
         GraphConstants.setLineEnd(map, 4);
         break;
      case 3:
         GraphConstants.setLineEnd(map, 4);
         break;
      case 4:
         GraphConstants.setLineEnd(map, 9);
         break;
      case 5:
         GraphConstants.setLineEnd(map, 2);
      }

      return map;
   }

   Edge edgeGenerator(int stereotype_left, int stereotype_right, Object relation) {
      Edge e = new SpecialEdge(relation);
      ((SpecialEdge)e).add(new SpecialPort());
      e.setAttributes(this.getAttributesforEdge(stereotype_left, stereotype_right));
      return e;
   }

   private int getStereotype() {
      switch(this.ACTION_KIND) {
      case 101:
         return 2;
      case 102:
         return 1;
      case 103:
         return 3;
      case 104:
         return 3;
      case 105:
         return 5;
      case 106:
         return 6;
      default:
         return 0;
      }
   }

   void drawExistingAutoAssociation(DefaultGraphCell cell, Association asoc, int left_end, int right_end) {
      ConnectionSet cs = new ConnectionSet();
      SpecialPort port = new SpecialPort();
      Map attrs = GraphConstants.createMap();
      int u = 1000;
      GraphConstants.setOffset(attrs, new Point(u / 2, 0));
      port.setAttributes(attrs);
      cell.add(port);
      Edge edge = this.edgeGenerator(left_end, right_end, asoc);
      cs.connect(edge, this.model.getChild(cell, 0), true);
      cs.connect(edge, this.model.getChild(cell, 1), false);
      Object[] insert = new Object[]{edge};
      this.model.insert(insert, cs, (ParentMap)null, (Map)null);
      this.relationViews.add(edge);
      this.graph.setSelectionCell(edge);
      SpecialEdge specialEdge = (SpecialEdge)DiagramUtilities.updateGraphicalRelation(asoc, this.relationViews, this.graph);
      if (specialEdge != null) {
         this.addLabelsForAsociation(specialEdge);
      }

      this.graph.removeSelectionCell(edge);
      EdgeView eview = (EdgeView)this.graph.getView().getMapping(edge, false);
      CellView cview = this.graph.getView().getMapping(cell, false);
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
      this.graph.repaint();
   }

   protected void addClass(Object clasa, Point loc, boolean isNew) {
      Classifier cls = (Classifier)clasa;
      this.classes.add(cls);
      ClassCell cgc = new ClassCell(cls);
      if (this.getContext() != cls.getNamespace().getNamespace()) {
         cgc.setShowPackage(true);
      } else {
         cgc.setShowPackage(false);
      }

      cgc.setLocation(loc);
      if (cls instanceof Interface) {
         cgc.setIsInterface(true);
      }

      GProperty prop = GProperty.createProperty(cgc);
      prop.getPopup().addMouseListener(this.listener);
      cgc.setProperty(prop);
      SpecialPort port = new SpecialPort();
      cgc.add(port);
      this.classViews.add(cgc);
      Map map = GraphConstants.createMap();
      Map attributeMap = new Hashtable();
      Point pt = this.graph.snap(new Point(loc));
      GraphConstants.setBounds(map, new Rectangle(pt, DiagramUtilities.getDefaultSize(cgc)));
      GraphConstants.setOpaque(map, true);
      attributeMap.put(cgc, map);
      Object[] insert = new Object[]{cgc};
      this.model.insert(insert, (ConnectionSet)null, (ParentMap)null, attributeMap);
      if (!isNew) {
         validator.validate(cls, this);
      }

   }

   protected void addPackage(Object o, Point loc, boolean isNew) {
      Package pack = (Package)o;
      this.packages.add(pack);
      PackageCell pgc = new PackageCell(pack);
      if (this.getContext() != pack.getNamespace().getNamespace()) {
         pgc.setShowPackage(true);
      } else {
         pgc.setShowPackage(false);
      }

      GProperty prop = GProperty.createProperty(pgc);
      prop.getPopup().addMouseListener(this.listener);
      pgc.setProperty(prop);
      SpecialPort port = new SpecialPort();
      pgc.add(port);
      this.packageViews.add(pgc);
      Map map = GraphConstants.createMap();
      Map attributeMap = new Hashtable();
      Point pt = this.graph.snap(new Point(loc));
      GraphConstants.setBounds(map, new Rectangle(pt, DiagramUtilities.getDefaultSize(pgc)));
      GraphConstants.setOpaque(map, true);
      attributeMap.put(pgc, map);
      Object[] insert = new Object[]{pgc};
      this.model.insert(insert, (ConnectionSet)null, (ParentMap)null, attributeMap);
      if (!isNew) {
         validator.validate(pack, this);
      }

      this.graph.setSelectionCell(pgc);
   }

   protected void removeGraphicalItem(Object o) {
      DefaultGraphCell cell = null;

      if (o instanceof DefaultGraphCell) {
         cell = (DefaultGraphCell)o;
      }

      int index;
      if (o instanceof Package) {
         index = this.packages.indexOf(o);
         if (index == -1) {
            return;
         }

         cell = (DefaultGraphCell)this.packageViews.get(index);
      } else if (o instanceof Classifier) {
         index = this.classes.indexOf(o);
         if (index == -1) {
            return;
         }

         cell = (DefaultGraphCell)this.classViews.get(index);
      }

      if (cell != null) {
         ArrayList edges = new ArrayList();
         Object[] self;
         if (this.isGroup(cell)) {
            self = new Object[]{cell};
            this.ungroupCells(self);
         }

         for(int i = 0; i < cell.getChildCount(); ++i) {
            DefaultPort p = (DefaultPort)cell.getChildAt(i);
            Iterator it = p.edges();

            while(it.hasNext()) {
               edges.add(it.next());
            }
         }

         int j;
         while(edges.size() > 0) {
            DefaultEdge e = (DefaultEdge)edges.get(0);

            for(j = this.relationViews.indexOf(e); j != -1; j = this.relationViews.indexOf(e)) {
               this.relationViews.remove(j);
            }

            edges.remove(0);
            if (e.getUserObject() instanceof AssociationClass && e.getUserObject() != cell.getUserObject()) {
               int k = this.classes.indexOf(e.getUserObject());
               if (k != -1) {
                  DefaultGraphCell theCell = (DefaultGraphCell)this.classViews.get(k);
                  this.removeItem(theCell);
               }
            }

            ArrayList labels = ((SpecialEdge)e).getLabels();
            if (labels != null) {
               for( j = 0; j < labels.size(); ++j) {
                  Object[] text = new Object[]{labels.get(j)};
                  this.graph.getModel().remove(text);
               }
            }

            Object[] redges = new Object[]{e};
            this.graph.getModel().remove(redges);
         }

         if (cell instanceof SpecialEdge && cell.getUserObject() instanceof Association) {
            ArrayList labels = ((SpecialEdge)cell).getLabels();
            if (labels != null) {
               for(j = 0; j < labels.size(); ++j) {
                  Object[] text = new Object[]{labels.get(j)};
                  this.graph.getModel().remove(text);
               }
            }
         }

         self = new Object[]{cell};
         this.graph.getModel().remove(this.graph.getDescendants(self));
      }
   }

   protected void addGraphicalRelation(Object cell1, Object cell2, int stereotype) {
      DefaultGraphCell from_cell = (DefaultGraphCell)cell1;
      DefaultGraphCell to_cell = (DefaultGraphCell)cell2;
      ConnectionSet cs = new ConnectionSet();
      String[] types = new String[]{"Relation", "Association", "Generalization", "Dependency", "Permission", "Realization", "AssociationClass"};
      ModelElement supplier = (ModelElement)from_cell.getUserObject();
      ModelElement client = (ModelElement)to_cell.getUserObject();
      Object relation = ModelFactory.createNewRelation(supplier, client, (ModelElement)null, types[this.ACTION_KIND - 100]);
      if (relation == null) {
         GMainFrame.getMainFrame().updateMessages((Object)("Illegal " + types[this.ACTION_KIND - 100] + " relation."));
      } else if (relation instanceof ErrorMessage) {
         GMainFrame.getMainFrame().updateMessages((Object)((ErrorMessage)relation).getErrorMessage());
      } else {
         if (this.ACTION_KIND == 106) {
            from_cell.setUserObject(relation);
            to_cell.setUserObject(relation);
            AssociationClass ac = (AssociationClass)relation;

            for(int i = 0; i < this.classes.size(); ++i) {
               if (((ModelElement)this.classes.get(i)).getName().equals(ac.getName())) {
                  this.classes.remove(i);
                  this.classes.add(i, relation);
                  break;
               }
            }

            ModelFactory.fireModelEvent(supplier, (Object)null, 30);
            ModelFactory.fireModelEvent(client, (Object)null, 30);
            ModelFactory.fireModelEvent(relation, (Object)null, 0);
         }

         Map to_port;
         if (from_cell == to_cell && from_cell.getChildCount() == 1) {
            SpecialPort port = new SpecialPort();
            to_port = GraphConstants.createMap();
            int u = 1000;
            GraphConstants.setOffset(to_port, new Point(u / 2, 0));
            port.setAttributes(to_port);
            from_cell.add(port);
            if (from_cell instanceof ClassCell) {
               ((ClassCell)from_cell).setAutoRelation(true);
            }
         }

         DefaultPort from_port = (DefaultPort)this.model.getChild(from_cell, 0);

         DefaultPort to_port1;
         if (from_cell == to_cell) {
            to_port1 = (DefaultPort)this.model.getChild(to_cell, 1);
         } else {
            to_port1 = (DefaultPort)this.model.getChild(to_cell, 0);
         }

         Edge edge = null;
         if (relation instanceof Association) {
            if (relation instanceof AssociationClass) {
               edge = this.edgeGenerator(6, 6, relation);
            } else {
               edge = this.edgeGenerator(0, 2, relation);
            }
         } else {
            edge = this.edgeGenerator(0, stereotype, relation);
         }

         cs.connect(edge, from_port, true);
         cs.connect(edge, to_port1, false);
         Object[] insert = new Object[]{edge};
         this.model.insert(insert, cs, (ParentMap)null, (Map)null);
         this.relationViews.add(edge);
         if (from_cell == to_cell) {
            EdgeView eview = (EdgeView)this.graph.getView().getMapping(edge, false);
            CellView cview = this.graph.getView().getMapping(from_cell, false);
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
            this.graph.repaint();
         }

         if (relation instanceof Association) {
            this.addLabelsForAsociation((SpecialEdge)edge);
         }

         this.checkMultipleRelations(from_port, to_port1, edge);
      }
   }

   protected void addLabelsForAsociation(SpecialEdge edge) {
      edge.getStringForRelation(this.graph);
   }

   void checkMultipleRelations(Port from_port, Port to_port, Edge edge) {
      if (from_port != to_port) {
         boolean hasAutoR = false;
         Iterator it = from_port.edges();

         while(it.hasNext()) {
            Edge e = (Edge)it.next();
            if (e != edge && (e.getSource() == to_port || e.getTarget() == to_port)) {
               hasAutoR = true;
               break;
            }
         }

         if (hasAutoR) {
            PortView from_view = (PortView)this.graph.getView().getMapping(from_port, false);
            PortView to_view = (PortView)this.graph.getView().getMapping(to_port, false);
            EdgeView edge_view = (EdgeView)this.graph.getView().getMapping(edge, false);
            Point from_loc = from_view.getLocation(edge_view);
            Point to_loc = to_view.getLocation(edge_view);
            Point newP = new Point((from_loc.x + to_loc.x) / 2, (from_loc.y + to_loc.y) / 2);
            edge_view.addPoint(1, newP);
         }
      }
   }

   protected int[] getNavigabilityForNewAssociation(Association asoc, DefaultGraphCell source, DefaultGraphCell target) {
      int[] result = new int[]{0, 0};
      Enumeration asoc_ends = asoc.getConnectionList();

      try {
         while(asoc_ends.hasMoreElements()) {
            AssociationEnd end = (AssociationEnd)asoc_ends.nextElement();
            if (!end.isNavigable() && end.getParticipant() == source.getUserObject()) {
               result[1] = 4;
            }

            if (!end.isNavigable() && end.getParticipant() == target.getUserObject()) {
               result[0] = 4;
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return result;
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

   protected void signalElementChange(Object o) {
      if (o instanceof Classifier) {
         int pos = this.classes.indexOf(o);
         if (pos == -1) {
            return;
         }

         ClassCell cell = (ClassCell)this.classViews.get(pos);
         ClassView cell_view = (ClassView)this.graph.getView().getMapping(cell, false);
         cell_view.signalViewChange();
         this.graph.setSelectionCell(cell);
      }

      this.graph.repaint();
   }

   private boolean isPackage(Object o) {
      return o instanceof Package || o instanceof PackageCell;
   }

   private ArrayList filter() {
      ArrayList result = new ArrayList();

      for(int i = 0; i < this.selected.size(); ++i) {
         Object o = this.selected.get(i);
         if (o instanceof ClassCell || o instanceof PackageCell) {
            result.add(o);
         }
      }

      return result;
   }

   private int getTopLeftMin(ArrayList cells, char coord) {
      int min = 2147483647;

      for(int i = 0; i < cells.size(); ++i) {
         DefaultGraphCell cell = (DefaultGraphCell)cells.get(i);
         CellView cview = this.graph.getView().getMapping(cell, false);
         Rectangle rect = GraphConstants.getBounds(cview.getAttributes());
         if (coord == 'x' && rect.x < min) {
            min = rect.x;
         }

         if (coord == 'y' && rect.y < min) {
            min = rect.y;
         }
      }

      return min;
   }

   AssociationEnd getAssociationEndFromLabel(DefaultGraphCell labelCell) {
      if (!(labelCell.getUserObject() instanceof String)) {
         return null;
      } else {
         for(int i = 0; i < this.relationViews.size(); ++i) {
            SpecialEdge edge = (SpecialEdge)this.relationViews.get(i);
            if (edge.getLabels() != null && edge.getLabels().contains(labelCell)) {
               Association asoc = (Association)edge.getUserObject();
               String endName = null;
               int idx = edge.getLabels().indexOf(labelCell);
               if (idx % 2 == 0) {
                  ++idx;
               }

               endName = (String)((DefaultGraphCell)edge.getLabels().get(idx)).getUserObject();
               if (endName.length() > 1) {
                  endName = endName.substring(1);
               }

               Enumeration asocEnds = asoc.getConnectionList();

               while(asocEnds.hasMoreElements()) {
                  AssociationEnd aend = (AssociationEnd)asocEnds.nextElement();
                  if (endName.equals(aend.getName().trim())) {
                     return aend;
                  }
               }
            }
         }

         return null;
      }
   }

   void groupCells(Object[] cells) {
      cells = this.graph.getView().order(cells);
      if (cells != null && cells.length > 0) {
         DefaultGraphCell group = new DefaultGraphCell();
         ParentMap parrentMap = new ParentMap();

         for(int i = 0; i < cells.length; ++i) {
            parrentMap.addEntry(cells[i], group);
         }

         boolean repeat = true;

         do {
            repeat = true;

            for(int i = 0; i < cells.length - 1; ++i) {
               CellView view1 = this.graph.getView().getMapping(cells[i], false);
               CellView view2 = this.graph.getView().getMapping(cells[i + 1], false);
               Rectangle rect1 = GraphConstants.getBounds(view1.getAttributes());
               Rectangle rect2 = GraphConstants.getBounds(view2.getAttributes());
               int w1 = rect1.getLocation().x + rect1.width;
               int w2 = rect2.getLocation().x + rect2.width;
               if (w1 < w2) {
                  Object aux = cells[i];
                  cells[i] = cells[i + 1];
                  cells[i + 1] = aux;
                  repeat = false;
               }
            }
         } while(!repeat);

         Object[] arg = new Object[]{group};
         this.graph.getModel().insert(arg, (ConnectionSet)null, parrentMap, (Map)null);
         this.graph.getView().toFront(this.graph.getView().getMapping(cells));
      }

   }

   boolean isGroup(Object cell) {
      CellView view = this.graph.getView().getMapping(cell, false);
      if (view != null) {
         return !view.isLeaf();
      } else {
         return false;
      }
   }

   void ungroupCells(Object[] cells) {
      if (cells != null && cells.length > 0) {
         ArrayList groups = new ArrayList();
         ArrayList children = new ArrayList();

         for(int i = 0; i < cells.length; ++i) {
            if (this.isGroup(cells[i])) {
               groups.add(cells[i]);

               for(int j = 0; j < this.model.getChildCount(cells[i]); ++j) {
                  children.add(this.model.getChild(cells[i], j));
               }
            }
         }

         this.model.remove(groups.toArray());
         this.graph.setSelectionCells(children.toArray());
         children = null;
         groups = null;
      }

   }

   void alignOnMinimum(int type) {
      ArrayList cells = this.filter();
      if (cells.size() > 1) {
         int min;
         if (type == 501) {
            min = this.getTopLeftMin(cells, 'x');
         } else {
            if (type != 503) {
               return;
            }

            min = this.getTopLeftMin(cells, 'y');
         }

         for(int i = 0; i < cells.size(); ++i) {
            DefaultGraphCell cell = (DefaultGraphCell)cells.get(i);
            CellView cview = this.graph.getView().getMapping(cell, false);
            Rectangle rect = GraphConstants.getBounds(cview.getAttributes());
            Rectangle newRect = new Rectangle(rect);
            if (type == 501) {
               newRect.x = min;
            } else {
               newRect.y = min;
            }

            Map attrs = cview.getAttributes();
            GraphConstants.setBounds(attrs, newRect);
            cview.setAttributes(attrs);
         }

         this.graph.repaint();
         DiagramUtilities.deselectCells(this.graph);
      }
   }

   private int getBottomRightMax(ArrayList cells, char coord) {
      int max = -2147483648;

      for(int i = 0; i < cells.size(); ++i) {
         DefaultGraphCell cell = (DefaultGraphCell)cells.get(i);
         CellView cview = this.graph.getView().getMapping(cell, false);
         Rectangle rect = GraphConstants.getBounds(cview.getAttributes());
         if (coord == 'x' && rect.width + rect.x > max) {
            max = rect.width + rect.x;
         }

         if (coord == 'y' && rect.height + rect.y > max) {
            max = rect.height + rect.y;
         }
      }

      return max;
   }

   void alignOnMaximum(int type) {
      ArrayList cells = this.filter();
      if (cells.size() > 1) {
         int max = 0;
         if (type == 502) {
            max = this.getBottomRightMax(cells, 'x');
         } else if (type == 504) {
            max = this.getBottomRightMax(cells, 'y');
         }

         for(int i = 0; i < cells.size(); ++i) {
            DefaultGraphCell cell = (DefaultGraphCell)cells.get(i);
            CellView cview = this.graph.getView().getMapping(cell, false);
            Rectangle rect = GraphConstants.getBounds(cview.getAttributes());
            Rectangle newRect = new Rectangle(rect);
            if (type == 502) {
               newRect.x = newRect.x + max - (newRect.x + newRect.width);
            } else {
               newRect.y = newRect.y + max - (newRect.y + newRect.height);
            }

            Map attrs = cview.getAttributes();
            GraphConstants.setBounds(attrs, newRect);
            cview.setAttributes(attrs);
         }

         this.graph.repaint();
         DiagramUtilities.deselectCells(this.graph);
      }
   }

   public void clear() {
      this.graph.getModel().removeUndoableEditListener(this.undoManager);
      this.graph.getModel().removeGraphModelListener(this.diagramModelListener);
      this.model = null;
      this.classes.clear();
      this.classViews.clear();
      this.packages.clear();
      this.packageViews.clear();
      this.relationViews.clear();
      this.selected.clear();
      this.setUserObject((Object)null);
      this.setContext((ModelElement)null);
   }

   class DiagramModelListener implements GraphModelListener {
      DiagramModelListener() {
      }

      public void graphChanged(GraphModelEvent e) {
         if (ClassDiagram.this.ACTION_KIND == 301 || ClassDiagram.this.ACTION_KIND == 302) {
            Object[] inserted = e.getChange().getInserted();
            Object[] removed = e.getChange().getRemoved();
            int i;
            Object usr_obj;
            if (inserted != null) {
               for(i = 0; i < inserted.length; ++i) {
                  if (inserted[i] instanceof ClassCell) {
                     usr_obj = ((ClassCell)inserted[i]).getUserObject();
                     if (!ClassDiagram.this.classes.contains(usr_obj)) {
                        ClassDiagram.this.classes.add(usr_obj);
                        ClassDiagram.this.classViews.add(inserted[i]);
                     }
                  }

                  if (inserted[i] instanceof PackageCell) {
                     usr_obj = ((PackageCell)inserted[i]).getUserObject();
                     if (!ClassDiagram.this.packages.contains(usr_obj)) {
                        ClassDiagram.this.packages.add(usr_obj);
                        ClassDiagram.this.packageViews.add(inserted[i]);
                     }
                  }

                  if (inserted[i] instanceof Edge && !ClassDiagram.this.relationViews.contains(inserted[i])) {
                     ClassDiagram.this.relationViews.add(inserted[i]);
                  }
               }
            }

            if (removed != null) {
               for(i = 0; i < removed.length; ++i) {
                  if (removed[i] instanceof ClassCell) {
                     usr_obj = ((ClassCell)removed[i]).getUserObject();
                     if (ClassDiagram.this.classes.contains(usr_obj)) {
                        ClassDiagram.this.classes.remove(usr_obj);
                        ClassDiagram.this.classViews.remove(removed[i]);
                     }
                  }

                  if (removed[i] instanceof PackageCell) {
                     usr_obj = ((PackageCell)removed[i]).getUserObject();
                     if (ClassDiagram.this.packages.contains(usr_obj)) {
                        ClassDiagram.this.packages.remove(usr_obj);
                        ClassDiagram.this.packageViews.remove(removed[i]);
                     }
                  }

                  if (removed[i] instanceof Edge && ClassDiagram.this.relationViews.contains(removed[i])) {
                     ClassDiagram.this.relationViews.remove(removed[i]);
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
         ClassDiagram.this.selected.removeAll(ClassDiagram.this.selected);

         for(int i = 0; i < ClassDiagram.this.graph.getSelectionCells().length; ++i) {
            ClassDiagram.this.selected.add(ClassDiagram.this.graph.getSelectionCells()[i]);
         }

         if (ClassDiagram.this.selected.size() == 0) {
            ClassDiagram.this.deselectLabelsFromAssociation();
         }

         Object[] cells = ClassDiagram.this.selected.toArray();

         for(int ix = 0; ix < cells.length; ++ix) {
            Object[] cell = new Object[]{cells[ix]};
            if (((DefaultGraphCell)cells[ix]).getParent() == null) {
               ClassDiagram.this.graph.getView().toFront(ClassDiagram.this.graph.getView().getMapping(cell));
            }
         }

         if (e.getCell() instanceof SpecialEdge && ((SpecialEdge)e.getCell()).getUserObject() instanceof Association) {
            ((SpecialEdge)e.getCell()).higlightLabel(ClassDiagram.this.graph, true, true, true);
         }

         Dimension d;
         Map map;
         Rectangle old_rect;
         if (e.getCell() instanceof ClassCell) {
            ClassCell c = (ClassCell)e.getCell();
            ClassView view = (ClassView)ClassDiagram.this.graph.getView().getMapping(c, false);
            if (view == null) {
               return;
            }

            if (e.isAddedCell(c) && c.hasAutoRelation()) {
               Edge edge = ClassDiagram.this.getAutoRelation(c);
               if (edge != null) {
                  ClassDiagram.this.graph.addSelectionCell(edge);
               }
            }

            old_rect = GraphConstants.getBounds(view.getAttributes());
            ClassDiagram.this.graph.paintImmediately(old_rect);
            d = view.getPreferredSize();
            if (c.isFiltered()) {
               return;
            }

            if (d != null && (old_rect.width < d.width || old_rect.height < d.height || view.isSignaled())) {
               map = GraphConstants.createMap();
               GraphConstants.setBounds(map, new Rectangle(old_rect.x, old_rect.y, d.width, d.height));
               view.setAttributes(map);
               if (view.isSignaled()) {
                  view.signalViewChange();
               }
            }
         }

         if (e.getCell() instanceof PackageCell) {
            PackageCell cx = (PackageCell)e.getCell();
            PackageView viewx = (PackageView)ClassDiagram.this.graph.getView().getMapping(cx, false);
            if (viewx == null) {
               return;
            }

            old_rect = GraphConstants.getBounds(viewx.getAttributes());
            ClassDiagram.this.graph.paintImmediately(old_rect);
            d = viewx.getPreferredSize();
            if (old_rect.width < d.width || old_rect.height < d.height) {
               map = GraphConstants.createMap();
               GraphConstants.setBounds(map, new Rectangle(old_rect.x, old_rect.y, d.width, d.height));
               viewx.setAttributes(map);
            }
         }

      }
   }
}
