package reader.fileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Url;
import util.error.HttpsErrorMessage;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TemplatesFileReader implements FileReader {
    private static final Logger logger = LoggerFactory.getLogger(TemplatesFileReader.class);

    private static final String TEMPLATE_ROUTE = "./src/main/resources/templates";

    @Override
    public byte[] readFile(Url url) {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(new File(TEMPLATE_ROUTE + url.getUrl()).toPath());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(HttpsErrorMessage.NOT_VALID_URL + " url: {}", url);
        }
        return bytes;

    }
}