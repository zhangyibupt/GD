package gameMain;

public class GameMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameServer gameServer = new GameServer(Config.GAME_SERVER_PORT);
		gameServer.start();
	}

}
