package ro.ubbcluj.lci.gui.wizards.newprojectsteps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ro.ubbcluj.lci.gui.FileSelectionData;
import ro.ubbcluj.lci.gui.GUtils;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.gui.wizards.AbstractWizardStep;
import ro.ubbcluj.lci.gui.wizards.Wizard;

public class SelectFilesWizardStep extends AbstractWizardStep implements ActionListener, ListSelectionListener {
   private String description;
   private AFileFilter[] filters;
   private DefaultListModel dfm;
   private JList filesList;
   private JButton addBtn;
   private JButton remBtn;
   private final boolean selectOnlyOne;
   private final boolean defaultSelect;

   public SelectFilesWizardStep(Wizard owner, String s, AFileFilter[] aff, boolean selectOnlyOne, boolean defaultSelect) {
      super(owner);
      this.description = s;
      this.selectOnlyOne = selectOnlyOne;
      this.defaultSelect = defaultSelect;
      this.filters = aff;
      this.initComponents();
   }

   public boolean prepareToShow() {
      this.setNextButton(true);
      return true;
   }

   public boolean prepareToDissapear() {
      this.result = this.dfm;
      return true;
   }

   private void initComponents() {
      JPanel topPanel = new JPanel();
      topPanel.setBackground(Color.WHITE);
      topPanel.setLayout(new BoxLayout(topPanel, 0));
      topPanel.setBorder(BorderFactory.createEtchedBorder());
      JLabel text = new JLabel(this.description);
      text.setAlignmentX(0.0F);
      text.setAlignmentY(0.5F);
      text.setFont(new Font("SansSerif", 1, 12));
      JLabel image = new JLabel(GUtils.loadIcon("/images/wizards/npw_kop.gif"));
      topPanel.add(Box.createHorizontalStrut(10));
      topPanel.add(text);
      topPanel.add(Box.createHorizontalGlue());
      topPanel.add(image);
      topPanel.add(Box.createHorizontalStrut(10));
      JPanel centerPanel = new JPanel();
      centerPanel.setBorder(BorderFactory.createEtchedBorder());
      centerPanel.setLayout(new BorderLayout());
      JPanel filesPanel = new JPanel();
      filesPanel.setLayout(new BoxLayout(filesPanel, 1));
      this.dfm = new DefaultListModel();
      this.filesList = new JList(this.dfm);
      this.filesList.setCellRenderer(new SelectFilesWizardStep.CheckListCellRenderer());
      SelectFilesWizardStep.CheckListListener cll = new SelectFilesWizardStep.CheckListListener(this.filesList);
      this.filesList.addMouseListener(cll);
      this.filesList.addKeyListener(cll);
      this.filesList.setSelectionMode(2);
      this.filesList.addListSelectionListener(this);
      JPanel btnPanel = new JPanel();
      btnPanel.setLayout(new BoxLayout(btnPanel, 0));
      this.addBtn = new JButton("Add...");
      this.addBtn.setActionCommand("Add");
      this.addBtn.addActionListener(this);
      this.addBtn.setMargin(new Insets(1, 5, 1, 5));
      this.addBtn.setFont(new Font("Default", 0, 9));
      this.remBtn = new JButton("Remove");
      this.remBtn.setActionCommand("Remove");
      this.remBtn.addActionListener(this);
      this.remBtn.setMargin(new Insets(1, 5, 1, 5));
      this.remBtn.setFont(new Font("Default", 0, 9));
      this.remBtn.setEnabled(false);
      btnPanel.add(Box.createHorizontalGlue());
      btnPanel.add(this.addBtn);
      btnPanel.add(Box.createHorizontalStrut(5));
      btnPanel.add(this.remBtn);
      btnPanel.add(Box.createHorizontalStrut(10));
      filesPanel.add(Box.createHorizontalStrut(10));
      filesPanel.add(new JScrollPane(this.filesList));
      filesPanel.add(Box.createHorizontalStrut(5));
      filesPanel.add(btnPanel);
      filesPanel.add(Box.createHorizontalStrut(10));
      centerPanel.add(filesPanel, "Center");
      this.thePanel.add(centerPanel, "Center");
      this.thePanel.add(topPanel, "North");
   }

   public void actionPerformed(ActionEvent ae) {
      JButton source = (JButton)ae.getSource();
      int j;
      if (source.getActionCommand().equals("Add")) {
         File[] files = AFileFilter.chooseFiles(this.owner, "Attach", this.filters);

         for(j = 0; j < (files != null ? files.length : -1); ++j) {
            if (files[j] != null) {
               try {
                  this.dfm.addElement(new FileSelectionData(files[j].getCanonicalPath(), this.defaultSelect));
               } catch (IOException var7) {
                  System.err.println("File does not exist!");
               }
            }
         }
      } else if (source.getActionCommand().equals("Remove")) {
         int[] index = this.filesList.getSelectedIndices();
         if (index.length <= 0) {
            return;
         }

         j = 0;
         DefaultListModel temp = new DefaultListModel();

         for(int i = 0; i < this.dfm.size(); ++i) {
            if (j < index.length && i == index[j]) {
               ++j;
            } else {
               temp.addElement(this.dfm.elementAt(i));
            }
         }

         this.filesList.setModel(temp);
         this.dfm = temp;
         if (this.dfm.size() == 0) {
            this.remBtn.setEnabled(false);
         } else if (index[0] < this.dfm.size()) {
            this.filesList.setSelectedIndex(index[0]);
         } else {
            this.filesList.setSelectedIndex(-1);
         }
      }

   }

   public void valueChanged(ListSelectionEvent lse) {
      if (!lse.getValueIsAdjusting()) {
         if (this.filesList.getSelectedIndex() == -1) {
            this.remBtn.setEnabled(false);
         } else {
            this.remBtn.setEnabled(true);
         }
      } else {
         int fi = lse.getFirstIndex();
         int li = lse.getLastIndex();
         if (fi > -1 && li > -1) {
            this.remBtn.setEnabled(true);
         } else {
            this.remBtn.setEnabled(false);
         }
      }

   }

   class CheckListListener implements MouseListener, KeyListener {
      protected JList list;

      public CheckListListener(JList list) {
         this.list = list;
      }

      public void mouseClicked(MouseEvent e) {
         if (e.getX() < 20) {
            this.doCheck();
         }

      }

      public void keyPressed(KeyEvent e) {
         if (e.getKeyChar() == ' ') {
            this.doCheck();
         }

      }

      protected void doCheck() {
         int index = this.list.getSelectedIndex();
         if (index >= 0) {
            ListModel lm = this.list.getModel();
            FileSelectionData cld = (FileSelectionData)lm.getElementAt(index);
            if (cld.isSelected()) {
               cld.setSelected(false);
            } else {
               ((FileSelectionData)lm.getElementAt(index)).setSelected(true);
               if (SelectFilesWizardStep.this.selectOnlyOne) {
                  for(int i = 0; i < lm.getSize(); ++i) {
                     if (i != index) {
                        ((FileSelectionData)lm.getElementAt(i)).setSelected(false);
                     }
                  }
               }
            }

            this.list.repaint();
         }
      }

      public void mousePressed(MouseEvent e) {
      }

      public void mouseReleased(MouseEvent e) {
      }

      public void mouseEntered(MouseEvent e) {
      }

      public void mouseExited(MouseEvent e) {
      }

      public void keyTyped(KeyEvent e) {
      }

      public void keyReleased(KeyEvent e) {
      }
   }

   class CheckListCellRenderer extends JCheckBox implements ListCellRenderer {
      protected Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

      public CheckListCellRenderer() {
         this.setOpaque(true);
         this.setBorder(this.noFocusBorder);
      }

      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         this.setText(value.toString());
         this.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
         this.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
         this.setFont(list.getFont());
         this.setSelected(((FileSelectionData)value).isSelected());
         this.setBorder(cellHasFocus ? UIManager.getBorder("List.focusCellHighlightBorder") : this.noFocusBorder);
         return this;
      }
   }
}
