package org.zeroturnaround;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skype.api.Account;
import com.skype.api.Conversation;
import com.skype.api.Skype;
import com.skype.ipc.ClientConfiguration;
import com.skype.ipc.ConnectionListener;
import com.skype.util.PemReader;

public class SkypeEngine extends Thread {
	private static final Logger log = LoggerFactory
			.getLogger(SkypeEngine.class);
	private static Skype skype_instance;
	private boolean isConnected = false;

	public SkypeEngine() {
		skype_instance = new Skype();
	}

	public void run() {

		ClientConfiguration conf = configureSkype();
		ConnectionListener listeners = new SkypeGlobalListener();

		skype_instance.init(conf, listeners);
		skype_instance.start();
	}

	private boolean isConnected() {
		return isConnected;
	}

	private void disConnect() {
		this.isConnected = false;
	}

	private void tearDownSkype(SkypeGlobalListener listener) throws IOException {
		log.debug("Connection lost. Cleaning up.");

		unRegisterAllListeners(skype_instance, listener);
	}

	private ClientConfiguration configureSkype() {
		log.debug("skypeConnect()");
		ClientConfiguration conf = new ClientConfiguration();

		PemReader certAsPem;
		X509Certificate cert;
		PrivateKey privateKey;
		try {
			log.debug("Using pem file " + Configuration.pemFile);
			certAsPem = new PemReader(Configuration.pemFile);
			cert = certAsPem.getCertificate();
			privateKey = certAsPem.getKey();
		} catch (FileNotFoundException e1) {
			throw new RuntimeException(e1);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		conf.setTcpTransport("127.0.0.1", 8963);
		conf.setCertificate(Configuration.pemFile);

		log.debug("new TLSServerTransport created, calling skype.Init()...");

		skype_instance.start();

		log.debug("calling GetVersionString");
		String version = skype_instance.getVersionString();
		log.debug("Skype version " + version);

		Account account = skype_instance
				.getAccount(Configuration.skypeUsername);
		account.loginWithPassword(Configuration.skypePassword, false, true);

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
		Conversation[] convos = skype_instance
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
