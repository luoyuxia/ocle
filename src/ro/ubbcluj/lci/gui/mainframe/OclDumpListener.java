package ro.ubbcluj.lci.gui.mainframe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import ro.ubbcluj.lci.ocl.OclCompiler;
import ro.ubbcluj.lci.ocl.OclUtil;

public class OclDumpListener implements OclCompiler.DumpListener {
   public OclDumpListener() {
   }

   public void write(List list) {
      ArrayList rez = new ArrayList();
      Iterator it = list.iterator();

      while(it.hasNext()) {
         Object obj = it.next();
         rez.addAll(OclUtil.getCollection(obj));
      }

      GMainFrame.getMainFrame().updateOCLLog((Collection)rez);
   }
}
