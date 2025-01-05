package lk.ijse.bookworm.bo.custom;

import lk.ijse.bookworm.bo.SuperBO;
import lk.ijse.bookworm.dto.AdminDto;
import lk.ijse.bookworm.dto.UserDto;

import java.util.List;

public interface AdminBO extends SuperBO {
    boolean saveAdmin(AdminDto dto);
    boolean updateAdmin(AdminDto dto);
    boolean deleteAdmin(String id);
    List<AdminDto> getAllAdmin();
    boolean isAdminExist(AdminDto dto);
}
