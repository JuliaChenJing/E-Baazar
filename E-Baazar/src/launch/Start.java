package launch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.control.BrowseSelectUIControl;
import presentation.control.Callback;
import presentation.control.LoginUIControl;
import presentation.control.ManageProductsUIControl;
import presentation.control.ViewOrdersUIControl;

public class Start extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	private Stage primaryStage; 
	//to record the information like" you have successfully logged out"
	private Text messageBar = new Text();
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		BrowseSelectUIControl.INSTANCE.setPrimaryStage(primaryStage, getReturnMessageCallback());
		
		//used for Back button
		ViewOrdersUIControl.INSTANCE.setPrimaryStage(primaryStage, getReturnMessageCallback());
		
		//used for Back button
		ManageProductsUIControl.INSTANCE.setPrimaryStage(primaryStage, getReturnMessageCallback());
		primaryStage.setTitle("E-Bazaar Welcome Page");
		VBox topContainer = new VBox();		
		//create components
		HBox embeddedText = createLabelBox();
		HBox messageBarBox = createMessageBarBox();
		MenuBar menuBar = createMenuBar();
		//add components to container
		topContainer.getChildren().add(menuBar);
		topContainer.getChildren().add(embeddedText);
		topContainer.getChildren().add(messageBarBox);

		//place into scene and into stage
		primaryStage.setScene(new Scene(topContainer, 500, 200));
		primaryStage.show();
	}
	
	//store  "E-Bazaar" as the title
	private HBox createLabelBox() {
		Text label = new Text("E-Bazaar");
		label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 60));
		label.setFill(Color.DARKRED);
		HBox labelBox = new HBox(10);
		labelBox.setAlignment(Pos.CENTER);
		labelBox.getChildren().add(label);
		return labelBox;	
	}
	
	//store message like" you have successfully logged out"
	private HBox createMessageBarBox() {
		HBox box = new HBox(10);
		box.setAlignment(Pos.BASELINE_LEFT);
		box.getChildren().add(messageBar);
		return box;	
	}
	
	private MenuBar createMenuBar() {
		MenuBar retval = new MenuBar();
		
		//create menus to put into menu bar
		
		//login menu
		Menu loginMenu = new Menu("Login");
		loginMenu.getItems().addAll(login(), logout());
		
		//customer menu
		Menu custMenu = new Menu("Customer");
		custMenu.getItems().addAll(onlinePurchase(), retrieveCart(), reviewOrders(), exitApp());
		
		//admin menu
		Menu adminMenu = new Menu("Administrator");
		adminMenu.getItems().addAll(maintainCatalogs(), maintainProducts());
		
		//add menus to menu bar
		retval.getMenus().addAll(loginMenu, custMenu, adminMenu);
		return retval;
	
	}
	
	private MenuItem login() {
		messageBar.setText("");
		MenuItem retval = new MenuItem("Sign in");
		//has to create an object from LoginUIControl to call the event handler ,following MVC pattern
		LoginUIControl login = new LoginUIControl(primaryStage, primaryStage, new ReturnMessageCallback());
		retval.setOnAction(login.getShowLoginHandler());
		return retval;
	}
	
	private MenuItem logout() {
		messageBar.setText("");
		MenuItem retval = new MenuItem("Sign out");
		LoginUIControl login = new LoginUIControl(primaryStage, primaryStage, new ReturnMessageCallback());
		retval.setOnAction(login.getLogoutHandler());
		return retval;
	}
	
	//returns the MenuItem button for starting the browse and select process
	private MenuItem onlinePurchase() {
		messageBar.setText("");
		MenuItem retval = new MenuItem("Online Purchase");
		retval.setOnAction(BrowseSelectUIControl.INSTANCE.getOnlinePurchaseHandler());
		return retval;
	}
	
	//retrieve the previous shopping cart to check the previous products saved in the shopping cart
	// retrieveCart() is added into customer menu bar : custMenu.getItems().addAll(onlinePurchase(), retrieveCart(), reviewOrders(), exitApp());
	private MenuItem retrieveCart() {
		messageBar.setText("");
		MenuItem retval = new MenuItem("Retrieve Saved Cart");//set the menu name
		//from 1.1 UI to 1.2 UIController
		retval.setOnAction(BrowseSelectUIControl.INSTANCE.getRetrieveSavedCartHandler());
		return retval;//return type is a MenuItem 
	}
	
	private MenuItem reviewOrders() {
		messageBar.setText("");
		MenuItem retval = new MenuItem("Review Orders");
		//from 1.1 UI to 1.2 UIController
		retval.setOnAction(ViewOrdersUIControl.INSTANCE.getViewOrdersHandler());	
		return retval;
	}
	
	private MenuItem exitApp() {
		MenuItem retval = new MenuItem("Exit");
		retval.setOnAction(evt -> Platform.exit());
		return retval;
	}
	
	//movement handled only by the administrator
	private MenuItem maintainCatalogs() {
		messageBar.setText("");
		MenuItem retval = new MenuItem("Maintain Catalogs");
		retval.setOnAction(ManageProductsUIControl.INSTANCE.getMaintainCatalogsHandler());
		return retval;
	}
	private MenuItem maintainProducts() {
		messageBar.setText("");
		MenuItem retval = new MenuItem("Maintain Products");
		retval.setOnAction(ManageProductsUIControl.INSTANCE.getMaintainProductsHandler());
		return retval;
	} 
	
	public Callback getReturnMessageCallback() {
		return new ReturnMessageCallback();
	}
	
	
	private class ReturnMessageCallback implements Callback {
		public void doUpdate() {
			//do nothing
		}
		public Text getMessageBar() {
			return messageBar;
		}
		
	}



	
}
