import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * Details: 
 * 1. if special card drawn on first turn, add to random spot in deck
 * 2. deal with swap
 * 3. make number of players flexible
 * 4. check that shuffle algorithm works
 * 5. obscure cards that user shouldn't see (this is a final step)
 * 6. Why can there be multiple "0" scores in one round?
 * 
 */

/**
 * The GUI for this project.
 * @author micahlatty
 *
 */
// TODO set default radio button position to "3"
// TODO why does this give player 1 an advantage? Check shuffling, esp.
// Streamline printing to text areas—right now, printing is clumsy and slows down execution considerably
public class GUI extends JFrame implements ActionListener {

	/** The game */
	Game game;

	/** A button to start the simulation */
	JButton simStartB;

	/** A button to run the simulation 1000 times */
	JButton run1000TimesB;

	/** A button representing the deck */
	JButton deckB;

	/** A button representing the discard pile */
	JButton discardPileB;

	/** An ButtonGroup array to hold ButtonGroups containing JRadioButtons */
	ButtonGroup [] buttonGroupArray;
	
	JPanel protagonistP;
	
	JPanel antagonistsP;

	/** Arrays of buttons, representing the cards possessed by each player */
	JButton [] player1Hand;
	JButton [] player2Hand;
	JButton [] player3Hand;
	JButton [] player4Hand;
	JButton [] player5Hand;

	/** TextAreas for scores in "Play" tab */
	JTextArea player1ScoreTA;
	JTextArea player2ScoreTA;
	JTextArea player3ScoreTA;
	JTextArea player4ScoreTA;
	JTextArea player5ScoreTA;

	/** TextAreas for scores in "Simulate tab */
	JTextArea sim1ScoreTA;
	JTextArea sim2ScoreTA;
	JTextArea sim3ScoreTA;
	JTextArea sim4ScoreTA;
	JTextArea sim5ScoreTA;

	/** A button to call rat-a-tat */
	JButton callRatATatCatB;
	
	/** A boolean that is true except for the first time the "Start Simulation" button is pressed. */
	boolean rerun = false;

	/** Constructor for GUI class */
	public GUI() {
		setTitle("Rat-a-tat-cat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		game = new Game();

		JPanel mainPanel = new JPanel();
		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 450));

		JTabbedPane mainTabbedPane = new JTabbedPane();
		mainPanel.add(mainTabbedPane, BorderLayout.CENTER);

		JTabbedPane playTabbedPane = new JTabbedPane();
		mainTabbedPane.add("Play game!", playTabbedPane);

		// Table panel
		JPanel tableP = new JPanel();
		tableP.setLayout(new BorderLayout());
		playTabbedPane.addTab("Table", tableP);
		
		JPanel masterP = new JPanel();
		masterP.setLayout(new GridLayout(1, 4));
		tableP.add(masterP, BorderLayout.CENTER);

		deckB = new JButton("DECK (click on this to begin game)");
		deckB.addActionListener(this);
		deckB.setBackground(Color.RED);
		deckB.setOpaque(true);

		discardPileB = new JButton("discardPile");
		discardPileB.setBackground(Color.RED);
		discardPileB.setOpaque(true);

		JButton callRatATatCatB = new JButton("Rat-A-Tat-Cat!");
		callRatATatCatB.addActionListener(this);

		JButton notYetB = new JButton("Nahhh, not yet...");

		masterP.add(deckB);
		masterP.add(discardPileB);
		masterP.add(callRatATatCatB);
		masterP.add(notYetB);

		// Add hero's hand to Border.SOUTH part of table
		protagonistP = new JPanel();
		protagonistP.setLayout(new GridLayout(1, 5));
		tableP.add(protagonistP, BorderLayout.SOUTH);

		player1Hand = new JButton [4];
		protagonistP.add(new JLabel("My Kingdom"));
		initalizeProtagonistHand(player1Hand);

		// Add opposing hands to Border.NORTH part of table
		antagonistsP = new JPanel();
		antagonistsP.setLayout(new GridLayout(4, 5));
		tableP.add(antagonistsP, BorderLayout.NORTH);

		player2Hand = new JButton [4];
		player3Hand = new JButton [4];
		player4Hand = new JButton [4];
		player5Hand = new JButton [4];
		antagonistsP.add(new JLabel("Sherry"));
		initalizeAntagonistHands(player2Hand);
		antagonistsP.add(new JLabel("Lloyd"));
		initalizeAntagonistHands(player3Hand);
		antagonistsP.add(new JLabel("Iesha"));
		initalizeAntagonistHands(player4Hand);
		antagonistsP.add(new JLabel("Meraiah"));
		initalizeAntagonistHands(player5Hand);

		// Scores panel 
		JPanel scoresP = new JPanel();
		scoresP.setLayout(new GridLayout(1, 5));
		playTabbedPane.addTab("Scores", scoresP);

		player1ScoreTA = new JTextArea(20, 20);
		player1ScoreTA.setText("Micah—" + "\n");
		scoresP.add(player1ScoreTA);

		player2ScoreTA = new JTextArea(20, 20);
		player2ScoreTA.setText("Sherry—" + "\n");
		scoresP.add(player2ScoreTA);

		player3ScoreTA = new JTextArea(20, 20);
		player3ScoreTA.setText("Lloyd—" + "\n");
		scoresP.add(player3ScoreTA);

		player4ScoreTA = new JTextArea(20, 20);
		player4ScoreTA.setText("Iesha—" + "\n");
		scoresP.add(player4ScoreTA);

		player5ScoreTA = new JTextArea(20, 20);
		player5ScoreTA.setText("Meraiah—" + "\n");
		scoresP.add(player5ScoreTA);

		// Simulation panel
		JPanel simulationP = new JPanel();
		simulationP.setLayout(new GridLayout(2, 1));
		mainTabbedPane.add("Simulate game!", simulationP);
		
		JPanel settingsP = new JPanel();
		settingsP.setLayout(new GridLayout(7, 2));
		simulationP.add(settingsP);

		JPanel blank = new JPanel();
		JLabel radioButtonL = new JLabel("What does each player consider a low card?");
		settingsP.add(blank);
		settingsP.add(radioButtonL);

		// Populate settingsP
		buttonGroupArray = new ButtonGroup[5];

		// TODO why doesn't foreach method work here? 
		// It results in a null pointer in the following forloop
		for ( int i = 0; i < 5; i++ ) {
			buttonGroupArray[i] = new ButtonGroup();
		}

		for ( int i = 1; i < 6; i++ ) {
			JLabel playerL = new JLabel("Player " + i);
			JPanel radioButtonP = new JPanel();
			radioButtonP.setLayout(new GridLayout(1, 5));
			for ( int j = 1; j < 6; j++ ) {
				JRadioButton radioButton = new JRadioButton("" + j);
				radioButton.setText("" + j);
				radioButton.addActionListener(this);
				if ( j == 3 ) {
					radioButton.setSelected(true);
				}
				
				buttonGroupArray[i - 1].add(radioButton);
				radioButtonP.add(radioButton);
			}
			settingsP.add(playerL);
			settingsP.add(radioButtonP);
		}

		simStartB = new JButton("START SIMULATION");
		simStartB.addActionListener(this);
		run1000TimesB = new JButton("Try running it 50 times!");
		run1000TimesB.addActionListener(this);
		settingsP.add(simStartB);
		settingsP.add(run1000TimesB);

		JPanel simScoresP = new JPanel();
		simScoresP.setLayout(new GridLayout(1, 5));
		simulationP.add(simScoresP);

		sim1ScoreTA = new JTextArea(20, 20);
		simScoresP.add(sim1ScoreTA);

		sim2ScoreTA = new JTextArea(20, 20);
		simScoresP.add(sim2ScoreTA);

		sim3ScoreTA = new JTextArea(20, 20);
		simScoresP.add(sim3ScoreTA);

		sim4ScoreTA = new JTextArea(20, 20);
		simScoresP.add(sim4ScoreTA);

		sim5ScoreTA = new JTextArea(20, 20);
		simScoresP.add(sim5ScoreTA);
		
		resetSimScoreTextAreas();

		pack();
		setVisible(true);
	}

	private void resetSimScoreTextAreas() {
		sim1ScoreTA.setText("Micah—" + "\n");
		sim2ScoreTA.setText("Sherry—" + "\n");
		sim3ScoreTA.setText("Lloyd—" + "\n");
		sim4ScoreTA.setText("Iesha—" + "\n");
		sim5ScoreTA.setText("Meraiah—" + "\n");
	}

	// TODO there must be a more efficient way to do this than with two near-identical methods.
	public void initalizeProtagonistHand( JButton [] hand ) {
		for ( int i = 0; i < 4; i++ ) {
			JPanel panel = new JPanel();
			hand[i] = new JButton();
			panel.add(hand[i]);
			// hand[i].addActionListener(this);
			hand[i].setOpaque(true);
			// hand[i].setBorderPainted(false); 
			protagonistP.add(panel);	
		}
	}

	/** Sets up "hand" buttons */
	public void initalizeAntagonistHands( JButton [] hand ) {
		for ( int i = 0; i < 4; i++ ) {
			JPanel panel = new JPanel();
			hand[i] = new JButton();
			panel.add(hand[i]);
			// hand[i].addActionListener(this);
			hand[i].setOpaque(true);
			// hand[i].setBorderPainted(false); 
			antagonistsP.add(panel);	
		}
	}

	/** Labels cards (buttons) in "hands" */
	public void labelButtons() {
		for ( int i = 0; i < 4; i++ ) {
			player1Hand[i].setText(game.players[0].getHand()[i].getType().name());
			player2Hand[i].setText(game.players[1].getHand()[i].getType().name());
			player3Hand[i].setText(game.players[2].getHand()[i].getType().name());
			player4Hand[i].setText(game.players[3].getHand()[i].getType().name());
			player5Hand[i].setText(game.players[4].getHand()[i].getType().name());
		}
		discardPileB.setText(game.getDiscardPile().peek().getType().name());
	}

	// TODO method that obscures and unveils cards at appropriate times.

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource().equals(deckB)) {
			// TODO fix and use playGame() method
			simulateGame();
		}

		// TODO rename these variables according to personal style
		if ( e.getSource().equals(simStartB)) {
			if ( !rerun ) {
				simStartB.setText("Rerun game!");
				for ( int i = 0; i < buttonGroupArray.length; i++ ) {
					Enumeration<AbstractButton> en = buttonGroupArray[i].getElements();
					while(en.hasMoreElements()) {
						JRadioButton rb = (JRadioButton) en.nextElement();
						if(rb.isSelected()) {
							game.players[i].setLowPreference(Integer.parseInt(rb.getText())); 
						}
					}
				}
				
				int indexOfWinner = simulateGame();
				crownWinner(indexOfWinner);
				rerun = true;
			}
			
			else {
				revokeCrown();
				simStartB.setText("Rerun game!");
				
				for ( int i = 0; i < buttonGroupArray.length; i++ ) {
					Enumeration<AbstractButton> en = buttonGroupArray[i].getElements();
					while(en.hasMoreElements()) {
						JRadioButton rb = (JRadioButton) en.nextElement();
						if(rb.isSelected()) {
							game.players[i].setLowPreference(Integer.parseInt(rb.getText())); 
							System.out.println("Player " + ( i + 1) + " considers " + rb.getText() + " low" );
						}
					}
				}

				int indexOfWinner = simulateGame();
				crownWinner(indexOfWinner);
			}
		}	
		
		if ( e.getSource().equals(run1000TimesB)) {
			runSimXTimes(50);
		}

	}

	private void runSimXTimes(int x) {
		revokeCrown();
		simStartB.setText("Rerun game!");
		
		for ( int i = 0; i < buttonGroupArray.length; i++ ) {
			Enumeration<AbstractButton> en = buttonGroupArray[i].getElements();
			while(en.hasMoreElements()) {
				JRadioButton rb = (JRadioButton) en.nextElement();
				if(rb.isSelected()) {
					game.players[i].setLowPreference(Integer.parseInt(rb.getText())); 
				}
			}
		}
		
		int [] wins = new int [5];
		
		for ( int i = 0; i < x; i++) {
			int indexOfWinner = simulateGame();
			wins[indexOfWinner]++;
		}

		// TODO deal with equal number of wins
		int mostWins = 0;
		
		for ( int i = 0; i < 5; i++ ) {

			if ( wins[i] > mostWins ) {
				mostWins = wins[i];
			}
		}

		resetSimScoreTextAreas();
		sim1ScoreTA.append(wins[0] + "\n");
		sim2ScoreTA.append(wins[1] + "\n");
		sim3ScoreTA.append(wins[2] + "\n");
		sim4ScoreTA.append(wins[3] + "\n");
		sim5ScoreTA.append(wins[4] + "\n");

		crownWinner(mostWins);
	}

	
	/** Play game */
	public void playGame() {
		// TODO numberOfPlayers variable to change size of loop?
		game.setUpWar();
		labelButtons();
		int turn;
		int indexOfWinner = - 1;
		while ( indexOfWinner == - 1 ) {
			// TODO check logic of "turns"
			for ( turn = 0; turn < 5; turn++ ) {
				if ( !game.players[turn].getCalledRatATatCat() ) {
					game.players[turn].autoTurn(game);
					labelButtons();
				} else {
					for ( int i = 0; i < 5; i++ ) {
						game.players[i].updateWarScore();
					}

					// Present current scores to user
					player1ScoreTA.append(game.players[0].getWarScore() + "\n");
					player2ScoreTA.append(game.players[1].getWarScore() + "\n");
					player3ScoreTA.append(game.players[2].getWarScore() + "\n");
					player4ScoreTA.append(game.players[3].getWarScore() + "\n");
					player5ScoreTA.append(game.players[4].getWarScore() + "\n");

					// Reset "calledRatATatCat" for player who called it
					game.players[turn].setCalledRatATatCat(false);

					// Reset game
					game.setUpBattle();

					// Re-label buttons
					labelButtons();
					break;
				}
			}
			indexOfWinner = game.determineWinner();
		}
	}

	// No printing to GUI should happen here. Return GameData object, with 2D array of scores and index of winner.
	/** Simulate game. Return index of winner and 2D array of scores. */
	public int simulateGame() {
		resetSimScoreTextAreas();
		game.setUpWar();
		labelButtons();
		int indexOfWinner = - 1;
		int turn;

		while ( indexOfWinner == - 1 ) {
			// TODO check logic of "turns"
			for ( turn = 0; turn < 5; turn++ ) {
				if ( !game.players[turn].getCalledRatATatCat() ) {
					game.players[turn].autoTurn(game);
					labelButtons();
				} else {
					for ( int i = 0; i < 5; i++ ) {
						game.players[i].updateWarScore();
					}

					// Present current scores to user
					appendSimScoresToTextAreas();

					// Reset "calledRatATatCat" for player who called it
					game.players[turn].setCalledRatATatCat(false);

					// Reset game
					game.setUpBattle();

					// Re-label buttons
					labelButtons();
					break;
				}
			}
			indexOfWinner = game.determineWinner();
		}
		return indexOfWinner;
	}

	private void appendSimScoresToTextAreas() {
		resetSimScoreTextAreas();
		sim1ScoreTA.append(game.players[0].getWarScore() + "\n");
		sim2ScoreTA.append(game.players[1].getWarScore() + "\n");
		sim3ScoreTA.append(game.players[2].getWarScore() + "\n");
		sim4ScoreTA.append(game.players[3].getWarScore() + "\n");
		sim5ScoreTA.append(game.players[4].getWarScore() + "\n");
	}
	
	/** Set text area holding winner's scores yellow. */
	private void crownWinner(int indexOfWinner) {
		switch ( indexOfWinner ) {
			case 0: sim1ScoreTA.setBackground(Color.YELLOW);
				break;
			case 1: sim2ScoreTA.setBackground(Color.YELLOW);
				break;
			case 2: sim3ScoreTA.setBackground(Color.YELLOW);
				break;
			case 3: sim4ScoreTA.setBackground(Color.YELLOW);
				break;
			case 4: sim5ScoreTA.setBackground(Color.YELLOW);
				break;
		}
	}
	
	/** Reset all simulation score text areas white. */
	private void revokeCrown() {
		sim1ScoreTA.setBackground(Color.WHITE);
		sim2ScoreTA.setBackground(Color.WHITE);
		sim3ScoreTA.setBackground(Color.WHITE);
		sim4ScoreTA.setBackground(Color.WHITE);
		sim5ScoreTA.setBackground(Color.WHITE);
	}
	
	/** The main */
	public static void main(String [] args) {
		GUI newGUI = new GUI();
	}
}

