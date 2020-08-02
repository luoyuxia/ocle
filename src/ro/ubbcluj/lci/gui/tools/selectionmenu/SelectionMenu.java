package ro.ubbcluj.lci.gui.tools.selectionmenu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.EventListenerList;

public class SelectionMenu extends JPopupMenu {
   private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   private JScrollPane scroll;
   private JList items;
   private JPanel panel;
   private JLabel title = new JLabel("Title");
   private Color titleColor;
   private Color background;
   private SelectionMenu.SortableListModel model;
   private Comparator comparator;
   private EventListenerList listenerList = new EventListenerList();

   public SelectionMenu() {
      this.title.setHorizontalAlignment(0);
      this.panel = new JPanel();
      this.panel.setLayout(new BoxLayout(this.panel, 1));
      this.items = new JList();
      this.items.setModel(this.model = new SelectionMenu.SortableListModel());
      this.scroll = new JScrollPane(this.items);
      this.panel.add(this.title);
      this.panel.add(this.scroll);
      this.titleColor = new Color(255, 0, 0);
      this.title.setBackground(this.titleColor);
      this.background = new Color(215, 216, 197);
      this.items.setBackground(this.background);
      this.items.addKeyListener(new SelectionMenu.KeyboardListener());
      this.items.addMouseListener(new SelectionMenu.MouseSelectionListener());
      this.add(this.panel);
   }

   public void show(Component p, int x, int y) {
      super.show(p, x, y);
      this.adjustLocation();
   }

   public void setVisible(boolean bVisible) {
      super.setVisible(bVisible);
      if (!bVisible) {
         EventListener[] ls = this.listenerList.getListeners(MenuSelectionListener.class);

         for(int i = 0; i < ls.length; ++i) {
            this.listenerList.remove(MenuSelectionListener.class, ls[i]);
         }

         this.model.removeAll();
      }

   }

   public void setRenderer(ListCellRenderer lr) {
      this.items.setCellRenderer(lr);
   }

   private void fireValuePicked(MenuSelectionEvent evt) {
      EventListener[] listeners = this.listenerList.getListeners(MenuSelectionListener.class);

      for(int i = listeners.length - 1; i >= 0; --i) {
         ((MenuSelectionListener)listeners[i]).valuePicked(evt);
      }

   }

   public void addMenuSelectionListener(MenuSelectionListener ms) {
      this.listenerList.add(MenuSelectionListener.class, ms);
   }

   public void removeMenuSelectionListener(MenuSelectionListener ms) {
      this.listenerList.remove(MenuSelectionListener.class, ms);
   }

   public void setTitle(String t) {
      this.title.setText(t);
   }

   private Object getSelectedObject() {
      return this.items.getSelectedValue();
   }

   private void sort() {
      this.model.sort(this.comparator);
   }

   public void invoke(Component parent, int x, int y) {
      this.sort();
      if (this.model.getSize() > 0) {
         this.items.setSelectedIndex(0);
      }

      this.show(parent, x, y);
      this.adjustScrollPosition();
      this.items.requestFocus();
   }

   public void addAll(Collection itemsToAdd) {
      this.model.addAll(itemsToAdd);
   }

   public void setComparator(Comparator c) {
      this.comparator = c;
   }

   private void adjustLocation() {
      Point ls = this.getLocationOnScreen();
      Dimension size = this.getSize();
      int right = ls.x + size.width;
      int down = ls.y + size.height;
      int dx;
      if (right > screenSize.width) {
         dx = screenSize.width - right;
      } else {
         dx = 0;
      }

      int dy;
      if (down > screenSize.height) {
         dy = screenSize.height - down;
      } else {
         dy = 0;
      }

      this.setLocation(ls.x + dx, ls.y + dy);
   }

   private void adjustScrollPosition() {
      JScrollBar vsb = this.scroll.getVerticalScrollBar();
      vsb.setValue(vsb.getMinimum());
   }

   private class SortableListModel extends AbstractListModel {
      private ArrayList items;

      private SortableListModel() {
         this.items = new ArrayList();
      }

      void sort(Comparator sc) {
         if (sc != null) {
            Collections.sort(this.items, sc);
            this.fireContentsChanged(this, 0, this.items.size() - 1);
         }

      }

      void addAll(Collection itemsToAdd) {
         int oldSize = this.items.size();
         this.items.addAll(itemsToAdd);
         this.fireContentsChanged(this, oldSize, this.items.size() - 1);
      }

      void removeAll() {
         this.items.clear();
         this.fireContentsChanged(this, 0, 0);
      }

      public int getSize() {
         return this.items.size();
      }

      public Object getElementAt(int index) {
         return this.items.get(index);
      }
   }

   private class MouseSelectionListener extends MouseAdapter {
      private MouseSelectionListener() {
      }

      public void mouseClicked(MouseEvent ev) {
         if (ev.getClickCount() >= 2) {
            Object obj = SelectionMenu.this.getSelectedObject();
            if (obj != null) {
               MenuSelectionEvent mse = new MenuSelectionEvent(SelectionMenu.this, obj);
               SelectionMenu.this.fireValuePicked(mse);
            }
         }

      }
   }

   private class KeyboardListener extends KeyAdapter {
      private KeyboardListener() {
      }

      public void keyPressed(KeyEvent evt) {
         if (evt.getKeyCode() == 10) {
            Object obj = SelectionMenu.this.getSelectedObject();
            if (obj != null) {
               MenuSelectionEvent mse = new MenuSelectionEvent(SelectionMenu.this, obj);
               SelectionMenu.this.fireValuePicked(mse);
            }
         }

      }
   }
}
