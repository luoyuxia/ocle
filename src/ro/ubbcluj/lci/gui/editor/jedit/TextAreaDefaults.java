package ro.ubbcluj.lci.gui.editor.jedit;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.swing.JPopupMenu;
import ro.ubbcluj.lci.gui.editor.AutoIndentFormatter;
import ro.ubbcluj.lci.gui.editor.SyntaxDocument;
import ro.ubbcluj.lci.gui.editor.options.EditorProperties;

public class TextAreaDefaults {
   private static TextAreaDefaults DEFAULTS;
   public InputHandler inputHandler;
   public SyntaxDocument document;
   public boolean editable;
   public boolean caretVisible;
   public boolean caretBlinks;
   public boolean blockCaret;
   public int electricScroll;
   public int cols;
   public int rows;
   public SyntaxStyle[] styles;
   public Color caretColor;
   public Color selectionColor;
   public Color lineHighlightColor;
   public boolean lineHighlight;
   public Color bracketHighlightColor;
   public boolean bracketHighlight;
   public Color eolMarkerColor;
   public boolean eolMarkers;
   public boolean paintInvalid;
   public JPopupMenu popup;

   public TextAreaDefaults() {
   }

   public static TextAreaDefaults getDefaults() {
      if (DEFAULTS == null) {
         DEFAULTS = new TextAreaDefaults();
         DEFAULTS.inputHandler = new DefaultInputHandler();
         DEFAULTS.inputHandler.addDefaultKeyBindings();
         DEFAULTS.document = new SyntaxDocument();
         DEFAULTS.editable = true;
         DEFAULTS.caretVisible = true;
         DEFAULTS.electricScroll = 3;
         DEFAULTS.cols = 80;
         DEFAULTS.rows = 25;
      }

      Properties defProps = EditorProperties.defProps;
      DEFAULTS.styles = SyntaxUtilities.getDefaultSyntaxStyles();
      if (defProps == null) {
         DEFAULTS.document.putProperty("tabSize", new Integer(4));
         DEFAULTS.caretBlinks = true;
         DEFAULTS.caretColor = Color.red;
         DEFAULTS.selectionColor = new Color(13421823);
         DEFAULTS.lineHighlightColor = new Color(14737632);
         DEFAULTS.lineHighlight = false;
         DEFAULTS.bracketHighlightColor = Color.LIGHT_GRAY;
         DEFAULTS.bracketHighlight = true;
         DEFAULTS.eolMarkerColor = new Color(39321);
         DEFAULTS.eolMarkers = false;
         DEFAULTS.paintInvalid = false;
      } else {
         DEFAULTS.document.putProperty("tabSize", new Integer(Integer.parseInt(defProps.getProperty("tab_size"))));
         AutoIndentFormatter.INDENT_SIZE = Integer.parseInt(defProps.getProperty("indent_size"));
         DEFAULTS.caretBlinks = defProps.getProperty("caret_blinks").equalsIgnoreCase("true");
         DEFAULTS.lineHighlight = defProps.getProperty("highlight_line").equalsIgnoreCase("true");
         DEFAULTS.bracketHighlight = defProps.getProperty("highlight_brackets").equalsIgnoreCase("true");
         DEFAULTS.eolMarkers = defProps.getProperty("eol_markers").equalsIgnoreCase("true");
         Set tokens = new HashSet(EditorProperties.tokenNames.keySet());
         Iterator it = tokens.iterator();

         while(it.hasNext()) {
            String token = (String)it.next();
            int tokenId = Integer.parseInt(EditorProperties.tokenNames.get(token).toString());
            if (tokenId >= EditorProperties.CARET) {
               switch(tokenId) {
               case 100:
                  DEFAULTS.caretColor = Color.decode(defProps.getProperty(token));
                  break;
               case 101:
                  DEFAULTS.bracketHighlightColor = Color.decode(defProps.getProperty(token));
                  break;
               case 102:
                  DEFAULTS.selectionColor = Color.decode(defProps.getProperty(token));
                  break;
               case 103:
                  DEFAULTS.lineHighlightColor = Color.decode(defProps.getProperty(token));
                  break;
               case 104:
                  DEFAULTS.eolMarkerColor = Color.decode(defProps.getProperty(token));
               }
            }
         }
      }

      return DEFAULTS;
   }
}
