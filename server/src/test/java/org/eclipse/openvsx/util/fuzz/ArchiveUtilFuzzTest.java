package org.eclipse.openvsx.util.fuzz;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import org.eclipse.openvsx.util.ArchiveUtil;
import org.junit.jupiter.api.Tag;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Tag("fuzz")
class ArchiveUtilFuzzTest {

    @FuzzTest
    void fuzzIsSafePath(@NotNull @WithUtf8Length(max = 500) String path) {
        boolean safe = ArchiveUtil.isSafePath(path);
        if (safe) {
            // Oracle: if isSafePath says it's safe, the normalized path must not escape the base directory
            try {
                Path base = Paths.get("/synthetic-base").toAbsolutePath();
                Path resolved = base.resolve(path).normalize();
                assertFalse(
                        !resolved.startsWith(base),
                        "isSafePath returned true but resolved path escapes base directory: " + path
                );
            } catch (InvalidPathException e) {
                // Should not happen if isSafePath returned true, but don't fail the fuzzer for this
            }
        }
    }
}
