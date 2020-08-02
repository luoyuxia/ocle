package ro.ubbcluj.lci.gui.editor.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.editor.jedit.OCLTokenMarker;
import ro.ubbcluj.lci.gui.editor.jedit.SyntaxStyle;
import ro.ubbcluj.lci.gui.editor.jedit.TextAreaDefaults;

public class ColorPanel extends JPanel {
   private byte CARET;
   private byte BRACKET;
   private byte SELECTION;
   private byte LINE;
   private byte EOL;
   private HashMap tokenNames;
   JLabel chooseLabel;
   JList tokenList;
   JButton colorButton;
   JCheckBox boldCheckBox;
   JCheckBox italicCheckBox;
   JLabel previewLabel;
   JEditTextArea ta;
   SyntaxStyle[] styles;
   JScrollPane jScrollPane1;
   TitledBorder titledBorder1;
   TitledBorder titledBorder2;
   JCheckBox blinkCb;
   JCheckBox lineCb;
   JCheckBox bracketsCb;
   JCheckBox eolCb;
   JButton restoreBtn;
   JButton setAsDefaultBtn;

   public ColorPanel() {
      this.CARET = EditorProperties.CARET;
      this.BRACKET = EditorProperties.BRACKET;
      this.SELECTION = EditorProperties.SELECTION;
      this.LINE = EditorProperties.LINE;
      this.EOL = EditorProperties.EOL;
      this.tokenNames = (HashMap)EditorProperties.tokenNames.clone();
      this.chooseLabel = new JLabel();
      this.tokenList = new JList();
      this.colorButton = new JButton();
      this.boldCheckBox = new JCheckBox();
      this.italicCheckBox = new JCheckBox();
      this.previewLabel = new JLabel();
      this.ta = new JEditTextArea(EditorProperties.appProps);
      this.jScrollPane1 = new JScrollPane();
      this.blinkCb = new JCheckBox();
      this.lineCb = new JCheckBox();
      this.bracketsCb = new JCheckBox();
      this.eolCb = new JCheckBox();
      this.restoreBtn = new JButton();
      this.setAsDefaultBtn = new JButton();
      this.enableEvents(64L);

      try {
         this.jbInit();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void jbInit() throws Exception {
      this.styles = this.ta.getPainter().getStyles();
      JEditTextArea editorTA = new JEditTextArea(EditorProperties.appProps);
      this.chooseLabel.setText("Choose color for:");
      this.chooseLabel.setBounds(new Rectangle(10, 5, 102, 28));
      this.setLayout((LayoutManager)null);
      this.setSize(new Dimension(486, 411));
      this.setPreferredSize(new Dimension(500, 500));
      this.tokenList.setVisibleRowCount(5);
      this.tokenList.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
               ColorPanel.this.adjustButtonsForAttributes();
            }

         }
      });
      Object[] temp = this.tokenNames.keySet().toArray();
      Arrays.sort(temp);
      this.tokenList.setListData(temp);
      this.tokenList.setBorder(BorderFactory.createEtchedBorder());
      this.tokenList.setSelectedIndex(0);
      this.tokenList.setSelectionMode(0);
      this.colorButton.setBounds(new Rectangle(176, 38, 49, 22));
      this.colorButton.setBorder(BorderFactory.createRaisedBevelBorder());
      this.colorButton.setToolTipText("Press to choose a different color");
      this.colorButton.addActionListener(new ActionListener() {
         Color newColor;

         public void actionPerformed(ActionEvent e) {
            final JColorChooser cc = new JColorChooser(ColorPanel.this.colorButton.getBackground());
            cc.setPreviewPanel(new JPanel());
            ActionListener okListener = new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  newColor = cc.getColor();
               }
            };
            JDialog dialog = JColorChooser.createDialog(ColorPanel.this.colorButton, "Pick a color", true, cc, okListener, (ActionListener)null);
            dialog.show();
            if (this.newColor != null) {
               ColorPanel.this.colorButton.setBackground(this.newColor);
               String tokenName = (String)ColorPanel.this.tokenList.getSelectedValue();
               int tokenId = Integer.parseInt(ColorPanel.this.tokenNames.get(tokenName).toString());
               if (tokenId < ColorPanel.this.CARET) {
                  boolean isItalic = ColorPanel.this.styles[tokenId].isItalic();
                  boolean isBold = ColorPanel.this.styles[tokenId].isBold();
                  ColorPanel.this.styles[tokenId] = new SyntaxStyle(this.newColor, isItalic, isBold);
                  ColorPanel.this.ta.getPainter().setStyles(ColorPanel.this.styles);
               } else {
                  switch(tokenId) {
                  case 100:
                     ColorPanel.this.ta.getPainter().setCaretColor(this.newColor);
                     break;
                  case 101:
                     ColorPanel.this.ta.getPainter().setBracketHighlightColor(this.newColor);
                     break;
                  case 102:
                     ColorPanel.this.ta.getPainter().setSelectionColor(this.newColor);
                     break;
                  case 103:
                     ColorPanel.this.ta.getPainter().setLineHighlightColor(this.newColor);
                     break;
                  case 104:
                     ColorPanel.this.ta.getPainter().setEOLMarkerColor(this.newColor);
                  }
               }
            }

         }
      });
      this.boldCheckBox.setText("Bold");
      this.boldCheckBox.setBounds(new Rectangle(179, 70, 55, 25));
      this.boldCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean isBold = ((JCheckBox)e.getSource()).isSelected();
            String tokenName = (String)ColorPanel.this.tokenList.getSelectedValue();
            int tokenId = Integer.parseInt(ColorPanel.this.tokenNames.get(tokenName).toString());
            if (tokenId < ColorPanel.this.CARET) {
               boolean isItalic = ColorPanel.this.styles[tokenId].isItalic();
               Color color = ColorPanel.this.styles[tokenId].getColor();
               ColorPanel.this.styles[tokenId] = new SyntaxStyle(color, isItalic, isBold);
               ColorPanel.this.ta.getPainter().setStyles(ColorPanel.this.styles);
            }

         }
      });
      this.italicCheckBox.setText("Italic");
      this.italicCheckBox.setBounds(new Rectangle(179, 90, 53, 29));
      this.italicCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean isItalic = ((JCheckBox)e.getSource()).isSelected();
            String tokenName = (String)ColorPanel.this.tokenList.getSelectedValue();
            int tokenId = Integer.parseInt(ColorPanel.this.tokenNames.get(tokenName).toString());
            if (tokenId < ColorPanel.this.CARET) {
               boolean isBold = ColorPanel.this.styles[tokenId].isBold();
               Color color = ColorPanel.this.styles[tokenId].getColor();
               ColorPanel.this.styles[tokenId] = new SyntaxStyle(color, isItalic, isBold);
               ColorPanel.this.ta.getPainter().setStyles(ColorPanel.this.styles);
            }

         }
      });
      this.adjustButtonsForAttributes();
      this.previewLabel.setText("Preview:");
      this.previewLabel.setBounds(new Rectangle(9, 153, 78, 28));
      this.ta.setBounds(new Rectangle(9, 182, 457, 164));
      this.ta.setText("-- This is a one-line OCL comment \n/*This is a multiple-line \ncomment */\npackage P \n    context Company \n        inv invariant1: (a + b) * c < 10e10 \n        inv invariant2: self.name = 'LCI \n        inv invariant3: self.noOfEmployees > 50 \n        inv invariant4: self.type = #inc \nendpackage");
      this.ta.setFirstLine(0);
      this.ta.setTokenMarker(new OCLTokenMarker());
      this.ta.setSelectionStart(this.ta.getText().indexOf("invariant1"));
      this.ta.setSelectionEnd(this.ta.getText().indexOf("invariant1") + "invariant1".length());
      this.ta.setCaretPosition(this.ta.getText().indexOf(")") + 1);
      this.ta.setEditable(false);
      this.jScrollPane1.setBounds(new Rectangle(8, 31, 158, 120));
      this.blinkCb.setText("Blink caret");
      this.blinkCb.setBounds(new Rectangle(277, 39, 90, 25));
      this.blinkCb.setSelected(editorTA.isCaretBlinkEnabled());
      this.blinkCb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean sel = ((JCheckBox)e.getSource()).isSelected();
            ColorPanel.this.ta.setCaretBlinkEnabled(sel);
         }
      });
      this.lineCb.setText("Highlight current line");
      this.lineCb.setBounds(new Rectangle(277, 66, 146, 25));
      this.lineCb.setSelected(editorTA.getPainter().isLineHighlightEnabled());
      this.lineCb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean sel = ((JCheckBox)e.getSource()).isSelected();
            ColorPanel.this.ta.getPainter().setLineHighlightEnabled(sel);
         }
      });
      this.bracketsCb.setText("Highlight brackets");
      this.bracketsCb.setBounds(new Rectangle(277, 92, 138, 25));
      this.bracketsCb.setSelected(editorTA.getPainter().isBracketHighlightEnabled());
      this.bracketsCb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean sel = ((JCheckBox)e.getSource()).isSelected();
            ColorPanel.this.ta.getPainter().setBracketHighlightEnabled(sel);
         }
      });
      this.eolCb.setText("End of line markers visible");
      this.eolCb.setBounds(new Rectangle(277, 119, 166, 25));
      this.eolCb.setSelected(editorTA.getPainter().getEOLMarkersPainted());
      this.eolCb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean sel = ((JCheckBox)e.getSource()).isSelected();
            ColorPanel.this.ta.getPainter().setEOLMarkersPainted(sel);
         }
      });
      this.restoreBtn.setBounds(new Rectangle(8, 365, 138, 27));
      this.restoreBtn.setText("Restore defaults");
      this.restoreBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            TextAreaDefaults defaults = TextAreaDefaults.getDefaults();
            ColorPanel.this.blinkCb.setSelected(defaults.caretBlinks);
            ColorPanel.this.ta.setCaretBlinkEnabled(defaults.caretBlinks);
            ColorPanel.this.bracketsCb.setSelected(defaults.bracketHighlight);
            ColorPanel.this.ta.getPainter().setBracketHighlightEnabled(defaults.bracketHighlight);
            ColorPanel.this.eolCb.setSelected(defaults.eolMarkers);
            ColorPanel.this.ta.getPainter().setEOLMarkersPainted(defaults.eolMarkers);
            ColorPanel.this.lineCb.setSelected(defaults.lineHighlight);
            ColorPanel.this.ta.getPainter().setLineHighlightEnabled(defaults.lineHighlight);
            ColorPanel.this.ta.getPainter().setCaretColor(defaults.caretColor);
            ColorPanel.this.ta.getPainter().setBracketHighlightColor(defaults.bracketHighlightColor);
            ColorPanel.this.ta.getPainter().setEOLMarkerColor(defaults.eolMarkerColor);
            ColorPanel.this.ta.getPainter().setLineHighlightColor(defaults.lineHighlightColor);
            ColorPanel.this.ta.getPainter().setSelectionColor(defaults.selectionColor);
            ColorPanel.this.styles = defaults.styles;
            ColorPanel.this.ta.getPainter().setStyles(ColorPanel.this.styles);
            ColorPanel.this.adjustButtonsForAttributes();
         }
      });
      this.setAsDefaultBtn.setBounds(new Rectangle(328, 365, 138, 27));
      this.setAsDefaultBtn.setText("Set as default");
      this.setAsDefaultBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ColorPanel.this.setProps(EditorProperties.defProps);
            EditorProperties.saveDefProps(EditorProperties.defProps);
         }
      });
      this.add(this.chooseLabel, (Object)null);
      this.add(this.jScrollPane1, (Object)null);
      this.jScrollPane1.getViewport().add(this.tokenList, (Object)null);
      this.add(this.previewLabel, (Object)null);
      this.add(this.colorButton, (Object)null);
      this.add(this.boldCheckBox, (Object)null);
      this.add(this.italicCheckBox, (Object)null);
      this.add(this.restoreBtn, (Object)null);
      this.add(this.ta, (Object)null);
      this.add(this.blinkCb, (Object)null);
      this.add(this.lineCb, (Object)null);
      this.add(this.bracketsCb, (Object)null);
      this.add(this.eolCb, (Object)null);
      this.add(this.setAsDefaultBtn, (Object)null);
   }

   private void adjustButtonsForAttributes() {
      String tokenName = (String)this.tokenList.getSelectedValue();
      int tokenId = Integer.parseInt(this.tokenNames.get(tokenName).toString());
      if (tokenId < this.CARET) {
         this.colorButton.setBackground(this.styles[tokenId].getColor());
         this.boldCheckBox.setSelected(this.styles[tokenId].isBold());
         this.italicCheckBox.setSelected(this.styles[tokenId].isItalic());
      } else {
         switch(tokenId) {
         case 100:
            this.colorButton.setBackground(this.ta.getPainter().getCaretColor());
            break;
         case 101:
            this.colorButton.setBackground(this.ta.getPainter().getBracketHighlightColor());
            break;
         case 102:
            this.colorButton.setBackground(this.ta.getPainter().getSelectionColor());
            break;
         case 103:
            this.colorButton.setBackground(this.ta.getPainter().getLineHighlightColor());
            break;
         case 104:
            this.colorButton.setBackground(this.ta.getPainter().getEOLMarkerColor());
         }
      }

   }

   protected void setProps(Properties props) {
      Set tokens = new HashSet(this.tokenNames.keySet());
      Iterator it = tokens.iterator();

      while(it.hasNext()) {
         String token = (String)it.next();
         int tokenId = Integer.parseInt(this.tokenNames.get(token).toString());
         if (tokenId < this.CARET) {
            props.put(token, String.valueOf(this.styles[tokenId].getColor().getRGB()));
            props.put(token + "_bold", String.valueOf(this.styles[tokenId].isBold()));
            props.put(token + "_italic", String.valueOf(this.styles[tokenId].isItalic()));
         } else {
            switch(tokenId) {
            case 100:
               props.put(token, String.valueOf(this.ta.getPainter().getCaretColor().getRGB()));
               break;
            case 101:
               props.put(token, String.valueOf(this.ta.getPainter().getBracketHighlightColor().getRGB()));
               break;
            case 102:
               props.put(token, String.valueOf(this.ta.getPainter().getSelectionColor().getRGB()));
               break;
            case 103:
               props.put(token, String.valueOf(this.ta.getPainter().getLineHighlightColor().getRGB()));
               break;
            case 104:
               props.put(token, String.valueOf(this.ta.getPainter().getEOLMarkerColor().getRGB()));
            }
         }
      }

      props.put("caret_blinks", String.valueOf(this.blinkCb.isSelected()));
      props.put("highlight_line", String.valueOf(this.lineCb.isSelected()));
      props.put("highlight_brackets", String.valueOf(this.bracketsCb.isSelected()));
      props.put("eol_markers", String.valueOf(this.eolCb.isSelected()));
   }
}
