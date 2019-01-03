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
//import hudson.security.AuthorizationMatrixProperty;
//import com.coravy.hudson.plugins.github.GithubProjectProperty;

def githubProjects = [
    'halkeye/bamboohr-employee-stats',
    'halkeye/codacy-maven-plugin',
    'halkeye/dehydrated-docker',
    'halkeye/dind-jenkins-slave',
    'halkeye/docker-dnscrypt-2',
    'halkeye/docker-jenkins',
    'halkeye/docker-mineos',
    'halkeye/docker-nextcloud',
    'halkeye/docker-node-red',
    'halkeye/docker-pi-hole',
    'halkeye/docker-starbound',
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
    'halkeye/jackett-chart',
    'halkeye/minecraft.gavinmogan.com',
    'halkeye/nzbhydra2-chart',
    'halkeye/pgadmin-chart',
    'halkeye/proxy-s3-google-oauth',
    'halkeye/react-book-reader',
    'halkeye/release-dashboard',
    'halkeye/slack-confluence',
    'halkeye/slack-foodee',
    'halkeye/soundboard',
    'halkeye/thelounge-chart',
    'halkeye/traefik-forward-auth-chart',
    'halkeye/turtl-chart',
    'halkeye/www-gavinmogan-com',

    // 'halkeye/ecmproject',
]

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

def githubFolder = new Folder(Jenkins.instance, "Github Projects");
if (Jenkins.instance.getAuthorizationStrategy() instanceof ProjectMatrixAuthorizationStrategy) {
    githubFolder.addProperty(new AuthorizationMatrixProperty([
                (Jenkins.READ): ['nfg', 'aliaoca', 'authorized', 'authenticated'],
                (hudson.model.Item.READ): ['nfg', 'aliaoca', 'authorized', 'authenticated'],
                (hudson.model.Item.DISCOVER): ['nfg', 'aliaoca', 'authorized', 'authenticated']
            ]));
}
Jenkins.instance.putItem(githubFolder);

def bitbucketFolder = new Folder(Jenkins.instance, "Bitbucket Projects");
Jenkins.instance.putItem(bitbucketFolder);

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

def systemFolder = new Folder(Jenkins.instance, "System Projects");
Jenkins.instance.putItem(systemFolder);

if (1) {
  String slug = "halkeye/jenkins-docker";
  String id = slug.replaceAll(/[^a-zA-Z0-9_.-]/, '_');
  println("Creating - System Projects - " + slug);

  WorkflowJob j = systemFolder.createProject(WorkflowJob.class, id);
  j.setDefinition(
		new CpsScmFlowDefinition(
			new GitStep("git@github.com:halkeye/jenkins-docker.git").createSCM(), 
			"Jenkinsfile.loaddeps"
		)
	);

  j.displayName = "System: " + slug
  j.properties.add(new hudson.tasks.LogRotator(0, 5));
  PipelineTriggersJobProperty ptjp = new PipelineTriggersJobProperty();
  ptjp.addTrigger(new GitHubPushTrigger());
  j.properties.add(ptjp);

}
