
public class Coveted {

	private Card coveted;

	private Player holder;

	private int index;

	public Coveted() {
		coveted = null;
		holder = null;
		index = -1;
	}

	public void updateCoveted(Card newCoveted, Player newHolder, int newIndex, Game game) {
		if ( coveted == null || newCoveted.findValue() <= coveted.findValue() ) {
			coveted = newCoveted;
			holder = newHolder;
			index = newIndex;
		}	
	}

	public Card getCoveted() {
		return coveted;
	}

	public Player getHolder() {
		return holder;
	}

	public int getIndex() {
		return index;
	}
}
