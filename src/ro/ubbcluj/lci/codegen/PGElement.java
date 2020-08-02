package ro.ubbcluj.lci.codegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;

public abstract class PGElement extends DefaultMutableTreeNode implements TemplatesSymbolicNames, TemplateKeys, JavaReservedWords {
   protected String name;
   protected List modifiers;
   protected ImportStatementManager importManager;

   public PGElement(PGElement container) {
      if (container != null) {
         container.add(this);
      }

      this.importManager = container != null ? container.getImportManager() : null;
   }

   public final ImportStatementManager getImportManager() {
      if (this.importManager == null && this.getParent() != null) {
         this.importManager = ((PGElement)this.getParent()).getImportManager();
      }

      return this.importManager;
   }

   public void setImportManager(ImportStatementManager mgr) {
      this.importManager = mgr;
   }

   public final void setName(String name) {
      this.name = name;
   }

   public final String getName() {
      return this.name;
   }

   public final void setModifiers(List modifiers) {
      this.modifiers = modifiers;
   }

   public final void addModifier(String modifier) {
      if (this.modifiers == null) {
         this.modifiers = new ArrayList();
      }

      this.modifiers.add(modifier);
   }

   public final void removeModifier(String modifier) {
      if (this.modifiers != null) {
         this.modifiers.remove(modifier);
      }

   }

   public final List getModifiers() {
      if (this.modifiers == null) {
         this.modifiers = new ArrayList();
      }

      return this.modifiers;
   }

   public StringBuffer generateCode() {
      StringBuffer result = new StringBuffer();
      StringBuffer sbtemp = this.getDeclarationCode();
      if (sbtemp != null && sbtemp.length() > 0) {
         result.append(sbtemp).append('\n');
         sbtemp.setLength(0);
      }

      sbtemp = this.getChildrenCode();
      if (sbtemp != null && sbtemp.length() > 0) {
         result.append(sbtemp).append('\n');
         sbtemp.setLength(0);
      }

      sbtemp = this.getFinalizerCode();
      if (sbtemp != null && sbtemp.length() > 0) {
         result.append(sbtemp);
         sbtemp.setLength(0);
      }

      return result;
   }

   protected abstract StringBuffer getDeclarationCode();

   protected StringBuffer getChildrenCode() {
      if (this.getAllowsChildren() && this.children != null && this.children.size() > 0) {
         StringBuffer result = new StringBuffer();
         Iterator it = this.children.iterator();

         while(it.hasNext()) {
            PGElement child = (PGElement)it.next();
            result.append(child.generateCode());
         }

         return result;
      } else {
         return null;
      }
   }

   protected StringBuffer getFinalizerCode() {
      Template template = CodeGeneratorManager.getTemplate("UML_Finalizer.vm");
      Context context = CodeGeneratorManager.newContext();
      StringBuffer sb = CodeGenUtilities.getText(template, context);
      return sb;
   }
}
