package presentation.control;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.data.BrowseSelectData;
import presentation.data.CartItemPres;
import presentation.data.CatalogPres;
import presentation.data.ProductPres;
import presentation.gui.CatalogListWindow;
import presentation.gui.OrdersWindow;
import presentation.gui.ProductDetailsWindow;
import presentation.gui.ProductListWindow;
import presentation.gui.ShoppingCartWindow;
import presentation.util.CacheReader;
import presentation.util.TableUtil;
import business.exceptions.BackendException;
//import rulesengine.OperatingException;
//import rulesengine.ReteWrapper;
//import rulesengine.ValidationException;
//import system.rulescore.rulesengine.*;
//import system.rulescore.rulesupport.*;
//import system.rulescore.util.*;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.exceptions.UnauthorizedException;
import business.externalinterfaces.Product;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.usecasecontrol.BrowseAndSelectController;

public enum BrowseSelectUIControl {
	// Singleton
	INSTANCE;

	private BrowseAndSelectController controller = new BrowseAndSelectController();
	// Windows that this controller manages
	// It's difficult to manage CatalogListWindow, so instead
	// we access CatalogListWindow statically
	// private CatalogListWindow catalogListWindow;
	private ProductListWindow productListWindow;
	private ProductDetailsWindow productDetailsWindow;
	private ShoppingCartWindow shoppingCartWindow;
	private Stage primaryStage;
	private Callback startScreenCallback;

	public void setPrimaryStage(Stage ps, Callback callback) {
		primaryStage = ps;
		startScreenCallback = callback;
	}

	// Number of units requested when item is first added to cart
	private static final int INIT_QUANT_REQUESTED = 1;

	// Handlers for browse and select portion of Start page
	private class OnlinePurchaseHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			try {
				// when the window object is created, it is populated
				CatalogListWindow catList = CatalogListWindow.getInstance(primaryStage,
						FXCollections.observableList(BrowseSelectData.INSTANCE.getCatalogList()));

				catList.show(); // show the CatalogListWindow
				primaryStage.hide();
			} catch (BackendException e) {
				startScreenCallback.displayError("Database error. Message: " + e.getMessage());
				primaryStage.show();
			}
		}
	}

	// this is an event handler , after clicking the "Retrieve Saved Cart"
	// button
	private class RetrieveSavedCartHandler implements EventHandler<ActionEvent>, Callback {

		// interface Callback requires to implement this method
		public void doUpdate() {
			
			try {
				// check if this user have authorization to open this window
				Authorization.checkAuthorization(shoppingCartWindow, CacheReader.custIsAdmin());
			} catch (UnauthorizedException e) {
				displayError(e.getMessage());
				return;
			}
			// make saved cart the live cart
			controller.retrieveSavedCart(BrowseSelectData.INSTANCE.obtainCurrentShoppingCartSubsystem(),
					CacheReader.readLoggedIn());

			BrowseSelectData.INSTANCE.updateCartData();
			primaryStage.hide();
			shoppingCartWindow.clearMessages();
			shoppingCartWindow.show();
		}

		public Text getMessageBar() {
			return startScreenCallback.getMessageBar();
		}

		@Override
		// interface EventHandler requires to implement this method
		public void handle(ActionEvent evt) {
			// a shoppingCartWindow object is needed for a new window for
			// customer to do browse and select
			// there will only be one ShoppingCartWindow at one time so the
			// ShoppingCartWindow class is a singleton
			shoppingCartWindow = ShoppingCartWindow.INSTANCE;
			boolean isLoggedIn = CacheReader.readLoggedIn();
			// if not logged in
			if (!isLoggedIn) {
				LoginUIControl loginControl = new LoginUIControl(shoppingCartWindow, primaryStage, this);
				loginControl.startLogin();
			}
			// if logged in
			else {
				doUpdate();
			}
		}

	}

	public OnlinePurchaseHandler getOnlinePurchaseHandler() {
		return new OnlinePurchaseHandler();
	}

	// it is in BrowseSelectUIControl class, responsible for the connection
	// between UI and the controller
	public RetrieveSavedCartHandler getRetrieveSavedCartHandler() {
		return new RetrieveSavedCartHandler();
	}

	////////// Handlers for CatalogListWindow
	private class ViewProductsHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent evt) {

			TableView<CatalogPres> table = CatalogListWindow.getInstance().getTable();
			CatalogPres cat = table.getSelectionModel().getSelectedItem();
			if (cat == null) {
				CatalogListWindow.getInstance().displayError("Please select a row.");
			} else {
				try {
					BrowseSelectData.INSTANCE.setSelectedCatalog(cat);
					CatalogListWindow.getInstance().clearMessages();
					productListWindow = new ProductListWindow(cat);
					List<ProductPres> prods = BrowseSelectData.INSTANCE.getProductList(cat);
					productListWindow.setData(FXCollections.observableList(prods));
					CatalogListWindow.getInstance().hide();
					productListWindow.show();
				} catch (BackendException e) {
					CatalogListWindow.getInstance()
							.displayError("Unable to display list of products: " + e.getMessage());
				}
			}
		}
	}

	private class BackToPrimaryHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			startScreenCallback.clearMessages();
			primaryStage.show();
			CatalogListWindow.getInstance().hide();
		}
	}

	private class CatalogsToCartHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.clearMessages();
			ShoppingCartWindow.INSTANCE.show();
			CatalogListWindow.getInstance().hide();
		}
	}

	public ViewProductsHandler getViewProductsHandler() {
		return new ViewProductsHandler();
	}

	public BackToPrimaryHandler getBackToPrimaryHandler() {
		return new BackToPrimaryHandler();
	}

	public CatalogsToCartHandler getCatalogsToCartHandler() {
		return new CatalogsToCartHandler();
	}

	///////// Handlers for Product List Window
	private class BackToCatalogListHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			CatalogListWindow.getInstance().show();
			productListWindow.hide();
		}
	}

	private class ViewProductDetailsHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			TableView<ProductPres> table = productListWindow.getTable();
			ProductPres prod = table.getSelectionModel().getSelectedItem();
			if (prod == null) {
				productListWindow.displayError("Please select a row.");
			} else {
				BrowseSelectData.INSTANCE.setSelectedProduct(prod);
				productListWindow.clearMessages();
				productDetailsWindow = new ProductDetailsWindow(prod);
				productListWindow.hide();
				productDetailsWindow.show();
			}
		}
	}

	public BackToCatalogListHandler getBackToCatalogListHandler() {
		return new BackToCatalogListHandler();
	}

	public ViewProductDetailsHandler getViewProductDetailsHandler() {
		return new ViewProductDetailsHandler();
	}

	//////////// ProductDetailsWindow handlers
	private class BackToProductListHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			productListWindow.show();
			productDetailsWindow.hide();
		}
	}

	/**
	 * Converts new cart item data, which is input by user, into a CartItemPres
	 * object (data is put into a CartItemData which is inserted into
	 * CartItemPres) and this CartItemPres object is added to the top of the UI
	 * table of items. The cart in the ShoppingCartSubsystem is also updated
	 * with a new CartItem, obtained from the CartItemPres
	 */
	private class AddToCartHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			/* First create a CartItemPres from the user input */
			int quant = INIT_QUANT_REQUESTED; // = 1
			double unitPrice = Double
					.parseDouble(BrowseSelectData.INSTANCE.getSelectedProduct().unitPriceProperty().get());
			String name = BrowseSelectData.INSTANCE.getSelectedProduct().nameProperty().get();

			CartItemPres cartPres = BrowseSelectData.INSTANCE.cartItemPresFromData(name, unitPrice, quant);

			/* Then add it to the cart in the ShoppingCartSubsystem */
			BrowseSelectData.INSTANCE.addToCart(cartPres);

			shoppingCartWindow = ShoppingCartWindow.INSTANCE;
			shoppingCartWindow.setData(BrowseSelectData.INSTANCE.getCartData());
			shoppingCartWindow.setPrimaryStage(primaryStage);
			ShoppingCartWindow.INSTANCE.clearMessages();
			shoppingCartWindow.show();
			productDetailsWindow.hide();
		}
	}

	public void runShoppingCartRules() {

	}

	public void runQuantityRules(Product product, String quantityRequested) throws RuleException, BusinessException {
		controller.runQuantityRules(product, quantityRequested);
	}

	public BackToProductListHandler getBackToProductListHandler() {
		return new BackToProductListHandler();
	}

	public AddToCartHandler getAddToCartHandler() {
		return new AddToCartHandler();
	}

	//////////// Handlers for ShoppingCartWindow

	private class CartContinueHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			try {
				CatalogListWindow catalogwindow = CatalogListWindow.getInstance(primaryStage,
						FXCollections.observableList(BrowseSelectData.INSTANCE.getCatalogList()));
				catalogwindow.clearMessages();
				ShoppingCartWindow.INSTANCE.hide();
				catalogwindow.setTableAccessByRow();
				catalogwindow.show();
				
			} catch (BackendException e) {
				shoppingCartWindow.displayError("Database is unavailable. Please try again later.");
			}
		}
	}

	public CartContinueHandler getCartContinueHandler() {
		return new CartContinueHandler();
	}

	private class SaveCartHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent evt) {

			shoppingCartWindow.clearMessages();
			try {

				boolean isLoggedin = CacheReader.readLoggedIn();
				if (!isLoggedin) {

					// redirect to login

					// stay in shopping cart window after logged in
					LoginUIControl loginControl = new LoginUIControl(ShoppingCartWindow.INSTANCE,
							ShoppingCartWindow.INSTANCE.getPrimaryStage());

					loginControl.startLogin();
				}

				/**
				 * Sets the latest version of cartData to the
				 * ShoppingCartSubsystem
				 */
				BrowseSelectData.INSTANCE.updateShoppingCart();
				ShoppingCartSubsystem shopCartSs = BrowseSelectData.INSTANCE.obtainCurrentShoppingCartSubsystem();

				controller.updateShoppingCartItems(shopCartSs, shopCartSs.getCartItems());

				shopCartSs.saveLiveCart();
				int numbItems = shoppingCartWindow.getCartItems().size();
				// save successfully
				shoppingCartWindow.displayInfo(numbItems + " Items successfully saved");
			} catch (BackendException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				shoppingCartWindow.displayError("You need to successfully implement this handler.");
			}

		}
	}

	public SaveCartHandler getSaveCartHandler() {
		return new SaveCartHandler();
	}

	/**
	 * Updates the view of cart items in the UI
	 */
	public void updateCartItems(ObservableList<CartItemPres> items) {
		if (shoppingCartWindow != null) {
			shoppingCartWindow.setData(items);
			TableUtil.refreshTable(shoppingCartWindow.getTable(),
					BrowseSelectData.INSTANCE.getShoppingCartSynchronizer());
		}
	}

	public void handleEditedQuantity(CartItemPres cartItemPres, String quantRequested, TableView<CartItemPres> table)
			throws RuleException, BusinessException {
		// First, check quantity requested is valid
		// run quantity rules
		Product product = BrowseSelectData.INSTANCE.getProductForProductName(cartItemPres.getCartItem().getItemName());
		runQuantityRules(product, quantRequested);

		// Second, set the edited value into CartItemPres
		cartItemPres.setQuantity(new SimpleStringProperty(quantRequested));

		// Third, sets updated item into cart item list stored in BrowseSelect
		// Data, and
		// forces a refresh of the TableView
		TableUtil.refreshTable(table, BrowseSelectData.INSTANCE.getShoppingCartSynchronizer());

		// Fourth, update the live cart in the current Shopping Cart Subsystem
		BrowseSelectData.INSTANCE.updateShoppingCart();
	}

}
