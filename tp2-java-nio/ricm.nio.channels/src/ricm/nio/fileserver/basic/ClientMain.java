package ricm.nio.fileserver.basic;

import ricm.nio.channels.*;

public class ClientMain {

	public static final int DEFAULT_SERVER_PORT = 9999;
	
	public static void main(String args[]) throws Exception {
		String filename = ".project";
		String hostname = "localhost";
		int port = DEFAULT_SERVER_PORT;
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equals("-p")) {
				port = new Integer(args[++i]).intValue();
			} else if (arg.equals("-h")) {
				hostname = args[++i];
			} else if (arg.equals("-f")) {
				filename = args[++i];
			}
		}
		
		Broker engine = new Broker();
		
		FileDownloadApplication client = new FileDownloadApplication(engine);
		
		client.download(hostname, port,filename,true);
		
		engine.run();
	}
}
