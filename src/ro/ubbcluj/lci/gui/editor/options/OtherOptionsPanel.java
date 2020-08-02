package ro.ubbcluj.lci.gui.editor.options;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import ro.ubbcluj.lci.gui.editor.AutoIndentFormatter;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;

public class OtherOptionsPanel extends JPanel {
   protected static String[] tokens;
   protected static String[] align_tokens;
   JLabel tabSizeLabel = new JLabel();
   JComboBox tabSizeCb = new JComboBox();
   JLabel indentSizeLabel = new JLabel();
   JComboBox indentSizeCb = new JComboBox();
   private static byte[] sizes;
   JPanel indentPanel = new JPanel();
   TitledBorder titledBorder1;
   JPanel listPanel = new JPanel();
   TitledBorder titledBorder2;
   JScrollPane listScrollPane = new JScrollPane();
   JList tokenList = new JList();
   BorderLayout borderLayout1 = new BorderLayout();
   JPanel alignPanel = new JPanel();
   TitledBorder titledBorder3;
   JCheckBox ifEndifCb = new JCheckBox();
   JCheckBox packEndpackCb = new JCheckBox();
   JCheckBox enableCb = new JCheckBox();
   JButton restoreBtn = new JButton();
   JButton setAsDefaultBtn = new JButton();
   BorderLayout borderLayout2 = new BorderLayout();
   JScrollPane alignScrollPane = new JScrollPane();
   JList alignList = new JList();
   JPanel alignPairsPanel = new JPanel();
   TitledBorder titledBorder4;
   BorderLayout borderLayout3 = new BorderLayout();

   public OtherOptionsPanel() {
      try {
         this.jbInit();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   void jbInit() throws Exception {
      JEditTextArea editorTA = new JEditTextArea(EditorProperties.appProps);
      this.titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Auto-indentation options");
      this.titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Indent after:");
      this.titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Align corresponding statements");
      this.titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Align pairs");
      this.tabSizeLabel.setText("Tab size:");
      this.tabSizeLabel.setBounds(new Rectangle(19, 43, 53, 17));
      this.indentSizeLabel.setText("Block indent size:");
      this.indentSizeLabel.setBounds(new Rectangle(206, 33, 108, 17));
      this.setLayout((LayoutManager)null);
      this.tabSizeCb.setPreferredSize(new Dimension(50, 20));
      this.tabSizeCb.setEditable(true);
      this.tabSizeCb.setSelectedItem(String.valueOf(editorTA.getTabSize()));
      this.tabSizeCb.setBounds(new Rectangle(76, 41, 50, 20));

      int i;
      for(i = 0; i < sizes.length; ++i) {
         this.tabSizeCb.addItem(String.valueOf(sizes[i]));
      }

      this.tabSizeCb.addItemListener(new OtherOptionsPanel.MyItemListener());
      this.indentSizeCb.setPreferredSize(new Dimension(50, 20));
      this.indentSizeCb.setEditable(true);
      this.indentSizeCb.setSelectedItem(String.valueOf(AutoIndentFormatter.INDENT_SIZE));
      this.indentSizeCb.setBounds(new Rectangle(317, 31, 50, 20));

      for(i = 0; i < sizes.length; ++i) {
         this.indentSizeCb.addItem(String.valueOf(sizes[i]));
      }

      this.indentSizeCb.addItemListener(new OtherOptionsPanel.MyItemListener());
      this.restoreBtn.setBounds(new Rectangle(26, 357, 141, 27));
      this.restoreBtn.setText("Restore defaults");
      this.restoreBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            OtherOptionsPanel.this.adjustSelection(EditorProperties.defProps);
         }
      });
      this.setAsDefaultBtn.setBounds(new Rectangle(297, 356, 125, 27));
      this.setAsDefaultBtn.setText("Set as default");
      this.setAsDefaultBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            OtherOptionsPanel.this.setProps(EditorProperties.defProps);
            EditorProperties.saveDefProps(EditorProperties.defProps);
         }
      });
      this.indentPanel.setBorder(this.titledBorder1);
      this.indentPanel.setBounds(new Rectangle(14, 72, 425, 262));
      this.indentPanel.setLayout((LayoutManager)null);
      this.listPanel.setBorder(this.titledBorder2);
      this.listPanel.setBounds(new Rectangle(16, 60, 129, 185));
      this.listPanel.setLayout(this.borderLayout1);
      this.alignPanel.setBorder(this.titledBorder3);
      this.alignPanel.setBounds(new Rectangle(157, 60, 226, 107));
      this.alignPanel.setLayout(this.borderLayout2);
      this.enableCb.setText("Enable auto-indentation");
      this.enableCb.setBounds(new Rectangle(18, 29, 164, 25));
      this.enableCb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean enable = OtherOptionsPanel.this.enableCb.isSelected();
            OtherOptionsPanel.this.tokenList.setEnabled(enable);
            OtherOptionsPanel.this.indentSizeCb.setEnabled(enable);
         }
      });
      this.tokenList.setToolTipText("Press the Ctrl key to select more than one item");
      this.tokenList.setListData(tokens);
      this.alignList.setListData(align_tokens);
      this.ifEndifCb.setText("if - endif");
      this.packEndpackCb.setText("package - endpackage");
      this.alignPairsPanel.setBorder(this.titledBorder4);
      this.alignPairsPanel.setBounds(new Rectangle(157, 176, 226, 70));
      this.alignPairsPanel.setLayout(this.borderLayout3);
      this.indentPanel.add(this.indentSizeCb, (Object)null);
      this.indentPanel.add(this.indentSizeLabel, (Object)null);
      this.indentPanel.add(this.alignPanel, (Object)null);
      this.alignPanel.add(this.alignScrollPane, "Center");
      this.alignScrollPane.getViewport().add(this.alignList, (Object)null);
      this.listPanel.add(this.listScrollPane, "Center");
      this.indentPanel.add(this.listPanel, (Object)null);
      this.indentPanel.add(this.enableCb, (Object)null);
      this.listScrollPane.getViewport().add(this.tokenList, (Object)null);
      this.add(this.restoreBtn, (Object)null);
      this.add(this.setAsDefaultBtn, (Object)null);
      this.add(this.indentPanel, (Object)null);
      this.indentPanel.add(this.alignPairsPanel, (Object)null);
      this.alignPairsPanel.add(this.packEndpackCb, "North");
      this.alignPairsPanel.add(this.ifEndifCb, "Center");
      this.add(this.tabSizeLabel, (Object)null);
      this.add(this.tabSizeCb, (Object)null);
      this.adjustSelection(EditorProperties.appProps);
   }

   protected void setProps(Properties props) {
      props.put("tab_size", (String)this.tabSizeCb.getSelectedItem());
      props.put("indent_size", (String)this.indentSizeCb.getSelectedItem());
      props.put("enable_autoindent", String.valueOf(this.enableCb.isSelected()));
      props.put("align_ifendif", String.valueOf(this.ifEndifCb.isSelected()));
      props.put("align_packendpack", String.valueOf(this.packEndpackCb.isSelected()));

      int i;
      for(i = 0; i < tokens.length; ++i) {
         props.put("indent_".concat(tokens[i]), String.valueOf(this.tokenList.isSelectedIndex(i)));
      }

      for(i = 0; i < align_tokens.length; ++i) {
         props.put("align_".concat(align_tokens[i]), String.valueOf(this.alignList.isSelectedIndex(i)));
      }

   }

   private void adjustSelection(Properties props) {
      this.tabSizeCb.setSelectedItem(props.getProperty("tab_size", "4"));
      this.indentSizeCb.setSelectedItem(props.getProperty("indent_size", "4"));
      this.enableCb.setSelected(props.getProperty("enable_autoindent", "true").equalsIgnoreCase("true"));
      this.ifEndifCb.setSelected(props.getProperty("align_ifendif", "true").equalsIgnoreCase("true"));
      this.packEndpackCb.setSelected(props.getProperty("align_packendpack", "true").equalsIgnoreCase("true"));
      int[] indices = new int[tokens.length];

      int j;
      for(j = 0; j < tokens.length; ++j) {
         indices[j] = -1;
      }

      j = 0;

      int i;
      boolean sel;
      for(i = 0; i < tokens.length; ++i) {
         sel = props.getProperty("indent_".concat(tokens[i]), "true").equalsIgnoreCase("true");
         if (sel) {
            indices[j] = i;
            ++j;
         }
      }

      if (indices != null) {
         this.tokenList.setSelectedIndices(indices);
      }

      indices = new int[align_tokens.length];

      for(i = 0; i < align_tokens.length; ++i) {
         indices[i] = -1;
      }

      j = 0;

      for(i = 0; i < align_tokens.length; ++i) {
         sel = props.getProperty("align_".concat(tokens[i]), "true").equalsIgnoreCase("true");
         if (sel) {
            indices[j] = i;
            ++j;
         }
      }

      if (indices != null) {
         this.alignList.setSelectedIndices(indices);
      }

      if (!this.enableCb.isSelected()) {
         this.indentSizeCb.setEnabled(false);
         this.tokenList.setEnabled(false);
      }

   }

   static {
      tokens = AutoIndentFormatter.TOKENS;
      align_tokens = AutoIndentFormatter.ALIGN_TOKENS;
      sizes = new byte[]{2, 4, 6, 8};
   }

   private class MyItemListener implements ItemListener {
      private MyItemListener() {
      }

      public void itemStateChanged(ItemEvent e) {
         if (1 == e.getStateChange()) {
            JComboBox cb = (JComboBox)e.getSource();
            String message = "";
            String title;
            if (cb.equals(OtherOptionsPanel.this.indentSizeCb)) {
               message = message + "The indent";
               title = "Invalid indent size";
            } else {
               message = message + "The tab";
               title = "Invalid tab size";
            }

            message = message + " size must be an integer between 1 and 20.";

            try {
               byte tabSize = Byte.parseByte((String)cb.getSelectedItem());
               if (tabSize < 1 || tabSize > 20) {
                  JOptionPane.showMessageDialog(OtherOptionsPanel.this, message, title, 0);
                  throw new NumberFormatException();
               }
            } catch (NumberFormatException var6) {
               JOptionPane.showMessageDialog(OtherOptionsPanel.this, message, title, 0);
               cb.setSelectedItem("4");
            }
         }

      }
   }
}
