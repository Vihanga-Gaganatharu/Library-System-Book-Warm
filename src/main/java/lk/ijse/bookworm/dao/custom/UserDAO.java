package lk.ijse.bookworm.dao.custom;

import lk.ijse.bookworm.dao.CrudDAO;
import lk.ijse.bookworm.entity.User;

import java.util.List;

public interface UserDAO extends CrudDAO<User> {
    List<User> getUsersWithOverdueBooks();
    int updateUserEmail(String email,String oldEmail);
}
