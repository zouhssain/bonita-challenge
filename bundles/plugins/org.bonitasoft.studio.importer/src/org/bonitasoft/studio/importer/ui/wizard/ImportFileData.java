package org.bonitasoft.studio.importer.ui.wizard;

import org.bonitasoft.studio.importer.ImporterFactory;
import org.bonitasoft.studio.importer.bpmn.BpmnImportSource;

public class ImportFileData {

    private String filePath;
    private ImporterFactory importerFactory;

    private String selectedRepositoryName;
    private BpmnImportSource bpmnImportSource;

    public String getSelectedRepositoryName() {
        return selectedRepositoryName;
    }

    public void setSelectedRepositoryName(String selectedRepositoryName) {
        this.selectedRepositoryName = selectedRepositoryName;
    }

    public ImporterFactory getImporterFactory() {
        return importerFactory;
    }

    public void setImporterFactory(ImporterFactory importerFactory) {
        this.importerFactory = importerFactory;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public BpmnImportSource getBpmnImportSource() {
        return bpmnImportSource;
    }

    public void setBpmnImportSource(BpmnImportSource bpmnImportSource) {
        this.bpmnImportSource = bpmnImportSource;
    }
}
