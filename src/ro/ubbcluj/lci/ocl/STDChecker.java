package ro.ubbcluj.lci.ocl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CompositeState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.FinalState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Pseudostate;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SimpleState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateVertex;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Transition;

public class STDChecker {
   StateMachine sm;

   public STDChecker(StateMachine s) {
      this.sm = s;
   }

   public Collection check() {
      Collection result = new ArrayList();
      result.add(new String("Result of checking StateMachine : " + this.sm.getName()));
      Collection col = this.getSimpleStates(this.sm.getTop());
      Enumeration enum = ((CompositeState)this.sm.getTop()).getSubvertexList();
      Collection col2 = new ArrayList();
      boolean finalStateAdded = false;

      while(enum.hasMoreElements()) {
         StateVertex sv = (StateVertex)enum.nextElement();
         if (sv instanceof Pseudostate && ((Pseudostate)sv).getKind() == 3) {
            col2.add(sv);
         }

         if (sv instanceof FinalState && !finalStateAdded) {
            col.add(sv);
            finalStateAdded = true;
         }
      }

      col2.addAll(col);
      StringBuffer simpleStates = new StringBuffer("Simple states=[");
      Iterator iter = col2.iterator();

      while(iter.hasNext()) {
         StateVertex state = (StateVertex)iter.next();
         simpleStates.append(state.getName());
         simpleStates.append(", ");
      }

      simpleStates.setCharAt(simpleStates.length() - 2, ']');
      result.add(new String(simpleStates));
      HashMap hs = new HashMap();
      iter = col2.iterator();

      int i;
      for(i = 0; iter.hasNext(); ++i) {
         hs.put(new Integer(i), iter.next());
      }

      result.add(new String("Transitions:"));
      Transition[][] trans = new Transition[i][i];

      int s;
      int j1;
      int k;
      String str;
      for(int j = 0; j < i; ++j) {
         StateVertex sv = (StateVertex)hs.get(new Integer(j));

         label401:
         for(Object aux = sv; aux != this.sm.getTop(); aux = ((StateVertex)aux).getContainer()) {
            enum = ((StateVertex)aux).getOutgoingList();

            while(true) {
               while(true) {
                  if (!enum.hasMoreElements()) {
                     continue label401;
                  }

                  Transition tr = (Transition)enum.nextElement();
                  StateVertex targ = tr.getTarget();
                  s = -1;

                  for(j1 = 0; j1 < i; ++j1) {
                     if (hs.get(new Integer(j1)) == targ) {
                        s = j1;
                     }
                  }

                  if (s != -1) {
                     trans[s][j] = tr;
                     str = ((StateVertex)hs.get(new Integer(j))).getName();
                     str = str + " to " + ((StateVertex)hs.get(new Integer(s))).getName();
                     str = str + " transition " + tr.getName();
                     result.add(new String("From " + str));
                  } else if (targ instanceof CompositeState) {
                     CompositeState cs = (CompositeState)targ;
                     Collection col3 = this.getFirstSimpleStates(cs);
                     Iterator iter3 = col3.iterator();

                     while(iter3.hasNext()) {
                        StateVertex svt = (StateVertex)iter3.next();
                        int targIdx1 = -1;

                        for(k = 0; k < i; ++k) {
                           if (hs.get(new Integer(k)) == svt) {
                              targIdx1 = k;
                           }
                        }

                        if (targIdx1 != -1) {
                           trans[targIdx1][j] = tr;
                           str = ((StateVertex)hs.get(new Integer(j))).getName();
                           str = str + " to " + ((StateVertex)hs.get(new Integer(targIdx1))).getName();
                           str = str + " transition " + tr.getName();
                           result.add(new String("From " + str));
                        }
                     }
                  }
               }
            }
         }
      }

      int[][] quality = new int[i][i];

      int step;
      for(step = 0; step < i; ++step) {
         for(k = 0; k < i; ++k) {
            if (trans[step][k] != null) {
               quality[step][k] = 1;
            } else {
               quality[step][k] = 0;
            }
         }
      }

      step = 1;

      int j;
      for(boolean modified = true; modified; ++step) {
         modified = false;

         for(j = 0; j < i; ++j) {
            for(j = 0; j < i; ++j) {
               if (quality[j][j] == step) {
                  for(s = 0; s < i; ++s) {
                     if (quality[s][j] != 0 && quality[s][j] == 0) {
                        quality[s][j] = quality[s][j] + quality[j][j];
                        modified = true;
                     }
                  }
               }
            }
         }
      }

      boolean allZero;
      StateVertex sv;
      for(j = 0; j < i; ++j) {
         allZero = true;

         for(s = 0; s < i; ++s) {
            if (quality[s][j] != 0 && s != j) {
               allZero = false;
            }
         }

         if (allZero) {
            sv = (StateVertex)hs.get(new Integer(j));
            if (sv instanceof FinalState) {
               result.add(new String("NOTE:" + sv.getName() + " is a final state."));
            } else {
               result.add(new String("WARNING:" + sv.getName() + " is a (simple) trap state."));
            }
         }
      }

      for(j = 0; j < i; ++j) {
         allZero = true;

         for(s = 0; s < i; ++s) {
            if (quality[j][s] != 0 && s != j) {
               allZero = false;
            }
         }

         if (allZero) {
            sv = (StateVertex)hs.get(new Integer(j));
            if (sv instanceof Pseudostate) {
               if (((Pseudostate)sv).getKind() == 3) {
                  result.add(new String("NOTE:" + sv.getName() + " is an input state."));
               }
            } else {
               result.add(new String("ERROR:" + sv.getName() + " is an isolated state."));
            }
         }
      }

      int[][] groups = new int[i][i];

      int s1;
      for(j = 0; j < i; ++j) {
         if (quality[j][j] != 0) {
            boolean noneZero = true;
            boolean leadToFinal = false;

            for(s1 = 0; s1 < i; ++s1) {
               sv = (StateVertex)hs.get(new Integer(s1));
               groups[s1][j] = quality[s1][j] == 0 ? 0 : 1;
               if (groups[s1][j] == 0 && !(sv instanceof Pseudostate)) {
                  noneZero = false;
               }

               if (sv instanceof FinalState && groups[s1][j] != 0) {
                  leadToFinal = true;
               }
            }

            if (noneZero || leadToFinal) {
               for(s1 = 0; s1 < i; ++s1) {
                  groups[s1][j] = 0;
               }
            }
         }
      }

      for(j = 0; j < i; ++j) {
         s = 0;

         for(j1 = 0; j1 < i; ++j1) {
            s += groups[j1][j];
         }

         if (s > 0) {
            for(j1 = j + 1; j1 < i; ++j1) {
               s1 = 0;
               boolean equals = true;
               boolean left = true;
               boolean right = true;

               for(k = 0; k < i; ++k) {
                  s1 += groups[k][j1];
                  if (groups[k][j] != groups[k][j1]) {
                     equals = false;
                  }

                  if (left && groups[k][j] > groups[k][j1]) {
                     left = false;
                  }

                  if (right && groups[k][j] < groups[k][j1]) {
                     right = false;
                  }
               }

               if (equals) {
                  for(k = 0; k < i; ++k) {
                     groups[k][j1] = 0;
                  }
               }

               if (left && !right && s1 > 0) {
                  for(k = 0; k < i; ++k) {
                     groups[k][j1] = 0;
                  }
               }

               if (right && !left && s1 > 0) {
                  for(k = 0; k < i; ++k) {
                     groups[k][j] = 0;
                  }
               }
            }
         }
      }

      for(j = 0; j < i; ++j) {
         s = 0;

         for(j1 = 0; j1 < i; ++j1) {
            s += groups[j1][j];
         }

         if (s > 1) {
            str = new String("WARNING (Posible trap) : ");

            for(s1 = 0; s1 < i; ++s1) {
               if (groups[s1][j] == 1) {
                  sv = (StateVertex)hs.get(new Integer(s1));
                  str = str + sv.getName() + " ";
               }
            }

            result.add(str);
         }
      }

      return result;
   }

   private Collection getSimpleStates(State container) {
      ArrayList col;
      if (container instanceof SimpleState) {
         col = new ArrayList();
         col.add(container);
         return col;
      } else if (container instanceof CompositeState) {
         col = new ArrayList();
         Enumeration enum = ((CompositeState)container).getSubvertexList();

         while(enum.hasMoreElements()) {
            Object obj = enum.nextElement();
            if (obj instanceof State) {
               State state = (State)obj;
               col.addAll(this.getSimpleStates(state));
            }
         }

         return col;
      } else {
         return new ArrayList();
      }
   }

   private Collection getFirstSimpleStates(CompositeState cs) {
      Collection col = new ArrayList();
      Enumeration enum = cs.getSubvertexList();

      while(true) {
         StateVertex sv;
         do {
            do {
               if (!enum.hasMoreElements()) {
                  return col;
               }

               sv = (StateVertex)enum.nextElement();
            } while(!(sv instanceof Pseudostate));
         } while(((Pseudostate)sv).getKind() != 3);

         Enumeration enum2 = sv.getOutgoingList();

         while(enum2.hasMoreElements()) {
            StateVertex targ = ((Transition)enum2.nextElement()).getTarget();
            if (targ instanceof CompositeState) {
               col.addAll(this.getFirstSimpleStates((CompositeState)targ));
            } else if (targ instanceof SimpleState) {
               col.add(targ);
            }
         }
      }
   }
}
