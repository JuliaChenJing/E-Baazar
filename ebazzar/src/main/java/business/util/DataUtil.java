package business.util;


import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import business.BusinessConstants;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerSubsystem;
import presentation.data.SessionCache;

public class DataUtil {
	
	public static double computeTotal(List<CartItem> list) {
		DoubleSummaryStatistics summary 
		  = list.stream().collect(
		       Collectors.summarizingDouble(item -> Double.parseDouble(item.getTotalprice())));
		return summary.getSum();
	}
}
