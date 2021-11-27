package com.kazurayam.ks.logging

import org.apache.commons.io.FileUtils
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.ZoneId
import java.time.ZonedDateTime

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
                .resolve("reports.xml")
        Files.createDirectories(output.getParent())
        scanner.setOut(output)
        scanner.execute()
        assert Files.exists(output)
        assert output.size() > 0
    }

    @Test
    void test_OUR_DATETIME_FORMATTER() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault())
        assert now.format(ReportsDirectoryScanner.OUR_DATETIME_FORMATTER).startsWith("20")  // year 2021
    }

    @Test
    void test_formatLastModified() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault())
        long epoch = now.toInstant().getEpochSecond() * 1000
        String formatted = ReportsDirectoryScanner.formatLastModified(epoch)
        assert formatted.startsWith("20")  // year 2021
    }

    @Test
    void test_formatLastModified_file() {
        Path buildGradle = Paths.get("build.gradle")
        long lastModified = buildGradle.toFile().lastModified()
        String formatted = ReportsDirectoryScanner.formatLastModified(lastModified)
        assert formatted.startsWith("20")  // year 2021
    }

    @Ignore
    @Test
    void test_compare_timestamp_of_file_and_system_clock() {
        Path buildGradle = Paths.get("build.gradle")
        long lastModified = buildGradle.toFile().lastModified()
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault())
        long epoch = now.toInstant().getEpochSecond() * 1000
        assert Math.abs(lastModified - epoch) < 100000000
    }

    @Test
    void test_parseLastModified() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault())
        long epochMillis = now.toInstant().getEpochSecond() * 1000
        String formatted = ReportsDirectoryScanner.formatLastModified(epochMillis)
        long parseResult = ReportsDirectoryScanner.parseLastModified(formatted)
        assert parseResult == epochMillis
    }
}
