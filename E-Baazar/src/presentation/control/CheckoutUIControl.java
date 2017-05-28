package presentation.control;

import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import presentation.data.BrowseSelectData;
import presentation.data.CheckoutData;
import presentation.data.CustomerPres;
import presentation.data.ErrorMessages;
import presentation.gui.CatalogListWindow;
import presentation.gui.FinalOrderWindow;
import presentation.gui.OrderCompleteWindow;
import presentation.gui.PaymentWindow;
import presentation.gui.ShippingBillingWindow;
import presentation.gui.ShoppingCartWindow;
import presentation.gui.TermsWindow;
import presentation.util.CacheReader;
//import rulesengine.OperatingException;
//import rulesengine.ReteWrapper;
//import rulesengine.ValidationException;
//import system.rulescore.rulesengine.*;
//import system.rulescore.rulesupport.*;
//import system.rulescore.util.*;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.usecasecontrol.CheckoutController;

public enum CheckoutUIControl {
	INSTANCE;
	private CheckoutController controller = new CheckoutController();
	private static final Logger LOG = Logger.getLogger(CheckoutUIControl.class.getPackage().getName());
	// Windows managed by CheckoutUIControl
	ShippingBillingWindow shippingBillingWindow;
	PaymentWindow paymentWindow;
	TermsWindow termsWindow;
	FinalOrderWindow finalOrderWindow;
	OrderCompleteWindow orderCompleteWindow;

	public ShippingBillingWindow getShippingBillingWindow() {
		return shippingBillingWindow;
	}

	// handler for ShoppingCartWindow proceeding to checkout
	private class ProceedFromCartToShipBill implements EventHandler<ActionEvent>, Callback {
		CheckoutData data = CheckoutData.INSTANCE;

		public void doUpdate() {
			shippingBillingWindow = new ShippingBillingWindow();
			CustomerProfile custProfile = data.getCustomerProfile(CacheReader.readCustomer());
			Address defaultShipAddress = data.getDefaultShippingData();
			Address defaultBillAddress = data.getDefaultBillingData();

			shippingBillingWindow.setShippingAddress(custProfile.getFirstName() + " " + custProfile.getLastName(),
					defaultShipAddress.getStreet(), defaultShipAddress.getCity(), defaultShipAddress.getState(),
					defaultShipAddress.getZip());
			shippingBillingWindow.setBillingAddress(custProfile.getFirstName() + " " + custProfile.getLastName(),
					defaultBillAddress.getStreet(), defaultBillAddress.getCity(), defaultBillAddress.getState(),
					defaultBillAddress.getZip());
			shippingBillingWindow.show();
		}

		@Override
		public void handle(ActionEvent evt) {

			boolean rulesOk = true;
			/* check that cart is not empty before going to next screen */

			try {
				boolean isLoggedin = CacheReader.readLoggedIn();
				if (!isLoggedin) {

					// redirect to login

					// stay in shopping cart window after logged in
					LoginUIControl loginControl = new LoginUIControl(ShoppingCartWindow.INSTANCE,
							ShoppingCartWindow.INSTANCE);

					// jump to shippingBillingWindow after logged in
					// LoginUIControl loginControl = new
					// LoginUIControl(shippingBillingWindow,
					// ShoppingCartWindow.INSTANCE,this);
					loginControl.startLogin();
				}

				// already logged in
				controller.runShoppingCartRules(CacheReader.readCustomer().getShoppingCart());

			} catch (RuleException e) {
				ShoppingCartWindow.INSTANCE.displayError(e.getMessage());
				rulesOk = false;
			} catch (BusinessException e) {
				ShoppingCartWindow.INSTANCE.displayError(ErrorMessages.DATABASE_ERROR);
				rulesOk = false;
			}

			if (rulesOk) {

				ShoppingCartWindow.INSTANCE.hide();
				doUpdate();
			}

		}

		@Override
		public Text getMessageBar() {
			return ShoppingCartWindow.INSTANCE.getMessageBar();
		}
	}

	public class SetShippingInSelect implements Consumer<CustomerPres> {

		@Override
		public void accept(CustomerPres cust) {
			shippingBillingWindow.setShippingAddress(cust.fullNameProperty().get(), cust.streetProperty().get(),
					cust.cityProperty().get(), cust.stateProperty().get(), cust.zipProperty().get());

		}

	}

	public SetShippingInSelect getSetShippingInSelect() {
		return new SetShippingInSelect();
	}

	public class SetBillingInSelect implements Consumer<CustomerPres> {
		@Override
		public void accept(CustomerPres cust) {
			shippingBillingWindow.setBillingAddress(cust.fullNameProperty().get(), cust.streetProperty().get(),
					cust.cityProperty().get(), cust.stateProperty().get(), cust.zipProperty().get());

		}
	}

	public SetBillingInSelect getSetBillingInSelect() {
		return new SetBillingInSelect();
	}

	public ProceedFromCartToShipBill getProceedFromCartToShipBill() {
		return new ProceedFromCartToShipBill();
	}

	// handlers for ShippingBillingWindow
	private class BackToShoppingCartHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.show();
			shippingBillingWindow.clearMessages();
			shippingBillingWindow.hide();
		}
	}

	public BackToShoppingCartHandler getBackToShoppingCartHandler() {
		return new BackToShoppingCartHandler();
	}

	// the event handler for check out button , address rules needed to be check
	private class ProceedToPaymentHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			shippingBillingWindow.clearMessages();
			boolean rulesOk = true;
			Address cleansedShipAddress = null;
			Address cleansedBillAddress = null;
			CustomerSubsystem cust = CacheReader.readCustomer();
			if (shippingBillingWindow.getSaveShipAddr()) {
				try {
					// run address rules for shipping address
					cleansedShipAddress = controller.runAddressRules(cust, shippingBillingWindow.getShippingAddress());
				} catch (RuleException e) {
					rulesOk = false;
					shippingBillingWindow.displayError("Shipping address error: " + e.getMessage());
				} catch (BusinessException e) {
					rulesOk = false;
					shippingBillingWindow.displayError(ErrorMessages.GENERAL_ERR_MSG + ": Message: " + e.getMessage());
				}
			}
			if (rulesOk) {
				if (shippingBillingWindow.getSaveBillAddr()) {
					try {
						// run address rules for billing address
						cleansedBillAddress = controller.runAddressRules(cust,
								shippingBillingWindow.getBillingAddress());
					} catch (RuleException e) {
						rulesOk = false;
						shippingBillingWindow.displayError("Billing address error: " + e.getMessage());
					} catch (BusinessException e) {
						rulesOk = false;
						shippingBillingWindow
								.displayError(ErrorMessages.GENERAL_ERR_MSG + ": Message: " + e.getMessage());
					}
				}
			}
			if (rulesOk) {

				LOG.info("Address Rules passed!");
				if (cleansedShipAddress != null) {
					try {
						controller.saveNewAddress(cust, cleansedShipAddress);
					} catch (BackendException e) {
						shippingBillingWindow
								.displayError("New shipping address not saved. Message: " + e.getMessage());
					}
				}
				if (cleansedBillAddress != null) {
					try {
						controller.saveNewAddress(cust, cleansedBillAddress);
					} catch (BackendException e) {
						shippingBillingWindow.displayError("New billing address not saved. Message: " + e.getMessage());
					}
				}
				paymentWindow = new PaymentWindow();
				paymentWindow.show();
				shippingBillingWindow.hide();
			}
		}
	}

	public ProceedToPaymentHandler getProceedToPaymentHandler() {
		return new ProceedToPaymentHandler();
	}

	public class SaveShipChangeListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observed, Boolean oldval, Boolean newval) {
			shippingBillingWindow.displayInfo("");
		}
	}

	public class SaveBillChangeListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observed, Boolean oldval, Boolean newval) {
			shippingBillingWindow.displayInfo("");

		}
	}

	public SaveShipChangeListener getSaveShipChangeListener() {
		return new SaveShipChangeListener();
	}

	public SaveBillChangeListener getSaveBillChangeListener() {
		return new SaveBillChangeListener();
	}

	// handlers for PaymentWindow
	private class BackToShipBillWindow implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			paymentWindow.clearMessages();
			shippingBillingWindow.show();
			paymentWindow.hide();
		}
	}

	public BackToShipBillWindow getBackToShipBillWindow() {
		return new BackToShipBillWindow();
	}

	private class BackToCartHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			paymentWindow.clearMessages();
			ShoppingCartWindow.INSTANCE.show();
			paymentWindow.hide();
		}
	}

	public BackToCartHandler getBackToCartHandler() {
		return new BackToCartHandler();
	}

	// the event handler for the checkout button , needs to check the credit
	// card information
	private class ProceedToTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			try {
				// the billing address and credit card information needed
				controller.runPaymentRules(shippingBillingWindow.getBillingAddress(),
						paymentWindow.getCreditCardFromWindow());
				paymentWindow.clearMessages();
				paymentWindow.hide();

				// ask the customer to agree with the terms
				termsWindow = new TermsWindow();
				termsWindow.show();
			} catch (RuleException e) {
				// show information like "payment information error: all payment
				// fields are required"
				paymentWindow.displayError(e.getMessage());
			} catch (BusinessException e) {
				paymentWindow.displayError(ErrorMessages.DATABASE_ERROR);
			}
		}
	}

	public ProceedToTermsHandler getProceedToTermsHandler() {
		return new ProceedToTermsHandler();
	}

	// handlers for TermsWindow

	private class ToCartFromTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			termsWindow.hide();
			ShoppingCartWindow.INSTANCE.show();
		}
	}

	public ToCartFromTermsHandler getToCartFromTermsHandler() {
		return new ToCartFromTermsHandler();
	}

	private class AcceptTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			finalOrderWindow = new FinalOrderWindow();
			finalOrderWindow.setData(BrowseSelectData.INSTANCE.getCartData());
			finalOrderWindow.show();
			termsWindow.hide();
		}
	}

	public AcceptTermsHandler getAcceptTermsHandler() {
		return new AcceptTermsHandler();
	}

	// handlers for submit order in FinalOrderWindow
	private class SubmitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			CheckoutController checkoutController=new CheckoutController();
			try {
				checkoutController.submitFinalOrder();
			} catch (BackendException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			orderCompleteWindow = new OrderCompleteWindow();
			orderCompleteWindow.show();
			finalOrderWindow.clearMessages();
			finalOrderWindow.hide();
		}

	}

	public SubmitHandler getSubmitHandler() {
		return new SubmitHandler();
	}

	private class CancelOrderHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			finalOrderWindow.displayInfo("Order Cancelled");
		}
	}

	public CancelOrderHandler getCancelOrderHandler() {
		return new CancelOrderHandler();
	}

	private class ToShoppingCartFromFinalOrderHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.show();
			finalOrderWindow.hide();
			finalOrderWindow.clearMessages();
		}
	}

	public ToShoppingCartFromFinalOrderHandler getToShoppingCartFromFinalOrderHandler() {
		return new ToShoppingCartFromFinalOrderHandler();
	}

	// handlers for OrderCompleteWindow

	private class ContinueFromOrderCompleteHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			CatalogListWindow.getInstance().show();
			orderCompleteWindow.hide();
		}
	}

	public ContinueFromOrderCompleteHandler getContinueFromOrderCompleteHandler() {
		return new ContinueFromOrderCompleteHandler();
	}
}
