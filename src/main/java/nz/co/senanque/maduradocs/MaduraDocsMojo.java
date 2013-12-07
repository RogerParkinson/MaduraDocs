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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.peachjean.slf4j.mojo.AbstractLoggingMojo;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Goal which generates pdf documentation from the xml source file
 * 
 * @goal pdf
 * 
 * @phase package
 */
public class MaduraDocsMojo extends AbstractLoggingMojo {
	
	private static final Logger log = LoggerFactory
			.getLogger(MaduraDocsMojo.class);

	private static final String SCHEMA_LOCATION = "http://oss.sonatype.org/content/repositories/releases/nz/co/senanque/maduradocs/{0}/maduradocs-{0}.xsd";

	private String artifactId;
	private String projectName;
	private String version;
	private String company;
	private String scmURL = null;
	private InputSource history=null;

	/**
	 * baseDir.
	 * 
	 * @parameter expression="${basedir}"
	 * @readonly
	 */
	private String baseDir;

	/**
	 * userName.
	 * 
	 * @parameter expression="${user.name}"
	 * @required
	 */
	private String userName;

	/**
	 * Location of the targetDirectory.
	 * 
	 * @parameter expression="${basedir}/target
	 * @readonly
	 * 
	 */
	private String targetDir;
	
	/**
	 * Location of the scratchDir.
	 * 
	 * @parameter expression="${basedir}/target/scratchdocs"
	 * @readonly
	 * 
	 */
	private String scratchDirectory;
	
	/**
	 * Name of the sourceDoc.
	 * @parameter
	 * 
	 */
	private String sourceDoc;
	
	/**
	 * Name of the sourceDir.
	 * 
	 * @parameter expression="${basedir}/docs/"
	 * @required
	 */
	private String sourceDir;

	private Object pluginVersion;

	private void createNewSources(String baseName) {
		File sourceDirectoryFile = new File(getSourceDir());
		if (!sourceDirectoryFile.exists()) {
			sourceDirectoryFile.mkdirs();
		}
		File imagesDirectoryFile = new File(getSourceDir()+File.separatorChar+"images");
		if (!imagesDirectoryFile.exists()) {
			imagesDirectoryFile.mkdirs();
		}
		copyFile("MaduraHTML.xsl",sourceDirectoryFile);
		copyFile("style.xsl",sourceDirectoryFile);
		copyFile("rant.gif",imagesDirectoryFile);
		copyFile("note.png",imagesDirectoryFile);
		copyFile("warning.gif",imagesDirectoryFile);
		copyFile("logo.jpg",imagesDirectoryFile);
		
		try {
			copyTemplate(baseName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void copyTemplate(String baseName) throws Exception {
		
		File templateFile = new File(getSourceDir()+File.separatorChar+baseName+".xml");
		OutputStream templateFileStream = new FileOutputStream(templateFile);

		TransformerFactory factory = TransformerFactory.newInstance();
		factory.setURIResolver(new URIResolver(){

			@Override
			public Source resolve(String href, String base)
					throws TransformerException {
				File f = new File(getSourceDir()+href);
				StreamSource ret;
				try {
					ret = new StreamSource(new FileInputStream(f));
				} catch (FileNotFoundException e) {
					return null;
				}
				return ret;
			}});
		StreamSource xslStream = new StreamSource(this.getClass().getResourceAsStream("Template.xsl"));
		Transformer transformer = factory.newTransformer(xslStream);

		transformer.setParameter("product.name", projectName);
		transformer.setParameter("user.name", getUserName());
		transformer.setParameter("schema.location", MessageFormat.format(SCHEMA_LOCATION,pluginVersion));
		this.getLog().info(transformer.getParameter("schema.location").toString());
		
		DocumentBuilderFactory spf = DocumentBuilderFactory.newInstance();
		spf.setNamespaceAware(true);
		spf.setXIncludeAware(true);
		DocumentBuilder parser = spf.newDocumentBuilder();
		Document sourceDocument = parser.newDocument();

		Source in = new DOMSource(sourceDocument);
		StreamResult out = new StreamResult(templateFileStream);
		transformer.transform(in, out);
		this.setSourceDoc(baseName+".xml");

	}
	
	private void copyFile(String fileName, File outputDir) {
		try {
			InputStream is = getClass().getResourceAsStream(fileName);
			OutputStream os = new FileOutputStream(outputDir.getAbsolutePath()+File.separatorChar+fileName);
			byte b[] = new byte[1024];
			int len = is.read(b);
			while (len >0) {
				os.write(b, 0, len);
				len = is.read(b);
			}
			is.close();
			os.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void loadPluginProperties() {
		String path = "/META-INF/maven/nz.co.senanque/maduradocs/pom.properties";
		InputStream stream = getClass().getResourceAsStream(path);
		Properties props = new Properties();
		try {
			props.load(stream);
		} catch (Exception e) {
			getLog().info(
					"could not open maduradocs properties file, so xsd location (based on version) might be incorrect");
			pluginVersion = "0.0.0";
			return;
		}
		pluginVersion = props.get("version");
		getLog().info("using maduradocs plugin version "+pluginVersion);

	}

	public void executeWithLogging() throws MojoExecutionException, MojoFailureException {
		
		getLog().info("===Starting document generation==");
		loadPluginProperties();
//		setSourceDir("target/docs1/");
		if (getPluginContext() != null) {
			MavenProject project = (MavenProject)getPluginContext().get("project");
			projectName = project.getName();
	
			Artifact artifact = project.getArtifact();
			version = artifact.getVersion();
			artifactId = artifact.getArtifactId();
			company="";
			try {
				company = project.getModel().getOrganization().getName();
//				scmURL = project.getModel().getScm().getUrl();
				scmURL = project.getModel().getScm().getConnection();
			} catch (Exception e1) {
				company="Set Organization in POM file";
			}
		}
		
		File targetDirectoryFile = new File(getTargetDir());
		if (!targetDirectoryFile.exists()) {
			targetDirectoryFile.mkdirs();
		}
		
		String baseName = getSourceDoc();
		if (baseName == null) {
			baseName = artifactId;
		}
		StringTokenizer st = new StringTokenizer(baseName,",");
		while (st.hasMoreTokens()) {
			processOneFile(st.nextToken());
		}
	}
		
	private void processOneFile(String baseName) {
		int i = baseName.indexOf('.');
		if (i > -1) {
			baseName = baseName.substring(0, i);
		}
		
//        String url = "https://maduradocs.googlecode.com/svn/trunk";
//        String path = "src/MaduraDocs.xml";
		
		int l = getBaseDir().length();
		String sourceSubDir = getSourceDir().substring(l+1);

		HistoryExtractor historyExtractor = HistoryExtractorFactory.getHistoryExtractor(scmURL, baseName+".xml",sourceSubDir);
		history = historyExtractor.getHistory();
		
		File sourceFile = new File(getSourceDir()+File.separatorChar+baseName+".xml");
		if (!sourceFile.exists()) {
			// If there was no source file then create one and all the other bits in the sourcedir
			createNewSources(baseName);
		}
		File scratchDirectoryFile = new File(getScratchDirectory());
		if (!scratchDirectoryFile.exists()) {
			scratchDirectoryFile.mkdirs();
		}
		String foFile = getScratchDirectory()+File.separatorChar+baseName+".fo";
		
		DocumentBuilderFactory spf = DocumentBuilderFactory.newInstance();
		spf.setNamespaceAware(true);
		spf.setXIncludeAware(true);
		try {
			DocumentBuilder parser = spf.newDocumentBuilder();
			parser.setEntityResolver(new EntityResolver(){

				@Override
				public InputSource resolveEntity(String publicId,
						String systemId) throws SAXException, IOException {
					return null;
				}});
			Document sourceDocument = parser.parse(sourceFile);
			attachLogInformation(parser, sourceDocument,history);
		
			InputStream xslFile = this.getClass().getResourceAsStream("MaduraPDF.xsl");
			OutputStream outputHTML = new FileOutputStream(foFile);
			
			TransformerFactory factory = TransformerFactory.newInstance();
			factory.setURIResolver(new URIResolver(){

				@Override
				public Source resolve(String href, String base)
						throws TransformerException {
					File f = new File(getSourceDir()+File.separatorChar+href);
					StreamSource ret;
					try {
						ret = new StreamSource(new FileInputStream(f));
					} catch (FileNotFoundException e) {
						return null;
					}
					return ret;
				}});
			StreamSource xslStream = new StreamSource(xslFile);
			Transformer transformer = factory.newTransformer(xslStream);

			transformer.setParameter("ProductVersion", artifactId+"-"+stripSnapshot(version));
			transformer.setParameter("Company", company);
			transformer.setParameter("Year", getYear());
			transformer.setParameter("BaseDir", getSourceDir()+File.separatorChar);
			Source in = new DOMSource(sourceDocument);
			StreamResult out = new StreamResult(outputHTML);
			transformer.transform(in, out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		FopFactory fopFactory = FopFactory.newInstance();
		try {
			String outputPDF = getTargetDir()+File.separatorChar+baseName+"-"+version+".pdf";
			OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outputPDF)));

			try {
			  Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
			  TransformerFactory factory = TransformerFactory.newInstance();
			  factory.setErrorListener(new ErrorListener(){

				@Override
				public void warning(TransformerException exception)
						throws TransformerException {
					log.error(exception.getMessageAndLocation(), exception);					
				}

				@Override
				public void error(TransformerException exception)
						throws TransformerException {
					log.error(exception.getMessageAndLocation(), exception);					
				}

				@Override
				public void fatalError(TransformerException exception)
						throws TransformerException {
					log.error(exception.getMessageAndLocation(), exception);					
				}});
			  Transformer transformer = factory.newTransformer(); // identity transformer
			  Source src = new StreamSource(new File(foFile));

			  // Resulting SAX events (the generated FO) must be piped through to FOP
			  Result res = new SAXResult(fop.getDefaultHandler());

			  // Step 6: Start XSLT transformation and FOP processing
			  transformer.transform(src, res);

				getLog().info("Output PDF "+outputPDF);
			} finally {
			  //Clean-up
			  out.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		getLog().info("===End document generation==");
	}
	
	private String stripSnapshot(String v) {
		int i = v.indexOf("-SNAPSHOT");
		if (i > -1) {
			return v.substring(0,i);
		}
		return v;
	}

	private void attachLogInformation(DocumentBuilder parser, Document sourceDocument,
			InputSource history2) throws SAXException, IOException {
		if (history2 != null) {
			Node titleNode = sourceDocument.getDocumentElement().getElementsByTagName("title").item(0);
			Document historyDoc = parser.parse(history2);
			Element historyRoot = historyDoc.getDocumentElement();
			sourceDocument.adoptNode(historyRoot);
			titleNode.appendChild(historyRoot);
		}
	}

	public String getSourceDoc() {
		return sourceDoc;
	}

	public void setSourceDoc(String sourceDoc) {
		this.sourceDoc = sourceDoc;
	}

	public String getScratchDirectory() {
		return scratchDirectory;
	}

	public void setScratchDirectory(String scratchDirectory) {
		this.scratchDirectory = scratchDirectory;
	}
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getYear() {
		Calendar calendar = new GregorianCalendar();
		return String.valueOf(calendar.get(Calendar.YEAR));
	}

	public void setYear(String year) {
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	protected String getArtifactId() {
		return artifactId;
	}

	protected void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	protected String getVersion() {
		return version;
	}

	protected void setVersion(String version) {
		this.version = version;
	}

	protected String getProjectName() {
		return projectName;
	}

	protected void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	protected String getTargetDir() {
		return targetDir;
	}

	protected void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	protected String getBaseDir() {
		return baseDir;
	}

	protected void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

}
