package ro.ubbcluj.lci.gui.diagrams;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ActorGProperty extends DefaultGProperty {
   private transient ActorGProperty.ActorMenuElementListener listener;
   private Color fill_color;

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      this.initMainPopup(GProperty.getStringsForItem("MENIU_ACTOR"));
   }

   public void setFillColor(Color newColor) {
      this.fill_color = newColor;
   }

   public Color getFillColor() {
      return this.fill_color;
   }

   protected ActorGProperty(ArrayList strings) {
      this.fill_color = Color.white;
      this.listener = new ActorGProperty.ActorMenuElementListener();
      this.initMainPopup(strings);
   }

   private void buildColorMenu(JMenu menu, String actionCommand) {
      for(int i = 0; i < colors.length; ++i) {
         JButton b = new JButton();
         b.setBackground(colors[i]);
         b.setForeground(colors[i]);
         b.setActionCommand(actionCommand);
         b.setText("Color");
         b.addActionListener(this.listener);
         menu.add(b);
      }

      JButton bo = new JButton("Other");
      bo.setActionCommand(actionCommand);
      bo.addActionListener(this.listener);
      menu.add(bo);
   }

   private void checkSubmenu(String key, JMenuItem menu_i) {
      if (menu_i instanceof JMenu) {
         JMenu menu = (JMenu)menu_i;
         if (key.equals("CULOARE_UMPLERE")) {
            this.buildColorMenu(menu, "fill_color");
         }
      }

   }

   private void initPopup(ArrayList strings) {
      for(int i = 0; i < strings.size(); ++i) {
         String text = (String)strings.get(i);
         String content = "";
         if (!text.equals("-")) {
            content = res.getString(text).trim();
         }

         if (text.equals("-")) {
            this.popup.addSeparator();
         } else {
            JMenu menu = new JMenu(content);
            this.popup.add(menu);
            this.checkSubmenu(text, menu);
         }
      }

   }

   private void initMainPopup(ArrayList strings) {
      for(int i = 0; i < strings.size(); ++i) {
         String key = (String)strings.get(i);
         if (key.equals("SUBMENIU_ACTOR")) {
            this.popup_menu.add(this.popup);
            this.initPopup(GProperty.getStringsForItem("SUBMENIU_ACTOR"));
         } else if (key.equals("-")) {
            this.popup_menu.addSeparator();
         } else {
            String name = GProperty.res.getString(key).trim();
            JMenuItem item = new JMenuItem(name);
            this.popup_menu.add(item);
            item.addActionListener(this.listener);
         }
      }

   }

   class ActorMenuElementListener extends MouseAdapter implements ActionListener {
      ActorMenuElementListener() {
      }

      private void uniformize() {
         ActorGProperty.super.uniformize();
         ArrayList selection = ActorGProperty.this.getDiagramListener().getDiagram().getSelected();

         for(int i = 0; i < selection.size(); ++i) {
            ActorGProperty prop = null;
            if (selection.get(i) instanceof ActorCell) {
               prop = (ActorGProperty)((ActorCell)selection.get(i)).getProperty();
            }

            if (selection.get(i) instanceof ObjectCell) {
               prop = (ActorGProperty)((ObjectCell)selection.get(i)).getProperty();
            }

            prop.setFillColor(ActorGProperty.this.fill_color);
         }

      }

      public void actionPerformed(ActionEvent e) {
         if (e.getSource() instanceof JButton) {
            JButton choice = (JButton)e.getSource();
            if (e.getActionCommand().equals("fill_color") && choice.getText().equals("Other")) {
               ((JButton)e.getSource()).getParent().setVisible(false);
               ActorGProperty.this.fill_color = JColorChooser.showDialog((Component)null, "Another color", Color.black);
            } else if (e.getActionCommand().equals("fill_color")) {
               ActorGProperty.this.fill_color = choice.getForeground();
            }
         }

         this.uniformize();
         ActorGProperty.this.repaintCell(ActorGProperty.this.getDiagramListener());
         ((JButton)e.getSource()).getParent().setVisible(false);
         ActorGProperty.this.popup.getPopupMenu().setVisible(false);
         ActorGProperty.this.popup_menu.setVisible(false);
      }
   }
}
