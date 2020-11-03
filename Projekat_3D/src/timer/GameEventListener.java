package timer;

public interface GameEventListener {
	public void onGameWon ( double width, double height );
	public void onGameLost ( double width, double height );
}
