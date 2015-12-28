package me.jcomo.smtpd.message;

public interface MessageBuffer {
    void setSender(String sender);
    void addRecipient(String recipient);
    void receiveData();
    void done();
}
