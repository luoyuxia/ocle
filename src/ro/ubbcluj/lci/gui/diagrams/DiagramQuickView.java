package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.ParentMap;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.ToolTipManager;
import ro.ubbcluj.lci.gui.mainframe.GApplication;

public class DiagramQuickView extends JDialog {
   private DiagramGraph graph;
   double scale;
   private DiagramGraph original_graph;
   private static DiagramQuickView.DiagramQuickViewListener listener;
   private static DiagramQuickView instance = null;

   public static DiagramQuickView getInstance(DiagramGraph g) {
      if (instance == null) {
         instance = new DiagramQuickView(g);
      } else {
         instance.initView(g);
      }

      return instance;
   }

   protected void initView(DiagramGraph g) {
      this.setLocationRelativeTo(g);
      this.scale = 0.3D;
      this.original_graph = g;
      Object[] cells = g.getView().order(g.getRoots());
      Object[] flat = DefaultGraphModel.getDescendants(g.getModel(), cells).toArray();
      ConnectionSet cs = ConnectionSet.create(g.getModel(), flat, false);
      Map attrib = GraphConstants.createPropertyMap(flat, g.getView());
      this.graph.setModel(new DefaultGraphModel());
      this.graph.getModel().insert(cells, cs, (ParentMap)null, attrib);
      Object[] selected = g.getSelectionCells();

      for(int i = 0; i < selected.length; ++i) {
         g.removeSelectionCell(selected[i]);
      }

      this.graph.setScale(this.scale);
      this.graph.setGridEnabled(g.isGridEnabled());
      this.graph.setGridVisible(g.isGridVisible());
      this.graph.setGridColor(g.getGridColor());
      this.graph.setBackground(g.getBackground());
      this.getContentPane().add(this.graph);

      for(Dimension dim = this.getContentPane().getPreferredSize(); dim.getWidth() > 299.0D || dim.getHeight() > 299.0D; dim = this.getContentPane().getPreferredSize()) {
         this.getContentPane().removeAll();
         if (dim.height > 299 && dim.width < 299) {
            this.scale /= 2.0D;
         } else {
            this.scale /= 1.1D;
         }

         this.graph.setScale(this.scale);
         this.getContentPane().add(this.graph);
      }

      this.graph.addMouseListener(listener);
      this.graph.addMouseMotionListener(listener);
   }

   private DiagramQuickView(DiagramGraph g) {
      super(GApplication.frame);
      this.setTitle("Quick View");
      this.setModal(true);
      this.setResizable(false);
      this.setDefaultCloseOperation(2);
      this.setSize(300, 300);
      this.graph = new DiagramGraph();
      this.graph.setMoveable(false);
      this.graph.setSizeable(false);
      this.graph.setEditable(false);
      this.graph.setCloneable(false);
      ToolTipManager.sharedInstance().registerComponent(this.graph);
      listener = new DiagramQuickView.DiagramQuickViewListener();
      this.initView(g);
   }

   public String getToolTipText(MouseEvent e) {
      if (e != null) {
         Object c = this.graph.getFirstCellForLocation(e.getX(), e.getY());
         if (c != null && c instanceof DefaultGraphCell) {
            return ((DefaultGraphCell)c).getUserObject().toString();
         }
      }

      return null;
   }

   class DiagramQuickViewListener extends MouseAdapter implements MouseMotionListener {
      DiagramQuickViewListener() {
      }

      public void mousePressed(MouseEvent e) {
         DiagramQuickView.this.graph.setCursor(new Cursor(12));
         Object o = DiagramQuickView.this.graph.getFirstCellForLocation(e.getX(), e.getY());
         if (o != null) {
            DiagramQuickView.this.original_graph.scrollCellToVisible(o);
         }

      }

      public void mouseMoved(MouseEvent e) {
         Object o = DiagramQuickView.this.graph.getFirstCellForLocation(e.getX(), e.getY());
         if (o != null) {
            DiagramQuickView.this.graph.setCursor(new Cursor(12));
         } else {
            DiagramQuickView.this.graph.setCursor(new Cursor(0));
         }

      }

      public void mouseDragged(MouseEvent e) {
      }
   }
}
