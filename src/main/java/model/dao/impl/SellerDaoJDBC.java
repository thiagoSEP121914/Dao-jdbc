package model.dao.impl;

import db.DB;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.sql.*;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

private static final String MSG = "NAO FOI POSSIVEL CONECTAR COM O BANCO DE DADOS! ";

    private Connection conn;

    public SellerDaoJDBC () {
    }

    public SellerDaoJDBC (Connection connection  ) {
        this.conn = connection;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {

        if (conn == null) {
            throw new RuntimeException(MSG);
        }

        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT seller.*, department.Name as DepName "
                     + "FROM seller INNER JOIN department "
                     + "ON seller.DepartmentId = department.Id "
                     + "WHERE seller.Id = ?";
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Department department = new Department();
                department.setId(rs.getInt("DepartmentId"));
                department.setName(rs.getString("DepName"));

                Seller obj = new Seller();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                obj.setEmail(rs.getString("Email"));
                obj.setBaseSalary(rs.getDouble("BaseSalary"));
                obj.setBirthDate(rs.getDate("BirthDate"));
                obj.setDepartment(department);
                return obj;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(MSG + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
