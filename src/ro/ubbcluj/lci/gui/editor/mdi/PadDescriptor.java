package ro.ubbcluj.lci.gui.editor.mdi;

public abstract class PadDescriptor {
   protected PadContainer padContainer;
   private boolean bNeedsClosing;

   public PadDescriptor() {
   }

   public boolean needsClosing() {
      return this.bNeedsClosing;
   }

   public void needsClosing(boolean b) {
      this.bNeedsClosing = b;
   }

   public PadContainer getOwner() {
      return this.padContainer;
   }
}
