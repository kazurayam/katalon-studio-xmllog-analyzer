package com.kazurayam.ks.logging

import org.apache.commons.io.FileUtils
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ReportsDirectoryScannerTest {

    static Path projectDir
    static Path xsltDir
    static Path reportsDir
    static Path externalReportsDir
    static Path classOutputDir

    @BeforeClass
    static void setupClass() {
        projectDir = Paths.get(".")
        xsltDir = projectDir.resolve("src/main/xslt")
        reportsDir = projectDir.resolve("src/test/fixtures/Reports")
        externalReportsDir = Paths.get(System.getProperty("user.home"))
                .resolve("katalon-workspace")
                .resolve("ks_LogViewerSlowsDownTests_HowToPrevent")
                .resolve("Reports");
        classOutputDir = projectDir.resolve("build/tmp/testOutput")
                .resolve(ReportsDirectoryScannerTest.class.getName())
        if (Files.exists(classOutputDir)) {
            FileUtils.deleteDirectory(classOutputDir.toFile())
        }
        Files.createDirectories(classOutputDir)
    }

    @Test
    void test_execute() {
        ReportsDirectoryScanner scanner = new ReportsDirectoryScanner()
        scanner.setReportsDir(this.reportsDir)
        Path output = classOutputDir.resolve("test_execute")
                .resolve("logfiles.xml")
        Files.createDirectories(output.getParent())
        scanner.setOut(output)
        scanner.execute()
        assert Files.exists(output)
        assert output.size() > 0
    }

    @Test
    void test_formatLastModified() {
        long epoch = System.currentTimeMillis()
        String formatted = ReportsDirectoryScanner.formatLastModified(epoch)
        assert formatted != null
    }

    @Test
    void test_parseLastModified() {
        long epoch = System.currentTimeMillis()
        String formatted = ReportsDirectoryScanner.formatLastModified(epoch)
        long parseResult = ReportsDirectoryScanner.parseLastModified(formatted)
        assert parseResult == epoch
    }
}
