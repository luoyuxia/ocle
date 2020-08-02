package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.Integer;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Signal;

public class BehavioralFeatureImpl extends FeatureImpl implements BehavioralFeature {
   protected boolean isQuery;
   protected Set theRaisedSignalList;
   protected OrderedSet theParameterList;

   public BehavioralFeatureImpl() {
   }

   public boolean isQuery() {
      return this.isQuery;
   }

   public void setQuery(boolean isQuery) {
      this.isQuery = isQuery;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isQuery", 0));
      }

   }

   public Set getCollectionRaisedSignalList() {
      return this.theRaisedSignalList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theRaisedSignalList);
   }

   public java.util.Enumeration getRaisedSignalList() {
      return Collections.enumeration(this.getCollectionRaisedSignalList());
   }

   public void addRaisedSignal(Signal arg) {
      if (arg != null) {
         if (this.theRaisedSignalList == null) {
            this.theRaisedSignalList = new LinkedHashSet();
         }

         if (this.theRaisedSignalList.add(arg)) {
            arg.addContext(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "raisedSignal", 1));
            }
         }
      }

   }

   public void removeRaisedSignal(Signal arg) {
      if (this.theRaisedSignalList != null && arg != null && this.theRaisedSignalList.remove(arg)) {
         arg.removeContext(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "raisedSignal", 2));
         }
      }

   }

   public OrderedSet getCollectionParameterList() {
      return this.theParameterList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theParameterList);
   }

   public java.util.Enumeration getParameterList() {
      return Collections.enumeration(this.getCollectionParameterList());
   }

   public void addParameter(Parameter arg) {
      if (arg != null) {
         if (this.theParameterList == null) {
            this.theParameterList = new OrderedSet();
         }

         if (this.theParameterList.add(arg)) {
            arg.setBehavioralFeature(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "parameter", 1));
            }
         }
      }

   }

   public void removeParameter(Parameter arg) {
      if (this.theParameterList != null && arg != null && this.theParameterList.remove(arg)) {
         arg.setBehavioralFeature((BehavioralFeature)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "parameter", 2));
         }
      }

   }

   public boolean hasSame_Param_Size(BehavioralFeature b) {
      List seqParameter = this.getCollectionParameterList();
      int nSize = CollectionUtilities.size((Collection)seqParameter);
      List seqParameter0 = b.getCollectionParameterList();
      int nSize0 = CollectionUtilities.size((Collection)seqParameter0);
      boolean bEquals = nSize == nSize0;
      return bEquals;
   }

   public boolean hasSameSignature(BehavioralFeature bf) {
      String strName = this.getName();
      String strName0 = bf.getName();
      boolean bEquals1 = strName.equals(strName0);
      boolean bIf;
      if (bEquals1) {
         List seqParameter = this.getCollectionParameterList();
         int nSize = CollectionUtilities.size((Collection)seqParameter);
         boolean bEquals = nSize == 0;
         List seqParameter0 = bf.getCollectionParameterList();
         int nSize0 = CollectionUtilities.size((Collection)seqParameter0);
         boolean bEquals0 = nSize0 == 0;
         boolean bAnd = bEquals && bEquals0;
         if (bAnd) {
            bIf = true;
         } else {
            boolean bhasSame_Param_Size = this.hasSame_Param_Size(bf);
            boolean bIf0;
            if (!bhasSame_Param_Size) {
               bIf0 = false;
            } else {
               List seq = CollectionUtilities.newSequence();
               List seqParameter1 = this.getCollectionParameterList();
               int nSize1 = CollectionUtilities.size((Collection)seqParameter1);
               CollectionUtilities.addRange(seq, 1, nSize1);
               boolean bForAll = true;

               boolean bAnd0;
               for(Iterator iter = seq.iterator(); bForAll && iter.hasNext(); bForAll = bAnd0) {
                  int index = ((Integer)iter.next()).asInteger();
                  List seqParameter2 = bf.getCollectionParameterList();
                  Parameter parameterAt = (Parameter)CollectionUtilities.at(seqParameter2, index);
                  Classifier classifierType = parameterAt.getType();
                  List seqParameter3 = this.getCollectionParameterList();
                  Parameter parameterAt0 = (Parameter)CollectionUtilities.at(seqParameter3, index);
                  Classifier classifierType0 = parameterAt0.getType();
                  boolean bEquals2 = classifierType.equals(classifierType0);
                  List seqParameter4 = bf.getCollectionParameterList();
                  Parameter parameterAt1 = (Parameter)CollectionUtilities.at(seqParameter4, index);
                  int nKind = parameterAt1.getKind();
                  List seqParameter5 = this.getCollectionParameterList();
                  Parameter parameterAt2 = (Parameter)CollectionUtilities.at(seqParameter5, index);
                  int nKind0 = parameterAt2.getKind();
                  boolean bEquals3 = nKind == nKind0;
                  bAnd0 = bEquals2 && bEquals3;
               }

               bIf0 = bForAll;
            }

            bIf = bIf0;
         }
      } else {
         bIf = false;
      }

      return bIf;
   }

   public boolean matchesSignature(BehavioralFeature bf) {
      String strName = this.getName();
      String strName0 = bf.getName();
      boolean bEquals = strName.equals(strName0);
      List seqParameter = this.getCollectionParameterList();
      int nSize = CollectionUtilities.size((Collection)seqParameter);
      boolean bEquals0 = nSize == 0;
      List seqParameter0 = bf.getCollectionParameterList();
      int nSize0 = CollectionUtilities.size((Collection)seqParameter0);
      boolean bEquals1 = nSize0 == 0;
      boolean bAnd0 = bEquals0 && bEquals1;
      boolean bIf;
      boolean bIf0;
      if (bAnd0) {
         bIf = true;
      } else {
         boolean bhasSame_Param_Size = this.hasSame_Param_Size(bf);
         if (!bhasSame_Param_Size) {
            bIf0 = false;
         } else {
            List seq = CollectionUtilities.newSequence();
            List seqParameter1 = this.getCollectionParameterList();
            int nSize1 = CollectionUtilities.size((Collection)seqParameter1);
            CollectionUtilities.addRange(seq, 1, nSize1);
            boolean bForAll = true;

            boolean bOr;
            for(Iterator iter = seq.iterator(); bForAll && iter.hasNext(); bForAll = bOr) {
               int index = ((Integer)iter.next()).asInteger();
               List seqParameter2 = bf.getCollectionParameterList();
               Parameter parameterAt = (Parameter)CollectionUtilities.at(seqParameter2, index);
               Classifier classifierType = parameterAt.getType();
               List seqParameter3 = this.getCollectionParameterList();
               Parameter parameterAt0 = (Parameter)CollectionUtilities.at(seqParameter3, index);
               Classifier classifierType0 = parameterAt0.getType();
               boolean bEquals2 = classifierType.equals(classifierType0);
               List seqParameter4 = bf.getCollectionParameterList();
               Parameter parameterAt1 = (Parameter)CollectionUtilities.at(seqParameter4, index);
               int nKind = parameterAt1.getKind();
               List seqParameter5 = this.getCollectionParameterList();
               Parameter parameterAt2 = (Parameter)CollectionUtilities.at(seqParameter5, index);
               int nKind0 = parameterAt2.getKind();
               boolean bEquals3 = nKind == nKind0;
               boolean bAnd1 = bEquals2 && bEquals3;
               List seqParameter6 = bf.getCollectionParameterList();
               Parameter parameterAt3 = (Parameter)CollectionUtilities.at(seqParameter6, index);
               int nKind1 = parameterAt3.getKind();
               boolean bEquals4 = nKind1 == 3;
               List seqParameter7 = this.getCollectionParameterList();
               Parameter parameterAt4 = (Parameter)CollectionUtilities.at(seqParameter7, index);
               int nKind2 = parameterAt4.getKind();
               boolean bEquals5 = nKind2 == 3;
               boolean bAnd2 = bEquals4 && bEquals5;
               bOr = bAnd1 || bAnd2;
            }

            bIf0 = bForAll;
         }

         bIf = bIf0;
      }

      bIf0 = bEquals && bIf;
      return bIf0;
   }

   protected void internalRemove() {
      java.util.Enumeration tmpRaisedSignalEnum = this.getRaisedSignalList();
      ArrayList tmpRaisedSignalList = new ArrayList();

      while(tmpRaisedSignalEnum.hasMoreElements()) {
         tmpRaisedSignalList.add(tmpRaisedSignalEnum.nextElement());
      }

      Iterator it = tmpRaisedSignalList.iterator();

      while(it.hasNext()) {
         Signal tmpRaisedSignal = (Signal)it.next();
         tmpRaisedSignal.removeContext(this);
      }

      java.util.Enumeration tmpParameterEnum = this.getParameterList();
      ArrayList tmpParameterList = new ArrayList();

      while(tmpParameterEnum.hasMoreElements()) {
         tmpParameterList.add(tmpParameterEnum.nextElement());
      }

       it = tmpParameterList.iterator();

      while(it.hasNext()) {
         ((Parameter)it.next()).remove();
      }

      super.internalRemove();
   }
}
