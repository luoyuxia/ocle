package ro.ubbcluj.lci.gui.editor.options;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import ro.ubbcluj.lci.gui.editor.AutoIndentFormatter;
import ro.ubbcluj.lci.gui.editor.TextDocumentPadFactory;

public class EditorOptionsDialog extends JDialog {
   JPanel panel1;
   JTabbedPane tabbedPane;
   JButton okBtn;
   JButton closeBtn;
   JButton applyBtn;
   ColorPanel colorPanel;
   FontPanel fontPanel;
   OtherOptionsPanel otherOptionsPanel;

   public EditorOptionsDialog(Frame frame, String title, boolean modal) {
      super(frame, title, modal);
      this.panel1 = new JPanel();
      this.tabbedPane = new JTabbedPane();
      this.okBtn = new JButton();
      this.closeBtn = new JButton();
      this.applyBtn = new JButton();

      try {
         this.jbInit();
         this.pack();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public EditorOptionsDialog() {
      this((Frame)null, "", false);
   }

   void jbInit() throws Exception {
      this.colorPanel = new ColorPanel();
      this.fontPanel = new FontPanel();
      this.otherOptionsPanel = new OtherOptionsPanel();
      this.panel1.setLayout(new BorderLayout());
      this.panel1.setPreferredSize(new Dimension(500, 400));
      this.panel1.setBounds(new Rectangle(500, 0, 0, 0));
      this.getContentPane().setLayout((LayoutManager)null);
      this.tabbedPane.setBounds(new Rectangle(8, 12, 504, 430));
      this.okBtn.setBounds(new Rectangle(338, 457, 79, 27));
      this.okBtn.setText("OK");
      this.okBtn.addActionListener(new EditorOptionsDialog.MyActionListener());
      this.closeBtn.setBounds(new Rectangle(432, 457, 79, 27));
      this.closeBtn.setText("Close");
      this.closeBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            EditorOptionsDialog.this.processWindowEvent(new WindowEvent(EditorOptionsDialog.this, 201));
         }
      });
      this.applyBtn.setBounds(new Rectangle(244, 457, 79, 27));
      this.applyBtn.setText("Apply");
      this.applyBtn.addActionListener(new EditorOptionsDialog.MyActionListener());
      this.setSize(new Dimension(550, 500));
      this.getContentPane().add(this.panel1, (Object)null);
      this.getContentPane().add(this.tabbedPane, (Object)null);
      this.getContentPane().add(this.closeBtn, (Object)null);
      this.getContentPane().add(this.applyBtn, (Object)null);
      this.getContentPane().add(this.okBtn, (Object)null);
      ((JPanel)this.getContentPane()).setPreferredSize(new Dimension(550, 500));
      this.tabbedPane.addTab("Colors", this.colorPanel);
      this.tabbedPane.addTab("Font", this.fontPanel);
      this.tabbedPane.addTab("Other options", this.otherOptionsPanel);
   }

   private class MyActionListener implements ActionListener {
      private MyActionListener() {
      }

      public void actionPerformed(ActionEvent e) {
         TextDocumentPadFactory.getFactory().setStyles(EditorOptionsDialog.this.colorPanel.styles);
         Font f = new Font((String)EditorOptionsDialog.this.fontPanel.fontCb.getSelectedItem(), 0, Integer.parseInt((String)EditorOptionsDialog.this.fontPanel.sizeCb.getSelectedItem()));
         TextDocumentPadFactory.getFactory().setFont(f);
         TextDocumentPadFactory.getFactory().setSample(EditorOptionsDialog.this.colorPanel.ta);
         AutoIndentFormatter.INDENT_SIZE = Integer.parseInt((String)EditorOptionsDialog.this.otherOptionsPanel.indentSizeCb.getSelectedItem());
         Properties appProps = EditorProperties.appProps;
         Properties defProps = EditorProperties.defProps;
         EditorOptionsDialog.this.colorPanel.setProps(appProps);
         EditorOptionsDialog.this.fontPanel.setProps(appProps);
         EditorOptionsDialog.this.otherOptionsPanel.setProps(appProps);
         EditorProperties.saveAppProps(appProps);
         if (e.getSource().equals(EditorOptionsDialog.this.okBtn)) {
            EditorOptionsDialog.this.processWindowEvent(new WindowEvent(EditorOptionsDialog.this, 201));
         }

         TextDocumentPadFactory.getFactory().reloadProperties();
      }
   }
}
