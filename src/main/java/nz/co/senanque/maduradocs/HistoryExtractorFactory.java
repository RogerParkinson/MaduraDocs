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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import org.codehaus.plexus.util.StringUtils;

/**
 * @author roger
 *
 */
public class HistoryExtractorFactory {
	
	static HistoryExtractor getHistoryExtractor(String scmURL, String baseName, String subDir) {
		
		String repoType = getRepoType(scmURL);
		String bareURL = getBareURL(scmURL);
		
		if (StringUtils.isEmpty(scmURL)) {
			return new HistoryExtractorNOOP();
		}
		if (scmURL.startsWith("scm:svn")) {
			// assume it is SVN, takes the form: http://madura-rules.googlecode.com/svn/trunk
			return new HistoryExtractorSVN(scmURL, subDir+baseName, null, null);
		}
		if (scmURL.startsWith("scm:git")) {
			return new HistoryExtractorGit(scmURL, subDir+baseName);
			// assume it is github, takes the form: https://api.github.com/repos/RogerParkinson/HeartMonitor/commits?path=README.md
			// assume it is github, takes the form: https://github.com/RogerParkinson/HeartMonitor
			// beanvalidation is the user name
//			URL url;
//			try {
//				url = new URL(bareURL);
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//				return null;
//			}
//			String host = url.getHost();
//			if (!host.startsWith("api.")) {
//				host = "api."+host;
//			}
//			String urlQuery = url.getPath();
//			String urlString = url.getProtocol()+"://"+host;
//			StringTokenizer st = new StringTokenizer(urlQuery,"/");
//			String name = st.nextToken();
//			String repo = st.nextToken();
//			return new HistoryExtractorGitHub(urlString, subDir+baseName,name,repo);
		}
		return new HistoryExtractorNOOP();
	}
	
	private static String getBareURL(String url) {
		if (url == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(url,":");
		st.nextToken();
		st.nextToken();
		String host = st.nextToken();
		String body = st.nextToken();
		return host+":"+body;
	}

	static String getRepoType(String url) {
		if (url == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(url,":");
		st.nextToken();
		String ret = st.nextToken();
		return ret;
	}

}
