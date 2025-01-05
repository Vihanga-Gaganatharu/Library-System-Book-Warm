package lk.ijse.bookworm.bo.custom;

import lk.ijse.bookworm.bo.SuperBO;
import lk.ijse.bookworm.dto.UserDto;

import java.util.List;

public interface UserBO extends SuperBO {
    List<UserDto> getAllUsers();
    boolean saveUser(UserDto dto);
    boolean updateUser(UserDto dto);
    boolean updateUserAndImg(UserDto dto);
    boolean deleteUser(String id);
    UserDto searchUser(String id);
    boolean isUserExist(UserDto userDto);
    List<UserDto> getUsersWithOverdueBooks();
}

