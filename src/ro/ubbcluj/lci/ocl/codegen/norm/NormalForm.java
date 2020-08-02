package ro.ubbcluj.lci.ocl.codegen.norm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ro.ubbcluj.lci.ocl.OclType;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Operation;

public final class NormalForm {
   private ArrayList constrainedClasses = new ArrayList();
   private ArrayList constrainedOperations = new ArrayList();
   private Map distinctTupleTypes = null;
   private int index = 0;
   private Set allInstancesCandidates = null;

   public NormalForm() {
   }

   public void registerTupleType(OclType tupleType) {
      if (!tupleType.isTuple()) {
         throw new RuntimeException("Non-tuple type passed to registerTupleType()");
      } else {
         if (this.distinctTupleTypes == null) {
            this.distinctTupleTypes = new HashMap();
         }

         if (!this.distinctTupleTypes.containsKey(tupleType)) {
            String typeName = "TupleType" + ++this.index;
            this.distinctTupleTypes.put(tupleType, typeName);
         }

      }
   }

   public String tupleTypeName(OclType type) {
      return (String)this.distinctTupleTypes.get(type);
   }

   public Set getTupleTypes() {
      return this.distinctTupleTypes == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.distinctTupleTypes.keySet());
   }

   public ConstrainedClass getConstrainedClass(Classifier cl) {
      int n = this.constrainedClasses.size();

      ConstrainedClass cc;
      for(int i = 0; i < n; ++i) {
         cc = (ConstrainedClass)this.constrainedClasses.get(i);
         if (cl == cc.getConstrainedClass()) {
            return cc;
         }
      }

      cc = new ConstrainedClass(cl);
      this.constrainedClasses.add(cc);
      return cc;
   }

   public ConstrainedOperation getConstrainedOperation(Operation op) {
      int n = this.constrainedOperations.size();

      ConstrainedOperation co;
      for(int i = 0; i < n; ++i) {
         co = (ConstrainedOperation)this.constrainedOperations.get(i);
         if (op == co.getConstrainedOperation()) {
            return co;
         }
      }

      co = new ConstrainedOperation(op);
      this.constrainedOperations.add(co);
      return co;
   }

   public ConstrainedOperation getFor(Operation op) {
      int n = this.constrainedOperations.size();

      for(int i = 0; i < n; ++i) {
         ConstrainedOperation co = (ConstrainedOperation)this.constrainedOperations.get(i);
         if (op == co.getConstrainedOperation()) {
            return co;
         }
      }

      return null;
   }

   public ConstrainedClass getFor(Classifier op) {
      int n = this.constrainedClasses.size();

      for(int i = 0; i < n; ++i) {
         ConstrainedClass co = (ConstrainedClass)this.constrainedClasses.get(i);
         if (op == co.getConstrainedClass()) {
            return co;
         }
      }

      return null;
   }

   public ConstrainedClass[] getConstrainedClasses() {
      int n = this.constrainedClasses.size();
      ConstrainedClass[] ret = new ConstrainedClass[n];

      for(int i = 0; i < n; ++i) {
         ret[i] = (ConstrainedClass)this.constrainedClasses.get(i);
      }

      return ret;
   }

   public ConstrainedOperation[] getConstrainedOperations() {
      int n = this.constrainedOperations.size();
      ConstrainedOperation[] ret = new ConstrainedOperation[n];

      for(int i = 0; i < n; ++i) {
         ret[i] = (ConstrainedOperation)this.constrainedOperations.get(i);
      }

      return ret;
   }

   public void removeConstrainedOperation(ConstrainedOperation cop) {
      this.constrainedOperations.remove(cop);
   }

   public void removeEmptyConstrainedObjects() {
      clearConstrainedObjectList(this.constrainedClasses);
      clearConstrainedObjectList(this.constrainedOperations);
   }

   private static void clearConstrainedObjectList(List l) {
      List t = new ArrayList();
      Iterator it = l.iterator();

      while(it.hasNext()) {
         Object n = it.next();
         if (((ConstrainedObject)n).getConstraintCount() < 1) {
            t.add(n);
         }
      }

      l.removeAll(t);
   }

   public void registerAllInstancesCandidate(Classifier candidate) {
      if (this.allInstancesCandidates == null) {
         this.allInstancesCandidates = new HashSet();
      }

      this.allInstancesCandidates.add(candidate);
   }

   public boolean isAllInstancesCandidate(Classifier c) {
      return this.allInstancesCandidates == null ? false : this.allInstancesCandidates.contains(c);
   }
}
