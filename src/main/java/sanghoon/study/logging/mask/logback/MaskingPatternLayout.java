package sanghoon.study.logging.mask.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Setter;
import sanghoon.study.logging.mask.core.DefaultSensitiveStringSanitizer;
import sanghoon.study.logging.mask.core.SensitiveStringSanitizer;

import java.util.ArrayList;
import java.util.List;

@Setter
public class MaskingPatternLayout extends PatternLayout {
    private boolean enabled = true;
    private String triggers;

    // repeated <rule>...</rule>
    private final List<String> ruleSpecs = new ArrayList<>();

    private volatile SensitiveStringSanitizer sanitizer;

    // logback.xml에서 <rule>을 여러 번 쓰면 반복 호출됨
    public void setRule(String ruleSpec) {
        if (ruleSpec != null && !ruleSpec.isBlank()) {
            this.ruleSpecs.add(ruleSpec);
        }
    }

    @Override
    public void start() {
        List<DefaultSensitiveStringSanitizer.Rule> rules = new ArrayList<>();
        for (String spec : ruleSpecs) {
            rules.add(RuleSpecParser.parse(spec));
        }

        this.sanitizer = new DefaultSensitiveStringSanitizer(rules, parseTriggers(triggers));
        super.start();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        String out = super.doLayout(event);
        if (!enabled) return out;

        SensitiveStringSanitizer s = sanitizer;
        if (s == null) return out; // start() 전 방어

        return s.sanitize(out);
    }

    private static List<String> parseTriggers(String triggers) {
        if (triggers == null || triggers.isBlank())
            return List.of();

        String[] parts = triggers.split(",");
        List<String> list = new ArrayList<>(parts.length);
        for (String p : parts) {
            String t = p.trim();
            if (!t.isEmpty()) list.add(t);
        }
        return list;
    }
}
