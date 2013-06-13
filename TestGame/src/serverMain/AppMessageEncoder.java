package serverMain;

public class AppMessageEncoder implements MessageEncoder {
	public ByteBuffer encode(Message msg) {
		ByteBuffer buffer = new ByteBuffer(512);

		AppMessage appMsg = (AppMessage) msg;
		if (appMsg == null)
			appMsg = new AppMessage(AppMessage.MESSAGE_TYPE_NULL, new ByteBuffer(0), null);
		int type = appMsg.getType();
		// int leng = 1;
		ByteBuffer msgBuffer = appMsg.getBuffer();
		// int len = msgBuffer.length();
		int length = msgBuffer == null ? 0 : msgBuffer.available();

		buffer.writeInt(length);
		buffer.writeShort(type);
		if (msgBuffer != null) {
			int pos = msgBuffer.position();
			buffer.writeByteBuffer(msgBuffer, length);
			msgBuffer.position(pos);
		}

		return buffer;
	}

}