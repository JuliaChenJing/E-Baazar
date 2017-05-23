package presentation.data;

import business.externalinterfaces.Catalog;

public class CatalogPres {
	private Catalog catalog;

	public CatalogPres() {
	}
	
	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setName(String aName) {
		catalog.setName(aName);
	}

	public String getName() {
		return catalog.getName();
	}

	public int getId() {
		return catalog.getId();
	}

	public void setId(int id) {
		catalog.setId(id);
	}

	public boolean equals(Object ob) {
		if (ob == null)
			return false;
		if (this == ob)
			return true;
		if (getClass() != ob.getClass())
			return false;
		CatalogPres c = (CatalogPres) ob;
		return catalog.equals(c.catalog);
	}

	public int hashCode() {
		int result = 17;
		result += 31 * result + catalog.hashCode();
		return result;
	}
}
