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
		HistoryExtractor historyExtractor = HistoryExtractorFactory.getHistoryExtractor("https://maduradocs.googlecode.com/svn/trunk", "src/MaduraDocs.xml");
		InputSource inputSource = historyExtractor.getHistory();
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		assertEquals(6,nodeList.getLength());
	}
	@Test @Ignore
	public void testGetHistoryExtractorGitHub() throws Exception {
		HistoryExtractor historyExtractor = HistoryExtractorFactory.getHistoryExtractor("https://github.com/RogerParkinson/HeartMonitor", "README.md");
		InputSource inputSource = historyExtractor.getHistory();
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		assertEquals(2,nodeList.getLength());
	}

}
