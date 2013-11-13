/**
 * 
 */
package nz.co.senanque.maduradocs;

import java.io.StringReader;

import org.xml.sax.InputSource;

/**
 * @author roger
 * 
 */
public class HistoryExtractorNOOP implements HistoryExtractor {

	protected HistoryExtractorNOOP() {
	}

	public InputSource getHistory() {

		StringBuilder sb = new StringBuilder("<log>");

		sb.append("</log>");

		StringReader stringReader = new StringReader(sb.toString());
		InputSource inputSource = new InputSource(stringReader);
		return inputSource;
	}

}
