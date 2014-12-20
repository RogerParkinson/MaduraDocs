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

/**
 * @author roger
 * 
 */
public class HistoryExtractorSVN implements HistoryExtractor {

	private static final Logger log = LoggerFactory
			.getLogger(HistoryExtractorSVN.class);

	private final String m_url;
	private final String m_urlFinal;
	private final String m_path;
	private final String m_name;
	private final String m_password;

	protected HistoryExtractorSVN(String url, String path, String name,
			String password) {
		m_url = url;
		m_path = path;
		m_password = password;
		m_name = name;
		m_urlFinal = url.substring(8);
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
	}

}
