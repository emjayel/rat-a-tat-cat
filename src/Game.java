import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Game {

	private Stack<Card> deck;

	private Stack<Card> discardPile; 

	/** publicly known and coveted card */
	private Coveted coveted;

	Player [] players;

	/** Constructor */
	public Game() {
		deck = new Stack<Card>();
		discardPile = new Stack<Card>();
		players = new Player[5];
		coveted = new Coveted();

		for ( int i = 0; i < 5; i++ ) {
			players[i] = new Player();
		}
	}
	
	public void setUpWar() {
		setUpBattle();
		for ( Player player : players ) {
			player.resetWarScore();
		}
	}

	public void setUpBattle() {	
		// Set up deck
		addToDeck(Card.Type.ZERO, 4);
		addToDeck(Card.Type.ONE, 4);
		addToDeck(Card.Type.TWO, 4);
		addToDeck(Card.Type.THREE, 4);
		addToDeck(Card.Type.FOUR, 4);
		addToDeck(Card.Type.FIVE, 4);
		addToDeck(Card.Type.SIX, 4);
		addToDeck(Card.Type.SEVEN, 4);
		addToDeck(Card.Type.EIGHT, 4);
		addToDeck(Card.Type.NINE, 9);
		addToDeck(Card.Type.PEEK, 3);
		addToDeck(Card.Type.DRAWTWO, 3);
		addToDeck(Card.Type.SWAP, 3);

		shuffle();
		
		

		// Deal initial hands
		for ( int i = 0; i < 4; i++ ) {
			players[0].getHand()[i] = getDeck().pop();
			players[1].getHand()[i] = getDeck().pop();
			players[2].getHand()[i] = getDeck().pop();
			players[3].getHand()[i] = getDeck().pop();
			players[4].getHand()[i] = getDeck().pop();
			if ( i == 0 || i == 3 ) {
				players[0].getKnownHand()[i] = players[0].getHand()[i];
				players[1].getKnownHand()[i] = players[1].getHand()[i];
				players[2].getKnownHand()[i] = players[2].getHand()[i];
				players[3].getKnownHand()[i] = players[3].getHand()[i];
				players[4].getKnownHand()[i] = players[4].getHand()[i];	
			}
		}
		
		// Reset relevant instance variables in Player objects */
		for ( Player player : players ) {
			player.updateAbhorred();
		} 

		// Set up discard pile
		boolean discardExists = false;
		while ( !discardExists ) {
			Card topCard = getDeck().pop();
			Card.Type topCardType = topCard.getType();
			if ( topCardType.equals(Card.Type.PEEK) || topCardType.equals(Card.Type.SWAP) || topCardType.equals(Card.Type.DRAWTWO) ){
				Random generator = new Random();
				getDeck().insertElementAt(topCard, generator.nextInt(getDeck().size()));
				continue;
			} 
			else {
				getDiscardPile().push(topCard);
				discardExists = true;
			}
		}	
	}

	public void addToDeck(Card.Type typeToAdd, int numberToAdd) {
		Card cardToAdd = new Card(typeToAdd);
		for ( int i = 0; i < numberToAdd; i++ ) {
			getDeck().add(cardToAdd);
		}
	}

	/** Fisher-Yates shuffling algorithm */
	public void shuffle() {
		if ( deck.isEmpty() ) {
			deck = discardPile;
		}
		
		Random generator = new Random();
		for ( int i = 0; i < getDeck().size(); i++ ) {
			int j = generator.nextInt(getDeck().size() - i) + i;
			Card hold = new Card(getDeck().get(j).getType());
			getDeck().set(j, getDeck().get(i));
			getDeck().set(i, hold);
		}
	}

	// TODO move this to game class?
	/** Return null if no one has won—otherwise, return player who won */
	public int determineWinner() {
		int playersOver99 = 0;
		int lowestScore = 136;
		int indexOfPlayerWithLowestScore = - 1;
		for ( int i = 0; i < 5; i++ ) {
			if ( players[i].getWarScore() > 99 ) {
				playersOver99++;
			}
			if ( players[i].getWarScore() < lowestScore ) {
				lowestScore = players[i].getWarScore();
				indexOfPlayerWithLowestScore = i;
			}
		}
		if ( playersOver99 >= 4 ) {
			return indexOfPlayerWithLowestScore;
		}
		return - 1;	
	}

	public Coveted getCoveted() {
		return coveted;
	}

	public Stack<Card> getDeck() {
		return deck;
	}
	
	public Stack<Card> getDiscardPile() {
		return discardPile;
	}

	public void setDeck(Stack<Card> discardPile2) {
		// TODO Auto-generated method stub
		
	}
}

