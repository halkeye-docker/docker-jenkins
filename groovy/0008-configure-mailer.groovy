import jenkins.model.*

def inst = Jenkins.getInstance()

def desc = inst.getDescriptor("hudson.tasks.Mailer")

desc.setReplyToAddress("jenkins@gavinmogan.com")
desc.setSmtpHost("odin.kodekoan.com")
desc.setSmtpPort("25")

desc.save()
