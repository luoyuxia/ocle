package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.GraphCell;
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

public class ObjectGProperty extends DefaultGProperty {
   private transient ObjectGProperty.ObjectMenuElementListener listener;
   private Color fill_color;
   private int titlefont_size;
   private int bodyfont_size;

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.initMenuListener();
      this.initMainPopup(GProperty.getStringsForItem("MENIU_OBIECT"));
   }

   public void setFillColor(Color newColor) {
      this.fill_color = newColor;
   }

   public Color getFillColor() {
      return this.fill_color;
   }

   public void setTitleFontSize(int newSize) {
      this.titlefont_size = newSize;
   }

   public int getTitleFontSize() {
      return this.titlefont_size;
   }

   public void setBodyFontSize(int newSize) {
      this.bodyfont_size = newSize;
   }

   public int getBodyFontSize() {
      return this.bodyfont_size;
   }

   private void initMenuListener() {
      this.listener = new ObjectGProperty.ObjectMenuElementListener();
   }

   protected ObjectGProperty(ArrayList strings) {
      this.fill_color = Color.white;
      this.titlefont_size = 12;
      this.bodyfont_size = 11;
      this.initMenuListener();
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

   private void buildFontMenu(JMenu menu, String actionCommand, int begin_value, int end_value) {
      for(int i = begin_value; i <= end_value; ++i) {
         JButton b = new JButton((new Integer(i)).toString());
         b.setActionCommand(actionCommand);
         b.addActionListener(this.listener);
         menu.add(b);
      }

   }

   private void checkSubmenu(String key, JMenuItem menu_i) {
      if (menu_i instanceof JMenu) {
         JMenu menu = (JMenu)menu_i;
         if (key.equals("CULOARE_UMPLERE")) {
            this.buildColorMenu(menu, res.getString(key));
         }

         if (key.equals("FONT_TITLU")) {
            this.buildFontMenu(menu, res.getString(key), 15, 20);
         }

         if (key.equals("FONT_CORP")) {
            this.buildFontMenu(menu, res.getString(key), 10, 15);
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
         if (key.equals("SUBMENIU_OBIECT")) {
            this.popup_menu.add(this.popup);
            this.initPopup(GProperty.getStringsForItem("SUBMENIU_OBIECT"));
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

   class ObjectMenuElementListener extends MouseAdapter implements ActionListener {
      ObjectMenuElementListener() {
      }

      private void uniformize() {
         ObjectGProperty.super.uniformize();
         ArrayList selection = ObjectGProperty.this.getDiagramListener().getDiagram().getSelected();

         for(int i = 0; i < selection.size(); ++i) {
            ObjectCell cell = (ObjectCell)selection.get(i);
            ObjectGProperty prop = (ObjectGProperty)cell.getProperty();
            prop.setBodyFontSize(ObjectGProperty.this.bodyfont_size);
            prop.setFillColor(ObjectGProperty.this.fill_color);
            prop.setTitleFontSize(ObjectGProperty.this.titlefont_size);
         }

      }

      public void actionPerformed(ActionEvent e) {
         if (e.getSource() instanceof JButton) {
            JButton choice = (JButton)e.getSource();
            if (e.getActionCommand().equals(GProperty.res.getString("CULOARE_UMPLERE")) && choice.getText().equals("Other")) {
               ((JButton)e.getSource()).getParent().setVisible(false);
               ObjectGProperty.this.fill_color = JColorChooser.showDialog((Component)null, "Another color", Color.black);
            } else if (e.getActionCommand().equals(GProperty.res.getString("CULOARE_UMPLERE"))) {
               ObjectGProperty.this.fill_color = choice.getForeground();
            }

            if (e.getActionCommand().equals(GProperty.res.getString("FONT_TITLU"))) {
               ObjectGProperty.this.titlefont_size = Integer.parseInt(choice.getText());
            }

            if (e.getActionCommand().equals(GProperty.res.getString("FONT_CORP"))) {
               ObjectGProperty.this.bodyfont_size = Integer.parseInt(choice.getText());
            }

            ((JButton)e.getSource()).getParent().setVisible(false);
         }

         if (e.getSource() instanceof JMenuItem && e.getActionCommand().equals(GProperty.res.getString("NUME"))) {
            GraphCell cell = ObjectGProperty.this.getDiagramListener().source;
            if (cell instanceof ObjectCell) {
               InplaceEdit.getInstance().edit(((ObjectCell)cell).getName(), ((ObjectCell)cell).getLocation(), ((ObjectCell)cell).getUserObject());
            }
         }

         this.uniformize();
         ObjectGProperty.this.repaintCell(ObjectGProperty.this.getDiagramListener());
         ObjectGProperty.this.popup.getPopupMenu().setVisible(false);
         ObjectGProperty.this.popup_menu.setVisible(false);
      }
   }
}
