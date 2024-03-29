package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;

/*
 * This class allows to build an object representing an HTTP static request
 */
public class HttpStaticRequest extends HttpRequest {
	static final String DEFAULT_FILE = "index.html";

	public HttpStaticRequest(HttpServer hs, String method, String ressname) throws IOException {
		super(hs, method, ressname);
	}

	public void process(HttpResponse resp) throws Exception {

		if (m_method.equals("GET")) {

			File f = new File(m_hs.getFolder().getName() + m_ressname);
			if (f.exists() && f.isFile()) {
				resp.setReplyOk();
				resp.setContentType(getContentType(m_ressname));
				resp.setContentLength((int) f.length());
				PrintStream ps = resp.beginBody();
				buildBody(f, ps);
			} else {
				resp.setReplyError(404, "not found");
			}
		} else {
			resp.setReplyError(405, "method not allowed");
		}
	}

	private void buildBody(File f, PrintStream ps) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line = reader.readLine();
		while (line != null) {
			ps.println(line);
			line = reader.readLine();
		}
		reader.close();
	}

}
