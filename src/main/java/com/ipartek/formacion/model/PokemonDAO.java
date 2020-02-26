package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Habilidad;
import com.ipartek.formacion.model.pojo.Pokemon;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class PokemonDAO implements IDAO <Pokemon>{

	private static PokemonDAO INSTANCE;
	//private ArrayList<Pokemon> registros;
	private HashMap <Integer, Pokemon> hm;
	//INSERTAR HABILIDAD EN TABLA INTERMEDIA
	private static final String SQL_INSERT_POKEMON_HAS_HABILIDADES = "INSERT INTO `pokedex`.`pokemon_has_habilidades` (`id_pokemon`, `id_habilidad`) VALUES (?, ?);";
	private static final String SQL_DELETE_POKEMON_HAS_HABILIDADES = "DELETE FROM `pokemon_has_habilidades` WHERE  `id_pokemon`=? ";
	private static final String SQL_CREATE = "INSERT INTO `pokemon` (`nombre`,`imagen`) VALUES (?,?)";
	private final static Logger LOG = Logger.getLogger(PokemonDAO.class);

	
	private PokemonDAO() {
		super();
	}
	
	public static synchronized PokemonDAO getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new PokemonDAO();
		}
		return INSTANCE;
	}
	
	@Override
	
	public ArrayList<Pokemon> getAll() {
		String sql = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', p.imagen, h.nombre 'nombre_habilidad', h.id 'id_habilidad' FROM pokemon p LEFT JOIN pokemon_has_habilidades ph ON p.id = ph.id_pokemon LEFT JOIN habilidad h ON ph.id_habilidad = h.id  ORDER BY p.id DESC LIMIT 500;";
		hm = new HashMap<>();
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()){
		
			while(rs.next()) {
				mapper(rs);
			}			
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return new ArrayList<Pokemon> (hm.values());
	}

	@Override
	public ArrayList<Pokemon> getById(int id) throws SQLException {
		String sql = "\r\n" + 
				"SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', p.imagen, h.nombre 'nombre_habilidad', h.id 'id_habilidad' FROM pokemon p LEFT JOIN pokemon_has_habilidades ph ON p.id = ph.id_pokemon LEFT JOIN habilidad h  ON ph.id_habilidad = h.id WHERE p.id = ?  ORDER BY p.id DESC LIMIT 500;";
		ResultSet rs = null;
				hm = new HashMap<>();
				try (Connection con = ConnectionManager.getConnection();
						PreparedStatement pst = con.prepareStatement(sql);
						){

					pst.setInt(1,id );
					rs = pst.executeQuery();
					while(rs.next()) {
						mapper(rs);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}finally {
					
					rs.close();
				}
				return new ArrayList<Pokemon> (hm.values());

	}
	
	public ArrayList<Pokemon> getByName(String name) {
		String sql = "SELECT p.id, p.nombre, h.nombre 'habilidad'\r\n" + 
			"FROM pokemon p\r\n" + 
			"INNER JOIN pokemon_has_habilidades phh ON phh.id_pokemon = p.id\r\n" + 
			"INNER JOIN habilidad h ON h.id = phh.id_habilidad\r\n" + 
			"WHERE p.nombre LIKE ? \r\n" + 
			"ORDER BY p.nombre LIMIT 500;";
		ResultSet rs = null;
			hm = new HashMap<>();
			try (Connection con = ConnectionManager.getConnection();
					PreparedStatement pst = con.prepareStatement(sql);
					){

				pst.setString(1,"%" + name + "%");
				rs = pst.executeQuery();
				
				while(rs.next()) {
					mapper(rs);
				}
				
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}finally {
				
				try {
					rs.close();
				} catch (SQLException e) {
					LOG.error("getByName", e);
				}
			}
			return new ArrayList<Pokemon> (hm.values());

	}

	@Override
	public Pokemon delete(int id) throws Exception {
		String SQL_DELETE = "Delete from pokemon where id = ?";
		Pokemon p =  getById(id).get(0);
				
		try(Connection con = ConnectionManager.getConnection();PreparedStatement pst = con.prepareStatement(SQL_DELETE);){
			pst.setInt(1, id);
			LOG.debug(pst);
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				LOG.info("Se ha borrado con exito el pokemon con id " + id);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return p;
	}
	@Override
	public Pokemon update(int id, Pokemon pojo) throws Exception {
		String SQL_GET_UPDATE = "UPDATE pokemon SET nombre = ?, imagen = ? WHERE id = ?;";
		Connection con = null;
		//Obtenemos las habilidades del pokemon 
				ArrayList<Habilidad> habilidades = (ArrayList<Habilidad>) pojo.getHabilidades();
				
				try{
					con = ConnectionManager.getConnection();
					
					//No realizamos la transaccion hasta que se haga el commit
					con.setAutoCommit(false);
					
					//Actualizamos los datos del pokemon 
				   try( PreparedStatement pst = con.prepareStatement(SQL_GET_UPDATE)){
					   pst.setString(1, pojo.getNombre());
						pst.setString(2, pojo.getImagen());
						pst.setInt(3, pojo.getId());
						
						LOG.debug(pst);
						
						int filasAfectadas = pst.executeUpdate();
						
						//En caso de que se realice correctamente la update, continuamos con las demás transacciones.
						if (filasAfectadas == 1) {
							 try(PreparedStatement pstEliminarHabilidades = con.prepareStatement(SQL_DELETE_POKEMON_HAS_HABILIDADES)){
									//Recorremos las habilidades para eliminarlas de la tabla pokemon_has_habilidades
									pstEliminarHabilidades.setInt(1, pojo.getId());
									
									int affectedRows = pstEliminarHabilidades.executeUpdate();
													
									if(affectedRows>0) {
										//Ahora tenemos que insertar en la tabla por cada habilidad
										try(PreparedStatement pstAddHabilidades = con.prepareStatement(SQL_INSERT_POKEMON_HAS_HABILIDADES)){
											
												for (Habilidad habilidad : habilidades) {
												pstAddHabilidades.setInt(1, pojo.getId());
												pstAddHabilidades.setInt(2, habilidad.getId());
												LOG.debug(pstAddHabilidades);
												pstAddHabilidades.executeUpdate();
											}
										}
									
									}//Fin if affectedrows>0
									
									con.commit();
							 };
							
						
						}//Acaba el IF de filas afectadas
						
				   };
								
				}catch (MySQLIntegrityConstraintViolationException e) {
					if(con!=null) {
						con.rollback();
					}
					
					if(e.getMessage().contains("Duplicate")) {
						throw new MySQLIntegrityConstraintViolationException("El nombre ya existe");
					}else {
						throw new MySQLIntegrityConstraintViolationException("Has introducido alguna habilidad que no existe.");
					}
				}catch (Exception e) {
					throw new Exception(e);
				}finally {
					
					//Cerramos la conexión si no es nula
					
					if(con!=null) {
						con.close();
					}
				}
				return pojo;
	}

	@Override
	public Pokemon create(Pokemon pojo) throws Exception {
		Connection con = null;
							
		//Obtenemos las habilidades del pokemon
		ArrayList <Habilidad> habilidades = (ArrayList<Habilidad>) pojo.getHabilidades();
		
		PreparedStatement pstHab = null;
		try{
			con = ConnectionManager.getConnection();
			//No comita en la base de datos las consultas hasta que se haga un commit (No guarda automaticamente los cambios)
			con.setAutoCommit(false);
			
			PreparedStatement pst = con.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getImagen());
			LOG.debug(pst);
			
			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas == 1) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					pojo.setId(rs.getInt(1));
					
					//Preparo el Statement de las habilidades
					//Cogemos las habilidades
					for (Habilidad h : habilidades) {
						pstHab = con.prepareStatement(SQL_INSERT_POKEMON_HAS_HABILIDADES);
						pstHab.setInt(1, pojo.getId());
						pstHab.setInt(2, h.getId());
						
						LOG.debug(pstHab);
						
						pstHab.executeUpdate();
						
						
					}
									
				}
				
			}
			
		}catch (MySQLIntegrityConstraintViolationException e) {
			LOG.error(e.getMessage());
			//Restablece los cambios
			con.rollback();
			if(e.getMessage().contains("Duplicate")) {
				throw new MySQLIntegrityConstraintViolationException("El nombre ya existe");
			}else {
				throw new MySQLIntegrityConstraintViolationException("Has introducido alguna habilidad que no existe.");
			}
			
			
	
			
			
		}catch(Exception e) {
			LOG.error(e.getMessage());
			//Restablece los cambios
			con.rollback();
			throw new Exception(e);
			
		}finally {
			//Cerramos la conexión si no es nula
			pstHab.close();
			con.commit();
				
			if(con!=null) {
				con.close();
			}
		}
		return pojo;
	}
	
	
	public void mapper(ResultSet rs) {
		
		try {
			
				int idPokemon = rs.getInt("id_pokemon");
				Pokemon p = hm.get(idPokemon);
				
				if(p == null) {
					p = new Pokemon();
					p.setId(idPokemon);
					p.setNombre(rs.getString("nombre_pokemon"));
					p.setImagen(rs.getString("imagen"));
				}
				
				Habilidad h = new Habilidad();
				h.setId(rs.getInt("id_habilidad"));
				h.setHabilidad(rs.getString("nombre_habilidad"));
				p.getHabilidades().add(h);
				hm.put(idPokemon, p);
			
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}

	}

}
