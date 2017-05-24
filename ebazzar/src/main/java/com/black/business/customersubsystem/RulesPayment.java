package com.black.business.customersubsystem;

import java.util.*;

import com.black.business.exceptions.BusinessException;
import com.black.business.exceptions.RuleException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.Rules;
import business.externalinterfaces.RulesSubsystem;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import com.black.business.rulesbeans.PaymentBean;
import com.black.business.rulesubsystem.RulesSubsystemFacade;

public class RulesPayment implements Rules {
	
	private HashMap<String,DynamicBean> table;
	private DynamicBean bean;	
	private RulesConfigProperties config = new RulesConfigProperties();
	
	public RulesPayment(Address address, CreditCard creditCard){
		bean = new PaymentBean(address, creditCard);
	}	
	
	
	///////////////implementation of interface
	public String getModuleName(){
		return config.getProperty(RulesConfigKey.PAYMENT_MODULE.getVal());
	}
	public String getRulesFile() {
		return config.getProperty(RulesConfigKey.PAYMENT_RULES_FILE.getVal());
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();		
		String deftemplate = config.getProperty(RulesConfigKey.PAYMENT_DEFTEMPLATE.getVal());
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
