package org.bonitasoft.studio.importer.bpmn;

import java.time.Instant;

import org.bonitasoft.studio.importer.ImporterPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class BpmnImportSourceReporter {

    public void report(BpmnImportSource source) {
        if (source == null) {
            return;
        }
        String message = String.format(
                "BPMN import - tool=%s, version=%s, timestamp=%s",
                source.getToolName(),
                source.getVersion().isEmpty() ? "N/A" : source.getVersion(),
                Instant.now().toString()
        );

        ImporterPlugin.getDefault()
                .getLog()
                .log(new Status(IStatus.INFO, ImporterPlugin.PLUGIN_ID, message));
    }
}
