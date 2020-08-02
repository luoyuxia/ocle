package ro.ubbcluj.lci.gui.mainframe;

import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.gui.diagrams.ToolBarFactory;

public class GDiagramDesktop extends JDesktopPane {
   protected JToolBar diagramToolBar;
   protected boolean existDiagram = false;

   public GDiagramDesktop() {
   }

   public void setDiagramToolBar(JToolBar bar) {
      this.diagramToolBar = bar;
   }

   void removeDiagram(GAbstractDiagram diagram) {
      JInternalFrame[] ifs = this.getAllFrames();

      for(int i = 0; i < ifs.length; ++i) {
         if (ifs[i].equals(diagram)) {
            this.remove(ifs[i]);
            this.hideDiagram(diagram);
            diagram.setView((JInternalFrame)null);
            this.repaint(this.getBounds());
         }
      }

   }

   void addExistingDiagram(GAbstractDiagram dia) {
      GDiagramFrame frame = new GDiagramFrame(dia);
      dia.setView(frame);
      frame.setSize(this.getSize().width * 9 / 10, this.getSize().height * 9 / 10);
      frame.setNewParent(this);
      frame.setLocation(3, 3);
      frame.setVisible(false);
      this.add(frame);
   }

   void addDiagram(GAbstractDiagram dia) {
      GDiagramFrame frame = new GDiagramFrame(dia);
      dia.setView(frame);
      frame.setSize(this.getSize().width * 9 / 10, this.getSize().height * 9 / 10);
      frame.setNewParent(this);
      frame.setLocation(3, 3);
      frame.setVisible(true);
      this.add(frame);

      try {
         frame.setSelected(true);
         this.showDiagram(dia);
      } catch (PropertyVetoException var4) {
         var4.printStackTrace();
      }

   }

   public void hideDiagram(GAbstractDiagram dia) {
      if (this.getAllFrames().length == 0 || !this.allVisible()) {
         this.existDiagram = false;
         if (this.diagramToolBar != null) {
            this.diagramToolBar.setVisible(false);
            ((JPanel)this.getParent()).remove(this.diagramToolBar);
         }
      }

   }

   public void showDiagram(GAbstractDiagram dia) {
      if (this.existDiagram) {
         this.diagramToolBar.setVisible(false);
         ((JPanel)this.getParent()).remove(this.diagramToolBar);
      }

      dia.getView().show();
      dia.getView().toFront();

      try {
         dia.getView().setSelected(true);
      } catch (PropertyVetoException var3) {
      }

      this.diagramToolBar = ToolBarFactory.getToolBar(dia);
      ((JPanel)this.getParent()).add(this.diagramToolBar, "North");
      this.diagramToolBar.setVisible(true);
      dia.getView().setVisible(true);
      if (!this.existDiagram) {
         this.existDiagram = true;
      }

   }

   protected boolean allVisible() {
      boolean result = false;
      JInternalFrame[] all_frames = this.getAllFrames();

      for(int i = 0; i < all_frames.length; ++i) {
         if (all_frames[i].isVisible()) {
            result = true;
         }
      }

      return result;
   }
}
