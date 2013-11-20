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

import static org.junit.Assert.assertEquals;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class HistoryExtractorFactoryTest {
	
	// tests are marked ignored because they depend on external stability which we cannot guarantee.

	@Test @Ignore
	public void testGetHistoryExtractorSVN() throws Exception {
		HistoryExtractor historyExtractor = HistoryExtractorFactory.getHistoryExtractor("https://maduradocs.googlecode.com/svn/trunk", "MaduraDocs.xml", "/src/");
		InputSource inputSource = historyExtractor.getHistory();
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		assertEquals(6,nodeList.getLength());
	}
	@Test @Ignore
	public void testGetHistoryExtractorGitHub() throws Exception {
		HistoryExtractor historyExtractor = HistoryExtractorFactory.getHistoryExtractor("https://github.com/RogerParkinson/HeartMonitor", "README.md", "/");
		InputSource inputSource = historyExtractor.getHistory();
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		assertEquals(2,nodeList.getLength());
	}

}
