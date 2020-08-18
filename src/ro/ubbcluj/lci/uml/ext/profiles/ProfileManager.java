package ro.ubbcluj.lci.uml.ext.profiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ro.ubbcluj.lci.errors.BasicErrorMessage;

public class ProfileManager {
   private List errors = new ArrayList();
   private ProfileFactory profileFactory;
   private HashMap currentProfiles;

   public ProfileManager(String fileName, ProfileFactory pf) {
      this.profileFactory = pf;
      this.currentProfiles = new HashMap();
      this.loadProfileData(fileName);
   }

   public Profile getProfileByName(String name) {
      return (Profile)this.currentProfiles.get(name);
   }

   public Profile getProfileForFile(String fileName) {
      Profile p = null;
      Iterator it = this.currentProfiles.values().iterator();

      while(it.hasNext() && p == null) {
         Profile pp = (Profile)it.next();
         if (pp.containsFile(fileName)) {
            p = pp;
         }
      }

      return p;
   }

   public List getErrors() {
      return Collections.unmodifiableList(this.errors);
   }

   public void cleanUp() {
      Iterator profiles = this.currentProfiles.values().iterator();

      while(profiles.hasNext()) {
         Profile profile = (Profile)profiles.next();
         profile.cleanUp();
      }

   }

   private void loadProfileData(String fileName) {
      DocumentBuilder builder;
      try {
         builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = builder.parse(new File(fileName));
         NodeList profileNodes = doc.getElementsByTagName("profile");
         System.out.println(profileNodes.getLength() + " profiles found");

         for(int i = 0; i < profileNodes.getLength(); ++i) {
            Node item = profileNodes.item(i);
            Profile np = this.profileFactory.newProfile();
            String name;
            np.setName(name = item.getAttributes().getNamedItem("name").getNodeValue());
        //    System.out.println("Profile " + name);
            this.currentProfiles.put(name, np);
            NodeList children = item.getChildNodes();

            for(int j = 0; j < children.getLength(); ++j) {
               Node chld = children.item(j);
               if ("file".equals(chld.getNodeName())) {
                  String file_name = chld.getAttributes().getNamedItem("name").getNodeValue();
                  np.registerFile(file_name);
             //     System.out.println("registered file " + file_name + " in the " + name + " profile");
               }
            }
         }
      } catch (ParserConfigurationException var13) {
         this.errors.add(new BasicErrorMessage("Creation error:" + var13.getMessage()));
      } catch (IOException var14) {
         this.errors.add(new BasicErrorMessage("IO error:" + var14.getMessage()));
      } catch (SAXException var15) {
         this.errors.add(new BasicErrorMessage("Parse error:" + var15.getMessage()));
      }

      builder = null;
   }
}
