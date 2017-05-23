package presentation.data;



public class CartItemPres {

	private CartItemData cartItem;

	public CartItemPres() {
	}

	public void setCartItem(CartItemData item) {
		cartItem = item;
	}

	public CartItemData getCartItem() {
		return cartItem;
	}

	public String getItemName() {
		return cartItem.getItemName();
	}

	public void setItemName(String name) {
		cartItem.setItemName(name);
	}

	public int getQuantity() {
		return cartItem.getQuantity();
	}

	public void setQuantity(int quant) {
		cartItem.setQuantity(quant);
	}

	public double getPrice() {
		return cartItem.getPrice();
	}

	public void setPrice(double price) {
		cartItem.setPrice(price);
	}

	public double getTotalPrice() {
		return cartItem.getTotalPrice();
	}
}
