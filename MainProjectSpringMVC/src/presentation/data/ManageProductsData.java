package presentation.data;

import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import presentation.util.Util;
import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import business.usecasecontrol.BrowseAndSelectController;
import business.usecasecontrol.ManageProductsController;


public enum ManageProductsData {
	INSTANCE;
	
	private CatalogPres defaultCatalog = readDefaultCatalogFromDataSource();
	private CatalogPres readDefaultCatalogFromDataSource() {
		return DefaultData.CATALOG_LIST_DATA.get(0);
	}
	public CatalogPres getDefaultCatalog() {
		return defaultCatalog;
	}
	
	private CatalogPres selectedCatalog = defaultCatalog;
	public void setSelectedCatalog(CatalogPres selCatalog) {
		selectedCatalog = selCatalog;
	}
	public CatalogPres getSelectedCatalog() {
		return selectedCatalog;
	}
	//////////// Products List model
	private ObservableMap<CatalogPres, List<ProductPres>> productsMap
	   = FXCollections.observableMap(new HashMap<CatalogPres, List<ProductPres>>());
	
	public ObservableMap<CatalogPres, List<ProductPres>> getProductsMap() {
		return productsMap;
	}
	
	/** Initializes the productsMap */
	
	public ProductPres createProductPres(String catName, int prodId, String name,
			int quantAvail, double unitPrice, LocalDate mfgDate, String description)
	           throws BackendException {
		
		ProductSubsystem pss = new ProductSubsystemFacade();
		Catalog catalog = pss.getCatalogFromName(catName);
		Product newProd = ProductSubsystemFacade.createProduct(catalog, 
				prodId, name, quantAvail, unitPrice, mfgDate, description);			   
		ProductPres prodPres = new ProductPres();
		prodPres.setProduct(newProd);
		return prodPres;
		
	}
	
	public ProductPres createProductPresByName(int prodId) throws BackendException{
		ProductSubsystem pss = new ProductSubsystemFacade();
		Product prod = pss.getProductFromId(prodId);
		ProductPres prodPres = new ProductPres();
		prodPres.setProduct(prod);
		return prodPres;
	}
	
	public CatalogPres createCatalogPres(Integer catId, String catName) {
		Catalog catalog = ProductSubsystemFacade.createCatalog(catId, catName);
		CatalogPres cp = new CatalogPres();
		cp.setCatalog(catalog);
		return cp;
	}
	
	/**
	 * This is the in-memory alternative to getProductsList(cat). If you
	 * believe productMap is up-to-date and contains the requested
	 * CatalogPres as a key, use this. If catPres is not found in the
	 * productsMap, this method will defer to the db-reading method
	 * getProductsList
	 * @see ManageProductsData.getProductsList
	 */
	public ObservableList<ProductPres> getObservableProdListFromProdMap(CatalogPres catPres) throws BackendException {
		if(productsMap.containsKey(catPres)) {
			return FXCollections.observableList(productsMap.get(catPres));
		} else {
			return getProductsList(catPres);
		}
	}
					
	/** 
	 * Makes requested products available to the UI. Used by UI controller
	 * to first populate ManageProductsWindow. Makes a call to database.
	 * Should use getObservableProdListFromProdMap if the required data
	 * is already in memory. 
	 */
	public ObservableList<ProductPres> getProductsList(CatalogPres catPres) throws BackendException {
		List<ProductPres> list =
			BrowseAndSelectController.INSTANCE.getProducts(selectedCatalog.getCatalog())
			    .stream()
			    .map(prod -> Util.productToProductPres(prod))
			    .collect(Collectors.toList());
		//keep productsMap updated
		productsMap.put(catPres, list);
		return FXCollections.observableList(productsMap.get(catPres));
	}
	
	public ProductPres productPresFromData(Catalog c, String name, String date,  //MM/dd/yyyy 
			int numAvail, double price) {
		
		Product product = ProductSubsystemFacade.createProduct(c, name, 
				Util.localDateForString(date), numAvail, price);
		ProductPres prodPres = new ProductPres();
		prodPres.setProduct(product);
		return prodPres;
	}
	
//	public void addToProdList(CatalogPres catPres, ProductPres prodPres) throws BackendException {
//		ObservableList<ProductPres> newProducts =
//		           FXCollections.observableArrayList(prodPres);
//		List<ProductPres> specifiedProds = productsMap.get(catPres);
//		
//		//Place the new item at the bottom of the list; productMap is now updated
//		specifiedProds.addAll(newProducts);
//		
//		//Insert new product into database
//		ManageProductsController.INSTANCE.addNewProduct(prodPres.getProduct());
//	}
	
	/** This method looks for the 0th element of the toBeRemoved list 
	 *  and if found, removes it. In this app, removing more than one product at a time
	 *  is  not supported.
	 */
	public boolean removeFromProductList(CatalogPres cat, ObservableList<ProductPres> toBeRemoved) {
		if(toBeRemoved != null && !toBeRemoved.isEmpty()) {
			boolean result = productsMap.get(cat).remove(toBeRemoved.get(0));
			return result;
		}
		return false;
	}
		
	//////// Catalogs List model
	private ObservableList<CatalogPres> catalogList = readCatalogsFromDataSource();

	/** Initializes the catalogList */
	private ObservableList<CatalogPres> readCatalogsFromDataSource() {
		return FXCollections.observableList(DefaultData.CATALOG_LIST_DATA);
	}

	/** Delivers the already-populated catalogList to the UI */
	public ObservableList<CatalogPres> getCatalogList() throws BackendException {
		List<CatalogPres> list = BrowseAndSelectController.INSTANCE.getCatalogs()
			    .stream()
			    .map(catalog -> Util.catalogToCatalogPres(catalog))
			    .collect(Collectors.toList());
		return FXCollections.observableList(list);
	}

	public CatalogPres catalogPresFromData(int id, String name) {
		Catalog cat = ProductSubsystemFacade.createCatalog(id, name);
		CatalogPres catPres = new CatalogPres();
		catPres.setCatalog(cat);
		return catPres;
	}

//	public void addToCatalogList(CatalogPres catPres) throws BackendException {
//		//Insert new catalog into database
//		Catalog cat = catPres.getCatalog();
//		int newId = ManageProductsController.INSTANCE.addNewCatalog(cat);
//		cat.setId(newId);
//		ObservableList<CatalogPres> newCatalogs = FXCollections
//				.observableArrayList(catPres);
//		
//		// Place the new item at the bottom of the list
//		// catalogList is guaranteed to be non-null
//		boolean result = catalogList.addAll(newCatalogs);
//		if(result) { //must make this catalog accessible in productsMap
//			productsMap.put(catPres, 
//				FXCollections.observableList(new ArrayList<ProductPres>()));
//		}
//		
//		
//	}

	/**
	 * This method looks for the 0th element of the toBeRemoved list in
	 * catalogList and if found, removes it. In this app, removing more than one
	 * catalog at a time is not supported.
	 * 
	 * This method also updates the productList by removing the products that
	 * belong to the Catalog that is being removed.
	 * 
	 * Also: If the removed catalog was being stored as the selectedCatalog,
	 * the next item in the catalog list is set as "selected"
	 */
	public boolean removeFromCatalogList(ObservableList<CatalogPres> toBeRemoved) {
		boolean result = false;
		CatalogPres item = toBeRemoved.get(0);
		if (toBeRemoved != null && !toBeRemoved.isEmpty()) {
			result = catalogList.remove(item);
		}
		if(item.equals(selectedCatalog)) {
			if(!catalogList.isEmpty()) {
				selectedCatalog = catalogList.get(0);
			} else {
				selectedCatalog = null;
			}
		}
		if(result) {//update productsMap
			productsMap.remove(item);
		}
		return result;
	}
	
	//Synchronizers
	public class ManageProductsSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			productsMap.put(selectedCatalog, list);
		}
	}
	public ManageProductsSynchronizer getManageProductsSynchronizer() {
		return new ManageProductsSynchronizer();
	}
	
	private class ManageCatalogsSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			catalogList = list;
		}
	}
	public ManageCatalogsSynchronizer getManageCatalogsSynchronizer() {
		return new ManageCatalogsSynchronizer();
	}
}
