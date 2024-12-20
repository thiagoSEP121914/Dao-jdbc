package org.example;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();
        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);
        System.out.println();

        System.out.println("=== TEST 2: seller findById ===");
        List<Seller> list = sellerDao.findByDepartment(new Department(2, null));
        list.forEach(System.out::println);

        System.out.println("=== TEST 3: seller findById ===");
        list = sellerDao.findAll();
        for (Seller obj : list) {
            System.out.println(obj);
        }

        System.out.println("=== TEST 4: seller INSERT ===");
        Seller newSeller = new Seller(null, "Greg", "gregMaluico@Gmail.com", new Date(), 4000.0, new Department(2, null));
        sellerDao.insert(newSeller);
        System.out.println("INSERTED! New id = " + newSeller.getId());

        System.out.println("=== TEST 5: seller UPDATE ===");
         seller = sellerDao.findById(1);
         seller.setName("Martha Brown");
         sellerDao.update(seller);
         System.out.println("UPDATE Completed!");
    }
}