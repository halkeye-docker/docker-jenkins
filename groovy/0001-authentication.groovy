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

println("Setting up authorization stategy")
def strategy = new hudson.security.ProjectMatrixAuthorizationStrategy()

strategy.add(Jenkins.ADMINISTER, 'halkeye')
strategy.add(Jenkins.RUN_SCRIPTS, 'halkeye')
strategy.add(Jenkins.READ, 'halkeye')

strategy.add(Jenkins.ADMINISTER, 'halkeye')
strategy.add(Jenkins.RUN_SCRIPTS, 'halkeye')
strategy.add(Jenkins.READ, 'halkeye')

strategy.add(Jenkins.READ, 'authenticated')
strategy.add(hudson.model.Item.DISCOVER, 'anonymous')

// Agent (Slave < 2.0) - http://javadoc.jenkins-ci.org/jenkins/model/Jenkins.MasterComputer.html
strategy.add(Jenkins.MasterComputer.BUILD, 'halkeye')
strategy.add(Jenkins.MasterComputer.CONFIGURE, 'halkeye')
strategy.add(Jenkins.MasterComputer.CONNECT, 'halkeye')
strategy.add(Jenkins.MasterComputer.CREATE, 'halkeye')
strategy.add(Jenkins.MasterComputer.DELETE, 'halkeye')
strategy.add(Jenkins.MasterComputer.DISCONNECT, 'halkeye')

// Job - http://javadoc.jenkins-ci.org/hudson/model/Item.html
strategy.add(hudson.model.Item.BUILD, 'halkeye')
strategy.add(hudson.model.Item.CANCEL, 'halkeye')
strategy.add(hudson.model.Item.CONFIGURE, 'halkeye')
strategy.add(hudson.model.Item.CREATE, 'halkeye')
strategy.add(hudson.model.Item.DELETE, 'halkeye')
strategy.add(hudson.model.Item.DISCOVER, 'halkeye')
strategy.add(hudson.model.Item.EXTENDED_READ, 'halkeye')
strategy.add(hudson.model.Item.READ, 'halkeye')
strategy.add(hudson.model.Item.WIPEOUT, 'halkeye')
strategy.add(hudson.model.Item.WORKSPACE, 'halkeye')

// Run - http://javadoc.jenkins-ci.org/hudson/model/Run.html
strategy.add(hudson.model.Run.DELETE, 'halkeye')
strategy.add(hudson.model.Run.UPDATE, 'halkeye')
strategy.add(hudson.model.Run.ARTIFACTS, 'halkeye')

// View - http://javadoc.jenkins-ci.org/hudson/model/View.html
strategy.add(hudson.model.View.CONFIGURE, 'halkeye')
strategy.add(hudson.model.View.CREATE, 'halkeye')
strategy.add(hudson.model.View.DELETE, 'halkeye')
strategy.add(hudson.model.View.READ, 'halkeye')

// SCM - http://javadoc.jenkins-ci.org/hudson/model/View.html
strategy.add(hudson.scm.SCM.TAG, 'halkeye')

// Credentials - https://github.com/jenkinsci/credentials-plugin/blob/master/src/main/java/com/cloudbees/plugins/credentials/CredentialsProvider.java
strategy.add(CredentialsProvider.CREATE, "halkeye")
strategy.add(CredentialsProvider.UPDATE, "halkeye")
strategy.add(CredentialsProvider.VIEW, "halkeye")
strategy.add(CredentialsProvider.DELETE, "halkeye")
strategy.add(CredentialsProvider.MANAGE_DOMAINS, "halkeye")

// Plugin Manager http://javadoc.jenkins-ci.org/hudson/PluginManager.html
strategy.add(hudson.PluginManager.UPLOAD_PLUGINS, 'halkeye')
strategy.add(hudson.PluginManager.CONFIGURE_UPDATECENTER, 'halkeye')


/*
// Team that has access to RUN the Job(s)
strategy.add(Jenkins.READ, '{{ JENKINS_DEVELOPER_GROUP }}')
strategy.add(hudson.model.Item.BUILD, '{{ JENKINS_DEVELOPER_GROUP }}')
strategy.add(hudson.model.Item.CANCEL, '{{ JENKINS_DEVELOPER_GROUP }}')
strategy.add(hudson.model.Item.DISCOVER, '{{ JENKINS_DEVELOPER_GROUP }}')
strategy.add(hudson.model.Item.READ, '{{ JENKINS_DEVELOPER_GROUP }}')

*/

instance.setAuthorizationStrategy(strategy)
instance.save()
