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

public class PackageGProperty extends DefaultGProperty {
   private transient PackageGProperty.PackageMenuElementListener listener;
   private Color fill_color;
   private int titlefont_size;

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.initMainPopup(GProperty.getStringsForItem("MENIU_PACHET"));
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

   protected PackageGProperty(ArrayList strings) {
      this.fill_color = Color.white;
      this.titlefont_size = 12;
      this.listener = new PackageGProperty.PackageMenuElementListener();
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
            this.buildColorMenu(menu, "fill_color");
         }

         if (key.equals("FONT_TITLU")) {
            this.buildFontMenu(menu, "title_font", 15, 20);
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
         if (key.equals("SUBMENIU_PACHET")) {
            this.popup_menu.add(this.popup);
            this.initPopup(GProperty.getStringsForItem("SUBMENIU_PACHET"));
         } else if (key.equals("-")) {
            this.popup_menu.addSeparator();
         } else {
            JMenuItem item = new JMenuItem(GProperty.res.getString(key).trim());
            this.popup_menu.add(item);
            item.addActionListener(this.listener);
         }
      }

   }

   class PackageMenuElementListener extends MouseAdapter implements ActionListener {
      PackageMenuElementListener() {
      }

      private void uniformize() {
         PackageGProperty.super.uniformize();
         ArrayList selection = PackageGProperty.this.getDiagramListener().getDiagram().getSelected();

         for(int i = 0; i < selection.size(); ++i) {
            PackageGProperty prop = null;
            if (selection.get(i) instanceof PackageCell) {
               prop = (PackageGProperty)((PackageCell)selection.get(i)).getProperty();
            }

            if (selection.get(i) instanceof UseCaseCell) {
               prop = (PackageGProperty)((UseCaseCell)selection.get(i)).getProperty();
            }

            prop.setFillColor(PackageGProperty.this.fill_color);
            prop.setTitleFontSize(PackageGProperty.this.titlefont_size);
         }

      }

      public void actionPerformed(ActionEvent e) {
         if (e.getSource() instanceof JButton) {
            JButton choice = (JButton)e.getSource();
            if (e.getActionCommand().equals("fill_color") && choice.getText().equals("Other")) {
               ((JButton)e.getSource()).getParent().setVisible(false);
               PackageGProperty.this.fill_color = JColorChooser.showDialog((Component)null, "Another color", Color.black);
            } else if (e.getActionCommand().equals("fill_color")) {
               PackageGProperty.this.fill_color = choice.getForeground();
            }

            if (e.getActionCommand().equals("title_font")) {
               PackageGProperty.this.titlefont_size = Integer.parseInt(choice.getText());
            }

            ((JButton)e.getSource()).getParent().setVisible(false);
         }

         if (e.getSource() instanceof JMenuItem && e.getActionCommand().equals(GProperty.res.getString("NUME"))) {
            GraphCell cell = PackageGProperty.this.getDiagramListener().source;
            if (cell instanceof PackageCell) {
               InplaceEdit.getInstance().edit(((PackageCell)cell).getName(), ((PackageCell)cell).getLocation(), ((PackageCell)cell).getUserObject());
            }

            if (cell instanceof UseCaseCell) {
               InplaceEdit.getInstance().edit(((UseCaseCell)cell).getName(), ((UseCaseCell)cell).getLocation(), ((UseCaseCell)cell).getUserObject());
            }
         }

         this.uniformize();
         PackageGProperty.this.repaintCell(PackageGProperty.this.getDiagramListener());
         PackageGProperty.this.popup.getPopupMenu().setVisible(false);
         PackageGProperty.this.popup_menu.setVisible(false);
      }
   }
}
