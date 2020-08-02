package ro.ubbcluj.lci.ocl;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLinkImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValueImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEndImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ObjectImpl;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.utils.ModelFactory;

public class ImportBoldObjects {
   private Hashtable classes = new Hashtable();
   private Hashtable objects = new Hashtable();
   private Model model;
   private Collaboration clb;
   private DOMSource domSource;
   private Document doc;

   public ImportBoldObjects(Model m, String theFile) {
      this.model = m;
      this.clb = (Collaboration)ModelFactory.createNewElement(this.model, "Collaboration");
      this.clb.setName(ModelFactory.getDefaultName((Namespace)this.clb.directGetNamespace(), this.clb, "Objects imported from ModelRUN"));
      this.getModelClasses(m);
      DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();

      try {
         DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
         this.doc = dBuilder.parse(new File(theFile));
         this.domSource = new DOMSource(this.doc);
      } catch (IOException var5) {
         System.err.println("fatal error: cannot open " + theFile);
      } catch (ParserConfigurationException var6) {
         System.err.println("fatal error: " + var6);
      } catch (SAXException var7) {
         System.err.println("fatal error: " + var7);
      }
   }

   public Model addObjects() {
      Node root = this.domSource.getNode();
      NodeList childs = root.getFirstChild().getChildNodes();

      for(int i = 0; i < childs.getLength(); ++i) {
         Node detObj = childs.item(i);
         this.doObjectInit(detObj);
      }

      return this.model;
   }

   private void doObjectInit(Node objRoot) {
      String className = objRoot.getNodeName();
      Node idNode = null;
      Node membersNode = null;
      Object detObj = null;
      Classifier detObjClassifier = null;
      NodeList objRootNodeList = objRoot.getChildNodes();

      for(int i = 0; i < objRootNodeList.getLength(); ++i) {
         Node node = objRootNodeList.item(i);
         if (node.getNodeName().equals("id")) {
            idNode = node;
         } else if (node.getNodeName().equals("members")) {
            membersNode = node;
         }
      }

      NodeList idNodeNodeList = idNode.getChildNodes();

      for(int i = 0; i < idNodeNodeList.getLength(); ++i) {
         Node node = idNodeNodeList.item(i);
         if (node.getNodeName().equals("DbValue")) {
            String objId = ((CharacterData)node.getFirstChild()).getData();
            detObj = (Object)this.objects.get(objId);
            if (detObj == null) {
               detObj = new ObjectImpl();
               ModelFactory.createElementOwnership(this.clb, (ModelElement)detObj);
               ((Object)detObj).setName("obj_" + className + "_" + objId);
               this.objects.put(objId, detObj);
               detObjClassifier = (Classifier)this.classes.get(className);
               ((Object)detObj).addClassifier(detObjClassifier);
            } else {
               detObjClassifier = (Classifier)((Object)detObj).getClassifierList().nextElement();
            }
         }
      }

      Set attribs = detObjClassifier.allAttributes();
      Set asocEnds = detObjClassifier.allOppositeAssociationEnds();
      NodeList membersNodeNodeList = membersNode.getChildNodes();

      for(int i = 0; i < membersNodeNodeList.getLength(); ++i) {
         Node attAssNode = membersNodeNodeList.item(i);
         String attAssName = attAssNode.getNodeName();
         Attribute attr = null;
         boolean found = false;
         Iterator value = attribs.iterator();

         while(value.hasNext() && !found) {
            ModelElement me = (ModelElement)value.next();
            if (me.getName().equals(attAssName)) {
               found = true;
               attr = (Attribute)me;
               break;
            }
         }

         NodeList attAssNodeNodeList;
         if (attr != null) {
            attAssNodeNodeList = attAssNode.getChildNodes();

            for(int ii = 0; ii < attAssNodeNodeList.getLength(); ++ii) {
               if (attAssNodeNodeList.item(ii).getNodeName().equals("content")) {
                  String value1 = ((CharacterData)attAssNodeNodeList.item(ii).getFirstChild()).getData();
                  this.setAttributeValue((Object)detObj, attr, value1);
                  break;
               }
            }
         } else {
            AssociationEnd ass = null;
            found = false;
            Iterator it = asocEnds.iterator();

            while(it.hasNext() && !found) {
               ModelElement me = (ModelElement)it.next();
               if (me.getName().equals(attAssName)) {
                  found = true;
                  ass = (AssociationEnd)me;
                  break;
               }
            }

            if (ass != null) {
               attAssNodeNodeList = attAssNode.getChildNodes();
               Node contentNode = null;

               for(int ii = 0; ii < attAssNodeNodeList.getLength(); ++ii) {
                  if (attAssNodeNodeList.item(ii).getNodeName().equals("content")) {
                     contentNode = attAssNodeNodeList.item(ii);
                     break;
                  }
               }

               NodeList contentNodeNodeList = contentNode.getChildNodes();

               for(int ii = 0; ii < contentNodeNodeList.getLength(); ++ii) {
                  Node contIdNode = contentNodeNodeList.item(ii);
                  if (contIdNode.getNodeName().startsWith("idlist")) {
                     contIdNode = contIdNode.getFirstChild();
                  }

                  if (contIdNode != null && contIdNode.getNodeName().equals("id")) {
                     NodeList contIdNodeNodeList = contIdNode.getChildNodes();
                     String linkValue = null;
                     String linkClassName = null;

                     for(int iii = 0; iii < contIdNodeNodeList.getLength(); ++iii) {
                        Node n = contIdNodeNodeList.item(iii);
                        if (n.getNodeName().equals("ClassName")) {
                           linkClassName = ((CharacterData)n.getFirstChild()).getData();
                        }

                        if (n.getNodeName().equals("DbValue")) {
                           linkValue = ((CharacterData)n.getFirstChild()).getData();
                        }
                     }

                     this.setAsocEndValue((Object)detObj, ass, linkValue, linkClassName);
                  }
               }
            }
         }
      }

   }

   private void setAttributeValue(Object detObj, Attribute attr, String value) {
      AttributeLink al = new AttributeLinkImpl();
      attr.addAttributeLink(al);
      detObj.addSlot(al);
      Instance ival = null;
      if (attr.getType() instanceof DataType) {
         ival = new DataValueImpl();
      } else if (attr.getType() instanceof Class) {
         ival = new ObjectImpl();
      }

      ((Instance)ival).setName(value);
      ((Instance)ival).addClassifier(attr.getType());
      ModelFactory.createElementOwnership(this.model, (ModelElement)ival);
      al.setValue((Instance)ival);
   }

   private void setAsocEndValue(Object detObj, AssociationEnd ass, String linkValue, String linkClassName) {
      Instance opObj = (Instance)this.objects.get(linkValue);
      Classifier c = (Classifier)this.classes.get(linkClassName);
      if (opObj == null) {
         opObj = new ObjectImpl();
         ((Instance)opObj).setName("obj_" + linkClassName + "_" + linkValue);
         ((Instance)opObj).addClassifier(c);
         this.objects.put(linkValue, opObj);
      }

      Association a = ass.getAssociation();
      Link l = new LinkImpl();
      l.setAssociation(a);
      detObj.addOwnedLink(l);
      ModelFactory.createElementOwnership(this.clb, l);
      LinkEnd leDetObj = new LinkEndImpl();
      ass.addLinkEnd(leDetObj);
      l.addConnection(leDetObj);
      leDetObj.setInstance(detObj);
      LinkEnd leOpObj = new LinkEndImpl();
      AssociationEnd ae = null;
      Enumeration aAssEnds = a.getConnectionList();

      while(aAssEnds.hasMoreElements()) {
         ae = (AssociationEnd)aAssEnds.nextElement();
         if (ae == ass) {
            break;
         }
      }

      ae.addLinkEnd(leOpObj);
      l.addConnection(leOpObj);
      leOpObj.setInstance((Instance)opObj);
   }

   private void getModelClasses(Namespace nn) {
      Enumeration en = nn.getOwnedElementList();

      while(en.hasMoreElements()) {
         ModelElement me = ((ElementOwnership)en.nextElement()).getOwnedElement();
         if (me instanceof Class || me instanceof DataType) {
            this.classes.put(me.getName(), me);
         }

         if (me instanceof Namespace) {
            this.getModelClasses((Namespace)me);
         }
      }

   }
}
