package com.kduytran.cards.service;

import com.kduytran.cards.dto.CardsDto;

public interface ICardsService {
    /**
     * @param mobileNumber - Mobile number of the customer
     */
    void createCard(String mobileNumber);

    /**
     *
     * @param cardsDto - CardsDto object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateCard(CardsDto cardsDto);

    /**
     *
     * @param mobileNumber - Mobile number of the customer
     * @return boolean indicating if the delete of card is successful or not
     */
    boolean deleteCard(String mobileNumber);

    /**
     *
     * @param mobileNumber - Mobile number of the customer
     * @return Card Details based on a given mobileNumber
     */
    CardsDto fetchCard(String mobileNumber);
}
