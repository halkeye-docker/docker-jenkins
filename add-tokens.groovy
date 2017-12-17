import jenkins.model.*;
import groovy.io.FileType;
import org.apache.commons.io.FilenameUtils;
import com.cloudbees.hudson.plugins.folder.*;
import com.cloudbees.hudson.plugins.folder.properties.*;
import com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty;
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

/*

Example:
To create a username-password credential named github-halkeye you need:

/run/secrets/credentials/username/github-halkeye/username
/run/secrets/credentials/username/github-halkeye/password
*/

def dir = new File("/run/secrets/credentials/username")
if (dir.exists()) {
  println("Adding Username Credentials")
  dir.eachFile(FileType.DIRECTORIES) { file -> 
    String id = FilenameUtils.getBaseName(file.name).toString();
    println("Handling: " + id)
    def c = new UsernamePasswordCredentialsImpl(
      CredentialsScope.GLOBAL, 
      id, 
      "description:"+id, 
      new File("/run/secrets/credentials/username/" + id + "/username").text.trim(),
      new File("/run/secrets/credentials/username/" + id + "/password").text.trim(),
    )
    SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
  }
}