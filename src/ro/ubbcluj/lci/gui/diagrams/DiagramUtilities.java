package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.utils.ModelUtilities;

public class DiagramUtilities {
   private static final int W_REZ = 1280;
   private static final int H_REZ = 960;

   public DiagramUtilities() {
   }

   public static Dimension getDefaultSize(DefaultGraphCell cell) {
      Dimension screen_rez = Toolkit.getDefaultToolkit().getScreenSize();
      int width;
      int height;
      if (cell instanceof ClassCell) {
         width = screen_rez.width * ClassCell.CELL_WIDTH / 1280;
         height = screen_rez.height * ClassCell.CELL_HEIGHT / 960;
         return new Dimension(width, height);
      } else if (cell instanceof PackageCell) {
         width = screen_rez.width * 150 / 1280;
         height = screen_rez.height * 100 / 960;
         return new Dimension(width, height);
      } else if (cell instanceof ActorCell) {
         width = screen_rez.width * 110 / 1280;
         height = screen_rez.height * 110 / 960;
         return new Dimension(width, height);
      } else if (cell instanceof UseCaseCell) {
         width = screen_rez.width * 120 / 1280;
         height = screen_rez.height * 70 / 960;
         return new Dimension(width, height);
      } else if (cell instanceof ObjectCell) {
         width = screen_rez.width * ObjectCell.CELL_WIDTH / 1280;
         height = screen_rez.height * ObjectCell.CELL_HEIGHT / 960;
         return new Dimension(width, height);
      } else {
         return null;
      }
   }

   private static void paintAggregateEnd(Map newAttrs, Map eAttrs) {
      GraphConstants.setEndFill(newAttrs, false);
      GraphConstants.setLineEnd(newAttrs, 9);
      GraphConstants.setEndFill(eAttrs, false);
      GraphConstants.setLineEnd(eAttrs, 9);
   }

   private static void paintAggregateBegin(Map newAttrs, Map eAttrs) {
      GraphConstants.setBeginFill(newAttrs, false);
      GraphConstants.setLineBegin(newAttrs, 9);
      GraphConstants.setBeginFill(eAttrs, false);
      GraphConstants.setLineBegin(eAttrs, 9);
   }

   private static void paintCompositeEnd(Map newAttrs, Map eAttrs) {
      GraphConstants.setEndFill(newAttrs, true);
      GraphConstants.setLineEnd(newAttrs, 9);
      GraphConstants.setEndFill(eAttrs, true);
      GraphConstants.setLineEnd(eAttrs, 9);
   }

   private static void paintCompositeBegin(Map newAttrs, Map eAttrs) {
      GraphConstants.setBeginFill(newAttrs, true);
      GraphConstants.setLineBegin(newAttrs, 9);
      GraphConstants.setBeginFill(eAttrs, true);
      GraphConstants.setLineBegin(eAttrs, 9);
   }

   private static void paintSimpleBegin(Map newAttrs, Map eAttrs, AssociationEnd assocEnd) {
      boolean oppositeEndNavigability = ModelUtilities.findOppositeEnd(assocEnd).isNavigable();
      int endType = oppositeEndNavigability ? 0 : 4;
      GraphConstants.setLineBegin(newAttrs, endType);
      GraphConstants.setLineBegin(eAttrs, endType);
   }

   private static void paintSimpleEnd(Map newAttrs, Map eAttrs, AssociationEnd assocEnd) {
      boolean oppositeEndNavigability = ModelUtilities.findOppositeEnd(assocEnd).isNavigable();
      int endType = oppositeEndNavigability ? 0 : 4;
      GraphConstants.setLineEnd(newAttrs, endType);
      GraphConstants.setLineEnd(eAttrs, endType);
   }

   public static DefaultEdge updateGraphicalRelation(Association assoc, ArrayList relationViews, DiagramGraph graph) {
      DefaultEdge edge = null;

      for(int i = 0; i < relationViews.size(); ++i) {
         if (((SpecialEdge)relationViews.get(i)).getUserObject() == assoc) {
            edge = (DefaultEdge)relationViews.get(i);
            break;
         }
      }

      if (edge == null) {
         return null;
      } else {
         EdgeView eview = (EdgeView)graph.getView().getMapping(edge, false);
         Map newAttrs = edge.getAttributes();
         Map eAttrs = eview.getAttributes();
         Enumeration connections = assoc.getConnectionList();
         AssociationEnd end1 = (AssociationEnd)connections.nextElement();
         AssociationEnd end2 = (AssociationEnd)connections.nextElement();
         Classifier participant1 = end1.getParticipant();
         Classifier participant2 = end2.getParticipant();
         DefaultGraphCell begin = (DefaultGraphCell)((DefaultPort)edge.getSource()).getParent();
         DefaultGraphCell end = (DefaultGraphCell)((DefaultPort)edge.getTarget()).getParent();
         if (end.getUserObject() == participant1) {
            switch(end1.getAggregation()) {
            case 0:
               paintCompositeEnd(newAttrs, eAttrs);
               break;
            case 1:
               paintSimpleEnd(newAttrs, eAttrs, end1);
               break;
            case 2:
               paintAggregateEnd(newAttrs, eAttrs);
            }
         } else if (begin.getUserObject() == participant1) {
            switch(end1.getAggregation()) {
            case 0:
               paintCompositeBegin(newAttrs, eAttrs);
               break;
            case 1:
               paintSimpleBegin(newAttrs, eAttrs, end1);
               break;
            case 2:
               paintAggregateBegin(newAttrs, eAttrs);
            }
         }

         if (end.getUserObject() == participant2) {
            switch(end2.getAggregation()) {
            case 0:
               paintCompositeEnd(newAttrs, eAttrs);
               break;
            case 1:
               paintSimpleEnd(newAttrs, eAttrs, end2);
               break;
            case 2:
               paintAggregateEnd(newAttrs, eAttrs);
            }
         } else if (begin.getUserObject() == participant2) {
            switch(end2.getAggregation()) {
            case 0:
               paintCompositeBegin(newAttrs, eAttrs);
               break;
            case 1:
               paintSimpleBegin(newAttrs, eAttrs, end2);
               break;
            case 2:
               paintAggregateBegin(newAttrs, eAttrs);
            }
         }

         edge.setAttributes(newAttrs);
         eview.setAttributes(eAttrs);
         graph.repaint();
         graph.setSelectionCell(begin);
         return edge;
      }
   }

   public static void deselectCells(DiagramGraph graph) {
      Object[] selected = graph.getSelectionCells();

      for(int i = 0; i < selected.length; ++i) {
         graph.removeSelectionCell(selected[i]);
      }

   }
}
