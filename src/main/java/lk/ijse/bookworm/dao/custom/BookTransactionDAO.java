package lk.ijse.bookworm.dao.custom;

import lk.ijse.bookworm.dao.CrudDAO;
import lk.ijse.bookworm.entity.BookTransactions;

import java.util.List;

public interface BookTransactionDAO extends CrudDAO<BookTransactions> {

    List<BookTransactions> getAllLateBookDetails();

}
