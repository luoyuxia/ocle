package ro.ubbcluj.lci.gui.browser;

import ro.ubbcluj.lci.gui.browser.BTree.CustomFilter;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.utils.ModelEvent;

public class MMBrowser extends GBrowser {
   public MMBrowser(GUMLModel gmodel) {
      super(gmodel, new CustomFilter());
   }

   protected void installPopup() {
      this.pum = new MMPopupMenu(this);
      this.browser_tree.add(this.pum);
   }

   public void modelChanged(ModelEvent evt) {
      if (evt.getOperation() == 0) {
         this.selectInBrowser(evt.getSubject());
      }
   }
}
