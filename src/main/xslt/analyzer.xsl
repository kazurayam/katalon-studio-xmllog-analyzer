<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" omit-xml-declaration="yes" indent="yes"/>
    <xsl:strip-space elements="*"/>
    <xsl:include href="identity-transform.xsl"/>

    <!--
<log>
  <record>
    <date>2021-11-21T20:49:26</date>
    <millis>1637495366328</millis>
    <sequence>0</sequence>
    <level>START</level>
    <class>com.kms.katalon.core.logging.XmlKeywordLogger</class>
    <method>startSuite</method>
    <thread>1</thread>
    <message>Start Test Suite : Test Suites/TS1</message>
    <nestedLevel>0</nestedLevel>
    <escapedJava>false</escapedJava>
    <property name="rerunTestFailImmediately">false</property>
    <property name="retryCount">0</property>
    <property name="name">TS1</property>
    <property name="description"></property>
    <property name="id">Test Suites/TS1</property>
  </record>
  ...
    -->
    <xsl:template match="/">
        <html>
            <body>
                <xsl:apply-templates select="log"/>
            </body>
        </html>
    </xsl:template>

    <xsl:key name="level" match="/log/record/level" use="."/>
    <xsl:key name="message" match="/log/record/message" use="."/>

    <xsl:template match="log">
        <h1>count by level</h1>
        <table>
            <tbody>
                <xsl:for-each select="/log/record/level[generate-id()=generate-id(key('level',.)[1])]">
                    <xsl:variable name="v" select="."/>
                    <tr>
                        <td><xsl:value-of select="."/></td>
                        <td><xsl:value-of select="count(/log/record/level[text()=$v])"/></td>
                    </tr>
                </xsl:for-each>
            </tbody>
        </table>
        <h1>count by message</h1>
        <table>
            <tbody>
                <xsl:for-each select="/log/record/message[generate-id()=generate-id(key('message',.)[1])]">
                    <xsl:variable name="v" select="."/>
                    <tr>
                        <td><xsl:value-of select="."/></td>
                        <td><xsl:value-of select="count(/log/record/message[text()=$v])"/></td>
                    </tr>
                </xsl:for-each>
            </tbody>
        </table>
    </xsl:template>

</xsl:stylesheet>