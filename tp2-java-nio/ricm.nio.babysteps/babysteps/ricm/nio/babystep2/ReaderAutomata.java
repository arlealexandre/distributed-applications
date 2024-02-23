package ricm.nio.babystep2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ReaderAutomata {

	enum State {READ_LENGTH, READ_MESSAGE};
	
	private State currentState = State.READ_LENGTH;
	private ByteBuffer buffer;
	private int messageLength;
	
	public ReaderAutomata() {
		this.buffer = ByteBuffer.allocate(4);
	}
	
	public void handleRead(SelectionKey key) throws IOException {
		SocketChannel sc = (SocketChannel) key.channel();
		int n = 0;
		switch (currentState) {
			case READ_LENGTH:
				
				n = sc.read(buffer);
				System.out.println("Nb read for length = "+n);
				System.out.println("Remaining = "+buffer.remaining());
				
				if (n == -1) {
					key.cancel();
					sc.close(); 
					return;
				}
				
				if (buffer.remaining()==0) {
					buffer.rewind();
					messageLength = buffer.getInt();
					System.out.println("Length = "+messageLength);
					buffer = ByteBuffer.allocate(messageLength);
					currentState = State.READ_MESSAGE;
				}
				
				break;
			case READ_MESSAGE:
				n = sc.read(buffer);
				System.out.println("index buffer = "+buffer.remaining());
				if (n == -1) {
					key.cancel();
					sc.close(); 
					return;
				}
				
				if (buffer.remaining()==0) {
					byte[] data = new byte[buffer.position()];
					buffer.rewind();
					buffer.get(data);
					buffer = ByteBuffer.allocate(4);
					System.out.println("Received message: "+data.toString());
				}
				break;
			default:
				break;
		}
	}

}
