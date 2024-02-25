package ricm.nio.channels;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Channel implements IChannel {

	private IChannelListener listener;
	private Reader reader;
	private Writer writer;
	private SocketChannel socket;

	public Channel(SocketChannel sc) {
		this.socket = sc;
		this.writer = new Writer();
		this.reader = new Reader(this);
	}

	@Override
	public void setListener(IChannelListener l) {
		this.listener = l;
		reader.setChannelListener(l);
	}

	@Override
	public void send(byte[] bytes, int offset, int count) {
		
	}

	@Override
	public void send(byte[] bytes) {
		writer.sendMsg(bytes);
		try {
			writer.handleWrite(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean closed() {
		return !socket.isOpen();
	}

	public void read() throws IOException {
		reader.handleRead(socket);
	}
}
