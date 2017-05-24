package com.black.presentation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


import com.black.business.exceptions.BackendException;
import com.black.business.exceptions.EbazRuntimeException;
import com.black.business.externalinterfaces.CartItem;
import com.black.business.externalinterfaces.Catalog;
import com.black.business.externalinterfaces.Order;
import com.black.business.externalinterfaces.OrderItem;
import com.black.business.externalinterfaces.Product;
import com.black.business.externalinterfaces.ShoppingCart;
import com.black.business.ordersubsystem.OrderSubsystemFacade;
import com.black.business.productsubsystem.ProductSubsystemFacade;
import com.black.business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import com.black.business.util.Pair;
import com.black.business.util.TwoKeyHashMap;
import com.black.presentation.data.CartItemPres;
import com.black.presentation.data.CartItemData;
import com.black.presentation.data.CatalogPres;
import com.black.presentation.data.OrderPres;
import com.black.presentation.data.ProductPres;

public class Util {
private final static String REL_RULES_PATH = "business/rulefiles";
	
	public static BufferedReader pathToRules(ClassLoader classLoader, String filename) throws IOException {
	    URL url = classLoader.getResource(REL_RULES_PATH + "/" + filename);
	    BufferedReader buf = new BufferedReader((new InputStreamReader(url.openStream())));
	    return buf;
	}
	
	public static final String DATE_PATTERN = "MM/dd/yyyy"; 
	public static LocalDate localDateForString(String date) {  //pattern: "MM/dd/yyyy"
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
	}
	
	public static String localDateAsString(LocalDate date) {  //pattern: "MM/dd/yyyy"
		return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
	}
	
	
	public static CatalogPres catalogToCatalogPres(Catalog catalog) {
		CatalogPres retVal = new CatalogPres();
		retVal.setCatalog(catalog);
		return retVal;
	}
	
	public static ProductPres productToProductPres(Product product) {
		ProductPres retVal = new ProductPres();
		retVal.setProduct(product);
		return retVal;
	}
	
	public static List<OrderPres> ordersToOrderPresList(List<Order> list) {
		return list.stream()
				   .map(ord -> {OrderPres op = new OrderPres(); op.setOrder(ord); return op;})
				   .collect(Collectors.toList());
		
	}
	
	/**
	 * Throws EbazRuntimeException
	 */
	public static List<CartItem> cartItemPresToCartItemList(List<CartItemPres> list) {
		if(list == null) return null;
		return list.stream()
				.map(pres -> pres.getCartItem())
				.map(cartdata -> ShoppingCartSubsystemFacade.createCartItem(cartdata.getItemName(), 
						String.valueOf(cartdata.getQuantity()), String.valueOf(cartdata.getTotalPrice())))
				.collect(Collectors.toList());
		
	}
	
	public static List<CartItemPres> cartItemsToCartItemPres(List<CartItem> list) {
		if(list == null) return null;
		return list.stream()
				.map(c -> {CartItemData d = new CartItemData(); 
				    d.setItemName(c.getProductName()); 
				    double total = Double.parseDouble(c.getTotalprice());
				    int quantity = Integer.parseInt(c.getQuantity()); 
				    d.setPrice(total/quantity);
				    d.setQuantity(quantity); return d;})
				.map(d -> {CartItemPres p = new CartItemPres(); p.setCartItem(d); return p;})
				.collect(Collectors.toList());
		
	}
	public static double computeTotal(List<CartItem> list) {
		DoubleSummaryStatistics summary 
		  = list.stream().collect(
		       Collectors.summarizingDouble(item -> Double.parseDouble(item.getTotalprice())));
		return summary.getSum();
	}
	
	 
}
