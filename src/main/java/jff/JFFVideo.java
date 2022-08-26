package jff;

public class JFFVideo {
	String url;
	String thumbnailUrl;
	String filename;
	String description;
	Long timestamp;
	String timeDescription;

	public JFFVideo() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public Long getTimestamp() { return timestamp; }

	public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

	public String getTimeDescription() {
		return timeDescription;
	}

	public void setTimeDescription(String timeDescription) {
		this.timeDescription = timeDescription;
	}

}
