package middleware.creditverifcation;

import middleware.exceptions.MiddlewareException;
import middleware.externalinterfaces.CreditVerification;
import middleware.externalinterfaces.CreditVerificationProfile;
import publicview.IVerificationSystem;
import publicview.VerificationManager;


public class CreditVerificationFacade implements CreditVerification {

	@Override
	public void checkCreditCard(CreditVerificationProfile profile) throws MiddlewareException {	
		IVerificationSystem verifSystem = VerificationManager.clientInterface();
		CreditVerifMediator mediator = new CreditVerifMediator();
		mediator.processCreditRequest(verifSystem, profile);
	}
	
	public static CreditVerificationProfile getCreditProfileShell() {
		return new CreditVerificationProfileImpl();
	}
}
