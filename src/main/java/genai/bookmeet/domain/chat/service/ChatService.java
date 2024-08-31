package genai.bookmeet.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import genai.bookmeet.domain.chat.dto.ChatRequest;
import genai.bookmeet.domain.chat.dto.MessageDto;
import genai.bookmeet.domain.chat.entity.Chat;
import genai.bookmeet.domain.chat.entity.Message;
import genai.bookmeet.domain.chat.repository.ChatRepository;
import genai.bookmeet.domain.chat.repository.MessageRepository;
import genai.bookmeet.global.config.OpenAiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor

@Service
public class ChatService {

    private final OpenAiConfig openAiConfig;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String promptUrl;

    @Transactional
    public Map<String, Object> chat(String userId, ChatRequest chatRequest) {
        log.debug("[+] 신규 프롬프트를 수행합니다.");

        Map<String, Object> resultMap = new HashMap<>();

        Chat chat = chatRepository.findByUserId(userId);

        if (chat == null) {
            chat = Chat.builder().userId(userId).build();
            chat.getMessages().add(Message.builder().role(chatRequest.getMessages().get(0).getRole())
                    .content(chatRequest.getMessages().get(0).getContent()).chat(chat).build());
        }
        else {
            chat.getMessages().add(Message.builder().role(chatRequest.getMessages().get(0).getRole())
                    .content(chatRequest.getMessages().get(0).getContent()).chat(chat).build());
        }

        List<MessageDto> messageDtos = chat.getMessages().stream().map(MessageDto::from).toList();

        chatRequest.setMessages(messageDtos);

        HttpHeaders headers = openAiConfig.httpHeaders();

        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);
        ResponseEntity<String> response = openAiConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        try {
            // [STEP6] String -> HashMap 역직렬화를 구성합니다.
            ObjectMapper om = new ObjectMapper();
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {
            });

            List<Map<String, Object>> choicesList = (List<Map<String, Object>>) resultMap.get("choices");
            Map<String, Object> firstChoice = choicesList.get(0);

            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");

            chat.getMessages().add(Message.builder().role(message.get("role").toString())
                    .content(message.get("content").toString())
                    .chat(chat).build());

            chatRepository.save(chat);

        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultMap;
    }


}