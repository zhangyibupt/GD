package serverMain;

public interface MessageCodecFactory {
	public MessageDecoder createDecoder();
	public MessageEncoder createEncoder();
}
