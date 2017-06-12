package de.funkedigital.hackathon;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLContext;

import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletResponse;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by aherr on 12.06.2017.
 */
@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class CrowdTangleProxy {

    @CrossOrigin
    @RequestMapping("/posts")
    public void posts(@RequestParam("listIds") String listIds, HttpServletResponse response) throws Exception {
        HttpClientBuilder b = HttpClientBuilder.create();

        // setup a Trust Strategy that allows all certificates.
        //
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException{
                return true;
            }
        }).build();
        b.setSSLContext( sslContext);

        HttpClient client = b.build();
        HttpGet request = new HttpGet("https://api.crowdtangle.com/posts?startDate=2017-05-01T00:00:00&listIds="+listIds);
        request.addHeader("x-api-token", "PuHehOp3YJXHmhXrLSat7nfK8sM6jxUKKLjyXhMa");
        HttpResponse forward = client.execute(request);
        HttpUtils.forward(forward, response);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CrowdTangleProxy.class, args);


    }

}
