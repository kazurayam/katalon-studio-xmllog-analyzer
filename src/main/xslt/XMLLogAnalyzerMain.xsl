<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="XMLLogAnalyzerCount.xsl"/>
    <xsl:import href="XMLLogAnalyzerReport.xsl"/>

    <xsl:template match="/">
        <xsl:variable name="result">
            <xsl:apply-templates select="log" mode="count"/>
        </xsl:variable>
        <xsl:call-template name="compileHTMLReport">
            <xsl:with-param name="count" select="$result"/>
        </xsl:call-template>
    </xsl:template>

</xsl:stylesheet>