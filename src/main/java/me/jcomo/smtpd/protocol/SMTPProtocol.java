package me.jcomo.smtpd.protocol;

import me.jcomo.smtpd.command.Command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SMTPProtocol implements StateMachine<Command> {
    private static Map<State, Map<String, State>> transitions = new HashMap<>();
    private static Set<String> unsequencedCommands = new HashSet<>();

    static {
        Map<String, State> initTransitions = new HashMap<>();
        initTransitions.put("HELO", State.HELO);
        initTransitions.put("QUIT", State.QUIT);

        Map<String, State> heloTransitions = new HashMap<>();
        heloTransitions.put("MAIL", State.MAIL);
        heloTransitions.put("QUIT", State.QUIT);

        Map<String, State> mailTransitions = new HashMap<>();
        mailTransitions.put("RCPT", State.RCPT);
        mailTransitions.put("RSET", State.HELO);
        mailTransitions.put("QUIT", State.QUIT);

        Map<String, State> rcptTransitions = new HashMap<>();
        rcptTransitions.put("RCPT", State.RCPT);
        rcptTransitions.put("DATA", State.HELO);
        rcptTransitions.put("RSET", State.HELO);
        rcptTransitions.put("QUIT", State.QUIT);

        transitions.put(State.INIT, initTransitions);
        transitions.put(State.HELO, heloTransitions);
        transitions.put(State.MAIL, mailTransitions);
        transitions.put(State.RCPT, rcptTransitions);
        transitions.put(State.QUIT, new HashMap<>());

        unsequencedCommands.add("HELP");
        unsequencedCommands.add("NOOP");
    }

    State state = State.INIT;

    public void transition(Command command) {
        if (commandIsStateless(command)) {
            command.execute();
        } else {
            runSequencedCommand(command);
        }
    }

    private boolean commandIsStateless(Command command) {
        return unsequencedCommands.contains(command.getName());
    }

    private void runSequencedCommand(Command command) {
        State nextState = nextStateForCommand(command);
        if (nextState != null) {
            runCommandAndTransitionOnSuccess(command, nextState);
        } else {
            throw new IllegalStateException();
        }
    }

    private State nextStateForCommand(Command command) {
        return transitions.get(state).get(command.getName());
    }

    private void runCommandAndTransitionOnSuccess(Command command, State nextState) {
        boolean succeeded = command.execute();
        if (succeeded) {
            state = nextState;
        }
    }

    public boolean isTerminated() {
        return state == State.QUIT;
    }
}
