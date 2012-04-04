package edu.sinclair.ssp.service.reference.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import edu.sinclair.ssp.dao.reference.MessageTemplateDao;
import edu.sinclair.ssp.model.ObjectStatus;
import edu.sinclair.ssp.model.reference.MessageTemplate;
import edu.sinclair.ssp.service.ObjectNotFoundException;

public class MessageTemplateServiceTest {

	private MessageTemplateServiceImpl service;
	private MessageTemplateDao dao;

	@Before
	public void setup() {
		service = new MessageTemplateServiceImpl();
		dao = createMock(MessageTemplateDao.class);

		service.setDao(dao);
	}

	@Test
	public void testGetAll() {
		List<MessageTemplate> daoAll = new ArrayList<MessageTemplate>();
		daoAll.add(new MessageTemplate());

		expect(dao.getAll(ObjectStatus.ACTIVE)).andReturn(daoAll);

		replay(dao);

		List<MessageTemplate> all = service.getAll(ObjectStatus.ACTIVE);
		assertTrue(all.size() > 0);
		verify(dao);
	}

	@Test
	public void testGet() throws ObjectNotFoundException {
		UUID id = UUID.randomUUID();
		MessageTemplate daoOne = new MessageTemplate(id);

		expect(dao.get(id)).andReturn(daoOne);

		replay(dao);

		assertNotNull(service.get(id));
		verify(dao);
	}

	@Test
	public void testSave() throws ObjectNotFoundException {
		UUID id = UUID.randomUUID();
		MessageTemplate daoOne = new MessageTemplate(id);

		expect(dao.get(id)).andReturn(daoOne);
		expect(dao.save(daoOne)).andReturn(daoOne);

		replay(dao);

		assertNotNull(service.save(daoOne));
		verify(dao);
	}

	@Test
	public void testDelete() throws ObjectNotFoundException {
		UUID id = UUID.randomUUID();
		MessageTemplate daoOne = new MessageTemplate(id);

		expect(dao.get(id)).andReturn(daoOne).times(2);
		expect(dao.save(daoOne)).andReturn(daoOne);
		expect(dao.get(id)).andReturn(null);

		replay(dao);

		service.delete(id);

		boolean found = true;
		try {
			service.get(id);
		} catch (ObjectNotFoundException e) {
			found = false;
		}

		assertFalse(found);
		verify(dao);
	}

}
