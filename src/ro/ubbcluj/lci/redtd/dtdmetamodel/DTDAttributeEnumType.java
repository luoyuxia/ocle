package ro.ubbcluj.lci.redtd.dtdmetamodel;

import java.util.ArrayList;
import java.util.Collection;

public class DTDAttributeEnumType implements DTDAttributeType {
   private ArrayList enumLiterals;

   public DTDAttributeEnumType(ArrayList literals) {
      this.enumLiterals = literals;
   }

   public Collection getLiterals() {
      return this.enumLiterals;
   }
}
