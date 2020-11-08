package de.tisan.church.untertitelinator.churchtools.api.objects;

public class Service {
	long id;
	String name;
	long serviceGroupId;
	boolean commentOnConfirmation;
	long sortKey;
	boolean allowDecline;
	boolean allowExchange;
	String comment;
	boolean standard;
	boolean hidePersonName;
	boolean sendReminderMails;
	boolean sendServiceRequestEmails;
	boolean allowControlLiveAgenda;
	String groupIds;
	String tagIds;
	String calTextTemplate;
	boolean allowChat;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(long serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	public boolean isCommentOnConfirmation() {
		return commentOnConfirmation;
	}

	public void setCommentOnConfirmation(boolean commentOnConfirmation) {
		this.commentOnConfirmation = commentOnConfirmation;
	}

	public long getSortKey() {
		return sortKey;
	}

	public void setSortKey(long sortKey) {
		this.sortKey = sortKey;
	}

	public boolean isAllowDecline() {
		return allowDecline;
	}

	public void setAllowDecline(boolean allowDecline) {
		this.allowDecline = allowDecline;
	}

	public boolean isAllowExchange() {
		return allowExchange;
	}

	public void setAllowExchange(boolean allowExchange) {
		this.allowExchange = allowExchange;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isStandard() {
		return standard;
	}

	public void setStandard(boolean standard) {
		this.standard = standard;
	}

	public boolean isHidePersonName() {
		return hidePersonName;
	}

	public void setHidePersonName(boolean hidePersonName) {
		this.hidePersonName = hidePersonName;
	}

	public boolean isSendReminderMails() {
		return sendReminderMails;
	}

	public void setSendReminderMails(boolean sendReminderMails) {
		this.sendReminderMails = sendReminderMails;
	}

	public boolean isSendServiceRequestEmails() {
		return sendServiceRequestEmails;
	}

	public void setSendServiceRequestEmails(boolean sendServiceRequestEmails) {
		this.sendServiceRequestEmails = sendServiceRequestEmails;
	}

	public boolean isAllowControlLiveAgenda() {
		return allowControlLiveAgenda;
	}

	public void setAllowControlLiveAgenda(boolean allowControlLiveAgenda) {
		this.allowControlLiveAgenda = allowControlLiveAgenda;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public String getCalTextTemplate() {
		return calTextTemplate;
	}

	public void setCalTextTemplate(String calTextTemplate) {
		this.calTextTemplate = calTextTemplate;
	}

	public boolean isAllowChat() {
		return allowChat;
	}

	public void setAllowChat(boolean allowChat) {
		this.allowChat = allowChat;
	}

}
