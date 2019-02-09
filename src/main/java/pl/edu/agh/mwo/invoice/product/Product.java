package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
	private final String name;

	private final BigDecimal price;

	private final BigDecimal taxPercent;

	protected Product(String name, BigDecimal price, BigDecimal tax) {
		this.name = name;
		this.price = price;
		this.taxPercent = tax;
	}

	public String getName() {
		if (this.name.equals("") || this.name.equals(null)) {
			throw new IllegalArgumentException("Pusta nazwa");
		}
		return this.name;
	}

	public BigDecimal getPrice() {
		if (this.price.equals(null)) {
			throw new IllegalArgumentException("Pusta nazwa");
		}
		return this.price;
	}

	public BigDecimal getTaxPercent() {
		return this.taxPercent;
	}

	public BigDecimal getPriceWithTax() {
		BigDecimal result = this.price.add(this.price.multiply(this.taxPercent));
		return result;
	}
}
