package com.ipartek.formacion.model.pojo;

public class Habilidad {

	private String Habilidad;
	private int id;
	
	public Habilidad() {
		super();
		this.Habilidad = "";
		this.id = 0;
	}

	public Habilidad(String Habilidad ) {
		this();
		this.Habilidad = Habilidad;
	}
	
	public String getHabilidad() {
		return Habilidad;
	}

	public void setHabilidad(String habilidad) {
		Habilidad = habilidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
