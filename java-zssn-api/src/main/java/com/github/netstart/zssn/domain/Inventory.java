package com.github.netstart.zssn.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Inventory.
 */
@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "inventory")
	@JsonIgnore
	private Set<Item> itens = new HashSet<>();

	public Inventory() {

	}

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Item> getItens() {
		return itens;
	}

	public Inventory itens(Set<Item> items) {
		this.itens = items;
		return this;
	}

	public Inventory addItens(Item item) {
		this.itens.add(item);
		item.setInventory(this);
		return this;
	}

	public Inventory addItens(Set<Item> itens) {
		itens.forEach((Item item) -> this.addItens(item));
		return this;
	}

	public Inventory removeItens(Set<Item> itens) {
		this.itens.removeAll(itens);
		return this;
	}

	public Inventory removeItem(Item item) {
		this.itens.remove(item);
		item.setInventory(null);
		return this;
	}

	public void setItens(Set<Item> items) {
		this.itens = items;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	public static Boolean trade(Set<Item> leftItens, Set<Item> rightItems) {
		if (!Item.isEqualPoints(leftItens, rightItems)) {
			// Quantidade de pontos não é igual... talvez seja melhor lançar uma exception
			return false;
		}
		Inventory leftInventory = getInventory(leftItens);
		Inventory rightInventory = getInventory(rightItems);

		Set<Item> leftCloned = Item.clone(leftItens);
		Set<Item> rightCloned = Item.clone(rightItems);

		leftInventory.removeItens(leftItens);
		rightInventory.removeItens(rightItems);

		rightInventory.addItens(leftCloned);
		leftInventory.addItens(rightCloned);

		return true;
	}

	private static Inventory getInventory(Set<Item> itens) {
		// Considero que todos os itens sãodo mesmo Inventory
		return itens.iterator().next().getInventory();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Inventory inventory = (Inventory) o;
		if (inventory.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), inventory.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Inventory{" + "id=" + getId() + "}";
	}
}
