package org.example.server;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.net.ServerSocket;
import java.nio.file.*;
import java.io.*;
import java.net.Socket;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MainTest {

    private Gson gson;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        gson = new Gson();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testMain_createsDbAndStartsServer() throws Exception {
        // Mock static Files methods
        try (MockedStatic<Files> filesMock = mockStatic(Files.class);
             MockedConstruction<ServerSocket> serverSocketMock = mockConstruction(ServerSocket.class,
                     (mock, context) -> {
                         // accept() returns a mock socket once, then throw IOException to exit loop
                         when(mock.accept())
                                 .thenReturn(mock(Socket.class))
                                 .thenThrow(new IOException("stop loop"));
                     })) {

            Path fakePath = Paths.get("fake/db.json");

            // Simulate DB file does not exist
            filesMock.when(() -> Files.exists(any(Path.class))).thenReturn(false);
            filesMock.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            filesMock.when(() -> Files.write(any(Path.class), any(byte[].class))).thenReturn(fakePath);

            // Call main
            Main.main(new String[]{});

            // Verify DB directory & file creation
            filesMock.verify(() -> Files.exists(any(Path.class)));
            filesMock.verify(() -> Files.createDirectories(any(Path.class)));
            filesMock.verify(() -> Files.write(any(Path.class), eq("{}".getBytes())));

            // Verify server prints started
            String output = outContent.toString();
            assertTrue(output.contains("Server started!"));
        }
    }


    private String runHandleClient(Map<String, Object> request) throws IOException {
        // Prepare input stream with proper UTF encoding
        ByteArrayOutputStream inputBuffer = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(inputBuffer)) {
            dos.writeUTF(gson.toJson(request));
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBuffer.toByteArray());

        // Output stream to capture responses
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Socket fakeSocket = new Socket() {
            @Override
            public InputStream getInputStream() {
                return inputStream;
            }

            @Override
            public OutputStream getOutputStream() {
                return outputStream;
            }
        };

        Main.handleClient(fakeSocket);
        return outputStream.toString();
    }


    @Test
    void testSetAndGet() throws IOException {
        // SET
        String setResponse = runHandleClient(Map.of("type", "set", "key", "name", "value", "Alice"));
        assertTrue(setResponse.contains("OK"));

        // GET
        String getResponse = runHandleClient(Map.of("type", "get", "key", "name"));
        assertTrue(getResponse.contains("OK") && getResponse.contains("Alice"));
    }

    @Test
    void testDeleteNonExistingKey() throws IOException {
        String response = runHandleClient(Map.of("type", "delete", "key", "nonexistent"));
        assertTrue(response.contains("ERROR") && response.contains("No such key"));
    }

    @Test
    void testGetNonExistingKey() throws IOException {
        String response = runHandleClient(Map.of("type", "get", "key", "missing"));
        assertTrue(response.contains("ERROR") && response.contains("No such key"));
    }

    @Test
    void testInvalidKeyType() throws IOException {
        String response = runHandleClient(Map.of("type", "set", "key", 123, "value", "value"));
        assertTrue(response.contains("ERROR") && response.contains("Wrong key type"));
    }

    @Test
    void testUnknownRequestType() throws IOException {
        String response = runHandleClient(Map.of("type", "foobar", "key", "key"));
        assertTrue(response.contains("ERROR") && response.contains("Unknown request type"));
    }

    @Test
    void testExit() throws IOException {
        String response = runHandleClient(Map.of("type", "exit"));
        assertTrue(response.contains("OK"));
    }

    @Test
    void testHandleClientSet() throws Exception {

        Gson gson = new Gson();

        Map<String, Object> request = Map.of(
                "type", "set",
                "key", "name",
                "value", "Alice"
        );

        // Simulate client writing using writeUTF
        ByteArrayOutputStream clientBytes = new ByteArrayOutputStream();
        DataOutputStream clientOut = new DataOutputStream(clientBytes);
        clientOut.writeUTF(gson.toJson(request));
        clientOut.flush();

        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(clientBytes.toByteArray());

        ByteArrayOutputStream serverOutput = new ByteArrayOutputStream();

        Socket socket = new Socket() {
            @Override
            public InputStream getInputStream() {
                return inputStream;
            }

            @Override
            public OutputStream getOutputStream() {
                return serverOutput;
            }
        };

        Main.handleClient(socket);

        DataInputStream responseReader =
                new DataInputStream(new ByteArrayInputStream(serverOutput.toByteArray()));

        String response = responseReader.readUTF();

        assertTrue(response.contains("\"response\":\"OK\""));
    }


    @Test
    void testHandleClientDeleteNonExistingKey() throws Exception {

        Gson gson = new Gson();

        Map<String, Object> request = Map.of(
                "type", "delete",
                "key", "nonexistent"
        );

        // Simulate client writing correctly using writeUTF
        ByteArrayOutputStream clientBytes = new ByteArrayOutputStream();
        DataOutputStream clientOut = new DataOutputStream(clientBytes);
        clientOut.writeUTF(gson.toJson(request));
        clientOut.flush();

        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(clientBytes.toByteArray());

        ByteArrayOutputStream serverOutput = new ByteArrayOutputStream();

        Socket socket = new Socket() {
            @Override
            public InputStream getInputStream() {
                return inputStream;
            }

            @Override
            public OutputStream getOutputStream() {
                return serverOutput;
            }
        };

        Main.handleClient(socket);

        // Read server response properly
        DataInputStream responseReader =
                new DataInputStream(new ByteArrayInputStream(serverOutput.toByteArray()));

        String response = responseReader.readUTF();

        assertTrue(response.contains("\"response\":\"ERROR\""));
        assertTrue(response.contains("No such key"));
    }

}
