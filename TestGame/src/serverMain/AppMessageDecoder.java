package serverMain;

public class AppMessageDecoder implements MessageDecoder {
	public Message decode(ByteBuffer buffer) {

//		int _length = buffer.available();
//		byte[] b = buffer.getBytes();
//		for (int i = 0; i < _length; i++) {
//			 System.out.println("buffer c[" + i + "]" + b[i] );
//		}

//		int b_length = buffer.length();
		if (buffer.available() < 6)
			return null;
		int position = buffer.position();
		int length = buffer.readInt();// 4
		int type = buffer.readUnsignedShort();// 2

//		int __length = buffer.length();

		if (buffer.available() < length) {
			buffer.position(position);
			return null;
		}
		if (length < 0) {
			System.out.println("nagative length:" + length);
			System.out.println("buffer available:" + buffer.available());
			System.out.println("type:" + type);
			return null;
		}
		AppMessage message = new AppMessage(type, length);
		message.getBuffer().writeByteBuffer(buffer, length);
		return message;
	}

	public Message decode(Message message, ByteBuffer buffer) {

		return null;
	}
}