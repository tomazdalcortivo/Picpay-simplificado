package br.com.picpay.picpaysimplificado.infra;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        return getRestTemplateWithoutSSL();
    }

    /**
     * @return Method to get a RestTemplate with SSL verification disabled
     */
    public RestTemplate getRestTemplateWithoutSSL() throws NoSuchAlgorithmException, KeyManagementException {

        // Create SSL context to trust all certificates
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // Define trust managers to accept all certificates
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                // Accept all client certificates
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                // Accept all server certificates
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        // Initialize SSL context with the defined trust managers
        sslContext.init(null, trustManagers, null);

        // Set the default SSL socket factory to use the custom SSL context
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // Set the default hostname verifier to allow all hostnames
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        return new RestTemplateBuilder().requestFactory(SimpleClientHttpRequestFactory.class).build();
    }
}