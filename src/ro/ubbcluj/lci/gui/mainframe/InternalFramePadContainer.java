package ro.ubbcluj.lci.gui.mainframe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import ro.ubbcluj.lci.gui.editor.AbstractPad;
import ro.ubbcluj.lci.utils.Utils;

public class InternalFramePadContainer extends ro.ubbcluj.lci.gui.editor.mdi.PadContainer {
   private JComboBox cbFrames = new JComboBox();
   private Component activeComponent = null;
   private JPanel component = new JPanel();
   private JDesktopPane desktop = new JDesktopPane();
   private JToolBar statusPanel = new JToolBar();
   private JInternalFrame activeFrame;
   private JButton[] buttons;
   private AbstractAction[] actions;
   private boolean maximized = false;
   private boolean bSetActivePad = true;
   private HashMap pads = new HashMap();
   private InternalFrameListener customListener = new InternalFramePadContainer.CustomListener();

   public InternalFramePadContainer() {
      this.doLayout();
      this.configureListenersAndRenderer();
   }

   public void contentsModified(AbstractPad[] pads, boolean modified) {
      for(int i = 0; i < pads.length; ++i) {
         InternalFramePadContainer.IFPadDescriptor ifpd = (InternalFramePadContainer.IFPadDescriptor)pads[i].getDescriptor();
         JInternalFrame f = ifpd.getFrame();
         String text = pads[i].getDescription();
         if (text == null || "".equals(text)) {
            text = pads[i].getTitle();
         }

         if (modified) {
            f.setTitle(text + PadContainer.UNSAVED_MARKER);
         } else {
            f.setTitle(text);
         }
      }

      this.cbFrames.repaint();
   }

   public int closeAll(AbstractPad[] padsToClose) {
      boolean cancelled = false;
      this.bSetActivePad = false;

      int i;
      AbstractPad padToClose;
      for(i = 0; i < padsToClose.length; ++i) {
         padToClose = padsToClose[i];
         if (padToClose != null) {
            padToClose.getDescriptor().needsClosing(true);
         }
      }

      for(i = 0; i < padsToClose.length; ++i) {
         padToClose = padsToClose[i];
         if (padToClose != null) {
            InternalFramePadContainer.IFPadDescriptor ifpd = (InternalFramePadContainer.IFPadDescriptor)padToClose.getDescriptor();
            if (ifpd.needsClosing()) {
               int ret = padsToClose[i].close();
               if (ret == 4) {
                  cancelled = true;
                  break;
               }

               if (ret == 2) {
                  JInternalFrame f = ifpd.getFrame();
                  this.cbFrames.removeItem(f);
                  this.desktop.remove(f);
                  this.pads.remove(padsToClose[i]);
               }
            }
         }
      }

      this.updateForClose();
      this.bSetActivePad = true;
      return cancelled ? 4 : 2;
   }

   public Collection getPads() {
      return this.pads.values();
   }

   public int removePadView(AbstractPad padView) {
      JInternalFrame frame = ((InternalFramePadContainer.IFPadDescriptor)padView.getDescriptor()).getFrame();
      int ret = padView.close();
      if (ret != 2) {
         return 3;
      } else {
         this.bSetActivePad = false;
         this.pads.remove(padView);
         this.desktop.remove(frame);
         this.desktop.validate();
         this.desktop.repaint();
         frame.dispose();
         this.cbFrames.removeItem(frame);
         this.updateForClose();
         this.bSetActivePad = true;
         return 2;
      }
   }

   public void activatePad(AbstractPad p) {
      AbstractPad pp = (AbstractPad)this.pads.get(p);
      if (pp != null) {
         JInternalFrame jif = ((InternalFramePadContainer.IFPadDescriptor)pp.getDescriptor()).getFrame();
         this.activateFrame(jif, pp);
      } else {
         this.activeFrame = this.frameForPad(p);
         this.bSetActivePad = false;
         p.initialize();
         this.cbFrames.addItem(this.activeFrame);
         this.cbFrames.setSelectedItem(this.activeFrame);
         this.pads.put(p, p);
         p.addPropertyChangeListener(this);
         if (this.maximized) {
            this.desktop.remove(this.activeComponent);
            this.desktop.add(this.activeComponent = p.getComponent());
            this.activeComponent.setBounds(this.desktop.getBounds());
            this.desktop.validate();
            this.desktop.repaint();
         } else {
            if (this.activePad == null) {
               this.activeFrame.setBounds(0, 0, Utils.screenSize.width / 2, Utils.screenSize.height / 2);
            } else {
               Rectangle d = this.desktop.getBounds();
               this.activeFrame.setBounds(d.width / 4, d.height / 4, d.width / 2, d.height / 2);
            }

            this.desktop.add(this.activeFrame);
            this.activeFrame.setVisible(true);
            this.activeFrame.toFront();

            try {
               this.activeFrame.setSelected(true);
            } catch (PropertyVetoException var4) {
               var4.printStackTrace(System.err);
            }
         }

         this.bSetActivePad = true;
      }

      if (p != this.activePad) {
         this.setActivePad(p);
      }

   }

   private JInternalFrame frameForPad(AbstractPad p) {
      JInternalFrame f = new JInternalFrame(p.getDescription(), true, true, true, true);
      InternalFramePadContainer.IFPadDescriptor pd = new InternalFramePadContainer.IFPadDescriptor(this);
      f.setFrameIcon(p.getIcon());
      if (f.getTitle() == null) {
         f.setTitle(p.getTitle());
      }

      f.setDefaultCloseOperation(0);
      f.addInternalFrameListener(this.customListener);
      f.getContentPane().add(p.getComponent());
      pd.setFrame(f);
      p.setDescriptor(pd);
      return f;
   }

   private void setActivePad(AbstractPad pad) {
      this.oldActivePad = this.activePad;
      this.activePad = pad;
      if (this.activePad != null) {
         this.activePad.activate();
      }

      this.fireViewActivated();
   }

   public void propertyChange(PropertyChangeEvent evt) {
      String pName = evt.getPropertyName();
      AbstractPad pad = (AbstractPad)evt.getSource();
      JInternalFrame jif = ((InternalFramePadContainer.IFPadDescriptor)pad.getDescriptor()).getFrame();
      if ("".equals(pName)) {
         jif.setTitle(pad.getDescription());
         jif.setFrameIcon(pad.getIcon());
      } else if ("description".equals(pName)) {
         jif.setTitle(pad.getDescription());
      } else if ("icon".equals(pName)) {
         jif.setFrameIcon(pad.getIcon());
      }

   }

   public JComponent getComponent() {
      return this.component;
   }

   public void setBackground(Color clrBack) {
      this.desktop.setBackground(clrBack);
   }

   private void doLayout() {
      this.statusPanel.setBorder(BorderFactory.createEtchedBorder());
      this.statusPanel.setRollover(true);
      this.statusPanel.setLayout(new BoxLayout(this.statusPanel, 0));
      this.statusPanel.setMaximumSize(new Dimension(Utils.screenSize.width, 30));
      this.statusPanel.add(this.cbFrames);
      this.configureActionButtons();
      this.statusPanel.addSeparator();

      for(int i = 0; i < this.buttons.length; ++i) {
         this.statusPanel.add(this.buttons[i]);
      }

      this.component.setLayout(new BoxLayout(this.component, 1));
      this.component.add(this.desktop);
      this.component.add(this.statusPanel);
   }

   private AbstractPad getPadForFrame(JInternalFrame f) {
      Iterator it = this.pads.values().iterator();

      AbstractPad p;
      InternalFramePadContainer.IFPadDescriptor ifpd;
      do {
         if (!it.hasNext()) {
            return null;
         }

         ifpd = (InternalFramePadContainer.IFPadDescriptor)(p = (AbstractPad)it.next()).getDescriptor();
      } while(f != ifpd.getFrame());

      return p;
   }

   private void configureActionButtons() {
      this.buttons = new JButton[2];
      this.actions = new AbstractAction[2];
      this.actions[0] = new InternalFramePadContainer.MoveAction(1);
      this.actions[1] = new InternalFramePadContainer.MoveAction(2);

      for(int i = 0; i < this.actions.length; ++i) {
         this.buttons[i] = new JButton(this.actions[i]);
         this.buttons[i].setMargin(new Insets(1, 1, 1, 1));
      }

   }

   private void configureListenersAndRenderer() {
      this.cbFrames.addItemListener(new InternalFramePadContainer.FrameSelectionListener());
      this.cbFrames.setRenderer(new InternalFramePadContainer.CustomListCellRenderer());
   }

   private void activateFrame(JInternalFrame frame, AbstractPad p) {
      this.activeFrame = frame;
      this.bSetActivePad = false;
      this.cbFrames.setSelectedItem(this.activeFrame);

      try {
         if (this.activeFrame.isIcon()) {
            this.activeFrame.setIcon(false);
         }
      } catch (PropertyVetoException var5) {
         var5.printStackTrace();
      }

      if (!this.maximized) {
         frame.toFront();

         try {
            frame.setSelected(true);
         } catch (PropertyVetoException var4) {
            var4.printStackTrace(System.err);
         }
      } else {
         this.desktop.remove(this.activeComponent);
         this.activeComponent = p.getComponent();
         this.desktop.add(this.activeComponent);
         this.activeComponent.setBounds(this.desktop.getBounds());
         this.desktop.validate();
         this.desktop.repaint();
      }

      this.bSetActivePad = true;
   }

   private void updateForClose() {
      AbstractPad p;
      if (this.maximized) {
         this.desktop.remove(this.activeComponent);
         if (this.cbFrames.getSelectedIndex() < 0) {
            if (this.cbFrames.getItemCount() <= 0) {
               this.activeComponent = null;
               this.activeFrame = null;
               this.setActivePad((AbstractPad)null);
               return;
            }

            this.cbFrames.setSelectedIndex(0);
         }

         this.activeFrame = (JInternalFrame)this.cbFrames.getSelectedItem();
         p = this.getPadForFrame(this.activeFrame);
         this.activeComponent = p.getComponent();
         this.desktop.add(this.activeComponent);
         this.activeComponent.setBounds(this.desktop.getBounds());
      } else {
         JInternalFrame[] allFrames = this.desktop.getAllFrames();
         if (allFrames.length <= 0) {
            this.activeFrame = null;
            this.activeComponent = null;
            p = null;
         } else {
            this.activeFrame = allFrames[allFrames.length - 1];

            try {
               this.activeFrame.setSelected(true);
            } catch (PropertyVetoException var4) {
               var4.printStackTrace(System.err);
            }

            p = this.getPadForFrame(this.activeFrame);
            this.cbFrames.setSelectedItem(this.activeFrame);
         }
      }

      this.desktop.validate();
      this.desktop.repaint();
      this.setActivePad(p);
   }

   private class CustomListener extends InternalFrameAdapter {
      private CustomListener() {
      }

      public void internalFrameClosing(InternalFrameEvent evt) {
         JInternalFrame jif = evt.getInternalFrame();
         InternalFramePadContainer.this.removePadView(InternalFramePadContainer.this.getPadForFrame(jif));
      }

      public void internalFrameActivated(InternalFrameEvent evt) {
         if (InternalFramePadContainer.this.bSetActivePad) {
            JInternalFrame frame = evt.getInternalFrame();
            int index = -1;
            InternalFramePadContainer.this.bSetActivePad = false;
            InternalFramePadContainer.this.activeFrame = frame;

            for(int i = 0; i < InternalFramePadContainer.this.cbFrames.getItemCount(); ++i) {
               if (frame.equals(InternalFramePadContainer.this.cbFrames.getItemAt(i))) {
                  index = i;
                  break;
               }
            }

            if (index == -1) {
               throw new RuntimeException("Activated frame not registered.");
            } else {
               AbstractPad pad = InternalFramePadContainer.this.getPadForFrame(frame);
               InternalFramePadContainer.this.cbFrames.setSelectedIndex(index);
               InternalFramePadContainer.this.setActivePad(pad);
               InternalFramePadContainer.this.bSetActivePad = true;
            }
         }
      }
   }

   private static class IFPadDescriptor extends ro.ubbcluj.lci.gui.editor.mdi.PadDescriptor {
      private JInternalFrame frame = null;

      public IFPadDescriptor(ro.ubbcluj.lci.gui.editor.mdi.PadContainer pc) {
         this.padContainer = pc;
      }

      public JInternalFrame getFrame() {
         return this.frame;
      }

      public void setFrame(JInternalFrame jif) {
         this.frame = jif;
      }
   }

   private class FrameSelectionListener implements ItemListener {
      private FrameSelectionListener() {
      }

      public void itemStateChanged(ItemEvent evt) {
         if (InternalFramePadContainer.this.bSetActivePad) {
            if (evt.getStateChange() == 1) {
               JInternalFrame jif = (JInternalFrame)InternalFramePadContainer.this.cbFrames.getSelectedItem();
               if (InternalFramePadContainer.this.activeFrame != jif) {
                  AbstractPad p = InternalFramePadContainer.this.getPadForFrame(jif);
                  InternalFramePadContainer.this.activateFrame(jif, p);
                  InternalFramePadContainer.this.setActivePad(p);
               }
            }

         }
      }
   }

   private class CustomListCellRenderer extends DefaultListCellRenderer {
      private CustomListCellRenderer() {
      }

      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         JInternalFrame frame = (JInternalFrame)value;
         if (frame != null) {
            this.setIcon(frame.getFrameIcon());
            this.setText(frame.getTitle());
         }

         return this;
      }
   }

   private class MoveAction extends AbstractAction {
      int direction;

      public MoveAction(int dir) {
         if (dir >= 1 && dir <= 2) {
            this.direction = dir;
            if (dir == 1) {
               this.putValue("SmallIcon", new ImageIcon(this.getClass().getResource("/resources/first.gif")));
            } else {
               this.putValue("SmallIcon", new ImageIcon(this.getClass().getResource("/resources/last.gif")));
            }

         } else {
            throw new IllegalArgumentException("Invalid direction");
         }
      }

      public void actionPerformed(ActionEvent evt) {
         int si = InternalFramePadContainer.this.cbFrames.getSelectedIndex();
         if (si >= 0) {
            if (this.direction == 1) {
               --si;
               if (si < 0) {
                  si = InternalFramePadContainer.this.cbFrames.getItemCount() - 1;
               }
            } else {
               ++si;
               if (si >= InternalFramePadContainer.this.cbFrames.getItemCount()) {
                  si = 0;
               }
            }

            InternalFramePadContainer.this.cbFrames.setSelectedIndex(si);
         }
      }
   }
}
