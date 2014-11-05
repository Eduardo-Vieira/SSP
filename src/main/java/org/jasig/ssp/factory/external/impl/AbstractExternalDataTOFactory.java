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
package org.jasig.ssp.factory.external.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jasig.ssp.dao.external.ExternalDataDao;
import org.jasig.ssp.factory.AbstractAuditableTOFactory;
import org.jasig.ssp.factory.external.ExternalTOFactory;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.transferobject.external.ExternalDataTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class AbstractExternalDataTOFactory<TO extends ExternalDataTO<M>, M>
		implements ExternalTOFactory<TO, M> {

	private final transient Class<TO> toClass;

	protected final transient Class<M> mClass;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractExternalDataTOFactory.class);

	/**
	 * Constructor that initializes specific class instances for use by the
	 * common base class methods.
	 * 
	 * @param toClass
	 *            Transfer object class for the associated model class
	 * @param mClass
	 *            The model class
	 */
	public AbstractExternalDataTOFactory(final Class<TO> toClass,
			final Class<M> mClass) {
		this.toClass = toClass;
		this.mClass = mClass;
	}

	@Override
	public TO from(final M model) {
		final TO tObject = newTObject();
		tObject.from(model);
		return tObject;
	}

	@Override
	public M from(final TO tObject) throws ObjectNotFoundException {
		return newModel();
	}

	protected abstract ExternalDataDao<M> getDao();

	@Override
	public List<TO> asTOList(final Collection<M> models) {
		final List<TO> tos = Lists.newArrayList();

		if ((models != null) && !models.isEmpty()) {
			for (final M model : models) {
				tos.add(from(model));
			}
		}

		return tos;
	}

	@Override
	public Set<TO> asTOSet(final Collection<M> models) {
		final Set<TO> tos = Sets.newHashSet();
		for (final M model : models) {
			tos.add(from(model));
		}
		return tos;
	}

	@Override
	public Set<M> asSet(final Collection<TO> tObjects)
			throws ObjectNotFoundException {
		final Set<M> models = Sets.newHashSet();
		for (final TO tObject : tObjects) {
			models.add(from(tObject));
		}
		return models;
	}

	protected TO newTObject() {
		try {
			return toClass.newInstance();
		} catch (final InstantiationException e) {
			LOGGER.error("unable to instantiate Transfer object in factory.");
		} catch (final IllegalAccessException e) {
			LOGGER.error("unable to instantiate Transfer object in factory.");
		}
		return null;
	}

	protected M newModel() {
		try {
			return mClass.newInstance();
		} catch (final InstantiationException e) {
			LOGGER.error("unable to instantiate Model object in factory.");
		} catch (final IllegalAccessException e) {
			LOGGER.error("unable to instantiate Model object in factory.");
		}

		return null;
	}
}