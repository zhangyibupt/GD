package serverMain;

public interface MessageEncoder {
	public ByteBuffer encode(Message msg);
}
