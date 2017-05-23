package presentation.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import presentation.control.BrowseSelectUIControl;
import presentation.data.BrowseSelectData;
import presentation.data.CartItemPres;
import presentation.data.ErrorMessages;
import presentation.util.GuiUtils;
import presentation.util.TableUtil;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Product;

/**
 * This interface provides support for ShoppingCartWindow and
 * FinalOrderWindow. The screens contain exactly the same table,
 * so the table is set up for both of these screens using a default
 * interface method setUpTable().
 */
public interface DefaultShoppingCartWindow extends MessageableWindow, ModifiableTableWindow {
	
	//public GridPane setUpGridPane();
	public TableView<CartItemPres> getTable();
	public Text getTotal();
	public Label getTotalLabel();
	public TableColumn<CartItemPres, String> getQuantityCol();
	public void setQuantityCol(TableColumn<CartItemPres, String> quantCol);
	public HBox setUpButtons();
	public void setBtnBox(HBox box);
	public HBox getBtnBox();
	public String getTitleString();
	
	@SuppressWarnings("unchecked")
	default public void setUpTable() {
		getTable().setEditable(true);
        
        //create columns
        TableColumn<CartItemPres, String> itemNameCol 
      
        	= TableUtil.makeTableColumn(new CartItemPres(), "Item Name", "itemNameProperty", 200);
                       //makeTableColumn(T underlyingClass, String colHeader, String nameOfProperty, int minWidth)
       
        //set quantity column to be edible. 
        //It needs to run the quantity rule
	    setQuantityCol(TableUtil.makeEditableTableColumn(getTable(), new CartItemPres(), 
	    		"Quantity",         "quantityProperty",    80));  
	                            // makeEditableTableColumn(TableView<T> table, T underlyingClass,
	            //String colHeader, String nameOfProperty, int minWidth)
	    
	    //set the event handler when editing the quantity
		/*  public final void setOnEditCommit(EventHandler<CellEditEvent<S,T>> value) {
		          onEditCommitProperty().set(value);
		    }
		 */

	    
       getQuantityCol().setOnEditCommit(t -> {
		   CartItemPres cartItemPres = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   String quantRequested = t.getNewValue();
		   
		   try {
			   BrowseSelectUIControl.INSTANCE.handleEditedQuantity(
				    cartItemPres, quantRequested, getTable());
			   clearMessages();
			   //after editing the quantity, quantity needs to be set to the entity
			   cartItemPres.setQuantity(new SimpleStringProperty(quantRequested));
			   //total price of all shopping cart  should be calculated again
			   double sumTotal = GuiUtils.computeTotalInTable(getTable());
			   //and set the new total to the cart
			   setTotalInCart(GuiUtils.formatPrice(sumTotal));
		   } catch(RuleException e) {
			   //if there is something wrong ,cancel the edit and return to the previous value
			   getQuantityCol().getCellFactory().call(getQuantityCol()).cancelEdit();		
			   displayError(e.getMessage());
		   } catch(BusinessException e) {
			   getQuantityCol().getCellFactory().call(getQuantityCol()).cancelEdit();
			   displayError(ErrorMessages.GENERAL_ERR_MSG + ": Message: " + e.getMessage());
		   }
	     }); 

        TableColumn<CartItemPres, String> unitPriceCol 
	        = TableUtil.makeTableColumn(new CartItemPres(), "Unit Price", "priceProperty", 80);
        TableColumn<CartItemPres, String> totalPriceCol 
        	= TableUtil.makeTableColumn(new CartItemPres(), "Total Price", "totalPriceProperty", 80);

		getTable().getColumns().addAll(itemNameCol, getQuantityCol(), unitPriceCol, totalPriceCol);
		
		//make sure row selection is enabled after any mouse click
		getTable().setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            TableUtil.selectByRow(getTable());
	        }
	    });
	}
	
	//the default method in an interface
	//returns an HBox with all the settings settled
	default public HBox setUpTopLabel() {
		Label label = new Label(getTitleString());
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	//the default method in DefaltShoppingCart interface
	//returns a Scene as the table in the shopping cart window
	default public Scene createScene() {
		
		//set up top label
		HBox labelHbox = setUpTopLabel();////returns an HBox with all the settings settled
		
		//set up table
		setUpTable();
        
		//set up buttons
		setBtnBox(setUpButtons());
		
		//assemble all in a GridPane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10); 
		grid.setHgap(10);
		
		grid.add(labelHbox, 0, 1);
		grid.add(getTable(), 0, 2);
		HBox totalBox = new HBox(10);
		totalBox.setAlignment(Pos.BOTTOM_RIGHT);
		totalBox.getChildren().add(getTotalLabel());
		totalBox.getChildren().add(getTotal());
		grid.add(totalBox, 0,3);
		grid.add(getMessageBar(), 0, 5);	
		grid.add(getBtnBox(),0,6);
		grid.add(new HBox(10), 0, 7);
		
		//set in scene
        Scene scene = new Scene(grid, 
        	GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);  
		
		
		//Make sure that mouse click outside of table will also restore row selection
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	clearMessages();
	        	setTableAccessByRow();
	        }
	    });
		return scene;
	}
	
	//sets data from outside into shopping cart
	default public void setData(ObservableList<CartItemPres> items) {
		
		getTable().setItems(items);
		setTotalInCart(GuiUtils.formatPrice(GuiUtils.computeTotalInTable(getTable())));
		
		//turns on cell selection 
		TableView.TableViewSelectionModel<CartItemPres> selModel 
			= TableUtil.selectByCell(getTable());
		
		//selects the quantity cell so that it can be edited easily
		selModel.select(0, getQuantityCol());
	}
	
	
	
	default public void setTotalInCart(String amt) {
		getTotal().setText(amt);
	}
	
	
}
