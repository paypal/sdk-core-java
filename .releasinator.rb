#### releasinator config ####
configatron.product_name = "sdk core java"

# List of items to confirm from the person releasing.  Required, but empty list is ok.
configatron.prerelease_checklist_items = [
    "Sanity check the master branch."
]

def validate_version_match()
  if 'v'+parent_pom_version() != @current_release.version
    Printer.fail("parent pom version #{parent_pom_version} does not match changelog version #{@current_release.version}.")
    abort()
  end
  Printer.success("parent pom version version #{parent_pom_version} matches latest changelog version #{@current_release.version}.")
end

def check_tokens
  check_token("SONATYPE_USERNAME")
  check_token("SONATYPE_PASSWORD")
end

configatron.custom_validation_methods = [
    method(:validate_version_match),
    method(:check_tokens)
]

def build_method
  # to run Unit tests
  CommandProcessor.command("mvn clean install", live_output=true)
end

# The command that builds the sdk.  Required.
configatron.build_method = method(:build_method)

def publish_to_package_manager(version)
  CommandProcessor.command("mvn gpg:sign-and-deploy-file -Durl=https://#{ENV["SONATYPE_USERNAME"]}:#{ENV["SONATYPE_PASSWORD"]}@oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=#{pom_path}/com/paypal/sdk/paypal-core/#{parent_pom_version}/paypal-core-#{parent_pom_version}.pom -Dfile=target/paypal-core-#{parent_pom_version}.jar -Dgpg.passphrase=#{ENV["SONATYPE_PASSWORD"]}", live_output=true)
  CommandProcessor.command("mvn gpg:sign-and-deploy-file -Durl=https://#{ENV["SONATYPE_USERNAME"]}:#{ENV["SONATYPE_PASSWORD"]}@oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=#{pom_path}/com/paypal/sdk/paypal-core/#{parent_pom_version}/paypal-core-#{parent_pom_version}.pom -Dfile=target/paypal-core-#{parent_pom_version}-sources.jar -Dclassifier=sources -Dgpg.passphrase=#{ENV["SONATYPE_PASSWORD"]}", live_output=true)
  CommandProcessor.command("mvn gpg:sign-and-deploy-file -Durl=https://#{ENV["SONATYPE_USERNAME"]}:#{ENV["SONATYPE_PASSWORD"]}@oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=#{pom_path}/com/paypal/sdk/paypal-core/#{parent_pom_version}/paypal-core-#{parent_pom_version}.pom -Dfile=target/paypal-core-#{parent_pom_version}-javadoc.jar -Dclassifier=javadoc -Dgpg.passphrase=#{ENV["SONATYPE_PASSWORD"]}", live_output=true)
end

# The method that publishes the sdk to the package manager.  Required.
configatron.publish_to_package_manager_method = method(:publish_to_package_manager)


def wait_for_package_manager(version)
  CommandProcessor.wait_for("wget -U \"non-empty-user-agent\" -qO- http://central.maven.org/maven2/com/paypal/sdk/paypal-core/#{parent_pom_version}/paypal-core-#{parent_pom_version}.pom | cat")
end

# The method that waits for the package manager to be done.  Required
configatron.wait_for_package_manager_method = method(:wait_for_package_manager)

# Whether to publish the root repo to GitHub.  Required.
configatron.release_to_github = true

def parent_pom_version()
  File.open("pom.xml", 'r') do |f|
    f.each_line do |line|
      if line.match ("<artifactId>paypal-core<\/artifactId>")
        line = f.gets
        if line.match (/<version>\d+\.\d+\.\d+<\/version>/)
          return line.strip.split('>')[1].strip.split('<')[0]
        end
      end
    end
  end
end

def check_token(token_param)
  if !ENV[token_param]
    Printer.fail("#{token_param} environment variable required.  Please add this value to your environment file.")
    abort()
  end
  Printer.success("#{token_param} environment variable found.")
end

def pom_path()
  File.open(ENV['HOME']+'/.m2/settings.xml', 'r') do |f|
    f.each_line do |line|
      if line.match (/<\/localRepository>/)
        return line.strip.split('>')[1].strip.split('<')[0]
      end
    end
  end
end
