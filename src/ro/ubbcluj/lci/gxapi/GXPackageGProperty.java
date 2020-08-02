package ro.ubbcluj.lci.gxapi;

import java.awt.Color;
import ro.ubbcluj.lci.gui.diagrams.GProperty;
import ro.ubbcluj.lci.gui.diagrams.PackageGProperty;

public class GXPackageGProperty extends GXDefaultGProperty {
   protected String fillColor;
   protected int titlefontSize;

   public GXPackageGProperty() {
   }

   public void setFillColor(String fillColor) {
      this.fillColor = fillColor;
   }

   public String getFillColor() {
      return this.fillColor;
   }

   public void setTitlefontSize(int titlefontSize) {
      this.titlefontSize = titlefontSize;
   }

   public int getTitlefontSize() {
      return this.titlefontSize;
   }

   public void copy(GProperty gp) {
      super.copy(gp);
      this.fillColor = ((PackageGProperty)gp).getFillColor().toString();
      this.titlefontSize = ((PackageGProperty)gp).getTitleFontSize();
   }

   public void extractData(GProperty gp) {
      super.extractData(gp);
      ((PackageGProperty)gp).setFillColor(this.stringToColor(this.fillColor));
      ((PackageGProperty)gp).setTitleFontSize(this.titlefontSize);
   }

   private Color stringToColor(String val) {
      int index = val.indexOf("r=");
      String temp = val.substring(index + 2, val.indexOf(",", index));
      int r = Integer.parseInt(temp);
      index = val.indexOf("g=");
      temp = val.substring(index + 2, val.indexOf(",", index));
      int g = Integer.parseInt(temp);
      index = val.indexOf("b=");
      temp = val.substring(index + 2, val.indexOf("]", index));
      int b = Integer.parseInt(temp);
      return new Color(r, g, b);
   }
}
