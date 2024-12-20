package br.com.rafaelvieira.cryptocypher.enums;

public enum ExtensionFile {

    TEXT("*.txt"),
    PDF("*.pdf"),
    DOC("*.doc"),
    DOCX("*.docx"),
    XLS("*.xls"),
    XLSX("*.xlsx"),
    CSV("*.csv");

    private final String extension;

    public String getExtension() {
        return extension;
    }

    ExtensionFile(String extension) {
        this.extension = extension;
    }

    public static ExtensionFile getExtensionFile(String name) {
        for (ExtensionFile extensionFile : ExtensionFile.values()) {
            if (extensionFile.name().equalsIgnoreCase(name)) {
                return extensionFile;
            }
        }
        return null;
    }
}
