package presentation.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import business.externalinterfaces.Product;

public class ProductPres {
	private Product product;

	public ProductPres() {
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

	public String getName() {
		return product.getProductName();
	}

	public void setName(String aName) {
		product.setProductName(aName);
	}

	public int getId() {
		return product.getProductId();
	}

	public void setId(int idStr) {
		product.setProductId(idStr);
	}

	public String getMfg() {
		return product.getMfgDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}

	public void setMfgDate(String date) {
		product.setMfgDate(LocalDate.parse(date));
	}

	public String getDescription() {
		return product.getDescription();
	}

	public void setDescription(String d) {
		product.setDescription(d);
	}

	public String getUnitPrice() {
		return String.format("%.2f", product.getUnitPrice());
	}

	public void setUnitPrice(String up) {
		product.setUnitPrice(Double.parseDouble(up));
	}

	public int getQuantityAvail() {
		return product.getQuantityAvail();
	}

	public void setQuantityAvail(String qa) {
		product.setUnitPrice(Integer.parseInt(qa));
	}
}
