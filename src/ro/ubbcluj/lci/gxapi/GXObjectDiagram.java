package ro.ubbcluj.lci.gxapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class GXObjectDiagram {
   protected String name;
   protected Object owner;
   protected ArrayList objects = new ArrayList();
   protected ArrayList objectViews = new ArrayList();
   protected ArrayList relationViews = new ArrayList();
   protected ArrayList flat = new ArrayList();
   protected ArrayList cells = new ArrayList();
   protected GXConnectionSet connectionSet;
   protected GXHashtable viewAttributes;
   protected boolean isLocked;
   protected boolean gridVisible;
   protected String background = new String("java.awt.Color[r=255,g=255,b=255]");

   public GXObjectDiagram() {
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

   public void addObjects(Object c) {
      this.objects.add(c);
   }

   public Enumeration getObjectsList() {
      return Collections.enumeration(this.objects);
   }

   public ArrayList getCollectionObjects() {
      return this.objects;
   }

   public void setObjects(ArrayList o) {
      this.objects = o;
   }

   public void addObjectViews(Object ov) {
      this.objectViews.add(ov);
   }

   public Enumeration getObjectViewsList() {
      return Collections.enumeration(this.objectViews);
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
