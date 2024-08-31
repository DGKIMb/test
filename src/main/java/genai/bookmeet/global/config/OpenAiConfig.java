package genai.bookmeet.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfig {

    @Value("${openai.api.key}")
    private String openAiKey;


//    @Bean
//    public RestTemplate restTemplate(){
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getInterceptors().add((request, body, execution) -> {
//            request.getHeaders().add("Authorization", "Bearer " + openAiKey);
//            return execution.execute(request, body);
//        });
//        return restTemplate;
//    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
