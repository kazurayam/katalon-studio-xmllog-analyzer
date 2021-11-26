package com.kazurayam.ks.logging

import java.nio.file.Files
import java.nio.file.Path

import javax.xml.parsers.SAXParserFactory
import javax.xml.parsers.SAXParser
import org.xml.sax.InputSource
import org.xml.sax.XMLReader

import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.sax.SAXTransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import java.nio.charset.StandardCharsets

class XMLLogAnalyzer {

    private InputSource xsltInputSource
    private InputSource xmlInputSource
    private Result outputResult

    static final enum Format {
        MARKDOWN,
        JSON,
        HTML
    }

    XMLLogAnalyzer() {
        xsltInputSource = null
        xmlInputSource = null
        outputResult = null
    }

    void setXslt(Path xslt) {
        Objects.requireNonNull(xslt)
        assert Files.exists(xslt)
        File file = xslt.toFile()
        this.setXslt(file)
    }

    void setXslt(File xslt) {
        Objects.requireNonNull(xslt)
        assert Files.exists(xslt.toPath())
        Reader reader = new InputStreamReader(
                new FileInputStream(xslt), StandardCharsets.UTF_8)
        InputSource is = new InputSource(reader)
        is.setSystemId(xslt.toURI().toURL().toExternalForm())
        this.setXslt(is)
    }

    void setXslt(InputSource inputSource) {
        this.xsltInputSource = inputSource
    }

    void setInput(Path xml) {
        Objects.requireNonNull(xml)
        this.setInput(xml.toFile())
    }

    void setInput(File xml) {
        Objects.requireNonNull(xml)
        assert Files.exists(xml.toPath())
        Reader reader = new InputStreamReader(
                new FileInputStream(xml), StandardCharsets.UTF_8)
        InputSource is = new InputSource(reader)
        is.setSystemId(xml.toURI().toURL().toExternalForm())
        this.setInput(is)
    }

    void setInput(InputSource xml) {
        this.xmlInputSource = xml
    }

    void setOutput(Path result) {
        Objects.requireNonNull(result)
        this.setOutput(result.toFile())
    }

    void setOutput(File result) {
        Objects.requireNonNull(result)
        Files.createDirectories(result.toPath().getParent())
        StreamResult sr = new StreamResult(result)
        this.setOutput(sr)
    }

    void setOutput(Result result) {
        this.outputResult = result
    }


    void execute() {
        validateParams()
        Transformer transformer = createTransformerThatIgnoresDTD(this.xsltInputSource)
        Source s = createSAXSourceThatIgnoresDTD(this.xmlInputSource)
        transformer.transform(s, this.outputResult)
    }

    private void validateParams() {
        assert xsltInputSource != null
        assert xmlInputSource != null
        assert outputResult != null
    }


    private XMLReader createXMLReaderThatIgnoresDTD() {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance()
        SAXParser saxParser = saxParserFactory.newSAXParser()
        XMLReader parser = saxParser.getXMLReader()
        // Ignore the DTD declaration
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        parser.setFeature("http://xml.org/sax/features/validation", false);
    }

    private Transformer createTransformerThatIgnoresDTD(InputSource xsltInputSource) {
        assert xsltInputSource != null : "xsltInputSource is null. do setXslt(Path p)"
        SAXSource saxSource = createSAXSourceThatIgnoresDTD(xsltInputSource)
        TransformerFactory trf = TransformerFactory.newInstance()
        println trf.getClass().getName()
        Transformer transformer = trf.newTransformer(saxSource)
        return transformer
    }

    private Source createSAXSourceThatIgnoresDTD(InputSource inputSource) {
        SAXParserFactory spf = SAXParserFactory.newInstance()
        spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        spf.setFeature("http://xml.org/sax/features/validation", false);
        XMLReader xmlReader = spf.newSAXParser().getXMLReader()
        return new SAXSource(xmlReader, inputSource)
    }
}