package org.wowbagger.rules;

import java.util.Locale;

/**
 * Rule allows to use specific locale for particular tests.
 * 
 * Default locale is stored before test and restored after.
 * Rule can be used on the method or class level. 
 * 
 * @author setkomac
 *
 */
public class LocaleRule extends InitializationRule {

	private static final Locale defaultLocale = Locale.getDefault();
	private Locale locale;
	
	/**
	 * Object constructor.
	 * @param locale Local that will be used inside test.
	 */
	public LocaleRule(Locale locale){
		this.locale = locale;
	}
	
	@Override
	public void before() {
		Locale.setDefault(locale);
	}

	@Override
	public void after() {
		Locale.setDefault(defaultLocale);
	}

}
