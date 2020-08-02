package ro.ubbcluj.lci.uml.foundation.extensionMechanisms;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface TaggedValue extends ModelElement {
   String getDataValue();

   void setDataValue(String var1);

   ModelElement getModelElement();

   void setModelElement(ModelElement var1);

   Enumeration getReferenceValueList();

   Set getCollectionReferenceValueList();

   void addReferenceValue(ModelElement var1);

   void removeReferenceValue(ModelElement var1);

   TagDefinition getType();

   void setType(TagDefinition var1);
}
