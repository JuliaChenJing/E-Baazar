package presentation.gui;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.control.BrowseSelectUIControl;
import presentation.control.CheckoutUIControl;
import presentation.control.DeleteCartItemHandler;
import presentation.data.CartItemPres;

//shows the shopping cart of this customer
//only quantity could be edited and the quantity rules should be applied
public class ShoppingCartWindow extends Stage implements DefaultShoppingCartWindow {
	
	//title
	private final static String TITLE_STRING = "Shopping Cart";
	//Singleton ,there will only be one (live) shopping cart window  in one application
	public final static ShoppingCartWindow INSTANCE = new ShoppingCartWindow();
	//table holds the shopping cart items
	private TableView<CartItemPres> table = new TableView<CartItemPres>();
	//shows message like "quantity requested exceeds quantity available"
	private Text messageBar = new Text();
	private HBox btnBox;
	
	private Stage primaryStage;//used when retrieved cart needs to be navigated back
	
	public void setPrimaryStage(Stage stage) {
		this.primaryStage = stage;
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	//shows the total price of the products in shopping cart in the right down corner  of the table
	private Label totalLabel = new Label("Total:");
	private Text total = new Text();
	
	
	//editable column
	private TableColumn<CartItemPres, String> quantityCol;
	
	public ShoppingCartWindow() {
		setTitle(TITLE_STRING);//set  the title of the window to be "Shopping Cart"
		
		setScene(createScene());// createScene() in DefaultShoppingCart interface
	}
	
	public ObservableList<CartItemPres> getCartItems() {
		return table.getItems();
	}
	
	//buttons :name ,location and acton 
	public HBox setUpButtons() {
		// check out button
		Button proceedButton = new Button("Proceed to Checkout");
		Button continueButton = new Button("Continue Shopping");
		Button saveButton = new Button("Save Cart");
		Button deleteButton = new Button("Delete Selected");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(proceedButton);
		btnBox.getChildren().add(continueButton);
		btnBox.getChildren().add(saveButton);
		btnBox.getChildren().add(deleteButton);
		//Shop cart deletions are managed by the GUI
		deleteButton.setOnAction(new DeleteCartItemHandler(this));
		continueButton.setOnAction(BrowseSelectUIControl.INSTANCE.getCartContinueHandler());
		saveButton.setOnAction(BrowseSelectUIControl.INSTANCE.getSaveCartHandler());
		// event handler of check out button
		proceedButton.setOnAction(CheckoutUIControl.INSTANCE.getProceedFromCartToShipBill());
		return btnBox;
	}
	
	
	
	//getters and setters in ShoppingCartWindow
	public TableView<CartItemPres> getTable() {
		return table;
	}
	public Text getTotal() {
		return total;
	}
	public Label getTotalLabel() {
		return totalLabel;
	}
	
	public String getTitleString() {
		return TITLE_STRING;
	}
	public void setBtnBox(HBox btnBox) {
		this.btnBox = btnBox;
	}
	
	@Override
	public Text getMessageBar() {
		return messageBar;
	}
	@Override
	public TableColumn<CartItemPres, String> getQuantityCol() {
		return quantityCol;
	}
	@Override
	public void setQuantityCol(TableColumn<CartItemPres, String> quantCol) {
		quantityCol = quantCol;
		
	}
	@Override
	public HBox getBtnBox() {
		return btnBox;
	}
	
	

	
}
