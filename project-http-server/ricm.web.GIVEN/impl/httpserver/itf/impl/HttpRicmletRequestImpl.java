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
		if (this.getRessname().contains("?")) {
			String[] parameters = this.getRessname().split("\\?", 2)[1].split("&");
			for (String s : parameters) {
				String[] temp = s.split("=");
				if (temp[0].equals(name)) {
					return temp[1];
				}
			}
		} else {
			return "";
		}
		return "";
	}

	@Override
	public String getCookie(String name) {
		return m_hs.getCookies().get(name);
	}

	@Override
	public void process(HttpResponse resp) throws Exception {
		if (m_method.equals("GET")) {
			try {
				String[] splitedRessname = m_ressname.split("/");
				boolean isClassPath = false;
				boolean first = true;
				String classPath = "";
				
				for (int i = 0; i < splitedRessname.length; i++) {
					if (!isClassPath) {
						if (splitedRessname[i].equals("ricmlets")) {
							isClassPath = true;
						}
					} else {
						if (first) {
							first = !first;
						} else {
							classPath += ".";
						}
						if (i == splitedRessname.length - 1) {
							if (splitedRessname[i].contains("?")) {
								classPath += splitedRessname[i].split("\\?", 2)[0];
							} else {
								classPath += splitedRessname[i];
							}
						} else {
							
							classPath += splitedRessname[i];
						}
					}
				}
				
	            HttpRicmlet ricmlet = m_hs.getInstance(classPath);
	            ricmlet.doGet(this, (HttpRicmletResponse) resp);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            resp.setReplyError(404, "Ricmlet not found");
	        }
			
		} else {
			resp.setReplyError(405, "method not allowed");
		}
	}

}
