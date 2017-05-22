package presentation.data;

import static presentation.util.UtilForUIClasses.catalogToCatalogPres;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import presentation.util.UtilForUIClasses;
import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.util.Convert;
import business.productsubsystem.ProductSubsystemFacade;
import business.usecasecontrol.ManageProductsController;

public enum ManageProductsData {
	INSTANCE;
	
	ManageProductsController manProdCont; 
	
	private ManageProductsController controller = new ManageProductsController();

	private CatalogPres defaultCatalog = readDefaultCatalogFromDataSource();
	private CatalogPres selectedCatalog = defaultCatalog;
//========================================================
	private CatalogPres readDefaultCatalogFromDataSource() {
		//return DefaultData.CATALOG_LIST_DATA.get(0);
		try {
			defaultCatalog = controller.getCatalogs()
				    .stream()
				    .map(catalog -> catalogToCatalogPres(catalog))
				    .collect(Collectors.toList()).get(0);
		} catch (BackendException e) {
			e.printStackTrace();
		}
		return defaultCatalog;
	}
	public CatalogPres getDefaultCatalog() {
		return defaultCatalog;
	}

	public void setSelectedCatalog(CatalogPres selCatalog) {
		selectedCatalog = selCatalog;
	}
	public CatalogPres getSelectedCatalog() {
		return selectedCatalog;
	}
	//////////// Products List model
	private ObservableMap<CatalogPres, List<ProductPres>> productsMap
	   = readProductsFromDataSource();
	
	/** Initializes the productsMap */
	private ObservableMap<CatalogPres, List<ProductPres>> readProductsFromDataSource() {
		/*public final static ObservableMap<CatalogPres, List<ProductPres>> PRODUCT_LIST_DATA =
	            FXCollections.observableHashMap();*/
		return DefaultData.PRODUCT_LIST_DATA;
	}
	
	/** Delivers the requested products list to the UI */
	public ObservableList<ProductPres> getProductsList(CatalogPres catPres) {
		manProdCont = new ManageProductsController();
		Catalog cat = catPres.getCatalog();
		
		List<ProductPres> productList  = null;
		try {
			 productList = manProdCont.getProductsList(cat)
				    .stream()
				    .map(product -> UtilForUIClasses.productToProductPres(product))
				    .collect(Collectors.toList());
	} catch (BackendException e) {
		// TODO Auto-generated catch block//////////////////////////////////do something abt exception//\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		
		e.printStackTrace();
	}	
	
		//return FXCollections.observableList(productsMap.get(catPres));
		return FXCollections.observableList(productList);
	}
	
	public ProductPres productPresFromData(Catalog c, String name, String date,  //MM/dd/yyyy 
			int numAvail, double price) {
		
		Product product = ProductSubsystemFacade.createProduct(c, name, 
				Convert.localDateForString(date), numAvail, price);
		ProductPres prodPres = new ProductPres();
		prodPres.setProduct(product);
		return prodPres;
	}
	
	public void addToProdList(CatalogPres catPres, ProductPres prodPres) {
		ObservableList<ProductPres> newProducts =
		           FXCollections.observableArrayList(prodPres);
		List<ProductPres> specifiedProds = productsMap.get(catPres);
		
		//Place the new item at the bottom of the list
		specifiedProds.addAll(newProducts);
		
	}
	
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
	public ObservableList<CatalogPres> readCatalogsFromDataSource() {
		
		 manProdCont = new ManageProductsController();
		List<CatalogPres> catalogList  = null;
		try {
		catalogList = manProdCont.getCatalogs()
				    .stream()
				    .map(catalog -> catalogToCatalogPres(catalog))
				    .collect(Collectors.toList());
	} catch (BackendException e) {
		
		e.printStackTrace();
	}	
		
		return FXCollections.observableList(catalogList);	
		
		//return FXCollections.observableList(DefaultData.CATALOG_LIST_DATA);
	}

	/** Delivers the already-populated catalogList to the UI */
	public ObservableList<CatalogPres> getCatalogList() {
		
		return readCatalogsFromDataSource();
		
	}

	public CatalogPres catalogPresFromData(int id, String name) {
		Catalog cat = ProductSubsystemFacade.createCatalog(id, name);
		CatalogPres catPres = new CatalogPres();
		catPres.setCatalog(cat);
		return catPres;
	}
/*
	public void addToCatalogList(CatalogPres catPres) {
		ObservableList<CatalogPres> newCatalogs = FXCollections
				.observableArrayList(catPres);

		// Place the new item at the bottom of the list
		// catalogList is guaranteed to be non-null
		boolean result = catalogList.addAll(newCatalogs);
		if(result) { //must make this catalog accessible in productsMap
			productsMap.put(catPres, FXCollections.observableList(new ArrayList<ProductPres>()));
		}
	}*/

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
	public boolean removeFromCatalogList(ObservableList<CatalogPres> toBeRemoved) 
	{
		
		/*boolean result = false;
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
		return result;*/
		boolean result = false;
		ManageProductsController mpCont = new ManageProductsController();
		CatalogPres item = toBeRemoved.get(0);
		Catalog cat = item.getCatalog() ;
		if (toBeRemoved != null && !toBeRemoved.isEmpty()) {
			try {
				mpCont.deleteCatalog(cat);
				result = true;
				
			} catch (BackendException e) {
				
			}
		}
		getManageCatalogsSynchronizer().refresh(readCatalogsFromDataSource());
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
