package jff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import de.tisan.tisanapi.logger.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JFFScraper {

	public static String PAGE_0_URL = "https://justfor.fans/ajax/getPosts.php?Type=One&UserID=755747&PosterID=1760042&StartAt=0&Page=Profile&UserHash4=ade910ab5f4569494166db05a9238248&SplitTest=0";
	public static String COOKIE_TOKEN = "PHPSESSID=nq1gmsgomjkt75ii0kdao1evp5; UserHash3=66207ba24e018bf9c4e32bed9fc7bcc2; UserHash4=7b194f3f6613d31557cb20897bc1c78c; cookiesAccepted=active";
	public static String DOWNLOAD_PATH = "\\\\10.100.100.100\\Kuschelburrito\\jff-downloads\\holeslut69\\";

	public static ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
	public static ObjectMapper mapper = new ObjectMapper();

	public static Logger logger = new Logger();

	public static String datePattern = "MMMM dd, yyyy, h:mm aa"; // May 23, 2022, 3:27 pm
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern, Locale.US);

	public static void main(String[] args) throws Exception {
		DateFormatSymbols symbols = new DateFormatSymbols(Locale.US);
		// OVERRIDE SOME symbols WHILE RETAINING OTHERS
		symbols.setAmPmStrings(new String[] { "am", "pm" });
		simpleDateFormat.setDateFormatSymbols(symbols);

		String html = downloadHtml(PAGE_0_URL);

		Long loadedNewestDate = -1L;
		Long newestDate = -1L;

		File file = new File(DOWNLOAD_PATH + "newestPost.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		String tmp = Files.readString(file.toPath());
		if(tmp != null && tmp.length() > 0) {
			loadedNewestDate = Long.parseLong(tmp);
		}

		long page = 1;
		while (true) {
			ExecutorService taskExecutor = Executors.newFixedThreadPool(10);
			List<JFFVideo> videoUrls = scrapeVideoUrls(html);
			int i = 0;
			for (JFFVideo video : videoUrls) {
				int i2 = i;
				long page2 = page;

				if(loadedNewestDate > video.getTimestamp()) {
					logger.log("Video Post " + (i2 + 1) + " of " + videoUrls.size() + " (page " + page2 + ") ist zu alt, überspringe... ");
					i++;
					continue;
				}
				newestDate = video.getTimestamp();

				taskExecutor.execute(() -> {
					try {
						logger.log(" #################### Video " + (i2 + 1) + " of " + videoUrls.size()
								+ " (page " + page2 + "). #################### ");
						downloadVideo(video);
						saveDescription(video);

					} catch (Exception ex) {
						ex.printStackTrace();
						logger.log("Fehler beim Downloaden des Videos, versuche nächstes...");
					}
				});

				i++;

			}
			taskExecutor.shutdown();
			try {
				taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<JFFPhoto> photoUrls = scrapePhotoUrls(html);
			i = 0;
			for (JFFPhoto photo : photoUrls) {
				if(newestDate > photo.getTimestamp()) {
					logger.log("Photo Post " + (i + 1) + " of " + photoUrls.size() + " (page " + page + ") ist zu alt, überspringe... ");
					i++;
					continue;
				}
				newestDate = photo.getTimestamp();

				try {
					logger.log(" #################### Photo " + (i + 1) + " of " + photoUrls.size() + " (page "
							+ page + "). #################### ");
					downloadPhoto(photo);
					saveDescription(photo);
					i++;
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.log("Fehler beim Downloaden des Videos, versuche nächstes...");
				}
			}

			String nextUrl = scrapeMoreLink(html)
					.orElse(null);
			if(nextUrl == null) {
				Files.writeString(file.toPath(), Long.toString(newestDate));
				throw new Exception("Keine weitere Download-Page gefunden. Beende...");
			}
			html = downloadHtml(nextUrl);
			page++;
		}
	}

	public static String downloadHtml(String url) {
		logger.log("Downloade neues HTML " + url);
		ResteasyWebTarget target = client.target(url);
		Response response = target.request().header("cookie", COOKIE_TOKEN).header("accept", "text/html, */*; q=0.01")
				.header("referer", "https://justfor.fans/pervertmedia")
				.header("user-agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.102 Safari/537.36 Edg/104.0.1293.63")
				.get();
		logger.log(String.valueOf(response.getHeaders()));
		String html = response.readEntity(String.class);
		response.close();
		return html;
	}

	public static Optional<String> scrapeMoreLink(String html) throws IOException {
		Document doc = Jsoup.parse(html);
		String url = null;
		try {
			url = doc.getElementsByTag("a").stream().filter(a -> a.text().equalsIgnoreCase("More...")).findFirst().get()
					.attr("href");

		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}

		return Optional.ofNullable("https://justfor.fans/" + url);
	}

	public static List<JFFVideo> scrapeVideoUrls(String html) throws IOException {
		List<JFFVideo> urlList = new ArrayList<JFFVideo>();

		Document doc = Jsoup.parse(html);

		Elements videoCards = doc.getElementsByClass("jffPostClass");
		try {
			for (Element videoCard : videoCards) {
				if (videoCard.hasClass("video")) {

					JFFVideo video = new JFFVideo();

					String timestampString = videoCard.getElementsByClass("mbsc-card-header")
													  .get(0)
													  .getElementsByClass("mbsc-card-subtitle")
													  .get(0)
													  .text();
					video.setTimeDescription(timestampString);
					video.setTimestamp(simpleDateFormat.parse(timestampString).getTime());

					Elements videoBlocks = videoCard.getElementsByClass("videoBlock");

					for(Element videoBlock : videoBlocks) {
						video.setFilename(videoBlock.attr("id"));
						String javascript = videoBlock.getElementsByTag("a").get(0).attr("onClick");
						javascript = javascript.substring(javascript.indexOf("{"), javascript.lastIndexOf("}") + 1);
						JsonNode node = mapper.readTree(javascript);
						String   url  = null;
						if(node.get("1080p") != null) {
							url = node.get("1080p").asText();

						}
						else {
							url = node.get(0).asText();
						}
						video.setUrl(url);
						video.setThumbnailUrl(videoBlock.getElementsByTag("img").get(0).attr("src"));
						break;
					}
					try {
						video.setDescription(videoCard.getElementsByClass("mbsc-card-content").get(0)
													  .getElementsByClass("post-text-content").get(0).text());

					} catch(Exception e) {
						logger.log("Keine Description für video " + video.getUrl() + " gefunden.");
					}
					if(video.getUrl() == null) {
						continue;
					}
					urlList.add(video);
				}
			}
		} catch (Exception e) {
			logger.err("Fehler beim Scrapen des Videos, überspringe...", e, JFFScraper.class);
		}
		return urlList;
	}

	public static List<JFFPhoto> scrapePhotoUrls(String html) throws IOException {
		List<JFFPhoto> urlList = new ArrayList<JFFPhoto>();

		Document doc = Jsoup.parse(html);

		Elements photoCards = doc.getElementsByClass("jffPostClass");
		try {
			for (Element photoCard : photoCards) {
				if (photoCard.hasClass("photo")) {
					JFFPhoto photo = new JFFPhoto();

					String timestampString = photoCard.getElementsByClass("mbsc-card-header")
													  .get(0)
													  .getElementsByClass("mbsc-card-subtitle")
													  .get(0)
													  .text();
					photo.setTimeDescription(timestampString);
					photo.setTimestamp(simpleDateFormat.parse(timestampString).getTime());
					photo.setFilename(photoCard.attr("id"));

					try {
						Element elementWithImages = photoCard.hasClass("galleryWrapper")
													? photoCard.getElementsByClass("galleryWrapper").get(0).getElementsByClass("galleryLarge").get(0)
													: photoCard;
						photo.setUrls(elementWithImages.getElementsByTag("img").stream().map(img -> img.attr("src"))
											 .collect(Collectors.toList()));
					} catch(IndexOutOfBoundsException e) {
						logger.err("Unerwartete HTML Struktur", e, JFFScraper.class);
					}

					try {
						photo.setDescription(photoCard.getElementsByClass("mbsc-card-content").get(0)
													  .getElementsByClass("post-text-content").get(0).text());

					} catch (Exception e) {
						logger.log("Keine Description für Fotogallerie gefunden.");
					}

					urlList.add(photo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log("Fehler beim Scrapen der Fotos, überspringe...");
		}
		return urlList;
	}

	public static void downloadVideo(JFFVideo video) throws IOException {
		if (video.getUrl() == null) {
			return;
		}

		File file = new File(DOWNLOAD_PATH);
		if (file.exists() == false) {
			file.mkdir();
		}
		File videoFile = new File(file.getAbsolutePath() + File.separator + video.getFilename() + ".mp4");
		File thumbnailFile = new File(file.getAbsolutePath() + File.separator + video.getFilename() + ".jpg");

		downloadFile(video.getUrl(), videoFile.toPath());

		if (video.getThumbnailUrl() == null) {
			return;
		}

		downloadFile(video.getThumbnailUrl(), thumbnailFile.toPath());

	}

	public static void downloadPhoto(JFFPhoto photo) throws IOException {
		if (photo.getUrls() == null || photo.getUrls().isEmpty()) {
			return;
		}

		File folder = new File(DOWNLOAD_PATH + photo.getFilename());
		if (folder.exists() == false) {
			folder.mkdir();
		}
		for (String url : photo.getUrls()) {
			String filename = url.substring(url.lastIndexOf("/"));
			File videoFile = new File(folder.getAbsolutePath() + File.separator + filename);

			downloadFile(url, videoFile.toPath());

		}

	}

	public static void downloadFile(String url, Path filePath) throws IOException {
		logger.log("Downloading " + url + " to " + filePath);

		ResteasyWebTarget videoTarget = client.target(url);
		Response videoResponse = videoTarget.request().header("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.102 Safari/537.36 Edg/104.0.1293.63")
				.get();

		String locationHeader = videoResponse.getHeaderString("Location");

		if (locationHeader != null) {
			videoResponse.close();
			downloadFile(locationHeader, filePath);
			return;
		}

		if (filePath.toFile().exists()) {
			long expectedLength = Long.parseLong(videoResponse.getHeaderString("Content-Length"));
			long downloadedLength = 0;
			downloadedLength = Files.size(filePath);
			logger.log("expected: " + expectedLength + " == current: " + downloadedLength + " ?");
			if (expectedLength <= downloadedLength) {
				logger.log("Datei " + filePath + " bereits heruntergeladen, überspringe...");
				//videoResponse.close();
				return;
			}
			logger.log("Dateien gleichen nicht, lade erneut runter...");
			filePath.toFile().delete();
		}

		InputStream videoIn = videoResponse.readEntity(InputStream.class);
		Files.copy(videoIn, filePath);
		videoResponse.close();

	}

	public static void saveDescription(JFFVideo video) throws IOException {
		File file = new File(DOWNLOAD_PATH + video.filename + ".txt");
		if (file.exists() == false) {
			file.createNewFile();
		}
		Files.writeString(file.toPath(), video.getDescription() + "\n" + video.getTimestamp() + "\n" + video.getTimeDescription());
	}

	public static void saveDescription(JFFPhoto photo) throws IOException {
		File file = new File(DOWNLOAD_PATH + photo.filename + File.separator
				+ photo.filename + ".txt");
		if (file.exists() == false) {
			file.createNewFile();
		}
		Files.writeString(file.toPath(), photo.getDescription() + "\n" + photo.getTimestamp() + "\n" + photo.getTimeDescription());
	}

}
