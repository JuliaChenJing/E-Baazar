package presentation.data;

import static presentation.util.UtilForUIClasses.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import business.exceptions.BackendException;
import business.externalinterfaces.*;
import business.usecasecontrol.BrowseAndSelectController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.control.BrowseSelectUIControl;
import presentation.gui.GuiConstants;

public enum BrowseSelectData {
	INSTANCE;
	private static final Logger LOG = Logger.getLogger(BrowseSelectData.class.getName());
	// Fields that are maintained as user interacts with UI
	private CatalogPres selectedCatalog;
	private ProductPres selectedProduct;
	private CartItemPres selectedCartItem;

	private BrowseAndSelectController browseAndSelectController = new BrowseAndSelectController();

	public CatalogPres getSelectedCatalog() {
		return selectedCatalog;
	}

	public void setSelectedCatalog(CatalogPres selectedCatalog) {
		this.selectedCatalog = selectedCatalog;
	}

	public ProductPres getSelectedProduct() {
		return selectedProduct;
	}

	public Product getProductForProductName(String name) throws BackendException {
		return browseAndSelectController.getProductForProductName(name);
	}

	public void setSelectedProduct(ProductPres selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public CartItemPres getSelectedCartItem() {
		return selectedCartItem;
	}

	public void setSelectedCartItem(CartItemPres selectedCartItem) {
		this.selectedCartItem = selectedCartItem;
	}

	// ShoppingCart model
	private ObservableList<CartItemPres> cartData;

	public ObservableList<CartItemPres> getCartData() {
		return cartData;
	}

	public CartItemPres cartItemPresFromData(String name, double unitPrice, int quantAvail) {
		CartItemData item = new CartItemData();
		item.setItemName(name);
		item.setPrice(unitPrice);
		item.setQuantity(quantAvail);
		CartItemPres cartPres = new CartItemPres();
		cartPres.setCartItem(item);
		return cartPres;
	}

	/**
	 * Adds a new item to the displayed shopping cart based on user input. New
	 * item is placed at the top of the displayed table. Then it updates the
	 * items in the liveCart stored in the ShoppingCartSubsystem
	 */
	public void addToCart(CartItemPres cartPres) {
		ObservableList<CartItemPres> newCartItems = FXCollections.observableArrayList(cartPres);
		// Place the new item at the top of the list
		if (cartData != null) {
			newCartItems.addAll(cartData);
		}

		// replace the backing data for displayed table by the updated list
		cartData = newCartItems;

		// updates the liveCart in ShoppingCartSubsystem
		updateShoppingCart();
	}

	public boolean removeFromCart(ObservableList<CartItemPres> toBeRemoved) {
		if (cartData != null && toBeRemoved != null && !toBeRemoved.isEmpty()) {
			cartData.remove(toBeRemoved.get(0));
			updateShoppingCart();
			return true;
		}
		return false;
	}

	/** Sets the latest version of cartData to the ShoppingCartSubsystem */
	public void updateShoppingCart() {
		List<CartItem> theCartItems = cartItemPresToCartItemList(cartData);
		browseAndSelectController.updateShoppingCartItems(obtainCurrentShoppingCartSubsystem(), theCartItems);
	}

	/**
	 * Used to update cartData (in this class) when shopping cart subsystem is
	 * changed at back end
	 */
	public void updateCartData() {
		List<CartItem> cartItems = new ArrayList<CartItem>();
		List<CartItem> newlist = obtainCurrentShoppingCartSubsystem().getCartItems();
		if (newlist != null)
			cartItems = newlist;
		cartData = FXCollections.observableList(cartItemsToCartItemPres(cartItems));
		BrowseSelectUIControl.INSTANCE.updateCartItems(cartData);
	}

	/**
	 * Returns number of units of this product available according to the
	 * database
	 */
	public int quantityAvailable(Product product) {
		LOG.warning("BrowseSelectData method quantityAvailable has not been implemented");
		return DefaultData.DEFAULT_QUANTITY_AVAILABLE;

	}

	// obtain current shopping cart
	public ShoppingCartSubsystem obtainCurrentShoppingCartSubsystem() {
		SessionCache session = SessionCache.getInstance();
		ShoppingCartSubsystem cachedCart = (ShoppingCartSubsystem) session.get(SessionCache.SHOP_CART);
		CustomerSubsystem custInSession = (CustomerSubsystem) session.get(SessionCache.CUSTOMER);

		//from 1.3 UIData to 1.4 Controller to 1.5 Subsystem
		ShoppingCartSubsystem returnedShoppingCartSS = browseAndSelectController.obtainCurrentShoppingCartSubsystem(custInSession, cachedCart);
		if (cachedCart == null) {
			session.add(SessionCache.SHOP_CART, returnedShoppingCartSS);
		}
		return returnedShoppingCartSS;
	}

	// CatalogList data
	public List<CatalogPres> getCatalogList() throws BackendException {
		return browseAndSelectController.getCatalogs().stream().map(catalog -> catalogToCatalogPres(catalog))
				.collect(Collectors.toList());
	}

	// ProductList data
	public List<ProductPres> getProductList(CatalogPres selectedCatalog) throws BackendException {
		return browseAndSelectController.getProducts(selectedCatalog.getCatalog()).stream().map(prod -> productToProductPres(prod))
				.collect(Collectors.toList());
	}

	// ProductDetails data
	public List<String> getProductDisplayValues(ProductPres productPres) {
		return Arrays.asList(productPres.nameProperty().get(), productPres.unitPriceProperty().get(),
				productPres.quantityAvailProperty().get(), productPres.descriptionProperty().get());
	}

	public List<String> getProductFieldNamesForDisplay() {
		return GuiConstants.DISPLAY_PRODUCT_FIELDS;
	}

	// Synchronizers
	private class ShoppingCartSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			cartData = list;
		}
	}

	public ShoppingCartSynchronizer getShoppingCartSynchronizer() {
		return new ShoppingCartSynchronizer();
	}
}
