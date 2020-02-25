package com.ipartek.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.ipartek.formacion.model.pojo.Habilidad;
import com.ipartek.formacion.model.pojo.Pokemon;

public class PokemonTest {

	
	
	@Test
	public void test() {
	
		Pokemon p = new Pokemon();
		Habilidad h = new Habilidad();
		
		p.setHabilidades(new ArrayList<Habilidad>());
		assertNotNull(p.getHabilidades());
		
		if(p.getHabilidades() != null)
			assertTrue(true);
		
		
	}

	
}
