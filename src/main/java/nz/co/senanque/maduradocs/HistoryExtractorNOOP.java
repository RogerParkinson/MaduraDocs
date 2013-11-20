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
