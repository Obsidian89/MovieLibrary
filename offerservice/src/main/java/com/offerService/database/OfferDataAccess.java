package com.offerService.database;

import com.offerService.model.Offer;
import com.offerService.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class OfferDataAccess {
    @Autowired
    private OfferRepository offerRepository;

    private DatabaseUtils databaseUtils = new DatabaseUtils();

    public void createOffer(Offer offer) {
        offer.setId(databaseUtils.getNextSequence("offer_sequence"));
        calculateExpiryDate(offer);
        offerRepository.save(offer);
    }

    public List getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        updateExpiredOffers(offers);
        return offers;
    }

    public Offer getOffer(String id) {
        Offer offer = offerRepository.findById(id)
                .orElse(null);

        if (offer != null) {
            updateExpiredOffer(offer);
        }

        return offer;
    }

    public void cancelOffer(String id) {
        Offer offer = offerRepository.findById(id)
                .orElse(null);

        if (offer != null) {
            offer.setExpired(true);
            offerRepository.save(offer);
        }

    }

    private void calculateExpiryDate(Offer offer) {
        offer.setExpiryDate(LocalDate.now().plus(offer.getDuration(), ChronoUnit.DAYS));
    }

    private void updateExpiredOffers(List<Offer> offers) {
        for (Offer offer : offers) {
            updateExpiredOffer(offer);
        }
    }

    private void updateExpiredOffer(Offer offer) {
        if (LocalDate.now().isAfter(offer.getExpiryDate()) && !offer.isExpired()) {
            cancelOffer(offer.getId());
            offer.setExpired(true);
        }
    }
}
