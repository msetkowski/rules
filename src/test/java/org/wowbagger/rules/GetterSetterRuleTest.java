package org.wowbagger.rules;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;


public class GetterSetterRuleTest {

	@ClassRule
	public static GetterSetterRule<SampleObject> tester = new GetterSetterRule<SampleObject>(new SampleObject(), new String[]{"ignored"}, true);
	
	@Test
	public void test() {
		Assert.assertTrue(true);		
	}

}
