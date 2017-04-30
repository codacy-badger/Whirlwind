package cli;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Scanner;

import logic.Game;

public class Main {

	private static int GAMEMODE = 0;

	public static void main(String[] args) throws Exception {
		setConfigurations();
		Game game = new Game(GAMEMODE);
		int winner = game.startGame();

		if (winner == 1)
			System.out.println("Black Won!");
		else if (winner == 2)
			System.out.println("White Won!");
		else
			System.err.println("Error");
	}

	private static void setConfigurations() {
		if (selectDisplay() == 1)
			guiConfigs();
		else
			consoleConfigs();
	}

	private static int selectDisplay() {
		int answer = 0;
		System.out.println("If you want a Graphic display press 1");
		System.out.println("If you want a Console display press 2");

		while (answer != 1 && answer != 2)
			try {
				answer = -48 + System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}

		return answer;
	}

	private static void guiConfigs() {
		ConfigFrame configframe = new ConfigFrame();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					configframe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		do {
			GAMEMODE = configframe.getGAMEMODE();
			System.out.print("");
		} while (GAMEMODE == 0);
		configframe.dispose();
	}

	private static void consoleConfigs() {
		System.out.println("Choose the game mode:");
		System.out.println("1 - Human versus Human");
		System.out.println("2 - Human versus Computer");
		System.out.println("3 - Computer versus Computer");

		Scanner reader = new Scanner(System.in);

		while (GAMEMODE != 1 && GAMEMODE != 2 && GAMEMODE != 3) {
			GAMEMODE = reader.nextInt();
		}
	}
}
