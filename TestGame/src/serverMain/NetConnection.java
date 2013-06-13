package serverMain;

import java.util.ArrayList;

public abstract class NetConnection {

	public static class ConnectionInfo {

		private String remoteAddress;

		private int remotePort;

		private int localPort;

		private RemoteType remoteType;// 因为无法区分连接上来的设备,只有通过登录时客户端自主发送补全此信息

		public ConnectionInfo(String addr, int port1, int port2) {
			remoteAddress = addr;
			remotePort = port1;
			localPort = port2;
		}

		public String getRemoteAddress() {
			return remoteAddress;
		}

		public int getRemotePort() {
			return remotePort;
		}

		public int getLocalPort() {
			return localPort;
		}

		public RemoteType getRemoteType() {
			return remoteType;
		}

		public void setRemoteType(RemoteType remoteType) {
			this.remoteType = remoteType;
		}

		/**
		 * 客户端类型
		 * 
		 * @author 
		 * 
		 */
		public static enum RemoteType {
			MOBILE(1), WEB(2);

			private int id;

			private RemoteType(int id) {
				this.id = id;
			}

			public int getId() {
				return id;
			}

			public static final RemoteType getRemoteTypeById(int id) {
				for (RemoteType am : RemoteType.values())
					if (am.getId() == id)
						return am;
				return null;
			}
		}
	}

	private ArrayList<NetConnectionListener> listeners = new ArrayList<NetConnectionListener>();

	private Object attachment;

	private long id;

	private MessageDecoder messageDecoder;

	private MessageEncoder messageEncdoer;

	private MessageQueue messageQueue;

	private ByteBuffer readBuffer = new ByteBuffer(512);

	private int maxBufferSize = Integer.MAX_VALUE;

	protected long idleTime;

	protected int pingTime = 0;

	protected int timeout = 0;

	private int hungTime = 0;

	protected ConnectionInfo info;

	public void addListener(NetConnectionListener l) {
		listeners.add(l);
	}

	public void removeListener(NetConnectionListener l) {
		listeners.remove(l);
	}

	public void setListener(NetConnectionListener l) {
		listeners.clear();
		listeners.add(l);
	}

	public void removeAllListeners() {
		listeners.clear();
	}

	public NetConnectionListener[] getAllListeners() {
		NetConnectionListener[] array = new NetConnectionListener[listeners.size()];
		listeners.toArray(array);
		return array;
	}

	public int getHungTime() {
		return hungTime;
	}

	public void setHungTime(int hungTime) {
		this.hungTime = hungTime;
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public void setAttachment(Object o) {
		attachment = o;
	}

	public Object getAttachment() {
		return attachment;
	}

	public MessageDecoder getMessageDecoder() {
		return messageDecoder;
	}

	public void setMessageDecoder(MessageDecoder decoder) {
		messageDecoder = decoder;
	}

	public MessageEncoder getMessageEncoder() {
		return messageEncdoer;
	}

	public void setMessageEncoder(MessageEncoder encoder) {
		messageEncdoer = encoder;
	}

	public void setMessageQueue(MessageQueue queue) {
		messageQueue = queue;
	}

	public MessageQueue getMessageQueue() {
		return messageQueue;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int time) {
		timeout = time;
	}

	public int getPingTime() {
		return pingTime;
	}

	public void setPingTime(int time) {
		pingTime = time;
	}

	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	public void setMaxBufferSize(int size) {
		maxBufferSize = size;
	}

	public ConnectionInfo getConnectionInfo() {
		return info;
	}

	public void sendMessage(Message msg) {
		if (messageEncdoer == null)
			return;
		ByteBuffer sendBuffer = messageEncdoer.encode(msg);
		sendData(sendBuffer.getRawBytes(), 0, sendBuffer.length());
	}

	public void sendData(byte[] data) {
		sendData(data, 0, data.length);
	}

	public void sendData(byte[] data, int offset, int count) {
		// int ubound = offset + count;
		// for (int i = offset; i < ubound; i++)
		// {
		// data[i] ^= code;
		// }
		sendDataImpl(data, offset, count);
		// for (int i = offset; i < ubound; i++)
		// {
		// data[i] ^= code;
		// }
	}

	protected abstract void sendDataImpl(byte[] data, int offset, int count);

	public abstract void close();

	public abstract String getIP();

	public abstract boolean isActive();

	public void idle(long currentTime) {
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		if (idleTime == 0) {
			idleTime = currentTime;
		} else {
			if (pingTime > 0 && currentTime - idleTime > pingTime) {
				sendMessage(null);
			}
			if (timeout > 0 && idleTime > 0 && currentTime - idleTime > timeout) {
				close();
			}
		}
	}

	protected void onDataRead(byte[] data, int offset, int count) {
		// int ubound = offset + count;
		// for (int i = offset; i < ubound; i++)
		// {
		// data[i] ^= code;
		// }
		idleTime = 0;
		if (messageDecoder != null) {
			readBuffer.writeBytes(data, offset, count);
			Message msg = messageDecoder.decode(readBuffer);
			while (msg != null) {
				msg.setSource(this);
				if (messageQueue != null)
					messageQueue.post(msg);
				else
					dispatchMessage(msg);
				msg = messageDecoder.decode(readBuffer);
			}
			readBuffer.pack();
		}
		// 当前的缓存数据大于所指定值，客户有乱发数据的嫌疑，则关闭
		if (readBuffer.available() > maxBufferSize) {
			readBuffer.clear();
			close();
		}
	}

	public void dispatchMessage(Message message) {
		for (int i = 0; i < listeners.size(); i++) {
			NetConnectionListener l = (NetConnectionListener) listeners.get(i);
			l.messageArrived(this, message);
		}
	}
}