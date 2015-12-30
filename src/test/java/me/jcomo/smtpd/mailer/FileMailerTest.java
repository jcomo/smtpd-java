package me.jcomo.smtpd.mailer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileMailerTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testWritesMailToFile() throws Exception {
        File file = tempFolder.newFile("mail.dat");
        FileFactory factory = mock(FileFactory.class);
        when(factory.createFile()).thenReturn(file);
        FileMailer mailer = new FileMailer(factory);

        mailer.send("no-reply@example.com", "person@example.com", "data\r\nhere");

        String expected = "From: no-reply@example.com\n"
                + "To: person@example.com\n"
                + "\n"
                + "data\n"
                + "here";

        assertThat(file).hasContent(expected);
    }

    @Test(expected = RuntimeException.class)
    public void testThrowsWhenFileIsInvalid() throws Exception {
        File file = mock(File.class);
        when(file.getPath()).thenThrow(new FileNotFoundException());

        FileFactory factory = mock(FileFactory.class);
        when(factory.createFile()).thenReturn(file);

        FileMailer mailer = new FileMailer(factory);

        mailer.send("no-reply@example.com", "person@example.com", "data");
    }
}