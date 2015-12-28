
public class Player {

	private Card [] hand;

	private Card [] knownHand;

	/** The index of the worst card the player knows they possess */
	private int abhorred;

	/** Assuming that unknown cards are of value 9, the "battle" score the user would wind up with */
	private int projectedScore;

	/** The total points collected by the player */
	private int warScore;

	/** The highest card the user considers low. Default value = 4 */
	private int lowPreference;

	/** True if user has called rat-a-tat, false otherwise */
	private boolean calledRatATatCat;

	/** Constructor */
	public Player() {
		hand = new Card [4];
		knownHand = new Card [4];
		updateAbhorred();
		updateProjectedScore();
		warScore = 0;
		setLowPreference(3);
		setCalledRatATatCat(false);
	}

	/** Automated turn */
	public void autoTurn(Game game) {
		Card pickedUp = autoPickUp(game);
		int newPlacementIndex = - 1;

		if ( pickedUp.getType() == Card.Type.SWAP ) {
			/*
			 * -----I'm getting odd null pointer exceptions from this.
			game.getDiscardPile().push(pickedUp);
			if ( game.getCoveted() != null && game.getCoveted().getCoveted().findValue() < hand[abhorred].findValue() && game.getCoveted().getCoveted().findValue() < 5 ) {
				Card sacrifice = null;
				// TODO also if card is lowValue, trade for mystery cards.
				Card plunder = game.getCoveted().getHolder().getHand()[game.getCoveted().getIndex()];
				if ( hand[abhorred].findValue() > 4 ) {
					sacrifice = hand[abhorred];
				} else {
					for ( int i = 0; i < 4; i++ ) {
						if ( knownHand[i] == null ) {
							sacrifice = knownHand[i];
						}
					}
				}
				hand[abhorred] = plunder;
				game.getCoveted().getHolder().getHand()[game.getCoveted().getIndex()] = sacrifice;

				// Reset coveted
				game.getCoveted().updateCoveted( plunder, this, abhorred, game );
			}
			 */
			// TODO deal with epistemic issues surrounding swaps
		} else if ( pickedUp.getType() == Card.Type.PEEK ) {
			game.getDiscardPile().push(pickedUp);
			for ( int i = 0; i < 4; i++ ) {
				if ( knownHand[i] == null ) {
					knownHand[i] = hand[i];
				}
			} 
		} else if ( pickedUp.lowValue( lowPreference ) ) {
			if ( hand[abhorred].findValue() > 4 ) {
				game.getDiscardPile().push(hand[abhorred]);
				hand[abhorred] = pickedUp;
				newPlacementIndex = abhorred;
			} else {
				for ( int i = 0; i < 4; i++ ) {
					if ( knownHand[i] == null ) {
						game.getDiscardPile().push(hand[i]);
						knownHand[i] = pickedUp;
						hand[i] = pickedUp;
						newPlacementIndex = i;
					}
				}
			}
		} else {
			if ( pickedUp.findValue() < getHand()[getAbhorred()].findValue() ) {
				hand[abhorred] = pickedUp;
				newPlacementIndex = abhorred;

			}
		}

		if ( pickedUp.isKnownToAll() == true ) {
			game.getCoveted().updateCoveted( pickedUp, this, newPlacementIndex, game );
			pickedUp.setKnownToAll(false);
		}

		updateAbhorred();
		updateProjectedScore();
		autoCallRatATatCat();
	}

	// TODO why does this stop working with low thresholds?
	public void autoCallRatATatCat() {
		if ( getProjectedScore() < 15 ) {
			callRatATatCat();
		}
	}

	/** Automated card pick up. Deals with DRAWTWO cards. */
	public Card autoPickUp(Game game) {
		Card pickedUp;
		if ( game.getDiscardPile() != null && game.getDiscardPile().size() > 0  && game.getDiscardPile().peek().lowValue( lowPreference ) ) {
			pickedUp = game.getDiscardPile().pop();
			pickedUp.setKnownToAll(true);
		} else {
			// TODO set up method in game to deal with shuffling
			if ( game.getDeck().isEmpty() ) {
				game.shuffle();
			}
			pickedUp = game.getDeck().pop();
		}

		/*
			// TODO decide how to handle special cards here
			// TODO why am I getting stackOverflow errors here?
			if ( pickedUp.getType() == Card.Type.DRAWTWO ) {
				Card toDiscard = pickedUp;
				game.discardPile.push(toDiscard);
				pickedUp = pickUp();
				if ( !lowValue(pickedUp) ) {
					toDiscard = pickedUp;
					game.discardPile.push(toDiscard);
					pickedUp = pickUp();
				}
			} */
		return pickedUp;
	}

	/** Sets calledRatATatCat to true */
	public void callRatATatCat() {
		setCalledRatATatCat(true);
	}
	
	public Card [] getHand() {
		return hand;
	}

	public void setHand(Card [] hand) {
		this.hand = hand;
	}

	public Card [] getKnownHand() {
		return knownHand;
	}

	public void setKnownHand(Card [] knownHand) {
		this.knownHand = knownHand;
	}

	public int getAbhorred() {
		return abhorred;
	}

	/** Set "abhorred" to highest known card in possession. */
	public void updateAbhorred() {
		int formerAbhorred = abhorred;
		for ( int i = 0; i < 4; i++ ) {
			if ( knownHand[i] != null && knownHand[i].findValue() > hand[formerAbhorred].findValue() ) {
				abhorred = i;
			}
		}
	}
	
	/** Returns the actual "battle" score */
	public int getBattleScore() {
		int battleScore = 0;
		for ( Card card : hand ) {
			battleScore += card.findValue();
		}
		return battleScore;
	}

	/** Set "war" score to 0 */
	public void resetWarScore() {
		warScore = 0;
	}

	/** Update "war" score at the end of a "battle" */
	public void updateWarScore() {
		warScore += getBattleScore();
	}

	/** Getter for warScore */
	public int getWarScore() {
		return warScore;
	}

	public int getProjectedScore() {
		return projectedScore;
	}

	public void updateProjectedScore() {
		projectedScore = 0;
		for ( int i = 0; i < 4; i++ ) {
			if ( knownHand[i] == null ) {
				// Maximum possible score
				projectedScore += 9;
			} else {
				projectedScore += knownHand[i].findValue();
			}
		}
	}
	
	public int getLowPreference() {
		return lowPreference;
	}

	public void setLowPreference(int lowPreference) {
		this.lowPreference = lowPreference;
	}

	public boolean getCalledRatATatCat() {
		return calledRatATatCat;
	}

	public void setCalledRatATatCat(boolean calledRatATatCat) {
		this.calledRatATatCat = calledRatATatCat;
	}
}
