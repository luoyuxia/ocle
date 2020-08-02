package ro.ubbcluj.lci.gui.tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import javax.swing.JOptionPane;
import ro.ubbcluj.lci.errors.CompilationErrorMessage;
import ro.ubbcluj.lci.gui.Actions.CompilerInvoker;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.ocl.ExceptionCompiler;
import ro.ubbcluj.lci.ocl.OclCompiler;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.nodes.constraint;
import ro.ubbcluj.lci.ocl.nodes.formalParameterList;
import ro.ubbcluj.lci.ocl.nodes.letExpression;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.uml.foundation.core.StructuralFeature;

public class FeatureLocator {
   private static FeatureLocator locator = null;
   private OclNode root;
   private HashMap definitionCache;

   public FeatureLocator() {
   }

   public static FeatureLocator getLocator() {
      if (locator == null) {
         locator = new FeatureLocator();
         URL tmpUrl = (Integer.class).getResource("/.");
         File tmpFile = new File(tmpUrl.getFile());
         tmpFile = tmpFile.getParentFile();
         File oclSpecFile = new File(new File(tmpFile, "metamodel"), "UML_AO.ocl");
         locator.initialize(new String[]{oclSpecFile.getAbsolutePath()});
      }

      return locator;
   }

   private void initialize(String[] specification_files) {
      char[][] data = (char[][])null;
      OclCompiler compiler = new OclCompiler();

      try {
         data = CompilerInvoker.getCompileData(specification_files);
         compiler.setModel(GRepository.getInstance().getMetamodel().getModel(), true);
         compiler.compile(data, specification_files);
      } catch (IOException var6) {
         this.root = null;
         this.displayWarning("Could not read specification file(s).");
      } catch (ExceptionCompiler var7) {
         CompilationErrorMessage errMsg = new CompilationErrorMessage(var7.getFilename(), var7.getStart(), var7.getStop(), var7.getColumn(), var7.getRow(), var7.getMessage());
         this.displayWarning(var7.getFilename() + ":" + errMsg.getDescription());
      }

      this.root = compiler.getRoot();
      this.definitionCache = new HashMap();
      OclCompiler.cleanup();
   }

   public OclNode getDefinition(Feature f) {
      OclNode ocl = (OclNode)this.definitionCache.get(f);
      if (ocl == null) {
         ocl = this.getDefinition2(f);
         if (ocl != null) {
            this.definitionCache.put(f, ocl);
         }
      }

      return ocl;
   }

   private static boolean matchName(letExpression le, Feature f) {
      String fName = f.getName() + "S";
      return fName.equals(le.getChild(1).getValueAsString());
   }

   private static Classifier getContext(letExpression le) {
      return le.getParent().getChild(0).getChild(1).type.type.classifier;
   }

   private static boolean matchSignature(letExpression le, Feature f) {
      Classifier type = getType(le);
      if (!(f instanceof BehavioralFeature)) {
         StructuralFeature sf = (StructuralFeature)f;
         if (type != sf.getType()) {
            return false;
         } else {
            return !(le.getChild(3) instanceof formalParameterList);
         }
      } else {
         BehavioralFeature bf = (BehavioralFeature)f;
         Collection params = bf.getCollectionParameterList();
         Iterator it = params.iterator();
         if (le.getChild(3) instanceof formalParameterList) {
            OclNode fpl = le.getChild(3);
            int i = 2;

            while(it.hasNext()) {
               Parameter p = (Parameter)it.next();
               if (p.getKind() != 3) {
                  if (i >= fpl.getChildCount()) {
                     return false;
                  }

                  OclNode current = fpl.getChild(i);
                  if (p.getType() != current.type.type.classifier) {
                     return false;
                  }

                  i += 4;
               }
            }
         } else if (params.size() != 1) {
            return false;
         }

         return true;
      }
   }

   private OclNode getDefinition2(Feature f) {
      Stack nodes = new Stack();
      Classifier context = f.getOwner();
      nodes.push(this.root);

      Classifier oclContext;
      letExpression le;
      do {
         while(true) {
            OclNode c;
            do {
               if (nodes.isEmpty()) {
                  return null;
               }

               c = (OclNode)nodes.pop();
            } while(c == null);

            if (c instanceof letExpression && c.getParent() instanceof constraint) {
               le = (letExpression)c;
               oclContext = getContext(le);
               break;
            }

            for(int i = 0; i < c.getChildCount(); ++i) {
               nodes.push(c.getChild(i));
            }
         }
      } while(context != oclContext || !matchName(le, f) || !matchSignature(le, f));

      return le.getChild(1);
   }

   private static Classifier getType(letExpression le) {
      String s = le.getChild(2).getValueAsString();
      byte tss;
      if ("(".equals(s)) {
         if (le.getChild(3) instanceof formalParameterList) {
            tss = 5;
         } else {
            tss = 4;
         }
      } else {
         tss = 2;
      }

      return le.getChild(tss + 1).type.type.classifier;
   }

   private void displayWarning(String w) {
      JOptionPane.showMessageDialog(GApplication.frame, w, "Warning", 2);
   }
}
