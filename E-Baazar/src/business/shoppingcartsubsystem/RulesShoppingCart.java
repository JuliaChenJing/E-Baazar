package business.shoppingcartsubsystem;

import java.util.HashMap;
import java.util.List;

import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.Rules;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.externalinterfaces.RulesSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.rulesbeans.ShopCartBean;
import business.rulesubsystem.RulesSubsystemFacade;

class RulesShoppingCart implements Rules{
	
	private HashMap<String,DynamicBean> table;
	private DynamicBean bean;	
	private RulesConfigProperties config = new RulesConfigProperties();
	
	public RulesShoppingCart(ShoppingCart shoppingCart){
		bean = new ShopCartBean(shoppingCart);
	}	
		
	///////////////implementation of interface
	public String getModuleName(){
		return config.getProperty(RulesConfigKey.SHOPCART_MODULE.getVal());
	}
	public String getRulesFile() {
		return config.getProperty(RulesConfigKey.SHOPCART_RULES_FILE.getVal());
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();		
		String deftemplate = config.getProperty(RulesConfigKey.SHOPCART_DEFTEMPLATE.getVal());
		table.put(deftemplate, bean);
		
	}
	public void runRules() throws BusinessException, RuleException{
    	RulesSubsystem rules = new RulesSubsystemFacade();
    	rules.runRules(this);
	}
	public HashMap<String,DynamicBean> getTable(){
		return table;
	}
	/* expect a list of address values, in order
	 * street, city, state ,zip
	 */
	public void populateEntities(List<String> updates){
		//do nothing
		
	}
	
	public List<Object> getUpdates() {
		//do nothing
		return null;
	}
		
}
