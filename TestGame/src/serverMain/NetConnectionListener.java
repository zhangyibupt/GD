package serverMain;

public interface  NetConnectionListener {
	public void messageArrived(NetConnection conn, Message msg);
}
