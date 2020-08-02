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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import javax.swing.border.Border;

public class UseCaseView extends VertexView {
   public static UseCaseView.UseCase_Renderer renderer = new UseCaseView.UseCase_Renderer();
   private boolean VIEW_CHANGE = false;

   public UseCaseView(Object cell, JGraph graph, CellMapper cm) {
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
      return renderer;
   }

   public Dimension getPreferredSize() {
      return renderer.getPreferredSize();
   }

   public void signalViewChange() {
      this.VIEW_CHANGE = !this.VIEW_CHANGE;
   }

   public boolean isSignaled() {
      return this.VIEW_CHANGE;
   }

   public static class UseCase_Renderer extends VertexRenderer {
      private Color outline_color;
      private int outline_width;
      private Color selection_color;
      private Color fill_color;
      private int titlefont_size;
      private Stroke drawStroke;
      private Font drawFont;

      public UseCase_Renderer() {
      }

      protected void initGraphicalAttributes() {
         PackageGProperty property = (PackageGProperty)((UseCaseCell)this.view.getCell()).getProperty();
         this.outline_color = property.getOutlineColor();
         this.fill_color = property.getFillColor();
         this.selection_color = property.getSelectionColor();
         this.outline_width = property.getOutlineWidth();
         this.titlefont_size = property.getTitleFontSize();
         this.drawStroke = new BasicStroke((float)this.outline_width);
         this.drawFont = new Font("DIALOG", 0, this.titlefont_size);
      }

      public void paint(Graphics g) {
         this.initGraphicalAttributes();
         int b = this.borderWidth;
         Graphics2D g2 = (Graphics2D)g;
         g2.setStroke(this.drawStroke);
         Dimension d = this.getSize();
         boolean tmp = this.selected;
         if (super.isOpaque()) {
            g2.setColor(this.fill_color);
            g2.fillOval(b - 1, b - 1, d.width - b - 1, d.height - b - 1);
         }

         g2.setColor(this.outline_color);
         g2.drawOval(b, b, d.width - b - 1, d.height - b - 1);

         try {
            this.setBorder((Border)null);
            this.setOpaque(false);
            this.selected = false;
            super.setFont(this.drawFont);
            super.paint(g);
         } finally {
            this.selected = tmp;
         }

         if (this.bordercolor != null) {
            g2.setColor(this.outline_color);
            g2.setStroke(this.drawStroke);
            g.drawOval(b, b, d.width - b, d.height - b);
         }

         if (this.selected) {
            g2.setStroke(GraphConstants.SELECTION_STROKE);
            g.setColor(this.selection_color);
            g.drawOval(b, b, d.width - b, d.height - b);
         }

      }
   }
}
