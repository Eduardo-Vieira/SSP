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
package org.jasig.ssp.service.security.lti;

import org.springframework.security.oauth.common.OAuthException;

/**
 * Because there is no other subclass of {@link OAuthException} which represents
 * "consumer record simply not on file", but some API contracts
 * ({@link org.springframework.security.oauth.provider.ConsumerDetailsService}
 * in particular) require a missing consumer to be represented by throwing
 * something castable to a {@link OAuthException}.
 */
public class ConsumerDetailsNotFoundException extends OAuthException {

	public ConsumerDetailsNotFoundException(String message) {
		super(message);
	}

	public ConsumerDetailsNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
