package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
			if (f.exists() && f.isFile()) { // if ressource exists and type file
				resp.setReplyOk();
				resp.setContentType(getContentType(m_ressname));
				resp.setContentLength((int) f.length());
				PrintStream ps = resp.beginBody();
				buildBody(f, ps);
			} else if (f.exists() && f.isDirectory()) { // if ressource exists but directory
				File indexFile = new File(f.getPath()+"/"+DEFAULT_FILE); // we are seeking to return index.html file if exists
				if (indexFile.exists()) {
					resp.setReplyOk();
					resp.setContentType(getContentType(DEFAULT_FILE));
					resp.setContentLength((int) indexFile.length());
					PrintStream ps = resp.beginBody();
					buildBody(indexFile, ps);
				} else {
					resp.setReplyError(404, "index.html not found");
				}
			} else { 
				resp.setReplyError(404, "ressource not found");
			}
			
		} else {
			resp.setReplyError(405, "method not allowed");
		}
	}

	/**
	 * Write entire file into PrintStream
	 * @param f
	 * @param ps
	 * @throws IOException
	 */
	private void buildBody(File f, PrintStream ps) throws IOException {
		byte[] bytes = new byte[(int) f.length()];
		FileInputStream fis = new FileInputStream(f);
		fis.read(bytes);
		ps.writeBytes(bytes);
	}

}
