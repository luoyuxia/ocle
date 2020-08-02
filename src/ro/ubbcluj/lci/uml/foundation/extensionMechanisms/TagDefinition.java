package ro.ubbcluj.lci.uml.foundation.extensionMechanisms;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface TagDefinition extends ModelElement {
   String getTagType();

   void setTagType(String var1);

   Multiplicity getMultiplicity();

   void setMultiplicity(Multiplicity var1);

   Stereotype getOwner();

   void setOwner(Stereotype var1);

   Enumeration getTypedValueList();

   Set getCollectionTypedValueList();

   void addTypedValue(TaggedValue var1);

   void removeTypedValue(TaggedValue var1);
}
