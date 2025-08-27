package com.example.legacy.model;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;

public class InstantAdapter extends XmlAdapter<String, Instant> {

    @Override
    public Instant unmarshal(String value) throws Exception {
        return value != null ? Instant.parse(value) : null;
    }

    @Override
    public String marshal(Instant instant) throws Exception {
        return instant != null ? instant.toString() : null;
    }
}