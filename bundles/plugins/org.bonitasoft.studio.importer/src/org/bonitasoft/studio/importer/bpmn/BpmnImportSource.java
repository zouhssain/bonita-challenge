package org.bonitasoft.studio.importer.bpmn;

import java.util.Objects;

public class BpmnImportSource {

    private final String toolName;
    private final String version;

    public BpmnImportSource(String toolName, String version) {
        this.toolName = toolName != null ? toolName : "UNKNOWN";
        this.version = version != null ? version : "";
    }

    public String getToolName() {
        return toolName;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "BpmnImportSource{" +
                "toolName='" + toolName + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BpmnImportSource)) return false;
        BpmnImportSource that = (BpmnImportSource) o;
        return Objects.equals(toolName, that.toolName) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toolName, version);
    }
}
