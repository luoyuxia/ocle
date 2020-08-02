package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellView;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.Port;
import com.jgraph.graph.PortView;
import com.jgraph.graph.VertexView;
import java.awt.Point;
import java.awt.Rectangle;

public class SpecialPortView extends PortView {
   public SpecialPortView(Object cell, JGraph graph, CellMapper mapper) {
      super(cell, graph, mapper);
   }

   public Point getOldStyleLocation(EdgeView edge) {
      Object modelAnchor = null;
      if (this.cell instanceof Port) {
         modelAnchor = ((Port)this.cell).getAnchor();
      }

      PortView anchor = (PortView)this.mapper.getMapping(modelAnchor, false);
      Point pos = null;
      boolean isAbsolute = GraphConstants.isAbsolute(this.attributes);
      Point offset = GraphConstants.getOffset(this.attributes);
      Point centerPoint = null;
      Rectangle bounds = null;
      if (this.getParentView() instanceof VertexView) {
         centerPoint = ((VertexView)this.getParentView()).getCenterPoint();
         bounds = ((VertexView)this.getParentView()).getBounds();
      }

      if (this.getParentView() instanceof SpecialEdgeView) {
         centerPoint = ((SpecialEdgeView)this.getParentView()).getCenterPoint();
         bounds = ((SpecialEdgeView)this.getParentView()).getBounds();
      }

      if (edge == null && offset == null) {
         pos = centerPoint;
      }

      if (offset != null) {
         int x = offset.x;
         int y = offset.y;
         if (!isAbsolute) {
            x = x * (bounds.width - 1) / 1000;
            y = y * (bounds.height - 1) / 1000;
         }

         pos = anchor != null ? anchor.getLocation(edge) : bounds.getLocation();
         pos = new Point(pos.x + x, pos.y + y);
      } else if (edge != null) {
         Point nearest = this.getNextPoint(edge);
         if (nearest == null) {
            return centerPoint;
         }

         Point perimiterPoint = null;
         if (this.getParentView() instanceof VertexView) {
            perimiterPoint = ((VertexView)this.getParentView()).getPerimeterPoint(pos, nearest);
         } else {
            perimiterPoint = centerPoint;
         }

         pos = perimiterPoint;
      }

      return pos;
   }

   protected Point getNextPoint(EdgeView edge) {
      int n = edge.getPointCount();
      if (n > 1) {
         if (edge.getSource() == this) {
            return this.getEdgePoint(edge, 1);
         }

         if (edge.getTarget() == this) {
            return this.getEdgePoint(edge, n - 2);
         }
      }

      return null;
   }

   protected Point getEdgePoint(EdgeView view, int index) {
      Object obj = GraphConstants.getPoints(view.getAttributes()).get(index);
      if (obj instanceof Point) {
         return (Point)obj;
      } else {
         Point centerPoint = null;
         if (((CellView)obj).getParentView() instanceof VertexView) {
            centerPoint = ((VertexView)((CellView)obj).getParentView()).getCenterPoint();
         }

         if (((CellView)obj).getParentView() instanceof SpecialEdgeView) {
            centerPoint = ((SpecialEdgeView)((CellView)obj).getParentView()).getCenterPoint();
         }

         return centerPoint;
      }
   }

   public Rectangle getBounds() {
      return this.getParentView().getBounds();
   }

   public Point getLocation(EdgeView edge) {
      if (this.getParentView() instanceof SpecialEdgeView) {
         return this.getOldStyleLocation(edge);
      } else {
         Point p = super.getLocation(edge);
         Rectangle r = this.getParentView().getBounds();
         if (edge != null && edge.getPointCount() > 2) {
            Point p1 = this.getNextPoint(edge);
            if (p1 != null) {
               if (p1.x > r.x && p1.x < r.x + r.width) {
                  p.x = p1.x;
               } else if (p1.y >= r.y && p1.y < r.y + r.height) {
                  p.y = p1.y;
               }

               if (p1.y < r.y) {
                  p.y = r.y;
               } else if (p1.y > r.y + r.height) {
                  p.y = r.y + r.height;
               }
            }
         }

         return p;
      }
   }
}
