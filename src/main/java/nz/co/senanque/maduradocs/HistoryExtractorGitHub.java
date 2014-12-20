/*******************************************************************************
 * Copyright (c)2013 Prometheus Consulting
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/**
 * 
 */
package nz.co.senanque.maduradocs;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.xml.sax.InputSource;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Specifically handles Github.com but it doesn't use git directly. Instead it
 * converts the url to an svn compatible one and uses svn protocol.
 * This may only work with github.com.
 * 
 * @author Roger Parkinson
 * 
 */
public class HistoryExtractorGitHub implements HistoryExtractor {

	private static final Logger log = LoggerFactory
			.getLogger(HistoryExtractorGitHub.class);

	private final String m_url;
	private final String m_path;
	private final String m_name;
	private final String m_password;
	private final String m_urlFinal;

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

	protected HistoryExtractorGitHub(String url, String path, String name,
			String password) {
		m_url = url;
		m_path = path;
		m_name = name;
		m_password = password;
		m_urlFinal = "https://"+url.substring(12).replace(':', '/').replace(".git", "/trunk/");
	}

	@SuppressWarnings("unchecked")
	public InputSource getHistory() {

		StringBuilder sb = new StringBuilder("<log>");

		long startRevision = 0;
		long endRevision = -1; // HEAD (the latest) revision

		SVNRepository repository = null;
		try {
			repository = SVNRepositoryFactory.create(SVNURL
					.parseURIEncoded(m_urlFinal));
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(m_name, m_password);
			repository.setAuthenticationManager(authManager);
			Collection<SVNLogEntry> logEntries = null;

			logEntries = (Collection<SVNLogEntry>) repository.log(
					new String[] { m_path }, null, startRevision, endRevision,
					true, true);
			for (SVNLogEntry logEntry : logEntries) {
				if (StringUtils.isNotEmpty(logEntry.getMessage())) {
					sb.append("<logentry>");
					sb.append("<author>").append(logEntry.getAuthor()).append("</author>");
					sb.append("<date>").append(logEntry.getDate().toString()).append("</date>");
					sb.append("<msg>").append(logEntry.getMessage()).append("</msg>");
					sb.append("</logentry>");
				}
			}
		} catch (Exception e) {
			log.warn("Failed to get SVN history: ", e.getMessage());
		}
		sb.append("</log>");

		StringReader stringReader = new StringReader(sb.toString());
		InputSource inputSource = new InputSource(stringReader);
		return inputSource;

//		RequestInterceptor requestInterceptor = new RequestInterceptor() {
//			@Override
//			public void intercept(RequestFacade request) {
//				request.addQueryParam("path", m_path);
//			}
//		};
//		RestAdapter restAdapter = new RestAdapter.Builder().setServer(m_url)
//				.setRequestInterceptor(requestInterceptor).build();
//
//		// Create an instance of our GitHub API interface.
//		GitHub github = restAdapter.create(GitHub.class);
//
//		try {
//			List<CommitRecord> commits = github.commits(m_name,m_repo);
//			for (CommitRecord commitRecord : commits) {
//				if (StringUtils.isNotEmpty(commitRecord.getMessage())) {
//					sb.append("<logentry>");
//					sb.append("<author>").append(commitRecord.getAuthor())
//							.append("</author>");
//					sb.append("<date>").append(commitRecord.getDate().toString())
//							.append("</date>");
//					sb.append("<msg>").append(commitRecord.getMessage().replace('\n', ' '))
//							.append("</msg>");
//					sb.append("</logentry>");
//				}
//			}
//		} catch (Exception e) {
//			log.warn("Failed to get Git history: ", e.getMessage());
//		}
//		sb.append("</log>");
//
//		StringReader stringReader = new StringReader(sb.toString());
//		InputSource inputSource = new InputSource(stringReader);
//		return inputSource;
	}

}
