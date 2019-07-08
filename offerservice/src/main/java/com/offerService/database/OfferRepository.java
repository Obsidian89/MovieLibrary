package com.offerService.database;

import com.offerService.model.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String>
{
}
