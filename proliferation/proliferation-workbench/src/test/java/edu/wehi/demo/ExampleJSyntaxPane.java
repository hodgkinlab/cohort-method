package edu.wehi.demo;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import jsyntaxpane.DefaultSyntaxKit;



public class ExampleJSyntaxPane {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ExampleJSyntaxPane();
            }
        });
    }

    public ExampleJSyntaxPane() {
        JFrame f = new JFrame("Hello");
        final Container c = f.getContentPane();
        c.setLayout(new BorderLayout());

        DefaultSyntaxKit.initKit();


        final JEditorPane codeEditor = new JEditorPane();
        JScrollPane scrPane = new JScrollPane(codeEditor);
        c.add(scrPane, BorderLayout.CENTER);
        c.doLayout();
        codeEditor.setContentType("text/python");
        codeEditor.setText("def Hello:");
        
        f.setSize(800, 600);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}