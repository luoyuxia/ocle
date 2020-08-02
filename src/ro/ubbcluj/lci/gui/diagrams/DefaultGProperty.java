package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.DefaultGraphCell;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.PropertyResourceBundle;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

public class DefaultGProperty extends GProperty {
   private transient DefaultGProperty.MenuElementListener listener;
   protected transient JMenu popup;
   protected transient JPopupMenu popup_menu;
   protected static final Color[] colors;
   protected static final int MAX_WIDTH = 5;
   private Color outline_color;
   private int outline_width;
   private Color selection_color;

   public void setOutlineColor(Color newColor) {
      this.outline_color = newColor;
   }

   public Color getOutlineColor() {
      return this.outline_color;
   }

   public void setOutlineWidth(int newWidth) {
      this.outline_width = newWidth;
   }

   public int getOutlineWidth() {
      return this.outline_width;
   }

   public void setSelectionColor(Color newColor) {
      this.selection_color = newColor;
   }

   public Color getSelectionColor() {
      return this.selection_color;
   }

   private static ArrayList parseDefaultKeys() {
      ArrayList result = new ArrayList();

      try {
         result.add(new String("CULOARE_CONTUR"));
         result.add(new String("GROSIME_CONTUR"));
         result.add(new String("SELECTIE_CULOARE"));
      } catch (NullPointerException var2) {
         System.err.println("Nu gases o resursa default pentru popup !" + var2.getLocalizedMessage());
      }

      return result;
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      if (res == null) {
         try {
            res = new PropertyResourceBundle((GProperty.class).getResourceAsStream("gproperty_en.properties"));
         } catch (Exception var3) {
         }
      }

      this.initPopup(parseDefaultKeys());
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

   private void buildWidthMenu(JMenu menu) {
      for(int i = 1; i <= 5; ++i) {
         JButton b = new JButton((new Integer(i)).toString());
         b.setActionCommand("Grosime contur");
         b.setSize(20, 20);
         b.addActionListener(this.listener);
         menu.add(b);
      }

   }

   private void checkSubmenu(String key, JMenu menu) {
      if (key.equals("CULOARE_CONTUR")) {
         this.buildColorMenu(menu, "outline_color");
      }

      if (key.equals("GROSIME_CONTUR")) {
         this.buildWidthMenu(menu);
      }

      if (key.equals("SELECTIE_CULOARE")) {
         this.buildColorMenu(menu, "selection_color");
      }

   }

   private void initPopup(ArrayList strings) {
      this.popup = new JMenu(GProperty.res.getString("PROPRIETATI_GR"));
      this.popup_menu = new JPopupMenu();
      this.popup_menu.setLightWeightPopupEnabled(true);
      this.listener = new DefaultGProperty.MenuElementListener();

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

   protected DefaultGProperty() {
      this.outline_color = Color.black;
      this.outline_width = 1;
      this.selection_color = Color.red;
      this.initPopup(parseDefaultKeys());
   }

   public JPopupMenu getPopup() {
      return this.popup_menu;
   }

   public JMenu getPropertyMenu() {
      return this.popup;
   }

   DiagramListener getDiagramListener() {
      MouseListener[] listeners = this.popup_menu.getMouseListeners();

      for(int i = 0; i < listeners.length; ++i) {
         if (listeners[i] instanceof DiagramListener) {
            return (DiagramListener)listeners[i];
         }
      }

      return null;
   }

   void uniformize() {
      ArrayList selection = this.getDiagramListener().getDiagram().getSelected();

      for(int i = 0; i < selection.size(); ++i) {
         DefaultGraphCell cell = (DefaultGraphCell)selection.get(i);
         DefaultGProperty prop = null;
         if (cell instanceof ClassCell) {
            prop = (DefaultGProperty)((ClassCell)cell).getProperty();
         }

         if (cell instanceof PackageCell) {
            prop = (DefaultGProperty)((PackageCell)cell).getProperty();
         }

         if (cell instanceof ActorCell) {
            prop = (DefaultGProperty)((ActorCell)cell).getProperty();
         }

         if (cell instanceof UseCaseCell) {
            prop = (DefaultGProperty)((UseCaseCell)cell).getProperty();
         }

         if (cell instanceof ObjectCell) {
            prop = (DefaultGProperty)((ObjectCell)cell).getProperty();
         }

         if (prop != null && prop != this) {
            prop.setOutlineColor(this.getOutlineColor());
            prop.setOutlineWidth(this.getOutlineWidth());
            prop.setSelectionColor(this.getSelectionColor());
         }
      }

   }

   static {
      colors = new Color[]{Color.black, Color.blue, Color.cyan, Color.darkGray, Color.gray, Color.green, Color.magenta, Color.orange, Color.pink, Color.red, Color.white, Color.yellow};
   }

   class MenuElementListener extends MouseAdapter implements ActionListener {
      MenuElementListener() {
      }

      public void actionPerformed(ActionEvent e) {
         if (e.getSource() instanceof JButton) {
            JButton choice = (JButton)e.getSource();
            if (e.getActionCommand().equals("outline_color") && choice.getText().equals("Other")) {
               ((JButton)e.getSource()).getParent().setVisible(false);
               DefaultGProperty.this.outline_color = JColorChooser.showDialog((Component)null, "Another color", Color.black);
            } else if (e.getActionCommand().equals("outline_color")) {
               DefaultGProperty.this.outline_color = choice.getForeground();
            }

            if (e.getActionCommand().equals("Grosime contur")) {
               DefaultGProperty.this.outline_width = Integer.parseInt(choice.getText());
            }

            if (e.getActionCommand().equals("selection_color") && choice.getText().equals("Other")) {
               DefaultGProperty.this.selection_color = JColorChooser.showDialog((Component)null, "Another color", Color.black);
            } else if (e.getActionCommand().equals("selection_color")) {
               DefaultGProperty.this.selection_color = choice.getForeground();
            }

            ((JButton)e.getSource()).getParent().setVisible(false);
         }

         DefaultGProperty.this.uniformize();
         DefaultGProperty.this.repaintCell(DefaultGProperty.this.getDiagramListener());
         DefaultGProperty.this.popup.getPopupMenu().setVisible(false);
         DefaultGProperty.this.popup_menu.setVisible(false);
      }
   }
}
