package ro.ubbcluj.lci.gui.tools.search;

import java.awt.Component;
import java.awt.Container;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.editor.SyntaxDocument;
import ro.ubbcluj.lci.gui.editor.TextDocumentPad;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.utils.Utils;
import ro.ubbcluj.lci.utils.search.TextFinder;

public class FindReplaceDialog extends JDialog implements ActionListener, ItemListener, KeyListener {
   private boolean bAll = false;
   private int lastPosition = -1;
   private String textToFind = "";
   private String newText = "";
   private int searchDirection = 1;
   private int lastOption;
   private JLabel initialTextLabel = new JLabel("Text to find:");
   private JLabel newTextLabel = new JLabel("Replace with:");
   private JLabel filesLabel = new JLabel("Files:");
   private JComboBox cbInitialText = new JComboBox();
   private JComboBox cbNewText = new JComboBox();
   private JRadioButton rbForward = new JRadioButton("Forward");
   private JRadioButton rbBackward = new JRadioButton("Backward");
   private JRadioButton rbCurrentBuffer = new JRadioButton("Current buffer");
   private JRadioButton rbProject = new JRadioButton("Files in the project");
   private JRadioButton rbDirectory = new JRadioButton("Custom directory:");
   private JTextField txtDirectory = new JTextField();
   private JTextField txtFiles = new JTextField();
   private JCheckBox matchCaseCheckBox = new JCheckBox("Case sensitive");
   private JCheckBox matchWholeWordCb = new JCheckBox("Whole words only");
   private JButton findBtn = new JButton("Find next");
   private JButton findAllBtn = new JButton("Find  all");
   private JButton replaceBtn = new JButton("Replace");
   private JButton controlBtn = new JButton("Cancel");
   private JButton browseBtn = new JButton("...");
   private File[] projectFiles;
   private File currentFile;
   private int state = 1;
   private static final int STATE_FIND = 0;
   private static final int STATE_PAUSED = 1;
   private JEditTextArea ta;
   private SyntaxDocument doc;

   public FindReplaceDialog(Frame frame, String title, boolean modal) {
      super(frame, title, modal);

      try {
         this.jbInit();
         this.pack();
         TextFinder.setSearchStrategy(new SimpleSearchStrategy());
         this.rbForward.setSelected(true);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void jbInit() throws Exception {
      this.setFocusTraversalPolicy(new FindReplaceDialog.DirectFocusTraversalPolicy());
      this.layoutComponents();
      this.configureActions();
   }

   public void setDefaultFilter(String pattern) {
      this.txtFiles.setText(pattern);
   }

   public void setVisible(boolean bVis) {
      super.setVisible(bVis);
      if (bVis) {
         this.cbInitialText.getEditor().getEditorComponent().requestFocus();
      }

   }

   public void setTargets(TextDocumentPad pad, File[] projectFiles) {
      if (pad == null) {
         this.ta = null;
         this.doc = null;
         this.rbCurrentBuffer.setEnabled(false);
         this.rbProject.setSelected(true);
         this.currentFile = null;
         this.rbCurrentBuffer.setText("Current buffer [none]");
      } else {
         this.rbCurrentBuffer.setEnabled(true);
         this.rbCurrentBuffer.setSelected(true);
         this.ta = pad.getView();
         this.doc = this.ta.getDocument();
         this.currentFile = pad.getModel().getFile();
         this.rbCurrentBuffer.setText("Current buffer [" + pad.getModel().getShortFileName() + "]");
      }

      this.projectFiles = projectFiles;
   }

   public void itemStateChanged(ItemEvent evt) {
      JRadioButton src = (JRadioButton)evt.getSource();
      int sc = evt.getStateChange();
      if (sc == 1) {
         if (src == this.rbForward) {
            this.searchDirection = 1;
            return;
         }

         if (src == this.rbBackward) {
            this.searchDirection = 2;
            return;
         }

         if (src == this.rbCurrentBuffer) {
            this.txtDirectory.setEnabled(false);
            this.browseBtn.setEnabled(false);
            this.txtFiles.setEnabled(false);
            this.rbForward.setEnabled(true);
            this.rbBackward.setEnabled(true);
            this.findBtn.setEnabled(true);
            this.replaceBtn.setEnabled(true);
            this.getRootPane().setDefaultButton(this.findBtn);
         } else {
            this.rbForward.setEnabled(false);
            this.rbBackward.setEnabled(false);
            this.findBtn.setEnabled(false);
            this.replaceBtn.setEnabled(false);
            this.txtDirectory.setEnabled(src == this.rbDirectory);
            this.browseBtn.setEnabled(src == this.rbDirectory);
            this.txtFiles.setEnabled(src == this.rbDirectory);
            this.getRootPane().setDefaultButton(this.findAllBtn);
         }
      }

   }

   public void keyPressed(KeyEvent evt) {
      if (evt.getKeyCode() == 27) {
         this.cancel();
      }

   }

   public void keyReleased(KeyEvent evt) {
   }

   public void keyTyped(KeyEvent evt) {
      if (evt.getKeyChar() == '\n') {
         this.getRootPane().getDefaultButton().doClick();
      }

   }

   public void actionPerformed(ActionEvent evt) {
      Object src = evt.getSource();
      if (src != this.cbInitialText && src != this.cbNewText) {
         if (src != this.browseBtn && src != this.controlBtn) {
            this.doFind(src);
         } else if (src == this.browseBtn) {
            this.browseForDirectory();
         } else if (src == this.controlBtn) {
            this.cancel();
         }
      } else {
         JComboBox cb = (JComboBox)src;
         String text;
         if (!search(cb, text = (String)cb.getSelectedItem())) {
            cb.addItem(text);
         }

         if (cb == this.cbInitialText) {
            this.textToFind = text;
         } else {
            this.newText = text;
         }
      }

   }

   private void doFind(final Object src) {
      Thread finder = new Thread("Finder") {
         public void run() {
            if (FindReplaceDialog.this.rbCurrentBuffer.isSelected()) {
               if (src == FindReplaceDialog.this.findBtn) {
                  FindReplaceDialog.this.findInCurrentBuffer();
               } else if (src == FindReplaceDialog.this.findAllBtn) {
                  FindReplaceDialog.this.findAllInCurrentBuffer();
               } else {
                  FindReplaceDialog.this.replace();
               }
            }

            if (FindReplaceDialog.this.rbProject.isSelected()) {
               FindReplaceDialog.this.findInProjectFiles();
            }

            if (FindReplaceDialog.this.rbDirectory.isSelected()) {
               FindReplaceDialog.this.findInCustomDirectory();
            }

         }
      };
      finder.start();
   }

   private void findInCustomDirectory() {
      this.textToFind = (String)this.cbInitialText.getSelectedItem();
      if (this.textToFind != null) {
         DefaultMutableTreeNode root = new DefaultMutableTreeNode("Search results for '" + this.textToFind + "'");
         this.updateSearchStrategy();
         TextFinder.setStopped(false);
         this.state = 0;
         ArrayList filters = new ArrayList();
         StringTokenizer st = new StringTokenizer(this.txtFiles.getText(), ",;", false);

         while(st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            filters.add(token);
            if (TextFinder.isStopped()) {
               break;
            }
         }

         if (!TextFinder.isStopped()) {
            File dir = new File(this.txtDirectory.getText());
            if (!dir.exists() || !dir.isDirectory()) {
               JOptionPane.showMessageDialog(this, "Invalid directory:" + dir.getAbsolutePath(), "Error", 0);
               TextFinder.setStopped(true);
               this.state = 1;
               return;
            }

            ArrayList files = new ArrayList();
            ArrayList results = new ArrayList();
            files.add(dir);
            WildcardFileFilter ff = new WildcardFileFilter(filters);

            label43:
            while(true) {
               while(true) {
                  if (files.isEmpty()) {
                     break label43;
                  }

                  File f = (File)files.remove(0);
                  if (TextFinder.isStopped()) {
                     break label43;
                  }

                  if (f.isDirectory()) {
                     File[] contents = f.listFiles(ff);

                     for(int p = 0; p < contents.length; ++p) {
                        files.add(contents[p]);
                     }
                  } else {
                     results.clear();
                     TextFinder.findInFile(f, this.textToFind, results);
                     this.updateTreeNode(root, results, f);
                  }
               }
            }
         }

         GMainFrame.getMainFrame().updateSearchResults(root);
         this.state = 1;
      }
   }

   private void findAllInCurrentBuffer() {
      this.textToFind = (String)this.cbInitialText.getSelectedItem();
      if (this.textToFind != null) {
         ArrayList results = new ArrayList();
         DefaultMutableTreeNode root = new DefaultMutableTreeNode("Search results for '" + this.textToFind + "'");
         this.updateSearchStrategy();
         TextFinder.findInBuffer(this.doc, this.ta.getCaretPosition(), this.searchDirection, this.textToFind, results);
         this.updateTreeNode(root, results);
         GMainFrame.getMainFrame().updateSearchResults(root);
      }
   }

   private void findInProjectFiles() {
      this.textToFind = (String)this.cbInitialText.getSelectedItem();
      if (this.textToFind != null) {
         ArrayList results = new ArrayList();
         DefaultMutableTreeNode root = new DefaultMutableTreeNode("Search results for '" + this.textToFind + "'");
         this.updateSearchStrategy();
         TextFinder.setStopped(false);
         this.state = 0;

         for(int i = 0; i < this.projectFiles.length; ++i) {
            results.clear();
            if (!this.projectFiles[i].exists()) {
               GMainFrame.getMainFrame().updateLog("File " + this.projectFiles[i].getAbsolutePath() + " not found; skipped.\n");
            } else {
               TextFinder.findInFile(this.projectFiles[i], this.textToFind, results);
               this.updateTreeNode(root, results, this.projectFiles[i]);
               if (TextFinder.isStopped()) {
                  break;
               }
            }
         }

         GMainFrame.getMainFrame().updateSearchResults(root);
         this.state = 1;
      }
   }

   private void findInCurrentBuffer() {
      this.textToFind = (String)this.cbInitialText.getSelectedItem();
      if (this.textToFind != null) {
         this.updateSearchStrategy();
         int pos = this.findNext();
         if (pos < 0) {
            this.showTextNotFound();
         } else {
            if (this.searchDirection == 1) {
               this.ta.select(pos, pos + this.textToFind.length());
            } else {
               this.ta.select(pos + this.textToFind.length(), pos);
            }

            this.ta.repaint();
         }

      }
   }

   private void updateTreeNode(DefaultMutableTreeNode root, List res) {
      this.updateTreeNode(root, res, this.currentFile);
   }

   private void updateTreeNode(DefaultMutableTreeNode root, List res, File file) {
      int s = res.size();
      if (s > 0) {
         DefaultMutableTreeNode child;
         if (file == null) {
            child = new DefaultMutableTreeNode("Untitled file");
         } else {
            child = new DefaultMutableTreeNode(file);
         }

         for(int i = 0; i < s; ++i) {
            child.add(new DefaultMutableTreeNode(res.get(i)));
         }

         root.add(child);
      }

   }

   private void replace() {
      int i = 0;
      boolean stop = false;
      this.bAll = false;
      this.textToFind = (String)this.cbInitialText.getSelectedItem();
      this.newText = (String)this.cbNewText.getSelectedItem();
      if (this.newText == null) {
         this.newText = "";
      }

      this.updateSearchStrategy();
      this.setVisible(false);

      int v;
      while((v = this.findNext()) >= 0) {
         ++i;
         this.lastPosition = v;
         if (this.bAll) {
            this.doReplace();
         } else {
            this.ta.select(v, v + this.textToFind.length());
            Object[] options = new Object[]{"Yes", "No", "All", "Stop"};
            Object selectedValue = this.showReplaceConfirmationDialog(options);
            if (options[0].equals(selectedValue)) {
               this.doReplace();
               this.lastOption = 0;
            } else if (options[2].equals(selectedValue)) {
               this.bAll = true;
               this.doc.replaceAll();
               this.doReplace();
               this.lastOption = 2;
            } else {
               if (options[3].equals(selectedValue)) {
                  stop = true;
                  this.lastOption = 3;
                  break;
               }

               this.lastOption = 1;
            }
         }

         if (this.searchDirection == 1) {
            if (this.lastOption != 0 && this.lastOption != 2) {
               this.ta.setCaretPosition(v + this.textToFind.length());
            } else {
               this.ta.setCaretPosition(v + this.newText.length());
            }
         } else {
            this.ta.setCaretPosition(v);
         }
      }

      if (i == 0 && !this.bAll) {
         this.showTextNotFound();
      } else if (this.bAll) {
         this.showAllMessage();
      } else if (!stop) {
         this.showNoMoreOccurencesMessage();
      }

      if (this.bAll) {
         this.doc.endCompoundEdit();
      }

      this.setVisible(true);
   }

   private void updateSearchStrategy() {
      SimpleSearchStrategy sss = (SimpleSearchStrategy)TextFinder.getSearchStrategy();
      sss.matchCase(this.matchCaseCheckBox.isSelected());
      sss.matchWholeWords(this.matchWholeWordCb.isSelected());
   }

   private void cancel() {
      if (this.state == 0) {
         TextFinder.setStopped(true);
         this.state = 1;
      } else {
         this.setVisible(false);
         this.doc = null;
         this.ta = null;
         this.projectFiles = null;
         System.gc();
      }

   }

   private void browseForDirectory() {
      File f = AFileFilter.chooseDirectory(this);
      if (f != null) {
         this.txtDirectory.setText(f.getAbsolutePath());
      }

   }

   private static boolean search(JComboBox source, String item) {
      int itemCount = source.getItemCount();

      for(int i = 0; i < itemCount; ++i) {
         String currentItem = (String)source.getItemAt(i);
         if (currentItem.equals(item)) {
            return true;
         }
      }

      return false;
   }

   private void layoutComponents() {
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();
      this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), 0));
      JPanel leftPanel = new JPanel();
      leftPanel.setLayout(new BoxLayout(leftPanel, 1));
      JPanel parameterPanel = new JPanel();
      parameterPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
      parameterPanel.setLayout(gbl);
      gbc.anchor = 18;
      gbc.fill = 0;
      gbc.gridx = gbc.gridy = 0;
      gbc.gridwidth = gbc.gridheight = 1;
      gbc.weightx = 0.0D;
      gbc.weighty = 0.5D;
      gbl.setConstraints(this.initialTextLabel, gbc);
      parameterPanel.add(this.initialTextLabel);
      gbc.anchor = 17;
      gbc.gridy = 1;
      gbl.setConstraints(this.newTextLabel, gbc);
      parameterPanel.add(this.newTextLabel);
      gbc.anchor = 12;
      gbc.fill = 2;
      gbc.gridx = -1;
      gbc.gridy = 0;
      gbc.gridwidth = 0;
      gbc.weightx = 1.0D;
      gbl.setConstraints(this.cbInitialText, gbc);
      parameterPanel.add(this.cbInitialText);
      this.cbInitialText.setEditable(true);
      gbc.anchor = 13;
      gbc.gridy = 1;
      gbl.setConstraints(this.cbNewText, gbc);
      parameterPanel.add(this.cbNewText);
      this.cbNewText.setEditable(true);
      gbc.anchor = 16;
      gbc.gridx = 0;
      gbc.gridy = -1;
      gbc.gridwidth = 1;
      gbc.gridheight = 0;
      gbc.weightx = 0.5D;
      gbc.weighty = 0.0D;
      gbl.setConstraints(this.matchCaseCheckBox, gbc);
      parameterPanel.add(this.matchCaseCheckBox);
      gbc.anchor = 14;
      gbc.gridx = -1;
      gbl.setConstraints(this.matchWholeWordCb, gbc);
      parameterPanel.add(this.matchWholeWordCb);
      leftPanel.add(parameterPanel);
      JPanel targetPanel = new JPanel();
      targetPanel.setLayout(new BoxLayout(targetPanel, 1));
      targetPanel.setBorder(BorderFactory.createTitledBorder("Target"));
      JPanel tmp = new JPanel();
      ButtonGroup rbg = new ButtonGroup();
      rbg.add(this.rbCurrentBuffer);
      rbg.add(this.rbProject);
      rbg.add(this.rbDirectory);
      gbl = new GridBagLayout();
      tmp.setLayout(gbl);
      gbc.anchor = 17;
      gbc.fill = 0;
      gbc.weightx = 0.5D;
      gbc.weighty = 1.0D;
      gbc.gridx = gbc.gridy = 0;
      gbc.gridwidth = gbc.gridheight = 1;
      gbl.setConstraints(this.rbCurrentBuffer, gbc);
      tmp.add(this.rbCurrentBuffer);
      targetPanel.add(tmp);
      tmp = new JPanel(gbl = new GridBagLayout());
      gbl.setConstraints(this.rbProject, gbc);
      tmp.add(this.rbProject);
      targetPanel.add(tmp);
      tmp = new JPanel(gbl = new GridBagLayout());
      gbc.weightx = 0.0D;
      gbl.setConstraints(this.rbDirectory, gbc);
      tmp.add(this.rbDirectory);
      gbc.anchor = 10;
      gbc.fill = 2;
      gbc.weightx = 1.0D;
      gbc.gridx = -1;
      gbl.setConstraints(this.txtDirectory, gbc);
      tmp.add(this.txtDirectory);
      gbc.weightx = 0.0D;
      gbc.anchor = 13;
      gbc.fill = 0;
      gbc.gridwidth = 0;
      gbl.setConstraints(this.browseBtn, gbc);
      tmp.add(this.browseBtn);
      targetPanel.add(tmp);
      tmp = new JPanel(gbl = new GridBagLayout());
      gbc.anchor = 17;
      gbc.gridx = 0;
      gbc.gridwidth = 1;
      gbl.setConstraints(this.filesLabel, gbc);
      tmp.add(this.filesLabel);
      gbc.weightx = 1.0D;
      gbc.fill = 2;
      gbc.gridx = -1;
      gbc.gridwidth = 0;
      gbl.setConstraints(this.txtFiles, gbc);
      tmp.add(this.txtFiles);
      targetPanel.add(tmp);
      leftPanel.add(targetPanel);
      JPanel searchDirectionPanel = new JPanel(gbl = new GridBagLayout());
      searchDirectionPanel.setBorder(BorderFactory.createTitledBorder("Search direction"));
      ButtonGroup bgr2 = new ButtonGroup();
      bgr2.add(this.rbForward);
      bgr2.add(this.rbBackward);
      gbc.anchor = 17;
      gbc.fill = 2;
      gbc.gridx = gbc.gridy = 0;
      gbc.weightx = 0.5D;
      gbc.weighty = 1.0D;
      gbc.gridheight = gbc.gridwidth = 1;
      gbl.setConstraints(this.rbForward, gbc);
      searchDirectionPanel.add(this.rbForward);
      gbc.anchor = 13;
      gbc.gridx = -1;
      gbc.gridwidth = 0;
      gbl.setConstraints(this.rbBackward, gbc);
      searchDirectionPanel.add(this.rbBackward);
      searchDirectionPanel.setMaximumSize(new Dimension(Utils.screenSize.width, Utils.screenSize.height / 20));
      leftPanel.add(searchDirectionPanel);
      JPanel actionPanel = new JPanel();
      actionPanel.setLayout(new BoxLayout(actionPanel, 1));
      actionPanel.setMaximumSize(new Dimension(Utils.screenSize.width / 12, Utils.screenSize.height));
      actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      actionPanel.add(this.findBtn);
      actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      actionPanel.add(this.findAllBtn);
      actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      actionPanel.add(this.replaceBtn);
      actionPanel.add(Box.createVerticalGlue());
      actionPanel.add(this.controlBtn);
      actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      this.getRootPane().setDefaultButton(this.findBtn);
      this.getContentPane().add(leftPanel);
      this.getContentPane().add(actionPanel);
      this.setSize(Utils.screenSize.width / 2, Utils.screenSize.height / 2);
   }

   private void configureActions() {
      this.rbForward.addItemListener(this);
      this.rbBackward.addItemListener(this);
      this.rbCurrentBuffer.addItemListener(this);
      this.rbProject.addItemListener(this);
      this.rbDirectory.addItemListener(this);
      this.findBtn.addActionListener(this);
      this.findAllBtn.addActionListener(this);
      this.replaceBtn.addActionListener(this);
      this.controlBtn.addActionListener(this);
      this.browseBtn.addActionListener(this);
      this.cbInitialText.addActionListener(this);
      this.cbNewText.addActionListener(this);
      this.cbInitialText.getEditor().getEditorComponent().addKeyListener(this);
      this.cbNewText.getEditor().getEditorComponent().addKeyListener(this);
      this.findBtn.addKeyListener(this);
      this.findAllBtn.addKeyListener(this);
      this.controlBtn.addKeyListener(this);
      this.cbInitialText.addKeyListener(this);
      this.cbNewText.addKeyListener(this);
      this.rbForward.addKeyListener(this);
      this.rbBackward.addKeyListener(this);
      this.rbDirectory.addKeyListener(this);
      this.rbCurrentBuffer.addKeyListener(this);
      this.rbProject.addKeyListener(this);
      this.browseBtn.addKeyListener(this);
      this.txtDirectory.addKeyListener(this);
      this.txtFiles.addKeyListener(this);
      this.matchCaseCheckBox.addKeyListener(this);
      this.matchWholeWordCb.addKeyListener(this);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            FindReplaceDialog.this.cancel();
         }
      });
   }

   private int findNext() {
      return TextFinder.findInBuffer(this.doc, this.ta.getCaretPosition(), this.searchDirection, this.textToFind);
   }

   private void doReplace() {
      try {
         this.doc.replace(this.lastPosition, this.textToFind.length(), this.newText, this.ta);
      } catch (Exception var2) {
      }

   }

   private void showTextNotFound() {
      JOptionPane.showMessageDialog((Component)null, "Search string \"" + this.textToFind + "\" not found.", "Search failed", 1);
   }

   private void showNoMoreOccurencesMessage() {
      JOptionPane.showMessageDialog((Component)null, "There are no more occurences of search string \"" + this.textToFind + "\".", "No more occurences", 1);
   }

   private void showAllMessage() {
      JOptionPane.showMessageDialog((Component)null, "All occurences of search string \"" + this.textToFind + "\" have been replaced.", "All occurences replaced", 1);
   }

   private Object showReplaceConfirmationDialog(Object[] options) {
      Object initValue = options[0];
      JOptionPane pane = new JOptionPane("Replace this occurence?", 3, -1, (Icon)null, options, initValue);
      JDialog dialog = pane.createDialog((Component)null, "Confirm replace");
      dialog.show();
      return pane.getValue();
   }

   private class DirectFocusTraversalPolicy extends ContainerOrderFocusTraversalPolicy {
      private DirectFocusTraversalPolicy() {
      }

      public Component getInitialComponent(Window w) {
         return FindReplaceDialog.this.cbInitialText;
      }

      public Component getComponentAfter(Container root, Component c) {
         if (c.equals(FindReplaceDialog.this.cbInitialText.getEditor().getEditorComponent())) {
            return FindReplaceDialog.this.cbNewText.getEditor().getEditorComponent();
         } else if (c.equals(FindReplaceDialog.this.cbNewText.getEditor().getEditorComponent())) {
            return FindReplaceDialog.this.matchCaseCheckBox;
         } else if (c.equals(FindReplaceDialog.this.matchCaseCheckBox)) {
            return FindReplaceDialog.this.matchWholeWordCb;
         } else if (c.equals(FindReplaceDialog.this.matchWholeWordCb)) {
            return FindReplaceDialog.this.rbCurrentBuffer.isEnabled() ? FindReplaceDialog.this.rbCurrentBuffer : FindReplaceDialog.this.rbProject;
         } else if (c.equals(FindReplaceDialog.this.rbCurrentBuffer)) {
            return FindReplaceDialog.this.rbProject;
         } else if (c.equals(FindReplaceDialog.this.rbProject)) {
            return FindReplaceDialog.this.rbDirectory;
         } else if (c.equals(FindReplaceDialog.this.rbDirectory)) {
            if (FindReplaceDialog.this.rbDirectory.isSelected()) {
               return FindReplaceDialog.this.txtDirectory;
            } else {
               Component component;
               if (FindReplaceDialog.this.rbCurrentBuffer.isSelected()) {
                  component = FindReplaceDialog.this.rbForward;
               } else {
                  component = FindReplaceDialog.this.findAllBtn;
               }
               return component;
            }
         } else if (c.equals(FindReplaceDialog.this.txtDirectory)) {
            return FindReplaceDialog.this.browseBtn;
         } else if (c.equals(FindReplaceDialog.this.browseBtn)) {
            return FindReplaceDialog.this.txtFiles;
         } else if (c.equals(FindReplaceDialog.this.txtFiles)) {
            return FindReplaceDialog.this.findAllBtn;
         } else if (c.equals(FindReplaceDialog.this.rbForward)) {
            return FindReplaceDialog.this.rbBackward;
         } else if (c.equals(FindReplaceDialog.this.rbBackward)) {
            return FindReplaceDialog.this.findBtn;
         } else if (c.equals(FindReplaceDialog.this.findBtn)) {
            return FindReplaceDialog.this.findAllBtn;
         } else if (c.equals(FindReplaceDialog.this.findAllBtn)) {
            return FindReplaceDialog.this.controlBtn;
         } else {
            return (Component)(c.equals(FindReplaceDialog.this.replaceBtn) ? FindReplaceDialog.this.controlBtn : FindReplaceDialog.this.cbInitialText.getEditor().getEditorComponent());
         }
      }
   }
}
