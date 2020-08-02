package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface AssociationClass extends Association, Class {
   Set allConnections();

   Set allOppositeAssociationEnds();
}
