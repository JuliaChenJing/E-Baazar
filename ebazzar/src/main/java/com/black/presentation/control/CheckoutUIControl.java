package com.black.presentation.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import com.black.business.exceptions.BackendException;
import com.black.business.exceptions.BusinessException;
import com.black.business.externalinterfaces.Address;
import com.black.business.usecasecontrol.BrowseAndSelectController;
import com.black.business.usecasecontrol.CheckoutController;
import com.black.presentation.data.CartItemData;
import com.black.presentation.data.CartItemPres;
import com.black.presentation.data.CheckoutData;
import com.black.presentation.data.CheckoutModel;
import com.black.presentation.data.ViewOrdersData;
import com.black.presentation.util.Util;

/**
 * Created by zaid on 5/23/2017.
 */



@RestController
public class CheckoutUIControl {
    @Autowired
    ViewOrdersData viewOrdersData;

    @Autowired
    CheckoutData checkoutData;

    @Autowired
    CheckoutController checkoutController;

    @Autowired
    BrowseAndSelectController browseAndSelectController;

    @RequestMapping(value = { "/mycheckout" }, method = RequestMethod.GET)
    public String checkoutCart(ModelMap modelMap, HttpServletRequest request) throws BackendException{
        //Update live shopping cart with session data
        List<CartItemData> cartDataItems = (List<CartItemData>)request.getSession().getAttribute("cartDataItems");
        List<CartItemPres> cartItemPresList = cartDataItems
                .stream()
                .map(d -> {CartItemPres p = new CartItemPres(); p.setCartItem(d); return p;})
                .collect(Collectors.toList());

        browseAndSelectController.updateShoppingCartItems(Util.cartItemPresToCartItemList(cartItemPresList));

        try{
            //check rules
            checkoutController.runShoppingCartRules();
        }
        catch(BusinessException e){
            request.setAttribute("exception", e.getMessage());
            return "shopping_cart";
        }


        Address defaultShipAddress = checkoutData.getDefaultShippingData();
        Address defaultBillAddress = checkoutData.getDefaultBillingData();

        CheckoutModel checkoutModel = new CheckoutModel();
        checkoutModel.setShippingAddress(defaultShipAddress);
        checkoutModel.setBillingAddress(defaultBillAddress);

        modelMap.addAttribute("checkoutmodel", checkoutModel);
        modelMap.addAttribute("allShipAddresses", checkoutData.getCustomerShipAddresses());
        modelMap.addAttribute("allBillAddresses", checkoutData.getCustomerBillAddresses());
        return "checkout_address";
    }

    @RequestMapping(value = {"/mypaymentinfo"}, method= RequestMethod.POST)
    public String getPaymentInfo(ModelMap modelMap, @ModelAttribute("checkoutmodel") CheckoutModel checkoutModel, HttpServletRequest request){

        try{
            //check rules
            checkoutController.runAddressRules(checkoutModel.getShippingAddress());
            checkoutController.runAddressRules(checkoutModel.getBillingAddress());
        }
        catch(BusinessException e){
            request.setAttribute("exception", e.getMessage());
            return "checkout_address";
        }


        //Update live shopping cart address, should be completed in Checkout Controller
        checkoutController.setShippingAddress(checkoutModel.getShippingAddress());
        checkoutController.setBillingAddress(checkoutModel.getBillingAddress());

        //save shipping & billing info here
        checkoutModel.setCreditCard(checkoutData.getDefaultPaymentInfo());
        request.getSession().setAttribute("checkoutmodel", checkoutModel);
        return "getPaymentInfo";
    }

    @RequestMapping(value={"/myagreement"},method=RequestMethod.POST)
    public String getAgreement(HttpServletRequest request, @ModelAttribute("checkoutmodel") CheckoutModel checkoutModel){

        CheckoutModel checkoutModelInSession = (CheckoutModel) request.getSession().getAttribute("checkoutmodel");
        try{
            //For verify
            checkoutController.setPaymentInfo(checkoutModel.getCreditCard());
            checkoutController.verifyCreditCard();

            //check rules
            checkoutController.runPaymentRules(checkoutModelInSession.getBillingAddress(), checkoutModel.getCreditCard());
        }
        catch(BusinessException e){
            request.setAttribute("exception", e.getMessage());
            return "getPaymentInfo";
        }

        checkoutModelInSession.setCreditCard(checkoutModel.getCreditCard());
        return "agreement_form";
    }

    @RequestMapping(value={"/myordersummary"})
    public String summaryOrder(HttpSession session){
        //Calculate total
        double total = 0;
        List<CartItemData> cartDataItems = (List<CartItemData>)session.getAttribute("cartDataItems");
        for (CartItemData cartItemData : cartDataItems) {
            total += cartItemData.getTotalPrice();
        }
        session.setAttribute("total", total);
        return "final_order";
    }

    @RequestMapping(value={"/myplaceorder"})
    public String placeOrder(HttpServletRequest request){
        //Save order here
        try{
            //check rules
            checkoutController.runFinalOrderRules(null);
        }
        catch(BusinessException e){
            request.setAttribute("exception", e.getMessage());
            return "final_order";
        }

        HttpSession session = request.getSession();
        CheckoutModel checkoutModelInSession = (CheckoutModel) session.getAttribute("checkoutmodel");
        try {

            //save order which will read all data from
            checkoutController.submitFinalOrder();

            //Save address
            if(checkoutModelInSession.isSaveShippingAddress()){
                checkoutController.saveNewAddress(checkoutModelInSession.getShippingAddress());
            }

            if(checkoutModelInSession.isSaveBillingAddress()){
                checkoutController.saveNewAddress(checkoutModelInSession.getBillingAddress());
            }

            //refresh order history
            viewOrdersData.refreshAfterSubmit();

            //Clear shopping car
            session.setAttribute("cartDataItems", null);
        } catch (BackendException e) {
            e.printStackTrace();
        }
        catch (BusinessException e) {
            e.printStackTrace();
        }

        return "submit_order";
    }
}

