package org.example.repository

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object Database {

    val sessionFactory: SessionFactory = Configuration()
        .configure()
        .buildSessionFactory()

    fun <T> transaction(block: (org.hibernate.Session) -> T): T {
        val session = sessionFactory.openSession()

        return try {
            val transaction = session.beginTransaction()
            val result = block(session)
            transaction.commit()
            result
        } catch (e: Exception) {
            session.transaction.rollback()
            throw e
        } finally {
            session.close()
        }
    }
}