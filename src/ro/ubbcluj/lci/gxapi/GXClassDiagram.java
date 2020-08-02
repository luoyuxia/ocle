package ro.ubbcluj.lci.gxapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import ro.ubbcluj.lci.gxapi.filters.GXClassFilter;

public class GXClassDiagram {
   protected String name;
   protected Object owner;
   protected ArrayList classes = new ArrayList();
   protected ArrayList packages = new ArrayList();
   protected ArrayList classViews = new ArrayList();
   protected ArrayList packageViews = new ArrayList();
   protected ArrayList relationViews = new ArrayList();
   protected ArrayList flat = new ArrayList();
   protected ArrayList cells = new ArrayList();
   protected GXClassFilter globalFilter = new GXClassFilter();
   protected GXConnectionSet connectionSet;
   protected GXHashtable viewAttributes;
   protected boolean isLocked;
   protected boolean gridVisible;
   protected String background = new String("java.awt.Color[r=255,g=255,b=255]");

   public GXClassDiagram() {
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setOwner(Object owner) {
      this.owner = owner;
   }

   public Object getOwner() {
      return this.owner;
   }

   public void addFlat(GXDefaultGraphCell gxdgc) {
      this.flat.add(gxdgc);
   }

   public Enumeration getFlatList() {
      return Collections.enumeration(this.flat);
   }

   public void setConnectionSet(GXConnectionSet gxcs) {
      this.connectionSet = gxcs;
   }

   public GXConnectionSet getConnectionSet() {
      return this.connectionSet;
   }

   public void addCells(GXDefaultGraphCell gxdgc) {
      this.cells.add(gxdgc);
   }

   public Enumeration getCellsList() {
      return Collections.enumeration(this.cells);
   }

   public ArrayList getCollectionCells() {
      return this.cells;
   }

   public void addClasses(Object c) {
      this.classes.add(c);
   }

   public Enumeration getClassesList() {
      return Collections.enumeration(this.classes);
   }

   public ArrayList getCollectionClasses() {
      return this.classes;
   }

   public void setClasses(ArrayList c) {
      this.classes = c;
   }

   public void addPackages(Object p) {
      this.packages.add(p);
   }

   public Enumeration getPackagesList() {
      return Collections.enumeration(this.packages);
   }

   public ArrayList getCollectionPackages() {
      return this.packages;
   }

   public void setPackages(ArrayList p) {
      this.packages = p;
   }

   public void addClassViews(Object cv) {
      this.classViews.add(cv);
   }

   public Enumeration getClassViewsList() {
      return Collections.enumeration(this.classViews);
   }

   public void addPackageViews(Object pv) {
      this.packageViews.add(pv);
   }

   public Enumeration getPackageViewsList() {
      return Collections.enumeration(this.packageViews);
   }

   public void addRelationViews(Object rv) {
      this.relationViews.add(rv);
   }

   public Enumeration getRelationViewsList() {
      return Collections.enumeration(this.relationViews);
   }

   public void setViewAttributes(GXHashtable viewAttributes) {
      this.viewAttributes = viewAttributes;
   }

   public GXHashtable getViewAttributes() {
      return this.viewAttributes;
   }

   public void setGlobalFilter(GXClassFilter globalFilter) {
      this.globalFilter = globalFilter;
   }

   public GXClassFilter getGlobalFilter() {
      return this.globalFilter;
   }

   public void setIsLocked(boolean isLocked) {
      this.isLocked = isLocked;
   }

   public boolean getIsLocked() {
      return this.isLocked;
   }

   public void setGridVisible(boolean gridVisible) {
      this.gridVisible = gridVisible;
   }

   public boolean getGridVisible() {
      return this.gridVisible;
   }

   public void setBackground(String background) {
      this.background = background;
   }

   public String getBackground() {
      return this.background;
   }
}
