package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Edge;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.ParentMap;
import com.jgraph.graph.Port;
import com.jgraph.graph.PortView;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.uml.foundation.core.Abstraction;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Dependency;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public class ClassDiagramValidator implements DiagramValidator {
   private ClassDiagram diagram;
   private ArrayList classes;
   private ArrayList class_views;
   private ArrayList packages;
   private ArrayList package_views;
   private ArrayList relation_views;
   private JGraph g;
   private DefaultGraphModel model;

   public ClassDiagramValidator() {
   }

   private void initMembers(Diagram diag) {
      if (diag instanceof ClassDiagram) {
         this.diagram = (ClassDiagram)diag;
         this.classes = this.diagram.getClasses();
         this.class_views = this.diagram.getClassViews();
         this.packages = this.diagram.getPackages();
         this.package_views = this.diagram.getPackageViews();
         this.relation_views = this.diagram.getRelationViews();
         this.g = this.diagram.getGraph();
         this.model = this.diagram.getGraphModel();
      }
   }

   public void validate(Object obj, Diagram diag) {
      if (obj instanceof Package) {
         this.initMembers(diag);
         Package pck = (Package)obj;
         this.checkPackageInheritances(pck);
         this.checkPackageSpecializations(pck);
         this.checkPackageDependencies(pck);
      } else if (obj instanceof Classifier) {
         this.initMembers(diag);
         Classifier cls = (Classifier)obj;
         this.checkClassAssociations(cls);
         this.checkClassInheritances(cls);
         this.checkClassSpecializations(cls);
         this.checkClassDependencies(cls);
         this.checkForAssociationsClasses(cls);
      }
   }

   private void checkAssociationClass(AssociationClass acls) {
      Enumeration en = acls.getConnectionList();
      ArrayList parts = new ArrayList();

      while(en.hasMoreElements()) {
         AssociationEnd ae = (AssociationEnd)en.nextElement();
         parts.add(ae.getParticipant());
      }

      if (parts.size() != 2) {
         System.err.println("AssociationClass with more/less than 2 participants !");
         parts = null;
      } else if (this.diagram.contains(parts.get(0)) && this.diagram.contains(parts.get(1))) {
         ClassCell p1 = (ClassCell)this.class_views.get(this.classes.indexOf(parts.get(0)));
         SpecialPort port = (SpecialPort)p1.getChildAt(0);
         ArrayList edges = new ArrayList();
         Iterator it = port.edges();

         while(it.hasNext()) {
            edges.add(it.next());
         }

         for(int i = 0; i < edges.size(); ++i) {
            SpecialEdge edge = (SpecialEdge)edges.get(i);
            if (edge.getUserObject() == acls) {
               SpecialEdgeView eview = (SpecialEdgeView)this.g.getView().getMapping(edge, false);
               PortView p1view = (PortView)this.g.getView().getMapping(edge.getSource(), false);
               PortView p2view = (PortView)this.g.getView().getMapping(edge.getTarget(), false);
               Point p1Loc = p1view.getLocation(eview);
               Point p2Loc = p2view.getLocation(eview);
               Point middle = new Point((p1Loc.x + p2Loc.x) / 2, (p1Loc.y + p2Loc.y) / 2);
               Point location = new Point(middle.x, middle.y + 100);
               if (this.classes.contains(acls)) {
                  this.diagram.removeItem(acls);
               }

               this.diagram.addClass(acls, location, false);
               DefaultGraphCell cell = (DefaultGraphCell)this.class_views.get(this.classes.indexOf(acls));
               ConnectionSet cs = new ConnectionSet();
               Edge newEdge = this.diagram.edgeGenerator(6, 6, acls);
               cs.connect(newEdge, cell.getChildAt(0), true);
               cs.connect(newEdge, edge.getChildAt(0), false);
               Object[] insert = new Object[]{newEdge};
               this.model.insert(insert, cs, (ParentMap)null, (Map)null);
               this.relation_views.add(edge);
            }
         }

      } else {
         parts = null;
      }
   }

   private void checkClassDependencies(Classifier cls) {
      Enumeration c_dep = cls.getClientDependencyList();
      Enumeration s_dep = cls.getSupplierDependencyList();
      ConnectionSet cs = new ConnectionSet();
      DefaultPort current_port = (DefaultPort)this.model.getChild((ClassCell)this.class_views.get(this.class_views.size() - 1), 0);
      DefaultGraphCell cell = null;

      Dependency dep;
      Enumeration en;
      ModelElement me;
      Edge edge;
      DefaultPort foreign_port;
      Object[] insert;
      while(c_dep.hasMoreElements()) {
         dep = (Dependency)c_dep.nextElement();
         en = dep.getSupplierList();

         while(en.hasMoreElements()) {
            me = (ModelElement)en.nextElement();
            if (this.diagram.contains(me)) {
               if (this.packages.contains(me)) {
                  cell = (DefaultGraphCell)this.package_views.get(this.packages.indexOf(me));
               } else if (this.classes.contains(me)) {
                  cell = (DefaultGraphCell)this.class_views.get(this.classes.indexOf(me));
               }

               edge = null;
               if (dep instanceof Abstraction) {
                  edge = this.diagram.edgeGenerator(0, 5, dep);
               } else {
                  edge = this.diagram.edgeGenerator(0, 3, dep);
               }

               foreign_port = (DefaultPort)this.model.getChild(cell, 0);
               cs.connect(edge, current_port, true);
               cs.connect(edge, foreign_port, false);
               insert = new Object[]{edge};
               this.model.insert(insert, cs, (ParentMap)null, (Map)null);
               this.relation_views.add(edge);
               this.checkMultipleRelations(current_port, foreign_port, edge);
            }
         }
      }

      while(s_dep.hasMoreElements()) {
         dep = (Dependency)s_dep.nextElement();
         en = dep.getClientList();

         while(en.hasMoreElements()) {
            me = (ModelElement)en.nextElement();
            if (this.diagram.contains(me)) {
               if (this.packages.contains(me)) {
                  cell = (DefaultGraphCell)this.package_views.get(this.packages.indexOf(me));
               } else if (this.classes.contains(me)) {
                  cell = (DefaultGraphCell)this.class_views.get(this.classes.indexOf(me));
               }

               edge = this.diagram.edgeGenerator(3, 0, dep);
               foreign_port = (DefaultPort)this.model.getChild(cell, 0);
               cs.connect(edge, current_port, true);
               cs.connect(edge, foreign_port, false);
               insert = new Object[]{edge};
               this.model.insert(insert, cs, (ParentMap)null, (Map)null);
               this.relation_views.add(edge);
               this.checkMultipleRelations(current_port, foreign_port, edge);
            }
         }
      }

   }

   private void checkForAssociationsClasses(Classifier cls) {
      Enumeration asoc_ends = cls.getAssociationList();

      while(asoc_ends.hasMoreElements()) {
         AssociationEnd ae = (AssociationEnd)asoc_ends.nextElement();
         Association asoc = ae.getAssociation();
         if (asoc instanceof AssociationClass) {
            this.checkAssociationClass((AssociationClass)asoc);
         }
      }

   }

   private void checkClassAssociations(Classifier cls) {
      int left_end = 0;
      int right_end = 0;
      boolean hadAAsoc = false;
      Enumeration asoc_ends = cls.getAssociationList();
      ConnectionSet cs = new ConnectionSet();
      DefaultPort current_port = (DefaultPort)this.model.getChild((ClassCell)this.class_views.get(this.class_views.size() - 1), 0);

      while(true) {
         while(asoc_ends.hasMoreElements()) {
            AssociationEnd ae = (AssociationEnd)asoc_ends.nextElement();
            Association asoc = ae.getAssociation();
            Enumeration en = asoc.getConnectionList();
            String p_name = null;
            Classifier participant = null;

            while(en.hasMoreElements()) {
               AssociationEnd end = (AssociationEnd)en.nextElement();
               if (end != ae) {
                  if (end.isNavigable() && !ae.isNavigable()) {
                     right_end = 2;
                  }

                  if (ae.isNavigable() && !end.isNavigable()) {
                     left_end = 2;
                  }

                  p_name = end.getParticipant().getName();
                  participant = end.getParticipant();
                  if (participant == cls && !hadAAsoc) {
                     ClassCell cell = (ClassCell)this.class_views.get(this.classes.indexOf(cls));
                     cell.setAutoRelation(true);
                     this.diagram.drawExistingAutoAssociation(cell, asoc, left_end, right_end);
                     hadAAsoc = true;
                  }
               }
            }

            if (p_name == null) {
               GMainFrame.getMainFrame().updateMessages((Object)"Bad Classifier for AssociationEnd");
            } else {
               for(int i = 0; i < this.classes.size() - 1; ++i) {
                  if (((Classifier)this.classes.get(i)).getName().equals(p_name)) {
                     Edge edge = this.diagram.edgeGenerator(left_end, right_end, asoc);
                     DefaultPort foreign_port = (DefaultPort)this.model.getChild((ClassCell)this.class_views.get(i), 0);
                     cs.connect(edge, current_port, true);
                     cs.connect(edge, foreign_port, false);
                     Object[] insert = new Object[]{edge};
                     this.model.insert(insert, cs, (ParentMap)null, (Map)null);
                     this.relation_views.add(edge);
                     this.g.setSelectionCell(edge);
                     SpecialEdge specialEdge = (SpecialEdge)DiagramUtilities.updateGraphicalRelation(asoc, this.relation_views, (DiagramGraph)this.g);
                     if (specialEdge != null) {
                        this.diagram.addLabelsForAsociation(specialEdge);
                     }

                     this.g.removeSelectionCell(edge);
                     this.checkMultipleRelations(current_port, foreign_port, edge);
                  }
               }
            }
         }

         return;
      }
   }

   private void checkClassInheritances(Classifier cls) {
      Enumeration gen = cls.getGeneralizationList();
      ConnectionSet cs = new ConnectionSet();
      DefaultPort current_port = (DefaultPort)this.model.getChild((ClassCell)this.class_views.get(this.class_views.size() - 1), 0);

      while(true) {
         Generalization g;
         do {
            if (!gen.hasMoreElements()) {
               return;
            }

            g = (Generalization)gen.nextElement();
         } while(cls != g.getChild());

         for(int i = 0; i < this.classes.size() - 1; ++i) {
            if ((Classifier)this.classes.get(i) == g.getParent()) {
               Edge edge = this.diagram.edgeGenerator(0, 1, g);
               DefaultPort foreign_port = (DefaultPort)this.model.getChild((ClassCell)this.class_views.get(i), 0);
               cs.connect(edge, current_port, true);
               cs.connect(edge, foreign_port, false);
               Object[] insert = new Object[]{edge};
               this.model.insert(insert, cs, (ParentMap)null, (Map)null);
               this.relation_views.add(edge);
               this.checkMultipleRelations(current_port, foreign_port, edge);
            }
         }
      }
   }

   private void checkClassSpecializations(Classifier cls) {
      Enumeration gen = cls.getSpecializationList();
      ConnectionSet cs = new ConnectionSet();
      DefaultPort current_port = (DefaultPort)this.model.getChild((ClassCell)this.class_views.get(this.class_views.size() - 1), 0);

      while(true) {
         Generalization g;
         do {
            if (!gen.hasMoreElements()) {
               return;
            }

            g = (Generalization)gen.nextElement();
         } while(cls != g.getParent());

         for(int i = 0; i < this.classes.size() - 1; ++i) {
            if ((Classifier)this.classes.get(i) == g.getChild()) {
               Edge edge = this.diagram.edgeGenerator(1, 0, g);
               DefaultPort foreign_port = (DefaultPort)this.model.getChild((ClassCell)this.class_views.get(i), 0);
               cs.connect(edge, current_port, true);
               cs.connect(edge, foreign_port, false);
               Object[] insert = new Object[]{edge};
               this.model.insert(insert, cs, (ParentMap)null, (Map)null);
               this.relation_views.add(edge);
               this.checkMultipleRelations(current_port, foreign_port, edge);
            }
         }
      }
   }

   private void checkPackageDependencies(Package pck) {
      Enumeration c_dep = pck.getClientDependencyList();
      Enumeration s_dep = pck.getSupplierDependencyList();
      ConnectionSet cs = new ConnectionSet();
      DefaultPort current_port = (DefaultPort)this.model.getChild((PackageCell)this.package_views.get(this.package_views.size() - 1), 0);
      DefaultGraphCell cell = null;

      Dependency dep;
      Enumeration en;
      ModelElement me;
      Edge edge;
      DefaultPort foreign_port;
      Object[] insert;
      while(c_dep.hasMoreElements()) {
         dep = (Dependency)c_dep.nextElement();
         en = dep.getSupplierList();

         while(en.hasMoreElements()) {
            me = (ModelElement)en.nextElement();
            if (this.diagram.contains(me)) {
               if (this.packages.contains(me)) {
                  cell = (DefaultGraphCell)this.package_views.get(this.packages.indexOf(me));
               } else if (this.classes.contains(me)) {
                  cell = (DefaultGraphCell)this.class_views.get(this.classes.indexOf(me));
               }

               edge = this.diagram.edgeGenerator(0, 3, dep);
               foreign_port = (DefaultPort)this.model.getChild(cell, 0);
               cs.connect(edge, current_port, true);
               cs.connect(edge, foreign_port, false);
               insert = new Object[]{edge};
               this.model.insert(insert, cs, (ParentMap)null, (Map)null);
               this.relation_views.add(edge);
               this.checkMultipleRelations(current_port, foreign_port, edge);
            }
         }
      }

      while(s_dep.hasMoreElements()) {
         dep = (Dependency)s_dep.nextElement();
         en = dep.getClientList();

         while(en.hasMoreElements()) {
            me = (ModelElement)en.nextElement();
            if (this.diagram.contains(me)) {
               if (this.packages.contains(me)) {
                  cell = (DefaultGraphCell)this.package_views.get(this.packages.indexOf(me));
               } else if (this.classes.contains(me)) {
                  cell = (DefaultGraphCell)this.class_views.get(this.classes.indexOf(me));
               }

               edge = this.diagram.edgeGenerator(3, 0, dep);
               foreign_port = (DefaultPort)this.model.getChild(cell, 0);
               cs.connect(edge, current_port, true);
               cs.connect(edge, foreign_port, false);
               insert = new Object[]{edge};
               this.model.insert(insert, cs, (ParentMap)null, (Map)null);
               this.relation_views.add(edge);
               this.checkMultipleRelations(current_port, foreign_port, edge);
            }
         }
      }

   }

   private void checkPackageInheritances(Package pck) {
      Enumeration gen = pck.getGeneralizationList();
      ConnectionSet cs = new ConnectionSet();
      DefaultPort current_port = (DefaultPort)this.model.getChild((PackageCell)this.package_views.get(this.package_views.size() - 1), 0);

      while(true) {
         Generalization g;
         do {
            if (!gen.hasMoreElements()) {
               return;
            }

            g = (Generalization)gen.nextElement();
         } while(pck != g.getChild());

         for(int i = 0; i < this.packages.size() - 1; ++i) {
            if ((Package)this.packages.get(i) == g.getParent()) {
               Edge edge = this.diagram.edgeGenerator(0, 1, g);
               DefaultPort foreign_port = (DefaultPort)this.model.getChild((PackageCell)this.package_views.get(i), 0);
               cs.connect(edge, current_port, true);
               cs.connect(edge, foreign_port, false);
               Object[] insert = new Object[]{edge};
               this.model.insert(insert, cs, (ParentMap)null, (Map)null);
               this.relation_views.add(edge);
               this.checkMultipleRelations(current_port, foreign_port, edge);
            }
         }
      }
   }

   private void checkPackageSpecializations(Package pck) {
      Enumeration gen = pck.getSpecializationList();
      ConnectionSet cs = new ConnectionSet();
      DefaultPort current_port = (DefaultPort)this.model.getChild((PackageCell)this.package_views.get(this.package_views.size() - 1), 0);

      while(true) {
         Generalization g;
         do {
            if (!gen.hasMoreElements()) {
               return;
            }

            g = (Generalization)gen.nextElement();
         } while(pck != g.getParent());

         for(int i = 0; i < this.packages.size() - 1; ++i) {
            if ((Package)this.packages.get(i) == g.getChild()) {
               Edge edge = this.diagram.edgeGenerator(1, 0, g);
               DefaultPort foreign_port = (DefaultPort)this.model.getChild((PackageCell)this.package_views.get(i), 0);
               cs.connect(edge, current_port, true);
               cs.connect(edge, foreign_port, false);
               Object[] insert = new Object[]{edge};
               this.model.insert(insert, cs, (ParentMap)null, (Map)null);
               this.relation_views.add(edge);
               this.checkMultipleRelations(current_port, foreign_port, edge);
            }
         }
      }
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
            PortView from_view = (PortView)this.g.getView().getMapping(from_port, false);
            PortView to_view = (PortView)this.g.getView().getMapping(to_port, false);
            EdgeView edge_view = (EdgeView)this.g.getView().getMapping(edge, false);
            Point from_loc = from_view.getLocation(edge_view);
            Point to_loc = to_view.getLocation(edge_view);
            Point newP = new Point((from_loc.x + to_loc.x) / 2, (from_loc.y + to_loc.y) / 2);
            edge_view.addPoint(1, newP);
         }
      }
   }
}
