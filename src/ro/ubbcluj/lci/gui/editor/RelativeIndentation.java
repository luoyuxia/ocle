package ro.ubbcluj.lci.gui.editor;

import ro.ubbcluj.lci.gui.editor.jedit.JEditTextArea;

class RelativeIndentation {
   int[] refLines;
   boolean[] relPoz;

   public RelativeIndentation(JEditTextArea ta) {
      int nr = ta.getLineCount();
      this.refLines = new int[nr];
      this.relPoz = new boolean[nr];
      int[] startX = new int[nr];

      for(int i = 0; i < nr; ++i) {
         startX[i] = ta.getLineOffsetX(i);
      }

      this.compute(nr, startX);
   }

   private void compute(int nr, int[] startX) {
      this.refLines[0] = -1;
      this.relPoz[0] = false;
      if (nr > 1) {
         if (startX[0] == -1) {
            this.refLines[1] = -1;
            this.relPoz[1] = false;
         } else {
            this.refLines[1] = 0;
            if (startX[1] > startX[0]) {
               this.relPoz[1] = true;
            } else {
               this.relPoz[1] = false;
            }
         }
      }

      for(int i = 2; i < nr; ++i) {
         if (startX[i] <= 0) {
            this.refLines[i] = -1;
            this.relPoz[i] = false;
         } else {
            this.refLines[i] = -1;
            int max = 0;

            for(int j = 0; j < i; ++j) {
               if (startX[j] != -1 && startX[j] <= startX[i] && startX[j] >= max) {
                  this.refLines[i] = j;
                  max = startX[j];
               }
            }

            if (this.refLines[i] != -1 && startX[this.refLines[i]] < startX[i]) {
               this.relPoz[i] = true;
            } else {
               this.relPoz[i] = false;
            }
         }
      }

   }
}
