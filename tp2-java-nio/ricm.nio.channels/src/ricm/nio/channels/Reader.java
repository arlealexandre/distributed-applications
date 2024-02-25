package ricm.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Reader {

	enum State {
		READ_LENGTH, READ_MESSAGE
	};

	private State currentState = State.READ_LENGTH;
	private ByteBuffer buffer;
	private int messageLength;
	private IChannelListener listener;
	private IChannel channel;

	public Reader(IChannel c) {
		this.buffer = ByteBuffer.allocate(4);
		this.channel = c;
	}
	
	public void setChannelListener(IChannelListener l) {
		this.listener = l;
	}

	public void handleRead(SocketChannel sc) throws IOException {
		int n = 0;
		switch (currentState) {
		case READ_LENGTH:

			n = sc.read(buffer);

			if (n == -1) {
				sc.close();
				return;
			}

			if (buffer.remaining() == 0) {
				buffer.rewind();
				messageLength = buffer.getInt();
				buffer.rewind();
				buffer = ByteBuffer.allocate(messageLength);
				currentState = State.READ_MESSAGE;
			}

			break;
		case READ_MESSAGE:
			n = sc.read(buffer);
			if (n == -1) {
				sc.close();
				return;
			}

			if (buffer.remaining() == 0) {
				byte[] data = new byte[buffer.position()];
				buffer.rewind();
				buffer.get(data);
				buffer = ByteBuffer.allocate(4);
				String stringMessage = new String(data);
				System.out.println("Received message: " + stringMessage);
				buffer.rewind();
				listener.received(channel, data);
			}
			break;
		default:
			break;
		}
	}

}
