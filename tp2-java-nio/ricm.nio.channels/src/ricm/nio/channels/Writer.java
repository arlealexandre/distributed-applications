package ricm.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Writer {

	enum State {
		WRITE_LENGTH, WRITE_MESSAGE, WRITE_IDLE
	};

	ByteBuffer bb;
	byte[][] pendingMsg;
	State currentState;

	public Writer() {
		this.pendingMsg = new byte[0][];
		this.currentState = State.WRITE_IDLE;
	}

	public void sendMsg(byte[] msg) {
		if (this.currentState == State.WRITE_IDLE) {
			this.currentState = State.WRITE_LENGTH;
			this.bb = ByteBuffer.allocate(4);
			this.bb.rewind();
			this.bb.putInt(msg.length);
			this.bb.rewind();
		}
		byte[][] temp = new byte[this.pendingMsg.length + 1][];
		for (int i = 0; i < this.pendingMsg.length; i++) {
			temp[i] = this.pendingMsg[i];
		}
		temp[this.pendingMsg.length] = msg;
		this.pendingMsg = temp;
	}

	private void removeMessage() {
		byte[][] temp = new byte[this.pendingMsg.length - 1][];
		for (int i = 1; i < this.pendingMsg.length; i++) {
			temp[i - 1] = this.pendingMsg[i];
		}
		this.pendingMsg = temp;
	}

	public void handleWrite(SocketChannel sc) throws IOException {
		switch (this.currentState) {
		case WRITE_LENGTH:
			sc.write(bb);

			if (bb.remaining() == 0) {
				this.currentState = State.WRITE_MESSAGE;
				this.bb = ByteBuffer.wrap(this.pendingMsg[0]);
				this.removeMessage();
			}
			break;
		case WRITE_MESSAGE:
			sc.write(bb);

			if (bb.remaining() == 0) {
				if (this.pendingMsg.length == 0) {
					this.currentState = State.WRITE_IDLE;

				} else {
					this.currentState = State.WRITE_LENGTH;
					this.bb = ByteBuffer.allocate(4);
					this.bb.putInt(this.pendingMsg.length);
				}
			}
			break;
		case WRITE_IDLE:
		default:
			break;
		}
	}
}
