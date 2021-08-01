package org.hello.world;

import org.hello.world.transaction.manager.DatabaseProduct;
import org.hello.world.transaction.manager.TransactionManagerSetup;
import org.hello.world.data.Message;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

public class SaveEntityJPA {

    static public TransactionManagerSetup TM;

    public static void main(String[] args) throws Exception {
        TM = new TransactionManagerSetup(DatabaseProduct.MYSQL, "jdbc:mysql://localhost:3306/hello_world_jpa");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HelloWorldPU");
        UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        EntityManager em = emf.createEntityManager();
        Message message = new Message();
        message.setText("Hello World!");

        em.persist(message);
        tx.commit();
        em.close();
    }
}
