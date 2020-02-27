package com.ipartek.formacion.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.ipartek.formacion.model.PokemonDAO;
import com.ipartek.formacion.model.pojo.Pokemon;

/**
 * Servlet implementation class PokemonController
 */
@WebServlet({"/api/pokemon/", ("/api/pokemon/*")})
public class PokemonController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = Logger.getLogger(PokemonController.class);
	
	private final int SC_OK = 200, SC_NOT_FOUND = 404, SC_NO_CONTENT = 204, SC_CREATED = 201;
    
	//Usar un HASHMAP con ID (KEY), Pokemon
    private static PokemonDAO dao;
    
    public PokemonController() {
        super();
       
    }

	@Override
	public void destroy() {
		dao = null;
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		 dao = PokemonDAO.getInstance();
		 super.init();
	}

	
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		super.service(req, res);
		res.setContentType("aplication/json");
		res.setCharacterEncoding("utf-8");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nombre = request.getParameter("nombre");
		String id = request.getPathInfo();
		
		ArrayList<Pokemon> pokemons = null;
		try {
		
			if(!"".equals(nombre) && nombre != null) {
				pokemons =  dao.getByName(nombre);
			}else if(!"".equals(id) && id != null){	
				id = id.replaceAll("/", "");
					pokemons =  dao.getById(Integer.parseInt(id));
			}else {	
				pokemons =  dao.getAll();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int codigo = (pokemons.isEmpty()) ? 404 : 200;
		response.setStatus(codigo);
		
			try(PrintWriter out = response.getWriter();){
				
				if(codigo == 200) {
					Gson json = new Gson();
					out.println(json.toJson(pokemons));
					//out.println(pokemons);
				}else {
					String mensaje = "No se ha encontrado ningun Pokemon";
					out.println(mensaje);
				}
				out.flush();
			}
		
	}

	/** ---------------------------- doPost -----------------------------------------------
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Pokemon p = new Pokemon();
		// convertir json del request body a Objeto
		BufferedReader reader = request.getReader();               
		Gson gson = new Gson();
		p = gson.fromJson(reader, Pokemon.class);


		
		try {
			p = dao.create(p);
		} catch (Exception e) {
			LOG.error("No se ha podido crear el Pokemon" + e.getMessage());
		}
		
		int codigo = ( p.getId()!= 0 )? ("".equals(p.getNombre()) || p.getNombre() == null )? SC_NO_CONTENT : SC_CREATED : SC_NOT_FOUND; 
		response.setStatus(codigo);
		
			try(PrintWriter out = response.getWriter();){
				/**
				 * Condicion para comprobar si el codigo de estado que devuelve es el correcto y en caso contrario
				 * mandar un mensaje al usuario 
				 */
				if(codigo == SC_OK) {
					Gson json = new Gson();
					out.println(json.toJson(p));
				}else {
					String mensaje = "No se ha encontrado ningun Pokemon";
					out.println(mensaje);
				}
				out.flush();
				dao.getAll();

			}//End TRY
		
	}//DoPost END


	/** ---------------------------- doPut -----------------------------------------------
	 * 
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Pokemon p = new Pokemon();
		String id = request.getPathInfo();
		id = id.replace("/", "");
		
		// convertir json del request body a Objeto
		BufferedReader reader = request.getReader();               
		Gson gson = new Gson();
		p = gson.fromJson(reader, Pokemon.class);


		
		try {

			p = dao.update(Integer.parseInt(id), p);
		} catch (Exception e) {
			LOG.error("No se ha podido crear la resena " + e.getMessage());
		}
		
		int codigo = ( Integer.parseInt(id) != 0 )? ("".equals(p.getNombre()) || p.getNombre() == null )? SC_NO_CONTENT : SC_NOT_FOUND : SC_OK; 
		response.setStatus(codigo);
		
			try(PrintWriter out = response.getWriter();){
				/**
				 * Condicion para comprobar si el codigo de estado que devuelve es el correcto y en caso contrario
				 * mandar un mensaje al usuario 
				 */
				if(codigo == SC_OK) {
					Gson json = new Gson();
					out.println(json.toJson(p));
				}else {
					String mensaje = "No se ha encontrado ningun Pokemon";
					out.println(mensaje);
				}
				out.flush();
				dao.getAll();
			}
	}

	/** ---------------------------- doDelete -----------------------------------------------
	 * 
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getPathInfo();
		id = id.replace("/", "");
		try {
			
			
			dao.delete(Integer.parseInt(id));
		} catch (Exception e) {
			LOG.error("No se ha podido borrar el Pokemon " + e.getMessage());
		}
		
		int codigo = ( Integer.parseInt(id) != 0 )? SC_OK : SC_NOT_FOUND;
		response.setStatus(codigo);
		
			try(PrintWriter out = response.getWriter();){
				/**
				 * Condicion para comprobar si el codigo de estado que devuelve es el correcto y en caso contrario
				 * mandar un mensaje al usuario 
				 */
				if(codigo == SC_OK) {
					Gson json = new Gson();
					out.println(json.toJson("El Pokemon con id " + id + " ha sido borrado con exito"));
				}else {
					String mensaje = "No se ha encontrado ningun Pokemon";
					out.println(mensaje);
				}
				out.flush();
				dao.getAll();

			}
	}

	
}
