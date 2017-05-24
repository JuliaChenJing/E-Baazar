package com.black.middleware.externalinterfaces;

import com.black.middleware.exceptions.MiddlewareException;


public interface CreditVerification {
	public void checkCreditCard(CreditVerificationProfile profile) throws MiddlewareException;
}
