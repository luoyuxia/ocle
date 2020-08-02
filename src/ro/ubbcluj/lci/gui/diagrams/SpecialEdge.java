package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import com.jgraph.graph.CellView;
import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.ParentMap;
import com.jgraph.graph.PortView;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;

public class SpecialEdge extends DefaultEdge {
   ArrayList labels;
   transient String beginMul;
   transient String beginName;
   transient String endMul;
   transient String endName;
   private boolean isRouted;
   static final int QUALIFIER_END = 100;

   public SpecialEdge() {
      this((Object)null);
   }

   public SpecialEdge(Object userObject) {
      super(userObject);
      this.labels = null;
      this.beginMul = null;
      this.beginName = null;
      this.endMul = null;
      this.endName = null;
      this.isRouted = false;
      this.setUserObject(userObject);
      this.setAllowsChildren(true);
   }

   private ArrayList calcCoords(JGraph graph) {
      ArrayList result = new ArrayList();
      Point beginMulLoc = new Point();
      Point beginRoleLoc = new Point();
      Point endMulLoc = new Point();
      Point endRoleLoc = new Point();
      Rectangle beginMulD = new Rectangle();
      Rectangle beginRoleD = new Rectangle();
      Rectangle endMulD = new Rectangle();
      Rectangle endRoleD = new Rectangle();
      if (!this.labels.isEmpty()) {
         DefaultGraphCell beginMul = (DefaultGraphCell)this.labels.get(0);
         CellView cview = graph.getView().getMapping(beginMul, false);
         if (cview != null) {
            beginMulD = GraphConstants.getBounds(cview.getAttributes());
         }

         DefaultGraphCell beginRole = (DefaultGraphCell)this.labels.get(1);
         cview = graph.getView().getMapping(beginRole, false);
         if (cview != null) {
            beginRoleD = GraphConstants.getBounds(cview.getAttributes());
         }

         DefaultGraphCell endMul = (DefaultGraphCell)this.labels.get(2);
         cview = graph.getView().getMapping(endMul, false);
         if (cview != null) {
            endMulD = GraphConstants.getBounds(cview.getAttributes());
         }

         DefaultGraphCell endRole = (DefaultGraphCell)this.labels.get(3);
         cview = graph.getView().getMapping(endRole, false);
         if (cview != null) {
            endRoleD = GraphConstants.getBounds(cview.getAttributes());
         }
      }

      int dist = 10;
      SpecialEdgeView view = (SpecialEdgeView)graph.getView().getMapping(this, false);

      try {
         SpecialPort begin = (SpecialPort)this.getSource();
         SpecialPortView beginV = (SpecialPortView)graph.getView().getMapping(begin, false);
         SpecialPort end = (SpecialPort)this.getTarget();
         SpecialPortView endV = (SpecialPortView)graph.getView().getMapping(end, false);
         Point beginL = beginV.getLocation(view);
         Point endL = endV.getLocation(view);
         Point p1 = view.getPoint(1);
         Point pn = view.getPoint(view.getPointCount() - 2);
         int dx1 = p1.x - beginL.x;
         int dy1 = p1.y - beginL.y;
         if (Math.abs(dx1) >= Math.abs(dy1)) {
            if (dx1 >= 0) {
               beginMulLoc.x = beginL.x + dist;
               beginMulLoc.y = beginL.y + dist;
               beginRoleLoc.x = beginL.x + dist;
               beginRoleLoc.y = beginL.y - dist - beginRoleD.height;
            } else {
               beginMulLoc.x = beginL.x - dist - beginMulD.width;
               beginMulLoc.y = beginL.y + dist;
               beginRoleLoc.x = beginL.x - dist - beginRoleD.width;
               beginRoleLoc.y = beginL.y - dist - beginRoleD.height;
            }
         } else if (dy1 >= 0) {
            beginMulLoc.x = beginL.x + dist;
            beginMulLoc.y = beginL.y + dist;
            beginRoleLoc.x = beginL.x - dist - beginRoleD.width;
            beginRoleLoc.y = beginL.y + dist;
         } else {
            beginMulLoc.x = beginL.x + dist;
            beginMulLoc.y = beginL.y - dist - beginMulD.height;
            beginRoleLoc.x = beginL.x - dist - beginRoleD.width;
            beginRoleLoc.y = beginL.y - dist - beginRoleD.height;
         }

         int dxn = endL.x - pn.x;
         int dyn = endL.y - pn.y;
         if (Math.abs(dxn) >= Math.abs(dyn)) {
            if (dxn >= 0) {
               endMulLoc.x = endL.x - dist - endMulD.width;
               endMulLoc.y = endL.y + dist;
               endRoleLoc.x = endL.x - dist - endRoleD.width;
               endRoleLoc.y = endL.y - dist - endRoleD.height;
            } else {
               endMulLoc.x = endL.x + dist;
               endMulLoc.y = endL.y + dist;
               endRoleLoc.x = endL.x + dist;
               endRoleLoc.y = endL.y - dist - endRoleD.height;
            }
         } else if (dyn >= 0) {
            endMulLoc.x = endL.x + dist;
            endMulLoc.y = endL.y - dist - endMulD.height;
            endRoleLoc.x = endL.x - dist - endRoleD.width;
            endRoleLoc.y = endL.y - dist - endRoleD.height;
         } else {
            endMulLoc.x = endL.x + dist;
            endMulLoc.y = endL.y + dist;
            endRoleLoc.x = endL.x - dist - endRoleD.width;
            endRoleLoc.y = endL.y + dist;
         }
      } catch (Exception var25) {
      }

      result.add(beginMulLoc);
      result.add(beginRoleLoc);
      result.add(endMulLoc);
      result.add(endRoleLoc);
      return result;
   }

   void regroupLabels(JGraph graph) {
      if (this.labels != null && this.labels.size() != 0) {
         Map attrs = GraphConstants.createMap();
         Rectangle bounds = null;
         CellView cview = null;
         ArrayList locs = this.calcCoords(graph);
         Point beginMulLoc = (Point)locs.get(0);
         Point beginRoleLoc = (Point)locs.get(1);
         Point endMulLoc = (Point)locs.get(2);
         Point endRoleLoc = (Point)locs.get(3);
         DefaultGraphCell beginMul = (DefaultGraphCell)this.labels.get(0);
         cview = graph.getView().getMapping(beginMul, false);
         if (cview != null) {
            attrs = cview.getAttributes();
            bounds = GraphConstants.getBounds(attrs);
            bounds.x = beginMulLoc.x;
            bounds.y = beginMulLoc.y;
            GraphConstants.setBounds(attrs, bounds);
            cview.setAttributes(attrs);
            CellView[] views = new CellView[]{cview};
            graph.getView().toFront(views);
         }

         DefaultGraphCell beginRol = (DefaultGraphCell)this.labels.get(1);
         cview = graph.getView().getMapping(beginRol, false);
         if (cview != null) {
            attrs = cview.getAttributes();
            bounds = GraphConstants.getBounds(attrs);
            bounds.x = beginRoleLoc.x;
            bounds.y = beginRoleLoc.y;
            GraphConstants.setBounds(attrs, bounds);
            cview.setAttributes(attrs);
            CellView[] views = new CellView[]{cview};
            graph.getView().toFront(views);
         }

         DefaultGraphCell endMul = (DefaultGraphCell)this.labels.get(2);
         cview = graph.getView().getMapping(endMul, false);
         if (cview != null) {
            attrs = cview.getAttributes();
            bounds = GraphConstants.getBounds(attrs);
            bounds.x = endMulLoc.x;
            bounds.y = endMulLoc.y;
            GraphConstants.setBounds(attrs, bounds);
            cview.setAttributes(attrs);
            CellView[] views = new CellView[]{cview};
            graph.getView().toFront(views);
         }

         DefaultGraphCell endRol = (DefaultGraphCell)this.labels.get(3);
         cview = graph.getView().getMapping(endRol, false);
         if (cview != null) {
            attrs = cview.getAttributes();
            bounds = GraphConstants.getBounds(attrs);
            bounds.x = endRoleLoc.x;
            bounds.y = endRoleLoc.y;
            GraphConstants.setBounds(attrs, bounds);
            cview.setAttributes(attrs);
            CellView[] views = new CellView[]{cview};
            graph.getView().toFront(views);
         }

         graph.repaint();
      }
   }

   void higlightLabel(JGraph graph, boolean highlightBegin, boolean highlightEnd, boolean reset) {
      if (this.labels != null && this.labels.size() != 0) {
         Color border = graph.getBackground();
         if (!reset) {
            border = Color.red;
         }

         DefaultGraphCell beginMultC = (DefaultGraphCell)this.labels.get(0);
         CellView beginMulV = graph.getView().getMapping(beginMultC, false);
         DefaultGraphCell beginRoleC = (DefaultGraphCell)this.labels.get(1);
         CellView beginRoleV = graph.getView().getMapping(beginRoleC, false);
         DefaultGraphCell endMultC = (DefaultGraphCell)this.labels.get(2);
         CellView endMulV = graph.getView().getMapping(endMultC, false);
         DefaultGraphCell endRoleC = (DefaultGraphCell)this.labels.get(3);
         CellView endRoleV = graph.getView().getMapping(endRoleC, false);
         Map attrs;
         Color oldColor;
         CellView[] endViews;
         if (highlightBegin) {
            if (beginMulV != null) {
               attrs = beginMulV.getAttributes();
               oldColor = GraphConstants.getBorderColor(beginMulV.getAttributes());
               if (oldColor != border) {
                  GraphConstants.setBorderColor(attrs, border);
                  beginMulV.setAttributes(attrs);
                  endViews = new CellView[]{beginMulV};
                  graph.getView().toFront(endViews);
               }
            }

            if (beginRoleV != null) {
               attrs = beginRoleV.getAttributes();
               oldColor = GraphConstants.getBorderColor(beginRoleV.getAttributes());
               if (oldColor != border) {
                  GraphConstants.setBorderColor(attrs, border);
                  beginRoleV.setAttributes(attrs);
                  endViews = new CellView[]{beginRoleV};
                  graph.getView().toFront(endViews);
               }
            }
         }

         if (highlightEnd) {
            if (endMulV != null) {
               attrs = endMulV.getAttributes();
               oldColor = GraphConstants.getBorderColor(endMulV.getAttributes());
               if (oldColor != border) {
                  GraphConstants.setBorderColor(attrs, border);
                  endMulV.setAttributes(attrs);
                  endViews = new CellView[]{endMulV};
                  graph.getView().toFront(endViews);
               }
            }

            if (endRoleV != null) {
               attrs = endRoleV.getAttributes();
               oldColor = GraphConstants.getBorderColor(endRoleV.getAttributes());
               if (oldColor != border) {
                  GraphConstants.setBorderColor(attrs, border);
                  endRoleV.setAttributes(attrs);
                  endViews = new CellView[]{endRoleV};
                  graph.getView().toFront(endViews);
               }
            }
         }

      }
   }

   private void initLabel(JGraph graph, String text, Point loc) {
      DefaultGraphCell cell;
      if (text != null) {
         cell = new DefaultGraphCell(text);
      } else {
         cell = new DefaultGraphCell("");
      }

      int width = text == null ? 0 : graph.getGraphics().getFontMetrics().stringWidth(text);
      int height = graph.getGraphics().getFontMetrics().getHeight();
      int space = 5;
      Map map = GraphConstants.createMap();
      Map atribMap = GraphConstants.createMap();
      Point pt = graph.snap(loc);
      Rectangle bounds = new Rectangle(pt.x, pt.y, width + space, height + space);
      GraphConstants.setBounds(map, bounds);
      GraphConstants.setEditable(map, true);
      GraphConstants.setOpaque(map, false);
      atribMap.put(cell, map);
      if (text != null) {
         Object[] insert = new Object[]{cell};
         graph.getModel().insert(insert, (ConnectionSet)null, (ParentMap)null, atribMap);
      }

      this.labels.add(cell);
   }

   public ArrayList getLabels() {
      return this.labels;
   }

   public void setLabels(ArrayList l) {
      this.labels = l;
   }

   public void setRouted(boolean routed) {
      this.isRouted = routed;
   }

   public boolean getRouted() {
      return this.isRouted;
   }

   void getStringForRelation(DiagramGraph graph) {
      String result = null;
      if (this.userObject instanceof Association) {
         if (this.labels == null) {
            this.labels = new ArrayList();
         }

         SpecialEdgeView view = (SpecialEdgeView)graph.getView().getMapping(this, false);
         Association asoc = (Association)this.userObject;
         SpecialPort begin = (SpecialPort)this.getSource();
         SpecialPortView beginV = (SpecialPortView)graph.getView().getMapping(begin, false);
         SpecialPort end = (SpecialPort)this.getTarget();
         SpecialPortView endV = (SpecialPortView)graph.getView().getMapping(end, false);
         Point beginL = beginV.getLocation(view);
         Point endL = endV.getLocation(view);
         boolean reverse = beginL.x < endL.x;
         DefaultGraphCell beginCell = (DefaultGraphCell)begin.getParent();
         DefaultGraphCell endCell = (DefaultGraphCell)end.getParent();
         this.getInfoForAssociation(asoc, beginCell, endCell, reverse);
         if (this.labels.size() == 0) {
            this.initLabel(graph, this.beginMul, beginL);
            this.initLabel(graph, this.beginName, beginL);
            this.initLabel(graph, this.endMul, endL);
            this.initLabel(graph, this.endName, endL);
            this.regroupLabels(graph);
         } else {
            String[] text = new String[]{this.beginMul, this.beginName, this.endMul, this.endName};

            for(int i = 0; i < 4; ++i) {
               if (this.labels.get(i) != null) {
                  int width = graph.getFontMetrics(graph.getFont()).stringWidth(text[i]);
                  int space = 5;
                  DefaultGraphCell cell = (DefaultGraphCell)this.labels.get(i);
                  CellView cellView = graph.getView().getMapping(cell, false);
                  if (cellView != null) {
                     Map attrs = cellView.getAttributes();
                     Rectangle bounds = GraphConstants.getBounds(attrs);
                     bounds.width = width + space;
                     GraphConstants.setBounds(attrs, bounds);
                     cellView.setAttributes(attrs);
                  }

                  cell.setUserObject(text[i]);
               }
            }

            graph.repaint();
         }
      }

   }

   static String getVisibilityForAssociationEnd(AssociationEnd end) {
      switch(end.getVisibility()) {
      case 0:
         return "-";
      case 1:
      default:
         return "";
      case 2:
         return "#";
      case 3:
         return "+";
      }
   }

   private String getInfoForAssociation(Association asoc, DefaultGraphCell begin_c, DefaultGraphCell end_c, boolean reverse) {
      String str1 = null;
      String str2 = null;
      Enumeration enum = asoc.getConnectionList();

      while(true) {
         while(enum.hasMoreElements()) {
            AssociationEnd end = (AssociationEnd)enum.nextElement();
            Multiplicity mul;
            Enumeration en;
            MultiplicityRange range;
            String mult;
            int lower;
            int upper;
            if (end.getParticipant() == begin_c.getUserObject() && str1 == null) {
               mul = end.getMultiplicity();
               en = mul.getRangeList();
               range = (MultiplicityRange)en.nextElement();
               mult = "";
               if (range != null) {
                  lower = range.getLower();
                  upper = range.getUpper().intValue();
                  if (lower == upper) {
                     mult = Integer.toString(lower);
                  } else if (upper > 0) {
                     mult = Integer.toString(range.getLower()) + ".." + range.getUpper().toString();
                  } else {
                     mult = Integer.toString(range.getLower()) + "..*";
                  }
               }

               this.beginMul = mult;
               if (end.getName() != null && end.getName().trim().length() > 0) {
                  this.beginName = getVisibilityForAssociationEnd(end) + end.getName();
               }

               if (!reverse) {
                  str1 = mult;
                  if (end.getName() != null && end.getName().trim().length() > 0) {
                     str1 = getVisibilityForAssociationEnd(end) + end.getName() + "  " + mult;
                  }
               } else {
                  str1 = mult;
                  if (end.getName() != null && end.getName().trim().length() > 0) {
                     str1 = mult + " " + getVisibilityForAssociationEnd(end) + end.getName();
                  }
               }
            } else if (end.getParticipant() == end_c.getUserObject() && str2 == null) {
               mul = end.getMultiplicity();
               en = mul.getRangeList();
               range = (MultiplicityRange)en.nextElement();
               mult = "";
               if (range != null) {
                  lower = range.getLower();
                  upper = range.getUpper().intValue();
                  if (lower == upper) {
                     mult = Integer.toString(lower);
                  } else if (upper > 0) {
                     mult = Integer.toString(range.getLower()) + ".." + range.getUpper().toString();
                  } else {
                     mult = Integer.toString(range.getLower()) + "..*";
                  }
               }

               this.endMul = mult;
               if (end.getName() != null && end.getName().trim().length() > 0) {
                  this.endName = getVisibilityForAssociationEnd(end) + end.getName();
               }

               if (!reverse) {
                  str2 = mult;
                  if (end.getName() != null && end.getName().trim().length() > 0) {
                     str2 = mult + " " + getVisibilityForAssociationEnd(end) + end.getName();
                  }
               } else {
                  str2 = mult;
                  if (end.getName() != null && end.getName().trim().length() > 0) {
                     str2 = getVisibilityForAssociationEnd(end) + end.getName() + "  " + mult;
                  }
               }
            }
         }

         if (str1 != null && str2 != null) {
            if (!reverse) {
               return str2 + "                      " + str1;
            }

            return str1 + "                      " + str2;
         }

         return "";
      }
   }

   public void route(EdgeView edge, List points) {
      int n = points.size();
      Point from = edge.getPoint(0);
      if (edge.getSource() instanceof PortView) {
         from = edge.getSource().getLocation((EdgeView)null);
      }

      Point to = edge.getPoint(n - 1);
      if (edge.getTarget() instanceof PortView) {
         to = edge.getTarget().getLocation((EdgeView)null);
      }

      if (from != null && to != null) {
         Point[] routed = null;
         int i;
         int dy;
         int x2;
         if (edge.getSource() == edge.getTarget() && edge.getSource() != null) {
            Rectangle bounds = edge.getSource().getParentView().getBounds();
            dy = edge.getGraph().getGridSize();
            x2 = (int)(bounds.getWidth() / 3.0D);
            routed = new Point[]{new Point(bounds.x + x2, bounds.y + bounds.height), new Point(bounds.x + x2, bounds.y + bounds.height + dy), new Point(bounds.x + 2 * x2, bounds.y + bounds.height + dy), new Point(bounds.x + 2 * x2, bounds.y + bounds.height)};
         } else {
            i = Math.abs(from.x - to.x);
            dy = Math.abs(from.y - to.y);
            x2 = from.x + (to.x - from.x) / 2;
            int y2 = from.y + (to.y - from.y) / 2;
            routed = new Point[2];
            if (i > dy) {
               routed[0] = new Point(x2, from.y - 1);
               routed[1] = new Point(x2, to.y - 1);
            } else {
               routed[0] = new Point(from.x - 1, y2);
               routed[1] = new Point(to.x - 1, y2);
            }
         }

         for(i = 0; i < routed.length; ++i) {
            if (points.size() > i + 2) {
               points.set(i + 1, routed[i]);
            } else {
               points.add(i + 1, routed[i]);
            }
         }

         while(points.size() > routed.length + 2) {
            points.remove(points.size() - 2);
         }
      }

   }

   ArrayList getQualifiers(AssociationEnd end) {
      ArrayList result = new ArrayList();
      Enumeration quals = end.getQualifierList();

      while(quals.hasMoreElements()) {
         Attribute attr = (Attribute)quals.nextElement();
         String str = attr.getName() + ":" + attr.getType().getName();
         result.add(str);
      }

      return result;
   }

   boolean hasQualifiers() {
      Object usrObj = this.getUserObject();
      if (usrObj instanceof Association) {
         Enumeration asocEnds = ((Association)usrObj).getConnectionList();

         while(asocEnds.hasMoreElements()) {
            if (((AssociationEnd)asocEnds.nextElement()).getQualifierList().hasMoreElements()) {
               return true;
            }
         }
      }

      return false;
   }
}
