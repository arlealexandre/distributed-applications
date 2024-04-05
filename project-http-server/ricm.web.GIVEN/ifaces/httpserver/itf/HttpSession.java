package httpserver.itf;


/*
 * Interface provided by an object representing an HTTP session
 * An HTTP session provides a way to identify a client across consecutive requests and 
 * to store information about that user. 
 */
public interface HttpSession {
	
	/*
	 * Returns a string containing the unique identifier assigned to this session.
	 */
	public String getId() throws Exception;
	
	/*
	 * Returns the object bound with the specified name in this session, or null if no object is bound under the name.
	 */
	public Object getValue(String key) throws Exception;
	
	/*
	 * Binds an object to this session, using the name specified.
	 */
	public void setValue(String key, Object value) throws Exception ;
}
