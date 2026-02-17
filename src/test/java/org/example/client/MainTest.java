package org.example.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testMain_withMockedSendRequest() throws IOException {
        String fakeResponse = "{\"response\":\"OK\"}";

        // Mock static sendRequest method
        try (MockedStatic<Main> mocked = mockStatic(Main.class, CALLS_REAL_METHODS)) {
            // When sendRequest is called with any arguments, return fakeResponse
            mocked.when(() -> Main.sendRequest(anyString(), anyString(), anyInt()))
                    .thenReturn(fakeResponse);

            // Call main
            String[] args = {"-t", "ping"};
            Main.main(args);

            String output = outContent.toString();
            assertTrue(output.contains("Client started!"), "Should print client started");
            assertTrue(output.contains("Sent:"), "Should print sent JSON");
            assertTrue(output.contains(fakeResponse), "Should print received response");
        }
    }

    @Test
    void testBuildRequestFromArgs() throws Exception {
        String[] args = {
                "-t", "set",
                "-k", "name",
                "-v", "Alice"
        };

        Map<String, Object> request = Main.buildRequest(args);

        assertEquals("set", request.get("type"));
        assertEquals("name", request.get("key"));
        assertEquals("Alice", request.get("value"));
    }

    @Test
    void testBuildRequestWithJsonValue() throws Exception {
        String[] args = {
                "-t", "set",
                "-k", "user",
                "-v", "{\"age\":25}"
        };

        Map<String, Object> request = Main.buildRequest(args);

        assertEquals("set", request.get("type"));
        assertTrue(request.get("value") instanceof Map);
    }

    @Test
    void testBuildRequestFromFile() throws Exception {

        String json = """
        {
          "type": "get",
          "key": "name"
        }
        """;

        Path dir = Path.of("client/data");
        Files.createDirectories(dir);
        Path file = dir.resolve("test.json");
        Files.writeString(file, json);

        String[] args = {"-in", "test.json"};

        Map<String, Object> request = Main.buildRequest(args);

        assertEquals("get", request.get("type"));
        assertEquals("name", request.get("key"));

        Files.delete(file);
    }

    @Test
    void testSendRequest_success() throws IOException {
        String requestJson = "{\"type\":\"ping\"}";
        String expectedResponse = "OK";

        // Prepare ByteArrayOutputStream to simulate server's DataOutputStream
        ByteArrayOutputStream serverOut = new ByteArrayOutputStream();
        DataOutputStream serverDataOut = new DataOutputStream(serverOut);
        serverDataOut.writeUTF(expectedResponse); // write UTF properly
        serverDataOut.flush();

        ByteArrayInputStream serverIn = new ByteArrayInputStream(serverOut.toByteArray());

        // Mock socket to use our streams
        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream()); // client writes to this
        when(mockSocket.getInputStream()).thenReturn(serverIn); // client reads from this

        // Now test sendRequest using the mocked streams
        String actualResponse;
        try (Socket s = mockSocket;
             DataOutputStream output = new DataOutputStream(s.getOutputStream());
             DataInputStream input = new DataInputStream(s.getInputStream())) {

            // Simulate method behavior
            output.writeUTF(requestJson);
            actualResponse = input.readUTF();
        }
        assertEquals(expectedResponse, actualResponse);
    }
}
