package de.funkedigital.hackathon;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLContext;

import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletResponse;

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
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
        SSLContext.setDefault(sslContext);
        SSLSocketFactory sf = new SSLSocketFactory(sslContext);
        Scheme httpsScheme = new Scheme("https", 443, sf);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(httpsScheme);

// apache HttpClient version >4.2 should use BasicClientConnectionManager
        ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
        HttpClient client = new DefaultHttpClient(cm);


        HttpGet request = new HttpGet("https://api.crowdtangle.com/posts?listIds="+listIds);
        request.addHeader("x-api-token", "PuHehOp3YJXHmhXrLSat7nfK8sM6jxUKKLjyXhMa");
        HttpResponse forward = client.execute(request);
        HttpUtils.forward(forward, response);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CrowdTangleProxy.class, args);


    }

}
