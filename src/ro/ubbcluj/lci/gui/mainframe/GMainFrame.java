package ro.ubbcluj.lci.gui.mainframe;

import houtput.Token;
import houtput.TokenSelectionEvent;
import houtput.TokenSelectionListener;
import houtput.Whiteboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.MenuElement;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import ro.ubbcluj.lci.errors.AbstractEvaluationError;
import ro.ubbcluj.lci.errors.ErrorMessage;
import ro.ubbcluj.lci.gui.Actions.AEditActions;
import ro.ubbcluj.lci.gui.Actions.AFileActions;
import ro.ubbcluj.lci.gui.Actions.AHelpActions;
import ro.ubbcluj.lci.gui.Actions.AOptionsActions;
import ro.ubbcluj.lci.gui.Actions.AProjectActions;
import ro.ubbcluj.lci.gui.Actions.AToolsActions;
import ro.ubbcluj.lci.gui.Actions.AUMLModelActions;
import ro.ubbcluj.lci.gui.Actions.CompilationInfo;
import ro.ubbcluj.lci.gui.browser.GBrowser;
import ro.ubbcluj.lci.gui.diagrams.DiagramPad;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.gui.diagrams.ToolBarFactory;
import ro.ubbcluj.lci.gui.editor.AbstractPad;
import ro.ubbcluj.lci.gui.editor.Editor;
import ro.ubbcluj.lci.gui.editor.SyntaxDocument;
import ro.ubbcluj.lci.gui.editor.TextDocumentModel;
import ro.ubbcluj.lci.gui.editor.TextDocumentPad;
import ro.ubbcluj.lci.gui.editor.TextDocumentPadFactory;
import ro.ubbcluj.lci.gui.editor.event.DocumentStateEvent;
import ro.ubbcluj.lci.gui.editor.event.DocumentStateListener;
import ro.ubbcluj.lci.gui.editor.event.ViewActivationEvent;
import ro.ubbcluj.lci.gui.editor.event.ViewActivationListener;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.properties.GProperties;
import ro.ubbcluj.lci.gui.properties.TextualDescriptor;
import ro.ubbcluj.lci.gui.tools.ActionButton;
import ro.ubbcluj.lci.gui.tools.ActionMenuItem;
import ro.ubbcluj.lci.gui.tools.GResourceBundle;
import ro.ubbcluj.lci.gui.tools.RecentFiles;
import ro.ubbcluj.lci.gui.tools.tree.errors.ErrorTree;
import ro.ubbcluj.lci.ocl.batcheval.EvaluationEvent;
import ro.ubbcluj.lci.ocl.batcheval.EvaluationListener;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.RepositoryChangeListener;
import ro.ubbcluj.lci.uml.ext.profiles.Profile;
import ro.ubbcluj.lci.uml.ext.profiles.ProfileManager;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.OclEvaluator;
import ro.ubbcluj.lci.utils.search.SearchResult;

public class GMainFrame extends JPanel {
   public static final String NULL_ICON_URL = "/resources/null.gif";
   private ArrayList diagPads = new ArrayList();
   private HashMap commands = new HashMap();
   private GApplication app = GApplication.getApplication();
   private JMenuBar menubar;
   private JToolBar toolbar;
   private JToolBar editorToolbar;
   private BorderLayout mainlayout = new BorderLayout();
   private JPanel frame_center = new JPanel();
   private JPanel toolbar_panel = new JPanel();
   private BorderLayout frame_center_layout = new BorderLayout();
   private JSplitPane left_right = new JSplitPane();
   private JComponent metamodelBrowser;
   private JComponent browser;
   private JComponent fileBrowser;
   private JPanel noProjectPanel = null;
   private JPanel noModelPanel = null;
   private JComponent properties = GProperties.getInstance().getComponent();
   private GDiagramDesktop diagrams = new GDiagramDesktop();
   private JTabbedPane browserspane = new JTabbedPane();
   private JTabbedPane outputPane = new JTabbedPane(3);
   private JComponent output;
   private JTree searchResultsTree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("Search results")));
   private JTextArea log = new JTextArea();
   private Whiteboard ocllog = new Whiteboard();
   private GMainFrame.GMainFrameTokenSelectionListener oclLogListener = new GMainFrame.GMainFrameTokenSelectionListener();
   private MessagePane messagePane = new MessagePane();
   private ErrorTree evaluationErrors;
   private JToolBar activeToolbar;
   private JPanel backgroundPanel = new JPanel();
   private static ErrorLocatingVisitor visitor = new ErrorLocatingVisitor();
   private JSplitPane up_down = new JSplitPane();
   private JPanel editorPanel = new JPanel();
   private JPopupMenu editorViewPopup;
   private JSplitPane up_down2 = new JSplitPane();
   private JPanel diagramPanel;
   private GResourceBundle resources;
   private ro.ubbcluj.lci.gui.editor.mdi.PadContainer viewContainer = new InternalFramePadContainer();
   private static GMainFrame main_frame = new GMainFrame();
   private GMainFrame.GuiUpdater guiUpdater = new GMainFrame.GuiUpdater();
   private Color BACKGROUND_COLOR = new Color(8750486);
   private MessagePane errMessagePane = new MessagePane();

   public static GMainFrame getMainFrame() {
      return main_frame;
   }

   public ro.ubbcluj.lci.gui.editor.mdi.PadContainer getViewContainer() {
      return this.viewContainer;
   }

   private void getActions() {
      this.addToCommandsList(AFileActions.getActions());
      this.addToCommandsList(AEditActions.getActions());
      this.addToCommandsList(AUMLModelActions.getActions());
      this.addToCommandsList(AProjectActions.getActions());
      this.addToCommandsList(AToolsActions.getActions());
      this.addToCommandsList(AOptionsActions.getActions());
      this.addToCommandsList(AHelpActions.getActions());
   }

   private void addToCommandsList(Action[] actions) {
      for(int i = 0; i < actions.length; ++i) {
         this.commands.put(actions[i].getValue("ActionCommandKey"), actions[i]);
      }
   }

   private void updateComponents() {
      JLabel label;
      Action action;
      ImageIcon iconnew;
      JLabel labelnew;
      ImageIcon iconopen;
      JLabel labelopen;
      if (GRepository.getInstance().getUsermodel() != null && GRepository.getInstance().getUsermodel().getModel() != null) {
         this.browser = new JScrollPane(GRepository.getInstance().getUsermodel().getBrowser().getComponent());
      } else {
         if (this.noModelPanel == null) {
            this.noModelPanel = new JPanel();
            this.noModelPanel.setLayout(new BoxLayout(this.noModelPanel, 1));
            this.noModelPanel.setBackground(Color.white);
            label = new JLabel("No model currently active");
            action = (Action)this.commands.get("menubar.umlmodel.newUml");
            iconnew = (ImageIcon)action.getValue("SmallIcon");
            labelnew = new JLabel("Use \"New model\" to create a new model", iconnew, 0);
            labelnew.setFont(new Font("SansSerif", 0, 9));
            action = (Action)this.commands.get("menubar.umlmodel.openUml");
            iconopen = (ImageIcon)action.getValue("SmallIcon");
            labelopen = new JLabel("Use \"Open model\" to open an existing model", iconopen, 0);
            labelopen.setFont(new Font("SansSerif", 0, 9));
            this.noModelPanel.add(label);
            this.noModelPanel.add(Box.createVerticalStrut(5));
            this.noModelPanel.add(labelnew);
            this.noModelPanel.add(Box.createVerticalStrut(5));
            this.noModelPanel.add(labelopen);
         }

         this.browser = new JScrollPane(this.noModelPanel);
      }

      if (GRepository.getInstance().getProject() != null) {
         this.fileBrowser = new JScrollPane(GRepository.getInstance().getProject().getFileBrowser().getComponent());
      } else {
         if (this.noProjectPanel == null) {
            this.noProjectPanel = new JPanel();
            this.noProjectPanel.setLayout(new BoxLayout(this.noProjectPanel, 1));
            this.noProjectPanel.setBackground(Color.white);
            label = new JLabel("No project currently active");
            action = (Action)this.commands.get("menubar.project.newProject");
            iconnew = (ImageIcon)action.getValue("SmallIcon");
            labelnew = new JLabel("Use \"New Project Wizard\" to create a new project", iconnew, 0);
            labelnew.setFont(new Font("SansSerif", 0, 9));
            action = (Action)this.commands.get("menubar.project.openProject");
            iconopen = (ImageIcon)action.getValue("SmallIcon");
            labelopen = new JLabel("Use \"Open project\" to open an existing project", iconopen, 0);
            labelopen.setFont(new Font("SansSerif", 0, 9));
            this.noProjectPanel.add(label);
            this.noProjectPanel.add(Box.createVerticalStrut(5));
            this.noProjectPanel.add(labelnew);
            this.noProjectPanel.add(Box.createVerticalStrut(5));
            this.noProjectPanel.add(labelopen);
         }

         this.fileBrowser = new JScrollPane(this.noProjectPanel);
      }

   }

   public void usermodelChanged() {
      this.clearLog();
      this.clearOCLLog();
      this.clearEvaluationErrors();
      this.closeAllDiagrams();
      this.updateComponents();
      this.setBrowserTree(this.browser);
      this.setFileBrowser(this.fileBrowser);
      this.browserspane.setSelectedIndex(1);
      GUMLModel activeModel = GRepository.getInstance().getUsermodel();
      if (activeModel != null) {
         RepositoryChangeAgent notifier = RepositoryChangeAgent.getAgent(activeModel.getModel());
         if (notifier != null) {
            notifier.addChangeListener(this.guiUpdater);
         } else {
            System.err.println("No agent found for user model");
         }
      }

      if (activeModel != null && activeModel.getModel() != null) {
         this.updateLog("Activated UML model " + activeModel.getModel().getName() + "\n");
      } else {
         this.updateLog("No active model\n");
      }

      this.updateModelActions();
      this.updateEvaluationActions();
      this.updateProjectActions();
      this.focusLog();
   }

   public void activeProjectChanged() {
      this.updateComponents();
      this.setBrowserTree(this.browser);
      this.setFileBrowser(this.fileBrowser);
      if (GRepository.getInstance().getProject() != null) {
         this.updateLog("Activated project " + GRepository.getInstance().getProject().getProjectName() + "\n");
      } else {
         this.log.append("No active project\n");
      }

      this.updateProjectActions();
      this.updateCompilationActions();
      this.updateEvaluationActions();
   }

   private void closeAllDiagrams() {
      for(int i = 0; i < this.diagPads.size(); ++i) {
         this.viewContainer.removePadView((DiagramPad)this.diagPads.get(i));
      }

      this.diagPads.removeAll(this.diagPads);
   }

   private GMainFrame() {
      ((InternalFramePadContainer)this.viewContainer).setBackground(this.BACKGROUND_COLOR);
      this.resources = new GResourceBundle("ro.ubbcluj.lci.gui.MainFrame");
      this.getActions();
      this.menubar = this.createMenubar();
      this.toolbar = this.createToolBar("toolbar", new Insets(1, 1, 1, 1));
      this.editorToolbar = this.createToolBar("editorToolbar", new Insets(0, 0, 0, 0));
      this.editorToolbar.setVisible(false);
//      this.toolbar.setVisible(false);


      try {
         this.createDisplayablePane();
         this.ocllog.addTokenSelectionListener(this.oclLogListener);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.add(errMessagePane);
   }

   public  void writeMessageToTextArea(String message) {
    //  messageTextArea.setText(message);
   }
   private JMenuBar createMenubar() {
      JMenuBar mb = new JMenuBar();
      String str_items = this.resources.getResourceString("menubar");
      String[] menus = this.resources.tokenize(str_items);

      for(int i = 0; i < menus.length; ++i) {
         JMenu menu = this.createMenu(menus[i]);
         mb.add(menu);
         if (menus[i].equals("file") || menus[i].equals("project") || menus[i].equals("umlmodel")) {
            menu.addMenuListener(new GMainFrame.GMainFrameMenuListener());
         }
         if (menus[i].equals("project") || menus[i].equals("umlmodel") ||
             menus[i].equals("edit") || menus[i].equals("tools") ||
                 menus[i].equals("options") || menus[i].equals("help")) {
            menu.setVisible(false);
         }
      }

      return mb;
   }

   private JMenu createMenu(String key) {
      String title = this.resources.getResourceString(key + "Label");
      if (title.equals("File")) {
         title = "功能";
      }
      JMenu me = new JMenu(title);
      String str_items = this.resources.getResourceString(key);
      String[] items = this.resources.tokenize(str_items);

      for(int i = 0; i < items.length; ++i) {
         if (key.equals("file")) {
            if (!items[i].equals("-") && !items[i].equals("exit")  && !items[i].equals("chooseFile")) {
               continue;
            }
         }

         if (items[i].equals("-")) {
            me.addSeparator();
         } else {
            JMenuItem m_item = this.createMenuItem(items[i]);
            me.add(m_item);
            RecentFiles rf;
            JMenu menu;
            if (key.equals("project") && items[i].equals("openProject")) {
               rf = RecentFiles.getInstance();
               menu = rf.buildMenu(RecentFiles.PROJECT_FILES);
               me.add(menu);
            }

            if (key.equals("file") && items[i].equals("openFile")) {
               rf = RecentFiles.getInstance();
               menu = rf.buildMenu(RecentFiles.OCL_FILES);
               me.add(menu);
            }

            if (key.equals("umlmodel") && items[i].equals("openUml")) {
               rf = RecentFiles.getInstance();
               menu = rf.buildMenu(RecentFiles.MODEL_FILES);
               me.add(menu);
            }
            if (m_item.getText().equals("Choose File")) {
               m_item.setText("模型验证");
            } else if (m_item.getText().equals("Exit application")) {
               m_item.setText("退出");
            }
         }
      }

      return me;
   }

   private JMenuItem createMenuItem(String key) {
      String comm_key = this.resources.getResourceString(key + "CommandKey");
      Action action = (Action)this.commands.get(comm_key);
      String label = this.resources.getResourceString(key + "Label");
      action.putValue("Name", label);
      boolean iconNone = false;
      String url = this.resources.getResource(key + "Image");
      if (url != null && !url.equals("none")) {
         action.putValue("SmallIcon", new ImageIcon(this.getClass().getResource(url)));
      } else {
         iconNone = true;
      }

      if (iconNone) {
         action.putValue("SmallIcon", new ImageIcon(this.getClass().getResource("/resources/null.gif")));
      }

      JMenuItem m_item = new ActionMenuItem(action);
      return m_item;
   }

   private JToolBar createToolBar(String id, Insets insets) {
      JToolBar tb = new JToolBar();
      String tools = this.resources.getResourceString(id);
      String[] t_items = this.resources.tokenize(tools);

      for(int i = 0; i < t_items.length; ++i) {
         if (t_items[i].equals("-")) {
            tb.addSeparator();
         } else {
            JButton bt = this.createToolButton(t_items[i]);
            bt.setMargin(insets);
            tb.add(bt);
         }
      }

      tb.setBorder(BorderFactory.createEtchedBorder());
      tb.setRollover(true);
      return tb;
   }

   private JPopupMenu createPopup(String id) {
      JPopupMenu pum = new JPopupMenu();
      String res = this.resources.getResourceString(id);
      String[] items = this.resources.tokenize(res);

      for(int i = 0; i < items.length; ++i) {
         if ("-".equals(items[i])) {
            pum.addSeparator();
         } else {
            JMenuItem mi = this.createMenuItem(items[i]);
            pum.add(mi);
         }
      }

      return pum;
   }

   private JButton createToolButton(String key) {
      String comm_key = this.resources.getResourceString(key + "CommandKey");
      Action action = (Action)this.commands.get(comm_key);
      String url = this.resources.getResource(key + "Image");
      if (url != null && !url.equals("none")) {
         action.putValue("SmallIcon", new ImageIcon(this.getClass().getResource(url)));
      }

      String tool_tip = this.resources.getResourceString(key + "Tooltip");
      JButton button = new ActionButton(action);
      button.setToolTipText(tool_tip);
      String urld = this.resources.getResource(key + "Disabled");
      if (urld != null && !urld.equals("none")) {
         button.setDisabledIcon(new ImageIcon(this.getClass().getResource(urld)));
      }

      return button;
   }

   private void configureEditor() {
      Editor ed = this.app.getEditor();
      this.editorViewPopup = this.createPopup("editorViewPopup");
      ed.setView(this.viewContainer);
      ed.addDocumentStateListener(this.guiUpdater);
      this.viewContainer.addViewActivationListener(this.guiUpdater);
      this.configureListeners();
      this.configureKeyboardActions();
      TextDocumentPadFactory.getFactory().setPopup(this.createPopup("editorContextualPopup"));
      TextDocumentPadFactory.getFactory().addMouseListener(new OclEvaluator());
   }

   private void configureListeners() {
      TextDocumentPadFactory.getFactory().addCaretListener(this.guiUpdater);
   }

   private void configureKeyboardActions() {
      TextDocumentPadFactory factory = TextDocumentPadFactory.getFactory();
      factory.addAction("C+o", AFileActions.openFileAction);
      factory.addAction("C+F4", AFileActions.closeFileAction);
      factory.addAction("C+s", AFileActions.saveFileAction);
      factory.addAction("C+n", AFileActions.newFileAction);
      factory.addAction("C+c", AEditActions.copyAction);
      factory.addAction("C+x", AEditActions.cutAction);
      factory.addAction("C+v", AEditActions.pasteAction);
      factory.addAction("C+z", AEditActions.undoAction);
      factory.addAction("C+y", AEditActions.redoAction);
      factory.addAction("C+f", AEditActions.findAction);
      factory.addAction("C+t", AToolsActions.evaluateSelAction);
   }

   private void createDisplayablePane() throws Exception {
      GRepository.getInstance().getEvaluationSystem().addEvaluationListener(this.guiUpdater);
      this.setLayout(this.mainlayout);
      this.metamodelBrowser = new JScrollPane(GRepository.getInstance().getMetamodel().getBrowser().getComponent());
      this.updateComponents();
//      this.metamodelBrowser.setVisible(false);

      this.left_right.setOneTouchExpandable(true);
      this.browserspane.setTabPlacement(3);
      this.browserspane.setMinimumSize(new Dimension(160, 110));
      this.browserspane.setPreferredSize(new Dimension(160, 110));
//      this.browserspane.setVisible(false);

      this.up_down.setOrientation(0);
      this.up_down.setOneTouchExpandable(true);
//      up_down.setVisible(false);


      this.up_down2.setOrientation(0);
      this.up_down2.setOneTouchExpandable(true);
//      this.up_down2.setVisible(false);

      this.frame_center.setLayout(this.frame_center_layout);
      this.add(this.frame_center, "Center");
      this.frame_center.add(this.up_down, "Center");

      this.output = new JPanel(new GridLayout(1, 1));
      this.log.setEditable(false);
      this.log.setAutoscrolls(true);
      this.outputPane.add("LOG", new JScrollPane(this.log));
      this.searchResultsTree.setCellRenderer(new GMainFrame.SearchResultsCellRenderer());
      this.messagePane.addMouseListener(new GMainFrame.ErrorLocatingListener());
      this.outputPane.add("Messages", this.messagePane);
      this.outputPane.add("OCL output", new JScrollPane(this.ocllog));
      this.evaluationErrors = new ErrorTree(GRepository.getInstance().getMetamodel().getModel());
      this.outputPane.add("Evaluation", new JScrollPane(this.evaluationErrors.getComponent()));
      this.evaluationErrors.getComponent().addMouseListener(new GMainFrame.EvaluationErrorLocatingListener());
      this.outputPane.add("Search results", new JScrollPane(this.searchResultsTree));
      this.searchResultsTree.addMouseListener(new GMainFrame.SearchResultsListener());
      this.backgroundPanel.setBackground(this.BACKGROUND_COLOR);
      this.editorPanel.setLayout(new BorderLayout());
      this.editorPanel.add(this.backgroundPanel, "Center");
      this.left_right.add(this.editorPanel, "right");
      this.left_right.add(this.up_down2, "left");
      this.up_down.add(this.output, "bottom");
      this.up_down.add(this.left_right, "top");
      ((JPanel)this.output).add(this.outputPane);
//      outputPane.setVisible(false);

      this.diagramPanel = new JPanel(new BorderLayout());
      this.diagramPanel.add(this.diagrams, "Center");
      this.diagramPanel.add(Box.createVerticalGlue(), "South");
      this.diagramPanel.setMinimumSize(new Dimension(1, 60));
      diagramPanel.setVisible(false);

      this.configureEditor();
      this.up_down2.add(this.browserspane, "top");
      GProperties.getInstance().setTextualDescriptor(new TextualDescriptor() {
         public String getDescription(Object x) {
            String result = "";
            if (x instanceof Element) {
               result = ((Element)x).getMetaclassName();
            }

            if (x instanceof GAbstractDiagram) {
               result = "Diagram";
            }

            return result;
         }
      });
      this.up_down2.add(this.properties, "bottom");
      this.browserspane.add(this.fileBrowser, "Project");
      this.browserspane.add(this.browser, "User Model");
      this.browserspane.add(this.metamodelBrowser, "Metamodel");
      this.toolbar_panel.setLayout(new BorderLayout());
      this.editorToolbar.setAlignmentX(0.0F);
      this.editorToolbar.setFloatable(false);
      this.toolbar_panel.add(this.toolbar, "Center");
      this.frame_center.add(this.toolbar_panel, "North");
      this.add(this.menubar, "North");
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      this.up_down.setDividerLocation(screen.height * 6 / 10);
      this.up_down.setResizeWeight(1.0D);
      this.up_down2.setDividerLocation(screen.height / 3);
      this.up_down2.setResizeWeight(0.0D);
      this.left_right.setDividerLocation(screen.width / 5);
      final JProgressBar barmem = new JProgressBar(0, 100);
      barmem.setSize(100, 30);
      barmem.setStringPainted(true);
      barmem.setVisible(true);
      barmem.setToolTipText("Double click to run the garbage collector");
      barmem.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
               System.gc();
            }

         }

         public void mouseEntered(MouseEvent e) {
         }

         public void mouseExited(MouseEvent e) {
         }

         public void mousePressed(MouseEvent e) {
         }

         public void mouseReleased(MouseEvent e) {
         }
      });
      (new Thread() {
         public void run() {
            while(true) {
               long free = Runtime.getRuntime().freeMemory();
               long total = Runtime.getRuntime().totalMemory();
               barmem.setValue((int)((total - free) * 100L / total));
               barmem.setString("" + (total - free) / 1024L + "KB / " + total / 1024L + "KB");

               try {
                  Thread.sleep(1000L);
               } catch (InterruptedException var6) {
               }
            }
         }
      }).start();
      JPanel statusPanel = new JPanel(new BorderLayout());
      statusPanel.add(GStatusBar.getStatusBar(), "Center");
      JPanel subPanel = new JPanel(new BorderLayout());
      subPanel.add(barmem, "East");
      statusPanel.add(subPanel, "East");
      this.add(statusPanel, "South");
      subPanel.setVisible(false);
   }

   protected void setBrowserTree(Component new_browser) {
      this.browserspane.remove(1);
      this.browserspane.add(new_browser, 1);
      this.browserspane.setTitleAt(1, "UserModel");
   }

   private void setFileBrowser(Component newFileBrowser) {
      this.browserspane.remove(0);
      this.browserspane.add(newFileBrowser, 0);
      this.browserspane.setTitleAt(0, "Project");
   }

   public void addNewDiagramFrame(GAbstractDiagram diagram) {
      DiagramPad dpad;
      if (diagram.getPad() == null) {
         dpad = new DiagramPad(diagram, this.viewContainer);
         diagram.setPad(dpad);
         this.diagPads.add(dpad);
      } else {
         dpad = (DiagramPad)diagram.getPad();
      }

      this.viewContainer.activatePad(dpad);
   }

   public void removeDiagramFrame(GAbstractDiagram di) {
      AbstractPad ap = di.getPad();
      if (ap != null) {
         this.viewContainer.removePadView(ap);
      }

   }

   public void addExistingDiagramFrame(GAbstractDiagram diagram) {
      this.addNewDiagramFrame(diagram);
   }

   public void updateLog(String line) {
      this.log.append(line);
      this.log.setCaretPosition(this.log.getText().length());
      this.focusLog();
   }

   public void updateOCLLog(String line) {
      this.ocllog.append(line);
      this.focusOCLLog();
   }

   public void updateOCLLog(Collection c) {
      this.ocllog.append(c);
      this.focusOCLLog();
   }

   public void updateSearchResults(DefaultMutableTreeNode root) {
      DefaultTreeModel mdl = (DefaultTreeModel)this.searchResultsTree.getModel();
      ((DefaultMutableTreeNode)mdl.getRoot()).add(root);
      mdl.reload();
      this.outputPane.setSelectedIndex(4);
   }

   public void clearOCLLog() {
      this.ocllog.clear();
   }

   public void clearLog() {
      this.log.setText("");
   }

   public void clearErrorMessages() {
      this.messagePane.clear();
   }

   public void clearEvaluationErrors() {
      this.evaluationErrors.clearErrors();
   }

   public void updateMessages(List errors) {
      int i = 0;

      for(int s = errors.size(); i < s; ++i) {
         this.messagePane.addMessage(errors.get(i));
         this.errMessagePane.addMessage(errors.get(i));
      }

      this.messagePane.focusLastGroup();
      this.focusErros();
   }

   public void updateMessages(Object error) {
      this.messagePane.addMessage(error);
      this.messagePane.focusLastGroup();
      this.focusErros();
      this.errMessagePane.addMessage(error);
      this.messagePane.focusLastGroup();
   }

   public void updateInfoMessages(Object o) {
      this.messagePane.addMessage(o);
      this.focusErros();
   }

   public void focusLog() {
      this.outputPane.setSelectedIndex(0);
   }

   private void focusOCLLog() {
      this.outputPane.setSelectedIndex(2);
   }

   private void focusErros() {
      this.outputPane.setSelectedIndex(1);
   }

   public void focusBrowser(GBrowser b) {
      JComponent comp = b.getComponent();
      if (((JScrollPane)this.browser).getViewport().getComponent(0) == comp) {
         this.browserspane.setSelectedIndex(1);
      } else if (((JScrollPane)this.metamodelBrowser).getViewport().getComponent(0) == comp) {
         this.browserspane.setSelectedIndex(2);
      }

   }

   public void focusFileTree() {
      this.browserspane.setSelectedIndex(0);
   }

   public void updateModelActions() {
      GUMLModel activeModel = GRepository.getInstance().getUsermodel();
      boolean bAnyActiveModel = activeModel != null;
      AUMLModelActions.reverseDTDAction.setEnabled(true);
      AUMLModelActions.saveDTDAction.setEnabled(bAnyActiveModel);
      AUMLModelActions.importJarAction.setEnabled(bAnyActiveModel);
      AUMLModelActions.importJDKAction.setEnabled(bAnyActiveModel);
      AUMLModelActions.closeUmlAction.setEnabled(bAnyActiveModel);
      AUMLModelActions.importBoldAction.setEnabled(bAnyActiveModel);
      AUMLModelActions.importUseAction.setEnabled(bAnyActiveModel);
      AUMLModelActions.saveUmlAction.setEnabled(bAnyActiveModel && activeModel.isDirty());
      AUMLModelActions.saveUmlAsAction.setEnabled(bAnyActiveModel);
      AToolsActions.codeGenAction.setEnabled(bAnyActiveModel);
      AToolsActions.stdCheckAction.setEnabled(bAnyActiveModel);
   }

   private void updateEditActions(AbstractPad activePad) {
      boolean isText = activePad != null && activePad.getType() == 0;
      AEditActions.selectAllAction.setEnabled(isText);
      AEditActions.pasteAction.setEnabled(isText);
      if (!isText) {
         AEditActions.undoAction.setEnabled(false);
         AEditActions.redoAction.setEnabled(false);
         AEditActions.cutAction.setEnabled(false);
         AEditActions.copyAction.setEnabled(false);
      } else {
         TextDocumentPad pad = (TextDocumentPad)activePad;
         JEditTextArea ta = pad.getView();
         int ss = ta.getSelectionStart();
         int se = ta.getSelectionEnd();
         AEditActions.copyAction.setEnabled(ss != se);
         AEditActions.cutAction.setEnabled(ss != se && !pad.getModel().isReadOnly());
         SyntaxDocument mdl = pad.getModel().getModel();
         AEditActions.undoAction.setEnabled(mdl.canUndo());
         if (mdl.canUndo()) {
            AEditActions.undoAction.putValue("Name", mdl.getUndoPresentationName());
         } else {
            AEditActions.undoAction.putValue("Name", "Undo");
         }

         AEditActions.redoAction.setEnabled(mdl.canRedo());
         if (mdl.canRedo()) {
            AEditActions.redoAction.putValue("Name", mdl.getRedoPresentationName());
         } else {
            AEditActions.redoAction.putValue("Name", "Redo");
         }
      }

   }

   public void updateCompilationActions() {
      AbstractPad activePad = this.viewContainer.getActivePad();
      this.updateCompilationActions(activePad);
   }

   public void updateCompilationActions(AbstractPad activePad) {
      GAbstractProject proj = GRepository.getInstance().getProject();
      boolean isText = activePad != null && activePad.getType() == 0;
      AToolsActions.compileFileAction.setEnabled(isText);
      AToolsActions.compileAction.setEnabled(proj != null);
   }

   public void updateFilesActions() {
      this.updateFilesActions(this.viewContainer.getActivePad());
   }

   public void updateFilesActions(AbstractPad activePad) {
      boolean bAnyActivePad = activePad != null;
      AFileActions.closeAllAction.setEnabled(bAnyActivePad);
      AFileActions.closeFileAction.setEnabled(bAnyActivePad);
      if (activePad != null) {
         AFileActions.closeFileAction.putValue("Name", "Close \"" + activePad.getTitle() + "\"");
      } else {
         AFileActions.closeFileAction.putValue("Name", "Close");
      }

      AFileActions.saveAllAction.setEnabled(bAnyActivePad);
      boolean isTextPad = activePad != null && activePad.getType() == 0;
      AFileActions.saveFileAsAction.setEnabled(isTextPad);
      if (isTextPad) {
         TextDocumentPad pad = (TextDocumentPad)activePad;
         TextDocumentModel mdl = pad.getModel();
         AFileActions.saveFileAction.setEnabled(mdl.isDirty());
         AFileActions.saveFileAction.putValue("Name", "Save \"" + mdl.getShortFileName() + "\"");
      } else {
         AFileActions.saveFileAction.setEnabled(false);
         AFileActions.saveFileAction.putValue("Name", "Save");
      }

   }

   public void updateProjectActions() {
      GAbstractProject activeProject = GRepository.getInstance().getProject();
      boolean bAnyActiveProject = activeProject != null;
      AProjectActions.addFilesAction.setEnabled(bAnyActiveProject);
      AProjectActions.closeProjectAction.setEnabled(bAnyActiveProject);
      AProjectActions.removeFileAction.setEnabled(bAnyActiveProject);
      AProjectActions.saveProjectAction.setEnabled(bAnyActiveProject && activeProject.isDirty());
      AProjectActions.saveProjectAsAction.setEnabled(bAnyActiveProject);
   }

   public void updateEvaluationActions() {
      GRepository ri = GRepository.getInstance();
      CompilationInfo[] lastCompilationInfos = ri.getLastCompilationInfos();
      boolean bBatchAvailable = lastCompilationInfos.length > 0;
      if (bBatchAvailable) {
         if (lastCompilationInfos[0].getTargetModel() == null) {
            bBatchAvailable = false;
         }

         if (bBatchAvailable) {
            for(int i = 0; i < lastCompilationInfos.length; ++i) {
               if (!lastCompilationInfos[i].isCompiled()) {
                  bBatchAvailable = false;
               }
            }
         }
      }

      AToolsActions.checkAction.setEnabled(bBatchAvailable);
      AToolsActions.partialCheckAction.setEnabled(bBatchAvailable);
      AbstractPad activePad = this.viewContainer.getActivePad();
      boolean bIsTextPad = activePad instanceof TextDocumentPad;
      if (bIsTextPad) {
         String fileName = ((TextDocumentPad)activePad).getModel().getFileName();
         ProfileManager pm = ri.getProfileManager();
         CompilationInfo ci = null;
         boolean bProfile = false;
         if (pm != null) {
            Profile p = pm.getProfileForFile(fileName);
            if (p != null) {
               bProfile = true;
            }
         }

         if (bProfile) {
            AToolsActions.evaluateSelAction.setEnabled(true);
         } else {
            ci = ri.getInfoForFile(fileName);
            AToolsActions.evaluateSelAction.setEnabled(ci != null && (ci.isCompiled() || ci.getTargetModel() != null && ci.getRoot() != null));
         }
      } else {
         AToolsActions.evaluateSelAction.setEnabled(false);
      }

   }

   private class GMainFrameMenuListener implements MenuListener {
      private GMainFrameMenuListener() {
      }

      public void menuCanceled(MenuEvent e) {
      }

      public void menuDeselected(MenuEvent e) {
      }

      public void menuSelected(MenuEvent e) {
         JMenu menu = (JMenu)e.getSource();
         String menuName = menu.getText();
         GResourceBundle bundle = new GResourceBundle("ro.ubbcluj.lci.gui.MainFrame");
         if (menuName.equals(bundle.getResource("fileLabel")) || menuName.equals(bundle.getResource("umlmodelLabel")) || menuName.equals(bundle.getResource("projectLabel"))) {
            for(int k = 0; k < menu.getSubElements().length; ++k) {
               MenuElement mel = menu.getSubElements()[k];

               for(int i = 0; i < mel.getSubElements().length; ++i) {
                  Component c = mel.getSubElements()[i].getComponent();
                  if (c instanceof JMenu && (((JMenu)c).getText().equals("Open recent") || ((JMenu)c).getText().equals("No recent files available"))) {
                     JMenu rm = (JMenu)c;
                     rm.removeAll();
                     ArrayList items = null;
                     if (menuName.equals(bundle.getResource("projectLabel"))) {
                        RecentFiles.setFile(RecentFiles.PROJECT_FILES);
                        items = RecentFiles.getInstance().getMenuItems(RecentFiles.PROJECT_FILES);
                     }

                     if (menuName.equals(bundle.getResource("fileLabel"))) {
                        RecentFiles.setFile(RecentFiles.OCL_FILES);
                        items = RecentFiles.getInstance().getMenuItems(RecentFiles.OCL_FILES);
                     }

                     if (menuName.equals(bundle.getResource("umlmodelLabel"))) {
                        RecentFiles.setFile(RecentFiles.MODEL_FILES);
                        items = RecentFiles.getInstance().getMenuItems(RecentFiles.MODEL_FILES);
                     }

                     if (items.size() > 0) {
                        rm.setEnabled(true);
                        if (rm.getText().equals("No recent files available")) {
                           rm.setText("Open recent");
                        }

                        for(int j = 0; j < items.size(); ++j) {
                           rm.add((JMenuItem)items.get(j));
                        }
                     } else {
                        rm.setText("No recent files available");
                        rm.setEnabled(false);
                     }
                  }
               }
            }

         }
      }
   }

   private class SearchResultsListener extends MouseAdapter {
      private SearchResultsListener() {
      }

      public void mouseClicked(MouseEvent evt) {
         if (evt.getClickCount() >= 2) {
            TreePath tp = GMainFrame.this.searchResultsTree.getSelectionPath();
            if (tp != null) {
               DefaultMutableTreeNode c = (DefaultMutableTreeNode)tp.getLastPathComponent();
               Object x = c.getUserObject();
               if (x instanceof SearchResult) {
                  SearchResult sr = (SearchResult)x;
                  DefaultMutableTreeNode parent = (DefaultMutableTreeNode)c.getParent();
                  Object pf = parent.getUserObject();
                  if (pf instanceof File) {
                     String name = ((File)pf).getAbsolutePath();
                     if (GMainFrame.this.app.getEditor().activateFileView(name) == null) {
                        return;
                     }
                  }

                  try {
                     ((TextDocumentPad)GMainFrame.this.viewContainer.getActivePad()).getView().select(sr.getStart(), sr.getEnd());
                  } catch (Exception var9) {
                  }
               }
            }
         }

      }
   }

   private static class SearchResultsCellRenderer extends DefaultTreeCellRenderer {
      private SearchResultsCellRenderer() {
      }

      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
         super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
         if (((DefaultMutableTreeNode)value).getUserObject() instanceof File) {
            this.setIcon(new ImageIcon((Integer.class).getResource("/images/default_file_image.gif")));
         }

         return this;
      }
   }

   private class GMainFrameTokenSelectionListener implements TokenSelectionListener {
      private GMainFrameTokenSelectionListener() {
      }

      public void tokenSelected(TokenSelectionEvent e) {
         Token t = (Token)e.getSource();
         ModelFactory.fireModelEvent(t.getUserObject(), (Object)null, 0);
      }
   }

   private class GuiUpdater implements ViewActivationListener, CaretListener, DocumentStateListener, EvaluationListener, RepositoryChangeListener {
      private GuiUpdater() {
      }

      public void propertyChanged(ChangeEventObject ceo) {
         GUMLModel activeModel = GRepository.getInstance().getUsermodel();
         activeModel.setDirty(true);
         int type = ceo.getKind();
         switch(type) {
         case 3:
            Object source = ceo.getSource();
            GBrowser modelBrowser = activeModel.getBrowser();
            DefaultMutableTreeNode node = modelBrowser.getNodeFor(source);
            if (node != null) {
               Enumeration nodesAffected = node.breadthFirstEnumeration();

               while(nodesAffected.hasMoreElements()) {
                  DefaultMutableTreeNode cNode = (DefaultMutableTreeNode)nodesAffected.nextElement();
                  Object userObject = cNode.getUserObject();
                  if (userObject instanceof GAbstractDiagram) {
                     GAbstractDiagram diagramToClose = (GAbstractDiagram)userObject;
                     AbstractPad padToClose = diagramToClose.getPad();
                     int close = GMainFrame.this.viewContainer.closeAll(new AbstractPad[]{padToClose});
                     if (close == 4) {
                        GMainFrame.this.updateLog("Could not close diagram: " + diagramToClose.getName() + '\n');
                     }
                  }
               }
            }

            ModelFactory.fireModelEvent(source, (Object)null, 30);
         default:
            GMainFrame.this.updateModelActions();
         }
      }

      public void viewClosed(AbstractPad padClosed) {
      }

      public void viewActivated(ViewActivationEvent evt) {
         AbstractPad activePad = evt.getActivePad();
         AbstractPad oldPad = evt.getOldActivePad();
         GMainFrame.this.updateEditActions(activePad);
         GMainFrame.this.updateFilesActions(activePad);
         GMainFrame.this.updateCompilationActions(activePad);
         GMainFrame.this.updateEvaluationActions();
         if (activePad == null) {
            GMainFrame.this.viewContainer.setViewPopup((JPopupMenu)null);
            if (GMainFrame.this.activeToolbar != null) {
               GMainFrame.this.editorPanel.remove(GMainFrame.this.activeToolbar);
               GMainFrame.this.activeToolbar = null;
            }

            GMainFrame.this.editorPanel.remove(GMainFrame.this.viewContainer.getComponent());
            GMainFrame.this.editorPanel.add(GMainFrame.this.backgroundPanel, "Center");
            GMainFrame.this.editorPanel.validate();
            GMainFrame.this.editorPanel.repaint();
         } else {
            if (oldPad == null) {
               GMainFrame.this.editorPanel.remove(GMainFrame.this.backgroundPanel);
               GMainFrame.this.editorPanel.add(GMainFrame.this.viewContainer.getComponent(), "Center");
               GMainFrame.this.editorPanel.validate();
               GMainFrame.this.editorPanel.repaint();
            }

            if (activePad.getType() == 0) {
               GMainFrame.this.viewContainer.setViewPopup(GMainFrame.this.editorViewPopup);
            } else {
               GMainFrame.this.viewContainer.setViewPopup(((DiagramPad)activePad).getPopupMenu());
            }

            if (oldPad == null || activePad.getType() != 0 || activePad.getType() != oldPad.getType()) {
               if (activePad.getType() == 0) {
                  if (GMainFrame.this.activeToolbar != null) {
                     GMainFrame.this.editorPanel.remove(GMainFrame.this.activeToolbar);
                     GMainFrame.this.editorPanel.validate();
                  }

                  GMainFrame.this.editorPanel.add(GMainFrame.this.editorToolbar, "North");
                  GMainFrame.this.activeToolbar = GMainFrame.this.editorToolbar;
                  GMainFrame.this.editorPanel.validate();
                  GMainFrame.this.editorPanel.repaint();
               } else {
                  if (oldPad != null && oldPad.getType() == 1) {
                     GAbstractDiagram oldDiag = ((DiagramPad)oldPad).getDiagram();
                     GAbstractDiagram newDiag = ((DiagramPad)activePad).getDiagram();
                     if (oldDiag.getClass().equals(newDiag.getClass())) {
                        GMainFrame.this.activeToolbar = ToolBarFactory.getToolBar(((DiagramPad)activePad).getDiagram());
                        return;
                     }
                  }

                  if (GMainFrame.this.activeToolbar != null) {
                     GMainFrame.this.editorPanel.remove(GMainFrame.this.activeToolbar);
                  }

                  GMainFrame.this.activeToolbar = ToolBarFactory.getToolBar(((DiagramPad)activePad).getDiagram());
                  GMainFrame.this.editorPanel.add(GMainFrame.this.activeToolbar, "North");
                  GMainFrame.this.activeToolbar.validate();
                  GMainFrame.this.activeToolbar.repaint();
                  GMainFrame.this.editorPanel.validate();
                  GMainFrame.this.editorPanel.repaint();
               }

            }
         }
      }

      public void caretUpdate(CaretEvent evt) {
         GMainFrame.this.updateEditActions(GMainFrame.this.viewContainer.getActivePad());
      }

      public void documentSaved(DocumentStateEvent evt) {
         GMainFrame.this.updateFilesActions(GMainFrame.this.viewContainer.getActivePad());
      }

      public void documentModified(DocumentStateEvent evt) {
         GRepository ri = GRepository.getInstance();
         TextDocumentPad p = (TextDocumentPad)GMainFrame.this.viewContainer.getActivePad();
         GMainFrame.this.updateFilesActions(p);
         TextDocumentModel mdl = p.getModel();
         CompilationInfo ci = ri.getInfoForFile(mdl.getFileName());
         if (ci != null) {
            ri.removeInfo(ci);
         }

         GMainFrame.this.updateEvaluationActions();
      }

      public void documentStateChanged(TextDocumentModel mdl) {
         TextDocumentPad a = (TextDocumentPad)GMainFrame.this.viewContainer.getActivePad();
         GMainFrame.this.updateEditActions(a);
      }

      public void evaluationCancelled(EvaluationEvent evt) {
         GMainFrame.this.updateLog("Evaluation canceled.\n");
      }

      public void evaluationCompleted(final EvaluationEvent evt) {
         Thread updater = new Thread("GUI updater - evaluation results") {
            public void run() {
               GRepository ri = GRepository.getInstance();
               GMainFrame.this.updateLog(evt.getTotal() + " evaluations requested.\n");
               GMainFrame.this.updateLog(evt.getCount() + " have been performed, " + evt.getErrorCount() + " problem(s) found.");
               if (evt.getErrorCount() > 0L) {
                  GMainFrame.this.setCursor(new Cursor(3));
                  GStatusBar.getStatusBar().print("Updating message tree...");
                  GMainFrame.this.evaluationErrors.setUserModel(ri.getEvaluationSystem().getUserModel());
                  List bcrErrors = ri.getEvaluationSystem().getBcrErrorMessages();
                  List wfrErrors = ri.getEvaluationSystem().getWfrErrorMessages();
                  GMainFrame.this.evaluationErrors.setUserModelErrors(bcrErrors);
                  GMainFrame.this.evaluationErrors.setMetamodelErrors(wfrErrors);
                  GMainFrame.this.updateLog(" Please check the 'Evaluation' tab.\n");
                  GStatusBar.getStatusBar().print("Model checking finished.");
                  GMainFrame.this.setCursor(new Cursor(0));
               } else {
                  GMainFrame.this.updateLog("\nModel appears to be correct according to the selected rules.\n");
               }

               ri.getEvaluationSystem().clearData();
               GMainFrame.this.focusLog();
            }
         };
         updater.start();
      }
   }

   private class EvaluationErrorLocatingListener extends MouseAdapter {
      private EvaluationErrorLocatingListener() {
      }

      public void mousePressed(MouseEvent evt) {
         Object selectedObject = GMainFrame.this.evaluationErrors.getSelectedObject();
         if (selectedObject instanceof AbstractEvaluationError) {
            AbstractEvaluationError err = (AbstractEvaluationError)selectedObject;
            err.accept(GMainFrame.visitor);
         }

      }
   }

   private class ErrorLocatingListener extends MouseAdapter {
      private ErrorLocatingListener() {
      }

      public void mousePressed(MouseEvent ev) {
         Object selectedObject = GMainFrame.this.messagePane.getSelectedObject();
         if (selectedObject instanceof ErrorMessage) {
            ErrorMessage msg = (ErrorMessage)selectedObject;
            msg.accept(GMainFrame.visitor);
         }

      }
   }
}
