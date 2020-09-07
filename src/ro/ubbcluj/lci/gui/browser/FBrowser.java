package ro.ubbcluj.lci.gui.browser;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ro.ubbcluj.lci.gui.Actions.AFileActions;
import ro.ubbcluj.lci.gui.mainframe.GAbstractProject;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GProjectView;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.xmi.utils.XMIUtilities;

public class FBrowser extends GProjectView {
   public static String UML_NODE_NAME = "Models";
   public static String OCL_NODE_NAME = "Constraints";
   protected JTree tree;
   protected DefaultTreeModel treeModel;
   protected int newDirCount = 0;
   private static final String NEW_DIR_NAME = "NewDir";
   private FTreeNode constraintsNode;
   private FTreeNode modelsNode;

   public FBrowser(GAbstractProject prj) {
      this.setUserObject(prj);
      File projectFile = this.getProject().getProjectFile();
      if (projectFile == null) {
         FTreeNode fNode;
         this.treeModel = new DefaultTreeModel(fNode = new FTreeNode(this.getProject().getProjectName()));
         fNode.add(this.constraintsNode = new FTreeNode(OCL_NODE_NAME));
         fNode.add(this.modelsNode = new FTreeNode(UML_NODE_NAME));
      } else {
         try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(projectFile);
            Node node = doc.getChildNodes().item(0);
            String name = node.getAttributes().getNamedItem("name").getNodeValue();
            FTreeNode root = new FTreeNode(name);
            this.treeModel = new DefaultTreeModel(root);
            this.createTree(root, node);
            this.modelsNode = null;
            this.constraintsNode = null;

            for(int i = 0; i < root.getChildCount(); ++i) {
               FTreeNode fNode = (FTreeNode)root.getChildAt(i);
               Object userObject = fNode.getUserObject();
               if (userObject instanceof String) {
                  if (UML_NODE_NAME.equals(userObject) && this.modelsNode == null) {
                     this.modelsNode = fNode;
                  } else if (OCL_NODE_NAME.equals(userObject) && this.constraintsNode == null) {
                     this.constraintsNode = fNode;
                  }
               }
            }
         } catch (Exception var11) {
            var11.printStackTrace();
         }
      }

      this.tree = new JTree(this.treeModel);
      ToolTipManager.sharedInstance().registerComponent(this.tree);
      JPopupMenu popup = new FPopupMenu(this);
      this.tree.add(popup);
      DefaultTreeCellRenderer renderer = new FBrowser.FCellRenderer();
      this.tree.setCellRenderer(renderer);
      this.tree.setCellEditor(new FBrowser.FCellEditor(this.tree, renderer));
      this.tree.setEditable(true);
      this.tree.setSelectionPath(new TreePath(this.getRoot()));
      this.tree.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent evt) {
            if (evt.getClickCount() == 2) {
               FTreeNode node = (FTreeNode)FBrowser.this.tree.getSelectionPath().getLastPathComponent();
               if (node.getKind() == 1) {
                  ((AFileActions.OpenFileAction)AFileActions.openFileAction).openFile((File)node.getUserObject());
               }
            }

         }
      });
   }

   public void writeXml() {
      StreamResult r = null;

      try {
         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = builder.newDocument();
         Element elem = doc.createElement("Project");
         elem.setAttribute("name", this.getRoot().toString());
         doc.appendChild(elem);
         this.createDOM(doc, elem, this.getRoot());
         Transformer transf = TransformerFactory.newInstance().newTransformer();
         DOMSource s = new DOMSource(doc);
         if (this.getProject().getProjectFile() == null) {
            r = new StreamResult(new File("untitled.oepr"));
         } else {
            r = new StreamResult(this.getProject().getProjectFile());
         }

         transf.setOutputProperty("method", "xml");
         transf.setOutputProperty("version", "1.0");
         transf.setOutputProperty("indent", "yes");
         transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
         transf.transform(s, r);
      } catch (Exception var16) {
         var16.printStackTrace();
      } finally {
         if (r != null && r.getOutputStream() != null) {
            try {
               r.getOutputStream().close();
            } catch (IOException var15) {
               System.err.println("Could not close project file!");
            }
         }

      }

   }

   private void createTree(FTreeNode tNode, Node parent) {
      String baseDir = this.getProject().getProjectFile().getParentFile().getAbsolutePath();

      for(int i = 0; i < parent.getChildNodes().getLength(); ++i) {
         Node current = parent.getChildNodes().item(i);
         if (current.getAttributes() != null) {
            String sKind = current.getAttributes().getNamedItem("kind").getNodeValue();
            int kind = Integer.parseInt(sKind);
            String absolutePath;
            if (kind == 0) {
               absolutePath = current.getAttributes().getNamedItem("name").getNodeValue();
               this.createTree(this.addNewNode(tNode, absolutePath, kind), current);
            } else {
               absolutePath = current.getAttributes().getNamedItem("AbsolutePath").getNodeValue();
               Node relative = current.getAttributes().getNamedItem("RelativePath");
               File file1 = new File(absolutePath);
               String relativePath = relative != null ? relative.getNodeValue() : null;
               String absFromRelPath = relativePath != null ? XMIUtilities.getAbsoluteFromRelativePath(baseDir, relativePath) : null;
               File file2 = absFromRelPath != null ? new File(absFromRelPath) : null;
               File file = null;
               if (file2.exists()) {
                  file = file2;
               } else if (file1.exists()) {
                  file = file1;
               }

               if (file != null) {
                  FTreeNode tmp = this.addNewNode(tNode, file, kind);
                  Node temp;
                  boolean selected;
                  if (kind == 1) {
                     temp = current.getAttributes().getNamedItem("selected");
                     selected = temp != null && Boolean.valueOf(temp.getNodeValue()).booleanValue();
                     if (selected) {
                        this.getProject().getSelectedConstraints().add(file.getAbsolutePath());
                     }
                  }

                  if (kind == 2 || kind == 4) {
                     temp = current.getAttributes().getNamedItem("selected");
                     selected = temp != null && Boolean.valueOf(temp.getNodeValue()).booleanValue();
                     if (selected) {
                        GAbstractProject project = this.getProject();
                        project.setActiveModelFile(file.getAbsolutePath());
                     }

                     this.createTree(tmp, current);
                     if (kind == 4) {
                        try {
                           ZipFile zf = new ZipFile(file);
                           Enumeration zipEnum = zf.entries();

                           while(zipEnum.hasMoreElements()) {
                              ZipEntry ze = (ZipEntry)zipEnum.nextElement();
                              this.addNewNode(tmp, new File(ze.getName()), 5);
                           }

                           zf.close();
                        } catch (IOException var21) {
                           System.err.println("Invalid zip file!");
                        }
                     }
                  }
               } else {
                  GMainFrame.getMainFrame().updateMessages((Object)("File " + absolutePath + " not found."));
               }
            }
         }
      }

   }

   private void createDOM(Document doc, Node parent, FTreeNode tNode) {
      File parentDir = this.getProject().getProjectFile().getParentFile();

      for(int i = 0; i < tNode.getChildCount(); ++i) {
         FTreeNode current = (FTreeNode)tNode.getChildAt(i);
         Element newElement = null;
         if (current.getKind() != 5) {
            if (current.isDirectory()) {
               newElement = doc.createElement("Directory");
               newElement.setAttribute("name", current.toString());
               newElement.setAttribute("kind", String.valueOf(current.getKind()));
               this.createDOM(doc, newElement, current);
            } else {
               File file = (File)current.getUserObject();
               String absolutePath = XMIUtilities.toURI(file);
               String relativePath = XMIUtilities.getPathRelativeTo(file, parentDir);
               if (absolutePath != null) {
                  newElement = doc.createElement(!current.isModelFile() && !current.isZippedModelFile() ? "File" : "Model");
                  newElement.setAttribute("kind", String.valueOf(current.getKind()));
                  newElement.setAttribute("AbsolutePath", absolutePath);
                  if (relativePath != null) {
                     newElement.setAttribute("RelativePath", relativePath);
                  }

                  String selected;
                  if (!current.isModelFile() && !current.isZippedModelFile()) {
                     if (current.isConstraintFile()) {
                        selected = (new Boolean(this.getProject().getSelectedConstraints().contains(file.getAbsolutePath()))).toString();
                        newElement.setAttribute("selected", selected);
                     }
                  } else {
                     selected = (new Boolean(this.getProject().getActiveModelFile() != null ? this.getProject().getActiveModelFile().equals(file.getAbsolutePath()) : false)).toString();
                     newElement.setAttribute("selected", selected);
                     this.createDOM(doc, newElement, current);
                  }
               }
            }

            if (newElement != null) {
               parent.appendChild(newElement);
            }
         }
      }

   }

   public FTreeNode addNewNode(FTreeNode parent, Object child, int kind) {
      FTreeNode var4;
      if (child instanceof String) {
         var4 = new FTreeNode((String)child);
      } else {
         if (!(child instanceof File)) {
            return null;
         }

         var4 = new FTreeNode((File)child, kind);
      }

      String name = var4.isDirectory() ? " " + var4.toString() : var4.toString();
      if (parent.getChildCount() == 0) {
         parent.add(var4);
         this.treeModel.nodesWereInserted(parent, new int[]{0});
         return var4;
      } else {
         for(int i = 0; i < parent.getChildCount(); ++i) {
            FTreeNode currentChild = (FTreeNode)parent.getChildAt(i);
            String cName = currentChild.isDirectory() ? " " + currentChild.toString() : currentChild.toString();
            if (name.compareToIgnoreCase(cName) < 0) {
               parent.insert(var4, i);
               this.treeModel.nodesWereInserted(parent, new int[]{i});
               return var4;
            }
         }

         parent.add(var4);
         this.treeModel.nodesWereInserted(parent, new int[]{parent.getChildCount() - 1});
         return var4;
      }
   }

   public void addModel(String fileName, boolean zipped) {
      FTreeNode currentDir = this.getCurrentDirectory();
      FTreeNode node = null;
      if (currentDir != null && currentDir.getPath().length >= 3) {
         node = this.addNewNode(currentDir, new File(fileName), zipped ? 4 : 2);
      } else {
         node = this.addNewNode(this.modelsNode, new File(fileName), zipped ? 4 : 2);
      }

      if (zipped) {
         this.refreshZippedChildren(node);
      }

   }

   public void addConstraint(String fileName) {
      FTreeNode node = this.getCurrentDirectory();
      if (node.getPath().length < 3) {
         this.addNewNode(this.constraintsNode, new File(fileName), 1);
      } else {
         this.addNewNode(node, new File(fileName), 1);
      }

   }

   public void removeFile(String fileName) {
      Enumeration nodes = this.getRoot().breadthFirstEnumeration();

      FTreeNode node;
      FTreeNode pNode;
      int rmIdx;
      do {
         Object tmp;
         do {
            if (!nodes.hasMoreElements()) {
               return;
            }

            node = (FTreeNode)nodes.nextElement();
            tmp = node.getUserObject();
         } while((!(tmp instanceof File) || !((File)tmp).getPath().equals(fileName)) && (!node.isDirectory() || !fileName.equals((String)node.getUserObject())));

         pNode = (FTreeNode)node.getParent();
         rmIdx = -1;

         for(int i = 0; i < pNode.getChildCount(); ++i) {
            if (pNode.getChildAt(i) == node) {
               rmIdx = i;
            }
         }
      } while(rmIdx == -1);

      pNode.remove(rmIdx);
      this.treeModel.nodesWereRemoved(pNode, new int[]{rmIdx}, new Object[]{node});
   }

   public void newDirectory() {
      FTreeNode cd = this.getCurrentDirectory();
      this.addNewNode(cd, "NewDir" + ++this.newDirCount, 0);
   }

   public void removeNode(FTreeNode node) {
      FTreeNode parent = (FTreeNode)node.getParent();
      if (parent != null) {
         int idx = parent.getIndex(node);
         parent.remove(node);
         this.treeModel.nodesWereRemoved(parent, new int[]{idx}, new Object[]{node});
      }

   }

   /** @deprecated */
   public void removeObject(Object obj) {
      FTreeNode node = this.getNodeForObject(obj);
      if (node != null) {
         this.removeNode(node);
      }

   }

   public FTreeNode getNodeForObject(Object obj) {
      FTreeNode root = (FTreeNode)this.treeModel.getRoot();
      Enumeration enum = root.breadthFirstEnumeration();

      FTreeNode node;
      do {
         if (!enum.hasMoreElements()) {
            return null;
         }

         node = (FTreeNode)enum.nextElement();
      } while(!node.getUserObject().equals(obj));

      return node;
   }

   public FTreeNode getFileNode(String fullName) {
      FTreeNode root = (FTreeNode)this.treeModel.getRoot();
      Enumeration enum = root.breadthFirstEnumeration();

      FTreeNode node;
      do {
         if (!enum.hasMoreElements()) {
            return null;
         }

         node = (FTreeNode)enum.nextElement();
      } while(!node.isFile() || !((File)node.getUserObject()).getPath().equals(fullName));

      return node;
   }

   public FTreeNode getSelectedNode() {
      TreePath treePath = this.tree.getSelectionPath();
      return treePath == null ? null : (FTreeNode)treePath.getLastPathComponent();
   }

   public FTreeNode getRoot() {
      return (FTreeNode)this.treeModel.getRoot();
   }

   public FTreeNode getDirectoryForDiagrams() {
      FTreeNode root = this.getRoot();

      for(int i = 0; i < root.getChildCount(); ++i) {
         FTreeNode tn = (FTreeNode)root.getChildAt(i);
         if (tn.isDirectory() && "Diagrams".equals(tn.getUserObject())) {
            return tn;
         }
      }

      return null;
   }

   public File[] getDiagramFiles(String modelFile) {
      ArrayList result = new ArrayList();
      FTreeNode model = this.getFileNode(modelFile);
      if (model == null) {
         return new File[0];
      } else {
         for(int i = 0; i < model.getChildCount(); ++i) {
            result.add(((FTreeNode)model.getChildAt(i)).getUserObject());
         }

         return (File[])result.toArray(new File[0]);
      }
   }

   public ArrayList getAllModels() {
      ArrayList zippedModels = this.getNamesOf(this.getFilesByKind(4));
      ArrayList xmlModels = this.getNamesOf(this.getFilesByKind(2));
      zippedModels.addAll(xmlModels);
      return zippedModels;
   }

   public ArrayList getAllConstraints() {
      return this.getNamesOf(this.getFilesByKind(1));
   }

   public ArrayList getAllConstraintFiles() {
      return this.getFilesByKind(1);
   }

   private ArrayList getFilesByKind(int kind) {
      ArrayList result = new ArrayList();
      FTreeNode root = (FTreeNode)this.treeModel.getRoot();
      Enumeration enum = root.breadthFirstEnumeration();

      while(enum.hasMoreElements()) {
         FTreeNode node = (FTreeNode)enum.nextElement();
         if (node.getKind() == kind) {
            result.add(node.getUserObject());
         }
      }

      return result;
   }

   private ArrayList getNamesOf(ArrayList files) {
      ArrayList result = new ArrayList();
      Iterator it = files.iterator();

      while(it.hasNext()) {
         Object tmp = it.next();
         if (tmp instanceof File) {
            result.add(((File)tmp).getPath());
         } else {
            result.add(tmp);
         }
      }

      return result;
   }

   public String[] getFilesForExtension(String ext) {
      ArrayList result = new ArrayList();
      FTreeNode root = (FTreeNode)this.treeModel.getRoot();
      Enumeration enum = root.breadthFirstEnumeration();

      while(enum.hasMoreElements()) {
         FTreeNode node = (FTreeNode)enum.nextElement();
         if (node.isFile() && ((File)node.getUserObject()).getPath().endsWith(ext)) {
            result.add(((File)node.getUserObject()).getPath());
         }
      }

      return (String[])result.toArray(new String[0]);
   }

   public ArrayList getAllFiles() {
      ArrayList result = new ArrayList();
      FTreeNode root = (FTreeNode)this.treeModel.getRoot();
      Enumeration enum = root.breadthFirstEnumeration();

      while(enum.hasMoreElements()) {
         FTreeNode node = (FTreeNode)enum.nextElement();
         if (node.isFile()) {
            result.add(((File)node.getUserObject()).getAbsolutePath());
         }
      }

      return result;
   }

   public FTreeNode getCurrentDirectory() {
      FTreeNode selected = this.getSelectedNode();
      if (selected == null) {
         return null;
      } else {
         return selected.isDirectory() ? selected : (FTreeNode)selected.getParent();
      }
   }

   public void projectNameChanged() {
      this.getRoot().setUserObject(this.getProject().getProjectName());
      this.treeModel.nodeChanged(this.getRoot());
   }

   public JComponent getComponent() {
      return this.tree;
   }

   public void addSelectionListener(TreeSelectionListener l) {
      this.tree.addTreeSelectionListener(l);
   }

   public void removeSelectionListener(TreeSelectionListener l) {
      this.tree.removeTreeSelectionListener(l);
   }

   public FTreeNode getModelsNode() {
      return this.modelsNode;
   }

   public FTreeNode getConstraintsNode() {
      return this.constraintsNode;
   }

   public void refreshZippedChildren(FTreeNode node) {
      List list = new ArrayList();

      int i;
      for(i = 0; i < node.getChildCount(); ++i) {
         list.add(node.getChildAt(i));
      }

      for(i = 0; i < list.size(); ++i) {
         this.removeNode((FTreeNode)list.get(i));
      }

      String fileName = ((File)node.getUserObject()).getPath();

      try {
         ZipFile zf = new ZipFile(fileName);
         Enumeration zipEnum = zf.entries();

         while(zipEnum.hasMoreElements()) {
            ZipEntry ze = (ZipEntry)zipEnum.nextElement();
            this.addNewNode(node, new File(ze.getName()), 5);
         }

         zf.close();
      } catch (IOException var7) {
         System.err.println("Invalid zip file!");
      }

   }

   class FCellEditor extends DefaultTreeCellEditor {
      public FCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
         super(tree, renderer);
      }

      public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
         return ((FTreeNode)value).getUserObject() instanceof String ? super.getTreeCellEditorComponent(tree, value, false, expanded, false, row) : null;
      }

      public boolean isCellEditable(EventObject evt) {
         if (evt instanceof MouseEvent) {
            MouseEvent mEvt = (MouseEvent)evt;
            if (mEvt.getClickCount() == 2) {
               JTree tree = (JTree)mEvt.getSource();
               FTreeNode node = (FTreeNode)tree.getPathForLocation(mEvt.getX(), mEvt.getY()).getLastPathComponent();
               if (node.getParent() != null && node.getParent().getParent() == null) {
                  return false;
               }

               if (node != null && node.getUserObject() instanceof String) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   class FCellRenderer extends DefaultTreeCellRenderer {
      public FCellRenderer() {
       //  this.setOpenIcon(new ImageIcon(this.getClass().getResource("/resources/dir0.gif")));
      //   this.setClosedIcon(new ImageIcon(this.getClass().getResource("/resources/dir1.gif")));
      }

      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasfocus) {
         super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasfocus);
         FTreeNode node = (FTreeNode)value;
         Object userObject = node.getUserObject();

         try {
            this.setToolTipText(userObject instanceof File ? ((File)userObject).getCanonicalPath() : userObject.toString());
            if (node == tree.getModel().getRoot()) {
               GAbstractProject gp = GRepository.getInstance().getProject();
               File projectFile = gp != null ? gp.getProjectFile() : null;
               if (projectFile != null) {
                  this.setToolTipText(projectFile.getAbsolutePath());
               }
            }
         } catch (IOException var14) {
         }

         URL url = null;
         URL durl = null;
         if (node.isDirectory()) {
            if (expanded) {
               url = this.getClass().getResource("/resources/dir0.gif");
            } else {
               url = this.getClass().getResource("/resources/dir1.gif");
            }
         } else if (!node.isModelFile() && !node.isZippedModelFile()) {
            if (node.isConstraintFile()) {
               url = this.getClass().getResource("/resources/ocl1.gif");
               durl = this.getClass().getResource("/resources/ocl0.gif");
               if (FBrowser.this.getProject().getSelectedConstraints().contains(((File)node.getUserObject()).getAbsolutePath())) {
                  this.setEnabled(true);
               } else {
                  this.setEnabled(false);
               }
            } else {
               String fNamex;
               FTreeNode parent;
               if (node.isDiagramFile()) {
                  parent = (FTreeNode)node.getParent();
                  fNamex = ((File)parent.getUserObject()).getPath();
                  if (fNamex.equals(FBrowser.this.getProject().getActiveModelFile())) {
                     this.setEnabled(true);
                  } else {
                     this.setEnabled(false);
                  }
               } else if (node.isZipEntry()) {
                  url = this.getClass().getResource("/resources/entry.gif");
                  durl = this.getClass().getResource("/resources/xml0.gif");
                  parent = (FTreeNode)node.getParent();
                  fNamex = ((File)parent.getUserObject()).getPath();
                  if (fNamex.equals(FBrowser.this.getProject().getActiveModelFile())) {
                     this.setEnabled(true);
                  } else {
                     this.setEnabled(false);
                  }
               }
            }
         } else {
            String fName = ((File)node.getUserObject()).getPath();
            url = node.isModelFile() ? this.getClass().getResource("/resources/xml1.gif") : this.getClass().getResource("/resources/zip1.gif");
            durl = this.getClass().getResource("/resources/xml0.gif");
            if (fName.equals(FBrowser.this.getProject().getActiveModelFile())) {
               this.setEnabled(true);
            } else {
               this.setEnabled(false);
            }
         }

         if (url == null) {
            url = this.getClass().getResource("/images/default_file_image.gif");
         }

         this.setIcon(new ImageIcon(url));
         if (durl != null) {
            this.setDisabledIcon(new ImageIcon(durl));
         }

         return this;
      }
   }
}
