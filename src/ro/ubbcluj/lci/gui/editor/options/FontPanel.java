package ro.ubbcluj.lci.gui.editor.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;

public class FontPanel extends JPanel {
   JLabel fontLabel = new JLabel();
   JComboBox fontCb = new JComboBox();
   JLabel sizeLabel = new JLabel();
   JComboBox sizeCb = new JComboBox();
   JTextField preview = new JTextField();
   JLabel previewLabel = new JLabel();
   JButton restoreBtn = new JButton();
   JButton setAsDefaultBtn = new JButton();

   public FontPanel() {
      try {
         this.jbInit();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   void jbInit() throws Exception {
      JEditTextArea editorTA = new JEditTextArea(EditorProperties.appProps);
      Font currentFont = editorTA.getPainter().getFont();
      this.fontLabel.setPreferredSize(new Dimension(30, 17));
      this.fontLabel.setText("Font:");
      this.fontLabel.setBounds(new Rectangle(50, 44, 31, 17));
      this.setLayout((LayoutManager)null);
      this.sizeLabel.setText("Size:");
      this.sizeLabel.setBounds(new Rectangle(291, 44, 33, 17));
      this.fontCb.setPreferredSize(new Dimension(200, 21));
      GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
      Font[] fonts = gEnv.getAllFonts();
      String[] envFontNames = gEnv.getAvailableFontFamilyNames();

      int i;
      for(i = 0; i < envFontNames.length; ++i) {
         this.fontCb.addItem(envFontNames[i]);
      }

      this.fontCb.setSelectedItem(currentFont.getName());
      this.fontCb.setBounds(new Rectangle(90, 42, 188, 21));
      this.fontCb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox)e.getSource();
            String sel = (String)cb.getSelectedItem();
            Font newFont = new Font(sel, 0, Integer.parseInt((String)FontPanel.this.sizeCb.getSelectedItem()));
            FontPanel.this.preview.setFont(newFont);
         }
      });
      this.sizeCb.setPreferredSize(new Dimension(50, 21));

      for(i = 6; i < 15; ++i) {
         this.sizeCb.addItem(String.valueOf(i));
      }

      for(i = 8; i < 15; ++i) {
         this.sizeCb.addItem(String.valueOf(2 * i));
      }

      this.sizeCb.addItem(String.valueOf(36));
      this.sizeCb.addItem(String.valueOf(48));
      this.sizeCb.addItem(String.valueOf(72));
      this.sizeCb.setEditable(true);
      this.sizeCb.setSelectedItem(String.valueOf(currentFont.getSize()));
      this.sizeCb.setBounds(new Rectangle(324, 42, 56, 21));
      this.sizeCb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox)e.getSource();

            try {
               int size = Integer.parseInt((String)cb.getSelectedItem());
               if (size < 1 || size > 100) {
                  throw new NumberFormatException();
               }

               Font font = new Font((String)FontPanel.this.fontCb.getSelectedItem(), 0, size);
               FontPanel.this.preview.setFont(font);
            } catch (NumberFormatException var5) {
               cb.setSelectedItem("14");
            }

         }
      });
      this.sizeCb.addItemListener(new FontPanel.MyItemListener());
      this.preview.setBackground(Color.lightGray);
      this.preview.setEnabled(false);
      this.preview.setPreferredSize(new Dimension(300, 200));
      this.preview.setDisabledTextColor(Color.darkGray);
      this.preview.setEditable(false);
      this.preview.setText("Aa Bb Yy Zz");
      this.preview.setFont(new Font((String)this.fontCb.getSelectedItem(), 0, Integer.parseInt((String)this.sizeCb.getSelectedItem())));
      this.preview.setHorizontalAlignment(0);
      this.preview.setBounds(new Rectangle(46, 98, 334, 195));
      this.previewLabel.setPreferredSize(new Dimension(300, 17));
      this.previewLabel.setText("Preview:");
      this.previewLabel.setBounds(new Rectangle(45, 81, 300, 17));
      this.setPreferredSize(new Dimension(400, 400));
      this.restoreBtn.setBounds(new Rectangle(8, 330, 145, 27));
      this.restoreBtn.setText("Restore default");
      this.restoreBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Properties defProps = EditorProperties.defProps;
            FontPanel.this.fontCb.setSelectedItem(defProps.getProperty("font_name"));
            FontPanel.this.sizeCb.setSelectedItem(defProps.getProperty("font_size"));
         }
      });
      this.setAsDefaultBtn.setBounds(new Rectangle(271, 330, 145, 27));
      this.setAsDefaultBtn.setText("Set as default");
      this.setAsDefaultBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FontPanel.this.setProps(EditorProperties.defProps);
            EditorProperties.saveDefProps(EditorProperties.defProps);
         }
      });
      this.add(this.preview, (Object)null);
      this.add(this.sizeCb, (Object)null);
      this.add(this.previewLabel, (Object)null);
      this.add(this.restoreBtn, (Object)null);
      this.add(this.fontCb, (Object)null);
      this.add(this.fontLabel, (Object)null);
      this.add(this.sizeLabel, (Object)null);
      this.add(this.setAsDefaultBtn, (Object)null);
   }

   protected void setProps(Properties props) {
      props.put("font_name", (String)this.fontCb.getSelectedItem());
      props.put("font_size", (String)this.sizeCb.getSelectedItem());
   }

   private class MyItemListener implements ItemListener {
      private MyItemListener() {
      }

      public void itemStateChanged(ItemEvent e) {
         if (1 == e.getStateChange()) {
            String message = "The font size must be an integer between 1 and 100";
            String title = "Invalid font size";

            try {
               int fontSize = Integer.parseInt((String)FontPanel.this.sizeCb.getSelectedItem());
               if (fontSize < 1 || fontSize > 100) {
                  JOptionPane.showMessageDialog(FontPanel.this, message, title, 0);
               }
            } catch (NumberFormatException var5) {
               JOptionPane.showMessageDialog(FontPanel.this, message, title, 0);
            }
         }

      }
   }
}
