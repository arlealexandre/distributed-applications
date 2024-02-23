package ricm.nio.babystep2_3;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Iterator;

/**
 * NIO elementary client 
 * Implements an overly simplified echo client system
 * Polytech/Info4/AR 
 * Author: F. Boyer, O. Gruber
 */

public class NioClient {
	private static final int INBUF_SZ = 2048;

	// The client channel to communicate with the server 
	private SocketChannel sc;

	// The selection key to register events of interests on the channel
	private SelectionKey skey;

	// NIO selector
	private Selector selector;
	
	// Reader Automata
	private ReaderAutomata readerAutomata;
	private WriterAutomata writerAutomata;

	// Buffers for outgoing messages & incoming messages
	ByteBuffer outBuffer;

	// The message to send to the server
	byte[] first;  
	// The checksum of the last message sent to the server
	// Used to check if the received message is the same than the sent one
	byte[] digest;

	int nloops;

	/**
	 * NIO client initialization
	 * 
	 * @param serverName: the server name
	 * @param port: the server port
	 * @param payload: the message to send to the server
	 * @throws IOException
	 */
	public NioClient(String serverName, int port, byte[] payload) throws IOException {

		this.first = payload;

		// create a selector
		selector = SelectorProvider.provider().openSelector();

		// create a non-blocking socket channel
		sc = SocketChannel.open();
		sc.configureBlocking(false);

		// register a CONNECT interest for channel sc 
		skey = sc.register(selector, SelectionKey.OP_CONNECT);
		
		readerAutomata = new ReaderAutomata();
		writerAutomata = new WriterAutomata();
		
		// request to connect to the server
		InetAddress addr;
		addr = InetAddress.getByName(serverName);
		sc.connect(new InetSocketAddress(addr, port));
	}

	/**
	 * The client forever-loops on the NIO selector - waiting for events on 
	 * registered channels - possible events are CONNECT, READ, WRITE
	 */
	public void loop() throws IOException {
		System.out.println("NioClient running");
		while (true) {
			// wait for some events
			selector.select();

			// get the keys for which the events occurred
			Iterator<?> selectedKeys = this.selector.selectedKeys().iterator();
			while (selectedKeys.hasNext()) {
				SelectionKey key = (SelectionKey) selectedKeys.next();
				selectedKeys.remove();

				// process the event
				if (key.isValid() && key.isAcceptable())   // accept event
					handleAccept(key);
				if (key.isValid() && key.isReadable())     // read event
					readerAutomata.handleRead(key);
				if (key.isValid() && key.isWritable())     // write event
					writerAutomata.handleWrite(key);
				if (key.isValid() && key.isConnectable())  // connect event
					handleConnect(key);
			}
		}
	}

	/**
	 * Handle an accept event
	 * 
	 * @param the key of the channel on which a connection is requested
	 */
	private void handleAccept(SelectionKey key) throws IOException {
		throw new Error("Unexpected accept");
	}

	/**
	 * Handle a connect event - finish to establish the connection
	 * 
	 * @param the key of the channel on which a connection is requested
	 */
	private void handleConnect(SelectionKey key) throws IOException {
		assert (this.skey == key);
		assert (sc == key.channel());

		sc.finishConnect();
		skey.interestOps(SelectionKey.OP_WRITE);
		
		writerAutomata.sendMsg(first, key);

	}

	public static void main(String args[]) throws IOException {
		int serverPort = NioServer.DEFAULT_SERVER_PORT;
		String serverAddress = "localhost";
		String msg = "Hello there...";
		String arg;

		for (int i = 0; i < args.length; i++) {
			arg = args[i];
			if (arg.equals("-m")) {
				msg = args[++i];
			} else if (arg.equals("-p")) {
				serverPort = new Integer(args[++i]).intValue();
			} else if (arg.equals("-a")) {
				serverAddress = args[++i];
			}
		}
		byte[] bytes = msg.getBytes(Charset.forName("UTF-8"));
		NioClient nc = new NioClient(serverAddress, serverPort, bytes);
		nc.loop();
	}

	/*
	 * Wikipedia: The MD5 message-digest algorithm is a widely used hash function
	 * producing a 128-bit hash value. Although MD5 was initially designed to be
	 * used as a cryptographic hash function, it has been found to suffer from
	 * extensive vulnerabilities. It can still be used as a checksum to verify data
	 * integrity, but only against unintentional corruption. It remains suitable for
	 * other non-cryptographic purposes, for example for determining the partition
	 * for a particular key in a partitioned database.
	 */
	public static byte[] md5(byte[] bytes) throws IOException {
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes, 0, bytes.length);
			digest = md.digest();
		} catch (Exception ex) {
			throw new IOException(ex);
		}
		return digest;
	}

	public static boolean md5check(byte[] d1, byte[] d2) {
		if (d1.length != d2.length)
			return false;
		for (int i = 0; i < d1.length; i++)
			if (d1[i] != d2[i])
				return false;
		return true;
	}

	public static void echo(PrintStream ps, byte[] digest) {
		for (int i = 0; i < digest.length; i++)
			ps.print(digest[i] + ", ");
		ps.println();
	}

}
