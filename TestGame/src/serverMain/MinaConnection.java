package serverMain;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public class MinaConnection extends NetConnection {
	private IoSession ioSession;
	private String ip;

	public MinaConnection(IoSession session) {
		ioSession = session;
		String[] remoteAddress = session.getRemoteAddress().toString().split(":");
		ip = remoteAddress[0];
		String[] localAddress = session.getLocalAddress().toString().split(":");
		info = new ConnectionInfo(remoteAddress[0], Integer.parseInt(remoteAddress[1]), Integer.parseInt(localAddress[1]));
	}

	// TODO:sendDataImpl
	protected void sendDataImpl(byte[] data, int offset, int count) {
		IoBuffer buffer = IoBuffer.allocate(count);
		buffer.put(data, offset, count);
		buffer.flip();
		if (ioSession != null && !ioSession.isClosing()) {
			ioSession.write(buffer);
		}
	}

	public void close() {
		if (ioSession != null)
			ioSession.close(true);
		ioSession = null;
	}

	public String getIP() {
		return ip;
	}

	public boolean isActive() {
		return ioSession != null;
	}
}
