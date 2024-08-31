package genai.bookmeet.domain.chat.dto;

import genai.bookmeet.domain.chat.entity.Message;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageDto {

    private String role;
    private String content;

    public static MessageDto from(Message message) {
        return new MessageDto(message.getRole(), message.getContent());
    }

}
