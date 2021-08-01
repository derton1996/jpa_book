package org.hello.world;

import org.hello.world.data.Message;
import org.hello.world.transaction.manager.DatabaseProduct;
import org.hello.world.transaction.manager.TransactionManagerSetup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.resource.transaction.backend.jta.internal.JtaTransactionCoordinatorBuilderImpl;
import org.hibernate.service.ServiceRegistry;

import javax.transaction.UserTransaction;
import java.util.List;

public class HelloWorldHibernate {

    static public TransactionManagerSetup TM;

    public static void main(String[] args) throws Exception {
        TM = new TransactionManagerSetup(DatabaseProduct.MYSQL, "jdbc:mysql://localhost:3306/hello_world_jpa");
        SessionFactory sessionFactory = createSessionFactory();
        UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        Session session = sessionFactory.getCurrentSession();

        Message message = new Message();
        message.setText("Hello World! Hibernate");

        session.persist(message);
        tx.commit();

        tx.begin();
        List<Message> messages = sessionFactory.getCurrentSession().createCriteria(Message.class).list();
        System.out.println(messages);
        tx.commit();
    }

    private static SessionFactory createSessionFactory() {
        StandardServiceRegistryBuilder serviceRegistryBuilder =
                new StandardServiceRegistryBuilder();

        serviceRegistryBuilder
                .applySetting("hibernate.connection.datasource", "myDS")
                .applySetting("hibernate.format_sql", "true")
                .applySetting("hibernate.use_sql_comments", "true")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop");

        serviceRegistryBuilder.applySetting(
                Environment.TRANSACTION_COORDINATOR_STRATEGY,
                JtaTransactionCoordinatorBuilderImpl.class
        );
        ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(Message.class);
        MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();

        Metadata metadata = metadataBuilder.build();
        SessionFactory sessionFactory = metadata.buildSessionFactory();

        return sessionFactory;
    }
}
