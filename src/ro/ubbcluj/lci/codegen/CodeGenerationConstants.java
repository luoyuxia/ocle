package ro.ubbcluj.lci.codegen;

public interface CodeGenerationConstants {
   String BASE_FRAMEWORK_PATH = new String("ro.ubbcluj.lci.codegen.framework");
   String DATATYPES_PATH = BASE_FRAMEWORK_PATH + ".dt";
   String OCL_PATH = BASE_FRAMEWORK_PATH + ".ocl";
   String GETTER_PREFIX = new String("get");
   String SETTER_PREFIX = new String("set");
   String REMOVE_PREFIX = new String("remove");
   String DIRECT_PREFIX = new String("directGet");
   String JAVA_ENUM_TYPE = new String("java.util.Enumeration");
   String JAVA_SET_TYPE = new String("java.util.Set");
   String CONCRETE_SET_TYPE_ASSOCIATIONS = new String("java.util.LinkedHashSet");
   String JAVA_MAP_TYPE = new String("java.util.Map");
   String CONCRETE_MAP_TYPE_ASSOCIATIONS = new String("java.util.HashMap");
   String JAVA_ITERATOR_TYPE = new String("java.util.Iterator");
   String CONCRETE_LIST_TYPE = new String("java.util.ArrayList");
}
