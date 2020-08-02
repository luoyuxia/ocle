package ro.ubbcluj.lci.gui.diagrams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.PropertyResourceBundle;
import java.util.StringTokenizer;
import javax.swing.JPopupMenu;

public abstract class GProperty implements Serializable {
   protected static PropertyResourceBundle res = null;

   public GProperty() {
   }

   public static GProperty createProperty(Object cell) {
      try {
         if (res == null) {
            res = new PropertyResourceBundle((GProperty.class).getResourceAsStream("gproperty_en.properties"));
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      if (cell instanceof ActorCell) {
         return new ActorGProperty(getStringsForItem("MENIU_ACTOR"));
      } else if (!(cell instanceof PackageCell) && !(cell instanceof UseCaseCell)) {
         if (cell instanceof ClassCell) {
            return new ClassGProperty(getStringsForItem("MENIU_CLASA"));
         } else {
            return cell instanceof ObjectCell ? new ObjectGProperty(getStringsForItem("MENIU_OBIECT")) : null;
         }
      } else {
         return new PackageGProperty(getStringsForItem("MENIU_PACHET"));
      }
   }

   protected void repaintCell(DiagramListener listener) {
      listener.diagram.getGraph().repaint();
   }

   protected static ArrayList getStringsForItem(String item) {
      ArrayList strings = new ArrayList();
      String keys = res.getString(item);
      StringTokenizer st = new StringTokenizer(keys, "|");

      while(st.hasMoreTokens()) {
         strings.add(st.nextToken().trim());
      }

      return strings;
   }

   public abstract JPopupMenu getPopup();
}
