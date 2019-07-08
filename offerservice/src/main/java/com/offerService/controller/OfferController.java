package com.offerService.controller;

import com.offerService.database.OfferDataAccess;
import com.offerService.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class OfferController {

    private final String baseUrl = "/offer";

    @Autowired
    private OfferDataAccess offerDataAccess;

    @RequestMapping(value = baseUrl, method = RequestMethod.POST)
    public ResponseEntity<Object> createOffer(@RequestBody Offer offer) {
        offerDataAccess.createOffer(offer);
        return new ResponseEntity<Object>("Offer Created", HttpStatus.CREATED);
    }

    @RequestMapping(value = baseUrl, method = RequestMethod.GET)
    public ResponseEntity<Object> getAllOffers() {
        return new ResponseEntity<Object>(offerDataAccess.getAllOffers(), HttpStatus.OK);
    }

    @RequestMapping(value = baseUrl + "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOffer(@PathVariable("id") String id) {
        return new ResponseEntity<Object>(offerDataAccess.getOffer(id), HttpStatus.OK);
    }

    @RequestMapping(value = baseUrl + "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> cancelOffer(@PathVariable("id") String id) {
        offerDataAccess.cancelOffer(id);
        return new ResponseEntity<Object>("Offer cancelled", HttpStatus.OK);
    }
}
