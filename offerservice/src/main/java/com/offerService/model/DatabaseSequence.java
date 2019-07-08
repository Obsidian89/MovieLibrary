package com.offerService.model;

import org.springframework.data.annotation.Id;

public class DatabaseSequence
{
    @Id
    private String id;

    private String seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}
