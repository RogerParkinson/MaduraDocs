/**
 * 
 */
package nz.co.senanque.maduradocs;

import java.io.StringReader;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * @author roger
 * 
 */
public class HistoryExtractorGitHub implements HistoryExtractor {

	private static final Logger log = LoggerFactory
			.getLogger(HistoryExtractorGitHub.class);

	private final String m_url;
	private final String m_path;
	private final String m_name;
	private final String m_repo;

	static class Contributor {
		String login;
		int contributions;
	}

	static class Committer {
		String name;
		String date;

		public String toString() {
			return name + " " + date;
		}
	}

	static class Commit {
		Committer committer;
		String message;

		public String toString() {
			return committer.toString() + " "
					+ (message.replace('\n', ' ').trim());
		}
	}

	static class CommitRecord {
		String sha;
		String url;
		Commit commit;

		public String toString() {
			return commit.toString() + " " + sha;
		}

		public String getMessage() {
			return commit.message;
		}

		public String getDate() {
			return commit.committer.date;
		}

		public String getAuthor() {
			return commit.committer.name;
		}
	}

	static class ContentRecord {
		String name;
		String path;
		String sha;
	}

	interface GitHub {
		@GET("/repos/{owner}/{repo}/commits")
		List<CommitRecord> commits(@Path("owner") String owner,
				@Path("repo") String repo);
	}

	protected HistoryExtractorGitHub(String url, String path, String name, String repo) {
		m_url = url;
		m_path = path;
		m_name = name;
		m_repo = repo;
	}

	public InputSource getHistory() {

		StringBuilder sb = new StringBuilder("<log>");

		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addQueryParam("path", m_path);
			}
		};
		RestAdapter restAdapter = new RestAdapter.Builder().setServer(m_url)
				.setRequestInterceptor(requestInterceptor).build();

		// Create an instance of our GitHub API interface.
		GitHub github = restAdapter.create(GitHub.class);

		try {
			List<CommitRecord> commits = github.commits(m_name,m_repo);
			for (CommitRecord commitRecord : commits) {
				if (StringUtils.isNotEmpty(commitRecord.getMessage())) {
					sb.append("<logentry>");
					sb.append("<author>").append(commitRecord.getAuthor())
							.append("</author>");
					sb.append("<date>").append(commitRecord.getDate().toString())
							.append("</date>");
					sb.append("<msg>").append(commitRecord.getMessage().replace('\n', ' '))
							.append("</msg>");
					sb.append("</logentry>");
				}
			}
		} catch (Exception e) {
			log.warn("Failed to get Git history: ", e.getMessage());
		}
		sb.append("</log>");

		StringReader stringReader = new StringReader(sb.toString());
		InputSource inputSource = new InputSource(stringReader);
		return inputSource;
	}

}
