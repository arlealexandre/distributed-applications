package ricm.nio.fileserver.threaded;

import ricm.nio.channels.IChannel;

public class Request {
	IChannel channel;
	byte[] bytes;
	Request(IChannel channel,byte[] bytes) {
		this.channel = channel;
		this.bytes = bytes;
	}
}