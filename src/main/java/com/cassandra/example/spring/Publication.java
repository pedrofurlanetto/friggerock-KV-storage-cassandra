/*
 * (C) Copyright 2016 HP Development Company, L.P.
 */

package com.cassandra.example.spring;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

@Table
public class Publication {
    @PrimaryKey
    private PublicationKey publicationKey;

    @Column
    private int ttl;
    @Column
    private String link;
    @Column
    private UUID secret;
    @Column
    private String authCallback;

    public Publication() {
    }

    public Publication(PublicationKey publicationKey) {
        this.publicationKey = publicationKey;
    }

    public Publication(PublicationKey publicationKey, int ttl, String link, UUID secret, String authCallback) {
        this.publicationKey = publicationKey;
        this.ttl = ttl;
        this.link = link;
        this.secret = secret;
        this.authCallback = authCallback;
    }

    public PublicationKey getPublicationKey() {
        return publicationKey;
    }

    public void setPublicationKey(PublicationKey publicationKey) {
        this.publicationKey = publicationKey;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public UUID getSecret() {
        return secret;
    }

    public void setSecret(UUID secret) {
        this.secret = secret;
    }

    public String getAuthCallback() {
        return authCallback;
    }

    public void setAuthCallback(String authCallback) {
        this.authCallback = authCallback;
    }

    @Override
    public String toString() {
        return "PublicationKey [ttl=" + ttl + ", link=" + link
                + ", secret=" + secret + ", authCallback=" + authCallback + "]";
    }
}
