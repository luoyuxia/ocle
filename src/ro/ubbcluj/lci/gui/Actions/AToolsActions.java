package ro.ubbcluj.lci.gui.Actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import ro.ubbcluj.lci.gui.browser.GTree;
import ro.ubbcluj.lci.gui.browser.BTree.BTreeNode;
import ro.ubbcluj.lci.gui.editor.Editor;
import ro.ubbcluj.lci.gui.editor.TextDocumentModel;
import ro.ubbcluj.lci.gui.editor.TextDocumentPad;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.gui.mainframe.GAbstractProject;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.gui.tools.selectionmenu.MenuSelectionEvent;
import ro.ubbcluj.lci.gui.tools.selectionmenu.MenuSelectionListener;
import ro.ubbcluj.lci.gui.tools.selectionmenu.SelectionMenu;
import ro.ubbcluj.lci.ocl.ExceptionAny;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUserElement;
import ro.ubbcluj.lci.ocl.OclUserModel;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.STDChecker;
import ro.ubbcluj.lci.ocl.eval.OclConstant;
import ro.ubbcluj.lci.ocl.nodes.classifierContext;
import ro.ubbcluj.lci.ocl.nodes.formalParameterList;
import ro.ubbcluj.lci.ocl.nodes.name;
import ro.ubbcluj.lci.ocl.nodes.operationContext;
import ro.ubbcluj.lci.ocl.nodes.returnType;
import ro.ubbcluj.lci.ocl.nodes.simpleTypeSpecifier;
import ro.ubbcluj.lci.ocl.nodes.typeSpecifier;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class AToolsActions {
   public static Action compileAction = new AToolsActions.CompileAction();
   public static Action compileFileAction = new AToolsActions.CompileFileAction();
   public static Action evaluateSelAction = new AToolsActions.EvaluateSelAction();
   public static Action checkAction = new AToolsActions.CheckAction();
   public static Action partialCheckAction = new AToolsActions.PartialCheckAction();
   public static Action stdCheckAction = new AToolsActions.STDCheckAction();
   public static Action codeGenAction = new AToolsActions.CodeGenAction();
   private static Action[] actions;

   public AToolsActions() {
   }

   public static Action[] getActions() {
      return actions;
   }

   static {
      actions = new Action[]{compileAction, compileFileAction, evaluateSelAction, checkAction, partialCheckAction, stdCheckAction, codeGenAction};
   }

   private static class CodeGenAction extends AbstractAction {
      private CodeGenerationController controller = null;

      public CodeGenAction() {
         this.putValue("ActionCommandKey", "menubar.tools.codegen");
      }

      public void actionPerformed(ActionEvent evt) {
         if (this.controller == null) {
            this.controller = new CodeGenerationController();
         }

         this.controller.startCodeGeneration();
      }
   }

   public static class EvaluateSelAction extends AbstractAction {
      private final SelectionMenu selectionMenu;

      private EvaluateSelAction() {
         this.selectionMenu = new SelectionMenu();
         this.putValue("ActionCommandKey", "menubar.tools.evaluatesel");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(84, 2));
         this.selectionMenu.setRenderer(new OclUserElementRenderer());
         this.selectionMenu.setComparator(new TypeBasedOrderComparator());
      }

      public void actionPerformed(ActionEvent e) {
         this.evaluateSelection((MouseEvent)null);
      }

      private static void setContextsInAST(OclNode nod, HashMap contextBindings) {
         if (nod != null) {
            if (nod instanceof classifierContext && nod.type != null && nod.type.type != null) {
               Object value = contextBindings.get(nod.type.type);
               if (value != null) {
                  ((OclConstant)nod.evnode).setValue(value);
               }
            }

            Iterator it = nod.children.iterator();

            while(it.hasNext()) {
               setContextsInAST((OclNode)it.next(), contextBindings);
            }

         }
      }

      private static OclNode findBestFit(OclNode parent, int left, int right, String filename) {
         OclNode ret = null;
         Stack stack = new Stack();
         stack.push(parent);
         long dif = 9223372036854775807L;

         while(true) {
            OclNode node;
            do {
               if (stack.isEmpty()) {
                  return ret;
               }

               node = (OclNode)stack.pop();
            } while(node == null);

            int nleft = node.getStart();
            int nright = node.getStop() + 1;
            if ((long)(nright - nleft) <= dif && nleft <= left && nright >= right && node.type != null && node.getFilename().equals(filename)) {
               ret = node;
               dif = (long)(nright - nleft);
            }

            Iterator it = node.children.iterator();

            while(it.hasNext()) {
               stack.push(it.next());
            }
         }
      }

      public void evaluateSelection(MouseEvent evt) {
         if (this.isEnabled()) {
            GRepository repInstance = GRepository.getInstance();
            GMainFrame mainframe = GMainFrame.getMainFrame();
            TextDocumentPad activeView = (TextDocumentPad)mainframe.getViewContainer().getActivePad();
            List instances = null;
            TextDocumentModel activeModel = activeView.getModel();
            String filename = activeModel.getFileName();
            JEditTextArea realActiveView = activeView.getView();
            CompilationInfo ci = repInstance.getInfoForFile(filename);
            setContextsInAST(ci.getRoot(), ci.getContextBindings());
            OclNode bestFit = findBestFit(ci.getRoot(), realActiveView.getSelectionStart(), realActiveView.getSelectionEnd(), filename);
            if (bestFit == null) {
               mainframe.updateOCLLog("Selection cannot be evaluated.\n");
            } else {
               realActiveView.select(bestFit.getStart(), bestFit.getStop() + 1);
               OclNode contextNode = null;
               boolean isContext = false;
               if (bestFit instanceof classifierContext) {
                  contextNode = bestFit;
                  isContext = true;
               }

               if (bestFit instanceof name && bestFit.parent instanceof formalParameterList) {
                  int index = bestFit.getParent().indexOfChild(bestFit);
                  OclNode pTypeNode = bestFit.getParent().getChild(index + 2);
                  if (!pTypeNode.type.isPrimitiveType()) {
                     contextNode = bestFit;
                  }
               }

               if (bestFit instanceof simpleTypeSpecifier && bestFit.parent instanceof typeSpecifier && bestFit.parent.parent instanceof returnType && !bestFit.parent.parent.type.isPrimitiveType()) {
                  contextNode = bestFit.parent.parent;
               }

               if (bestFit instanceof name && bestFit.parent instanceof operationContext) {
                  contextNode = bestFit;
                  isContext = true;
               }

               if (contextNode != null) {
                  if (evt != null) {
                     OclUserModel userModel = new OclUserModel(ci.getTargetModel());

                     try {
                        if (ci.getMode() == 1) {
                           instances = OclUserModel.getObjectInstances(contextNode.type.type.classifier, false);
                        } else {
                           instances = userModel.getInstances(contextNode.type.type.getJavaClass(), false, false);
                        }
                     } catch (ExceptionAny var18) {
                        mainframe.updateOCLLog("Exception while filling instance list: " + var18.getMessage());
                     }

                     this.invokeSelectionMenu(contextNode, evt.getX(), evt.getY(), realActiveView, ci, instances, isContext);
                  }
               } else if (bestFit.type != null) {
                  String beginText = "Selection: " + bestFit.type.getFullName() + "=";

                  try {
                     if (bestFit.evnode == null) {
                        mainframe.updateOCLLog(beginText + "No evaluation information attached to current selection.");
                     } else {
                        Object obj = bestFit.evnode.evaluate();
                        ArrayList lstTemp = OclUtil.getCollection(obj);
                        ArrayList displayList = new ArrayList();
                        displayList.add(beginText);
                        displayList.addAll(lstTemp);
                        mainframe.updateOCLLog((Collection)displayList);
                        lstTemp = null;
                        displayList = null;
                     }
                  } catch (Exception var17) {
                     mainframe.updateOCLLog(beginText + var17.getMessage());
                  }
               }

            }
         }
      }

      private void invokeSelectionMenu(final OclNode node, int x, int y, JEditTextArea ta, final CompilationInfo ci, List instances, final boolean isContext) {
         String title = " Instances for " + UMLUtilities.getFullyQualifiedName((ModelElement)node.type.type.classifier);
         this.selectionMenu.addAll(instances);
         title = title + "(found " + instances.size() + ")";
         this.selectionMenu.setTitle(title);
         this.selectionMenu.invoke(ta, x, y);
         this.selectionMenu.addMenuSelectionListener(new MenuSelectionListener() {
            public void valuePicked(MenuSelectionEvent evt) {
               OclUserElement obj = (OclUserElement)evt.getSelectedValue();
               Object o = obj.getElement();
               if (isContext) {
                  HashMap hm = ci.getContextBindings();
                  hm.put(node.type.type, o);
               } else {
                  ((OclConstant)node.evnode).value = o;
               }

               String temp = node.type.getFullName() + " is now set to:";
               String beginText = isContext ? "Context for " + temp : "Value of " + temp;
               ArrayList lstTemp = OclUtil.getCollection(obj.getElement());
               ArrayList displayList = new ArrayList();
               displayList.add(beginText);
               displayList.addAll(lstTemp);
               GMainFrame.getMainFrame().updateOCLLog((Collection)displayList);
               EvaluateSelAction.this.selectionMenu.setVisible(false);
            }
         });
      }
   }

   private static class CompileFileAction extends AbstractAction {
      CompileFileAction() {
         this.putValue("ActionCommandKey", "menubar.tools.compilefile");
      }

      public void actionPerformed(ActionEvent e) {
         Editor ed = GApplication.getApplication().getEditor();
         GMainFrame mainframe = GMainFrame.getMainFrame();
         GRepository r = GRepository.getInstance();
         AFileFilter aff = new AFileFilter("ocl", "OCL text files (*.ocl)");
         AFileFilter aff2 = new AFileFilter("bcr", "OCL text files (*.bcr)");
         ed.addFileFilter(aff);
         ed.addFileFilter(aff2);
         int completionFlag = ed.saveActiveFile();
         if (completionFlag == -107) {
            mainframe.updateLog("Operation cancelled by user. File not compiled\n");
         } else if (completionFlag == -105) {
            mainframe.updateLog("File could not be saved. It will not be compiled\n");
         }

         ed.removeFileFilter(aff);
         ed.removeFileFilter(aff2);
         if (completionFlag == -104 || completionFlag == -106) {
            ArrayList errors = new ArrayList();
            TextDocumentPad activePad = (TextDocumentPad)ed.getActivePad();
            Model userModel = r.getUsermodel() != null ? r.getUsermodel().getModel() : null;
            String fileName;
            int type = OclUtil.getOclFileType(fileName = activePad.getModel().getFileName());
            int mode = type == 2 ? 1 : 0;
            mainframe.clearErrorMessages();
            String completion = "successfully completed";
            mainframe.setCursor(new Cursor(3));
            boolean compiled = false;

            try {
               if (type != 2 && type != 3) {
                  completion = null;
                  mainframe.updateLog("File type for " + fileName + " is not recognized. File not compiled\n");
               } else {
                  mainframe.updateLog("Compiling...");
                  CompilerInvoker.invokeCompilerOnFile(mode, fileName, errors);
               }

               int errCount = errors.size();
               if (errCount > 0) {
                  mainframe.updateMessages((List)errors);
                  completion = errCount + " compilation errors. Please check the 'Messages' tab.";
               } else {
                  compiled = true;
               }
            } catch (Exception var23) {
               completion = "unexpected error: " + var23.getMessage();
               var23.printStackTrace();
            } finally {
               if (completion != null) {
                  mainframe.updateLog(completion + '\n');
               }

               CompilationInfo ci = new CompilationInfo(compiled);
               ci.setTargetModel(userModel);
               ci.setMode(mode);
               ci.setRoot(r.getCompiler().getRoot());
               if (ci.getRoot() != null) {
                  ci.markAffected(new String[]{fileName});
               }

               r.setLastCompilationInfos(new CompilationInfo[]{ci});
               mainframe.updateEvaluationActions();
               mainframe.setCursor(new Cursor(0));
            }

         }
      }
   }

   private static class CompileAction extends AbstractAction {
      CompileAction() {
         this.putValue("ActionCommandKey", "menubar.tools.compile");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(120, 2));
      }

      public void actionPerformed(ActionEvent e) {
         if (this.isEnabled()) {
            Editor ed = GApplication.getApplication().getEditor();
            GMainFrame mainframe = GMainFrame.getMainFrame();
            GRepository r = GRepository.getInstance();
            Model userModel = r.getUsermodel() != null ? r.getUsermodel().getModel() : null;
            AFileFilter aff = new AFileFilter("ocl", "OCL text files (*.ocl)");
            ed.addFileFilter(aff);
            AFileFilter aff2 = new AFileFilter("bcr", "OCL text files (*.bcr)");
            ed.addFileFilter(aff2);
            int code = ed.saveAll();
            ed.removeFileFilter(aff);
            ed.removeFileFilter(aff2);
            if (code != -108) {
               if (code == -107) {
                  mainframe.updateLog("Operation cancelled by user. No files were compiled\n");
               } else {
                  mainframe.updateLog("Problems while writing files. Operation interrupted\n");
               }

            } else {
               mainframe.setCursor(new Cursor(3));
               GAbstractProject prj = r.getProject();
               List lst = prj.getSelectedConstraints();
               List bcrFileList = new ArrayList();
               List wfrFileList = new ArrayList();
               int size = lst.size();

               for(int i = 0; i < size; ++i) {
                  String file = (String)lst.get(i);
                  int type = OclUtil.getOclFileType(file);
                  if (type == 3) {
                     wfrFileList.add(file);
                  } else if (type == 2) {
                     bcrFileList.add(file);
                  } else {
                     mainframe.updateLog("File type for " + file + " is not recognized. File will not be compiled\n");
                  }
               }

               String[] bcrFiles = (String[])bcrFileList.toArray(new String[0]);
               String[] wfrFiles = (String[])wfrFileList.toArray(new String[0]);
               ArrayList errors = new ArrayList();
               int errCount = 0;
               CompilationInfo ci = null;
               Collection lci = new ArrayList();
               mainframe.clearErrorMessages();
               mainframe.clearEvaluationErrors();
               String completion = "successfully completed";

               try {
                  if (wfrFiles.length != 0) {
                     mainframe.updateLog("Compiling...");
                     CompilerInvoker.invokeCompilerOnFiles(0, wfrFiles, errors);
                     errCount = errors.size();
                     ci = new CompilationInfo(errCount == 0);
                     ci.setMode(0);
                     ci.setTargetModel(userModel);
                     ci.setRoot(r.getCompiler().getRoot());
                     ci.markAffected(wfrFiles);
                     lci.add(ci);
                     prj.setWfrInfo(ci);
                  } else {
                     // mainframe.updateMessages((Object)"No specifications found at the metamodel level.");
                     prj.setWfrInfo(new CompilationInfo(false));
                  }

                  ci = null;
                  if (bcrFiles.length != 0) {
                     if (wfrFiles.length == 0) {
                        mainframe.updateLog("Compiling...");
                     }

                     CompilerInvoker.invokeCompilerOnFiles(1, bcrFiles, errors);
                     ci = new CompilationInfo(errors.size() == errCount);
                     ci.setMode(1);
                     ci.setTargetModel(userModel);
                     ci.setRoot(r.getCompiler().getRoot());
                     ci.markAffected(bcrFiles);
                     lci.add(ci);
                     prj.setBcrInfo(ci);
                  } else {
                     mainframe.updateMessages((Object)"No specifications found at the model level.");
                     prj.setBcrInfo(new CompilationInfo(false));
                  }

                  if (wfrFiles.length + bcrFiles.length <= 0) {
                     completion = null;
                  } else {
                     errCount = errors.size();
                     if (errCount > 0) {
                        mainframe.updateMessages((List)errors);
                        completion = errCount + " compilation errors. Please check the 'Messages' tab";
                     }
                  }
               } catch (Exception var28) {
                  completion = "unexpected error: " + var28.getMessage();
                  var28.printStackTrace();
               } finally {
                  if (completion != null) {
                     mainframe.updateLog(completion + '\n');
                  }
                  if (completion != null && completion.equals("successfully completed")) {
                     ArrayList list = new ArrayList();
                     list.add("模型通过OCL验证");
                     mainframe.updateMessages(list);
                  }
                  r.setLastCompilationInfos((Collection)lci);
                  mainframe.setCursor(new Cursor(0));
                  mainframe.updateEvaluationActions();
               }

            }
         }
      }
   }

   private static class STDCheckAction extends AbstractAction {
      public STDCheckAction() {
         this.putValue("ActionCommandKey", "menubar.tools.stdCheck");
      }

      public void actionPerformed(ActionEvent e) {
         GMainFrame.getMainFrame().clearErrorMessages();
         GTree tree = (GTree)GRepository.getInstance().getUsermodel().getBrowser().getComponent();
         if (tree.getSelectionPath() == null) {
            GMainFrame.getMainFrame().updateInfoMessages("Please select a node which is a State Machine first.");
         } else {
            BTreeNode node = (BTreeNode)tree.getSelectionPath().getLastPathComponent();
            if (!(node.getUserObject() instanceof StateMachine)) {
               GMainFrame.getMainFrame().updateMessages((Object)"This element is not a state machine !");
            } else {
               StateMachine sm = (StateMachine)node.getUserObject();
               STDChecker checker = new STDChecker(sm);
               Collection result = checker.check();
               Iterator it = result.iterator();

               while(it.hasNext()) {
                  GMainFrame.getMainFrame().updateLog((String)it.next() + "\n");
               }

               GMainFrame.getMainFrame().focusLog();
            }
         }
      }
   }

   private static class PartialCheckAction extends AbstractAction {
      PartialCheckAction() {
         this.putValue("ActionCommandKey", "menubar.tools.partial");
      }

      public void actionPerformed(ActionEvent e) {
         EvaluationSystemInvoker.getInvoker().invokeEvaluationSystem(0);
      }
   }

   private static class CheckAction extends AbstractAction {
      CheckAction() {
         this.putValue("ActionCommandKey", "menubar.tools.check");
      }

      public void actionPerformed(ActionEvent e) {
         EvaluationSystemInvoker.getInvoker().invokeEvaluationSystem(1);
      }
   }
}
