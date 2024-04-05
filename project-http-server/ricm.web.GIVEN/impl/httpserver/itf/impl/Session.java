package httpserver.itf.impl;

import java.time.Clock;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import httpserver.itf.HttpSession;

public class Session implements HttpSession {
	private static final long MAX_INACTIVITY_TIME = 20000; // 20sec inactivity max
	
	private String id;
	private HashMap<String, Object> values;
	private Timer inactivityTimer;
	
	Session (String id) {
		this.id = id;
		this.values = new HashMap<>();
		this.inactivityTimer = new Timer();
		inactivityTimer.schedule(new InactivityCleaner(), MAX_INACTIVITY_TIME);
	}
	
	private void resetInactivityTimer() {
        inactivityTimer.cancel();
        inactivityTimer = new Timer();
        inactivityTimer.schedule(new InactivityCleaner(), MAX_INACTIVITY_TIME);
    }

	@Override
	public String getId() {
		this.resetInactivityTimer();
		return this.id;
	}

	@Override
	public Object getValue(String key) {
		this.resetInactivityTimer();
		return this.values.get(key);
	}

	@Override
	public void setValue(String key, Object value) {
		this.resetInactivityTimer();
		if (this.values.containsKey(key)) {
			this.values.replace(key, value);
		} else {
			this.values.put(key, value);
		}
	}
	
	private class InactivityCleaner extends TimerTask {
        @Override
        public void run() {
            HttpServer.clients.remove(Session.this);
        }
    }
}
