package me.jcomo.smtpd.protocol;

import me.jcomo.smtpd.command.Command;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class SMTPProtocolTest {
    private final SMTPProtocol protocol = new SMTPProtocol();

    public void assertStateTransitionsWithCommand(State from, State to, Command command) {
        protocol.state = from;
        protocol.transition(command);
        assertThat(protocol.state).isEqualTo(to);
    }

    public void assertStateDoesNotTransition(State state, Command command) {
        protocol.state = state;
        protocol.transition(command);
        assertThat(protocol.state).isEqualTo(state);
    }

    public void assertIllegalSequence(State state, Command command) {
        protocol.state = state;
        try {
            protocol.transition(command);
            fail("Expected illegal sequence from state "
                    + state + " with command "
                    + command.getName() + " but was valid");
        } catch (IllegalStateException e) {
            // Pass
        }
    }

    public Command successfulCommand(String commandName) {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn(commandName);
        when(command.execute()).thenReturn(true);
        return command;
    }

    public Command failedCommand(String commandName) {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn(commandName);
        when(command.execute()).thenReturn(false);
        return command;
    }

    @Test
    public void testInitStateTransitions() throws Exception {
        assertStateTransitionsWithCommand(State.INIT, State.HELO, successfulCommand("HELO"));
        assertStateTransitionsWithCommand(State.INIT, State.QUIT, successfulCommand("QUIT"));

        assertIllegalSequence(State.INIT, successfulCommand("MAIL"));
        assertIllegalSequence(State.INIT, successfulCommand("RCPT"));
        assertIllegalSequence(State.INIT, successfulCommand("RSET"));
    }

    @Test
    public void testHeloStateTransitions() throws Exception {
        assertStateTransitionsWithCommand(State.HELO, State.MAIL, successfulCommand("MAIL"));
        assertStateTransitionsWithCommand(State.HELO, State.QUIT, successfulCommand("QUIT"));

        assertIllegalSequence(State.HELO, successfulCommand("HELO"));
        assertIllegalSequence(State.HELO, successfulCommand("RCPT"));
        assertIllegalSequence(State.HELO, successfulCommand("DATA"));
        assertIllegalSequence(State.HELO, successfulCommand("RSET"));
    }

    @Test
    public void testMailStateTransitions() throws Exception {
        assertStateTransitionsWithCommand(State.MAIL, State.RCPT, successfulCommand("RCPT"));
        assertStateTransitionsWithCommand(State.MAIL, State.HELO, successfulCommand("RSET"));
        assertStateTransitionsWithCommand(State.MAIL, State.QUIT, successfulCommand("QUIT"));

        assertIllegalSequence(State.MAIL, successfulCommand("HELO"));
        assertIllegalSequence(State.MAIL, successfulCommand("DATA"));
    }

    @Test
    public void testRcptStateTransitions() throws Exception {
        assertStateTransitionsWithCommand(State.RCPT, State.RCPT, successfulCommand("RCPT"));
        assertStateTransitionsWithCommand(State.RCPT, State.HELO, successfulCommand("DATA"));
        assertStateTransitionsWithCommand(State.RCPT, State.HELO, successfulCommand("RSET"));
        assertStateTransitionsWithCommand(State.RCPT, State.QUIT, successfulCommand("QUIT"));

        assertIllegalSequence(State.RCPT, successfulCommand("HELO"));
        assertIllegalSequence(State.RCPT, successfulCommand("MAIL"));
    }

    @Test
    public void testQuitStateTransitions() throws Exception {
        assertIllegalSequence(State.QUIT, successfulCommand("HELO"));
        assertIllegalSequence(State.QUIT, successfulCommand("MAIL"));
        assertIllegalSequence(State.QUIT, successfulCommand("DATA"));
        assertIllegalSequence(State.QUIT, successfulCommand("RSET"));
        assertIllegalSequence(State.QUIT, successfulCommand("QUIT"));
    }

    @Test
    public void testUnsuccessfulCommandsDoNotTransition() throws Exception {
        assertStateDoesNotTransition(State.INIT, failedCommand("HELO"));
        assertStateDoesNotTransition(State.HELO, failedCommand("MAIL"));
        assertStateDoesNotTransition(State.MAIL, failedCommand("RCPT"));
        assertStateDoesNotTransition(State.RCPT, failedCommand("DATA"));
    }

    @Test
    public void testUnsequencedCommandsDoNotTransition() throws Exception {
        assertStateDoesNotTransition(State.INIT, successfulCommand("NOOP"));
        assertStateDoesNotTransition(State.MAIL, successfulCommand("HELP"));
    }

    @Test
    public void testCommandsRunWhenValidSequence() throws Exception {
        Command command = successfulCommand("HELO");

        protocol.transition(command);

        verify(command, times(1)).execute();
    }

    @Test
    public void testCommandsNotRunWhenInvalidSequence() throws Exception {
        Command command = successfulCommand("MAIL");

        try {
            protocol.transition(command);
            fail("Expected bad transition");
        } catch (IllegalStateException e) {
            // Continue
        }

        verify(command, never()).execute();
    }

    @Test
    public void testOnlyTerminatedInQuitState() throws Exception {
        protocol.state = State.INIT;
        assertThat(protocol.isTerminated()).isFalse();

        protocol.state = State.HELO;
        assertThat(protocol.isTerminated()).isFalse();

        protocol.state = State.MAIL;
        assertThat(protocol.isTerminated()).isFalse();

        protocol.state = State.RCPT;
        assertThat(protocol.isTerminated()).isFalse();

        protocol.state = State.QUIT;
        assertThat(protocol.isTerminated()).isTrue();
    }
}