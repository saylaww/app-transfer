package uz.pdp.lesson4tasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson4tasks.dto.CardDto;
import uz.pdp.lesson4tasks.dto.Response;
import uz.pdp.lesson4tasks.service.CardService;

import java.util.UUID;

@RestController
@RequestMapping("/card")
public class CardController {

    CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/transfer")
    public HttpEntity<?> transfer(@RequestParam Double amount,
                                  @RequestParam UUID senderId,
                                  @RequestParam Integer senderCardId,
                                  @RequestParam Integer recipientCardId) {
        final Response response = cardService.transfer(amount, senderId, senderCardId, recipientCardId);
        return ResponseEntity.status(response.isStatus() ? 201 : 409).body(response);
    }

    @PostMapping("/deposit")
    public HttpEntity<?> deposit(@RequestParam Double amount,
                                 @RequestParam UUID userId,
                                 @RequestParam Integer cardId) {
        final Response response = cardService.deposit(amount, userId, cardId);
        return ResponseEntity.status(response.isStatus() ? 201 : 409).body(response);
    }

    @PostMapping("/withdrawal")
    public HttpEntity<?> withdrawal(@RequestParam Double amount,
                                    @RequestParam UUID userId,
                                    @RequestParam Integer cardId) {
        final Response response = cardService.withdrawal(amount, userId, cardId);
        return ResponseEntity.status(response.isStatus() ? 201 : 409).body(response);
    }

    @PostMapping("/add")
    public HttpEntity<?> add(@RequestBody CardDto cardDto) {
        final Response response = cardService.add(cardDto);
        return ResponseEntity.status(response.isStatus() ? 201 : 409).body(response);
    }

}
