package com.kazurayam.ks.logging

import groovy.xml.MarkupBuilder

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ReportsDirectoryScanner {

    private Path reportsDir
    private Path outputFile

    public static final String XMLLOG_FILENAME = "execution0.log"

    public static final DateTimeFormatter OUR_DATETIME_FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME

    ReportsDirectoryScanner() {}

    void setReportsDir(Path dir) {
        Objects.requireNonNull(dir)
        assert Files.exists(dir)
        this.reportsDir = dir
    }

    void setOut(Path outputFile) {
        Objects.requireNonNull(outputFile)
        this.outputFile = outputFile
    }

    void execute() {
        validateParams()
        ensureOutputDir()
        List<Path> files = new ArrayList<Path>()
        Files.walkFileTree(this.reportsDir, new SimpleFileVisitor<Path>() {
            @Override
            FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                if (file.getFileName().toString() == XMLLOG_FILENAME) {
                    files.add(file.normalize().toAbsolutePath())
                };
                return FileVisitResult.CONTINUE;
            }
        })
        // serialize the files into XML
        StringWriter sw = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(sw)
        mb.logfiles() {
            files.each ({ Path p ->
                logfile(
                        'path': p.toString(),
                        'size': p.size(),
                        'lastModified': formatLastModified(p.toFile().lastModified())
                )
            })
        }
        this.outputFile.text = sw.toString()
    }

    private void validateParams() {
        assert this.reportsDir != null
        assert this.outputFile != null
    }

    private void ensureOutputDir() {
        assert this.outputFile != null
        Files.createDirectories(this.outputFile.getParent())
    }

    static String formatLastModified(long timeMillis) {
        Instant instant = Instant.ofEpochSecond(timeMillis)
        ZonedDateTime zonedDateTime =
                ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        String formatted = zonedDateTime.format(OUR_DATETIME_FORMATTER)
        return formatted
    }

    static long parseLastModified(String lastModified) {
        ZonedDateTime zonedDateTime =
                ZonedDateTime.parse(lastModified, OUR_DATETIME_FORMATTER)
        long epochSecond = zonedDateTime.toEpochSecond()
        return epochSecond
    }
}
