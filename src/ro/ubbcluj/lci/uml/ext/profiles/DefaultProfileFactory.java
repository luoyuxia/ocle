package ro.ubbcluj.lci.uml.ext.profiles;

public class DefaultProfileFactory implements ProfileFactory {
   public DefaultProfileFactory() {
   }

   public Profile newProfile() {
      return new BasicProfile();
   }
}
