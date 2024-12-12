package br.com.picpay.picpaysimplificado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;

    public boolean authorizeTransaction() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

            if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode().value() == 403) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    String status = (String) responseBody.get("status");
                    Map<String, Object> data = (Map<String, Object>) responseBody.get("data");

                    return "success".equalsIgnoreCase(status)
                            && data != null
                            && Boolean.TRUE.equals(data.get("authorization"));
                }
            }
        } catch (HttpStatusCodeException e) {
            System.out.println("Erro ao autorizar transação: " + e.getMessage());
        }
        return false;
    }
}
