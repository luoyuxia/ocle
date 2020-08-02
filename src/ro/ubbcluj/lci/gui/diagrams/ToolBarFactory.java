package ro.ubbcluj.lci.gui.diagrams;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;

public class ToolBarFactory {
   protected static final Insets INSET = new Insets(0, 0, 0, 0);
   protected static ToolBarFactory.Class_diagramToolBar classDiagramToolBar;
   protected static ToolBarFactory.UseCase_diagramToolBar useCaseDiagramToolBar;
   protected static ToolBarFactory.Snapshot_diagramToolBar snapshotDiagramToolBar;

   private ToolBarFactory() {
   }

   public static JToolBar getToolBar(Diagram d) {
      if (d instanceof ClassDiagram) {
         if (classDiagramToolBar == null) {
            classDiagramToolBar = new ToolBarFactory.Class_diagramToolBar();
         }

         classDiagramToolBar.setActiveDiagram(d);
         return classDiagramToolBar;
      } else if (d instanceof UseCaseDiagram) {
         if (useCaseDiagramToolBar == null) {
            useCaseDiagramToolBar = new ToolBarFactory.UseCase_diagramToolBar();
         }

         useCaseDiagramToolBar.setActiveDiagram(d);
         return useCaseDiagramToolBar;
      } else if (d instanceof ObjectDiagram) {
         if (snapshotDiagramToolBar == null) {
            snapshotDiagramToolBar = new ToolBarFactory.Snapshot_diagramToolBar();
         }

         snapshotDiagramToolBar.setActiveDiagram(d);
         return snapshotDiagramToolBar;
      } else {
         return null;
      }
   }

   protected static class Snapshot_diagramToolBar extends JToolBar {
      protected Diagram activeDiagram;

      protected URL getImage(String key) {
         return this.getClass().getResource("/images/" + key + ".gif");
      }

      public Snapshot_diagramToolBar() {
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Select", new ImageIcon(this.getImage("Select")), 0)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Object", new ImageIcon(this.getImage("Object")), 6)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Link", new ImageIcon(this.getImage("Link")), 107)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Default Zoom", new ImageIcon(this.getImage("zoom")), 201)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Zoom in", new ImageIcon(this.getImage("zoomin")), 202)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Zoom out", new ImageIcon(this.getImage("zoomout")), 203)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Undo", new ImageIcon(this.getImage("undo")), 301)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Redo", new ImageIcon(this.getImage("redo")), 302)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.addSeparator();
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Quick View", new ImageIcon(this.getImage("qw")), 400)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Align Left", new ImageIcon(this.getImage("align_left")), 501)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Align Right", new ImageIcon(this.getImage("align_right")), 502)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Align Top", new ImageIcon(this.getImage("align_top")), 503)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Snapshot_diagramToolBar.PickToolAction("Align Bottom", new ImageIcon(this.getImage("align_bottom")), 504)).setMargin(ToolBarFactory.INSET);
         this.setOrientation(0);
         this.setBorder(BorderFactory.createEtchedBorder());
         this.setFloatable(false);
         this.setRollover(true);
      }

      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         g2.setColor(this.getBackground());
         g2.draw3DRect(0, 0, this.getSize().width, this.getSize().height, true);
      }

      public void setActiveDiagram(Diagram d) {
         this.activeDiagram = d;
      }

      public Diagram getActiveDiagram() {
         return this.activeDiagram;
      }

      private class PickToolAction extends AbstractAction {
         private int kind;

         public PickToolAction(String name, Icon icon, int kind) {
            super(name, icon);
            this.kind = kind;
            this.putValue("ShortDescription", name);
         }

         public void actionPerformed(ActionEvent e) {
            Snapshot_diagramToolBar.this.activeDiagram.setActionKind(this.kind);
            switch(this.kind) {
            case 201:
               Snapshot_diagramToolBar.this.activeDiagram.zoomDefault();
               break;
            case 202:
               Snapshot_diagramToolBar.this.activeDiagram.zoomIn();
               break;
            case 203:
               Snapshot_diagramToolBar.this.activeDiagram.zoomOut();
               break;
            case 301:
               Snapshot_diagramToolBar.this.activeDiagram.undo();
               break;
            case 302:
               Snapshot_diagramToolBar.this.activeDiagram.redo();
               break;
            case 400:
               DiagramQuickView qw = DiagramQuickView.getInstance((DiagramGraph)Snapshot_diagramToolBar.this.activeDiagram.getGraph());
               qw.setVisible(true);
               break;
            case 501:
               ((ObjectDiagram)Snapshot_diagramToolBar.this.activeDiagram).alignOnMinimum(501);
               break;
            case 502:
               ((ObjectDiagram)Snapshot_diagramToolBar.this.activeDiagram).alignOnMaximum(502);
               break;
            case 503:
               ((ObjectDiagram)Snapshot_diagramToolBar.this.activeDiagram).alignOnMinimum(503);
               break;
            case 504:
               ((ObjectDiagram)Snapshot_diagramToolBar.this.activeDiagram).alignOnMaximum(504);
            }

            if (this.kind > 200) {
               Snapshot_diagramToolBar.this.activeDiagram.setActionKind(0);
            }

         }
      }
   }

   protected static class UseCase_diagramToolBar extends JToolBar {
      protected Diagram activeDiagram;

      protected URL getImage(String key) {
         return this.getClass().getResource("/images/" + key + ".gif");
      }

      public UseCase_diagramToolBar() {
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("Select", new ImageIcon(this.getImage("Select")), 0)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("AddActor", new ImageIcon(this.getImage("Actor")), 4)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("AddUseCase", new ImageIcon(this.getImage("UseCase")), 5)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("AddAssociation", new ImageIcon(this.getImage("Association")), 101)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("AddGeneralization", new ImageIcon(this.getImage("Generalization")), 102)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("AddInclude", new ImageIcon(this.getImage("Include")), 108)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("AddExtend", new ImageIcon(this.getImage("Extend")), 109)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("Default Zoom", new ImageIcon(this.getImage("zoom")), 201)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("Zoom in", new ImageIcon(this.getImage("zoomin")), 202)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("Zoom out", new ImageIcon(this.getImage("zoomout")), 203)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("Undo", new ImageIcon(this.getImage("undo")), 301)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("Redo", new ImageIcon(this.getImage("redo")), 302)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.addSeparator();
         this.add(new ToolBarFactory.UseCase_diagramToolBar.PickToolAction("Quick View", new ImageIcon(this.getImage("qw")), 400)).setMargin(ToolBarFactory.INSET);
         this.setOrientation(0);
         this.setBorder(BorderFactory.createEtchedBorder());
         this.setFloatable(false);
         this.setRollover(true);
      }

      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         g2.setColor(this.getBackground());
         g2.draw3DRect(0, 0, this.getSize().width, this.getSize().height, true);
      }

      public void setActiveDiagram(Diagram d) {
         this.activeDiagram = d;
      }

      public Diagram getActiveDiagram() {
         return this.activeDiagram;
      }

      private class PickToolAction extends AbstractAction {
         private int kind;

         public PickToolAction(String name, Icon icon, int kind) {
            super(name, icon);
            this.kind = kind;
            this.putValue("ShortDescription", name);
         }

         public void actionPerformed(ActionEvent e) {
            UseCase_diagramToolBar.this.activeDiagram.setActionKind(this.kind);
            switch(this.kind) {
            case 201:
               UseCase_diagramToolBar.this.activeDiagram.zoomDefault();
               break;
            case 202:
               UseCase_diagramToolBar.this.activeDiagram.zoomIn();
               break;
            case 203:
               UseCase_diagramToolBar.this.activeDiagram.zoomOut();
               break;
            case 301:
               UseCase_diagramToolBar.this.activeDiagram.undo();
               break;
            case 302:
               UseCase_diagramToolBar.this.activeDiagram.redo();
               break;
            case 400:
               DiagramQuickView qw = DiagramQuickView.getInstance((DiagramGraph)UseCase_diagramToolBar.this.activeDiagram.getGraph());
               qw.setVisible(true);
            }

            if (this.kind > 200) {
               UseCase_diagramToolBar.this.activeDiagram.setActionKind(0);
            }

         }
      }
   }

   protected static class Class_diagramToolBar extends JToolBar {
      protected Diagram activeDiagram;

      protected URL getImage(String key) {
         return this.getClass().getResource("/images/" + key + ".gif");
      }

      public Class_diagramToolBar() {
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Select", new ImageIcon(this.getImage("Select")), 0)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("AddClass", new ImageIcon(this.getImage("Class")), 1)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("AddInterface", new ImageIcon(this.getImage("Interface")), 2)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("AddPackage", new ImageIcon(this.getImage("Package")), 3)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("AddAssociation", new ImageIcon(this.getImage("Association")), 101)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("AddGeneralization", new ImageIcon(this.getImage("Generalization")), 102)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("AddDependency", new ImageIcon(this.getImage("Instantiate")), 103)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("AddPermission", new ImageIcon(this.getImage("Permission")), 104)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("AddRealization", new ImageIcon(this.getImage("Realize")), 105)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("AddAssociationClass", new ImageIcon(this.getImage("AssociationClass")), 106)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Default Zoom", new ImageIcon(this.getImage("zoom")), 201)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Zoom in", new ImageIcon(this.getImage("zoomin")), 202)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Zoom out", new ImageIcon(this.getImage("zoomout")), 203)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Undo", new ImageIcon(this.getImage("undo")), 301)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Redo", new ImageIcon(this.getImage("redo")), 302)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.addSeparator();
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Quick View", new ImageIcon(this.getImage("qw")), 400)).setMargin(ToolBarFactory.INSET);
         this.addSeparator();
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Align Left", new ImageIcon(this.getImage("align_left")), 501)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Align Right", new ImageIcon(this.getImage("align_right")), 502)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Align Top", new ImageIcon(this.getImage("align_top")), 503)).setMargin(ToolBarFactory.INSET);
         this.add(new ToolBarFactory.Class_diagramToolBar.PickToolAction("Align Bottom", new ImageIcon(this.getImage("align_bottom")), 504)).setMargin(ToolBarFactory.INSET);
         this.setOrientation(0);
         this.setBorder(BorderFactory.createEtchedBorder());
         this.setFloatable(false);
         this.setRollover(true);
      }

      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         g2.setColor(this.getBackground());
         g2.draw3DRect(0, 0, this.getSize().width, this.getSize().height, true);
      }

      public void setActiveDiagram(Diagram d) {
         this.activeDiagram = d;
      }

      public Diagram getActiveDiagram() {
         return this.activeDiagram;
      }

      private class PickToolAction extends AbstractAction {
         private int kind;

         public PickToolAction(String name, Icon icon, int kind) {
            super(name, icon);
            this.kind = kind;
            this.putValue("ShortDescription", name);
         }

         public void actionPerformed(ActionEvent e) {
            Class_diagramToolBar.this.activeDiagram.setActionKind(this.kind);
            switch(this.kind) {
            case 201:
               Class_diagramToolBar.this.activeDiagram.zoomDefault();
               break;
            case 202:
               Class_diagramToolBar.this.activeDiagram.zoomIn();
               break;
            case 203:
               Class_diagramToolBar.this.activeDiagram.zoomOut();
               break;
            case 301:
               Class_diagramToolBar.this.activeDiagram.undo();
               break;
            case 302:
               Class_diagramToolBar.this.activeDiagram.redo();
               break;
            case 400:
               DiagramQuickView qw = DiagramQuickView.getInstance((DiagramGraph)Class_diagramToolBar.this.activeDiagram.getGraph());
               qw.setVisible(true);
               break;
            case 501:
               ((ClassDiagram)Class_diagramToolBar.this.activeDiagram).alignOnMinimum(501);
               break;
            case 502:
               ((ClassDiagram)Class_diagramToolBar.this.activeDiagram).alignOnMaximum(502);
               break;
            case 503:
               ((ClassDiagram)Class_diagramToolBar.this.activeDiagram).alignOnMinimum(503);
               break;
            case 504:
               ((ClassDiagram)Class_diagramToolBar.this.activeDiagram).alignOnMaximum(504);
            }

            if (this.kind > 200) {
               Class_diagramToolBar.this.activeDiagram.setActionKind(0);
            }

         }
      }
   }
}
