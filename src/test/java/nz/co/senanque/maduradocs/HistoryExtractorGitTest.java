package nz.co.senanque.maduradocs;

import static org.junit.Assert.*;

import org.junit.Test;

public class HistoryExtractorGitTest {

	@Test
	public void testGetHistory() {
		HistoryExtractorGit git = new HistoryExtractorGit("scm:git:ssh://git@github.com/RogerParkinson/MaduraDocs.git","/docs/madura-utils.xml");
		git.getHistory();
	}

}
