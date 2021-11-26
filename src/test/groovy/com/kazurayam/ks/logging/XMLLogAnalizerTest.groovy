package com.kazurayam.ks.logging

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

import javax.xml.transform.TransformerFactory

import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import org.apache.commons.io.FileUtils

@RunWith(JUnit4.class)
public class XMLLogAnalyzerTest {

    static Path projectDir
    static Path xsltDir
    static Path reportsDir
    static Path classOutputDir
    static TransformerFactory tf

    static final String LOGFILE_NAME = 'execution0.log'

    @BeforeClass
    static void setupClass() {
        projectDir = Paths.get(".")
        xsltDir = projectDir.resolve("src/main/xslt")
        reportsDir = projectDir.resolve("src/test/fixtures/Reports")
        classOutputDir = projectDir.resolve("build/tmp/testOutput").resolve(XMLLogAnalyzerTest.class.getName())
        if (Files.exists(classOutputDir)) {
            FileUtils.deleteDirectory(classOutputDir.toFile())
        }
        Files.createDirectories(classOutputDir)
        tf = TransformerFactory.newInstance()
    }

    //@Ignore
    @Test
    void test_identity_transform() {
        Path xslt = xsltDir.resolve("identity-transform.xsl")
        Path input = findXML(reportsDir, "20210903_201226")
        Path output = classOutputDir.resolve("test_identity_transform").resolve("identity.xml")
        Files.createDirectories(output.getParent())
        //
        XMLLogAnalyzer analyzer = new XMLLogAnalyzer()
        analyzer.setXslt(xslt)
        analyzer.setInput(input)
        analyzer.setOutput(output)
        analyzer.execute()
        assert Files.exists(output)
        assert output.size() > 0
    }

    @Test
    void test_counter() {
        Path xslt = xsltDir.resolve("counter.xsl")
        Path input = findXML(reportsDir, "20210903_201226")
        Path output = classOutputDir.resolve("test_counter").resolve("count.xml")
        Files.createDirectories(output.getParent())
        //
        XMLLogAnalyzer analyzer = new XMLLogAnalyzer()
        analyzer.setXslt(xslt)
        analyzer.setInput(input)
        analyzer.setOutput(output)
        analyzer.execute()
        assert Files.exists(output)
        assert output.size() > 0
    }


    Path findXML(Path reportsDir, String timestamp) {
        List<Path> result = new ArrayList<Path>()
        Files.walkFileTree(reportsDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString() == LOGFILE_NAME) {
                    Path parent = file.getParent()
                    if (parent.getFileName().toString() == timestamp) {
                        result.add(file)
                    }
                }
                return FileVisitResult.CONTINUE
            }
        });
        assert result.size() > 0 : "no ${LOGFILE_NAME} was found in the ${timestamp} dir"
        return result[0]
    }


    String identityTemplates() {
        return ""
    }
}

