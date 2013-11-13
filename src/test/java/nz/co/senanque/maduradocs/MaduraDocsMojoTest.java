/**
 * 
 */
package nz.co.senanque.maduradocs;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author roger
 *
 */
public class MaduraDocsMojoTest {

	/**
	 * Test method for {@link nz.co.senanque.maduradocs.MaduraDocsMojo#execute()}.
	 * @throws MojoFailureException 
	 * @throws MojoExecutionException 
	 */
	@Test @Ignore
	public void testExecute() throws MojoExecutionException, MojoFailureException {
		MaduraDocsMojo maduraDocsMojo = new MaduraDocsMojo();
		maduraDocsMojo.setSourceDir("docs1/");
		maduraDocsMojo.setSourceDoc("maduradocs.xml");
		maduraDocsMojo.setScratchDirectory("target/scratchdocs/");
		maduraDocsMojo.setCompany("Prometheus Consulting");
		maduraDocsMojo.setUserName("Horatio Nelson");
		maduraDocsMojo.setCompany("Prometheus");

		maduraDocsMojo.setArtifactId("maduradocs");
		maduraDocsMojo.setProjectName("Madura Docs");
		maduraDocsMojo.setVersion("1.0");
		maduraDocsMojo.execute();
	}
}
