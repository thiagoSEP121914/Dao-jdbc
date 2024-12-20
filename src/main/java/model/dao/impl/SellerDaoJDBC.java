package model.dao.impl;

import db.DB;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

private static final String MSG = "NAO FOI POSSIVEL CONECTAR COM O BANCO DE DADOS! ";

    private Connection conn;

    public SellerDaoJDBC (Connection connection  ) {
        this.conn = connection;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;
        String sql = "INSERT INTO seller "
                     + "(Name, Email, Birthdate, BaseSalary, DepartmentId) "
                     + "VALUES ( ?, ?, ?, ?, ? )";
        try {
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1,obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4,obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                System.out.println("DADOS CADASTRADOS COM SUCESSO!");
                DB.closeResultSet(rs);
            } else {
                throw new RuntimeException("ERRO INESPERADO ! NENHUMA LINHA AFETADA! ");
            }
        } catch (SQLException e) {
            throw new RuntimeException(MSG + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        String sql = "UPDATE seller "
                     +"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentID = ? "
                     +"WHERE Id = ?";
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5,obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(MSG + e.getMessage());
        } finally {
                DB.closeStatement(st);
        }
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
                Department department = instantiateDepartment(rs);
                return instatiateSeller(rs, department);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(MSG + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    private Department instantiateDepartment (ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("Name"));
        return department;
    }

    private Seller instatiateSeller (ResultSet rs, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    @Override
    public List<Seller> findAll() {

        if (conn == null) {
            throw new RuntimeException(MSG);
        }

        PreparedStatement pstu = null;
        ResultSet rs = null;
        String sql = "SELECT seller.*, department.Name AS Depname "
                    + "FROM seller "
                    + "INNER JOIN department ON seller.DepartmentId = department.Id "
                    + "ORDER BY department.Name";

        try {
            pstu = conn.prepareStatement(sql);
            rs = pstu.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller obj = instatiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(MSG + e.getMessage());
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        if (conn == null) {
            throw new RuntimeException(MSG);
        }

        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql =
                "SELECT seller.*, department.Name AS Depname "
                +"FROM seller "
                +"INNER JOIN department ON seller.DepartmentId = department.Id "
                +"WHERE seller.DepartmentId = ? "
                +"ORDER BY department.Name";

        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1,department.getId());
            rs = pst.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department  dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller obj = instatiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(MSG + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(pst);
        }
    }
}
