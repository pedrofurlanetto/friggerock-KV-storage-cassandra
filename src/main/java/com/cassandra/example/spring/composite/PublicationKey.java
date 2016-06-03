/*
 * (C) Copyright 2016 HP Development Company, L.P.
 */

package com.cassandra.example.spring.composite;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;

@PrimaryKeyClass
public class PublicationKey implements Serializable {

    @PrimaryKeyColumn(name = "accountId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID accountId;
    @PrimaryKeyColumn(name = "ownerId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID ownerId;
    @PrimaryKeyColumn(name = "documentId", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private UUID documentId;
    @PrimaryKeyColumn(name = "publicationId", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
    private UUID publicationId;

    public PublicationKey(UUID accountId, UUID ownerId, UUID documentId, UUID publicationId) {
        this.accountId = accountId;
        this.ownerId = ownerId;
        this.documentId = documentId;
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

    public UUID getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(UUID publicationId) {
        this.publicationId = publicationId;
    }

    @Override
    public String toString() {
        return "PublicationKey [accountId=" + accountId + ", ownerId=" + ownerId
                + ", documentId=" + ownerId + ", publicationId=" + ownerId + "]";
    }

}