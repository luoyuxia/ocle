package ro.ubbcluj.lci.codegen;

import java.io.File;

public class CodeGenerationOptions {
   public File destinationDirectory;
   public boolean processConstraints;
   public boolean constraintsAvailable;
   public boolean braceOnNewLine;
   public int indentSize;
   public boolean convertTabsToSpaces;

   public CodeGenerationOptions() {
   }
}
