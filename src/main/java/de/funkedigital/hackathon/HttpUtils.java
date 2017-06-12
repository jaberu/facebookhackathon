package de.funkedigital.hackathon;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jaberu on 14.11.2015.
 */
public final class HttpUtils {


    private static final Logger LOG = Logger.getLogger(HttpUtils.class);

    public static void forward(HttpResponse call, HttpServletResponse response) throws IOException {
        // Set the content type, as it comes from the server
        String contentType = call.getEntity().getContentType().getValue();
        if (contentType != null) {
            LOG.trace("Content-Type: " + contentType);
            response.setContentType(contentType);
        } else {
            LOG.trace("no Content-Type found in header");
        }
        response.setStatus(call.getStatusLine().getStatusCode());

        OutputStream out = response.getOutputStream();

        // Write the body, flush and close
        IOUtils.copy(call.getEntity().getContent(), out);
        out.flush();
    }

    private HttpUtils() {
    }
}
