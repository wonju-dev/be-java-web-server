package http.response;

import enums.Status;
import exception.ConnectionClosedException;
import filesystem.FindResource;
import http.common.Body;
import http.common.Cookie;
import http.common.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

import static http.common.HeaderAttribute.*;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private ResponseStartLine startLine = new ResponseStartLine();
    private Header header = new Header();
    private Body body = new Body();
    private DataOutputStream out;

    public HttpResponse(DataOutputStream out) {
        this.out = out;
    }

    public void send() {
        try {
            writeMessage();
            out.flush();
        } catch (IOException e) {
            throw new ConnectionClosedException(e);
        }
    }

    public void update(FindResource findResult) {
        startLine.setStatus(findResult.getStatus());
        header.setAttribute(CONTENT_TYPE, findResult.getContentType().getType());
        header.setAttribute(CONTENT_LENGTH, findResult.getResource().length);
        body.setBody(findResult.getResource());
    }

    private void writeMessage() {
        try {
            out.writeBytes(startLine.toString());
            out.writeBytes(header.toString());
            out.writeBytes(body.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void redirect(String redirectUrl) {
        startLine.setStatus(Status.FOUND);
        header.setAttribute(LOCATION, redirectUrl);
        body.clear();
        send();
    }

    public void setCookie(Cookie newCookie) {
        header.setAttribute(SET_COOKIE, newCookie.toString());
    }
}