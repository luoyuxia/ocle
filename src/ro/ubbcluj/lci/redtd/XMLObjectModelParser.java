package ro.ubbcluj.lci.redtd;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.xml.sax.Attributes;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinition;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;

public class XMLObjectModelParser {
   private ContextStack contextStack = new ContextStack();
   private ModelBuilder modelBuilder = new ReDTDUMLModelBuilder();
   private Collaboration xmlObjects;
   private ReDTDModelCache modelCache = ReDTDModelCache.getInstance();

   public XMLObjectModelParser() {
   }

   public void parseXMLDocument() {
      this.xmlObjects = this.modelCache.getXmlCollaboration();
   }

   public void parseStartingXMLElement(String qName, Attributes attrs) {
      Instance obj = this.parseXMLObjects(qName);
      if (obj != null && attrs != null) {
         this.parseXMLAttributesForElement(obj, attrs);
      }

   }

   public void parseClosingXMLElement(String qName) {
      Classifier classifierOfClosingXMLElement = (Classifier)this.modelCache.getClass(qName);
      XMLParsingContext closingContext = this.contextStack.peek();

      for(Classifier classifierOfClosingContext = (Classifier)closingContext.getContextElement().getClassifierList().nextElement(); classifierOfClosingContext != classifierOfClosingXMLElement; classifierOfClosingContext = (Classifier)closingContext.getContextElement().getClassifierList().nextElement()) {
         if (!this.isContextResolved(closingContext)) {
            this.resolveClosingContext(closingContext);
         }

         this.contextStack.pop();
         closingContext = this.contextStack.peek();
      }

      if (!this.isContextResolved(closingContext)) {
         this.resolveClosingContext(closingContext);
      }

      this.contextStack.pop();
   }

   public void parseCharacterData(String data) {
      Instance obj = this.parseXMLObjects("#PCDATA");
      this.parseXMLAttributeForPCDATA(obj, data);
   }

   private void resolveClosingContext(XMLParsingContext context) {
      Classifier contextClass = (Classifier)context.getContextElement().getClassifierList().nextElement();
      if (ReDTDUtilities.isDTDSequenceGroup(contextClass)) {
         AssociationEnd subcontextEnd = context.getSubcontextEnd();
         Object[] oppEnds = ReDTDUtilities.getOrderedNavigableOppositeEnds(contextClass);

         int pos;
         for(pos = 0; pos < oppEnds.length; ++pos) {
            if (oppEnds[pos] == subcontextEnd) {
               ++pos;
               break;
            }
         }

         for(; pos < oppEnds.length; ++pos) {
            AssociationEnd assocEnd = (AssociationEnd)oppEnds[pos];
            if (((MultiplicityRange)assocEnd.getMultiplicity().getRangeList().nextElement()).getLower() > 0) {
               this.instantiateMissingElements(context.getContextElement(), assocEnd, this.xmlObjects);
            }
         }
      } else if (ReDTDUtilities.isDTDElement(contextClass)) {
         Iterator assocEndsIt = ReDTDUtilities.getNavigableOppositeEnds(contextClass).iterator();
         AssociationEnd subcontextEnd = context.getSubcontextEnd();
         if (subcontextEnd != null) {
            System.out.println("Subcontext should be null.. ");
            return;
         }

         while(assocEndsIt.hasNext()) {
            AssociationEnd assocEnd = (AssociationEnd)assocEndsIt.next();
            if (((MultiplicityRange)assocEnd.getMultiplicity().getCollectionRangeList().iterator().next()).getLower() > 0) {
               this.instantiateMissingElements(context.getContextElement(), assocEnd, this.xmlObjects);
            }
         }
      }

   }

   private Instance parseXMLObjects(String qName) {
      Instance obj = null;
      Classifier classToInstantiate = (Classifier)this.modelCache.getClass(qName);
      if (classToInstantiate == null) {
         classToInstantiate = this.modelBuilder.buildClass(qName);
         this.modelBuilder.attachStereotype(classToInstantiate, "DTDUndefinedElement");
      }

      if (this.contextStack.isEmpty()) {
         obj = this.modelBuilder.buildObject(classToInstantiate, this.xmlObjects);
         this.contextStack.push(new XMLParsingContext(obj, (AssociationEnd)null));
         return obj;
      } else {
         Collection path = null;
         boolean isObjectResolved = false;

         while(!isObjectResolved && !this.contextStack.isEmpty()) {
            XMLParsingContext currentContext = this.contextStack.peek();
            Classifier contextClass = (Classifier)currentContext.getContextElement().getClassifierList().nextElement();
            AssociationEnd subcontextEnd = currentContext.getSubcontextEnd();
            path = this.getPath(contextClass, classToInstantiate, subcontextEnd);
            if (path == null) {
               if (ReDTDUtilities.isDTDGroup(contextClass)) {
                  if (this.isContextResolved(currentContext)) {
                     this.contextStack.pop();
                  } else {
                     this.resolveStartingContext(contextClass, classToInstantiate, subcontextEnd);
                  }
               } else {
                  this.resolveStartingContext(contextClass, classToInstantiate, subcontextEnd);
               }
            } else {
               Iterator pathIt = path.iterator();

               while(pathIt.hasNext()) {
                  AssociationEnd assocEnd = (AssociationEnd)pathIt.next();
                  Classifier pathClass = assocEnd.getParticipant();
                  obj = this.modelBuilder.buildObject(pathClass, this.xmlObjects);
                  Instance contextObj = this.contextStack.peek().getContextElement();
                  this.modelBuilder.buildLink(contextObj, obj, assocEnd.getAssociation());
                  XMLParsingContext parsingContext;
                  if (pathClass.getName().equals("#PCDATA")) {
                     parsingContext = this.contextStack.peek();
                     parsingContext.setSubcontextEnd(assocEnd);
                  } else {
                     parsingContext = this.contextStack.peek();
                     parsingContext.setSubcontextEnd(assocEnd);
                     this.contextStack.push(new XMLParsingContext(obj, (AssociationEnd)null));
                  }
               }

               isObjectResolved = true;
            }
         }

         return obj;
      }
   }

   private Association resolveStartingContext(Classifier contextClass, Classifier unexpectedClass, AssociationEnd subcontextEnd) {
      boolean isPCDATAClass = unexpectedClass.getName().equals("#PCDATA");
      Association undefAssoc;
      int tagValue;
      if (!ReDTDUtilities.isDTDGroup(contextClass)) {
         Iterator assocEndsIt = ReDTDUtilities.getNavigableOppositeEnds(contextClass).iterator();
         TaggedValue taggedValue = null;
         if (assocEndsIt.hasNext()) {
             tagValue = 0;
            AssociationEnd assocEnd = (AssociationEnd)assocEndsIt.next();
            Classifier participant = assocEnd.getParticipant();
            if (ReDTDUtilities.isDTDGroup(participant)) {
               return this.resolveStartingContext(participant, unexpectedClass, (AssociationEnd)null);
            }

            Collection taggedValueList = assocEnd.getCollectionTaggedValueList();
            String taggedValueName = null;
            if (!isPCDATAClass && !participant.getName().equals("#PCDATA")) {
               if (taggedValueList.isEmpty()) {
                  taggedValueName = "sequence";
               } else {
                  taggedValueName = ((TaggedValue)taggedValueList.iterator().next()).getName();
               }
            } else {
               taggedValueName = "mixed";
            }

            if (taggedValueList.isEmpty()) {
               tagValue = tagValue + 1;
               taggedValue = this.modelBuilder.buildTaggedValue(taggedValueName, String.valueOf(tagValue));
               assocEnd.addTaggedValue(taggedValue);

               while(assocEndsIt.hasNext()) {
                  assocEnd = (AssociationEnd)assocEndsIt.next();
                  ++tagValue;
                  taggedValue = this.modelBuilder.buildTaggedValue(taggedValueName, String.valueOf(tagValue));
                  assocEnd.addTaggedValue(taggedValue);
               }
            } else {
               TaggedValue previousTaggedValue = (TaggedValue)taggedValueList.iterator().next();
               tagValue = tagValue + 1;
               if (taggedValueName.equals("mixed") && previousTaggedValue.getName().equals("sequence")) {
                  previousTaggedValue.setType((TagDefinition)this.modelCache.getTagDefinition(taggedValueName));
                  previousTaggedValue.setName(taggedValueName);
               }

               while(assocEndsIt.hasNext()) {
                  assocEnd = (AssociationEnd)assocEndsIt.next();
                  ++tagValue;
                  previousTaggedValue = (TaggedValue)assocEnd.getCollectionTaggedValueList().iterator().next();
                  if (taggedValueName.equals("mixed") && previousTaggedValue.getName().equals("sequence")) {
                     previousTaggedValue.setType((TagDefinition)this.modelCache.getTagDefinition(taggedValueName));
                     previousTaggedValue.setName(taggedValueName);
                  }
               }
            }

            ++tagValue;
            taggedValue = this.modelBuilder.buildTaggedValue(taggedValueName, String.valueOf(tagValue));
         }

         undefAssoc = this.modelBuilder.buildAssociation(contextClass, unexpectedClass, this.modelBuilder.buildMultiplicity(8), taggedValue);
         this.modelBuilder.attachStereotype(undefAssoc, "DTDUndefinedAssociation");
         return undefAssoc;
      } else {
         String tagName = ReDTDUtilities.getOppositeEndsTagName(contextClass);
         if (!ReDTDUtilities.isDTDMixedGroupKind(contextClass) && !ReDTDUtilities.isDTDChoiceGroup(contextClass)) {
            if (!ReDTDUtilities.isDTDSequenceGroup(contextClass)) {
               return null;
            } else {
               Object[] oppEnds = ReDTDUtilities.getOrderedNavigableOppositeEnds(contextClass);
               tagValue = 0;
               if (subcontextEnd != null) {
                  while(tagValue < oppEnds.length) {
                     if (oppEnds[tagValue] == subcontextEnd) {
                        ++tagValue;
                        break;
                     }

                     ++tagValue;
                  }
               }

               int shiftPos = tagValue;

               while(shiftPos < oppEnds.length) {
                  TaggedValue taggedValue = (TaggedValue)((AssociationEnd)oppEnds[shiftPos]).getTaggedValueList().nextElement();
                  ++shiftPos;
                  taggedValue.setDataValue(String.valueOf(shiftPos + 1));
               }

                undefAssoc = this.modelBuilder.buildAssociation(contextClass, unexpectedClass, this.modelBuilder.buildMultiplicity(8), this.modelBuilder.buildTaggedValue(tagName, String.valueOf(tagValue + 1)));
               if (isPCDATAClass) {
                  this.modelBuilder.attachStereotype(undefAssoc, "DTDInvalidAssociation");
               } else {
                  this.modelBuilder.attachStereotype(undefAssoc, "DTDUndefinedAssociation");
               }

               return undefAssoc;
            }
         } else if (tagName != null) {
             tagValue = ReDTDUtilities.getOrderedNavigableOppositeEnds(contextClass).length;
            undefAssoc = this.modelBuilder.buildAssociation(contextClass, unexpectedClass, this.modelBuilder.buildMultiplicity(8), this.modelBuilder.buildTaggedValue(tagName, String.valueOf(tagValue + 1)));
            if (ReDTDUtilities.isDTDChoiceGroup(contextClass)) {
               if (isPCDATAClass) {
                  this.modelBuilder.attachStereotype(undefAssoc, "DTDInvalidAssociation");
               } else {
                  this.modelBuilder.attachStereotype(undefAssoc, "DTDUndefinedAssociation");
               }
            } else {
               this.modelBuilder.attachStereotype(undefAssoc, "DTDUndefinedAssociation");
            }

            return undefAssoc;
         } else {
            return null;
         }
      }
   }

   private void parseXMLAttributesForElement(Instance obj, Attributes attrs) {
      Instance initialValue;
      for(int i = 0; i < attrs.getLength(); ++i) {
         String attrName = attrs.getQName(i);
         String attrValue = attrs.getValue(i);
         if (this.modelBuilder.buildAttributeLink(obj, attrName, attrValue) == null) {
            Attribute attr = this.modelBuilder.buildAttribute((Classifier)obj.getClassifierList().nextElement(), attrName, this.modelBuilder.buildDataType("String"), (String)null, false, false);
            this.modelBuilder.attachStereotype(attr, "CDATA");
            this.modelBuilder.attachStereotype(attr, "IMPLIED");
            this.modelBuilder.attachStereotype(attr, "DTDUndefinedAttribute");
            Iterator instances = ((Classifier)obj.getClassifierList().nextElement()).getCollectionInstanceList().iterator();

            while(instances.hasNext()) {
               initialValue = (Instance)instances.next();
               this.modelBuilder.buildAttributeLink(initialValue, attrName, "");
            }

            this.modelBuilder.buildAttributeLink(obj, attrName, attrValue);
         }
      }

      Collection attributes = ((Classifier)obj.getClassifierList().nextElement()).allAttributes();
      if (obj.getCollectionSlotList().size() < attributes.size()) {
         Collection instantiatedAttributes = new LinkedList();
         Iterator slotIterator = obj.getCollectionSlotList().iterator();

         while(slotIterator.hasNext()) {
            instantiatedAttributes.add(((AttributeLink)slotIterator.next()).getAttribute());
         }

         Iterator attrIt = attributes.iterator();

         while(true) {
            AttributeLink attrLink;
            Attribute attr;
            do {
               do {
                  if (!attrIt.hasNext()) {
                     return;
                  }

                  attr = (Attribute)attrIt.next();
               } while(instantiatedAttributes.contains(attr));

               String sinitialValue;
               if (attr.getInitialValue() != null) {
                  sinitialValue = attr.getInitialValue().getBody();
               } else {
                  sinitialValue = "";
               }

               attrLink = this.modelBuilder.buildAttributeLink(obj, attr.getName(), sinitialValue);
            } while(!attr.getCollectionStereotypeList().contains(this.modelCache.getStereotype("REQUIRED")) && ((MultiplicityRange)attr.getMultiplicity().getRangeList().nextElement()).getLower() <= 0);

            this.modelBuilder.attachStereotype(attrLink, "XMLMissingAttribute");
         }
      }
   }

   private void parseXMLAttributeForPCDATA(Instance obj, String data) {
      this.modelBuilder.buildAttributeLink(obj, "value", data);
   }

   private Collection getPath(Classifier fromClass, Classifier toClass, AssociationEnd subcontextEnd) {
      Collection children = this.getChildrenForSearch(fromClass, subcontextEnd);
      if (children == null) {
         return null;
      } else {
         Iterator childrenIt = children.iterator();

         AssociationEnd child;
         Collection partialPath;
         do {
            Classifier participant;
            do {
               if (!childrenIt.hasNext()) {
                  return null;
               }

               child = (AssociationEnd)childrenIt.next();
               participant = child.getParticipant();
               if (child.getParticipant() == toClass) {
                  Collection path = new LinkedList();
                  ((LinkedList)path).addFirst(child);
                  return path;
               }
            } while(!ReDTDUtilities.isDTDChoiceGroup(participant) && !ReDTDUtilities.isDTDMixedGroupKind(participant) && !ReDTDUtilities.isDTDSequenceGroup(participant));

            partialPath = this.getPath(participant, toClass, (AssociationEnd)null);
         } while(partialPath == null);

         Collection fullPath = new LinkedList(partialPath);
         ((LinkedList)fullPath).addFirst(child);
         return fullPath;
      }
   }

   private boolean isContextResolved(XMLParsingContext context) {
      Classifier contextClass = (Classifier)context.getContextElement().getClassifierList().nextElement();
      if (context.getSubcontextEnd() == null) {
         if (!ReDTDUtilities.isDTDEmptyElement(contextClass) && !ReDTDUtilities.isDTDAnyElement(contextClass)) {
            if (ReDTDUtilities.isDTDElement(contextClass)) {
               Iterator assocEndsIt = ReDTDUtilities.getNavigableOppositeEnds(contextClass).iterator();
               if (assocEndsIt.hasNext()) {
                  AssociationEnd assocEnd = (AssociationEnd)assocEndsIt.next();
                  return ((MultiplicityRange)assocEnd.getMultiplicity().getRangeList().nextElement()).getLower() == 0;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return true;
         }
      } else if (!ReDTDUtilities.isDTDGroup(contextClass)) {
         return true;
      } else if (!ReDTDUtilities.isDTDChoiceGroup(contextClass) && !ReDTDUtilities.isDTDMixedGroupKind(contextClass)) {
         int i = 0;
         boolean passed = false;
         Object[] oppEnds = ReDTDUtilities.getOrderedNavigableOppositeEnds(contextClass);

         for(AssociationEnd subcontextEnd = context.getSubcontextEnd(); i < oppEnds.length; ++i) {
            if (!passed) {
               if (oppEnds[i] == subcontextEnd) {
                  passed = true;
               }
            } else {
               AssociationEnd oppEnd = (AssociationEnd)oppEnds[i];
               if (((MultiplicityRange)oppEnd.getMultiplicity().getRangeList().nextElement()).getLower() > 0) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return true;
      }
   }

   private Collection getChildrenForSearch(Classifier contextClass, AssociationEnd subcontextEnd) {
      if (ReDTDUtilities.isPCDATA(contextClass)) {
         return null;
      } else {
         return subcontextEnd == null ? this.first(contextClass) : this.follow(contextClass, subcontextEnd);
      }
   }

   private Collection first(Classifier contextClass) {
      Collection children = new LinkedList();
      if (!ReDTDUtilities.isDTDSequenceGroup(contextClass)) {
         children.addAll(ReDTDUtilities.getNavigableOppositeEnds(contextClass));
      } else {
         Object[] oppEnds = ReDTDUtilities.getOrderedNavigableOppositeEnds(contextClass);

         for(int i = 0; i < oppEnds.length; ++i) {
            AssociationEnd oppEnd = (AssociationEnd)oppEnds[i];
            if (((MultiplicityRange)oppEnd.getMultiplicity().getRangeList().nextElement()).getLower() > 0) {
               children.add(oppEnd);
               break;
            }

            children.add(oppEnd);
         }
      }

      return children.isEmpty() ? null : children;
   }

   private Collection follow(Classifier contextClass, AssociationEnd subcontextEnd) {
      Collection children = new LinkedList();
      if (!ReDTDUtilities.isDTDGroup(contextClass)) {
         children.addAll(ReDTDUtilities.getNavigableOppositeEnds(contextClass));
      } else if (!ReDTDUtilities.isDTDMixedGroupKind(contextClass)) {
         if (ReDTDUtilities.isDTDChoiceGroup(contextClass)) {
            if (((MultiplicityRange)subcontextEnd.getMultiplicity().getRangeList().nextElement()).getUpper().intValue() == -1) {
               children.add(subcontextEnd);
            }
         } else if (ReDTDUtilities.isDTDSequenceGroup(contextClass)) {
            Object[] oppEnds = ReDTDUtilities.getOrderedNavigableOppositeEnds(contextClass);
            int i = 0;

            for(boolean passed = false; i < oppEnds.length; ++i) {
               if (!passed && oppEnds[i] == subcontextEnd) {
                  passed = true;
               }

               if (passed) {
                  AssociationEnd oppEnd = (AssociationEnd)oppEnds[i];
                  if (oppEnds[i] == subcontextEnd) {
                     if (((MultiplicityRange)subcontextEnd.getMultiplicity().getRangeList().nextElement()).getUpper().intValue() == -1) {
                        children.add(subcontextEnd);
                     }
                  } else {
                     children.add(oppEnd);
                     if (((MultiplicityRange)oppEnd.getMultiplicity().getRangeList().nextElement()).getLower() > 0) {
                        break;
                     }
                  }
               }
            }
         }
      }

      return children.isEmpty() ? null : children;
   }

   private void instantiateMissingElements(Instance context, AssociationEnd assocEnd, Collaboration collaboration) {
      Instance obj = this.modelBuilder.buildObject(assocEnd.getParticipant(), collaboration);
      this.modelBuilder.attachStereotype(obj, "XMLMissingElement");
      this.modelBuilder.buildLink(context, obj, assocEnd.getAssociation());
      this.modelBuilder.attachStereotype(obj, "XMLMissingLink");
   }
}
