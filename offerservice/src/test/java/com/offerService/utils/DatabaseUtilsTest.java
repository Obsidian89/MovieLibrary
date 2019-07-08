package com.offerService.utils;

import com.offerService.model.DatabaseSequence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DatabaseUtilsTest {
    Fixture fixture;

    @Before
    public void setUp() {
        fixture = new Fixture();
    }

    @Test
    public void getNextSequenceTest() {
        fixture.givenIHaveAValueDatabaseSequenceNumber();
        fixture.whenICallGetNextSequence();
        fixture.thenTheNextSequenceNumberIsReturned();
    }

    private class Fixture {
        @Mock
        private MongoOperations mongoOperations;

        @InjectMocks
        private DatabaseUtils databaseUtils = new DatabaseUtils();

        String result;

        private Fixture() {
            MockitoAnnotations.initMocks(this);
        }

        private void givenIHaveAValueDatabaseSequenceNumber() {
            DatabaseSequence databaseSequence = new DatabaseSequence();
            databaseSequence.setSeq("1");
            when(mongoOperations.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), any(Class.class))).thenReturn(databaseSequence);
        }

        private void whenICallGetNextSequence() {
            result = databaseUtils.getNextSequence("Offers");
        }

        private void thenTheNextSequenceNumberIsReturned() {
            assertEquals("1", result);
        }
    }
}
