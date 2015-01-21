package zuul.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.util.*;

public class Window extends JFrame {
	private JTextField input;
	private JTextPane log;
	
	public Window()
	{
		super("World of Zuul");
		
		int screenH = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.50);
		int screenW = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.30);	
		
		LayoutManager layout = new BorderLayout();
		
		JPanel fond = new JPanel();
		fond.setBackground(Color.white);		
		fond.setLayout(layout);	
		
		input = new JTextField();
		log = new JTextPane();
		
		fond.add(input, BorderLayout.PAGE_END);
		fond.add(log, BorderLayout.CENTER);
		
		this.setSize(screenH , screenH);
		this.setResizable(false);
		
		setContentPane(fond);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void write(String msg)
	{
		try {			
			log.getDocument().insertString(log.getDocument().getLength(), msg, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void writeLine(String msg)
	{
		write(msg+"\n");
	}
}
