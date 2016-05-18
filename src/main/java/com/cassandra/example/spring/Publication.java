package com.cassandra.example.spring;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.net.URL;
import java.util.UUID;

@Table
public class Publication {

    @PrimaryKey
    private UUID accountId;
    @PrimaryKey
    private UUID ownerId;

    private UUID documentId;
    private UUID publicationId;
    private int ttl;
    private URL link;
    private UUID secret;
    private String authCallback;

    public Publication(UUID accountId, UUID ownerId, UUID documentId, UUID publicationId,
                       int ttl, URL link, UUID secret, String authCallback) {
        this.accountId = accountId;
        this.ownerId = ownerId;
        this.documentId = documentId;
        this.publicationId = publicationId;
        this.ttl = ttl;
        this.link = link;
        this.secret = secret;
        this.authCallback = authCallback;
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

    public UUID getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(UUID publicationId) {
        this.publicationId = publicationId;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
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
        return "Publication [accountId=" + accountId + ", ownerId=" + ownerId  + ", documentId=" + documentId
                + ", publicationId=" + publicationId + ", ttl=" + ttl  + ", link=" + link
                + ", secret=" + secret + ", authCallback=" + authCallback + "]";
    }

}