package ro.ubbcluj.lci.gui.diagrams;

public interface DiagramAction {
   int SELECT = 0;
   int ADD_ELEMENT = 0;
   int ADD_CLASS = 1;
   int ADD_INTERFACE = 2;
   int ADD_PACKAGE = 3;
   int ADD_ACTOR = 4;
   int ADD_USECASE = 5;
   int ADD_OBJECT = 6;
   int ADD_RELATION = 100;
   int ADD_ASSOCIATION = 101;
   int ADD_GENERALIZATION = 102;
   int ADD_DEPENDENCY = 103;
   int ADD_PERMISSION = 104;
   int ADD_REALIZATION = 105;
   int ADD_ASSOCCLS = 106;
   int ADD_LINK = 107;
   int ADD_INCLUDE = 108;
   int ADD_EXTEND = 109;
   int ZOOM = 200;
   int DEFAULT_ZOOM = 201;
   int ZOOM_IN = 202;
   int ZOOM_OUT = 203;
   int UNDO = 301;
   int REDO = 302;
   int QUICK_VIEW = 400;
   int ALIGN = 500;
   int ALIGN_LEFT = 501;
   int ALIGN_RIGHT = 502;
   int ALIGN_TOP = 503;
   int ALIGN_BOTTOM = 504;
}
