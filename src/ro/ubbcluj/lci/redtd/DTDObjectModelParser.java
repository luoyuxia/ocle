package ro.ubbcluj.lci.redtd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import ro.ubbcluj.lci.redtd.dtdmetamodel.ChildrenContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.ChoiceContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAnyElement;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAttribute;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAttributeEnumType;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAttributeOtherType;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAttributeType;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDElement;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDRestrictedElement;
import ro.ubbcluj.lci.redtd.dtdmetamodel.ElementContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.LeafContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.MixedContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.SequenceContent;

public class DTDObjectModelParser {
   public DTDObjectModelParser() {
   }

   public DTDElement parseDTDElement(String name, String model) {
      DTDElement element = null;
      if (model.equals("ANY")) {
         element = new DTDAnyElement(name);
      } else {
         element = new DTDRestrictedElement(name);
         ((DTDRestrictedElement)element).setContent(this.parseDTDElementModel(model));
      }

      return (DTDElement)element;
   }

   public DTDAttribute parseDTDAttribute(String elementName, String attributeName, String type, String valueDefault, String value) {
      boolean isRequired = false;
      boolean isFixed = false;
      if (valueDefault != null) {
         if (valueDefault.equals("#REQUIRED")) {
            isRequired = true;
         } else if (valueDefault.equals("#FIXED")) {
            isFixed = true;
         }
      }

      return new DTDAttribute(elementName, attributeName, this.parseDTDAttributeType(type), value, isRequired, isFixed);
   }

   private DTDAttributeType parseDTDAttributeType(String type) {
      if (type.indexOf("|") == -1) {
         return new DTDAttributeOtherType(type);
      } else {
         StringTokenizer strTok = new StringTokenizer(type, "(|)");
         ArrayList tokList = new ArrayList();

         while(strTok.hasMoreTokens()) {
            tokList.add(strTok.nextToken());
         }

         return new DTDAttributeEnumType(tokList);
      }
   }

   private ElementContent parseDTDElementModel(String model) {
      if (model.equals("EMPTY")) {
         return null;
      } else {
         HashMap childrenContents = new HashMap();
         if (model.substring(1).indexOf(40) == -1) {
            ElementContent result = this.parsePlainDTDElementModel(model.substring(1, model.lastIndexOf(41)), childrenContents);
            int multiplicity;
            if (result instanceof LeafContent) {
               if (!((LeafContent)result).getName().equals("#PCDATA")) {
                  multiplicity = this.parseDTDOccurence(model.charAt(model.length() - 1));
                  ((LeafContent)result).setMultiplicity(this.resolveLeafContentMultiplicity(((LeafContent)result).getMultiplicity(), multiplicity));
               }
            } else if (result instanceof ChildrenContent) {
               multiplicity = this.parseDTDOccurence(model.charAt(model.length() - 1));
               ((ChildrenContent)result).setMultiplicity(multiplicity);
            }

            return result;
         } else {
            String delims = "()?+*|, ";
            StringTokenizer tokenizer = new StringTokenizer(model, delims, true);
            LinkedList stack = new LinkedList();
            int nameNo = 1;

            while(true) {
               String currToken;
               do {
                  do {
                     if (!tokenizer.hasMoreTokens()) {
                        if (stack.isEmpty()) {
                           return null;
                        }

                        StringBuffer plainElementModel = new StringBuffer();

                        while(!stack.isEmpty()) {
                           plainElementModel.insert(0, stack.removeLast());
                        }

                        return this.parsePlainDTDElementModel(plainElementModel.toString(), childrenContents);
                     }

                     currToken = tokenizer.nextToken();
                  } while(currToken.equals(" "));

                  if (!currToken.equals(")")) {
                     stack.addLast(currToken);
                  }
               } while(tokenizer.hasMoreTokens() && !currToken.equals(")"));

               StringBuffer plainElementModel = new StringBuffer();

               while(!stack.isEmpty() && !stack.getLast().equals("(")) {
                  plainElementModel.insert(0, stack.removeLast());
               }

               if (stack.isEmpty()) {
                  return this.parsePlainDTDElementModel(plainElementModel.toString(), childrenContents);
               }

               if (stack.getLast().equals("(")) {
                  String contentKey = null;
                  ElementContent plainContent = this.parsePlainDTDElementModel(plainElementModel.toString(), childrenContents);
                  contentKey = this.generateNextTemporaryName(nameNo);
                  stack.removeLast();
                  stack.addLast(contentKey);
                  childrenContents.put(contentKey, plainContent);
                  ++nameNo;
               }
            }
         }
      }
   }

   private ElementContent parsePlainDTDElementModel(String model, HashMap childrenContents) {
      int posChoiceSymbol = model.indexOf(124);
      int posSeqSymbol = model.indexOf(44);
      if (posChoiceSymbol == -1 && posSeqSymbol == -1) {
         return this.parseLeafContent(model, childrenContents);
      } else if (posChoiceSymbol == -1) {
         return this.parsePlainSequenceModel(model, childrenContents);
      } else if (posSeqSymbol == -1) {
         ElementContent elementContent;
         if (model.startsWith("#PCDATA")) {
            elementContent = this.parseMixedModel(model);
         } else {
            elementContent = this.parsePlainChoiceModel(model, childrenContents);
         }
         return elementContent;
      } else {
         return null;
      }
   }

   private SequenceContent parsePlainSequenceModel(String model, HashMap childrenContents) {
      SequenceContent sequenceContent = new SequenceContent();
      ChildrenContent childContent = null;
      StringTokenizer tokenizer = new StringTokenizer(model, ", ");

      while(tokenizer.hasMoreTokens()) {
         String token = tokenizer.nextToken();
         String tokenName = token;
         if (token.endsWith("?") || token.endsWith("*") || token.endsWith("+")) {
            tokenName = token.substring(0, token.length() - 1);
         }

         if (childrenContents.containsKey(tokenName)) {
            childContent = (ChildrenContent)childrenContents.get(tokenName);
         } else {
            childContent = new LeafContent(tokenName);
         }

         ((ChildrenContent)childContent).setMultiplicity(this.parseDTDOccurence(token.charAt(token.length() - 1)));
         sequenceContent.addChild((ChildrenContent)childContent);
      }

      return sequenceContent;
   }

   private ChildrenContent parseLeafContent(String model, HashMap childrenContents) {
      if (model.matches("#PCDATA")) {
         return new LeafContent("#PCDATA");
      } else {
         String leafName = model;
         if (model.endsWith("?") || model.endsWith("*") || model.endsWith("+")) {
            leafName = model.substring(0, model.length() - 1);
         }

         ChildrenContent children = null;
         if (childrenContents.containsKey(leafName)) {
            children = (ChildrenContent)childrenContents.get(leafName);
         } else {
            children = new LeafContent(leafName);
         }

         ((ChildrenContent)children).setMultiplicity(this.parseDTDOccurence(model.charAt(model.length() - 1)));
         return (ChildrenContent)children;
      }
   }

   private ChoiceContent parsePlainChoiceModel(String model, HashMap childrenContents) {
      ChoiceContent choiceContent = new ChoiceContent();
      ChildrenContent childContent = null;
      StringTokenizer tokenizer = new StringTokenizer(model, "| ");

      while(tokenizer.hasMoreTokens()) {
         String token = tokenizer.nextToken();
         String tokenName = token;
         if (token.endsWith("?") || token.endsWith("*") || token.endsWith("+")) {
            tokenName = token.substring(0, token.length() - 1);
         }

         if (childrenContents.containsKey(tokenName)) {
            childContent = (ChildrenContent)childrenContents.get(tokenName);
         } else {
            childContent = new LeafContent(tokenName);
         }

         ((ChildrenContent)childContent).setMultiplicity(this.parseDTDOccurence(token.charAt(token.length() - 1)));
         choiceContent.addChild((ChildrenContent)childContent);
      }

      return choiceContent;
   }

   private MixedContent parseMixedModel(String model) {
      MixedContent mixedContent = new MixedContent();
      LeafContent leafContent = null;
      StringTokenizer tokenizer = new StringTokenizer(model, "| ");

      while(tokenizer.hasMoreTokens()) {
         String token = tokenizer.nextToken();
         leafContent = new LeafContent(token);
         mixedContent.addLeaf(leafContent);
      }

      return mixedContent;
   }

   private String generateNextTemporaryName(int nameNo) {
      return "<temp_" + nameNo + ">";
   }

   private int parseDTDOccurence(char occurenceSymbol) {
      switch(occurenceSymbol) {
      case '*':
         return 8;
      case '+':
         return 9;
      case '?':
         return 7;
      default:
         return 6;
      }
   }

   private int resolveLeafContentMultiplicity(int innerMultiplicity, int outerMultiplicity) {
      if (innerMultiplicity == outerMultiplicity) {
         return innerMultiplicity;
      } else if (innerMultiplicity == 6) {
         return outerMultiplicity;
      } else {
         return outerMultiplicity == 6 ? innerMultiplicity : 8;
      }
   }
}
