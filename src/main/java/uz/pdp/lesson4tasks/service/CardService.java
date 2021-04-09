package uz.pdp.lesson4tasks.service;

import org.springframework.stereotype.Service;
import uz.pdp.lesson4tasks.dto.CardDto;
import uz.pdp.lesson4tasks.dto.Response;
import uz.pdp.lesson4tasks.entity.Card;
import uz.pdp.lesson4tasks.entity.Income;
import uz.pdp.lesson4tasks.entity.Outcome;
import uz.pdp.lesson4tasks.entity.User;
import uz.pdp.lesson4tasks.repository.CardRepository;
import uz.pdp.lesson4tasks.repository.IncomeRepository;
import uz.pdp.lesson4tasks.repository.OutcomeRepository;
import uz.pdp.lesson4tasks.repository.UserRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {

    UserRepository userRepository;
    CardRepository cardRepository;
    IncomeRepository incomeRepository;
    OutcomeRepository outcomeRepository;

    public CardService(UserRepository userRepository,
                       CardRepository cardRepository,
                       IncomeRepository incomeRepository,
                       OutcomeRepository outcomeRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.incomeRepository = incomeRepository;
        this.outcomeRepository = outcomeRepository;
    }


    public Response withdrawal(Double amount, UUID userId, Integer cardId) {

        Outcome outcome = new Outcome();
        outcome.setAmount(amount);

        final Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isEmpty()) {
            return new Response("Card not found", false);
        }
        outcome.setFromCard(optionalCard.get());

        final Card card = optionalCard.get();
        final Double money = card.getMoney();

        double commissionAmount = amount * 0.01;
        outcome.setCommissionAmount(commissionAmount);

        outcomeRepository.save(outcome);

        if (money < (money - amount * 1.01)) {
            return new Response("Not enough money", false);
        }

        double newBalance = money - amount * 1.01;
        cardRepository.editMoney(newBalance, cardId);

        return new Response("Withdrawal processed successfully", true);
    }

    public Response deposit(Double amount, UUID userId, Integer cardId) {

        Income income = new Income();
        income.setAmount(amount);

        final Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isEmpty()) {
            return new Response("Card not found", false);
        }
        income.setToCard(optionalCard.get());

        incomeRepository.save(income);

        final Card card = cardRepository.getCardByUserId(userId);
        final Double money = card.getMoney();

        cardRepository.editMoney(amount + money, cardId);

        return new Response("Added", true);
    }

    public Response add(CardDto cardDto) {
        Card card = new Card();
        card.setActive(cardDto.isActive());
        card.setExpiredDate(cardDto.getExpiredDate());
        card.setNumber(cardDto.getNumber());
        card.setUsername(cardDto.getUsername());

        final Optional<User> optionalUser = userRepository.findById(cardDto.getUserId());
        if (optionalUser.isEmpty()) {
            return new Response("User not found", false);
        }
        card.setUser(optionalUser.get());

        final Card save = cardRepository.save(card);
        return new Response("Saved!", true, save);
    }

    public Response transfer(Double amount, UUID userId, Integer senderCardId, Integer recipientCardId) {

        final boolean existsByIdAndUserId = cardRepository.existsByIdAndUserId(senderCardId, userId);
        if (!existsByIdAndUserId) {
            return new Response("No user with card id: " + senderCardId, false);
        }

        final boolean existsById = cardRepository.existsById(recipientCardId);
        if (!existsById) {
            return new Response("Not found recipient card", false);
        }

        final Double money = cardRepository.getMoney(senderCardId);

        double commissionAmount = amount * 0.01;
        double amountWithCommission = amount * 1.01;
        if (amountWithCommission > money) {
            return new Response("Not enough money", false);
        }

        double remainSenderMoney = money - amountWithCommission;
        cardRepository.editMoney(remainSenderMoney, senderCardId);

        final Double recipientMoney = cardRepository.getMoney(recipientCardId);
        final double newRecipientMoney = recipientMoney + amount;
        cardRepository.editMoney(newRecipientMoney, recipientCardId);

        Income income = new Income();
        income.setFromCard(cardRepository.getCardById(senderCardId));
        income.setToCard(cardRepository.getCardById(recipientCardId));
        income.setAmount(amount);
        incomeRepository.save(income);

        Outcome outcome = new Outcome();
        outcome.setCommissionAmount(commissionAmount);
        outcome.setAmount(amount);
        outcome.setFromCard(cardRepository.getCardById(senderCardId));
        outcome.setToCard(cardRepository.getCardById(recipientCardId));
        final Outcome save = outcomeRepository.save(outcome);

        return new Response("Transfer successfully processed", true, save);


    }
}
