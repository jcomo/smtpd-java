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

public class MailCommandTest {
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

    public void assertInvalidMailSyntax(String commandStr) {
        Command command = new MailCommand(session, commandStr);

        boolean succeeded = command.execute();

        assertThat(succeeded).isFalse();
        verify(session, only()).sendReply(new Reply(ReplyCode.SYNTAX_ERROR,
                "Syntax: MAIL FROM: <address>"));

        reset(session);
    }

    @Test
    public void testInvalidSyntaxRepliesWithError() throws Exception {
        assertInvalidMailSyntax("MAIL");
        assertInvalidMailSyntax("MAIL FROM someone");
        assertInvalidMailSyntax("MAIL TO: someone");
    }

    @Test
    public void testInvalidEmailRepliesWithError() throws Exception {
        Command command = new MailCommand(session, "MAIL FROM: mail.com");

        boolean succeeded = command.execute();

        assertThat(succeeded).isFalse();
        verify(session).sendReply(new Reply(ReplyCode.INVALID_MAILBOX_SYNTAX,
                "invalid email address: mail.com"));
    }

    @Test
    public void testInvalidEmailDoesNotSetSenderForSession() throws Exception {
        Command command = new MailCommand(session, "MAIL FROM: <mail.com>");

        boolean succeeded = command.execute();

        assertThat(succeeded).isFalse();
        verify(buffer, never()).setSender(any());
    }

    @Test
    public void testValidEmailSetsSendersForSession() throws Exception {
        Command command = new MailCommand(session, "MAIL FROM: <mail@example.com>");

        boolean succeeded = command.execute();

        assertThat(succeeded).isTrue();
        verify(buffer).setSender("mail@example.com");
        verify(session).sendReply(new Reply(ReplyCode.OK, "OK"));
    }
}