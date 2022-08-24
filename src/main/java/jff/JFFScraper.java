package jff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

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

	public static String PAGE_0_URL =
			// "https://justfor.fans/ajax/getPosts.php?Type=One&UserID=755747&PosterID=499601&StartAt=0&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c&SplitTest=0";
			// "https://justfor.fans//ajax/getPosts.php?UserID=755747&PosterID=499601&Type=One&StartAt=30&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c";
			//"https://justfor.fans/ajax/getPosts.php?Type=One&UserID=755747&PosterID=1299210&StartAt=0&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c&SplitTest=0";
	//"https://justfor.fans/ajax/getPosts.php?Type=One&UserID=755747&PosterID=543007&StartAt=0&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c&SplitTest=0";
	//"https://justfor.fans/ajax/getPosts.php?Type=One&UserID=755747&PosterID=1760042&StartAt=0&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c&SplitTest=0";
	//"https://justfor.fans/ajax/getPosts.php?Type=One&UserID=755747&PosterID=1739671&StartAt=0&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c&SplitTest=0";
	//"https://justfor.fans/ajax/getPosts.php?Type=One&UserID=755747&PosterID=1587363&StartAt=0&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c&SplitTest=0";
	//"https://justfor.fans/ajax/getPosts.php?Type=One&UserID=755747&PosterID=499601&StartAt=0&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c&SplitTest=0";
	"https://justfor.fans//ajax/getPosts.php?UserID=755747&PosterID=499601&Type=One&StartAt=486&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c";
			// public static String PAGE_0_URL =
	// "https://justfor.fans//ajax/getPosts.php?UserID=755747&PosterID=499601&Type=One&StartAt=70&Page=Profile&UserHash4=7b194f3f6613d31557cb20897bc1c78c";
	public static String COOKIE_TOKEN = "PHPSESSID=nq1gmsgomjkt75ii0kdao1evp5; UserHash3=66207ba24e018bf9c4e32bed9fc7bcc2; UserHash4=7b194f3f6613d31557cb20897bc1c78c; cookiesAccepted=active";
	public static String DOWNLOAD_PATH = "\\\\10.100.100.100\\Kuschelburrito\\jff-downloads\\dumbboundjock\\";

	public static ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
	public static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws Exception {
		String html = downloadHtml(PAGE_0_URL);
		long page = 1;
		while (true) {
			ExecutorService taskExecutor = Executors.newFixedThreadPool(10);
			List<JFFVideo> videoUrls = scrapeVideoUrls(html);
			int i = 0;
			for (JFFVideo video : videoUrls) {
				int i2 = i;
				long page2 = page;
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							System.out.println(" #################### Video " + (i2 + 1) + " of " + videoUrls.size()
									+ " (page " + page2 + "). #################### ");
							downloadVideo(video);
							saveDescription(video);

						} catch (Exception ex) {
							ex.printStackTrace();
							System.out.println("Fehler beim Downloaden des Videos, versuche nächstes...");
						}
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
				try {
					System.out.println(" #################### Photo " + (i + 1) + " of " + photoUrls.size() + " (page "
							+ page + "). #################### ");
					downloadPhoto(photo);
					saveDescription(photo);
					i++;
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("Fehler beim Downloaden des Videos, versuche nächstes...");
				}
			}

			String nextUrl = scrapeMoreLink(html)
					.orElseThrow(() -> new Exception("Keine weitere Download-Page gefunden. Beende..."));
			html = downloadHtml(nextUrl);
			page++;
		}
	}

	public static String downloadHtml(String url) {
		System.out.println("Downloade neues HTML " + url);
		ResteasyWebTarget target = client.target(url);
		Response response = target.request().header("cookie", COOKIE_TOKEN).header("accept", "text/html, */*; q=0.01")
				.header("referer", "https://justfor.fans/pervertmedia")
				.header("user-agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.102 Safari/537.36 Edg/104.0.1293.63")
				.get();
		System.out.println(response.getHeaders());
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

				JFFVideo video = new JFFVideo();

				Elements videoBlocks = videoCard.getElementsByClass("videoBlock");

				for (Element videoBlock : videoBlocks) {
					video.setFilename(videoBlock.attr("id"));
					String javascript = videoBlock.getElementsByTag("a").get(0).attr("onClick");
					javascript = javascript.substring(javascript.indexOf("{"), javascript.lastIndexOf("}") + 1);
					JsonNode node = mapper.readTree(javascript);
					String url = null;
					if (node.get("1080p") != null) {
						url = node.get("1080p").asText();

					} else {
						url = node.get(0).asText();
					}
					video.setUrl(url);
					video.setThumbnailUrl(videoBlock.getElementsByTag("img").get(0).attr("src"));
					break;
				}
				try {
					video.setDescription(videoCard.getElementsByClass("mbsc-card-content").get(0)
							.getElementsByClass("post-text-content").get(0).text());

				} catch (Exception e) {
					System.out.println("Keine Description für video " + video.getUrl() + " gefunden.");
				}
				if (video.getUrl() == null) {
					continue;
				}
				urlList.add(video);
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Scrapen des Videos, überspringe...");
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

					Element gallery = photoCard.getElementsByClass("galleryWrapper").get(0)
							.getElementsByClass("galleryLarge").get(0);
					photo.setFilename(photoCard.attr("id"));
					photo.setUrls(gallery.getElementsByTag("img").stream().map(img -> img.attr("src"))
							.collect(Collectors.toList()));

					try {
						photo.setDescription(photoCard.getElementsByClass("mbsc-card-content").get(0)
								.getElementsByClass("post-text-content").get(0).text());

					} catch (Exception e) {
						System.out.println("Keine Description für Fotogallerie gefunden.");
					}
					urlList.add(photo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fehler beim Scrapen der Fotos, überspringe...");
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
		System.out.println("Downloading " + url + " to " + filePath);

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
			long expectedLength = Long.valueOf(videoResponse.getHeaderString("Content-Length"));
			long downloadedLength = 0;
			downloadedLength = Files.size(filePath);
			System.out.println("expected: " + expectedLength + " == current: " + downloadedLength + " ?");
			if (expectedLength <= downloadedLength) {
				System.out.println("Datei " + filePath + " bereits heruntergeladen, überspringe...");
				//videoResponse.close();
				return;
			}
			System.out.println("Dateien gleichen nicht, lade erneut runter...");
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
		Files.writeString(file.toPath(), video.getDescription());
	}

	public static void saveDescription(JFFPhoto video) throws IOException {
		File file = new File(DOWNLOAD_PATH + video.filename + File.separator
				+ video.filename + ".txt");
		if (file.exists() == false) {
			file.createNewFile();
		}
		Files.writeString(file.toPath(), video.getDescription());
	}

}
