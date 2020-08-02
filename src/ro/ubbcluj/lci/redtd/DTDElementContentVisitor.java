package ro.ubbcluj.lci.redtd;

import ro.ubbcluj.lci.redtd.dtdmetamodel.ChoiceContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.LeafContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.MixedContent;
import ro.ubbcluj.lci.redtd.dtdmetamodel.SequenceContent;

public interface DTDElementContentVisitor {
   Object visitMixedContent(MixedContent var1);

   Object visitLeafContent(LeafContent var1);

   Object visitSequenceContent(SequenceContent var1);

   Object visitChoiceContent(ChoiceContent var1);
}
