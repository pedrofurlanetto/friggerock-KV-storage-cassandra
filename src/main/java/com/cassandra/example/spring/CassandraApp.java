package com.cassandra.example.spring;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.util.List;

public class CassandraApp {
    private static final Logger LOG = LoggerFactory.getLogger(CassandraApp.class);

    private static Cluster cluster;
    private static Session session;

    public static void main(String[] args) {

        cluster = Cluster.builder().addContactPoints("127.0.0.1").build();

        session = cluster.connect("friggerock");

        CassandraOperations cassandraOps = new CassandraTemplate(session);

        cassandraOps.insert(new Publication("1234567890", "David", 40));
        cassandraOps.insert(new Publication("123", "David2", 50));
        cassandraOps.insert(new Publication("456", "David3", 60));
        cassandraOps.insert(new Publication("7890", "David4", 70));
        cassandraOps.insert(new Publication("123456", "David5", 80));

        Select s = QueryBuilder.select().all().from("person");
        //s.where(QueryBuilder.eq("id", "1234567890"));

        List<Publication> r = cassandraOps.select(s, Publication.class);
        for (Publication p : r) {
            System.out.println("Id = " + p.getId());
            System.out.println("Name = " + p.getName());
            System.out.println("Age = " + p.getAge());
            System.out.println("---------------------------------------");
        }

        cassandraOps.truncate("person");

        session.close();
        cluster.close();

    }
}