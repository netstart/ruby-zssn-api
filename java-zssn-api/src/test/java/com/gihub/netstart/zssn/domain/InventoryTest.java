package com.gihub.netstart.zssn.domain;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.netstart.zssn.domain.EPoint;
import com.github.netstart.zssn.domain.Inventory;
import com.github.netstart.zssn.domain.Item;

public class InventoryTest {

	private Inventory leftInventory;
	private Inventory rightInventory;

	@Before
	public void before() throws Exception {
		leftInventory = intializeLeftInventory();
		rightInventory = intializeRightInventory();
	}

	@Test
	public void tradeFalse() {
		Boolean trade = Inventory.trade(leftInventory.getItens(), rightInventory.getItens());
		Assert.assertFalse(trade);
	}

	@Test
	public void tradeTrue() {
		Set<Item> leftItens = new HashSet<>();
		leftItens.add(new Item(EPoint.FOOD.toString(), EPoint.FOOD.getPoint(), null));
		Inventory leftInventory = new Inventory(leftItens);

		Boolean trade = Inventory.trade(leftInventory.getItens(), rightInventory.getItens());

		Assert.assertTrue(trade);
		Assert.assertEquals(2, leftInventory.getItens().size());
		Assert.assertEquals(1, rightInventory.getItens().size());

	}

	private Inventory intializeLeftInventory() {
		return new Inventory(initializeLeftItens());
	}

	private Inventory intializeRightInventory() {
		return new Inventory(initializeRightItens());
	}

	private Set<Item> initializeLeftItens() {
		Set<Item> leftItens = new HashSet<>();
		leftItens.add(new Item(EPoint.FOOD.toString(), EPoint.FOOD.getPoint(), null));
		leftItens.add(new Item(EPoint.WATER.toString(), EPoint.WATER.getPoint(), null));
		return leftItens;
	}

	private Set<Item> initializeRightItens() {
		Set<Item> rightItens = new HashSet<>();
		rightItens.add(new Item(EPoint.MEDICATION.toString(), EPoint.MEDICATION.getPoint(), null));
		rightItens.add(new Item(EPoint.AMMUNITION.toString(), EPoint.AMMUNITION.getPoint(), null));
		return rightItens;
	}

}
