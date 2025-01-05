package lk.ijse.bookworm.bo.custom.impl;

import lk.ijse.bookworm.bo.custom.BookTransactionBO;
import lk.ijse.bookworm.dao.DAOFactory;
import lk.ijse.bookworm.dao.custom.BookDAO;
import lk.ijse.bookworm.dao.custom.BookTransactionDAO;
import lk.ijse.bookworm.dao.custom.UserDAO;
import lk.ijse.bookworm.dto.BorrowBookDto;
import lk.ijse.bookworm.entity.Book;
import lk.ijse.bookworm.entity.BookTransactions;
import lk.ijse.bookworm.entity.User;
import lk.ijse.bookworm.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookTransactionBOImpl implements BookTransactionBO {

    private final BookTransactionDAO bookTransactionDAO = (BookTransactionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.BOOK_TRANSACTION);
    private final BookDAO bookDAO = (BookDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.BOOK);
    private final UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.USER);
    private Session session;

    @Override
    public List<BorrowBookDto> getAllBorrowedBooks() {
        session = SessionFactoryConfig.getInstance().getSession();
        bookTransactionDAO.setSession(session);
        List<BorrowBookDto> borrowBookDtoList = new ArrayList<>();
        try {
            for (BookTransactions bookTransactions : bookTransactionDAO.getAll()) {
                borrowBookDtoList.add(new BorrowBookDto(
                        String.valueOf(bookTransactions.getTransactionId()),
                        bookTransactions.getUser().getEmail(),
                        bookTransactions.getBook().getBookID(),
                        String.valueOf(bookTransactions.getBorrowDate()),
                        String.valueOf(bookTransactions.getReturnDate()),
                        bookTransactions.isReturned()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return borrowBookDtoList;
    }

    @Override
    public boolean saveBorrowedBook(BorrowBookDto dto) {
        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            bookTransactionDAO.setSession(session);
            bookDAO.setSession(session);
            userDAO.setSession(session);

            // Search Book and User
            User user = userDAO.search(dto.getUserId());
            Book book = bookDAO.search(dto.getBookId());
            book.setAvailable(false);

            bookTransactionDAO.save(new BookTransactions(book, user, LocalDate.parse(dto.getReturnDate())));
            bookDAO.update(book);

            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updateBorrowedBook(String id) {
        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            bookTransactionDAO.setSession(session);
            bookDAO.setSession(session);

            BookTransactions bookTransaction = bookTransactionDAO.search(id);

            // Get Book and set available
            Book book = bookTransaction.getBook();
            book.setAvailable(!book.isAvailable());

            // Set Book Entity and update the book transaction record
            bookTransaction.setBook(book);
            bookTransaction.setReturned(!bookTransaction.isReturned());

            bookTransactionDAO.update(bookTransaction);
            bookDAO.update(book);

            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteBorrowedBook(String id) {
        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            bookTransactionDAO.setSession(session);
            bookDAO.setSession(session);

            // Get the transaction record
            BookTransactions bookTransaction = bookTransactionDAO.search(id);

            // Get Book and set available
            Book book = bookTransaction.getBook();
            book.setAvailable(true);

            bookDAO.update(book);
            bookTransactionDAO.delete(bookTransaction);

            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public BorrowBookDto searchBorrowedBook(String id) {
        session = SessionFactoryConfig.getInstance().getSession();
        bookTransactionDAO.setSession(session);
        try {
            BookTransactions transactions = bookTransactionDAO.search(id);
            if (transactions != null) {
                return new BorrowBookDto(
                        String.valueOf(transactions.getTransactionId()),
                        transactions.getUser().getEmail(),
                        transactions.getBook().getBookID(),
                        String.valueOf(transactions.getBorrowDate()),
                        String.valueOf(transactions.getReturnDate()),
                        transactions.isReturned()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<BorrowBookDto> getAllLateBookDetails() {
        session = SessionFactoryConfig.getInstance().getSession();
        bookTransactionDAO.setSession(session);
        List<BorrowBookDto> borrowBookDtoList = new ArrayList<>();
        try {
            for (BookTransactions bookTransactions : bookTransactionDAO.getAllLateBookDetails()) {
                borrowBookDtoList.add(new BorrowBookDto(
                        String.valueOf(bookTransactions.getTransactionId()),
                        bookTransactions.getUser().getEmail(),
                        bookTransactions.getBook().getBookID(),
                        String.valueOf(bookTransactions.getBorrowDate()),
                        String.valueOf(bookTransactions.getReturnDate()),
                        bookTransactions.isReturned()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return borrowBookDtoList;
    }
}
