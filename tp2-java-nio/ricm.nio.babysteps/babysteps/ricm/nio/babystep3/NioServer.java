package ricm.nio.babystep3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * NIO elementary server 
 * Implements an overly simplified echo server system
 * Polytech/Info4/AR 
 * Author: F. Boyer, O. Gruber
 */

public class NioServer {
	public static final int DEFAULT_SERVER_PORT = 8888;
	private static final int INBUFFER_SZ = 2048;

	// The server channel to accept connections from clients
	private ServerSocketChannel ssc;
	
	// The selection key to register events of interests on the server channel
	private SelectionKey skey;
	
	// Reader Automata
	private ReaderAutomata readerAutomata;
	
	private WriterAutomata wAutomata;

	// NIO selector
	private Selector selector;
	
	// Buffers for outgoing messages & incoming messages
	ByteBuffer outBuffer;
	ByteBuffer inBuffer;

	/**
	 * NIO server initialization
	 * 
	 * @param the host address and port of the server
	 * @throws IOException
	 */
	public NioServer(int port) throws IOException {

		// create a new selector
		selector = SelectorProvider.provider().openSelector();

		// create a new non-blocking server socket channel
		ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		
		readerAutomata = new ReaderAutomata();
		wAutomata = new WriterAutomata();

		// bind the server socket to the given address and port
		InetAddress hostAddress;
		hostAddress = InetAddress.getByName("localhost");
		InetSocketAddress isa = new InetSocketAddress(hostAddress, port);
		ssc.socket().bind(isa);

		// register a ACCEPT interest for channel ssc
		skey = ssc.register(selector, SelectionKey.OP_ACCEPT);
	}

	/**
	 * NIO mainloop Wait for selected events on registered channels Selected events
	 * for a given channel may be ACCEPT, CONNECT, READ, WRITE Selected events for a
	 * given channel may change over time
	 */
	public void loop() throws IOException {
		System.out.println("NioServer running");
		while (true) {
			// wait for some events
			selector.select();

			// get the keys for which the events occurred
			Iterator<?> selectedKeys = this.selector.selectedKeys().iterator();

			while (selectedKeys.hasNext()) {

				SelectionKey key = (SelectionKey) selectedKeys.next();
				selectedKeys.remove();
				
				// process the event
				if (key.isValid() && key.isAcceptable())  // accept event
					handleAccept(key);
				if (key.isValid() && key.isReadable())    // read event
					readerAutomata.handleRead(key);
				if (key.isValid() && key.isWritable())    // write event
					wAutomata.handleWrite(key);
				if (key.isValid() && key.isConnectable())  // connect event
					handleConnect(key);
			}
		}
	}

	/**
	 * Handle an accept event - accept the connection and make it non-blocking
	 * 
	 * @param the key of the channel on which a connection is requested
	 */
	private void handleAccept(SelectionKey key) throws IOException {
		assert (this.skey == key);
		assert (ssc == key.channel());
		SocketChannel sc;

		// do the actual accept on the server-socket channel
		// get a client channel as result
		sc = ssc.accept();
		sc.configureBlocking(false);
		
		// register a READ interest on sc to receive the message sent by the client
		sc.register(selector, SelectionKey.OP_READ);
	}

	/**
	 * Handle a connect event, this should never happen
	 * 
	 * @param the key of the channel on which a connection is requested
	 * @throws Error since this should never happen
	 */
	private void handleConnect(SelectionKey key) throws IOException {
		throw new Error("Unexpected connect");
	}

	/**
	 *  Request to send data
	 * 
	 * @param the key of the channel on which data that should be sent
	 * @param the data that should be sent
	 */
	public void send(SocketChannel sc, byte[] data, int offset, int count) {
		// this is not optimized, we should try to reuse the same ByteBuffer
		outBuffer = ByteBuffer.wrap(data, offset, count);

		// register a write interest for the given client socket channel
		SelectionKey key = sc.keyFor(selector);
		
		byte[] temp = new byte[count];
		
		for (int i = offset; i < offset + count; i++) {
			temp[i - offset] = data[i];
		}
		
		this.wAutomata.sendMsg(temp, key);
	}
	
	

	public static void main(String args[]) throws IOException {
		int serverPort = DEFAULT_SERVER_PORT;
		String arg;

		for (int i = 0; i < args.length; i++) {
			arg = args[i];
			if (arg.equals("-p")) {
				serverPort = new Integer(args[++i]).intValue();
			}
		}
		NioServer ns;
		ns = new NioServer(serverPort);
		ns.loop();
	}

}
