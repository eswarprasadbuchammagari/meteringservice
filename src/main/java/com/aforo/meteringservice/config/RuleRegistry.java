package com.aforo.meteringservice.config;

import com.aforo.meteringservice.exception.RuleException;
import com.aforo.meteringservice.rule.Rule;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RuleRegistry {

    // ConcurrentHashMap to store rule implementations by type
    private final Map<String, Rule> rules = new ConcurrentHashMap<>();

    /**
     * Constructor injection for the list of Rule implementations.
     * Spring automatically injects all beans implementing the Rule interface.
     *
     * @param ruleImplementations List of Rule beans
     */
    public RuleRegistry(List<Rule> ruleImplementations) {
        ruleImplementations.forEach(rule -> {
            if (rules.put(rule.getRuleType(), rule) != null) {
                throw new IllegalStateException("Duplicate ruleType detected: " + rule.getRuleType());
            }
        });
    }

    /**
     * PostConstruct method for initialization checks or logging.
     */
    @PostConstruct
    public void init() {
        System.out.println("RuleRegistry initialized with " + rules.size() + " rules.");
        rules.forEach((type, rule) -> System.out.println("Registered Rule: " + type));
    }

    /**
     * Fetches the Rule implementation for the given ruleType.
     *
     * @param ruleType The rule type to retrieve
     * @return The matching Rule implementation
     * @throws RuleException if the ruleType is not found
     */
    public Rule getRule(String ruleType) {
        Rule rule = rules.get(ruleType);
        if (rule == null) {
            throw new RuleException("Rule not found for ruleType: " + ruleType);
        }
        return rule;
    }
}
