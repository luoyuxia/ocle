package ro.ubbcluj.lci.gui.mainframe;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import ro.ubbcluj.lci.gui.tools.Error;
import ro.ubbcluj.lci.gui.tools.Question;
import ro.ubbcluj.lci.gui.tools.Warning;

public class MessagePane extends JPanel {
   private static final int GROUP_SIZE = 50;
   private OutputPane output = new OutputPane();
   public List list = new ArrayList();
   private int group = 0;
   private JButton btnPrev;
   private JButton btnNext;
   private JButton btnFirst;
   private JButton btnLast;
   private JPanel btnPanel;
   private Action prevAction;
   private Action nextAction;
   private Action firstAction;
   private Action lastAction;

   public MessagePane() {
      this.btnPrev = new JButton(this.prevAction = new MessagePane.PrevAction());
      this.btnNext = new JButton(this.nextAction = new MessagePane.NextAction());
      this.btnFirst = new JButton(this.firstAction = new MessagePane.FirstAction());
      this.btnLast = new JButton(this.lastAction = new MessagePane.LastAction());
      this.btnPanel = new JPanel();
      this.btnPanel.setLayout(new BoxLayout(this.btnPanel, 0));
      this.btnPrev.setMaximumSize(new Dimension(50, 50));
      this.btnNext.setMaximumSize(new Dimension(50, 50));
      this.btnFirst.setMaximumSize(new Dimension(50, 50));
      this.btnLast.setMaximumSize(new Dimension(50, 50));
      this.btnPanel.setMaximumSize(new Dimension(32767, 50));
      this.btnPanel.add(this.btnPrev);
      this.btnPanel.add(this.btnNext);
      this.btnPanel.add(this.btnFirst);
      this.btnPanel.add(this.btnLast);
      this.btnPrev.setToolTipText("Previous 50 messages");
      this.btnNext.setToolTipText("Next 50 messages");
      this.btnFirst.setToolTipText("Begining of list");
      this.btnLast.setToolTipText("End of list");
      this.setLayout(new BoxLayout(this, 1));
      this.add(this.output);
      this.add(this.btnPanel);
      this.refreshButtonState();
   }

   public void addMessage(Object msg) {
      this.list.add(msg);
      this.refreshButtonState();
      this.refreshView();
   }

   private void refreshButtonState() {
      boolean b1 = this.group > 0;
      boolean b2 = (this.group + 1) * 50 < this.list.size();
      this.prevAction.setEnabled(b1);
      this.nextAction.setEnabled(b2);
      this.firstAction.setEnabled(b1);
      this.lastAction.setEnabled(b2);
   }

   private void refreshView() {
      this.output.clearPane();
      int s = this.list.size();
      int start = this.group * 50;
      int stop = start + 50;

      for(int i = start; i < stop && i < s; ++i) {
         Object obj = this.list.get(i);
         if (obj instanceof Warning) {
            this.output.addWarning(obj);
         } else if (obj instanceof Error) {
            this.output.addError(obj);
         } else if (obj instanceof Question) {
            this.output.addQuestion(obj);
         } else {
            this.output.addInfo(obj);
         }
      }

      this.output.repaint();
   }

   public void addMouseListener(MouseListener l) {
      this.output.addMouseListener(l);
   }

   public void removeMouseListener(MouseListener l) {
      this.output.removeMouseListener(l);
   }

   public Object getSelectedObject() {
      return this.output.getSelectedObject();
   }

   public void clear() {
      this.output.clearPane();
      this.group = 0;
      this.list.clear();
      this.refreshButtonState();
   }

   public void focusLastGroup() {
      int r = this.list.size() % 50;
      int temp = this.list.size() / 50;
      this.group = r > 0 ? temp : (temp > 0 ? temp - 1 : temp);
      this.refreshButtonState();
      this.refreshView();
   }

   private class LastAction extends AbstractAction {
      public LastAction() {
         super("");
         ImageIcon ic = new ImageIcon((MessagePane.class).getResource("/resources/last.gif"));
         this.putValue("SmallIcon", ic);
      }

      public void actionPerformed(ActionEvent evt) {
         MessagePane.this.focusLastGroup();
      }
   }

   private class FirstAction extends AbstractAction {
      public FirstAction() {
         super("");
         ImageIcon ic = new ImageIcon((MessagePane.class).getResource("/resources/first.gif"));
         this.putValue("SmallIcon", ic);
      }

      public void actionPerformed(ActionEvent evt) {
         MessagePane.this.group = 0;
         MessagePane.this.refreshButtonState();
         MessagePane.this.refreshView();
      }
   }

   private class NextAction extends AbstractAction {
      public NextAction() {
         super("");
         ImageIcon ic = new ImageIcon((MessagePane.class).getResource("/resources/next.gif"));
         this.putValue("SmallIcon", ic);
      }

      public void actionPerformed(ActionEvent evt) {
         MessagePane.this.group++;
         MessagePane.this.refreshButtonState();
         MessagePane.this.refreshView();
      }
   }

   private class PrevAction extends AbstractAction {
      public PrevAction() {
         super("");
         ImageIcon ic = new ImageIcon((MessagePane.class).getResource("/resources/prev.gif"));
         this.putValue("SmallIcon", ic);
      }

      public void actionPerformed(ActionEvent evt) {
         MessagePane.this.group--;
         MessagePane.this.refreshButtonState();
         MessagePane.this.refreshView();
      }
   }
}
