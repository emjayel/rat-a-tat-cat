// This could also be done by assigning "peek", etc., to int values, replacing the enum.
public class Card {

	private Type type;
	
	private boolean knownToAll;

	public Card(Type type) {
		knownToAll = false;
		this.type = type;
	}
	
	public enum Type {
		ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, PEEK, SWAP, DRAWTWO 
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	// TODO create separate "pick up" method
	public int findValue() {
		if ( type == Type.ZERO ) {
			return 0;
		} else if ( type == Type.ONE ) {
			return 1;
		} else if ( type == Type.TWO ) {
			return 2;
		} else if ( type == Type.THREE ) {
			return 3;
		} else if ( type == Type.FOUR ) {
			return 4;
		} else if ( type == Type.FIVE ) {
			return 5;
		} else if ( type == Type.SIX ) {
			return 6;
		} else if ( type == Type.SEVEN ) {
			return 7;
		} else if ( type == Type.EIGHT ) {
			return 8;
		} else if ( type == Type.NINE ) {
			return 9;
		} else {
			// If you want to change the value assigned to special cards, change this value.
			return 4;
		}
	}

	/** @return true if card <= 4 */
	public boolean lowValue(int lowPreference) {
		if ( findValue() <= lowPreference ) {
			return true;
		}
		return false;
	}

	public boolean isKnownToAll() {
		return knownToAll;
	}

	public boolean setKnownToAll(boolean knownToAll) {
		this.knownToAll = knownToAll;
		return knownToAll;
	}
}
