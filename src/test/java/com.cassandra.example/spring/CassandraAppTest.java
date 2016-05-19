/*
 * (C) Copyright 2016 HP Development Company, L.P.
 */

package com.cassandra.example.spring;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static java.lang.Thread.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.datastax.driver.core.querybuilder.Select;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class CassandraAppTest {

    private static final String keyspace = "friggerock";

    private static final String node = "127.0.0.1";

    private static final String table = "publication";

    private static final UUID accountId_1 = UUID.fromString("261fa1c7-b2be-4ebe-b50c-7beec9a131a1");
    private static final UUID accountId_2 = UUID.fromString("9a8274f8-e242-4b72-9d93-9e4fd22f725e");

    private static final UUID ownerId_1 = UUID.fromString("48b74702-8fb5-4edf-b76d-bb72edfe5f06");
    private static final UUID ownerId_2 = UUID.fromString("b25eecc9-e314-4418-9b34-67d1f7fd99d4");

    private static final UUID documentId_1 = UUID.fromString("5c5a6ce5-4ec2-4414-a2c9-afa89af01947");
    private static final UUID documentId_2 = UUID.fromString("d45a6ce5-4ec2-4414-a2c9-afa89af01947");

    private static final UUID publicationId_1 = UUID.fromString("a411c24e-24b3-45fe-a016-5bcf12c1c6ba");
    private static final UUID publicationId_2 = UUID.fromString("b211c24e-25e3-45fe-a016-5bcf12c1c6ba");

    private static final int ttl_1 = 2;
    private static final int ttl_2 = 4;

    private static final String link_1 = "link_1";

    private static final UUID secret_1 = UUID.fromString("401df481-e4b8-4a9a-80b9-fc883a085cdc");

    private static final String authCallback_1 = "authCallback_1";

    private static CassandraApp cassandraApp = new CassandraApp();



    @BeforeClass
    public static void setup() {
        cassandraApp.connect(node, keyspace);
        cassandraApp.createSchema(table);
    }

    @AfterClass
    public static void teadown() {
        cassandraApp.truncate(table);
        cassandraApp.dropSchema(table);
        cassandraApp.dropKeyspace(keyspace);
        cassandraApp.close();
    }

    @Test
    public void createPublicationWithTTL_shouldCreatePublicationWithTTLandRemoveWhenExpired() throws InterruptedException {
        cassandraApp.createPublicationWithTTL(accountId_1, ownerId_1, documentId_1,
                publicationId_1, ttl_1, link_1, secret_1, authCallback_1);

        Select select = select().all().from(table);
        select.where(eq("accountId", accountId_1))
                .and(eq("ownerId", ownerId_1))
                .and(eq("documentId", documentId_1))
                .and(eq("publicationId", publicationId_1));

        Publication publication = cassandraApp.cassandraOps.selectOne(select, Publication.class);
        assertNotNull(publication);
        assertEquals(publication.getTtl(), ttl_1);

        sleep(3000);

        Publication publicationAfterExpired = cassandraApp.cassandraOps.selectOne(select, Publication.class);
        assertNull(publicationAfterExpired);
    }

    @Test
    public void createPublicationWithoutTTL_shouldCreatePublicationWithTTLEqualToMinusOne() throws InterruptedException {
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_1, documentId_1,
                publicationId_2, link_1, secret_1, authCallback_1);

        Select select = select().all().from(table);
        select.where(eq("accountId", accountId_1))
                .and(eq("ownerId", ownerId_1))
                .and(eq("documentId", documentId_1))
                .and(eq("publicationId", publicationId_2));

        Publication publication = cassandraApp.cassandraOps.selectOne(select, Publication.class);

        assertNotNull(publication);
        assertEquals(publication.getTtl(), -1);
    }

    @Test
    public void revokePublication_shouldDeleteTheEntry() {
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_1, documentId_1,
                publicationId_1, link_1, secret_1, authCallback_1);

        Select select = select().all().from(table);
        select.where(eq("accountId", accountId_1))
                .and(eq("ownerId", ownerId_1))
                .and(eq("documentId", documentId_1))
                .and(eq("publicationId", publicationId_1));

        Publication publication = cassandraApp.cassandraOps.selectOne(select, Publication.class);

        assertNotNull(publication);

        cassandraApp.revokePublication(accountId_1, ownerId_1, documentId_1, publicationId_1);

        Publication publicationAfterRevoke = cassandraApp.cassandraOps.selectOne(select, Publication.class);

        assertNull(publicationAfterRevoke);
    }

    @Test
    public void getPublicationsFromUser_shouldRetrieveAllPublicationsForGivenUser() {
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_1, documentId_1,
                publicationId_1, link_1, secret_1, authCallback_1);
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_1, documentId_1,
                publicationId_2, link_1, secret_1, authCallback_1);
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_1, documentId_2,
                publicationId_1, link_1, secret_1, authCallback_1);
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_2, documentId_1,
                publicationId_1, link_1, secret_1, authCallback_1);
        cassandraApp.createPublicationWithoutTTL(accountId_2, ownerId_1, documentId_1,
                publicationId_1, link_1, secret_1, authCallback_1);

        List<Publication> publicationList = cassandraApp.getPublicationsFromUser(accountId_1, ownerId_1);
        assertNotNull(publicationList);

        cassandraApp.print(publicationList);
        System.out.println();
        System.out.println("-----------------------------------------------------");
        System.out.println("Size: " + publicationList.size());
        assertEquals(publicationList.size(), 3);
    }

    @Test
    public void getPublicationsFromAdmin_shouldRetrieveAllPublicationsForGivenUser() {
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_1, documentId_1,
                publicationId_1, link_1, secret_1, authCallback_1);
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_1, documentId_1,
                publicationId_2, link_1, secret_1, authCallback_1);
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_1, documentId_2,
                publicationId_1, link_1, secret_1, authCallback_1);
        cassandraApp.createPublicationWithoutTTL(accountId_1, ownerId_2, documentId_1,
                publicationId_1, link_1, secret_1, authCallback_1);
        cassandraApp.createPublicationWithoutTTL(accountId_2, ownerId_1, documentId_1,
                publicationId_1, link_1, secret_1, authCallback_1);

        List<Publication> publicationList = cassandraApp.getPublicationsFromAdmin(accountId_1);
        assertNotNull(publicationList);

        cassandraApp.print(publicationList);
        System.out.println();
        System.out.println("-----------------------------------------------------");
        System.out.println("Size: " + publicationList.size());
        assertEquals(publicationList.size(), 4);
    }

}
