package ro.ubbcluj.lci.ocl.batcheval;

import java.awt.Component;
import java.awt.Container;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import ro.ubbcluj.lci.gui.tools.tree.MyTreeSelectionListener;
import ro.ubbcluj.lci.gui.tools.tree.SelectionTreeCellRenderer;
import ro.ubbcluj.lci.gui.tools.tree.SelectionTreeNode;
import ro.ubbcluj.lci.utils.Utils;

public class CustomizationDialog extends JDialog implements ActionListener {
   private static String AC_EVALUATE = "Evaluate";
   private static String AC_CANCEL = "Cancel";
   private static SelectionTreeNode temp = new SelectionTreeNode("TEMP");
   private JTree userModelTree;
   private JTree referencedModelsTree;
   private JScrollPane spUserModel;
   private JScrollPane spMetamodel;
   private JLabel jLabel1 = new JLabel();
   private JLabel jLabel2 = new JLabel();
   private JButton btnEvaluate = new JButton();
   private JButton btnCancel = new JButton();
   private JPanel jPanel1 = new JPanel();
   private GridBagLayout gridBagLayout1 = new GridBagLayout();
   private GridBagLayout gridBagLayout2 = new GridBagLayout();
   private int exitCode;

   public CustomizationDialog(JFrame parent) {
      super(parent, "Customize evaluation process", true);
      this.initialize();
   }

   public void actionPerformed(ActionEvent evt) {
      String ac = evt.getActionCommand();
      if (AC_CANCEL.equals(ac)) {
         this.exitCode = 2;
      } else {
         this.exitCode = 0;
      }

      this.setVisible(false);
   }

   public int getExitCode() {
      return this.exitCode;
   }

   public void loadReferencedModelsTree(DefaultMutableTreeNode dmtn) {
      DefaultTreeModel dtm = (DefaultTreeModel)this.referencedModelsTree.getModel();
      DefaultMutableTreeNode root = (DefaultMutableTreeNode)dtm.getRoot();
      root.add(dmtn);
      this.referencedModelsTree.setModel(dtm);
      dtm.reload();
   }

   public void reloadReferencedModelsTree() {
      ((DefaultTreeModel)this.referencedModelsTree.getModel()).reload();
   }

   public void loadUserModelTree(DefaultMutableTreeNode root) {
      DefaultTreeModel mdl = new DefaultTreeModel(root);
      this.userModelTree.setModel(mdl);
      mdl.reload();
   }

   public void appendUserModel(DefaultMutableTreeNode dmtn) {
      DefaultTreeModel mdl = (DefaultTreeModel)this.referencedModelsTree.getModel();
      DefaultMutableTreeNode root = (DefaultMutableTreeNode)mdl.getRoot();
      root.add(dmtn);
      mdl.reload(dmtn);
   }

   public void defaultUserModel() {
      DefaultTreeModel defModel = new DefaultTreeModel(temp);
      this.userModelTree.setModel(defModel);
      DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.referencedModelsTree.getModel().getRoot();
      if (root.getChildCount() > 1) {
         root.remove(1);
      }

   }

   public void ensureSelection() {
      SelectionTreeNode root = (SelectionTreeNode)this.referencedModelsTree.getModel().getRoot();
      root.setSelected(true);
   }

   public void invoke() {
      this.setVisible(true);
   }

   protected void processWindowEvent(WindowEvent e) {
      if (e.getID() != 201) {
         super.processWindowEvent(e);
      } else {
         this.setVisible(false);
      }

   }

   private void initialize() {
      this.setFocusTraversalPolicy(new CustomizationDialog.DirectFocusTraversalPolicy());
      this.userModelTree = new JTree();
      this.userModelTree.setModel(new DefaultTreeModel(temp));
      updateTree(this.userModelTree);
      this.userModelTree.setToolTipText("Packages in the user model");
      this.referencedModelsTree = new JTree();
      this.referencedModelsTree.setModel(new DefaultTreeModel(new SelectionTreeNode("Models")));
      updateTree(this.referencedModelsTree);
      this.referencedModelsTree.setToolTipText("Classes in the metamodel and the user model and associated constraints");
      this.spUserModel = new JScrollPane(this.userModelTree);
      this.spMetamodel = new JScrollPane(this.referencedModelsTree);
      this.jLabel1.setText("Active user model:");
      this.jLabel1.setLabelFor(this.spUserModel);
      this.jLabel2.setText("Model/metamodel level constraints:");
      this.jLabel2.setLabelFor(this.spMetamodel);
      this.btnCancel.setActionCommand(AC_CANCEL);
      this.btnCancel.addActionListener(this);
      this.btnCancel.setText("Cancel");
      this.btnEvaluate.setActionCommand(AC_EVALUATE);
      this.btnEvaluate.addActionListener(this);
      this.btnEvaluate.setText("Evaluate");
      this.getContentPane().setLayout(this.gridBagLayout2);
      this.jPanel1.setLayout(this.gridBagLayout1);
      this.getContentPane().add(this.jPanel1, new GridBagConstraints(0, 0, 1, 2, 1.0D, 1.0D, 10, 1, new Insets(8, 9, 24, 0), 0, -28));
      this.jPanel1.add(this.jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(16, 6, 0, 0), 157, -1));
      this.jPanel1.add(this.jLabel2, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(16, 4, 0, 10), 128, -1));
      this.jPanel1.add(this.spUserModel, new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(8, 6, 11, 6), 142, -44));
      this.jPanel1.add(this.spMetamodel, new GridBagConstraints(1, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(8, 0, 11, 10), 144, -44));
      this.getContentPane().add(this.btnCancel, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 11, 0, new Insets(15, 14, 10, 37), 71, 0));
      this.getContentPane().add(this.btnEvaluate, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 15, 0, new Insets(50, 14, 0, 37), 63, 0));
      this.setSize(new Dimension(3 * Utils.screenSize.width / 4, Utils.screenSize.height / 2));
      this.setLocation(Utils.screenSize.width / 8, Utils.screenSize.height / 8);
   }

   private static void updateTree(JTree tree) {
      TreeSelectionListener tsl = new MyTreeSelectionListener(tree);
      tree.addTreeSelectionListener(tsl);
      tree.getSelectionModel().setSelectionMode(4);
      tree.setShowsRootHandles(true);
      tree.setCellRenderer(new SelectionTreeCellRenderer());
   }

   private class DirectFocusTraversalPolicy extends ContainerOrderFocusTraversalPolicy {
      private DirectFocusTraversalPolicy() {
      }

      public Component getInitialComponent(Window w) {
         return CustomizationDialog.this.userModelTree;
      }

      public Component getComponentAfter(Container cont, Component c) {
         if (CustomizationDialog.this.userModelTree.equals(c)) {
            return CustomizationDialog.this.referencedModelsTree;
         } else if (CustomizationDialog.this.referencedModelsTree.equals(c)) {
            return CustomizationDialog.this.btnEvaluate;
         } else {
            Component component;
            if (CustomizationDialog.this.btnEvaluate.equals(c)) {
               component = CustomizationDialog.this.btnCancel;
            } else {
               component = CustomizationDialog.this.referencedModelsTree;
            }
            return component;

         }
      }
   }
}
