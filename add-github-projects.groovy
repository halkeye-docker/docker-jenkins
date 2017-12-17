import jenkins.model.Jenkins;
import com.cloudbees.hudson.plugins.folder.computed.DefaultOrphanedItemStrategy;
import jenkins.branch.BranchSource;
import jenkins.branch.MultiBranchProject;
import jenkins.plugins.git.GitSCMSource;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMSource;
import org.jenkinsci.plugins.github_branch_source.BranchDiscoveryTrait;
import org.jenkinsci.plugins.github_branch_source.OriginPullRequestDiscoveryTrait;
import org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait;

def projects = [
    'halkeye/gavinmogan.com',
    'halkeye/minecraft.gavinmogan.com',
    'halkeye/docker-mineos',
    'halkeye/jenkins-docker',
    'halkeye/go_windows_stats',
    'halkeye/docker-starbound',
    'halkeye/slack-foodee',
    'halkeye/slack-confluence'
]

projects.each { slug ->
    String id = slug.replaceAll(/[^a-zA-Z0-9_.-]/, '_');
    println("Creating " + slug);
    WorkflowMultiBranchProject mbp = Jenkins.instance.createProject(WorkflowMultiBranchProject.class, id)
    mbp.displayName = slug
    GitHubSCMSource source = new GitHubSCMSource(slug.tokenize("/")[0], slug.tokenize("/")[1]);
    source.setCredentialsId('github-halkeye');
    source.setTraits([
        new BranchDiscoveryTrait(1),
        new OriginPullRequestDiscoveryTrait(1),
        new ForkPullRequestDiscoveryTrait(1, new ForkPullRequestDiscoveryTrait.TrustContributors())
    ])
    mbp.getSourcesList().add(new BranchSource(source));
    mbp.setOrphanedItemStrategy(new DefaultOrphanedItemStrategy(true, 5, 5));
}