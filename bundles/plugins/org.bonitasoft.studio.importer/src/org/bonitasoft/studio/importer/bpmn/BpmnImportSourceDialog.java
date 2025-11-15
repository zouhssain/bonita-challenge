package org.bonitasoft.studio.importer.bpmn;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class BpmnImportSourceDialog extends TitleAreaDialog {

    private static final String TOOL_CAMUNDA = "Camunda Modeler";
    private static final String TOOL_SIGNAVIO = "Signavio";
    private static final String TOOL_BIZAGI = "Bizagi";
    private static final String TOOL_OTHER = "Other";

    private final BpmnImportSource autoDetected;
    private BpmnImportSource result;

    private ComboViewer toolViewer;
    private Text otherToolText;

    public BpmnImportSourceDialog(Shell parentShell, BpmnImportSource autoDetected) {
        super(parentShell);
        this.autoDetected = autoDetected;
    }

    @Override
    public void create() {
        super.create();
        setTitle("BPMN source");
        setMessage("Please select the tool used to create this BPMN file.");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        container.setLayout(new GridLayout(2, false));

        // Label + combo
        new Label(container, SWT.NONE).setText("Tool");
        toolViewer = new ComboViewer(container, SWT.READ_ONLY);
        toolViewer.setContentProvider(ArrayContentProvider.getInstance());
        toolViewer.setLabelProvider(new LabelProvider());
        toolViewer.setInput(new String[] {
                TOOL_CAMUNDA,
                TOOL_SIGNAVIO,
                TOOL_BIZAGI,
                TOOL_OTHER
        });

        // Champ "Other"
        new Label(container, SWT.NONE).setText("Other tool");
        otherToolText = new Text(container, SWT.BORDER);
        otherToolText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Pré-sélection si on avait une auto-détection
        if (autoDetected != null && autoDetected.getToolName() != null) {
            String detectedName = autoDetected.getToolName();
            String initial;
            if (detectedName.toLowerCase().contains("camunda")) {
                initial = TOOL_CAMUNDA;
            } else if (detectedName.toLowerCase().contains("signavio")) {
                initial = TOOL_SIGNAVIO;
            } else if (detectedName.toLowerCase().contains("bizagi")) {
                initial = TOOL_BIZAGI;
            } else {
                initial = TOOL_OTHER;
                otherToolText.setText(detectedName);
            }
            toolViewer.setSelection(new org.eclipse.jface.viewers.StructuredSelection(initial));
        } else {
            toolViewer.setSelection(
                    new org.eclipse.jface.viewers.StructuredSelection(TOOL_CAMUNDA));
        }

        hookListeners();
        validate();

        return area;
    }

    private void hookListeners() {
        toolViewer.addSelectionChangedListener(event -> validate());
        otherToolText.addModifyListener(e -> validate());
    }

    private void validate() {
        String selected = getSelectedToolName();
        boolean needsOther = TOOL_OTHER.equals(selected);

        otherToolText.setEnabled(needsOther);

        var okButton = getButton(IDialogConstants.OK_ID);
        if (needsOther && otherToolText.getText().trim().isEmpty()) {
            setErrorMessage("Please specify the tool name.");
            if (okButton != null) {
                okButton.setEnabled(false);
            }
        } else {
            setErrorMessage(null);
            if (okButton != null) {
                okButton.setEnabled(true);
            }
        }
    }

    private String getSelectedToolName() {
        var sel = (IStructuredSelection) toolViewer.getSelection();
        Object element = sel.getFirstElement();
        if (element instanceof String) {
            return (String) element;
        }
        return TOOL_OTHER;
    }

    @Override
    protected void okPressed() {
        String selected = getSelectedToolName();
        String toolName = TOOL_OTHER.equals(selected)
                ? otherToolText.getText().trim()
                : selected;

        String version = autoDetected != null ? autoDetected.getVersion() : null;

        result = new BpmnImportSource(toolName, version);
        super.okPressed();
    }

    public BpmnImportSource getResult() {
        return result;
    }
}