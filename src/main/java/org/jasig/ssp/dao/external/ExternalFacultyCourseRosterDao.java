package org.jasig.ssp.dao.external;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.jasig.ssp.model.external.ExternalFacultyCourseRoster;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.springframework.stereotype.Repository;

/**
 * Data access class for the FacultyCourse reference entity.
 */
@Repository
public class ExternalFacultyCourseRosterDao extends
		AbstractExternalDataDao<ExternalFacultyCourseRoster> {

	public ExternalFacultyCourseRosterDao() {
		super(ExternalFacultyCourseRoster.class);
	}

	/**
	 * Gets the course roster for the specified faculty's course.
	 * 
	 * @param facultySchoolId
	 *            The faculty school id to use to lookup the associated data.
	 * @param formattedCourse
	 *            the course
	 * @return The specified courses if any were found.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 */
	@SuppressWarnings("unchecked")
	public List<ExternalFacultyCourseRoster> getRosterByFacultySchoolIdAndCourse(
			final String facultySchoolId, final String formattedCourse)
			throws ObjectNotFoundException {
		return createCriteria()
				.add(Restrictions.eq("facultySchoolId", facultySchoolId))
				.add(Restrictions.eq("formattedCourse", formattedCourse))
				.list();
	}
}