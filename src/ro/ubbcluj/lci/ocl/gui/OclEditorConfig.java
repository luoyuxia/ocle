package ro.ubbcluj.lci.ocl.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.io.IOException;
import java.io.Serializable;

public class OclEditorConfig implements Serializable {
   String fontName = "Courier New";
   int fontSize = 12;
   int lineSpacing = 0;
   int colSpacing = 0;
   int tokenCount = 8;
   Color[] tokenColors;
   boolean[] tokenBold;
   boolean[] tokenItalic;
   Font[] tokenFonts;
   Font font;
   int charWidth;
   int charHeight;
   boolean showMargin;
   int marginWidth;
   Color marginColor;
   Color lineNumberColor;
   boolean caretLine;
   Color caretLineColor;
   Color caretLineBorder;
   Color caretColor;
   boolean bracketMatching;
   Color bracketColor;
   Color bracketBorder;
   Color selectionColor;
   Color selectionBorder;
   boolean border;
   int borderWidth;
   Color borderColor;
   Color backgroundColor;
   int surroundWidth;
   Color surroundColor;
   int topSpacing;
   int bottomSpacing;
   int leftSpacing;
   int rightSpacing;
   int tabSize;
   int indentSize;
   private static final String spaces = "                                                                    ";
   String tabText;
   String indentText;
   String[] indentKeywords;
   String[][] indentPairs;
   String[][] indentAlligns;

   OclEditorConfig(Graphics g, String filename) throws IOException {
      this.tokenColors = new Color[]{Color.black, Color.red, new Color(0, 160, 0), Color.black, Color.blue, new Color(0, 160, 0), Color.gray, Color.magenta};
      this.tokenBold = new boolean[]{true, false, false, false, false, false, false, true};
      this.tokenItalic = new boolean[]{false, false, false, false, false, false, true, true};
      this.tokenFonts = new Font[this.tokenCount];
      this.showMargin = true;
      this.marginWidth = 30;
      this.marginColor = new Color(230, 230, 230);
      this.lineNumberColor = new Color(110, 110, 180);
      this.caretLine = true;
      this.caretLineColor = new Color(255, 225, 225);
      this.caretLineBorder = new Color(255, 170, 170);
      this.caretColor = Color.red;
      this.bracketMatching = true;
      this.bracketColor = new Color(150, 220, 220);
      this.bracketBorder = new Color(130, 200, 200);
      this.selectionColor = new Color(220, 220, 255);
      this.selectionBorder = new Color(190, 190, 235);
      this.border = true;
      this.borderWidth = 1;
      this.borderColor = Color.LIGHT_GRAY;
      this.backgroundColor = new Color(245, 250, 255);
      this.surroundWidth = 1;
      this.surroundColor = Color.gray;
      this.topSpacing = 1;
      this.bottomSpacing = 1;
      this.leftSpacing = 2;
      this.rightSpacing = 1;
      this.tabSize = 3;
      this.indentSize = 3;
      this.indentKeywords = new String[]{"model", "package", "context", "inv", "pre", "post", "def", "if"};
      this.indentPairs = new String[][]{{"model", "endmodel"}, {"package", "endpackage"}};
      this.indentAlligns = new String[][]{{"model"}, {"package"}, {"context"}, {"inv", "pre", "post", "def"}, {"let"}, {"if"}};
      this.load(filename);
      this.init(g);
   }

   OclEditorConfig(Graphics g) {
      this.tokenColors = new Color[]{Color.black, Color.red, new Color(0, 160, 0), Color.black, Color.blue, new Color(0, 160, 0), Color.gray, Color.magenta};
      this.tokenBold = new boolean[]{true, false, false, false, false, false, false, true};
      this.tokenItalic = new boolean[]{false, false, false, false, false, false, true, true};
      this.tokenFonts = new Font[this.tokenCount];
      this.showMargin = true;
      this.marginWidth = 30;
      this.marginColor = new Color(230, 230, 230);
      this.lineNumberColor = new Color(110, 110, 180);
      this.caretLine = true;
      this.caretLineColor = new Color(255, 225, 225);
      this.caretLineBorder = new Color(255, 170, 170);
      this.caretColor = Color.red;
      this.bracketMatching = true;
      this.bracketColor = new Color(150, 220, 220);
      this.bracketBorder = new Color(130, 200, 200);
      this.selectionColor = new Color(220, 220, 255);
      this.selectionBorder = new Color(190, 190, 235);
      this.border = true;
      this.borderWidth = 1;
      this.borderColor = Color.LIGHT_GRAY;
      this.backgroundColor = new Color(245, 250, 255);
      this.surroundWidth = 1;
      this.surroundColor = Color.gray;
      this.topSpacing = 1;
      this.bottomSpacing = 1;
      this.leftSpacing = 2;
      this.rightSpacing = 1;
      this.tabSize = 3;
      this.indentSize = 3;
      this.indentKeywords = new String[]{"model", "package", "context", "inv", "pre", "post", "def", "if"};
      this.indentPairs = new String[][]{{"model", "endmodel"}, {"package", "endpackage"}};
      this.indentAlligns = new String[][]{{"model"}, {"package"}, {"context"}, {"inv", "pre", "post", "def"}, {"let"}, {"if"}};
      this.init(g);
   }

   OclEditorConfig() {
      this.tokenColors = new Color[]{Color.black, Color.red, new Color(0, 160, 0), Color.black, Color.blue, new Color(0, 160, 0), Color.gray, Color.magenta};
      this.tokenBold = new boolean[]{true, false, false, false, false, false, false, true};
      this.tokenItalic = new boolean[]{false, false, false, false, false, false, true, true};
      this.tokenFonts = new Font[this.tokenCount];
      this.showMargin = true;
      this.marginWidth = 30;
      this.marginColor = new Color(230, 230, 230);
      this.lineNumberColor = new Color(110, 110, 180);
      this.caretLine = true;
      this.caretLineColor = new Color(255, 225, 225);
      this.caretLineBorder = new Color(255, 170, 170);
      this.caretColor = Color.red;
      this.bracketMatching = true;
      this.bracketColor = new Color(150, 220, 220);
      this.bracketBorder = new Color(130, 200, 200);
      this.selectionColor = new Color(220, 220, 255);
      this.selectionBorder = new Color(190, 190, 235);
      this.border = true;
      this.borderWidth = 1;
      this.borderColor = Color.LIGHT_GRAY;
      this.backgroundColor = new Color(245, 250, 255);
      this.surroundWidth = 1;
      this.surroundColor = Color.gray;
      this.topSpacing = 1;
      this.bottomSpacing = 1;
      this.leftSpacing = 2;
      this.rightSpacing = 1;
      this.tabSize = 3;
      this.indentSize = 3;
      this.indentKeywords = new String[]{"model", "package", "context", "inv", "pre", "post", "def", "if"};
      this.indentPairs = new String[][]{{"model", "endmodel"}, {"package", "endpackage"}};
      this.indentAlligns = new String[][]{{"model"}, {"package"}, {"context"}, {"inv", "pre", "post", "def"}, {"let"}, {"if"}};
      this.init((Graphics)null);
   }

   private void init(Graphics g) {
      this.font = new Font(this.fontName, 0, this.fontSize);
      if (g != null) {
         FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
         Rectangle r = this.font.getMaxCharBounds(frc).getBounds();
         this.charWidth = r.width;
         this.charHeight = r.height;
      }

      for(int i = 0; i < this.tokenCount; ++i) {
         this.tokenFonts[i] = new Font(this.fontName, (this.tokenBold[i] ? 1 : 0) + (this.tokenItalic[i] ? 2 : 0), this.fontSize);
      }

      this.tabText = "                                                                    ".substring(0, this.tabSize);
      this.indentText = "                                                                    ".substring(0, this.indentSize);
   }

   private void load(String filename) throws IOException {
   }

   private void save() throws IOException {
   }
}
