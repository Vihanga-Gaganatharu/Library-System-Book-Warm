package lk.ijse.bookworm.dao.custom;

import lk.ijse.bookworm.dao.CrudDAO;
import lk.ijse.bookworm.entity.Admin;

public interface AdminDAO extends CrudDAO<Admin> {

    int updateAdminUsername(String userName,String oldUsername);

}
