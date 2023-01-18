package controller;

import enums.ContentTypeEnum;
import enums.ControllerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;
import response.NewResponse;
import response.Response;
import response.ResponseSender;
import webserver.RequestResponseHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TemplateController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseHandler.class);
    @Override
    public NewResponse controllerService(Request request) throws IOException {
        logger.debug("firstLine : " + request.getRequestLine().getURL());
        String url = request.getRequestLine().getURL();

        byte[] body = Files.readAllBytes(new File("./src/main/resources/templates" + url).toPath());
        NewResponse newResponse = new NewResponse.Builder()
                .setResponseStatusLine(ControllerTypeEnum.TEMPLATE)
                .setResponseHeader(ContentTypeEnum.HTML,body.length)
                .setResponseBody(body)
                .build();
        return newResponse;


//        response.responseMaker(ControllerTypeEnum.TEMPLATE, ContentTypeEnum.HTML, body.length, url);
//        response.responseNewLineAdder();
//        response.responseBody(body);
    }
}