package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import java.awt.Point;
import java.util.ArrayList;

public interface Diagram {
   void addElement(Object var1, Point var2, boolean var3);

   void addRelation(Object var1, Object var2);

   void removeItem(Object var1);

   void updateItem(Object var1);

   boolean contains(Object var1);

   void setActionKind(int var1);

   int getActionKind();

   ArrayList getSelected();

   void setSelected(Object var1);

   JGraph getGraph();

   void lockItem(Object var1, boolean var2);

   void addDiagramListener(DiagramListener var1);

   void zoomDefault();

   void zoomIn();

   void zoomOut();

   void undo();

   void redo();
}
