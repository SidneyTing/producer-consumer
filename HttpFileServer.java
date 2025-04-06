import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.*;
import java.util.*;

public class HttpFileServer {

    private final int port;
    private final File saved_dir = new File("saved");
    private final File gui_dir = new File("gui");

    public HttpFileServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/videos", new VideoHandler());
        server.createContext("/list", new ListHandler());
        server.createContext("/", new GuiHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("HTTP server started at http://localhost:" + port + "!");
    }
    
    class VideoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String filename = path.substring("/videos/".length());

            File videoFile = new File(saved_dir, filename);

            byte[] videoBytes = Helper.convertFileToBytes(videoFile);
            
            exchange.getResponseHeaders().set("Content-Type", "video/mp4");
            exchange.sendResponseHeaders(200, videoBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(videoBytes);
            os.close();
        }
    }

    class ListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File[] videoFiles = saved_dir.listFiles((dir, name) -> name.endsWith(".mp4") || name.endsWith(".mov"));

            List<String> videoFilenames = new ArrayList<>();
            if (videoFiles != null && videoFiles.length != 0) {
                for (File videoFile : videoFiles) {
                    videoFilenames.add(videoFile.getName());
                }
            }

            List<String> temp = new ArrayList<>();
            for (String filename : videoFilenames) {
                temp.add("\"" + filename + "\"");
            }
            String json = "[" + String.join(",", temp) + "]";

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, json.length());
            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
        }
    }

    class GuiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            File file;
            if (path.equals("/")) {
                file = new File(gui_dir, "index.html");
            } else {
                file = new File(gui_dir, path.substring(1));
            }

            byte[] fileBytes = Helper.convertFileToBytes(file);
            String contentType = Files.probeContentType(file.toPath());

            exchange.getResponseHeaders().set("Content-Type", contentType != null ? contentType : "plain/text");
            exchange.sendResponseHeaders(200, fileBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(fileBytes);
            os.close();
        }
    }
}
