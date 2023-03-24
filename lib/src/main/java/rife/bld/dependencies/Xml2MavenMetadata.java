/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.dependencies;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import rife.xml.Xml2Data;

import java.util.*;
import java.util.regex.Pattern;

class Xml2MavenMetadata extends Xml2Data {
    private VersionNumber latest_ = VersionNumber.UNKNOWN;
    private VersionNumber release_ = VersionNumber.UNKNOWN;
    private final List<VersionNumber> versions_;
    private VersionNumber snapshot_ = VersionNumber.UNKNOWN;

    private StringBuilder characterData_ = null;

    private String lastTimestamp_ = null;
    private String lastBuildNumber_ = null;

    Xml2MavenMetadata() {
        versions_ = new ArrayList<>();
    }

    public VersionNumber getLatest() {
        return latest_;
    }

    public VersionNumber getRelease() {
        return release_;
    }

    public VersionNumber getSnapshot() {
        return snapshot_;
    }

    public List<VersionNumber> getVersions() {
        return versions_;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        characterData_ = new StringBuilder();
    }

    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "latest" -> latest_ = VersionNumber.parse(characterData_.toString());
            case "release" -> release_ = VersionNumber.parse(characterData_.toString());
            case "version" -> versions_.add(VersionNumber.parse(characterData_.toString()));
            case "timestamp" -> lastTimestamp_ = characterData_.toString();
            case "buildNumber" -> lastBuildNumber_ = characterData_.toString();
            case "snapshot" -> {
                var version = versions_.get(0);
                snapshot_ = new VersionNumber(version.major(), version.minor(), version.revision(), lastTimestamp_ + "-" + lastBuildNumber_);
                lastTimestamp_ = null;
                lastBuildNumber_ = null;
            }
        }

        characterData_ = null;
    }

    private static final Pattern MILESTONE = Pattern.compile("^m\\d*$");
    private static final Pattern BETA = Pattern.compile("^b\\d*$");
    private static final Pattern ALPHA = Pattern.compile("^a\\d*$");

    public void endDocument()
    throws SAXException {
        // determine latest stable version by removing pre-release qualifiers
        var filtered_versions = new TreeSet<VersionNumber>();
        filtered_versions.addAll(versions_.stream()
            .filter(v -> {
                if (v.qualifier() == null) return true;
                var q = v.qualifier().toLowerCase();
                return !q.startsWith("rc") &&
                       !q.startsWith("cr") &&
                       !q.contains("milestone") &&
                       !MILESTONE.matcher(q).matches() &&
                       !q.contains("beta") &&
                       !BETA.matcher(q).matches() &&
                       !q.contains("alpha") &&
                       !ALPHA.matcher(q).matches();
            }).toList());

        // only replace the stable version from the metadata when
        // something remained from the filtering, then use the
        // last version in the sorted set
        if (!filtered_versions.isEmpty()) {
            latest_ = filtered_versions.last();
        }
    }

    public void characters(char[] ch, int start, int length) {
        if (characterData_ != null) {
            characterData_.append(String.copyValueOf(ch, start, length));
        }
    }
}
