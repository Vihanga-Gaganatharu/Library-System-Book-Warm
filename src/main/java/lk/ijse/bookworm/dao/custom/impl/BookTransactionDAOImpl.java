package lk.ijse.bookworm.dao.custom.impl;

import lk.ijse.bookworm.dao.custom.BookTransactionDAO;
import lk.ijse.bookworm.entity.Book;
import lk.ijse.bookworm.entity.BookTransactions;
import lk.ijse.bookworm.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class BookTransactionDAOImpl implements BookTransactionDAO {


    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public List<BookTransactions> getAll() {
        String hql = "FROM BookTransactions ";
        Query query = session.createQuery(hql);
        List<BookTransactions> bookTransactionsList = query.list();
        session.close();
        return bookTransactionsList;
    }

    @Override
    public void save(BookTransactions entity) {
        session.save(entity);
    }

    @Override
    public void update(BookTransactions entity) {
     session.update(entity);
    }

    @Override
    public void delete(BookTransactions entity) {
        session.delete(entity);
    }

    @Override
    public BookTransactions search(String id) {
       return session.get(BookTransactions.class, Integer.parseInt(id));
    }

    @Override
    public List<BookTransactions> getAllLateBookDetails() {
        LocalDate currentDate = LocalDate.now();
        Session session = SessionFactoryConfig.getInstance().getSession();
        String hql = "FROM BookTransactions bt WHERE bt.isReturned = false AND bt.returnDate < :currentDate";
        Query query = session.createQuery(hql);
        query.setParameter("currentDate", currentDate);
        List<BookTransactions> bookTransactionsList = query.list();
        session.close();
        return bookTransactionsList;
    }
}

