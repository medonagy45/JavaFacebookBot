package com.crunchify.tutorials;

import org.json.JSONObject;

public class Constants {
	public static final String RECONNECT=JSONObject.quote("reconnect");
	public static final String DISCONNECT_ME=JSONObject.quote("disconnect");
	public static final String INSTRUCTIONS=JSONObject.quote("Type "+DISCONNECT_ME+" to end chatting, Type "+RECONNECT+" to reconnect with a new user");
	public static final String ONCE_FIND_SOMEONE=JSONObject.quote("once we will find you a match we will notify you");
	public static final String YOU_ARE_DISCONNECTED=JSONObject.quote("you are disconnected,");
	public static final String SAY_HI_TO_RECONNECT=JSONObject.quote("say hi to reconnect");
	
	public static final String YOUR_PARTNER_LEFT=JSONObject.quote("your partner left,");
	public static final String YOU_ARE_CONNECTED="you are now connected to a ";
	
	
}
