package lk.ijse.bookworm.bo.custom.impl;

import lk.ijse.bookworm.bo.custom.BookBO;
import lk.ijse.bookworm.dao.DAOFactory;
import lk.ijse.bookworm.dao.custom.BookDAO;
import lk.ijse.bookworm.dao.custom.BranchDAO;
import lk.ijse.bookworm.dto.BookDto;
import lk.ijse.bookworm.entity.Book;
import lk.ijse.bookworm.entity.Branch;
import lk.ijse.bookworm.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class BookBOImpl implements BookBO {

    BranchDAO branchDAO = (BranchDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.BRANCH);
    BookDAO bookDAO = (BookDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.BOOK);


    private Session session;
    @Override
    public List<BookDto> getAllBooks() {
        session= SessionFactoryConfig.getInstance().getSession();
        bookDAO.setSession(session);
        List<BookDto> bookDtoList = new ArrayList<>();
        try {
            for (Book book : bookDAO.getAll()) {
                bookDtoList.add(new BookDto(
                        book.getBookID(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre(),
                        book.isAvailable(),
                        book.getBranch().getBranchID()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return bookDtoList;
    }

    @Override
    public boolean saveBook(BookDto dto) {
        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            bookDAO.setSession(session);
            branchDAO.setSession(session);
            Branch branch = branchDAO.search(dto.getBranchID());
            bookDAO.save(new Book(dto.getBookId(), dto.getTitle(), dto.getAuthor(), dto.getGenre(), dto.isAvailability(), branch));
            session.getTransaction().commit();
            return true;
        }catch (Exception e) {
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean updateBook(BookDto dto) {
        session = SessionFactoryConfig.getInstance().getSession();
        branchDAO.setSession(session);
        Branch branch = branchDAO.search(dto.getBranchID());
        session.clear(); // Clear the session after fetching the Branch entity

        Transaction transaction = session.beginTransaction();
        bookDAO.setSession(session);
        try {
            bookDAO.update(new Book(dto.getBookId(), dto.getTitle(), dto.getAuthor(), dto.getGenre(), dto.isAvailability(), branch));
            transaction.commit();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean deleteBook(String id) {
        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            bookDAO.setSession(session);
            Book book = bookDAO.search(id);
            bookDAO.delete(book);
            transaction.commit();
            return true;
        }catch (Exception e) {
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public BookDto searchBook(String id) {
        session = SessionFactoryConfig.getInstance().getSession();
        try{
            bookDAO.setSession(session);
            Book book = bookDAO.search(id);
            if (book != null){
                return new BookDto(book.getBookID(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre(),
                        book.isAvailable(),
                        book.getBranch().getBranchID()
                );
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }
}

