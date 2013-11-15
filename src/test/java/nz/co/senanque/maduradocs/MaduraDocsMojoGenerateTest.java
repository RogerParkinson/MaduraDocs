/**
 * 
 */
package nz.co.senanque.maduradocs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;

/**
 * @author roger
 *
 */
public class MaduraDocsMojoGenerateTest {

	/**
	 * Test method for {@link nz.co.senanque.maduradocs.MaduraDocsMojo#execute()}.
	 * @throws MojoFailureException 
	 * @throws MojoExecutionException 
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testExecute() throws MojoExecutionException, MojoFailureException, FileNotFoundException, IOException, XmlPullParserException {
		
		MavenXpp3Reader r = new MavenXpp3Reader();
		Model model = r.read(new FileReader(new File("pom.xml")));
		MaduraDocsMojo maduraDocsMojo = new MaduraDocsMojo();
		maduraDocsMojo.setSourceDir("docs/");
		maduraDocsMojo.setSourceDoc("maduradocs.xml");
		maduraDocsMojo.setScratchDirectory("target/scratchdocs/");
		maduraDocsMojo.setCompany(model.getOrganization().getName());
		maduraDocsMojo.setUserName("Roger Parkinson");
		maduraDocsMojo.setTargetDir("target/");
		maduraDocsMojo.setBaseDir("./");

		maduraDocsMojo.setArtifactId(model.getArtifactId());
		maduraDocsMojo.setProjectName(model.getName());
		maduraDocsMojo.setVersion(model.getVersion());
		maduraDocsMojo.execute();
	}
}
