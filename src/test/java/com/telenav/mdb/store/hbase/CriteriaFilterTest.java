package com.telenav.mdb.store.hbase;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telenav.mdb.converters.bean.Tests;
import com.telenav.mdb.store.query.Condition;
import com.telenav.mdb.store.query.Criteria;
import com.telenav.mdb.store.query.CriteriaFilter;
import com.telenav.mdb.store.query.Operator;

public class CriteriaFilterTest extends Tests {
	CriteriaFilter cf = null;
	Criteria criteria = null;

	@Before
	public void setupFilter() {
		criteria = new Criteria();
		cf = new CriteriaFilter(criteria);
	}

	@Test
	public void testEq() {
		Condition eq = eqCondition();
		criteria.addCondition(eq);

		boolean accept = cf.isAccept(person);
		Assert.assertTrue(accept);
	}

	@Test
	public void testNe() {
		Condition ne = neCondition();
		criteria.addCondition(ne);

		boolean notAccept = cf.isAccept(person);
		Assert.assertFalse(notAccept);
	}

	@Test
	public void testlt() {
		Condition lt = ltCondition();
		criteria.addCondition(lt);

		boolean notAccept = cf.isAccept(person);
		Assert.assertFalse(notAccept);
	}

	@Test
	public void testGt() {
		Condition gt = ltCondition();
		gt.op = Operator.gt;
		criteria.addCondition(gt);

		boolean accept = cf.isAccept(person);
		Assert.assertTrue(accept);
	}

	Condition eqCondition() {
		Condition cond = new Condition();
		cond.op = Operator.eq;
		cond.property = "firstname";
		cond.value = person.getFirstname();

		return cond;
	}

	Condition neCondition() {
		Condition cond = new Condition();
		cond.op = Operator.ne;
		cond.property = "firstname";
		cond.value = person.getFirstname();

		return cond;
	}

	Condition ltCondition() {
		Condition cond = new Condition();
		cond.op = Operator.lt;
		cond.property = "firstname";
		cond.value = "a" + person.getFirstname();

		return cond;
	}

}
