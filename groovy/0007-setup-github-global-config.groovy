import jenkins.model.*;
import org.jenkinsci.plugins.github.GitHubPlugin
import org.jenkinsci.plugins.github.config.GitHubPluginConfig;
import org.jenkinsci.plugins.github.config.GitHubServerConfig;

GitHubPluginConfig pluginConfig = GitHubPlugin.configuration();
GitHubServerConfig serverConfig = new GitHubServerConfig('github-admin-halkeye')
pluginConfig.setConfigs([serverConfig])
pluginConfig.save()
