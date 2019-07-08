package com.offerService.utils;

import com.mongodb.MongoClient;
import com.offerService.model.DatabaseSequence;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class DatabaseUtils {
    private MongoOperations mongo = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient("localhost", 27017), "test"));

    public String getNextSequence(String seqName) {
        DatabaseSequence sequence = mongo.findAndModify(
                query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return sequence.getSeq();
    }
}
