/*
 * (C) Copyright 2016 HP Development Company, L.P.
 */

package com.cassandra.example.spring.simple;

import static com.datastax.driver.core.Cluster.builder;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import com.cassandra.example.spring.composite.PublicationKey;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.util.List;
import java.util.UUID;

public class CassandraApp {
    private static final Logger LOG = LoggerFactory.getLogger(CassandraApp.class);

    private static Cluster cluster = null;
    private static Session session = null;
    public CassandraOperations cassandraOps = null;
    private static String keyspace = "Friggerock";
    private static String table = "publication";

    public static Cluster getCluster() {
        return cluster;
    }

    public static void setCluster(Cluster cluster) {
        CassandraApp.cluster = cluster;
    }

    public static Session getSession() {
        return session;
    }

    public static void setSession(Session session) {
        CassandraApp.session = session;
    }

    public void createKeyspace(String keyspaceName) {
        String query = "CREATE KEYSPACE IF NOT EXISTS " + keyspaceName
                + " WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};";
        session = cluster.connect();
        System.out.printf("Creating keyspace: %s\n", keyspaceName);
        session.execute(query);
    }

    public void dropKeyspace(String keyspaceName) {
        String query = "DROP KEYSPACE IF EXISTS " + keyspaceName;
        System.out.printf("Dropping keyspace: %s\n", keyspaceName);
        session.execute(query);
    }

    public void connect(String node, String keyspaceName) {
        cluster = builder().addContactPoints(node).build();

        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for ( Host host : metadata.getAllHosts() ) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }

        this.createKeyspace(keyspaceName);

        System.out.printf("Using keyspace: %s\n", keyspaceName);
        session = cluster.connect(keyspaceName);

        this.cassandraOps = new CassandraTemplate(session);
    }

    public void createSchema(String tableName) {
        String schema = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + "publicationId uuid,"
                + "accountId uuid,"
                + "ownerId uuid,"
                + "documentId uuid,"
                + "ttl int,"
                + "link text,"
                + "secret uuid,"
                + "authCallback text,"
                + "PRIMARY KEY (publicationId)"
                + ");";
        System.out.printf("Creating table: %s\n", tableName);
        session.execute(schema);

        this.createIndex("account_idx", "owner_idx", tableName);
    }

    public void createIndex(String indexName1, String indexName2, String tableName) {
        String index1 = "CREATE INDEX " + indexName1 + " ON " +  tableName + " (\"accountId\");";
        System.out.printf("Creating index: %s\n", indexName1);
        session.execute(index1);

        String index2 = "CREATE INDEX " + indexName2 + " ON " +  tableName + " (\"ownerId\");";
        System.out.printf("Creating index: %s\n", indexName2);
        session.execute(index2);
    }

    public void dropSchema(String tableName) {
        String schema = "DROP TABLE IF EXISTS " + tableName;
        System.out.printf("Dropping table: %s\n", tableName);
        session.execute(schema);
    }

    public void createPublicationWithTtl(UUID publicationId, UUID accountId, UUID ownerId, UUID documentId,
                                         int ttl, String link, UUID secret, String authCallback) {
        WriteOptions options = new WriteOptions();
        options.setTtl(ttl);
        cassandraOps.insert(new Publication(publicationId, accountId, ownerId, documentId,
                            ttl, link, secret, authCallback), options);
    }

    public void createPublicationWithoutTtl(UUID publicationId, UUID accountId, UUID ownerId, UUID documentId,
                                            String link, UUID secret, String authCallback) {
        cassandraOps.insert(new Publication(publicationId, accountId, ownerId, documentId,
                            -1, link, secret, authCallback));
    }

    public void revokePublication(UUID publicationId) {
        cassandraOps.delete(new Publication(publicationId));
    }

    public List<Publication> getPublicationsFromUser(UUID accountId, UUID ownerId) {
        Select select = select().all().from(table);
        select.where(eq("publicationId", "?"))
                .and(eq("accountId", accountId))
                .and(eq("ownerId", ownerId));

        return cassandraOps.select(select, Publication.class);
    }

    public List<Publication> getPublicationsFromAdmin(UUID accountId) {
        Select select = select().all().from(table);
        select.where(eq("accountId", accountId));

        return cassandraOps.select(select, Publication.class);

    }

    public void print(List<Publication> publicationList) {
        for (Publication eachPublication : publicationList) {
            System.out.println("PublicationId = " + eachPublication.getPublicationId());
            System.out.println("AccountId = " + eachPublication.getAccountId());
            System.out.println("OwnerId = " + eachPublication.getOwnerId());
            System.out.println("DocumentId = " + eachPublication.getDocumentId());
            System.out.println("TTL = " + eachPublication.getTtl());
            System.out.println("Link = " + eachPublication.getLink());
            System.out.println("Secret = " + eachPublication.getSecret());
            System.out.println("AuthCallback = " + eachPublication.getAuthCallback());
            System.out.println("-----------------------------------------------------------------------");
        }
    }

    public void truncate(String tableName) {
        cassandraOps.truncate(tableName);
    }

    public void close() {
        session.close();
        cluster.close();
    }
}