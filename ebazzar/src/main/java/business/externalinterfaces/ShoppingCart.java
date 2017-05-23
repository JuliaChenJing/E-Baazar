package business.externalinterfaces;

import java.util.List;

// a shopping cart could be a live one or a saved one
public interface ShoppingCart {
    Address getShippingAddress();
    Address getBillingAddress();
    CreditCard getPaymentInfo();
    List<CartItem> getCartItems();
    void setCartItems(List<CartItem> cartItems);
    double getTotalPrice();
    boolean deleteCartItem(String name);
    boolean isEmpty();

    //setters for testing   
    
}
