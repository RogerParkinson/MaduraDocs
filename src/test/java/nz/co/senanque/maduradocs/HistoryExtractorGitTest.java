package nz.co.senanque.maduradocs;

import static org.junit.Assert.*;

import org.junit.Test;

public class HistoryExtractorGitTest {

	@Test
	public void testGetHistory() {
		HistoryExtractorGit git = new HistoryExtractorGit("scm:git:git@github.com:RogerParkinson/MaduraUtils.git","/docs/madura-utils.xml");
		git.getHistory();
	}

}
