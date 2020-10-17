package de.tisan.church.untertitelinator.churchtools.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventPermission {
	boolean useChat;
	boolean startChat;

	public boolean isUseChat() {
		return useChat;
	}

	public void setUseChat(boolean useChat) {
		this.useChat = useChat;
	}

	public boolean isStartChat() {
		return startChat;
	}

	public void setStartChat(boolean startChat) {
		this.startChat = startChat;
	}

}
