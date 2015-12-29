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

public class RsetCommandTest {
    private final Session session = mock(Session.class);
    private final MessageBuffer buffer = mock(MessageBuffer.class);

    @Before
    public void setUp() throws Exception {
        when(session.getMessageBuffer()).thenReturn(buffer);
    }

    @After
    public void tearDown() throws Exception {
        reset(session, buffer);
    }

    @Test
    public void testResetsTheSessionMessageBuffer() throws Exception {
        Command command = new RsetCommand(session);

        boolean succeeded = command.execute();

        assertThat(succeeded).isTrue();
        verify(buffer, only()).reset();
    }

    @Test
    public void testRepliesWithResetConfirmation() throws Exception {
        Command command = new RsetCommand(session);

        boolean succeeded = command.execute();

        assertThat(succeeded).isTrue();
        verify(session).sendReply(new Reply(ReplyCode.OK, "reset"));
    }
}