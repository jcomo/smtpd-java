# smtpd ![](https://travis-ci.org/jcomo/smtpd.svg?branch=master)

A server implementing the SMTP protocol.

The server is useful for debugging at the moment.
In its current state, the server is only useful for development and debugging.
There is support for sending messages to stdout and to individual files on disk.
Auth and TLS support have not been implemented yet.

### Usage

There is currently no jar that has been released but it has very few dependencies and can be built from source using maven.

```
git clone https://github.com/jcomo/smtpd
cd smtpd
mvn package
java -jar target/smtpd-1.0-SNAPSHOT.jar
```

To test it, you can point your mail client (such as Apple mail) to the address it is running on, or you can interact with it via telnet.
A example SMTP interaction [from RFC 821](http://www.freesoft.org/CIE/RFC/821/31.htm) can be used.

### Configuration

A properties file can be specified as the first positional argument to the program. Available configuration values are

| Key                       | Default                     | Description                                                                   |
| ------------------------- | --------------------------- | ----------------------------------------------------------------------------- |
| `server.port`             | 8025                        | port to run the server on                                                     |
| `server.hostname`         | contents of `/etc/hostname` | hostname of the mail server                                                   |
| `server.shutdown.timeout` | 5                           | time in seconds to wait for threads to finish before shutting down forcefully |
| `pool.connections.core`   | 0                           | number of unused threads to keep in the server thread pool                    |
| `pool.connections.max`    | 1024                        | max number of simultaneous connections to handle                              |
| `pool.idle.timeout`       | 60                          | time in seconds until idle threads are destroyed                              |
| `mailer.type`             | `debug`                     | the type of Mailer to use for sending mail                                    |
| `mailer.file.directory`   | `/tmp/mail`                 | the directory to use for mail when using the file Mailer                      |
