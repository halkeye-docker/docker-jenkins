import jenkins.model.*;
import groovy.io.FileType;
import hudson.util.Secret;
import org.apache.commons.io.FilenameUtils;
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;

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
/run/secrets/credentials/usernameColonPassword
/run/secrets/credentials/usernameColonPassword/dockerhub-halkeye
/run/secrets/credentials/usernameColonPassword/dockerhub-halkeye/password
/run/secrets/credentials/usernameColonPassword/dockerhub-halkeye/username
/run/secrets/credentials/usernameColonPassword/github-halkeye
/run/secrets/credentials/usernameColonPassword/github-halkeye/password
/run/secrets/credentials/usernameColonPassword/github-halkeye/username
/run/secrets/credentials/usernameColonPassword/halkeye_quay
/run/secrets/credentials/usernameColonPassword/halkeye_quay/password
/run/secrets/credentials/usernameColonPassword/halkeye_quay/username

*/

def usernamePasswordDir = new File("/run/secrets/credentials/usernameColonPassword")
if (usernamePasswordDir.exists()) {
  println("Adding Username Credentials")
  usernamePasswordDir.eachFile(FileType.DIRECTORIES) { file -> 
    String id = FilenameUtils.getBaseName(file.name).toString();
    println("Handling: " + id)
    def c = new UsernamePasswordCredentialsImpl(
      CredentialsScope.GLOBAL, 
      id, 
      "description:"+id, 
      new File("/run/secrets/credentials/usernameColonPassword/" + id + "/username").text.trim(),
      new File("/run/secrets/credentials/usernameColonPassword/" + id + "/password").text.trim(),
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
  println("Adding String Credentials")
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