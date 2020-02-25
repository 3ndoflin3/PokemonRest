package com.ipartek.formacion.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.ipartek.formacion.model.HabilidadesDAO;
import com.ipartek.formacion.model.pojo.Habilidad;

/**
 * Servlet implementation class HabilidadesController
 */
@WebServlet({"/api/habilidad/", ("/api/habilidad/*")})
public class HabilidadesController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = Logger.getLogger(PokemonController.class);
	
	private final int SC_OK = 200, SC_NOT_FOUND = 404, SC_NO_CONTENT = 204, SC_CREATED = 201;
    private ArrayList<Habilidad> habilidades;
	private HabilidadesDAO dao;
       
    //CONSTRUCTOR
    public HabilidadesController() {
        super();
    }

	//INIT
	public void init(ServletConfig config) throws ServletException {
		dao = HabilidadesDAO.getInstance();
		super.init();
	}

	//DESTROY
	public void destroy() {
		dao = null;
		super.destroy();
	}

	/** ---------------------------- doGet -----------------------------------------------
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		habilidades = (ArrayList<Habilidad>) dao.getHabilidades();
		
		int codigo = (habilidades.isEmpty()) ? 404 : 200;
		
		response.setStatus(codigo);
		
		try(PrintWriter out = response.getWriter();){
			
			if(codigo == 200) {
				Gson json = new Gson();
				out.println(json.toJson(habilidades));
			}else {
				String mensaje = "No se ha encontrado ninguna Habilidad";
				out.println(mensaje);
			}
			out.flush();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
