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
package org.jasig.ssp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Email (Message) model
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
final public class Message
		extends AbstractAuditable
		implements Auditable {

	private static final long serialVersionUID = -7643811408668209143L;

	@Column(nullable = false, length = 250)
	private String subject;

	@Column(nullable = false, columnDefinition = "text")
	private String body;

	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	private Person sender;

	@ManyToOne()
	@JoinColumn(name = "recipient_id", nullable = true)
	private Person recipient;

	@Column(length = 100, nullable = false)
	private String recipientEmailAddress;

	@Column(length = 512, nullable = true)
	private String carbonCopy;
	
	@Column(length = 512, nullable = true)
	private String sentToAddresses;
	
	@Column(length = 512, nullable = true)
	private String sentCcAddresses;
	
	@Column(length = 512, nullable = true)
	private String sentBccAddresses;
	
	@Column(length = 256, nullable = true)
	private String sentFromAddress;
	
	@Column(length = 256, nullable = true)
	private String sentReplyToAddress;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date sentDate;

	private Integer retryCount;
	/**
	 * Empty constructor
	 */
	public Message() {
		super();
	}

	/**
	 * Construct a new message with the required attributes.
	 * 
	 * @param subject
	 *            Message subject
	 * @param body
	 *            Message body
	 * @param sender
	 *            Message sender
	 * @param recipient
	 *            Message recipient
	 * @param recipientEmailAddress
	 *            Recipient e-mail address
	 */
	public Message(final String subject, final String body,
			final Person sender,
			final Person recipient, final String recipientEmailAddress) {
		super();
		setObjectStatus(ObjectStatus.ACTIVE);
		this.subject = subject;
		this.body = body;
		this.sender = sender;
		this.recipient = recipient;
		this.recipientEmailAddress = recipientEmailAddress;
	}

	public Message(final SubjectAndBody subjAndMessage) {
		super();
		setObjectStatus(ObjectStatus.ACTIVE);
		subject = subjAndMessage.getSubject();
		body = subjAndMessage.getBody();
	}

	/**
	 * Gets the subject
	 * 
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the email subject; maximum of 250 characters
	 * 
	 * @param subject
	 *            E-mail subject; maximum of 250 characters, can not be null
	 */
	public void setSubject(@NotNull final String subject) {
		this.subject = subject;
	}

	public Person getSender() {
		return sender;
	}

	public void setSender(@NotNull final Person sender) {
		this.sender = sender;
	}

	public Person getRecipient() {
		return recipient;
	}

	public void setRecipient(final Person recipient) {
		this.recipient = recipient;
		if (recipient != null && recipient.hasEmailAddresses() && recipient.getEmailAddresses().size() > 0) {
			recipientEmailAddress = recipient.getEmailAddresses().get(0);
		}
	}

	public String getRecipientEmailAddress() {
		return recipientEmailAddress;
	}

	/**
	 * Sets the recipient email address; maximum of 100 characters
	 * 
	 * @param recipientEmailAddress
	 *            Recipient email address; maximum of 100 characters; can not be
	 *            null
	 */
	public void setRecipientEmailAddress(
			@NotNull final String recipientEmailAddress) {
		this.recipientEmailAddress = recipientEmailAddress;
	}

	public String getBody() {
		return body;
	}

	public void setBody(@NotNull final String body) {
		this.body = body;
	}

	public Date getSentDate() {
		return sentDate == null ? null : new Date(sentDate.getTime());
	}

	public void setSentDate(final Date sentDate) {
		this.sentDate = sentDate == null ? null : new Date(sentDate.getTime());
	}

	public String getCarbonCopy() {
		return carbonCopy;
	}

	public void setCarbonCopy(final String carbonCopy) {
		this.carbonCopy = carbonCopy;
	}

	public String getSentToAddresses() {
		return sentToAddresses;
	}

	public void setSentToAddresses(String sentToAddresses) {
		this.sentToAddresses = sentToAddresses;
	}

	public String getSentCcAddresses() {
		return sentCcAddresses;
	}

	public void setSentCcAddresses(String sentCcAddresses) {
		this.sentCcAddresses = sentCcAddresses;
	}

	public String getSentBccAddresses() {
		return sentBccAddresses;
	}

	public void setSentBccAddresses(String sentBccAddresses) {
		this.sentBccAddresses = sentBccAddresses;
	}

	public String getSentFromAddress() {
		return sentFromAddress;
	}

	public void setSentFromAddress(String sentFromAddress) {
		this.sentFromAddress = sentFromAddress;
	}

	public String getSentReplyToAddress() {
		return sentReplyToAddress;
	}

	public void setSentReplyToAddress(String sentReplyToAddress) {
		this.sentReplyToAddress = sentReplyToAddress;
	}

	@Override
	protected int hashPrime() {
		return 181;
	}

	@Override
	public int hashCode() { // NOPMD by jon.adams on 5/3/12 11:46 AM
		int result = hashPrime();

		// AbstractAuditable properties
		result *= hashField("id", getId());
		result *= hashField("objectStatus", getObjectStatus());

		// Message
		result *= hashField("subject", subject);
		result *= hashField("body", body);
		result *= hashField("sender", sender);
		result *= hashField("recipient", recipient);
		result *= hashField("recipientEmailAddress", recipientEmailAddress);
		result *= hashField("sentDate", sentDate);
		result *= hashField("carbonCopy", carbonCopy);

		return result;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
}