package com.gihub.netstart.zssn.domain;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.netstart.zssn.domain.Inventory;
import com.github.netstart.zssn.domain.Location;
import com.github.netstart.zssn.domain.Survivor;

public class SurvivorTest {

	Survivor survivorClayton;
	Survivor survivorDaniela;
	Survivor survivorCatherine;
	Survivor survivorSophia;

	@Before
	public void beforeTest() {
		makeSurvivorClayton();
		makeSurvivorDaniela();
		makeSurvivorCatherine();
		makeSurvivorSophia();
	}

	@Test
	public void isContaminedTrueTest() {
		survivorDaniela.report(survivorCatherine);
		survivorDaniela.report(survivorSophia);
		survivorDaniela.report(survivorClayton);
		Assert.assertTrue("Deveria ser true, estar contaminado", survivorDaniela.isContamined());
	}
	
	@Test
	public void isContaminedFalseDontCountWhenRepeatReportTest() {
		survivorDaniela.report(survivorCatherine);
		survivorDaniela.report(survivorSophia);
		survivorDaniela.report(survivorCatherine);
		survivorDaniela.report(survivorCatherine);
		survivorDaniela.report(survivorCatherine);
		Assert.assertTrue("Deveria ser false, Não está contaminado", survivorDaniela.isContamined());
	}
	
	@Test
	public void isContaminedFalseTest() {
		survivorDaniela.report(survivorCatherine);
		survivorDaniela.report(survivorSophia);
		Assert.assertFalse("Deveria ser false, não está contaminado", survivorDaniela.isContamined());
	}

	private Survivor makeSurvivorClayton() {
		Location lastLocation = new Location("-23.3785587", "-51.9338098,18");
		Inventory inventory = new Inventory();
		this.survivorClayton = new Survivor("Clayton", 38, lastLocation, inventory);
		this.survivorClayton.setId(1L);
		return survivorClayton;
	}

	private Survivor makeSurvivorDaniela() {
		Location lastLocation = new Location("-23.3785587", "-51.9338098,18");
		Inventory inventory = new Inventory();
		this.survivorDaniela = new Survivor("Daniela", 38, lastLocation, inventory);
		this.survivorDaniela.setId(2L);
		return survivorDaniela;
	}

	private Survivor makeSurvivorCatherine() {
		Location lastLocation = new Location("-23.3785587", "-51.9338098,18");
		Inventory inventory = new Inventory();
		this.survivorCatherine = new Survivor("Catherine", 38, lastLocation, inventory);
		this.survivorCatherine.setId(3L);
		return survivorCatherine;
	}

	private Survivor makeSurvivorSophia() {
		Location lastLocation = new Location("-23.3785587", "-51.9338098,18");
		Inventory inventory = new Inventory();
		this.survivorSophia = new Survivor("Sophia", 38, lastLocation, inventory);
		this.survivorSophia.setId(4L);
		return survivorSophia;
	}

}
