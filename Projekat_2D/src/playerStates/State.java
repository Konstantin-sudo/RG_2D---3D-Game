package playerStates;

import objects.Player;

public abstract class State {
	protected Player player;
	
	protected State ( Player player ) {
		this.player = player;
	}
	
	public abstract void update ( );
}
