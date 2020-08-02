package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.math.BigInteger;

public interface SynchState extends StateVertex {
   BigInteger getBound();

   void setBound(BigInteger var1);
}
