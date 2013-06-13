package serverMain;

public class AppMessageCodecFactory implements MessageCodecFactory{
	public MessageDecoder createDecoder()
	{
		return new AppMessageDecoder();
	}
	public MessageEncoder createEncoder()
	{
		return new AppMessageEncoder();
	}
}
