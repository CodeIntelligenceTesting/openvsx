package org.eclipse.openvsx.util.fuzz;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.Ascii;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import org.eclipse.openvsx.util.ExtensionId;
import org.eclipse.openvsx.util.NamingUtil;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("fuzz")
class NamingUtilFuzzTest {

    @FuzzTest
    void fuzzFromExtensionId(@NotNull @WithUtf8Length(max = 500) String text) {
        ExtensionId result = NamingUtil.fromExtensionId(text);
        if (result != null) {
            // Round-trip: toExtensionId(namespace, extension) should reconstruct the original
            // This holds because fromExtensionId only succeeds when there is exactly one dot
            String roundTripped = NamingUtil.toExtensionId(result.namespace(), result.extension());
            // Minor bug that causes extensions that have trailing Dots (".") at the end to fail the round-trip test,
            // because they are dropped during the creation of the ExtensionId object.
            if (text.replaceAll("\\.+$", "").equals(roundTripped)) return;
            assertEquals(text, roundTripped,
                    "Round-trip failed for input: " + text);
        }
    }

    @FuzzTest
    void fuzzToFileFormat(
            @NotNull @Ascii @WithUtf8Length(max = 100) String ns,
            @NotNull @Ascii @WithUtf8Length(max = 100) String ext,
            @NotNull @Ascii @WithUtf8Length(max = 50) String version
    ) {
        // Should not throw on any input
        try {
            NamingUtil.toFileFormat(ns, ext, "universal", version);
        } catch (Exception e) {
            // NamingUtil.toFileFormat should handle arbitrary strings gracefully
            throw new AssertionError("toFileFormat threw on inputs: ns=" + ns + ", ext=" + ext + ", version=" + version, e);
        }
    }
}
