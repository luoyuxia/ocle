package ro.ubbcluj.lci.redtd;

import java.util.Collection;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;

public interface ModelBuilder {
   Classifier buildClass(String var1);

   Attribute buildAttribute(Classifier var1, String var2, DataType var3, String var4, boolean var5, boolean var6);

   Association buildAssociation(Classifier var1, Classifier var2, Multiplicity var3, TaggedValue var4);

   DataType buildDataType(String var1);

   Enumeration buildEnumeration(Collection var1);

   Multiplicity buildMultiplicity(int var1);

   TaggedValue buildTaggedValue(String var1, String var2);

   void attachStereotype(ModelElement var1, String var2);

   void detachStereotype(ModelElement var1, String var2);

   void buildAnyContent();

   Instance buildObject(Classifier var1, Collaboration var2);

   AttributeLink buildAttributeLink(Instance var1, String var2, String var3);

   Link buildLink(Instance var1, Instance var2, Association var3);

   Collaboration buildCollaboration(String var1);
}
