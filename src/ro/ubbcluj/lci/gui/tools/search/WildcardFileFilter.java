package ro.ubbcluj.lci.gui.tools.search;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import ro.ubbcluj.lci.utils.Utils;

public class WildcardFileFilter implements FileFilter {
   private List patterns;

   public WildcardFileFilter(List patterns) {
      this.patterns = patterns;
   }

   public boolean accept(File pathname) {
      if (pathname.isDirectory()) {
         return true;
      } else {
         String name = pathname.getName();

         for(int i = 0; i < this.patterns.size(); ++i) {
            if (Utils.matchesByWildcards((String)this.patterns.get(i), name)) {
               return true;
            }
         }

         return false;
      }
   }
}
