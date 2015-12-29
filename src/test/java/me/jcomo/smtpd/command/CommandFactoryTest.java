package me.jcomo.smtpd.command;

import me.jcomo.smtpd.server.Session;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CommandFactoryTest {
    private final Session session = mock(Session.class);
    private final CommandFactory factory = new CommandFactory(session);

    public void assertCreatesCommandType(String line, Class<? extends Command> klass) {
        Command command = factory.getCommand(line);
        assertThat(command).isInstanceOf(klass);
    }

    @Test
    public void testCreatesHeloCommand() throws Exception {
        assertCreatesCommandType("HELO world", HeloCommand.class);
    }

    @Test
    public void testCreatesMailCommand() throws Exception {
        assertCreatesCommandType("MAIL FROM: <test@example.com>", MailCommand.class);
    }

    @Test
    public void testCreatesRcptCommand() throws Exception {
        assertCreatesCommandType("RCPT TO: <person@example.com>", RcptCommand.class);
    }

    @Test
    public void testCreatesDataCommand() throws Exception {
        assertCreatesCommandType("DATA", DataCommand.class);
    }

    @Test
    public void testCreatesNoopCommand() throws Exception {
        assertCreatesCommandType("NOOP", NoopCommand.class);
    }

    @Test
    public void testCreatesRsetCommand() throws Exception {
        assertCreatesCommandType("RSET", RsetCommand.class);
    }

    @Test
    public void testCreatesQuitCommand() throws Exception {
        assertCreatesCommandType("QUIT", QuitCommand.class);
    }

    @Test
    public void testCreationIsCaseInsensitive() throws Exception {
        assertCreatesCommandType("helo", HeloCommand.class);
        assertCreatesCommandType("HeLo", HeloCommand.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsForUnimplementedCommand() throws Exception {
        factory.getCommand("EHLO");
    }

    @Test(expected = NoSuchElementException.class)
    public void testThrowsForUnrecognizedCommand() throws Exception {
        factory.getCommand("BLAH");
    }

    @Test(expected = NoSuchElementException.class)
    public void testThrowsForBlankCommandString() throws Exception {
        factory.getCommand("");
    }
}