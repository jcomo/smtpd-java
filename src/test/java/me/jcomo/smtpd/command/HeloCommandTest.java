package me.jcomo.smtpd.command;

import me.jcomo.smtpd.server.Reply;
import me.jcomo.smtpd.server.ReplyCode;
import me.jcomo.smtpd.server.Session;
import org.junit.After;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HeloCommandTest {
    private final Session session = mock(Session.class);

    @After
    public void tearDown() throws Exception {
        reset(session);
    }

    @Test
    public void testRepliesWithErrorWithoutDomain() throws Exception {
        Command command = new HeloCommand(session, "HELO");

        boolean succeeded = command.execute();

        assertThat(succeeded).isFalse();
        verify(session, only()).sendReply(new Reply(ReplyCode.SYNTAX_ERROR, "Syntax: HELO <domain>"));
    }

    @Test
    public void testSetsSessionDomainOnSuccess() throws Exception {
        Command command = new HeloCommand(session, "HELO domain");

        boolean succeeded = command.execute();

        assertThat(succeeded).isTrue();
        verify(session).setDomain("domain");
        verify(session).sendReply(new Reply(ReplyCode.OK, "HELO localhost"));
    }
}