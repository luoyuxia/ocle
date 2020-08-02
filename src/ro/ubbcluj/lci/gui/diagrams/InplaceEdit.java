package ro.ubbcluj.lci.gui.diagrams;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.utils.ModelFactory;

public class InplaceEdit {
   private static InplaceEdit instance;
   private static JTextField textf;
   private static JDialog dialog;
   private static Object target;

   private InplaceEdit() {
      textf = new JTextField();
      textf.addActionListener(new InplaceEdit.TextFieldListener());
      textf.addKeyListener(new InplaceEdit.InplaceKeyListener());
      dialog = new JDialog();
      dialog.setTitle("");
      dialog.setModal(true);
      dialog.setDefaultCloseOperation(0);
      dialog.setUndecorated(true);
      dialog.setSize(100, 20);
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(textf, "Center");
      dialog.setContentPane(panel);
   }

   public static InplaceEdit getInstance() {
      if (instance == null) {
         instance = new InplaceEdit();
      }

      return instance;
   }

   public void edit(String initialText, Point location, Object newTarget) {
      target = newTarget;
      textf.setText(initialText);
      dialog.setLocation(location);
      dialog.show();
   }

   class TextFieldListener implements ActionListener {
      TextFieldListener() {
      }

      public void actionPerformed(ActionEvent e) {
         ModelFactory.setAttribute(InplaceEdit.target, "Name", InplaceEdit.textf.getText());
         GRepository.getInstance().getUsermodel().getBrowser().selectInBrowser(InplaceEdit.target);
         InplaceEdit.dialog.dispose();
      }
   }

   class InplaceKeyListener extends KeyAdapter {
      InplaceKeyListener() {
      }

      public void keyPressed(KeyEvent e) {
         if (e.getKeyCode() == 27) {
            InplaceEdit.dialog.dispose();
         }

      }
   }
}
