import jenkins.model.*
import jenkins.install.InstallState
import hudson.security.SecurityRealm
import hudson.security.ProjectMatrixAuthorizationStrategy
import org.jenkinsci.plugins.GithubSecurityRealm
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*

def instance = Jenkins.getInstance()

println("Setting github secrets")
def githubClientId = new File("/run/secrets/githubClientId")
def githubClientSecret = new File("/run/secrets/githubClientSecret")
if (githubClientId.exists() && githubClientSecret.exists()) {
  SecurityRealm github_realm = new GithubSecurityRealm(
      "https://github.com",
      "https://api.github.com",
      githubClientId.text.trim(),
      githubClientSecret.text.trim(),
      "read:org,user:email")

  //check for equality, no need to modify the runtime if no settings changed
  instance.setSecurityRealm(github_realm)
  instance.save()
} else {
  return
}
