package me.jcomo.smtpd.message;

import me.jcomo.smtpd.mailer.Mailer;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class SimpleMessageBufferTest {
    private final Mailer mailer = mock(Mailer.class);

    @Test
    public void testSendsFromSender() throws Exception {
        SimpleMessageBuffer buffer = new SimpleMessageBuffer(null, mailer);

        buffer.setSender("test@example.com");
        buffer.addRecipient("person@example.com");
        buffer.done();

        verify(mailer, times(1)).send(eq("test@example.com"), anyString(), anyString());
    }

    @Test
    public void testNoRecipientsNeverSends() throws Exception {
        SimpleMessageBuffer buffer = new SimpleMessageBuffer(null, mailer);

        buffer.done();

        verify(mailer, never()).send(any(), any(), any());
    }

    @Test
    public void testSendsToAllRecipients() throws Exception {
        SimpleMessageBuffer buffer = new SimpleMessageBuffer(null, mailer);

        buffer.addRecipient("person1@example.com");
        buffer.addRecipient("person2@example.com");
        buffer.done();

        verify(mailer, times(1)).send(anyString(), eq("person1@example.com"), anyString());
        verify(mailer, times(1)).send(anyString(), eq("person2@example.com"), anyString());
    }

    @Test
    public void testSendsWithDataFromBuffer() throws Exception {
        String data = "Some text\r\nFor the message\r\n.\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(data));
        SimpleMessageBuffer buffer = new SimpleMessageBuffer(reader, mailer);

        buffer.addRecipient("person@example.com");
        buffer.receiveData();
        buffer.done();

        String expected = "Some text\nFor the message\n";
        verify(mailer).send(anyString(), anyString(), eq(expected));
    }

    @Test
    public void testReset() throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader("text"));
        SimpleMessageBuffer buffer = new SimpleMessageBuffer(reader, mailer);

        buffer.addRecipient("person@example.com");
        buffer.receiveData();

        buffer.reset();
        buffer.done();

        verify(mailer, never()).send(any(), any(), any());
    }
}