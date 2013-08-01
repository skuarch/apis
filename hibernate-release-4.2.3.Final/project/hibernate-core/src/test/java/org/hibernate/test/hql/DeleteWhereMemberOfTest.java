/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2013, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.test.hql;

import org.hibernate.Session;

import org.junit.Test;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.test.annotations.query.Attrset;
import org.hibernate.test.annotations.query.Attrvalue;
import org.hibernate.test.annotations.query.Employee;
import org.hibernate.test.annotations.query.Employeegroup;

/**
 * @author Steve Ebersole
 */
public class DeleteWhereMemberOfTest extends BaseCoreFunctionalTestCase {

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Attrset.class,
				Attrvalue.class,
				Employee.class,
				Employeegroup.class
		};
	}

	@Test
	@TestForIssue( jiraKey = "HHH-8318" )
//	@FailureExpected( jiraKey = "HHH-8318" )
	public void testDeleteMemberOf() {
		final String qry = "delete Attrvalue aval where aval.id in ( "
				+ "select val2.id from Employee e, Employeegroup eg, Attrset aset, Attrvalue val2 "
				+ "where eg.id = e.employeegroup.id " + "and aset.id = e.attrset.id "
				+ "and val2 member of aset.attrvalues)";
		Session s = openSession();
		s.getTransaction().begin();
		s.createQuery( qry ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}
}
