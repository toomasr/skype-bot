package org.zeroturnaround;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.commands.Command;
import org.zeroturnaround.commands.CommandFactory;

import com.skype.api.Contact;
import com.skype.api.ContactGroup;
import com.skype.api.Conversation;
import com.skype.api.Conversation.ListType;
import com.skype.api.Conversation.Property;
import com.skype.api.ConversationListener;
import com.skype.api.Message;
import com.skype.api.MessageListener;
import com.skype.api.Skype;
import com.skype.api.Skype.App2AppStreams;
import com.skype.api.Skype.AuthResult;
import com.skype.api.Skype.ProxyType;
import com.skype.api.Skype.QualityTestResult;
import com.skype.api.Skype.QualityTestType;
import com.skype.api.SkypeListener;
import com.skype.ipc.ConnectionListener;

public class SkypeGlobalListener implements MessageListener, SkypeListener,
		ConversationListener, ErrorListener, ConnectionListener {
	private static final String BOT_MSG_PREFIX = "bot:";
	private static final Logger log = LoggerFactory
			.getLogger(SkypeGlobalListener.class);
	private static Map<String, Conversation> conversations = new HashMap<String, Conversation>();

	public void onMessage(Skype skype, Message message,
			boolean changesInboxTimestamp, Message supersedesHistoryMessage,
			Conversation conversation) {

		log.debug("Incoming message from " + message.getAuthor());

		log.debug("SKYPE.OnMessage." + message.getAuthor() + " Message::TYPE = "
				+ message.mType);
		log.debug("CHAT." + message.getAuthor()+ ";oid=" + conversation.getOid() + ":"
				+ message.mBodyXml);

		conversations.put(conversation.mDisplayName, conversation);
		log.info("convo display name = " + conversation.mDisplayName);
		log.info("convo identity = " + conversation.mIdentity);

		String msgStr = message.getBodyXml();

		// ignore the processing of our own messages to avoid
		// indefinite loop
		if (Configuration.skypeUsername.equals(message.getAuthor())) {
			log.debug("Ignoring command because it is from "
					+ Configuration.skypeUsername);
			return;
		}

		if (msgStr.startsWith(BOT_MSG_PREFIX)) {
			String result = processCommand(msgStr);
			if (result != null) {
				conversation.postText(result, false);
			}
		} else {
			log.debug("Not an actual command, the prefix " + BOT_MSG_PREFIX
					+ " is not present");
		}
	}

	private String processCommand(String msgStr) {
		String cmdStr = msgStr.replaceFirst(BOT_MSG_PREFIX, "").trim();
		log.info("Got command " + cmdStr);

		Command cmd = CommandFactory.getCommand(cmdStr);
		if (cmd != null) {
			return cmd.execute(cmdStr);
		} else {
			log.error("Unable to get an actual command for the command string "
					+ cmdStr);
		}
		return null;
	}

	public static void postToChat(String chatName, String message) {
		log.debug("PostToChat=" + chatName);
		Conversation convo = conversations.get(chatName);
		if (convo != null) {
			log.debug("Found the conversation, posting");
			convo.postText(message, false);
		}
	}

	public void error(TransformerException arg0) throws TransformerException {
	}

	public void fatalError(TransformerException arg0)
			throws TransformerException {
	}

	public void warning(TransformerException arg0) throws TransformerException {
	}

	public void onPropertyChange(Conversation object, Property p, int value,
			String svalue) {
	}

	public void onParticipantListChange(Conversation object) {

	}

	public void onMessage(Conversation object, Message message) {

	}

	public void onSpawnConference(Conversation object, Conversation spawned) {

	}

	public void onNewCustomContactGroup(Skype object, ContactGroup group) {

	}

	public void onContactOnlineAppearance(Skype object, Contact contact) {

	}

	public void onContactGoneOffline(Skype object, Contact contact) {

	}

	public void onConversationListChange(Skype object,
			Conversation conversation, ListType type, boolean added) {

	}

	public void onAvailableVideoDeviceListChange(Skype object) {

	}

	public void onH264Activated(Skype object) {

	}

	public void onAvailableDeviceListChange(Skype object) {

	}

	public void onNrgLevelsChange(Skype object) {

	}

	public void onProxyAuthFailure(Skype object, ProxyType type) {

	}

	public void onApp2AppDatagram(Skype object, String appname, String stream,
			byte[] data) {
	}

	public void onApp2AppStreamListChange(Skype object, String appname,
			App2AppStreams listType, String[] streams, int[] receivedSizes) {

	}

	public void onAuthTokenResult(Skype object, boolean success, int requestId,
			String token) {

	}

	public void onAuthTokenRequest(Skype object, long partnerId, String[] scopes) {

	}

	public void onAccountPartnerLinkResult(Skype object, AuthResult code,
			String body) {
	}

	public void onQualityTestResult(Skype arg0, QualityTestType arg1,
			QualityTestResult arg2, String arg3, String arg4, String arg5) {
	}

	public void onPropertyChange(Message object,
			com.skype.api.Message.Property p, int value, String svalue) {
	}

	public void sidOnDisconnected(String cause) {
	}

	public void sidOnConnected() {
	}

	public void sidOnConnecting() {
	}
}
