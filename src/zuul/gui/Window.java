package zuul.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import zuul.Game;
import zuul.io.Parser;

import java.util.*;

public class Window extends JFrame {
	private JTextField input;
	private JTextPane log;
	private Parser parser;
	
	private Game game;

	public Window(Game game) {
		super("World of Zuul");
		
		parser = new Parser();
		
		this.game = game;

		int screenH = (int) (Toolkit.getDefaultToolkit().getScreenSize()
				.getHeight() * 0.75);
		int screenW = (int) (Toolkit.getDefaultToolkit().getScreenSize()
				.getWidth() * 0.50);

		LayoutManager layout = new BorderLayout();

		JPanel fond = new JPanel();
		fond.setBackground(Color.white);
		fond.setLayout(layout);

		input = new JTextField();
		log = new JTextPane();
		
		InputListener il = new InputListener();
		
		input.addKeyListener(il);
		
		JScrollPane sp = new JScrollPane(log);

		fond.add(input, BorderLayout.PAGE_END);
		fond.add(sp, BorderLayout.CENTER);

		this.setSize(screenH, screenH);
		this.setResizable(false);

		setContentPane(fond);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void write(String msg) {
		try {
			log.getDocument().insertString(log.getDocument().getLength(), msg,
					null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void writeLine(String msg) {
		write(msg + "\n");
	}

	private class InputListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				String text = input.getText();
				if(!text.isEmpty())
					game.process(parser.getCommand(text));
				input.setText("");
			}

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}
}
