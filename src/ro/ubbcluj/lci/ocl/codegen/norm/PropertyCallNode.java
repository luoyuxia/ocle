package ro.ubbcluj.lci.ocl.codegen.norm;

import java.util.ArrayList;
import java.util.List;
import ro.ubbcluj.lci.ocl.OclClassInfo;
import ro.ubbcluj.lci.ocl.OclLetItem;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.SearchResult;

public final class PropertyCallNode extends SyntaxTreeNode {
   private OclClassInfo realOwner;
   private ArrayList qualifiers;
   private SearchResult data;

   public PropertyCallNode() {
      this((OclClassInfo)null);
   }

   public PropertyCallNode(OclClassInfo realOwner) {
      this.realOwner = realOwner;
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitPropertyCall(this);
   }

   public OclClassInfo getRealOwner() {
      return this.realOwner;
   }

   public String getPropertyName() {
      if (this.userObject instanceof OclLetItem) {
         return ((OclLetItem)this.userObject).name;
      } else {
         OclNode firstChild = ((OclNode)this.userObject).firstChild();
         return firstChild.getChildCount() > 0 ? firstChild.lastChild().getValueAsString() : firstChild.getValueAsString();
      }
   }

   public void addQualifier(Qualifier q) {
      if (this.qualifiers == null) {
         this.qualifiers = new ArrayList();
      }

      this.qualifiers.add(q);
   }

   public List getQualifiers() {
      if (this.qualifiers == null) {
         this.qualifiers = new ArrayList();
      }

      return (List)this.qualifiers.clone();
   }

   public void setData(SearchResult d) {
      this.data = d;
   }

   public SearchResult getData() {
      return this.data;
   }
}
