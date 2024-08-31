package genai.bookmeet.domain.chat.repository;

import genai.bookmeet.domain.chat.entity.Chat;
import genai.bookmeet.domain.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c join fetch c.messages")
    Chat findByUserId(String userId);

}
