package zuul.io;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import zuul.io.Command;

/**
 * This class is part of the "World of Zuul" application. "World of Zuul" is a
 * very simple, text based adventure game.
 * <p/>
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and tries
 * to interpret the line as a two-word command. It returns the command as an
 * object of class Command.
 * <p/>
 * The parser has a set of known command words. It checks user input against the
 * known commands, and if the input is not one of the known commands, it returns
 * a command object that is marked as an unknown command.
 *
 * @author Nicolas Sarroche, Dorian Blanc
 *
 */
public class Parser {
	private Scanner reader; // source of command input

	/**
	 * Create a parser to read from the terminal window.
	 */
	public Parser() {
		reader = new Scanner(System.in);
	}

	/**
	 * @return The next command from the user.
	 */
	public Command getCommand() {
		String inputLine; // will hold the full input line
		String word1 = null;
		String word2 = "";

		System.out.print("> "); // print prompt

		inputLine = reader.nextLine();

		// Find up to two words on the line.
		Scanner tokenizer = new Scanner(inputLine);
		if (tokenizer.hasNext()) {
			word1 = tokenizer.next(); // get first word
			if (tokenizer.hasNext()) {
				while (tokenizer.hasNext()) {
					word2 += tokenizer.next() + " ";
				}
				word2 = word2.substring(0, word2.length() - 1);
				// get second word
				// note: we just ignore the rest of the input line.
			} else {
				word2 = null;
			}
		}

		return new Command(word1, word2);
	}

	/**
	 * Method used in Game class to get a String containing the player's name.
	 *
	 * @return String of the player's name.
	 */
	public String getPlayerName() {
		String inputLine; // will hold the full input line
		System.out.print("> "); // print prompt

		inputLine = reader.nextLine();
		if (inputLine.length() > 1) {
			String firstLetter = inputLine.substring(0, 1).toUpperCase();
			String rest = inputLine.substring(1).toLowerCase();
			return firstLetter + rest;
		}
		return inputLine;
	}

	/**
	 * @return The next command from the user.
	 */
	public Command getCommand(String input) {
		String command = "";
		String param = "";
		
		if(input.contains(" "))
		{
			command = input.substring(0, input.indexOf(" "));
			param = input.substring(command.length() + 1);
		}else{
			command = input;
		}
		
		return new Command(command, param);
	}
}
