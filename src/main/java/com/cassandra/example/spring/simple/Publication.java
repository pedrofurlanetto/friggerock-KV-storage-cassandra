/*
 * (C) Copyright 2016 HP Development Company, L.P.
 */

package com.cassandra.example.spring.simple;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Indexed;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

@Table
public class Publication {
    @PrimaryKey
    private UUID publicationId;

    @Column
    @Indexed
    private UUID accountId;
    @Column
    @Indexed
    private UUID ownerId;
    @Column
    private UUID documentId;
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

    public Publication(UUID publicationId) {
        this.publicationId = publicationId;
    }

    public Publication(UUID publicationId, UUID accountId, UUID ownerId, UUID documentId, int ttl, String link, UUID secret, String authCallback) {
        this.publicationId = publicationId;
        this.accountId = accountId;
        this.ownerId = ownerId;
        this.documentId = documentId;
        this.ttl = ttl;
        this.link = link;
        this.secret = secret;
        this.authCallback = authCallback;
    }

    public UUID getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(UUID publicationId) {
        this.publicationId = publicationId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
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
        return "PublicationKey [publication=" + publicationId + ", account=" + accountId
                + ", owner=" + ownerId + ", document=" + documentId + ", ttl=" + ttl
                + ", link=" + link + ", secret=" + secret + ", authCallback=" + authCallback + "]";
    }
}
