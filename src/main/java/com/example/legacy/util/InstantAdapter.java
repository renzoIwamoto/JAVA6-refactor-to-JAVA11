package com.example.legacy.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;

public class InstantAdapter extends XmlAdapter<String, Instant> {

    @Override
    public Instant unmarshal(String xmlDateString) throws Exception {
        return xmlDateString != null ? Instant.parse(xmlDateString) : null;
    }

    @Override
    public String marshal(Instant instantToSerialize) throws Exception {
        return instantToSerialize != null ? instantToSerialize.toString() : null;
    }
}