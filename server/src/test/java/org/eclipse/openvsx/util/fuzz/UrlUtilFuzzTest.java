package org.eclipse.openvsx.util.fuzz;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import org.eclipse.openvsx.util.UrlUtil;

class UrlUtilFuzzTest {

    @FuzzTest
    void fuzzCreateApiUrl(
            @NotNull @WithUtf8Length(max = 200) String baseUrl,
            @NotNull @WithUtf8Length(max = 100) String seg1,
            @NotNull @WithUtf8Length(max = 100) String seg2
    ) {
        try {
            UrlUtil.createApiUrl(baseUrl, seg1, seg2);
        } catch (IllegalArgumentException e) {
            // Acceptable
        }
    }

    @FuzzTest
    void fuzzAddQuery(
            @NotNull @WithUtf8Length(max = 200) String url,
            @NotNull @WithUtf8Length(max = 50) String key,
            @NotNull @WithUtf8Length(max = 100) String value
    ) {
        try {
            UrlUtil.addQuery(url, key, value);
        } catch (IllegalArgumentException | NullPointerException e) {
            // Acceptable
        }
    }
}
