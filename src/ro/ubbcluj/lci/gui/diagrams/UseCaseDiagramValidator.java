package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Edge;
import com.jgraph.graph.ParentMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Actor;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public class UseCaseDiagramValidator implements DiagramValidator {
   private UseCaseDiagram diagram;
   private ArrayList actors;
   private ArrayList actorViews;
   private ArrayList useCases;
   private ArrayList useCaseViews;
   private ArrayList relationViews;
   private DefaultGraphModel model;

   public UseCaseDiagramValidator() {
   }

   private void initMembers(Diagram diag) {
      if (diag instanceof UseCaseDiagram) {
         this.diagram = (UseCaseDiagram)diag;
         this.actors = this.diagram.getActors();
         this.actorViews = this.diagram.getActorViews();
         this.useCases = this.diagram.getUseCases();
         this.useCaseViews = this.diagram.getUseCaseViews();
         this.relationViews = this.diagram.getRelationViews();
         this.model = this.diagram.getGraphModel();
      }
   }

   public void validate(Object obj, Diagram diag) {
      if (obj instanceof Classifier) {
         this.initMembers(diag);
         Classifier cls = (Classifier)obj;
         this.checkAssociations(cls);
      }
   }

   private void checkAssociations(Classifier cls) {
      Enumeration asoc_ends = cls.getAssociationList();
      ArrayList views;
      ArrayList opViews;
      if (cls instanceof Actor) {
         views = this.actorViews;
         opViews = this.useCaseViews;
      } else {
         views = this.useCaseViews;
         opViews = this.actorViews;
      }

      new ArrayList();
      ArrayList elems;
      if (cls instanceof Actor) {
         elems = this.useCases;
      } else {
         elems = this.actors;
      }

      ConnectionSet cs = new ConnectionSet();
      DefaultPort current_port = (DefaultPort)this.model.getChild((DefaultGraphCell)views.get(views.size() - 1), 0);

      while(true) {
         while(asoc_ends.hasMoreElements()) {
            AssociationEnd ae = (AssociationEnd)asoc_ends.nextElement();
            Association asoc = ae.getAssociation();
            Enumeration en = asoc.getConnectionList();
            String p_name = null;

            while(en.hasMoreElements()) {
               AssociationEnd end = (AssociationEnd)en.nextElement();
               if (end != ae) {
                  p_name = end.getParticipant().getName();
               }
            }

            if (p_name == null) {
               System.err.println("Bad Classifier for AssociationEnd");
            } else {
               for(int i = 0; i < elems.size(); ++i) {
                  if (((Classifier)elems.get(i)).getName().equals(p_name)) {
                     Edge edge = this.diagram.edgeGenerator(0, 0, asoc);
                     DefaultPort foreign_port = (DefaultPort)this.model.getChild((DefaultGraphCell)opViews.get(i), 0);
                     cs.connect(edge, current_port, true);
                     cs.connect(edge, foreign_port, false);
                     Object[] insert = new Object[]{edge};
                     this.model.insert(insert, cs, (ParentMap)null, (Map)null);
                     this.relationViews.add(edge);
                  }
               }
            }
         }

         return;
      }
   }
}
