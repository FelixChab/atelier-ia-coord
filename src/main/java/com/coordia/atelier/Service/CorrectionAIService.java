package com.coordia.atelier.Service;

import com.coordia.atelier.Exception.AIServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CorrectionAIService {

    private final FileReadingService fileReadingService;
    private final OllamaChatModel chatModel;
    private final LogService logService;

    public String corrigerTexte(String texteOriginal) {
        try {
            log.info("Demande de correction pour un texte de {} caractères", texteOriginal.length());

            // Lecture du prompt de correction orthographique
            String promptTemplate = fileReadingService.readInternalFileAsString("prompts/promptCorrecteur.txt");

            // Préparation des messages pour l'IA
            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage("<start_of_turn>" + promptTemplate + "<end_of_turn>"));
            messages.add(new UserMessage("<start_of_turn>" + texteOriginal + "<end_of_turn>"));

            // Création et envoi du prompt
            Prompt promptToSend = new Prompt(messages);
            Flux<ChatResponse> chatResponses = chatModel.stream(promptToSend);

            // Récupération et assemblage de la réponse
            String texteCorrige = Objects.requireNonNull(chatResponses.collectList().block()).stream()
                    .map(response -> response.getResult().getOutput().getText())
                    .collect(Collectors.joining(""));

            // Enregistrement de l'interaction réussie
            logService.logInteraction(texteOriginal, texteCorrige, true, null);

            log.info("Correction réussie, réponse de {} caractères", texteCorrige.length());
            return texteCorrige;
        }
        catch (Exception e) {
            log.error("Erreur lors de la correction du texte", e);
            // Enregistrement de l'interaction échouée
            logService.logInteraction(texteOriginal, null, false, e.getMessage());
            throw new AIServiceException("Erreur lors de la correction du texte", e);
        }
    }
}