package com.offerService.database;

import com.offerService.model.Offer;
import com.offerService.utils.DatabaseUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OfferDataAccessTest
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
        fixture.givenIHaveAValidOffer();
        fixture.whenICreateAnOffer();
        fixture.thenTheIdIsGenerated();
        fixture.thenTheOfferIsCreated();
    }

    @Test
    public void getAllOffersTest()
    {
        fixture.givenIHaveAListOfValidOffers();
        fixture.whenICallGetAllOffers();
        fixture.thenAListOfOffersIsReturned();
    }

    @Test
    public void getOfferValidTest()
    {
        fixture.givenIHaveAValidOffer();
        fixture.givenTheOfferIsReturned();
        fixture.whenICallGetOffer();
        fixture.thenAnOfferIsReturned();
    }

    @Test
    public void getOfferInvalidOffer()
    {
        fixture.whenICallGetOfferWithAnInvalidOffer();
        fixture.thenNullIsReturned();
    }

    @Test
    public void getOfferExpiredTest()
    {
        fixture.givenIHaveAnExpiredOffer();
        fixture.whenIRequestTheExpiredOffer();
        fixture.thenTheOfferIsUpdatedToBeExpired();
        fixture.thenAnOfferIsReturned();
    }

    @Test
    public void cancelOffer()
    {
        fixture.givenIHaveAValidOffer();
        fixture.givenOfferIsReturnedFromDB();
        fixture.whenICancelTheOffer();
        fixture.thenTheOfferIsUpdated();
    }

    private class Fixture
    {
        @InjectMocks
        private OfferDataAccess offerDataAccess = new OfferDataAccess();

        @Mock
        private OfferRepository offerRepository;
        @Mock
        private DatabaseUtils databaseUtils;

        private Offer offer;
        private List resultList;
        private Offer resultOffer;

        private Fixture()
        {
            MockitoAnnotations.initMocks(this);
        }

        private void givenIHaveAValidOffer()
        {
            offer = new Offer();
            offer.setName("TestOffer");
            offer.setExpired(false);
            offer.setDuration(2);
            offer.setExpiryDate(LocalDate.now());
        }

        private void whenICreateAnOffer()
        {
            offerDataAccess.createOffer(offer);
        }

        private void thenTheIdIsGenerated()
        {
            verify(databaseUtils).getNextSequence(anyString());
        }

        private void thenTheOfferIsCreated()
        {
            verify(offerRepository).save(any(Offer.class));
        }

        private void givenIHaveAListOfValidOffers()
        {
            givenIHaveAValidOffer();
            when(offerRepository.findAll()).thenReturn(Collections.singletonList(offer));
        }

        private void whenICallGetAllOffers()
        {
            resultList = offerDataAccess.getAllOffers();
        }

        private void thenAListOfOffersIsReturned()
        {
            assertEquals(1, resultList.size());
        }

        private void whenICallGetOffer()
        {
            resultOffer = offerDataAccess.getOffer("1");
        }

        private void thenAnOfferIsReturned()
        {
            assertEquals("TestOffer", resultOffer.getName());
            assertEquals(2, resultOffer.getDuration());
        }

        private void whenICallGetOfferWithAnInvalidOffer()
        {
            resultOffer = offerDataAccess.getOffer("0");
        }

        private void thenNullIsReturned()
        {
            assertNull(resultOffer);
        }

        private void givenIHaveAnExpiredOffer()
        {
            givenIHaveAValidOffer();
            offer.setId("1");
            offer.setExpiryDate(LocalDate.now().minus(2, ChronoUnit.DAYS));
            when(offerRepository.findById(anyString())).thenReturn(Optional.of(offer));
        }

        private void whenIRequestTheExpiredOffer()
        {
            resultOffer = offerDataAccess.getOffer("1");
        }

        private void thenTheOfferIsUpdatedToBeExpired()
        {
            verify(offerRepository).save(any(Offer.class));
        }

        private void whenICancelTheOffer()
        {
            offerDataAccess.cancelOffer("1");
        }

        private void givenOfferIsReturnedFromDB()
        {
            when(offerRepository.findById("1")).thenReturn(Optional.of(offer));
        }

        private void thenTheOfferIsUpdated()
        {
            verify(offerRepository).findById(anyString());
            verify(offerRepository).save(any(Offer.class));
        }

        private void givenTheOfferIsReturned()
        {
            when(offerRepository.findById(anyString())).thenReturn(Optional.of(offer));
        }




    }
}
