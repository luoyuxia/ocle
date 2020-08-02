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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.border.Border;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;

public class ObjectView extends VertexView {
   public ObjectView.Object_Renderer renderer = new ObjectView.Object_Renderer();
   private Dimension preferred_size = null;
   private boolean VIEW_CHANGE = false;

   public ObjectView(Object cell, JGraph graph, CellMapper cm) {
      super(cell, graph, cm);
   }

   public Point getPerimeterPoint(Point source, Point p) {
      return super.getPerimeterPoint(source, p);
   }

   public CellViewRenderer getRenderer() {
      return this.renderer;
   }

   public Dimension getPreferredSize() {
      return this.preferred_size;
   }

   public void signalViewChange() {
      this.VIEW_CHANGE = !this.VIEW_CHANGE;
   }

   public boolean isSignaled() {
      return this.VIEW_CHANGE;
   }

   public class Object_Renderer extends VertexRenderer {
      private Color outline_color;
      private Color fill_color;
      private Color selection_color;
      private int outline_width;
      private int titlefont_size = 12;
      private int bodyfont_size = 10;
      private String titlefont_name = "Tahoma";
      private String bodyfont_name = "Tahoma";

      public Object_Renderer() {
      }

      protected void initGraphicalAttributes() {
         ObjectGProperty property = (ObjectGProperty)((ObjectCell)this.view.getCell()).getProperty();
         this.outline_color = property.getOutlineColor();
         this.fill_color = property.getFillColor();
         this.selection_color = property.getSelectionColor();
         this.outline_width = property.getOutlineWidth();
         this.titlefont_size = property.getTitleFontSize();
         this.bodyfont_size = property.getBodyFontSize();
      }

      private String getTitleFor(Object o) {
         if (!(o instanceof Instance)) {
            return "Error";
         } else {
            Instance inst = (Instance)o;
            String title = inst.getName() + ":";
            Collection clsf = inst.getCollectionClassifierList();
            Iterator it = clsf.iterator();
            if (it.hasNext()) {
               title = title + ((Classifier)it.next()).getName();
            }

            return title;
         }
      }

      private ArrayList getAttributesFor(Object o) {
         ArrayList result = new ArrayList();
         Instance inst = (Instance)o;
         Collection attrs = inst.getCollectionSlotList();

         String attr;
         for(Iterator it = attrs.iterator(); it.hasNext(); result.add(attr)) {
            attr = "";
            AttributeLink alink = (AttributeLink)it.next();
            if (alink.getValue() != null) {
               attr = alink.getAttribute().getName() + " = " + alink.getValue().getName();
            } else {
               attr = alink.getAttribute().getName() + " = undefined";
            }
         }

         return result;
      }

      private void drawObject(Graphics g) {
         boolean need_width_resize = false;
         Object o = ((ObjectCell)this.view.getCell()).getUserObject();
         int b = this.borderWidth;
         Graphics2D g2 = (Graphics2D)g;
         Dimension d = this.getSize();
         Stroke stroke1 = new BasicStroke(1.0F);
         Stroke stroke2 = new BasicStroke(2.0F);
         Stroke strokedef = new BasicStroke((float)this.outline_width);
         g2.setColor(Color.black);
         g2.setStroke(stroke2);
         Rectangle class_rect = new Rectangle(b, b, d.width - b - 1, d.height - b - 1);
         double pos_y = 0.0D;
         Font font = new Font(this.titlefont_name, 0, this.titlefont_size);
         g2.setFont(font);
         int i;
         int left_margin;
         if (o instanceof ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object) {
            Iterator stereotypes = ((Instance)o).getCollectionStereotypeList().iterator();
            Font sfont = new Font(this.titlefont_name, 0, this.titlefont_size);

            while(stereotypes.hasNext()) {
               g2.setFont(sfont);
               String stereotypeName = "<<" + ((Stereotype)stereotypes.next()).getName() + ">>";
               i = g2.getFontMetrics(sfont).getHeight();
               left_margin = g2.getFontMetrics(sfont).stringWidth(stereotypeName);
               double spos_x;
               if (left_margin < class_rect.width) {
                  spos_x = (double)((class_rect.width - left_margin) / 2);
               } else {
                  spos_x = 3.0D;
               }

               if (spos_x * 2.0D + (double)left_margin > (double)class_rect.width) {
                  class_rect.setSize((int)(spos_x * 2.0D + (double)left_margin + class_rect.getX()), class_rect.height);
                  need_width_resize = true;
               }

               pos_y += (double)(i + b + 3);
               g2.drawString(stereotypeName, (float)spos_x, (float)pos_y);
            }
         }

         String name = this.getTitleFor(o);
         int name_height = g2.getFontMetrics(font).getHeight();
         int name_width = g2.getFontMetrics(font).stringWidth(name);
         double pos_x;
         if (name_width < class_rect.width) {
            pos_x = (double)((class_rect.width - name_width) / 2) + class_rect.getX();
         } else {
            pos_x = 3.0D;
         }

         if (pos_x * 2.0D + (double)name_width + class_rect.getX() > (double)class_rect.width) {
            class_rect.setSize((int)(pos_x * 2.0D + (double)name_width + class_rect.getX()), class_rect.height);
            need_width_resize = true;
         }

         pos_y += (double)(name_height + b + 3);
         g2.drawString(name, (float)pos_x, (float)pos_y);
         g2.setStroke(stroke1);
         g2.drawLine((int)pos_x, (int)pos_y + 3, (int)pos_x + name_width, (int)pos_y + 3);
         g2.setStroke(stroke2);
         g2.setColor(this.outline_color);
         g2.setStroke(strokedef);
         g2.drawLine(b, (int)pos_y + b + 7, class_rect.width - b, (int)pos_y + b + 7);
         g2.setColor(Color.black);
         g2.setStroke(stroke2);
         font = new Font(this.bodyfont_name, 0, this.bodyfont_size);
         g2.setFont(font);
         double space = pos_y + (double)b + 6.0D;
         int aux_space = 18;
         if (o instanceof Instance) {
            ArrayList attrs = this.getAttributesFor(o);

            for(i = 0; i < attrs.size(); ++i) {
               int left_marginx = 5;
               String content = (String)attrs.get(i);
               left_margin = left_marginx + 3;
               int text_width = g2.getFontMetrics(font).stringWidth(content);
               int text_height = g2.getFontMetrics(font).getHeight();
               if (left_margin + text_width > class_rect.width) {
                  class_rect.width = left_margin + text_width;
                  need_width_resize = true;
               }

               if (ObjectView.this.preferred_size != null && left_margin + text_width > ObjectView.this.preferred_size.width) {
                  ObjectView.this.preferred_size.width = left_margin + text_width + 3;
               }

               g2.drawString(content, left_margin, (int)(space + (double)aux_space));
               int add = aux_space > text_height ? aux_space : text_height;
               space = space + (double)add + 3.0D;
            }
         }

         if ((double)class_rect.height < space) {
            class_rect.height = (int)space;
         }

         if (ObjectView.this.VIEW_CHANGE && class_rect.height > ClassCell.CELL_HEIGHT) {
            class_rect.height = (int)space;
            if (ObjectView.this.preferred_size != null) {
               ObjectView.this.preferred_size.height = (int)space;
            }
         }

         if (ObjectView.this.VIEW_CHANGE && ObjectView.this.preferred_size.width < class_rect.width && need_width_resize && ObjectView.this.preferred_size != null) {
            ObjectView.this.preferred_size.width = class_rect.width + 2;
         }

         ObjectView.this.preferred_size = new Dimension(class_rect.width, class_rect.height);
         this.setSize(class_rect.width, class_rect.height);
         g2.setColor(this.outline_color);
         g2.setStroke(strokedef);
         g2.drawRect(b, b, d.width - b - 1, d.height - b - 1);
         g2.setColor(Color.black);
      }

      public void paint(Graphics g) {
         this.initGraphicalAttributes();
         int b = this.borderWidth;
         boolean tmp = this.selected;
         Dimension d = this.getSize();
         if (super.isOpaque()) {
            g.setColor(this.fill_color);
            g.fillRect(b, b, d.width - b, d.height - b);
         }

         this.drawObject(g);
         Graphics2D g2 = (Graphics2D)g;
         this.setText((String)null);

         try {
            this.setBorder((Border)null);
            this.setOpaque(false);
            this.selected = false;
            super.paint(g);
         } finally {
            this.selected = tmp;
         }

         if (this.selected) {
            g2.setStroke(GraphConstants.SELECTION_STROKE);
            g2.setColor(this.selection_color);
            g2.drawRect(0, 0, d.width - b, d.height - b);
         }

      }
   }
}
