package ricm.nio.channels;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Broker implementation
 */

public class Broker implements IBroker {

	private IBrokerListener listener;

	@Override
	public void setListener(IBrokerListener l) {
		this.listener = l;
	}

	@Override
	public boolean connect(String host, int port) {
		try {
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(true);
			socketChannel.connect(new InetSocketAddress(host, port));

			handleConnection(socketChannel);

			return true;
		} catch (IOException e) {
			if (listener != null) {
				listener.refused(host, port);
			}
			return false;
		}
	}

	@Override
	public boolean accept(int port) {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(port));

			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private void handleConnection(SocketChannel socketChannel) {
		Channel channel = new Channel(socketChannel);
		if (listener != null) {
			listener.connected(channel);
		}
	}

	public void run() {
		// TODO Auto-generated method stub

	}

}
