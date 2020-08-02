package ro.ubbcluj.lci.gui.mainframe;

import ro.ubbcluj.lci.errors.AbstractEvaluationError;
import ro.ubbcluj.lci.errors.CompilationErrorMessage;
import ro.ubbcluj.lci.errors.ErrorVisitor;
import ro.ubbcluj.lci.errors.TextLocalizableErrorMessage;
import ro.ubbcluj.lci.gui.Actions.CompilationInfo;
import ro.ubbcluj.lci.gui.editor.Editor;
import ro.ubbcluj.lci.gui.editor.TextDocumentPad;
import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.utils.ModelFactory;

public class ErrorLocatingVisitor extends ErrorVisitor {
   public ErrorLocatingVisitor() {
   }

   public void visitCompilationError(CompilationErrorMessage msg) {
      this.visitTextLocalizableErrorMessage(msg);
   }

   public void visitEvaluationError(AbstractEvaluationError msg) {
      Object context = msg.getContext();
      OclNode rule = msg.getRule();
      ModelFactory.fireModelEvent(context, (Object)null, 0);
      this.visitTextLocalizableErrorMessage(msg);
      String fileName = (String)msg.getSource();
      CompilationInfo ci = GRepository.getInstance().getInfoForFile(fileName);
      if (ci != null) {
         ci.getContextBindings().put(rule.getParent().getChild(0).getChild(1).type.type, context);
      }

   }

   public void visitTextLocalizableErrorMessage(TextLocalizableErrorMessage msg) {
      String fileName = (String)msg.getSource();
      if (fileName != null) {
         Editor ed = GApplication.getApplication().getEditor();
         if (ed.activateFileView(fileName) != null) {
            JEditTextArea ta = ((TextDocumentPad)ed.getActivePad()).getView();

            try {
               ta.select(msg.getStart(), msg.getStop());
            } catch (Exception var6) {
            }
         }
      }

   }
}
