package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellView;
import com.jgraph.graph.CellViewRenderer;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.EdgeRenderer;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.PortView;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Extend;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.Include;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public class SpecialEdgeView extends EdgeView {
   public EdgeRenderer renderer = new SpecialEdgeView.SpecialEdgeRenderer();
   private Rectangle beginRect;
   private Rectangle endRect;
   private static ArrayList beginQuals;
   private static ArrayList endQuals;
   private static final int hGap = 3;
   private static final int vGap = 3;

   public SpecialEdgeView(Object cell, JGraph graph, CellMapper mapper) {
      super(cell, graph, mapper);
   }

   public Point getPoint(int index) {
      Object obj = this.points.get(index);
      if (obj instanceof SpecialPortView) {
         return ((SpecialPortView)obj).getLocation(this);
      } else if (obj instanceof PortView) {
         return ((PortView)obj).getLocation(this);
      } else {
         return obj instanceof Point ? (Point)obj : null;
      }
   }

   public void refresh(boolean createDependentViews) {
      super.refresh(createDependentViews);
      if (((SpecialEdge)this.getCell()).getRouted()) {
         ((SpecialEdge)this.getCell()).route(this, this.points);
      }

   }

   public void update() {
      super.update();
      if (((SpecialEdge)this.getCell()).getRouted()) {
         ((SpecialEdge)this.getCell()).route(this, this.points);
      }

   }

   List getPoints() {
      return this.points;
   }

   private ArrayList getControlPoints() {
      ArrayList result = new ArrayList();

      for(int i = 0; i < this.points.size(); ++i) {
         if (this.points.get(i) instanceof Point) {
            result.add(this.points.get(i));
         }
      }

      return result;
   }

   int getControlPointIndex() {
      for(int i = 0; i < this.points.size(); ++i) {
         if (this.points.get(i) instanceof Point) {
            return i;
         }
      }

      return -1;
   }

   public Point getCenterPoint() {
      Rectangle r = this.getBounds();
      int n = this.points.size();
      int i = n / 2;
      if (this.points.get(i) instanceof Point) {
         return (Point)this.points.get(i);
      } else if (this.points.get(i - 1) instanceof Point) {
         return (Point)this.points.get(i - 1);
      } else {
         Rectangle sourceBounds = ((SpecialPortView)this.points.get(0)).getBounds();
         Rectangle targetBounds = ((SpecialPortView)this.points.get(this.points.size() - 1)).getBounds();
         Point begin = new Point((int)sourceBounds.getCenterX(), (int)sourceBounds.getCenterY());
         Point end = new Point((int)targetBounds.getCenterX(), (int)targetBounds.getCenterY());
         return new Point(begin.x / 2 + end.x / 2, begin.y / 2 + end.y / 2);
      }
   }

   public Point getPerimeterPoint(Point source, Point p) {
      Rectangle bounds = this.getBounds();
      int x = bounds.x;
      int y = bounds.y;
      int width = bounds.width;
      int height = bounds.height;
      int xCenter = x + width / 2;
      int yCenter = y + height / 2;
      int dx = p.x - xCenter;
      int dy = p.y - yCenter;
      double alpha = Math.atan2((double)dy, (double)dx);
      double pi = 3.141592653589793D;
      double pi2 = 1.5707963267948966D;
      double beta = pi2 - alpha;
      double t = Math.atan2((double)height, (double)width);
      int xout;
      int yout;
      if (alpha >= -pi + t && alpha <= pi - t) {
         if (alpha < -t) {
            yout = y;
            xout = xCenter - (int)((double)height * Math.tan(beta) / 2.0D);
         } else if (alpha < t) {
            xout = x + width;
            yout = yCenter + (int)((double)width * Math.tan(alpha) / 2.0D);
         } else {
            yout = y + height;
            xout = xCenter + (int)((double)height * Math.tan(beta) / 2.0D);
         }
      } else {
         xout = x;
         yout = yCenter - (int)((double)width * Math.tan(alpha) / 2.0D);
      }

      return new Point(xout - 1, yout - 1);
   }

   public CellViewRenderer getRenderer() {
      return this.renderer;
   }

   private boolean checkSinglePointCondition(Point pt) {
      CellView beginView = this.getSource().getParentView();
      CellView endView = this.getTarget().getParentView();
      Rectangle beginRect = GraphConstants.getBounds(beginView.getAttributes());
      Rectangle endRect = GraphConstants.getBounds(endView.getAttributes());
      return !beginRect.contains(pt) && !endRect.contains(pt);
   }

   private boolean checkLineCondition(Point pt1, Point pt2) {
      CellView beginView = this.getSource().getParentView();
      CellView endView = this.getTarget().getParentView();
      Rectangle beginRect = GraphConstants.getBounds(beginView.getAttributes());
      Rectangle endRect = GraphConstants.getBounds(endView.getAttributes());
      Graphics2D g2 = (Graphics2D)this.getGraph().getGraphics();
      Double line = new Double(pt1, pt2);
      return !g2.hit(beginRect, line, true) && !g2.hit(endRect, line, true);
   }

   private void removeBadPoints() {
      ArrayList pts = this.getControlPoints();
      if (pts.size() != 0) {
         int i;
         Point pt1;
         for(i = 0; i < pts.size(); ++i) {
            pt1 = (Point)pts.get(i);
            if (!this.checkSinglePointCondition(pt1)) {
               this.points.remove(pt1);
            }
         }

         this.getGraph().repaint();
         pts = this.getControlPoints();
         if (pts.size() >= 2) {
            for(i = 0; i < pts.size() - 1; ++i) {
               pt1 = (Point)pts.get(i);
               Point pt2 = (Point)pts.get(i + 1);
               if (!this.checkLineCondition(pt1, pt2)) {
                  this.points.remove(pt1);
               }
            }

         }
      }
   }

   void removeAllControlPoints() {
      ArrayList lst = this.getControlPoints();

      for(int i = 0; i < lst.size(); ++i) {
         this.points.remove(lst.get(i));
      }

      this.renderer.repaint();
   }

   void straightAngles() {
      this.removeBadPoints();
      Point beginPort = this.getPoint(0);
      Point endPort = this.getPoint(this.getPointCount() - 1);
      ArrayList ctrlPts = this.getControlPoints();
      if (ctrlPts.size() == 0) {
         Point pt = new Point((beginPort.x + endPort.x) / 2, (beginPort.y + endPort.y) / 2);
         this.addPoint(1, pt);
      } else {
         ArrayList pts = new ArrayList();

         for(int i = 0; i < this.getPointCount(); ++i) {
            pts.add(this.getPoint(i));
         }

         CellView beginView = this.getSource().getParentView();
         CellView endView = this.getTarget().getParentView();
         Rectangle beginRect = GraphConstants.getBounds(beginView.getAttributes());
         Rectangle endRect = GraphConstants.getBounds(endView.getAttributes());
         int ct = 5;
         Point p2;
         Point interm;
         if (pts.size() == 3) {
            Point port1 = (Point)pts.get(0);
            interm = (Point)pts.get(1);
            p2 = (Point)pts.get(2);
            if ((port1.x != interm.x || interm.x != p2.x) && (port1.y != interm.y || interm.y != p2.y)) {
               int dp1 = Math.abs(port1.x - interm.x);
               int dp2 = Math.abs(p2.x - interm.x);
               Point interm1 = new Point();
               if (dp1 < dp2) {
                  if (interm.x < port1.x) {
                     interm.x = port1.x + ct;
                  } else {
                     interm.x = port1.x - ct;
                  }
               } else if (interm.x < p2.x) {
                  interm.x = p2.x + ct;
               } else {
                  interm.x = p2.x - ct;
               }

               interm1.x = dp1 < dp2 ? p2.x + ct : port1.x - ct;
               interm1.y = interm.y;
               if (interm.x < interm1.x) {
                  this.points.set(1, interm);
                  this.points.add(2, interm1);
               } else {
                  this.points.set(1, interm1);
                  this.points.add(2, interm);
               }

               this.removeBadPoints();
               if (this.points.size() == 2) {
                  this.straightAngles();
               }

            }
         } else {
            int size = this.points.size();

            for(int j = 2; j < size - 2; ++j) {
               this.points.remove(3);
            }

            interm = (Point)pts.get(0);
            p2 = (Point)this.points.get(1);
            Point p3 = (Point)this.points.get(2);
            Point port2 = (Point)pts.get(3);
            int dp1 = Math.abs(interm.x - p2.x);
            int dp2 = Math.abs(interm.y - p2.y);
            int dp3 = Math.abs(port2.x - p3.x);
            int dp4 = Math.abs(port2.y - p3.y);
            boolean alignP1OnX = false;
            boolean alignP1OnY = false;
            boolean alignP2OnX = false;
            boolean alignP2OnY = false;
            int aux;
            if (dp1 < dp2) {
               if (p2.x != interm.x) {
                  aux = p2.x;
                  p2.x = interm.x;
                  if (aux > interm.x) {
                     p2.x -= ct;
                  } else {
                     p2.x += ct;
                  }
               }

               alignP1OnX = true;
            } else {
               if (p2.y != interm.y) {
                  aux = p2.y;
                  p2.y = interm.y;
                  if (aux > interm.y) {
                     p2.y -= ct;
                  } else {
                     p2.y += ct;
                  }
               }

               alignP1OnY = true;
            }

            if (dp3 < dp4) {
               if (p3.x != port2.x) {
                  aux = p3.x;
                  p3.x = port2.x;
                  if (aux > port2.x) {
                     p3.x -= ct;
                  } else {
                     p3.x += ct;
                  }
               }

               alignP2OnX = true;
            } else {
               if (p3.y != port2.y) {
                  aux = p3.y;
                  p3.y = port2.y;
                  if (aux > port2.y) {
                     p3.y -= ct;
                  } else {
                     p3.y += ct;
                  }
               }

               alignP2OnY = true;
            }

            if (alignP1OnY && alignP2OnY) {
               p2.x = p3.x;
            } else if (alignP1OnX && alignP2OnX) {
               p2.y = p3.y;
            } else if (dp1 < dp4) {
               p3.x = p2.x;
            } else {
               p2.x = p3.x;
            }

            this.removeBadPoints();
            if (this.points.size() == 2) {
               this.straightAngles();
            }

         }
      }
   }

   class SpecialEdgeRenderer extends EdgeRenderer {
      private String label = null;
      private Shape oldBeginShape;
      private Shape oldEndShape;
      private int oldBeginDeco;
      private int oldEndDeco;

      SpecialEdgeRenderer() {
      }

      public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview) {
         if (view instanceof EdgeView && graph != null && view != null) {
            this.view = (EdgeView)view;
            this.graph = graph;
            this.focus = focus;
            this.selected = sel;
            this.preview = preview;
            this.installAttributes(view);
            return this;
         } else {
            return null;
         }
      }

      public Rectangle getPaintBounds(EdgeView view) {
         Rectangle r = super.getPaintBounds(view);
         Font f = GraphConstants.getFont(view.getAttributes());
         if (SpecialEdgeView.this.beginRect != null) {
            r = r.union(SpecialEdgeView.this.beginRect);
         }

         if (SpecialEdgeView.this.endRect != null) {
            r = r.union(SpecialEdgeView.this.endRect);
         }

         if (this.label != null) {
            int sw = this.getFontMetrics(f).stringWidth(this.label);
            int sh = this.getFontMetrics(f).getHeight();
            r.width += 2 * sw;
            r.height += 2 * sh;
            r.x -= sw;
            r.y -= sh;
         }

         return r;
      }

      private void initRectDimension(Graphics g, ArrayList text, Rectangle r) {
         int height = g.getFontMetrics().getHeight();
         int totalHeight = (height + 3) * text.size() + 6;
         int totalWidth = 0;
         int maxWidth = 0;

         for(int i = 0; i < text.size(); ++i) {
            String s = (String)text.get(i);
            int sWidth = g.getFontMetrics().stringWidth(s);
            if (sWidth > maxWidth) {
               maxWidth = sWidth;
               totalWidth = 6 + sWidth;
            }
         }

         r.width = totalWidth;
         r.height = totalHeight;
      }

      private void initQualifierArea(Graphics g) {
         SpecialEdge edge = (SpecialEdge)SpecialEdgeView.this.cell;
         if (edge.hasQualifiers()) {
            Classifier beginCls = (Classifier)((DefaultGraphCell)((SpecialPort)edge.getSource()).getParent()).getUserObject();
            Classifier endCls = (Classifier)((DefaultGraphCell)((SpecialPort)edge.getTarget()).getParent()).getUserObject();
            Association asoc = (Association)edge.getUserObject();
            Enumeration asocEnds = asoc.getConnectionList();

            while(asocEnds.hasMoreElements()) {
               AssociationEnd end = (AssociationEnd)asocEnds.nextElement();
               ArrayList quals = edge.getQualifiers(end);
               if (end.getParticipant() == endCls) {
                  SpecialEdgeView.beginQuals = quals;
                  if (SpecialEdgeView.this.beginRect == null) {
                     SpecialEdgeView.this.beginRect = new Rectangle();
                  }

                  this.initRectDimension(g, quals, SpecialEdgeView.this.beginRect);
               } else {
                  SpecialEdgeView.endQuals = quals;
                  if (SpecialEdgeView.this.endRect == null) {
                     SpecialEdgeView.this.endRect = new Rectangle();
                  }

                  this.initRectDimension(g, quals, SpecialEdgeView.this.endRect);
               }
            }

         }
      }

      private void initOldShapes(boolean begin, int size, Point src, Point dst) {
         if (begin && this.oldBeginDeco != 0 && this.beginDeco == 100) {
            this.oldBeginShape = super.createLineEnd(size, this.oldBeginDeco, src, dst);
         }

         if (!begin && this.oldEndDeco != 0 && this.endDeco == 100) {
            this.oldEndShape = super.createLineEnd(size, this.oldEndDeco, src, dst);
         }

      }

      protected Shape createLineEnd(int size, int style, Point src, Point dst) {
         if (style == 100) {
            Point p0 = SpecialEdgeView.this.getPoint(0);
            boolean begin = false;
            Rectangle drawRect = p0.equals(dst) ? SpecialEdgeView.this.beginRect : SpecialEdgeView.this.endRect;
            if (drawRect == null) {
               return null;
            } else {
               if (drawRect == SpecialEdgeView.this.beginRect) {
                  begin = true;
               }

               int rHeight = drawRect.height / 2;
               int rWidth = drawRect.width / 2;
               int dx = src.x - dst.x;
               int dy = src.y - dst.y;
               if (Math.abs(dx) > Math.abs(dy)) {
                  if (dx > 0) {
                     drawRect.x = dst.x;
                     drawRect.y = dst.y - rHeight;
                     dst.x += drawRect.width;
                     this.initOldShapes(begin, size, src, dst);
                     return drawRect;
                  }

                  if (dx <= 0) {
                     drawRect.x = dst.x - drawRect.width;
                     drawRect.y = dst.y - rHeight;
                     dst.x -= drawRect.width;
                     this.initOldShapes(begin, size, src, dst);
                     return drawRect;
                  }
               } else if (Math.abs(dx) <= Math.abs(dy)) {
                  if (dy > 0) {
                     drawRect.x = dst.x - rWidth;
                     drawRect.y = dst.y;
                     dst.y += drawRect.height;
                     this.initOldShapes(begin, size, src, dst);
                     return drawRect;
                  }

                  if (dy <= 0) {
                     drawRect.x = dst.x - rWidth;
                     drawRect.y = dst.y - drawRect.height;
                     dst.y -= drawRect.height;
                     this.initOldShapes(begin, size, src, dst);
                     return drawRect;
                  }
               }

               return null;
            }
         } else {
            return super.createLineEnd(size, style, src, dst);
         }
      }

      private void initDecos() {
         SpecialEdge edge = (SpecialEdge)SpecialEdgeView.this.cell;
         Classifier beginCls = (Classifier)((DefaultGraphCell)((SpecialPort)edge.getSource()).getParent()).getUserObject();
         Classifier endCls = (Classifier)((DefaultGraphCell)((SpecialPort)edge.getTarget()).getParent()).getUserObject();
         Association asoc = (Association)edge.getUserObject();
         Enumeration asocEnds = asoc.getConnectionList();

         while(asocEnds.hasMoreElements()) {
            AssociationEnd end = (AssociationEnd)asocEnds.nextElement();
            ArrayList quals = edge.getQualifiers(end);
            if (quals.size() > 0) {
               if (end.getParticipant() == endCls) {
                  this.beginDeco = 100;
                  this.beginFill = false;
               } else {
                  this.endDeco = 100;
                  this.endFill = false;
               }
            }
         }

      }

      protected Shape createShape() {
         SpecialEdge edge = (SpecialEdge)SpecialEdgeView.this.cell;
         this.oldBeginDeco = this.beginDeco;
         this.oldEndDeco = this.endDeco;
         if (edge.hasQualifiers()) {
            this.initDecos();
         }

         return super.createShape();
      }

      public void paint(Graphics g) {
         if (((SpecialEdge)SpecialEdgeView.this.cell).hasQualifiers()) {
            this.oldBeginDeco = this.beginDeco;
            this.oldEndDeco = this.endDeco;
            this.oldBeginShape = null;
            this.oldEndShape = null;
            boolean auxBeginFill = this.beginFill;
            boolean auxEndFill = this.endFill;
            this.initQualifierArea(g);
            super.paint(g);
            this.beginFill = auxBeginFill;
            this.endFill = auxEndFill;
            this.drawQualifiers(g);
         } else {
            super.paint(g);
         }

         this.label = this.getLabelForEdge();
         int middle = this.view.getPointCount() / 2;
         Point p1 = this.view.getPoint(middle - 1);
         Point p2 = this.view.getPoint(middle);
         Point drawP = new Point();
         if (this.view.getPointCount() % 2 == 0) {
            drawP.x = (p1.x + p2.x) / 2;
            drawP.y = (p1.y + p2.y) / 2;
         }

         if (this.label != null) {
            this.paintLabel(g, this.label);
         }

      }

      private void drawQualifiers(Graphics g) {
         Graphics2D g2 = (Graphics2D)g;
         if (this.oldBeginShape != null) {
            g2.draw(this.oldBeginShape);
            if (this.beginFill) {
               g2.fill(this.oldBeginShape);
            }
         }

         if (this.oldEndShape != null) {
            g2.draw(this.oldEndShape);
            if (this.endFill) {
               g2.fill(this.oldEndShape);
            }
         }

         int fHeight = g.getFontMetrics().getHeight();
         int posY = 3 + fHeight;
         int posXx = 3;
         int i;
         String text;
         int posX;
         if (SpecialEdgeView.this.beginRect != null && SpecialEdgeView.beginQuals.size() > 0) {
            posX = posXx + SpecialEdgeView.this.beginRect.x;
            posY += SpecialEdgeView.this.beginRect.y;

            for(i = 0; i < SpecialEdgeView.beginQuals.size(); ++i) {
               text = (String)SpecialEdgeView.beginQuals.get(i);
               g.drawString(text, posX, posY);
               posY += 3 + fHeight;
            }
         }

         posY = 3 + fHeight;
         posXx = 3;
         if (SpecialEdgeView.this.endRect != null && SpecialEdgeView.endQuals.size() > 0) {
            posX = posXx + SpecialEdgeView.this.endRect.x;
            posY += SpecialEdgeView.this.endRect.y;

            for(i = 0; i < SpecialEdgeView.endQuals.size(); ++i) {
               text = (String)SpecialEdgeView.endQuals.get(i);
               g.drawString(text, posX, posY);
               posY += 3 + fHeight;
            }
         }

      }

      public Rectangle getLabelBounds(EdgeView view) {
         Point p = this.getLabelPosition(this.view);
         Dimension d = this.getLabelSize(this.view);
         if (p != null && d != null) {
            p.translate(-d.width / 2, -d.height / 2);
            return new Rectangle(p.x, p.y, d.width + 1, d.height + 1);
         } else {
            return null;
         }
      }

      public Dimension getLabelSize(EdgeView view) {
         if (this.label != null && this.label.toString().length() > 0) {
            this.fontGraphics.setFont(GraphConstants.getFont(view.getAttributes()));
            this.metrics = ((Graphics2D)this.fontGraphics).getFontMetrics();
            int sw = this.metrics.stringWidth(this.label.toString());
            int sh = this.metrics.getHeight();
            return new Dimension(sw, sh);
         } else {
            return null;
         }
      }

      protected void paintLabel(Graphics g, String label) {
         Point p = this.getLabelPosition(this.view);
         if (p != null && label != null && label.length() > 0) {
            Graphics2D g2 = (Graphics2D)g;
            int sw = this.metrics.stringWidth(label);
            int sh = this.metrics.getHeight();
            if (this.isOpaque()) {
               g.setColor(this.getBackground());
               g.fillRect(p.x - sw / 2 - 1, p.y - sh / 2 - 1, sw + 2, sh + 2);
            }

            if (this.borderColor != null) {
               g.setColor(this.borderColor);
               g.drawRect(p.x - sw / 2 - 1, p.y - sh / 2 - 1, sw + 2, sh + 2);
            }

            g.setColor(this.fontColor);
            int middle = this.view.getPointCount() / 2;
            Point p1 = this.view.getPoint(middle - 1);
            Point p2 = this.view.getPoint(middle);
            Point drawP = new Point();
            if (this.view.getPointCount() % 2 == 0) {
               drawP.x = (p1.x + p2.x) / 2;
               drawP.y = (p1.y + p2.y) / 2;
            } else {
               drawP = p2;
            }

            ((Graphics2D)g).drawString(label, drawP.x - sw / 2, drawP.y - sh / 2);
         }

      }

      private String getLabelForEdge() {
         String result = null;
         Object usrObj = ((SpecialEdge)this.view.getCell()).getUserObject();
         if (usrObj instanceof Include) {
            result = "<<include>>";
         } else if (usrObj instanceof Extend) {
            result = "<<extend>>";
         }

         return result;
      }
   }
}
