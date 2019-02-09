package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
	private final String name;

	private final BigDecimal price;

	private final BigDecimal taxPercent;

	protected Product(String name, BigDecimal price, BigDecimal tax) {
		if (name == null) {
			throw new IllegalArgumentException("null");
		}
		if (price == null) {
			throw new IllegalArgumentException("null");
		}

		
		this.name = name;
		this.price = price;
		this.taxPercent = tax;
		
		if (name.equals("")) {
			throw new IllegalArgumentException("Pusta nazwa");
		}
		if (price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Cena mniejsza od zera");
		}
	}

	public String getName() {
		return this.name;
	}

	public BigDecimal getPrice() {
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
