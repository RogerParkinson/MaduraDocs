<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright 2010 Prometheus Consulting Licensed under the Apache License, 
	Version 2.0 (the "License"); you may not use this file except in compliance 
	with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
	Unless required by applicable law or agreed to in writing, software distributed 
	under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
	OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
	the specific language governing permissions and limitations under the License. -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" 
		omit-xml-declaration="yes"
		indent="yes" />
	<xsl:param name="product.name">Product-Name</xsl:param>
	<xsl:param name="user.name">Your Name</xsl:param>
	<xsl:param name="schema.location">currentlocation</xsl:param>
	<xsl:template match="/">
		<xsl:processing-instruction name="xml-stylesheet">
			type="text/xsl" href="MaduraHTML.xsl"
		</xsl:processing-instruction>
		<doc xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:xi="http://www.w3.org/2001/XInclude" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	 		<xsl:attribute name="xsi:noNamespaceSchemaLocation">
	 	        	<xsl:value-of select="$schema.location" />
			</xsl:attribute>
			<title>
				<MainTitle>
					<xsl:value-of select="$product.name" />
				</MainTitle>
				<SubTitle>User Guide</SubTitle>
				<Author>
					<xsl:value-of select="$user.name" />
				</Author>
				<Revision>$Revision: 1$</Revision>
				<image>logo.jpg</image>
				<!-- <xi:include href="scm:MaduraDocs.log"/> -->
				<references>
					<!-- List of references -->
					<reference t="maduradocs" url="http://code.google.com/p/maduradocs/" />
					<reference t="Apache Licence 2.0" url="http://www.apache.org/licenses/LICENSE-2.0" />
					<reference t="XInclude" url="http://www.w3.org/TR/xinclude/" />
					<reference t="Spring Framework" url="http://www.springsource.org/" />
				</references>

			</title>
			<body>
				<process-log />
				<process-references />
				<h1 t="First Heading">
					<p>This is a paragraph.</p>
					<p>
						This is a reference to
						<figureLink t="Sample Image" />
					</p>
					<img width="450px" href="images/logo.jpg">Sample Image</img>
					<h2 t="First subheading">
						<p>This is a list:</p>
						<list>
							<le>Entry one.</le>
							<le>Entry two.</le>
						</list>
						<p>This is another list:</p>
						<list>
							<ln>Entry one.</ln>
							<ln>Entry two.</ln>
						</list>
						<p>And another</p>
						<list>
							<ll name="first">Entry one.</ll>
							<ll name="second">Entry two.</ll>
						</list>
						<p>Sample code:</p>
						<code><![CDATA[
formatting is suppressed and courier font is (normally) used.
		]]></code>
						<p>
							You can also do something
							<courier>similar</courier>
							in-line.
						</p>
						<p>
							Reference to earlier
							<sectionLink t="First Heading" />
						</p>
						<p>
							Reference to reference
							<referenceLink t="maduradocs" />
						</p>
						<note>This is a sidebar note.</note>
						<warning>This is a warning note.</warning>
						<rant>This is a rant. It is not rendered in the PDF version so it
							is safe to write anything you like
							in these even in a production document.
						</rant>
					</h2>
					<h2 t="Tables">
						<table width="12cm">
							<tw>6cm</tw>
							<tw>6cm</tw>
							<tr>
								<th>Name</th>
								<th>Address</th>
							</tr>
							<tr>
								<td>Frank Kelly</td>
								<td>Ruatane</td>
							</tr>
							<tr>
								<td>Sarah Millish</td>
								<td>Auckland</td>
							</tr>
						</table>
					</h2>
				</h1>
				<a1 t="Licence">
					<p>Use an appendix to list the dependent licences.</p>
					<p>The dependent products have the following licences:</p>
					<list>
						<ll name="apache commons">Apache Software License, Version 2.0</ll>
						<ll name="apache fop">Apache Software License, Version 2.0</ll>
						<ll name="jdom">http://www.activemath.org/readmes/legal/jdom/LICENSE.txt
						</ll>
						<ll name="xalan">Apache Software License, Version 2.0</ll>
						<ll name="xerces">Apache Software License, Version 1.1</ll>
						<ll name="xinclude">Apache Software License, Version 1.1</ll>
						<ll name="xincluder-fi">The GNU General Public License, Version 2</ll>
						<ll name="xmlgraphics-commons">The Apache Software License, Version 2.0</ll>
					</list>
				</a1>
				<a1 t="Release Notes">
					<table width="12cm">
						<tw>12cm</tw>
						<tr>
							<th>0.1</th>
						</tr>
						<tr>
							<td>Intial version.</td>
						</tr>
					</table>
				</a1>
			</body>

		</doc>
	</xsl:template>
</xsl:stylesheet>