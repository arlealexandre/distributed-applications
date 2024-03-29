package ricm.nio.fileserver.threaded;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

import ricm.nio.channels.*;

public class FileDownloadApplication implements IBrokerListener, IChannelListener {

	String server;
	int port;
	String filename;
	IBroker engine;
	boolean isText;

	public FileDownloadApplication(IBroker engine) {
		this.engine = engine;
		this.engine.setListener(this);
	}

	public void download(String hostname, int port, String filename, boolean isText) throws Exception {
		this.filename = filename;
		this.isText = isText;
		if (!this.engine.connect(hostname, port)) {
			System.err.println("Refused connect on "+port);
			System.exit(-1);
		}
	}

	@Override
	public void connected(IChannel c) {
		System.out.println("Connected");
		System.out.println("  downloading " + filename);
		c.setListener(this);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		try {
			dos.writeUTF(filename);
			dos.close();
			byte[] bytes = os.toByteArray();
			c.send(bytes);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	@Override
	public void accepted(IChannel c) {
		System.out.println("Unexpected accepted connection");
		System.exit(-1);
	}

	@Override
	public void refused(String host, int port) {
		System.out.println("Refused " + host + ":" + port);
		System.exit(-1);
	}

	@Override
	public void received(IChannel c, byte[] reply) {
		InputStream is = new ByteArrayInputStream(reply);
		DataInputStream dis = new DataInputStream(is);
		try {
			int nbytes = dis.readInt();
			if (nbytes < 0)
				System.out.println("Download failed: error code: " + nbytes);
			else {
				System.out.println("Download " + nbytes + " bytes");
				byte[] bytes = new byte[nbytes];
				dis.readFully(bytes);
			}
		} catch (Exception ex) {
			System.out.println(" failed parsing the received message");
			ex.printStackTrace();
		}
		c.close();
	}

	@Override
	public void closed(IChannel c, Exception e) {
		System.out.println("Unexpected closed channel");
		if (e!=null)
			e.printStackTrace();
		System.exit(-1);
	}

}
