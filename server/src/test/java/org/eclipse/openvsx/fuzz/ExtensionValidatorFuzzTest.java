package org.eclipse.openvsx.fuzz;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import org.eclipse.openvsx.ExtensionValidator;
import org.junit.jupiter.api.Tag;

@Tag("fuzz")
class ExtensionValidatorFuzzTest {

    private final ExtensionValidator validator = new ExtensionValidator();

    @FuzzTest
    void fuzzValidateNamespace(@NotNull @WithUtf8Length(max = 500) String namespace) {
        // Must never throw — it returns Optional<Issue>
        validator.validateNamespace(namespace);
    }

    @FuzzTest
    void fuzzValidateExtensionName(@NotNull @WithUtf8Length(max = 500) String name) {
        // Must never throw — it returns Optional<Issue>
        validator.validateExtensionName(name);
    }

    @FuzzTest
    void fuzzValidateExtensionVersion(@NotNull @WithUtf8Length(max = 200) String version) {
        // Must never throw — it returns Optional<Issue>
        validator.validateExtensionVersion(version);
    }
}
