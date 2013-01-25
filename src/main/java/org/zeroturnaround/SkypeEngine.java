package org.zeroturnaround;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skype.api.Account;
import com.skype.api.Conversation;
import com.skype.api.Skype;
import com.skype.ipc.ClientConfiguration;
import com.skype.ipc.ConnectionListener;

public class SkypeEngine extends Thread {
	private static final Logger log = LoggerFactory
			.getLogger(SkypeEngine.class);
	private static Skype skype;
	private boolean isConnected = false;

	public SkypeEngine() {
		skype = new Skype();
	}

	public void run() {
		ClientConfiguration conf = configureSkype();
		ConnectionListener listeners = new SkypeGlobalListener();

		skype.init(conf, listeners);
		log.debug("Starting Skype");
		skype.start();

		String version = skype.getVersionString();
		log.debug("Skype version " + version);

		log.debug("Logging in with user " + Configuration.skypeUsername);
		Account account = skype
				.getAccount(Configuration.skypeUsername);
		account.loginWithPassword(Configuration.skypePassword, false, true);
	}

	private boolean isConnected() {
		return isConnected;
	}

	private void disConnect() {
		this.isConnected = false;
	}

	private void tearDownSkype(SkypeGlobalListener listener) throws IOException {
		log.debug("Connection lost. Cleaning up.");

		unRegisterAllListeners(skype, listener);
	}

	private ClientConfiguration configureSkype() {
		log.debug("skypeConnect()");
		ClientConfiguration conf = new ClientConfiguration();

		conf.setTcpTransport("127.0.0.1", 8963);
		conf.setCertificate(Configuration.pemFile);

		return conf;
	}

	private static void registerAllListeners(Skype skype,
			SkypeGlobalListener listener) {
		skype.registerMessageListener(listener);
		skype.registerConversationListener(listener);
	}

	private static void unRegisterAllListeners(Skype skype,
			SkypeGlobalListener listener) {
		skype.unRegisterMessageListener(listener);
		skype.unRegisterConversationListener(listener);
	}

	public static boolean post(String group, String message) {
		Conversation[] convos = skype
				.getConversationList(Conversation.ListType.ALL_CONVERSATIONS);
		for (int i = 0; i < convos.length; i++) {
			if (convos[i].getDisplayName().equals(group)) {
				convos[i].postText(message, false);
				return true;
			}
		}
		return false;
	}
}
