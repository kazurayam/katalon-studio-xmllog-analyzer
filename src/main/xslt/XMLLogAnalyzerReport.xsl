<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="IdentityTransform.xsl"/>
    <xsl:output method="xml"/>

    <xsl:template name="compileHTMLReport">
        <xsl:param name="count"/>
        <html>
            <body>
                <xsl:copy-of select="$count"/>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>