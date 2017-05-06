package com.crunchify.tutorials;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHelper {

	private static int messageid = 0;
	private static String pageToken = "EAAGG9TjBpZBYBANajcXNZByydAPiNZApfO9PFudZCaZBmco1ZAghfdKz7CRyQIwKpHCahboBWmk2GU2cbZAV5ZCK9bjcmYkc9bLIbNm7YoGAQcP6LxwxoPpBrhO88q5IYlHbX6DavFxECemUw20bYJAsqF5E3ZARrlKxvYhddbxjAugZDZD";

	public static void sendInstructions(RequestParser requestHelper,
			String senderId) throws Exception {
		if (!DataHandler.isJoinedBefore(senderId)) {
			DataHandler.addNewJoinedBefore(senderId);
			sendPost(requestHelper.generateResponseMessage(senderId,
					Constants.INSTRUCTIONS));
		}
	}

	public static boolean checkAndDisconnect(RequestParser requestHelper,
			String senderId, String condition) throws Exception {
		if (requestHelper.getMessageOnly().toLowerCase().equals(condition)) {
			disconnectMeAction(requestHelper, senderId);
			if(condition.equals(Constants.DISCONNECT_ME))
				sendPost(requestHelper.generateResponseMessage(senderId,
						Constants.SAY_HI_TO_RECONNECT));
			return true;
		}
		return false;
	}

	public static boolean isChatting(String senderId) {
		return DataHandler.isInChattingBool(senderId);
	}

	private static void disconnectMeAction(RequestParser requestHelper,
			String senderId) throws Exception {
		sendPost(requestHelper.generateResponseMessage(senderId,
				Constants.YOU_ARE_DISCONNECTED));
		if (DataHandler.isInChattingBool(senderId)) {
			String recipientId = DataHandler.getRecipientId(senderId);
			DataHandler.removeFromChattingBool(senderId);
			DataHandler.removeFromChattingBool(recipientId);
			sendPost(requestHelper.generateResponseMessage(recipientId,
					Constants.YOUR_PARTNER_LEFT));
			sendPost(requestHelper.generateResponseMessage(recipientId,
					Constants.SAY_HI_TO_RECONNECT));
		}
	}

	public static void handleWaitingAndMatchedCases(
			RequestParser requestHelper, String senderId) throws Exception {
		if (DataHandler.isWaitingListEmpty()) {
			DataHandler.addToWaitingList(senderId);
			sendPost(requestHelper.generateResponseMessage(senderId,
					Constants.ONCE_FIND_SOMEONE));

		} else if (!DataHandler.isInWaitingList(senderId)) {
			String recipientId = DataHandler.removeFromWaitingList();
			DataHandler.addInChattingBool(senderId, recipientId);
			DataHandler.addInChattingBool(recipientId, senderId);
			sendPost(requestHelper.generateResponseMessage(recipientId,
					Constants.YOU_ARE_CONNECTED));
			sendPost(requestHelper.generateResponseMessage(senderId,
					Constants.YOU_ARE_CONNECTED));
			sendPost(requestHelper.generateResponseMessage(recipientId,
					requestHelper.getMessageOnly()));
		}
	}

	public static void sendChatMessage(RequestParser requestHelper,
			String senderId) throws Exception {
		String recipientId = DataHandler.getRecipientId(senderId);
		sendPost(requestHelper.generateResponse(recipientId));

	}

	private static void sendPost(String body) throws Exception {
		System.out.println("the response id " + messageid + " value : " + body);
		String url = "https://graph.facebook.com/v2.6/me/messages?access_token="
				+ pageToken;

		URL object = new URL(url);

		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");

		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(body);
		wr.flush();
		wr.close();
		// display what returns the POST request

		int HttpResult = con.getResponseCode();
		if (HttpResult == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println("the response of my send " + messageid
						+ "value :" + line + "\n");
			}
			br.close();
		} else {
			System.out.println("the error message " + messageid + " "
					+ con.getResponseCode());
		}

	}
}
