package org.wowbagger.rules;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.wowbagger.rules.annotation.Invariant;
import org.wowbagger.rules.annotation.Invariants;

@Invariants(
		names={"date","counter"}, 
		descriptions={"some date rules","greater than 0"})
class InvariantObject{
	
};

public class InvariantRuleTest {

	@ClassRule public static InvariantRule invariantRule = new InvariantRule(InvariantObject.class);
	
	@Invariant(name="counter")
	@Test
	public void test() {
		int i=0;
	}
	@Ignore
	@Invariant(name="date")
	@Test
	public void testDate() {
		int i=0;
	}

	
	@Invariant(name="date")
	@Test
	public void testDateSecond() {
		int i=0;
	}
}
