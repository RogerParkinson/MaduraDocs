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
package nz.co.senanque.maduradocs;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * This uses git protocol.
 * 
 * @author Roger Parkinson
 * 
 */
public class HistoryExtractorGit implements HistoryExtractor {

	private static Log log;

	private final String m_url;
	private final String m_path;
	private final String m_name;
//	private final String m_password;
	private final URL m_urlFinal;
	private String m_repo; 

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

	/**
	 * @param url eg scm:git:git@github.com:RogerParkinson/MaduraUtils.git
	 * @param path eg /README.cmd
	 * @param log2 
	 */
	protected HistoryExtractorGit(String url, String path, Log log2) {
		if (log2 != null) {
			log2.info("url: "+url+" path: "+path);
		}
		
		int k = url.indexOf(".git/");
		if (k > -1) {
			path = url.substring(k+4)+"/"+path;
		}

		m_url = url.substring(12);
		m_path = path;
		log = log2;
		
		// Convert the given url into an https url which should look something like:
		// https://api.github.com/RogerParkinson/MaduraUtils.git

		int i = m_url.indexOf(':');
		int j=0;
		if (m_url.startsWith("//")) {
			j=2;
		}
		if (m_url.startsWith("//git@")) {
			j=6;
		}
		if (i < 0) {
			i = m_url.indexOf('/', j+1);
		}
		String host = m_url.substring(j,i);
		if (host.equals("github.com")) {
			host = "api."+host;
		}
		String query = m_url.substring(i+1);
		try {
			m_urlFinal = new URL("https://"+host);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		StringTokenizer st = new StringTokenizer(query,"/.");
		m_name = st.nextToken();
		m_repo = st.nextToken();
	}
	
	@SuppressWarnings("unchecked")
	public InputSource getHistory() {

		StringBuilder sb = new StringBuilder("<log>");

		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addQueryParam("path", m_path);
			}
		};
		if (log != null) {
			log.info("url: "+m_urlFinal.toString());
			log.info("path: "+m_path.toString());
		}
		RestAdapter restAdapter = new RestAdapter.Builder().setServer(m_urlFinal.toString())
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
			log.warn("Failed to get Git history: ", e);
		}
		sb.append("</log>");

		StringReader stringReader = new StringReader(sb.toString());
		InputSource inputSource = new InputSource(stringReader);
		return inputSource;
	}

}
