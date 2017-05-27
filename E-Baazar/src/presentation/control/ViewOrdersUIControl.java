package presentation.control;

import java.util.List;

import business.exceptions.BackendException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.data.OrderItemPres;
import presentation.data.OrderPres;
import presentation.data.ViewOrdersData;
import presentation.gui.OrderDetailWindow;
import presentation.gui.OrdersWindow;
import presentation.util.CacheReader;

public enum ViewOrdersUIControl {
	INSTANCE;

	private OrdersWindow ordersWindow;
	private OrderDetailWindow orderDetailWindow;
	private Stage primaryStage;
	private Callback startScreenCallback;

	public void setPrimaryStage(Stage ps, Callback returnMessage) {
		primaryStage = ps;
		startScreenCallback = returnMessage;
	}

	private class ViewOrdersHandler implements EventHandler<ActionEvent>, Callback  {
		@Override
		public void doUpdate() {
		
			primaryStage.hide();
			//from 1.2 UIController to 1.3 UIData
			List<OrderPres> orders = ViewOrdersData.INSTANCE.getOrders();
			try {
				//do something to the data so it could be shown like a table
				ObservableList<OrderPres> data = FXCollections.observableList(orders);  
				ordersWindow.setData(data);//
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ordersWindow.show();
			
		}
		@Override
		public void handle(ActionEvent evt) {

			boolean isLoggedin = CacheReader.readLoggedIn();
			ordersWindow = new OrdersWindow(primaryStage);
			if (!isLoggedin) {

				// redirect to login
				primaryStage.hide();

				LoginUIControl loginControl = new LoginUIControl(ordersWindow , primaryStage,this);
				loginControl.startLogin();
				isLoggedin = CacheReader.readLoggedIn();
				if(isLoggedin)
					doUpdate();
			}
			else
				doUpdate();
		}
			
		


		@Override
		public Text getMessageBar() {
			return ordersWindow.getMessageBar();
		}

	}

	public ViewOrdersHandler getViewOrdersHandler() {
		return new ViewOrdersHandler();
	}

	// OrdersWindow
	private class ViewOrderDetailsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			TableView<OrderPres> table = ordersWindow.getTable();
			OrderPres selectedOrder = table.getSelectionModel().getSelectedItem();
			if (selectedOrder == null) {
				ordersWindow.displayError("Please select a row.");
			} else {
				ordersWindow.clearMessages();
				orderDetailWindow = new OrderDetailWindow();
				ObservableList<OrderItemPres> orderItems = selectedOrder.getOrderItemsPres();
				orderDetailWindow.setData(FXCollections.observableList(orderItems));
				ordersWindow.hide();
				orderDetailWindow.show();
			}
		}
	}

	public ViewOrderDetailsHandler getViewOrderDetailsHandler() {
		return new ViewOrderDetailsHandler();
	}

	// OrderDetailWindow
	private class OrderDetailsOkHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ordersWindow.show();
			orderDetailWindow.hide();
		}
	}

	public OrderDetailsOkHandler getOrderDetailsOkHandler() {
		return new OrderDetailsOkHandler();
	}

	private class CancelHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			ordersWindow.hide();
			startScreenCallback.clearMessages();
			primaryStage.show();

		}
	}

	public CancelHandler getCancelHandler() {
		return new CancelHandler();
	}
}
