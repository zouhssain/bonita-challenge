package org.bonitasoft.studio.importer.bpmn;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BpmnSourceDetector {

    public Optional<BpmnImportSource> detect(File bpmnFile) {
        if (bpmnFile == null || !bpmnFile.isFile()) {
            return Optional.empty();
        }

        try (var in = Files.newInputStream(bpmnFile.toPath())) {
            var factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            Document doc = factory.newDocumentBuilder().parse(in);
            Element defs = (Element) doc.getDocumentElement();

            if (!"definitions".equals(defs.getLocalName())) {
                return Optional.empty();
            }

            String exporter = defs.getAttribute("exporter");
            String exporterVersion = defs.getAttribute("exporterVersion");

            if ((exporter == null || exporter.isBlank()) &&
                    (exporterVersion == null || exporterVersion.isBlank())) {
                return Optional.empty();
            }

            String normalizedTool = normalizeToolName(exporter);

            return Optional.of(new BpmnImportSource(normalizedTool, exporterVersion));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String normalizeToolName(String exporterRaw) {
        if (exporterRaw == null) {
            return "UNKNOWN";
        }
        String lower = exporterRaw.toLowerCase(Locale.ROOT);

        if (lower.contains("camunda")) {
            return "Camunda Modeler";
        } else if (lower.contains("signavio")) {
            return "Signavio";
        } else if (lower.contains("bizagi")) {
            return "Bizagi";
        }
        return exporterRaw.trim();
    }
}
