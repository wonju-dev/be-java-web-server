package http.request;

import http.common.Method;
import http.common.Protocol;

public class RequestStartLine {
    private Method method;
    private String url;
    private Protocol protocol;

    public RequestStartLine(String startLine) {
        String[] chunks = startLine.split(" ");
        this.method = Method.find(chunks[0]);
        this.url = chunks[1];
        this.protocol = Protocol.find(chunks[2]);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }
}

