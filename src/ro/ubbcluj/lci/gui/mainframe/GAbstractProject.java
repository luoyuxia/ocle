package ro.ubbcluj.lci.gui.mainframe;

import java.io.File;
import java.io.IOException;
import java.util.List;
import ro.ubbcluj.lci.gui.Actions.CompilationInfo;
import ro.ubbcluj.lci.gui.browser.FBrowser;

public interface GAbstractProject {
   boolean isDirty();

   void setDirty(boolean var1);

   boolean containsModel(String var1);

   String getProjectName();

   void setProjectName(String var1);

   File getProjectFile();

   void setProjectFile(File var1) throws IOException;

   void addView(GView var1);

   boolean removeView(GView var1);

   FBrowser getFileBrowser();

   void attachModel(String var1, boolean var2);

   void detachModel(String var1, boolean var2);

   void attachConstraint(String var1);

   void attachConstraint(String var1, boolean var2);

   void detachConstraint(String var1);

   void setReadOnly(boolean var1);

   boolean isReadOnly();

   GUMLModel getActiveModel();

   void setActiveModel(GUMLModel var1);

   String getActiveModelFile();

   void setActiveModelFile(String var1);

   List getSelectedConstraints();

   void loadProject(String var1) throws Exception;

   void loadProject(File var1) throws Exception;

   void saveProject(File var1) throws IOException;

   void setWfrInfo(CompilationInfo var1);

   CompilationInfo getWfrInfo();

   void setBcrInfo(CompilationInfo var1);

   CompilationInfo getBcrInfo();
}
