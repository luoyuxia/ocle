package ro.ubbcluj.lci.uml.foundation.dataTypes;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.Element;

public interface Multiplicity extends Element {
   Enumeration getRangeList();

   Set getCollectionRangeList();

   void addRange(MultiplicityRange var1);

   void removeRange(MultiplicityRange var1);
}
