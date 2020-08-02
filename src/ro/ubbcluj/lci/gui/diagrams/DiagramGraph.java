package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import com.jgraph.graph.BasicMarqueeHandler;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellView;
import com.jgraph.graph.GraphModel;
import com.jgraph.graph.GraphView;

public class DiagramGraph extends JGraph {
   public DiagramGraph() {
      this((GraphModel)null);
   }

   public DiagramGraph(GraphModel model) {
      this(model, (GraphView)null);
   }

   public DiagramGraph(GraphModel model, GraphView view) {
      this(model, view, new BasicMarqueeHandler());
   }

   public DiagramGraph(GraphModel model, BasicMarqueeHandler mh) {
      this(model, (GraphView)null, mh);
   }

   public DiagramGraph(GraphModel model, GraphView view, BasicMarqueeHandler mh) {
      super(model, view, mh);
   }

   public CellView createView(Object cell, CellMapper map) {
      CellView view = null;
      if (cell instanceof ClassCell) {
          view = new ClassView(cell, this, map);
         map.putMapping(cell, view);
         view.refresh(true);
         view.update();
         return view;
      } else if (cell instanceof ObjectCell) {
          view = new ObjectView(cell, this, map);
         map.putMapping(cell, view);
         view.refresh(true);
         view.update();
         return view;
      } else if (cell instanceof PackageCell) {
          view = new PackageView(cell, this, map);
         map.putMapping(cell, view);
         view.refresh(true);
         view.update();
         return view;
      } else if (cell instanceof ActorCell) {
          view = new ActorView(cell, this, map);
         map.putMapping(cell, view);
         view.refresh(true);
         view.update();
         return view;
      } else if (cell instanceof UseCaseCell) {
          view = new UseCaseView(cell, this, map);
         map.putMapping(cell, view);
         view.refresh(true);
         view.update();
         return view;
      } else if (cell instanceof SpecialPort) {
          view = new SpecialPortView(cell, this, map);
         map.putMapping(cell, view);
         view.refresh(true);
         view.update();
         return view;
      } else if (cell instanceof SpecialEdge) {
         view = new SpecialEdgeView(cell, this, map);
         map.putMapping(cell, view);
         view.refresh(true);
         view.update();
         return view;
      } else {
         return super.createView(cell, map);
      }
   }

   public String convertValueToString(Object value) {
      return value instanceof SpecialEdge ? null : super.convertValueToString(value);
   }
}
