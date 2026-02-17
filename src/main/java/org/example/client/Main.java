package org.example.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;

public class Main {

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            // Build the request from CLI or file
            Map<String, Object> request = buildRequest(args);

            System.out.println("Client started!");
            String requestJson = gson.toJson(request);
            System.out.println("Sent: " + requestJson);

            // Send the request to server
            String responseJson = sendRequest(requestJson, "localhost", 23456);
            System.out.println("Received: " + responseJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Build a request map from command-line args or a file (-in <filename>).
     */
    public static Map<String, Object> buildRequest(String[] args) throws IOException {
        Map<String, Object> request = new HashMap<>();

        if (args.length >= 2 && "-in".equals(args[0])) {
            String fileName = args[1];
            Path filePath = Paths.get("client/data/" + fileName);
            String content = Files.readString(filePath); // Java 11+
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            return gson.fromJson(content, type);
        }

        // Parse CLI args: -t <type> -k <key> -v <value>
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-t":
                    request.put("type", args[++i]);
                    break;
                case "-k":
                    request.put("key", args[++i]);
                    break;
                case "-v":
                    String valueArg = args[++i];
                    Object value;
                    try {
                        value = gson.fromJson(valueArg, Object.class);
                    } catch (Exception e) {
                        value = valueArg;
                    }
                    request.put("value", value);
                    break;
            }
        }
        return request;
    }

    public static String sendRequest(String requestJson, String host, int port) throws IOException {
        try (
                Socket socket = new Socket(host, port);
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream())
        ) {
            output.writeUTF(requestJson);

            try {
                return input.readUTF();
            } catch (EOFException ignored) {
                // Server may close connection after "exit"
                return "";
            }
        }
    }
}
