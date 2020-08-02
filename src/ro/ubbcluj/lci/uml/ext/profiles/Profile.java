package ro.ubbcluj.lci.uml.ext.profiles;

import java.util.List;
import ro.ubbcluj.lci.gui.Actions.CompilationInfo;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public interface Profile {
   void cleanUp();

   void registerFile(String var1);

   boolean containsFile(String var1);

   List getErrors();

   String getName();

   void setName(String var1);

   void compile(Model var1);

   OclNode getRules();

   CompilationInfo getInfo();
}
