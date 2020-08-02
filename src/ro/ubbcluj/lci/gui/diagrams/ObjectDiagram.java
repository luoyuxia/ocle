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
import ro.ubbcluj.lci.gui.tools.JDialogChooser;
import ro.ubbcluj.lci.gxapi.GXConnectionSet;
import ro.ubbcluj.lci.gxapi.GXDefaultEdge;
import ro.ubbcluj.lci.gxapi.GXDefaultGraphCell;
import ro.ubbcluj.lci.gxapi.GXDefaultMutableTreeNode;
import ro.ubbcluj.lci.gxapi.GXDefaultPort;
import ro.ubbcluj.lci.gxapi.GXHashtable;
import ro.ubbcluj.lci.gxapi.GXObjectCell;
import ro.ubbcluj.lci.gxapi.GXObjectDiagram;
import ro.ubbcluj.lci.gxapi.GXObjectGProperty;
import ro.ubbcluj.lci.gxapi.GXSpecialEdge;
import ro.ubbcluj.lci.gxapi.GXSpecialPort;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkObject;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.utils.CreateObjectDialog;
import ro.ubbcluj.lci.utils.EditQualifiersDialog;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.ModelUtilities;

public class ObjectDiagram extends GAbstractDiagram {
   private DefaultGraphModel model = new DefaultGraphModel();
   private DiagramGraph g;
   private ArrayList objects;
   private ArrayList object_views;
   private ArrayList relation_views;
   private ArrayList selected;
   private int ACTION_KIND;
   private GraphUndoManager undo_manager;
   private DiagramListener listener;
   private static DiagramValidator validator = new SnapshotDiagramValidator();

   public ObjectDiagram(GUMLModel gmodel, ModelElement context) {
      this.g = new DiagramGraph(this.model);
      this.objects = new ArrayList();
      this.object_views = new ArrayList();
      this.relation_views = new ArrayList();
      this.selected = new ArrayList();
      this.undo_manager = new GraphUndoManager();
      this.setUserObject(gmodel);
      this.setContext(context);
      this.initGraph();
      this.addDiagramListener(this.listener);
   }

   private void initGraph() {
      this.g.setGridEnabled(false);
      this.g.setGridColor(Color.black);
      this.g.setBackground(Color.white);
      this.g.setDisconnectable(false);
      this.g.setEditable(false);
      this.g.setCloneable(false);
      this.g.setSelectNewCells(true);
      this.g.setHandleColor(Color.blue);
      this.g.setScale(0.9D);
      this.g.addGraphSelectionListener(new ObjectDiagram.DiagramSelectionListener());
      this.g.getModel().addUndoableEditListener(this.undo_manager);
      this.g.getModel().addGraphModelListener(new ObjectDiagram.DiagramModelListener());
      this.listener = new DiagramListener(this);
      this.diagram = new JPanel(new BorderLayout());
      this.diagram.add(new JScrollPane(this.g), "Center");
   }

   ArrayList getObjects() {
      return this.objects;
   }

   ArrayList getObjectViews() {
      return this.object_views;
   }

   ArrayList getRelationViews() {
      return this.relation_views;
   }

   DefaultGraphModel getGraphModel() {
      return this.model;
   }

   public JGraph getGraph() {
      return this.g;
   }

   public Color getBackgroundColor() {
      return this.g.getBackground();
   }

   public void setBackgroundColor(Color newColor) {
      this.g.setBackground(newColor);
   }

   public void setGridVisible(boolean state) {
      this.g.setGridVisible(state);
   }

   public boolean getGridVisible() {
      return this.g.isGridVisible();
   }

   public ArrayList getObjectCells() {
      return this.object_views;
   }

   public ArrayList getSelected() {
      return this.selected;
   }

   public void setSelected(Object o) {
      if (o instanceof ModelElement) {
         DefaultGraphCell view = null;
         int i = this.objects.indexOf(o);
         if (i >= 0) {
            view = (DefaultGraphCell)this.object_views.get(i);
         }

         if (view == null) {
            for(int j = 0; j < this.relation_views.size(); ++j) {
               Object obj = ((SpecialEdge)this.relation_views.get(j)).getUserObject();
               if (obj == o) {
                  view = (SpecialEdge)this.relation_views.get(j);
                  break;
               }
            }
         }

         if (view != null) {
            this.g.setSelectionCell(view);
            this.g.scrollCellToVisible(view);
         }
      }

      if (o instanceof DefaultGraphCell) {
         this.g.setSelectionCell(o);
         this.g.scrollCellToVisible(o);
      }

      if (o == null) {
         this.g.clearSelection();
      }

   }

   public void setActionKind(int kind) {
      this.ACTION_KIND = kind;
   }

   public int getActionKind() {
      return this.ACTION_KIND;
   }

   public void addElement(Object o, Point location, boolean isNew) {
      this.zoomDefault();
      if (o instanceof Instance) {
         this.addInstance(o, location, isNew);
      }

   }

   public void zoomDefault() {
      this.g.setScale(0.9D);
   }

   public void zoomIn() {
      this.g.setScale(1.5D * this.g.getScale());
   }

   public void zoomOut() {
      this.g.setScale(this.g.getScale() / 1.5D);
   }

   public void undo() {
      try {
         if (this.undo_manager.canUndo()) {
            this.undo_manager.undo(this.g.getView());
         }
      } catch (Exception var2) {
      }

   }

   public void redo() {
      try {
         if (this.undo_manager.canRedo()) {
            this.undo_manager.redo(this.g.getView());
         }
      } catch (Exception var2) {
      }

   }

   public void addRelation(Object from, Object to) {
      Object tmpSupplier = null;
      Object tmpClient = null;
      Instance supplier = null;
      Instance client = null;
      if (from instanceof DefaultGraphCell && to instanceof DefaultGraphCell) {
         tmpSupplier = ((DefaultGraphCell)from).getUserObject();
         tmpClient = ((DefaultGraphCell)to).getUserObject();
      } else {
         tmpSupplier = from;
         tmpClient = to;
      }

      if (tmpClient instanceof Instance && tmpSupplier instanceof Instance) {
         client = (Instance)tmpClient;
         supplier = (Instance)tmpSupplier;
         if (client.getClassifierList().hasMoreElements() && supplier.getClassifierList().hasMoreElements()) {
            Classifier clientClass = (Classifier)client.getClassifierList().nextElement();
            Classifier supplierClass = (Classifier)supplier.getClassifierList().nextElement();
            Map assocs = ModelUtilities.findAssociationsBetween(supplierClass, clientClass);
            Association as = null;
            String asKey = null;
            if (assocs.isEmpty()) {
               GMainFrame.getMainFrame().updateMessages((Object)"There is no association between the involved classifiers.");
            } else {
               if (assocs.size() > 1) {
                  JDialogChooser dialog = new JDialogChooser("Select association");
                  dialog.setListContent(assocs.keySet().toArray());
                  dialog.setSelectionMode(0);
                  dialog.setInfo("Select the appropriate association for the newly created link");
                  dialog.setParticipants(client.getName() + ":" + clientClass.getName(), supplier.getName() + ":" + supplierClass.getName());
                  dialog.show();
                  if (dialog.getExitCode() != 1) {
                     return;
                  }

                  asKey = (String)dialog.getSelectedValues()[0];
                  if (asKey == null) {
                     GMainFrame.getMainFrame().updateMessages((Object)"No association was selected");
                     return;
                  }

                  if (asKey.equals("InvalidAutoassociation")) {
                     GMainFrame.getMainFrame().updateMessages((Object)"There is an autoassociation with identical rolenames. Link can not be created without knowing the role of each object.");
                     return;
                  }

                  as = (Association)assocs.get(asKey);
               } else {
                  Object invalidAutoassociation = assocs.get("InvalidAutoassociation");
                  if (invalidAutoassociation != null) {
                     GMainFrame.getMainFrame().updateMessages((Object)"There is an autoassociation with identical rolenames. Link can not be created without knowing the role of each object.");
                     return;
                  }

                  as = (Association)assocs.values().iterator().next();
               }

               Iterator asIt = as.getCollectionConnectionList().iterator();
               AssociationEnd ae1 = (AssociationEnd)asIt.next();
               AssociationEnd ae2 = (AssociationEnd)asIt.next();
               if (ae1.getParticipant() == ae2.getParticipant()) {
                  String aename1 = asKey.substring(asKey.lastIndexOf("{") + 1, asKey.lastIndexOf("-"));
                  if (!ae1.getName().equals(aename1)) {
                     Instance tmpInst = client;
                     client = supplier;
                     supplier = tmpInst;
                  }
               }

               Object result;
               if (as instanceof AssociationClass) {
                  result = ModelFactory.createNewRelation(supplier, client, as, "LinkObject");
               } else {
                  result = ModelFactory.createNewRelation(supplier, client, as, "Link");
               }

               if (result instanceof LinkObject) {
                  CreateObjectDialog.editObject((ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object)result);
               }

               if (result instanceof Link) {
                  Link resultAsLink = (Link)result;
                  Iterator itConn = resultAsLink.getCollectionConnectionList().iterator();

                  while(itConn.hasNext()) {
                     LinkEnd currLinkEnd = (LinkEnd)itConn.next();
                     if (!currLinkEnd.getCollectionQualifierValueList().isEmpty()) {
                        EditQualifiersDialog.editLinkEnd(currLinkEnd);
                     }
                  }

                  this.addRelation(from, to, (Link)result);
               } else {
                  GMainFrame.getMainFrame().updateMessages((Object)"Link not created.");
               }

            }
         } else {
            GMainFrame.getMainFrame().updateMessages((Object)"Instance's classifier not set.");
         }
      } else {
         GMainFrame.getMainFrame().updateMessages((Object)"Links can only connect instances.");
      }
   }

   void addRelation(Object from, Object to, Link lnk) {
      DefaultGraphCell from_cell = null;
      DefaultGraphCell to_cell = null;
      if (from instanceof DefaultGraphCell && to instanceof DefaultGraphCell) {
         from_cell = (DefaultGraphCell)from;
         to_cell = (DefaultGraphCell)to;
         if (!(from_cell.getUserObject() instanceof String) && !(to_cell.getUserObject() instanceof String)) {
            this.addGraphicalRelation(from_cell, to_cell, lnk);
         }
      } else {
         if (from instanceof Instance && to instanceof Instance) {
            int from_index = this.objects.indexOf(from);
            int to_index = this.objects.indexOf(to);
            if (from_index == -1 || to_index == -1) {
               GMainFrame.getMainFrame().updateMessages((Object)"AddRelation:Can't find object instance");
               return;
            }

            from_cell = (DefaultGraphCell)this.object_views.get(from_index);
            to_cell = (DefaultGraphCell)this.object_views.get(to_index);
         }

         this.addGraphicalRelation(from_cell, to_cell, lnk);
      }
   }

   public void updateItem(Object o) {
      if (this.objects.contains(o)) {
         this.signalElementChange(o);
      }

   }

   public void removeItem(Object o) {
      this.removeGraphicalItem(o);
      int i;
      if (o instanceof Instance) {
         i = this.objects.indexOf(o);
         if (i >= 0) {
            this.objects.remove(i);
            this.object_views.remove(i);
         }
      }

      if (o instanceof Link) {
         Iterator it = this.relation_views.iterator();

         while(it.hasNext()) {
            DefaultGraphCell dgc = (DefaultGraphCell)it.next();
            if (o == dgc.getUserObject()) {
               it.remove();
               this.g.getModel().remove(new Object[]{dgc});
               break;
            }
         }
      }

      if (o instanceof ObjectCell) {
         i = this.object_views.indexOf(o);
         if (i >= 0) {
            this.objects.remove(i);
            this.object_views.remove(i);
         }
      }

      if (o instanceof Edge) {
         for(i = this.relation_views.indexOf(o); i != -1; i = this.relation_views.indexOf(o)) {
            this.relation_views.remove(i);
         }
      }

   }

   public boolean contains(Object o) {
      if (o instanceof Edge) {
         return this.relation_views.contains(o);
      } else if (o instanceof Link) {
         for(int i = 0; i < this.relation_views.size(); ++i) {
            if (((DefaultGraphCell)this.relation_views.get(i)).getUserObject() == o) {
               return true;
            }
         }

         return false;
      } else {
         return this.objects.contains(o);
      }
   }

   public void lockItem(Object o, boolean state) {
      this.setDragable(o, !state);
   }

   public void lockDiagram(boolean state) {
      this.isLocked = !state;

      for(int i = 0; i < this.object_views.size(); ++i) {
         DefaultGraphCell cell = (DefaultGraphCell)this.object_views.get(i);
         Map attrs = cell.getAttributes();
         GraphConstants.setMoveable(attrs, state);
         cell.setAttributes(attrs);
      }

   }

   public void addDiagramListener(DiagramListener dl) {
      this.g.addMouseListener(dl);
      this.g.addMouseMotionListener(dl);
      this.g.addKeyListener(dl);
   }

   protected boolean exists(Instance inst) {
      return this.objects.contains(inst);
   }

   protected void addInstance(Object obj, Point loc, boolean isNew) {
      Instance inst = (Instance)obj;
      if (this.exists(inst)) {
         GMainFrame.getMainFrame().updateMessages((Object)"Object already added to this diagram.");
      } else {
         this.objects.add(inst);
         ObjectCell ogc = new ObjectCell(inst);
         ogc.setLocation(loc);
         this.object_views.add(ogc);
         DefaultPort port = new DefaultPort();
         ogc.add(port);
         GProperty prop = GProperty.createProperty(ogc);
         prop.getPopup().addMouseListener(this.listener);
         ogc.setProperty(prop);
         Map map = GraphConstants.createMap();
         Map attributeMap = new Hashtable();
         Point pt = this.g.snap(new Point(loc));
         GraphConstants.setBounds(map, new Rectangle(pt, DiagramUtilities.getDefaultSize(ogc)));
         GraphConstants.setOpaque(map, true);
         attributeMap.put(ogc, map);
         Object[] insert = new Object[]{ogc};
         this.model.insert(insert, (ConnectionSet)null, (ParentMap)null, attributeMap);
         if (!isNew) {
            validator.validate(inst, this);
         }

      }
   }

   protected void removeGraphicalItem(Object o) {
      DefaultGraphCell cell = null;

      if (o instanceof DefaultGraphCell) {
         cell = (DefaultGraphCell)o;
      }

      if (o instanceof Instance) {
         int index = this.objects.indexOf(o);
         if (index == -1) {
            return;
         }

         cell = (DefaultGraphCell)this.object_views.get(index);
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

            for(int idx = this.relation_views.indexOf(e); idx != -1; idx = this.relation_views.indexOf(e)) {
               this.relation_views.remove(idx);
            }

            edges.remove(0);
            Object[] redges = new Object[]{e};
            this.g.getModel().remove(redges);
         }

         edges = null;
         Object[] self = new Object[]{cell};
         this.g.getModel().remove(this.g.getDescendants(self));
      }
   }

   protected void addGraphicalRelation(Object cell1, Object cell2, Link lnk) {
      DefaultGraphCell from_cell = (DefaultGraphCell)cell1;
      DefaultGraphCell to_cell = (DefaultGraphCell)cell2;
      ConnectionSet cs = new ConnectionSet();
      this.model.getChildCount(from_cell);
      this.model.getChildCount(to_cell);
      DefaultPort from_port = (DefaultPort)this.model.getChild(from_cell, 0);
      DefaultPort to_port = null;
      if (from_cell == to_cell) {
         to_port = (DefaultPort)this.model.getChild(to_cell, 1);
      } else {
         to_port = (DefaultPort)this.model.getChild(to_cell, 0);
      }

      Map map = GraphConstants.createMap();
      GraphConstants.setLineWidth(map, 1.0F);
      GraphConstants.setEditable(map, false);
      GraphConstants.setLineBegin(map, 0);
      GraphConstants.setLineEnd(map, 0);
      Edge edge = new SpecialEdge(lnk);
      edge.setAttributes(map);
      cs.connect(edge, from_port, true);
      cs.connect(edge, to_port, false);
      Object[] insert = new Object[]{edge};
      this.model.insert(insert, cs, (ParentMap)null, (Map)null);
      this.relation_views.add(edge);
   }

   protected void setDragable(Object o, boolean state) {
      if (o instanceof DefaultGraphCell) {
         DefaultGraphCell cell = (DefaultGraphCell)o;
         Map map = GraphConstants.createMap();
         GraphConstants.setMoveable(map, state);
         cell.setAttributes(map);
         this.g.setSelectionCell(cell);
      }

   }

   void deselectCells() {
      Object[] selected = this.g.getSelectionCells();

      for(int i = 0; i < selected.length; ++i) {
         this.g.removeSelectionCell(selected[i]);
      }

   }

   protected void signalElementChange(Object o) {
      if (o instanceof Instance) {
         int pos = this.objects.indexOf(o);
         if (pos == -1) {
            return;
         }

         ObjectCell cell = (ObjectCell)this.object_views.get(pos);
         ObjectView cell_view = (ObjectView)this.g.getView().getMapping(cell, false);
         cell_view.signalViewChange();
         this.g.setSelectionCell(cell);
      }

      this.g.repaint();
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
      return "Object";
   }

   private ArrayList filter() {
      ArrayList result = new ArrayList();

      for(int i = 0; i < this.selected.size(); ++i) {
         Object o = this.selected.get(i);
         if (o instanceof ObjectCell) {
            result.add(o);
         }
      }

      return result;
   }

   private int getTopLeftMin(ArrayList cells, char coord) {
      int min = 2147483647;

      for(int i = 0; i < cells.size(); ++i) {
         DefaultGraphCell cell = (DefaultGraphCell)cells.get(i);
         CellView cview = this.g.getView().getMapping(cell, false);
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
            CellView cview = this.g.getView().getMapping(cell, false);
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

         this.g.repaint();
         this.deselectCells();
      }
   }

   private int getBottomRightMax(ArrayList cells, char coord) {
      int max = -2147483648;

      for(int i = 0; i < cells.size(); ++i) {
         DefaultGraphCell cell = (DefaultGraphCell)cells.get(i);
         CellView cview = this.g.getView().getMapping(cell, false);
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
            CellView cview = this.g.getView().getMapping(cell, false);
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

         this.g.repaint();
         this.deselectCells();
      }
   }

   public GXObjectDiagram copy() {
      GXObjectDiagram gxod = new GXObjectDiagram();
      gxod.setName(this.getName());
      gxod.setIsLocked(this.isLocked);
      gxod.setOwner(this.getContext());
      if (this.g != null) {
         gxod.setGridVisible(this.g.isGridVisible());
         gxod.setBackground(this.g.getBackground().toString());
         gxod.setObjects(this.objects);
         Object[] cells = this.g.getView().order(this.g.getRoots());
         Object[] flat = DefaultGraphModel.getDescendants(this.g.getModel(), cells).toArray();
         ConnectionSet cs = ConnectionSet.create(this.g.getModel(), flat, false);
         Map viewAttributes = GraphConstants.createPropertyMap(flat, this.g.getView());
         Hashtable clones = new Hashtable();

         int i;
         Object o;
         for(i = 0; i < flat.length; ++i) {
            o = null;
            if (flat[i] instanceof ObjectCell) {
               o = new GXObjectCell();
            } else if (flat[i] instanceof SpecialEdge) {
               o = new GXSpecialEdge();
            } else if (flat[i] instanceof SpecialPort) {
               o = new GXSpecialPort();
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
            gxod.addFlat((GXDefaultGraphCell)clones.get(flat[i]));
         }

         for(i = 0; i < cells.length; ++i) {
            gxod.addCells((GXDefaultGraphCell)clones.get(cells[i]));
         }

         Iterator it = this.object_views.iterator();

         while(it.hasNext()) {
            gxod.addObjectViews(clones.get(it.next()));
         }

         it = this.relation_views.iterator();

         while(it.hasNext()) {
            gxod.addRelationViews(clones.get(it.next()));
         }

         GXConnectionSet csclone = new GXConnectionSet();
         clones.put(cs, csclone);
         csclone.copy(cs, clones);
         gxod.setConnectionSet(csclone);
         GXHashtable gxh = new GXHashtable();
         gxh.copy((Hashtable)viewAttributes, clones);
         gxod.setViewAttributes(gxh);
      }

      return gxod;
   }

   public void extractData(GXObjectDiagram gxod) {
      this.initGraph();
      this.addDiagramListener(this.listener);
      this.setName(gxod.getName());
      this.isLocked = gxod.getIsLocked();
      this.objects = gxod.getCollectionObjects();
      Hashtable clones = new Hashtable();

      Enumeration en;
      Object flatObj;
      Object clone;
      for(en = gxod.getFlatList(); en.hasMoreElements(); clones.put(flatObj, clone)) {
         flatObj = en.nextElement();
         clone = null;
         if (flatObj instanceof GXObjectCell) {
            clone = new ObjectCell();
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

      en = gxod.getFlatList();

      while(en.hasMoreElements()) {
         GXDefaultMutableTreeNode flatObj1 = (GXDefaultMutableTreeNode)en.nextElement();
         DefaultMutableTreeNode clone1 = (DefaultMutableTreeNode)clones.get(flatObj1);
         flatObj1.extractData(clone1, clones);
      }

      Object[] cells = new Object[gxod.getCollectionCells().size()];
      int i = 0;

      Object relationView;
      for(en = gxod.getCellsList(); en.hasMoreElements(); cells[i++] = (DefaultGraphCell)clones.get(relationView)) {
         relationView = en.nextElement();
      }

      en = gxod.getObjectViewsList();

      while(en.hasMoreElements()) {
         relationView = en.nextElement();
         this.object_views.add(clones.get(relationView));
      }

      en = gxod.getRelationViewsList();

      while(en.hasMoreElements()) {
         relationView = en.nextElement();
         this.relation_views.add(clones.get(relationView));
      }

      ConnectionSet cs = new ConnectionSet();
      clones.put(gxod.getConnectionSet(), cs);
      gxod.getConnectionSet().extractData(cs, clones);
      Hashtable attrib = new Hashtable();
      gxod.getViewAttributes().extractData(attrib, clones);
      this.model = new DefaultGraphModel();
      this.g = new DiagramGraph(this.model);
      this.undo_manager = new GraphUndoManager();
      this.selected = new ArrayList();
      this.g.getModel().insert(cells, cs, (ParentMap)null, attrib);
      this.initGraph();
      this.g.setGridVisible(gxod.getGridVisible());
      this.g.setGridEnabled(gxod.getGridVisible());
      this.g.setBackground(GXObjectGProperty.stringToColor(gxod.getBackground()));
      this.g.repaint();
      this.addDiagramListener(this.listener);

      for(i = 0; i < this.objects.size(); ++i) {
         ObjectCell oc = (ObjectCell)this.object_views.get(i);
         oc.getProperty().getPopup().addMouseListener(this.listener);
      }

   }

   public void clear() {
      this.model = null;
      this.g = null;
      this.objects = null;
      this.object_views = null;
      this.relation_views = null;
      this.selected = null;
      this.setUserObject((Object)null);
      this.setContext((ModelElement)null);
   }

   class DiagramModelListener implements GraphModelListener {
      DiagramModelListener() {
      }

      public void graphChanged(GraphModelEvent e) {
         if (ObjectDiagram.this.ACTION_KIND == 301 || ObjectDiagram.this.ACTION_KIND == 302) {
            Object[] inserted = e.getChange().getInserted();
            Object[] removed = e.getChange().getRemoved();
            int i;
            Object usr_obj;
            if (inserted != null) {
               for(i = 0; i < inserted.length; ++i) {
                  if (inserted[i] instanceof ObjectCell) {
                     usr_obj = ((ObjectCell)inserted[i]).getUserObject();
                     if (!ObjectDiagram.this.objects.contains(usr_obj)) {
                        ObjectDiagram.this.objects.add(usr_obj);
                        ObjectDiagram.this.object_views.add(inserted[i]);
                     }
                  }

                  if (inserted[i] instanceof Edge && !ObjectDiagram.this.relation_views.contains(inserted[i])) {
                     ObjectDiagram.this.relation_views.add(inserted[i]);
                  }
               }
            }

            if (removed != null) {
               for(i = 0; i < removed.length; ++i) {
                  if (removed[i] instanceof ObjectCell) {
                     usr_obj = ((ObjectCell)removed[i]).getUserObject();
                     if (ObjectDiagram.this.objects.contains(usr_obj)) {
                        ObjectDiagram.this.objects.remove(usr_obj);
                        ObjectDiagram.this.object_views.remove(removed[i]);
                     }
                  }

                  if (inserted[i] instanceof Edge && ObjectDiagram.this.relation_views.contains(inserted[i])) {
                     ObjectDiagram.this.relation_views.remove(inserted[i]);
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
         ObjectDiagram.this.selected.removeAll(ObjectDiagram.this.selected);

         for(int i = 0; i < ObjectDiagram.this.g.getSelectionCells().length; ++i) {
            ObjectDiagram.this.selected.add(ObjectDiagram.this.g.getSelectionCells()[i]);
         }

         ObjectDiagram.this.g.getView().toFront(ObjectDiagram.this.g.getView().getMapping(ObjectDiagram.this.selected.toArray()));
         if (e.getCell() instanceof ObjectCell) {
            ObjectCell c = (ObjectCell)e.getCell();
            ObjectView view = (ObjectView)ObjectDiagram.this.g.getView().getMapping(c, false);
            if (view == null) {
               return;
            }

            Rectangle old_rect = GraphConstants.getBounds(view.getAttributes());
            ObjectDiagram.this.g.paintImmediately(old_rect);
            Dimension d = view.getPreferredSize();
            if (d != null && (old_rect.width < d.width || old_rect.height < d.height || view.isSignaled())) {
               Map map = GraphConstants.createMap();
               GraphConstants.setBounds(map, new Rectangle(old_rect.x, old_rect.y, d.width, d.height));
               view.setAttributes(map);
               if (view.isSignaled()) {
                  view.signalViewChange();
               }
            }
         }

      }
   }
}
