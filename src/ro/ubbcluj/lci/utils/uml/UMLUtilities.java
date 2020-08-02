package ro.ubbcluj.lci.utils.uml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import ro.ubbcluj.lci.uml.foundation.core.Abstraction;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Dependency;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.uml.foundation.core.Permission;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public final class UMLUtilities {
   public UMLUtilities() {
   }

   public static Package getPackage(Package start, String[] path) {
      Package cp = start;
      int i = 0;

      while(i < path.length && cp != null) {
         Iterator ito = cp.directGetCollectionOwnedElementList().iterator();
         boolean found = false;

         while(ito.hasNext() && !found) {
            Object next = ito.next();
            if (next instanceof Package) {
               Package p = (Package)next;
               if (path[i].equals(p.getName())) {
                  cp = p;
                  ++i;
                  found = true;
                  break;
               }
            }
         }

         if (!found) {
            cp = null;
         }
      }

      return cp;
   }

   public static boolean hasPermission(Package client, Package server) {
      boolean has = client.equals(server);
      if (!has) {
         Iterator itd = client.getCollectionClientDependencyList().iterator();

         while(true) {
            while(!has && itd.hasNext()) {
               Dependency dep = (Dependency)itd.next();
               if (dep instanceof Permission) {
                  has = hasStereotype(dep, "access") || hasStereotype(dep, "import");
               } else {
                  has = true;
               }
            }

            return has;
         }
      } else {
         return has;
      }
   }

   public static boolean hasStereotype(ModelElement elem, String stName) {
      boolean result = false;
      Iterator its = elem.getCollectionStereotypeList().iterator();

      while(!result && its.hasNext()) {
         Stereotype st = (Stereotype)its.next();
         if (stName.equals(st.getName())) {
            result = true;
         }
      }

      return result;
   }

   public static boolean isEnumeration(ModelElement c) {
      Enumeration enStereotypeList = c.getStereotypeList();
      boolean isEnum = false;

      while(!isEnum && enStereotypeList.hasMoreElements()) {
         Stereotype st = (Stereotype)enStereotypeList.nextElement();
         if ("enumeration".equals(st.getName())) {
            isEnum = true;
         }
      }

      return isEnum;
   }

   public static boolean isRealization(Abstraction a) {
      return hasStereotype(a, "realize");
   }

   public static boolean isMultiple(AssociationEnd ae) {
      boolean isMultiple = false;
      Multiplicity m = ae.getMultiplicity();
      if (m == null) {
         throw new RuntimeException("Multiplicity not set for association end " + ae.getName());
      } else {
         Enumeration rl = m.getRangeList();
         BigInteger minusOne = BigInteger.ONE.negate();

         while(!isMultiple && rl.hasMoreElements()) {
            MultiplicityRange mpr = (MultiplicityRange)rl.nextElement();
            BigInteger upp = mpr.getUpper();
            if (minusOne.equals(upp) || upp.compareTo(BigInteger.ONE) > 0) {
               isMultiple = true;
            }
         }

         return isMultiple;
      }
   }

   public static boolean isQualified(AssociationEnd ae) {
      return !ae.getCollectionQualifierList().isEmpty();
   }

   public static AssociationEnd getOppositeAssociationEnd(AssociationEnd a) {
      Association as = a.getAssociation();
      Enumeration ae = as.getConnectionList();
      AssociationEnd res = null;

      while(ae.hasMoreElements()) {
         AssociationEnd t = (AssociationEnd)ae.nextElement();
         if (t != a) {
            if (res != null) {
               throw new InternalError("Result is not uniquely determined");
            }

            res = t;
         }
      }

      if (res == null) {
         throw new InternalError("No opposite association end found");
      } else {
         return res;
      }
   }

   public static List getParticipatingAssociations(Classifier c) {
      List result = new ArrayList();
      Iterator assocEnds = c.getCollectionAssociationList().iterator();

      while(assocEnds.hasNext()) {
         AssociationEnd tmp = (AssociationEnd)assocEnds.next();
         result.add(tmp.getAssociation());
      }

      return result;
   }

   public static boolean fromAssociationClass(AssociationEnd ae) {
      AssociationEnd opEnd = getOppositeAssociationEnd(ae);
      return getParticipatingAssociations(ae.getParticipant()).contains(opEnd.getParticipant());
   }

   public static boolean towardsAssociationClass(AssociationEnd ae) {
      AssociationEnd opEnd = getOppositeAssociationEnd(ae);
      Classifier opParticipant = opEnd.getParticipant();
      Classifier thisParticipant = ae.getParticipant();
      return getParticipatingAssociations(opParticipant).contains(thisParticipant);
   }

   public static AssociationEnd getOppositeAssociationEnd(Classifier thisParticipant, Association a) {
      Iterator itConnections = a.getCollectionConnectionList().iterator();
      AssociationEnd result = null;

      while(itConnections.hasNext() && result == null) {
         result = (AssociationEnd)itConnections.next();
         if (result.getParticipant() == thisParticipant) {
            result = null;
         }
      }

      if (result == null) {
         throw new RuntimeException("Association end not found as opposite for " + thisParticipant.getName());
      } else {
         return result;
      }
   }

   public static AssociationEnd getAssociationEnd(Association a, String name) {
      AssociationEnd result = null;
      Iterator itc = a.getCollectionConnectionList().iterator();

      while(itc.hasNext() && result == null) {
         AssociationEnd con = (AssociationEnd)itc.next();
         if (name.equals(con.getName())) {
            result = con;
         }
      }

      return result;
   }

   public static String getFullSignature(BehavioralFeature bf, boolean paramNames) {
      StringBuffer result = new StringBuffer();
      if (bf == null) {
         return null;
      } else if (bf.getName() == null) {
         return result.append("Unnamed Operation").toString();
      } else {
         result = (new StringBuffer(bf.getName())).append('(');
         Iterator params = bf.getCollectionParameterList().iterator();
         String returnType = null;
         boolean firstPassed = false;

         while(params.hasNext()) {
            Parameter cp = (Parameter)params.next();
            if (cp.getKind() == 3) {
               returnType = cp.getType().getName();
            } else {
               if (returnType == null && firstPassed) {
                  result.append(", ");
               }

               firstPassed = true;
               if (paramNames) {
                  result.append(cp.getName()).append(':');
               }

               result.append(cp.getType().getName());
               if (params.hasNext() && returnType != null) {
                  result.append(", ");
               }
            }
         }

         result.append(')');
         if (returnType != null) {
            result.append(':').append(returnType);
         }

         return result.toString();
      }
   }

   public static String getFullyQualifiedName(ModelElement elem) {
      if (elem instanceof Operation) {
         return getFullyQualifiedName((Operation)elem);
      } else {
         String result = elem.getName();
         Namespace nms = elem.directGetNamespace();

         for(Namespace temp = null; nms != null; nms = temp) {
            temp = nms.directGetNamespace();
            if (temp != null) {
               result = nms.getName() + "::" + result;
            }
         }

         if (result == null) {
            result = "";
         }

         return result;
      }
   }

   public static String getFullyQualifiedName(Operation op) {
      StringBuffer bf = new StringBuffer();
      String ownerName = op.getOwner() != null ? getFullyQualifiedName((ModelElement)op.getOwner()) : "";
      bf.append(ownerName);
      if (!"".equals(ownerName)) {
         bf.append("::");
      }

      bf.append(getFullSignature(op, false));
      return bf.toString();
   }
}
