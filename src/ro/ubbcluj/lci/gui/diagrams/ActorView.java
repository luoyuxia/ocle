package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellViewRenderer;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.VertexRenderer;
import com.jgraph.graph.VertexView;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import javax.swing.border.Border;

public class ActorView extends VertexView {
   public ActorView.ActorRenderer renderer = new ActorView.ActorRenderer();
   private boolean VIEW_CHANGE = false;
   Dimension prefSize = null;

   public ActorView(Object cell, JGraph graph, CellMapper cm) {
      super(cell, graph, cm);
   }

   public Point getPerimeterPoint(Point source, Point p) {
      Rectangle r = this.getBounds();
      int x = r.x;
      int y = r.y;
      int a = (r.width + 1) / 2;
      int b = (r.height + 1) / 2;
      int xCenter = x + a - 2;
      int yCenter = y + b - 2;
      int dx = p.x - xCenter;
      int dy = p.y - yCenter;
      double t = Math.atan2((double)dy, (double)dx);
      int xout = xCenter + (int)((double)a * Math.cos(t));
      int yout = yCenter + (int)((double)b * Math.sin(t));
      return new Point(xout, yout);
   }

   public CellViewRenderer getRenderer() {
      return this.renderer;
   }

   public Dimension getPreferredSize() {
      return this.prefSize;
   }

   public void signalViewChange() {
      this.VIEW_CHANGE = !this.VIEW_CHANGE;
   }

   public boolean isSignaled() {
      return this.VIEW_CHANGE;
   }

   public class ActorRenderer extends VertexRenderer {
      private Color outline_color;
      private int outline_width;
      private Color selection_color;
      private Color fill_color;
      private Stroke drawStroke;

      public ActorRenderer() {
      }

      protected void initGraphicalAttributes() {
         ActorGProperty property = (ActorGProperty)((ActorCell)this.view.getCell()).getProperty();
         this.outline_color = property.getOutlineColor();
         this.fill_color = property.getFillColor();
         this.selection_color = property.getSelectionColor();
         this.outline_width = property.getOutlineWidth();
         this.drawStroke = new BasicStroke((float)this.outline_width);
      }

      public void paint(Graphics g) {
         this.initGraphicalAttributes();
         Graphics2D g2 = (Graphics2D)g;
         g2.setStroke(this.drawStroke);
         Dimension d = this.getSize();
         boolean tmp = this.selected;
         if (super.isOpaque()) {
            g2.setColor(this.fill_color);
            g2.fillOval(5 * d.width / 12, d.height / 12, d.width / 6, d.height / 6);
         }

         g2.setColor(this.outline_color);
         g2.drawOval(5 * d.width / 12, d.height / 12, d.width / 6, d.height / 6);
         g2.drawLine(d.width / 3, d.height / 3, 2 * d.width / 3, d.height / 3);
         g2.drawLine(d.width / 2, d.height / 2, d.width / 3, 2 * d.height / 3);
         g2.drawLine(d.width / 2, d.height / 2, 2 * d.width / 3, 2 * d.height / 3);
         g2.drawLine(d.width / 2, d.height / 2, d.width / 2, d.height / 4);
         String name = ((ActorCell)this.view.getCell()).getUserObject().toString();
         int fw = g2.getFontMetrics().stringWidth(name);
         g2.drawString(name, d.width / 2 - fw / 2, 5 * d.height / 6);
         this.setText((String)null);

         try {
            this.setBorder((Border)null);
            this.setOpaque(false);
            this.selected = false;
            super.paint(g);
         } finally {
            this.selected = tmp;
         }

         if (this.bordercolor != null) {
            g.setColor(this.bordercolor);
         }

         if (this.selected) {
            g2.setStroke(GraphConstants.SELECTION_STROKE);
            g.setColor(this.selection_color);
         }

      }
   }
}
