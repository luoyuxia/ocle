package ro.ubbcluj.lci.gxapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class GXUseCaseDiagram {
   protected String name;
   protected Object owner;
   protected ArrayList actors = new ArrayList();
   protected ArrayList actorViews = new ArrayList();
   protected ArrayList useCases = new ArrayList();
   protected ArrayList useCaseViews = new ArrayList();
   protected ArrayList flat = new ArrayList();
   protected ArrayList cells = new ArrayList();
   protected GXConnectionSet connectionSet;
   protected GXHashtable viewAttributes;
   protected boolean isLocked;
   protected boolean gridVisible;
   protected String background = new String("java.awt.Color[r=255,g=255,b=255]");

   public GXUseCaseDiagram() {
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

   public void addActors(Object c) {
      this.actors.add(c);
   }

   public Enumeration getActorsList() {
      return Collections.enumeration(this.actors);
   }

   public ArrayList getCollectionActors() {
      return this.actors;
   }

   public void setActors(ArrayList a) {
      this.actors = a;
   }

   public void addUseCases(Object p) {
      this.useCases.add(p);
   }

   public Enumeration getUseCasesList() {
      return Collections.enumeration(this.useCases);
   }

   public ArrayList getCollectionUseCases() {
      return this.useCases;
   }

   public void setUseCases(ArrayList p) {
      this.useCases = p;
   }

   public void addActorViews(Object cv) {
      this.actorViews.add(cv);
   }

   public Enumeration getActorViewsList() {
      return Collections.enumeration(this.actorViews);
   }

   public void addUseCaseViews(Object pv) {
      this.useCaseViews.add(pv);
   }

   public Enumeration getUseCaseViewsList() {
      return Collections.enumeration(this.useCaseViews);
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
