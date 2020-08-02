package ro.ubbcluj.lci.gui.mainframe;

import java.beans.ExceptionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.JScrollPane;
import ro.ubbcluj.lci.errors.BasicErrorMessage;
import ro.ubbcluj.lci.gui.browser.FBrowser;
import ro.ubbcluj.lci.gui.browser.FTreeNode;
import ro.ubbcluj.lci.gui.browser.GBrowser;
import ro.ubbcluj.lci.gui.browser.GTree;
import ro.ubbcluj.lci.gui.browser.BTree.BTreeNode;
import ro.ubbcluj.lci.gui.diagrams.ClassDiagram;
import ro.ubbcluj.lci.gui.diagrams.Diagram;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.gui.diagrams.ObjectDiagram;
import ro.ubbcluj.lci.gui.diagrams.UseCaseDiagram;
import ro.ubbcluj.lci.gxapi.GXClassDiagram;
import ro.ubbcluj.lci.gxapi.GXObjectDiagram;
import ro.ubbcluj.lci.gxapi.GXUseCaseDiagram;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.ModelImpl;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.xmi.XMIDecoder;
import ro.ubbcluj.lci.xmi.XMIEncoder;
import ro.ubbcluj.lci.xmi.XMIErrorException;
import ro.ubbcluj.lci.xmi.XMIException;
import ro.ubbcluj.lci.xmi.XMISerializer;
import ro.ubbcluj.lci.xmi.utils.XMIUtilities;

public class GUMLModel {
   private static final GUMLModel.XMLSerializerErrorListener listener = new GUMLModel.XMLSerializerErrorListener();
   private GAbstractProject ownerProject;
   private boolean readOnly;
   private String modelFileName;
   private Model model;
   protected List views;
   private List diagramViews;
   private int diagramsNo;
   private boolean dirty;

   public GUMLModel() {
      this(false);
   }

   public GUMLModel(boolean ro) {
      this.readOnly = false;
      this.modelFileName = null;
      this.model = null;
      this.views = new ArrayList();
      this.diagramViews = new ArrayList();
      this.readOnly = ro;
   }

   public GUMLModel(String filename) {
      this(filename, false);
   }

   public GUMLModel(String filename, boolean ro) {
      this.readOnly = false;
      this.modelFileName = null;
      this.model = null;
      this.views = new ArrayList();
      this.diagramViews = new ArrayList();
      this.readOnly = ro;
      if (!this.setModel(filename)) {
         this.model = null;
         this.modelFileName = null;
      }

   }

   public GUMLModel(File file) {
      this(file, false);
   }

   public GUMLModel(File file, boolean ro) {
      this(file.getAbsolutePath(), ro);
   }

   public void setReadOnly(boolean ro) {
      this.readOnly = ro;
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public void setOwnerProject(GAbstractProject ownerProject) {
      this.ownerProject = ownerProject;
   }

   public GAbstractProject getOwnerProject() {
      return this.ownerProject;
   }

   public GBrowser getBrowser() {
      return this.views.size() > 0 ? (GBrowser)this.views.get(0) : null;
   }

   public String getModelFileName() {
      return this.modelFileName;
   }

   public void setModelFileName(String newFile) {
      if (newFile != null) {
         this.modelFileName = newFile;
      }

   }

   public Model getModel() {
      return this.model;
   }

   public void addView(GView view) {
      this.views.add(view);
   }

   public boolean removeView(GView view) {
      return this.views.remove(view);
   }

   public List getDiagrams() {
      return this.diagramViews;
   }

   public List getDiagramsFor(Element me) {
      List result = new Vector();

      for(int i = 0; i < this.diagramViews.size(); ++i) {
         if (((Diagram)this.diagramViews.get(i)).contains(me)) {
            result.add(this.diagramViews.get(i));
         }
      }

      return result;
   }

   public boolean addDiagram(Object context, String diagramType) {
      GBrowser browser = this.getBrowser();
      GAbstractDiagram diag = null;
      ModelElement me = (ModelElement)context;
      if (diagramType.equals("Class")) {
         diag = new ClassDiagram(this, me);
      } else if (diagramType.equals("Object")) {
         diag = new ObjectDiagram(this, me);
      } else if (diagramType.equals("UseCase")) {
         diag = new UseCaseDiagram(this, me);
      }

      if (diag == null) {
         return false;
      } else {
         browser.addDiagram((GAbstractDiagram)diag, me, true);
         this.diagramViews.add(diag);
         GMainFrame.getMainFrame().addNewDiagramFrame((GAbstractDiagram)diag);
         this.setDirty(true);
         return true;
      }
   }

   public void removeDiagram(GAbstractDiagram di) {
      GMainFrame.getMainFrame().removeDiagramFrame(di);
      this.diagramViews.remove(di);
      this.setDirty(true);
   }

   public void searchDiagramFor(BTreeNode node) {
      Object o = node.getUserObject();
      if (o instanceof GAbstractDiagram) {
         GAbstractDiagram diag = (GAbstractDiagram)o;
         if (diag.getView() == null) {
            GMainFrame.getMainFrame().addExistingDiagramFrame(diag);
         }

      } else {
         if (o instanceof ModelElement) {
            Object parentObj = null;
            if (node.getParent() != null) {
               parentObj = ((BTreeNode)node.getParent()).getUserObject();
            }

            boolean roleSelect = false;
            if (o instanceof AssociationEnd) {
               roleSelect = true;
            }

            GAbstractDiagram candidateDiag = null;
            boolean foundMatch = false;
            GBrowser browser = this.getBrowser();

            for(int i = 0; i < this.diagramViews.size(); ++i) {
               GAbstractDiagram diag = (GAbstractDiagram)this.diagramViews.get(i);
               if (roleSelect) {
                  Association asoc = ((AssociationEnd)o).getAssociation();
                  if (diag.contains(asoc)) {
                     ((ClassDiagram)diag).highlightRole((AssociationEnd)o);
                     candidateDiag = diag;
                  }
               }

               if (diag.contains(o)) {
                  BTreeNode diagNode = ((GTree)browser.getComponent()).getNodeFor(diag);
                  if (diagNode != null) {
                     Object parentDiag = ((BTreeNode)diagNode.getParent()).getUserObject();
                     if (parentDiag == parentObj && !foundMatch) {
                        candidateDiag = diag;
                        foundMatch = true;
                     } else if (!foundMatch) {
                        candidateDiag = diag;
                     }
                  }
               }
            }

            if (candidateDiag != null) {
               if (candidateDiag.getView() == null) {
                  GMainFrame.getMainFrame().addExistingDiagramFrame(candidateDiag);
               }

               candidateDiag.setSelected(o);
            }
         }

      }
   }

   public ArrayList searchDiagramsFor(BTreeNode node) {
      Object o = node.getUserObject();
      ArrayList result = new ArrayList();
      if (o instanceof GAbstractDiagram) {
         GAbstractDiagram diag = (GAbstractDiagram)o;
         if (diag.getView() == null) {
            GMainFrame.getMainFrame().addExistingDiagramFrame(diag);
         }

         return result;
      } else {
         if (o instanceof ModelElement) {
            boolean roleSelect = false;
            if (o instanceof AssociationEnd) {
               roleSelect = true;
            }

            for(int i = 0; i < this.diagramViews.size(); ++i) {
               GAbstractDiagram diag = (GAbstractDiagram)this.diagramViews.get(i);
               if (roleSelect) {
                  Association asoc = ((AssociationEnd)o).getAssociation();
                  if (diag.contains(asoc)) {
                     ((ClassDiagram)diag).highlightRole((AssociationEnd)o);
                     result.add(diag);
                  }
               } else if (diag.contains(o)) {
                  result.add(diag);
               }
            }
         }

         return result;
      }
   }

   protected void initModelViews() {
      GBrowser browser = null;
      if (this.model != null) {
         browser = new GBrowser(this, GRepository.getInstance().getCurrentBrowserFilter());
      }

      if (this.views.size() > 0 && this.views.get(0) != null) {
         this.views.set(0, browser);
         GMainFrame.getMainFrame().setBrowserTree(new JScrollPane(browser.getComponent()));
      } else {
         this.views.add(browser);
      }

      if (this.views.size() > 1 && this.views.get(1) != null) {
         this.views.set(1, this.diagramViews);
      } else {
         this.views.add(this.diagramViews);
      }

   }

   private boolean setModel(String fileName) {
      if (fileName == null) {
         this.modelFileName = null;
         this.model = ModelFactory.createNewModel();
         this.initModelViews();
         this.setDirty(true);
         return true;
      } else {
         this.model = null;
         this.modelFileName = fileName;
         File mFile = null;
         File[] diagramFiles = null;
         File tempDir = null;
         boolean removeTempDir = false;
         if (this.modelFileName.endsWith(".xml.zip")) {
            File ocleTempDir = new File(new File(System.getProperty("user.home")), GApplication.APP_TEMP_DIR);
            if (!ocleTempDir.exists() && !ocleTempDir.mkdirs()) {
               throw new RuntimeException("Could not create required temporary files");
            }

            tempDir = new File(ocleTempDir, String.valueOf(System.currentTimeMillis()));
            tempDir.mkdir();
            this.unzipFile(new File(fileName), tempDir);
            File[] files = tempDir.listFiles();

            for(int i = 0; i < files.length; ++i) {
               if (files[i].isDirectory()) {
                  diagramFiles = files[i].listFiles();
               } else {
                  mFile = files[i];
               }
            }

            removeTempDir = true;
         } else {
            mFile = new File(fileName);
            tempDir = (new File(fileName)).getParentFile();
         }

         try {
            this.loadModelAndDiagrams(mFile, diagramFiles, tempDir);
         } catch (Exception var9) {
            var9.printStackTrace();
            String s = var9.getMessage();
            GMainFrame.getMainFrame().updateMessages((Object)(new BasicErrorMessage("Unexpected error: " + s)));
            this.modelFileName = null;
            this.model = null;
            this.initModelViews();
            this.setDirty(false);
            if (removeTempDir) {
               this.removeDirectory(tempDir);
            }

            return false;
         }

         if (removeTempDir) {
            this.removeDirectory(tempDir);
         }

         return true;
      }
   }

   private void saveXML(File dir) {
      XMIEncoder encoder = XMISerializer.getInstance().getEncoder();
      encoder.encode(this.getModel(), (String)(new File(dir, this.getModel().getName()) + ".xml"), "XMI", "1.1", "UML", "1.3");
   }

   private Model loadXML(String filename) {
      Class metaclass = null;
      metaclass = (new ModelImpl()).getClass();
      XMIDecoder decoder = XMISerializer.getInstance().getDecoder();
      Object m = null;
      if (decoder.isSessionOpen()) {
         decoder.setMetadataRootClass(metaclass);
         m = decoder.decode(filename);
      } else {
         decoder.newSession((new File(filename)).getParentFile());
         decoder.setMetadataRootClass(metaclass);
         m = decoder.decode(filename);
         decoder.closeSession();
      }

      if (m instanceof Model) {
         ModelFactory.currentModel = (Model)m;
         ModelFactory.createElementOwnership((Namespace)null, ModelFactory.currentModel);
         ModelFactory.currentModel.setOwnerModel(ModelFactory.currentModel);
         ModelFactory.attachPredefinedDataTypes();
         ModelFactory.attachStereotypes();
         return ModelFactory.currentModel;
      } else {
         return null;
      }
   }

   private void loadModelAndDiagrams(File mFile, File[] diagFiles, File parentDir) throws Exception {
      XMIDecoder decoder = XMISerializer.getInstance().getDecoder();
      decoder.newSession(parentDir);
      if (mFile != null) {
         this.model = this.loadXML(mFile.getAbsolutePath());
         if (this.model == null) {
            throw new Exception("File " + mFile.getAbsolutePath() + " is not an XMI file or does not contain a valid UML model.");
         }

         this.initModelViews();
         this.setDirty(false);
      }

      GBrowser browser = this.getBrowser();
      this.diagramsNo = 0;

      for(int i = 0; diagFiles != null && i < diagFiles.length; ++i) {
         Object gxd = null;
         gxd = decoder.decode(diagFiles[i].getPath());
         if (gxd == null) {
            listener.exceptionThrown(new XMIErrorException("File " + mFile.getAbsolutePath() + " is not an XMI file or does not contain a valid UML diagram."));
         } else {
            ModelElement owner;
            if (gxd instanceof GXClassDiagram) {
               GXClassDiagram gxcd = (GXClassDiagram)gxd;
               owner = (ModelElement)gxcd.getOwner();
               ClassDiagram cd = new ClassDiagram(this, owner);
               cd.extractData(gxcd);
               browser.addDiagram(cd, owner, false);
               this.diagramViews.add(cd);
               ++this.diagramsNo;
            } else if (gxd instanceof GXObjectDiagram) {
               GXObjectDiagram gxod = (GXObjectDiagram)gxd;
               owner = (ModelElement)gxod.getOwner();
               ObjectDiagram od = new ObjectDiagram(this, owner);
               od.extractData(gxod);
               browser.addDiagram(od, owner, false);
               this.diagramViews.add(od);
               ++this.diagramsNo;
            } else if (gxd instanceof GXUseCaseDiagram) {
               GXUseCaseDiagram gxucd = (GXUseCaseDiagram)gxd;
               owner = (ModelElement)gxucd.getOwner();
               UseCaseDiagram ucd = new UseCaseDiagram(this, owner);
               ucd.extractData(gxucd);
               browser.addDiagram(ucd, owner, false);
               this.diagramViews.add(ucd);
               ++this.diagramsNo;
            }
         }
      }

      decoder.closeSession();
   }

   public void saveModel(File file) {
      File parentDir = file.getParentFile();
      File tempDir = new File(parentDir, String.valueOf(System.currentTimeMillis()).toString());
      tempDir.mkdir();
      File diagramsDir = new File(tempDir, "Diagrams");
      diagramsDir.mkdir();
      XMIEncoder encoder = XMISerializer.getInstance().getEncoder();
      encoder.newSession(tempDir);
      encoder.addExternalMapping("XMI", "1.1", "UML", "1.3");
      this.saveXML(tempDir);
      boolean isZipFile = file.getPath().endsWith(".xml.zip");
      if (isZipFile) {
         for(int i = 0; i < this.diagramViews.size(); ++i) {
            GAbstractDiagram diagram = (GAbstractDiagram)this.diagramViews.get(i);
            String diagramFileName = XMIUtilities.toURI(diagramsDir, true) + diagram.getName() + ".xml";
            File diagramFile = new File(diagramFileName);
            if (diagram instanceof ClassDiagram) {
               ClassDiagram cDiagram = (ClassDiagram)diagram;
               GXClassDiagram gxcd = cDiagram.copy();
               encoder.encode(gxcd, (File)diagramFile, "XMI", "1.1", "GXAPI", "0.1");
            } else if (diagram instanceof ObjectDiagram) {
               ObjectDiagram oDiagram = (ObjectDiagram)diagram;
               GXObjectDiagram gxod = oDiagram.copy();
               encoder.encode(gxod, (File)diagramFile, "XMI", "1.1", "GXAPI", "0.1");
            } else if (diagram instanceof UseCaseDiagram) {
               UseCaseDiagram ucDiagram = (UseCaseDiagram)diagram;
               GXUseCaseDiagram gxucd = ucDiagram.copy();
               encoder.encode(gxucd, (File)diagramFile, "XMI", "1.1", "GXAPI", "0.1");
            }
         }
      }

      encoder.closeSession();
      if (isZipFile) {
         this.zipDirectory(tempDir, file);
      } else {
         FileInputStream fis = null;
         FileOutputStream fos = null;

         try {
            fis = new FileInputStream(new File(tempDir, this.model.getName() + ".xml"));
            fos = new FileOutputStream(file);
            byte[] input = new byte[4096];

            for(int inputLength = fis.read(input); inputLength >= 0; inputLength = fis.read(input)) {
               fos.write(input, 0, inputLength);
            }
         } catch (IOException var22) {
            var22.printStackTrace();
         } finally {
            try {
               fos.close();
               fis.close();
            } catch (IOException var21) {
               var21.printStackTrace();
            }

         }
      }

      this.removeDirectory(tempDir);
      this.diagramsNo = this.diagramViews.size();
      this.setModelFileName(file.getAbsolutePath());
      this.setDirty(false);
      if (this.ownerProject != null) {
         FBrowser fbrowser = this.ownerProject.getFileBrowser();
         FTreeNode node = fbrowser.getNodeForObject(new File(this.modelFileName));
         if (node != null) {
            fbrowser.refreshZippedChildren(node);
         }
      }

   }

   private void zipDirectory(File tempDir, File file) {
      try {
         ZipOutputStream zipOS = new ZipOutputStream(new FileOutputStream(file));
         zipOS.setMethod(8);
         zipOS.setLevel(-1);
         File[] tfiles = tempDir.listFiles();
         ArrayList files = new ArrayList();

         for(int i = 0; i < tfiles.length; ++i) {
            if (tfiles[i].isDirectory()) {
               File[] tfiles2 = tfiles[i].listFiles();

               for(int j = 0; j < tfiles2.length; ++j) {
                  files.add(tfiles2[j]);
               }
            } else {
               files.add(tfiles[i]);
            }
         }

         Iterator it = files.iterator();

         while(it.hasNext()) {
            File tempFile = (File)it.next();
            URI tempFileRelativeURI = tempDir.toURI().relativize(tempFile.toURI());
            ZipEntry zipEntry = new ZipEntry(tempFileRelativeURI.toString());
            zipOS.putNextEntry(zipEntry);
            InputStream is = new FileInputStream(tempFile);
            byte[] buffer = new byte[4096];

            int read;
            while((read = is.read(buffer, 0, 4096)) != -1) {
               zipOS.write(buffer, 0, read);
            }

            is.close();
            zipOS.closeEntry();
         }

         zipOS.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   private void unzipFile(File file, File tempDir) {
      if (!tempDir.exists()) {
         tempDir.mkdirs();
      }

      File dirDiag = new File(tempDir, "Diagrams");
      if (!dirDiag.exists()) {
         dirDiag.mkdir();
      }

      try {
         ZipFile zf = new ZipFile(file);
         Enumeration zipEnum = zf.entries();

         while(true) {
            while(zipEnum.hasMoreElements()) {
               ZipEntry ze = (ZipEntry)zipEnum.nextElement();
               String zeName = ze.getName();
               if (ze.isDirectory()) {
                  File dir = new File(tempDir, zeName);
                  if (!dir.exists()) {
                     dir.mkdirs();
                  }
               } else {
                  InputStream ins = zf.getInputStream(ze);
                  File outFile = new File(tempDir, zeName);
                  FileOutputStream fos = new FileOutputStream(outFile);
                  byte[] bin = new byte[4096];

                  int bread;
                  while((bread = ins.read(bin, 0, 4096)) > -1) {
                     fos.write(bin, 0, bread);
                  }

                  ins.close();
                  fos.close();
               }
            }

            zf.close();
            break;
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   private void removeDirectory(File dir) {
      File[] files = dir.listFiles();

      for(int i = 0; i < files.length; ++i) {
         if (files[i].isDirectory()) {
            this.removeDirectory(files[i]);
         } else {
            files[i].delete();
         }
      }

      dir.delete();
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void setDirty(boolean dirty) {
      this.dirty = dirty;
   }

   static {
      XMISerializer.getInstance().setExceptionListener(listener);
   }

   private static class XMLSerializerErrorListener implements ExceptionListener {
      private XMLSerializerErrorListener() {
      }

      public void exceptionThrown(Exception e) {
         if (e instanceof XMIException) {
            GMainFrame.getMainFrame().updateMessages((Object)e);
         }

      }
   }
}
