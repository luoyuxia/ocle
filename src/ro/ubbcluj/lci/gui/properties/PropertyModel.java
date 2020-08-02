package ro.ubbcluj.lci.gui.properties;

public class PropertyModel extends AbstractTreeTableModel {
   protected static String[] cNames = new String[]{"", ""};
   protected static Class[] cTypes;
   protected Object theElement;

   public PropertyModel(Object theElement) {
      super(new PropertyNode(theElement));
   }

   protected Object[] getChildren(Object node) {
      PropertyNode pNode = (PropertyNode)node;
      return pNode.getChildren();
   }

   public int getChildCount(Object node) {
      Object[] children = this.getChildren(node);
      return children == null ? 0 : children.length;
   }

   public Object getChild(Object node, int i) {
      return this.getChildren(node)[i];
   }

   public boolean isAllowingChildren(Object node) {
      return node instanceof PropertyNode ? ((PropertyNode)node).isAllowingChildren() : false;
   }

   public boolean isLeaf(Object node) {
      return ((PropertyNode)node).isLeaf();
   }

   public int getColumnCount() {
      return cNames.length;
   }

   public String getColumnName(int i) {
      return cNames[i];
   }

   public Class getColumnClass(int column) {
      return cTypes[column];
   }

   public Object getValueAt(Object node, int column) {
      PropertyNode pNode = (PropertyNode)node;
      if (pNode == null) {
         return "";
      } else {
         switch(column) {
         case 0:
            return pNode;
         case 1:
            return pNode.getValue();
         default:
            return "";
         }
      }
   }

   static {
      cTypes = new Class[]{TreeTableModel.class, String.class };
   }
}
