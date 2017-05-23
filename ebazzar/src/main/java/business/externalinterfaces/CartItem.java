package business.externalinterfaces;

public interface CartItem {
	//shopping cart could be a live one or a saved one
	public boolean isAlreadySaved();
	//records the shopping cart this cart item is in
	public Integer getCartid();
	//?
	public Integer getLineitemid();
	public Integer getProductid();
	public String getProductName();
	public String getQuantity();
	public String getTotalprice();
	public void setCartId(int id);
}
