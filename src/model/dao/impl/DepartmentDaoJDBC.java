package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.MyDbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC() {
		conn = DB.getConnection();
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.executeUpdate();

			ResultSet rs = st.getGeneratedKeys();
			if (rs.next())
				obj.setId(rs.getInt(1));
			rs.close();

		} catch (SQLException e) {
			throw new MyDbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {

			st = conn.prepareStatement("UPDATE department SET name = ? WHERE id = ?");
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new MyDbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("DELETE FROM department WHERE id = ?;");
			st.setInt(1, id);
			st.execute();

		} catch (SQLException e) {
			throw new MyDbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement("SELECT * FROM department WHERE id = ?;");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next())
				return new Department(rs.getInt("id"), rs.getString("name"));

		} catch (SQLException e) {
			throw new MyDbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	@Override
	public List<Department> findAll() {
		Statement st = null;
		ResultSet rs = null;
		try {

			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM department");

			List<Department> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Department(rs.getInt("id"), rs.getString("name")));
			}
			return list;

		} catch (SQLException e) {
			throw new MyDbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
