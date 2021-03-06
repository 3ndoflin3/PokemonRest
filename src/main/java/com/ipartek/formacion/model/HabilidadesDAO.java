package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Habilidad;

public class HabilidadesDAO {

	
	private static HabilidadesDAO INSTANCE;
	private ArrayList<Habilidad> habilidades;
	//INSERTAR HABILIDAD EN TABLA INTERMEDIA
	String SQL_GET_ALL = "SELECT id, nombre FROM habilidad";
	private final static Logger LOG = Logger.getLogger(HabilidadesDAO.class);

	
	private HabilidadesDAO() {
		super();
	}
	
	public static synchronized HabilidadesDAO getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new HabilidadesDAO();
		}
		return INSTANCE;
	}
	
	
	
	public List<Habilidad> getHabilidades() {
		
		habilidades = new ArrayList<>();
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery()){
		
			while(rs.next()) {
				habilidades.add(mapper(rs));
			}			
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return habilidades;
	}
	
	public Habilidad mapper(ResultSet rs) {
		Habilidad h = null;
		try {
			
				
				h = new Habilidad();
				h.setId(rs.getInt("id"));
				h.setHabilidad(rs.getString("nombre"));
				
			
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		
		return h;
	}
}
