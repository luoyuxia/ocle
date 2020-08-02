package ro.ubbcluj.lci.xmi.config;

import ro.ubbcluj.lci.xmi.structure.ClassDescriptor;

public interface ClassIntrospector {
   void setFullyQualifiedClassName(String var1) throws ClassNotFoundException;

   ClassDescriptor introspect(String var1) throws ClassNotFoundException;

   ClassDescriptor introspect();
}
