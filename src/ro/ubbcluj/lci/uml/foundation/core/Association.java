package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationRole;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;

public interface Association extends Relationship, GeneralizableElement {
   java.util.Enumeration getAssociationRoleList();

   Set getCollectionAssociationRoleList();

   void addAssociationRole(AssociationRole var1);

   void removeAssociationRole(AssociationRole var1);

   java.util.Enumeration getConnectionList();

   OrderedSet getCollectionConnectionList();

   void addConnection(AssociationEnd var1);

   void removeConnection(AssociationEnd var1);

   java.util.Enumeration getLinkList();

   Set getCollectionLinkList();

   void addLink(Link var1);

   void removeLink(Link var1);

   Set allConnections();
}
