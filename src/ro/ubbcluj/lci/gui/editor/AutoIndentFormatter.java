package ro.ubbcluj.lci.gui.editor;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;
import ro.ubbcluj.lci.gui.editor.jedit.InputHandler;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.editor.jedit.OCLTokenMarker;
import ro.ubbcluj.lci.gui.editor.options.EditorProperties;
import ro.ubbcluj.lci.gui.editor.undo.DeleteEdit;
import ro.ubbcluj.lci.gui.editor.undo.InsertionEdit;

public class AutoIndentFormatter {
   public static int INDENT_SIZE = 4;
   public static final String[] TOKENS = new String[]{"package", "context", "def", "let", "pre", "post", "inv", "if", "then", "else"};
   public static final String[] ALIGN_TOKENS = new String[]{"package", "context", "def", "let", "inv"};

   public AutoIndentFormatter() {
   }

   public void newLineAndIndent(JEditTextArea ta, KeyEvent evt) {
      if (EditorProperties.appProps.getProperty("enable_autoindent", "true").equalsIgnoreCase("true")) {
         int currentLine = ta.getCaretLine();
         int currentCaretPos = ta.getCaretPosition();
         String prevLineText = ta.getLineText(currentLine - 1);
         int i = 0;

         String insertText;
         for(insertText = ""; i < prevLineText.length() && Character.isWhitespace(prevLineText.charAt(i)); ++i) {
            insertText = insertText.concat(String.valueOf(prevLineText.charAt(i)));
         }

         int newIndent = this.getCorrectIndentation(prevLineText.trim());
         if (newIndent == 1) {
            for(int j = 0; j < INDENT_SIZE; ++j) {
               insertText = insertText.concat(" ");
            }
         }

         if (!insertText.equals("")) {
            InsertionEdit te = new InsertionEdit(ta, currentCaretPos, insertText);
            ta.getDocument().addUndoableEdit(te);
            te.execute();
         }
      }

   }

   protected int getCorrectIndentation(String prevLineText) {
      for(int i = 0; i < TOKENS.length; ++i) {
         StringTokenizer st = new StringTokenizer(prevLineText, OCLTokenMarker.getDelimiters());

         while(st.hasMoreTokens()) {
            if (st.nextToken().equals(TOKENS[i]) && EditorProperties.appProps.getProperty("indent_".concat(TOKENS[i]), "true").equalsIgnoreCase("true")) {
               if (TOKENS[i].equals("if")) {
                  int nrIf = 0;
                  int nrEndif = 0;
                  StringTokenizer st2 = new StringTokenizer(prevLineText, OCLTokenMarker.getDelimiters());

                  while(st2.hasMoreTokens()) {
                     String token = st2.nextToken();
                     if (token.equals("if")) {
                        ++nrIf;
                     } else if (token.equals("endif")) {
                        ++nrEndif;
                     }
                  }

                  if (nrIf > nrEndif) {
                     return 1;
                  }

                  return 0;
               }

               return 1;
            }
         }
      }

      return 0;
   }

   protected boolean searchDef(String prevLineText) {
      int i = prevLineText.lastIndexOf("pre");
      if (i == -1) {
         i = prevLineText.lastIndexOf("post");
         if (i == -1) {
            i = prevLineText.lastIndexOf("inv");
            if (i == -1) {
               i = prevLineText.lastIndexOf("def");
            }
         }
      }

      if (i == -1) {
         return false;
      } else {
         if (prevLineText.substring(i, i + 3).equals("pos")) {
            i += 4;
         } else {
            i += 3;
         }

         while(i < prevLineText.length() && prevLineText.charAt(i) != ':') {
            ++i;
         }

         return i == prevLineText.length() - 1 && prevLineText.charAt(i) == ':';
      }
   }

   public void backspaceAndIndent(InputHandler ih, KeyEvent evt, ActionListener o) {
      JEditTextArea ta = InputHandler.getTextArea(evt);
      int caretPosition = ta.getCaretPosition();
      int currentLineNumber = ta.getCaretLine();
      String currentLineText = ta.getLineText(currentLineNumber);
      int currentLineCaretOffset = ta.getCaretPosition() - ta.getLineStartOffset(currentLineNumber);
      if (ta.containsOnlyWhitespace(currentLineNumber, ta.getCaretPosition()) && currentLineText.length() != 0) {
         int n;
         for(n = ta.getCaretLine() - 1; n >= 0 && (ta.containsOnlyWhitespace(n) || ta.getLineOffsetX(n) >= ta.offsetToX(currentLineNumber, currentLineCaretOffset)); --n) {
         }

         String delText;
         int p;
         if (n < 0) {
            int xForCurrentLine = ta.offsetToX(currentLineNumber, ta.getCaretPosition() - ta.getLineStartOffset(ta.getCaretLine()));
            if (xForCurrentLine == 0) {
               ih.executeAction(o, evt.getSource(), (String)null);
            } else {
               delText = "";

               while(ta.offsetToX(currentLineNumber, ta.getCaretPosition() - ta.getLineStartOffset(ta.getCaretLine())) > 0) {
                  p = ta.getCaretPosition();
                  String str;
                  DeleteEdit de = new DeleteEdit(ta, false, 1, p, str = ta.getText(p - 1, 1));
                  delText = delText.concat(str);
                  de.execute();
               }

               if (!delText.equals("")) {
                  DeleteEdit de = new DeleteEdit(ta, false, 1, caretPosition, delText);
                  ta.getDocument().addUndoableEdit(de);
               }
            }
         } else {
             delText = "";
            int i = ta.getLineOffset(n);
            p = ta.offsetToX(n, i);

            while(ta.offsetToX(currentLineNumber, ta.getCaretPosition() - ta.getLineStartOffset(ta.getCaretLine())) > p) {
                p = ta.getCaretPosition();
               DeleteEdit de = new DeleteEdit(ta, false, 1, p, delText = ta.getText(p - 1, 1));
               delText = delText.concat(delText);
               de.execute();
            }

            if (!delText.equals("")) {
               DeleteEdit de = new DeleteEdit(ta, false, 1, caretPosition, delText);
               ta.getDocument().addUndoableEdit(de);
            }
         }

      } else {
         ih.executeAction(o, evt.getSource(), (String)null);
      }
   }

   public int specialWord(JEditTextArea ta, KeyEvent evt) {
      int caretOffset = ta.getCaretPosition();
      int currentLineNumber = ta.getCaretLine();
      int i = caretOffset;

      for(int length = ta.getDocumentLength(); i < length && !OCLTokenMarker.isDelimiter(ta.getText(i, 1).charAt(0)); ++i) {
      }

      String textBefore = ta.getText(0, i);
      if (textBefore.endsWith("endpackage") && EditorProperties.appProps.getProperty("align_packendpack", "true").equalsIgnoreCase("true")) {
         return this.alignPair("package", "endpackage", ta, i, evt, currentLineNumber);
      } else if (textBefore.endsWith("endif") && EditorProperties.appProps.getProperty("align_ifendif", "true").equalsIgnoreCase("true")) {
         return this.alignPair("if", "endif", ta, i, evt, currentLineNumber);
      } else if (textBefore.endsWith("inv") && EditorProperties.appProps.getProperty("align_inv", "true").equalsIgnoreCase("true")) {
         return this.alignPair("inv", "inv", ta, caretOffset, evt, currentLineNumber);
      } else {
         for(i = 0; i < ALIGN_TOKENS.length; ++i) {
            if (textBefore.endsWith(ALIGN_TOKENS[i]) && EditorProperties.appProps.getProperty("align_".concat(ALIGN_TOKENS[i]), "true").equalsIgnoreCase("true")) {
               return this.alignPair(ALIGN_TOKENS[i], ALIGN_TOKENS[i], ta, caretOffset, evt, currentLineNumber);
            }
         }

         return 0;
      }
   }

   protected int alignPair(String first, String second, JEditTextArea ta, int caretOffset, KeyEvent evt, int currentLineNumber) {
      int number = 0;
      int lineNumber = ta.getCaretLine();
      if (ta.containsOnlyWhitespace(lineNumber, caretOffset - second.length())) {
         int n = lineNumber - 1;
         boolean nextIsGood = true;

         String text;
         boolean existsFirst;
         label144:
         while(true) {
            while(true) {
               while(true) {
                  if (n < 0) {
                     break label144;
                  }

                  text = ta.getLineText(n);
                  StringTokenizer st = new StringTokenizer(text, OCLTokenMarker.getDelimiters());
                  if (!first.equals(second)) {
                     existsFirst = false;

                     while(st.hasMoreTokens() && !existsFirst) {
                        String nextToken = st.nextToken();
                        if (nextToken.equals(second)) {
                           existsFirst = true;
                        }

                        if (nextToken.equals(first)) {
                           existsFirst = true;
                        }
                     }

                     if (existsFirst) {
                        if (!existsFirst) {
                           nextIsGood = false;
                           --n;
                        } else {
                           --n;
                        }
                     } else if (existsFirst && !nextIsGood) {
                        nextIsGood = true;
                        --n;
                     } else {
                        if (existsFirst) {
                           break label144;
                        }

                        --n;
                     }
                  } else {
                     existsFirst = false;

                     while(st.hasMoreTokens() && !existsFirst) {
                        if (st.nextToken().equals(first)) {
                           existsFirst = true;
                        }
                     }

                     if (existsFirst) {
                        break label144;
                     }

                     --n;
                  }
               }
            }
         }

         if (n >= 0) {
            text = ta.getLineText(n);
            int i = 0;
            int index = 0;

            for(existsFirst = false; !existsFirst; i = index) {
               index = text.indexOf(first, i);
               if ((index == 0 || index > 0 && (Character.isWhitespace(text.charAt(index - 1)) || OCLTokenMarker.isOperator(text.charAt(index - 1)))) && (index + first.length() == text.length() || index + first.length() < text.length() && (Character.isWhitespace(text.charAt(index + first.length())) || OCLTokenMarker.isOperator(text.charAt(index + first.length()))))) {
                  existsFirst = true;
               }
            }

            int xForN = ta.offsetToX(n, index);
            ta.setCaretPosition(caretOffset - second.length());
            String insertText;
            if (ta.offsetToX(currentLineNumber, ta.getLineText(currentLineNumber).indexOf(second)) > xForN) {
               insertText = "";
               int caretInitialPos = ta.getCaretPosition();

               while(ta.offsetToX(currentLineNumber, ta.getLineText(currentLineNumber).indexOf(second)) > xForN) {
                  int p = ta.getCaretPosition();
                  String str;
                  DeleteEdit de = new DeleteEdit(ta, false, 1, p, str = ta.getText(p - 1, 1));
                  insertText = insertText.concat(str);
                  de.execute();
               }

               if (!insertText.equals("")) {
                  DeleteEdit de = new DeleteEdit(ta, false, 1, caretInitialPos, insertText, "alignment");
                  ta.getDocument().addUndoableEdit(de);
               }
            } else if (ta.offsetToX(currentLineNumber, ta.getLineText(currentLineNumber).indexOf(second)) < xForN) {
               insertText = "";
               int caretInitialPos = ta.getCaretPosition();

               InsertionEdit te;
               while(ta.offsetToX(currentLineNumber, ta.getLineText(currentLineNumber).indexOf(second)) < xForN) {
                  te = new InsertionEdit(ta, ta.getCaretPosition(), " ");
                  insertText = insertText.concat(" ");
                  te.execute();
               }

               if (!insertText.equals("")) {
                  te = new InsertionEdit(ta, caretInitialPos, insertText, "alignment");
                  ta.getDocument().addUndoableEdit(te);
               }
            }

            ta.setCaretPosition(ta.getCaretPosition() + second.length());
         }
      }

      return number;
   }

   public static void reformatIndentation(JEditTextArea ta) {
      RelativeIndentation ri = new RelativeIndentation(ta);
      if (ta.getLineCount() >= 2 && ri.relPoz[1]) {
         reindent(1, 0, ta, true);
      }

      for(int i = 2; i < ta.getLineCount(); ++i) {
         if (ri.refLines[i] != -1) {
            reindent(i, ri.refLines[i], ta, ri.relPoz[i]);
         }
      }

      ta.setCaretPosition(0);
   }

   private static void reindent(int currentLine, int refLine, JEditTextArea ta, boolean indentRight) {
      int aux = 0;

      for(String text = ta.getLineText(currentLine); aux < text.length() && Character.isWhitespace(text.charAt(aux)); ++aux) {
      }

      ta.setCaretPosition(ta.getLineStartOffset(currentLine) + aux);
      int xForPrevious;
      if (ta.getLineOffsetX(currentLine) > ta.getLineOffsetX(refLine)) {
         xForPrevious = ta.getLineOffsetX(refLine);

         while(ta.offsetToX(currentLine, ta.getCaretPosition() - ta.getLineStartOffset(ta.getCaretLine())) > xForPrevious) {
            int p = ta.getCaretPosition();
            DeleteEdit de = new DeleteEdit(ta, false, 1, p, ta.getText(p - 1, 1));
            de.execute();
         }
      } else if (ta.getLineOffsetX(currentLine) < ta.getLineOffsetX(refLine)) {
         xForPrevious = ta.getLineOffsetX(refLine);

         while(ta.offsetToX(currentLine, ta.getCaretPosition() - ta.getLineStartOffset(ta.getCaretLine())) < xForPrevious) {
            InsertionEdit te = new InsertionEdit(ta, ta.getCaretPosition(), " ");
            te.execute();
         }
      }

      if (indentRight && INDENT_SIZE != 0) {
         String insertText = "";

         for(int i = 0; i < INDENT_SIZE; ++i) {
            insertText = insertText.concat(" ");
         }

         InsertionEdit te = new InsertionEdit(ta, ta.getCaretPosition(), insertText);
         te.execute();
      }

   }
}
