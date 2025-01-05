package lk.ijse.bookworm.bo.custom;

import lk.ijse.bookworm.bo.SuperBO;
import lk.ijse.bookworm.dto.BookDto;

import java.util.List;

public interface BookBO extends SuperBO {
    List<BookDto> getAllBooks();
    boolean saveBook(BookDto dto);
    boolean updateBook(BookDto dto);
    boolean deleteBook(String id);
    BookDto searchBook(String id);

}
