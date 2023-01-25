package webserver;

import controller.Controller;
import exception.FileNotFoundException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Map<String, Controller> controllers;

    public RequestHandler(Socket connectionSocket,  Map<String, Controller> controllers) {
        this.connection = connectionSocket;
        this.controllers = controllers;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.from(in);

            HttpResponse httpResponse = HttpResponse.createDefaultHttpResponse(out);

            Controller controller = controllers.getOrDefault(httpRequest.getPath(), controllers.get("file"));
            logger.info(controller.toString());

            controller.service(httpRequest, httpResponse);

        } catch (IOException | URISyntaxException | FileNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

}
