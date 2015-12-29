package me.jcomo.smtpd.command;

import me.jcomo.smtpd.server.Reply;
import me.jcomo.smtpd.server.ReplyCode;
import me.jcomo.smtpd.server.Session;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NoopCommandTest {
    private final Session session = mock(Session.class);

    @Test
    public void testNoopRepliesWithConfirmation() throws Exception {
        Command command = new NoopCommand(session);

        boolean succeeded = command.execute();

        assertThat(succeeded).isTrue();
        verify(session).sendReply(new Reply(ReplyCode.OK, "noop"));
    }
}