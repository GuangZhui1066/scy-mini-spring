package com.minis.core.env;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractEnvironment implements ConfigurableEnvironment {

    private final Set<String> activeProfiles = new LinkedHashSet<>();

}
