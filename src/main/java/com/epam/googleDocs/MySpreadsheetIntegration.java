package com.epam.googleDocs;

import com.epam.steps.Steps;

import com.google.gdata.util.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;

public class MySpreadsheetIntegration {

	public static void main(String[] args) throws AuthenticationException,
			MalformedURLException, IOException, ServiceException,
			GeneralSecurityException {

		Steps.authorize();
		Steps.getListNames(1, "XML+XSD");
		Steps.loginInClassMarket();


	}
}
