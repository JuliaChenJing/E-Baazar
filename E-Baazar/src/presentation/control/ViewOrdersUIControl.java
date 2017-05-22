package presentation.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
		public void doUpdate() {
			// TODO Auto-generated method stub
			primaryStage.hide();
			ordersWindow.setData(FXCollections.observableList(ViewOrdersData.INSTANCE.getOrders()));
			ordersWindow.show();
			
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
				orderDetailWindow.setData(FXCollections.observableList(selectedOrder.getOrderItemsPres()));
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
