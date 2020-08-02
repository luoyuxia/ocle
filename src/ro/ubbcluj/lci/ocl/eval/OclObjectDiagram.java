package ro.ubbcluj.lci.ocl.eval;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.ExceptionAny;
import ro.ubbcluj.lci.ocl.OclClassInfo;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUmlApi;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.datatypes.OclBoolean;
import ro.ubbcluj.lci.ocl.datatypes.OclCollection;
import ro.ubbcluj.lci.ocl.datatypes.OclInteger;
import ro.ubbcluj.lci.ocl.datatypes.OclReal;
import ro.ubbcluj.lci.ocl.datatypes.OclSet;
import ro.ubbcluj.lci.ocl.datatypes.OclString;
import ro.ubbcluj.lci.ocl.datatypes.Undefined;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkObject;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;

public class OclObjectDiagram extends OclExpression {
   private static final int ATTR = 1;
   private static final int ASSOC_MULTI = 2;
   private static final int ASSOC_SINGLE = 3;
   private OclExpression owner = null;
   private int type;
   private Attribute attr;
   private AssociationEnd asend;
   private Vector qualifierValues = null;
   private OclCollection container;
   private OclClassInfo expectedType;

   public OclObjectDiagram(OclNode p_nod, OclExpression p_owner, Attribute p_attr, OclClassInfo p_expectedType) {
      this.nod = p_nod;
      this.owner = p_owner;
      this.attr = p_attr;
      this.type = 1;
      this.expectedType = p_expectedType;
   }

   public OclObjectDiagram(OclNode p_nod, OclExpression p_owner, AssociationEnd p_asend) {
      this.nod = p_nod;
      this.owner = p_owner;
      this.type = 3;
      this.asend = p_asend;
   }

   public OclObjectDiagram(OclNode p_nod, OclExpression p_owner, AssociationEnd p_asend, String qualifier) {
      this.nod = p_nod;
      this.owner = p_owner;
      this.type = 3;
      this.asend = p_asend;
      this.qualifierValues = new Vector();
      StringTokenizer tokenizer = new StringTokenizer(qualifier, ",", false);

      while(true) {
         while(tokenizer.hasMoreTokens()) {
            String tok = tokenizer.nextToken();
            if (tok.charAt(0) != '\'' && tok.charAt(0) != '"') {
               this.qualifierValues.add(tok);
            } else {
               this.qualifierValues.add(tok.substring(1, tok.length() - 1));
            }
         }

         return;
      }
   }

   public OclObjectDiagram(OclNode p_nod, OclExpression p_owner, AssociationEnd p_asend, OclCollection p_container) {
      this.nod = p_nod;
      this.owner = p_owner;
      this.type = 2;
      this.asend = p_asend;
      this.container = p_container;
   }

   public Object evaluate() throws ExceptionEvaluate {
      super.evaluate();
      if (this.owner == null) {
         throw new ExceptionEvaluate("object property call for a null object", this.nod);
      } else {
         Object ownerobj = this.owner.evaluate();
         if (!(ownerobj instanceof Instance)) {
            throw new ExceptionEvaluate("[internal] object property call for a non Instance", this.nod);
         } else {
            Object rezObject;
            if (this.type == 1) {
               Object value = this.getAttributeValue((Instance)ownerobj);
               if (value != null) {
                  rezObject = this.convertDataValue(value);
                  if (rezObject instanceof String) {
                     throw new ExceptionEvaluate((String)rezObject, this.nod);
                  } else {
                     return rezObject;
                  }
               } else {
                  throw new ExceptionEvaluate(this.attr.getName() + " value is null", this.nod);
               }
            } else {
               Vector result = this.getLinkValue((Instance)ownerobj);
               if (this.type != 3) {
                  try {
                     this.container = this.container.newInstance();
                  } catch (ExceptionAny var6) {
                     throw new ExceptionEvaluate(var6.getMessage(), this.nod);
                  }

                  Iterator it = result.iterator();

                  while(it.hasNext()) {
                     Object item = this.convertDataValue(it.next());
                     if (item instanceof String) {
                        throw new ExceptionEvaluate((String)item, this.nod);
                     }

                     this.container.getCollection().add(item);
                  }

                  return this.container;
               } else if (result.size() == 1) {
                  rezObject = this.convertDataValue(result.elementAt(0));
                  if (rezObject instanceof String) {
                     throw new ExceptionEvaluate((String)rezObject, this.nod);
                  } else {
                     return rezObject;
                  }
               } else if (result.size() < 1) {
                  return new Undefined();
               } else if (this.asend.getCollectionQualifierList().isEmpty() || this.qualifierValues != null && !this.qualifierValues.isEmpty()) {
                  throw new ExceptionEvaluate("navigation with multiplicity one resulted in a set with size greater than one", this.nod);
               } else {
                  OclCollection set = new OclSet();
                  Iterator it = result.iterator();

                  while(it.hasNext()) {
                     Object item = this.convertDataValue(it.next());
                     if (item instanceof String) {
                        throw new ExceptionEvaluate((String)item, this.nod);
                     }

                     set.getCollection().add(item);
                  }

                  return set;
               }
            }
         }
      }
   }

   private Object convertDataValue(Object obj) {
      if (obj == null) {
         return new Undefined();
      } else {
         if (obj instanceof DataValue) {
            String s = ((Instance)obj).getName();
            if (s.equals("undefined") || s.equals("<undefined>")) {
               return new Undefined();
            }

            if (this.expectedType == OclUtil.umlapi.STRING) {
               return new OclString(s);
            }

            if (s.equals("")) {
               return new Undefined();
            }

            if (this.expectedType == OclUtil.umlapi.REAL) {
               try {
                  return new OclReal(s);
               } catch (NumberFormatException var4) {
                  return "Type mismatch: " + ((DataValue)obj).getName() + " is not a valid Real value";
               }
            }

            if (this.expectedType == OclUtil.umlapi.INTEGER) {
               try {
                  return new OclInteger(s);
               } catch (NumberFormatException var5) {
                  return "Type mismatch: " + ((DataValue)obj).getName() + " is not a valid Integer value";
               }
            }

            if (this.expectedType == OclUtil.umlapi.BOOLEAN) {
               if (!s.equals("1") && !s.equalsIgnoreCase("true")) {
                  if (!s.equals("0") && !s.equalsIgnoreCase("false")) {
                     return "Type mismatch: " + ((DataValue)obj).getName() + " is not a valid Boolean value";
                  }

                  return new OclBoolean(false);
               }

               return new OclBoolean(true);
            }

            if (this.attr.getType() instanceof Enumeration && !((Enumeration)this.attr.getType()).getCollectionLiteralList().contains(obj)) {
               return "Type mismatch: " + obj.toString() + " is not a valid literal value for " + this.attr.getType().getName();
            }
         }

         return obj;
      }
   }

   private Object getAttributeValue(Instance inst) {
      java.util.Enumeration atlinks = inst.getSlotList();

      AttributeLink atlink;
      Attribute tmpAttr;
      do {
         if (!atlinks.hasMoreElements()) {
            return null;
         }

         atlink = (AttributeLink)atlinks.nextElement();
         tmpAttr = atlink.getAttribute();
      } while(tmpAttr != this.attr);

      if (this.attr.getType() instanceof Enumeration) {
         Enumeration enum = (Enumeration)tmpAttr.getType();
         Iterator it = enum.getCollectionLiteralList().iterator();

         EnumerationLiteral literal;
         do {
            if (!it.hasNext()) {
               return atlink.getValue();
            }

            literal = (EnumerationLiteral)it.next();
         } while(!literal.getName().equals(atlink.getValue().getName()));

         return literal;
      } else if (this.attr.getType().getName().equals("Boolean")) {
         if (!atlink.getValue().getName().equals("true") && !atlink.getValue().getName().equals("false")) {
            return atlink.getValue();
         } else {
            return atlink.getValue();
         }
      } else {
         return atlink.getValue();
      }
   }

   private Vector getLinkValue(Instance inst) {
      Vector result = new Vector();
      java.util.Enumeration linkends = null;
      Classifier p2Asend = this.asend.getParticipant();
      boolean isQualified = this.qualifierValues != null && !this.qualifierValues.isEmpty() && !this.asend.getCollectionQualifierList().isEmpty();
      if (inst instanceof LinkObject) {
         linkends = ((LinkObject)inst).getConnectionList();

         while(linkends.hasMoreElements()) {
            LinkEnd conn = (LinkEnd)linkends.nextElement();
            AssociationEnd a2Conn = conn.getAssociationEnd();
            Classifier p1Conn = a2Conn.getParticipant();
            if (p1Conn == p2Asend && a2Conn.getName().equals(this.asend.getName())) {
               if (!isQualified) {
                  result.add(conn.getInstance());
                  break;
               }

               if (this.hasQualifiedValue(conn)) {
                  result.add(conn.getInstance());
                  break;
               }
            }
         }
      }

      if (result.size() < 1) {
         String oppEndRoleName = null;
         if (this.asend instanceof OclUmlApi.MyAssociationEndImpl && ((OclUmlApi.MyAssociationEndImpl)this.asend).methodName != null) {
            oppEndRoleName = ((OclUmlApi.MyAssociationEndImpl)this.asend).methodName.substring(3);
            oppEndRoleName = OclUmlApi.firstLower(oppEndRoleName);
         }

         boolean toLinkObject = false;
         boolean toDirectOppEnd = false;
         boolean toOppEnd = false;
         boolean notFound = true;
         linkends = inst.getLinkEndList();

         while(true) {
            LinkEnd opLinkend;
            Link selectedLink;
            label137:
            do {
               while(true) {
                  while(true) {
                     LinkEnd linkend;
                     label116:
                     do {
                        while(linkends.hasMoreElements()) {
                           linkend = (LinkEnd)linkends.nextElement();
                           opLinkend = this.oppositeLinkEnd(linkend);
                           selectedLink = opLinkend.getLink();
                           if (selectedLink instanceof LinkObject) {
                              continue label116;
                           }

                           if ((notFound || toOppEnd) && p2Asend == opLinkend.getAssociationEnd().getParticipant() && opLinkend.getAssociationEnd().getName().equals(this.asend.getName())) {
                              if (isQualified) {
                                 if (this.hasQualifiedValue(opLinkend)) {
                                    result.add(opLinkend.getInstance());
                                    if (!notFound) {
                                       notFound = false;
                                    }

                                    if (!toOppEnd) {
                                       toOppEnd = true;
                                    }
                                 }
                              } else {
                                 result.add(opLinkend.getInstance());
                                 if (!notFound) {
                                    notFound = false;
                                 }

                                 if (!toOppEnd) {
                                    toOppEnd = true;
                                 }
                              }
                           }
                        }

                        return result;
                     } while(!notFound && !toLinkObject);

                     java.util.Enumeration classifiers = ((LinkObject)selectedLink).getClassifierList();
                     Object it = classifiers.nextElement();
                     if (p2Asend == it) {
                        if (linkend.getAssociationEnd().getParticipant() == opLinkend.getAssociationEnd().getParticipant()) {
                           continue label137;
                        }

                        result.add(selectedLink);
                        if (!notFound) {
                           notFound = false;
                        }

                        if (!toLinkObject) {
                           toLinkObject = true;
                        }
                     } else if (notFound || toDirectOppEnd) {
                        AssociationEnd a1Opp = opLinkend.getAssociationEnd();
                        Classifier p1Opp = a1Opp.getParticipant();
                        if (p1Opp == p2Asend && a1Opp.getName().equals(this.asend.getName())) {
                           if (isQualified) {
                              if (this.hasQualifiedValue(opLinkend)) {
                                 result.add(opLinkend.getInstance());
                                 if (!notFound) {
                                    notFound = false;
                                 }

                                 if (!toDirectOppEnd) {
                                    toDirectOppEnd = true;
                                 }
                              }
                           } else {
                              result.add(opLinkend.getInstance());
                              if (!notFound) {
                                 notFound = false;
                              }

                              if (!toDirectOppEnd) {
                                 toDirectOppEnd = true;
                              }
                           }
                        }
                     }
                  }
               }
            } while(oppEndRoleName != null && (oppEndRoleName == null || opLinkend.getAssociationEnd().getName().equals(oppEndRoleName)));

            result.add(selectedLink);
            if (!notFound) {
               notFound = false;
            }

            if (!toLinkObject) {
               toLinkObject = true;
            }
         }
      } else {
         return result;
      }
   }

   private LinkEnd oppositeLinkEnd(LinkEnd linkend) {
      java.util.Enumeration linkends = linkend.getLink().getConnectionList();

      LinkEnd oplinkend;
      do {
         if (!linkends.hasMoreElements()) {
            System.err.println("[internal] cannot find opposite link end");
            return null;
         }

         oplinkend = (LinkEnd)linkends.nextElement();
      } while(linkend == oplinkend);

      return oplinkend;
   }

   private boolean hasQualifiedValue(LinkEnd conn) {
      int i = 0;

      for(Iterator it = conn.getCollectionQualifierValueList().iterator(); it.hasNext(); ++i) {
         AttributeLink attrLink = (AttributeLink)it.next();
         if (!attrLink.getValue().getName().equals(this.qualifierValues.get(i))) {
            return false;
         }
      }

      return true;
   }
}
