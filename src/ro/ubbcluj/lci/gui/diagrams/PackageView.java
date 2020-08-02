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
import java.awt.Rectangle;
import javax.swing.border.Border;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.uml.modelManagement.Subsystem;

public class PackageView extends VertexView {
   public PackageView.PackageRenderer renderer = new PackageView.PackageRenderer();
   Dimension prefSize = null;

   public PackageView(Object cell, JGraph graph, CellMapper cm) {
      super(cell, graph, cm);
   }

   public CellViewRenderer getRenderer() {
      return this.renderer;
   }

   public Dimension getPreferredSize() {
      return this.prefSize;
   }

   public class PackageRenderer extends VertexRenderer {
      protected Color outline_color;
      protected int outline_width;
      protected Color selection_color;
      protected Color fill_color;
      protected int titlefont_size;
      protected String titlefont_name = "Tahoma";
      protected Font font;

      public PackageRenderer() {
      }

      protected void initGraphicalAttributes() {
         PackageGProperty property = (PackageGProperty)((PackageCell)this.view.getCell()).getProperty();
         this.outline_color = property.getOutlineColor();
         this.fill_color = property.getFillColor();
         this.selection_color = property.getSelectionColor();
         this.outline_width = property.getOutlineWidth();
         this.titlefont_size = property.getTitleFontSize();
         this.font = new Font(this.titlefont_name, 0, this.titlefont_size);
      }

      public void paint(Graphics g) {
         this.initGraphicalAttributes();
         int b = this.borderWidth;
         Graphics2D g2 = (Graphics2D)g;
         g2.setStroke(new BasicStroke((float)this.outline_width));
         Dimension d = this.getSize();
         boolean tmp = this.selected;
         if (super.isOpaque()) {
            g2.setColor(this.fill_color);
            g2.fillRect(b, b, (d.width - b) * 2 / 5 - 1, (d.height - b) / 5);
            g2.fillRect(b, b + (d.height - b) / 5, d.width - b - 1, (d.height - b) * 4 / 5 - 1);
         }

         g2.setColor(this.outline_color);
         g2.drawRect(b, b, (d.width - b) * 2 / 5 - 1, (d.height - b) / 5);
         g2.drawRect(b, b + (d.height - b) / 5, d.width - b - 1, (d.height - b) * 4 / 5 - 1);
         Object o = ((PackageCell)this.view.getCell()).getUserObject();
         if (o instanceof Subsystem) {
            int lw = d.width * 2 / 5;
            int lh = d.height / 5;
            g2.drawLine(lw * 5 / 6, lh / 6, lw * 5 / 6, lh / 2);
            g2.drawLine(lw * 9 / 12, lh / 2, lw * 11 / 12, lh / 2);
            g2.drawLine(lw * 9 / 12, lh / 2, lw * 9 / 12, lh * 5 / 6);
            g2.drawLine(lw * 11 / 12, lh / 2, lw * 11 / 12, lh * 5 / 6);
         }

         this.setText((String)null);
         Rectangle rect = new Rectangle(b, b, d.width, d.height);
         String name = ((Package)o).getName();
         g2.setFont(this.font);
         int name_height = g2.getFontMetrics(this.font).getHeight();
         int name_width = g2.getFontMetrics(this.font).stringWidth(name);
         double pos_x;
         if (name_width < rect.width) {
            pos_x = (double)((rect.width - name_width) / 2);
         } else {
            pos_x = 3.0D;
         }

         if (pos_x * 2.0D + (double)name_width > (double)rect.width) {
            rect.setSize((int)(pos_x * 2.0D + (double)name_width + rect.getX()), rect.height);
         }

         double pos_y = (double)(name_height + b + 3 + (d.height - b) / 5);
         g2.drawString(name, (float)pos_x, (float)pos_y);
         ElementOwnership eo = ((Package)o).getNamespace();
         String packageName = "(from " + eo.getNamespace().getName() + ")";
         if (((PackageCell)this.view.getCell()).getShowPackage()) {
            int p_height = g2.getFontMetrics(this.font).getHeight();
            int p_width = g2.getFontMetrics(this.font).stringWidth(packageName);
            double ppos_x;
            if (p_width < rect.width) {
               ppos_x = (double)((rect.width - p_width) / 2);
            } else {
               ppos_x = 3.0D;
            }

            if (ppos_x * 2.0D + (double)p_width + rect.getX() > (double)rect.width) {
               rect.setSize((int)(ppos_x * 2.0D + (double)p_width + rect.getX()), rect.height);
            }

            pos_y = pos_y + (double)p_height + (double)b;
            g2.drawString(packageName, (float)ppos_x, (float)pos_y);
            pos_y += (double)(p_height + 3);
         }

         this.setSize(rect.width, rect.height);
         if (PackageView.this.prefSize == null) {
            PackageView.this.prefSize = new Dimension(rect.width, rect.height);
         } else {
            int inc = 10;
            PackageView.this.prefSize.width = rect.width;
            PackageView.this.prefSize.height = rect.height;
            if ((double)rect.height < pos_y) {
               PackageView.this.prefSize.height = (int)pos_y + inc;
            }
         }

         try {
            this.setBorder((Border)null);
            this.setOpaque(false);
            this.selected = false;
            super.paint(g);
         } finally {
            this.selected = tmp;
         }

         if (this.bordercolor != null) {
            g2.setColor(this.outline_color);
            g2.setStroke(new BasicStroke((float)this.outline_width));
            g2.drawRect(b, b, (d.width - b) * 2 / 5 - 1, (d.height - b) / 5);
            g2.drawRect(b, b + (d.height - b) / 5, d.width - b - 1, (d.height - b) * 4 / 5 - 1);
         }

         if (this.selected) {
            g2.setStroke(GraphConstants.SELECTION_STROKE);
            g2.setColor(this.selection_color);
            g2.drawRect(b, b, (d.width - b) * 2 / 5 - 1, (d.height - b) / 5);
            g2.drawRect(b, b + (d.height - b) / 5, d.width - b - 1, (d.height - b) * 4 / 5 - 1);
         }

      }
   }
}
