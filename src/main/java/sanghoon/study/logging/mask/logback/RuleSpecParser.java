package sanghoon.study.logging.mask.logback;

import sanghoon.study.logging.mask.core.DefaultSensitiveStringSanitizer;

import java.util.Objects;
import java.util.regex.Pattern;

public final class RuleSpecParser {
    private RuleSpecParser() {}

    public static DefaultSensitiveStringSanitizer.Rule parse(String spec) {
        Objects.requireNonNull(spec, "spec");
        String trimmed = spec.trim();
        if (trimmed.isEmpty())
            throw new IllegalArgumentException("rule spec is empty");

        String[] parts = trimmed.split("\\|", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("invalid rule spec. expected name|regex|replacement. got: " + spec);
        }

        String name = parts[0].trim();
        String regex = parts[1];
        String replacement = parts[2];

        if (name.isEmpty())
            throw new IllegalArgumentException("rule name is empty: " + spec);

        if (regex == null || regex.isEmpty())
            throw new IllegalArgumentException("regex is empty: " + spec);

        return new DefaultSensitiveStringSanitizer.Rule(name, Pattern.compile(regex), replacement);
    }
}
