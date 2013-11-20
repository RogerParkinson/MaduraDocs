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
