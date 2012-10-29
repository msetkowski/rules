package org.wowbagger.rules;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;


public class GetterSetterRuleTest {

	@ClassRule
	public static GetterSetterRule<TestedObject> tester = new GetterSetterRule<TestedObject>(new TestedObject(), new String[]{"ignored"}, true);
	
	@Test
	public void test() {
		Assert.assertTrue(true);		
	}

}
