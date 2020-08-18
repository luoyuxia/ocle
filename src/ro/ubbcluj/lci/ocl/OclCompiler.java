package ro.ubbcluj.lci.ocl;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.eval.ExceptionEvaluate;
import ro.ubbcluj.lci.ocl.eval.OclConstant;
import ro.ubbcluj.lci.ocl.nodes.classifierContext;
import ro.ubbcluj.lci.ocl.nodes.constraint;
import ro.ubbcluj.lci.ocl.nodes.expression;
import ro.ubbcluj.lci.ocl.nodes.freeConstraint;
import ro.ubbcluj.lci.ocl.nodes.oclExpression;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.ModelImpl;

public class OclCompiler {
   private static OclUmlApi emptyUmlapi = null;
   private OclUmlApi umlapi = null;
   private OclNode root;
   private OclParser parser;
   private Vector instances;
   private int evalCount;
   private int evalTotal;
   private int lastPercent;
   private static OclCompiler.ProgressListener progressListener;

   public OclCompiler() {
      this.umlapi = getEmptyUmlApi();
   }

   public OclCompiler(Model model, boolean isMetamodel) {
      this.umlapi = new OclUmlApi(model, isMetamodel);
   }

   public OclNode analyze(char[][] data, String[] filenames, boolean checkSemantics) throws ExceptionTokenizer, ExceptionParser, ExceptionChecker {
      return this.doit(data, filenames, checkSemantics);
   }

   public OclNode compile(char[][] data, String[] filenames) throws ExceptionTokenizer, ExceptionParser, ExceptionChecker {
      return this.doit(data, filenames, true);
   }

   public OclNode analyze(char[] data, boolean checkSemantics) throws ExceptionCompiler {
      return this.doit(new char[][]{data}, new String[]{""}, checkSemantics);
   }

   public OclNode compile(char[] data) throws ExceptionCompiler {
      return this.doit(new char[][]{data}, new String[]{""}, true);
   }

   public Object evaluate(OclNode nod) throws ExceptionEvaluate {
      return this.evaluate(nod, (Object)null);
   }

   public Object evaluate(OclNode nod, Object self) throws ExceptionEvaluate {
      OclNode context = this.getContextNode(nod);
      if (context != null) {
         ((OclConstant)context.evnode).setValue(self);
      }

      return nod.evnode.evaluate();
   }

   private OclNode doit(char[][] data, String[] filenames, boolean checkSemantics) throws ExceptionTokenizer, ExceptionParser, ExceptionChecker {
      if (filenames.length != data.length) {
         throw new ExceptionTokenizer("the length of the array of filenames must be equal to the length of char arrays", 0, 0, 1, 1, "");
      } else {
         this.root = null;
         OclTokenizer[] tokenizers = new OclTokenizer[filenames.length];

         for(int i = 0; i < filenames.length; ++i) {
            tokenizers[i] = new OclTokenizer(data[i], filenames[i]);
         }

         this.parser = new OclParser(tokenizers);
         long tim = System.currentTimeMillis();
         this.parser.parse();
         this.root = this.parser.getRoot();
         if (checkSemantics) {
            this.root.acceptVisitor(new OclChecker(this.umlapi, 1));
            this.root.acceptVisitor(new OclChecker(this.umlapi, 2));
         }

         return this.root;
      }
   }

   public OclNode getRoot() {
      return this.parser == null ? null : this.parser.getRoot();
   }

   public Model getModel() {
      return this.umlapi.getModel();
   }

   public OclUmlApi getUmlApi() {
      return this.umlapi;
   }

   public void setModel(Model mdl, boolean isMetamodel) {
      this.umlapi = mdl == null ? getEmptyUmlApi() : new OclUmlApi(mdl, isMetamodel);
   }

   public void setUserModel(Model umdl) {
      if (OclUtil.usermodel == null || OclUtil.usermodel.getUMLModel() != umdl) {
         OclUtil.usermodel = new OclUserModel(umdl);
      }

   }

   public void setDumpListener(OclCompiler.DumpListener dumpListener) {
      OclUtil.setDumpListener(dumpListener);
   }

   public static void cleanup() {
      OclUtil.cleanup();
      OclClassInfo.cleanup();
   }

   public OclNode getContextNode(OclNode nod) {
      OclNode result;
      for(result = null; nod != null; nod = nod.parent) {
         if (nod instanceof constraint) {
            result = nod.firstChild().getChild(1);
         }
      }

      return result;
   }

   private static OclUmlApi getEmptyUmlApi() {
      Model temp = new ModelImpl();
      temp.setName("");
      emptyUmlapi = new OclUmlApi(temp, false);
      return emptyUmlapi;
   }

   public void evaluateAll(PrintStream fis) {
      this.evalCount = 0;
      this.testRec(this.root, fis, false);
      this.evalTotal = this.evalCount;
      this.evalCount = 0;
      this.testRec(this.root, fis, true);
   }

   private void testRec(OclNode nod, PrintStream fis, boolean isEval) {
      if (nod instanceof classifierContext) {
         if (isEval) {
            fis.print(nod.toString() + " - ");
         }

         try {
            if (this.umlapi.isMetamodel) {
               Class cls = nod.type.type.getJavaClass();
               this.instances = OclUtil.usermodel.getInstances(cls, true);
            } else {
               Classifier csf = nod.type.type.classifier;
               this.instances = OclUserModel.getObjectInstances(csf, true);
            }

            if (isEval) {
               fis.println(this.instances.size() + " instances");
            }
         } catch (ExceptionAny var10) {
            if (isEval) {
               fis.println("cannot get the coresponding Java class");
            }
         }
      } else {
         Iterator it;
         if (nod instanceof oclExpression) {
            if (isEval) {
               fis.println("   " + nod.toString());
               int k = 1;

               for(it = this.instances.iterator(); it.hasNext(); ++k) {
                  Object obj = ((OclUserElement)it.next()).getElement();
                  fis.print("      " + OclUtil.printValue(obj) + " : ");

                  try {
                     Object result = this.evaluate(nod, obj);
                     fis.println(OclUtil.printValue(result));
                  } catch (ExceptionEvaluate var8) {
                     fis.println(var8.getClass().getName() + ": " + var8.getMessage());
                  }

                  ++this.evalCount;
                  this.progress();
               }
            } else {
               this.evalCount += this.instances.size();
            }
         } else if (nod instanceof freeConstraint) {
            ++this.evalCount;
            if (isEval) {
               try {
                  OclNode found = null;
                  it = nod.children.iterator();

                  label67:
                  while(true) {
                     OclNode child;
                     do {
                        if (!it.hasNext()) {
                           fis.print(found.toString() + " = ");
                           String s = OclUtil.printValue(found.evnode.evaluate());
                           fis.println(s);
                           break label67;
                        }

                        child = (OclNode)it.next();
                     } while(!(child instanceof expression) && !(child instanceof oclExpression));

                     found = child;
                  }
               } catch (ExceptionEvaluate var9) {
                  fis.println(var9.getClass().getName() + ": " + var9.getMessage());
               }

               this.progress();
            }
         } else {
             it = nod.children.iterator();

            while(it.hasNext()) {
               OclNode child = (OclNode)it.next();
               this.testRec(child, fis, isEval);
            }
         }
      }

   }

   public static void setProgressListener(OclCompiler.ProgressListener pl) {
      progressListener = pl;
   }

   private void progress() {
      int percent = (int)((long)this.evalCount * 100L / (long)this.evalTotal);
      if (percent != this.lastPercent) {
         if (progressListener != null) {
            progressListener.setPercent(percent);
         } else {
         //   System.out.print(percent + "%" + '\r');
         }

         this.lastPercent = percent;
      }

   }

   public interface ProgressListener {
      void setPercent(int var1);
   }

   public interface DumpListener {
      void write(List var1);
   }
}
