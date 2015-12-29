package me.jcomo.smtpd.command;

import me.jcomo.smtpd.message.MessageBuffer;
import me.jcomo.smtpd.server.Reply;
import me.jcomo.smtpd.server.ReplyCode;
import me.jcomo.smtpd.server.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RcptCommandTest {
    private final MessageBuffer buffer = mock(MessageBuffer.class);
    private final Session session = mock(Session.class);

    @Before
    public void setUp() throws Exception {
        when(session.getMessageBuffer()).thenReturn(buffer);
    }

    @After
    public void tearDown() throws Exception {
        reset(session);
    }

    public void assertInvalidRcptSyntax(String commandStr) {
        Command command = new RcptCommand(session, commandStr);

        boolean succeeded = command.execute();

        assertThat(succeeded).isFalse();
        verify(session, only()).sendReply(new Reply(ReplyCode.SYNTAX_ERROR,
                "Syntax: RCPT TO: <address>"));

        reset(session);
    }

    @Test
    public void testInvalidSyntaxRepliesWithError() throws Exception {
        assertInvalidRcptSyntax("RCPT");
        assertInvalidRcptSyntax("RCPT TO someone");
        assertInvalidRcptSyntax("RCPT FROM: someone");
    }

    @Test
    public void testInvalidEmailRepliesWithError() throws Exception {
        Command command = new RcptCommand(session, "RCPT TO: mail.com");

        boolean succeeded = command.execute();

        assertThat(succeeded).isFalse();
        verify(session, only()).sendReply(new Reply(ReplyCode.INVALID_MAILBOX_SYNTAX, "invalid email address: mail.com"));
    }

    @Test
    public void testInvalidEmailDoesNotAddRecipientForSession() throws Exception {
        Command command = new RcptCommand(session, "RCPT TO: <mail.com>");

        boolean succeeded = command.execute();

        assertThat(succeeded).isFalse();
        verify(buffer, never()).addRecipient(any());
    }

    @Test
    public void testValidEmailAddsRecipientForSession() throws Exception {
        Command command = new RcptCommand(session, "RCPT TO: <mail@example.com>");

        boolean succeeded = command.execute();

        assertThat(succeeded).isTrue();
        verify(buffer).addRecipient("mail@example.com");
        verify(session).sendReply(new Reply(ReplyCode.OK, "OK"));
    }
}