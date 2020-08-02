package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import com.jgraph.graph.DefaultGraphModel;
import java.util.ArrayList;
import java.util.Enumeration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;

public class SnapshotDiagramValidator implements DiagramValidator {
   private ObjectDiagram diagram;
   private ArrayList objects;
   private ArrayList object_views;
   private ArrayList relation_views;
   private JGraph g;
   private DefaultGraphModel model;

   public SnapshotDiagramValidator() {
   }

   private void initMembers(Diagram diag) {
      if (diag instanceof ObjectDiagram) {
         this.diagram = (ObjectDiagram)diag;
         this.objects = this.diagram.getObjects();
         this.object_views = this.diagram.getObjectViews();
         this.relation_views = this.diagram.getRelationViews();
         this.g = this.diagram.getGraph();
         this.model = this.diagram.getGraphModel();
      }
   }

   public void validate(Object obj, Diagram diag) {
      if (obj instanceof Instance) {
         this.initMembers(diag);
         Instance inst = (Instance)obj;
         this.checkLinks(inst);
      }

   }

   private void checkLinks(Instance inst) {
      Enumeration linksEnds = inst.getLinkEndList();

      while(linksEnds.hasMoreElements()) {
         LinkEnd le = (LinkEnd)linksEnds.nextElement();
         Link lnk = le.getLink();
         Enumeration con = lnk.getConnectionList();

         while(con.hasMoreElements()) {
            LinkEnd end = (LinkEnd)con.nextElement();
            if (end != le) {
               Instance newInst = end.getInstance();
               if (this.objects.contains(newInst)) {
                  this.diagram.addRelation(inst, newInst, lnk);
               }
            }
         }
      }

   }
}
