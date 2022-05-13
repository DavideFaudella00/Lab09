package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Country(rs.getString("StateAbb"), rs.getInt("ccode"), rs.getString("StateNme")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno) {
		String sql = "select state1no, state2no from contiguity where contiguity.conttype=1 and contiguity.year <= ?";

		List<Border> result = new LinkedList<Border>();
		Map<Integer, Country> allCountries = new HashMap<Integer, Country>();

		for (Country c : loadAllCountries()) {
			allCountries.put(c.getcCode(), c);
		}

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				int c1Code = rs.getInt("state1no");
				int c2Code = rs.getInt("state2no");

				Country c1 = allCountries.get(c1Code);
				Country c2 = allCountries.get(c2Code);

				if (c1 != null && c2 != null) {
					Border b = new Border(c1, c2);
					result.add(b);
				}
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Country> getCountryByYears(int anno) {
		String sql = "SELECT DISTINCT state1no " + "FROM contiguity " + "WHERE YEAR <= ?";
		List<Country> result = new ArrayList<Country>();
		Map<Integer, Country> allCountries = new HashMap<Integer, Country>();
		for (Country c : loadAllCountries()) {
			allCountries.put(c.getcCode(), c);
		}

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c = allCountries.get(rs.getInt("state1no"));
				result.add(c);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
