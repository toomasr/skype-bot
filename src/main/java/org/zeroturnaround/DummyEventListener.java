package org.zeroturnaround;

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

public class DummyEventListener implements MessageListener, SkypeListener,
    ConversationListener, ErrorListener, ConnectionListener {

  @Override
  public void sidOnDisconnected(String cause) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void sidOnConnected() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void sidOnConnecting() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void error(TransformerException arg0) throws TransformerException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void fatalError(TransformerException arg0) throws TransformerException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void warning(TransformerException arg0) throws TransformerException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onPropertyChange(Conversation object, Property p, int value, String svalue) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onParticipantListChange(Conversation object) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onMessage(Conversation object, Message message) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onSpawnConference(Conversation object, Conversation spawned) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onNewCustomContactGroup(Skype object, ContactGroup group) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onContactOnlineAppearance(Skype object, Contact contact) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onContactGoneOffline(Skype object, Contact contact) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onConversationListChange(Skype object, Conversation conversation, ListType type, boolean added) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onMessage(Skype object, Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage, Conversation conversation) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onAvailableVideoDeviceListChange(Skype object) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onH264Activated(Skype object) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onAvailableDeviceListChange(Skype object) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onNrgLevelsChange(Skype object) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onProxyAuthFailure(Skype object, ProxyType type) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onApp2AppDatagram(Skype object, String appname, String stream, byte[] data) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onApp2AppStreamListChange(Skype object, String appname, App2AppStreams listType, String[] streams, int[] receivedSizes) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onAuthTokenResult(Skype object, boolean success, int requestId, String token) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onAuthTokenRequest(Skype object, long partnerId, String[] scopes) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onAccountPartnerLinkResult(Skype object, AuthResult code, String body) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onQualityTestResult(Skype arg0, QualityTestType arg1, QualityTestResult arg2, String arg3, String arg4, String arg5) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onPropertyChange(Message object, com.skype.api.Message.Property p, int value, String svalue) {
    // TODO Auto-generated method stub
    
  }

}
