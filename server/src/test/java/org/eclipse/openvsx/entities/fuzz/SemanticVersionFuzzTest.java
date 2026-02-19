package org.eclipse.openvsx.entities.fuzz;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import com.code_intelligence.jazzer.mutation.annotation.Ascii;
import org.eclipse.openvsx.entities.SemanticVersion;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SemanticVersionFuzzTest {

    @FuzzTest
    void fuzzParse(@NotNull @WithUtf8Length(max = 200) String version) {
        try {
            SemanticVersion.parse(version);
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Expected for invalid version strings (NumberFormatException is a subclass of IllegalArgumentException)
        }
    }

    @FuzzTest
    void fuzzParseCompareTo(
            @NotNull @Ascii @WithUtf8Length(max = 100) String v1,
            @NotNull @Ascii @WithUtf8Length(max = 100) String v2
    ) {
        SemanticVersion sv1;
        SemanticVersion sv2;
        try {
            sv1 = SemanticVersion.parse(v1);
            sv2 = SemanticVersion.parse(v2);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return;
        }

        int cmp12 = sv1.compareTo(sv2);
        int cmp21 = sv2.compareTo(sv1);

        // Antisymmetry: sign(compareTo(a,b)) == -sign(compareTo(b,a))
        assertTrue(
                Integer.signum(cmp12) == -Integer.signum(cmp21),
                "compareTo is not antisymmetric for '" + v1 + "' and '" + v2 + "': "
                        + cmp12 + " vs " + cmp21
        );
    }
}
