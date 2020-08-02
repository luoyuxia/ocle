package ro.ubbcluj.lci.redtd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public class UMLToXMLTransformer extends UMLTransformer {
   private ReDTDModelCache modelCache = ReDTDModelCache.getInstance();
   private FileOutputStream xmlOutputStream;

   public UMLToXMLTransformer() {
   }

   public void transformModel(Model umlModel, File outputFile) {
      this.initOutputStream(outputFile);
      this.transformCollaborationToXMLDocument();

      try {
         this.xmlOutputStream.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   private void transformCollaborationToXMLDocument() {
      this.emitXML("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      Instance root = ReDTDUtilities.findRootInstance();
      if (root == null) {
         System.out.println("XML objects could not be saved. No root element found.");
      } else {
         this.transformObjectToXML(root);
      }
   }

   private void transformObjectToXML(Instance obj) {
      Classifier classifier = (Classifier)obj.getClassifierList().nextElement();
      if (classifier.getName().equals("#PCDATA")) {
         this.transformPCDATAToXML(obj);
      } else {
         Iterator oppLinkEndsIt;
         if (ReDTDUtilities.isDTDGroup(classifier)) {
            oppLinkEndsIt = ReDTDUtilities.getNavigableOppositeLinkEnds(obj).iterator();

            while(oppLinkEndsIt.hasNext()) {
               LinkEnd oppLinkEnd = (LinkEnd)oppLinkEndsIt.next();
               this.transformObjectToXML(oppLinkEnd.getInstance());
            }
         } else {
            this.emitXML("<" + classifier.getName());
            oppLinkEndsIt = obj.getCollectionSlotList().iterator();

            while(true) {
               while(oppLinkEndsIt.hasNext()) {
                  this.emitXML(" ");
                  AttributeLink attrLink = (AttributeLink)oppLinkEndsIt.next();
                  String attrValue = attrLink.getValue().getName();
                  if (!attrValue.equals("<undefined>") && !attrValue.equals("")) {
                     this.emitXML(attrLink.getName() + "=" + '"' + attrValue + '"');
                  } else if (attrLink.getAttribute().getCollectionStereotypeList().contains(this.modelCache.getStereotype("REQUIRED"))) {
                     this.emitXML(attrLink.getName() + "=" + '"' + '"');
                  }
               }

               if (ReDTDUtilities.isDTDEmptyElement(classifier)) {
                  this.emitXML("/>");
               } else {
                  this.emitXML(">");
                  Iterator oppLinkEndsIt1;
                  if (ReDTDUtilities.getNavigableOppositeEnds(classifier).size() == 1) {
                     oppLinkEndsIt1 = ReDTDUtilities.getNavigableOppositeLinkEnds(obj).iterator();

                     while(oppLinkEndsIt1.hasNext()) {
                        this.transformObjectToXML(((LinkEnd)oppLinkEndsIt1.next()).getInstance());
                     }
                  } else {
                     oppLinkEndsIt1 = ReDTDUtilities.getNavigableOppositeLinkEnds(obj).iterator();

                     while(oppLinkEndsIt1.hasNext()) {
                        LinkEnd oppLinkEnd = (LinkEnd)oppLinkEndsIt1.next();
                        this.transformObjectToXML(oppLinkEnd.getInstance());
                     }
                  }

                  this.emitXML("</" + classifier.getName() + ">");
               }
               break;
            }
         }
      }

   }

   private void transformPCDATAToXML(Instance pcdataObj) {
      Iterator slotsIt = pcdataObj.getCollectionSlotList().iterator();

      while(slotsIt.hasNext()) {
         AttributeLink attrLink = (AttributeLink)slotsIt.next();
         if (attrLink.getName().equals("value")) {
            String value = attrLink.getValue().getName();
            value = value.replaceAll("&", "&amp;");
            value = value.replaceAll("<", "&lt;");
            value = value.replaceAll(">", "&gt;");
            this.emitXML(value);
            break;
         }
      }

   }

   private void initOutputStream(File outputFile) {
      try {
         this.xmlOutputStream = new FileOutputStream(outputFile);
      } catch (FileNotFoundException var3) {
         var3.printStackTrace();
      }

   }

   private void emitXML(String chars) {
      try {
         this.xmlOutputStream.write(chars.getBytes());
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }
}
