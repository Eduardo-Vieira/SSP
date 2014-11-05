/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.model

import static org.junit.Assert.*

import org.junit.Test

import com.google.common.collect.Sets

class AuditableTest implements Serializable {

	private static final long serialVersionUID = 1L;

	class AuditableSubClass extends AbstractAuditable{

		private static final long serialVersionUID = 51248202154962763L;

		protected int hashPrime(){
			return 197
		};

		public int hashCode() {
			return hashPrime() * (id == null ? "id".hashCode() : id.hashCode());
		}
	}

	class AuditableSubClass2 extends AbstractAuditable{

		private static final long serialVersionUID = 612482021549627685L;

		protected int hashPrime(){
			return 193
		};

		public int hashCode() {
			return hashPrime() * (id == null ? "id".hashCode() : id.hashCode());
		}
	}

	@Test
	void equals1(){
		UUID id = UUID.randomUUID()
		Auditable a1 = new AuditableSubClass(id:id)
		Auditable a2 = new AuditableSubClass(id:id)
		assertTrue("with same id, should be equal", a1.equals(a2))
		assertTrue("with same id, should be equal - backwards", a2.equals(a1))
		assertTrue("with same memory object, should be equal for obj1", a1.equals(a1))
		assertTrue("with same memory object, should be equal for obj2", a2.equals(a2))


		a1 = new AuditableSubClass(id:id)
		a2 = new AuditableSubClass()
		assertFalse(a1.equals(a2))
		assertFalse(a2.equals(a1))
		assertTrue(a2.equals(a2))
		assertTrue(a1.equals(a1))
		assertTrue(a1.hashCode() != a2.hashCode())
		assertEquals(a1.hashCode(), a1.hashCode())
		assertEquals(a2.hashCode(), a2.hashCode())

		AuditableSubClass a3 = new AuditableSubClass(id:id)
		assertTrue("with same id, should be equal", a1.equals(a3))
		assertTrue("with same id, should be equal - backwards", a3.equals(a1))
		assertEquals("with same id, hashcode should be equal", a1.hashCode(), a3.hashCode())
	}

	@Test
	void equals2(){
		UUID id = UUID.randomUUID()
		Auditable a1 = new AuditableSubClass(id:id)
		Auditable a2 = new AuditableSubClass2(id:id)
		assertFalse("with diff classes, obj should not be equal, even with same id", a1.equals(a2))
		assertFalse("with diff classes, obj should not be equal, even with same id - backwards", a2.equals(a1))
		assertTrue("hashcode should be different for different classes", a1.hashCode()!= a2.hashCode())
	}

	@Test
	public void testSetOperations() {
		Auditable pel1 = new AuditableSubClass()
		Auditable pel2 = new AuditableSubClass()
		Set<AuditableSubClass> container = Sets.newHashSet();

		container.add(pel1)
		container.add(pel2);

		assertEquals("hashcode with null id calculated incorrectly", 1, container.size())

		container = Sets.newHashSet()
		pel1.setId(UUID.randomUUID())
		container.add(pel1)
		pel2.setId(UUID.randomUUID())
		container.add(pel2)

		assertEquals("hashcode with different id should not be equal", 2, container.size())

		container = Sets.newHashSet()
		container.add(pel1)
		pel2.setId(pel1.getId())
		container.add(pel2)

		assertEquals("hashcode with same id should be equal", 1, container.size())
	}
}