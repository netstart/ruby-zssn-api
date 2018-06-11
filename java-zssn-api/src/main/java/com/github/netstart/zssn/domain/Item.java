package com.github.netstart.zssn.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
public class Item implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull
	@Min(value = 1L)
	@Column(name = "point", nullable = false)
	private Long point;

	@ManyToOne(optional = false)
	@NotNull
	private Inventory inventory;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove

	public Item() {
	}

	public Item(String name, Long point, Inventory inventory) {
		this.name = name;
		this.point = point;
		this.inventory = inventory;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Item name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPoint() {
		return point;
	}

	public Item point(Long point) {
		this.point = point;
		return this;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Item inventory(Inventory inventory) {
		this.inventory = inventory;
		return this;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	public static Long sumPoint(Set<Item> itens) {
		if (itens == null || itens.size() == 0) {
			return 0L;
		}

		return itens.stream().mapToLong(item -> item.getPoint()).sum();
	}

	public static Boolean isEqualPoints(Set<Item> itensLeft, Set<Item> rightItems) {
		Long rightPoints = sumPoint(rightItems);
		Long leftPoins = sumPoint(itensLeft);
		return leftPoins.equals(rightPoints);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Item item = (Item) o;
		if (item.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), item.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Item{" + "id=" + getId() + ", name='" + getName() + "'" + ", point=" + getPoint() + "}";
	}

	@Override
	public Item clone() {
		Item clone = new Item();
		clone.setId(id);
		clone.setPoint(point);
		clone.setName(name);
		return clone;
	}

	public static Set<Item> clone(Set<Item> itens) {
		return itens.stream().map((Item item) -> item.clone()).collect(Collectors.toSet());
	}

}
