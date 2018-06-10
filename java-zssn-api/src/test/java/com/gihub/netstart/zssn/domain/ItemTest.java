package com.gihub.netstart.zssn.domain;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.netstart.zssn.domain.EPoint;
import com.github.netstart.zssn.domain.Item;

public class ItemTest {

	private Set<Item> leftItens;
	private Set<Item> rightItens;

	@Before
	public void before() throws Exception {
		initializeLeftItens();
		initializeRightItens();
	}

	@Test
	public void sumLeftItenNullTest() {
		Long sumLeftPoints = Item.sumPoint(null);
		Assert.assertEquals(new Long(0), sumLeftPoints);
	}

	@Test
	public void sumLeftZeroItemTest() {
		Long sumLeftPoints = Item.sumPoint(Sets.newLinkedHashSet());
		Assert.assertEquals(new Long(0), sumLeftPoints);
	}

	@Test
	public void sumLeftItensTest() {
		Long sumLeftPoints = Item.sumPoint(this.leftItens);
		Assert.assertEquals(new Long(7), sumLeftPoints);
	}

	@Test
	public void isEqualPointFalseTest() {
		Boolean equalPoints = Item.isEqualPoints(leftItens, rightItens);
		org.junit.Assert.assertFalse(equalPoints);
	}
	
	@Test
	public void isEqualPointTrueTest() {
		Boolean equalPoints = Item.isEqualPoints(leftItens, leftItens);
		org.junit.Assert.assertTrue(equalPoints);
	}

	private void initializeLeftItens() {
		this.leftItens = new HashSet<>();
		leftItens.add(new Item(EPoint.FOOD.toString(), EPoint.FOOD.getPoint(), null));
		leftItens.add(new Item(EPoint.WATER.toString(), EPoint.WATER.getPoint(), null));
	}

	private void initializeRightItens() {
		this.rightItens = new HashSet<>();
		rightItens.add(new Item(EPoint.MEDICATION.toString(), EPoint.MEDICATION.getPoint(), null));
		rightItens.add(new Item(EPoint.AMMUNITION.toString(), EPoint.AMMUNITION.getPoint(), null));
	}

}
