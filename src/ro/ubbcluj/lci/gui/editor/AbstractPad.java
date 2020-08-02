package ro.ubbcluj.lci.gui.editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import ro.ubbcluj.lci.gui.editor.mdi.GView;
import ro.ubbcluj.lci.gui.editor.mdi.PadDescriptor;

public abstract class AbstractPad extends GView {
   public static final int TEXT_PAD = 0;
   public static final int DIAGRAM_PAD = 1;
   public static final int PAD_CLOSED = 2;
   public static final int PAD_NOT_CLOSED = 3;
   public static final int OPERATION_CANCELLED = 4;
   public static final String TITLE = "title";
   public static final String ICON = "icon";
   public static final String DESCRIPTION = "description";
   private static int ID = 0;
   private String id = "";
   private PadDescriptor descriptor;
   private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
   private boolean notifies = true;
   private Icon icon = null;
   private String description = null;
   private String title = "";

   public AbstractPad() {
      this.id = "ID" + ++ID;
   }

   public void setNotifies(boolean bNot) {
      this.notifies = bNot;
      if (this.notifies) {
         this.pcs.firePropertyChange("", (Object)null, (Object)null);
      }

   }

   public PadDescriptor getDescriptor() {
      return this.descriptor;
   }

   public void setDescriptor(PadDescriptor aDescriptor) {
      this.descriptor = aDescriptor;
   }

   public abstract int close();

   public abstract void activate();

   public abstract void initialize();

   public abstract int getType();

   public Icon getIcon() {
      return this.icon;
   }

   public void setIcon(Icon newIcon) {
      Icon old = this.icon;
      this.icon = newIcon;
      if (this.notifies) {
         this.pcs.firePropertyChange("icon", old, this.icon);
      }

   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String newTitle) {
      String old = this.title;
      this.title = newTitle;
      if (this.notifies) {
         this.pcs.firePropertyChange("title", old, this.title);
      }

   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String newDesc) {
      String old = this.description;
      this.description = newDesc;
      if (this.notifies) {
         this.pcs.firePropertyChange("description", old, this.description);
      }

   }

   public void addPropertyChangeListener(PropertyChangeListener pcl) {
      this.pcs.addPropertyChangeListener(pcl);
   }

   public void removePropertyChangeListener(PropertyChangeListener pcl) {
      this.pcs.removePropertyChangeListener(pcl);
   }

   public int hashCode() {
      return this.id.hashCode();
   }
}
