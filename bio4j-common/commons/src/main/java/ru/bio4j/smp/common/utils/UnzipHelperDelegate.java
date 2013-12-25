package ru.bio4j.smp.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Делегат для UnzipHelper
 */
public interface UnzipHelperDelegate {
    void callback(ZipInputStream zis, ZipEntry entry) throws Exception;
}
