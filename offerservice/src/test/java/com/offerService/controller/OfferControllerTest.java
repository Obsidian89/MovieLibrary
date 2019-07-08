package com.offerService.controller;

import com.offerService.database.OfferDataAccess;
import com.offerService.model.Offer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OfferControllerTest
{
    Fixture fixture;

    @Before
    public void setUp()
    {
      fixture = new Fixture();
    }

    @Test
    public void createOfferTest()
    {
        fixture.givenIHaveAnOffer();
        fixture.whenITryToCreateAnOffer();
        fixture.thenTheOfferPostedToTheDatabase();
        fixture.thenACreatedResponseIsReturned();
    }

    @Test
    public void getAllOffersTest()
    {
        fixture.givenThereAreOffersInTheDatabase();
        fixture.whenIRequestAllOffers();
        fixture.thenAListOfOffersIsReturned();
        fixture.thenASuccessfulResponseIsReturned();
    }

    @Test
    public void getOfferTest()
    {
        fixture.givenThereIsAnOfferInTheDatabase();
        fixture.whenIRequestAnOfferById();
        fixture.thenAnOfferIsReturned();
        fixture.thenASuccessfulResponseIsReturned();
    }

    @Test
    public void cancelOfferTest()
    {
        fixture.whenICancelAnValidOffer();
        fixture.thenCancelOfferIsCalled();
        fixture.thenASuccessfulResponseIsReturned();
    }

    private class Fixture
    {
        @InjectMocks
        private OfferController offerController = new OfferController();
        @Mock
        private OfferDataAccess offerDataAccess;
        private Offer offer;
        private ResponseEntity result;

        private Fixture()
        {
            MockitoAnnotations.initMocks(this);
        }

        private void givenIHaveAnOffer()
        {
            offer = new Offer();
            offer.setId("1");
            offer.setExpired(false);
            offer.setAmount(2.0);
            offer.setDescription("This is an offer");
            offer.setName("TestOffer");
        }

        private void whenITryToCreateAnOffer()
        {
            result = offerController.createOffer(offer);
        }

        private void thenTheOfferPostedToTheDatabase()
        {
            verify(offerDataAccess).createOffer(offer);
        }

        private void thenASuccessfulResponseIsReturned()
        {
            assertEquals(result.getStatusCode(), HttpStatus.OK);
        }

        private void thenACreatedResponseIsReturned()
        {
            assertEquals(result.getStatusCode(), HttpStatus.CREATED);
        }

        private void givenThereAreOffersInTheDatabase()
        {
            givenIHaveAnOffer();
            when(offerDataAccess.getAllOffers()).thenReturn(Collections.singletonList(offer));
        }

        private void whenIRequestAllOffers()
        {
            result = offerController.getAllOffers();
        }

        private void thenAListOfOffersIsReturned()
        {
            List<Offer> responseBody = (List<Offer>)result.getBody();
            assertEquals(1, responseBody.size());
            assertEquals("TestOffer", responseBody.get(0).getName());
        }

        private void whenIRequestAnOfferById()
        {
            result = offerController.getOffer("1");
        }

        private void thenAnOfferIsReturned()
        {
            Offer responseBody = (Offer)result.getBody();
            assertEquals("1", responseBody.getId());
            assertEquals("This is an offer",responseBody.getDescription());
            assertEquals("TestOffer", responseBody.getName());
        }

        private void whenICancelAnValidOffer()
        {
            result = offerController.cancelOffer("1");
        }

        private void thenCancelOfferIsCalled()
        {
            verify(offerDataAccess).cancelOffer("1");
        }

        private void givenThereIsAnOfferInTheDatabase()
        {
            givenIHaveAnOffer();
            when(offerDataAccess.getOffer(anyString())).thenReturn(offer);
        }

    }
}


