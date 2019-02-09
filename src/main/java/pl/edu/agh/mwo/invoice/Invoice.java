package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Collection;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private Collection<Product> products;
	
	public void addProduct(Product product) {
		products.add(product);
	}

	public void addProduct(Product product, Integer quantity) {
		if (quantity == 0 || quantity < 0) {
			throw new IllegalArgumentException("quantity == zero");
		}
		for (int i = 0; i < quantity; i++) {
			addProduct(product);
		}
	}

	public BigDecimal getSubtotal() {
		BigDecimal result = null;
		for (Product product : products) {
			result = result.add(product.getPrice());
		}
		return result;
		//return null;
	}

	public BigDecimal getTax() {
//		BigDecimal result = null;
//		for (Product product : products) {
//			result = result.add(product.getTaxPercent());
//		}
		return null;
	}

	public BigDecimal getTotal() {
//		BigDecimal result = null;
//		for (Product product : products) {
//			result = result.add(product.getPrice());
//		}
//		return result;
		return null;
	}
}
