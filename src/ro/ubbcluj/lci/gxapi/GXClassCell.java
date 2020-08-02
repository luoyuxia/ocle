package ro.ubbcluj.lci.gxapi;

import java.util.Hashtable;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.diagrams.ClassCell;
import ro.ubbcluj.lci.gui.diagrams.ClassGProperty;
import ro.ubbcluj.lci.gui.diagrams.GProperty;
import ro.ubbcluj.lci.gui.diagrams.filters.ClassFilter;
import ro.ubbcluj.lci.gxapi.filters.GXClassFilter;

public class GXClassCell extends GXDefaultGraphCell {
   protected boolean isFiltered;
   protected boolean hasAutoR;
   protected boolean isInterface;
   protected boolean showPackage;
   protected GXGProperty property;
   protected GXClassFilter filter;

   public GXClassCell() {
      this.filter = new GXClassFilter();
   }

   public GXClassCell(ClassCell cc) {
      this.allowsChildren = cc.getAllowsChildren();
      this.userObject = cc.getUserObject();
      this.isInterface = cc.getIsInterface();
      this.isFiltered = cc.isFiltered();
      this.hasAutoR = cc.hasAutoRelation();
   }

   public void setIsInterface(boolean isInterface) {
      this.isInterface = isInterface;
   }

   public boolean getIsInterface() {
      return this.isInterface;
   }

   public void setIsFiltered(boolean isFiltered) {
      this.isFiltered = isFiltered;
   }

   public boolean getIsFiltered() {
      return this.isFiltered;
   }

   public void setHasAutoR(boolean hasAutoR) {
      this.hasAutoR = hasAutoR;
   }

   public boolean getHasAutoR() {
      return this.hasAutoR;
   }

   public void setShowPackage(boolean showPackage) {
      this.showPackage = showPackage;
   }

   public boolean getShowPackage() {
      return this.showPackage;
   }

   public void setProperty(GXGProperty property) {
      this.property = property;
   }

   public GXGProperty getProperty() {
      return this.property;
   }

   public void setFilter(GXClassFilter filter) {
      this.filter = filter;
   }

   public GXClassFilter getFilter() {
      return this.filter;
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.copy(dmtn, clones);
      this.isInterface = ((ClassCell)dmtn).getIsInterface();
      this.isFiltered = ((ClassCell)dmtn).isFiltered();
      this.hasAutoR = ((ClassCell)dmtn).hasAutoRelation();
      this.showPackage = ((ClassCell)dmtn).getShowPackage();
      this.property = new GXClassGProperty();
      this.property.copy((ClassGProperty)((ClassCell)dmtn).getProperty());
      this.filter = new GXClassFilter();
      this.filter.copy((ClassFilter)((ClassCell)dmtn).getFilter());
   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.extractData(dmtn, clones);
      ((ClassCell)dmtn).setIsInterface(this.isInterface);
      ((ClassCell)dmtn).setFiltered(this.isFiltered);
      ((ClassCell)dmtn).setAutoRelation(this.hasAutoR);
      ((ClassCell)dmtn).setShowPackage(this.showPackage);
      GProperty gp = GProperty.createProperty((ClassCell)dmtn);
      this.property.extractData(gp);
      ((ClassCell)dmtn).setProperty(gp);
      ClassFilter cf = new ClassFilter();
      this.filter.extractData(cf);
      ((ClassCell)dmtn).setFilter(cf);
   }
}
