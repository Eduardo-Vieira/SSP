package org.studentsuccessplan.ssp.service.reference.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.studentsuccessplan.ssp.dao.reference.ChallengeCategoryDao;
import org.studentsuccessplan.ssp.model.ObjectStatus;
import org.studentsuccessplan.ssp.model.reference.ChallengeCategory;
import org.studentsuccessplan.ssp.service.ObjectNotFoundException;
import org.studentsuccessplan.ssp.service.reference.ChallengeCategoryService;
import org.studentsuccessplan.ssp.util.sort.PagingWrapper;
import org.studentsuccessplan.ssp.util.sort.SortingAndPaging;

@Service
@Transactional
public class ChallengeCategoryServiceImpl implements ChallengeCategoryService {

	@Autowired
	private ChallengeCategoryDao dao;

	@Override
	public PagingWrapper<ChallengeCategory> getAll(SortingAndPaging sAndP) {
		return dao.getAll(sAndP);
	}

	@Override
	public ChallengeCategory get(UUID id) throws ObjectNotFoundException {
		ChallengeCategory obj = dao.get(id);
		if (null == obj) {
			throw new ObjectNotFoundException(id, "ChallengeCategory");
		}

		return obj;
	}

	@Override
	public ChallengeCategory create(ChallengeCategory obj) {
		return dao.save(obj);
	}

	@Override
	public ChallengeCategory save(ChallengeCategory obj)
			throws ObjectNotFoundException {
		ChallengeCategory current = get(obj.getId());

		current.setName(obj.getName());
		current.setDescription(obj.getDescription());
		current.setObjectStatus(obj.getObjectStatus());

		return dao.save(current);
	}

	@Override
	public void delete(UUID id) throws ObjectNotFoundException {
		ChallengeCategory current = get(id);

		if (null != current) {
			current.setObjectStatus(ObjectStatus.DELETED);
			save(current);
		}
	}

	protected void setDao(ChallengeCategoryDao dao) {
		this.dao = dao;
	}
}
