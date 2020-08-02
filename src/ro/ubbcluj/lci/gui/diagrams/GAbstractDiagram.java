package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.JGraph;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import ro.ubbcluj.lci.gui.browser.BTree.Browseable;
import ro.ubbcluj.lci.gui.browser.BTree.Filter;
import ro.ubbcluj.lci.gui.browser.BTree.Order;
import ro.ubbcluj.lci.gui.editor.AbstractPad;
import ro.ubbcluj.lci.gui.mainframe.GUMLModelView;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.utils.ModelEvent;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.ModelListener;

public abstract class GAbstractDiagram extends GUMLModelView implements Diagram, Browseable, Printable, ModelListener {
   public static final String CLASS_DIAGRAM = "Class";
   public static final String OBJECT_DIAGRAM = "Object";
   public static final String SEQUENCE_DIAGRAM = "Sequence";
   public static final String USECASE_DIAGRAM = "UseCase";
   private static final String LOCK_ACTIONCMD = "Lock diagram";
   private static final String GRID_ACTIONCMD = "Show grid";
   private static final String BACKCOLOR_ACTIONCMD = "Background color";
   protected transient JComponent diagram;
   private transient PageFormat pf = null;
   protected ModelElement context;
   protected transient JInternalFrame view;
   protected transient AbstractPad pad;
   private String name;
   private static transient JPopupMenu popup = null;
   private static transient GAbstractDiagram.PopupListener plistener = new GAbstractDiagram.PopupListener();
   private static transient JCheckBoxMenuItem lockCb = new JCheckBoxMenuItem("Lock diagram");
   private static transient JCheckBoxMenuItem showGridCb = new JCheckBoxMenuItem("Show grid");
   private static transient JMenuItem diagBackground = new JMenuItem("Background color");
   boolean isLocked;

   public GAbstractDiagram() {
      ModelFactory.addModelListener(this);
   }

   public JComponent getComponent() {
      return this.diagram;
   }

   public String getName() {
      return this.name;
   }

   public boolean isLocked() {
      return this.isLocked;
   }

   public void setName(String new_name) {
      this.name = new_name;
      if (this.pad != null) {
         this.pad.setDescription(this.name);
      }

   }

   public boolean equals(Object obj) {
      if (obj instanceof GAbstractDiagram) {
         GAbstractDiagram temp = (GAbstractDiagram)obj;
         return this.getName() == null ? false : this.getName().equals(temp.getName());
      } else {
         return false;
      }
   }

   public List getChildren(Filter filter, Order order) {
      return new ArrayList();
   }

   public List getChildren() {
      return new ArrayList();
   }

   public abstract String getDiagramKind();

   public String toString() {
      return this.getDiagramKind() + ": " + this.getName();
   }

   public ModelElement getContext() {
      return this.context;
   }

   public void setContext(ModelElement context) {
      this.context = context;
   }

   public JInternalFrame getView() {
      return this.view;
   }

   public void setView(JInternalFrame newView) {
      this.view = newView;
   }

   public AbstractPad getPad() {
      return this.pad;
   }

   public void setPad(AbstractPad newPad) {
      this.pad = newPad;
   }

   public void setPageFormat(PageFormat newPF) {
      this.pf = newPF;
   }

   public void print() {
      PrinterJob pj = PrinterJob.getPrinterJob();
      pj.setPrintable(this);
      if (pj.printDialog()) {
         try {
            pj.print();
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }

   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
      JGraph graph = (JGraph)((JScrollPane)this.diagram.getComponent(0)).getComponent(0);
      if (pageIndex > 0) {
         return 1;
      } else {
         double oldScale = graph.getScale();
         graph.setScale(1.0D);
         graph.paint(graphics);
         graph.setScale(oldScale);
         return 0;
      }
   }

   public void showPopup(MouseEvent e) {
      plistener.setDiagram(this);
      lockCb.setSelected(this.isLocked);
      showGridCb.setState(this.getGraph().isGridVisible());
      if (popup == null) {
         popup = new JPopupMenu();
         lockCb.setActionCommand("Lock diagram");
         showGridCb.setActionCommand("Show grid");
         diagBackground.setActionCommand("Background color");
         lockCb.addItemListener(plistener);
         showGridCb.addItemListener(plistener);
         diagBackground.addActionListener(plistener);
         popup.add(lockCb);
         popup.add(showGridCb);
         popup.addSeparator();
         popup.add(diagBackground);
      }

      popup.show(this.diagram, e.getX(), e.getY());
   }

   public abstract void addObject(Object var1, int var2, int var3);

   public abstract boolean accept(Object var1);

   public abstract JPanel getDropPanel();

   public abstract void lockDiagram(boolean var1);

   public void modelChanged(ModelEvent evt) {
      int operation = evt.getOperation();
      Object subject = evt.getSubject();
      switch(operation) {
      case 0:
         this.setSelected(subject);
         break;
      case 10:
         if (!(subject instanceof Feature) && !(subject instanceof AttributeLink)) {
            if (subject instanceof Parameter) {
               this.updateItem(((Parameter)subject).getBehavioralFeature().getOwner());
            }
         } else {
            this.updateItem(evt.getContext());
         }
         break;
      case 20:
      case 21:
         this.updateItem(subject);
         break;
      case 30:
         this.removeItem(subject);
      }

   }

   public abstract void clear();

   private static class PopupListener implements ActionListener, ItemListener {
      private GAbstractDiagram diag;
      private JGraph g;

      private PopupListener() {
      }

      public void setDiagram(GAbstractDiagram dg) {
         this.diag = dg;
         this.g = this.diag.getGraph();
      }

      public void actionPerformed(ActionEvent e) {
         Color backColor = JColorChooser.showDialog(this.diag.getComponent(), "Choose BackgroundColor", Color.white);
         this.g.setBackground(backColor);
         this.g.repaint();
         GAbstractDiagram.popup.setVisible(false);
      }

      public void itemStateChanged(ItemEvent e) {
         boolean val = false;
         if (e.getStateChange() == 1) {
            val = true;
         }

         JCheckBoxMenuItem mitem = (JCheckBoxMenuItem)e.getItem();
         if (mitem.getActionCommand().equals("Lock diagram")) {
            if (this.diag.isLocked == val) {
               return;
            }

            this.diag.lockDiagram(!val);
            GAbstractDiagram.popup.setVisible(false);
         } else if (mitem.getActionCommand().equals("Show grid")) {
            this.g.setGridVisible(val);
            this.g.setGridEnabled(val);
            GAbstractDiagram.popup.setVisible(false);
         }

      }
   }
}
