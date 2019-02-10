package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
	private Invoice invoice;

	@Before
	public void createEmptyInvoiceForTheTest() {
		invoice = new Invoice();
	}

	@Test
	public void testEmptyInvoiceHasEmptySubtotal() {
		Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
	}

	@Test
	public void testEmptyInvoiceHasEmptyTaxAmount() {
		Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
	}

	@Test
	public void testEmptyInvoiceHasEmptyTotal() {
		Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
	}

	@Test
	public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
		Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
		invoice.addProduct(taxFreeProduct);
		Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
	}

	@Test
	public void testInvoiceHasProperSubtotalForManyProducts() {
		invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
		invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
		invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
		Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
	}

	@Test
	public void testInvoiceHasProperTaxValueForManyProduct() {
		// tax: 0
		invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
		// tax: 8
		invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
		// tax: 2.30
		invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
		Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
	}

	@Test
	public void testInvoiceHasProperTotalValueForManyProduct() {
		// price with tax: 200
		invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
		// price with tax: 108
		invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
		// price with tax: 12.30
		invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
		Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
	}

	@Test
	public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
		// 2x kubek - price: 10
		invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
		// 3x kozi serek - price: 30
		invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
		// 1000x pinezka - price: 10
		invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
		Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
	}

	@Test
	public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
		// 2x chleb - price with tax: 10
		invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
		// 3x chedar - price with tax: 32.40
		invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
		// 1000x pinezka - price with tax: 12.30
		invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
		Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithZeroQuantity() {
		invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithNegativeQuantity() {
		invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
	}
	
	@Test
	public void testInvoiceHasNumber() {
		Integer number = invoice.getNumber();
		Assert.assertNotNull(number);
	}
	
	@Test
	public void testInvoiceNumberIsGreaterThanZero() {
		Integer number = invoice.getNumber();
		Assert.assertThat(number, Matchers.greaterThan(0));
	}
	
	@Test
	public void testTwoInvoiceHasDifferentNumber() {
		Integer number1 = invoice.getNumber();
		Integer number2 = new Invoice().getNumber();
		Assert.assertNotEquals(number1, number2);
	}
	
	@Test
	public void testTheSameInvoiceHasTheSameNumber() {
		Integer number1 = invoice.getNumber();
		Integer number2 = invoice.getNumber();
		Assert.assertEquals(number1, number2);
	}
	
	@Test
	public void testTheSecondInvoiceHasGreaterNumber() {
		for (int i = 0; i < 100; i++) {
			Integer number1 = invoice.getNumber();
			Integer number2 = new Invoice().getNumber();
			Assert.assertThat(number1, Matchers.lessThan(number2));
		}
	}
	
	@Test
	public void testPrintedInvoiceHasNumber(){
		String printedInvoice = invoice.getAsText();
		String number = invoice.getNumber().toString();
		Assert.assertThat(printedInvoice, Matchers.containsString("nr " + number));
	}
	
	@Test
	public void testLiczbaPozycji(){
		//invoice.addProduct(new TaxFreeProduct("chleb", new BigDecimal("5")), 2);
		//invoice.addProduct(new TaxFreeProduct("frytki", new BigDecimal("6.50")), 3);
		Assert.assertThat(invoice.getAsText(), Matchers.containsString("Liczba pozycji 3"));
	}
	
	@Test
	public void testEachProductInItsOwnLine(){
		invoice.addProduct(new TaxFreeProduct("chleb", new BigDecimal("5")), 2);
		invoice.addProduct(new TaxFreeProduct("frytki", new BigDecimal("6.50")), 3);
		Assert.assertThat(invoice.getAsText(), Matchers.containsString("chleb 2 5,00\nfrytki 3 6,50"));
	}
	
	@Test
	public void testAddingTheSameProductTwice(){
		invoice.addProduct(new TaxFreeProduct("chleb", new BigDecimal("5")));
		invoice.addProduct(new TaxFreeProduct("chleb", new BigDecimal("5")));
		Assert.assertThat(invoice.getAsText(), Matchers.containsString("chleb 2 5,00"));
	}
	
	
	
	
}
