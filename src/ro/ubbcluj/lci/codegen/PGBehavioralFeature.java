package ro.ubbcluj.lci.codegen;

import java.util.ArrayList;
import java.util.List;

public abstract class PGBehavioralFeature extends PGElement {
   protected String returnTypeName;
   protected List parameters;

   public PGBehavioralFeature(PGClass parent) {
      super(parent);
   }

   public final void setReturnTypeName(String returnTypeName) {
      this.returnTypeName = returnTypeName;
   }

   public final String getReturnTypeName() {
      return this.returnTypeName;
   }

   public final void addParameter(PGParameter parameter) {
      if (this.parameters == null) {
         this.parameters = new ArrayList();
      }

      this.parameters.add(parameter);
   }
}
