import jenkins.model.Jenkins

jenkins.model.Jenkins.instance.getDescriptor("jenkins.CLI").get().setEnabled(false)
