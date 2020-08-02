package ro.ubbcluj.lci.ocl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLinkImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValueImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEndImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.ObjectImpl;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnershipImpl;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.utils.ModelFactory;

public class ImportUseObjects {
   private Model model;
   private String filename;
   private HashMap classes = new HashMap();
   private HashMap associations = new HashMap();
   private HashMap instances = new HashMap();
   private Collaboration clb;

   public ImportUseObjects(Model model, String filename) {
      this.model = model;
      this.filename = filename;
   }

   public void addObjects() {
      this.clb = (Collaboration)ModelFactory.createNewElement(this.model, "Collaboration");
      this.clb.setName(ModelFactory.getDefaultName((Namespace)this.clb.directGetNamespace(), this.clb, "Objects imported from USE"));
      this.visit(this.model);
      this.parseFile(this.filename);
   }

   private void visit(Namespace elem) {
      Enumeration ownedList = elem.getOwnedElementList();

      while(ownedList.hasMoreElements()) {
         ModelElement owned = ((ElementOwnership)ownedList.nextElement()).getOwnedElement();
         if (owned instanceof Class) {
            Object old = this.classes.put(owned.getName(), owned);
            if (old != null) {
               System.err.println("WARNING: there are more classes with the name " + owned.getName());
            }
         }

         if (owned instanceof Association) {
            String s = owned.getName();
            if (s.indexOf(123) >= 0) {
               s = s.substring(0, s.indexOf(123));
            }

            Object old = this.associations.put(s, owned);
            if (old != null) {
               System.err.println("WARNING: there are more associations with the name " + s);
            }
         }

         if (owned instanceof Namespace) {
            this.visit((Namespace)owned);
         }
      }

   }

   private void parseFile(String filename) {
      try {
         BufferedReader in = new BufferedReader(new FileReader(filename));

         while(true) {
            String s = in.readLine();
            if (s == null) {
               break;
            }

            StringTokenizer tok = new StringTokenizer(s, " ,:().=");
            Vector v = new Vector();

            while(tok.hasMoreTokens()) {
               v.add(tok.nextToken());
            }

            String[] vs = new String[v.size()];

            for(int i = 0; i < v.size(); ++i) {
               vs[i] = (String)v.elementAt(i);
            }

            if (vs.length >= 3 && vs[0].equals("!create")) {
               this.cmdCreate(s, vs);
            } else if (vs.length >= 4 && vs[0].equals("!set")) {
               this.cmdSet(s, vs);
            } else if (vs.length >= 5 && vs[0].equals("!insert")) {
               this.cmdInsert(s, vs);
            } else if (vs.length > 0 && vs[0].startsWith("!")) {
               System.err.println("ignored: " + s);
            }
         }
      } catch (IOException var8) {
         System.err.println(var8.getClass().getName() + ": " + var8.getMessage());
      }

   }

   private void cmdCreate(String cmd, String[] s) {
      Class cls = (Class)this.classes.get(s[s.length - 1]);
      if (cls == null) {
         System.err.println("ERROR: " + cmd + " : class cannot be found");
      } else {
         for(int i = 1; i < s.length - 1; ++i) {
            Instance instance = new ObjectImpl();
            ModelFactory.createElementOwnership(this.clb, instance);
            instance.setName(s[i]);
            cls.addInstance(instance);
            this.instances.put(s[i], new ModelElement[]{instance, cls});
         }

      }
   }

   private void cmdSet(String cmd, String[] s) {
      ModelElement[] pair = (ModelElement[])this.instances.get(s[1]);
      if (pair == null) {
         System.err.println("ERROR: " + cmd + " : object cannot be found");
      } else {
         Iterator it = ((Class)pair[1]).allAttributes().iterator();

         while(it.hasNext()) {
            Attribute at = (Attribute)it.next();
            if (at.getName().equals(s[2])) {
               AttributeLinkImpl atlink = new AttributeLinkImpl();
               atlink.setNamespace(new ElementOwnershipImpl());
               atlink.setAttribute(at);
               atlink.setInstance((Instance)pair[0]);
               DataValueImpl data = new DataValueImpl();
               data.setNamespace(new ElementOwnershipImpl());
               data.setName(s[3]);
               ModelFactory.createElementOwnership(this.model, data);
               atlink.setValue(data);
            }
         }

      }
   }

   private void cmdInsert(String cmd, String[] s) {
      ModelElement[][] objects = new ModelElement[s.length - 3][];
      StringBuffer linkName = new StringBuffer();

      for(int i = 0; i < objects.length; ++i) {
         objects[i] = (ModelElement[])this.instances.get(s[i + 1]);
         if (objects[i] == null) {
            System.err.println("ERROR: " + cmd + " : object " + i + "cannot be found");
            return;
         }

         linkName.append(objects[i][0] + "-");
      }

      Association as = (Association)this.associations.get(s[s.length - 1]);
      Enumeration connections = as.getConnectionList();
      AssociationEnd[] asends = new AssociationEnd[objects.length];
      AssociationEnd asend = null;

      for(int i = 0; i < objects.length; ++i) {
         asend = (AssociationEnd)connections.nextElement();
         Classifier assocEndParticipant = asend.getParticipant();
         Classifier instanceClassifier = (Classifier)((Instance)objects[i][0]).getClassifierList().nextElement();
         if (assocEndParticipant != instanceClassifier && !instanceClassifier.allParents().contains(assocEndParticipant)) {
            System.err.println("Object type does not conform to classifier type");
            return;
         }

         asends[i] = asend;
      }

      LinkImpl link = new LinkImpl();
      link.setAssociation(as);
      link.setName((new String(linkName)).substring(0, linkName.length() - 1));
      ModelFactory.createElementOwnership(this.clb, link);

      for(int i = 0; i < objects.length; ++i) {
         LinkEndImpl linkend = new LinkEndImpl();
         linkend.setName("link_" + objects[i][0].getName());
         linkend.setInstance((Instance)objects[i][0]);
         linkend.setAssociationEnd(asends[i]);
         linkend.setLink(link);
      }

   }
}
