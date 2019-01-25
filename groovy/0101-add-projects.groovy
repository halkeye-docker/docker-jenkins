import jenkins.model.Jenkins;
import hudson.model.Item.*;
import hudson.tasks.LogRotator;
import com.cloudbees.hudson.plugins.folder.computed.DefaultOrphanedItemStrategy;
import hudson.security.ProjectMatrixAuthorizationStrategy;
import jenkins.branch.BranchSource;
import jenkins.branch.MultiBranchProject;
import jenkins.branch.DefaultBranchPropertyStrategy;
import jenkins.branch.BranchProperty;
import jenkins.plugins.git.GitSCMSource;
import jenkins.plugins.git.GitStep;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMSource;
import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSource;
import org.jenkinsci.plugins.github_branch_source.BranchDiscoveryTrait;
import org.jenkinsci.plugins.github_branch_source.OriginPullRequestDiscoveryTrait;
import org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait;
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty;
import com.cloudbees.jenkins.GitHubPushTrigger;

import com.cloudbees.hudson.plugins.folder.Folder;
import com.cloudbees.hudson.plugins.folder.properties.AuthorizationMatrixProperty;

import jenkins.branch.OrganizationFolder;
import jenkins.branch.NoTriggerOrganizationFolderProperty;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator;

def githubProjects = [
    'halkeye/bamboohr-employee-stats',
    'halkeye/codacy-maven-plugin',
    'halkeye/flask_atlassian_connect',
    'halkeye/gavinmogan.com',
    'halkeye/get_groups',
    'halkeye/git-version-commits',
    'halkeye/go_windows_stats',
    'halkeye/graphite_scripts',
    'halkeye/halkeye-ansible',
    'halkeye/http_bouncer_client',
    'halkeye/http_bouncer_server',
    'halkeye/hubot-brain-redis-hash',
    'halkeye/hubot-confluence-search',
    'halkeye/hubot-jenkins-notifier',
    'halkeye/hubot-regex',
    'halkeye/hubot-sonarr',
    'halkeye/hubot-url-describer',
    'halkeye/infinicatr',
    'halkeye/minecraft.gavinmogan.com',
    'halkeye/proxy-s3-google-oauth',
    'halkeye/react-book-reader',
    'halkeye/release-dashboard',
    'halkeye/slack-foodee',
    'halkeye/soundboard',
    'halkeye/www-gavinmogan-com',
];

def githubDockerProjects = [
    'halkeye/dind-jenkins-slave',
    'halkeye/discorse-docker-builder',
    'halkeye/docker-dnscrypt-2',
    'halkeye/docker-jenkins',
    'halkeye/docker-mineos',
    'halkeye/docker-nextcloud',
    'halkeye/docker-node-red',
    'halkeye/docker-pi-hole',
    'halkeye/docker-starbound',
];

def githubOrganizations = [
    'halkeye-helm-charts'
];

def bitbucketProjects = [
    'halkeye/hpmud',
    'halkeye/ingrid_intimidator',
    'halkeye/love-notes-app',
    'halkeye/loves-notes-api',
    'halkeye/presentation-devops',
    'halkeye/presentation-linux101',
    'halkeye/presentation-react-vs-angular',
    'halkeye/presentation-stats',
    'saltystories/stories',
]

def allowedGithubUsers = ['nfg', 'aliaoca', 'authorized', 'authenticated'];
def githubFolder = new Folder(Jenkins.instance, "Github Projects");
Jenkins.instance.putItem(githubFolder);
def githubDockerFolder = new Folder(Jenkins.instance, "Github Docker Projects");
Jenkins.instance.putItem(githubDockerFolder);
def githubOrganizationsFolder = new Folder(Jenkins.instance, "Github Organization Projects");
Jenkins.instance.putItem(githubOrganizationsFolder);
def bitbucketFolder = new Folder(Jenkins.instance, "Bitbucket Projects");
Jenkins.instance.putItem(bitbucketFolder);

if (Jenkins.instance.getAuthorizationStrategy() instanceof ProjectMatrixAuthorizationStrategy) {
    githubFolder.addProperty(new AuthorizationMatrixProperty([
                (Jenkins.READ): allowedGithubUsers,
                (hudson.model.Item.READ): allowedGithubUsers,
                (hudson.model.Item.DISCOVER): allowedGithubUsers
            ]));
    githubDockerFolder.addProperty(new AuthorizationMatrixProperty([
                (Jenkins.READ): allowedGithubUsers,
                (hudson.model.Item.READ): allowedGithubUsers,
                (hudson.model.Item.DISCOVER): allowedGithubUsers
            ]));
    githubOrganizationsFolder.addProperty(new AuthorizationMatrixProperty([
                (Jenkins.READ): allowedGithubUsers,
                (hudson.model.Item.READ): allowedGithubUsers,
                (hudson.model.Item.DISCOVER): allowedGithubUsers
            ]));
}

githubProjects.each { slug ->
    String id = slug.replaceAll(/[^a-zA-Z0-9_.-]/, '_');
    println("Creating - Github Project - " + slug);
    WorkflowMultiBranchProject mbp = githubFolder.createProject(WorkflowMultiBranchProject.class, id)
    mbp.displayName = "Github: " + slug
    GitHubSCMSource source = new GitHubSCMSource(slug.tokenize("/")[0], slug.tokenize("/")[1]);
    source.setCredentialsId('github-halkeye');
    source.setTraits([
        new BranchDiscoveryTrait(1),
        new OriginPullRequestDiscoveryTrait(1),
        new ForkPullRequestDiscoveryTrait(1, new org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait.TrustPermission())
    ])
    BranchSource branchSource = new BranchSource(source);
    branchSource.setStrategy(new DefaultBranchPropertyStrategy([] as BranchProperty[]));
    mbp.getSourcesList().add(branchSource);
    mbp.setOrphanedItemStrategy(new DefaultOrphanedItemStrategy(true, 5, 5));
}

githubDockerProjects.each { slug ->
    String id = slug.replaceAll(/[^a-zA-Z0-9_.-]/, '_');
    println("Creating - Github Project - " + slug);
    WorkflowMultiBranchProject mbp = githubFolder.createProject(WorkflowMultiBranchProject.class, id)
    mbp.displayName = "Github: " + slug
    GitHubSCMSource source = new GitHubSCMSource(slug.tokenize("/")[0], slug.tokenize("/")[1]);
    source.setCredentialsId('github-halkeye');
    source.setTraits([
        new BranchDiscoveryTrait(1),
        new OriginPullRequestDiscoveryTrait(1),
        new ForkPullRequestDiscoveryTrait(1, new org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait.TrustPermission())
    ])
    BranchSource branchSource = new BranchSource(source);
    branchSource.setStrategy(new DefaultBranchPropertyStrategy([] as BranchProperty[]));
    mbp.getSourcesList().add(branchSource);
    mbp.setOrphanedItemStrategy(new DefaultOrphanedItemStrategy(true, 5, 5));
}

bitbucketProjects.each { slug ->
    String id = slug.replaceAll(/[^a-zA-Z0-9_.-]/, '_');
    println("Creating - Bitbucket Project - " + slug);
    WorkflowMultiBranchProject mbp = bitbucketFolder.createProject(WorkflowMultiBranchProject.class, id)
    mbp.displayName = "Bitbucket: " + slug
    BitbucketSCMSource source = new BitbucketSCMSource(slug.tokenize("/")[0], slug.tokenize("/")[1]);
    source.setCredentialsId('bitbucket-halkeye');
    source.setTraits([
        new BranchDiscoveryTrait(1),
        new OriginPullRequestDiscoveryTrait(1),
        new ForkPullRequestDiscoveryTrait(1, new ForkPullRequestDiscoveryTrait.TrustContributors())
    ])
    mbp.getSourcesList().add(new BranchSource(source));
    mbp.setOrphanedItemStrategy(new DefaultOrphanedItemStrategy(true, 5, 5));
}

githubOrganizations.each { slug ->
    String id = slug.replaceAll(/[^a-zA-Z0-9_.-]/, '_');
    println("Creating - Github Organization Project - " + slug);
    jenkins.branch.OrganizationFolder of = githubOrganizationsFolder.createProject(jenkins.branch.OrganizationFolder.class, id)
    of.displayName = "Github Org: " + slug
    // of.onCreatedFromScratch();
    of.addProperty(new jenkins.branch.NoTriggerOrganizationFolderProperty('.*'));
    of.addProperty(new org.jenkinsci.plugins.pipeline.modeldefinition.config.FolderConfig());
    of.setOrphanedItemStrategy(new DefaultOrphanedItemStrategy(true, 5, 5));
    org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator scmNav = new org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator(slug);
    scmNav.setCredentialsId('github-halkeye');
    scmNav.setTraits([
        new BranchDiscoveryTrait(1),
        new OriginPullRequestDiscoveryTrait(1),
        new ForkPullRequestDiscoveryTrait(1, new org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait.TrustPermission())
    ]);
    of.getNavigators().add(scmNav);


/*
  <projectFactories>
    <org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProjectFactory plugin="workflow-multibranch@2.20">
      <scriptPath>Jenkinsfile</scriptPath>
    </org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProjectFactory>
  </projectFactories>
  <buildStrategies>
    <com.github.kostyasha.github.integration.multibranch.GitHubBranchBuildStrategy plugin="github-pullrequest@0.2.4"/>
  </buildStrategies>
  */
}
