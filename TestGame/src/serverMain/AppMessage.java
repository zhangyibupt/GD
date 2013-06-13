package serverMain;

import java.io.Serializable;
/**
 * 大消息头
 * @author 
 *
 */
public class AppMessage extends Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int MESSAGE_TYPE_NULL = 0;

	/** 登录,注册,帐号安全相关 */
	public static final int MSG_AUTH = 0x100;

	private int type;

	public AppMessage(int type) {
		this(type, 256);
	}

	public AppMessage(int type, int size) {
		this(type, new ByteBuffer(size), null);
	}

	public AppMessage(int type, ByteBuffer buffer) {
		this(type, buffer, null);
	}

	public AppMessage(int type, ByteBuffer buffer, Object source) {
		super(buffer, source);
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public ByteBuffer getBuffer() {
		return (ByteBuffer) getContent();
	}

	public String toString() {
		return getClass().getName() + ":" + type + ";" + getBuffer().capacity();
	}
}