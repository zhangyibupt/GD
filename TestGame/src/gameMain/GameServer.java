package gameMain;

import serverMain.AbstractNetServer;
import serverMain.AppMessage;
import serverMain.AppMessageCodecFactory;
import serverMain.ByteBuffer;
import serverMain.Message;
import serverMain.MessageCode;
import serverMain.NetConnection;

public class GameServer extends AbstractNetServer {

	public String s_ip = "";

	public GameServer(int port) {
		super(port, new AppMessageCodecFactory());
	}

	@Override
	public void connectionOpened(NetConnection connection) {
		_log.debug("connectionOpened=" + connection.getIP() + " PingTime=" + connection.getPingTime());
		this.s_ip = connection.getIP();
		connection.addListener(this);
		connection.setTimeout(Config.GAME_SERVER_TIMEOUT);
		connection.setPingTime(Config.GAME_SERVER_PINGTIME);
		connection.setMaxBufferSize(Config.GAME_SERVER_MAXBUFFERSIZE);
	}

	@Override
	public void connectionClosed(NetConnection connection) {
		_log.debug("connectionClosed=" + connection.getIP() + " PingTime=" + connection.getPingTime());
		if (connection.getAttachment() == null)
			return;

		if (connection.getAttachment() instanceof Role) {// 有可能是通行证
			Role roleController = (Role) connection.getAttachment();
			//role操作
		}
	}

	@Override
	public void messageArrived(NetConnection conn, Message msg) {
		if (isRunning()) {
			AppMessage appMsg = (AppMessage) msg;
			if (appMsg.getType() == AppMessage.MESSAGE_TYPE_NULL) {
				ByteBuffer byteBuffer = new ByteBuffer();
				byteBuffer.writeShort(MessageCode.TYPE_PENG);
				int tempTime = (int) (System.currentTimeMillis() / 1000);
				byteBuffer.writeInt(tempTime);
				appMsg = new AppMessage(AppMessage.MESSAGE_TYPE_NULL, byteBuffer);
				conn.sendMessage(appMsg);// 回送一个空消息
				return;
			}
			messageArrivedImpl(conn, appMsg);
		}
	}

	@Override
	public void messageArrivedImpl(NetConnection conn, AppMessage appMsg) {
		int type = appMsg.getType();
		ByteBuffer buffer = appMsg.getBuffer();
		switch (type) {
			case AppMessage.MSG_AUTH:
				break;
		}
		if (conn.getAttachment() == null || !(conn.getAttachment() instanceof Role)) {// 未执行登录操作
			return;
		}
		Role roleController = (Role) conn.getAttachment();
	}

	public void stop() {
		super.stop();
		
	}

}