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
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
import ro.ubbcluj.lci.gui.diagrams.filters.AbstractFilter;
import ro.ubbcluj.lci.gui.diagrams.filters.ClassFilter;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.Method;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class ClassView extends VertexView {
   public ClassView.Class_Renderer renderer = new ClassView.Class_Renderer();
   private Dimension preferred_size = null;
   private boolean VIEW_CHANGE = false;

   public ClassView(Object cell, JGraph graph, CellMapper cm) {
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

   private class Class_Renderer extends VertexRenderer {
      private Color outline_color;
      private Color fill_color;
      private Color selection_color;
      private int outline_width;
      private int titlefont_size;
      private int bodyfont_size;
      private String titlefont_name;
      private String bodyfont_name;
      private AbstractFilter filter;
      private Stroke drawStroke;
      private Font ifaceFont;
      private Font normalFont;
      private Font bodyFont;

      private Class_Renderer() {
         this.titlefont_name = "Tahoma";
         this.bodyfont_name = "Tahoma";
      }

      protected void initGraphicalAttributes() {
         this.filter = ((ClassCell)this.view.getCell()).getFilter();
         ClassGProperty property = (ClassGProperty)((ClassCell)this.view.getCell()).getProperty();
         this.outline_color = property.getOutlineColor();
         this.fill_color = property.getFillColor();
         this.selection_color = property.getSelectionColor();
         this.outline_width = property.getOutlineWidth();
         this.titlefont_size = property.getTitleFontSize();
         this.bodyfont_size = property.getBodyFontSize();
         this.drawStroke = new BasicStroke((float)this.outline_width);
         this.ifaceFont = new Font(this.titlefont_name, 2, this.titlefont_size);
         this.normalFont = new Font(this.titlefont_name, 0, this.titlefont_size);
         this.bodyFont = new Font(this.bodyfont_name, 0, this.bodyfont_size);
      }

      private ImageIcon getImageforAttr(int type) {
         return ImageHolder.getImageforAttr(type);
      }

      private ImageIcon getImageforMethod(int type) {
         return ImageHolder.getImageforMethod(type);
      }

      private Vector getAttributes(Classifier c) {
         Vector v = new Vector();
         Enumeration features = c.getFeatureList();

         while(features.hasMoreElements()) {
            Object el = features.nextElement();
            if (el instanceof Attribute && this.filter.accepts(el)) {
               v.add(el);
            }
         }

         return v;
      }

      private Vector getMethods(Classifier c) {
         Vector v = new Vector();
         Enumeration features = c.getFeatureList();

         while(true) {
            Object el;
            do {
               if (!features.hasMoreElements()) {
                  return v;
               }

               el = features.nextElement();
            } while(!(el instanceof Method) && !(el instanceof Operation));

            if (this.filter.accepts(el)) {
               v.add(el);
            }
         }
      }

      private ArrayList getStereotypes(Classifier c) {
         ArrayList result = new ArrayList();
         Enumeration stereos = c.getStereotypeList();

         while(stereos.hasMoreElements()) {
            Stereotype stereo = (Stereotype)stereos.nextElement();
            if (this.filter.accepts(stereo)) {
               String s = "<<" + stereo.getName().trim() + ">>";
               result.add(s);
            }
         }

         return result;
      }

      private void drawClass(Graphics g) {
         boolean need_width_resize = false;
         boolean has_stereotypes = false;
         Object o = ((ClassCell)this.view.getCell()).getUserObject();
         boolean is_interface = o instanceof Interface;
         int b = this.borderWidth;
         Graphics2D g2 = (Graphics2D)g;
         Dimension d = this.getSize();
         g2.setColor(Color.black);
         Rectangle class_rect = new Rectangle(b, b, d.width - b - 1, d.height - b - 1);
         double pos_y = 0.0D;
         Font font = null;
         if (((ClassCell)this.view.getCell()).isFiltered()) {
            g2.setColor(Color.darkGray);
         }

         if (is_interface) {
            font = this.ifaceFont;
         } else {
            font = this.normalFont;
         }

         if (o instanceof Classifier && ((Classifier)o).isAbstract()) {
            font = this.ifaceFont;
         }

         g2.setFont(font);
         String name = ((Classifier)o).getName();
         ElementOwnership eo = ((Classifier)o).getNamespace();
         String packageName = "(from " + eo.getNamespace().getName() + ")";
         int name_height = g2.getFontMetrics(font).getHeight();
         int name_width = g2.getFontMetrics(font).stringWidth(name);
         Font p_font;
         int p_width;
         int left_marginxx;
         if (is_interface) {
            p_font = this.normalFont;
            g2.setFont(p_font);
            String iface = "<<interface>>";
            p_width = g2.getFontMetrics(p_font).getHeight();
            left_marginxx = g2.getFontMetrics(p_font).stringWidth(iface);
            double ipos_x;
            if (left_marginxx < class_rect.width) {
               ipos_x = (double)((class_rect.width - left_marginxx) / 2) + class_rect.getX();
            } else {
               ipos_x = 3.0D;
               class_rect.width = 5 + left_marginxx + 5;
            }

            pos_y = (double)(p_width + b + 3);
            g2.drawString(iface, (float)ipos_x, (float)pos_y);
            pos_y += (double)p_width;
         }

         int i;
         if (o instanceof Classifier) {
            Classifier c = (Classifier)o;
            ArrayList stlist = this.getStereotypes(c);

            for(i = 0; i < stlist.size(); ++i) {
               has_stereotypes = true;
               Font sfont = this.normalFont;
               g2.setFont(sfont);
               String stereotype = (String)stlist.get(i);
               int s_height = g2.getFontMetrics(sfont).getHeight();
               int s_width = g2.getFontMetrics(sfont).stringWidth(stereotype);
               double spos_x;
               if (s_width < class_rect.width) {
                  spos_x = (double)((class_rect.width - s_width) / 2);
               } else {
                  spos_x = 3.0D;
               }

               if (spos_x * 2.0D + (double)s_width > (double)class_rect.width) {
                  class_rect.setSize((int)(spos_x * 2.0D + (double)s_width + class_rect.getX()), class_rect.height);
                  need_width_resize = true;
               }

               pos_y += (double)(s_height + b + 3);
               g2.drawString(stereotype, (float)spos_x, (float)pos_y);
            }
         }

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

         g2.setFont(font);
         if (!is_interface || has_stereotypes) {
            pos_y += (double)(name_height + b + 5);
         }

         g2.drawString(name, (float)pos_x, (float)pos_y);
         if (((ClassCell)this.view.getCell()).getShowPackage()) {
            p_font = this.normalFont;
            g2.setFont(p_font);
            int p_height = g2.getFontMetrics(p_font).getHeight();
            p_width = g2.getFontMetrics(p_font).stringWidth(packageName);
            double ppos_x;
            if (p_width < class_rect.width) {
               ppos_x = (double)((class_rect.width - p_width) / 2) + class_rect.getX();
            } else {
               ppos_x = 3.0D;
            }

            if (ppos_x * 2.0D + (double)p_width + class_rect.getX() > (double)class_rect.width) {
               class_rect.setSize((int)(ppos_x * 2.0D + (double)p_width + class_rect.getX()), class_rect.height);
               need_width_resize = true;
            }

            pos_y = pos_y + (double)p_height + (double)b;
            g2.drawString(packageName, (float)ppos_x, (float)pos_y);
         }

         g2.setColor(this.outline_color);
         g2.setStroke(this.drawStroke);
         g2.drawLine(b, (int)pos_y + b + 5, class_rect.width - b, (int)pos_y + b + 5);
         g2.setColor(Color.black);
         font = this.bodyFont;
         g2.setFont(font);
         double space = pos_y + (double)b + 6.0D;
         if (o instanceof Class || o instanceof Interface) {
            Classifier cx = (Classifier)o;
            Vector attrs = this.getAttributes(cx);

            byte left_marginxxx;
            ImageIcon img;
            int text_widthx;
            int text_height;
            int image_height;
            for(i = 0; i < attrs.size(); ++i) {
               int left_margin = 5;
               left_marginxxx = 5;
               Attribute a = (Attribute)attrs.get(i);
               StringBuffer content = new StringBuffer();
               content.append(a.getName());
               if (a.getType() != null) {
                  content.append(":" + a.getType().getName());
               }

               img = this.getImageforAttr(a.getVisibility());
               g2.drawImage(img.getImage(), left_margin, (int)space, (ImageObserver)null);
               left_marginxx = left_margin + img.getIconWidth() + 2;
               int text_width = g2.getFontMetrics(font).stringWidth(content.toString());
               text_widthx = g2.getFontMetrics(font).getHeight();
               if (left_marginxx + text_width + left_marginxxx > class_rect.width) {
                  class_rect.width = left_marginxx + text_width + left_marginxxx;
                  need_width_resize = true;
               }

               if (ClassView.this.preferred_size != null && left_marginxx + text_width > ClassView.this.preferred_size.width) {
                  ClassView.this.preferred_size.width = left_marginxx + text_width + 3;
               }

               g2.drawString(content.toString(), left_marginxx, (int)(space + (double)img.getIconHeight()));
               text_height = img.getIconHeight();
               image_height = text_height > text_widthx ? text_height : text_widthx;
               space = space + (double)image_height + 3.0D;
            }

            space += 3.0D;
            Vector mthd = this.getMethods(cx);
            if (attrs.size() > 0 && mthd.size() > 0 && !is_interface) {
               g2.setColor(this.outline_color);
               g2.setStroke(this.drawStroke);
               g2.drawLine(class_rect.x, (int)space, class_rect.width, (int)space);
               g2.setColor(Color.black);
            }

            for(i = 0; i < mthd.size(); ++i) {
               left_marginxxx = 5;
               int right_margin = 5;
               BehavioralFeature feat = (BehavioralFeature)mthd.get(i);
               img = this.getImageforMethod(feat.getVisibility());
               StringBuffer contentx = new StringBuffer();
               if (((ClassFilter)this.filter).showMethodSignature()) {
                  contentx.append(UMLUtilities.getFullSignature(feat, true));
               } else {
                  contentx.append(feat.getName().trim() + "()");
               }

               g2.drawImage(img.getImage(), left_marginxxx, (int)space, (ImageObserver)null);
               int left_marginx = left_marginxxx + img.getIconWidth() + 2;
               text_widthx = g2.getFontMetrics(font).stringWidth(contentx.toString());
               text_height = g2.getFontMetrics(font).getHeight();
               if (left_marginx + text_widthx + right_margin > class_rect.width) {
                  class_rect.width = left_marginx + text_widthx + right_margin;
                  need_width_resize = true;
               }

               if (ClassView.this.preferred_size != null && left_marginx + text_widthx > ClassView.this.preferred_size.width) {
                  ClassView.this.preferred_size.width = left_marginx + text_widthx + 3;
               }

               g2.drawString(contentx.toString(), left_marginx, (int)(space + (double)img.getIconHeight()));
               image_height = img.getIconHeight();
               int add = image_height > text_height ? image_height : text_height;
               space = space + (double)add + 3.0D;
            }
         }

         space += 3.0D;
         if ((double)class_rect.height < space) {
            class_rect.height = (int)space;
         }

         if (ClassView.this.VIEW_CHANGE && class_rect.height > ClassCell.CELL_HEIGHT) {
            class_rect.height = (int)space;
            ClassView.this.preferred_size.height = (int)space;
         }

         if (ClassView.this.VIEW_CHANGE && ClassView.this.preferred_size.width < class_rect.width && need_width_resize) {
            ClassView.this.preferred_size.width = class_rect.width + 2;
         }

         ClassView.this.preferred_size = new Dimension(class_rect.width, class_rect.height);
         this.setSize(class_rect.width, class_rect.height);
         g2.setColor(this.outline_color);
         g2.setStroke(this.drawStroke);
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

         this.drawClass(g);
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
