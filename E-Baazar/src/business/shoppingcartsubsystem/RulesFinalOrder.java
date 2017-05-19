package business.shoppingcartsubsystem;

import java.util.HashMap;
import java.util.List;

import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.Rules;
import business.externalinterfaces.RulesSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.rulesbeans.FinalOrderBean;
import business.rulesubsystem.RulesSubsystemFacade;

/**
 * This Rules class is different from RulesShoppingCart
 * (even though it checks aspects of a Shopping Cart)
 * because it needs to execute at a different time during
 * execution of the application (namely, when final order
 * is submitted)
 */
class RulesFinalOrder implements Rules {
    @Override
    public String getModuleName() {
        return null;
    }

    @Override
    public String getRulesFile() {
        return null;
    }

    @Override
    public void prepareData() {

    }

    @Override
    public HashMap<String, DynamicBean> getTable() {
        return null;
    }

    @Override
    public void runRules() throws BusinessException, RuleException {

    }

    @Override
    public void populateEntities(List<String> updates) {

    }

    @Override
    public List<?> getUpdates() {
        return null;
    }
//implement
	
}
