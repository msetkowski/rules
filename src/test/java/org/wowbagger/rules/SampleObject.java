package org.wowbagger.rules;

import java.util.List;

class Embedded{
	
}

public class SampleObject{
	private int integer;
	private Integer integerObject;
	private List<String> listString;
	private Boolean flagObject;
	private boolean flag;
	private String ignored;
	private Embedded embedded;
	private SampleEnum testEnum;
	
	
	public SampleEnum getTestEnum() {
		return testEnum;
	}

	public void setTestEnum(SampleEnum testEnum) {
		this.testEnum = testEnum;
	}

	public Embedded getEmbedded() {
		return embedded;
	}

	public void setEmbedded(Embedded embedded) {
		this.embedded = embedded;
	}

	public String getIgnored(){
		return ignored;
	}
	
	public int getInteger() {
		return integer;
	}
	
	public void setInteger(int integer) {
		this.integer = integer;
	}
	public Integer getIntegerObject() {
		return integerObject;
	}
	public void setIntegerObject(Integer integerObject) {
		this.integerObject = integerObject;
	}
	public List<String> getListString() {
		return listString;
	}
	public void setListString(List<String> listString) {
		this.listString = listString;
	}
	public Boolean isFlagObject() {
		return flagObject;
	}
	public void setFlagObject(Boolean flagObject) {
		this.flagObject = flagObject;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	
}
