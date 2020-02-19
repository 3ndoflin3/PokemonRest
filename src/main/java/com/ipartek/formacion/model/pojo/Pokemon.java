package com.ipartek.formacion.model.pojo;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {

	private int id;
	private String nombre;
	private String imagen;
	private List<Habilidad> habilidades;
//	TODO validaciones con javax validator 
	
	public Pokemon() {
		super();
		this.id = 0;
		this.nombre = "";
		this.habilidades = new ArrayList<>();;
	}
	
	public Pokemon(int id, String nombre, String habilidad) {
		this();
		this.id = id;
		this.nombre = nombre;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Habilidad> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidad> habilidades) {
		this.habilidades = habilidades;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	
	
	
	
	
	
	
}
