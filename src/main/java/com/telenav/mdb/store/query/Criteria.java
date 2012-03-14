package com.telenav.mdb.store.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Criteria {

	List<Condition> conds = new ArrayList<Condition>();

	/**
	 * @param cond
	 */
	public void addCondition(Condition cond) {
		conds.add(cond);
	}

	public Iterator<Condition> iterator() {
		return conds.iterator();
	}
}
