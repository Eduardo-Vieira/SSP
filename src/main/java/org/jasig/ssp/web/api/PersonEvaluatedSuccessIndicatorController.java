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
package org.jasig.ssp.web.api;

import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.service.EvaluatedSuccessIndicatorService;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.transferobject.EvaluatedSuccessIndicatorTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/1/person/{personId}/evaluatedSuccessIndicator")
public class PersonEvaluatedSuccessIndicatorController extends AbstractBaseController {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PersonEvaluatedSuccessIndicatorController.class);

    @Autowired
    private EvaluatedSuccessIndicatorService evaluatedSuccessIndicatorService;

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_PERSON_READ') or hasRole('ROLE_PERSON_SUCCESS_INDICATOR_READ')")
    public @ResponseBody List<EvaluatedSuccessIndicatorTO> getForPerson(final @PathVariable UUID personId,
                                                                        final @RequestParam(required = false) ObjectStatus status)
            throws ObjectNotFoundException {
        return evaluatedSuccessIndicatorService.getForPerson(personId, status);
    }
}
