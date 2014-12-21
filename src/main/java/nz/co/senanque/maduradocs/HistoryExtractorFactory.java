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

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author Roger Parkinson
 *
 */
public class HistoryExtractorFactory {
	
	static HistoryExtractor getHistoryExtractor(String scmURL, String baseName, String subDir, Log log) {
		
		if (StringUtils.isEmpty(scmURL)) {
			return new HistoryExtractorNOOP();
		}
		if (scmURL.startsWith("scm:svn")) {
			return new HistoryExtractorSVN(scmURL, subDir+baseName, null, null,log);
		}
		if (scmURL.startsWith("scm:git")) {
			return new HistoryExtractorGit(scmURL, subDir+baseName,log);
		}
		return new HistoryExtractorNOOP();
	}
	
}
