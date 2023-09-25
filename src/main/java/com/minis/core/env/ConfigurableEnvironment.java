package com.minis.core.env;

/**
 * 提供了配置环境的能力
 */
public interface ConfigurableEnvironment extends Environment {

    void setActiveProfiles(String... profiles);

    void addActiveProfile(String profile);

    void setDefaultProfiles(String... profiles);

    void merge(ConfigurableEnvironment parent);

}
