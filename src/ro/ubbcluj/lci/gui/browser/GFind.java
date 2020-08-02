package ro.ubbcluj.lci.gui.browser;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.browser.BTree.BTreeNode;
import ro.ubbcluj.lci.gui.browser.BTree.CustomFilter;
import ro.ubbcluj.lci.gui.browser.BTree.Filter;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.utils.ModelFactory;

public class GFind extends JDialog implements ActionListener, MouseListener, MouseMotionListener, KeyListener, ComponentListener {
   static JComboBox findText = new JComboBox();
   JLabel jLabel1 = new JLabel();
   JLabel jLabel2 = new JLabel();
   JComboBox typeCombo = new JComboBox();
   JCheckBox caseCheckBox = new JCheckBox();
   JCheckBox wcCheckBox = new JCheckBox();
   JCheckBox wwCheckBox = new JCheckBox();
   JButton findButton = new JButton();
   JButton closeButton = new JButton();
   JButton diagramButton = new JButton();
   DefaultTableModel resultModel = new UneditableTableModel(new String[]{"Element", "Type", "Context"}, 0);
   JTable resultTable;
   JScrollPane scrollTable;
   JLabel jLabel3;
   JLabel status;
   JComboBox sortCombo;
   private static String sortByName = "Name";
   private static String sortByType = "Type";
   private static String sortByContext = "Context";
   protected GBrowser browser;
   Tuple[] arrayResult;

   protected GFind(GBrowser browser) {
      super(GApplication.frame, "Find in browser", false);
      this.resultTable = new JTable(this.resultModel);
      this.scrollTable = new JScrollPane(this.resultTable);
      this.jLabel3 = new JLabel();
      this.status = new JLabel();
      this.sortCombo = new JComboBox();
      this.browser = browser;

      try {
         int width = 540;
         int height = 450;
         this.arrayResult = new Tuple[0];
         this.getContentPane().setLayout((LayoutManager)null);
         findText.setEditable(true);
         findText.setSelectedItem("");
         findText.setBounds(new Rectangle(20, 50, width - 150, 21));
         ((JTextField)findText.getEditor().getEditorComponent()).addKeyListener(this);
         findText.addKeyListener(this);
         this.getRootPane().setDefaultButton(this.findButton);
         this.jLabel1.setText("Find:");
         this.jLabel1.setBounds(new Rectangle(20, 30, 41, 17));
         this.jLabel2.setText("Type:");
         this.jLabel2.setBounds(new Rectangle(20, 90, 41, 17));
         this.typeCombo.setBounds(new Rectangle(20, 110, width - 150, 21));
         Filter filter = (CustomFilter)browser.getFilter();
         Vector types = new Vector(filter.getAccepted());
         types.addAll(filter.getAlwaysAccepted());
         String[] typesA = (String[])types.toArray(new String[0]);
         Arrays.sort(typesA);
         this.typeCombo.addItem("<any type>");

         for(int i = 0; i < typesA.length; ++i) {
            this.typeCombo.addItem(typesA[i]);
         }

         this.caseCheckBox.setText("Case sensitive");
         this.caseCheckBox.setBounds(new Rectangle(30, 145, 105, 25));
         this.caseCheckBox.setMnemonic(65);
         this.wwCheckBox.setText("Whole words only");
         this.wwCheckBox.setBounds(new Rectangle(150, 145, 130, 25));
         this.wwCheckBox.setMnemonic(87);
         this.findButton.setBounds(new Rectangle(width - 100, 47, 80, 27));
         this.findButton.setText("Find");
         this.findButton.addActionListener(this);
         this.findButton.setMnemonic(70);
         this.closeButton.setBounds(new Rectangle(width - 100, 100, 80, 27));
         this.closeButton.setText("Close");
         this.closeButton.addActionListener(this);
         this.closeButton.setMnemonic(67);
         this.diagramButton.setText("Show in diagram");
         this.diagramButton.addActionListener(this);
         this.diagramButton.setMnemonic(68);
         this.resultTable.setShowGrid(false);
         this.resultTable.setSelectionMode(0);
         this.resultTable.getTableHeader().setReorderingAllowed(false);
         this.resultTable.getColumn("Element").setCellEditor((TableCellEditor)null);
         this.resultTable.getColumn("Type").setCellEditor((TableCellEditor)null);
         this.resultTable.addMouseListener(this);
         this.scrollTable.setBounds(new Rectangle(20, 200, width - 40, height - 250));
         this.status.setBounds(new Rectangle(20, height - 25, 200, 17));
         this.jLabel3.setText("Results sorted by:");
         this.jLabel3.setBounds(new Rectangle(20, 177, 130, 17));
         this.wcCheckBox.setText("Use wildcards");
         this.wcCheckBox.setBounds(new Rectangle(300, 145, 105, 25));
         this.wcCheckBox.setMnemonic(85);
         this.sortCombo.setBounds(new Rectangle(130, 175, 80, 21));
         this.sortCombo.addItem(sortByName);
         this.sortCombo.addItem(sortByType);
         this.sortCombo.addItem(sortByContext);
         this.sortCombo.setSelectedItem(sortByName);
         this.sortCombo.addActionListener(this);
         this.getContentPane().add(findText, (Object)null);
         this.getContentPane().add(this.jLabel1, (Object)null);
         this.getContentPane().add(this.jLabel2, (Object)null);
         this.getContentPane().add(this.typeCombo, (Object)null);
         this.getContentPane().add(this.scrollTable, (Object)null);
         this.getContentPane().add(this.jLabel3, (Object)null);
         this.getContentPane().add(this.status, (Object)null);
         this.getContentPane().add(this.sortCombo, (Object)null);
         this.getContentPane().add(this.caseCheckBox, (Object)null);
         this.getContentPane().add(this.wcCheckBox, (Object)null);
         this.getContentPane().add(this.wwCheckBox, (Object)null);
         this.getContentPane().add(this.findButton, (Object)null);
         this.getContentPane().add(this.closeButton, (Object)null);
         ToolTipManager.sharedInstance().registerComponent(this.resultTable);
         this.resultTable.addMouseMotionListener(this);
         this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
               GFind.this.clear();
               GFind.this.dispose();
               GFind.this.setVisible(false);
            }
         });
         this.addComponentListener(this);
         this.pack();
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   private void clear() {
      ToolTipManager.sharedInstance().unregisterComponent(this.resultTable);
      this.arrayResult = new Tuple[0];
      this.resultModel = new UneditableTableModel(new String[]{"Element", "Type", "Context"}, 0);
   }

   public void actionPerformed(ActionEvent evt) {
      if (evt.getSource().equals(this.findButton)) {
         this.processFindAction();
      } else if (evt.getSource() != this.diagramButton) {
         if (evt.getSource() == this.sortCombo) {
            this.displayResult();
         } else if (evt.getSource() == this.closeButton) {
            this.clear();
            this.dispose();
            this.setVisible(false);
         }
      }

   }

   private void displayResult() {
      this.resultModel.setRowCount(0);
      Arrays.sort(this.arrayResult, new Comparator() {
         public int compare(Object o1, Object o2) {
            Tuple t1 = (Tuple)o1;
            Tuple t2 = (Tuple)o2;
            ModelElement me1 = (ModelElement)t1.element;
            ModelElement me2 = (ModelElement)t2.element;
            String c1 = t1.context != null ? t1.context.toString() : "";
            String c2 = t2.context != null ? t2.context.toString() : "";
            String n1;
            String n2;
            if (GFind.this.sortCombo.getSelectedItem() == GFind.sortByType) {
               n1 = me1.getMetaclassName() + " " + me1.toString() + " " + c1;
               n2 = me2.getMetaclassName() + " " + me2.toString() + " " + c2;
            } else if (GFind.this.sortCombo.getSelectedItem() == GFind.sortByName) {
               n1 = me1.toString() + " " + me1.getMetaclassName() + " " + c1;
               n2 = me2.toString() + " " + me2.getMetaclassName() + " " + c2;
            } else {
               n1 = c1 + " " + me1.toString() + " " + me1.getMetaclassName();
               n2 = c2 + " " + me2.toString() + " " + me2.getMetaclassName();
            }

            return n1.compareTo(n2);
         }

         public boolean equal(Object o1) {
            return this.compare(this, o1) == 0;
         }
      });

      for(int i = 0; i < this.arrayResult.length; ++i) {
         Tuple tuple = this.arrayResult[i];
         ModelElement me = (ModelElement)tuple.element;
         BTreeNode node = (BTreeNode)tuple.context;
         String context = node == null ? "" : node.toString();
         this.resultModel.addRow(new String[]{me.toString(), me.getMetaclassName(), context});
      }

   }

   private boolean matchesString(String expr, String str) {
      if (str.equals("")) {
         return expr.equals("") || expr.equals("*");
      } else if (expr.equals("*")) {
         return true;
      } else if (expr.equals("")) {
         return false;
      } else {
         int index;
         switch(expr.charAt(0)) {
         case '*':
            String newExpr = expr.substring(1);
            int wcIndex1 = newExpr.indexOf("?");
            if (wcIndex1 == -1) {
               wcIndex1 = newExpr.length();
            }

            int wcIndex2 = newExpr.indexOf("*");
            if (wcIndex2 == -1) {
               wcIndex2 = newExpr.length();
            }

            int wcIndex = wcIndex1 < wcIndex2 ? wcIndex1 : wcIndex2;
            boolean match = false;
            String aux = newExpr.substring(0, wcIndex);

            for(index = 0; str.indexOf(aux, index) != -1; match = match || this.matchesString(newExpr, str.substring(index - 1))) {
               index = str.indexOf(aux, index) + 1;
            }

            return match;
         case '?':
            return this.matchesString(expr.substring(1), str.substring(1));
         default:
            if (str.charAt(0) != expr.charAt(0)) {
               return false;
            } else {
               index = 0;

               for(int minLength = str.length() < expr.length() ? str.length() : expr.length(); index < minLength && str.charAt(index) == expr.charAt(index); ++index) {
               }

               return this.matchesString(expr.substring(index), str.substring(index));
            }
         }
      }
   }

   private void processFindAction() {
      String current = (String)findText.getSelectedItem();
      boolean exist = false;

      for(int i = 0; i < findText.getItemCount(); ++i) {
         if (current.equals(findText.getItemAt(i))) {
            exist = true;
         }
      }

      if (!exist) {
         findText.insertItemAt(current, 0);
      }

      boolean caseSensitive = this.caseCheckBox.isSelected();
      boolean useWildCards = this.wcCheckBox.isSelected();
      boolean wholeWords = this.wwCheckBox.isSelected();
      String name = caseSensitive ? current : current.toUpperCase();
      String type = (String)this.typeCombo.getSelectedItem();
      Vector result = new Vector();
      Enumeration treeNodes = ((DefaultMutableTreeNode)((JTree)this.browser.getComponent()).getModel().getRoot()).preorderEnumeration();

      while(true) {
         BTreeNode context;
         ModelElement me;
         do {
            while(true) {
               Object userObj;
               do {
                  if (!treeNodes.hasMoreElements()) {
                     this.arrayResult = (Tuple[])result.toArray(new Tuple[0]);
                     this.displayResult();
                     this.status.setText("Found " + Integer.toString(this.arrayResult.length) + " matches.");
                     return;
                  }

                  BTreeNode node = (BTreeNode)treeNodes.nextElement();
                  context = (BTreeNode)node.getParent();
                  userObj = node.getUserObject();
               } while(!(userObj instanceof ModelElement));

               me = (ModelElement)userObj;
               String meName;
               if (me.getName() != null) {
                  meName = caseSensitive ? me.getName() : me.getName().toUpperCase();
               } else {
                  meName = "";
               }

               if (useWildCards && this.matchesString(name, meName)) {
                  break;
               }

               if (type.equals("<any type>") && meName.indexOf(name) != -1 || me.getMetaclassName().equals(type) && meName.indexOf(name) != -1) {
                  if (wholeWords) {
                     int idx = meName.indexOf(name);
                     char before = idx == 0 ? 32 : meName.charAt(idx);
                     char after = idx + name.length() == meName.length() ? 32 : meName.charAt(idx + name.length());
                     if (this.isSeparator(before) && this.isSeparator(after)) {
                        result.add(new Tuple(me, context));
                     }
                  } else {
                     result.add(new Tuple(me, context));
                  }
               }
            }
         } while(!type.equals("<any type>") && !me.getMetaclassName().equals(type));

         result.add(new Tuple(me, context));
      }
   }

   protected boolean isSeparator(char c) {
      return !Character.isLetterOrDigit(c);
   }

   public void mouseClicked(MouseEvent evt) {
      if (evt.getClickCount() == 2) {
         Tuple t = this.arrayResult[this.resultTable.getSelectedRow()];
         ModelFactory.fireModelEvent(t.element, t.context, 0);
      }

   }

   public void mousePressed(MouseEvent evt) {
   }

   public void mouseReleased(MouseEvent evt) {
   }

   public void mouseEntered(MouseEvent evt) {
   }

   public void mouseExited(MouseEvent evt) {
   }

   public void mouseMoved(MouseEvent evt) {
      if (evt.getSource() == this.resultTable) {
         int row = this.resultTable.rowAtPoint(evt.getPoint());
         int col = this.resultTable.columnAtPoint(evt.getPoint());
         if (row != -1 && col != -1) {
            this.resultTable.setToolTipText(this.resultTable.getModel().getValueAt(row, col).toString());
         }
      }

   }

   public void mouseDragged(MouseEvent evt) {
   }

   public void keyTyped(KeyEvent evt) {
   }

   public void keyPressed(KeyEvent evt) {
      if (evt.getKeyCode() == 10) {
         findText.setSelectedItem(findText.getEditor().getItem());
         this.processFindAction();
      } else if (evt.getKeyCode() == 27) {
         this.clear();
         this.dispose();
         this.setVisible(false);
      }

   }

   public void keyReleased(KeyEvent evt) {
   }

   public void componentResized(ComponentEvent evt) {
      int width = evt.getComponent().getSize().width;
      int height = evt.getComponent().getSize().height;
      if (width < 540) {
         width = 540;
      }

      if (height < 450) {
         height = 450;
      }

      this.setSize(new Dimension(width, height));
      findText.setBounds(new Rectangle(20, 50, width - 150, 21));
      this.typeCombo.setBounds(new Rectangle(20, 110, width - 150, 21));
      this.findButton.setBounds(new Rectangle(width - 100, 47, 80, 27));
      this.closeButton.setBounds(new Rectangle(width - 100, 107, 80, 27));
      this.diagramButton.setBounds(new Rectangle(width - 150, 146, 140, 27));
      this.scrollTable.setBounds(new Rectangle(20, 200, width - 40, height - 260));
      this.status.setBounds(new Rectangle(20, height - 55, 200, 17));
      this.validate();
   }

   public void componentMoved(ComponentEvent evt) {
   }

   public void componentHidden(ComponentEvent evt) {
   }

   public void componentShown(ComponentEvent evt) {
   }
}
