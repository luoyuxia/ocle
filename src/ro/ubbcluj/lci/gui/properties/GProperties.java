package ro.ubbcluj.lci.gui.properties;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.gui.mainframe.GUMLModelView;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.utils.ModelEvent;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.ModelListener;

public class GProperties extends GUMLModelView implements ModelListener, PropertyListener {
   protected static GProperties instance;
   protected Properties sheet;
   protected PropertiesInspector theElement;
   private boolean showingInherited = true;
   private JPopupMenu popup = new JPopupMenu("Options");
   private MouseListener popupListener = new GProperties.PopupAdapter();
   private TextualDescriptor currentTextualDescriptor;

   protected GProperties(GUMLModel gmodel) {
      this.setUserObject(gmodel);
      this.sheet = new Properties();
      this.sheet.addPropertyListener(this);
      this.sheet.setShowingTooltips(true);
   }

   public static GProperties getInstance() {
      if (instance == null) {
         instance = new GProperties(GRepository.getInstance().getMetamodel());
      }

      return instance;
   }

   public void setElement(Object element) {
      this.theElement = new PropertiesInspector(element);
   }

   public Object getElement() {
      return this.theElement.getElement();
   }

   public JComponent getComponent() {
      return this.sheet;
   }

   public void setTextualDescriptor(TextualDescriptor td) {
      this.currentTextualDescriptor = td;
   }

   public void setShowingInherited(boolean val) {
      this.showingInherited = val;
      this.updateView();
   }

   public boolean isShowingInherited() {
      return this.showingInherited;
   }

   public void updateView() {
      Object element = this.theElement.getElement();
      if (element instanceof Element && element != null && ((Element)element).getOwnerModel() == GRepository.getInstance().getMetamodel()) {
         this.sheet.setReadOnly(true);
      }

      this.sheet.updateView(this.theElement);
      this.sheet.setTitle(this.currentTextualDescriptor.getDescription(element) + " properties");
   }

   public void modelChanged(ModelEvent evt) {
      if (evt.getOperation() == 0) {
         this.setElement(evt.getSubject());
         this.updateView();
      }

   }

   public void subjectChanged(PropertyEvent evt) {
      ModelFactory.fireModelEvent(evt.getNewValue(), (Object)null, 0);
   }

   public void valueChanged(PropertyEvent evt) {
      Object subject = ((PropertiesInspector)evt.getSubject()).getElement();
      String property = evt.getProperty();
      Object newValue = evt.getNewValue();
      if (newValue instanceof ComboItem) {
         newValue = ((ComboItem)newValue).getValue();
      }

      if (newValue instanceof ComboItem2) {
         ModelFactory.addValue((Element)subject, property, (Element)((ComboItem2)newValue).getValue());
      } else if (newValue.equals("<undefined>")) {
         ModelFactory.setAttribute(subject, property, (Object)null);
      } else {
         if (subject instanceof AttributeLink && property.equals("Value")) {
            Classifier type = ((AttributeLink)subject).getAttribute().getType();
            if (type instanceof Class) {
               ModelFactory.setAttribute(subject, property, newValue);
            } else if (type instanceof DataType) {
               DataValue dataValue = (DataValue)((AttributeLink)subject).getValue();
               if (dataValue == null || !dataValue.getName().equals(newValue)) {
                  dataValue = ModelFactory.createNewDataValue(newValue.toString(), type);
               }

               ModelFactory.setAttribute(subject, property, dataValue);
            }
         } else if (subject instanceof Element) {
            ModelFactory.setAttribute(subject, property, newValue);
         } else if (subject instanceof GAbstractDiagram) {
            if (property.equals("Name")) {
               ((GAbstractDiagram)subject).setName(newValue.toString());
            }

            ModelFactory.fireModelEvent(subject, (Object)null, 20);
            ModelFactory.fireModelEvent(subject, (Object)null, 0);
         }

      }
   }

   class ShowInheritedAction extends AbstractAction {
      public ShowInheritedAction() {
         if (GProperties.this.showingInherited) {
            this.putValue("Name", "Hide inherited");
         } else {
            this.putValue("Name", "Show inherited");
         }

      }

      public void actionPerformed(ActionEvent evt) {
         GProperties.this.showingInherited = !GProperties.this.showingInherited;
         GProperties.this.updateView();
      }
   }

   class PopupAdapter extends MouseAdapter {
      PopupAdapter() {
      }

      public void mousePressed(MouseEvent evt) {
         this.showPopup(evt);
      }

      public void mouseReleased(MouseEvent evt) {
         this.showPopup(evt);
      }

      public void showPopup(MouseEvent evt) {
         if (evt.isPopupTrigger()) {
            GProperties.this.popup.removeAll();
            GProperties.this.popup.add(GProperties.this.new ShowInheritedAction());
            GProperties.this.popup.show((JComponent)evt.getSource(), evt.getX(), evt.getY());
         }

      }
   }
}
