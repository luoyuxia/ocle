package ro.ubbcluj.lci.gui.tools;

import java.net.URL;
import javax.swing.ImageIcon;
import ro.ubbcluj.lci.gui.diagrams.ClassDiagram;
import ro.ubbcluj.lci.gui.diagrams.ObjectDiagram;
import ro.ubbcluj.lci.gui.diagrams.UseCaseDiagram;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Reception;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Pseudostate;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.Operation;

public class AbstractRenderer {
   private static RendererInfo ri = new RendererInfo();
   private static String PREFIX = "/resources/";

   public AbstractRenderer() {
   }

   public static RendererInfo getRendererInfo(Object target) {
      ri.fontStyle = 0;
      Class cls = target.getClass();
      String cName = OclUtil.className(cls);
      String path;
      if (target instanceof Feature) {
         if (target instanceof Reception) {
            path = "Reception";
         } else {
            if (target instanceof Operation) {
               path = "operation-";
            } else {
               path = "attribute-";
            }

            switch(((Feature)target).getVisibility()) {
            case 0:
               path = path + "private";
               break;
            case 1:
               path = path + "package";
               break;
            case 2:
               path = path + "protected";
               break;
            default:
               path = path + "public";
            }
         }
      } else if (target instanceof AssociationEnd) {
         path = "AssociationEnd_";
         AssociationEnd ae = (AssociationEnd)target;
         switch(ae.getAggregation()) {
         case 0:
            path = path + "composite";
            break;
         case 2:
            path = path + "aggregate";
            break;
         default:
            if (ae.isNavigable()) {
               path = path + "navigable";
            } else {
               path = "AssociationEnd";
            }
         }
      } else if (target instanceof Pseudostate) {
         path = "Pseudostate_";
         Pseudostate ps = (Pseudostate)target;
         switch(ps.getKind()) {
         case 0:
            path = path + "fork";
            break;
         case 1:
         default:
            path = path + "shallow_history";
            break;
         case 2:
            path = path + "join";
            break;
         case 3:
            path = path + "initial";
            break;
         case 4:
            path = path + "choice";
            break;
         case 5:
            path = path + "deep_history";
            break;
         case 6:
            path = path + "junction";
         }
      } else if (cName.equals("Model")) {
         path = "Package";
      } else if (cName.equals("Usage")) {
         path = "Realize";
      } else if (target instanceof ClassDiagram) {
         path = "class_diagram";
      } else if (target instanceof UseCaseDiagram) {
         path = "usecase_diagram";
      } else if (target instanceof ObjectDiagram) {
         path = "snapshot_diagram";
      } else {
         path = cName;
      }

      URL u = (Integer.class).getResource(PREFIX + path + ".gif");
      ImageIcon icon;
      if (u == null) {
         icon = null;
      } else {
         icon = new ImageIcon(u);
      }

      ri.icon = icon;
      if (target instanceof Interface) {
         ri.fontStyle = 2;
      } else if (target instanceof GeneralizableElement) {
         GeneralizableElement el = (GeneralizableElement)target;
         if (el.isAbstract()) {
            ri.fontStyle = 2;
         }
      }

      return ri;
   }
}
