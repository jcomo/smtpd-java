package me.jcomo.smtpd.command;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuitCommandTest {

    @Test
    public void testCommandIsAlwaysSuccessful() throws Exception {
        Command command = new QuitCommand();

        boolean succeeded = command.execute();

        assertThat(succeeded).isTrue();
    }
}