package org.zeroturnaround.skypebot;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

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

public class SkypeAdapter implements MessageListener, SkypeListener,
    ConversationListener, ErrorListener, ConnectionListener {

  @Override
  public void sidOnDisconnected(String cause) {
  }

  @Override
  public void sidOnConnected() {
  }

  @Override
  public void sidOnConnecting() {
  }

  @Override
  public void error(TransformerException arg0) throws TransformerException {
  }

  @Override
  public void fatalError(TransformerException arg0) throws TransformerException {
  }

  @Override
  public void warning(TransformerException arg0) throws TransformerException {
  }

  @Override
  public void onPropertyChange(Conversation object, Property p, int value, String svalue) {
  }

  @Override
  public void onParticipantListChange(Conversation object) {
  }

  @Override
  public void onMessage(Conversation object, Message message) {
  }

  @Override
  public void onSpawnConference(Conversation object, Conversation spawned) {
  }

  @Override
  public void onNewCustomContactGroup(Skype object, ContactGroup group) {
  }

  @Override
  public void onContactOnlineAppearance(Skype object, Contact contact) {
  }

  @Override
  public void onContactGoneOffline(Skype object, Contact contact) {
  }

  @Override
  public void onConversationListChange(Skype object, Conversation conversation, ListType type, boolean added) {
  }

  @Override
  public void onMessage(Skype object, Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage, Conversation conversation) {
  }

  @Override
  public void onAvailableVideoDeviceListChange(Skype object) {
  }

  @Override
  public void onH264Activated(Skype object) {
  }

  @Override
  public void onAvailableDeviceListChange(Skype object) {
  }

  @Override
  public void onNrgLevelsChange(Skype object) {
  }

  @Override
  public void onProxyAuthFailure(Skype object, ProxyType type) {
  }

  @Override
  public void onApp2AppDatagram(Skype object, String appname, String stream, byte[] data) {
  }

  @Override
  public void onApp2AppStreamListChange(Skype object, String appname, App2AppStreams listType, String[] streams, int[] receivedSizes) {
  }

  @Override
  public void onAuthTokenResult(Skype object, boolean success, int requestId, String token) {
  }

  @Override
  public void onAuthTokenRequest(Skype object, long partnerId, String[] scopes) {
  }

  @Override
  public void onAccountPartnerLinkResult(Skype object, AuthResult code, String body) {
  }

  @Override
  public void onQualityTestResult(Skype arg0, QualityTestType arg1, QualityTestResult arg2, String arg3, String arg4, String arg5) {
  }

  @Override
  public void onPropertyChange(Message object, com.skype.api.Message.Property p, int value, String svalue) {
  }

}
