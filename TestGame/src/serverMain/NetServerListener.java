package serverMain;

public interface NetServerListener {
	public void connectionOpened(NetConnection connection);

	public void connectionClosed(NetConnection connection);
}
