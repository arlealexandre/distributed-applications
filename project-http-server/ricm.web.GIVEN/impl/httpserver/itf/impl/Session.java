package httpserver.itf.impl;

import java.time.Clock;
import java.util.HashMap;

import httpserver.itf.HttpSession;

public class Session implements HttpSession {
	String id;
	HashMap<String, Object> values;
	long lastAction;
	
	Session (String id) {
		this.id = id;
		this.values = new HashMap<>();
		this.lastAction = Clock.systemDefaultZone().millis() / 1000;
	}

	@Override
	public String getId() throws Exception {
		long currentAction = Clock.systemDefaultZone().millis() / 1000;
		if (currentAction - this.lastAction < 20) {
			this.lastAction = currentAction;
			return this.id;
		}
		throw new Exception ("Session expired");
	}

	@Override
	public Object getValue(String key) throws Exception {
		long currentAction = Clock.systemDefaultZone().millis() / 1000;
		if (currentAction - this.lastAction < 20) {
			this.lastAction = currentAction;
			return this.values.get(key);
		}
		throw new Exception ("Session expired");
	}

	@Override
	public void setValue(String key, Object value) throws Exception {
		long currentAction = Clock.systemDefaultZone().millis() / 1000;
		if (currentAction - this.lastAction < 20) {
			this.lastAction = currentAction;
			if (this.values.containsKey(key)) {
				this.values.replace(key, value);
			} else {
				this.values.put(key, value);
			}
		} else {
			throw new Exception ("Session expired");
		}
	}
}
