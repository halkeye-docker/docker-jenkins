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

Class getClassByName(String clazz) {
  try {
    return Class.forName(clazz);
  } catch (ClassNotFoundException e) {
    return null;
  }
}

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
      new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(new File("/run/secrets/credentials/sshUserPrivateKey/" + id + "/secretKey").text.trim()),
      new File("/run/secrets/credentials/sshUserPrivateKey/" + id + "/password").text.trim(),
      "description:"+id, 
    )
    SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
  }
}

def sauceDir = new File("/run/secrets/credentials/saucelabs")
if (sauceDir.exists()) {
  Class clazz = getClassByName("hudson.plugins.sauce_ondemand.credentials.SauceCredentials");
  if (clazz != null) {
    println("Adding Sauce Labs Credentials")
    sauceDir.eachFile(FileType.DIRECTORIES) { file -> 
      String id = FilenameUtils.getBaseName(file.name).toString();
      println("Handling: " + id)
      def c = clazz.newInstance(
        CredentialsScope.GLOBAL, 
        id, 
        new File("/run/secrets/credentials/saucelabs/" + id + "/username").text.trim(),
        new File("/run/secrets/credentials/saucelabs/" + id + "/password").text.trim(),
        "description:"+id
      )
      SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
    }
   } else {
    println("[ERROR] SauceCredentials not found, did you load the right plugin")
  }
}

def dockerServerCredentialsDir = new File("/run/secrets/credentials/dockerServerCredentials")
if (dockerServerCredentialsDir.exists()) {
  Class clazz = getClassByName("org.jenkinsci.plugins.docker.commons.credentials.DockerServerCredentials");
  if (clazz != null) {
    println("Adding dockerServerCredentials Credentials")
    dockerServerCredentialsDir.eachFile(FileType.DIRECTORIES) { file -> 
      String id = FilenameUtils.getBaseName(file.name).toString();
      println("Handling: " + id)
      def c = clazz.newInstance(
        CredentialsScope.GLOBAL, 
        id,
        "description:"+id,
        new File("/run/secrets/credentials/dockerServerCredentials/" + id + "/key.pem").text.trim(),
        new File("/run/secrets/credentials/dockerServerCredentials/" + id + "/cert.pem").text.trim(),
        new File("/run/secrets/credentials/dockerServerCredentials/" + id + "/ca.pem").text.trim(),
      )
      SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
    }
  } else {
    println("[ERROR] DockerServerCredentials not found, did you load the right plugin")
  }
}

def condiutCredentialsDir = new File("/run/secrets/credentials/condiutCredentials")
if (condiutCredentialsDir.exists()) {
  Class clazz = getClassByName("com.uber.jenkins.phabricator.credentials.ConduitCredentialsImpl");
  if (clazz != null) {
    println("Adding condiutCredentials Credentials")
    condiutCredentialsDir.eachFile(FileType.DIRECTORIES) { file ->
      String id = FilenameUtils.getBaseName(file.name).toString();
      println("Handling: " + id)
      def c = clazz.newInstance(
          id,
          new File("/run/secrets/credentials/condiutCredentials/" + id + "/username").text.trim() /* url */,
          '' /* gateway */,
          "description:"+id,
          new File("/run/secrets/credentials/condiutCredentials/" + id + "/password").text.trim(),
      )
      SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
    }
  } else {
    println("[ERROR] ConduitCredentialsImpl not found, did you load the right plugin")
  }
}

def AWSCredentialsDir = new File("/run/secrets/credentials/awsCredentials")
if (AWSCredentialsDir.exists()) {
  Class clazz = getClassByName("com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl");
  if (clazz != null) {  
    println("Adding AWSCredentials Credentials")
    AWSCredentialsDir.eachFile(FileType.DIRECTORIES) { file -> 
      String id = FilenameUtils.getBaseName(file.name).toString();
      println("Handling: " + id)
      def c = clazz.newInstance(
        CredentialsScope.GLOBAL, 
        id, 
        new File("/run/secrets/credentials/awsCredentials/" + id + "/accessKeyID").text.trim(),
        new File("/run/secrets/credentials/awsCredentials/" + id + "/secretAccessKey").text.trim(),
        "description:"+id,
        "",
        ""
      )
      SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
    }
  } else {
    println("[ERROR] AWSCredentialsClass not found, did you load the right plugin")
  }
}
