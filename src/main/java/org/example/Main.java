package org.example;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

import java.sql.Connection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Connection conn = DB.getConn();

        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(3);

        System.out.println(seller);

    }
}