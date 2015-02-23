package com.epam.utils;

import java.util.Map;

public class AssociationModuleNameToTestName {
	private Map<String, String> moduleNameToTestName;
	
	public AssociationModuleNameToTestName(Map<String, String> map) {
		this.moduleNameToTestName = map;
	}

	public Map<String, String> getModuleNameToTestName() {
		return moduleNameToTestName;
	}

	public void setModuleNameToTestName(Map<String, String> moduleNameToTestName) {
		this.moduleNameToTestName = moduleNameToTestName;
	}
}
