package lk.ijse.bookworm.bo.custom;

import lk.ijse.bookworm.bo.SuperBO;
import lk.ijse.bookworm.dto.BorrowBookDto;

import java.util.List;

public interface BookTransactionBO extends SuperBO {
    List<BorrowBookDto> getAllBorrowedBooks();
    boolean saveBorrowedBook(BorrowBookDto dto);
    boolean updateBorrowedBook(String id);
    boolean deleteBorrowedBook(String id);
    BorrowBookDto searchBorrowedBook(String id);
    List<BorrowBookDto> getAllLateBookDetails();
}
