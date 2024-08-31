package genai.bookmeet.domain.chat.controller;

import genai.bookmeet.domain.chat.dto.ChatRequest;
import genai.bookmeet.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestParam("userId") String userId, @RequestBody ChatRequest chatRequest) {
        log.debug("param :: " + chatRequest.toString());
        Map<String, Object> result = chatService.chat(userId,chatRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}