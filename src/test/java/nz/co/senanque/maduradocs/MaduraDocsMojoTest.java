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
