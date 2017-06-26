package ru.nsu.ccfit.boltava.model.server;

import ru.nsu.ccfit.boltava.model.chat.User;
import ru.nsu.ccfit.boltava.model.message.Request;
import ru.nsu.ccfit.boltava.model.message.ServerMessage;
import ru.nsu.ccfit.boltava.model.message.event.UserLeftChatEvent;
import ru.nsu.ccfit.boltava.model.net.IServerSocketMessageStream;
import ru.nsu.ccfit.boltava.model.net.ISocketMessageStream;
import ru.nsu.ccfit.boltava.model.net.ISocketMessageStream.MessageStreamType;
import ru.nsu.ccfit.boltava.model.net.ServerMessageStreamFactory;
import ru.nsu.ccfit.boltava.model.serializer.IMessageSerializer;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class ChatMember {

    private static AtomicLong ID_GENERATOR = new AtomicLong(0);

    private long id = ID_GENERATOR.incrementAndGet();
    private User user;
    private String sessionId;

    private Socket socket;
    private Server server;
    private IServerMessageHandler messageHandler;
    private IServerSocketMessageStream stream;

    private Thread senderThread;
    private Thread receiverThread;
    private LinkedBlockingQueue<ServerMessage> messageQueue = new LinkedBlockingQueue<>();
    private boolean isClosed;

    public ChatMember(Socket socket,
                      Server server,
                      MessageStreamType type) throws IOException, JAXBException {
        this.socket = socket;
        this.server = server;
        messageHandler = new ServerMessageHandler(server, this);
        stream = ServerMessageStreamFactory.get(type, socket);

        senderThread = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    stream.write(messageQueue.take());
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted");
            } catch (ISocketMessageStream.StreamWriteException | IMessageSerializer.MessageSerializationException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }, "Sender Thread");


        receiverThread = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    Request msg = stream.read();
                    msg.handle(messageHandler);
                }
            } catch (InterruptedException | IMessageSerializer.MessageSerializationException | ISocketMessageStream.StreamReadException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }, "Receiver Thread");

        senderThread.start();
        receiverThread.start();

    }

    public void sendMessage(ServerMessage msg) throws InterruptedException {
        messageQueue.put(msg);
    }

    public long getID() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    void close() {
        try {
            if (!isClosed) {
                isClosed = true;
                socket.close();
                if (getUser() != null) {
                    server.broadcastMessageFrom(new UserLeftChatEvent(getUser().getUsername()), this);
                }
                server.removeChatMember(this);
            }
        } catch (InterruptedException | IOException e) {}
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMember that = (ChatMember) o;

        if (id != that.id) return false;
        return sessionId != null ? sessionId.equals(that.sessionId) : that.sessionId == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        return result;
    }
}
