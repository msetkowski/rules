package org.wowbagger.rules;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

public class LocaleRuleTest {

	@Rule
	public LocaleRule rule = new LocaleRule(Locale.GERMAN);

	@Test
	public void testFormattingNumberWithSpecificLocale() {
		DecimalFormat formatter = (DecimalFormat) NumberFormat
				.getNumberInstance();
		formatter.setMinimumFractionDigits(2);
		formatter.setMaximumFractionDigits(2);
		String result = formatter.format(45.33);
		Assert.assertEquals(result, "45,33");
	}
}
