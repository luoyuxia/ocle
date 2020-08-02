package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Port;
import java.util.HashSet;

public class SpecialPort extends DefaultPort {
   public SpecialPort() {
      this((Object)null, (Port)null);
   }

   public SpecialPort(Object userObject) {
      this(userObject, (Port)null);
   }

   public SpecialPort(Object userObject, Port anchor) {
      this.userObject = userObject;
      this.setAllowsChildren(false);
      this.anchor = anchor;
   }

   public Object clone() {
      SpecialPort c = (SpecialPort)super.clone();
      c.edges = new HashSet();
      return c;
   }
}
