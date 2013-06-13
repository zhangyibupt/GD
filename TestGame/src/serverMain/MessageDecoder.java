package serverMain;

public interface MessageDecoder {
	public Message decode(ByteBuffer buffer);
	public Message decode(Message message, ByteBuffer buffer);
}
