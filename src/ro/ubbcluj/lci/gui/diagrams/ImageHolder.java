package ro.ubbcluj.lci.gui.diagrams;

import java.net.URL;
import javax.swing.ImageIcon;

public class ImageHolder {
   private static final ImageIcon attrPrivate = new ImageIcon(getImage("PrivateAttribute"), "private");
   private static final ImageIcon attrProtected = new ImageIcon(getImage("ProtectedAttribute"), "protected");
   private static final ImageIcon attrPackage = new ImageIcon(getImage("PackageAttribute"), "package");
   private static final ImageIcon attrPublic = new ImageIcon(getImage("PublicAttribute"), "public");
   private static final ImageIcon methodPrivate = new ImageIcon(getImage("PrivateMethod"), "private");
   private static final ImageIcon methodProtected = new ImageIcon(getImage("ProtectedMethod"), "protected");
   private static final ImageIcon methodPackage = new ImageIcon(getImage("PackageMethod"), "package");
   private static final ImageIcon methodPublic = new ImageIcon(getImage("PublicMethod"), "public");

   public ImageHolder() {
   }

   private static URL getImage(String key) {
      return (ImageHolder.class).getResource("/images/" + key + ".gif");
   }

   static ImageIcon getImageforAttr(int type) {
      switch(type) {
      case 0:
         return attrPrivate;
      case 1:
      default:
         return attrPackage;
      case 2:
         return attrProtected;
      case 3:
         return attrPublic;
      }
   }

   static ImageIcon getImageforMethod(int type) {
      switch(type) {
      case 0:
         return methodPrivate;
      case 1:
      default:
         return methodPackage;
      case 2:
         return methodProtected;
      case 3:
         return methodPublic;
      }
   }
}
