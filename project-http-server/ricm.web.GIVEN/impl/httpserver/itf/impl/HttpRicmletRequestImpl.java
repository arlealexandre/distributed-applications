package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.IOException;
import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmlet;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpRicmletResponse;
import httpserver.itf.HttpSession;

public class HttpRicmletRequestImpl extends HttpRicmletRequest {
	
	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname, BufferedReader br) throws IOException {
		super(hs, method, ressname, br);
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArg(String name) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process(HttpResponse resp) throws Exception {
		if (m_method.equals("GET")) {
			try {
				String[] splitedRessname = m_ressname.split("/");
				String classPath = splitedRessname[splitedRessname.length-2] + '.' + splitedRessname[splitedRessname.length-1];
	            HttpRicmlet ricmlet = m_hs.getInstance(classPath);
	            ricmlet.doGet(this, (HttpRicmletResponse) resp);
	        } catch (Exception e) {
	            resp.setReplyError(404, "Ricmlet not found");
	        }
			
		} else {
			resp.setReplyError(405, "method not allowed");
		}
	}

}
