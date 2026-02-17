package org.example.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Main {

    private static final int PORT = 23456;
    private static final String DB_PATH = "server/data/db.json";

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();

    private static volatile boolean running = true;
    private static ServerSocket serverSocket;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            Path dbPath = Paths.get(DB_PATH);
            if (!Files.exists(dbPath)) {
                Files.createDirectories(dbPath.getParent());
                Files.write(dbPath, "{}".getBytes());
            }

            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started!");

            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.submit(() -> handleClient(socket));
                } catch (IOException e) {
                    if (!running) break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    static void handleClient(Socket socket) {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String requestJson = input.readUTF();
            Map<String, Object> request = gson.fromJson(requestJson, Map.class);

            Map<String, Object> response = new HashMap<>();
            String type = (String) request.get("type");

            // ---- EXIT HANDLED FIRST ----
            if ("exit".equals(type)) {
                response.put("response", "OK");
                output.writeUTF(gson.toJson(response));

                running = false;
                if (serverSocket != null) {
                    serverSocket.close();
                }
                if (executor != null) {
                    executor.shutdown();
                }
                return;
            }


            Object keyObj = request.get("key");

            // All other commands require a string key
            if (!(keyObj instanceof String)) {
                response.put("response", "ERROR");
                response.put("reason", "Wrong key type");
                output.writeUTF(gson.toJson(response));
                return;
            }

            String key = keyObj.toString();

            switch (type) {
                case "get":
                    readLock.lock();
                    try {
                        Map<String, Object> db = readDatabase();
                        if (db.containsKey(key)) {
                            response.put("response", "OK");
                            response.put("value", db.get(key));
                        } else {
                            response.put("response", "ERROR");
                            response.put("reason", "No such key");
                        }
                    } finally {
                        readLock.unlock();
                    }
                    break;

                case "set":
                    writeLock.lock();
                    try {
                        Map<String, Object> db = readDatabase();
                        db.put(key, request.get("value"));
                        writeDatabase(db);
                        response.put("response", "OK");
                    } finally {
                        writeLock.unlock();
                    }
                    break;

                case "delete":
                    writeLock.lock();
                    try {
                        Map<String, Object> db = readDatabase();
                        if (db.containsKey(key)) {
                            db.remove(key);
                            writeDatabase(db);
                            response.put("response", "OK");
                        } else {
                            response.put("response", "ERROR");
                            response.put("reason", "No such key");
                        }
                    } finally {
                        writeLock.unlock();
                    }
                    break;

                default:
                    response.put("response", "ERROR");
                    response.put("reason", "Unknown request type");
            }

            output.writeUTF(gson.toJson(response));

        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    private static Map<String, Object> readDatabase() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(DB_PATH)));
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> db = gson.fromJson(content, type);
        return db != null ? db : new HashMap<>();
    }

    private static void writeDatabase(Map<String, Object> db) throws IOException {
        Files.write(Paths.get(DB_PATH), gson.toJson(db).getBytes());
    }
}

