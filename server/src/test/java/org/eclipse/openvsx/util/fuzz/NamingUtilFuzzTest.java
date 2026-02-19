package org.eclipse.openvsx.util.fuzz;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.Ascii;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import org.eclipse.openvsx.util.NamingUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NamingUtilFuzzTest {

    @FuzzTest
    void fuzzFromExtensionId(@NotNull @WithUtf8Length(max = 500) String text) {
        var result = NamingUtil.fromExtensionId(text);
        if (result != null) {
            // Round-trip: toExtensionId(namespace, extension) should reconstruct the original
            // This holds because fromExtensionId only succeeds when there is exactly one dot
            String roundTripped = NamingUtil.toExtensionId(result.namespace(), result.extension());
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
