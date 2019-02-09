package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private Collection<Product> products = new ArrayList<Product>();
	
	public void addProduct(Product product) {
		products.add(product);
	}

	public void addProduct(Product product, Integer quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("quantity == zero");
		}
		for (int i = 0; i < quantity; i++) {
			addProduct(product);
		}
	}

	public BigDecimal getTotalNetPrice() {
		BigDecimal result = BigDecimal.ZERO;
		for (Product product : products) {
			result = result.add(product.getPrice());
		}
		return result;
	}

	public BigDecimal getTax() {
		BigDecimal result = BigDecimal.ZERO;
		for (Product product : products) {
			result = result.add(product.getTaxPercent().multiply(product.getPrice()));
		}
		return result;
	}

	public BigDecimal getTotalGrossPrice() {
		BigDecimal result = BigDecimal.ZERO;
		for (Product product : products) {
			result = result.add(product.getPriceWithTax());
		}
		return result;
	}
}
