package ro.ubbcluj.lci.gui.browser.BTree;

import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.gui.tools.AbstractRenderer;
import ro.ubbcluj.lci.gui.tools.RendererInfo;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Dependency;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class BCellRenderer extends DefaultTreeCellRenderer {
   public BCellRenderer() {
   }

   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasfocus) {
      super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasfocus);
      BTreeNode n = (BTreeNode)value;
      Object user_object = n.getUserObject();
      RendererInfo renderInfo = AbstractRenderer.getRendererInfo(user_object);
      this.setFont(this.getFont().deriveFont(renderInfo.fontStyle));
      String text = getDescription(user_object);
      this.setToolTipText(text);
      this.setText(text);
      if (user_object instanceof ModelElement) {
         Collection stereotypeList = ((ModelElement)user_object).getCollectionStereotypeList();
         if (!stereotypeList.isEmpty()) {
            String name = ((ModelElement)stereotypeList.iterator().next()).getName();
            if (name.indexOf("Undefined") > 0 || name.indexOf("Missing") > 0 || name.indexOf("Unexpected") > 0 || name.indexOf("Invalid") > 0) {
               this.setForeground(Color.red);
            }
         }
      }

      if (renderInfo.icon != null) {
         this.setIcon(renderInfo.icon);
      }

      return this;
   }

   private static String getDescription(Object obj) {
      String result = null;
      if (obj instanceof GAbstractDiagram) {
         result = ((GAbstractDiagram)obj).getName();
      } else if (obj instanceof BehavioralFeature) {
         result = UMLUtilities.getFullSignature((BehavioralFeature)obj, false);
      } else if (obj instanceof AttributeLink) {
         AttributeLink tmpLnk = (AttributeLink)obj;
         StringBuffer bf = new StringBuffer(tmpLnk.getAttribute().getName());
         bf.append('=');
         if (tmpLnk.getValue() != null) {
            if (tmpLnk.getValue().getName() != null) {
               if (!tmpLnk.getValue().getName().equals("<undefined>") && !tmpLnk.getValue().getName().equals("")) {
                  bf.append(tmpLnk.getValue().getName());
               } else {
                  bf.append("undefined");
               }
            } else {
               bf.append("undefined");
            }
         } else {
            bf.append("undefined");
         }

         result = bf.toString();
      } else if (obj instanceof Instance) {
         Enumeration objsClassifiers = ((Instance)obj).getClassifierList();
         if (objsClassifiers.hasMoreElements()) {
            result = ((Instance)obj).getName() + " : " + ((Classifier)objsClassifiers.nextElement()).getName();
         }
      } else if (obj instanceof ModelElement) {
         ModelElement me = (ModelElement)obj;
         result = me.getName();
         if (result == null || "".equals(result)) {
            result = "Unnamed " + me.getMetaclassName();
            StringBuffer bf;
            Enumeration clients;
            if (obj instanceof Association) {
               Association tmpAssoc = (Association)obj;
               bf = new StringBuffer();
               clients = tmpAssoc.getConnectionList();
               bf.append("A_");

               while(clients.hasMoreElements()) {
                  AssociationEnd con = (AssociationEnd)clients.nextElement();
                  bf.append(getDescription(con.getParticipant()));
                  if (clients.hasMoreElements()) {
                     bf.append('_');
                  }
               }

               result = bf.toString();
            } else if (obj instanceof AssociationEnd) {
               AssociationEnd end = (AssociationEnd)obj;
               String participantName = end.getParticipant().getName();
               result = Character.toLowerCase(participantName.charAt(0)) + participantName.substring(1);
            } else if (obj instanceof Dependency) {
               Dependency dep = (Dependency)obj;
               bf = new StringBuffer("dependency_");
               clients = dep.getClientList();

               while(clients.hasMoreElements()) {
                  bf.append(getDescription(clients.nextElement()));
                  if (clients.hasMoreElements()) {
                     bf.append(',');
                  }
               }

               bf.append('-');
               Enumeration suppliers = dep.getSupplierList();

               while(suppliers.hasMoreElements()) {
                  bf.append(getDescription(suppliers.nextElement()));
                  if (suppliers.hasMoreElements()) {
                     bf.append(',');
                  }
               }

               result = bf.toString();
            }
         }
      } else if (obj instanceof Expression) {
         result = ((Expression)obj).getBody();
      }

      return result;
   }
}
