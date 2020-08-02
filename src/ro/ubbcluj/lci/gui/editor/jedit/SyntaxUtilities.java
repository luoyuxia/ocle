package ro.ubbcluj.lci.gui.editor.jedit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;
import ro.ubbcluj.lci.gui.editor.options.EditorProperties;

public class SyntaxUtilities {
   public static boolean regionMatches(boolean ignoreCase, Segment text, int offset, String match) {
      int length = offset + match.length();
      char[] textArray = text.array;
      if (length > text.offset + text.count) {
         return false;
      } else {
         int i = offset;

         for(int j = 0; i < length; ++j) {
            char c1 = textArray[i];
            char c2 = match.charAt(j);
            if (ignoreCase) {
               c1 = Character.toUpperCase(c1);
               c2 = Character.toUpperCase(c2);
            }

            if (c1 != c2) {
               return false;
            }

            ++i;
         }

         return true;
      }
   }

   public static boolean regionMatches(boolean ignoreCase, Segment text, int offset, char[] match) {
      int length = offset + match.length;
      char[] textArray = text.array;
      if (length > text.offset + text.count) {
         return false;
      } else {
         int i = offset;

         for(int j = 0; i < length; ++j) {
            char c1 = textArray[i];
            char c2 = match[j];
            if (ignoreCase) {
               c1 = Character.toUpperCase(c1);
               c2 = Character.toUpperCase(c2);
            }

            if (c1 != c2) {
               return false;
            }

            ++i;
         }

         return true;
      }
   }

   public static SyntaxStyle[] getDefaultSyntaxStyles() {
      SyntaxStyle[] styles = new SyntaxStyle[11];
      Properties defProps = EditorProperties.defProps;
      styles[0] = new SyntaxStyle(Color.blue.brighter(), false, false);
      styles[1] = new SyntaxStyle(Color.green.darker().darker(), true, false);
      styles[2] = new SyntaxStyle(Color.green.darker().darker(), true, false);
      styles[6] = new SyntaxStyle(Color.black, false, true);
      styles[7] = new SyntaxStyle(Color.magenta, false, false);
      styles[8] = new SyntaxStyle(new Color(38400), false, false);
      styles[3] = new SyntaxStyle(Color.cyan.darker(), false, false);
      styles[4] = new SyntaxStyle(Color.cyan.darker(), false, true);
      styles[5] = new SyntaxStyle(new Color(10027059), false, true);
      styles[9] = new SyntaxStyle(Color.black, false, true);
      styles[10] = new SyntaxStyle(Color.red, false, true);
      if (defProps != null) {
         Set tokens = new HashSet(EditorProperties.tokenNames.keySet());
         Iterator it = tokens.iterator();

         while(it.hasNext()) {
            String token = (String)it.next();
            int tokenId = Integer.parseInt(EditorProperties.tokenNames.get(token).toString());
            if (tokenId < EditorProperties.CARET && defProps.getProperty(token) != null) {
               Color color = Color.decode(defProps.getProperty(token));
               boolean bold = defProps.getProperty(token + "_bold").equalsIgnoreCase("true");
               boolean italic = defProps.getProperty(token + "_italic").equalsIgnoreCase("true");
               styles[tokenId] = new SyntaxStyle(color, italic, bold);
            }
         }
      }

      return styles;
   }

   public static int paintSyntaxLine(Segment line, Token tokens, SyntaxStyle[] styles, TabExpander expander, Graphics gfx, int x, int y) {
      Font defaultFont = gfx.getFont();
      Color defaultColor = gfx.getColor();
      int offset = 0;

      while(true) {
         byte id = tokens.id;
         if (id == 127) {
            return x;
         }

         int length = tokens.length;
         styles[id].setGraphicsFlags(gfx, defaultFont);
         line.count = length;
         x = Utilities.drawTabbedText(line, x, y, gfx, expander, 0);
         line.offset += length;
         offset += length;
         tokens = tokens.next;
      }
   }

   private SyntaxUtilities() {
   }
}
