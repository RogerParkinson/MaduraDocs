/**
 * 
 */
package nz.co.senanque.maduradocs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * @author roger
 *
 */
public class HistoryExtractorFactory {
	
	static HistoryExtractor getHistoryExtractor(String scmURL, String baseName, String subDir) {
		
		if (scmURL == null) {
			return new HistoryExtractorNOOP();
		}
		if (scmURL.indexOf("svn") > -1) {
			// assume it is SVN, takes the form: http://madura-rules.googlecode.com/svn/trunk
			return new HistoryExtractorSVN(scmURL, subDir+baseName, null, null);
		}
		if (scmURL.indexOf("github") > -1) {
			// assume it is github, takes the form: https://api.github.com/repos/RogerParkinson/HeartMonitor/commits?path=README.md
			// assume it is github, takes the form: https://github.com/RogerParkinson/HeartMonitor
			// beanvalidation is the user name
			URL url;
			try {
				url = new URL(scmURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
			String host = url.getHost();
			if (!host.startsWith("api.")) {
				host = "api."+host;
			}
			String urlQuery = url.getPath();
			String urlString = url.getProtocol()+"://"+host;
			StringTokenizer st = new StringTokenizer(urlQuery,"/");
			String name = st.nextToken();
			String repo = st.nextToken();
			return new HistoryExtractorGitHub(urlString, subDir+baseName,name,repo);
		}
		return new HistoryExtractorNOOP();
	}

}
