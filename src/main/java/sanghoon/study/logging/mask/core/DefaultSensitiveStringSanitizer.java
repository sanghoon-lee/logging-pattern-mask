package sanghoon.study.logging.mask.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public final class DefaultSensitiveStringSanitizer implements SensitiveStringSanitizer{
    public record Rule(String name, Pattern pattern, String replacement) {
        public Rule {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(pattern, "pattern");
            Objects.requireNonNull(replacement, "replacement");
        }

        String apply(String input) {
            return pattern.matcher(input).replaceAll(replacement);
        }
    }

    private final List<Rule> rules;
    private final List<String> triggers;

    public DefaultSensitiveStringSanitizer(List<Rule> rules, List<String> triggers) {
        Objects.requireNonNull(rules, "rules");
        this.rules = Collections.unmodifiableList(new ArrayList<>(rules));
        this.triggers = (triggers == null) ? List.of() : Collections.unmodifiableList(new ArrayList<>(triggers));
    }

    @Override
    public String sanitize(String input) {
        if (input == null || input.isEmpty())
            return input;

        // triggers가 있으면 "민감 가능성" 빠른 체크로 대부분 로그는 스킵
        if (!maybeSensitive(input))
            return input;

        String out = input;
        for (Rule r : rules) {
            out = r.apply(out);
        }
        return out;
    }

    private boolean maybeSensitive(String input) {
        if (triggers.isEmpty())
            return true;

        for (String t : triggers) {
            if (t == null)
                continue;
            if (!t.isEmpty() && input.contains(t))
                return true;
        }
        return false;
    }

    public List<Rule> rules() {
        return rules;
    }
    public List<String> triggers() {
        return triggers;
    }
}
