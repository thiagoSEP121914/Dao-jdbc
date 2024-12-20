package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJbdc;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao () {
        return new SellerDaoJDBC(DB.getConn());
    }
    public static DepartmentDao createDepartmentDao () {
        return new DepartmentDaoJbdc(DB.getConn());
    }

}
