package ro.ubbcluj.lci.ocl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.tools.ProgressBar;
import ro.ubbcluj.lci.ocl.gui.IntArray;
import ro.ubbcluj.lci.uml.foundation.core.AttributeImpl;
import ro.ubbcluj.lci.uml.foundation.core.ClassImpl;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataTypeImpl;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnershipImpl;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizationImpl;
import ro.ubbcluj.lci.uml.foundation.core.InterfaceImpl;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.OperationImpl;
import ro.ubbcluj.lci.uml.foundation.core.ParameterImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.StereotypeImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValueImpl;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.ModelImpl;
import ro.ubbcluj.lci.uml.modelManagement.PackageImpl;

public class ImportJar {
   private static String jarname = null;
   private static Model model = null;
   private static boolean completing = false;
   private static final int CLASS = 1;
   private static final int INTERFACE = 2;
   private static final int DATATYPE = 3;
   private static final int REFERENCE = 4;
   private static HashMap classCache = new HashMap();
   private static HashMap packageCache = new HashMap();
   StereotypeImpl realize = new StereotypeImpl();
   private static ImportJar.PoolItem[] pool = null;
   private static byte[] buf = null;
   private static int pos = 0;
   private static final int CONSTANT_CLASS = 7;
   private static final int CONSTANT_FIELDREF = 9;
   private static final int CONSTANT_METHODREF = 10;
   private static final int CONSTANT_INTERFACEMETHODREF = 11;
   private static final int CONSTANT_INTEGER = 3;
   private static final int CONSTANT_FLOAT = 4;
   private static final int CONSTANT_LONG = 5;
   private static final int CONSTANT_DOUBLE = 6;
   private static final int CONSTANT_NAMEANDTYPE = 12;
   private static final int CONSTANT_UTF8 = 1;
   private static final int CONSTANT_STRING = 8;
   private static final int ACC_PUBLIC = 1;
   private static final int ACC_PRIVATE = 2;
   private static final int ACC_PROTECTED = 4;
   private static final int ACC_STATIC = 8;
   private static final int ACC_FINAL = 16;
   private static final int ACC_INTERFACE = 512;
   private static final int ACC_ABSTRACT = 1024;
   private static int classes = 0;
   private static int packages = 0;
   private static int attributes = 0;
   private static int operations = 0;
   private static int hits = 0;
   private static int misses = 0;

   public ImportJar() {
   }

   public static void importJar(String filename, Model model, boolean full) {
      try {
         jarname = filename;
         JarFile jar = new JarFile(filename);
         int n = jar.size();
         int i = 0;
         int percent = 0;
         ProgressBar pb = ProgressBar.getProgressBar(GApplication.frame);
         pb.startProgress("Processing JAR ...");
         pb.setColor(ProgressBar.OPEN_COLOR);
         Enumeration en = jar.entries();

         while(en.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)en.nextElement();
            ++i;
            if (i * 100 / n != percent) {
               percent = i * 100 / n;
               pb.progressValueChanged(percent, (Object)null);
               Thread.yield();
            }

            if (entry.getName().endsWith(".class")) {
               processClass(jar.getInputStream(entry), model, full);
            }
         }

         pb.stopProgress();
      } catch (IOException var13) {
         var13.printStackTrace();
      } finally {
         cleanup();
      }

   }

   public static void importClass(String filename, Model model) {
      try {
         processClass(new FileInputStream(filename), model, true);
      } catch (IOException var6) {
         var6.printStackTrace();
      } finally {
         cleanup();
      }

   }

   public static void importJarClass(String filename, String classEntry, Model model) {
      try {
         JarFile jar = new JarFile(filename);
         ZipEntry entry = jar.getEntry(classEntry);
         processClass(jar.getInputStream(entry), model, true);
      } catch (IOException var8) {
         var8.printStackTrace();
      } finally {
         cleanup();
      }

   }

   public static void importJarClass(String filename, Classifier cls) {
      String fullname = ".class";
      boolean first = true;

      Object ns;
      for(ns = cls; ns != null && !(ns instanceof ModelImpl); first = false) {
         fullname = ((Namespace)ns).getName() + (first ? "" : "/") + fullname;
         ns = ((Namespace)ns).getNamespace().getNamespace();
      }

      if (ns != null) {
         completing = true;
         importJarClass(filename, fullname, (Model)ns);
         completing = false;
      }
   }

   private static void cleanup() {
      classCache.clear();
      packageCache.clear();
   }

   private static Classifier addClass(String cls, int type) {
      Classifier existent = (Classifier)classCache.get(cls);
      Object ns;
      String className;
      int i;
      if (existent == null) {
         ++misses;
         String[] parts = cls.split("/");
         ns = model;
         String path = "";

         for(i = 0; i < parts.length - 1; ++i) {
            path = path + parts[i] + "/";
            ns = addPackage((Namespace)ns, parts[i], path);
         }

         className = parts[parts.length - 1];
         StringTokenizer tok = new StringTokenizer(cls, "$");

         for(path = tok.nextToken(); tok.hasMoreTokens(); path = path + '/' + className) {
            className = tok.nextToken();
            ns = addClass(path, 4);
         }

         Enumeration en = ((Namespace)ns).directGetOwnedElementList();

         while(en.hasMoreElements()) {
            ModelElement elem = (ModelElement)en.nextElement();
            if (elem instanceof Classifier && elem.getName().equals(className)) {
               existent = (Classifier)elem;
               break;
            }
         }
      } else {
         ++hits;
         ns = existent.getNamespace().getNamespace();
         className = existent.getName();
      }

      if (existent != null) {
         if (type == 4) {
            return existent;
         }

         if (type == 2 && existent instanceof InterfaceImpl) {
            return existent;
         }

         if (type == 1 && existent instanceof ClassImpl) {
            return existent;
         }

         if (type == 3 && existent instanceof DataTypeImpl) {
            return existent;
         }
      }

      ++classes;
      Object cs;
      if (type == 2) {
         cs = new InterfaceImpl();
      } else if (type == 1) {
         cs = new ClassImpl();
      } else if (type == 3) {
         cs = new DataTypeImpl();
      } else {
         cs = new ClassImpl();
      }

      ((Classifier)cs).setName(className);
      if (existent == null) {
         ElementOwnershipImpl eo = new ElementOwnershipImpl();
         eo.setNamespace((Namespace)ns);
         eo.setOwnedElement((ModelElement)cs);
         eo.setVisibility(3);
      } else {
         existent.getNamespace().setOwnedElement((ModelElement)cs);
         Object[] objs = existent.getCollectionSpecializationList().toArray();

         for(i = 0; i < objs.length; ++i) {
            ((GeneralizationImpl)objs[i]).setParent((GeneralizableElement)cs);
         }

         objs = existent.getCollectionTypedFeatureList().toArray();

         for(i = 0; i < objs.length; ++i) {
            ((AttributeImpl)objs[i]).setType((Classifier)cs);
         }

         objs = existent.getCollectionTypedParameterList().toArray();

         for(i = 0; i < objs.length; ++i) {
            ((ParameterImpl)objs[i]).setType((Classifier)cs);
         }
      }

      classCache.put(cls, cs);
      return (Classifier)cs;
   }

   private static void addGeneralization(Classifier superclass, Classifier subclass, boolean isGeneralization) {
      GeneralizationImpl gene = new GeneralizationImpl();
      gene.setChild(subclass);
      gene.setParent(superclass);
   }

   private static Namespace addPackage(Namespace parent, String pkname, String fullpath) {
      Namespace cached = (Namespace)packageCache.get(fullpath);
      if (cached != null) {
         return cached;
      } else {
         Enumeration en = parent.directGetOwnedElementList();

         ModelElement elem;
         do {
            if (!en.hasMoreElements()) {
               ++packages;
               PackageImpl pk = new PackageImpl();
               pk.setName(pkname);
               ElementOwnershipImpl eo = new ElementOwnershipImpl();
               eo.setNamespace(parent);
               eo.setOwnedElement(pk);
               eo.setVisibility(3);
               packageCache.put(fullpath, pk);
               return pk;
            }

            elem = (ModelElement)en.nextElement();
         } while(!(elem instanceof Namespace) || !elem.getName().equals(pkname));

         return (Namespace)elem;
      }
   }

   private static Feature addProperty(Classifier cls, String name, ArrayList params, String[] paramNames, boolean isAttribute) {
      if (isAttribute) {
         AttributeImpl attr = new AttributeImpl();
         attr.setName(name);
         attr.setType((Classifier)params.get(0));
         cls.addFeature(attr);
         ++attributes;
         return attr;
      } else {
         OperationImpl op = new OperationImpl();
         if (name.equals("<init>")) {
            name = cls.getName();
         }

         op.setName(name);
         int i = 0;

         for(Iterator it = params.iterator(); it.hasNext(); ++i) {
            ParameterImpl param = new ParameterImpl();
            param.setType((Classifier)it.next());
            if (it.hasNext()) {
               param.setName(paramNames[i] != null ? paramNames[i] : "p" + (i + 1));
               param.setKind(2);
            } else {
               param.setName("return");
               param.setKind(3);
            }

            op.addParameter(param);
         }

         cls.addFeature(op);
         ++operations;
         return op;
      }
   }

   private static void processClass(InputStream is, Model model, boolean full) throws IOException, ClassFormatError {
      ImportJar.model = model;
      buf = new byte[is.available()];

      int accessFlags;
      for(pos = 0; pos < buf.length; pos += accessFlags) {
         accessFlags = is.read(buf, pos, buf.length - pos);
         if (accessFlags < 0) {
            break;
         }
      }

      pos = 0;
      if (read() == 202 && read() == 254 && read() == 186 && read() == 190) {
         skip(4);
         readPool();
         accessFlags = readWord();
         int thisClassIndex = readWord();
         checkIndex(thisClassIndex);
         ImportJar.PoolItem thisClassPI = pool[thisClassIndex];
         checkIndex(thisClassPI.index1);
         String thisClassName = pool[thisClassPI.index1].value;
         boolean isInterface = (accessFlags & 512) != 0;
         Classifier thisclass = addClass(thisClassName, isInterface ? 2 : 1);
         thisclass.setAbstract((accessFlags & 1024) != 0);
         thisclass.setLeaf((accessFlags & 16) != 0);
         thisclass.getNamespace().setVisibility((accessFlags & 1) != 0 ? 3 : 1);
         int superclassIndex = readWord();
         if (!completing && superclassIndex > 0) {
            checkIndex(superclassIndex);
            ImportJar.PoolItem superclassPI = pool[superclassIndex];
            checkIndex(superclassPI.index1);
            String superclassName = pool[superclassPI.index1].value;
            Classifier superclass = addClass(superclassName, isInterface && !superclassName.equals("java/lang/Object") ? 2 : 1);
            addGeneralization(superclass, thisclass, true);
         }

         int interfaceCount = readWord();

         for(int i = 0; i < interfaceCount; ++i) {
            int superinterfaceIndex = readWord();
            if (!completing) {
               checkIndex(superinterfaceIndex);
               ImportJar.PoolItem superinterfacePI = pool[superinterfaceIndex];
               checkIndex(superinterfacePI.index1);
               String superinterfaceName = pool[superinterfacePI.index1].value;
               Classifier superinterface = addClass(superinterfaceName, 2);
               addGeneralization(superinterface, thisclass, false);
            }
         }

         if (full) {
            readFieldsAndMethods(thisclass);
         } else if (jarname != null) {
            TaggedValue tv = new TaggedValueImpl();
            tv.setName("jar");
            tv.setDataValue(jarname);
            thisclass.addTaggedValue(tv);
         }

         if (Character.isDigit(thisClassName.charAt(0))) {
            ElementOwnership eo = thisclass.getNamespace();
            eo.setNamespace((Namespace)null);
            eo.setOwnedElement((ModelElement)null);
         }

      } else {
         throw new ClassFormatError("Incorrect magic number");
      }
   }

   private static int readWord() throws ClassFormatError {
      if (pos + 1 >= buf.length) {
         throw new ClassFormatError("premature end of file");
      } else {
         int b1 = buf[pos++];
         int b2 = buf[pos++];
         if (b1 < 0) {
            b1 += 256;
         }

         if (b2 < 0) {
            b2 += 256;
         }

         return (b1 << 8) + b2;
      }
   }

   private static int read() throws ClassFormatError {
      if (pos >= buf.length) {
         throw new ClassFormatError("premature end of file");
      } else {
         int b = buf[pos++];
         if (b < 0) {
            b += 256;
         }

         return b;
      }
   }

   private static void checkIndex(int index) throws ClassFormatError {
      if (index < 1 || index >= pool.length) {
         throw new ClassFormatError("pool index out of range");
      }
   }

   private static void skip(int bytes) throws ClassFormatError {
      pos += bytes;
      if (pos > buf.length) {
         throw new ClassFormatError("premature end of file");
      }
   }

   private static void readPool() throws ClassFormatError {
      pool = new ImportJar.PoolItem[readWord()];

      for(int i = 1; i < pool.length; ++i) {
         pool[i] = new ImportJar.PoolItem();
         pool[i].tag = (byte)read();
         switch(pool[i].tag) {
         case 1:
            int len = readWord();
            byte[] text = new byte[len];

            for(int j = 0; j < len; ++j) {
               int b = (byte)read();
               if (b == -1) {
                  throw new ClassFormatError("Invalid UTF8 pool item " + i);
               }

               text[j] = (byte)b;
            }

            pool[i].value = new String(text);
            break;
         case 2:
         default:
            throw new ClassFormatError("Invalid pool tag");
         case 3:
         case 4:
            skip(4);
            break;
         case 5:
         case 6:
            skip(8);
            pool[i + 1] = pool[i];
            ++i;
            break;
         case 7:
         case 8:
            pool[i].index1 = (short)readWord();
            break;
         case 9:
         case 10:
         case 11:
         case 12:
            pool[i].index1 = (short)readWord();
            pool[i].index2 = (short)readWord();
         }
      }

   }

   private static void readFieldsAndMethods(Classifier cls) throws ClassFormatError {
      for(int k = 0; k < 2; ++k) {
         int fieldOrMethodCount = readWord();

         for(int i = 0; i < fieldOrMethodCount; ++i) {
            int flags = readWord();
            int nameIndex = readWord();
            checkIndex(nameIndex);
            String name = pool[nameIndex].value;
            int sigIndex = readWord();
            checkIndex(sigIndex);
            String sig = pool[sigIndex].value;
            ArrayList params = new ArrayList();
            IntArray paramFrameIndices = new IntArray();
            String dims = "";
            int frameIndex = 0;

            int ii;
            for(int j = 0; j < sig.length(); ++j) {
               String paramName = null;
               int type = 3;
               ii = frameIndex++;
               switch(sig.charAt(j)) {
               case 'B':
                  paramName = "byte";
                  break;
               case 'C':
                  paramName = "char";
                  break;
               case 'D':
                  paramName = "double";
                  ++frameIndex;
               case 'E':
               case 'G':
               case 'H':
               case 'K':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'T':
               case 'U':
               case 'W':
               case 'X':
               case 'Y':
               default:
                  break;
               case 'F':
                  paramName = "float";
                  break;
               case 'I':
                  paramName = "int";
                  break;
               case 'J':
                  paramName = "long";
                  ++frameIndex;
                  break;
               case 'L':
                  int jj = sig.indexOf(59, j);
                  paramName = sig.substring(j + 1, jj);
                  j = jj;
                  type = 4;
                  break;
               case 'S':
                  paramName = "short";
                  break;
               case 'V':
                  paramName = "void";
                  break;
               case 'Z':
                  paramName = "boolean";
                  break;
               case '[':
                  dims = dims + "[]";
               }

               if (paramName != null) {
                  params.add(addClass(paramName + dims, type));
                  paramFrameIndices.add(ii);
                  dims = "";
               }
            }

            String[] paramNames = new String[frameIndex];
            int attrCount = readWord();

            for(int j = 0; j < attrCount; ++j) {
               ii = readWord();
               checkIndex(ii);
               String atName = pool[ii].value;
               int length = (readWord() << 16) + readWord();
               if (!atName.equals("Code")) {
                  skip(length);
               } else {
                  skip(4);
                  length = (readWord() << 16) + readWord();
                  skip(length);
                  skip(readWord() * 8);
                  int attrCount2 = readWord();

                  for( ii = 0; ii < attrCount2; ++ii) {
                     ii = readWord();
                     checkIndex(ii);
                     atName = pool[ii].value;
                     length = (readWord() << 16) + readWord();
                     if (!atName.equals("LocalVariableTable")) {
                        skip(length);
                     } else {
                        int varCount = readWord();

                        for(int l = 0; l < varCount; ++l) {
                           skip(4);
                           int varNameIndex = readWord();
                           checkIndex(varNameIndex);
                           String varName = pool[varNameIndex].value;
                           skip(2);
                           int index = readWord();
                           if (index < paramNames.length) {
                              paramNames[index] = varName;
                           }
                        }
                     }
                  }
               }
            }

            if (!name.equals("<clinit>") && name.indexOf("$") < 0) {
               String[] names = new String[params.size()];

               for(ii = 0; ii < names.length; ++ii) {
                  frameIndex = paramFrameIndices.get(ii);
                  names[ii] = paramNames[frameIndex];
               }

               Feature feat = addProperty(cls, name, params, names, k == 0);
               feat.setVisibility(1);
               if ((flags & 1) != 0) {
                  feat.setVisibility(3);
               }

               if ((flags & 2) != 0) {
                  feat.setVisibility(0);
               }

               if ((flags & 4) != 0) {
                  feat.setVisibility(2);
               }

               feat.setOwnerScope((flags & 8) != 0 ? 0 : 1);
               if (k == 1) {
                  ((Operation)feat).setLeaf((flags & 16) != 0);
               }

               if (k == 1) {
                  ((Operation)feat).setAbstract((flags & 1024) != 0);
               }
            }
         }
      }

   }

   private static void rec(ModelElement elem, int k) {
      String s = elem.getClass().getName();
      s.substring(s.lastIndexOf(46) + 1);
      Enumeration en;
      if (elem instanceof Classifier) {
         en = ((Classifier)elem).getFeatureList();

         while(true) {
            Feature feat;
            do {
               if (!en.hasMoreElements()) {
                  return;
               }

               feat = (Feature)en.nextElement();
            } while(feat instanceof AttributeImpl);

            Object[] params = ((OperationImpl)feat).getCollectionParameterList().toArray();

            for(int i = 0; i < params.length - 1; ++i) {
               ParameterImpl var7 = (ParameterImpl)params[i];
            }
         }
      } else if (elem instanceof Namespace) {
         en = ((Namespace)elem).directGetOwnedElementList();

         while(en.hasMoreElements()) {
            ModelElement child = (ModelElement)en.nextElement();
            rec(child, k + 3);
         }
      }

   }

   private static class PoolItem {
      private String value;
      private byte tag;
      private short index1;
      private short index2;

      private PoolItem() {
         this.value = "";
         this.tag = -1;
         this.index1 = -1;
         this.index2 = -1;
      }
   }
}
