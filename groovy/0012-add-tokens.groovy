import jenkins.model.*;
import groovy.io.FileType;
import hudson.util.Secret;
import org.apache.commons.io.FilenameUtils;
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import org.jenkinsci.plugins.docker.commons.credentials.DockerServerCredentials;
import com.uber.jenkins.phabricator.credentials.ConduitCredentialsImpl;
import hudson.plugins.sauce_ondemand.credentials.SauceCredentials;

/*

Examples:

/run/secrets/credentials/sshUserPrivateKey
/run/secrets/credentials/sshUserPrivateKey/halkeye-bitbucket
/run/secrets/credentials/sshUserPrivateKey/halkeye-bitbucket/password
/run/secrets/credentials/sshUserPrivateKey/halkeye-bitbucket/secretKey
/run/secrets/credentials/sshUserPrivateKey/halkeye-bitbucket/username
/run/secrets/credentials/string
/run/secrets/credentials/string/hipchat-gavinmogan
/run/secrets/credentials/string/hipchat-gavinmogan/username
/run/secrets/credentials/usernamePassword
/run/secrets/credentials/usernamePassword/dockerhub-halkeye
/run/secrets/credentials/usernamePassword/dockerhub-halkeye/password
/run/secrets/credentials/usernamePassword/dockerhub-halkeye/username
/run/secrets/credentials/usernamePassword/github-halkeye
/run/secrets/credentials/usernamePassword/github-halkeye/password
/run/secrets/credentials/usernamePassword/github-halkeye/username
/run/secrets/credentials/usernamePassword/halkeye_quay
/run/secrets/credentials/usernamePassword/halkeye_quay/password
/run/secrets/credentials/usernamePassword/halkeye_quay/username

*/

def usernamePasswordDir = new File("/run/secrets/credentials/usernamePassword")
if (usernamePasswordDir.exists()) {
  println("Adding Username Credentials")
  usernamePasswordDir.eachFile(FileType.DIRECTORIES) { file -> 
    String id = FilenameUtils.getBaseName(file.name).toString();
    println("Handling: " + id)
    def c = new UsernamePasswordCredentialsImpl(
      CredentialsScope.GLOBAL, 
      id, 
      "description:"+id, 
      new File("/run/secrets/credentials/usernamePassword/" + id + "/username").text.trim(),
      new File("/run/secrets/credentials/usernamePassword/" + id + "/password").text.trim(),
    )
    SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
  }
}

def sauceDir = new File("/run/secrets/credentials/saucelabs")
if (sauceDir.exists()) {
  println("Adding Sauce Labs Credentials")
  sauceDir.eachFile(FileType.DIRECTORIES) { file -> 
    String id = FilenameUtils.getBaseName(file.name).toString();
    println("Handling: " + id)
    def c = new SauceCredentials(
      CredentialsScope.GLOBAL, 
      id, 
      new File("/run/secrets/credentials/saucelabs/" + id + "/username").text.trim(),
      new File("/run/secrets/credentials/saucelabs/" + id + "/password").text.trim(),
      "description:"+id
    )
    SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
  }
}
def stringDir = new File("/run/secrets/credentials/string")
if (stringDir.exists()) {
  println("Adding String Credentials")
  stringDir.eachFile(FileType.DIRECTORIES) { file -> 
    String id = FilenameUtils.getBaseName(file.name).toString();
    println("Handling: " + id)

    def c = new StringCredentialsImpl(
      CredentialsScope.GLOBAL, 
      id, 
      "description:"+id, 
      Secret.fromString(new File("/run/secrets/credentials/string/" + id + "/username").text.trim())
    )
    SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
  }
}

def sshUserPrivateKeyDir = new File("/run/secrets/credentials/sshUserPrivateKey")
if (sshUserPrivateKeyDir.exists()) {
  println("Adding sshUserPrivateKey Credentials")
  sshUserPrivateKeyDir.eachFile(FileType.DIRECTORIES) { file -> 
    String id = FilenameUtils.getBaseName(file.name).toString();
    println("Handling: " + id)
    def c = new BasicSSHUserPrivateKey(
      CredentialsScope.GLOBAL, 
      id, 
      new File("/run/secrets/credentials/sshUserPrivateKey/" + id + "/username").text.trim(),
      new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource("/run/secrets/credentials/sshUserPrivateKey/" + id + "/secretKey"),
      new File("/run/secrets/credentials/sshUserPrivateKey/" + id + "/password").text.trim(),
      "description:"+id, 
    )
    SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
  }
}

def dockerServerCredentialsDir = new File("/run/secrets/credentials/dockerServerCredentials")
if (dockerServerCredentialsDir.exists()) {
  println("Adding dockerServerCredentials Credentials")
  dockerServerCredentialsDir.eachFile(FileType.DIRECTORIES) { file -> 
    String id = FilenameUtils.getBaseName(file.name).toString();
    println("Handling: " + id)
    def c = new DockerServerCredentials (
      CredentialsScope.GLOBAL, 
      id,
      "description:"+id,
      new File("/run/secrets/credentials/dockerServerCredentials/" + id + "/key.pem").text.trim(),
      new File("/run/secrets/credentials/dockerServerCredentials/" + id + "/cert.pem").text.trim(),
      new File("/run/secrets/credentials/dockerServerCredentials/" + id + "/ca.pem").text.trim(),
    )
    SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
  }
}

def condiutCredentialsDir = new File("/run/secrets/credentials/condiutCredentials")
if (condiutCredentialsDir.exists()) {
  println("Adding condiutCredentials Credentials")
  condiutCredentialsDir.eachFile(FileType.DIRECTORIES) { file ->
    String id = FilenameUtils.getBaseName(file.name).toString();
    println("Handling: " + id)
    def c = new ConduitCredentialsImpl(
        id,
        new File("/run/secrets/credentials/condiutCredentials/" + id + "/username").text.trim() /* url */,
        '' /* gateway */,
        "description:"+id,
        new File("/run/secrets/credentials/condiutCredentials/" + id + "/password").text.trim(),
    )
    SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
  }
}
