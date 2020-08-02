package ro.ubbcluj.lci.uml.foundation.dataTypes;

import java.math.BigInteger;
import ro.ubbcluj.lci.uml.foundation.core.Element;

public interface MultiplicityRange extends Element {
   int getLower();

   void setLower(int var1);

   BigInteger getUpper();

   void setUpper(BigInteger var1);

   Multiplicity getMultiplicity();

   void setMultiplicity(Multiplicity var1);
}
